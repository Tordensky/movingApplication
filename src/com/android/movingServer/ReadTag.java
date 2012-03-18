package com.android.movingServer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import uit.nfc.EasyNfcReaderActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadTag.
 */
public class ReadTag extends EasyNfcReaderActivity{

	/* (non-Javadoc)
	 * @see uit.nfc.EasyNfcReaderActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scann_tag);
	}

	/* (non-Javadoc)
	 * @see uit.nfc.EasyNfcReaderActivity#onNewIntent(android.content.Intent)
	 */
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
	    Intent mIntent = new Intent();
	    setResult(RESULT_CANCELED, mIntent);
	    super.onBackPressed();
	}

}
