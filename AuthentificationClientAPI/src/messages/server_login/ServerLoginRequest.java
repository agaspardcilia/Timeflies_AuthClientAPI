package messages.server_login;

import java.io.Serializable;

/**
* @author alexandre
* ServerLoginRequest.java
*/
public class ServerLoginRequest extends ServerLoginMessage  implements Serializable {
	private static final long serialVersionUID = -5130194278356505223L;

	private String login;
	private String pwd;
	
	public ServerLoginRequest(String login, String pwd) {
		super();
		this.login = login;
		this.pwd = pwd;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPwd() {
		return pwd;
	}
	
}
