package edu.hermes;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment{
	
	ViewPager mViewPager;
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setHasOptionsMenu(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main,container,false);
		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mViewPager = (ViewPager) v.findViewById(R.id.main_pager);
		mViewPager.setAdapter(new HermesPagerAdapter(getActivity().getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				actionBar.setSelectedNavigationItem(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				//hide tab
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				//show tab
				mViewPager.setCurrentItem(tab.getPosition());
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				
			}
		};
		
		actionBar.addTab(actionBar.newTab().setText("Conversation").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Contact").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Settings").setTabListener(tabListener));
		return v;
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_friend:
			Intent i = new Intent(getActivity(), AddFriendActivity.class);
			getActivity().startActivityForResult(i, 0);
			
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
