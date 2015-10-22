package messages.refresh;

import java.io.Serializable;

/**
* @author alexandre
* RefreshEndOfCom.java
*/
public class RefreshEndOfCom extends RefreshMessage implements Serializable {
	private static final long serialVersionUID = 2342175863027686650L;

	public RefreshEndOfCom() {
		super();
	}

	@Override
	public boolean isEndOfCom() {
		return true;
	}
	
}
