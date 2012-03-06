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

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class HttpMovingClient extends Service {

	private final IBinder mBinder = new MyBinder();
	private static final long UPDATE_INTERVAL = 5000;
	private Timer timer = new Timer();
	
	private MovingDbAdapter mDbHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		executeHttpGetTimer();
	}

	public void executeHttpGetTimer(){
		timer.scheduleAtFixedRate(new TimerTask() {		
			@Override
			public void run() {
				try {
					executeHttpGet();
				} catch (Exception e) {
					print("ERROR IN TIMER");
					e.printStackTrace();
				}
			}
		}, 0, UPDATE_INTERVAL);
	}


	public void executeHttpGet() throws Exception {

		BufferedReader input = null;
		try {
			HttpClient movingClient = new DefaultHttpClient();
			
			HttpGet getTest = new HttpGet(new URI("http://129.242.115.12:4500/login"));

			HttpResponse response = movingClient.execute(getTest);

			input = new BufferedReader
			(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");

			while((line = input.readLine()) != null) {
				sb.append(line + NL);
			}

			input.close();
			String page = sb.toString();
			print (page);

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
		return mBinder;
	}

	public class MyBinder extends Binder {
		HttpMovingClient getService() {
			return HttpMovingClient.this;
		} 
	}

	private void print (String Text){
		Log.i(getClass().getSimpleName(), "TIMER EVENT PRINT --< FUCK YEAH >--" + Text);
		//Toast.makeText(this, Text, 1000).show();
	}

}
