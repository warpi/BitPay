package com.bitcoin.bitpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;
import android.widget.Toast;




public class BitPayHttpsConnection {
	
	private String acountBalance;
	private String acountBTCAddress;
	private String accountURL;
	

	public BitPayHttpsConnection() throws Exception {
		
		// create a new instawallet account
	
			String myString = (String) downloadHttpsUrl(
					"https://www.instawallet.org/api/v1/new_wallet", "");

			// Get url
			Pattern pattern = Pattern.compile("wallet_id\": \"(.+?)\"\\}");
			Matcher matcher = pattern.matcher(myString);
			matcher.find();
			accountURL = (String) matcher.group(1); // Access a submatch
														// group

			myString = (String) downloadHttpsUrl(
					"https://www.instawallet.org/api/v1/w/" + accountURL
							+ "/address", "");

			// Get Public key
			pattern = Pattern.compile("address\": \"(.+?)\"\\}");
			matcher = pattern.matcher(myString);
			matcher.find();
			acountBTCAddress = (String) matcher.group(1); // Access a submatch
														// group

			myString = (String) downloadHttpsUrl(
					"https://www.instawallet.org/api/v1/w/" + accountURL
							+ "/balance", "");

			// Balance in BTC
			pattern = Pattern.compile("balance\": (.+?)\\}");
			matcher = pattern.matcher(myString);
			matcher.find();
			acountBalance = String.valueOf(Double.valueOf(matcher
					.group(1).toString()) / 100000000); // Access a submatch
														// group
	
	}

	public BitPayHttpsConnection(String accountURL, String acountBTCAddress) {
		this.accountURL = accountURL;
		this.acountBTCAddress = acountBTCAddress;		
	}
	
	
	public boolean updateWalletInfo()	{
		//downloads wallet data from instawallet.		
		//TODO update acountBalance
		
		try {

			String myString = (String) downloadHttpsUrl(
					"https://www.instawallet.org/api/v1/w/" + accountURL
							+ "/balance", "");

			// Balance in BTC
			Pattern pattern = Pattern.compile("balance\": (.+?)\\}");
			Matcher matcher = pattern.matcher(myString);
			matcher.find();
			acountBalance = String.valueOf(Double.valueOf(matcher
					.group(1).toString()) / 100000000); // Access a submatch
														// group
			return true;

		} catch (Exception e) {

			return false;

		}
		
		
	}
	
	
	public String getAccountBalance()	{
		return this.acountBalance;
	}
	
	public String getAcountBTCAddress()	{
		return this.acountBTCAddress;
	}

	public String getAccountURL()	{
		return this.accountURL;
	}
	
	
	public boolean sendBitCoins(String sendAcountBTCAddress, String sendAmount)	{
		//sends bitcoins
		String myString;
		
		try {

			// Sending money and update balance
			myString = (String) downloadHttpsUrl(
					"https://www.instawallet.org/api/v1/w/"
							+ accountURL + "/payment",
					"address="
							+ sendAcountBTCAddress
							+ "&amount="
							+ sendAmount);
			Pattern pattern = Pattern.compile("successful\": (.+?)\\}");
			Matcher matcher = pattern.matcher(myString);
			matcher.find();
			if(matcher.group(1).toString().startsWith("true"))	{
				return true;
			}
											
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	
	
	private static String downloadHttpsUrl(String url1, String post1) {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}

		HttpsURLConnection con = null;
		URL url;
		InputStream is = null;

		try {
			url = new URL(url1);
			con = (HttpsURLConnection) url.openConnection();
			con.setReadTimeout(10000); // milliseconds
			con.setConnectTimeout(15000); // milliseconds

			if (!post1.equals("")) {
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(
						con.getOutputStream());
				// this is were we're adding post data to the request
				wr.write(post1);
				wr.flush();
				wr.close();
				is = con.getInputStream();
			} else {
				con.setRequestMethod("GET");
				con.setDoInput(true);
				// Start the query
				con.connect();
				is = con.getInputStream();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader rd = new BufferedReader(new InputStreamReader(is), 4096);
		String line;
		StringBuilder sb = new StringBuilder();
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