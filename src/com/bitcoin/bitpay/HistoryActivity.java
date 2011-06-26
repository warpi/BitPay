package com.bitcoin.bitpay;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class HistoryActivity extends Activity {
	private TextView accountNameTextView;
	private TextView accountAddressTextView;
	
	private TextView balanceTextView;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        
		accountNameTextView = (TextView) findViewById(R.id.account_name3);
		accountNameTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounName());
		accountNameTextView.setMovementMethod(LinkMovementMethod.getInstance());

		accountAddressTextView = (TextView) findViewById(R.id.account_address3);
		accountAddressTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounAddress());
		accountAddressTextView.setMovementMethod(LinkMovementMethod.getInstance());

		
		balanceTextView = (TextView) findViewById(R.id.balance3);
		balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
        
    }
    
    
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
		accountAddressTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounAddress());
		accountNameTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounName());
        balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
    }
	
	private static final String TAG = "history_tab";
}