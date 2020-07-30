package org.example.common.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.example.common.exception.HansTypeException;
import org.example.common.protocol.HansMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 19:47
 */
public abstract class AbstractHansInboundHandler extends AbstractCoreInboundHandler implements HansProcess {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HansMessage) {
            HansMessage message = (HansMessage) msg;
            switch (message.getType()) {
                case REGISTER:
                    doRegister(ctx, message);
                    break;
                case REGISTER_RESP:
                    doRegisterResp(ctx, message);
                    break;
                case READ:
                    doForward(ctx, message);
                    break;
                case HEART:
                    doHeart(ctx, message);
                    break;
                case ACTIVE:
                    doConnect(ctx, message);
                    break;
                case INACTIVE:
                    doDisConnect(ctx, message);
                    break;
                default:
                    throw new HansTypeException("Unknown type: " + message.getType().name());
            }
        }
    }

    @Override
    public void doRegister(ChannelHandlerContext ctx, HansMessage msg) {

    }

    @Override
    public void doConnect(ChannelHandlerContext ctx, HansMessage msg) {

    }

    @Override
    public void doRegisterResp(ChannelHandlerContext ctx, HansMessage msg) {

    }

    @Override
    public void doDisConnect(ChannelHandlerContext ctx, HansMessage msg) {

    }

    @Override
    public void doForward(ChannelHandlerContext ctx, HansMessage msg) {

    }

    @Override
    public void doHeart(ChannelHandlerContext ctx, HansMessage msg) {

    }
}
