package edu.hermes;

import android.support.v4.app.Fragment;

public class AddFriendActivity extends TemplateActivity {

	@Override
	protected Fragment createFragment() {
		return new AddFriendFragment(); 
	}

}
