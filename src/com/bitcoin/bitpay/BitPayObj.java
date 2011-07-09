package com.bitcoin.bitpay;

import java.util.Vector;

import android.util.Log;

public class BitPayObj implements Runnable {

	private BitPayAccount account;
	private String balance;
	private String receive_account;
	String myText;
	
	private Vector<BitPayAccount> accountVector;

	private BitPayObj() {
		this.balance = "0.00";
		this.receive_account = "none";
		accountVector = new Vector<BitPayAccount>();
		
		//TODO open sql to get userdata, accountURL....
		//TODO open https connection to get balance and accountNumber.....
		
		new Thread(this).start();
	}

	
	public Vector<BitPayAccount> getAccountVector() {
		return this.accountVector;
	}
	public BitPayAccount getAccount() {
		return this.account;
	}
	public void setAccount(BitPayAccount account) {
		this.account = account;
	}
	public String getReceiverAccount() {
		return this.receive_account;
	}

	public void setReceiveAccount(String receive_account) {
		this.receive_account = receive_account;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	private static BitPayObj bitPayObj;


	
	public static BitPayObj getBitPayObj() {
		if (bitPayObj == null) {
			bitPayObj = new BitPayObj();
		}
		return bitPayObj;
	}

	public void run() {
		
		while(true)	{
			Log.v(TAG, "BitPayObj update thread running");
			
			//TODO update accounts....
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static final String TAG = "BitPayObj";
}