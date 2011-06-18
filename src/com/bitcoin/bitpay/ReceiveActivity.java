package com.bitcoin.bitpay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ReceiveActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Receive tab");
        setContentView(textview);
    }
}