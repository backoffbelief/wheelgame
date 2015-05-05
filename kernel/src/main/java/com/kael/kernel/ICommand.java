package com.kael.kernel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public interface ICommand {

	void execute(ChannelHandlerContext ctx,IMessage message);
	
	AttributeKey<AppPlayer> key = AttributeKey.valueOf("player");
}
