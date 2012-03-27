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

// TODO: Auto-generated Javadoc
/**
 * The Class ItemEdit.
 */
public class ItemEdit extends Activity{

	private EditText mItemNameText;
	private EditText mItemDescriptionText;
	private MovingDbAdapter mDbHelper;
	private long CurrentBoxID;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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
				//startBoxView();
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
				//startBoxView();
		    	
				
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
	}
	
	/**
	 * Start box view.
	 */
	private void startBoxView(){
		Intent i = new Intent(this, itemList.class);
    	i.putExtra(MovingDbAdapter.KEY_BOX_ID, CurrentBoxID);
    	startActivity(i);
	}
	
	/**
	 * Print_msg.
	 *
	 * @param message the message
	 * @param duration the duration
	 */
	private void print_msg(String message, int duration){
		Toast.makeText(this, message, duration).show();
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
	}
	
}
