package uet.invincible.assistant.fragments;

import uet.invincible.assistant.R;
import uet.invincible.assistant.activities.RobotMainActivity;
import uet.invincible.assistant.bases.BaseFragment;
import uet.invincible.assistant.customize.MyGifView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SplashFragment extends BaseFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_splash, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.splash_start_button:
			mBtStart.setEnabled(false);
			mRobotActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gif.setVisibility(View.VISIBLE);
				}
			});
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						mFragmentManager.beginTransaction().replace(R.id.robot_canvas, CanvasFragment.getInstance(mRobotActivity)).commit();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.fragment_splash_avatar_demo:
			switch (mAvatarStatus) {
			case 0:
				mAvatarStatus = 1;
				mAvatarDemo.setBackgroundResource(R.drawable.splash_avatar_demo_bound_giap);
				break;
			case 1:
				mAvatarStatus = 2;
				mAvatarDemo.setBackgroundResource(R.drawable.splash_avatar_demo_bound_ha);
				break;
			case 2:
				mAvatarStatus = 3;
				mAvatarDemo.setBackgroundResource(R.drawable.splash_avatar_demo_bound_tuan);
				break;
			case 3:
				mAvatarStatus = 0;
				mAvatarDemo.setBackgroundResource(R.drawable.splash_avatar_demo_bound_va);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	protected void initModels() {
		gif.setMovieResource(R.drawable.loadding_metro_white);
	}

	@Override
	protected void initViews(View view) {
		gif = (MyGifView) view.findViewById(R.id.splash_logo);
		mBtStart = (TextView) view.findViewById(R.id.splash_start_button);
		mBtStart.setOnClickListener(this);
		mAvatarDemo = (TextView) view.findViewById(R.id.fragment_splash_avatar_demo);
		mAvatarDemo.setOnClickListener(this);
	}

	@Override
	protected void initAnimations() {
		
	}
	
	private TextView mBtStart;
	private TextView mAvatarDemo;
	private int mAvatarStatus = 0;
	private MyGifView gif;
	
	private static final long serialVersionUID = -3335182509181200753L;
	private static SplashFragment mInstance;
	private static RobotMainActivity mRobotActivity;
	public static SplashFragment getInstance(RobotMainActivity robotActivity) {
		if (mInstance == null) {
			mInstance = new SplashFragment();
			mRobotActivity = robotActivity;
		}
		return mInstance;
	}
}
