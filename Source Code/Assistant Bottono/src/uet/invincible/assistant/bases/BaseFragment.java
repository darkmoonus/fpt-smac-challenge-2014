package uet.invincible.assistant.bases;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseFragment extends Fragment implements OnClickListener, Serializable {

	public static final long serialVersionUID = 7080889824192321168L;
	public static FragmentManager mFragmentManager;
	protected Context mContext;
	public String TAG = "UET-INVINCIBLE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getFragmentManager();
		mContext = getActivity();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	

	@Override
	public void onClick(View view) {
		
	}
	

//	public void switchFragment(Fragment fragment) {
//		if (getActivity() == null) return;
//		((BaseActivity) getActivity()).switchContent(R.id.content_frame, fragment);
//	}
//	public void switchFragmentWithAnimation(Fragment fragment, int arg0, int arg1) {
//		if (getActivity() == null) return;
//		((BaseActivity) getActivity()).switchContentWithAnimation(R.id.content_frame, fragment, arg0, arg1);
//	}
//	public void switchFragmentWithAnimation(Fragment fragment, int arg0, int arg1, int arg2, int arg3) {
//		if (getActivity() == null) return;
//		((BaseActivity) getActivity()).switchContentWithAnimation(R.id.content_frame, fragment, arg0, arg1, arg2, arg3);
//	}
//	public void switchFragment(Fragment fragment, String tag) {
//		if (getActivity() == null) return;
//		((BaseActivity) getActivity()).switchContent(R.id.content_frame, fragment, tag);
//	}
//	public void switchFragmentWithAnimation(Fragment fragment, int arg0, int arg1, String tag) {
//		if (getActivity() == null) return;
//		((BaseActivity) getActivity()).switchContentWithAnimation(R.id.content_frame, fragment, arg0, arg1, tag);
//	}
//	public void switchFragmentWithAnimation(Fragment fragment, int arg0, int arg1, int arg2, int arg3, String tag) {
//		if (getActivity() == null) return;
//		((BaseActivity) getActivity()).switchContentWithAnimation(R.id.content_frame, fragment, arg0, arg1, arg2, arg3, tag);
//	}
	
	public void finishFragment() {
		try {
			mFragmentManager.popBackStack();
		} catch (Exception e) {
		//	Log.e(e.getMessage());
		}
	}
	
	protected abstract void initModels();
	protected abstract void initViews(View view);
	protected abstract void initAnimations();

}
