package edu.hermes;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationFragment extends ListFragment{
	ArrayList<Conversation> mConversationAL;
	ConversationtAdapter contactAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mConversationAL = ConversationLab.getInstance(getActivity().getApplicationContext()).getList();
		
		 contactAdapter = new ConversationtAdapter(mConversationAL);
		setListAdapter(contactAdapter);
		setRetainInstance(true);
	}
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		
	}
	private class ConversationtAdapter extends ArrayAdapter<Conversation>{

		public ConversationtAdapter(ArrayList<Conversation> conversations) {
			super(getActivity(), android.R.layout.simple_list_item_1, conversations);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView ==null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_conversation_detail, null);
			}
			final Conversation convS = getItem(position);
			
			
			TextView contactName = (TextView) convertView.findViewById(R.id.conversation_talk_to_person);
			contactName.setText(convS.getPartner()); 
			TextView lastTimeTalk = (TextView) convertView.findViewById(R.id.conversation_last_seen);
			lastTimeTalk.setText(convS.getHistory()); 
			TextView lastMessage = (TextView) convertView.findViewById(R.id.conversation_last_message);
			lastMessage.setText(convS.getText()); 
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getContext(), IndividualConversationActivity.class);
					i.putExtra("buddy", new BuddyInfo(convS.getPartner(), null, false, null, null));
					startActivity(i);
				}
			});
			return convertView;
		}
		
	}
	@Override
	public void onResume() {
		super.onResume();
		mConversationAL = ConversationLab.getInstance(getActivity().getApplicationContext()).getList();
		
		 contactAdapter = new ConversationtAdapter(mConversationAL);
		setListAdapter(contactAdapter);
	}
}
