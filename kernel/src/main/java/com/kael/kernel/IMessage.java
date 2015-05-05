package com.kael.kernel;

import com.google.protobuf.Message;

public class IMessage {

	private short code;
	private byte[] body;
	
	private IMessage() {
	}

	public static IMessage.Builder create(){
		return new IMessage.Builder();
	}
	
	public static class Builder{
		private final IMessage msg;

		private Builder() {
			super();
			this.msg = new IMessage();
		}
		
		public Builder withCode(short code){
			msg.code =  code;
			return this;
		}
		
		public Builder withBody(byte[] body){
			msg.body = body;
			return this;
		}
		
		public Builder withBody(Message.Builder builder){
			msg.body = builder.build().toByteArray();
			return this;
		}
		
		public IMessage build(){
			return msg;
		}
	}

	public short getCode() {
		return code;
	}

	public byte[] getBody() {
		return body;
	}
}
