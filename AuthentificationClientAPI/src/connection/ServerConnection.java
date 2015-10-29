package connection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import exceptions.AuthentificationFailedException;
import exceptions.NotLoggedAsAServerException;
import messages.Message;
import messages.error.ErrorCodes;
import messages.login.LoginAnswer;
import messages.login.LoginRequest;
import messages.refresh.RefreshEndOfCom;
import messages.refresh.RefreshSessionAnswer;
import messages.refresh.RefreshSessionAnswer.Answer;
import messages.refresh.RefreshSessionRequest;
import messages.server_login.ServerLoginAnswer;
import messages.server_login.ServerLoginAnswer.AnswerType;
import messages.server_login.ServerLoginRequest;
import utils.Sha1;

/**
* @author alexandre
* ServerConnection.java
*/
public class ServerConnection {
	//Message sending attempts.
	public final static int RETRY = 5;
	
	//Server's info
	private String addr;
	private int port;
	
	//Client's info
	private String username;
	private String pwd;
	
	//Socket and streams
	private Socket refreshSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	/**
	 * Collects data to connect to the authentication server.
	 * @param addr
	 * 	Server's address.
	 * @param port
	 * 	Server's port.
	 * @param username
	 * 	Username
	 * @param pwd
	 * 	Password
	 * @param useSha1
	 * 	Do you wan't to let ServerConnection hash the password ? 
	 */
	public ServerConnection(String addr, int port, String username, String pwd, boolean useSha1) {
			this.addr = addr;
			this.port = port;
			this.username = username;
			if (useSha1)
				this.pwd = Sha1.sha1(pwd);
			else
				this.pwd = pwd;
	}
	
	public UUID connect() throws AuthentificationFailedException {
		Socket socket = null;
		ObjectOutputStream out;
		ObjectInputStream in;
		LoginAnswer message;
		UUID token = null;
		
		try {
			socket = new Socket(InetAddress.getByName(addr), port);
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			
			sendMessage(out, new LoginRequest(username, pwd));
			
			message = (LoginAnswer) readMessage(in);
			token = message.getToken();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		if (token == null)
			throw new AuthentificationFailedException(ErrorCodes.BAD_USR_OR_PWD_txt);
		return token;
	}
	
	
	/**
	 * Connects an user.
	 * @throws AuthentificationFailedException
	 * 	Thrown when something wen't wrong.
	 */
	public void connectAsServer() throws AuthentificationFailedException {
		ServerLoginAnswer message;
		
		try {
			refreshSocket = new Socket(InetAddress.getByName(addr), port);
			in = new ObjectInputStream(refreshSocket.getInputStream());
			out = new ObjectOutputStream(refreshSocket.getOutputStream());
			
			sendMessage(out, new ServerLoginRequest(username, pwd));
			
			message  = (ServerLoginAnswer) readMessage(in);
			
			
			if (message.getAnswer().equals(AnswerType.SUCCES)) {
				return;
			} else if (message.getAnswer().equals(AnswerType.BAD_INFOS)) {
				refreshSocket.close();
				refreshSocket = null;
				throw new AuthentificationFailedException(ErrorCodes.BAD_USR_OR_PWD_txt);
			} else {
				refreshSocket.close();
				refreshSocket = null;
				throw new AuthentificationFailedException(ErrorCodes.NOT_A_SERVER_txt);
			}
			
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//TODO handle communication errors.
	//TODO include client InetAddress
	/**
	 * If you are connected as a server, this method will refresh a token. By that i mean test if the token
	 * is still valid (not out dated and with the right InetAddress).
	 * @param token
	 * 	Token to refresh.
	 * @return
	 * 	True = the token has been refreshed. False = the token is not valid. 
	 * @throws NotLoggedAsAServerException
	 * 	Thrown if you try to use this method without been connected as a server. It could mean that you forgot to use
	 * the method connectAsServer().
	 */
	public boolean refreshToken(UUID token) throws NotLoggedAsAServerException {
		if(refreshSocket == null) {
			System.err.println("You must be logged as a server to do that.");
			throw new NotLoggedAsAServerException();
		} else {
			sendMessage(out, new RefreshSessionRequest(token));
			
			RefreshSessionAnswer answer = (RefreshSessionAnswer) readMessage(in);
			
			return answer.getMessageType().equals(Answer.SUCCESS);
		}
	}
	
	public void closeRefreshSocket() {
		try {
			sendMessage(out, new RefreshEndOfCom());
			refreshSocket.close();
		} catch (IOException e) {
		}
	}
	
	/**
	 * Send a message through the socket.
	 * @param m 
	 * 	Message to send.
	 * @return
	 * 	True = the message has been send. False = Failed to deliver the message.
	 */
	private boolean sendMessage(ObjectOutputStream out, Message m) {
		for (int i = 0; i < RETRY; i++) {
			try {
				out.writeObject(m);
				return true;
			} catch (IOException e) {
				
			}
		}
		
		return false;
	}
	
	/**
	 * Read the next message in socket.
	 * @return
	 * 	Read message.
	 */
	private Message readMessage(ObjectInputStream in) {
		try {
			return (Message)in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}
	
	
}
