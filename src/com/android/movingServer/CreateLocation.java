package com.android.movingServer;

import com.android.movingServer.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
