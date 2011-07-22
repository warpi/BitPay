package com.bitcoin.bitpay;

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

	private TabHost tabHost;
	
	private BitPayHttpsConnection bitPayHttpsConnection;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String account_url = "";
		String account_pkey = "";

		// FIX INSTAWALLET ACCOUNT

		SQLiteDatabase myDB;
		myDB = openOrCreateDatabase("bitpay", Context.MODE_PRIVATE, null);
		/*
		 * try { myDB.execSQL("drop table bitpay;"); } catch (SQLException e) {
		 * }
		 */
		try {

			myDB.execSQL("create table bitpay (_id integer primary key autoincrement, url text not null, pkey text not null);");

		} catch (SQLException e) {

			// the table is not possible to create

		}

		try {

			// READ FROM DATABASE
			Cursor allRows = myDB.rawQuery("select * from bitpay;", null);
			if (allRows.getCount() > 0) {
				// bitpay db exists.
				Integer cindex = allRows.getColumnIndex("url");

				allRows.moveToFirst();
				account_url = allRows.getString(cindex);

				cindex = allRows.getColumnIndex("pkey");
				allRows.moveToFirst();
				account_pkey = allRows.getString(cindex);

				Toast.makeText(BitPay.this, "Stored account loaded",
						Toast.LENGTH_LONG).show();

				// create bitPayHttpsConnection object
				bitPayHttpsConnection = new BitPayHttpsConnection(account_url,
						account_pkey);

			} else {
				try {

					// create bitPayHttpsConnection object and new account
					bitPayHttpsConnection = new BitPayHttpsConnection();

					myDB.execSQL("insert into bitpay (url,pkey) values ('"
							+ bitPayHttpsConnection.getAccountURL() + "','"
							+ bitPayHttpsConnection.getAcountBTCAddress()
							+ "');");

					Toast.makeText(BitPay.this, "New account created",
							Toast.LENGTH_LONG).show();

				} catch (Exception g) {

					Toast.makeText(BitPay.this, "Connection lost",
							Toast.LENGTH_LONG).show();
					finish();

				}

			}

			// Load balance from Internet
			while (!bitPayHttpsConnection.updateWalletInfo()) {
				Toast.makeText(BitPay.this,
						"Balance update failed, retry in 1 sec.",
						Toast.LENGTH_LONG).show();
				Thread.sleep(1000);
			}

		} catch (Exception e) {

			Toast.makeText(BitPay.this, "Unable to open bitpay db.",
					Toast.LENGTH_LONG).show();
			finish();

		}

		myDB.close();
		
		//Create the bitPayObj
		BitPayObj bitBayObj;
		bitBayObj = BitPayObj.getBitPayObj();
		bitBayObj.setBitPayHttpsConnection(this.bitPayHttpsConnection);
		

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

		if (tabId.equals("TAB_2_TAG")) {

		}

		if (tabId.equals("TAB_4_TAG")) {

			// Load balance from Internet
			while (!bitPayHttpsConnection.updateWalletInfo()) {
				Toast.makeText(BitPay.this,
						"Balance update failed, retry in 1 sec.",
						Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Toast.makeText(BitPay.this, "Balance updated", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	private static final String TAG = "bitpay.java";

}