package org.example.server;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.common.codec.HansMessageDecoder;
import org.example.common.codec.HansMessageEncoder;
import org.example.common.core.TcpServer;
import org.example.common.exception.HansException;
import org.example.server.handler.HansServerHandler;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 9:11
 */
public class HansCoreServer {
    public void start(int port,String password) {
        TcpServer tcpServer = new TcpServer();
        try {
            tcpServer.bind(port, new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new IdleStateHandler(60, 30, 0));
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    pipeline.addLast(new HansMessageDecoder());
                    pipeline.addLast(new HansMessageEncoder());
                    pipeline.addLast(new HansServerHandler(password));
                }
            });
        } catch (HansException e) {
            e.printStackTrace();
        }
    }

}
