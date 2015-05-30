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

public class ExamDetailDialog extends DialogFragment {
	public Context mContext;
	public LayoutInflater mInflater;
	protected Dialog mDialog;
	protected EditText mQtt;
	protected EditText mDur;
	public TextView mEntry;
	public StudentModel mStudent = null;
	public RobotMainActivity mRobotActivity;
	
	private ExamDetailDialog(Context context, RobotMainActivity mRobotActivity, StudentModel mStudent) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mRobotActivity = mRobotActivity;
		this.mStudent = mStudent;
	}

	public static ExamDetailDialog newInstance(Context context,RobotMainActivity mRobotActivity , StudentModel mStudent) {
		ExamDetailDialog alertDialog = new ExamDetailDialog(context, mRobotActivity, mStudent);
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
		View view = mInflater.inflate(R.layout.dialog_exam_detail, null);
		initViews(view);
		mDialog.setContentView(view);
		return mDialog;
	}
	
	public void initViews(View view) {
		mQtt = (EditText) view.findViewById(R.id.dialog_exam_detail_qtt);
		mDur = (EditText) view.findViewById(R.id.dialog_exam_detail_duration);
		mEntry = (TextView) view.findViewById(R.id.dialog_exam_detail_start);
		mEntry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!mQtt.getText().toString().equals("") && !mDur.getText().toString().equals("")) {
					if(Integer.parseInt(mQtt.getText().toString()) <= Saved.questionList.size()) {
						mDialog.dismiss();
						mRobotActivity.showExamDialog("", mRobotActivity, mStudent, Integer.parseInt(mQtt.getText().toString()), Integer.parseInt(mDur.getText().toString()));
					} else {
						mQtt.setText("");
						mRobotActivity.showToastFromUIThread("Quantity cannot greater than the numbers of question : " + Saved.questionList.size());
					}
				}
			}
		});
	}
	
}
