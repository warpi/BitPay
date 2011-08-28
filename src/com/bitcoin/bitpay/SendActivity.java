package com.bitcoin.bitpay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendActivity extends Activity implements OnClickListener {

	private TextView accountAddressTextView;
	private TextView balanceTextView;
	private EditText amountText;
	private CheckBox useGreenAddress;
	private ProgressDialog gettingWebPageDialog;

	Pattern pattern;
	Matcher matcher;
	String myString;

	private TextView receiveAccountTextView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_layout);

		accountAddressTextView = (TextView) findViewById(R.id.account_address1);
		accountAddressTextView.setText(BitPayObj.getBitPayObj()
				.getAcountBTCAddress());
		accountAddressTextView.setOnClickListener(this);

		balanceTextView = (TextView) findViewById(R.id.balance1);
		balanceTextView.setText(BitPayObj.getBitPayObj().getAccountBalance()
				+ " BTC");

		receiveAccountTextView = (TextView) findViewById(R.id.receive_account);
		
		receiveAccountTextView.setText(BitPayObj.getBitPayObj()
				.getSendAcountBTCAddress());

		Button button1 = (Button) findViewById(R.id.from_camera_button);
		button1.setOnClickListener(this);

		Button button2 = (Button) findViewById(R.id.send_button);
		button2.setOnClickListener(this);

		Button button3 = (Button) findViewById(R.id.updateBalanceButton);
		button3.setOnClickListener(this);

		amountText = (EditText) findViewById(R.id.input_amount);

		useGreenAddress = (CheckBox) findViewById(R.id.green_address);

	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");

		balanceTextView.setText(BitPayObj.getBitPayObj().getAccountBalance()
				+ " BTC");
		receiveAccountTextView.setText(BitPayObj.getBitPayObj()
				.getSendAcountBTCAddress());

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
					+ BitPayObj.getBitPayObj().getSendAcountBTCAddress());

			// TODO make a confirm dialog, showing the receiver address and
			// amount.

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Send " + this.amountText.getText().toString() + " BTC to "
							+ receiveAccountTextView.getText() + "?")
					.setCancelable(false)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {

									dialog.cancel();
									dialog.dismiss();
									


									gettingWebPageDialog = new ProgressDialog(
											SendActivity.this);
									gettingWebPageDialog
											.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
									gettingWebPageDialog
											.setMessage("Sending bitcoins");
									gettingWebPageDialog.setCancelable(false);
									gettingWebPageDialog.show();
									gettingWebPageDialog.setProgress(10);

									new Thread() {
										public void run() {
											if(sendBTCDialog())	{
												gettingWebPageDialog.dismiss();
												Toast.makeText(SendActivity.this, "Bitcoins sent.",
														Toast.LENGTH_LONG).show();	
												
											}
											else {
												gettingWebPageDialog.dismiss();
												Toast.makeText(SendActivity.this, "Transaction Error please check balance.",
														Toast.LENGTH_LONG).show();
											}
											
										}
									}.start();

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			
			// TODO redraw
		} else if (R.id.updateBalanceButton == arg0.getId()) {
			// Load balance from Internet

			while (!BitPayObj.getBitPayObj().updateWalletInfo()) {
				Toast.makeText(SendActivity.this,
						"Balance update failed, retry in 1 sec.",
						Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			balanceTextView.setText(BitPayObj.getBitPayObj()
					.getAccountBalance() + " BTC");

			Toast.makeText(SendActivity.this, "Balance updated.",
					Toast.LENGTH_LONG).show();

		}

		else if (R.id.account_address1 == arg0.getId()) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(accountAddressTextView.getText());
			Toast.makeText(SendActivity.this,
					"Your bitcoin address is copied to clipboard.",
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

				BitPayObj.getBitPayObj().setSendAcountBTCAddress(
						(String) matcher.group(0)); // Get bitcoin
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
		
		balanceTextView.setText(BitPayObj.getBitPayObj().getAccountBalance()
				+ " BTC");
		receiveAccountTextView.setText(BitPayObj.getBitPayObj()
				.getSendAcountBTCAddress());

	}

	private boolean sendBTCDialog() {

		if (BitPayObj.getBitPayObj().sendBTC(
				String.valueOf(""
						+ (long) (Double.parseDouble(amountText.getText()
								.toString()) * 100000000)), useGreenAddress.isChecked())) {

			gettingWebPageDialog.setProgress(50);

			// Load balance from Internet
			while (!BitPayObj.getBitPayObj().updateWalletInfo()) {
				gettingWebPageDialog.setProgress(75);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			gettingWebPageDialog.setProgress(100);
			return true;
			
		} else {
			gettingWebPageDialog.setProgress(100);
			return false;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		balanceTextView.setText(BitPayObj.getBitPayObj().getAccountBalance()
				+ " BTC");
		receiveAccountTextView.setText(BitPayObj.getBitPayObj()
				.getSendAcountBTCAddress());

	}

	private static final String TAG = "send_tab";


}