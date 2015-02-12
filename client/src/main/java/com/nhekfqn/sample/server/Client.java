package com.nhekfqn.sample.server;

import com.nhekfqn.sample.server.proto.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class Client {

    private final String host;
    private final int port;

    private EventLoopGroup workerGroup;

    private Channel channel;
    private ClientHandler clientHandler;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception {
        workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap =
                new Bootstrap()
                        .group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();

                                pipeline.addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
                                pipeline.addLast(new ProtobufDecoder(Protocol.BaseMessage.getDefaultInstance()));

                                clientHandler = new ClientHandler();
                                pipeline.addLast(clientHandler);

                                pipeline.addLast(new LengthFieldPrepender(4));
                                pipeline.addLast(new ProtobufEncoder());
                            }
                        });

        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        channel = channelFuture.channel();
    }

    public void close() {
        workerGroup.shutdownGracefully();
    }

    public int ping(String userId) throws InterruptedException {
        Protocol.Ping ping = Protocol.Ping.newBuilder()
                .setUserId(userId)
                .build();

        Protocol.BaseMessage baseMessage = Protocol.BaseMessage.newBuilder()
                .setMessageType(ping.getMessageType())
                .setPing(ping)
                .build();

        channel.writeAndFlush(baseMessage);

        return clientHandler.getPong();
    }

}
