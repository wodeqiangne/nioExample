package com.leixie.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;

/**
 * @author xielei
 */
public class MyByteToMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int readableBytes = in.readableBytes();
        if (readableBytes >= 10) {
            byte[] bytes = new byte[readableBytes];
//            byte[] bytesUseArrayCopy = new byte[readableBytes];
//            if(in.hasArray()) {
//                int offset = in.arrayOffset() + in.readerIndex();
//                System.arraycopy(in.array(), offset, bytesUseArrayCopy, 0, readableBytes);
//                System.out.println("print with arrayCopy:" + new String(bytesUseArrayCopy));
//            }
            System.out.println("begin sleep " + Thread.currentThread());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("end sleep " + Thread.currentThread());
            in.readBytes(bytes, in.readerIndex(), readableBytes);
            Object object = SerializationUtils.deserialize(bytes);
            System.out.println(object.toString());
            out.add(object);
        }
    }
}
