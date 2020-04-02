package com.leixie.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.SerializationUtils;

/**
 * @author xielei
 */
public class MyMessageToByteEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        System.out.println("client " + msg.toString());
        byte[] bytes = SerializationUtils.serialize(msg);
        out.writeBytes(bytes);

    }
}
