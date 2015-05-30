package uet.invincible.dialogs;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.mobile.R;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class StudentCodeDialog extends DialogFragment {
	public Context mContext;
	public LayoutInflater mInflater;
	protected Dialog mDialog;
	protected EditText mCode;
	public TextView mEntry;
	public RobotMainActivity mRobotActivity;
	
	private StudentCodeDialog(Context context, RobotMainActivity mRobotActivity) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mRobotActivity = mRobotActivity;
	}

	public static StudentCodeDialog newInstance(Context context,RobotMainActivity mRobotActivity ) {
		StudentCodeDialog alertDialog = new StudentCodeDialog(context, mRobotActivity);
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
		mDialog.setCancelable(false);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = mInflater.inflate(R.layout.dialog_student_code, null);
		initViews(view);
		mDialog.setContentView(view);
		return mDialog;
	}
	
	public StudentModel mStudent = null;
	public void initViews(View view) {
		mCode = (EditText) view.findViewById(R.id.dialog_student_code_code);
		mEntry = (TextView) view.findViewById(R.id.dialog_student_code_entry);
		mEntry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = mCode.getText().toString();
				for(int i=0; i<Saved.studentList.size(); i++) {
					if(Saved.studentList.get(i).studentCode.equals(s)) {
						mStudent = Saved.studentList.get(i);
						break;
					}
				}
				if(mStudent == null) {
					mRobotActivity.showToastFromUIThread("Student code not exist. Try again !");
					mCode.setText("");
				} else {
					mDialog.dismiss();
					mRobotActivity.showRequestDialog("", mRobotActivity, mStudent);
				}
			}
		});
	}
	
}
