package org.example.common.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.example.common.protocol.HansMessage;
import org.example.common.protocol.HansType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 16:57
 */
public class HansMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        HansType hansType = HansType.valueOf(msg.readInt());
        int metaLen = msg.readInt();
        byte[] metaBytes = ByteBufUtil.getBytes(msg, msg.readerIndex(), metaLen);
        Map<String,Object> meta = JSON.parseObject(metaBytes,HashMap.class);
        msg.skipBytes(metaLen);
        byte[] data = null;
        if (msg.isReadable()) {
            data = ByteBufUtil.getBytes(msg);
        }
        HansMessage message = new HansMessage();
        message.setType(hansType);
        message.setData(data);
        message.setMeta(meta);
        out.add(message);
    }
}
