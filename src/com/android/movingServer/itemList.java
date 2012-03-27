package com.android.movingServer;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * The Class itemList.
 */
public class itemList extends ListActivity{


	private static final int ACTIVITY_CREATE_ITEM = 0;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	private long CurrentBoxID;
	
	TextView boxName;
	TextView boxDescription;
	TextView boxLocation;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.items_list);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			CurrentBoxID = extras.getLong(MovingDbAdapter.KEY_BOX_ID);		
		}
		
		mDbHelper = new MovingDbAdapter(this);
        mDbHelper.open();
        
        boxName = (TextView)findViewById(R.id.boxName); 
        boxDescription = (TextView)findViewById(R.id.boxDescription);
        boxLocation = (TextView)findViewById(R.id.boxLocation);
                    
		fillData();
	
		registerForContextMenu(getListView());
	}
	
	
	/**
	 * Fill data.
	 */
	private void fillData(){
		mMovingCursor = mDbHelper.getBoxFromID(CurrentBoxID);
		mMovingCursor.moveToFirst();
		
		boxName.setText(mMovingCursor.getString(1));
		boxDescription.setText(mMovingCursor.getString(2));
		
		try {
			mMovingCursor = mDbHelper.getLocationFromID(mMovingCursor.getLong(3));
			mMovingCursor.moveToFirst();
			boxLocation.setText(mMovingCursor.getString(0));
		} catch (Exception e) {
			boxLocation.setText("No Location");
		}
		
    	mMovingCursor = mDbHelper.fetchAllItemsFromBox(CurrentBoxID);
    	
    	startManagingCursor(mMovingCursor);
    	
    	String[] from = new String[]{MovingDbAdapter.KEY_ITEM_NAME, MovingDbAdapter.KEY_ITEM_DESC};
    	
    	int[] to = new int[]{R.id.itemName, R.id.itemDescription};
    	
    	SimpleCursorAdapter items =
    		new SimpleCursorAdapter(this, R.layout.items_row, mMovingCursor, from, to);
    	setListAdapter(items);
	}
		

	/**
	 * Creates the item.
	 */
	public void createItem(View v) {
		//mDbHelper.createItem(CurrentBoxID, "Korn", "Diverse småsaker");
		Intent i = new Intent(this, ItemEdit.class);
		i.putExtra(MovingDbAdapter.KEY_BOX_ID, CurrentBoxID);
    	startActivityForResult(i, ACTIVITY_CREATE_ITEM);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
	    Intent mIntent = new Intent();
	    setResult(RESULT_OK, mIntent);
	    finish();
	    
	    super.onBackPressed();
	}

	/* 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.itemListMenuDelete);
	}

	/* 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		Toast.makeText(this, "Fill data", 1000).show();
		
		switch (item.getItemId()){
		
		case DELETE_ID:
			mDbHelper.deleteItem(info.id);
			fillData();
			return true;
		}		
		
		return super.onContextItemSelected(item);
	}
	
}
