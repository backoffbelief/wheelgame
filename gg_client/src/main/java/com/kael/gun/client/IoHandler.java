package com.kael.gun.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.kael.req.RespProto.AskNextPlayerActProto;
import com.kael.req.RespProto.LoginResultProto;
import com.kael.req.RespProto.PairingResultProto;
import com.kael.req.RespProto.PlayerProto;
import com.kael.req.RespProto.RoomStartResultProto;

public class IoHandler extends SimpleChannelInboundHandler<IMessage> {
	
	private final GuiClient client;

	public IoHandler(GuiClient client) {
		super();
		this.client = client;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
//		AppPlayer appPlayer = new AppPlayer(AppContext.getInstance().getAppExecutors(), ctx);
//		ctx.attr(AttributeKey.valueOf("user")).set(appPlayer);
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
//		ctx.attr(AttributeKey.valueOf("user")).getAndRemove();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IMessage msg)
			throws Exception {
//		AppContext.getInstance().get(msg.getCode()).execute(ctx, msg);
		short code = msg.getCode();
		System.out.println("channelRead0==" +code );
		switch (code) {
		case Constants.SERVER_LOGIN_RESP:
			LoginResultProto lrp = LoginResultProto.parseFrom(msg.getBody());
			client.getPlayer().parse(lrp);
			client.refreshJlabel();
			client.print(lrp);
			System.out.println(lrp);
			break;
		case Constants.SERVER_IN_PAIRING_ROOM:
			PairingResultProto pairingResultProto = PairingResultProto.parseFrom(msg.getBody());
			for(PlayerProto pp : pairingResultProto.getPlayersList()){
				if(pp.getUserId() == client.getPlayer().getUserId()){
					client.getPlayer().parse(pp);
					client.refreshJlabel();
					break;
				}
			}
			System.out.println(pairingResultProto);
			client.print(pairingResultProto);
			break;
		case Constants.SERVER_START_RESP:
			RoomStartResultProto roomStartResultProto = RoomStartResultProto.parseFrom(msg.getBody());
			client.print(roomStartResultProto);
			break;
		case Constants.SERVER_ASK_ACT:
			AskNextPlayerActProto askNextPlayerActProto = AskNextPlayerActProto.parseFrom(msg.getBody());
			client.print(askNextPlayerActProto);
			break;

		default:
			break;
		}
		
	}

}