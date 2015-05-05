package com.kael.kernel;

public abstract class AbsCommand implements ICommand {

	protected abstract void exec(AppPlayer appPlayer,IMessage message);

}
