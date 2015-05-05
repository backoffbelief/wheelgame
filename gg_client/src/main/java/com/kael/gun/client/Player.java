package com.kael.gun.client;

import com.kael.req.RespProto.LoginResultProto;
import com.kael.req.RespProto.PlayerProto;

public class Player {
	private String name;

	private int index;

	private int userId;

	private int counter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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

	public Player() {
	}

	public void parse(PlayerProto pp){
		this.name = pp.getName();
		this.counter = pp.getCounter();
		this.index = pp.getIndex();
		this.userId = pp.getUserId();
	}
	
	public void parse(LoginResultProto pp){
		this.name = pp.getName();
		this.counter = pp.getCounter();
//		this.index = pp.getIndex();
		this.index = -1;
		this.userId = pp.getUserId();
	}
	
	public Player(String name, int index, int userId, int counter) {
		super();
		this.name = name;
		this.index = index;
		this.userId = userId;
		this.counter = counter;
	}

	@Override
	public String toString() {
		return String.format(
				"[name=%s, index=%s, userId=%s, counter=%s]", name,
				index, userId, counter);
	}
	
	
}
