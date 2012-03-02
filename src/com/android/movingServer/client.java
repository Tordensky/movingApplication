package com.android.movingServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Client extends Activity {

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
	}
	
	private void startBoxListAction(){
		Intent i = new Intent(this, MovingApplicationActivity.class);
		startActivity(i);
	}
	
	private void startSearchAction(){
		Intent i = new Intent(this, Search.class);
		startActivity(i);
	}

}
