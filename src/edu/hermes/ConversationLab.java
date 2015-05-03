package edu.hermes;

import java.util.ArrayList;

import android.content.Context;

public class ConversationLab {
	private static ConversationLab lab;
	private ArrayList<Conversation> conList = new ArrayList<Conversation>();
	private static  Context context;
	private ConversationLab(Context appContext){
		ConversationLab.context = appContext;
	}
	public static ConversationLab getInstance(Context appContext){
		if(lab == null){
			lab = new ConversationLab(appContext);
		}
		return lab;
	}
	
	public ArrayList<Conversation> getList(){
		return conList;
	}
	
	public void addConversation(Conversation con){
		for (Conversation conversation : conList) {
			if(conversation.getPartner().equals(con.getPartner())){
				conversation.setHistory(con.getHistory());
				conversation.setText(con.getText());
				return;
			}
		};
		conList.add(con);
	}
}
