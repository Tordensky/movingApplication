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

// TODO: Auto-generated Javadoc
/**
 * The Class LocationList.
 */
public class LocationList extends ListActivity {

	/** The m db helper. */
	private MovingDbAdapter mDbHelper;
	
	/** The m moving cursor. */
	private Cursor mMovingCursor;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.location_list);
		mDbHelper = new MovingDbAdapter(this);
		setupMenu();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMovingCursor.close();
		mDbHelper.close();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mDbHelper.open();
		fillData();
	}
	
	/**
	 * Fill data.
	 */
	private void fillData(){
		mMovingCursor = mDbHelper.fetchAllLocations();
		startManagingCursor(mMovingCursor);
		
		String[] from = new String[]{MovingDbAdapter.KEY_LOCATION_NAME, MovingDbAdapter.KEY_LOCATION_DESC};
		
		int[] to = new int[]{R.id.locationName, R.id.locationDescription};
		
		SimpleCursorAdapter items = 
			new SimpleCursorAdapter(this, R.layout.location_row, mMovingCursor, from, to);
		setListAdapter(items);
	}
	
	/**
	 * Setup menu.
	 */
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
	
	/**
	 * New location.
	 */
	private void newLocation(){
		Toast.makeText(this, "New Location", 500).show();
		Intent i = new Intent(this, CreateLocation.class);
		startActivity(i);
	}
	
	/**
	 * Search location.
	 */
	private void searchLocation() {
		Toast.makeText(this, "search Location", 500).show();	
	}
	
	/**
	 * Search from tag.
	 */
	private void searchFromTag(){
		Toast.makeText(this, "Tag Location", 500).show();
	}
	
	/**
	 * Refresh.
	 */
	private void refresh(){
		Toast.makeText(this, "Refresh Location", 500).show();
	}
	
}
