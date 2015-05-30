package uet.invincible.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.bases.BaseFragment;

public class AssistantAccountFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_account, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}

	@Override
	protected void initModels() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initViews(View view) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initAnimations() {
		// TODO Auto-generated method stub
	}
	
	private static final long serialVersionUID = -1379377360733503854L;
	private static AssistantAccountFragment mInstance;
	public static RobotMainActivity mRobotActivity;
	public static AssistantAccountFragment getInstance(RobotMainActivity mRobotActivity2) {
		if (mInstance == null) {
			mInstance = new AssistantAccountFragment();
		}
		mRobotActivity = mRobotActivity2;
		return mInstance;
	}
}
