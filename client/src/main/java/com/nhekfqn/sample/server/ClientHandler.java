package com.nhekfqn.sample.server;

import com.nhekfqn.sample.server.proto.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private final BlockingQueue<Integer> pongQueue = new ArrayBlockingQueue<Integer>(1000);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Protocol.BaseMessage baseMessage = (Protocol.BaseMessage) msg;

        if (baseMessage.hasPong()) {
            Protocol.Pong pong = baseMessage.getPong();

            logger.info("Pong N: {}", pong.getN());

            pongQueue.put(pong.getN());
        }
    }

    public int getPong() throws InterruptedException {
        return pongQueue.take();
    }

}
