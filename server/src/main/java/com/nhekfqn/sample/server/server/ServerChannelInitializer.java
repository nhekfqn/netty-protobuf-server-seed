package com.nhekfqn.sample.server.server;

import com.google.inject.Inject;
import com.nhekfqn.sample.server.command.CommandRegistry;
import com.nhekfqn.sample.server.proto.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerChannelInitializer extends ChannelInitializer {

    private final CommandRegistry commandRegistry;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Inject
    public ServerChannelInitializer(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
        pipeline.addLast(new ProtobufDecoder(Protocol.BaseMessage.getDefaultInstance()));

        pipeline.addLast(new ServerHandler(commandRegistry, executorService));

        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new ProtobufEncoder());
    }

}
