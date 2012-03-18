package com.android.movingServer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.movingServer.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class CreateLocation extends Activity {

	// Spinner images
	private static Integer[] imageIconDatabase = { R.drawable.blue_box, R.drawable.box };
	private String[] imageNameDatabase = { "Blue", "White"};
	private List<? extends Map<String, ?>> spinnerList;
	
	
	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.box_edit);
		
		mDbHelper = new MovingDbAdapter(this);
		
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
		
		//startManagingCursor(mMovingCursor);
		
		Spinner mSpinner = (Spinner) findViewById(R.id.spinner1);
		
		String[] from = new String[]{MovingDbAdapter.KEY_LOCATION_NAME, MovingDbAdapter.KEY_LOCATION_ID};
		
		int[] to = new int[]{R.id.imageNameSpinner, R.id.imageIconSpinner};
		
		SimpleCursorAdapter items = 
			new SimpleCursorAdapter(this, R.layout.spinner_view, mMovingCursor, from, to);
		
		items.setViewBinder(new SpinnerViewBinder());
		mSpinner.setAdapter(items);
	}

	public class SpinnerViewBinder implements SimpleCursorAdapter.ViewBinder {
		 
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		    int viewId = view.getId();
		    switch (viewId) {
		    case R.id.imageNameSpinner:
		        // the textview
		        TextView mView = (TextView) view;
		        // display the name
		        mView.setText(cursor.getString(columnIndex));
		        break;
		 
		    case R.id.imageIconSpinner:
		        // the icon
		        ImageView mIconView = (ImageView) view;
		        int dialectId = cursor.getInt(columnIndex);
		        switch (dialectId){
		        case 1:
		            mIconView.setImageResource(R.drawable.house_blue);
		            break;
		        case 2:
		            mIconView.setImageResource(R.drawable.house_green);
		            break;
		            
		        case 3:
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
}
