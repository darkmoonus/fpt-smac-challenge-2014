package uet.invincible.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.jcraft.jsch.jce.Random;
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
	
	@SuppressLint("NewApi")
	private class MyAsync extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			if(isConnected()) {
				String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),  DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR);
				doInsertRecord(1, result, label);
				adapter.mMessageModels.add(new MessageModel(result, "At: " + label, 1));
				adapter.add(new MessageModel(result, "At: " + label, 1));
				adapter.notifyDataSetChanged();
				mListView.setSelection(messageModel.size()-1);
				mListView.requestFocus();
				new Thread(new Runnable() {
					@Override
					public void run() {
						if(mRobotActivity.getRobot() == null) {
							mRobotActivity.scan();
						} else {
						mRobotActivity.speak(result, RobotTextToSpeech.ROBOT_TTS_LANG_VI);
						}
					}
				}).start();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if(mRobotActivity.getRobot() == null) {
								mRobotActivity.scan();
							} else {
								String[] s = RobotGesture.getGestureList(mRobotActivity.getRobot());
								int indexx = (int)(Math.random() * s.length);
								mRobotActivity.showProgressDialog( "Gesture: " + s[indexx]);
								RobotGesture.runGesture(mRobotActivity.getRobot(), s[indexx]);
								mRobotActivity.removePreviousDialog();
							}
						} catch (RobotException e1) {
							e1.printStackTrace();
						}
					}
				}).start();
				super.onPostExecute(result);
			} else {
				mRobotActivity.showToast( "Disconnected to network. ");
			}
		}
	}
	public void runRandomGesture(int num) {
		String[] gestures = new String[11];
		for(int i=1; i<=10; i++) {
			gestures[i] = "HandMotionBehavior" + i;
		}
		for(int i=0; i<num; i++) {
			int j = (int)(Math.random()*10)+1;
			mRobotActivity.runGesture(gestures[j]);
		}
	}
	@SuppressLint("NewApi")
	private class MyAsync2 extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mRobotActivity.showProgressDialog("Waiting...");
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
					if(res.equals("")) res = "Xin lỗi mình không biết, bạn có thể hỏi câu khác được không.";
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
			if(isConnected()) {
				insertToListview(result);
				new Thread(new Runnable() {
					@Override
					public void run() {
						if(mRobotActivity.getRobot() == null) {
							mRobotActivity.scan();
						} else {
							mRobotActivity.speak(result, RobotTextToSpeech.ROBOT_TTS_LANG_VI);
						}
					}
				}).start();
				new Thread(new Runnable() {
					@Override
					public void run() {
						if(mRobotActivity.getRobot() == null) {
							mRobotActivity.scan();
						} else {
							runRandomGesture(5);
						}
					}
				}).start();
				super.onPostExecute(result);
			} else {
				mRobotActivity.showToast( "Disconnected to network. ");
			}
		}
	}
	
	public boolean contain(String s, String x) {
		for(int i=0; i<s.length()-x.length()+1; i++) {
			char c1 = s.charAt(i);
			char c2 = x.charAt(0);
			if(c1 == c2) {
				System.out.println(x.charAt(0));
				int j = i+1;
				int jj = 1;
				while(j < s.length() && jj < x.length() && s.charAt(j) == x.charAt(jj)) {
					System.out.println(x.charAt(jj));
					j++;
					jj++;
				}
				if(jj == x.length()) return true;
			}
		}
		return false;
	}
	public boolean containNumberChar(String s) {
		int sum = 0;
		if(contain(s, "một")) sum++;
		if(contain(s, "hai")) sum++;
		if(contain(s, "ba")) sum++;
		if(contain(s, "bốn")) sum++;
		if(contain(s, "năm")) sum++;
		if(contain(s, "sáu")) sum++;
		if(contain(s, "bảy")) sum++;
		if(contain(s, "tám")) sum++;
		if(contain(s, "chín")) sum++;
		if(sum != 0) return true;
		return false;
	}
	public boolean containtNumbers(String s) {
		for(int i=0; i<s.length(); i++) {
			if(s.charAt(i) <= '9' && s.charAt(i) >= '0') return true;
		}
		return false;
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
	@Override
	public void onClick(View view) {
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
				if(containtNumbers(message) || containNumberChar(message)) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							if(mRobotActivity.getRobot() == null) {
								mRobotActivity.scan();
							} else {
								runRandomGesture(5);
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
					new MyAsync2().execute(message);
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
    	    if (networkInfo != null && networkInfo.isConnected()) return true;
    	    else return false;	
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
