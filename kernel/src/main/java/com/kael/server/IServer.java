package com.kael.server;

import java.util.Set;

import com.kael.kernel.AppContext;
import com.kael.kernel.Scan;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class IServer {
	
	public static void main(String[] args) {
		Set<Class<?>> clazs = Scan.getClasses("com");
		try {
			AppContext.getInstance().init(clazs);
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup(4))
			   .channel(NioServerSocketChannel.class)
			   .childHandler(new IoChannelInitializer());
			System.out.println("start listen at port==>10000");
			bootstrap.bind(10001).sync().channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
