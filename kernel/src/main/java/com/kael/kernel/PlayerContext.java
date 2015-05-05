package com.kael.kernel;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.kael.logic.AppRoom;
import com.kael.logic.AppRoom.RoomStatus;

public class PlayerContext {
	
	private static final PlayerContext instance = new PlayerContext();
	
	public static PlayerContext getInstance(){
		return instance;
	}

	//private ConcurrentHashMap<Integer, AppPlayer> appPlayers = new ConcurrentHashMap<Integer, AppPlayer>();
	
	private ConcurrentHashMap<Integer, AppRoom> appRooms = new ConcurrentHashMap<Integer, AppRoom>();
	
	private ConcurrentHashMap<Integer, AppRoom> appPairingRooms = new ConcurrentHashMap<Integer, AppRoom>();
	
	private static final IdGenerator GENERATOR = new IdGenerator();
	
	
	private static class IdGenerator{
		private AtomicInteger id = new AtomicInteger();
		public int incrId(){
			return id.getAndIncrement();
		}
	}
	
	public int generatorRoomId(){
		return GENERATOR.incrId();
	}

	public void addInPairingRoom(final AppPlayer appPlayer) {
		new AppTask(appPlayer.getAppQueue()) {
			@Override
			protected void exec() {
				boolean isPairSuccess = false;
				while(!isPairSuccess){
					if(appPairingRooms.isEmpty()){
						int roomId = generatorRoomId();
						appPairingRooms.putIfAbsent(roomId, new AppRoom(roomId));
					}
					for (Iterator<AppRoom> iterator = appPairingRooms.values().iterator(); iterator
							.hasNext();) {
						AppRoom room = iterator.next();
						if(room != null && room.applyIntoRoom(appPlayer)){
							appPlayer.setAppRoom(room);
							room.notifyToAllPairing();
							if(room.isFull()
									&& room.casStatus(RoomStatus.pairing, RoomStatus.running) 
									&& appPairingRooms.remove(room.getRoomId()) != null){
								appRooms.putIfAbsent(room.getRoomId(), room);
								room.start();
							}
							isPairSuccess = true;
							break;
						}
					}
				}
			}
		}.checkIn();
	}

	public void remove(AppRoom appRoom) {
		appRoom.casStatus(RoomStatus.exiting, RoomStatus.exited);
		
		appRooms.remove(appRoom.getRoomId());
	}

}
