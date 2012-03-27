/*
 * 
 */
package com.android.movingServer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BoxEdit extends Activity {
	
	private EditText mBoxNameText;
	private EditText mBoxDescriptionText;

	private Long mRowId;
	private Long mLocationID;
	
	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	Spinner mSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.box_edit);
		
		mDbHelper = new MovingDbAdapter(this);
		
		mBoxNameText = (EditText) findViewById(R.id.name);
		mBoxDescriptionText = (EditText) findViewById(R.id.description);
		Button confirmButton = (Button) findViewById(R.id.confirm);
		
		mRowId = null;
		mLocationID = (long) 0;
		Bundle extras = getIntent().getExtras();
		
		if (extras != null){
			TextView header = (TextView) findViewById(R.id.actionName);
			header.setText("Edit Box");
			confirmButton.setText("Confirm");
				
			String title = extras.getString(MovingDbAdapter.KEY_BOX_NAME);
		    String body = extras.getString(MovingDbAdapter.KEY_BOX_DESC);
		    mRowId = extras.getLong(MovingDbAdapter.KEY_BOX_ID);
		    mLocationID = extras.getLong(MovingDbAdapter.KEY_BOX_LOCATION_ID);
		    
		    if (title != null) {
		    	mBoxNameText.setText(title);
		    }
		    if (body != null) {
		    	mBoxDescriptionText.setText(body);
		    }
		    if (mLocationID == 0){
		    	mLocationID = (long) 0;
		    }
		}	
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle bundle = new Bundle();

				bundle.putString(MovingDbAdapter.KEY_BOX_NAME, mBoxNameText.getText().toString());
				bundle.putString(MovingDbAdapter.KEY_BOX_DESC, mBoxDescriptionText.getText().toString());
				bundle.putLong(MovingDbAdapter.KEY_BOX_LOCATION_ID, mLocationID);
				
				if (mRowId != null) {
				    bundle.putLong(MovingDbAdapter.KEY_BOX_ID, mRowId);
				}
				
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();	
			}
		});		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mDbHelper.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDbHelper.open();
		fillData();
	}
	
	private void fillData(){
		mMovingCursor = mDbHelper.fetchAllLocations();
		
		if (mMovingCursor.getCount() == 0){
			mLocationID = mDbHelper.createLocation("No Location", "", 0);
			mMovingCursor = mDbHelper.fetchAllLocations();
		}
		
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		
		String[] from = new String[]{MovingDbAdapter.KEY_LOCATION_NAME, MovingDbAdapter.KEY_LOCATION_ID};
		
		int[] to = new int[]{R.id.imageNameSpinner, R.id.imageIconSpinner};
		
		SimpleCursorAdapter items = 
			new SimpleCursorAdapter(this, R.layout.spinner_view, mMovingCursor, from, to);
		
		items.setViewBinder(new SpinnerViewBinder());
		mSpinner.setAdapter(items);
		mSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}
	
	public class SpinnerViewBinder implements SimpleCursorAdapter.ViewBinder {
		 
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		    int viewId = view.getId();
		    switch (viewId) {
		    case R.id.imageNameSpinner:
		        TextView mView = (TextView) view;
		        mView.setText(cursor.getString(columnIndex));
		        break;
		 
		    case R.id.imageIconSpinner:
		        // the icon
		        ImageView mIconView = (ImageView) view;
		        int dialectId = cursor.getInt(columnIndex);
		        switch (dialectId){
		        
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
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	mLocationID = id;
	      Toast.makeText(parent.getContext(), "The planet is", Toast.LENGTH_LONG).show();
	    }

	    public void onNothingSelected(AdapterView parent) {
	    	Toast.makeText(parent.getContext(), "Kommer ikke hit", Toast.LENGTH_LONG).show();
	    	
	    }
	}
	
	public void createLocation(View v){
		Intent i = new Intent(this, CreateLocation.class);
		startActivity(i);
	}
}
