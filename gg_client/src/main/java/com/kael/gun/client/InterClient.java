package com.kael.gun.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class InterClient {

	private String host;
	private int port;
	
	private Channel channel;
	
	private boolean isConnect;
	
	private final GuiClient client;

	public InterClient(String host, int port,GuiClient client) {
		super();
		this.host = host;
		this.port = port;
		isConnect = false;
		this.client = client;
	}

	public void startConnect(){
		if(isConnect){
			return ;
		}
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup(2)).channel(NioSocketChannel.class)
		.handler(new ClientChannelInitializer(client));
		try {
			channel = bootstrap.connect(host, port).sync().channel();
			isConnect = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void writeMsg(IMessage message){
		channel.writeAndFlush(message);
	}
}
