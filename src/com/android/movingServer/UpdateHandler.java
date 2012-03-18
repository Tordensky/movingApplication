package com.android.movingServer;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class UpdateHandler.
 */
public class UpdateHandler{

	/** The m db helper. */
	private MovingDbAdapter mDbHelper;
	
	/** The Constant PREFS_NAME. */
	public static final String PREFS_NAME = "MyPrefsFile";
	
	/** The owners context. */
	private Context ownersContext;
	
	/**
	 * Instantiates a new update handler.
	 *
	 * @param context the context
	 */
	public UpdateHandler(Context context){
		ownersContext = context;
		mDbHelper = new MovingDbAdapter(context);
	}
	
	// TODO CREATE METHOD
	/**
	 * Creates the update message.
	 *
	 * @return the string
	 */
	public String createUpdateMessage(){
		try {
			mDbHelper.open();
			JSONObject updateMessage = new JSONObject();
			updateMessage.put("NewLocations", mDbHelper.getLocationsCreatedAfterLastSync());
			updateMessage.put("NewBoxes", mDbHelper.getBoxesCreatedAfterLastSync());
			updateMessage.put("NewItems", mDbHelper.getItemsCreatedAfterLastSync());
			
			updateMessage.put("UpdatedLocations", mDbHelper.getLocationsUpdatedAfter());
			updateMessage.put("UpdatedBoxes", mDbHelper.getBoxesUpdatedAfter());
			updateMessage.put("UpdatedItems", mDbHelper.getItemsUpdatedAfter());
			
			updateMessage.put("DeletedLocations", mDbHelper.getLocationsDeletedAfter());
			updateMessage.put("DeletedBoxes", mDbHelper.getBoxesDeletedAfter());
			updateMessage.put("DeletedItems", mDbHelper.getItemsDeletedAfter());
			
			
			mDbHelper.close();
			return updateMessage.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "FUNKER IKKE";
		} finally {
			mDbHelper.close();
		}
	}
	
	// TODO IMPLEMENT
	/**
	 * Update id safter post.
	 *
	 * @param body the body
	 */
	public void updateIDSafterPOST(String body){
		
		try {
			JSONObject newIDs = new JSONObject(body);	
			if ((newIDs.getString("Status").compareTo("OK")) == 0){
				Log.e("UPDATE BODY",body);
						
				mDbHelper.open();
				// BOXES
				JSONObject locationIDs = newIDs.getJSONObject("LocationIdMap");
				Iterator<String> locationIter = locationIDs.keys();
				while(locationIter.hasNext()){
					String keyName = locationIter.next();
					Long newID = locationIDs.getLong(keyName);
					Log.i("NEW IDS", keyName+" : "+newID.toString());
					mDbHelper.setRemoteIdLocations(Long.parseLong(keyName), newID);
				}
				
				// Boxes
				JSONObject boxIDs = newIDs.getJSONObject("BoxIdMap");
				Iterator<String> boxIter = boxIDs.keys();
				while(boxIter.hasNext()){
					String keyName = boxIter.next();
					Long newID = boxIDs.getLong(keyName);
					Log.i("NEW IDS", keyName+" : "+newID.toString());
					mDbHelper.setRemoteIdBoxes(Long.parseLong(keyName), newID);
				}
				
				// Items
				JSONObject itemIDs = newIDs.getJSONObject("ItemIdMap");
				Iterator<String> itemIter = itemIDs.keys();
				while(itemIter.hasNext()){
					String keyName = itemIter.next();
					Long newID = itemIDs.getLong(keyName);
					Log.i("NEW IDS", keyName+" : "+newID.toString());
					mDbHelper.setRemoteIdItems(Long.parseLong(keyName), newID);
				}
				
				mDbHelper.deleteFromFlag();
				
				mDbHelper.resetUpdateFlags();
				mDbHelper.close();
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mDbHelper.close();
		}
	}
	
	
	/**
	 * Update from sync.
	 *
	 * @param body the body
	 */
	public void updateFromSync(String body){
		mDbHelper.open();
		try {
			JSONObject json_message_body = new JSONObject(body);
			//JSONArray jArray = new JSONArray(json_message_body.getString("NewLocations"));
			
			createLocationsFromInput(new JSONArray(json_message_body.getString("NewLocations")));
			createBoxesFromInput(new JSONArray(json_message_body.getString("NewBoxes")));
			createItemsFromInput(new JSONArray(json_message_body.getString("NewItems")));
			
			updateLocationsFromInput(new JSONArray(json_message_body.getString("UpdatedLocations")));
			updateBoxesFromInput(new JSONArray(json_message_body.getString("UpdatedBoxes")));
			updateItemsFromInput(new JSONArray(json_message_body.getString("UpdatedItems")));
			
			deleteLocationsFromInput(new JSONArray(json_message_body.getString("DeletedLocations")));
			deleteBoxesFromInput(new JSONArray(json_message_body.getString("DeletedBoxes")));
			deleteItemsFromInput(new JSONArray(json_message_body.getString("DeletedItems")));
			
			setConnectionTimeStamp(json_message_body.getLong("TimeStamp"));
			
		} catch  (Exception e) {
			e.printStackTrace();
			Log.e("UPDATE FROM SYNC", "COULD NOT PARSE RESULT");
		} finally {
			mDbHelper.close();
		}
	}
	
	/**
	 * Creates the locations from input.
	 *
	 * @param locations the locations
	 * @throws JSONException the jSON exception
	 */
	private void createLocationsFromInput(JSONArray locations) throws JSONException{
		for (int i = 0; i < locations.length(); i++){
			JSONObject location = locations.getJSONObject(i);
			Log.i("LOCATION", location.toString());
			mDbHelper.createLocationFromUpdate(location.getLong("LID"), location.getString("locationName"), location.getString("locationDescription"));			
		}
	}
	
	/**
	 * Creates the boxes from input.
	 *
	 * @param boxes the boxes
	 * @throws JSONException the jSON exception
	 */
	private void createBoxesFromInput(JSONArray boxes) throws JSONException{
		for (int i = 0; i < boxes.length(); i++){
			JSONObject box = boxes.getJSONObject(i);
			Log.i("BOXES", box.toString());
			mDbHelper.createBoxFromUpdate(box.getLong("BID"), box.getString("boxName"), box.getString("boxDescription"), box.getLong("boxLocation"));
		}
	}
	
	/**
	 * Creates the items from input.
	 *
	 * @param items the items
	 * @throws JSONException the jSON exception
	 */
	private void createItemsFromInput(JSONArray items) throws JSONException{
		for (int i = 0; i < items.length(); i++){
			JSONObject item = items.getJSONObject(i);
			Log.i("ITEM", item.toString());
			mDbHelper.createItemFromUpdate(item.getLong("IID"), item.getString("itemName"), item.getString("itemDescription"), item.getLong("BID"));
		}
	}
	
	/**
	 * Update locations from input.
	 *
	 * @param locations the locations
	 * @throws JSONException the jSON exception
	 */
	private void updateLocationsFromInput(JSONArray locations) throws JSONException {
		for (int i = 0; i < locations.length(); i++){
			JSONObject location = locations.getJSONObject(i);
			mDbHelper.updateLocationFromUpdate(location.getLong("LID"), location.getString("locationName"), location.getString("locationDescription"));
		}
	}
	
	/**
	 * Update boxes from input.
	 *
	 * @param boxes the boxes
	 * @throws JSONException the jSON exception
	 */
	private void updateBoxesFromInput(JSONArray boxes) throws JSONException {
		for (int i = 0; i < boxes.length(); i++){
			JSONObject box = boxes.getJSONObject(i);
			mDbHelper.updateBoxFromUpdate(box.getLong("BID"), box.getString("boxName"), box.getString("boxDescription"), box.getLong("boxLocation"));
		}
	}
	
	/**
	 * Update items from input.
	 *
	 * @param items the items
	 * @throws JSONException the jSON exception
	 */
	private void updateItemsFromInput(JSONArray items) throws JSONException {
		for (int i = 0; i < items.length(); i++){
			JSONObject item = items.getJSONObject(i);
			mDbHelper.updateItemFromUpdate(item.getLong("IID"), item.getString("itemName"), item.getString("itemDescription"));
		}
	}
	
	/**
	 * Delete locations from input.
	 *
	 * @param locations the locations
	 * @throws JSONException the jSON exception
	 */
	private void deleteLocationsFromInput(JSONArray locations) throws JSONException{
		for (int i = 0; i < locations.length(); i++){
			JSONObject location = locations.getJSONObject(i);
			mDbHelper.deleteLocationFromUpdate(location.getLong("LID"));
		}
	}
	
	/**
	 * Delete boxes from input.
	 *
	 * @param boxes the boxes
	 * @throws JSONException the jSON exception
	 */
	private void deleteBoxesFromInput(JSONArray boxes) throws JSONException{
		for (int i = 0; i < boxes.length(); i++){
			JSONObject box = boxes.getJSONObject(i);
			mDbHelper.deleteBoxFromUpdate(box.getLong("BID"));
		}
	}
	
	/**
	 * Delete items from input.
	 *
	 * @param items the items
	 * @throws JSONException the jSON exception
	 */
	private void deleteItemsFromInput(JSONArray items) throws JSONException{
		for (int i = 0; i < items.length(); i++){
			JSONObject item = items.getJSONObject(i);
			mDbHelper.deleteItemFromUpdate(item.getLong("IID"));
		}
	}
	
	
		
	/**
	 * Update from input.
	 *
	 * @param result the result
	 */
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
	
	/**
	 * Sets the connection time stamp.
	 *
	 * @param timeStamp the new connection time stamp
	 */
	private void setConnectionTimeStamp(long timeStamp){
		SharedPreferences settings = ownersContext.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		settings.getLong("lastConnectionTimeStamp", 0);
		
		editor.putLong("lastConnectionTimeStamp", timeStamp);
		editor.commit();	
	}
}
