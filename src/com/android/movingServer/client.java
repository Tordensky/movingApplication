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

// TODO: Auto-generated Javadoc
/**
 * The Class Client.
 */
public class Client extends Activity {

	/** The http service. */
	private HttpMovingClient httpService;
	
	/** The receiver. */
//	private ResponseReceiver receiver;
	
	/** The Constant PREFS_NAME. */
	public static final String PREFS_NAME = "MyPrefsFile";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);	
		setupMainMenu();
		
/*        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter); 
*/		
        setupSharedPreferences(); 
		startHttpService();
		
	}
	
	/**
	 * Setup main menu.
	 */
	private void setupMainMenu(){
		Button gotoBoxMenuButton = (Button) findViewById(R.id.gotoBoxMenuButton);
		Button gotoSearchMenuButton = (Button) findViewById(R.id.gotoSearchMenuButton);
		Button gotoLocationMenuButton = (Button) findViewById(R.id.gotoLocationsMenuButton);
		
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
		
		gotoLocationMenuButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startLocationAction();
				
			}
		});
	}
	
	/**
	 * Setup shared preferences.
	 */
	private void setupSharedPreferences(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		if (settings.getLong("lastConnectionTimeStamp", 0) == 0){
			editor.putLong("lastConnectionTimeStamp", 0);
			editor.commit();
		}
	}
	
	
	/**
	 * Start box list action.
	 */
	private void startBoxListAction(){
		Intent i = new Intent(this, MovingApplicationActivity.class);
		startActivity(i);
	}
	
	/**
	 * Start search action.
	 */
	private void startSearchAction(){
		Intent i = new Intent(this, Search.class);
		startActivity(i);
	}
	
	/**
	 * Start location action.
	 */
	private void startLocationAction(){
		Intent i =  new Intent(this, LocationList.class);
		startActivity(i);
	}
	
		
	/**
	 * Start http service.
	 */
	private void startHttpService() {
		Intent i = new Intent(this, HttpMovingClient.class);
		startService(i);
	}	
	
	/**
	 * The Class ResponseReceiver.
	 */
	public class ResponseReceiver extends BroadcastReceiver {
		
		/** The Constant ACTION_RESP. */
		public static final String ACTION_RESP =
		      "com.mamlambo.intent.action.MESSAGE_PROCESSED";

		/* (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context arg0, Intent arg1) {

		}	
	}
}
