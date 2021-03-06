package xyz.nickr.superbot.sys.gitter;

import java.util.HashMap;
import java.util.Map;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;
import xyz.nickr.superbot.SuperBotCommands;
import xyz.nickr.superbot.sys.GroupConfiguration;
import xyz.nickr.superbot.sys.MessageBuilder;
import xyz.nickr.superbot.sys.Sys;

public class GitterSys implements Sys {

    private final Map<String, GroupConfiguration> configs = new HashMap<>();

    final Jitter jitter;

    public GitterSys(String token) {
        if (token == null) {
            System.err.println("Gitter Token Missing!");
            this.jitter = null;
            return;
        }
        this.jitter = Jitter.builder().token(token).build();

        this.jitter.onMessage(m -> SuperBotCommands.exec(this, wrap(m.getRoom()), wrap(m.getSender()), wrap(m)));

        this.jitter.getCurrentRooms().forEach(room -> {
            room.beginMessageStreaming();
        });
    }

    @Override
    public String getName() {
        return "Gitter";
    }

    @Override
    public String prefix() {
        return "+";
    }

    @Override
    public boolean isUIDCaseSensitive() {
        return false;
    }

    @Override
    public boolean columns() {
        return true;
    }

    @Override
    public MessageBuilder<?> message() {
        return new MarkdownMessageBuilder();
    }

    @Override
    public String getUserFriendlyName(String uniqueId) {
        return uniqueId;
    }

    @Override
    public GroupConfiguration getGroupConfiguration(String uniqueId) {
        return configs.get(uniqueId);
    }

    @Override
    public void addGroupConfiguration(GroupConfiguration cfg) {
        configs.put(cfg.getUniqueId(), cfg);
    }

    GitterMessage wrap(Message message) {
        return new GitterMessage(this, message);
    }

    GitterUser wrap(User user) {
        return new GitterUser(this, user);
    }

    GitterGroup wrap(Room room) {
        return new GitterGroup(this, room);
    }

}
