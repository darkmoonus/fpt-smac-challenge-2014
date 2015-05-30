package uet.invincible.fragments;

import java.util.ArrayList;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.adapters.StudentRollupAdapter;
import uet.invincible.bases.BaseFragment;
import uet.invincible.libs.pulltorefresh.PullToRefreshListView;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fpt.robot.sensors.RobotSonar.Monitor;
import com.fpt.robot.tts.RobotTextToSpeech;

public class AssistantRollupFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_rollup, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	public static void startRollup() {
		mRobotActivity.speak("Bắt đầu điểm danh", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
	}
	public ArrayList<StudentModel> mStudentList;
	public static StudentRollupAdapter mStudentRollupListAdapter;
	public static PullToRefreshListView mStudentRollupListView;
	public static void initStudentListView() {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				ArrayList<CheckinModel> checkinList = new ArrayList<CheckinModel>();
//				for(int i=0; i<20; i++) {
//					StudentModel newStudent = new StudentModel("Việt Anh Vũ", "Male", "vav", "12020010", "vietanhuet@gmail.com", "01684015252", "10", "9.5", "9", "9.7", "20", "K57CLC", "Mã sinh viên: 12020010, K57CLC", checkinList);
//					Saved.studentList.add(newStudent);
//				}
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
	}

	@Override
	protected void initAnimations() {
		// TODO Auto-generated method stub
	}
	
	private Monitor monitor;
	
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
}
