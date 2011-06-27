package com.bitcoin.bitpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

public class BitPayObj implements Runnable {

	private BitPayAccount account;
	private String balance;
	private String receive_account;

	private Vector<BitPayAccount> accountVector;

	private BitPayObj() {
		this.balance = "0.00";
		this.receive_account = "none";
		accountVector = new Vector<BitPayAccount>();
		
		//TODO open sql to get userdata, accountURL....
		//TODO open https connection to get balance and accountNumber.....
		
		//FIXME
		this.accountVector.add(new BitPayAccount("My Account_1", "174MgqAd2NqnwAcpajCQBo3AhwaEQDCUT1", "https://www.instawallet.org/w/9ODM4oWAiq9oXE4Ji6qTsg"));
		this.accountVector.add(new BitPayAccount("My Account_2", "174MgqAd2NqnwAcpajCQBo3AhwaEQDCUT1", "https://www.instawallet.org/w/9ODM4oWAiq9oXE4Ji6qTsg"));
		this.account = this.accountVector.get(0);	

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

    private String downloadHttpsUrl(String url1, String post1) {

    	// Create a trust manager that does not validate certificate chains
    	TrustManager[] trustAllCerts = new TrustManager[]{
    	    new X509TrustManager() {
    	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    	            return null;
    	        }
    	        public void checkClientTrusted(
    	            java.security.cert.X509Certificate[] certs, String authType) {
    	        }
    	        public void checkServerTrusted(
    	            java.security.cert.X509Certificate[] certs, String authType) {
    	        }
    	    }
    	};

    	// Install the all-trusting trust manager
    	try {
    	    SSLContext sc = SSLContext.getInstance("TLS");
    	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    	} catch (Exception e) {
    	}

    	HttpsURLConnection con = null;
		URL url;
		InputStream is=null;

		try {
			url = new URL(url1);
			con = (HttpsURLConnection) url.openConnection();
			con.setReadTimeout(10000); // milliseconds
			con.setConnectTimeout(15000); // milliseconds

			if (!post1.equals("")){
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
	            // this is were we're adding post data to the request
	            wr.write(post1);
				wr.flush();
				is = con.getInputStream();
				wr.close();
			} else {
				con.setRequestMethod("GET");
				con.setDoInput(true);
				// Start the query
				con.connect();
				is = con.getInputStream();
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!post1.equals("")){
			
			return "";
		
		} else {
		
			BufferedReader rd = new BufferedReader(new InputStreamReader(is), 4096);
			String line;
			StringBuilder sb =  new StringBuilder();
			try {
				while ((line = rd.readLine()) != null) {
						sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				rd.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return sb.toString();
		
		}
		
    }
	
	public static BitPayObj getBitPayObj() {
		if (bitPayObj == null) {
			bitPayObj = new BitPayObj();
		}
		return bitPayObj;
	}

	@Override
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