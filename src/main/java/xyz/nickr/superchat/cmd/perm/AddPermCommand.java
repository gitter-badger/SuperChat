package xyz.nickr.superchat.cmd.perm;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import xyz.nickr.superchat.SuperChatPermissions;
import xyz.nickr.superchat.cmd.Command;
import xyz.nickr.superchat.cmd.Permission;

public class AddPermCommand implements Command {

    @Override
    public String[] names() {
        return new String[]{ "addperm" };
    }

    @Override
    public String[] help(SkypeUser user, boolean userChat) {
        return new String[]{ "[username] [perm]", "gives [perm] to [username]" };
    }

    @Override
    public Permission perm() {
        return string("permissions.modify");
    }

    @Override
    public boolean userchat() {
        return true;
    }

    @Override
    public void exec(SkypeUser user, SkypeConversation group, String used, String[] args, SkypeMessage message) {
        if (args.length < 2) {
            sendUsage(user, group);
        } else {
            if (SuperChatPermissions.set(args[0], args[1], true)) {
                group.sendMessage(bold(encode(args[0])) + encode(" now has: ") + bold(encode(args[1])));
            } else {
                group.sendMessage(bold(encode(args[0])) + encode(" already has: ") + bold(encode(args[1])));
            }
        }
    }

}