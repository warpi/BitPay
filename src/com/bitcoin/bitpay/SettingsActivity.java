package com.bitcoin.bitpay;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SettingsActivity extends Activity implements OnItemSelectedListener {
	private TextView accountNameTextView;
	private TextView accountAddressTextView;
	
	private Spinner account_spinner; 
	private TextView balanceTextView;
	private ArrayAdapter<CharSequence> acount_array_adapter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setttings_layout);
        
		accountNameTextView = (TextView) findViewById(R.id.account_name4);
		accountNameTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounName());
		accountNameTextView.setMovementMethod(LinkMovementMethod.getInstance());

		accountAddressTextView = (TextView) findViewById(R.id.account_address4);
		accountAddressTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounAddress());
		accountAddressTextView.setMovementMethod(LinkMovementMethod.getInstance());

        account_spinner = (Spinner) findViewById(R.id.account_spinner);
        
        //FIXME
        acount_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        for(int i = 0; i < BitPayObj.getBitPayObj().getAccountVector().size(); i++)	{
        	acount_array_adapter.add(BitPayObj.getBitPayObj().getAccountVector().get(i).getAccounName());	
        }
        
        acount_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        account_spinner.setAdapter(acount_array_adapter);
        account_spinner.setOnItemSelectedListener(this);
		
		balanceTextView = (TextView) findViewById(R.id.balance4);
		balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
        
		Button button1 = (Button)findViewById(R.id.button1);
        Button.OnClickListener myListener = new Button.OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				
				Toast.makeText(SettingsActivity.this, "HELLO", Toast.LENGTH_LONG).show();
				
				BitPayObj.getBitPayObj().getAccountVector().add(new BitPayAccount("My Account_1", "174MgqAd2NqnwAcpajCQBo3AhwaEQDCUT1", "https://www.instawallet.org/w/9ODM4oWAiq9oXE4Ji6qTsg"));
				
			}
			
        };
        
        button1.setOnClickListener(myListener);
		
        Button button2 = (Button)findViewById(R.id.button2);
        Button.OnClickListener myListener2 = new Button.OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				
				Toast.makeText(SettingsActivity.this, "HELLO", Toast.LENGTH_LONG).show();
				
				BitPayObj.getBitPayObj().getAccountVector().remove(new BitPayAccount("My Account_1", "174MgqAd2NqnwAcpajCQBo3AhwaEQDCUT1", "https://www.instawallet.org/w/9ODM4oWAiq9oXE4Ji6qTsg"));
				
			}
			
        };
        
        button2.setOnClickListener(myListener2);
       
    }
    
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        balanceTextView.setText(BitPayObj.getBitPayObj().getBalance() + " BTC");
        
		accountAddressTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounAddress());
		accountNameTextView.setText(BitPayObj.getBitPayObj().getAccount().getAccounName());
        
        acount_array_adapter.clear();
        for(int i = 0; i < BitPayObj.getBitPayObj().getAccountVector().size(); i++)	{
        	acount_array_adapter.add(BitPayObj.getBitPayObj().getAccountVector().get(i).getAccounName());	
        }
    }
	
	private static final String TAG = "settings_tab";
	@Override
	
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		BitPayObj.getBitPayObj().setAccount(BitPayObj.getBitPayObj().getAccountVector().get(account_spinner.getSelectedItemPosition()));
		this.onResume();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}