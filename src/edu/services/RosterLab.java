package edu.services;

import java.util.ArrayList;

import edu.hermes.BuddyInfo;
import android.content.Context;

public class RosterLab {
	
	private Context mAppContext;
	private static RosterLab sRosterLab;
	private ArrayList<BuddyInfo> mBuddyList;
	private RosterLab(Context c) {
		this.mAppContext= c;
		if(mBuddyList==null){
			
			mBuddyList = new ArrayList<BuddyInfo>();
		}
	}
	public static RosterLab getInstance(Context context){
		if(sRosterLab ==null){
			sRosterLab = new RosterLab(context.getApplicationContext());
		}
		return sRosterLab;
	}
	public ArrayList<BuddyInfo> getRosterList(){
		return mBuddyList;
	}
	
	public void  updateRosterList(ArrayList<BuddyInfo> buddyList){
		this.mBuddyList = buddyList;
	}
	
	public void addRoster(BuddyInfo newBuddy){
		for (BuddyInfo buddyInfo : mBuddyList) {
			if(buddyInfo.getUsername().equals(newBuddy.getUsername())){
				buddyInfo.setOnline(newBuddy.isOnline());
				buddyInfo.setStatus(newBuddy.getStatus());
				return;
			}	
		}
		mBuddyList.add(newBuddy);
	}
}
