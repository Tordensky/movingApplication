package com.android.movingServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.movingServer.R;
import com.android.movingServer.BoxEdit.MyOnItemSelectedListener;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class CreateLocation extends Activity {

	// Spinner images
	private static Integer[] imageIconDatabase = { 	R.drawable.box_blue, 
													R.drawable.box_green, 
													R.drawable.box_orange, 
													R.drawable.box_purple,
													R.drawable.box_yellow, 
													R.drawable.box_darkpurple 
													};
	
	private String[] imageNameDatabase = { 			"Blue", 
													"Green",
													"Orange",
													"Purple",
													"Yellow",
													"Dark Purple"
													};
	
	private List<Map<String, ?>> datalist;
		
	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	
	private EditText locationName;
	private EditText locationDescription;
	private long colorCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.box_edit);		
		mDbHelper = new MovingDbAdapter(this);
		
		TextView header = (TextView) findViewById(R.id.actionName);
		header.setText("Create Location");
		
		ImageView image = (ImageView) findViewById(R.id.typeImage);
		image.setImageResource(R.drawable.house_blue);
		
		locationName = (EditText) findViewById(R.id.name);
		locationName.setHint("Tap to enter location name");
		locationDescription = (EditText) findViewById(R.id.description);
		locationDescription.setHint("Tap to enter location Description");
		Button confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setText("Create Location");
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createLocation();
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
		setupColorSpinner();
		//fillData();
	}
	
	private void createLocation() {
		mDbHelper.createLocation(locationName.getText().toString(), locationDescription.getText().toString(), colorCode);	
		finish();
	}
		
	void setupColorSpinner(){
		
		datalist = new ArrayList<Map<String, ?>>();
		Map<String, Object> map;
		
		for (int i = 0; i < imageNameDatabase.length; i++){
			map = new HashMap<String, Object>();
			map.put("Name", imageNameDatabase[i]);
			map.put("Icon", imageIconDatabase[i]);
			datalist.add(map);
		}
		
		Spinner mSpinner = (Spinner) findViewById(R.id.spinner1);
		
		int[] to = new int[]{R.id.imageNameSpinner, R.id.imageIconSpinner};
		
		String[] from = new String[]{"Name", "Icon"};
		
		SimpleAdapter colors = 
			new SimpleAdapter(this, datalist, R.layout.spinner_view, from , to);
		
		mSpinner.setAdapter(colors);
		mSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	colorCode = id;
	      Toast.makeText(parent.getContext(), "The planet is", Toast.LENGTH_LONG).show();
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
}
