package com.bitcoin.bitpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class BitPay extends TabActivity implements OnTouchListener,
		OnTabChangeListener {

	public static String account_url = "";
	public static String account_pkey = "";
	public static String account_balance = "";
	public static String send_pkey = "";
	
	private static String TAB_1_TAG = "Send";
	private static String TAB_2_TAG = "Receive";
	private static String TAB_4_TAG = "Credits";

	private TabHost tabHost;

	String myText;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
    	
		// FIX INSTAWALLET ACCOUNT
		
		SQLiteDatabase myDB;
		myDB = openOrCreateDatabase("bitpay", Context.MODE_PRIVATE, null);
/*
		try {
			myDB.execSQL("drop table bitpay;");
    	} catch (SQLException e) {
    	}
*/
    	try {
    		myDB.execSQL("create table bitpay (_id integer primary key autoincrement, url text not null, pkey text not null, balance text not null);");
    		
    		String myString = (String) downloadHttpsUrl("https://www.instawallet.org/", "");
    		
    		// Get url
    		Pattern pattern = Pattern.compile("BALANCE_URL = \"/b/(.+?)\"");
    		Matcher matcher = pattern.matcher(myString);
    		matcher.find();
    		account_url = (String) matcher.group(1); // Access a submatch group
    		
    		// Get Public key
			pattern = Pattern.compile("<div id=\"addr\">(.+?)</div>");
    		matcher = pattern.matcher(myString);
    		matcher.find();
    		account_pkey = (String) matcher.group(1); // Access a submatch group
    		
			// Balance in BTC
			pattern = Pattern.compile("<span id=\"balance\">(.+?)</span>");
    		matcher = pattern.matcher(myString);
    		matcher.find();
    		account_balance = (String) matcher.group(1); // Access a submatch group
    		
    		//myDB.execSQL("insert into bitpay (url,pkey,balance) values ('"+account_url+"','"+account_pkey+"','"+account_balance+"');");
    		myDB.execSQL("insert into bitpay (url,pkey,balance) values ('7OnRE1Ryna1_aiEXcR8N-Q','1EtGRPWfZoZBSmjscqcLuQaYRvhXguSGpQ','1.48');");
    		
    		Toast.makeText(BitPay.this, "New account created", Toast.LENGTH_LONG).show();
    		
    	} catch (SQLException e) {

    		Toast.makeText(BitPay.this, "Old account loaded", Toast.LENGTH_LONG).show();
    		
    		// READ FROM DATABASE
			Cursor allRows = myDB.rawQuery("select * from bitpay;", null);
			Integer cindex = allRows.getColumnIndex("url");
			allRows.moveToFirst();
			account_url = allRows.getString(cindex);
			//account_url = "";
			
			cindex = allRows.getColumnIndex("pkey");
			allRows.moveToFirst();
			account_pkey = allRows.getString(cindex);
			//account_pkey = "";
			
			cindex = allRows.getColumnIndex("balance");
			allRows.moveToFirst();
			account_balance = allRows.getString(cindex);
    		//account_balance = "0.00";
			
			//Toast.makeText(BitPay.this, account_url, Toast.LENGTH_LONG).show();
			
    	}
    	
    	myDB.close();
    	
    	//BitPay.account_pkey = "13KzwhsJDYuo4xQv3G6CTyRjkagMp7dnrm";
    	
    	//Toast.makeText(BitPay.this, "HELLO", Toast.LENGTH_LONG).show();
		
		/// END
		
		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, SendActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("TAB_1_TAG")
				.setIndicator("Send", res.getDrawable(R.drawable.ic_tab_send))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, ReceiveActivity.class);
		spec = tabHost
				.newTabSpec("TAB_2_TAG")
				.setIndicator("Receive",
						res.getDrawable(R.drawable.ic_tab_receive))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SettingsActivity.class);
		spec = tabHost
				.newTabSpec("TAB_4_TAG")
				.setIndicator("Credits",
						res.getDrawable(R.drawable.ic_tab_settings))
				.setContent(intent);

		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

		tabHost.setOnTouchListener(this);
		tabHost.setOnTabChangedListener(this);
	
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		Log.v(TAG, "tabId: " + tabId);

		if (tabId.equals(BitPay.TAB_1_TAG)) {
			
		} else if (tabId.equals(BitPay.TAB_2_TAG)) {
			
		} else if (tabId.equals(BitPay.TAB_4_TAG)) {
			
		} else {
			
		}
		
	}

	private static final String TAG = "bitpay.java";

    public static String downloadHttpsUrl(String url1, String post1) {

    	// Create a trust manager that does not validate certificate chains
    	TrustManager[] trustAllCerts = new TrustManager[]{
    	    new X509TrustManager() {
    	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    	            return null;
    	        }
    	        public void checkClientTrusted(
    	            java.security.cert.X509Certificate[] certs, String authType) {
    	        }
    	        public void checkServerTrusted(
    	            java.security.cert.X509Certificate[] certs, String authType) {
    	        }
    	    }
    	};

    	// Install the all-trusting trust manager
    	try {
    	    SSLContext sc = SSLContext.getInstance("TLS");
    	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    	} catch (Exception e) {
    	}

    	HttpsURLConnection con = null;
		URL url;
		InputStream is=null;

		try {
			url = new URL(url1);
			con = (HttpsURLConnection) url.openConnection();
			con.setReadTimeout(10000); // milliseconds
			con.setConnectTimeout(15000); // milliseconds

			if (!post1.equals("")){
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
	            // this is were we're adding post data to the request
	            wr.write(post1);
				wr.flush();
				is = con.getInputStream();
				wr.close();
			} else {
				con.setRequestMethod("GET");
				con.setDoInput(true);
				// Start the query
				con.connect();
				is = con.getInputStream();
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!post1.equals("")){
			
			return "";
		
		} else {
		
			BufferedReader rd = new BufferedReader(new InputStreamReader(is), 4096);
			String line;
			StringBuilder sb =  new StringBuilder();
			try {
				while ((line = rd.readLine()) != null) {
						sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				rd.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return sb.toString();
		
		}
		
    }
	
}