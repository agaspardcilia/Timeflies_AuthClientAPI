package messages.server_login;

import messages.Message;

/**
* @author alexandre
* ServerLoginMessage.java
*/
public class ServerLoginMessage extends Message {
	private final static MessageType TYPE = MessageType.SERVER_LOGIN;
	
	public ServerLoginMessage() {
		super(TYPE);
	}

}
