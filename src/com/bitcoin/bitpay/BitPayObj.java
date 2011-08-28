package com.bitcoin.bitpay;

import android.util.Log;

public class BitPayObj implements Runnable {

	private BitPayHttpsConnection bitPayHttpsConnection;

	private String sendAcountBTCAddress;

	private BitPayObj() {
		
		this.sendAcountBTCAddress = "";
		
		new Thread(this).start();
	}

	private static BitPayObj bitPayObj;
	
	public static BitPayObj getBitPayObj() {
		if (bitPayObj == null) {
			bitPayObj = new BitPayObj();
		}
		return bitPayObj;
	}
	
	public void setBitPayHttpsConnection(BitPayHttpsConnection bitPayHttpsConnection)	{
		this.bitPayHttpsConnection = bitPayHttpsConnection;
	}

	public void run() {
		
		while(true)	{
			Log.v(TAG, "BitPayObj update thread running ");
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public boolean updateWalletInfo()	{
		return this.bitPayHttpsConnection.updateWalletInfo();
	}
	
	public String getAccountBalance()	{
		return this.bitPayHttpsConnection.getAccountBalance();
	}
	
	public String getAcountBTCAddress()	{
		return this.bitPayHttpsConnection.getAcountBTCAddress();
	}

	public String getSendAcountBTCAddress()	{
		return this.sendAcountBTCAddress;
	}
	public void setSendAcountBTCAddress(String sendAcountBTCAddress)	{
		this.sendAcountBTCAddress = sendAcountBTCAddress;
	}
	public String getAccountURL()	{
		return this.bitPayHttpsConnection.getAccountURL();
	}
	
	public boolean sendBTC(String sendAmount, boolean useGreenAddress)	{
		return this.bitPayHttpsConnection.sendBitCoins(this.sendAcountBTCAddress, sendAmount, useGreenAddress);
	}
	
	private static final String TAG = "BitPayObj";
}