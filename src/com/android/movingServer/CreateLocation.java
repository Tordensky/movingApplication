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

		
	private MovingDbAdapter mDbHelper;
	
	private EditText locationName;
	private EditText locationDescription;

	
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
		
		findViewById(R.id.createLocationEdit).setVisibility(View.GONE);
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createLocation();
			}
		});
	}
	
	private void createLocation() {
		mDbHelper.open();
		mDbHelper.createLocation(locationName.getText().toString(), locationDescription.getText().toString(), 0);	
		mDbHelper.close();
		finish();
	}
}
