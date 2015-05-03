package edu.hermes;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment{
	Button signOuBtn;
	public static final String ACTION_SIGN_OUT ="edu.hermes.SettingsFragment.SIGN_OUT";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings, container,false);
		signOuBtn = (Button) v.findViewById(R.id.signoutBtn_fragment_settings);
		signOuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(intent);
				getActivity().finish(); 
				
			}
		});
		return v; 
	}
}
