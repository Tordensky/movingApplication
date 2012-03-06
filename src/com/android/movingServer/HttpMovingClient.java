package com.android.movingServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.android.movingServer.Client.ResponseReceiver;
import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class HttpMovingClient extends IntentService {

	public HttpMovingClient() {
		super("SOME NAME");
	}

	private final IBinder mBinder = new MyBinder();
	private static final long UPDATE_INTERVAL = 60000;
	private Timer timer = new Timer();
	

	private UpdateHandler updateHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		executeHttpGetTimer();
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
				}
//			}
//		}, 0, UPDATE_INTERVAL);
	}
	

	public void executeHttpGet() throws Exception {

		BufferedReader input = null;
		try {
			HttpClient movingClient = new DefaultHttpClient();
			
			HttpGet getTest = new HttpGet(new URI("http://129.242.115.12:4500/"));

			HttpResponse response = movingClient.execute(getTest);

			input = new BufferedReader
			(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");

			while((line = input.readLine()) != null) {
				sb.append(line);
			}

			input.close();
			String result = sb.toString();
			print (result);
			updateHandler.updateFromInput(result);
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
		

	}
	
    @Override
    public void onDestroy() {

        // Tell the user we stopped.
        Toast.makeText(this, "SERVICE CLOSED", Toast.LENGTH_SHORT).show();
    }

	
	private void broadcastUpdate(){
		
		/*Sends Response to application*/
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		sendBroadcast(broadcastIntent);
	}
}
