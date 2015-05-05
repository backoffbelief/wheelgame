package com.kael.kernel;

import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        System.out.println( "Hello World!" );
    	Set<Class<?>> clazs = Scan.getClasses("com");
    	for (Class<?> class1 : clazs) {
			System.out.println(class1.getName());
		}
    	
    	try {
			AppContext.getInstance().init(clazs);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
