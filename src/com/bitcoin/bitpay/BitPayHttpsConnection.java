package com.bitcoin.bitpay;


public class BitPayHttpsConnection {
	
	private String accountURL;
	
	private String acountNumber;	//Bitcoin address
	private String acountBalance;	
	
	

	public BitPayHttpsConnection() {

		// TODO create a new instawallet account
		this.accountURL = "https://www.instawallet.org/w/s8h_vuRAfsaJQdzs-zn0yA";

	}

	public BitPayHttpsConnection(String accountURL) {
		this.accountURL = accountURL;
	}
	
	
	public void updateWalletInfo()	{
		//downloads wallet data from instawallet.
		
		//TODO update acountBalance and accountNumber
		
	}
	
	
	public String getAccountBalance()	{
		return this.acountBalance;
	}
	
	public String getAccountNumber()	{
		return this.acountNumber;
	}
	
	public int sendBitCoins(String receiverAddress)	{
		//sends bitcoins
		
		
		return 0;
	}
	
	
	

}