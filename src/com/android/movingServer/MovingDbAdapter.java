package com.android.movingServer;

import org.apache.http.util.ExceptionUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovingDbAdapter {
		
	private static final String TAG = "MovingDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String DATABASE_NAME = "data";
	
	/**
	 * LOCATION TABLE AND KEYS
	 */
	private static final String DATABASE_CREATE_LOCATIONS_TABLE =
		"CREATE TABLE Locations "+
		"(_id integer primary key autoincrement, " +
		"LID INTEGER NOT NULL DEFAULT 0, " +
		"locationName TEXT, " +
		"locationDescription TEXT, " +
		"Created INTEGER NOT NULL DEFAULT 0, " +
		"Updated INTEGER NOT NULL DEFAULT 0, " +
		"Deleted INTEGER NOT NULL DEFAULT 0" +
		");";
	
	private static final String DATABASE_LOCATIONS_TABLE = "Locations";
	public static final String KEY_LOCATION_ID = "_id";
	public static final String KEY_LOCATION_REMOTE_LID = "LID";
	public static final String KEY_LOCATION_NAME = "locationName";
	public static final String KEY_LOCATION_DESC = "locationDescription";
	
	/**
	 * BOXES TABLE AND KEYS
	 */
	private static final String DATABASE_CREATE_BOXES_TABLE = 
		"CREATE TABLE Boxes " +
		"(_id integer primary key autoincrement, " +
		"BID INTEGER NOT NULL DEFAULT 0," +
		"boxName text not null," +
		"boxDescription text not null, " + 
		"localLID INTEGER NOT NULL DEFAULT 0, " +
		"LID INTEGER NOT NULL DEFAULT 0, " +
		"Created INTEGER NOT NULL DEFAULT 0, " +
		"Updated INTEGER NOT NULL DEFAULT 0, " +
		"Deleted INTEGER NOT NULL DEFAULT 0" +
		");";
	
	private static final String DATABASE_BOX_TABLE = "Boxes";
	public static final String KEY_BOX_ID = "_id";
	public static final String KEY_BOX_REMOTE_BID = "BID";
	public static final String KEY_BOX_NAME = "boxName";
	public static final String KEY_BOX_DESC = "boxDescription";
	public static final String KEY_BOX_LOCATION_ID = "localLID";
	public static final String KEY_BOX_REMOTE_LOCATION_ID = "LID";
	
	/**
	 * ITEMS TABLE AND KEYS	
	 */
	private static final String DATABASE_CREATE_ITEMS_TABLE =	
		"CREATE TABLE Items "+
		"(_id integer primary key autoincrement, " +
		"IID INTEGER NOT NULL DEFAULT 0, " +
		"itemName TEXT NOT NULL, " +
		"itemDescription TEXT NOT NULL, " +
		"localBID INTEGER NOT NULL, " +
		"BID INTEGER NOT NULL DEFAULT 0, " +
		"Created INTEGER NOT NULL DEFAULT 0, " +
		"Updated INTEGER NOT NULL DEFAULT 0, " +
		"Deleted INTEGER NOT NULL DEFAULT 0" +
		");";
			
	private static final String DATABASE_ITEM_TABLE = "Items";
	public static final String KEY_ITEM_ID = "_id";
	public static final String KEY_ITEM_REMOTE_ID = "IID";
	public static final String KEY_ITEM_NAME = "itemName";
	public static final String KEY_ITEM_DESC = "itemDescription";
	public static final String KEY_ITEM_BOX_ID = "localBID";
	public static final String KEY_ITEM_REMOTE_BOX_ID = "BID";
	
	/**
	 * SHARED KEYS
	 */
	public static final String KEY_CREATED = "Created";
	public static final String KEY_UPDATED = "Updated";
	public static final String KEY_DELETED = "Deleted";
	
	
		
	private static final int DATABASE_VERSION = 2;
	
	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_LOCATIONS_TABLE);
			db.execSQL(DATABASE_CREATE_BOXES_TABLE);
			db.execSQL(DATABASE_CREATE_ITEMS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
		}
	}

    public MovingDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public MovingDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }
    
    /**
     * Create a new box
     * @param Name
     * @return rowId or -1 if failed
     */
    public long createBox(String Name, String Description) {
    	ContentValues initialValues = new ContentValues();
    	
    	if (Description.length() > 1){
    		Description = Description.substring(0,1).toUpperCase()+Description.substring(1).toLowerCase();	
    	}
    	
    	initialValues.put(KEY_BOX_NAME, Name.toUpperCase());
    	initialValues.put(KEY_BOX_DESC, Description);
    	
    	return mDb.insert(DATABASE_BOX_TABLE, null, initialValues);
    }
    
    public Cursor getBoxFromID(long BID){
    	String where = DATABASE_BOX_TABLE+"."+KEY_BOX_ID + "=" + BID;// + " AND " + KEY_BOX_LOCATION_ID + " LIKE " + DATABASE_LOCATIONS_TABLE+"."+KEY_LOCATION_ID;
    	return mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID, KEY_BOX_NAME, KEY_BOX_DESC, KEY_BOX_LOCATION_ID}, 
    			where, null, null, null, null);
    }
    
    public Cursor getLocationFromID(long localLID){
    	return mDb.query(DATABASE_LOCATIONS_TABLE, new String[] {KEY_LOCATION_NAME, KEY_LOCATION_DESC}, KEY_LOCATION_ID+"="+localLID, null, null, null, null);
    }
    
    public Cursor fetchAllBoxes() {
    	return mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID, KEY_BOX_NAME, KEY_BOX_DESC}, 
    			null, null, null, null, KEY_BOX_NAME + " ASC");
    }
    
    public Cursor fetchAllBoxesSearch(String search) {
    	if (search == null){
    		search = "";
    	}
    	search = search.toLowerCase();
    	String where = 	KEY_BOX_NAME.toLowerCase() + " LIKE '%" + search + "%' or " + 
    					KEY_BOX_DESC.toLowerCase() + " LIKE '%" + search + "%' ";
    	
    	return mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID, KEY_BOX_NAME, KEY_BOX_DESC}, 
    			where, null, null, null, KEY_BOX_NAME + " ASC");
    }

	public boolean deleteBox(long rowId) {
		deleteAllItemsInBox(rowId);
		return mDb.delete(DATABASE_BOX_TABLE, KEY_BOX_ID + "=" + rowId, null) > 0;
	}

	public boolean editBox(Long boxID, String newBoxName, String newBoxDescription) {
        ContentValues args = new ContentValues();
        
        if (newBoxDescription.length() > 1){
    		newBoxDescription = newBoxDescription.substring(0,1).toUpperCase()+newBoxDescription.substring(1).toLowerCase();	
    	}
        
        args.put(KEY_BOX_NAME, newBoxName.toUpperCase());
        args.put(KEY_BOX_DESC, newBoxDescription);

        return mDb.update(DATABASE_BOX_TABLE, args, KEY_BOX_ID + "=" + boxID, null) > 0;
		
	}
	
    /**
     * Create a new box
     * @param Name
     * @return rowId or -1 if failed
     */
    public long createItem(long boxID, String itemName, String itemDescription) {
    	ContentValues initialValues = new ContentValues();
    	if (itemName.length() == 0){
    		itemName = "no name item";
    	}
    	
    	itemName = itemName.substring(0,1).toUpperCase()+itemName.substring(1).toLowerCase();
    	
    	if (itemDescription.length() > 1){
    		itemDescription = itemDescription.substring(0,1).toUpperCase()+itemDescription.substring(1).toLowerCase();	
    	}

    	initialValues.put(KEY_ITEM_NAME, itemName);
    	initialValues.put(KEY_ITEM_DESC, itemDescription);
    	initialValues.put(KEY_ITEM_BOX_ID, boxID);
    	
    	return mDb.insert(DATABASE_ITEM_TABLE, null, initialValues);
    }
    
    public Cursor fetchAllItemsFromBox(long boxID) {
    	return mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ITEM_DESC}, 
    			KEY_ITEM_BOX_ID + "=" + boxID, null, null, null, KEY_ITEM_NAME + " ASC");
    }
    
    public Cursor fetchAllITemsFromBoxesWhere(String search){
    	if (search == null){
    		search = "";
    	}
    	search = search.toLowerCase();
    	
    	String where = 	DATABASE_BOX_TABLE+"."+KEY_BOX_ID + " = " + KEY_ITEM_BOX_ID + " AND (" +
    					KEY_ITEM_NAME.toLowerCase() + " LIKE '%" + search + "%' or " +
    					KEY_ITEM_DESC.toLowerCase() + " LIKE '%" + search + "%' or " +
						KEY_BOX_NAME.toLowerCase() + " LIKE '%" + search + "%') ";
    	
    	
    	return mDb.query(DATABASE_ITEM_TABLE + ", " + DATABASE_BOX_TABLE, new String[] {DATABASE_ITEM_TABLE+"."+KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ITEM_DESC, KEY_BOX_NAME}, 
    			where, null, null, null, KEY_ITEM_NAME + " ASC");
    }
    
    public long boxIdFromItemId(long IID){
    	
		Cursor tmpCursor = mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_BOX_ID}, KEY_ITEM_ID + "=" +IID,null,null,null,null);
    	tmpCursor.moveToFirst();
    	return tmpCursor.getLong(0);
    }

	public boolean deleteItem(long rowId) {
		return mDb.delete(DATABASE_ITEM_TABLE, KEY_ITEM_ID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteAllItemsInBox(long BID){
		return mDb.delete(DATABASE_ITEM_TABLE, KEY_ITEM_BOX_ID + "=" + BID, null) > 0;
	}

	public boolean editItem(Long itemID, String newItemName, String newItemDescription) {
        ContentValues args = new ContentValues();
        args.put(KEY_BOX_NAME, newItemName);
        args.put(KEY_BOX_DESC, newItemDescription);

        return mDb.update(DATABASE_ITEM_TABLE, args, KEY_ITEM_ID + "=" + itemID, null) > 0;
		
	}
	
	/**
	 * UPDATE HANDLERS
	 */
	private long getLocalLIDfromLID(long LID){
		//Cursor tmpCursor = mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_BOX_ID}, KEY_ITEM_ID + "=" +IID,null,null,null,null);
		
		Cursor tmpCursor = mDb.query(DATABASE_LOCATIONS_TABLE, new String[] {KEY_LOCATION_ID}, KEY_LOCATION_REMOTE_LID + " = " + LID, null, null, null, null);
		
		try{
			tmpCursor.moveToFirst();
			Long id = tmpCursor.getLong(0);
			tmpCursor.close();
			return id;
		} catch (Exception e){
			Log.e("GET LOCAL LID", "RETURNS 0");
			return 0;
		}

	}
	
	private long getLocalBIDfromBID(long BID){
		Cursor tmpCursor = mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID}, KEY_BOX_REMOTE_BID + " = " + BID, null, null, null, null);
		try{
			tmpCursor.moveToFirst();
			Long id = tmpCursor.getLong(0);
			tmpCursor.close();
			return id;
		} catch (Exception e){
			Log.e("GET LOCAL BID", "RETURNS 0");
			return 0;
		}
	}
	/**
	 * 
	 * @param IID
	 * @return
	 */
	private long getLocalIIDfromIID(long IID){
		Cursor tmpCursor = mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_ID}, KEY_ITEM_REMOTE_ID + " = " + IID, null, null, null, null);
		try{
			tmpCursor.moveToFirst();
			Long id = tmpCursor.getLong(0);
			tmpCursor.close();
			return id;
		} catch (Exception e){
			Log.e("GET LOCAL IID", "RETURNS 0");
			return 0;
		}
	}
	
	/**
	 * Creates a location entry if not already exists a location with this LID
	 * @param LID
	 * @param locationName
	 * @param locationDescription
	 * @return Location row ID
	 */
	public long createLocationFromUpdate(long LID, String locationName, String locationDescription){
		long exist = getLocalLIDfromLID(LID);
		if (exist == 0){
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_LOCATION_REMOTE_LID, LID);
			initialValues.put(KEY_LOCATION_NAME, locationName);
			initialValues.put(KEY_LOCATION_DESC, locationDescription);
			return mDb.insert(DATABASE_LOCATIONS_TABLE, null, initialValues);
		}
		return exist;
	}
	 /**
	  * Creates a Box entry if not already exists a box with this BID
	  * @param BID
	  * @param boxName
	  * @param boxDescription
	  * @param LID
	  * @return Box row ID
	  */
	public long createBoxFromUpdate(long BID, String boxName, String boxDescription, long LID){
		long exist = getLocalBIDfromBID(BID);
		if (exist == 0){
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_BOX_REMOTE_BID, BID);
			initialValues.put(KEY_BOX_NAME, boxName);
			initialValues.put(KEY_BOX_DESC, boxDescription);
			initialValues.put(KEY_BOX_REMOTE_LOCATION_ID, LID);
			initialValues.put(KEY_BOX_LOCATION_ID, getLocalLIDfromLID(LID));
		
			return mDb.insert(DATABASE_BOX_TABLE, null, initialValues);
		}
		return exist;
	}
	/**
	 * Creates a item entry if not already exists a item with this IID
	 * @param IID
	 * @param itemName
	 * @param itemDescription
	 * @param BID
	 * @return item Row ID
	 */
	public long createItemFromUpdate(long IID, String itemName, String itemDescription, long BID){
		long exist = getLocalIIDfromIID(IID);
		if (exist == 0){
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_ITEM_REMOTE_ID, IID);
			initialValues.put(KEY_ITEM_NAME, itemName);
			initialValues.put(KEY_ITEM_DESC, itemDescription);
			initialValues.put(KEY_ITEM_REMOTE_BOX_ID, BID);
			initialValues.put(KEY_ITEM_BOX_ID, getLocalBIDfromBID(BID));
			return mDb.insert(DATABASE_ITEM_TABLE, null, initialValues);			
		}
		return exist;
	}
	
	/**
	 * Update location info from sync to server
	 * @param IID
	 * @param locationName
	 * @param locationDescription
	 * @return Success
	 */
	public boolean updateLocationFromUpdate(long LID, String locationName, String locationDescription){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_LOCATION_NAME, locationName);
		updatedValues.put(KEY_LOCATION_DESC, locationDescription);
		return mDb.update(DATABASE_LOCATIONS_TABLE, updatedValues, KEY_LOCATION_REMOTE_LID + "=" + LID, null) > 0;
	}
	
	/**
	 * Update box info from sync to server
	 * @param BID
	 * @param boxName
	 * @param boxDescription
	 * @param LID
	 * @return success
	 */
	public boolean updateBoxFromUpdate(long BID, String boxName, String boxDescription, long LID){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_BOX_NAME, boxName);
		updatedValues.put(KEY_BOX_DESC, boxDescription);
		updatedValues.put(KEY_BOX_REMOTE_LOCATION_ID, LID);
		updatedValues.put(KEY_BOX_LOCATION_ID, getLocalLIDfromLID(LID));
		return mDb.update(DATABASE_BOX_TABLE, updatedValues, KEY_BOX_REMOTE_BID + "=" + BID, null) > 0;
	}
	
	/**
	 * Update item info from sync to server
	 * @param IID
	 * @param itemName
	 * @param itemDescription
	 * @return success
	 */
	public boolean updateItemFromUpdate(long IID, String itemName, String itemDescription){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_ITEM_NAME, itemName);
		updatedValues.put(KEY_ITEM_DESC, itemDescription);
		return mDb.update(DATABASE_ITEM_TABLE, updatedValues, KEY_ITEM_REMOTE_ID + "=" + IID, null) > 0;
	}
	
	/**
	 * HARD delete location from sync to server
	 * @param LID
	 * @return success
	 */
	public boolean deleteLocationFromUpdate(long LID){
		return mDb.delete(DATABASE_LOCATIONS_TABLE, KEY_LOCATION_REMOTE_LID + "=" + LID, null) > 0;
	}
	
	/**
	 * HARD delete box from sync to server
	 * @param BID
	 * @return success
	 */
	public boolean deleteBoxFromUpdate(long BID){
		return mDb.delete(DATABASE_BOX_TABLE, KEY_BOX_REMOTE_BID + "=" + BID, null) > 0;
	}
	
	/**
	 * HARD delete item from sync to server
	 * @param IID
	 * @return success
	 */
	public boolean deleteItemFromUpdate(long IID){
		return mDb.delete(DATABASE_ITEM_TABLE, KEY_ITEM_REMOTE_ID + "=" + IID, null) > 0;
	}
	
}



