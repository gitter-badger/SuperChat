package xyz.nickr.superchat.cmd.shows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import xyz.nickr.superchat.Joiner;
import xyz.nickr.superchat.SuperChatShows;
import xyz.nickr.superchat.SuperChatShows.Show;
import xyz.nickr.superchat.cmd.Command;
import xyz.nickr.superchat.sys.Group;
import xyz.nickr.superchat.sys.Message;
import xyz.nickr.superchat.sys.MessageBuilder;
import xyz.nickr.superchat.sys.Sys;
import xyz.nickr.superchat.sys.User;

public class TimetableCommand implements Command {

    @Override
    public String[] names() {
        return new String[] { "timetable", "days" };
    }

    @Override
    public String[] help(User user, boolean userChat) {
        return new String[] { "", "see what days each show is on" };
    }

    void append(String day, Set<Show> set, MessageBuilder<?> builder) {
        if (builder.length() > 0)
            builder.newLine();
        List<String> names = set.stream().map(s -> s.display).collect(Collectors.toList());
        names.sort(String.CASE_INSENSITIVE_ORDER);
        builder.bold(true).text(day + ": ").bold(false).text(Joiner.join(", ", names));
    }

    @Override
    public void exec(Sys sys, User user, Group conv, String used, String[] args, Message message) {
        Map<String, Set<Show>> days = new HashMap<>();
        SuperChatShows.TRACKED_SHOWS.forEach(s -> {
            Set<Show> set = days.get(s.day == null || s.day.isEmpty() || s.day.equals("N/A") ? "not airing" : s.day.toLowerCase());
            if (set == null)
                set = new HashSet<>();
            set.add(s);
            days.put(s.day == null || s.day.isEmpty() || s.day.equals("N/A") ? "not airing" : s.day.toLowerCase(), set);
        });
        MessageBuilder<?> builder = sys.message();
        // days.forEach((day, set) -> {
        for (String day : Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Netflix", "Not airing")) {
            Set<Show> set = days.get(day.toLowerCase());
            if (set != null)
                append(day, set, builder);
        }
        if (builder.length() > 0) {
            conv.sendMessage(builder.build());
        } else {
            conv.sendMessage(builder.text("Something went wrong!"));
        }
    }

}
