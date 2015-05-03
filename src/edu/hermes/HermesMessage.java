package edu.hermes;

import java.io.Serializable;

public class HermesMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5926972851188612793L;
	private String to;
	private String body;
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public HermesMessage(String to, String body) {
		super();
		this.to = to;
		this.body = body;
	}
	
}
