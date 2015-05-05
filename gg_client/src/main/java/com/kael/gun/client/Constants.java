package com.kael.gun.client;

public interface Constants {
    /**===============client========================*/
	short CLIENT_LOGIN = 100;
	short CLIENT_ASK_PAIRING = 101;
	short CLIENT_ACT_IN_ROOM = 102;
	
	short CLIENT_HEARTBEAT = 1000;//
	/**=====================server==*/
	short SERVER_LOGIN_RESP = 200;
	short SERVER_START_RESP = 201;
	short SERVER_IN_PAIRING_ROOM = 202;
	short SERVER_ASK_ACT = 203;
}
