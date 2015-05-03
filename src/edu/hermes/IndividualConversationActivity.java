package edu.hermes;

import android.support.v4.app.Fragment;

public class IndividualConversationActivity extends TemplateActivity {

	@Override
	protected Fragment createFragment() {
		return new IndividualConversationFragment();
	}

}
