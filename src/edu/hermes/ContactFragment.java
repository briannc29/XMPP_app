package edu.hermes;

import java.util.ArrayList;

import edu.services.AddFriendService;
import edu.services.GetFriendListService;
import edu.services.RosterLab;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactFragment extends ListFragment {
	private ArrayList<BuddyInfo> mContactAL;
	
	private BroadcastReceiver friendListReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mContactAL.clear(); 
			mContactAL.addAll(RosterLab.getInstance(getActivity()).getRosterList());
			((BuddyAdapter)getListAdapter()).notifyDataSetChanged();
		}
	}; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContactAL = RosterLab.getInstance(getActivity()).getRosterList();
		
		BuddyAdapter buddyAdapter = new BuddyAdapter(mContactAL); 
		setListAdapter(buddyAdapter);
		setRetainInstance(true);
	}
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id); 
		
		
	}
	private class BuddyAdapter extends ArrayAdapter<BuddyInfo>{

		public BuddyAdapter(ArrayList<BuddyInfo> buddies) {
			super(getActivity(), android.R.layout.simple_list_item_1, buddies);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(null ==convertView){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_contact_detail, null);
			}
			final BuddyInfo buddy = getItem(position);
			ImageView contactImage = (ImageView) convertView.findViewById(R.id.image_fragment_contact_detail);
			if(buddy.isOnline){
				contactImage.setImageResource(R.drawable.online);
			}else{
				contactImage.setImageResource(R.drawable.ic_action_person);
				
			}
			
			TextView contactName = (TextView) convertView.findViewById(R.id.name_fragment_contact_detail);
			contactName.setText(buddy.getUsername()); 
			
			TextView status = (TextView) convertView.findViewById(R.id.status_fragment_contact_detail);
			status.setText(buddy.getStatus());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ConversationLab.getInstance(getActivity().getApplicationContext()).addConversation(new Conversation(buddy.getUsername(), "", "",null));
					Intent i = new Intent(getContext(), IndividualConversationActivity.class);
					i.putExtra("buddy", buddy);
					startActivity(i);
				}
			});
			return convertView;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		mContactAL.clear(); 
		mContactAL.addAll(RosterLab.getInstance(getActivity()).getRosterList());
		((BuddyAdapter)getListAdapter()).notifyDataSetChanged();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GetFriendListService.ACTION_NEW_FRIENDLIST);
		filter.addAction(GetFriendListService.ACTION_PRESENCE_CHANGE);
		getActivity().registerReceiver(friendListReceiver, filter);
		
	}
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(friendListReceiver);
		
	}
}
