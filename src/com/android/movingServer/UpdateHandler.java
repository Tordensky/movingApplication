package com.android.movingServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UpdateHandler{

	private MovingDbAdapter mDbHelper;
	
	public static final String PREFS_NAME = "MyPrefsFile";
	
	private Context ownersContext;
	
	public UpdateHandler(Context context){
		ownersContext = context;
		mDbHelper = new MovingDbAdapter(context);
	}
	
	public void updateFromInput(String result){
		mDbHelper.open();
		try {
		
		JSONObject json_message_body = new JSONObject(result);
		
		Log.i("RECEIVED TIME STAMP", json_message_body.getString("timeStamp"));
		
		
		//InputStrem is = JsonParsing.class. 
		JSONArray jArray = new JSONArray(json_message_body.getString("boxes"));
		for (int i = 0; i < jArray.length();i++){
			JSONObject json_data = jArray.getJSONObject(i);
			
			Log.i("log_tag", "BoxName: "+json_data.getString("boxName")
					+ "BoxDescription: "+json_data.getString("boxDescription"));
			
			long boxID = mDbHelper.createBox(json_data.getString("boxName"), json_data.getString("boxDescription"));
			
			JSONArray itemArray = new JSONArray(json_data.getString("items"));
			for (int j = 0; j < itemArray.length(); j++){
				JSONObject json_item_data = itemArray.getJSONObject(j);
				mDbHelper.createItem(boxID, json_item_data.getString("itemName"), json_item_data.getString("itemDescription"));
			}
		}
		// Everything is loaded OK, set time stamp for successful update
		setConnectionTimeStamp(json_message_body.getLong("timeStamp"));
		
		} catch (JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());

		} finally {
			mDbHelper.close();
		}
	}
	
	private void setConnectionTimeStamp(long timeStamp){
		SharedPreferences settings = ownersContext.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		settings.getLong("lastConnectionTimeStamp", 0);
		
		editor.putLong("lastConnectionTimeStamp", timeStamp);
		editor.commit();	
	}
}
