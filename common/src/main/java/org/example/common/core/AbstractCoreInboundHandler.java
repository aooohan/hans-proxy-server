package org.example.common.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.example.common.protocol.HansMessage;
import org.example.common.protocol.HansType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 12:40
 */
public abstract class AbstractCoreInboundHandler extends ChannelInboundHandlerAdapter  {


    protected InternalLogger logger = InternalLoggerFactory.getInstance(this.getClass());


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("error:{}", cause.getMessage());
        ctx.channel();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.warn("read idle loss connection");
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                HansMessage message = new HansMessage();
                message.setType(HansType.HEART);
                ctx.writeAndFlush(message);
            }
        }
    }


}
