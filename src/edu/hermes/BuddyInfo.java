package edu.hermes;

import java.io.Serializable;

public class BuddyInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3992216592441163186L;
	String username;
	String nickName;
	boolean  isOnline;
	String status;
	String subscribed;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BuddyInfo(String username, String nickName, boolean isOnline,
			String status,String subscribed) {
		super();
		if(username !=null){
			
			String [] tokens =username.split("@");
			this.username = tokens[0];
		}
		this.nickName = nickName;
		this.isOnline = isOnline;
		this.status = status;
		this.subscribed = subscribed;
	}
	
	
}
