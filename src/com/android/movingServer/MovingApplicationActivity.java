package com.android.movingServer;


import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class MovingApplicationActivity.
 */
public class MovingApplicationActivity extends ListActivity {
    /** Called when the activity is first created. */
    
	private static final int ACTIVITY_CREATE_BOX = 0;
	private static final int ACTIVITY_EDIT_BOX = 1;
	private static final int ACTIVTY_ITEMS_LIST = 2;
	private static final int ACTIVTY_READ_TAG = 3;
	
	private MovingDbAdapter mDbHelper;
	private Cursor mMovingCursor;
	

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int EDIT_BOX = Menu.FIRST + 2;
	private static final int CREATE_TAG = Menu.FIRST + 3;
	private static final int READ_BOX_TAG = Menu.FIRST + 4;
	private static final int REFRESH = Menu.FIRST + 5;
	

	private EditText boxSearchField;
	private String boxSearchString;
	
	private ResponseReceiver receiver;
	private IntentFilter filter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.box_list);
        
        mDbHelper = new MovingDbAdapter(this);
        mDbHelper.open();
                
        fillData();
        setupMenu();
        
        boxSearchField = (EditText) findViewById(R.id.searchBoxesInputField);
        
        boxSearchField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				boxSearchString = boxSearchField.getText().toString();
				fillData();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {		
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
        
        filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        	        
        registerForContextMenu(getListView());
    }
	    
    private void fillData() {
    	
    	mMovingCursor = mDbHelper.fetchAllBoxesSearch(boxSearchString);
    	startManagingCursor(mMovingCursor);
    	
    	String[] from = new String[]{	MovingDbAdapter.KEY_BOX_NAME, 
    									MovingDbAdapter.KEY_BOX_DESC, 
    									MovingDbAdapter.KEY_BOX_LOCATION_ID,
    									MovingDbAdapter.KEY_LOCATION_NAME
    									};
    	
    	// Used if different box images
    	//int[] to = new int[]{R.id.boxName, R.id.boxDescription, R.id.boxImage};
    	
    	int[] to = new int[]{R.id.boxName, R.id.boxDescription, 0, R.id.boxLocation};
    	
    	SimpleCursorAdapter boxes =
    		new SimpleCursorAdapter(this, R.layout.box_row, mMovingCursor, from, to);
    	
    	// Used if different box images
    	//boxes.setViewBinder(new listViewBinder());
    	setListAdapter(boxes);
    }
    
    /**
     * Makes it possible to have different images in list view
     */
	public class listViewBinder implements SimpleCursorAdapter.ViewBinder {
		 
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		    int viewId = view.getId();
		    TextView mView;
		    
		    switch (viewId) {
		    
		    case R.id.boxName:
		        mView = (TextView) view;
		        mView.setText(cursor.getString(columnIndex));
		        break;
		        
		    case R.id.boxDescription:
		        mView = (TextView) view;
		        mView.setText(cursor.getString(columnIndex));
		        break;
		 
		    case R.id.boxImage:
		    	long color = mDbHelper.getLocationColor(cursor.getLong(columnIndex));
		        ImageView mIconView = (ImageView) view;
		        
		        switch ((int)color){
		        
		        case 0:
		            mIconView.setImageResource(R.drawable.box_blue);
		            break;
		        case 1:
		            mIconView.setImageResource(R.drawable.box_green);
		            break;	            
		        case 2:
		            mIconView.setImageResource(R.drawable.box_orange);
		            break;   
		        case 3:
		        	mIconView.setImageResource(R.drawable.box_purple);
		            break;
		        case 4:
		        	mIconView.setImageResource(R.drawable.box_yellow);
		            break;
		        case 5:
		        	mIconView.setImageResource(R.drawable.box_purple);
		            break;
		        default:
		            mIconView.setImageResource(R.drawable.box_orange);
		            break;
		        }
		    }
		    return true;
		}
	}
     
	private void setupMenu(){
		Button newButton = (Button) findViewById(R.id.new_button);
		Button searchButton = (Button) findViewById(R.id.search_button);
		Button tagButton = (Button) findViewById(R.id.tag_button);
		Button refreshButton = (Button) findViewById(R.id.refresh_button);
		
		
		
		newButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createBox();
			}
		});
		
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Enable search when clicked
			}
		});
		
		tagButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				readTag();
			}
		});
		
		refreshButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startHttpService();
			}
		});
	}
    

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CREATE_TAG, 0, R.string.boxListMenuCreateTag);
		menu.add(0, EDIT_BOX, 0, R.string.boxListMenuEdit);
		menu.add(0, DELETE_ID, 0, R.string.boxListMenuDelete);
		
	}
    

    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()){
		case DELETE_ID:
			
	        mDbHelper.deleteBox(info.id);
	        fillData();
			return true;
		
		case EDIT_BOX:

			Cursor c = mMovingCursor;
	        c.moveToPosition(info.position);
	        Intent i = new Intent(this, BoxEdit.class);
	        i.putExtra(MovingDbAdapter.KEY_BOX_ID, info.id);
	        
	        i.putExtra(MovingDbAdapter.KEY_BOX_NAME, c.getString(
	                c.getColumnIndexOrThrow(MovingDbAdapter.KEY_BOX_NAME)));
	        
	        i.putExtra(MovingDbAdapter.KEY_BOX_DESC, c.getString(
	                c.getColumnIndexOrThrow(MovingDbAdapter.KEY_BOX_DESC)));
	        
	        i.putExtra(MovingDbAdapter.KEY_BOX_LOCATION_ID, c.getString(
	                c.getColumnIndexOrThrow(MovingDbAdapter.KEY_BOX_LOCATION_ID)));
	        /*i.putExtra(MovingDbAdapter.KEY_BOX_LOCATION_ID, mDbHelper.getBoxLocationId(info.id));*/
	        
	        startActivityForResult(i, ACTIVITY_EDIT_BOX);
	        
	        startHttpService();
			return true;
		
    	case CREATE_TAG:
    		createTag(info.id);
    		return true;
    	}
    	
    	return super.onContextItemSelected(item);
	}
    
    protected void onListItemClick(ListView l, View v, int position, long id){
    	super.onListItemClick(l, v, position, id);
    	gotoBox(id);
    }
	
    private void createBox() {
    	Intent i = new Intent(this, BoxEdit.class);
    	startActivityForResult(i, ACTIVITY_CREATE_BOX);
    }

	private void readTag() {
		Intent i = new Intent(this, ReadTag.class);
		startActivityForResult(i, ACTIVTY_READ_TAG);	
	}
	

    
    private void createTag(long BID) {
    	long remoteID = mDbHelper.getRemoteBIDforRowID(BID);
    	if (remoteID == 0){
    		print("MUST SYNC BEFORE TAG CAN BE CREATED");
    	} else {
    	
	    	Intent i = new Intent(this, CreateTag.class);
	    	i.putExtra(CreateTag.TAG_TEXT, "BOX#"+BID);
	    	startActivity(i);
    	}
    }
    
    private void createItems(long BID){
    	Intent i = new Intent(this, ItemEdit.class);
    	i.putExtra(MovingDbAdapter.KEY_BOX_ID, BID);
    	startActivityForResult(i, ACTIVTY_ITEMS_LIST);
    }
    
    private void gotoBox(long BID){
    	
    	Intent i = new Intent(this, itemList.class);
    	i.putExtra(MovingDbAdapter.KEY_BOX_ID, BID);
    	startActivityForResult(i, ACTIVTY_ITEMS_LIST);
    }
    
	private void startHttpService() {
		Intent i = new Intent(this, HttpMovingClient.class);
		startService(i);
	}
    
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	
    	mDbHelper.open();
    	
    	Bundle extras;
		extras = intent.getExtras();
		
    	switch(requestCode){
        
        case ACTIVITY_CREATE_BOX:
        	String BoxName = extras.getString(MovingDbAdapter.KEY_BOX_NAME);
        	String BoxDescription = extras.getString(MovingDbAdapter.KEY_BOX_DESC);
        	Long BoxLocation = extras.getLong(MovingDbAdapter.KEY_BOX_LOCATION_ID);
        	
        	if (BoxName.length() == 0){
            	BoxName = "Div box";
            }
        	
        	if (BoxDescription.length() == 0){
            	BoxDescription = "no description";
            }
        	
        	
        	createItems(mDbHelper.createBox(BoxName, BoxDescription, BoxLocation));
        	fillData();
        	break;
        
        case ACTIVITY_EDIT_BOX:
        	String NewBoxName = extras.getString(MovingDbAdapter.KEY_BOX_NAME);
        	String NewBoxDescription = extras.getString(MovingDbAdapter.KEY_BOX_DESC);
        	Long BoxID = extras.getLong(MovingDbAdapter.KEY_BOX_ID);
        	Long newBoxLocation = extras.getLong(MovingDbAdapter.KEY_BOX_LOCATION_ID);
        	
        	mDbHelper.editBox(BoxID, NewBoxName, NewBoxDescription, newBoxLocation);
        	fillData();
        	break;
        	
        case ACTIVTY_ITEMS_LIST:
        	fillData();
        	break;
        	
        case ACTIVTY_READ_TAG:
        	
        	try {
	        	String tagText = extras.getString("TAG_TEXT");
	        	String[] tagSplit;
	        	
	        	tagSplit = tagText.split("#");
	        	
	        	if (tagSplit[0].compareTo("BOX") == 0){
	        		Toast.makeText(this, tagSplit[1], 1000).show();
	        		long rowID = mDbHelper.getLocalBIDfromBID((long)Integer.parseInt(tagSplit[1]));
	        		if (rowID == 0){
	        			print("Could not fin box");
	        		} else {
	        			gotoBox(rowID);
	        		}
	        		
	        	}
        	} catch (Exception e){
        		Log.e("READ TAG", "Crashed when checking tag content");
        	}
        	
        	Log.w("ACTIVITY READ", "Kommer hit");
        	break;
    	}

    }
    
	public class ResponseReceiver extends BroadcastReceiver {
		
		/** The Constant ACTION_RESP. */
		public static final String ACTION_RESP =
		      "com.mamlambo.intent.action.MESSAGE_PROCESSED";

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			fillData();
		}	
	}

	@Override
	protected void onPause() {
		mMovingCursor.close();
		mDbHelper.close();
		unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		mDbHelper.open();
		registerReceiver(receiver, filter);
		startHttpService();
		fillData();
		super.onResume();
	}
	
	private void print (String Text){
		Toast.makeText(this, Text, 1000).show();
	}
	
	
}