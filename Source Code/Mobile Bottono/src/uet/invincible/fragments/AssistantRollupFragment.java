package uet.invincible.fragments;

import java.util.ArrayList;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.adapters.StudentRollupAdapter;
import uet.invincible.bases.BaseFragment;
import uet.invincible.libs.pulltorefresh.PullToRefreshBase;
import uet.invincible.libs.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import uet.invincible.libs.pulltorefresh.PullToRefreshListView;
import uet.invincible.mobile.R;
import uet.invincible.models.CheckinModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import uet.invincible.utilities.SystemUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AssistantRollupFragment extends BaseFragment implements OnRefreshListener<ListView> {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_rollup, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	public static void startRollup() {

	}
	public ArrayList<StudentModel> mStudentList;
	public static StudentRollupAdapter mStudentRollupListAdapter;
	public static PullToRefreshListView mStudentRollupListView;
	public static void initStudentListView() {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentRollupListAdapter = new StudentRollupAdapter(mRobotActivity, mRobotActivity, 0, Saved.studentList);
				mStudentRollupListView.setAdapter(mStudentRollupListAdapter);
				mStudentRollupListAdapter.setNotifyOnChange(true);
			}
		});
	}
	public void addStudentIntoListView(final StudentModel student) {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentRollupListAdapter.add(student);
				mStudentRollupListAdapter.setNotifyOnChange(true);
			}
		});
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		default:
			break;
		}
	}
	@Override
	protected void initModels() {
		initStudentListView();
	}

	@Override
	protected void initViews(View view) {
		mStudentRollupListView = (PullToRefreshListView) view.findViewById(R.id.fragment_student_rollup_listview);
		mStudentRollupListView.setOnRefreshListener(this);
	}

	@Override
	protected void initAnimations() {
		// TODO Auto-generated method stub
	}
	
	private static final long serialVersionUID = -1379377360733503854L;
	private static AssistantRollupFragment mInstance;
	public static RobotMainActivity mRobotActivity;
	public static AssistantRollupFragment getInstance(RobotMainActivity mRobotActivity2) {
		if (mInstance == null) {
			mInstance = new AssistantRollupFragment();
		}
		mRobotActivity = mRobotActivity2;
		return mInstance;
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		new Thread(new Runnable() {
			@Override
			public void run() {
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
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mStudentRollupListAdapter = new StudentRollupAdapter(mRobotActivity, mRobotActivity, 0, Saved.studentList);
							mStudentRollupListView.setAdapter(mStudentRollupListAdapter);
							mStudentRollupListAdapter.setNotifyOnChange(true);
							mStudentRollupListView.onRefreshComplete();
						}
					});
				}
				else mRobotActivity.showToastFromUIThread("Load all rollup data: Request failed.");
			}
		}).start();
	}
}
