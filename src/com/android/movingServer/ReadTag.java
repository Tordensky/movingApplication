package com.android.movingServer;

import android.content.Intent;
import android.os.Bundle;
import uit.nfc.EasyNfcReaderActivity;

public class ReadTag extends EasyNfcReaderActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scann_tag);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//Toast.makeText(this, mTagText, Toast.LENGTH_LONG).show();
		
		Bundle bundle = new Bundle();
		
		if (mTagText.length() == 0){
			mTagText = "ERROR";
		}
		
		bundle.putString("TAG_TEXT", mTagText);
		
		Intent mIntent = new Intent();
		mIntent.putExtras(bundle);
		setResult(RESULT_OK, mIntent);
		finish();	
	}
	
	@Override
	public void onBackPressed() {
	    Intent mIntent = new Intent();
	    setResult(RESULT_CANCELED, mIntent);
	    super.onBackPressed();
	}

}
