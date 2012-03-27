package com.android.movingServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Client extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);			
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
		
	public void startHelpAction(View v){
		Intent i = new Intent(this, Help.class);
		startActivity(i);
	}
}
