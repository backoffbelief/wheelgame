package com.kael.kernel;

import io.netty.channel.ChannelHandlerContext;

public abstract class QueueCommand extends AbsCommand{

	public void execute(ChannelHandlerContext ctx, IMessage message) {
		AppPlayer appPlayer = ctx.attr(key).get();
		new PlayerReqTask(appPlayer, this, message).checkIn();
	}

}
