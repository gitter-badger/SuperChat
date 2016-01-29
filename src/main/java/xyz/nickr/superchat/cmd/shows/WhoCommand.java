package xyz.nickr.superchat.cmd.shows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xyz.nickr.superchat.SuperChatController;
import xyz.nickr.superchat.SuperChatShows.Show;
import xyz.nickr.superchat.cmd.Command;
import xyz.nickr.superchat.sys.Group;
import xyz.nickr.superchat.sys.Message;
import xyz.nickr.superchat.sys.MessageBuilder;
import xyz.nickr.superchat.sys.Sys;
import xyz.nickr.superchat.sys.User;

public class WhoCommand implements Command {

    @Override
    public String[] names() {
        return new String[] { "who", "whois" };
    }

    @Override
    public String[] help(User user, boolean userChat) {
        return new String[] { "(username)", "gets (username)'s progress on all shows" };
    }

    String pad(String s, int len) {
        StringBuilder builder = new StringBuilder(s);
        while (builder.length() < len)
            builder.insert(builder.indexOf("("), ' ');
        return builder.toString();
    }

    @Override
    public void exec(Sys sys, User user, Group conv, String used, String[] args, Message message) {
        String username = args.length > 0 ? args[0].toLowerCase() : user.getUsername();
        List<String> shows = new ArrayList<>();
        Map<Show, String> progress = SuperChatController.getUserProgress(username);
        progress.forEach((show, ep) -> {
            if (show != null)
                shows.add(show.getDisplay() + "    (" + ep + ")");
        });
        int rows = shows.size() / 2 + shows.size() % 2;
        shows.sort(String.CASE_INSENSITIVE_ORDER);
        int maxLen1 = shows.subList(0, rows).stream().max((s1, s2) -> s1.length() - s2.length()).orElse("").length();
        int maxLen2 = shows.subList(rows, shows.size()).stream().max((s1, s2) -> s1.length() - s2.length()).orElse("").length();
        String s = "";
        for (int i = 0; i < rows; i++) {
            if (shows.size() > i) {
                String t = pad(shows.get(i), maxLen1);
                if (shows.size() > rows + i)
                    t += "    |    " + pad(shows.get(rows + i), maxLen2);
                s += sys.message().text(t).build();
                if (i != rows - 1)
                    s += "\n   ";
            }
        }
        MessageBuilder<?> mb = sys.message();
        if (shows.size() > 0)
            conv.sendMessage(mb.bold(true).text("Shows " + username + " is watching:").bold(false).html("\n").code(true).text("   " + s));
        else
            conv.sendMessage(mb.bold(true).text("Error: ").bold(false).text("It doesn't look like " + username + " uses me. :("));
    }

}
