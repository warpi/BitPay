package com.bitcoin.bitpay;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class BitPay extends TabActivity implements OnTouchListener,
		OnTabChangeListener {

	private static String TAB_1_TAG = "send";
	private static String TAB_2_TAG = "receive";
	private static String TAB_3_TAG = "history";
	private static String TAB_4_TAG = "settings";

	private TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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

		intent = new Intent().setClass(this, HistoryActivity.class);
		spec = tabHost
				.newTabSpec("TAB_3_TAG")
				.setIndicator("History",
						res.getDrawable(R.drawable.ic_tab_history))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SettingsActivity.class);
		spec = tabHost
				.newTabSpec("TAB_4_TAG")
				.setIndicator("Settings",
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

		} else if (tabId.equals(BitPay.TAB_3_TAG)) {

		} else if (tabId.equals(BitPay.TAB_4_TAG)) {

		}
	}

	private static final String TAG = "bitpay.java";

}