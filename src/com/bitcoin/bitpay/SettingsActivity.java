package com.bitcoin.bitpay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnItemSelectedListener {
	
	private Spinner account_spinner; 
	private TextView balanceTextView;
	private ArrayAdapter<CharSequence> acount_array_adapter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setttings_layout);
        
        account_spinner = (Spinner) findViewById(R.id.account_spinner);
        acount_array_adapter = ArrayAdapter.createFromResource(
                this, R.array.account_array, android.R.layout.simple_spinner_item);
        acount_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        account_spinner.setAdapter(acount_array_adapter);
        account_spinner.setOnItemSelectedListener(this);
        
		
		balanceTextView = (TextView) findViewById(R.id.balance4);
		balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
        
       
    }
    
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
    }
	
	private static final String TAG = "settings_tab";
	@Override
	
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		BitPayObj.getBitPayObj().setAccount(acount_array_adapter.getItem(account_spinner.getSelectedItemPosition()).toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}