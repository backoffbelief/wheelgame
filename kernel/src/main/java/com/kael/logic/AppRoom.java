package com.kael.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import com.kael.kernel.AppPlayer;
import com.kael.kernel.AppPlayer.PlayerStatus;
import com.kael.kernel.IMessage;
import com.kael.kernel.PlayerContext;
import com.kael.req.RespProto.AskNextPlayerActProto;
import com.kael.req.RespProto.PairingResultProto;
import com.kael.req.RespProto.RoomStartResultProto;
import com.kael.server.JRedis;

public class AppRoom {

	public enum RoomStatus{
		pairing,
		running,
		exiting,
		exited;
	}
	
	private AtomicReference<RoomStatus> roomStatus = new AtomicReference<AppRoom.RoomStatus>();
	private ReentrantLock lock = new ReentrantLock();
	private TreeMap<Integer, AppPlayer> appPlayers = new TreeMap<Integer, AppPlayer>();
	private final int roomId;
	
	private int choose;
	
	private Map<Integer, Integer> counters = new HashMap<Integer, Integer>();
	
	private int currentActIndexId;
	
	public AppRoom(int roomId) {
		this.roomId = roomId;
		roomStatus.set(RoomStatus.pairing);
	}
	
	public int getRoomId() {
		return roomId;
	}

	public boolean applyIntoRoom(AppPlayer appPlayer){
		try {
			lock.lock();
			if(appPlayers.size() >= Constants.total_num){
				return false;
			}
			return appPlayers.put(appPlayers.size(), appPlayer) == null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			lock.unlock();
		}
	}
	
	public boolean isFull(){
		try {
			lock.lock();
			if(appPlayers.size() >= Constants.total_num){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			lock.unlock();
		}
	}
	
	public boolean casStatus(RoomStatus expect,RoomStatus update){
		return roomStatus.compareAndSet(expect, update);
	}

	public void start() {
		try {
			lock.lock();
			RoomStartResultProto.Builder builder = RoomStartResultProto.newBuilder();
			for (Iterator<Entry<Integer, AppPlayer>> iterator = appPlayers.entrySet().iterator(); iterator.hasNext();) {
				Entry<Integer, AppPlayer> apEntry =  iterator.next();
				
				if(apEntry != null){
					AppPlayer ap = apEntry.getValue();
					ap.changeStatus(PlayerStatus.pairing, PlayerStatus.playInRoom);
					ap.delCounter(100);
					int old = counters.get(ap.getUserId()) == null ? 0 : counters.get(ap.getUserId());
					counters.put(ap.getUserId(),old + 100);
					builder.addPlayers(ap.toBuilder(apEntry.getKey()));
				}
			}
			choose();
			currentActIndexId = appPlayers.firstKey();
			builder.setActIndex(currentActIndexId);
			notifyToAll(IMessage.create().withCode(Constants.SERVER_START_RESP).withBody(builder));
			System.out.println("--------start--------");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		
	}

	public void notifyToAll(final IMessage.Builder message) {
		for (Iterator<AppPlayer> iterator = appPlayers.values().iterator(); iterator.hasNext();) {
			AppPlayer ap =  iterator.next();
			if(ap != null){
				ap.write(message);
			}
		}
	}
	
	public void notifyToAllPairing(){
		notifyToAll(IMessage.create().withCode(Constants.SERVER_IN_PAIRING_ROOM).withBody(getAllPlayersInfo()));
	}
	
	public PairingResultProto.Builder getAllPlayersInfo() {
		PairingResultProto.Builder builder = PairingResultProto.newBuilder();
		for (Iterator<Entry<Integer, AppPlayer>> iterator = appPlayers.entrySet().iterator(); iterator.hasNext();) {
			Entry<Integer, AppPlayer> apEntry =  iterator.next();
			
			if(apEntry != null){
				AppPlayer ap = apEntry.getValue();
				builder.addPlayers(ap.toBuilder(apEntry.getKey()));
			}
		}
		return builder;
	}
	
	public void reChoose(){
		AppPlayer ap = appPlayers.get(currentActIndexId);
		ap.delCounter(100);
		int old = counters.get(ap.getUserId()) == null ? 0 : counters.get(ap.getUserId());
		counters.put(ap.getUserId(),old + 100);
		choose();
		notifyToAll(IMessage.create().withCode(Constants.SERVER_ASK_ACT)
				.withBody(AskNextPlayerActProto.newBuilder().setIndex(currentActIndexId)));
	}
	
	private void choose(){
		choose = new Random().nextInt(Constants.total_num);
	}
	
	public void fire(){
		if(currentActIndexId == choose){
			end();
		}else{
			next();
		}
		
	}

	private void next() {
		currentActIndexId = (currentActIndexId >=  appPlayers.lastKey() ? appPlayers.firstKey() : appPlayers.higherKey(currentActIndexId));
//		appPlayers.get(currentActIndexId).write(IMessage.create().withCode(Constants.SERVER_ASK_ACT).
//				withBody(AskNextPlayerActProto.newBuilder()).build());
		notifyToAll(IMessage.create().withCode(Constants.SERVER_ASK_ACT)
				.withBody(AskNextPlayerActProto.newBuilder().setIndex(currentActIndexId)));
	}

	private void end() {
		this.casStatus(RoomStatus.running, RoomStatus.exiting);
		AppPlayer appPlayer = appPlayers.get(currentActIndexId);
		int extra = counters.get(appPlayer.getUserId()) / (Constants.total_num - 1);
		for (Iterator<AppPlayer> iterator = appPlayers.values().iterator(); iterator.hasNext();) {
			AppPlayer ap = iterator.next();
			
			if(ap != null && ap.getUserId() != appPlayer.getUserId()){
				ap.incrCounter(extra + counters.get(ap.getUserId()));
			}
			JRedis.getInstance().set(("entity_gamePlayer_"+ap.getUserId()).getBytes(), appPlayer.copyTo());
			ap.exit();
//			iterator.remove();
		}
		PlayerContext.getInstance().remove(this);
	}

	public int getCurrentActIndexId() {
		return currentActIndexId;
	}
	
	public boolean isRunning(){
		return roomStatus.get() == RoomStatus.running;
	}
	
	public boolean isCurrentPlayerAct(int userId){
		return appPlayers.get(currentActIndexId).getUserId() == userId;
	}
	
}
