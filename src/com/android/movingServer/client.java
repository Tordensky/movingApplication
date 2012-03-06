package com.android.movingServer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Client extends Activity {

	private HttpMovingClient httpService;
	
	private ResponseReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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
		
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
		
		startHttpService();
		//doBindService();
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(Client.this,HttpMovingClient.class));

	}
	
	private void startHttpService() {
		Intent i = new Intent(this, HttpMovingClient.class);
		startService(i);
	}
	
	private void print_msg(String message, int duration){
		Toast.makeText(this, message, duration).show();
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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

}
