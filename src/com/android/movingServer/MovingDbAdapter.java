package com.android.movingServer;

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
	
    /**
     * Database creation sql statement
     */
	private static final String DATABASE_CREATE_BOXES_TABLE = 
		"CREATE TABLE Boxes (_id integer primary key autoincrement, "
		+ "boxName text not null, boxDescription text not null);";
	private static final String DATABASE_CREATE_ITEMS_TABLE =	
		"CREATE TABLE Items (_id integer primary key autoincrement, "
		+ "itemName text not null, itemDescription text not null, boxID integer);";
	
	private static final String DATABASE_BOX_TABLE = "Boxes";
	public static final String KEY_BOX_ID = "_id";
	public static final String KEY_BOX_NAME = "boxName";
	public static final String KEY_BOX_DESC = "boxDescription";
	
	private static final String DATABASE_ITEM_TABLE = "Items";
	public static final String KEY_ITEM_ID = "_id";
	public static final String KEY_ITEM_NAME = "itemName";
	public static final String KEY_ITEM_DESC = "itemDescription";
	public static final String KEY_ITEM_BOX_ID = "boxID";
	
	
	private static final String DATABASE_NAME = "data";
	
	
	
	private static final int DATABASE_VERSION = 2;
	
	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
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
    	initialValues.put(KEY_BOX_NAME, Name.toUpperCase());
    	initialValues.put(KEY_BOX_DESC, Description);
    	
    	return mDb.insert(DATABASE_BOX_TABLE, null, initialValues);
    }
    
    public Cursor getBoxFromID(long BID){
    	return mDb.query(DATABASE_BOX_TABLE, new String[] {KEY_BOX_ID, KEY_BOX_NAME, KEY_BOX_DESC}, 
    			KEY_BOX_ID + "=" + BID, null, null, null, null);
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
		return mDb.delete(DATABASE_BOX_TABLE, KEY_BOX_ID + "=" + rowId, null) > 0;
	}

	public boolean editBox(Long boxID, String newBoxName, String newBoxDescription) {
        ContentValues args = new ContentValues();
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
    
    	initialValues.put(KEY_ITEM_NAME, itemName);
    	initialValues.put(KEY_ITEM_DESC, itemDescription);
    	initialValues.put(KEY_ITEM_BOX_ID, boxID);
    	
    	return mDb.insert(DATABASE_ITEM_TABLE, null, initialValues);
    }
    
    public Cursor fetchAllItemsFromBox(long boxID) {
    	return mDb.query(DATABASE_ITEM_TABLE, new String[] {KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ITEM_DESC}, 
    			KEY_ITEM_BOX_ID + "=" + boxID, null, null, null, null);
    }

	public boolean deleteItem(long rowId) {
		return mDb.delete(DATABASE_ITEM_TABLE, KEY_ITEM_ID + "=" + rowId, null) > 0;
	}

	public boolean editItem(Long itemID, String newItemName, String newItemDescription) {
        ContentValues args = new ContentValues();
        args.put(KEY_BOX_NAME, newItemName);
        args.put(KEY_BOX_DESC, newItemDescription);

        return mDb.update(DATABASE_ITEM_TABLE, args, KEY_ITEM_ID + "=" + itemID, null) > 0;
		
	}
}
