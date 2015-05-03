package edu.services;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class HermesService {
	private static final String TAG = "HermesService";
	public static final String ACTION_NEW_MESSAGE = "edu.hermes.HermesService.NEW_MESSAGE";
	public static final String ACTION_NEW_FILE ="edu.hermes.HermesService.NEW_FILE";
	public static final int REQUEST_CODE_NEW_MESSAGE =0;
	private static Context mAppContext;
	private static HermesService hermesService;
	private static AbstractXMPPConnection connection;
	private HermesService(Context mAppC) {
		HermesService.mAppContext = mAppC;
	}
	
	public static HermesService getService(Context mAppC){
		if(hermesService ==null){
			hermesService = new HermesService(mAppC);
		}
		return hermesService;
	}
	public  boolean login(String username, String password){
			try {
				if(connection.isAuthenticated()){
					connection.disconnect();
					connection = getConnection();
				}
				
				connection.login(username, password); 
				Log.i(TAG, "log in successfully!");
				setFileTransListener();
				return true;
			} catch (XMPPException | SmackException | IOException e) {
				e.printStackTrace();
				Log.e(TAG, "login failed!");
				return false;
			}
	}
	
	public AbstractXMPPConnection getConnection( ){
		XMPPTCPConnectionConfiguration.Builder conBuilder = XMPPTCPConnectionConfiguration.builder();
//		SASLAuthentication.blacklistSASLMechanism(SASLMechanism.DIGESTMD5);
		conBuilder.setSecurityMode(SecurityMode.disabled);
		conBuilder.setServiceName("proj-309-09");
		conBuilder.setResource("HERMES");
		conBuilder.setHost("proj-309-09.cs.iastate.edu");
		 HermesService.connection = new XMPPTCPConnection(conBuilder.build());
		
		try {
			connection.connect();
			setMessageListener();
		Log.i(TAG, "connected successfull! Congrats!");
		} catch (SmackException | IOException | XMPPException e) { 
			e.printStackTrace();
			Log.e(TAG, "failed to connect to server"); 
		}
		return connection;
	}
	public  boolean signup(String username, String password){
		try {
			AccountManager accountManager = AccountManager.getInstance(connection);
			accountManager.sensitiveOperationOverInsecureConnection(true);
			accountManager.createAccount(username, password);
			Log.i(TAG, "account is created!");
			return true;
		} catch (XMPPException | SmackException e1) {
			Log.e(TAG, "failed to create account!"); 
			e1.printStackTrace();
			return false;
		}
	}
	private  void setMessageListener (){
		StanzaFilter filter = new StanzaTypeFilter(Message.class);
//		PacketCollector myCollector = connection.createPacketCollector(filter);
		StanzaListener myListener = new StanzaListener() {
			@Override
			public void processPacket(Stanza stanza) throws NotConnectedException {
					Intent i = new Intent(ACTION_NEW_MESSAGE);
					i.putExtra("REQUEST_CODE",REQUEST_CODE_NEW_MESSAGE);
					Message message = (Message) stanza;
					i.putExtra("from", message.getFrom());
					i.putExtra("body", message.getBody());
					i.putExtra("to", message.getTo());
					mAppContext.sendBroadcast(i);
					
			}
		};
		
		connection.addAsyncStanzaListener(myListener, filter);
	}
	
	private void setFileTransListener (){
		final FileTransferManager manager = FileTransferManager.getInstanceFor(connection);
		manager.addFileTransferListener(new FileTransferListener() {
			
			@Override
			public void fileTransferRequest(FileTransferRequest arg0) {
				IncomingFileTransfer transfer = arg0.accept();
				File file = new File(Environment.getExternalStorageDirectory()+"/Download",transfer.getFileName());
				Uri uri = Uri.fromFile(file);
				try {
					transfer.recieveFile(file);
					while(!transfer.isDone()){};
					Intent i = new Intent(ACTION_NEW_FILE);
					i.putExtra("uri", uri.toString());
					i.putExtra("user", arg0.getRequestor().toString());
					mAppContext.sendBroadcast(i);
				} catch (SmackException | IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
