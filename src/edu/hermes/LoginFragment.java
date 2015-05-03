package edu.hermes;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment{
	EditText usernameET;
	EditText passwordET;
	Callbacks mCallbacks;
	public interface Callbacks{
		void login(String username, String password);
		void switchToRegister();
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		usernameET = (EditText) v.findViewById(R.id.usernameET);
		passwordET = (EditText) v.findViewById(R.id.passwordET);
		Button connectBtn = (Button) v.findViewById(R.id.connectBtn);
		connectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = usernameET.getText().toString().trim();
				String password = passwordET.getText().toString().trim();
				if(username.equals("")||password.equals("")){
					Toast.makeText(getActivity(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
				}else {
					mCallbacks.login(username, password);
				}
			}
		});
		Button registerBtn = (Button) v.findViewById(R.id.register_login_fragment);
		registerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					mCallbacks.switchToRegister();
			}
		});
		return v;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks) activity;
	}
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
}
