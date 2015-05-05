package com.kael.gun.client;

import com.google.protobuf.Message;

public class IMessage {
	private short code;
	private byte[] body;
	
	public static Builder create(){
		return new Builder();
	}

	public static class Builder{
		private IMessage icm;

		public Builder() {
			icm = new IMessage();
		}
		
		public Builder withCode(short code){
			icm.code = code;
			return this;
		}
		
		public Builder withBody(byte[] bytes){
			icm.body = bytes;
			return this;
		}
		
		public Builder withBody(Message.Builder builder){
			icm.body = builder.build().toByteArray();
			return this;
		}
		
		public IMessage build(){
			return icm;
		}
	}

	public short getCode() {
		return code;
	}

	public byte[] getBody() {
		return body;
	}
	
}
