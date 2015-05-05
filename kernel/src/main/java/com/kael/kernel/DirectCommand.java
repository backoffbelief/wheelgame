package com.kael.kernel;

import io.netty.channel.ChannelHandlerContext;

public abstract class DirectCommand extends AbsCommand {

	public void execute(ChannelHandlerContext ctx, IMessage message) {
		AppPlayer appPlayer = ctx.attr(key).get();
		exec(appPlayer, message);
	}

}
