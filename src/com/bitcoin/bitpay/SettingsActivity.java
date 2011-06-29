package com.bitcoin.bitpay;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	private TextView textView5;
	
	SQLiteDatabase myDB;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setttings_layout);
        
        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText("instawallet.org/w/"+BitPay.account_url);
        
    }
    
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        
        textView5.setText("instawallet.org/w/"+BitPay.account_url);
        
	}
	
	private static final String TAG = "settings_tab";


}