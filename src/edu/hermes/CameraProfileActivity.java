package edu.hermes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CameraProfileActivity extends TemplateActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}
	@Override
	protected Fragment createFragment() {
		return new CameraProfileFragment();
	}

}
