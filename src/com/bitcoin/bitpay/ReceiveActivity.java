package com.bitcoin.bitpay;

import java.util.Hashtable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//import com.google.bitcoin.core.Address;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ReceiveActivity extends Activity implements TextWatcher {

	
	private TextView accountNameTextView;
	private TextView accountAddressTextView;
	
	private TextView balanceTextView;
	private QRCodeWriter qrCodeWriter = new QRCodeWriter();
	private EditText receiveAmountText;

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_layout);

		accountNameTextView = (TextView) findViewById(R.id.account_name2);
		accountNameTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounName());
		accountNameTextView.setMovementMethod(LinkMovementMethod.getInstance());

		accountAddressTextView = (TextView) findViewById(R.id.account_address2);
		accountAddressTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounAddress());
		accountAddressTextView.setMovementMethod(LinkMovementMethod.getInstance());

		balanceTextView = (TextView) findViewById(R.id.balance2);
		balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
		
		receiveAmountText = (EditText) findViewById(R.id.receive_amount);
		receiveAmountText.addTextChangedListener(this);
		
		
		Double recAmount = Double.parseDouble("" + receiveAmountText.getText());
		showQrBitmap("174MgqAd2NqnwAcpajCQBo3AhwaEQDCUT1", recAmount, "test trans", "test");

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
		accountAddressTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounAddress());
		accountNameTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounName());
		balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");

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

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		Double recAmount = Double.parseDouble("" + receiveAmountText.getText());
		showQrBitmap("174MgqAd2NqnwAcpajCQBo3AhwaEQDCUT1", recAmount, "test trans", "test");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}