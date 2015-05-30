package uet.invincible.fragments;

import java.util.ArrayList;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.adapters.StudentListAdapter;
import uet.invincible.bases.BaseFragment;
import uet.invincible.libs.pulltorefresh.PullToRefreshListView;
import uet.invincible.models.CheckinModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.tts.RobotTextToSpeech;

public class AssistantStudentFragment extends BaseFragment {
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
//		mStudentListView.setOnRefreshListener(PullToRefreshBase.OnRefreshListener2<ListView>);	
	}

	public static void initStudentListView() {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				ArrayList<CheckinModel> checkinList = new ArrayList<CheckinModel>();
//				for(int i=0; i<20; i++) {
//					StudentModel newStudent = new StudentModel("Việt Anh Vũ", "Male", "vav", "12020010", "vietanhuet@gmail.com", "01684015252", "10", "9.5", "9", "9.7", "20", "K57CLC", "Mã sinh viên: 12020010, K57CLC", checkinList);
//					Saved.studentList.add(newStudent);
//				}
				mStudentListAdapter = new StudentListAdapter(mRobotActivity, mRobotActivity, 0, Saved.studentList);
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
	@Override
	protected void initViews(View view) {
		mStudentAdd = (TextView) view.findViewById(R.id.fragment_student_manager_add);
		mStudentAdd.setOnClickListener(this);
		mStudentSearchButton = (TextView) view.findViewById(R.id.fragment_student_manager_search_button);
		mStudentSearchButton.setOnClickListener(this);
		mStudentListView = (PullToRefreshListView) view.findViewById(R.id.fragment_student_manager_listview);
		mStudentSearch = (EditText) view.findViewById(R.id.fragment_student_manager_search);
		mStudentAddLayout = (LinearLayout) view.findViewById(R.id.fragment_student_manager_add_student_layout);
		mStudentAddCreate = (TextView) view.findViewById(R.id.fragment_student_manager_add_student_create);
		mStudentAddCreate.setOnClickListener(this);
		mStudentAddName = (EditText) view.findViewById(R.id.fragment_student_manager_add_student_name);
		mStudentAddId = (EditText) view.findViewById(R.id.fragment_student_manager_add_student_uid);
		mStudentAddCode = (EditText) view.findViewById(R.id.fragment_student_manager_add_student_code);
		mStudentAddAge = (EditText) view.findViewById(R.id.fragment_student_manager_add_student_age);
		mStudentAddClass = (EditText) view.findViewById(R.id.fragment_student_manager_add_student_class);
		mStudentAddEmail = (EditText) view.findViewById(R.id.fragment_student_manager_add_student_email);
		mStudentAddMobilePhone = (EditText) view.findViewById(R.id.fragment_student_manager_add_student_mobile);
		mStudentAddAddress = (EditText) view.findViewById(R.id.fragment_student_manager_add_student_address);
		mStudentAddCheckboxFemale = (CheckBox) view.findViewById(R.id.fragment_student_manager_add_student_checkbox_female);
		mStudentAddCheckboxFemale.setOnClickListener(this);
		mStudentAddCheckboxMale = (CheckBox) view.findViewById(R.id.fragment_student_manager_add_student_checkbox_male);
		mStudentAddCheckboxMale.setOnClickListener(this);
		mStudentDelete = (TextView) view.findViewById(R.id.fragment_student_manager_delete_all);
		mStudentDelete.setOnClickListener(this);
	}

	@Override
	protected void initAnimations() {
		// TODO Auto-generated method stub
		
	}
	
	public void greet(String name) {
		final String greeting = "Chào " + name;
		final String goIntoClassroom = "Mời bạn về chỗ.";
		final String gesture1 = "Give";
		final String gesture2 = "Far";
		new Thread(new Runnable() {
			@Override
			public void run() {
				Robot robot = mRobotActivity.getRobot();
				if (robot != null) {
					String language = RobotTextToSpeech.ROBOT_TTS_LANG_VI;
					boolean result1 = false;
					boolean result2 = false;
					
					mRobotActivity.showToastFromUIThread("Greeting ...");
					try {
						result1 = RobotTextToSpeech.say(robot, greeting, language);
						RobotGesture.runGesture(mRobotActivity.getRobot(),gesture1);
						result2 = RobotTextToSpeech.say(robot, goIntoClassroom, language);
						RobotGesture.runGesture(mRobotActivity.getRobot(),gesture2);
					} catch (final RobotException e) {
						e.printStackTrace();
						 mRobotActivity.showToastFromUIThread("Greeting failed! " + e.getMessage());
						return;
					}
					if (!result1 || !result2) {
						 mRobotActivity.showToastFromUIThread("Greeting failed!");
					}
				}
			}
		}).start();
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fragment_student_manager_add_student_checkbox_male:
			mStudentAddCheckboxFemale.setChecked(false);
			mStudentAddCheckboxMale.setChecked(true);
			break;
		case R.id.fragment_student_manager_add_student_checkbox_female:
			mStudentAddCheckboxFemale.setChecked(true);
			mStudentAddCheckboxMale.setChecked(false);
			break;
		case R.id.fragment_student_manager_add:
			if(mStudentAddLayoutStatus == 0) {
				mStudentAddLayoutStatus = 1;
				mStudentAddLayout.setVisibility(View.VISIBLE);
			} else {
				mStudentAddLayoutStatus = 0;
				mStudentAddLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.fragment_student_manager_delete_all:
//			mRobotActivity.showTwoChosenDialog("Bottono", "Delete all students ?", "OK", "Cancel", new TwoChosenDialogListener() {
//				@Override
//				public void onLeftClick(Dialog dialog) {
//					mRobotActivity.removePreviousDialog();
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							mRobotActivity.queryNeo4j("match n where n.label = \"student\"delete n");
//							mRobotActivity.runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									mStudentListAdapter.removeAll();
//									mStudentListView.setAdapter(mStudentListAdapter);	
//								}
//							});
//						}
//					}).start();
//				}
//				@Override
//				public void onRightClick(Dialog dialog) {
//					mRobotActivity.removePreviousDialog();
//				}
//			});
			break;
		case R.id.fragment_student_manager_search:
			
			break;
		case R.id.fragment_student_manager_add_student_create:
			if(!Saved.duplicateStudentId( mStudentAddId.getText().toString())) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						String gender = "male";
						if(mStudentAddCheckboxMale.isChecked()) gender = "male"; else gender = "female";
						String queryString = "CREATE(n{app:\"bottono\", label:\"student\",name:" + "\"" +
								mStudentAddName.getText().toString() + "\"" +
								", uId:" + "\"" + mStudentAddId.getText().toString() + "\"" +
								", gender:" + "\"" + gender + "\"" +
								", studentCode:" + "\"" + mStudentAddCode.getText().toString() + "\"" +
								", email:" + "\"" +mStudentAddEmail.getText().toString() + "\"" +
								", mobilePhone:" + "\"" + mStudentAddMobilePhone.getText().toString() + "\"" +
								", age:" + "\"" + mStudentAddAge.getText().toString() + "\"" +
								", classs:" + "\"" + mStudentAddClass.getText().toString() + "\"" +
								", address:" + "\"" + mStudentAddAddress.getText().toString() + "\"" +
								", cuoiKi:\"0\", giuaKi:\"0\", chuyenCan:\"0\", tongKet:\"0\"}) RETURN n";
						mRobotActivity.queryNeo4j(queryString);
						ArrayList<CheckinModel> checkinList = new ArrayList<CheckinModel>();
						StudentModel newStudent = new StudentModel(mStudentAddName
								.getText().toString(), gender, mStudentAddId
								.getText().toString(), mStudentAddCode.getText()
								.toString(), mStudentAddEmail.getText().toString(),
								mStudentAddMobilePhone.getText().toString(), "0",
								"0", "0", "0", mStudentAddAge.getText().toString(),
								mStudentAddClass.getText().toString(),
								mStudentAddAddress.getText().toString(),
								checkinList);
						Saved.studentList.add(newStudent);
						addStudentIntoListView(newStudent);
						mRobotActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mStudentAddLayoutStatus = 0;
								mStudentAddLayout.setVisibility(View.GONE);
							}
						});
					}
				}).start();
			} else {
				mRobotActivity.showToastFromUIThread("Duplicate student id !");
			}
			break;
		default:
			break;
		}
	}
	
	public TextView mStudentAdd;
	public TextView mStudentDelete;
	public TextView mStudentAddCreate;
	public EditText mStudentAddName;
	public EditText mStudentAddId;
	public EditText mStudentAddCode;
	public EditText mStudentAddAge;
	public EditText mStudentAddClass;
	public EditText mStudentAddEmail;
	public EditText mStudentAddMobilePhone;
	public EditText mStudentAddAddress;
	public CheckBox mStudentAddCheckboxMale;
	public CheckBox mStudentAddCheckboxFemale;
	
	public EditText mStudentSearch;
	public TextView mStudentSearchButton;
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
	
}
