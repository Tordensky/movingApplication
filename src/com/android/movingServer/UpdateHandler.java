package com.android.movingServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;


public class UpdateHandler {

	private MovingDbAdapter mDbHelper;
	
	public UpdateHandler(Context context){
		mDbHelper = new MovingDbAdapter(context);
	}
	
	public void updateFromInput(String result){
		mDbHelper.open();
		try {
		
		//InputStrem is = JsonParsing.class. 
		JSONArray jArray = new JSONArray(result);
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
		
		
		} catch (JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());

		} finally {
			mDbHelper.close();
		}
	}
}
