package com.kael.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.kael.kernel.IMessage;

public class IoProtoBufEncoder extends MessageToMessageEncoder<IMessage.Builder> {

	@Override
	protected void encode(ChannelHandlerContext ctx, IMessage.Builder msg,
			List<Object> out) throws Exception {
         IMessage tmpMsg = msg.build();
		byte[] dst = tmpMsg.getBody();
		ByteBuf wrappedBuffer = Unpooled.buffer();
		wrappedBuffer.writeInt(dst.length + 2);
		wrappedBuffer.writeShort(tmpMsg.getCode());
		wrappedBuffer.writeBytes(dst);
		out.add(wrappedBuffer);		
	}

	
}
