package edu.hermes;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import edu.services.GetFriendListService;
import edu.services.MainService;
import edu.services.MainService.LoginCallback;
import edu.services.MainService.RegisterCallback;

public class MainActivity extends FragmentActivity implements LoginFragment.Callbacks,RegistrationFragment.Callback,CongratFragment.Callback{
	public static final String TAG ="HERMES_MainActivity";
	private MainService mServiceThread;
	private static FragmentManager mFragMger;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mFragMger = getSupportFragmentManager(); 
		setContentView(R.layout.activity_main);
        initService();
        initLoginFragment();
        
	}
	
	private void initLoginFragment(){
		
		Fragment loginFragment = mFragMger.findFragmentById(R.id.login_frame_container);
        if(loginFragment ==null){
        	loginFragment = new LoginFragment();
        	mFragMger.beginTransaction().add(R.id.login_frame_container, loginFragment).commit();
        }
	}
	private void initService(){ 
		mServiceThread =MainService.getInstance(getApplicationContext());
		
		mServiceThread.setLoginListener(new LoginCallback() {
			@Override
			public void onLoginSuccessfull() {
				removeAllFragments();
				mFragMger.beginTransaction().add(R.id.fragmentContainer, new MainFragment()).commit();
			}
		});
		mServiceThread.setRegisterListener(new RegisterCallback() {
			
			@Override
			public void onRegisterSuccessfull() {
				removeAllFragments();
				if(mFragMger==null){
					mFragMger = getSupportFragmentManager();
				}
				mFragMger.beginTransaction().add(R.id.congrat_frame_container, new CongratFragment()).commit();
			}
			
			@Override
			public void onRegisterFailed() {
				
			}
		});
			mServiceThread.start();
			mServiceThread.getLooper();
	};
	@Override
	public void login(String username, String password) {
		mServiceThread.login(username, password);
	}

	@Override
	public void switchToRegister() {
		removeAllFragments();
		mFragMger.beginTransaction().add(R.id.register_frame_container, new RegistrationFragment()).commit();
	}

	@Override
	public void signup(String username, String password) {
		mServiceThread.register(username, password);
	}

	@Override
	public void getLoginFragment() {
		removeAllFragments();
		mFragMger.beginTransaction().add(R.id.login_frame_container, new LoginFragment()).commit();
	}
	
	private void removeAllFragments(){
		if(mFragMger==null){
			mFragMger = getSupportFragmentManager();
		}
		List<Fragment> fragments = mFragMger.getFragments();
		if(fragments !=null){
			for (Fragment fragment : fragments) {
				if(fragment!=null){
					mFragMger.beginTransaction().remove(fragment).commit();
				}
			}
		}
	}
	@Override
	protected void onDestroy() {
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
		mServiceThread.quit();
		mServiceThread = null;
	}
	
}
