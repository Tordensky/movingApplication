package com.android.movingServer;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class Search.
 */
public class Search extends ListActivity {


	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	private EditText itemSearchField;
	private String itemSearchString;
	private static final int DELETE_ID = Menu.FIRST;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.box_list);
		
		mDbHelper = new MovingDbAdapter(this);
        
        findViewById(R.id.boxListViewSearchBox).setVisibility(View.VISIBLE);
        itemSearchField =(EditText) findViewById(R.id.searchBoxesInputField);
        findViewById(R.id.boxListBottomMenu).setVisibility(View.GONE);
        
        
        itemSearchField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				itemSearchString = itemSearchField.getText().toString();
				fillData();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
        
		fillData();
		
		registerForContextMenu(getListView());
	}
	
	/**
	 * Fill data.
	 */
	private void fillData(){
		
		mDbHelper.open();
		mMovingCursor = mDbHelper.fetchAllITemsFromBoxesWhere(itemSearchString);
    	
    	startManagingCursor(mMovingCursor);
    	
    	String[] from = new String[]{MovingDbAdapter.KEY_ITEM_NAME , MovingDbAdapter.KEY_ITEM_DESC, MovingDbAdapter.KEY_BOX_NAME};
    	
    	int[] to = new int[]{R.id.itemName, R.id.itemDescription, R.id.itemBox};
    	
    	SimpleCursorAdapter items =
    		new SimpleCursorAdapter(this, R.layout.search_items_row, mMovingCursor, from, to);
    	setListAdapter(items);
    	mDbHelper.close();
	}
	
	 /* (non-Javadoc)
 	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
 	 */
 	protected void onListItemClick(ListView l, View v, int position, long id){
	    	super.onListItemClick(l, v, position, id);
	    	mDbHelper.open();
	    	gotoBox(mDbHelper.boxIdFromItemId(id));
	    }
	 
	 /**
 	 * Goto box.
 	 *
 	 * @param BID the bID
 	 */
 	private void gotoBox(long BID){
	    	Intent i = new Intent(this, itemList.class);
	    	i.putExtra(MovingDbAdapter.KEY_BOX_ID, BID);
	    	mDbHelper.close();
	    	startActivity(i);
	    }
	 
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.itemListMenuDelete);
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		Toast.makeText(this, "Fill data", 1000).show();
		
		switch (item.getItemId()){
		
		case DELETE_ID:
			mDbHelper.open();
			mDbHelper.deleteItem(info.id);
			mDbHelper.close();
			fillData();
			return true;
		}		
		
		return super.onContextItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		fillData();
	}

}
