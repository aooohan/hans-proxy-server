package org.example.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.example.common.core.AbstractCoreInboundHandler;
import org.example.common.protocol.HansMessage;
import org.example.common.protocol.HansType;

import java.util.Map;

/**
 * Client与本地服务的交互，handler
 * @author ：lihan
 * @description： Client <-> Local
 * @date ：2020/7/29 14:25
 */
public class ProxyLocalHandler extends AbstractCoreInboundHandler {
    private ChannelHandlerContext proxyContext;
    private Map<String, Object> routeMap;

    public ProxyLocalHandler(ChannelHandlerContext proxyContext, Map<String, Object>  map) {
        this.proxyContext = proxyContext;
        this.routeMap = map;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HansMessage message = new HansMessage();
        message.setType(HansType.READ);
        message.setData((byte[]) msg);
        message.setMeta(routeMap);
        proxyContext.writeAndFlush(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        HansMessage message = new HansMessage();
        message.setType(HansType.INACTIVE);
        message.setMeta(routeMap);
        proxyContext.writeAndFlush(message);
    }
}
