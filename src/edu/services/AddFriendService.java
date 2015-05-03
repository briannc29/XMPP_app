package edu.services;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.roster.Roster;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class AddFriendService extends IntentService{
	public static final String TAG = "edu.services.AddFriendService";
	private static AbstractXMPPConnection connection;
	private static String username;
	private static String nickname;
	public AddFriendService() { 
		super(TAG);
	} 

	@Override
	protected void onHandleIntent(Intent intent) {
		Roster roster = Roster.getInstanceFor(connection);
		
		try {
			Thread.sleep(500);
			roster.createEntry(username+"@proj-309-09", nickname, null);
			
		} catch (NotLoggedInException | NoResponseException
				| XMPPErrorException | NotConnectedException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void addFriend(Context context, AbstractXMPPConnection conn, String user,String nick){
		connection = conn;
		username = user;
		nickname = nick;
		Intent i = new Intent(context, AddFriendService.class);
		context.startService(i);
	}

}
