package org.example.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.example.common.core.AbstractCoreInboundHandler;
import org.example.common.protocol.HansMessage;
import org.example.common.protocol.HansType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：lihan
 * @description： Server <-> Remote
 * @date ：2020/7/29 14:24
 */
public class ProxyRemoteHandler extends AbstractCoreInboundHandler {
    private ChannelHandlerContext forwardLocalContext;

    public ProxyRemoteHandler(ChannelHandlerContext forwardLocalContext) {
        this.forwardLocalContext = forwardLocalContext;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HansMessage message = new HansMessage();
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", ctx.channel().id().asLongText());
        message.setMeta(map);
        message.setType(HansType.ACTIVE);
        forwardLocalContext.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HansMessage message = new HansMessage();
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", ctx.channel().id().asLongText());
        message.setMeta(map);
        message.setData((byte[]) msg);
        message.setType(HansType.READ);
        forwardLocalContext.writeAndFlush(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        HansMessage message = new HansMessage();
        message.setType(HansType.INACTIVE);
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", ctx.channel().id().asLongText());
        message.setMeta(map);
        forwardLocalContext.writeAndFlush(message);
    }
}
