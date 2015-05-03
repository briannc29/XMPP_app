package edu.hermes;

import edu.services.GetFriendListService;
import edu.services.MainService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFriendFragment extends Fragment{
	EditText userET ;
	EditText nickET;
	Button addBtn;
	
	public static  String ACTION_ADD_FRIEND ="edu.hermes.AddFriendFragment.ADD_FRIEND";
	
	private BroadcastReceiver addFriendSuccess = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(getActivity(), "Friend add successfully", Toast.LENGTH_SHORT).show();
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add_friend, container,false);
		userET = (EditText) v.findViewById(R.id.username_add_friend_fragment);
		nickET = (EditText) v.findViewById(R.id.nickname_add_friend_fragment);
		addBtn = (Button) v.findViewById(R.id.addBtn_add_friend_fragment);
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String user =userET.getText().toString().trim();
				String nick =nickET.getText().toString().trim();
				if(!user.isEmpty()){
					MainService.getInstance(getActivity().getApplicationContext()).addFriend(user, nick);
				}else{
					Toast.makeText(getActivity(), "user name can't be empty", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(GetFriendListService.ACTION_PRESENCE_ADDED);
		getActivity().registerReceiver(addFriendSuccess, filter);
	}
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(addFriendSuccess);
	}
}
