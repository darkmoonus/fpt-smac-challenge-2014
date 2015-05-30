package uet.invincible.assistant.fragments;

import uet.invincible.assistant.R;
import uet.invincible.assistant.activities.RobotMainActivity;
import uet.invincible.assistant.bases.BaseFragment;
import uet.invincible.assistant.listeners.OptionsDialogListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CanvasFragment extends BaseFragment implements OnClickListener{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_canvas, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.activity_main_developers_icon :
				mPager.setCurrentItem(1);
				break;
			case R.id.activity_main_conversation_icon :
				mPager.setCurrentItem(0);
				break;
			case R.id.activity_main_options :
				mRobotActivity.showOptionsDialog(new OptionsDialogListener() {
					@Override
					public void onExitClick(Dialog dialog) {
						System.exit(0);
					}
				});
				break;
		}
		
	}
	
	public static final int NUM_PAGES = 2;
	public static ViewPager mPager;
	public PagerAdapter mPagerAdapter;
	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }
		@Override
        public BaseFragment getItem(int position) {
			if (position == 0) return AssistantChatbotFragment.getInstance(mRobotActivity);
			else if(position == 1) return DevelopersFragment.getInstance(mRobotActivity);
			return null;
        }
        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

	protected void initModels() {
		mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		mPager.setOffscreenPageLimit(NUM_PAGES);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if(mPager.getCurrentItem() == 0) {
					mDevelopersTriangle.setBackgroundResource(R.color.trans);
					mConversationTriangle.setBackgroundResource(R.drawable.triangle);
					mTitle.setText("Assistant");
				} else 
				if(mPager.getCurrentItem() == 1) {
					mDevelopersTriangle.setBackgroundResource((R.drawable.triangle));
					mConversationTriangle.setBackgroundResource(R.color.trans);
					mTitle.setText("Developers");
				} 
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	protected void initViews(View v) {
		mOptionButton = (LinearLayout) v.findViewById(R.id.activity_main_options);
		mDevelopersTriangle = (TextView) v.findViewById(R.id.activity_main_developers_triangle);
		mConversationTriangle = (TextView) v.findViewById(R.id.activity_main_conversation_triangle);
		mDevelopers = (LinearLayout) v.findViewById(R.id.activity_main_developers_icon);
		mDevelopers.setOnClickListener(this);
		mConversation = (LinearLayout) v.findViewById(R.id.activity_main_conversation_icon);
		mConversation.setOnClickListener(this);
		mTitle = (TextView) v.findViewById(R.id.activity_main_title);
		mOptionButton.setOnClickListener(this);
		mPager = (ViewPager) v.findViewById(R.id.acti_main_frame);
	}

	@Override
	protected void initAnimations() {
		
	}
	
	public static final long serialVersionUID = 6036846677812555352L;
	public LinearLayout mOptionButton;
	public TextView mTitle;
	public LinearLayout mConversation;
	public LinearLayout mDevelopers;
	public TextView mConversationTriangle;
	public TextView mDevelopersTriangle;
	
	public static RobotMainActivity mRobotActivity;
	public static CanvasFragment mInstance;
	public static CanvasFragment getInstance(RobotMainActivity activity) {
		if (mInstance == null) {
			mInstance = new CanvasFragment();
		}
		mRobotActivity = activity;
		return mInstance;
	}
	public static CanvasFragment getInstance() {
		if (mInstance == null) {
			mInstance = new CanvasFragment();
		}
		return mInstance;
	}
}
