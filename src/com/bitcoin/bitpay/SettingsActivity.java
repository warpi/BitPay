package com.bitcoin.bitpay;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends Activity {

	
	SQLiteDatabase myDB;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setttings_layout);
       
    }
    
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        
	}
	
	private static final String TAG = "settings_tab";


}