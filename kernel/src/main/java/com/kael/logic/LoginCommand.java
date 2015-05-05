package com.kael.logic;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kael.kernel.AppPlayer;
import com.kael.kernel.AppPlayer.PlayerStatus;
import com.kael.kernel.AppTask;
import com.kael.kernel.Cmd;
import com.kael.kernel.IMessage;
import com.kael.kernel.QueueCommand;
import com.kael.req.ReqProto.LoginProto;
import com.kael.req.RespProto.LoginResultProto;
import com.kael.server.JRedis;

@Cmd(value = Constants.CLIENT_LOGIN)
public class LoginCommand extends QueueCommand {

	@Override
	protected void exec(final AppPlayer appPlayer,final IMessage message) {
		new AppTask(appPlayer.getAppQueue()) {
			
			@Override
			protected void exec() {
				LoginProto loginProto = null;
				try {
					loginProto = LoginProto.parseFrom(message.getBody());
				} catch (InvalidProtocolBufferException e) {
					throw new RuntimeException(e);
				}
				JRedis jredis = JRedis.getInstance();
				byte[] bytes = jredis.get(("gamePlayer_"+loginProto.getName()).getBytes());
				int playerId = 0;
				if(bytes != null){
					playerId = Integer.parseInt(new String(bytes));
					appPlayer.parseFrom(jredis.get(("entity_gamePlayer_"+playerId).getBytes()));
				}else{
					playerId = jredis.incr(("id_gamePlayer").getBytes()).intValue();
					jredis.set(("gamePlayer_"+loginProto.getName()).getBytes(), String.valueOf(playerId).getBytes());
					appPlayer.setUserId(playerId);
					appPlayer.setCounter(300);
					appPlayer.setName(loginProto.getName());
					jredis.set(("entity_gamePlayer_"+playerId).getBytes(), appPlayer.copyTo());
				}
				
				
				LoginResultProto.Builder builder = LoginResultProto.newBuilder();
				builder.setCounter(appPlayer.getCounter());
				builder.setName(appPlayer.getName());
				builder.setUserId(appPlayer.getUserId());
				appPlayer.write(IMessage.create().withCode(Constants.SERVER_LOGIN_RESP).withBody(builder));
				appPlayer.changeStatus(null, PlayerStatus.login);
			}
		}.checkIn();
	}

}
