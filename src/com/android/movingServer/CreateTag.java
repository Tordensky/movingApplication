package com.android.movingServer;

import uit.nfc.EasyNfcWriterActivity;
import android.os.Bundle;
import android.widget.Toast;

public class CreateTag extends EasyNfcWriterActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scann_tag);
		Toast.makeText(this, "Why?", 1000).show();
	}
	
//	@Override
//	public void onBackPressed() {
//	    //Intent mIntent = new Intent();
//	    //setResult(RESULT_OK, mIntent);
//	    super.onBackPressed();
//	}
}
