package com.android.movingServer;

import org.apache.http.util.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class MovingDbAdapter.
 */
public class MovingDbAdapter {
		
	/** The Constant TAG. */
	private static final String TAG = "MovingDbAdapter";
	
	/** The m db helper. */
	private DatabaseHelper mDbHelper;
	
	/** The m db. */
	private SQLiteDatabase mDb;
	
	/** The Constant DATABASE_NAME. */
	private static final String DATABASE_NAME = "data";
	
	/** LOCATION TABLE AND KEYS. */
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
	
	/** The Constant DATABASE_LOCATIONS_TABLE. */
	private static final String DATABASE_LOCATIONS_TABLE = "Locations";
	
	/** The Constant KEY_LOCATION_ID. */
	public static final String KEY_LOCATION_ID = "_id";
	
	/** The Constant KEY_LOCATION_REMOTE_LID. */
	public static final String KEY_LOCATION_REMOTE_LID = "LID";
	
	/** The Constant KEY_LOCATION_NAME. */
	public static final String KEY_LOCATION_NAME = "locationName";
	
	/** The Constant KEY_LOCATION_DESC. */
	public static final String KEY_LOCATION_DESC = "locationDescription";
	
	/** BOXES TABLE AND KEYS. */
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
	
	/** The Constant DATABASE_BOX_TABLE. */
	private static final String DATABASE_BOX_TABLE = "Boxes";
	
	/** The Constant KEY_BOX_ID. */
	public static final String KEY_BOX_ID = "_id";
	
	/** The Constant KEY_BOX_REMOTE_BID. */
	public static final String KEY_BOX_REMOTE_BID = "BID";
	
	/** The Constant KEY_BOX_NAME. */
	public static final String KEY_BOX_NAME = "boxName";
	
	/** The Constant KEY_BOX_DESC. */
	public static final String KEY_BOX_DESC = "boxDescription";
	
	/** The Constant KEY_BOX_LOCATION_ID. */
	public static final String KEY_BOX_LOCATION_ID = "localLID";
	
	/** The Constant KEY_BOX_REMOTE_LOCATION_ID. */
	public static final String KEY_BOX_REMOTE_LOCATION_ID = "LID";
	
	/** ITEMS TABLE AND KEYS. */
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
			
	/** The Constant DATABASE_ITEM_TABLE. */
	private static final String DATABASE_ITEM_TABLE = "Items";
	
	/** The Constant KEY_ITEM_ID. */
	public static final String KEY_ITEM_ID = "_id";
	
	/** The Constant KEY_ITEM_REMOTE_ID. */
	public static final String KEY_ITEM_REMOTE_ID = "IID";
	
	/** The Constant KEY_ITEM_NAME. */
	public static final String KEY_ITEM_NAME = "itemName";
	
	/** The Constant KEY_ITEM_DESC. */
	public static final String KEY_ITEM_DESC = "itemDescription";
	
	/** The Constant KEY_ITEM_BOX_ID. */
	public static final String KEY_ITEM_BOX_ID = "localBID";
	
	/** The Constant KEY_ITEM_REMOTE_BOX_ID. */
	public static final String KEY_ITEM_REMOTE_BOX_ID = "BID";
	
	/** SHARED KEYS. */
	public static final String KEY_CREATED = "Created";
	
	/** The Constant KEY_UPDATED. */
	public static final String KEY_UPDATED = "Updated";
	
	/** The Constant KEY_DELETED. */
	public static final String KEY_DELETED = "Deleted";
	
	
		
	/** The Constant DATABASE_VERSION. */
	private static final int DATABASE_VERSION = 2;
	
	/** The m ctx. */
	private final Context mCtx;
	
	/**
	 * The Class DatabaseHelper.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		/**
		 * Instantiates a new database helper.
		 *
		 * @param context the context
		 */
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_LOCATIONS_TABLE);
			db.execSQL(DATABASE_CREATE_BOXES_TABLE);
			db.execSQL(DATABASE_CREATE_ITEMS_TABLE);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
		}
	}

    /**
     * Instantiates a new moving db adapter.
     *
     * @param ctx the ctx
     */
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
    
    /**
     * Close.
     */
    public void close() {
        mDbHelper.close();
    }
    
    /**
     * Creates the location.
     *
     * @param Name the name
     * @param Description the description
     * @return the long
     */
    public long createLocation(String Name, String Description) {
    	ContentValues initialValues = new ContentValues();
    	
    	if (Description.length() > 1){
    		Description = Description.substring(0,1).toUpperCase()+Description.substring(1).toLowerCase();	
    	}
    	
    	initialValues.put(KEY_LOCATION_NAME, Name.toUpperCase());
    	initialValues.put(KEY_LOCATION_DESC, Description);
    	initialValues.put(KEY_CREATED, 1);
    	
    	return mDb.insert(DATABASE_LOCATIONS_TABLE, null, initialValues);
    }
    
	/**
	 * Edits the location.
	 *
	 * @param locationID the location id
	 * @param name the name
	 * @param description the description
	 * @return true, if successful
	 */
	public boolean editLocation(Long locationID, String name, String description) {
        ContentValues args = new ContentValues();
        
        if (description.length() > 1){
    		description = description.substring(0,1).toUpperCase()+description.substring(1).toLowerCase();	
    	}
        
        args.put(KEY_LOCATION_NAME, name.toUpperCase());
        args.put(KEY_LOCATION_DESC, description);
        args.put(KEY_UPDATED, 1);

        return mDb.update(DATABASE_LOCATIONS_TABLE, args, KEY_BOX_ID + "=" + locationID, null) > 0;		
	}
	
	/**
	 * Delete location.
	 *
	 * @param locationID the location id
	 * @return true, if successful
	 */
	public boolean deleteLocation(Long locationID){
		ContentValues args = new ContentValues();
		args.put(KEY_DELETED, 1);
		return mDb.update(DATABASE_LOCATIONS_TABLE, args, KEY_LOCATION_ID + "=" + locationID, null) > 0;
	}
    
    
    
    /**
     * Create a new box.
     *
     * @param Name the name
     * @param Description the description
     * @return rowId or -1 if failed
     */
    public long createBox(String Name, String Description) {
    	ContentValues initialValues = new ContentValues();
    	
    	if (Description.length() > 1){
    		Description = Description.substring(0,1).toUpperCase()+Description.substring(1).toLowerCase();	
    	}
    	
    	initialValues.put(KEY_BOX_NAME, Name.toUpperCase());
    	initialValues.put(KEY_BOX_DESC, Description);
    	initialValues.put(KEY_CREATED, 1);
    	
    	return mDb.insert(DATABASE_BOX_TABLE, null, initialValues);
    }
    
    /**
     * Gets the box from id.
     *
     * @param BID the bID
     * @return the box from id
     */
    public Cursor getBoxFromID(long BID){
    	String where = DATABASE_BOX_TABLE+"."+KEY_BOX_ID + "=" + BID;// + " AND " + KEY_BOX_LOCATION_ID + " LIKE " + DATABASE_LOCATIONS_TABLE+"."+KEY_LOCATION_ID;
    	return mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID, KEY_BOX_NAME, KEY_BOX_DESC, KEY_BOX_LOCATION_ID}, 
    			where, null, null, null, null);
    }
    
    /**
     * Gets the location from id.
     *
     * @param localLID the local lid
     * @return the location from id
     */
    public Cursor getLocationFromID(long localLID){
    	return mDb.query(DATABASE_LOCATIONS_TABLE, new String[] {KEY_LOCATION_NAME, KEY_LOCATION_DESC}, KEY_LOCATION_ID+"="+localLID, null, null, null, null);
    }
    
    /**
     * Fetch all boxes.
     *
     * @return the cursor
     */
    public Cursor fetchAllBoxes() {
    	return mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID, KEY_BOX_NAME, KEY_BOX_DESC}, 
    			KEY_DELETED +" = 0", null, null, null, KEY_BOX_NAME + " ASC");
    }
    
    /**
     * Fetch all locations.
     *
     * @return the cursor
     */
    public Cursor fetchAllLocations(){
    	return mDb.query(DATABASE_LOCATIONS_TABLE, new String[] {KEY_LOCATION_ID, KEY_LOCATION_NAME, KEY_LOCATION_DESC}, null, null, null, null, null);
    }
    
    /**
     * Fetch all boxes search.
     *
     * @param search the search
     * @return the cursor
     */
    public Cursor fetchAllBoxesSearch(String search) {
    	if (search == null){
    		search = "";
    	}
    	search = search.toLowerCase();
    	String where = 	"("+KEY_BOX_NAME.toLowerCase() + " LIKE '%" + search + "%' OR " + 
    					KEY_BOX_DESC.toLowerCase() + " LIKE '%" + search + "%' ) AND " +
    					KEY_DELETED + "= 0";
    	
    	return mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID, KEY_BOX_NAME, KEY_BOX_DESC}, 
    			where, null, null, null, KEY_BOX_NAME + " ASC");
    }

	/**
	 * Delete box.
	 *
	 * @param rowId the row id
	 * @return true, if successful
	 */
	public boolean deleteBox(long rowId) {
		deleteAllItemsInBox(rowId);
		ContentValues args = new ContentValues();
		args.put(KEY_DELETED, 1);
		return mDb.update(DATABASE_BOX_TABLE, args, KEY_BOX_ID + "=" + rowId, null) > 0;
		//return mDb.delete(DATABASE_BOX_TABLE, KEY_BOX_ID + "=" + rowId, null) > 0;
	}

	/**
	 * Edits the box.
	 *
	 * @param boxID the box id
	 * @param newBoxName the new box name
	 * @param newBoxDescription the new box description
	 * @return true, if successful
	 */
	public boolean editBox(Long boxID, String newBoxName, String newBoxDescription) {
        ContentValues args = new ContentValues();
        
        if (newBoxDescription.length() > 1){
    		newBoxDescription = newBoxDescription.substring(0,1).toUpperCase()+newBoxDescription.substring(1).toLowerCase();	
    	}
        
        args.put(KEY_BOX_NAME, newBoxName.toUpperCase());
        args.put(KEY_BOX_DESC, newBoxDescription);
        args.put(KEY_UPDATED, 1);

        return mDb.update(DATABASE_BOX_TABLE, args, KEY_BOX_ID + "=" + boxID, null) > 0;		
	}
	
    /**
     * Create a new Item.
     *
     * @param boxID the box id
     * @param itemName the item name
     * @param itemDescription the item description
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
    	initialValues.put(KEY_ITEM_REMOTE_BOX_ID, getRemoteBIDforRowID(boxID));
    	initialValues.put(KEY_CREATED, 1);
    	
    	return mDb.insert(DATABASE_ITEM_TABLE, null, initialValues);
    }
    
    /**
     * Fetch all items from box.
     *
     * @param boxID the box id
     * @return the cursor
     */
    public Cursor fetchAllItemsFromBox(long boxID) {
    	return mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ITEM_DESC}, 
    			KEY_ITEM_BOX_ID + "=" + boxID + " AND " + KEY_DELETED +" = 0", null, null, null, KEY_ITEM_NAME + " ASC");
    }
    
    /**
     * Fetch all i tems from boxes where.
     *
     * @param search the search
     * @return the cursor
     */
    public Cursor fetchAllITemsFromBoxesWhere(String search){
    	if (search == null){
    		search = "";
    	}
    	search = search.toLowerCase();
    	
    	String where = 	DATABASE_BOX_TABLE+"."+KEY_BOX_ID + " = " + KEY_ITEM_BOX_ID + " AND ((" +
    					KEY_ITEM_NAME.toLowerCase() + " LIKE '%" + search + "%' or " +
    					KEY_ITEM_DESC.toLowerCase() + " LIKE '%" + search + "%' or " +
						KEY_BOX_NAME.toLowerCase() + " LIKE '%" + search + "%')) AND "+
						DATABASE_ITEM_TABLE+"."+KEY_DELETED + "= 0";
    	
    	
    	return mDb.query(DATABASE_ITEM_TABLE + ", " + DATABASE_BOX_TABLE, new String[] {DATABASE_ITEM_TABLE+"."+KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ITEM_DESC, KEY_BOX_NAME}, 
    			where, null, null, null, KEY_ITEM_NAME + " ASC");
    }
    
    /**
     * Box id from item id.
     *
     * @param IID the iID
     * @return the long
     */
    public long boxIdFromItemId(long IID){
    	
		Cursor tmpCursor = mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_BOX_ID}, KEY_ITEM_ID + "=" +IID,null,null,null,null);
    	tmpCursor.moveToFirst();
    	return tmpCursor.getLong(0);
    }

	/**
	 * Delete item.
	 *
	 * @param rowId the row id
	 * @return true, if successful
	 */
	public boolean deleteItem(long rowId) {
		ContentValues args = new ContentValues();
		args.put(KEY_DELETED, 1);
		return mDb.update(DATABASE_ITEM_TABLE, args, KEY_ITEM_ID + "=" + rowId, null) > 0;
		//return mDb.delete(DATABASE_ITEM_TABLE, KEY_ITEM_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete all items in box.
	 *
	 * @param BID the bID
	 * @return true, if successful
	 */
	public boolean deleteAllItemsInBox(long BID){
		ContentValues args = new ContentValues();
		args.put(KEY_DELETED, 1);
		return mDb.update(DATABASE_ITEM_TABLE, args, KEY_ITEM_BOX_ID + "=" + BID, null) > 0;
		
		
		//return mDb.delete(DATABASE_ITEM_TABLE, KEY_ITEM_BOX_ID + "=" + BID, null) > 0;
	}

	/**
	 * Edits the item.
	 *
	 * @param itemID the item id
	 * @param newItemName the new item name
	 * @param newItemDescription the new item description
	 * @return true, if successful
	 */
	public boolean editItem(Long itemID, String newItemName, String newItemDescription) {
        ContentValues args = new ContentValues();
        args.put(KEY_BOX_NAME, newItemName);
        args.put(KEY_BOX_DESC, newItemDescription);
        args.put(KEY_UPDATED, 1);

        return mDb.update(DATABASE_ITEM_TABLE, args, KEY_ITEM_ID + "=" + itemID, null) > 0;
		
	}
	
	/**
	 * UPDATE HANDLERS.
	 *
	 * @param LID the lID
	 * @return the local li dfrom lid
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
	
	/**
	 * Gets the remote bi dfor row id.
	 *
	 * @param rowID the row id
	 * @return the remote bi dfor row id
	 */
	public long getRemoteBIDforRowID(long rowID){
		Cursor tmpCursor = mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_REMOTE_BID}, KEY_BOX_ID + " = " + rowID, null, null, null, null);
		
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
	
	/**
	 * Gets the local bi dfrom bid.
	 *
	 * @param BID the bID
	 * @return the local bi dfrom bid
	 */
	public long getLocalBIDfromBID(long BID){
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
	 * Gets the local ii dfrom iid.
	 *
	 * @param IID the iID
	 * @return the local ii dfrom iid
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
	 * Creates a location entry if not already exists a location with this LID.
	 *
	 * @param LID the lID
	 * @param locationName the location name
	 * @param locationDescription the location description
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
 	 * Creates a Box entry if not already exists a box with this BID.
 	 *
 	 * @param BID the bID
 	 * @param boxName the box name
 	 * @param boxDescription the box description
 	 * @param LID the lID
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
	 * Creates a item entry if not already exists a item with this IID.
	 *
	 * @param IID the iID
	 * @param itemName the item name
	 * @param itemDescription the item description
	 * @param BID the bID
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
	 * Update location info from sync to server.
	 *
	 * @param LID the lID
	 * @param locationName the location name
	 * @param locationDescription the location description
	 * @return Success
	 */
	public boolean updateLocationFromUpdate(long LID, String locationName, String locationDescription){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_LOCATION_NAME, locationName);
		updatedValues.put(KEY_LOCATION_DESC, locationDescription);
		return mDb.update(DATABASE_LOCATIONS_TABLE, updatedValues, KEY_LOCATION_REMOTE_LID + "=" + LID, null) > 0;
	}
	
	/**
	 * Update box info from sync to server.
	 *
	 * @param BID the bID
	 * @param boxName the box name
	 * @param boxDescription the box description
	 * @param LID the lID
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
	 * Update item info from sync to server.
	 *
	 * @param IID the iID
	 * @param itemName the item name
	 * @param itemDescription the item description
	 * @return success
	 */
	public boolean updateItemFromUpdate(long IID, String itemName, String itemDescription){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_ITEM_NAME, itemName);
		updatedValues.put(KEY_ITEM_DESC, itemDescription);
		return mDb.update(DATABASE_ITEM_TABLE, updatedValues, KEY_ITEM_REMOTE_ID + "=" + IID, null) > 0;
	}
	
	/**
	 * HARD delete location from sync to server.
	 *
	 * @param LID the lID
	 * @return success
	 */
	public boolean deleteLocationFromUpdate(long LID){
		return mDb.delete(DATABASE_LOCATIONS_TABLE, KEY_LOCATION_REMOTE_LID + "=" + LID, null) > 0;
	}
	
	/**
	 * HARD delete box from sync to server.
	 *
	 * @param BID the bID
	 * @return success
	 */
	public boolean deleteBoxFromUpdate(long BID){
		return mDb.delete(DATABASE_BOX_TABLE, KEY_BOX_REMOTE_BID + "=" + BID, null) > 0;
	}
	
	/**
	 * HARD delete item from sync to server.
	 *
	 * @param IID the iID
	 * @return success
	 */
	public boolean deleteItemFromUpdate(long IID){
		return mDb.delete(DATABASE_ITEM_TABLE, KEY_ITEM_REMOTE_ID + "=" + IID, null) > 0;
	}
	
	/**
	 * Gets the locations created after last sync.
	 *
	 * @return the locations created after last sync
	 */
	public JSONArray getLocationsCreatedAfterLastSync(){
		String where = "("+KEY_CREATED + "= 1 OR (" + KEY_CREATED + "= 1 AND " + KEY_UPDATED + "= 1)) AND " + KEY_DELETED + "= 0";
		Cursor cursor = mDb.query(DATABASE_LOCATIONS_TABLE, new String[] {KEY_LOCATION_ID, KEY_LOCATION_NAME, KEY_LOCATION_DESC}, where, null, null, null, null);
		return cursorToJSON(cursor);
	}
	
	/**
	 * Gets the boxes created after last sync.
	 *
	 * @return the boxes created after last sync
	 */
	public JSONArray getBoxesCreatedAfterLastSync(){
		String where = "("+KEY_CREATED + "= 1 OR (" + KEY_CREATED + "= 1 AND " + KEY_UPDATED + "= 1)) AND " + KEY_DELETED + "= 0";
		Cursor cursor = mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID, KEY_BOX_NAME, KEY_BOX_DESC, KEY_BOX_LOCATION_ID, KEY_BOX_REMOTE_LOCATION_ID}, where, null, null, null, null);
		return cursorToJSON(cursor);
	}
	
	/**
	 * Gets the items created after last sync.
	 *
	 * @return the items created after last sync
	 */
	public JSONArray getItemsCreatedAfterLastSync(){
		String where = "("+KEY_CREATED + "= 1 OR (" + KEY_CREATED + "= 1 AND " + KEY_UPDATED + "= 1)) AND " + KEY_DELETED + "= 0";
		Cursor cursor = mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ITEM_DESC, KEY_ITEM_BOX_ID, KEY_ITEM_REMOTE_BOX_ID}, where, null, null, null, null);
		return cursorToJSON(cursor);
	}
	
	/**
	 * Gets the locations updated after.
	 *
	 * @return the locations updated after
	 */
	public JSONArray getLocationsUpdatedAfter(){
		String where = KEY_CREATED + "= 0 AND " + KEY_UPDATED + "= 1 AND " + KEY_DELETED + "= 0";
		Cursor cursor = mDb.query(DATABASE_LOCATIONS_TABLE, new String[] {KEY_LOCATION_REMOTE_LID, KEY_LOCATION_NAME, KEY_LOCATION_DESC}, where, null, null, null, null);
		return cursorToJSON(cursor);
	}
	
	/**
	 * Gets the boxes updated after.
	 *
	 * @return the boxes updated after
	 */
	public JSONArray getBoxesUpdatedAfter(){
		String where = KEY_CREATED + "= 0 AND " + KEY_UPDATED + "= 1 AND " + KEY_DELETED + "= 0";
		Cursor cursor = mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_REMOTE_BID, KEY_BOX_NAME, KEY_BOX_DESC, KEY_BOX_LOCATION_ID, KEY_BOX_REMOTE_LOCATION_ID}, where, null, null, null, null);
		return cursorToJSON(cursor);
	}
	
	/**
	 * Gets the items updated after.
	 *
	 * @return the items updated after
	 */
	public JSONArray getItemsUpdatedAfter(){
		String where = KEY_CREATED + "= 0 AND " + KEY_UPDATED + "= 1 AND " + KEY_DELETED + "= 0";
		Cursor cursor = mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_REMOTE_ID, KEY_ITEM_NAME, KEY_ITEM_DESC, KEY_ITEM_BOX_ID, KEY_ITEM_REMOTE_BOX_ID}, where, null, null, null, null);
		return cursorToJSON(cursor);
	}
	
	/**
	 * Gets the locations deleted after.
	 *
	 * @return the locations deleted after
	 */
	public JSONArray getLocationsDeletedAfter(){
		String where = KEY_DELETED + "= 1";
		Cursor cursor = mDb.query(DATABASE_LOCATIONS_TABLE, new String[] {KEY_LOCATION_REMOTE_LID}, where, null, null, null, null);
		return cursorToJSON(cursor);
	}
	
	/**
	 * Gets the boxes deleted after.
	 *
	 * @return the boxes deleted after
	 */
	public JSONArray getBoxesDeletedAfter(){
		String where = KEY_DELETED + "= 1";
		Cursor cursor = mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_REMOTE_BID}, where, null, null, null, null);
		return cursorToJSON(cursor);	
	}
	
	/**
	 * Gets the items deleted after.
	 *
	 * @return the items deleted after
	 */
	public JSONArray getItemsDeletedAfter(){
		String where = KEY_DELETED + "= 1";
		Cursor cursor = mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_REMOTE_ID}, where, null, null, null, null);
		return cursorToJSON(cursor);
		
	}
	
	/**
	 * Cursor to json.
	 *
	 * @param cursor the cursor
	 * @return the jSON array
	 */
	private JSONArray cursorToJSON(Cursor cursor){
		JSONArray result = new JSONArray();
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++){
			JSONObject row = new JSONObject();
			for (int j = 0; j < cursor.getColumnCount(); j++){
				try {
					//
					Log.i("J =", Integer.toString(j));
					row.putOpt(cursor.getColumnName(j), cursor.getString(j));
					
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("REULT TO JASON FUNKER IKKE!", "DRITT");
				}
				
			}
			cursor.moveToNext();
			result.put(row);
		}				
		return result;
	}
	
	/**
	 * Delete from flag.
	 */
	public void deleteFromFlag(){
		mDb.delete(DATABASE_LOCATIONS_TABLE, KEY_DELETED + "= 1", null);
		mDb.delete(DATABASE_BOX_TABLE, KEY_DELETED + "= 1", null);
		mDb.delete(DATABASE_ITEM_TABLE, KEY_DELETED + "= 1", null);
	}
	
	/**
	 * Sets the remote id locations.
	 *
	 * @param rowID the row id
	 * @param LID the lID
	 */
	public void setRemoteIdLocations(long rowID, long LID){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_LOCATION_REMOTE_LID, LID);
		updatedValues.put(KEY_CREATED, 0);
		mDb.update(DATABASE_LOCATIONS_TABLE, updatedValues, KEY_LOCATION_ID + "=" + rowID, null);
		updateBoxLocationLID(rowID, LID);
	}
	
	/**
	 * Update box location lid.
	 *
	 * @param localLID the local lid
	 * @param remoteLID the remote lid
	 */
	private void updateBoxLocationLID(long localLID, long remoteLID){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_BOX_REMOTE_LOCATION_ID, remoteLID);
		mDb.update(DATABASE_BOX_TABLE, updatedValues, KEY_BOX_LOCATION_ID +"="+ localLID, null);
	}
	
	/**
	 * Sets the remote id boxes.
	 *
	 * @param rowID the row id
	 * @param BID the bID
	 */
	public void setRemoteIdBoxes(long rowID, long BID){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_BOX_REMOTE_BID, BID);
		updatedValues.put(KEY_CREATED, 0);
		mDb.update(DATABASE_BOX_TABLE, updatedValues, KEY_BOX_ID + "=" +rowID, null);
		updateItemBoxBID(rowID, BID);
	}
	
	/**
	 * Update item box bid.
	 *
	 * @param localBID the local bid
	 * @param remoteBID the remote bid
	 */
	private void updateItemBoxBID(long localBID, long remoteBID){
		Log.e("UPDATE ITEM BOX ID", Long.toString(localBID) +" : "+ Long.toString(remoteBID));
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_ITEM_REMOTE_BOX_ID, remoteBID);
		mDb.update(DATABASE_ITEM_TABLE, updatedValues, KEY_ITEM_BOX_ID +"="+ localBID, null);
	}
	
	/**
	 * Sets the remote id items.
	 *
	 * @param rowId the row id
	 * @param IID the iID
	 */
	public void setRemoteIdItems(long rowId, long IID){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_ITEM_REMOTE_ID, IID);
		updatedValues.put(KEY_CREATED, 0);
		mDb.update(DATABASE_ITEM_TABLE, updatedValues, KEY_ITEM_ID + "=" + rowId, null);
	}
	
	/**
	 * Reset update flags.
	 */
	public void resetUpdateFlags(){
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_UPDATED, 0);
		mDb.update(DATABASE_LOCATIONS_TABLE, updatedValues, null, null);
		mDb.update(DATABASE_BOX_TABLE, updatedValues, null, null);
		mDb.update(DATABASE_ITEM_TABLE, updatedValues, null, null);
	}
}



