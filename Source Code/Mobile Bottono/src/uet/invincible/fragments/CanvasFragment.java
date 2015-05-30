package uet.invincible.fragments;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.bases.BaseFragment;
import uet.invincible.listeners.OptionsDialogListener;
import uet.invincible.listeners.TwoChosenDialogListener;
import uet.invincible.mobile.R;
import uet.invincible.mobile.R.drawable;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
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
		switch (view.getId()) {
			case R.id.fragment_assistant_student_layout:
				mPager.setCurrentItem(0);
				changeTabState(true, false, false, false, false);
				break;
			case R.id.fragment_assistant_rollup_layout:
				mPager.setCurrentItem(1);
				changeTabState(false, true, false, false, false);		
				break;
			case R.id.fragment_assistant_exam_layout:
				mPager.setCurrentItem(2);
				changeTabState(false, false, true, false, false);		
				break;
			case R.id.fragment_assistant_chatbot_layout:
				mPager.setCurrentItem(3);
				changeTabState(false, false, false, true, false);		
				break;
			case R.id.fragment_assistant_account_layout:
				mPager.setCurrentItem(4);
				changeTabState(false, false, false, false, true);		
				break;
			case R.id.activity_main_options :
				mRobotActivity.showOptionsDialog(new OptionsDialogListener() {
					@Override
					public void onExitClick(Dialog dialog) {
						System.exit(0);
					}
				});
			case R.id.activity_main_developers_exit:
				mRobotActivity.showTwoChosenDialog("", "All data will be saved.", "OK", "Cancel", new TwoChosenDialogListener() {
					@Override
					public void onRightClick(Dialog dialog) {
						dialog.dismiss();
					}
					@Override
					public void onLeftClick(Dialog dialog) {
						dialog.dismiss();
						System.exit(0);
					}
				});
				break;
			case R.id.activity_main_conversation_voice:
				mRobotActivity.showStudentCodeDialog(mRobotActivity);
				break;
			default:
				break;
		}
	}
	
	public static final int NUM_PAGES = 5;
	public static ViewPager mPager;
	public PagerAdapter mPagerAdapter;
	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }
		@Override
        public BaseFragment getItem(int position) {
			if(position == 0) return AssistantStudentFragment.getInstance(mRobotActivity);
			else if(position == 1) return AssistantRollupFragment.getInstance(mRobotActivity);
			else if(position == 2) return AssistantExamFragment.getInstance(mRobotActivity);
			else if(position == 3) return AssistantChatbotFragment.getInstance(mRobotActivity);
			else if(position == 4) return DevelopersFragment.getInstance(mRobotActivity);
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
					mTitle.setText("Students");
					changeTabState(true, false, false, false, false);
				} else 
				if(mPager.getCurrentItem() == 1) {
					mTitle.setText("Roll up");
					changeTabState(false, true, false, false, false);		
				} else
				if(mPager.getCurrentItem() == 2) {
					mTitle.setText("Examinations");
					changeTabState(false, false, true, false, false);		
				} else
				if(mPager.getCurrentItem() == 3) {
					mTitle.setText("Assistant");
					changeTabState(false, false, false, true, false);		
				} else
				if(mPager.getCurrentItem() == 4) {
					mTitle.setText("Developers");
					changeTabState(false, false, false, false, true);
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
	protected void initViews(View view) {
		mOptionButton = (LinearLayout) view.findViewById(R.id.activity_main_options);
		mTitle = (TextView) view.findViewById(R.id.activity_main_title);
		mOptionButton.setOnClickListener(this);
		mPager = (ViewPager) view.findViewById(R.id.acti_main_frame);
		mVoice = (LinearLayout) view.findViewById(R.id.activity_main_conversation_voice);
		mVoice.setOnClickListener(this);
		mExit = (LinearLayout) view.findViewById(R.id.activity_main_developers_exit);
		mExit.setOnClickListener(this);
		mStudentLayout = (LinearLayout) view.findViewById(R.id.fragment_assistant_student_layout);
		mStudentLayout.setOnClickListener(this);
		mStudentLayoutIcon = (ImageView) view.findViewById(R.id.fragment_assistant_student_image);
		mStudentLayoutText = (TextView) view.findViewById(R.id.fragment_assistant_student_text);
		mRollupLayout = (LinearLayout) view.findViewById(R.id.fragment_assistant_rollup_layout);
		mRollupLayout.setOnClickListener(this);
		mRollupLayoutIcon = (ImageView) view.findViewById(R.id.fragment_assistant_rollup_image);
		mRollupLayoutText = (TextView) view.findViewById(R.id.fragment_assistant_rollup_text);
		mExamLayout = (LinearLayout) view.findViewById(R.id.fragment_assistant_exam_layout);
		mExamLayout.setOnClickListener(this);
		mExamLayoutIcon = (ImageView) view.findViewById(R.id.fragment_assistant_exam_image);
		mExamLayoutText = (TextView) view.findViewById(R.id.fragment_assistant_exam_text);
		mChatbotLayout = (LinearLayout) view.findViewById(R.id.fragment_assistant_chatbot_layout);
		mChatbotLayout.setOnClickListener(this);
		mChatbotLayoutIcon = (ImageView) view.findViewById(R.id.fragment_assistant_chatbot_image);
		mChatbotLayoutText = (TextView) view.findViewById(R.id.fragment_assistant_chatbot_text);
		mAccountLayout = (LinearLayout) view.findViewById(R.id.fragment_assistant_account_layout);
		mAccountLayout.setOnClickListener(this);
		mAccountLayoutIcon = (ImageView) view.findViewById(R.id.fragment_assistant_account_image);
		mAccountLayoutText = (TextView) view.findViewById(R.id.fragment_assistant_account_text);
	}
	@Override
	protected void initAnimations() {
		
	}
	
	public static final long serialVersionUID = 6036846677812555352L;
	public LinearLayout mOptionButton;
	public TextView mTitle;
	
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
	
	public void changeTabState(boolean studentState, boolean rollupState, boolean examState, boolean chatbotState, boolean accountState) {
		int tab_index = 0;
		if(studentState) tab_index = 0; else
		if(rollupState) tab_index = 1; else
		if(examState) tab_index = 2; else
		if(chatbotState) tab_index = 3; else
		if(accountState) tab_index = 4; 	
		if(tab_index != TAB_INDEX) {
			switch (tab_index) {
			case 0:
				mStudentLayoutIcon.setBackgroundResource(drawable.assistant_student_checked);
				mStudentLayoutText.setTextColor(getResources().getColor(R.color.iconchecked));
				break;
			case 1:
				mRollupLayoutIcon.setBackgroundResource(drawable.assistant_rollup_checked);
				mRollupLayoutText.setTextColor(getResources().getColor(R.color.iconchecked));
				break;
			case 2:
				mExamLayoutIcon.setBackgroundResource(drawable.assistant_exam_checked);
				mExamLayoutText.setTextColor(getResources().getColor(R.color.iconchecked));
				break;
			case 3:
				mChatbotLayoutIcon.setBackgroundResource(drawable.assistant_chatbot_checked);
				mChatbotLayoutText.setTextColor(getResources().getColor(R.color.iconchecked));
				break;
			case 4:
				mAccountLayoutIcon.setBackgroundResource(drawable.assistant_account_checked);
				mAccountLayoutText.setTextColor(getResources().getColor(R.color.iconchecked));
				break;
			default:
				break;
			}
			switch (TAB_INDEX) {
			case 0:
				mStudentLayoutIcon.setBackgroundResource(R.drawable.assistant_student_unchecked);
				mStudentLayoutText.setTextColor(getResources().getColor(R.color.iconunchecked));
				break;
			case 1:
				mRollupLayoutIcon.setBackgroundResource(R.drawable.assistant_rollup_unchecked);
				mRollupLayoutText.setTextColor(getResources().getColor(R.color.iconunchecked));
				break;
			case 2:
				mExamLayoutIcon.setBackgroundResource(R.drawable.assistant_exam_unchecked);
				mExamLayoutText.setTextColor(getResources().getColor(R.color.iconunchecked));
				break;
			case 3:
				mChatbotLayoutIcon.setBackgroundResource(R.drawable.assistant_chatbot_unchecked);
				mChatbotLayoutText.setTextColor(getResources().getColor(R.color.iconunchecked));
				break;
			case 4:
				mAccountLayoutIcon.setBackgroundResource(R.drawable.assistant_account_unchecked);
				mAccountLayoutText.setTextColor(getResources().getColor(R.color.iconunchecked));
				break;
			default:
				break;
			}
			TAB_INDEX = tab_index;
		}
	}
	
	public LinearLayout mExit;
	public LinearLayout mVoice;
	public static int TAB_INDEX = 0;
	public LinearLayout mStudentLayout;
	public LinearLayout mRollupLayout;
	public LinearLayout mExamLayout;
	public LinearLayout mChatbotLayout;
	public LinearLayout mAccountLayout;
	public ImageView mStudentLayoutIcon;
	public ImageView mRollupLayoutIcon;
	public ImageView mExamLayoutIcon;
	public ImageView mChatbotLayoutIcon;
	public ImageView mAccountLayoutIcon;
	public TextView mStudentLayoutText;
	public TextView mRollupLayoutText;
	public TextView mExamLayoutText;
	public TextView mChatbotLayoutText;
	public TextView mAccountLayoutText;
}
