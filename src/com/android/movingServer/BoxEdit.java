package com.android.movingServer;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class BoxEdit extends Activity {
	
	private EditText mBoxNameText;
	private EditText mBoxDescriptionText;
	private Long mRowId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.box_edit);
		
		mBoxNameText = (EditText) findViewById(R.id.name);
		mBoxDescriptionText = (EditText) findViewById(R.id.description);
		Button confirmButton = (Button) findViewById(R.id.confirm);
		
		mRowId = null;
		Bundle extras = getIntent().getExtras();
		
		if (extras != null){
			String title = extras.getString(MovingDbAdapter.KEY_BOX_NAME);
		    String body = extras.getString(MovingDbAdapter.KEY_BOX_DESC);
		    mRowId = extras.getLong(MovingDbAdapter.KEY_BOX_ID);
		    
		    if (title != null) {
		    	mBoxNameText.setText(title);
		    }
		    if (body != null) {
		    	mBoxDescriptionText.setText(body);
		    }
		}	
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle bundle = new Bundle();

				bundle.putString(MovingDbAdapter.KEY_BOX_NAME, mBoxNameText.getText().toString());
				bundle.putString(MovingDbAdapter.KEY_BOX_DESC, mBoxDescriptionText.getText().toString());
				if (mRowId != null) {
				    bundle.putLong(MovingDbAdapter.KEY_BOX_ID, mRowId);
				}
				
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();	
			}
		});
		
		/*
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Bundle bundle = new Bundle();
				
				bundle.putString(MovingDbAdapter.KEY_BOX_NAME, mBoxNameText.getText().toString());
				bundle.putString(MovingDbAdapter.KEY_BOX_DESC, mBoxDescriptionText.getText().toString());
				
				if (mRowId != null) {
				    bundle.putLong(MovingDbAdapter.KEY_BOX_ID, mRowId);
				}
				
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();	
			}
		
		});*/
	}
}
