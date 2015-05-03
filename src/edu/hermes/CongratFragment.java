package edu.hermes;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CongratFragment extends Fragment {
	
	Callback callback;
	public interface Callback{
		void getLoginFragment();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_congrat, container,false);
		Button backtoLoginBtn = (Button) v.findViewById(R.id.login_congrat_fragment);
		backtoLoginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.getLoginFragment();
			}
		});
		return v;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (Callback) activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		callback = null;
	}
}
