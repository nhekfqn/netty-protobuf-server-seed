package com.nhekfqn.sample.server.server;

import com.nhekfqn.sample.server.command.BaseCommand;
import com.nhekfqn.sample.server.command.CommandRegistry;
import com.nhekfqn.sample.server.proto.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private final CommandRegistry commandRegistry;
    private final ExecutorService executorService;

    public ServerHandler(CommandRegistry commandRegistry, ExecutorService executorService) {
        this.commandRegistry = commandRegistry;
        this.executorService = executorService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel active.");
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        final Protocol.BaseMessage baseMessage = (Protocol.BaseMessage) msg;

        Protocol.MessageType messageType = baseMessage.getMessageType();

        final BaseCommand command = commandRegistry.getCommandByMessageType(messageType);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                command.execute(ctx, baseMessage);
            }
        });
    }

}
