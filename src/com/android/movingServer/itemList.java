package com.android.movingServer;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class itemList extends ListActivity{

	private static final int ACTIVITY_CREATE_ITEM = 0;
	
	private static final int INSERT_ID = Menu.FIRST;
	
	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	
	private long CurrentBoxID;
	
	TextView boxName;
	TextView boxDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.items_list);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			CurrentBoxID = extras.getLong(MovingDbAdapter.KEY_BOX_ID);
		}
		
		mDbHelper = new MovingDbAdapter(this);
        mDbHelper.open();
        
        boxName = (TextView)findViewById(R.id.itemListBoxName); 
        boxDescription = (TextView)findViewById(R.id.itemListBoxDescription);
        
		fillData();
		
		registerForContextMenu(getListView());
	}
	
	
	private void fillData(){
		mMovingCursor = mDbHelper.getBoxFromID(CurrentBoxID);
		mMovingCursor.moveToFirst();
		
		boxName.setText(mMovingCursor.getString(1));
		boxDescription.setText(mMovingCursor.getString(2));
		
    	mMovingCursor = mDbHelper.fetchAllItemsFromBox(CurrentBoxID);
    	
    	startManagingCursor(mMovingCursor);
    	
    	String[] from = new String[]{MovingDbAdapter.KEY_ITEM_NAME, MovingDbAdapter.KEY_ITEM_DESC};
    	
    	int[] to = new int[]{R.id.itemName, R.id.itemDescription};
    	
    	SimpleCursorAdapter items =
    		new SimpleCursorAdapter(this, R.layout.items_row, mMovingCursor, from, to);
    	setListAdapter(items);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.addItemMenu);
        return true;
    }
    
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()){
		case INSERT_ID:
			
	        createItem();
	        fillData();
			return true;
    	}
    	
    	return super.onContextItemSelected(item);
	}
	
	private void createItem() {
		//mDbHelper.createItem(CurrentBoxID, "Korn", "Diverse småsaker");
		Intent i = new Intent(this, ItemEdit.class);
		i.putExtra(MovingDbAdapter.KEY_BOX_ID, CurrentBoxID);
    	startActivityForResult(i, ACTIVITY_CREATE_ITEM);
	}
	
	@Override
	public void onBackPressed() {
	    Intent mIntent = new Intent();
	    setResult(RESULT_OK, mIntent);
	    super.onBackPressed();
	}
}
