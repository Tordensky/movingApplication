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
	
	
	public void updateFromSync(String body){
		mDbHelper.open();
		try {
			JSONObject json_message_body = new JSONObject(body);
			//JSONArray jArray = new JSONArray(json_message_body.getString("NewLocations"));
			
			createLocationsFromInput(new JSONArray(json_message_body.getString("NewLocations")));
			createBoxesFromInput(new JSONArray(json_message_body.getString("NewBoxes")));
			createItemsFromInput(new JSONArray(json_message_body.getString("NewItems")));
			
			setConnectionTimeStamp(json_message_body.getLong("TimeStamp"));
			
		} catch  (Exception e) {
			e.printStackTrace();
			Log.e("UPDATE FROM SYNC", "COULD NOT PARSE RESULT");
		} finally {
			mDbHelper.close();
		}
	}
	
	private void createLocationsFromInput(JSONArray locations) throws JSONException{
		for (int i = 0; i < locations.length(); i++){
			JSONObject location = locations.getJSONObject(i);
			Log.i("LOCATION", location.toString());
			mDbHelper.createLocationFromUpdate(location.getLong("LID"), location.getString("locationName"), location.getString("locationDescription"));
			
		}
	}
	
	private void createBoxesFromInput(JSONArray boxes) throws JSONException{
		for (int i = 0; i < boxes.length(); i++){
			JSONObject box = boxes.getJSONObject(i);
			Log.i("BOXES", box.toString());
			mDbHelper.createBoxFromUpdate(box.getLong("BID"), box.getString("boxName"), box.getString("boxDescription"), box.getLong("boxLocation"));
		}
	}
	
	private void createItemsFromInput(JSONArray items) throws JSONException{
		for (int i = 0; i < items.length(); i++){
			JSONObject item = items.getJSONObject(i);
			Log.i("ITEM", item.toString());
			mDbHelper.createItemFromUpdate(item.getLong("IID"), item.getString("itemName"), item.getString("itemDescription"), item.getLong("BID"));
		}
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
