package me.nickrobson.skype.superchat.cmd;

import java.util.Arrays;
import java.util.Optional;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.nickrobson.skype.superchat.SuperChatShows;
import me.nickrobson.skype.superchat.SuperChatShows.Show;

/**
 * Created by Horrgs on 1/1/2016.
 *
 * @author Horrgs
 * @author Nick Robson
 */
public class AddShowCommand implements Command {

    @Override
    public String[] names() {
        return new String[] { "addshow" };
    }

    @Override
    public SkypeUserRole role() {
        return SkypeUserRole.ADMIN;
    }

    @Override
    public String[] help(SkypeUser user, boolean userChat) {
        return new String[] { "[display] [day] [aliases...]", "add a new show to the list" };
    }

    @Override
    public void exec(SkypeUser user, SkypeConversation group, String used, String[] args, SkypeMessage message) {
        if (args.length < 3) {
            group.sendMessage(bold(encode("Usage: ")) + encode(PREFIX + "addshow [display] [day] [aliases...]"));
        } else {
            String showName = args[0], day = args[1];
            String[] aliases = new String[args.length-2];
            for (int i = 2; i < args.length; i++)
                aliases[i-2] = args[i].toLowerCase();
            Optional<Show> opt = Arrays.stream(aliases)
                                    .map(s -> SuperChatShows.getShow(s))
                                    .filter(s -> s != null)
                                    .findAny();
            SuperChatShows.Show show = opt.orElse(null);
            if (show == null) {
                show = new SuperChatShows.Show(showName, day, aliases);
                if (SuperChatShows.addShow(show)) {
                    group.sendMessage(encode("Added a new Show! Let's review the info:" +
                        "\nDisplay Name: " + show.display +
                        "\nDay of the Week: " + show.day +
                        "\nAliases: " + Arrays.asList(show.names)));
                } else {
                    group.sendMessage(encode("Something went wrong."));
                }
            } else {
                group.sendMessage(encode("A show already exists with the name \"" + showName + "\""));
            }
        }
    }

}