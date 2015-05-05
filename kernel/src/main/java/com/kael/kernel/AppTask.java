package com.kael.kernel;

public abstract class AppTask implements Runnable{

	protected AppQueue appQueue;
	
	public AppTask(AppQueue appQueue) {
		this.appQueue = appQueue;
	}

	public void checkIn(){
		appQueue.execute(this);
	}
	
	public void run() {
		try {
			exec();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			appQueue.checkOut();
		}
	}

	protected abstract void exec() ;

}
