package com.bitcoin.bitpay;



public class BitPayObj	{
	
	private String account;
	private String balance;
	
	private BitPayObj()	{
		this.account = "init_accounts";
		this.balance = "0.00";	
	}
	
	public String getAccount()	{
		return this.account;
	}
	
	public void setAccount(String account)	{
		this.account = account;
	}
	
	public String getBalance()	{
		return this.balance;
	}
	
	public void setBalance(String balance)	{
		this.balance = balance;
	}
	
	private static BitPayObj bitPayObj;
	
	public static BitPayObj getBitPayObj()	{
		if(bitPayObj == null)	{
			bitPayObj = new BitPayObj();
		}
		return bitPayObj;
	}	
}