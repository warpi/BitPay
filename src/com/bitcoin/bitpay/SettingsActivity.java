package com.bitcoin.bitpay;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener {

	private TextView textView5;
	private TextView textView2;

	SQLiteDatabase myDB;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setttings_layout);

		textView5 = (TextView) findViewById(R.id.textView5);
		textView5.setText("instawallet.org/w/" + BitPay.account_url);
		textView5.setOnClickListener(this);

		try
		{
			textView2 = (TextView) findViewById(R.id.textView2);
		    textView2.setText("BitPay version " + this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
		}
		catch (Exception e)
		{
		    
		}

		
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");

		textView5.setText("instawallet.org/w/" + BitPay.account_url);

	}

	private static final String TAG = "settings_tab";

	@Override
	public void onClick(View arg0) {
		if (R.id.textView5 == arg0.getId()) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(textView5.getText());
			Toast.makeText(SettingsActivity.this, "Your instawallet address is copied to clipboard.",
					Toast.LENGTH_LONG).show();
			
			Log.v(TAG, "" + textView5.getText());
		}
	}

}