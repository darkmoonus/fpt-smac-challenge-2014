package uet.invincible.fragments;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.bases.BaseFragment;
import uet.invincible.customize.MyAnimations;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.DONUT)
@SuppressLint({ "ResourceAsColor", "NewApi" })
public class DevelopersFragment extends BaseFragment {

	private static final long serialVersionUID = 1706274487430793790L;

	private static DevelopersFragment mInstance;

	private LinearLayout full_tuan;
	private LinearLayout full_giap;
	private LinearLayout full_vanh;
	private LinearLayout full_ha;
	
	private LinearLayout full_tuan_after_sms;
	private LinearLayout full_giap_after_sms;
	private LinearLayout full_vanh_after_sms;
	private LinearLayout full_ha_after_sms;
	
	private TextView full_tuan_after_sms_send;
	private TextView full_giap_after_sms_send;
	private TextView full_vanh_after_sms_send;
	private TextView full_ha_after_sms_send;
	
	private EditText full_tuan_after_sms_edit;
	private EditText full_giap_after_sms_edit;
	private EditText full_vanh_after_sms_edit;
	private EditText full_ha_after_sms_edit;
	
	private boolean full_tuan_is_slided;
	private boolean full_giap_is_slided;
	private boolean full_vanh_is_slided;
	private boolean full_ha_is_slided;
	
	private TextView avatar_tuan;
	private TextView avatar_giap;
	private TextView avatar_vanh;
	private TextView avatar_ha;
	
	
	private LinearLayout sms_tuan;
	private LinearLayout sms_giap;
	private LinearLayout sms_vanh;
	private LinearLayout sms_ha;
	private LinearLayout about_us;
	private LinearLayout applications;
	private TextView about_us_button;
	private TextView app_button;

	private static RobotMainActivity mRobotActivity;
	public static DevelopersFragment getInstance(RobotMainActivity activity) {
		if (mInstance == null) {
			mInstance = new DevelopersFragment();
		}
		mRobotActivity = activity;
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_developer, container,false);
		initViews(view);
		initAnimations();
		initModels();
		return view;
	}

	@Override
	protected void initModels() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initViews(View view) {
		full_tuan = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_all_tuan);
		full_giap = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_all_giap);
		full_vanh = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_all_vanh);
		full_ha = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_all_ha);

		full_tuan_after_sms = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_all_tuan_after_sms);
		full_giap_after_sms = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_all_giap_after_sms);
		full_vanh_after_sms = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_all_vanh_after_sms);
		full_ha_after_sms = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_all_ha_after_sms);
		
		full_tuan_after_sms_edit = (EditText) view.findViewById(R.id.fragment_developers_layout_all_tuan_after_sms_edit_text);
		full_giap_after_sms_edit = (EditText) view.findViewById(R.id.fragment_developers_layout_all_giap_after_sms_edit_text);
		full_vanh_after_sms_edit = (EditText) view.findViewById(R.id.fragment_developers_layout_all_vanh_after_sms_edit_text);
		full_ha_after_sms_edit = (EditText) view.findViewById(R.id.fragment_developers_layout_all_ha_after_sms_edit_text);
		
		full_tuan_after_sms_send = (TextView) view.findViewById(R.id.fragment_developers_layout_all_tuan_after_sms_send);
		full_giap_after_sms_send = (TextView) view.findViewById(R.id.fragment_developers_layout_all_giap_after_sms_send);
		full_vanh_after_sms_send = (TextView) view.findViewById(R.id.fragment_developers_layout_all_vanh_after_sms_send);
		full_ha_after_sms_send = (TextView) view.findViewById(R.id.fragment_developers_layout_all_ha_after_sms_send);
		full_tuan_after_sms_send.setOnClickListener(this);
		full_vanh_after_sms_send.setOnClickListener(this);
		full_giap_after_sms_send.setOnClickListener(this);
		full_ha_after_sms_send.setOnClickListener(this);
		
		avatar_tuan = (TextView) view.findViewById(R.id.fragment_developer_avatar_tuan);
		avatar_tuan.setOnClickListener(this);
		avatar_giap = (TextView) view.findViewById(R.id.fragment_developer_avatar_giap);
		avatar_giap.setOnClickListener(this);
		avatar_vanh = (TextView) view.findViewById(R.id.fragment_developer_avatar_vanh);
		avatar_vanh.setOnClickListener(this);
		avatar_ha = (TextView) view.findViewById(R.id.fragment_developer_avatar_ha);
		avatar_ha.setOnClickListener(this);
		
		sms_tuan = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_sms_tuan);
		sms_tuan.setOnClickListener(this);
		sms_giap = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_sms_giap);
		sms_giap.setOnClickListener(this);
		sms_vanh = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_sms_vanh);
		sms_vanh.setOnClickListener(this);
		sms_ha = (LinearLayout) view.findViewById(R.id.fragment_developers_layout_sms_ha);
		sms_ha.setOnClickListener(this);
		
		about_us = (LinearLayout) view.findViewById(R.id.fragment_developer_about_us);
		applications = (LinearLayout) view.findViewById(R.id.fragment_developer_application);
		about_us_button = (TextView) view.findViewById(R.id.fragment_developer_about_us_tab);
		app_button = (TextView) view.findViewById(R.id.fragment_developer_application_tab);
		
		about_us_button.setOnClickListener(this);
		app_button.setOnClickListener(this);
		
		full_tuan_is_slided = false;
		full_giap_is_slided = false;
		full_vanh_is_slided = false;
		full_ha_is_slided = false;
	}

	@Override
	protected void initAnimations() {
//		full_tuan.setAnimation(MyAnimations.fromLeft(500, 500));
//		full_giap.setAnimation(MyAnimations.fromLeft(500, 1000));
//		full_vanh.setAnimation(MyAnimations.fromLeft(500, 850));
//		full_ha.setAnimation(MyAnimations.fromLeft(500, 1150));
//		
//		about_us.setAnimation(MyAnimations.fromDown(500, 1000));
//		applications.setAnimation(MyAnimations.fromDown(500, 1004));
		
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View view) {
		super.onClick(view);
		Display display = mRobotActivity.getWindowManager().getDefaultDisplay(); 
		int pixelMove = (int) convertDpToPixel(280, mContext);
		int pixelSlideAfterSMS = (int) convertDpToPixel(70, mContext);
		switch (view.getId()) {
		case R.id.fragment_developers_layout_all_tuan_after_sms_send:
			String k = full_tuan_after_sms_edit.getText().toString();
			if(!k.equals("")) {
				sendSMS("01697740001", k);
				full_tuan_after_sms_edit.setText("");
				mRobotActivity.showToast("Send SMS to Trương Quốc Tuấn successful");
			} else {
				mRobotActivity.showToast("Message field empty !");
			}
			break;
		case R.id.fragment_developers_layout_all_vanh_after_sms_send:
			String k1 = full_vanh_after_sms_edit.getText().toString();
				if(!k1.equals("")) {
				sendSMS("01684015252", k1);
				full_vanh_after_sms_edit.setText("");
				mRobotActivity.showToast("Send SMS to Vũ Việt Anh successful");
			} else {
				mRobotActivity.showToast("Message field empty !");
			}
			break;
		case R.id.fragment_developers_layout_all_giap_after_sms_send:
			String k2 = full_giap_after_sms_edit.getText().toString();
			if(!k2.equals("")) {
				sendSMS("01692740975", k2);
				full_giap_after_sms_edit.setText("");
				mRobotActivity.showToast("Send SMS to Nguyễn Văn Giáp successful");
			} else {
				mRobotActivity.showToast("Message field empty !");
			}
			break;
		case R.id.fragment_developers_layout_all_ha_after_sms_send:
			String k3 = full_ha_after_sms_edit.getText().toString();
			if(!k3.equals("")) {
				sendSMS("01672286349", k3);
				full_ha_after_sms_edit.setText("");
				mRobotActivity.showToast("Send SMS to Hoàng Hà successful");
			} else {
				mRobotActivity.showToast("Message field empty !");
			}
			break;
		case R.id.fragment_developer_avatar_tuan:
			if(full_tuan_is_slided) {
				full_tuan_is_slided = false;
				full_tuan.startAnimation(MyAnimations.fromLeft(pixelMove, 500));
				full_tuan_after_sms.setVisibility(View.GONE);
				full_tuan.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.fragment_developer_avatar_giap:
			if(full_giap_is_slided) {
				full_giap_is_slided = false;
				full_giap.startAnimation(MyAnimations.fromLeft(pixelMove, 500));
				full_giap_after_sms.setVisibility(View.GONE);
				full_giap.setVisibility(View.VISIBLE);
				sms_giap.setEnabled(true);
			}
			break;
		case R.id.fragment_developer_avatar_vanh:
			if(full_vanh_is_slided) {
				full_vanh_is_slided = false;
				full_vanh.startAnimation(MyAnimations.fromLeft(pixelMove, 500));
				full_vanh_after_sms.setVisibility(View.GONE);
				full_vanh.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.fragment_developer_avatar_ha:
			if(full_ha_is_slided) {
				full_ha_is_slided = false;
				full_ha.startAnimation(MyAnimations.fromLeft(pixelMove, 500));
				full_ha_after_sms.setVisibility(View.GONE);
				full_ha.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.fragment_developers_layout_sms_tuan:
			if(!full_tuan_is_slided) {
				full_tuan_is_slided = true;
				full_tuan.startAnimation(MyAnimations.slideLeft(pixelMove, 500));
				full_tuan_after_sms.setVisibility(View.VISIBLE);
				full_tuan_after_sms.startAnimation(MyAnimations.fromRight(pixelMove, pixelSlideAfterSMS, 500));
				full_tuan_after_sms.setVisibility(View.VISIBLE);
				full_tuan.setVisibility(View.GONE);
			}  else {
				String kk = full_tuan_after_sms_edit.getText().toString();
				if(!kk.equals("")) {
					sendSMS("01697740001", kk);
					full_tuan_after_sms_edit.setText("");
					mRobotActivity.showToast("Send SMS to TrÆ°Æ¡ng Quá»‘c Tuáº¥n successful");
				} else {
					mRobotActivity.showToast("Message field empty !");
				}
			}
			break;
		case R.id.fragment_developers_layout_sms_giap:
			if(!full_giap_is_slided) {
				full_giap_is_slided = true;
				full_giap.startAnimation(MyAnimations.slideLeft(pixelMove, 500));
				full_giap_after_sms.setVisibility(View.VISIBLE);
				full_giap_after_sms.startAnimation(MyAnimations.fromRight(pixelMove, pixelSlideAfterSMS, 500));
				full_giap_after_sms.setVisibility(View.VISIBLE);
				full_giap.setVisibility(View.GONE);
			} else {
				String kk2 = full_giap_after_sms_edit.getText().toString();
				if(!kk2.equals("")) {
					sendSMS("01692740975", kk2);
					full_giap_after_sms_edit.setText("");
					mRobotActivity.showToast("Send SMS to Nguyá»…n VÄƒn GiÃ¡p successful");
				} else {
					mRobotActivity.showToast("Message field empty !");
				}
			}
			break;
		case R.id.fragment_developers_layout_sms_vanh:
			if(!full_vanh_is_slided) {
				full_vanh_is_slided = true;
				full_vanh.startAnimation(MyAnimations.slideLeft(pixelMove, 500));
				full_vanh_after_sms.setVisibility(View.VISIBLE);
				full_vanh_after_sms.startAnimation(MyAnimations.fromRight(pixelMove, pixelSlideAfterSMS, 500));
				full_vanh_after_sms.setVisibility(View.VISIBLE);
				full_vanh.setVisibility(View.GONE);
			} else {
				String kk1 = full_vanh_after_sms_edit.getText().toString();
				if(!kk1.equals("")) {
					sendSMS("01684015252", kk1);
					full_vanh_after_sms_edit.setText("");
					mRobotActivity.showToast("Send SMS to VÅ© Viá»‡t Anh successful");
				} else {
					mRobotActivity.showToast("Message field empty !");
				}
			}
			break;
		case R.id.fragment_developers_layout_sms_ha:
			if(!full_ha_is_slided) {
				full_ha_is_slided = true;
				full_ha.startAnimation(MyAnimations.slideLeft(pixelMove, 500));
				full_ha_after_sms.setVisibility(View.VISIBLE);
				full_ha_after_sms.startAnimation(MyAnimations.fromRight(pixelMove, pixelSlideAfterSMS, 500));
				full_ha_after_sms.setVisibility(View.VISIBLE);
				full_ha.setVisibility(View.GONE);
			}  else {
				String kk3 = full_ha_after_sms_edit.getText().toString();
				if(!kk3.equals("")) {
					sendSMS("01672286349", kk3);
					full_ha_after_sms_edit.setText("");
					mRobotActivity.showToast("Send SMS to HoÃ ng HÃ  successful");
				} else {
					mRobotActivity.showToast("Message field empty !");
				}
			}
			break;
		case R.id.fragment_developer_about_us_tab:
			applications.setVisibility(View.GONE);
			about_us.setVisibility(View.VISIBLE);
			applications.startAnimation(MyAnimations.slideRight(1000, 700));
			about_us.startAnimation(MyAnimations.fromLeft(1000, 700));
			about_us_button.setBackgroundResource(R.drawable.bound_gray);
			about_us_button.setTextColor(Color.WHITE);
			app_button.setBackgroundResource(R.drawable.bound_white);
			app_button.setTextColor(Color.GRAY);
			about_us_button.setEnabled(false);
			app_button.setEnabled(true);
			break;
		case R.id.fragment_developer_application_tab:
			applications.setVisibility(View.VISIBLE);
			about_us.setVisibility(View.GONE);
			applications.startAnimation(MyAnimations.fromRight(1000, 700));
			about_us.startAnimation(MyAnimations.slideLeft(1000, 700));
			about_us_button.setBackgroundResource(R.drawable.bound_white);
			about_us_button.setTextColor(Color.GRAY);
			app_button.setTextColor(Color.WHITE);
			app_button.setBackgroundResource(R.drawable.bound_gray);
			about_us_button.setEnabled(true);
			app_button.setEnabled(false);
			break;
		default:
			break;
		}
	}
	
	private void sendSMS(String phoneNumber, String message) {
	       SmsManager sms = SmsManager.getDefault();
	       sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
	
	@SuppressLint("NewApi")
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
	
}
