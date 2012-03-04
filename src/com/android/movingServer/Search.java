package com.android.movingServer;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Search extends ListActivity {

	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	
	private EditText itemSearchField;
	private String itemSearchString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_list);
		mDbHelper = new MovingDbAdapter(this);
        mDbHelper.open();
        
        itemSearchField =(EditText) findViewById(R.id.searchBoxesInputField);
        
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
	}
	
	private void fillData(){
		Toast.makeText(this, "Fill data", 1000).show();
    	mMovingCursor = mDbHelper.fetchAllITemsFromBoxesWhere(itemSearchString);//fetchAllITemsFromBoxesWhere("");
    	
    	startManagingCursor(mMovingCursor);
    	
    	String[] from = new String[]{MovingDbAdapter.KEY_ITEM_NAME , MovingDbAdapter.KEY_ITEM_DESC, MovingDbAdapter.KEY_BOX_NAME};
    	
    	int[] to = new int[]{R.id.itemName, R.id.itemDescription, R.id.itemBox};
    	
    	SimpleCursorAdapter items =
    		new SimpleCursorAdapter(this, R.layout.search_items_row, mMovingCursor, from, to);
    	setListAdapter(items);
	}

}
