package messages.error;

import java.io.Serializable;

import messages.Message;

/**
* @author alexandre
* ErrorMessage.java
*/
public class ErrorMessage extends Message implements Serializable {
	private static final long serialVersionUID = 4514385579687171951L;

	public final static MessageType TYPE = MessageType.ERROR;
	
	private final int code;
	private final String text;
	
	public ErrorMessage(int code, String text) {
		super(TYPE);
		this.code = code;
		this.text = text;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getText() {
		return text;
	}
	
	
	@Override
	public String toString() {
		return "Error : " + text + "(code # " + code + " )";
	}
	
}
