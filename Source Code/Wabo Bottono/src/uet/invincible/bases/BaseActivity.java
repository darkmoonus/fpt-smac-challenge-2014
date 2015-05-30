package uet.invincible.bases;

import java.io.Serializable;

import uet.invincible.customize.ProgressDialog;
import uet.invincible.listeners.ProgressDialogListenter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fpt.robot.app.RobotFragmentActivity;

public abstract class BaseActivity extends RobotFragmentActivity implements OnClickListener, Serializable, OnTouchListener {
	
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
	public void showProgressDialog(String content) {
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
	public void showToast(String message) {
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
