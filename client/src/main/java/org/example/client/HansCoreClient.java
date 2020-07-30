package org.example.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.client.handler.HansClientHandler;
import org.example.common.codec.HansMessageDecoder;
import org.example.common.codec.HansMessageEncoder;
import org.example.common.core.TcpClient;
import org.example.common.exception.HansException;

import java.util.Map;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 9:11
 */
public class HansCoreClient {
    private Map<String, Object> args;

    public HansCoreClient(Map<String, Object> args) {
        this.args = args;
    }

    public void start() {
        TcpClient tcpClient = new TcpClient();
        try {
            tcpClient.connect((String) args.get("serverIp"), (Integer) args.get("serverPort"), new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new IdleStateHandler(60, 30, 0));
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    pipeline.addLast(new HansMessageDecoder());
                    pipeline.addLast(new HansMessageEncoder());
                    pipeline.addLast(new HansClientHandler(args));
                }
            });
        } catch (HansException e) {
            e.printStackTrace();
        }
    }

}
