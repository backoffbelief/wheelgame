package com.kael.gun.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class IoProtoBufEncoder extends MessageToMessageEncoder<IMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, IMessage msg,
			List<Object> out) throws Exception {
         byte[] dst = msg.getBody();
		ByteBuf wrappedBuffer = Unpooled.buffer();
		wrappedBuffer.writeInt(dst.length + 2);
		wrappedBuffer.writeShort(msg.getCode());
		wrappedBuffer.writeBytes(dst);
		out.add(wrappedBuffer);		
	}

	
}
