package com.bitcoin.bitpay;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class ReceiveActivity extends Activity {
	
	private TextView accountTextView;
	private TextView balanceTextView;
	
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_layout);
        
		accountTextView = (TextView) findViewById(R.id.account_address2);
		accountTextView.setText(BitPayObj.getBitPayObj().getAccount());
		accountTextView.setMovementMethod(LinkMovementMethod.getInstance());
		
		balanceTextView = (TextView) findViewById(R.id.balance2);
		balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
		
		
        
    }
    
    
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        accountTextView.setText(BitPayObj.getBitPayObj().getAccount());
        balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
        
    }
	
	private static final String TAG = "receive_tab";
	
}