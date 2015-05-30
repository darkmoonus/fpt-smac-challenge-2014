package uet.invincible.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.adapters.StudentExamAdapter;
import uet.invincible.bases.BaseFragment;
import uet.invincible.libs.pulltorefresh.PullToRefreshBase;
import uet.invincible.libs.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import uet.invincible.libs.pulltorefresh.PullToRefreshListView;
import uet.invincible.mobile.R;
import uet.invincible.models.QuestionModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AssistantExamFragment extends BaseFragment implements OnRefreshListener<ListView>, OnItemClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_exam, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	public static void startExamination() {

	}
	public ArrayList<StudentModel> mStudentList;
	public static StudentExamAdapter mStudentExamListAdapter;
	public static PullToRefreshListView mStudentExamListView;
	public static void initStudentListView() {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentExamListAdapter = new StudentExamAdapter(mRobotActivity, mRobotActivity, 0, Saved.studentList);
				mStudentExamListView.setAdapter(mStudentExamListAdapter);
				mStudentExamListAdapter.setNotifyOnChange(true);
			}
		});
	}
	public void addStudentIntoListView(final StudentModel student) {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentExamListAdapter.add(student);
				mStudentExamListAdapter.setNotifyOnChange(true);
			}
		});
	}
	@Override
	protected void initModels() {
		initStudentListView();
	}
	@Override
	protected void initViews(View view) {
		mStudentExamListView = (PullToRefreshListView) view.findViewById(R.id.fragment_student_exam_listview);
		mStudentExamListView.setOnRefreshListener(this);
		mStudentExamListView.setOnItemClickListener(this);
	}
	@Override
	protected void initAnimations() {
	}
	private static final long serialVersionUID = -1379377360733503854L;
	private static AssistantExamFragment mInstance;
	public static RobotMainActivity mRobotActivity;
	public static AssistantExamFragment getInstance(RobotMainActivity mRobotActivity2) {
		if (mInstance == null) {
			mInstance = new AssistantExamFragment();
		}
		mRobotActivity = mRobotActivity2;
		return mInstance;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mRobotActivity.showExamDetailDialog(mRobotActivity, Saved.studentList.get(arg2-1)); 
	}
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		new Thread(new Runnable() {
			@Override
			public void run() {
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
									mRobotActivity.removePreviousDialog();
								} catch (JSONException e) {
									e.printStackTrace();
								}
								mRobotActivity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mStudentExamListAdapter = new StudentExamAdapter(mRobotActivity, mRobotActivity, 0, Saved.studentList);
										mStudentExamListView.setAdapter(mStudentExamListAdapter);
										mStudentExamListAdapter.setNotifyOnChange(true);
										mStudentExamListView.onRefreshComplete();
									}
								});
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
