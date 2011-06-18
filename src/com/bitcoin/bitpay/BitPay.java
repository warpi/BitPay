package com.bitcoin.bitpay;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class BitPay extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, SendActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("send")
				.setIndicator("Send", res.getDrawable(R.drawable.ic_tab_send))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, ReceiveActivity.class);
		spec = tabHost
				.newTabSpec("receive")
				.setIndicator("Receive",
						res.getDrawable(R.drawable.ic_tab_receive))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, HistoryActivity.class);
		spec = tabHost
				.newTabSpec("history")
				.setIndicator("History", res.getDrawable(R.drawable.ic_tab_history))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, HistoryActivity.class);
		spec = tabHost
				.newTabSpec("settings")
				.setIndicator("Settings", res.getDrawable(R.drawable.ic_tab_history))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}