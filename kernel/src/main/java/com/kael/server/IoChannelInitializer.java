package com.kael.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import com.kael.server.codec.IoProtoBufDecoder;
import com.kael.server.codec.IoProtoBufEncoder;

public class IoChannelInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline cp = ch.pipeline();
		cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
		cp.addLast(new IoProtoBufDecoder());
		cp.addLast(new IoProtoBufEncoder());
		cp.addLast(new IoHandler());
	}

}
