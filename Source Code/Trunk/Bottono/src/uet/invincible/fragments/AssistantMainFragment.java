package uet.invincible.fragments;

import uet.invincible.R;
import uet.invincible.R.drawable;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.bases.BaseFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint({ "ResourceAsColor", "NewApi" })
public class AssistantMainFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_main, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fragment_assistant_student_layout:
			mFragmentManager.beginTransaction().replace(R.id.fragment_assistant_frame, AssistantStudentFragment.getInstance(mRobotActivity)).commit();
			changeTabState(true, false, false, false, false);
			break;
		case R.id.fragment_assistant_rollup_layout:
			mFragmentManager.beginTransaction().replace(R.id.fragment_assistant_frame, AssistantRollupFragment.getInstance(mRobotActivity)).commit();
			changeTabState(false, true, false, false, false);		
			break;
		case R.id.fragment_assistant_exam_layout:
			mFragmentManager.beginTransaction().replace(R.id.fragment_assistant_frame, AssistantExamFragment.getInstance(mRobotActivity)).commit();
			changeTabState(false, false, true, false, false);		
			break;
		case R.id.fragment_assistant_chatbot_layout:
			mFragmentManager.beginTransaction().replace(R.id.fragment_assistant_frame, AssistantChatbotFragment.getInstance(mRobotActivity)).commit();
			changeTabState(false, false, false, true, false);		
			break;
		case R.id.fragment_assistant_account_layout:
			mFragmentManager.beginTransaction().replace(R.id.fragment_assistant_frame, AssistantAccountFragment.getInstance(mRobotActivity)).commit();
			changeTabState(false, false, false, false, true);		
			break;
		default:
			break;
		}
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
	
	@Override
	protected void initModels() {
		mFragmentManager = getFragmentManager();
		mFragmentManager.beginTransaction().add(R.id.fragment_assistant_frame, AssistantStudentFragment.getInstance(mRobotActivity)).commit();
	}

	@Override
	protected void initViews(View view) {
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
		// TODO Auto-generated method stub
	}
	
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
	public static FragmentManager mFragmentManager;
	
	public static final long serialVersionUID = 150049551510246302L;
	public static AssistantMainFragment mInstance;
	public static RobotMainActivity mRobotActivity;
	public static AssistantMainFragment getInstance(RobotMainActivity mRobotActivity2) {
		if (mInstance == null) {
			mInstance = new AssistantMainFragment();
		}
		mRobotActivity = mRobotActivity2;
		return mInstance;
	}

}
