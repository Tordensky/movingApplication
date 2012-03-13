package com.android.movingServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import com.android.movingServer.Client.ResponseReceiver;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class HttpMovingClient extends IntentService {

	public HttpMovingClient() {
		super("HttpThread");
	}

	private final IBinder mBinder = new MyBinder();
	private static final long UPDATE_INTERVAL = 10000;
	//private Timer timer = new Timer();
	public static final String PREFS_NAME = "MyPrefsFile";

	private UpdateHandler updateHandler;
	
	private String serverURI = "http://129.242.22.95:47301";

	@Override
	public void onCreate() {
		super.onCreate();
		updateHandler = new UpdateHandler(this);
		
		
	}
	
	public void executeHttpGetTimer(){
		//timer.scheduleAtFixedRate(new TimerTask() {		
			//@Override
		//public void run() {
				try {
					executeHttpGet();
					
				} catch (Exception e) {
					print("ERROR IN TIMER");
					e.printStackTrace();
				} finally {
					//stopSelf();
				}
		//	}
		//}, 0, UPDATE_INTERVAL);
	}
	
	public void executeHttpPost() {
		try {
			HttpClient movingClient = new DefaultHttpClient();
			HttpPost postUpdates = new HttpPost(new URI(serverURI+"/updates/0"));
			
			// TODO CREATE UPDATE MESSAGE
			
			StringEntity stringEnt = new StringEntity(updateHandler.createUpdateMessage());
			
			postUpdates.setEntity(stringEnt);
			
			HttpResponse response = movingClient.execute(postUpdates);
			
			String message = responseToString(response);
			updateHandler.updateIDSafterPOST(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			;
		}
	}
	
	public void executeHttpGet() throws Exception {

		BufferedReader input = null;
		long lastConnTime = 0;
		try {
			HttpClient movingClient = new DefaultHttpClient();
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			//SharedPreferences.Editor editor = settings.edit();
			
			lastConnTime = settings.getLong("lastConnectionTimeStamp", 0);
						
			HttpGet getTest = new HttpGet(new URI(serverURI+"/updates/"+lastConnTime));

			HttpResponse response = movingClient.execute(getTest);

			input = new BufferedReader
			(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			//String NL = System.getProperty("line.separator");

			while((line = input.readLine()) != null) {
				sb.append(line);
			}

			input.close();
			String result = sb.toString();
			print (result);
			updateHandler.updateFromSync(result);//updateFromInput(result);
			broadcastUpdate();

		} finally {
			if (input != null) {
				try {
					input.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String responseToString(HttpResponse response) throws IOException{
		BufferedReader input = null;
		input = new BufferedReader
		(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer sb = new StringBuffer("");
		String line = "";

		while((line = input.readLine()) != null) {
			sb.append(line);
		}

		input.close();
		String result = sb.toString();
		return result;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		/*return mBinder;*/
		return null;
	}

	public class MyBinder extends Binder {
		HttpMovingClient getService() {
			return HttpMovingClient.this;
		} 
	}

	private void print (String Text){
		Log.i(getClass().getSimpleName(), "TIMER EVENT PRINT --< "+Text+" >--");
		//Toast.makeText(this, Text, 1000).show();
	}

	@Override
	protected void onHandleIntent(Intent intent) {		
		while(true){
			try {
				executeHttpGetTimer();
				Thread.sleep(10000);
				executeHttpPost();
				Thread.sleep(10000);
				print("ON HANDLE INTENT");
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	//timer.cancel();
        // Tell the user we stopped.
        Toast.makeText(this, "SERVICE DESTROYED", Toast.LENGTH_SHORT).show();
    }

	
	private void broadcastUpdate(){
		
		/*Sends Response to application*/
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		sendBroadcast(broadcastIntent);
	}
}
