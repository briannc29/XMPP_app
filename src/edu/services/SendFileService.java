package edu.services;

import java.io.File;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SendFileService extends IntentService{
	public static final String TAG = "edu.services.SendFileService";
	private static AbstractXMPPConnection connection;
	private static String toUser;
	private static String path;
	private static Uri mUri;
	public SendFileService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		FileTransferManager manager = FileTransferManager.getInstanceFor(connection);
//		OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer("spark@proj-309-09/Spark 2.6.3");
		OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(toUser);
		try {
			transfer.sendFile(new File(mUri.getPath()), "Top secret!!!");
		} catch (SmackException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendFile(Context appContext, AbstractXMPPConnection conn, Uri uri, String user){
		mUri = uri;
		connection = conn;
		toUser = user+"/HERMES";
		Intent i = new Intent(appContext, SendFileService.class);
		appContext.startService(i);
	}
}
