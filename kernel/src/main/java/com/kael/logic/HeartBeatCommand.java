package com.kael.logic;

import com.kael.kernel.AppPlayer;
import com.kael.kernel.Cmd;
import com.kael.kernel.DirectCommand;
import com.kael.kernel.IMessage;

@Cmd(value = Constants.CLIENT_HEARTBEAT)
public class HeartBeatCommand extends DirectCommand {

	@Override
	protected void exec(AppPlayer appPlayer, IMessage message) {
		appPlayer.updateHeartBeatTime();
	}

}
