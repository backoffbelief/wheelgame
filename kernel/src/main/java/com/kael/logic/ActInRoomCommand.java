package com.kael.logic;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kael.kernel.AppPlayer;
import com.kael.kernel.Cmd;
import com.kael.kernel.IMessage;
import com.kael.kernel.QueueCommand;
import com.kael.req.ReqProto.ReqActInGame;

@Cmd(value = Constants.CLIENT_ACT_IN_ROOM)
public class ActInRoomCommand extends QueueCommand {

	@Override
	protected void exec(AppPlayer appPlayer, IMessage message) {
		ReqActInGame actInGame = null;
		try {
			actInGame = ReqActInGame.parseFrom(message.getBody());
		} catch (InvalidProtocolBufferException e) {
			throw new RuntimeException(e);
		}
		AppRoom room = appPlayer.getAppRoom();
		
		if(room != null && room.isRunning() ){
			if(!room.isCurrentPlayerAct(appPlayer.getUserId())){
				throw new RuntimeException("currIndex:"+ room.getCurrentActIndexId()+",");
			}
			if(0 == actInGame.getAction()){
			    room.reChoose();
			}else if(1 == actInGame.getAction()){
				room.fire();
			}
		}
		
	}

}
