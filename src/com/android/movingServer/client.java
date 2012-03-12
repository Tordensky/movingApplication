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
import android.widget.Button;
import android.widget.Toast;

public class Client extends Activity {

	private HttpMovingClient httpService;
	
	private ResponseReceiver receiver;
	
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setupMainMenu();
		
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
		
        setupSharedPreferences();
        
		//startHttpService();
		
		//doBindService();
	}
	
	private void setupMainMenu(){
		Button gotoBoxMenuButton = (Button) findViewById(R.id.gotoBoxMenuButton);
		Button gotoSearchMenuButton = (Button) findViewById(R.id.gotoSearchMenuButton);
		
		gotoBoxMenuButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startBoxListAction();
			}
		});
		
		gotoSearchMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startSearchAction();
			}
		});
	}
	
	private void setupSharedPreferences(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		if (settings.getLong("lastConnectionTimeStamp", 0) == 0){
			editor.putLong("lastConnectionTimeStamp", 0);
			editor.commit();
		}
	}
	
	
	private void startBoxListAction(){
		Intent i = new Intent(this, MovingApplicationActivity.class);
		startActivity(i);
	}
	
	private void startSearchAction(){
		Intent i = new Intent(this, Search.class);
		startActivity(i);
	}
	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
		stopService(new Intent(this, HttpMovingClient.class));
		super.onDestroy();
	}
	
	private void startHttpService() {
		Intent i = new Intent(this, HttpMovingClient.class);
		startService(i);
	}
	
	private void print_msg(String message, int duration){
		Toast.makeText(this, message, duration).show();
		//Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		//v.vibrate(100);
	}
	
	public class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP =
		      "com.mamlambo.intent.action.MESSAGE_PROCESSED";

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			print_msg("DETTE FUNKA FADERMEG", 1000);
		}	
	}
	
	

/*	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}*/

}
