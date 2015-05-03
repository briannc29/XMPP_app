package edu.services;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import edu.hermes.BuddyInfo;
import edu.hermes.Contact;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GetFriendListService extends IntentService{
	public static final String TAG ="edu.services.GetFriendListService";
	public static final String ACTION_NEW_FRIENDLIST = "edu.services.GetFriendListService.NEW_FRIEND";
	public static final String ACTION_PRESENCE_CHANGE = "edu.services.GetFriendListServies.PRESENCE_CHANGE";
	public static final String ACTION_PRESENCE_ADDED = "edu.services.GetFriendListServies.PRESENCE_ADDED";
	private static AbstractXMPPConnection connection;
	private static Context mAppContext;
	public GetFriendListService() {
		super(TAG);
	}
	Roster roster;
	@Override
	protected void onHandleIntent(Intent intent) {
		 roster = Roster.getInstanceFor(connection);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Collection<RosterEntry> entries = roster.getEntries();
		ArrayList<BuddyInfo> friendList = new ArrayList<BuddyInfo>();
		for(RosterEntry entry: entries){
			String username = entry.getUser(); 
			String nickName = entry.getName();
			boolean isOnline = roster.getPresence(username) != null;
			String subscribed = entry.getType().name();
			String status   = roster.getPresence(username).getStatus();
			friendList.add(new BuddyInfo(username,nickName,isOnline,status,subscribed));
		}
		Log.i("GET FRIEND",friendList.toString());
		RosterLab.getInstance(mAppContext).updateRosterList(friendList);
		Intent i = new Intent(ACTION_NEW_FRIENDLIST);
		sendBroadcast(i);
		roster.addRosterListener(new RosterListener() {
			
			@Override
			public void presenceChanged(Presence presence) {
				Log.i("CHANGED", "From "+presence.getFrom()+" --To "+presence.getTo()+" --Status "+presence.getStatus()+" --Type "+presence.getType());
				RosterLab.getInstance(mAppContext).addRoster(new BuddyInfo(presence.getFrom(), null, presence.getType().name().equals("available"), presence.getStatus(), null));
				Intent i = new Intent(ACTION_PRESENCE_CHANGE);
				sendBroadcast(i);
			}
			
			@Override
			public void entriesUpdated(Collection<String> presence) {
				for (String info : presence) {
					Log.i("UPDATED", info); 
				}
				Intent i = new Intent(ACTION_PRESENCE_CHANGE);
				sendBroadcast(i);
			}
			
			@Override
			public void entriesDeleted(Collection<String> arg0) {
				for (String info : arg0) {
					Log.i("DELETED", info);
				}
				Intent i = new Intent(ACTION_PRESENCE_CHANGE);
				sendBroadcast(i);
			}
			
			@Override
			public void entriesAdded(Collection<String> arg0) {
				
				for (String info : arg0) { 
					Log.i("ADDED", info);
					Intent i = new Intent(ACTION_PRESENCE_ADDED); 
					sendBroadcast(i);
				}
				Intent i = new Intent(ACTION_PRESENCE_CHANGE);
				sendBroadcast(i);
			}
		});
	}
	
	public static void getFriendList(AbstractXMPPConnection conn, Context appContext){
		connection = conn;
		mAppContext = appContext;
		Intent i = new Intent(mAppContext, GetFriendListService.class);
		mAppContext.startService(i);
	}
}
