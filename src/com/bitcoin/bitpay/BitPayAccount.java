package com.bitcoin.bitpay;

public class BitPayAccount {

	private String accountName;
	private String accountAddress;
	private String accountURL;
	
	public BitPayAccount(String accountName, String accountAddress, String accountURL)	{
		this.accountName = accountName;
		this.accountAddress = accountAddress;
		this.accountURL = accountURL;
	}
	
	public String getAccounName()	{
		return this.accountName;
	}
	
	public void setAccountName(String accountName)	{
		this.accountName = accountName;
	}
	
	public String getAccounAddress()	{
		return this.accountAddress;
	}
	
	public void setAccountAddress(String accountAddress)	{
		this.accountAddress = accountAddress;
	}
	public String getAccounURL()	{
		return this.accountURL;
	}
	
	public void setAccountURL(String accountURL)	{
		this.accountURL = accountURL;
	}
	
}
