package com.kael.kernel;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppQueue {

	private LinkedBlockingQueue<AppTask> queues = null;
	private AppExecutors appExecutors;
	private AtomicBoolean isRunning;
	public AppQueue(AppExecutors appExecutors) {
		super();
		this.appExecutors = appExecutors;
		queues = new LinkedBlockingQueue<AppTask>();
		isRunning = new AtomicBoolean(false);
	}
	
	
	public void execute(AppTask appTask){
		this.queues.offer(appTask);
		if(isRunning.compareAndSet(false, true)){
			execNext();
		}
	}


	private void execNext() {
		AppTask appTask = queues.peek();
		if(appTask != null){
			appExecutors.execute(appTask);
		}else{
			isRunning.set(false);
			
			appTask = queues.peek();
			
			if(appTask != null && isRunning.compareAndSet(false, true)){
				appExecutors.execute(appTask);
			}
		}
		
	}
	
	public void checkOut(){
		queues.poll();
		execNext();
	}
	
}
