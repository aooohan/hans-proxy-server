package org.example.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.example.common.core.AbstractHansInboundHandler;
import org.example.common.core.TcpServer;
import org.example.common.exception.HansException;
import org.example.common.protocol.HansMessage;
import org.example.common.protocol.HansType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 13:31
 */
public class HansServerHandler extends AbstractHansInboundHandler {
    protected static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final TcpServer server = new TcpServer();
    private String password;

    public HansServerHandler(String password) {
        this.password = password;
    }

    @Override
    public void doRegister(final ChannelHandlerContext ctx, HansMessage msg) {
        Map<String, Object> meta = new HashMap<String, Object>(2);
        final Map<String, Object> originalMeta = msg.getMeta();
        final Integer remotePort = (Integer) originalMeta.get("remotePort");
        final String clientPassword = (String) originalMeta.get("password");
        HansMessage response = new HansMessage();
        if (!password.equals(clientPassword)) {
            meta.put("status", 0);
            meta.put("message","incorrect password.");
            response.setType(HansType.REGISTER_RESP);
            response.setMeta(meta);
            ctx.writeAndFlush(response);
            return;
        }
        try {
            server.bind(remotePort, new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ProxyRemoteHandler proxyRemoteHandler = new ProxyRemoteHandler(ctx);
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ByteArrayDecoder());
                    pipeline.addLast(new ByteArrayEncoder());
                    pipeline.addLast(proxyRemoteHandler);
                    group.add(ch);
                }
            });
            meta.put("status", 1);
            meta.put("message","remote register success.");
            logger.info("Register client successfully,port: {}", remotePort);

        } catch (HansException e) {
            meta.put("status", 0);
            meta.put("message",e.getMessage());
        }
        response.setMeta(meta);
        response.setType(HansType.REGISTER_RESP);
        ctx.writeAndFlush(response);
    }

    @Override
    public void doDisConnect(ChannelHandlerContext ctx, HansMessage msg) {
        Map<String, Object> meta = msg.getMeta();
        String id = (String) meta.get("id");
        group.close(channel -> channel.id().asLongText().equals(id));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("代理主通道 {} 关闭.", ctx.channel().remoteAddress());
        group.close();
    }

    @Override
    public void doForward(ChannelHandlerContext ctx, HansMessage msg) {
        Map<String, Object> meta = msg.getMeta();
        String id = (String) meta.get("id");
        group.writeAndFlush(msg.getData(), channel -> channel.id().asLongText().equals(id));
    }
}
