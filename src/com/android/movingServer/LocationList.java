package com.android.movingServer;

import com.android.movingServer.MovingApplicationActivity.listViewBinder;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
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
		
		String[] from = new String[]{MovingDbAdapter.KEY_LOCATION_NAME, MovingDbAdapter.KEY_LOCATION_DESC, MovingDbAdapter.KEY_LOCATION_COLOR};
		
		int[] to = new int[]{R.id.locationName, R.id.locationDescription, R.id.locationImage};
		
		SimpleCursorAdapter items = 
			new SimpleCursorAdapter(this, R.layout.location_row, mMovingCursor, from, to);
		items.setViewBinder(new listViewBinder());
		setListAdapter(items);
	}
	
	public class listViewBinder implements SimpleCursorAdapter.ViewBinder {
		 
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		    int viewId = view.getId();
		    TextView mView;
		    
		    switch (viewId) {
		    
		    case R.id.locationName:
		        mView = (TextView) view;
		        mView.setText(cursor.getString(columnIndex));
		        break;
		        
		    case R.id.locationDescription:
		        mView = (TextView) view;
		        mView.setText(cursor.getString(columnIndex));
		        break;
		 
		    case R.id.locationImage:
		    	//long color = mDbHelper.getLocationColor(cursor.getLong(columnIndex));
		        // the icon
		        ImageView mIconView = (ImageView) view;//findViewById(R.id.boxImage);//
		        
		        int color = cursor.getInt(columnIndex);
		        switch (color){
		        
		        case 0:
		            mIconView.setImageResource(R.drawable.house_blue);
		            break;
		        case 1:
		            mIconView.setImageResource(R.drawable.house_green);
		            break;	            
		        case 2:
		            mIconView.setImageResource(R.drawable.house_orange);
		            break;   
		        case 3:
		        	mIconView.setImageResource(R.drawable.house_purple);
		            break;
		        case 4:
		        	mIconView.setImageResource(R.drawable.house_yellow);
		            break;
		        case 5:
		        	mIconView.setImageResource(R.drawable.house_purple);
		            break;
		        default:
		            mIconView.setImageResource(R.drawable.house_orange);
		            break;
		        }
		    }
		    return true;
		}
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
