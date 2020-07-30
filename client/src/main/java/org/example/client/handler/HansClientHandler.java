package org.example.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.example.common.core.AbstractHansInboundHandler;
import org.example.common.core.TcpClient;
import org.example.common.exception.HansException;
import org.example.common.protocol.HansMessage;
import org.example.common.protocol.HansType;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 11:20
 */
public class HansClientHandler extends AbstractHansInboundHandler {

    private Map<String, Object> args;
    protected static ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>();

    public HansClientHandler(Map<String, Object> args) {
        this.args = args;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HansMessage message = new HansMessage();
        HashMap<String, Object> objectObjectHashMap = new HashMap<>(2);
        objectObjectHashMap.put("remotePort", args.get("remotePort"));
        objectObjectHashMap.put("password", args.get("password"));
        message.setType(HansType.REGISTER);
        message.setMeta(objectObjectHashMap);
        ctx.writeAndFlush(message);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("代理主通道 {} 关闭.", ctx.channel().remoteAddress());
        Enumeration<Channel> elements = map.elements();
        while (elements.hasMoreElements()) {
            elements.nextElement().close();
        }

        map.clear();
    }

    @Override
    public void doRegisterResp(ChannelHandlerContext ctx,HansMessage msg) {
        String message = (String) msg.getMeta().get("message");
        if (msg.getMeta().get("status").equals(0)) {
            logger.error(message);
            ctx.close();
        } else {
            logger.info(message);
        }
    }

    @Override
    public void doConnect(ChannelHandlerContext ctx, HansMessage msg) {
        Map<String, Object> meta = msg.getMeta();
        String id = (String) meta.get("id");
        try {
            TcpClient tcpClient = new TcpClient();
            tcpClient.connect((String) args.get("proxyIp"), (Integer) args.get("proxyPort"), new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ProxyLocalHandler proxyLocalHandler = new ProxyLocalHandler(ctx, meta);
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ByteArrayEncoder());
                    pipeline.addLast(new ByteArrayDecoder());
                    pipeline.addLast(proxyLocalHandler);
                    map.put(id, pipeline.lastContext().channel());
                }
            });
            logger.info("has a remote connection access");
        } catch (HansException e) {
            // todo 异常没有做分类
            logger.error("has a remote connection access,but local port: {} didn't respond", 8080);
        }
    }

    @Override
    public void doDisConnect(ChannelHandlerContext ctx, HansMessage msg) {
        Map<String, Object> meta = msg.getMeta();
        String id = (String) meta.get("id");
        Channel channel = map.get(id);
        if (channel != null) {
            channel.close();
            map.remove(id);
        }
    }

    @Override
    public void doForward(ChannelHandlerContext ctx, HansMessage msg) {
        Map<String, Object> meta = msg.getMeta();
        String id = (String) meta.get("id");
        Channel channel = map.get(id);
        if (channel != null) {
            channel.writeAndFlush(msg.getData());
        }

    }
}
