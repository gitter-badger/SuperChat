package xyz.nickr.superchat.cmd;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import xyz.nickr.superchat.SuperChatController;

public class ReloadCommand implements Command {

    @Override
    public String[] names() {
        return new String[] { "reload" };
    }

    @Override
    public String[] help(SkypeUser user, boolean userChat) {
        return new String[] { "", "reloads the bot" };
    }

    @Override
    public Permission perm() {
        return string("admin.reload");
    }

    @Override
    public boolean userchat() {
        return true;
    }

    @Override
    public boolean alwaysEnabled() {
        return true;
    }

    @Override
    public void exec(SkypeUser user, SkypeConversation group, String used, String[] args, SkypeMessage message) {
        SkypeMessage msg = group.sendMessage(encode(" "));
        SuperChatController.saveProgress();
        SuperChatController.savePermissions();
        new Thread(() -> {
            SuperChatController.load(s -> {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msg.edit("Reloading... " + s);
            });
        }, "Reload Thread").start();
    }

}