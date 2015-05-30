package uet.invincible.activities;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import uet.invincible.R;
import uet.invincible.adapters.ControlerBehaviorListviewAdapter;
import uet.invincible.adapters.ControlerGestureListviewAdapter;
import uet.invincible.bases.BaseActivity;
import uet.invincible.fragments.AssistantExamFragment;
import uet.invincible.fragments.AssistantMainFragment;
import uet.invincible.fragments.AssistantRollupFragment;
import uet.invincible.fragments.CanvasFragment;
import uet.invincible.fragments.SplashFragment;
import uet.invincible.libs.pulltorefresh.PullToRefreshListView;
import uet.invincible.models.AccountModel;
import uet.invincible.models.CheckinModel;
import uet.invincible.models.QuestionModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import uet.invincible.utilities.SystemUtil;
import vn.adflex.ads.AdFlex;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;
import com.fpt.api.skybiometry.FaceApiClient;
import com.fpt.lib.asr.Languages;
import com.fpt.lib.asr.Result;
import com.fpt.lib.asr.SpeakToText;
import com.fpt.lib.asr.SpeakToTextListener;
import com.fpt.robot.RobotException;
import com.fpt.robot.RobotHardware;
import com.fpt.robot.behavior.RobotBehavior;
import com.fpt.robot.binder.RobotValue;
import com.fpt.robot.event.RobotEvent;
import com.fpt.robot.event.RobotEventSubscriber;
import com.fpt.robot.event.RobotEventSubscriber.OnRobotEventListener;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.motion.RobotPosture;
import com.fpt.robot.sensors.RobotSonar;
import com.fpt.robot.stt.RobotSpeechRecognition;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.robot.vision.RobotCamera;
import com.fpt.robot.vision.RobotCameraPreview;
import com.fpt.robot.vision.RobotCameraPreviewView;
import com.fpt.robot.vision.RobotFaceDetection;

@SuppressLint("NewApi")
public class RobotMainActivity extends BaseActivity implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_robot);
		FragmentManager mFragmentManager = getSupportFragmentManager();
		mFragmentManager.beginTransaction().add(R.id.robot_canvas, SplashFragment.getInstance(RobotMainActivity.this)).commit();
		new Saved();
		faceApi = new FaceApiClient(apiKey, apiSecret);
		
//		AdFlex.showGift(RobotMainActivity.this, AdFlex.GIFT_CENTER_LEFT);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.robot, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_scan_robot:
			scan();
			break;
		case R.id.menu_rollup:
			startRollUp();
			break;
		case R.id.menu_exam:
			startExam();
			break;
		case R.id.menu_stand_up:
			standUp();
			break;
		case R.id.menu_crouch_robot:
			crouch();
			break;
		case R.id.menu_sitdown_robot:
			sitDown();
			break;
		case R.id.menu_sensor_head_middle:
			startHeadMiddleSensor();
			break;
		default:
			
			break;
		}
		return true;
	}
	/*--------------------------- TEXT TO SPEECH --------------------------------*/
	public void speak(String message, String language) {
		try {
			RobotTextToSpeech.say(getRobot(), message, language);
		} catch (RobotException e) {
			showToastFromUIThread("Speak failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	public static ArrayList<Thread> controlMainThreads = new ArrayList<Thread>();
	public void startBottono() {
		Thread controlBotono = new Thread(new Runnable() {
			@Override
			public void run() {
				new SpeakToText(Languages.VIETNAMESE, new SpeakToTextListener() {
					@Override
					public void onWaiting() {
						showToastFromUIThread( "Speak now ...");
					}
					@Override
					public void onTimeout() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								new Thread(new Runnable() {
									@Override
									public void run() {
//										startBottono();
									}
								}).start();
							}
						});
					}
					@Override
					public void onStopped() {
//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								new Thread(new Runnable() {
//									@Override
//									public void run() {
//										startBottono();
//									}
//								}).start();
//							}
//						});
					}
					
					@Override
					public void onResult(final Result result) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String s = result.result[0].alternative[0].transcript.toString();
								showToastFromUIThread(s);
								doSomething(s);
								new Thread(new Runnable() {
									@Override
									public void run() {
//										startBottono();
									}
								}).start();
							}
						});
					}
					
					@Override
					public void onRecording() {
						showToastFromUIThread( "Recording...");
					}
					
					@Override
					public void onProcessing() {
						showToastFromUIThread( "Processing...");
					}
					
					@Override
					public void onError(Exception ex) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								new Thread(new Runnable() {
									@Override
									public void run() {
//										startBottono();
									}
								}).start();
							}
						});
						ex.printStackTrace();
					}
				}).recognize(4000, 500000);
			}
		});
		controlMainThreads.add(controlBotono);
		controlBotono.start();
	}
	/*------------------------------STUDENTS MANAGER--------------------------------*/
	public void speakRandomBeforeLoadStudentData(final String name, final String level) {
		final int ran = (int) (Math.random() * 10);
		new Thread(new Runnable() {
			@Override
			public void run() {
				switch (ran) {
				case 0: speak("Chào " + name + " . Bạn được truy cập quyền " + level + " nhé . Hãy đợi 1 chút để mình cập nhật dữ liệu .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 1: speak(name + " đó hả . Chào mừng bạn đã trở lại . Đợi mình 1 chút để tải dữ liệu .  Bạn được truy cập quyền " + level + " nhé .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 2: speak("Ô kê " + name + " . Đã lâu rồi không thấy bạn đó . Bạn được truy cập quyền " + level + " nhé . Chờ mình chút mình cập nhật hệ thống .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 3: speak("Chào " + name + " . Hôm nay trông bạn khác quá . Phải mất 1 lúc mình mới nhận ra được đó . Đợi 1 chút để mình ắp đết dữ liệu nhé . Bạn được truy cập quyền " + level + " . ", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 4: speak(name + " phải không . Nay mạng chậm quá nên mình nhận diện hơi lâu . Xin lỗi bạn nhé . Giờ mình sẽ cập nhật dữ liệu mới nhất cho bạn . Bạn có quyền sử dụng các tính năng cho " + level + " nhé .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 5: speak("Đúng là bạn rồi ," + name + " . Lâu lắm không gặp . Bạn được truy cập quyền " + level + " . Chờ mình chút nhé . Mình đang cập nhật dữ liệu .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 6: speak("Chào " + name + " . Ánh sáng kém quá mình nhận diện hơi lâu , xin lỗi bạn nhé . Đợi chút để mình loát dữ liệu đã . Bạn được đăng nhập với quyền " + level, RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 7: speak("Chúc mừng " + name + " . Bạn đã đăng nhập thành công với quyền " + level + " . Chờ chút mình cập nhật dữ liệu .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 8: speak(name + " , bạn đã đăng nhập thành công với quyền " + level + " . Đợi mình cập nhật dữ liệu 1 chút nhé .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 9: speak("Hì , mình nhận ra bạn rồi " + name + " . Bạn được truy cập quyền " + level + " nhé . Hãy chờ chút để mình ắp đết dữ liệu. " , RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				default: break;
				}
			}
		}).start();
	}
	public void speakRandomAfterLoadStudentData() {
		final int ran = (int) (Math.random() * 10);
		new Thread(new Runnable() {
			@Override
			public void run() {
				switch (ran) {
				case 0: speak("Cập nhật dữ liệu hoàn tất . Mình đã sẵn sàng .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 1: speak("Tất cả đã sẵn sàng . Chúng mình bắt đầu làm việc thôi .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 2: speak("Đã xong . Bạn muốn mình giúp gì nào .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 3: speak("Xong rồi . Chúng mình bắt đầu làm việc luôn nhé .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 4: speak("Cập nhật thành công . Hình như mạng chậm nên loát dữ liệu hơi lâu thì phải . Không sao . Giờ bạn muốn mình giúp gì nào .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 5: speak("Ô kê mình xong rồi đây . Bắt đầu làm việc nhé .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 6: speak("Tuyệt vời . Đã tải xong dữ liệu . Chúng mình làm gì tiếp đây .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 7: speak("Mình tải xong rồi đó . Chúng ta nên làm gì tiếp bây giờ .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 8: speak("Mạng quá tuyệt vời , dữ liệu loát về nhanh thật . Bây giờ chúng mình làm gì nhỉ . ", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 9: speak("Hì . Xong rồi đó . Mình có thể giúp gì cho bạn nào .", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				default: break;
				}
			}
		}).start();
	}
	public void speakRandomStartRollup() {
		final int ran = (int) (Math.random() * 10);
		new Thread(new Runnable() {
			@Override
			public void run() {
				switch (ran) {
				case 0: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 1: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 2: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 3: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 4: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 5: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 6: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 7: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 8: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 9: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				default: break;
				}
			}
		}).start();
	}
	public void loadRollUpDailyData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				CypherRequest.setURL(queryURL);
				final CypherResult result0 = CypherRequest.makeRequest(accessToken, "match n where n.label='rollup' and n.date=\'" + SystemUtil.getDate() + "\' return n");
				if(result0.result == "") {
					final CypherResult result = CypherRequest.makeRequest(accessToken, "CREATE (n { label:\"rollup\", date:\"" + SystemUtil.getDate() + "\" }) RETURN n");
					final CypherResult result2 = CypherRequest.makeRequest(accessToken, "match m, n where m.label = \"student\" and n.label = \"rollup\" and n.date = \"" + SystemUtil.getDate() + "\" create m-[:status{status:\"false\"}]->n");
					if(result == null || result2 == null) showToastFromUIThread("Load rollup daily data: Result null");
					else if(result.status.equals("success") && result2.status.equals("success"))showToastFromUIThread("Load rollup daily data successful");
					else showToastFromUIThread("Load rollup daily data: Request failed.");
				} 
				final CypherResult result3 = CypherRequest.makeRequest(accessToken, "match m, n, m-[r]->n where m.label = \"student\" and n.label = \"rollup\" return m.uId, n.date, r.status");
				if(result3 == null) showToastFromUIThread("Load all rollup data : Result null");
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
				}
				else showToastFromUIThread("Load all rollup data: Request failed.");
			}
		}).start();
	}
	public void startRollUp() {
		AssistantMainFragment.mFragmentManager.beginTransaction().replace(R.id.fragment_assistant_frame, AssistantRollupFragment.getInstance(RobotMainActivity.this)).commit();
		speakRandomStartRollup();
		AssistantRollupFragment.startRollup();
		for(int i=0; i<controlMainThreads.size(); i++) {
			controlMainThreads.get(i).interrupt();
		}
	}
	public void speakRandomStartExam() {
		final int ran = (int) (Math.random() * 10);
		new Thread(new Runnable() {
			@Override
			public void run() {
				switch (ran) {
				case 0: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 1: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 2: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 3: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 4: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 5: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 6: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 7: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 8: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				case 9: speak("", RobotTextToSpeech.ROBOT_TTS_LANG_VI); break;
				default: break;
				}
			}
		}).start();
	}
	public void loadExamData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final ArrayList<QuestionModel> questionList = new ArrayList<QuestionModel>();
				CypherRequest.setURL(queryURL);
				final CypherResult result = CypherRequest.makeRequest(accessToken, "match n where n.label = \"QA\" return n");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (result != null) {
							if(!result.status.equals("success")) {
								showToastFromUIThread("Request failed.");
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
										if(isBeforeJSONObject(index, x)) {
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
									showToast("Update question list successfull");
									removePreviousDialog();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						} else {
							showToastFromUIThread("Result null");
						}
					}
				});
			}
		}).start();
	}
	public void loadAccountData() {
		final ArrayList<AccountModel> accountList = new ArrayList<AccountModel>();
		CypherRequest.setURL(queryURL);
		final CypherResult result = CypherRequest.makeRequest(accessToken, "match n where n.label = \"account\" return n");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (result != null) {
					if(!result.status.equals("success")) {
						showToastFromUIThread("Request failed.");
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
								if(isBeforeJSONObject(index, x)) {
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
								String level = jsonData.getString("level");
								String email = jsonData.getString("email");
								String mobilePhone = jsonData.getString("mobilePhone");
								String age = jsonData.getString("age");
								String address = jsonData.getString("address");
								AccountModel account = new AccountModel(name, level, uId, gender, email, mobilePhone, age, address);
								accountList.add(account);
							}
							Saved.accountList = accountList;
							showToast("Finish update account list");
							removePreviousDialog();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {
					showToastFromUIThread("Result null");
				}
			}
		});
	}
	public void startExam() {
		AssistantMainFragment.mFragmentManager.beginTransaction().replace(R.id.fragment_assistant_frame, AssistantExamFragment.getInstance(RobotMainActivity.this)).commit();
		speakRandomStartExam();
		loadExamData();
		AssistantExamFragment.startExamination();
		for(int i=0; i<controlMainThreads.size(); i++) {
			controlMainThreads.get(i).interrupt();
		}
	}
	public void startProjector() {
		
	}
	public void stopProjector() {
		
	}
	public void startGreeting() {
		
	}
	public void startByeBye() {
		
	}
	public void startUnknow() {
		speak("Xin lỗi, mình không hiểu ý bạn . Bạn nói lại được không .", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
	}
	public void doSomething(String s) {
		if(contain(s, "điểm danh")) {
			showToastFromUIThread("OK! bắt đầu điểm danh");
			startRollUp();
		} else 
		if(contain(s, "xin chào")) {
			showToastFromUIThread("OK! Xin chào.");  
			startGreeting();
		} else
		if(contain(s, "bật máy chiếu")) {
			showToastFromUIThread("OK! bật máy chiếu");
			startProjector();
		} else
		if(contain(s, "tắt máy chiếu")) {
			showToastFromUIThread("OK! tắt máy chiếu");
			stopProjector();
		} else 
		if(contain(s, "bắt đầu") && contain(s, "kiểm tra")) {
			showToastFromUIThread("OK! bắt đầu kiểm tra");
			startExam();
		} else 
		if(contain(s, "tạm biệt")) {
			showToastFromUIThread("OK! tạm biệt");
			startByeBye();
		} else {
			showToastFromUIThread("Mình không hiểu");
			startUnknow();
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
	public boolean isBeforeJSONObject(int i, String x) {
		if(i > x.length()-5) return false;
		if(x.charAt(i) == ',' && x.charAt(i+1) == '\"' && x.charAt(i+2) == 'n' && x.charAt(i+3) == '\"' && x.charAt(i+4) == ':') {
			return true;
		}
		return false;
	}
	public void initStudentList() {
		final ArrayList<StudentModel> studentList = new ArrayList<StudentModel>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				CypherRequest.setURL(queryURL);
				final CypherResult result = CypherRequest.makeRequest(accessToken, "match n where n.label = \"student\" return n");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (result != null) {
							if(!result.status.equals("success")) {
								showToastFromUIThread("Request failed.");
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
										if(isBeforeJSONObject(index, x)) {
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
							showToastFromUIThread("Result null");
						}
						speakRandomAfterLoadStudentData();
						mFragmentManager.beginTransaction().replace(R.id.robot_canvas, CanvasFragment.getInstance(RobotMainActivity.this)).commit();
						startBottono();
					}
				});
			}
		}).start();
	}
	public void BEGIN() {
		mFragmentManager.beginTransaction().replace(R.id.robot_canvas, CanvasFragment.getInstance(RobotMainActivity.this)).commit();
	}
	/*-------------------------------- AUDIO - TEXT TO SPEECH ----------------------*/
	public void say(final String message, final String language, final boolean stopable) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
					
				} else {
					if (message.isEmpty()) {
						showToastFromUIThread("nothing to say!");
						return;
					}
					boolean result = false;
					try {
						if (!stopable) result = RobotTextToSpeech.say(getRobot(), message, language);
						else result = RobotTextToSpeech.sayStoppable(getRobot(),message, language);
					} catch (final RobotException e) {
						e.printStackTrace();
						
						showToastFromUIThread("say text failed! " + e.getMessage());
						return;
					}
					
					if (!result) {
						showToastFromUIThread("say text failed!");
					}
					Log.d(TAG, "say('" + message + "'): result=" + result);
				}
			}
		}).start();
	}
	public void shutup() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					showToastFromUIThread("stopping...");
					boolean result = false;
					try {
						result = RobotTextToSpeech.shutUp(getRobot());
					} catch (final RobotException e) {
						e.printStackTrace();
						
						showToastFromUIThread("stop failed! " + e.getMessage());
						return;
					}
					
					if (!result) {
						showToastFromUIThread("stop failed!");
					}
				}
			}
		}).start();
	}
	/*----------------------------- MOTION - ACTION ---------------------------------*/
	public void standUp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					boolean result = false;
					try {
						showToastFromUIThread("Standing up...");
						result = RobotMotionAction.standUp(getRobot(), 0.5f);
					} catch (RobotException e) {
						showToastFromUIThread("Stand up failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
					if (result) {
						showToastFromUIThread( "Stand up successfully");
					} else {
						showToastFromUIThread( "Stand up failed");
					}
				}
			}
		}).start();
	}
	public void standInit() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					boolean result = false;
					try {
						showToastFromUIThread("Standing init...");
						RobotPosture.goToPosture(getRobot(), RobotHardware.Posture.STAND_INIT, 0.5f);
					} catch (RobotException e) {
						showToastFromUIThread("Stand up failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
					if (result) {
						showToastFromUIThread( "Stand up successfully");
					} else {
						showToastFromUIThread( "Stand up failed");
					}
				}
			}
		}).start();
	}
	public void sitDown() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					boolean result = false;
					try {
						showToastFromUIThread("Sitting down...");
						result = RobotMotionAction.sitDown(getRobot(), 0.5f);
					} catch (RobotException e) {
						showToastFromUIThread("Sit down failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
					if (result) {
						showToastFromUIThread("Sit Down successfully");
					} else {
						showToastFromUIThread("Sit Down failed");
					}
				}
			}
		}).start();
	}
	public void crouch() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					boolean result = false;
					try {
						showToastFromUIThread("Crouching ...");
						result = RobotPosture.goToPosture(getRobot(), "Crouch",0.5f);
						RobotMotionStiffnessController.rest(getRobot());
					} catch (RobotException e) {
						showToastFromUIThread("Crouch failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
					if (result) {
						showToastFromUIThread( "Crouch successfully");
					} else {
						showToastFromUIThread("Crouch failed");
					}
				}
			}
		}).start();
	}
	public void stepForWard() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					try {
						RobotMotionAction.stepForward(getRobot(), 0.5f);
					} catch (RobotException e) {
						showToastFromUIThread( "Step ForWard failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	public void stepBackWard() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					try {
						// Robot go backward and only stop if stopWalk is called
						RobotMotionAction.stepBackward(getRobot(), 0.5f);
					} catch (RobotException e) {
						showToastFromUIThread( "Step ForWard failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	public void turnLeft() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					try {
						// Robot turn left and only stop if stopWalk is called
						RobotMotionAction.turnLeft(getRobot(), 0.5f);
					} catch (RobotException e) {
						showToastFromUIThread("Turn Left failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	public void turnRight() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					try {
						// Robot turn right and only stop if stopWalk is called
						RobotMotionAction.turnRight(getRobot(), 0.5f);
					} catch (RobotException e) {
						showToastFromUIThread("Turn Right failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	public void stopWalk() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					try {
						// Stop walk, also stop turn left and turn right
						RobotMotionAction.stopWalk(getRobot());
					} catch (RobotException e) {
						showToastFromUIThread("Stop Walk failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	/*------------------------------SENSOR--------------------------------*/
	public void startHeadMiddleSensor() {
		if(getRobot() == null) {
			scan();
		} else {
			RobotEventSubscriber mEventSubscriber = null;
			if (mEventSubscriber == null) {
				mEventSubscriber = new RobotEventSubscriber(getRobot(),
					new OnRobotEventListener() {
						@Override
						public void onRobotEvent(String event, RobotValue data) {
//							takePicture(0, 1, "VGA", false, false);
							showToastFromUIThread("Subscriber " + event + " successfully.");
						}
					});
			}
			boolean result;
			String eventName = RobotEvent.MIDDLE_TACTIL_TOUCHED;
			try {
				result = mEventSubscriber.subscribe(eventName,RobotEvent.TYPE_SYSTEM);
			} catch (final RobotException e) {
				e.printStackTrace();
				showToastFromUIThread("Subscribe event" + eventName + " failed! "+ e.getMessage());
				return;
			}
			if (result) {
				showToastFromUIThread("Subscribe event '" + eventName + "' sucessfully!");
			} else {
				showToastFromUIThread("Subscribe event '" + eventName + "' failed!");
			}
		}
	}
	/*------------------------------- VISION ----------------------------------------*/
	private FaceApiClient faceApi;
	private RobotFaceDetection.Monitor faceMonitor;
	private RobotCameraPreviewView mCameraPreview;
	private RobotCameraPreview mRobotCameraPreview;
	private RobotCamera robotCamera;
	public void startCameraPreview() {
		if (getRobot() == null) {
			scan();
		} else {
			if (mRobotCameraPreview == null) {
				mRobotCameraPreview = RobotCamera.getCameraPreview(getRobot(), RobotCamera.CAMERA_TOP);
			}
			mRobotCameraPreview.setPreviewDisplay(mCameraPreview);
			mRobotCameraPreview.setQuality(mRobotCameraPreview.QUALITY_MEDIUM);
			mRobotCameraPreview.setSpeed(mRobotCameraPreview.SPEED_MEDIUM);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					boolean result = false;
					try {
						result = mRobotCameraPreview.startPreview();
					} catch (final RobotException e) {
						e.printStackTrace();
						showToast("Start camera preview failed: " + e.getMessage().toString());
						return;
					}
					if (result) {
						 showToast("Camera preview started!");
					} else {
						 showToast("Start camera preview failed!");
					}
				}
			});
		}
	}
	public void trainUrl(final ArrayList<String> urls, final String uid, final String nameSpace, final String label) {
		showToastFromUIThread("URLs training ...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				faceApi.trainUrls(urls, uid, nameSpace, label);
				removePreviousDialog();
			}
		}).start();
	}
	private void train(final String uid, final String namespace, final String label, final int numberOfPictures) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String[] fileNames = robotCamera.takePictures(RobotCamera.PICTURE_RESOLUTION_VGA, RobotCamera.PICTURE_FORMAT_JPG, numberOfPictures, 1000);
					ArrayList<File> imageFiles = new ArrayList<File>();
					for (String s : fileNames) {
						imageFiles.add(new File(s));
					}
					showToastFromUIThread("Training ...");
					faceApi.trainFiles(imageFiles, uid, namespace, label);
				} catch (RobotException e) {
					removePreviousDialog();
					e.printStackTrace();
				}
			}
		}).start();
	}
	/*------------------------------ DATABASE --------------------------------------*/
	public void queryNeo4j(final String input) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (input == "") {
					showToastFromUIThread("Query is empty");
				} else {
					showToastFromUIThread("Processing ...");
					CypherRequest.setURL(queryURL);
					final CypherResult result = CypherRequest.makeRequest(accessToken, input);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (result != null) {
								if(!result.status.equals("success")) {
									showToastFromUIThread("Request failed.");
								} else {
									showToastFromUIThread(result.result);
								}
							} else {
								showToastFromUIThread("Result null");
							}
						}
					});
					removePreviousDialog();
				}
			}
		}).start();
	}
	/*------------------------------ GESTURE ---------------------------------------*/
	public ArrayList<String> installedGestureList = new ArrayList<String>();
	public ControlerGestureListviewAdapter installedGestureListAdapter;
	public PullToRefreshListView lvInstalledGestureList;
	public int selectedGestureIndex = ListView.INVALID_POSITION;
	public void initGestureModule(PullToRefreshListView lv){
		lvInstalledGestureList = lv;
		refreshInstalledGestureList();
		installedGestureListAdapter = new ControlerGestureListviewAdapter(RobotMainActivity.this, RobotMainActivity.this, 0, installedGestureList);
		installedGestureListAdapter.setNotifyOnChange(true);
		lv.setAdapter(installedGestureListAdapter);
	}
	public void refreshInstalledGestureList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					final String[] behaviorList;
					showToastFromUIThread("Getting installed gestures list ...");
					try {
						behaviorList = RobotGesture.getGestureList(getRobot());
					} catch (final RobotException e) {
						e.printStackTrace();
						showToastFromUIThread("Get installed gestures failed! "+ e.getMessage());
						return;
					}
					if (behaviorList == null || behaviorList.length == 0) {
						showToastFromUIThread("None of behavior is available!");
						return;
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							selectedGestureIndex = ListView.INVALID_POSITION;
							installedGestureListAdapter.clear();
							installedGestureList.clear();
							for (int i = 0; i < behaviorList.length; i++) {
								installedGestureListAdapter.add(behaviorList[i]);
								installedGestureList.add(behaviorList[i]);
							}
							installedGestureListAdapter.notifyDataSetChanged();
							removePreviousDialog();
						}
					});
				}
			}
		}).start();
	}
	public void runGesture(final String name) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					showToastFromUIThread( "Gesture: " + name);
					RobotGesture.runGesture(getRobot(), name);
				} catch (RobotException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	/*----------------------------------- SONAR --------------------------*/
	public float getLeftSonarValue() {
		try {
			float value = RobotSonar.getLeftSonarValue(getRobot());
			return value;
		} catch (RobotException e) {
			showToastFromUIThread("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
	public float getRightSonarValue() {
		try {
			float value = RobotSonar.getRightSonarValue(getRobot());
			return value;
		} catch (RobotException e) {
			showToastFromUIThread("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
	public float[] getLeftSonarValues() {
		showToastFromUIThread("Getting left sonnars value...");
		try {
			float[] values = RobotSonar.getLeftSonarValues(getRobot());
			return values;
		} catch (RobotException e) {
			showToastFromUIThread("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	public float[] getRightSonarValues() {
		showToastFromUIThread("Getting right sonnars value...");
		try {
			float[] values = RobotSonar.getRightSonarValues(getRobot());
			return values;
		} catch (RobotException e) {
			showToastFromUIThread("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	/*------------------------------ BEHAVIOR ---------------------------------------*/
	public ArrayList<String> installedBehaviorList = new ArrayList<String>();
	public ControlerBehaviorListviewAdapter installedBehaviorListAdapter;
	public PullToRefreshListView lvInstalledBehaviorList;
	public int selectedBehaviorIndex = ListView.INVALID_POSITION;
	public void initBehaviorModule(PullToRefreshListView lv){
		lvInstalledBehaviorList = lv;
		refreshInstalledBehaviorList();
		installedBehaviorListAdapter = new ControlerBehaviorListviewAdapter(RobotMainActivity.this, RobotMainActivity.this, 0, installedBehaviorList);
		installedBehaviorListAdapter.setNotifyOnChange(true);
		lv.setAdapter(installedBehaviorListAdapter);
	}
	public void refreshInstalledBehaviorList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					final String[] behaviorList;
					try {
						showToastFromUIThread("Getting installed behavior list ...");
						behaviorList = RobotBehavior.getInstalledBehaviors(getRobot());
					} catch (final RobotException e) {
						e.printStackTrace();
						showToastFromUIThread("Get installed behaviors failed! "+ e.getMessage());
						return;
					}
					if (behaviorList == null || behaviorList.length == 0) {
						showToastFromUIThread("None of behavior is available!");
						return;
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							selectedBehaviorIndex = ListView.INVALID_POSITION;
							installedBehaviorListAdapter.clear();
							installedBehaviorList.clear();
							for (int i = 0; i < behaviorList.length; i++) {
								installedBehaviorListAdapter.add(behaviorList[i]);
								installedBehaviorList.add(behaviorList[i]);
							}
							installedBehaviorListAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		}).start();
	}
	public void runBehavior(final String behaviorName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					showToastFromUIThread("Running " + behaviorName + "...");
					RobotBehavior.runBehavior(getRobot(), behaviorName);
					removePreviousDialog();
				} catch (final RobotException e) {
					e.printStackTrace();
					showToastFromUIThread("Run " + behaviorName + " failed! " + e.getMessage());
					return;
				}
			}
		}).start();
	}
	public void stopBehavior(final String behaviorName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					showToastFromUIThread("Stopping " + behaviorName + "...");
					RobotBehavior.stopBehavior(getRobot(), behaviorName);
				} catch (final RobotException e) {
					e.printStackTrace();
					showToastFromUIThread("Stop " + behaviorName + " failed! "+ e.getMessage());
					return;
				}
			}
		}).start();
	}
	public void stopAllBehaviors() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					scan();
				} else {
					try {
						showToastFromUIThread("Stopping all behaviors...");
						RobotBehavior.stopAllBehaviors(getRobot());
					} catch (final RobotException e) {
						e.printStackTrace();
						showToastFromUIThread("Stop all behaviors failed! " + e.getMessage());
						return;
					}
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	/* ---------------------------------------------------------------- */
	private static final long serialVersionUID = -6313302259718885854L;
	public static final String TAG = "UET-INVINCIBLE";
	public final static String apiKey = "6b6603c0e2b34e679e8b9d09a253922e";
	public final static String apiSecret = "92b7dd7243c645ef895ff7f4a2084d0a";
	public String accessToken = "47a461cb-3519-4e46-82a4-4cb91644b4dd";
	public String queryURL = "https://fti.pagekite.me/graph4mrc/excute";
}
