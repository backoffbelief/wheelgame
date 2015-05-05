package com.kael.kernel;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AppExecutors {
	private AppQueue appQueue;
    
	private ThreadPoolExecutor tpe;
	public AppExecutors(String namePrefix) {
//		Executors.newFixedThreadPool(nThreads, threadFactory)
		tpe = new ThreadPoolExecutor(1, 5, 5, TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory(
						namePrefix));
		appQueue = new AppQueue(this);
	}
	
	public AppQueue getAppQueue() {
		return appQueue;
	}

	public void execute(AppTask task){
		tpe.execute(task);
	}
	
	
	 /**
     * The default thread factory
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                                  Thread.currentThread().getThreadGroup();
            if(namePrefix == null)
            	{this.namePrefix = "pool-" +
                          poolNumber.getAndIncrement() +
                         "-thread-";
            	}
            else{
            	{this.namePrefix = "pool-" +
                        poolNumber.getAndIncrement() +
                       "-thread-"+namePrefix;
          	}
            }
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
