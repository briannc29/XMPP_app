package edu.hermes;

import android.support.v4.app.Fragment;

public class DemoActivity extends TemplateActivity {

	@Override
	protected Fragment createFragment() {
		return new IndividualConversationFragment();
	}

}
