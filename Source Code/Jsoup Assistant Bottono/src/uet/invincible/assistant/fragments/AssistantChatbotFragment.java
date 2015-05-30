package uet.invincible.assistant.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fpt.robot.tts.RobotTextToSpeech;

import uet.invincible.assistant.R;
import uet.invincible.assistant.activities.RobotMainActivity;
import uet.invincible.assistant.adapters.BottonoChatAdapter;
import uet.invincible.assistant.algorithm.Calculator;
import uet.invincible.assistant.bases.BaseFragment;
import uet.invincible.assistant.configure.AppConfigure;
import uet.invincible.assistant.configure.ServiceConfigure;
import uet.invincible.assistant.customize.MyEditText;
import uet.invincible.assistant.listeners.ChattingBottonoListener;
import uet.invincible.assistant.models.MessageModel;
import uet.invincible.jsoup.Geographic;
import uet.invincible.jsoup.History;
import uet.invincible.jsoup.MultipleClauses;
import uet.invincible.jsoup.QuestionContent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint({ "NewApi", "DefaultLocale" })
public class AssistantChatbotFragment extends BaseFragment {

	private static final long serialVersionUID = 9158868130895684042L;
	private LinearLayout mLayoutSend;
	private LinearLayout mVoiceStart;
	private MyEditText mTyping;
	private ListView mListView;
	private static RobotMainActivity mRobotActivity;
	private ArrayList<MessageModel> messageModel;
	private BottonoChatAdapter adapter;

	private SQLiteDatabase database;
	private boolean databaseOpen = false;
	private boolean adapterLoaded = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_chatbot,
				container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}

	protected static final int RESULT_SPEECH = 1;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == -1 && null != data) {
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				mTyping.append(text.get(0));
			}
			break;
		}
		}
	}

	public String getFinalResult(String result) {
		Log.d("debuggggg", "de");
		int count = 0;
		int cutPoint = 0;
		
		int lastIndex = 0;
		// int count = 0;
		String findStr = ". ";
		while (lastIndex != -1) {

			lastIndex = result.indexOf(findStr, lastIndex);

			if (lastIndex != -1) {
				count++;
				Log.e("count", String.valueOf(count));
				lastIndex += 1;
				if (count == 3) {
					cutPoint = lastIndex;
				}
			}
		}
		Log.e("countSentences", String.valueOf(count) + "de");

		if (count >= 6) {
			result = result.substring(0, cutPoint);
		}

		return result;
	}

	public static String messageInput = "";

	@SuppressLint("NewApi")
	private class AsyncAIML extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mRobotActivity.showProgressDialog("AIML Waiting...");
		}

		@Override
		protected String doInBackground(String... params) {
			if (isConnected()) {
				messageInput = params[0];
				String s = "";
				for (int i = 0; i < params[0].length(); i++) {
					if (params[0].charAt(i) == ' ') {
						s += "%20";
					} else {
						s += params[0].charAt(i);
					}
				}
				JSONObject jSon;
				try {
					jSon = new JSONObject(
							GET("http://tech.fpt.com.vn/AIML/api/bots/"
									+ ServiceConfigure.bot_id
									+ "/chat?request=" + s + "&token="
									+ ServiceConfigure.token));
					String response = jSon.getString("response");

					// / weather
					if (response.contains("fcttext_metric")) {
						JSONObject weatherJSON = new JSONObject(response);
						Log.d("weatherJSON", weatherJSON.toString());

						JSONObject forecast = weatherJSON
								.getJSONObject("response")
								.getJSONObject("forecast")
								.getJSONObject("txt_forecast")
								.getJSONArray("forecastday").getJSONObject(0);
						Log.d("forecast", forecast.toString());

						String weather = forecast.getString("fcttext_metric");
						Log.d("weather", weather);

						response = weather.replace("độ", " độ")
								.replace("\\/", "/").replace(".", ",");
					}

					return response;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("NO DATA IN AIML")) {
				// if(result.equals("")) {
				Log.e("wwwwwwwwwww", result);
				String s = messageInput;
				String siteUrl = s;
				String tmp = "";

				// thời gian X xảy ra sự kiện gì
				String[] sk = { "xảy ra sự kiện gì", "có những sự kiện",
						"có các sự kiện", "có sự kiện", "diễn ra sự kiện",
						"diễn ra các sự kiện", "diễn ra những sự kiện" };
				for (int i = 0; i < sk.length; ++i) {
					if (s.contains(sk[i])) {
						String[] splits = s.split(" ");
						for (int j = 0; j < splits.length; ++j) {
							if (isNumberic(splits[j])) {
								if (!(tmp = History.getAnswerHistoryEvents(
										Integer.parseInt(splits[j]),
										mRobotActivity))
										.equals("NO DATA FROM JSOUP")) {
									s = tmp + flag;
								}
							}
						}
						Log.d("db", "getAnswerHistoryEvents");
					}
				}

				String[] dynasties = { "nhà nguyễn", "nhà tây sơn", "nhà mạc",
						"nhà hậu lê", "nhà hồ", "nhà trần", "nhà lý",
						"nhà lê sơ", "nhà tiền lê", "nhà ngô", "nhà đinh" };
				for (int i = 0; i < dynasties.length; ++i) {
					if (s.contains(dynasties[i])) {
						if (!(tmp = History.getAnswerHistoryDynasty(
								dynasties[i], mRobotActivity))
								.equals("NO DATA FROM JSOUP")) {
							s = tmp + flag;
						}
						Log.d("db", "getAnswerHistoryDynasty");
					}
				}
				String[] capitals = { "thủ đô của nước nào", "thủ đô nước nào" };
				for (String capital : capitals) {
					if (s.contains(capital)) {
						Log.d("xxxxx", s);
						if (!(tmp = Geographic.getAnswerGeographicCountry(s,
								mRobotActivity)).equals("NO DATA FROM JSOUP")) {
							s = tmp + flag;
						}
					}
				}

				Log.d("after capital", s);

				String[] leadings = { "thủ đô" };
				String[] endings = { "là thành phố nào", "thành phố nào",
						"ở đâu" };
				boolean fl = false;
				boolean fe = false;
				for (String leading : leadings) {
					if (s.contains(leading))
						fl = true;
				}
				for (String ending : endings) {
					if (s.contains(ending))
						fe = true;
				}
				if (fl && fe) {
					if (!(tmp = Geographic.getAnswerGeographicCapital(s,
							mRobotActivity)).equals("NO DATA FROM JSOUP")) {
						s = tmp + flag;
					}
				}

				String[] leadingPops = { "dân số của", "dân số nước" };
				boolean fp = false;
				for (String leading : leadingPops) {
					if (s.contains(leading))
						fp = true;
				}
				if (fp) {
					if (!(tmp = Geographic.getAnswerGeographicPopularity(s,
							mRobotActivity)).equals("NO DATA FROM JSOUP")) {
						s = tmp + flag;
					}
				}

				String[] wheres = { "nằm ở đâu" };
				boolean fw = false;
				for (String w : wheres) {
					if (s.contains(w))
						fw = true;
				}
				if (fw) {
					if (!(tmp = Geographic.getAnswerGeographicGeo(s,
							mRobotActivity)).equals("NO DATA FROM JSOUP")) {
						s = tmp + flag;
					}
				}
				String[] t = QuestionContent.removeQuestionWords(s).split(" ");
				if (t.length >= 4) {    // for better probability of correct answer 
					Log.d("xxxxxx", String.valueOf(t.length) + t);
					if (!(tmp = History.getAnswerHistoryTime(s, mRobotActivity))
							.equals("NO DATA FROM JSOUP")) {
						s = tmp + flag;
					}
				}

				Log.d("after history", s);
								 
				// 2 clauses
//				Dân số của trung quốc và ấn độ là bao nhiêu
//				Diên tích của hà nội và hồ chí minh là bao nhiêu
				
				if (s.contains("diện tích") && s.contains("và")) {		
					Log.d("2 clauses dien tich", s);
					if (!(tmp = MultipleClauses.getMultipleArea(s, mRobotActivity))
							.equals("NO DATA FROM JSOUP")) {
						s = tmp + flag;
					}					
				}
				
				if (s.contains("dân số") && s.contains("và")) {		
					Log.d("2 clauses daan soo", s);
					if (!(tmp = MultipleClauses.getMultiplePopularity(s, mRobotActivity))
							.equals("NO DATA FROM JSOUP")) {
						s = tmp + flag;
					}					
				}

				if (!s.contains(flag)) {
					// no data in UET-DB - send query to GGNow - Please dont
					// modify string s !!!
					s = QuestionContent.removeQuestionWords(s);

					// starting encode for google url
					try {
						s = URLEncoder.encode(s, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					siteUrl = "https://www.google.com.vn/search?q=" + s
							+ "&ie=UTF-8&oe=UTF-8";
				}

				(new AsyncParseURL()).execute(new String[] { siteUrl, s });

				// } else {
				// if(isConnected()) {
				// final String finalResult = getFinalResult(result);
				// insertToListview(finalResult);
				// runGestureAndSay(finalResult);
				// super.onPostExecute(finalResult);
				// } else {
				// mRobotActivity.showToast( "Disconnected to network. ");
				// }
				// }

				return;
			}
			mRobotActivity.removePreviousDialog();
			if (isConnected()) {
				final String finalResult = getFinalResult(result);
				insertToListview(finalResult);
				runGestureAndSay(finalResult);
				super.onPostExecute(finalResult);
			} else {
				mRobotActivity.showToast("Disconnected to network. ");
			}
		}
	}

	@SuppressLint("NewApi")
	private class AsyncQueryData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mRobotActivity.showProgressDialog("Query Waiting...");
		}

		@Override
		protected String doInBackground(String... params) {
			if (isConnected()) {
				String s = "";
				for (int i = 0; i < params[0].length(); i++) {
					if (params[0].charAt(i) == ' ') {
						s += "%20";
					} else {
						s += params[0].charAt(i);
					}

				}
				JSONObject jSon;
				try {
					jSon = new JSONObject(
							GET("http://54.255.200.131/QAGraph/getAnswerTotal?text="
									+ s));
					String response = jSon.getString("answer");
					String res = "";
					for (int i = 0; i < response.length(); i++) {
						if (response.charAt(i) != '['
								&& response.charAt(i) != ']')
							res += response.charAt(i);
					}
					res = getFinalResult(res);
					Log.d("debugggg", res);
					return res;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(final String result) {
			mRobotActivity.removePreviousDialog();
			if (result.equals("")) {
				new AsyncUnknownQuestion().execute(messageInput);
			} else {
				insertToListview(result);
				runGestureAndSay(result);
				super.onPostExecute(result);
			}
		}
	}

	final private String flag = "!@#";

	private boolean isNumberic(String split) {
		try {
			Integer.parseInt(split);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private class AsyncParseURL extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			mRobotActivity.showProgressDialog("Parse URL Waiting...");
		}

		@Override
		protected String doInBackground(String... strings) {
			if (strings[1].contains(flag)) {
				strings[1] = strings[1].substring(0,
						strings[1].length() - flag.length());
				return getFinalResult(strings[1]);
			} else {
				StringBuffer buffer = new StringBuffer();
				try {
					Log.d("JSwa", "Connecting to [" + strings[0] + "]");
					Document doc = Jsoup.connect(strings[0]).get();
					Log.d("JSwa", "Connected to [" + strings[0] + "]");

					String resp;
					if ((resp = doc.select(".kno-rdesc>span:first-child")
							.html()) != ""
							|| (resp = doc.select("._eF").html()) != ""
							|| (resp = doc.select(".kpd-ans").html()) != "") {
						buffer.append(resp);
					} else {
						buffer.append("NO DATA FROM JSOUP");
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}

				String bufferString = StringEscapeUtils.unescapeHtml4(buffer
						.toString());
				Log.d("db", bufferString);

				bufferString = getFinalResult(bufferString);
				return bufferString;
			}
		}

		@Override
		protected void onPostExecute(String s) {
			mRobotActivity.removePreviousDialog();
			if (s.equals("NO DATA FROM JSOUP")) {
				new AsyncQueryData().execute(messageInput);
			} else {
				insertToListview(s);
				runGestureAndSay(s);
				super.onPostExecute(s);
			}
		}
	}

	@SuppressLint("NewApi")
	private class AsyncUnknownQuestion extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mRobotActivity.showProgressDialog("Unknown Waiting...");
		}

		@Override
		protected String doInBackground(String... params) {
			if (isConnected()) {
				String s = "";
				for (int i = 0; i < params[0].length(); i++) {
					if (params[0].charAt(i) == ' ') {
						s += "%20";
					} else {
						s += params[0].charAt(i);
					}
				}
				JSONObject jSon;
				try {
					jSon = new JSONObject(
							GET("http://54.255.200.131/fti-qa/nlp/vi/answer-type?question="
									+ s));
					return jSon.getString("answer_type");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mRobotActivity.removePreviousDialog();
			String resultFinal = "Câu này khó thật, để mình cập nhật dữ liệu rồi trả lời bạn sau nhé .";
			if (isConnected()) {
				switch (result) {
				case "ORG":
					resultFinal = "Hiện tại mình chưa có thông tin về tổ chức này, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "HUM":
					resultFinal = "Hiện tại mình chưa có thông tin về người này, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "LOC":
					resultFinal = "Hiện tại mình chưa có thông tin về địa điểm này, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				// case "DESC":
				// resultFinal =
				// "Mô tả này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
				// break;
				// case "THG":
				// resultFinal =
				// "THG này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
				// break;
				// case "CONC":
				// resultFinal =
				// "CONC này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
				// break;
				// case "NUM":
				// resultFinal =
				// "Số này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
				// break;
				case "DTIME":
					resultFinal = "Hiện tại mình chưa có thông tin về mốc thời gian này, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				// case "NET":
				// resultFinal =
				// "Mạng này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
				// break;
				case "EVT":
					resultFinal = "Hiện tại mình chưa có thông tin về sự kiện này, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				default:
					break;
				}
				insertToListview(resultFinal);
				// resultFinal = resultFinal.replace("",
				// " phẩy ").replace("km²", " km vuông").replace(".", "");

				final String newResult = resultFinal;
				runGestureAndSay(newResult);
				super.onPostExecute(newResult);
			} else {
				mRobotActivity.showToast("Disconnected to network. ");
			}
		}
	}

	public boolean containCal(String s) {
		String[] stringArr = s.split(" ");
		String[] cal = { "nhân", "chia", "cộng", "trừ", "đếm", "từ", "đến",
				"v�?" };
		for (int i = 0; i < stringArr.length; i++) {
			for (int j = 0; j < cal.length; j++) {
				if (stringArr[i].equals(cal[j])) {
					return true;
				}
			}
		}
		return false;
	}

	public int containNumberChar(String s) {
		int count = 0;
		String[] stringArr = s.split(" ");
		String[] numbers = { "một", "hai", "ba", "bốn", "năm", "sáu", "báy",
				"tám", "chín", "mười" };
		for (int i = 0; i < stringArr.length; i++) {
			for (int j = 0; j < numbers.length; j++) {
				if (stringArr[i].equals(numbers[j])) {
					count++;
				}
			}
		}
		return count;
	}

	public int containtNumbers(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) <= '9' && s.charAt(i) >= '0') {
				count++;
			}
		}
		return count;
	}

	public void insertToListview(final String s) {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String label = DateUtils.formatDateTime(mContext,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_WEEKDAY
								| DateUtils.FORMAT_SHOW_YEAR);
				doInsertRecord(1, s, label);
				adapter.mMessageModels.add(new MessageModel(s, "At: " + label,
						1));
				adapter.add(new MessageModel(s, "At: " + label, 1));
				adapter.notifyDataSetChanged();
				mListView.setSelection(messageModel.size() - 1);
				mListView.requestFocus();
			}
		});
	}

	public void runGestureAndSay(final String s) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (mRobotActivity.getConnectedRobot() == null) {
					mRobotActivity.scan();
				} else {
					Log.e("Assistant Bottono", "Speak: " + s);

					String message = s;
					if (s.matches(".*[0-9][.][0-9].*")) {
						Log.e("matched", s);
						message = s.replace("km²", "ki lô mét vuông")
								.replace(".", "");
					}
					if (s.matches(".*[0-9][,][0-9].*")) {
						Log.e("matched", s);
						message = s.replace(",", " phẩy ")
								.replace("km²", "ki lô mét vuông");								
					}
					Log.e("Assistant Bottono", "Speakxxx: " + message);

					////
					Pattern p = Pattern
							.compile("[0-3]*[0-9]*[/-]*[0-1]*[0-9][/-][0-9]*[0-9]*[0-9][0-9]");
					Matcher m = p.matcher(s);

					ArrayList<String> theGroup = new ArrayList<String>();
					while (m.find()) {
						theGroup.add(m.group(0));
					}

					ArrayList<String> dateList = theGroup;

					for (int i = 0; i < dateList.size(); ++i) {
						String date = dateList.get(i);
						String[] splits = date.contains("-") ? date.split("-")
								: date.split("/");
						if (splits.length == 2) {
							date = "tháng " + splits[0] + " năm " + splits[1];
						} else if (splits.length == 3) {
							date = "ngày " + splits[0] + " tháng " + splits[1]
									+ " năm " + splits[2];
						}
						message = message.replace(theGroup.get(i), date);
						Log.d(theGroup.get(i), date);
						// 
					}

					// //

					message = message.replace("(", "").replace(")", "")
							.replace("SMAC 2014.", "Sờ mác 2014 .")
							.replace("UET", "U E T").replace("km", "ki lô mét");
					for (int i = 0; i < 9; ++i) {
						message = message.replaceAll(String.valueOf(i) + "[,]",
								String.valueOf(i) + " ,");
					}

					Log.d("hoa hong", message);
					for (int i = 0; i < 9; ++i) {
						message = message.replaceAll(String.valueOf(i) + "[.]",
								String.valueOf(i) + " .");
					}

					Log.e("Assistant Bottono", "Speak111: " + message);

					// message = getFinalResult(message);

					mRobotActivity.speak(message,
							RobotTextToSpeech.ROBOT_TTS_LANG_VI);
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (mRobotActivity.getConnectedRobot() == null) {
					mRobotActivity.scan();
				} else {
					Log.e("Assistant Bottono", "Run gestures");
					String[] gestures = new String[11];
					for (int i = 1; i <= 10; i++) {
						gestures[i] = "HandMotionBehavior" + i;
					}
					for (int i = 0; i < 5; i++) {
						int j = (int) (Math.random() * 10) + 1;
						mRobotActivity.runGesture(gestures[j]);
					}
				}
			}
		}).start();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fragment_conversation_layout_send:
			mRobotActivity.hideSoftKeyboard(getActivity());
			final String message = mTyping.getText().toString().trim();
			if (!message.equals("")) {
				String label = DateUtils.formatDateTime(mContext,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_WEEKDAY
								| DateUtils.FORMAT_SHOW_YEAR);
				doInsertRecord(0, message, label);
				adapter.mMessageModels.add(new MessageModel(message, "At: "
						+ label, 0));
				adapter.add(new MessageModel(message, "At: " + label, 0));
				adapter.notifyDataSetChanged();
				mListView.setSelection(messageModel.size() - 1);
				mListView.requestFocus();
				if ((containtNumbers(message) + containNumberChar(message)) >= 2
						&& containCal(message)) {
					String calRes = Calculator.getResult(message);
					final String s = calRes
							.contains("Error Expression1000000000") ? "Phép tính không hợp lệ"
							: "Đáp số là " + calRes;
					runGestureAndSay(s);
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							insertToListview(s);
						}
					});
				} else {
					new AsyncAIML().execute(message);
				}
			}
			mTyping.setText("");
			break;
		case R.id.fragment_conversation_voice_start:
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
			try {
				startActivityForResult(intent, RESULT_SPEECH);
				mTyping.append("");
			} catch (ActivityNotFoundException a) {
				a.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void initModels() {
		if (!databaseOpen) {
			database = mContext.openOrCreateDatabase(
					AppConfigure.DATABASE_NAME, 0x0000, null);
			databaseOpen = true;
		}
		SharedPreferences pre = mContext.getSharedPreferences("botono", 0x0000);
		String s = pre.getString("exist", "");
		if (s == "") {
			doCreateTable();
			SharedPreferences.Editor edit = pre.edit();
			edit.putString("exist", "1");
			edit.commit();
		}
		if (!adapterLoaded) {
			adapterLoaded = true;
			messageModel = loadAllMessage();
			adapter = new BottonoChatAdapter(mContext, 0, messageModel,
					new ChattingBottonoListener() {
						@Override
						public void onTrainClicked() {

						}

						@Override
						public void onItemSelected() {

						}
					});
		}
		mListView.setAdapter(adapter);
		mListView.setSelection(messageModel.size() - 1);
		mListView.requestFocus();
	}

	@Override
	protected void initViews(View view) {
		mLayoutSend = (LinearLayout) view
				.findViewById(R.id.fragment_conversation_layout_send);
		mLayoutSend.setOnClickListener(this);
		mVoiceStart = (LinearLayout) view
				.findViewById(R.id.fragment_conversation_voice_start);
		mVoiceStart.setOnClickListener(this);
		mTyping = (MyEditText) view
				.findViewById(R.id.fragment_conversation_typing);
		mListView = (ListView) view
				.findViewById(R.id.fragmet_conversation_listview);
	}

	@Override
	protected void initAnimations() {

	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		} catch (Exception e) {
			Log.d("Assistant Log", e.getLocalizedMessage());
		}
		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getActivity()
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public void doDeleteDb() {
		String msg = "";
		if (getActivity().deleteDatabase(AppConfigure.DATABASE_NAME) == true) {
			msg = "Delete successful";
		} else {
			msg = "Delete failed";
		}
		mRobotActivity.showToast(msg);
	}

	public void doCreateTable() {
		String sql = "CREATE TABLE message (";
		sql += "id INTEGER PRIMARY KEY AUTOINCREMENT,";
		sql += "tag INT,";
		sql += "content TEXT,";
		sql += "time TEXT)";
		database.execSQL(sql);
	}

	public void doDeleteTable(String tableName) {
		database.delete(tableName, null, null);
	}

	public String getIndex() {
		String s = "";
		Cursor c = database
				.query("message", null, null, null, null, null, null);
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			s += c.getString(1);
			c.moveToNext();
		}
		c.close();
		return s;
	}

	public ArrayList<MessageModel> loadAllMessage() {
		ArrayList<MessageModel> messageModel = new ArrayList<MessageModel>();
		Cursor c = database
				.query("message", null, null, null, null, null, null);
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			messageModel.add(new MessageModel(c.getString(2), c.getString(3),
					Integer.parseInt(c.getString(1))));
			c.moveToNext();
		}
		c.close();
		return messageModel;
	}

	public void doInsertRecord(int tag, String message, String time) {
		ContentValues values = new ContentValues();
		values.put("tag", tag);
		values.put("content", message);
		values.put("time", time);
		database.insert("message", null, values);
	}

	private static AssistantChatbotFragment mInstance;

	public static AssistantChatbotFragment getInstance(
			RobotMainActivity mRobotActivity2) {
		if (mInstance == null) {
			mInstance = new AssistantChatbotFragment();
		}
		mRobotActivity = mRobotActivity2;
		return mInstance;
	}
}
