package com.android.movingServer;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class Search extends ListActivity {

	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_list);
		fillData();
	}
	
	private void fillData(){
    	mMovingCursor = mDbHelper.fetchAllItemsFromBox((long)10);
    	
    	startManagingCursor(mMovingCursor);
    	
    	String[] from = new String[]{MovingDbAdapter.KEY_ITEM_NAME, MovingDbAdapter.KEY_ITEM_DESC};
    	
    	int[] to = new int[]{R.id.itemName, R.id.itemDescription};
    	
    	SimpleCursorAdapter items =
    		new SimpleCursorAdapter(this, R.layout.items_row, mMovingCursor, from, to);
    	setListAdapter(items);
	}

}
