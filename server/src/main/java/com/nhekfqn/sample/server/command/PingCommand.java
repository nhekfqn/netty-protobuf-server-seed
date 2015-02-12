package com.nhekfqn.sample.server.command;

import com.google.inject.Inject;
import com.nhekfqn.sample.server.persistense.PingDao;
import com.nhekfqn.sample.server.proto.Protocol;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class PingCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(PingCommand.class);

    private final PingDao pingDao;

    @Inject
    public PingCommand(PingDao pingDao) {
        super(Protocol.MessageType.ping);

        this.pingDao = pingDao;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, Protocol.BaseMessage baseMessage) {
        logger.info("Ping Command called!");

        assert baseMessage.hasPing();

        String userId = baseMessage.getPing().getUserId();

        logger.info("User Id: {}", userId);

        if (StringUtils.isEmpty(userId)) {
            return;
        }

        try {
            int n = pingDao.incrementPingN(userId);

            Protocol.Pong pong = Protocol.Pong.newBuilder()
                    .setN(n)
                    .build();

            Protocol.BaseMessage baseMessageResponse = Protocol.BaseMessage.newBuilder()
                    .setMessageType(pong.getMessageType())
                    .setPong(pong)
                    .build();

            ctx.channel().writeAndFlush(baseMessageResponse);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
