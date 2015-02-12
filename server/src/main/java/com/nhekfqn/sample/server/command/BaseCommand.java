package com.nhekfqn.sample.server.command;

import com.nhekfqn.sample.server.proto.Protocol;
import io.netty.channel.ChannelHandlerContext;

public abstract class BaseCommand {

    private final Protocol.MessageType messageType;

    protected BaseCommand(Protocol.MessageType messageType) {
        this.messageType = messageType;
    }

    public abstract void execute(ChannelHandlerContext ctx, Protocol.BaseMessage baseMessage);

    public Protocol.MessageType getMessageType() {
        return messageType;
    }

}
