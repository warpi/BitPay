package com.bitcoin.bitpay;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SendActivity extends Activity implements OnClickListener {
	private TextView accountTextView;
	private TextView balanceTextView;
	private EditText amountText;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_layout);
		
		accountTextView = (TextView) findViewById(R.id.account_address1);
		accountTextView.setText(BitPayObj.getBitPayObj().getAccount());
		accountTextView.setMovementMethod(LinkMovementMethod.getInstance());

		balanceTextView = (TextView) findViewById(R.id.balance1);
		balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
		
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);

		amountText = (EditText)  findViewById(R.id.editText1);
		
	}
    
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        accountTextView.setText(BitPayObj.getBitPayObj().getAccount());
        balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
        
    }	

	@Override
	public void onClick(View arg0) {
		if (R.id.button1 == arg0.getId()) {
			Log.v(TAG, "onClick: button1");
		} else if (R.id.button2 == arg0.getId()) {
			Log.v(TAG, "onClick: Send, send " + this.amountText.getText().toString() + " to " + BitPayObj.getBitPayObj().getAccount());
		}
	}
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus)	{
				accountTextView.setText(BitPayObj.getBitPayObj().getAccount());
	}

	private static final String TAG = "send_tab";

}