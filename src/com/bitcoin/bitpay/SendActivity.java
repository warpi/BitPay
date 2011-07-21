package com.bitcoin.bitpay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendActivity extends Activity implements OnClickListener {

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

		accountAddressTextView = (TextView) findViewById(R.id.account_address1);
		accountAddressTextView.setText(BitPay.account_pkey);
		accountAddressTextView.setOnClickListener(this);

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

			try {

				// Sending money and update balance
				myString = (String) BitPay.downloadHttpsUrl(
						"https://www.instawallet.org/api/v1/w/"
								+ BitPay.account_url + "/payment",
						"address="
								+ BitPay.send_pkey
								+ "&amount="
								+ String.valueOf("" + (long)(Double.parseDouble(this.amountText.getText().toString()) * 100000000)));
					
				Pattern pattern = Pattern.compile("successful\": (.+?)\\}");
				Matcher matcher = pattern.matcher(myString);
				matcher.find();
				if(matcher.group(1).toString().startsWith("true"))	{
					//TODO update balance.
					Toast.makeText(SendActivity.this, "Bitcoins sent",
							Toast.LENGTH_LONG).show();
				}
												
			} catch (Exception e) {

				Toast.makeText(SendActivity.this, "Connection lost",
						Toast.LENGTH_LONG).show();

			}

		}

		else if (R.id.account_address1 == arg0.getId()) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(accountAddressTextView.getText());
			Toast.makeText(SendActivity.this, "Your bitcoin address is copied to clipboard.",
					Toast.LENGTH_LONG).show();
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Uri bitcoinUri = Uri.parse(contents);

				Log.v(TAG,
						"onActivityResult: Content: " + contents + " format: "
								+ format + " Uri: " + bitcoinUri.toString());

				Pattern pattern = Pattern
						.compile("1[a-km-zA-HJ-NP-Z1-9]{24,33}");
				Matcher matcher = pattern.matcher(contents);
				matcher.find();
				BitPay.send_pkey = (String) matcher.group(0); // Get bitcoin
																// address

				try {

					this.amountText.setText(contents.substring(
							contents.indexOf("?amount=") + 8,
							contents.indexOf("&label=")));

				} catch (Exception e) {

				}

				onResume();

			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

	}

	private static final String TAG = "send_tab";

}