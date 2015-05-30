package uet.invincible.dialogs;


import java.util.ArrayList;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.fragments.AssistantExamFragment;
import uet.invincible.fragments.AssistantRollupFragment;
import uet.invincible.fragments.AssistantStudentFragment;
import uet.invincible.mobile.R;
import uet.invincible.models.CheckinModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import uet.invincible.utilities.SystemUtil;
import vn.adflex.ads.h.n;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class AddStudentDialog extends DialogFragment implements OnClickListener {
	public Context mContext;
	public LayoutInflater mInflater;
	protected Dialog mDialog;
	public RobotMainActivity mRobotActivity;
	
	private AddStudentDialog(Context context, RobotMainActivity mRobotActivity) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mRobotActivity = mRobotActivity;
	}

	public static AddStudentDialog newInstance(Context context, RobotMainActivity mRobotActivity) {
		AddStudentDialog alertDialog = new AddStudentDialog(context, mRobotActivity);
		Bundle bundle = new Bundle();
		alertDialog.setArguments(bundle);
		return alertDialog;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			
		}
	}
	
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDialog = new Dialog(mContext);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = mInflater.inflate(R.layout.dialog_create_student, null);
	
		initViews(view);
		setViews();
		initOnClick();
		initAnimation();
		
		mDialog.setContentView(view);
		return mDialog;
	}
	
	public void initViews(View view) {
		mStudentAddCreate = (TextView) view.findViewById(R.id.fragment_student_manager_add_student_create);
		mStudentAddCreate.setOnClickListener(this);
		mStudentAddCancel = (TextView) view.findViewById(R.id.fragment_student_manager_add_student_cancel);
		mStudentAddCancel.setOnClickListener(this);
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
	}
	public void setViews() {
		
	}
	
	public void initOnClick() {
		
	}
	
	public void initAnimation() {
		
	}
	
	public TextView mStudentAddCreate;
	public TextView mStudentAddCancel;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_student_manager_add_student_cancel:
			mDialog.dismiss();
			break;
		case R.id.fragment_student_manager_add_student_checkbox_male:
			mStudentAddCheckboxFemale.setChecked(false);
			mStudentAddCheckboxMale.setChecked(true);
			break;
		case R.id.fragment_student_manager_add_student_checkbox_female:
			mStudentAddCheckboxFemale.setChecked(true);
			mStudentAddCheckboxMale.setChecked(false);
			break;
		case R.id.fragment_student_manager_add_student_create:
			if(!Saved.duplicateStudentId( mStudentAddId.getText().toString())) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						String name = mStudentAddName.getText().toString();
						String id = mStudentAddId.getText().toString();
						String code = mStudentAddCode.getText().toString();
						String email = mStudentAddEmail.getText().toString();
						String mobile = mStudentAddMobilePhone.getText().toString();
						String age = mStudentAddAge.getText().toString();
						String classs = mStudentAddClass.getText().toString();
						String add = mStudentAddAddress.getText().toString();
						if(name.equals("") || id.equals("") || code .equals("") || email.equals("") || mobile.equals("") || age.equals("") || classs.equals("") || add.equals("")) {
							mRobotActivity.showToastFromUIThread("Opp! some fields empty :)");
						} else {
							String gender = "male";
							if(mStudentAddCheckboxMale.isChecked()) gender = "male"; else gender = "female";
							mRobotActivity.showProgressDialog("Creating...");
							String queryString = "CREATE(n{app:\"bottono\", label:\"student\",name:" + "\"" +
									name + "\"" +", uId:" + "\"" + id + "\"" +
									", gender:" + "\"" + gender + "\"" +
									", studentCode:" + "\"" + code + "\"" +
									", email:" + "\"" + email + "\"" +
									", mobilePhone:" + "\"" + mobile + "\"" +
									", age:" + "\"" + age + "\"" +
									", classs:" + "\"" + classs + "\"" +
									", address:" + "\"" + add + "\"" +
									", cuoiKi:\"0\", giuaKi:\"0\", chuyenCan:\"0\", tongKet:\"0\"}) RETURN n";
							mRobotActivity.queryNeo4j(queryString);
							ArrayList<CheckinModel> checkinList = new ArrayList<CheckinModel>();
							checkinList.add(new CheckinModel(SystemUtil.getDate(), false));
							final StudentModel newStudent = new StudentModel(mStudentAddName
									.getText().toString(), gender, mStudentAddId
									.getText().toString(), mStudentAddCode.getText()
									.toString(), mStudentAddEmail.getText().toString(),
									mStudentAddMobilePhone.getText().toString(), "0",
									"0", "0", "0", mStudentAddAge.getText().toString(),
									mStudentAddClass.getText().toString(),
									mStudentAddAddress.getText().toString(),
									checkinList);
							mRobotActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Saved.studentList.add(newStudent);
									AssistantStudentFragment.initStudentListView();
									AssistantExamFragment.initStudentListView();
									AssistantRollupFragment.initStudentListView();
								}
							});
						}
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
}
