package uet.invincible.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.adapters.StudentListAdapter;
import uet.invincible.adapters.StudentRollupAdapter;
import uet.invincible.bases.BaseFragment;
import uet.invincible.libs.pulltorefresh.PullToRefreshBase;
import uet.invincible.libs.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import uet.invincible.libs.pulltorefresh.PullToRefreshListView;
import uet.invincible.listeners.TwoChosenDialogListener;
import uet.invincible.mobile.R;
import uet.invincible.models.CheckinModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import uet.invincible.utilities.SystemUtil;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;

@SuppressLint("DefaultLocale")
public class AssistantStudentFragment extends BaseFragment implements OnRefreshListener<ListView>{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_students, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	@Override
	protected void initModels() {
		initStudentListView();
	}

	public static void initStudentListView() {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentListAdapter = new StudentListAdapter(mRobotActivity, mRobotActivity, 0, Saved.studentList);
				mStudentListView.setAdapter(mStudentListAdapter);
				mStudentListAdapter.setNotifyOnChange(true);
			}
		});
	}
	public static void initSearchStudentListView(final ArrayList<StudentModel> studentList) {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentListAdapter = new StudentListAdapter(mRobotActivity, mRobotActivity, 0, studentList);
				mStudentListView.setAdapter(mStudentListAdapter);
				mStudentListAdapter.setNotifyOnChange(true);
			}
		});
	}
	public void addStudentIntoListView(final StudentModel student) {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentListAdapter.add(student);
				mStudentListAdapter.setNotifyOnChange(true);
			}
		});
	}
	public void removeStudentIntoListView(final int studentId) {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentListAdapter.remove(studentId);
				mStudentListAdapter.setNotifyOnChange(true);
			}
		});
	}
	
	@Override
	protected void initViews(View view) {
		mStudentAdd = (TextView) view.findViewById(R.id.fragment_student_manager_add);
		mStudentAdd.setOnClickListener(this);
		mStudentSearchButton = (TextView) view.findViewById(R.id.fragment_student_manager_search_button);
		mStudentSearchButton.setOnClickListener(this);
		mStudentListView = (PullToRefreshListView) view.findViewById(R.id.fragment_student_manager_listview);
		mStudentListView.setOnRefreshListener(this);
		mStudentSearch = (EditText) view.findViewById(R.id.fragment_student_manager_search);
		mStudentAddLayout = (LinearLayout) view.findViewById(R.id.fragment_student_manager_add_student_layout);
		
		mStudentDelete = (TextView) view.findViewById(R.id.fragment_student_manager_delete_all);
		mStudentDelete.setOnClickListener(this);
	}

	@Override
	protected void initAnimations() {
		
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fragment_student_manager_add:
			mRobotActivity.showAddStudentDiaalog(mRobotActivity);
			break;
		case R.id.fragment_student_manager_delete_all:
			mRobotActivity.showTwoChosenDialog("Bottono", "Delete all students ?", "OK", "Cancel", new TwoChosenDialogListener() {
				@Override
				public void onLeftClick(Dialog dialog) {
					mRobotActivity.removePreviousDialog();
					new Thread(new Runnable() {
						@Override
						public void run() {
							mRobotActivity.showToastFromUIThread("Chức năng này chỉ dành cho admin.");
//							mRobotActivity.queryNeo4j("match n where n.label = \"student\"delete n");
//							mRobotActivity.runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									mStudentListAdapter.removeAll();
//									mStudentListView.setAdapter(mStudentListAdapter);	
//								}
//							});
						}
					}).start();
				}
				@Override
				public void onRightClick(Dialog dialog) {
					mRobotActivity.removePreviousDialog();
				}
			});
			break;
		case R.id.fragment_student_manager_search_button:
				ArrayList<StudentModel> studentList = new ArrayList<StudentModel>();
				String name = mStudentSearch.getText().toString().toLowerCase();
				for(int i=0; i<Saved.studentList.size(); i++) {
					String sName = Saved.studentList.get(i).name.toLowerCase();
					if(sName.contains(name)){
						studentList.add(Saved.studentList.get(i));
					}
				}
				initSearchStudentListView(studentList);
			break;
		default:
			break;
		}
	}
	
	public TextView mStudentAdd;
	public TextView mStudentDelete;
	
	public EditText mStudentSearch;
	public TextView mStudentSearchButton;
	public boolean mIsSearch = false;
	public static PullToRefreshListView mStudentListView;
	public LinearLayout mStudentAddLayout;
	public int mStudentAddLayoutStatus = 0;
	public ArrayList<StudentModel> mStudentList;
	public static StudentListAdapter mStudentListAdapter;
	
	public static final long serialVersionUID = 893778922592521847L;
	public static AssistantStudentFragment mInstance;
	public static RobotMainActivity mRobotActivity;
	public static AssistantStudentFragment getInstance(RobotMainActivity mRobotActivity2) {
		if (mInstance == null) {
			mInstance = new AssistantStudentFragment();
		}
		mRobotActivity = mRobotActivity2;
		return mInstance;
	}
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		final ArrayList<StudentModel> studentList = new ArrayList<StudentModel>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				CypherRequest.setURL(mRobotActivity.queryURL);
				final CypherResult result = CypherRequest.makeRequest(mRobotActivity.accessToken, "match n where n.label = \"student\" return n");
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
									
									CypherRequest.setURL(mRobotActivity.queryURL);
									final CypherResult result0 = CypherRequest.makeRequest(mRobotActivity.accessToken, "match n where n.label='rollup' and n.date=\'" + SystemUtil.getDate() + "\' return n");
									if(result0.result == "") {
										final CypherResult result1 = CypherRequest.makeRequest(mRobotActivity.accessToken, "CREATE (n { label:\"rollup\", date:\"" + SystemUtil.getDate() + "\" }) RETURN n");
										final CypherResult result2 = CypherRequest.makeRequest(mRobotActivity.accessToken, "match m, n where m.label = \"student\" and n.label = \"rollup\" and n.date = \"" + SystemUtil.getDate() + "\" create m-[:status{status:\"false\"}]->n");
										if(result1 == null || result2 == null) mRobotActivity.showToastFromUIThread("Load rollup daily data: Result null");
										else if(result1.status.equals("success") && result2.status.equals("success"))mRobotActivity.showToastFromUIThread("Load rollup daily data successful");
										else mRobotActivity.showToastFromUIThread("Load rollup daily data: Request failed.");
									} 
									final CypherResult result3 = CypherRequest.makeRequest(mRobotActivity.accessToken, "match m, n, m-[r]->n where m.label = \"student\" and n.label = \"rollup\" return m.uId, n.date, r.status");
									if(result3 == null) mRobotActivity.showToastFromUIThread("Load all rollup data : Result null");
									else if(result3.status.equals("success")) {
										String x1 = "";
										for(int i=0; i<result3.result.length(); i++){
											if(result3.result.charAt(i) != '}' && result3.result.charAt(i) != '{' && result3.result.charAt(i) != ',' && result3.result.charAt(i) != ':' ) {
												x1 += result3.result.charAt(i);
											}
										}
										String ss = "";
										for(int i=1; i<x1.length(); i++) {
											if(x1.charAt(i) != '\"' || (x1.charAt(i) != x1.charAt(i-1))) {
												ss += x1.charAt(i);
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
									else mRobotActivity.showToastFromUIThread("Load all rollup data: Request failed.");
									
									mRobotActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											mStudentListAdapter = new StudentListAdapter(mRobotActivity, mRobotActivity, 0, Saved.studentList);
											mStudentListView.setAdapter(mStudentListAdapter);
											mStudentListAdapter.setNotifyOnChange(true);
											mStudentListView.onRefreshComplete();
										}
									});
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						} else {
							mRobotActivity.showToastFromUIThread("Result null");
						}
			}
		}).start();
	}
}
