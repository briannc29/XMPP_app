package edu.hermes;

import java.io.Serializable;

public class Contact implements Serializable{
	private String name;
	private String status;
	
	public Contact(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
