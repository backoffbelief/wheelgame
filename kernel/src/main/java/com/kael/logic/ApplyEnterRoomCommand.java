package com.kael.logic;

import com.kael.kernel.AppPlayer;
import com.kael.kernel.AppPlayer.PlayerStatus;
import com.kael.kernel.Cmd;
import com.kael.kernel.IMessage;
import com.kael.kernel.PlayerContext;
import com.kael.kernel.QueueCommand;

@Cmd(value = Constants.CLIENT_ASK_PAIRING)
public class ApplyEnterRoomCommand extends QueueCommand {

	@Override
	protected void exec(AppPlayer appPlayer, IMessage message) {
		if(appPlayer.changeStatus(PlayerStatus.login, PlayerStatus.pairing)){
			PlayerContext.getInstance().addInPairingRoom(appPlayer);
		}

	}

}
