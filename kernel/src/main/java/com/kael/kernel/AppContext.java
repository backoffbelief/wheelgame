package com.kael.kernel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AppContext {

	//Map<String, Object> ctx = new HashMap<String, Object>();
	
	
	private Map<Short, ICommand> cc = new HashMap<Short, ICommand>();
	
	
//	public static <T> T get(Class<T> clazz){
//		
//	}
	private static final AppContext APP_CONTEXT = new AppContext();
	
	public static AppContext getInstance(){
		return APP_CONTEXT;
	}
	
	public void init(final Set<Class<?>> clazz) throws Exception{
		for (Class cla : clazz) {
			
			if(cla.isAnnotationPresent(Cmd.class)){
				Cmd cmd = (Cmd) cla.getAnnotation(Cmd.class);
				short value = cmd.value();
				if(cc.containsKey(value)){
					throw new RuntimeException("repeated cmd code["+value+"] cla["+cla.getName()+"]");
				}
				cc.put(value,(ICommand)(cla.newInstance()));
			}
		}
//		System.out.println(cc.size());
		executors = new AppExecutors("player");
	}
	
	
	public ICommand get(short code){
		ICommand command = cc.get(code);
		if(command == null){
			throw new RuntimeException("not exist code="+code);
		}
		return command;
	}
	
	private AppExecutors executors ;
	public AppExecutors getAppExecutors(){
		return executors;
	}
}
