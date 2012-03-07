package com.android.movingServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import android.util.Log;

import uit.nfc.ResponseListener;

public class movingResponse implements ResponseListener  {

	@Override
	public void onResponseReceived(HttpResponse response, String message) {
		
		BufferedReader input = null;
		try {
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
			Log.i(getClass().getSimpleName(), "TIMER EVENT PRINT --< in thread >--" + page);
		} catch (IOException e) {
			Log.i(getClass().getSimpleName(), "SomeTHingWrong --< in thread >--");
			// TODO Auto-generated catch block
			e.printStackTrace();
		
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

}
