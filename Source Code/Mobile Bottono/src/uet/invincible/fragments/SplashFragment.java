package uet.invincible.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.bases.BaseFragment;
import uet.invincible.customize.MyAnimations;
import uet.invincible.customize.MyGifView;
import uet.invincible.mobile.R;
import uet.invincible.models.CheckinModel;
import uet.invincible.models.QuestionModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import uet.invincible.utilities.SystemUtil;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;

public class SplashFragment extends BaseFragment {

	public boolean isConnected(){
    	ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) {
    	    	return true;
    	    } else {
    	    	return false;	
    	    }
    }
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_splash, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	
	public void onGifClick(View v) {
		MyGifView gif = (MyGifView) v;
		gif.setPaused(!gif.isPaused());
	}
	
	public boolean isStart = false;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.splash_start_button:
			if(!isStart) {
				isStart = true;
				new Thread(new Runnable() {
					@Override
					public void run() {
						if(!isConnected()) {
							try {
								mRobotActivity.showProgressDialog("No network. exiting...");
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.exit(0);
						}
						mRobotActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mBtStart.setBackgroundResource(R.drawable.floating_button_white_exit);
								mLoadingText.setVisibility(View.VISIBLE);
								gif.setVisibility(View.VISIBLE);
								loadExamData();
							}
						});
					}
				}).start();
			} else {
				System.exit(0);
			}
			
			break;
		case R.id.fragment_splash_avatar_demo:
			switch (mAvatarStatus) {
			case 0:
				mAvatarStatus = 1;
				mAvatarDemo.setBackgroundResource(R.drawable.splash_avatar_demo_bound_giap);
				break;
			case 1:
				mAvatarStatus = 2;
				mAvatarDemo.setBackgroundResource(R.drawable.splash_avatar_demo_bound_ha);
				break;
			case 2:
				mAvatarStatus = 3;
				mAvatarDemo.setBackgroundResource(R.drawable.splash_avatar_demo_bound_tuan);
				break;
			case 3:
				mAvatarStatus = 0;
				mAvatarDemo.setBackgroundResource(R.drawable.splash_avatar_demo_bound_va);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	protected void initModels() {
		gif.setMovieResource(R.drawable.loadding_metro_white);
	}
	@Override
	protected void initViews(View view) {
		gif = (MyGifView) view.findViewById(R.id.splash_logo);
		mBtStart = (TextView) view.findViewById(R.id.splash_start_button);
		mBtStart.setOnClickListener(this);
		mAvatarDemo = (TextView) view.findViewById(R.id.fragment_splash_avatar_demo);
		mAvatarDemo.setOnClickListener(this);
		mLoadingText = (TextView) view.findViewById(R.id.splash_text);
	}

	@Override
	protected void initAnimations() {
		mBtStart.setAnimation(MyAnimations.fromUp(100, 1300));
	}
	
	public TextView mLoadingText;
	private TextView mBtStart;
	private TextView mAvatarDemo;
	private int mAvatarStatus = 0;
	private MyGifView gif;
	
	private static final long serialVersionUID = -3335182509181200753L;
	private static SplashFragment mInstance;
	private static RobotMainActivity mRobotActivity;
	public static SplashFragment getInstance(RobotMainActivity robotActivity) {
		if (mInstance == null) {
			mInstance = new SplashFragment();
			mRobotActivity = robotActivity;
		}
		return mInstance;
	}
	void loadText(final String s) {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mLoadingText.setText(s);
			}
		});
	}
	
	public void initStudentList() {
		final ArrayList<StudentModel> studentList = new ArrayList<StudentModel>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadText("Loading students data...");
				CypherRequest.setURL(mRobotActivity.queryURL);
				final CypherResult result = CypherRequest.makeRequest(mRobotActivity.accessToken, "match n where n.label = \"student\" return n");
				mRobotActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (result != null) {
							if(!result.status.equals("success")) {
								mRobotActivity.showToastFromUIThread("Request failed.");
							} else {
								try {
									String x = "";
									for(int i=5; i<result.result.length()-1; i++) {
										x += result.result.charAt(i);
									}
									ArrayList<String> JSONArrayString = new ArrayList<String>();
									int index = 0;
									String s = "";
									while(index < x.length()) {
										if(mRobotActivity.isBeforeJSONObject(index, x)) {
											JSONArrayString.add(s);
											s = "";
											index += 5;
										} else {
											s += x.charAt(index++);
										}
									}
									JSONArrayString.add(s);
									for(int i=0; i<JSONArrayString.size(); i++) {
										JSONObject jsonObject = new JSONObject(JSONArrayString.get(i));
										JSONObject jsonData = jsonObject.getJSONObject("data");
										String name = jsonData.getString("name");
										String gender = jsonData.getString("gender");
										String uId = jsonData.getString("uId");
										String studentCode = jsonData.getString("studentCode");
										String email = jsonData.getString("email");
										String mobilePhone = jsonData.getString("mobilePhone");
										String cuoiKi = jsonData.getString("cuoiKi");
										String giuaKi = jsonData.getString("giuaKi");
										String chuyenCan = jsonData.getString("cuoiKi");
										String tongKet = jsonData.getString("tongKet");
										String age = jsonData.getString("age");
										String classs = jsonData.getString("classs");
										String address = jsonData.getString("address");
										ArrayList<CheckinModel> checkinList = new ArrayList<CheckinModel>();
										StudentModel student = new StudentModel(name, gender, uId, studentCode, email, mobilePhone, cuoiKi, giuaKi, chuyenCan, tongKet, age, classs, address, checkinList);
										studentList.add(student);
									}
									Saved.studentList = studentList;
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						} else {
							mRobotActivity.showToastFromUIThread("Result null");
						}
						
						loadRollUpDailyData();
						
					}
				});
			}
		}).start();
	}
	public void loadRollUpDailyData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadText("Loading roll up data...");
				CypherRequest.setURL(mRobotActivity.queryURL);
				final CypherResult result0 = CypherRequest.makeRequest(mRobotActivity.accessToken, "match n where n.label='rollup' and n.date=\'" + SystemUtil.getDate() + "\' return n");
				if(result0.result == "") {
					final CypherResult result = CypherRequest.makeRequest(mRobotActivity.accessToken, "CREATE (n { label:\"rollup\", date:\"" + SystemUtil.getDate() + "\" }) RETURN n");
					final CypherResult result2 = CypherRequest.makeRequest(mRobotActivity.accessToken, "match m, n where m.label = \"student\" and n.label = \"rollup\" and n.date = \"" + SystemUtil.getDate() + "\" create m-[:status{status:\"false\"}]->n");
					if(result == null || result2 == null) mRobotActivity.showToastFromUIThread("Load rollup daily data: Result null");
					else if(result.status.equals("success") && result2.status.equals("success"))mRobotActivity.showToastFromUIThread("Load rollup daily data successful");
					else mRobotActivity.showToastFromUIThread("Load rollup daily data: Request failed.");
				} 
				final CypherResult result3 = CypherRequest.makeRequest(mRobotActivity.accessToken, "match m, n, m-[r]->n where m.label = \"student\" and n.label = \"rollup\" return m.uId, n.date, r.status");
				if(result3 == null) mRobotActivity.showToastFromUIThread("Load all rollup data : Result null");
				else if(result3.status.equals("success")) {
					String x = "";
					for(int i=0; i<result3.result.length(); i++){
						if(result3.result.charAt(i) != '}' && result3.result.charAt(i) != '{' && result3.result.charAt(i) != ',' && result3.result.charAt(i) != ':' ) {
							x += result3.result.charAt(i);
						}
					}
					String ss = "";
					for(int i=1; i<x.length(); i++) {
						if(x.charAt(i) != '\"' || (x.charAt(i) != x.charAt(i-1))) {
							ss += x.charAt(i);
						}
					}
					String[] finalResult = ss.split("\"");
					for(int i=0; i<finalResult.length-5; i+=6) {
						boolean status = false;
						if(finalResult[i+1].equals("true")) {
							status = true;
						}
						String uId = finalResult[i+3];
						String date = finalResult[i+5];
						for(int j=0; j<Saved.studentList.size(); j++) {
							if(Saved.studentList.get(j).uId.equals(uId)) {
								Saved.studentList.get(j).checkinList.add(new CheckinModel(date, status));
								break;
							}
						}
					}
					String yy = "";
					for(int i=0; i<Saved.studentList.size(); i++){
						yy = yy + Saved.studentList.get(i).name + ": " + Saved.studentList.get(i).checkinList.toString();
					}
					
					mFragmentManager.beginTransaction().replace(R.id.robot_canvas, CanvasFragment.getInstance(mRobotActivity)).commit();
					
				}
				else mRobotActivity.showToastFromUIThread("Load all rollup data: Request failed.");
			}
		}).start();
	}
	public void loadExamData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadText("Loading exam data...");
				final ArrayList<QuestionModel> questionList = new ArrayList<QuestionModel>();
				CypherRequest.setURL(mRobotActivity.queryURL);
				final CypherResult result = CypherRequest.makeRequest(mRobotActivity.accessToken, "match n where n.label = \"QA\" return n");
				mRobotActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (result != null) {
							if(!result.status.equals("success")) {
								mRobotActivity.showToastFromUIThread("Request failed.");
							} else {
								try {
									String x = "";
									for(int i=5; i<result.result.length()-1; i++) {
										x += result.result.charAt(i);
									}
									ArrayList<String> JSONArrayString = new ArrayList<String>();
									int index = 0;
									String s = "";
									while(index < x.length()) {
										if(mRobotActivity.isBeforeJSONObject(index, x)) {
											JSONArrayString.add(s);
											s = "";
											index += 5;
										} else {
											s += x.charAt(index++);
										}
									}
									JSONArrayString.add(s);
									for(int i=0; i<JSONArrayString.size(); i++) {
										JSONObject jsonObject = new JSONObject(JSONArrayString.get(i));
										JSONObject jsonData = jsonObject.getJSONObject("data");
										String quest = jsonData.getString("question");
										String answer = jsonData.getString("answer");
										String difficulty = jsonData.getString("difficulty");
										QuestionModel question = new QuestionModel(quest, answer, difficulty);
										questionList.add(question);
									}
									Saved.questionList = questionList;
									
									initStudentList();
									
									mRobotActivity.removePreviousDialog();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						} else {
							mRobotActivity.showToastFromUIThread("Result null");
						}
					}
				});
			}
		}).start();
	}
}
