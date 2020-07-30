package org.example.common.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.example.common.protocol.HansMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;


/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 21:12
 */
public class HansMessageEncoder extends MessageToByteEncoder<HansMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HansMessage msg, ByteBuf out) throws Exception {
        int type = msg.getType().getCode();
        String meta = JSON.toJSONString(msg.getMeta());
        byte[] metaBytes = meta.getBytes(CharsetUtil.UTF_8);
        byte[] data = msg.getData();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (DataOutputStream outputStream = new DataOutputStream(bytes)) {
            outputStream.writeInt(type);
            outputStream.writeInt(metaBytes.length);
            outputStream.write(metaBytes);

            if (data != null && data.length > 0) {
                outputStream.write(data);
            }
            byte[] transfer = bytes.toByteArray();
            out.writeInt(transfer.length);
            out.writeBytes(transfer);
        }
    }
}
