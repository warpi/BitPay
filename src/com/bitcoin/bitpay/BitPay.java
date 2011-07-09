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
    		
    		myDB.execSQL("create table bitpay (_id integer primary key autoincrement, url text not null, pkey text not null);");

    	} catch (SQLException e) {
    		
    		// the table is not possible to create
    		
    	}
    	    		
    	try {
    			
    		// READ FROM DATABASE
			Cursor allRows = myDB.rawQuery("select * from bitpay;", null);
			Integer cindex = allRows.getColumnIndex("url");
			allRows.moveToFirst();
			account_url = allRows.getString(cindex);
			
			cindex = allRows.getColumnIndex("pkey");
			allRows.moveToFirst();
			account_pkey = allRows.getString(cindex);
			
			Toast.makeText(BitPay.this, "Stored account loaded", Toast.LENGTH_LONG).show();
			
			try {
			
				// Load balance from internet
				String myString = (String) downloadHttpsUrl("https://www.instawallet.org/w/"+account_url, "");
				
				// Balance in BTC
				Pattern pattern = Pattern.compile("<span id=\"balance\">(.+?)</span>");
	    		Matcher matcher = pattern.matcher(myString);
	    		matcher.find();
	    		account_balance = (String) matcher.group(1); // Access a submatch group
	    		
	    		Toast.makeText(BitPay.this, "Balance updated", Toast.LENGTH_LONG).show();
    		
			} catch(Exception f) {
				
				Toast.makeText(BitPay.this, "Connection lost", Toast.LENGTH_LONG).show();
				
			}
			
    	} catch(Exception e) {
			
    		try {
				
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
        		
        		myDB.execSQL("insert into bitpay (url,pkey) values ('"+account_url+"','"+account_pkey+"');");
        		
        		Toast.makeText(BitPay.this, "New account created", Toast.LENGTH_LONG).show();
        		
    			} catch(Exception g) {
    				
    				Toast.makeText(BitPay.this, "Connection lost", Toast.LENGTH_LONG).show();
    				finish();
    				
    			}
    		
    	}
    	
    	myDB.close();
    			
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

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		return false;
	}

	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		Log.v(TAG, "tabId: " + tabId);

		if (tabId.equals("TAB_4_TAG")) {
			
			// Load balance from internet
			try {
				String myString = (String) downloadHttpsUrl("https://www.instawallet.org/w/"+account_url, "");

				// Balance in BTC
				Pattern pattern = Pattern.compile("<span id=\"balance\">(.+?)</span>");
	    		Matcher matcher = pattern.matcher(myString);
	    		matcher.find();
	    		account_balance = (String) matcher.group(1); // Access a submatch group
				
				Toast.makeText(BitPay.this, "Balance updated", Toast.LENGTH_LONG).show();

			} catch(Exception e) {
				
				Toast.makeText(BitPay.this, "Connection lost", Toast.LENGTH_LONG).show();
				
			}
			
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
				wr.close();
				is = con.getInputStream();
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