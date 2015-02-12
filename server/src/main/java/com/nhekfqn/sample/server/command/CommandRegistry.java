package com.nhekfqn.sample.server.command;

import com.google.inject.Inject;

import com.nhekfqn.sample.server.proto.Protocol;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandRegistry {

    private final Map<Protocol.MessageType, BaseCommand> commandsByMessageType = new HashMap<>();

    @Inject
    public CommandRegistry(Set<BaseCommand> commands) {
        for (BaseCommand command : commands) {
            commandsByMessageType.put(command.getMessageType(), command);
        }
    }

    public BaseCommand getCommandByMessageType(Protocol.MessageType messageType) {
        return commandsByMessageType.get(messageType);
    }

}
