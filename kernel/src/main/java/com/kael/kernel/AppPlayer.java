package com.kael.kernel;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kael.bo.BoProto.GamePlayerProto;
import com.kael.logic.AppRoom;
import com.kael.req.RespProto.PlayerProto;

public class AppPlayer {
    protected AppExecutors executors;
    
    protected ChannelHandlerContext ctx;
    
    protected AtomicLong lastHeartTime = new AtomicLong();
    
    protected AtomicReference<PlayerStatus> ps = new AtomicReference<AppPlayer.PlayerStatus>();
    
    private int counter;
    
    private int userId;
    private String name ;
    
    private AtomicReference<AppRoom> appRoom;
    
    public enum PlayerStatus{
    	create(0),
    	login(1 << 1),
    	pairing(1 << 2),
    	playInRoom(1 << 3),
    	exit(1<<4);
    	
    	final int code;

		private PlayerStatus(int code) {
			this.code = code;
		}
    }

	public AppPlayer(AppExecutors executors, ChannelHandlerContext ctx) {
		super();
		this.executors = executors;
		this.ctx = ctx;
		lastHeartTime.set(System.currentTimeMillis());
		appRoom = new AtomicReference<AppRoom>();
	}
    
	public AppQueue getAppQueue(){
		return executors.getAppQueue();
	}
	
	public void write(IMessage.Builder msg){
		if(ctx != null && PlayerStatus.exit != ps.get()){
			ctx.channel().writeAndFlush(msg);
		}
	}
	
	public void updateHeartBeatTime(){
		lastHeartTime.set(System.currentTimeMillis());
	}
	
	public boolean changeStatus(PlayerStatus expect ,PlayerStatus update){
		return ps.compareAndSet(expect, update);
	}

	public void delCounter(int i) {
		if(i <= 0 ){
			throw new RuntimeException("i["+i+"]  <=  0");
		}
		if(counter < i){
			throw new RuntimeException("i["+i+"]  >  counter["+counter+"]");
		}
		counter -= i;
	}
	
	public void incrCounter(int i) {
		if(i <= 0){
			throw new RuntimeException("["+i+"]  <=  0");
		}
		counter += i;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public AppRoom getAppRoom() {
		return appRoom.get();
	}

	public void setAppRoom(AppRoom appRoom) {
		this.appRoom.compareAndSet(null, appRoom);
	}
	
	public void exit(){
		this.appRoom.set(null);
	}
	
	public PlayerProto.Builder toBuilder(int index){
		PlayerProto.Builder builder = PlayerProto.newBuilder();
		builder.setCounter(counter);
		builder.setIndex(index);
		builder.setUserId(userId);
		builder.setName(name);
		return builder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public byte[] copyTo(){
		GamePlayerProto.Builder builder = GamePlayerProto.newBuilder();
		builder.setCounter(counter);
		builder.setName(name);
		builder.setUserId(userId);
		return builder.build().toByteArray();
	}
	
	public void parseFrom(byte[] bytes){
		try {
			GamePlayerProto proto = GamePlayerProto.parseFrom(bytes);
			this.counter = proto.getCounter();
			this.name = proto.getName();
			this.userId = proto.getUserId();
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		
	}
}
