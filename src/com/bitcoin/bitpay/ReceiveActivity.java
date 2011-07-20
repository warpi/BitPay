package com.bitcoin.bitpay;

import java.util.Hashtable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ReceiveActivity extends Activity implements TextWatcher, OnClickListener {

	
	private TextView accountAddressTextView;
	
	private QRCodeWriter qrCodeWriter = new QRCodeWriter();
	private EditText receiveAmountText;

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_layout);

		accountAddressTextView = (TextView) findViewById(R.id.account_address2);
		accountAddressTextView.setText(BitPay.account_pkey);
		accountAddressTextView.setOnClickListener(this);

		
		receiveAmountText = (EditText) findViewById(R.id.receive_amount);
		receiveAmountText.addTextChangedListener(this);
		
		Double recAmount = Double.parseDouble("" + receiveAmountText.getText());
		showQrBitmap(BitPay.account_pkey, recAmount, "test trans", "test");

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
		
	//	balanceTextView.setText(BitPay.account_balance + " BTC");

	}

	private void showQrBitmap(String btcAddress, Double amount, String label,
			String message) {
		Builder builder = new Uri.Builder();
		builder.scheme("bitcoin");
		builder.authority(btcAddress);
		if (amount != null) {
			builder.appendQueryParameter("amount", amount + "");
		}
		if (label != null && label.length() > 0) {
			builder.appendQueryParameter("label", label);
		}
		if (message != null && message.length() > 0) {
			builder.appendQueryParameter("message", message);
		}
		String btcUri = builder.build().toString().replaceAll("//", "");
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>(
				2);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		BitMatrix result;
		try {
			result = qrCodeWriter.encode(btcUri, BarcodeFormat.QR_CODE, 0, 0,
					hints);
			int width = result.getWidth();
			int height = result.getHeight();
			int[] pixels = new int[width * height];
			// All are 0, or black, by default
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {
					pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
				}
			}

			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			BitmapDrawable drawable = new BitmapDrawable(bitmap);
			drawable.setFilterBitmap(false);
			final ImageView qrDisplay = (ImageView) findViewById(R.id.imageView1);
			qrDisplay.setImageDrawable(drawable);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static final String TAG = "receive_tab";

	public void afterTextChanged(Editable s) {
		try	{
			Double recAmount = Double.parseDouble("" + receiveAmountText.getText());
			showQrBitmap(BitPay.account_pkey, recAmount, "test trans", "test");
		}
		catch(NumberFormatException e)	{
			Log.v(TAG, "NumberFormatException");
		}
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		if (R.id.account_address2 == arg0.getId()) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(accountAddressTextView.getText());
			Toast.makeText(ReceiveActivity.this, "Your bitcoin address is copied to clipboard.",
					Toast.LENGTH_LONG).show();
		}
	}

}