package edu.services;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import edu.hermes.AddFriendFragment;
import edu.hermes.HermesMessage;
import edu.hermes.UserRegister;

public class MainService extends HandlerThread{
	private static final String TAG ="MainService";
	
	public static final int MESSAGE_LOGIN =0;
	public static final int MESSAGE_REGISTER=1;
	private static final int MESSAGE_SEND =2;
	private static MainService manager ;
	HermesService hermesService;
	Context mAppContext;
	AbstractXMPPConnection connection;
	
	/**************************************************** Constructor*****************************************************/
	private MainService(Context context) {
		super(TAG);
		this.mAppContext = context;
		hermesService = HermesService.getService(mAppContext);
	}
	
	public static MainService getInstance(Context appContext){
		if(manager ==null){
			manager = new MainService(appContext);
		}
		return manager;
	}
	/**************************************************** LoginHandler*****************************************************/
	Handler mLoginHandler;
	LoginCallback loginCallback;
	public interface LoginCallback{
		void onLoginSuccessfull();
	}
	public void setLoginListener(LoginCallback loginCallback){
		this.loginCallback = loginCallback;
	};
	
	/**************************************************** DataHandler*****************************************************/
	Handler mDataHandler;
	DataCallback dataCallback;
	public interface DataCallback{
		void onReceiveData(Message message);
		void onSendData();
	}
	public void setDataCallback(DataCallback dataCallback){
		this.dataCallback = dataCallback;
	}
	/*****************************************************MessageHandler*********************************************************/
	
	Handler mMessageHandler;
	
	/**************************************************** RegisterHandler*****************************************************/
	Handler mRegisterHandler;
	RegisterCallback registerCallback;
	public interface RegisterCallback{
		void onRegisterSuccessfull();
		void onRegisterFailed();
	}
	public void setRegisterListener (RegisterCallback callback){
		this.registerCallback = callback;
	}
	
	
	
	
	@Override
	protected void onLooperPrepared() {
		mLoginHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				UserRegister user = (UserRegister) msg.obj;
				if(connection ==null){
					connection = hermesService.getConnection();
				}
				boolean isLogin =hermesService.login( user.getUsername(), user.getPassword());
				if(isLogin){
					GetFriendListService.getFriendList(connection,mAppContext );
					loginCallback.onLoginSuccessfull();
				}
			};
		};
		
		mDataHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
			};
		};
		mMessageHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				HermesMessage message = (HermesMessage) msg.obj;
				ChatManager chatMG = ChatManager.getInstanceFor(connection);
				Chat newChat = chatMG.createChat(message.getTo()+"@proj-309-09", new ChatMessageListener() {
					
					@Override
					public void processMessage(Chat arg0, org.jivesoftware.smack.packet.Message arg1) {
						
					}
				});
				try {
					newChat.sendMessage(message.getBody());
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};
		mRegisterHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				UserRegister user = (UserRegister) msg.obj;
				connection =hermesService.getConnection();
				boolean isRegisterd = hermesService.signup( user.getUsername(), user.getPassword());
				if(isRegisterd){
					registerCallback.onRegisterSuccessfull();
				}else{
					registerCallback.onRegisterFailed();
				}
			};
		};
		connection = hermesService.getConnection();
	}
	
	public void login(String username, String password){
		UserRegister user = new UserRegister(username, password);
		mLoginHandler.obtainMessage(MESSAGE_LOGIN, user).sendToTarget();
	}
	
	public void register(String username,String password){
		UserRegister user = new UserRegister(username, password);
		mRegisterHandler.obtainMessage(MESSAGE_REGISTER, user).sendToTarget();
	}
	


	public void sendMessage(String messageTo, String messageBody) {
		HermesMessage message = new HermesMessage(messageTo, messageBody);
		mMessageHandler.obtainMessage(MESSAGE_SEND, message).sendToTarget();
	}
	public void addFriend(String newFriendName, String nickname ){
		AddFriendService.addFriend(mAppContext, connection, newFriendName, nickname);
	}
	
	public void sendFile(Uri uri, String toUser){
		SendFileService.sendFile(mAppContext, connection, uri, toUser);
	}
}
