package messages;
/**
* @author alexandre
* Message.java
*/
public abstract class Message {
	private final MessageType messageType;
	
	public Message(MessageType type) {
		messageType = type;
	}
	
	public MessageType getMessageType() {
		return messageType;
	}
	
	public enum MessageType {
		LOGIN, SERVER_LOGIN, REFRESH, ERROR;
	}
	
	public boolean isEndOfCom() {
		return false;
	}
}

