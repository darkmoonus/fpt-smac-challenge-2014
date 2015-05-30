package uet.invincible.assistant.activities;

import uet.invincible.assistant.R;
import uet.invincible.assistant.bases.BaseActivity;
import uet.invincible.assistant.fragments.SplashFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.tts.RobotTextToSpeech;

public class RobotMainActivity extends BaseActivity implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_robot);
		FragmentManager mFragmentManager = getSupportFragmentManager();
		mFragmentManager.beginTransaction().add(R.id.robot_canvas, SplashFragment.getInstance(RobotMainActivity.this)).commit();
	}
	@SuppressLint("NewApi")
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		View view = activity.getCurrentFocus();
		if (view != null) {
			IBinder binder = view.getWindowToken();
			if (binder != null) {
				inputMethodManager.hideSoftInputFromWindow(binder, 0);
			}
		}

	}
	public void speak(String message, String language) {
		try {
			RobotTextToSpeech.say(getRobot(), message, language);
		} catch (RobotException e) {
			showToastFromUIThread("Speak failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	public void runGesture(final String name) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					showToastFromUIThread( "Gesture: " + name);
					RobotGesture.runGesture(getRobot(), name);
				} catch (RobotException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	/* ---------------------------------------------------------------- */
	private static final long serialVersionUID = -6313302259718885854L;
	public static final String TAG = "UET-INVINCIBLE";
	public final static String apiKey = "6b6603c0e2b34e679e8b9d09a253922e";
	public final static String apiSecret = "92b7dd7243c645ef895ff7f4a2084d0a";
	public String accessToken = "47a461cb-3519-4e46-82a4-4cb91644b4dd";
	public String queryURL = "https://fti.pagekite.me/graph4mrc/excute";
}
