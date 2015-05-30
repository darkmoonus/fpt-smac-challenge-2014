package uet.invincible.webservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Parrams {
	public static List<NameValuePair> loginParrams(String user, String password) {
		List<NameValuePair> mParrams = new ArrayList<NameValuePair>();
		mParrams.add(new BasicNameValuePair("user", user));
		mParrams.add(new BasicNameValuePair("password", password));
		return mParrams;
	}
	public static List<NameValuePair> tokenValidate(String token) {
		List<NameValuePair> mParrams = new ArrayList<NameValuePair>();
		mParrams.add(new BasicNameValuePair("token", token));
		return mParrams;
	}
}
