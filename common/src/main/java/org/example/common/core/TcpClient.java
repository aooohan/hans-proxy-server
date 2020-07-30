package org.example.common.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.example.common.exception.HansException;


/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 10:04
 */
public class TcpClient {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(TcpServer.class);

    public void connect(String host, int port, ChannelInitializer<SocketChannel> channelChannelInitializer) throws HansException {
        EventLoopGroup work = new NioEventLoopGroup(1);

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(work)
                    .channel(NioSocketChannel.class)
                    .handler(channelChannelInitializer);
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().addListener(future -> work.shutdownGracefully());
        } catch (Exception e) {
            work.shutdownGracefully();
            throw new HansException("tcp connect fail.");
        }
    }
}
