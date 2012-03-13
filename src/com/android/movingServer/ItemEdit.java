package com.android.movingServer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Vibrator;

public class ItemEdit extends Activity{

	private EditText mItemNameText;
	private EditText mItemDescriptionText;
	
	private MovingDbAdapter mDbHelper;
	
	private long CurrentBoxID;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		Button finishedItemButton = (Button) findViewById(R.id.addNewItemFinishedButton);
				
		confirmButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				mDbHelper.createItem(CurrentBoxID, mItemNameText.getText().toString(), mItemDescriptionText.getText().toString());
				print_msg("Item added", 100);
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
		
		newItemButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				mDbHelper.createItem(CurrentBoxID, mItemNameText.getText().toString(), mItemDescriptionText.getText().toString());
				
				print_msg("Item added", 100);
				
				mItemNameText.setText("");
				
				mItemDescriptionText.setText("");
			}
		});
		
		finishedItemButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
	}
	
	private void print_msg(String message, int duration){
		Toast.makeText(this, message, duration).show();
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
	}
}
