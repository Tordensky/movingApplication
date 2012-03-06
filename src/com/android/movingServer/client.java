package com.android.movingServer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

public class Client extends Activity {

	private HttpMovingClient httpService;
	
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
		
		doBindService();
	}
	
	private void startBoxListAction(){
		Intent i = new Intent(this, MovingApplicationActivity.class);
		startActivity(i);
	}
	
	private void startSearchAction(){
		Intent i = new Intent(this, Search.class);
		startActivity(i);
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			httpService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			httpService = ((HttpMovingClient.MyBinder) binder).getService();
		}
	};
	
	void doBindService(){
		bindService(new Intent(this, HttpMovingClient.class), mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(Client.this,HttpMovingClient.class));

	}

}
