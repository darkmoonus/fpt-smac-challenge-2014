package uet.invincible.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import uet.invincible.bases.BaseActivity;
import uet.invincible.fragments.CanvasFragment;
import uet.invincible.fragments.SplashFragment;
import uet.invincible.jsoup.QuestionContent;
import uet.invincible.mobile.R;
import uet.invincible.models.AccountModel;
import uet.invincible.models.CheckinModel;
import uet.invincible.models.QuestionModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import uet.invincible.utilities.Log;
import uet.invincible.utilities.SystemUtil;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;

@SuppressLint({ "NewApi", "DefaultLocale" })
public class RobotMainActivity extends BaseActivity implements OnClickListener{
	public static String getActionFromQuery(String query) {
        String[] nullAnswers = {"Xin lỗi, tôi chưa thể làm việc này.", "Tôi không đủ quyền để làm việc này.", "Xin lỗi tôi phiên bản này chưa hỗ trợ chức năng bạn yêu cầu!"};
        String[] suggestActions = {"Hiện tại, bạn có thể: Làm bài thi lịch sử, Tra điểm thi, Xem thông tin cá nhân..."};
        String result = nullAnswers[(int)Math.random()*nullAnswers.length] + " " + suggestActions[(int)Math.random()*suggestActions.length];

        query = QuestionContent.removeQuestionWords(query).toLowerCase();
        if (query.equals("")) {
            result = "Bạn chưa nhập truy vấn." + " " + suggestActions[(int)Math.random()*suggestActions.length];
        }

        String[] subjectQuery = {"môn lịch sử", "môn sử", "lịch sử"};
        String[] testQuery = {"kiểm tra", "thi"};
        boolean fl = false;
        boolean fe = false;
        for (String subject : subjectQuery) {
            if (query.contains(subject)){
                fl = true;
            }
        }
        for (String test : testQuery) {
            if (query.contains(test)){
                fe = true;
            }
        }
        if (fe) {
            if (fl) {
                result = "THI LỊCH SỬ";
            } else {
                result = "Bạn chưa chỉ rõ môn thi hoặc tôi chưa có dữ liệu cho môn học này. Bạn vui lòng liên hệ với nhà phát triển để bổ sung!";
            }
        }

        String[] examResultQuery = {"điểm thi", "kết quả thi", "điểm kiểm tra", "kết quả kiểm tra", "kết quả thi", "kết quả kiểm tra"};
        boolean isResult = false;
        for (String point : examResultQuery) {
            if (query.contains(point)){
                isResult = true;
            }
        }
        if (isResult) {
            result = "TRA ĐIỂM THI";
        }

        String[] rollOutQuery = {"điểm danh"};
        boolean isRollOut = false;
        for (int i = 0; i < rollOutQuery.length; ++i) {
            if (query.contains(rollOutQuery[i])) {
                isRollOut = true;
                break;
            }
        }
        if (isRollOut) {
            result = "ĐIỂM DANH";
        }

        String[] sendMailQuery = {"mail", "email"};
        boolean isSendEmail = false;
        for (int i = 0; i < sendMailQuery.length; ++i) {
            if (query.contains(sendMailQuery[i])) {
                isSendEmail = true;
                break;
            }
        }
        if (isSendEmail) {
            result = "GỬI MAIL KẾT QUẢ";
        } else if (query.contains("gửi kết quả") || query.contains("gửi điểm thi")) {
            result = "Tôi có thể gửi điểm thi qua email. Nhưng bạn hãy yêu cầu lại thật rõ ràng nhé!";
        }

        String[] inforQuery = {"thông tin của tôi", "thông tin của mình", "thông tin cá nhân"};
        boolean isInfor = false;
        for (int i = 0; i < inforQuery.length; ++i) {
            if (query.contains(inforQuery[i])) {
                isInfor = true;
                break;
            }
        }
        if (isInfor) {
            result = "THÔNG TIN CÁ NHÂN";
        }
        return result;
    }
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_robot);
		FragmentManager mFragmentManager = getSupportFragmentManager();
		mFragmentManager.beginTransaction().add(R.id.robot_canvas, SplashFragment.getInstance(RobotMainActivity.this)).commit();
		new Saved();
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
	public void doSomething(String s) {
		if(contain(s, "điểm danh")) {
			showToastFromUIThread("OK! bắt đầu điểm danh");
		} else 
		if(contain(s, "xin chào")) {
			showToastFromUIThread("OK! Xin chào.");  
		} else
		if(contain(s, "bật máy chiếu")) {
			showToastFromUIThread("OK! bật máy chiếu");
		} else
		if(contain(s, "tắt máy chiếu")) {
			showToastFromUIThread("OK! tắt máy chiếu");
		} else 
		if(contain(s, "bắt đầu") && contain(s, "kiểm tra")) {
			showToastFromUIThread("OK! bắt đầu kiểm tra");
		} else 
		if(contain(s, "tạm biệt")) {
			showToastFromUIThread("OK! tạm biệt");
		} else {
			showToastFromUIThread("Mình không hiểu");
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
	public void BEGIN() {
		mFragmentManager.beginTransaction().replace(R.id.robot_canvas, CanvasFragment.getInstance(RobotMainActivity.this)).commit();
	}
	public void queryNeo4j(final String input) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (input == "") {
				} else {
					CypherRequest.setURL(queryURL);
					final CypherResult result = CypherRequest.makeRequest(accessToken, input);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (result != null) {
								if(!result.status.equals("success")) {
									showToastFromUIThread("Request failed.");
								} else {
									showToastFromUIThread("Request successful !");
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

	private static final long serialVersionUID = -6313302259718885854L;
	public static final String TAG = "UET-INVINCIBLE";
	public String accessToken = "47a461cb-3519-4e46-82a4-4cb91644b4dd";
	public String queryURL = "https://fti.pagekite.me/graph4mrc/excute";
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	@Override
	public void onClick(View v) {
		
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
}
