package edu.hermes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HermesPagerAdapter extends FragmentPagerAdapter{

	public HermesPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			return new ConversationFragment();
		case 1:
			return new ContactFragment();
		case 2:
			return new SettingsFragment();
		default:
			return new ConversationFragment();
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

}
