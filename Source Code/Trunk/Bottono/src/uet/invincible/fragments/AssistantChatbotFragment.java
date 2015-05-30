package uet.invincible.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.adapters.BottonoChatAdapter;
import uet.invincible.algorithm.Calculator;
import uet.invincible.bases.BaseFragment;
import uet.invincible.configure.AppConfigure;
import uet.invincible.customize.MyEditText;
import uet.invincible.listeners.ChattingBottonoListener;
import uet.invincible.models.MessageModel;
import uet.invincible.utilities.Log;
import uet.invincible.utilities.SystemUtil;
import uet.invincible.webservice.ServiceConfigure;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fpt.robot.tts.RobotTextToSpeech;
@SuppressLint("NewApi")
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_chatbot, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	
	protected static final int RESULT_SPEECH = 1;
	protected static final String TAG = "Conversation";
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case RESULT_SPEECH: {
				if (resultCode == -1 && null != data) {
					ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					mTyping.append(text.get(0));
				}
				break;
			}
		}
	}
	
	public String getFinalResult(String result) {
		String newResult = "";
		int index = 0;
		int count = 0;
		while(index < result.length()) {
			if(result.charAt(index) == '.') count++;
			if(count == 2) break;
			newResult += result.charAt(index++);
		}
		if(count <= 1) return result;
		else return newResult;
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
			if(isConnected()) {
				messageInput = params[0];
				String s = "";
				for(int i=0; i<params[0].length(); i++) {
					if(params[0].charAt(i) == ' ') {
						s += "%20";
					} else {
						s += params[0].charAt(i);
					}
				}
				JSONObject jSon;
				try {
					jSon = new JSONObject(GET("http://tech.fpt.com.vn/AIML/api/bots/"+ ServiceConfigure.bot_id +"/chat?request="+ s + "&token=" + ServiceConfigure.token));
					String response = jSon.getString("response");
					return response; 
				} catch (JSONException e) {
					e.printStackTrace();
				}		
			} 
			return null;
		}
		@Override
		protected void onPostExecute(final String result) {
			if(result.equals("NO DATA IN AIML")) {
				new AsyncQueryData().execute(messageInput);
				return;
			}
			mRobotActivity.removePreviousDialog();
			if(isConnected()) {
				final String finalResult = getFinalResult(result);
				insertToListview(finalResult);
				runGestureAndSay(finalResult);
				super.onPostExecute(finalResult);
			} else {
				mRobotActivity.showToast( "Disconnected to network. ");
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
			if(isConnected()) {
				String s = "";
				for(int i=0; i<params[0].length(); i++) {
					if(params[0].charAt(i) == ' ') {
						s += "%20";
					} else {
						s += params[0].charAt(i);
					}
				}
				JSONObject jSon;
				try {
					jSon = new JSONObject(GET("http://54.255.200.131/QAGraph/getAnswerTotal?text=" + s));
					String response = jSon.getString("answer");
					String res = "";
					for(int i=0; i<response.length(); i++) {
						if(response.charAt(i) != '[' && response.charAt(i) != ']') res += response.charAt(i);
					}
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
			if(result.equals("")) {
				String s = messageInput;
	            String[] questionWords = {"là gì", "là bao nhiêu", "là mấy", "là ai", "tên là gì", "là ngày nào", "bạn có biết không", "là thành phố nào"};
	            String[] leadingQuestionWords = {"hãy cho biết", "bạn hãy cho biết", "bạn có biết về", "cho biết", "hãy cho biết ai là", "ai là", "bạn biết gì về", "bạn có biết"};
	            for (int i = 0; i < leadingQuestionWords.length; ++i) {
	                if (s.contains(leadingQuestionWords[i])) {
	                    s = s.substring(leadingQuestionWords[i].length() + 1, s.length());
	                    Log.d(leadingQuestionWords[i]);
	                }
	            }
	            Log.d(s);
	            for (int i = 0; i < questionWords.length; ++i) {
	                if (s.contains(questionWords[i])) {
	                    s = s.substring(0, s.length() - questionWords[i].length() - 1);
	                }
	            }
	            try {
	                Log.d(s);
	                s = URLEncoder.encode(s, "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	            Log.d(s);
	            String siteUrl = "https://www.google.com.vn/search?q=" + s + "&ie=UTF-8&oe=UTF-8";
	            (new AsyncParseURL()).execute(new String[]{siteUrl});
				
	            
			} else {
				if(isConnected()) {
					final String finalResult = getFinalResult(result);
					insertToListview(finalResult);
					runGestureAndSay(finalResult);
					super.onPostExecute(finalResult);
				} else {
					mRobotActivity.showToast( "Disconnected to network. ");
				}
			}
		}
	}
	private class AsyncParseURL extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(mRobotActivity);
        @Override
        protected void onPreExecute() {
            mRobotActivity.showProgressDialog("Parse URL Waiting...");
        }
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            try {
                Log.d("Connecting to [" + strings[0] + "]");
                Document doc = Jsoup.connect(strings[0]).get();
                Log.d("Connected to [" + strings[0] + "]");

                String resp;
                if ((resp = doc.select(".kno-rdesc>span:first-child").html()) != ""
                        || (resp = doc.select("._eF").html()) != ""
                        || (resp = doc.select(".kpd-ans").html()) != "") {
                    buffer.append(resp);
//                } else if ((resp = doc.select(".r:first-child>a").first().html()) != "") {
//                    resp = Jsoup.parse(resp).text();
//                    buffer.append(resp);
                } else {
                	buffer.append("NO DATA IN JSOUP");
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

            String bufferString = StringEscapeUtils.unescapeHtml4(buffer.toString());

            Log.d(bufferString);
            return bufferString;
        }
        @Override
        protected void onPostExecute(String s) {
            mRobotActivity.removePreviousDialog();
            if(s.equals("NO DATA IN JSOUP")) {
            	new AsyncUnknowQuestion().execute(messageInput);
            } else {
            	insertToListview(s);
    			runGestureAndSay(s);
                super.onPostExecute(s);
            }
        }
    }
	@SuppressLint("NewApi")
	private class AsyncUnknowQuestion extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mRobotActivity.showProgressDialog("Unknow Waiting...");
		}
		@Override
		protected String doInBackground(String... params) {
			if(isConnected()) {
				String s = "";
				for(int i=0; i<params[0].length(); i++) {
					if(params[0].charAt(i) == ' ') {
						s += "%20";
					} else {
						s += params[0].charAt(i);
					}
				}
				JSONObject jSon;
				try {
					jSon = new JSONObject(GET("http://54.255.200.131/fti-qa/nlp/vi/answer-type?question=" + s));
					return jSon.getString("answer_type");
				} catch (JSONException e) {
					e.printStackTrace();
				}		
			} 
			return null;
		}
		@Override
		protected void onPostExecute(final String result) {
			mRobotActivity.removePreviousDialog();
			String resultFinal = "Câu này khó thật, để mình cập nhật dữ liệu rồi trả lời bạn sau nhé .";
			if(isConnected()) {
				switch (result) {
				case "ORG":
					resultFinal = "Tổ chức này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "HUM":
					resultFinal = "Người này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "LOC":
					resultFinal = "Nơi này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "DESC":
					resultFinal = "Mô tả này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "THG":
					resultFinal = "THG này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "CONC":
					resultFinal = "CONC này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "NUM":
					resultFinal = "Số này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "DTIME":
					resultFinal = "Thời gian này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "NET":
					resultFinal = "Mạng này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				case "EVT":
					resultFinal = "Sự kiện này hiện tại mình chưa có thông tin, mình sẽ tìm hiểu và trả lời bạn sau. ";
					break;
				default:
					break;
				}
				insertToListview(resultFinal);
				final String newResult = resultFinal;
				new Thread(new Runnable() {
					@Override
					public void run() {
						if(mRobotActivity.getRobot() == null) {
							mRobotActivity.scan();
						} else {
							mRobotActivity.speak(newResult, RobotTextToSpeech.ROBOT_TTS_LANG_VI);
						}
					}
				}).start();
				new Thread(new Runnable() {
					@Override
					public void run() {
						if(mRobotActivity.getRobot() == null) {
							mRobotActivity.scan();
						} else {
							runRandomGesture(4);
						}
					}
				}).start();
				super.onPostExecute(newResult);
			} else {
				mRobotActivity.showToast( "Disconnected to network. ");
			}
		}
	}
	
	public boolean containCal(String s) {
		String[] stringArr = s.split(" ");
		String[] cal = {"nhân", "chia", "cộng", "trừ", "đếm", "từ", "đến", "về"};
		for(int i=0; i<stringArr.length; i++) {
			for(int j=0; j<cal.length; j++) {
				if(stringArr[i].equals(cal[j])) {
					return true;
				}
			}
		}
		return false;
	}
	public int containNumberChar(String s) {
		int count = 0;
		String[] stringArr = s.split(" ");
		String[] numbers = {"một", "hai", "ba", "bốn", "năm", "sáu", "báy", "tám", "chín"};
		for(int i=0; i<stringArr.length; i++) {
			for(int j=0; j<numbers.length; j++) {
				if(stringArr[i].equals(numbers[j])) {
					count++;
				}
			}
		}
		return count;
	}
	public int containtNumbers(String s) {
		int count = 0;
		for(int i=0; i<s.length(); i++) {
			if(s.charAt(i) <= '9' && s.charAt(i) >= '0') {
				count++;
			}
		}
		return count;
	}
	public void insertToListview(String s) {
		String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),  DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR);
		doInsertRecord(1, s, label);
		adapter.mMessageModels.add(new MessageModel(s, "At: " + label, 1));
		adapter.add(new MessageModel(s, "At: " + label, 1));
		adapter.notifyDataSetChanged();
		mListView.setSelection(messageModel.size()-1);
		mListView.requestFocus();
	}
	public void runGestureAndSay(final String s) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(mRobotActivity.getRobot() == null) {
					mRobotActivity.scan();
				} else {
					mRobotActivity.speak(s, RobotTextToSpeech.ROBOT_TTS_LANG_VI);
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(mRobotActivity.getRobot() == null) {
					mRobotActivity.scan();
				} else {
					runRandomGesture(4);
				}
			}
		}).start();
	}
	public void runRandomGesture(int num) {
		AppConfigure.SHOW_TOAST = false;
		String[] gestures = new String[11];
		for(int i=1; i<=10; i++) {
			gestures[i] = "HandMotionBehavior" + i;
		}
		for(int i=0; i<num; i++) {
			int j = (int)(Math.random()*10)+1;
			mRobotActivity.runGesture(gestures[j]);
		}
		AppConfigure.SHOW_TOAST = true;
	}
	@Override public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fragment_conversation_layout_send:
			SystemUtil.hideSoftKeyboard(getActivity());
			final String message = mTyping.getText().toString();
			if(!message.equals("")) {
				String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),  DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR);
				doInsertRecord(0, message, label);
				adapter.mMessageModels.add(new MessageModel(message, "At: " + label, 0));
				adapter.add(new MessageModel(message, "At: " + label, 0));
				adapter.notifyDataSetChanged();
				mListView.setSelection(messageModel.size()-1);
				mListView.requestFocus();
				if((containtNumbers(message) + containNumberChar(message)) >= 2 && containCal(message)) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							if(mRobotActivity.getRobot() == null) {
								mRobotActivity.scan();
							} else {
								runRandomGesture(4);
							}
						}
					}).start();
					new Thread(new Runnable() {
						@Override
						public void run() {
							insertToListview(Calculator.getResult(message));
							mRobotActivity.speak(Calculator.getResult(message), RobotTextToSpeech.ROBOT_TTS_LANG_VI);
						}
					}).start();
				} else {
					new AsyncAIML().execute(message);
				}
			}
			mTyping.setText("");
			break;
		case R.id.fragment_conversation_voice_start:
//			new Thread(new Runnable() {
//			@Override
//			public void run() {
//				new SpeakToText(Languages.VIETNAMESE, new SpeakToTextListener() {
//					@Override
//					public void onWaiting() {
//						mRobotActivity.showProgressDialog( "Waiting...");
//					}
//					@Override
//					public void onTimeout() {
//						mRobotActivity.removePreviousDialog();
//					}
//					@Override
//					public void onStopped() {
//						mRobotActivity.removePreviousDialog();
//					}
//					@Override
//					public void onResult(final Result result) {
//						mRobotActivity.removePreviousDialog();
//						mRobotActivity.runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								mTyping.setText(result.result[0].alternative[0].transcript.toString());
//							}
//						});
//					}
//					@Override
//					public void onRecording() {
//						mRobotActivity.showProgressDialog( "Recording...");
//					}
//					@Override
//					public void onProcessing() {
//						mRobotActivity.showProgressDialog( "Processing...");
//					}
//					@Override
//					public void onError(Exception ex) {
//						mRobotActivity.removePreviousDialog();
//						ex.printStackTrace();
//					}
//				}).recognize(1000, 5000);
//			}
//		}).start();
		
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
		if(!databaseOpen) {
			database = mContext.openOrCreateDatabase(AppConfigure.DATABASE_NAME, 0x0000, null);
			databaseOpen = true;
		}
		SharedPreferences pre = mContext.getSharedPreferences("botono", 0x0000);
		String s = pre.getString("exist", "");
		if(s == "") {
			doCreateTable();
			SharedPreferences.Editor edit=pre.edit();
			edit.putString("exist", "1");
			edit.commit();
		}
		if(!adapterLoaded) {
			adapterLoaded = true;
			messageModel = loadAllMessage();
			adapter = new BottonoChatAdapter(mContext, 0, messageModel, new ChattingBottonoListener() {
				@Override
				public void onTrainClicked() {
					
				}
				@Override
				public void onItemSelected() {
					
				}
			});
		}
		mListView.setAdapter(adapter);
		mListView.setSelection(messageModel.size()-1);
		mListView.requestFocus();
	}

	@Override
	protected void initViews(View view) {
		mLayoutSend = (LinearLayout) view.findViewById(R.id.fragment_conversation_layout_send);
		mLayoutSend.setOnClickListener(this);
		mVoiceStart = (LinearLayout) view.findViewById(R.id.fragment_conversation_voice_start);
		mVoiceStart.setOnClickListener(this);
		mTyping = (MyEditText) view.findViewById(R.id.fragment_conversation_typing);
		mListView = (ListView) view.findViewById(R.id.fragmet_conversation_listview);
	}

	@Override
	protected void initAnimations() {
		
	}

	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
			if(inputStream != null) result = convertInputStreamToString(inputStream);
			else result = "Did not work!";
		} catch (Exception e) {
			Log.d(e.getLocalizedMessage());
		}
		return result;
	}
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        return result;
    }
	public boolean isConnected(){
    	ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) {
    	    	return true;
    	    } else {
    	    	return false;	
    	    }
    }
	
	
	public void doDeleteDb(){
		String msg="";
		if(getActivity().deleteDatabase(AppConfigure.DATABASE_NAME)==true){
			msg="Delete successful";
		} else{
			msg="Delete failed";
		}
		mRobotActivity.showToast(msg);
	}
	
	public void doCreateTable(){
		String sql="CREATE TABLE message (";
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
		Cursor c = database.query("message",null, null, null, null, null, null);
		c.moveToFirst();
		while(c.isAfterLast()==false){
			s += c.getString(1);
			c.moveToNext();
		}
		c.close();
		return s;
	}
	public ArrayList<MessageModel> loadAllMessage(){
		ArrayList<MessageModel> messageModel = new ArrayList<MessageModel>();
		Cursor c = database.query("message",null, null, null, null, null, null);
		c.moveToFirst();
		while(c.isAfterLast()==false){
			messageModel.add(new MessageModel(c.getString(2), c.getString(3), Integer.parseInt(c.getString(1))));
			c.moveToNext();
		}
		c.close();
		return messageModel;
	}
	public void doInsertRecord(int tag, String message, String time){
		ContentValues values=new ContentValues();
		values.put("tag", tag);
		values.put("content", message);
		values.put("time", time);
		database.insert("message", null, values);
	}
	
	
	private static AssistantChatbotFragment mInstance;
	public static AssistantChatbotFragment getInstance(RobotMainActivity mRobotActivity2) {
		if (mInstance == null) {
			mInstance = new AssistantChatbotFragment();
		}
		mRobotActivity = mRobotActivity2;
		return mInstance;
	}
}
