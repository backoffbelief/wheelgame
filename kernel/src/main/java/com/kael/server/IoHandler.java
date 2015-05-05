package com.kael.server;

import com.kael.kernel.AppContext;
import com.kael.kernel.AppPlayer;
import com.kael.kernel.ICommand;
import com.kael.kernel.IMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

public class IoHandler extends SimpleChannelInboundHandler<IMessage> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
		AppPlayer appPlayer = new AppPlayer(AppContext.getInstance().getAppExecutors(), ctx);
		ctx.attr(ICommand.key).set(appPlayer);
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
		ctx.attr(ICommand.key).getAndRemove();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IMessage msg)
			throws Exception {
		System.out.println("channelRead0===" + msg.getCode());
		AppContext.getInstance().get(msg.getCode()).execute(ctx, msg);
		
	}

}
