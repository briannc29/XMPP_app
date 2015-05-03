package edu.hermes;

import android.net.Uri;

public class Conversation {
	private String partner;
	private String history;
	private String text;
	private Uri uri;
	
	public Conversation(String name, String time, String text, Uri uri) {
		this.partner = name;
		this.history = time;
		this.text = text;
		this.uri = uri;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}
	public void setText(String text){
		this.text = text;
	}
	public void setUri(Uri uri){
		this.uri = uri;
	}
	public Uri getUri(){
		return uri;
	}
}
