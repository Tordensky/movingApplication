package com.android.movingServer;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class LocationList extends ListActivity {

	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.location_list);
		mDbHelper = new MovingDbAdapter(this);
		setupMenu();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMovingCursor.close();
		mDbHelper.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDbHelper.open();
		fillData();
	}
	
	private void fillData(){
		mMovingCursor = mDbHelper.fetchAllLocations();
		startManagingCursor(mMovingCursor);
		
		String[] from = new String[]{MovingDbAdapter.KEY_LOCATION_NAME, MovingDbAdapter.KEY_LOCATION_DESC};
		
		int[] to = new int[]{R.id.locationName, R.id.locationDescription};
		
		SimpleCursorAdapter items = 
			new SimpleCursorAdapter(this, R.layout.location_row, mMovingCursor, from, to);
		setListAdapter(items);
	}
	
	private void setupMenu(){
		Button newButton = (Button) findViewById(R.id.new_button);
		Button searchButton = (Button) findViewById(R.id.search_button);
		Button tagButton = (Button) findViewById(R.id.tag_button);
		Button refreshButton = (Button) findViewById(R.id.refresh_button);
		
		newButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newLocation();				
			}
		});
		
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchLocation();				
			}
		});
		
		tagButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchFromTag();				
			}
		});
		
		refreshButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh();				
			}
		});
	}
	
	private void newLocation(){
		Toast.makeText(this, "New Location", 500).show();
		Intent i = new Intent(this, CreateLocation.class);
		startActivity(i);
	}
	
	private void searchLocation() {
		Toast.makeText(this, "search Location", 500).show();	
	}
	
	private void searchFromTag(){
		Toast.makeText(this, "Tag Location", 500).show();
	}
	
	private void refresh(){
		Toast.makeText(this, "Refresh Location", 500).show();
	}
	
}
