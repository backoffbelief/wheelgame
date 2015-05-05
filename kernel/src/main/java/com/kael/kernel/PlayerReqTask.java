package com.kael.kernel;

public class PlayerReqTask extends AppTask {

	private AppPlayer appPlayer;
	private AbsCommand command;
	private IMessage message;
	public PlayerReqTask(AppPlayer appPlayer, AbsCommand command,
			IMessage message) {
		super(appPlayer.getAppQueue());
		this.appPlayer = appPlayer;
		this.command = command;
		this.message = message;
	}


	@Override
	protected void exec() {
          command.exec(appPlayer, message);
	}

}
