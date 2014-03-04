package com.example.rest.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Base64;
import android.view.View;

public class MainActivity extends Activity {

	public String apiURL = "https://w1.mercurycert.net/PaymentsAPI";
	public String merchantID = "118725340908147";
	public String password = "xyz";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void clickCreditSale(View view) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("InvoiceNo","2001");
		map.put("RefNo","2001");
		map.put("Memo","REST.android");
		map.put("Purchase","2.00");
		map.put("LaneID", "02");
		map.put("Frequency","OneTime");
		map.put("RecordNo","RecordNumberRequested");
		map.put("EncryptedFormat","MagneSafe");
		map.put("AccountSource","Swiped");
		map.put("EncryptedBlock","2F8248964608156B2B1745287B44CA90A349905F905514ABE3979D7957F13804705684B1C9D5641C");
		map.put("EncryptedKey","9500030000040C200026");

		runTransactionJSON(map, "/Credit/Sale");
	}

	public void clickCreditReturn(View view) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("InvoiceNo","2001");
		map.put("RefNo","2001");
		map.put("Memo","REST.android");
		map.put("Purchase","2.00");
		map.put("LaneID", "02");
		map.put("Frequency","OneTime");
		map.put("RecordNo","RecordNumberRequested");
		map.put("EncryptedFormat","MagneSafe");
		map.put("AccountSource","Swiped");
		map.put("EncryptedBlock","2F8248964608156B2B1745287B44CA90A349905F905514ABE3979D7957F13804705684B1C9D5641C");
		map.put("EncryptedKey","9500030000040C200026");

		runTransactionJSON(map, "/Credit/Return");
	}

	public void clickPrePaidSale(View view) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("InvoiceNo","2001");
		map.put("RefNo","2001");
		map.put("Memo","REST.android");
		map.put("Purchase","2.00");
		map.put("Frequency","OneTime");
		map.put("RecordNo","RecordNumberRequested");
		map.put("EncryptedFormat","MagneSafe");
		map.put("AccountSource","Swiped");
		map.put("EncryptedBlock","C8C8F9536826D5450E734953206E7F4DC6812C6858037F5ABF23D9F83F948AF7");
		map.put("EncryptedKey","9012090B06349B000056");

		runTransactionJSON(map, "/PrePaid/Sale");
	}

	public void clickPrePaidReturn(View view) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("InvoiceNo","2001");
		map.put("RefNo","2001");
		map.put("Memo","REST.android");
		map.put("Purchase","2.00");
		map.put("Frequency","OneTime");
		map.put("RecordNo","RecordNumberRequested");
		map.put("EncryptedFormat","MagneSafe");
		map.put("AccountSource","Swiped");
		map.put("EncryptedBlock","C8C8F9536826D5450E734953206E7F4DC6812C6858037F5ABF23D9F83F948AF7");
		map.put("EncryptedKey","9012090B06349B000056");

		runTransactionJSON(map, "/PrePaid/Return");
	}

	private void runTransactionJSON (Map <String, String> params, String endPoint) {
		try {
			String jsonString = new JSONObject(params).toString();
			String targetURL = apiURL + endPoint;
			String auth64 = Base64.encodeToString((merchantID + ":" + password).getBytes(), Base64.NO_WRAP);
			
			new PaymentsAPITask().execute(jsonString, targetURL, auth64);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class PaymentsAPITask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			
			String resp = postJSONRequest(params[0], params[1], params[2]);
			return resp;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			parseJSONResponse(result);
		}
	}
	
	public static String postJSONRequest(String jsonRequest, String targetURL , String auth) {
		String line;
		String response = "";
		
		try {
			URL url = new URL(targetURL);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", auth);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Length", Integer.toString(jsonRequest.getBytes().length));
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(jsonRequest);
			writer.close();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				while ((line = rd.readLine()) != null) {
					response += line;
				}

				rd.close();	
			}
			else {
				response = "Error: " + conn.getResponseCode() + " " + conn.getResponseMessage();
			}

			conn.disconnect();
		}
		catch (Exception e) {
			response = "Exception: " + e.getMessage();
		}
		
		return response;
	}

	private void parseJSONResponse (String jsonResponse) {
		String message = "";

		try {
			JSONObject jsonObject = new JSONObject(jsonResponse);
			Iterator keys = jsonObject.keys();

			while (keys.hasNext()) {
				String key = (String) keys.next();
				message += key + ": " + jsonObject.getString(key) + ";\n";
			}

			new AlertDialog.Builder(this)  
				.setMessage(message) 
				.setTitle("Response")
				.setNeutralButton(android.R.string.ok, null)
				.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
