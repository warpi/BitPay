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
	
	private TextView receiveAccountTextView;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_layout);
		
		accountTextView = (TextView) findViewById(R.id.account_address1);
		accountTextView.setText(BitPayObj.getBitPayObj().getAccount());
		accountTextView.setMovementMethod(LinkMovementMethod.getInstance());

		balanceTextView = (TextView) findViewById(R.id.balance1);
		balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
		
		
		receiveAccountTextView = (TextView) findViewById(R.id.receive_account);
		receiveAccountTextView.setText(BitPayObj.getBitPayObj().getReceiverAccount());
		
		
		Button button1 = (Button) findViewById(R.id.from_camera_button);
		button1.setOnClickListener(this);

		Button button2 = (Button) findViewById(R.id.send_button);
		button2.setOnClickListener(this);

		amountText = (EditText)  findViewById(R.id.input_amount);
		
	}
    
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        accountTextView.setText(BitPayObj.getBitPayObj().getAccount());
        balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
        receiveAccountTextView.setText(BitPayObj.getBitPayObj().getReceiverAccount());
        
    }	

	@Override
	public void onClick(View arg0) {
		if (R.id.from_camera_button == arg0.getId()) {
			Log.v(TAG, "onClick: from camera button");
		} else if (R.id.send_button == arg0.getId()) {
			Log.v(TAG, "onClick: Send, send " + this.amountText.getText().toString() + " to " + BitPayObj.getBitPayObj().getReceiverAccount());
		}
	}
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus)	{
				accountTextView.setText(BitPayObj.getBitPayObj().getAccount());
	}

	private static final String TAG = "send_tab";

}