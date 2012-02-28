package com.android.movingServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ItemEdit extends Activity{

	private EditText mItemNameText;
	private EditText mItemDescriptionText;
	
	private MovingDbAdapter mDbHelper;
	
	private long CurrentBoxID;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_create);
		
		mDbHelper = new MovingDbAdapter(this);
        mDbHelper.open();
		
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			CurrentBoxID = extras.getLong(MovingDbAdapter.KEY_BOX_ID);
		}

		mItemNameText = (EditText) findViewById(R.id.newItemName);
		mItemDescriptionText = (EditText) findViewById(R.id.newItemDescription);
		Button confirmButton = (Button) findViewById(R.id.newItemConfirmButton);
		Button newItemButton = (Button) findViewById(R.id.addNewItemButton);
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				mDbHelper.createItem(CurrentBoxID, mItemNameText.getText().toString(), mItemDescriptionText.getText().toString());
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
				finish();	
			}
		});
		
		newItemButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				mDbHelper.createItem(CurrentBoxID, mItemNameText.getText().toString(), mItemDescriptionText.getText().toString());
				
				
				
				mItemNameText.setText("");
				mItemDescriptionText.setText("");
			}
		});
	}
}
