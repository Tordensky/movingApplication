package com.android.movingServer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class Client extends Activity {
	
	/** Shared prefs filename */
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);			
        setupSharedPreferences();
        
        // Sync with Server
		startHttpService();
		
	}
		
	private void setupSharedPreferences(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		if (settings.getLong("lastConnectionTimeStamp", 0) == 0){
			editor.putLong("lastConnectionTimeStamp", 0);
			editor.commit();
		}
	}
	
	public void startBoxListAction(View v){
		Intent i = new Intent(this, MovingApplicationActivity.class);
		startActivity(i);
	}
	
	public void startSearchAction(View v){
		Intent i = new Intent(this, Search.class);
		startActivity(i);
	}
	
	public void startLocationAction(View v){
		Intent i =  new Intent(this, LocationList.class);
		startActivity(i);
	}
	
	private void startHttpService() {
		Intent i = new Intent(this, HttpMovingClient.class);
		startService(i);
	}	
	
	public void startHelpAction(View v){
		Intent i = new Intent(this, Help.class);
		startActivity(i);
	}
	
	public class ResponseReceiver extends BroadcastReceiver {
		
		/** The Constant ACTION_RESP. */
		public static final String ACTION_RESP =
		      "com.mamlambo.intent.action.MESSAGE_PROCESSED";

		@Override
		public void onReceive(Context arg0, Intent arg1) {

		}	
	}
}
