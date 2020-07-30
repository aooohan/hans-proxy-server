package org.example.common.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.example.common.exception.HansException;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 9:18
 */
public class TcpServer {

    ReentrantLock lock = new ReentrantLock();

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(TcpServer.class);

    public void bind(int port, ChannelInitializer<SocketChannel> channelInitializer) throws HansException {

        lock.lock();
        EventLoopGroup work = new NioEventLoopGroup(1);
        EventLoopGroup boss = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(work, boss)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(channelInitializer);

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().addListener(future -> {
                work.shutdownGracefully();
                boss.shutdownGracefully();
            });
        } catch (Exception e) {
            work.shutdownGracefully();
            boss.shutdownGracefully();
            logger.error("remote tcp server start fail: {}", e.getMessage());
            throw new HansException("remote tcp server start fail.");
        }
    }
}
