package messages.server_login;

import java.io.Serializable;

/**
* @author alexandre
* ServerLoginAnswer.java
*/
public class ServerLoginAnswer extends ServerLoginMessage implements Serializable {
	private static final long serialVersionUID = 7144684580692667678L;
	
	private AnswerType answer;
	
	public ServerLoginAnswer(AnswerType answer) {
		super();
		this.answer = answer;
	}
	
	public AnswerType getAnswer() {
		return answer;
	}

	public enum AnswerType {
		SUCCES, BAD_INFOS, PERMISSION_DENIED;
	}
	
}
