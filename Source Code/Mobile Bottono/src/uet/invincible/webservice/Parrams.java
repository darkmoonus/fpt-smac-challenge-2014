package uet.invincible.webservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Parrams {
	public static List<NameValuePair> getAllBots(String token) {
		List<NameValuePair> mParrams = new ArrayList<NameValuePair>();
		mParrams.add(new BasicNameValuePair("token", token));
		return mParrams;
	}
	public static List<NameValuePair> getBotDetails(String botID, String token) {
		List<NameValuePair> mParrams = new ArrayList<NameValuePair>();
		mParrams.add(new BasicNameValuePair("botID", botID));
		mParrams.add(new BasicNameValuePair("token", token));
		return mParrams;
	}
	public static List<NameValuePair> botChatAIP(String botID, String data, String token) {
		List<NameValuePair> mParrams = new ArrayList<NameValuePair>();
		mParrams.add(new BasicNameValuePair("botID", botID));
		mParrams.add(new BasicNameValuePair("data", data));
		mParrams.add(new BasicNameValuePair("token", token));
		return mParrams;
	}
	public static List<NameValuePair> botChatAPI() {
		List<NameValuePair> mParrams = new ArrayList<NameValuePair>();
		return mParrams;
	}
	
	public static List<NameValuePair> wikiAPI(String input) {
		List<NameValuePair> mParrams = new ArrayList<NameValuePair>();
		mParrams.add(new BasicNameValuePair("input", input));
		mParrams.add(new BasicNameValuePair("filter", "%22stopword%22"));
		return mParrams;
	}
	public static List<NameValuePair> nothing() {
		List<NameValuePair> mParrams = new ArrayList<NameValuePair>();
		return mParrams;
	}
}
