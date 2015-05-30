package uet.invincible.bases;

import java.io.Serializable;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.configure.AppConfigure;
import uet.invincible.dialogs.AddStudentDialog;
import uet.invincible.dialogs.ExamDetailDialog;
import uet.invincible.dialogs.ExamDialog;
import uet.invincible.dialogs.OptionsDialog;
import uet.invincible.dialogs.ProgressDialog;
import uet.invincible.dialogs.RequestDialog;
import uet.invincible.dialogs.StudentCodeDialog;
import uet.invincible.dialogs.TwoChosenDialog;
import uet.invincible.listeners.ExamDialogListioner;
import uet.invincible.listeners.OptionsDialogListener;
import uet.invincible.listeners.ProgressDialogListenter;
import uet.invincible.listeners.TwoChosenDialogListener;
import uet.invincible.models.StudentModel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener, Serializable, OnTouchListener {
	
	private static final long serialVersionUID = -6132406776837910717L;
	private static String TAG = "UET_INVINCIBLE";

	@Override
	public  void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	public void switchContent(int contentId, Fragment fragment, String tag) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(contentId, fragment);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}
	public void switchContentWithAnimation(int contentId, Fragment fragment, int arg0, int arg1, String tag) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(arg0, arg1);
		transaction.replace(contentId, fragment);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}
	public void switchContentWithAnimation(int contentId, Fragment fragment, int arg0, int arg1, int arg2, int arg3, String tag) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(arg0, arg1, arg2, arg3);
		transaction.replace(contentId, fragment);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}
	public void switchContent(int contentId, Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(contentId, fragment);
		transaction.commitAllowingStateLoss();
	}
	public void switchContentWithAnimation(int contentId, Fragment fragment, int arg0, int arg1) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(arg0, arg1);
		transaction.replace(contentId, fragment);
		transaction.commitAllowingStateLoss();
	}
	public void switchContentWithAnimation(int contentId, Fragment fragment, int arg0, int arg1, int arg2, int arg3) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(arg0, arg1, arg2, arg3);
		transaction.replace(contentId, fragment);
		transaction.commitAllowingStateLoss();
	}
	
	public android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
	public void removePreviousDialog() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		Fragment prev = mFragmentManager.findFragmentByTag(TAG);
		if (prev != null) ft.remove(prev);
		ft.commit();
	}
	public void showTwoChosenDialog(String title, String content, String leftButton, String rightButton, TwoChosenDialogListener listener) {
		if(!AppConfigure.SHOW_TOAST) return;
		removePreviousDialog();
		TwoChosenDialog mYesNoDialog = null;
		mYesNoDialog = TwoChosenDialog.newInstance(BaseActivity.this, content, leftButton, rightButton, listener);
		mYesNoDialog.show(getSupportFragmentManager(), TAG);
	}
	public void showExamDialog(String title, RobotMainActivity mRobotActivity, StudentModel mStudent, int MAX, int MAX_TIMER) {
		if(!AppConfigure.SHOW_TOAST) return;
		removePreviousDialog();
		ExamDialog mYesNoDialog = null;
		mYesNoDialog = ExamDialog.newInstance(BaseActivity.this, mRobotActivity, mStudent, MAX, MAX_TIMER);
		mYesNoDialog.setCancelable(false);
		mYesNoDialog.show(getSupportFragmentManager(), TAG);
	}
	public void showRequestDialog(String title, RobotMainActivity mRobotActivity, StudentModel mStudent) {
		if(!AppConfigure.SHOW_TOAST) return;
		removePreviousDialog();
		RequestDialog mYesNoDialog = null;
		mYesNoDialog = RequestDialog.newInstance(BaseActivity.this, mRobotActivity, mStudent);
		mYesNoDialog.show(getSupportFragmentManager(), TAG);
	}
	public void showStudentCodeDialog(RobotMainActivity mRobotActivity) {
		if(!AppConfigure.SHOW_TOAST) return;
		removePreviousDialog();
		StudentCodeDialog mYesNoDialog = null;
		mYesNoDialog = StudentCodeDialog.newInstance(BaseActivity.this, mRobotActivity);
		mYesNoDialog.show(getSupportFragmentManager(), TAG);
	}
	public void showExamDetailDialog(RobotMainActivity mRobotActivity, StudentModel mStudent) {
		if(!AppConfigure.SHOW_TOAST) return;
		removePreviousDialog();
		ExamDetailDialog mYesNoDialog = null;
		mYesNoDialog = ExamDetailDialog.newInstance(BaseActivity.this, mRobotActivity, mStudent);
		mYesNoDialog.show(getSupportFragmentManager(), TAG);
	}
	public void showAddStudentDiaalog(RobotMainActivity mRobotActivity) {
		if(!AppConfigure.SHOW_TOAST) return;
		removePreviousDialog();
		AddStudentDialog mYesNoDialog = null;
		mYesNoDialog = AddStudentDialog.newInstance(BaseActivity.this, mRobotActivity);
		mYesNoDialog.show(getSupportFragmentManager(), TAG);
	}
	public void showProgressDialog(String content) {
		if(!AppConfigure.SHOW_TOAST) return;
		removePreviousDialog();
		ProgressDialog mProgressDialog  = ProgressDialog.newInstance(BaseActivity.this, content, new ProgressDialogListenter() {
			
		});
		mProgressDialog.show(getSupportFragmentManager(), TAG);
	}
	public void showProgressDialogFromUIThread(final String content) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showProgressDialog(content);
			}
		});
	}
	public void showOptionsDialog(OptionsDialogListener listener) {
		if(!AppConfigure.SHOW_TOAST) return;
		removePreviousDialog();
		OptionsDialog mDialog  = OptionsDialog.newInstance(BaseActivity.this, listener);
		mDialog.show(getSupportFragmentManager(), TAG);
	}
	
	public void showToast(String message) {
		if(!AppConfigure.SHOW_TOAST) return;
		Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
	}
	public void showToastFromUIThread(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showToast(message);
			}
		});
	}
	
}
