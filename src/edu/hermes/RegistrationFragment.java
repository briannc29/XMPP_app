package edu.hermes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegistrationFragment extends Fragment{
	public static final int REQUEST_PHOTO = 0;
	EditText newNameET;
	EditText newPassET;
	Callback mCallback;
	ImageView imageIV;
	String path;
	public interface Callback{
		void signup(String username, String password);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_registration, container,false);
		newNameET = (EditText) v.findViewById(R.id.username_registration_fragment);
		newPassET = (EditText) v.findViewById(R.id.password_registration_fragment);
		imageIV = (ImageView) v.findViewById(R.id.image_registration_fragment);
		imageIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(path !=null){
					FragmentManager fm = getActivity().getSupportFragmentManager();
					ImageFragment.createInstance(path).show(fm, "Image");
					
				}
			}
		});
		Button cameraBtn = (Button) v.findViewById(R.id.takephoto_registration_fragment);
		cameraBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CameraProfileActivity.class);
				startActivityForResult(i,REQUEST_PHOTO );
			}
		});
		Button connectBtn = (Button) v.findViewById(R.id.signup_registration_fragment);
		connectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = newNameET.getText().toString().trim();
				String password = newPassET.getText().toString().trim();
				if(username.equals("")||password.equals("")){
					Toast.makeText(getActivity(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
				}else {
					mCallback.signup(username, password);
				}
			}
		});
		
		return v;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallback = (Callback) activity;
	}
	@Override
	public void onDetach() {
		super.onDetach();
		mCallback = null;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode== REQUEST_PHOTO){
			String filename = data.getStringExtra(CameraProfileFragment.EXTRA_PHOTO_FILENAME);
			if(filename !=null || filename!=""){
				 path = getActivity().getFileStreamPath(filename).getAbsolutePath();
				BitmapDrawable b = PictureUtils.getScaledDrawable(getActivity(), path);
				imageIV.setImageDrawable(b);
			}
			Toast.makeText(getActivity(), data.getStringExtra(CameraProfileFragment.EXTRA_PHOTO_FILENAME), Toast.LENGTH_SHORT).show();
		}
	}
}
