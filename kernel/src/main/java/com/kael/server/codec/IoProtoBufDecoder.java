package com.kael.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import com.kael.kernel.IMessage;

public class IoProtoBufDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		IMessage.Builder builder = IMessage.create();
		builder.withCode(msg.readShort());
		if(!msg.hasArray()){
			byte[] datas = new byte[msg.readableBytes()];
			msg.getBytes(msg.readerIndex(),datas, 0, datas.length);
			builder.withBody(datas);
		}else{
			byte[] datas = msg.array();
			builder.withBody(datas);
		}
		out.add(builder.build());
	}

}
