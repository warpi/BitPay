package com.bitcoin.bitpay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SendActivity extends Activity implements OnClickListener{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_layout);

        
        Spinner spinner = (Spinner) findViewById(R.id.account_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(this);

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(this);



        
        
    }

	@Override
	public void onClick(View arg0) {
		if(R.id.button1 == arg0.getId())	{
			Log.v(TAG, "onClick: button1");
		}
		else if(R.id.button2 == arg0.getId())	{
			Log.v(TAG, "onClick: button2");
		}
			
		
	}
	
	private static final String TAG = "test";

}