package com.bitcoin.bitpay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendActivity extends Activity implements OnClickListener {
	private TextView accountNameTextView;
	private TextView accountAddressTextView;
	private TextView balanceTextView;
	private EditText amountText;

	Pattern pattern;
	Matcher matcher;
	String myString;
	
	private TextView receiveAccountTextView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_layout);

		
		accountNameTextView = (TextView) findViewById(R.id.account_name1);
		accountNameTextView.setText("InstaWallet.org");

		accountAddressTextView = (TextView) findViewById(R.id.account_address1);
		accountAddressTextView.setText(BitPay.account_pkey);

		balanceTextView = (TextView) findViewById(R.id.balance1);
		balanceTextView.setText(BitPay.account_balance + " BTC");

		receiveAccountTextView = (TextView) findViewById(R.id.receive_account);
		receiveAccountTextView.setText(BitPay.send_pkey);

		Button button1 = (Button) findViewById(R.id.from_camera_button);
		button1.setOnClickListener(this);

		Button button2 = (Button) findViewById(R.id.send_button);
		button2.setOnClickListener(this);

		amountText = (EditText) findViewById(R.id.input_amount);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
		
		balanceTextView.setText(BitPay.account_balance + " BTC");
		receiveAccountTextView.setText(BitPay.send_pkey);

	}

	@Override
	public void onClick(View arg0) {
		if (R.id.from_camera_button == arg0.getId()) {
			Log.v(TAG, "onClick: from camera button");

			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			try {
				startActivityForResult(intent, 0);
			} catch (Exception e) {
				Toast.makeText(this, "Install the barcode scanner!!!",
						Toast.LENGTH_LONG).show();
			}
			
		} else if (R.id.send_button == arg0.getId()) {
			Log.v(TAG, "onClick: Send, send "
					+ this.amountText.getText().toString() + " to "
					+ BitPayObj.getBitPayObj().getReceiverAccount());
		
			Toast.makeText(SendActivity.this, BitPay.send_pkey, Toast.LENGTH_LONG).show();
			Toast.makeText(SendActivity.this, this.amountText.getText().toString(), Toast.LENGTH_LONG).show();

			//BitPay.send_pkey = "13KzwhsJDYuo4xQv3G6CTyRjkagMp7dnrm";
			
			// Sending money and update balance
    		myString = (String) BitPay.downloadHttpsUrl("https://www.instawallet.org/w/"+BitPay.account_url,"address="+BitPay.send_pkey+"&amount="+this.amountText.getText().toString());
    		//myString = (String) BitPay.downloadHttpsUrl("https://www.instawallet.org/w/"+BitPay.account_url,"");
			
			// Balance in BTC
			pattern = Pattern.compile("<span id=\"balance\">(.+?)</span>");
    		matcher = pattern.matcher(myString);
    		matcher.find();
    		BitPay.account_balance = (String) matcher.group(1); // Access a submatch group
    		balanceTextView.setText(BitPay.account_balance + " BTC");

		}
	}
	
	
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Uri bitcoinUri = Uri.parse(contents);
                
                Log.v(TAG, "onActivityResult: Content: " + contents + " format: " + format + " Uri: " + bitcoinUri.toString());
                
                BitPay.send_pkey = contents.substring(contents.indexOf("bitcoin:") + 8, contents.indexOf("?amount="));
                this.amountText.setText(contents.substring(contents.indexOf("?amount=") + 8, contents.indexOf("&label=")));
                
                onResume();
                //ConfirmPay.callMe(this, bitcoinUri);
                
                
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // Handle cancel
            }
        }
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		//accountAddressTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounAddress());
	}

	private static final String TAG = "send_tab";

}