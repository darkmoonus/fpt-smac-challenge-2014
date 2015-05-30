package uet.invincible.dialogs;

import java.util.ArrayList;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.customize.MyAnimations;
import uet.invincible.customize.MyGifView;
import uet.invincible.fragments.AssistantRollupFragment;
import uet.invincible.mobile.R;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import uet.invincible.utilities.Mail;
import uet.invincible.utilities.SystemUtil;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class RequestDialog extends DialogFragment implements OnClickListener {
	public Context mContext;
	public LayoutInflater mInflater;
	protected Dialog mDialog;
	
	public RobotMainActivity mRobotActivity;

	public TextView mExamName;
	public TextView mStudentName;
	public TextView mStudentCode;
	public TextView mStudentClass;
	public TextView mVoice;
	public TextView mAvatar;
	public TextView mRequestContent;
	public StudentModel mStudent;
	public int mStudentID = -1;
	public EditText mAnswer;
	
	private RequestDialog(Context context, RobotMainActivity mRobotActivity, StudentModel mStudent) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mRobotActivity = mRobotActivity;
		this.mStudent = mStudent;
		for(int i=0; i<Saved.studentList.size(); i++) {
			if(Saved.studentList.get(i).studentCode.equals(mStudent.studentCode)) {
				mStudentID = i;
				break;
			}
		}
	}

	public static RequestDialog newInstance(Context context, RobotMainActivity mRobotActivity, StudentModel mStudent) {
		RequestDialog alertDialog = new RequestDialog(context, mRobotActivity, mStudent);
		Bundle bundle = new Bundle();
		alertDialog.setArguments(bundle);
		return alertDialog;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
		}
	}
	
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDialog = new Dialog(mContext);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(false);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = mInflater.inflate(R.layout.dialog_request, null);
		initViews(view);
		setViews();
		initAnimation();
		mDialog.setContentView(view);
		return mDialog;
	}
	
	public void initViews(View view) {
		mExamName = (TextView) view.findViewById(R.id.exam_dialog_exam_name);
		mStudentName = (TextView) view.findViewById(R.id.exam_dialog_student_name);
		mStudentCode = (TextView) view.findViewById(R.id.exam_dialog_student_code);
		mStudentClass = (TextView) view.findViewById(R.id.exam_dialog_student_class);
		mRequestContent = (TextView) view.findViewById(R.id.exam_dialog_question_content);
		mVoice = (TextView) view.findViewById(R.id.exam_dialog_voice);
		mVoice.setOnClickListener(this);
		mAvatar = (TextView) view.findViewById(R.id.exam_dialog_avatar);
		mAnswer = (EditText) view.findViewById(R.id.exam_dialog_answer);
	}
	
	public void setViews() {
		mStudentName.setText(mStudent.name);
		mStudentCode.setText(mStudent.studentCode);
		mStudentClass.setText(mStudent.classs);
		int uId = -1;
		for(int i=0; i<Saved.studentList.size(); i++){
			if(Saved.studentList.get(i).studentCode.equals(mStudent.studentCode)) {
				uId = i;
				break;
			}
		}
		switch (uId % 20) {
			case 0: mAvatar.setBackgroundResource(R.drawable.avatar_0); break;
			case 1: mAvatar.setBackgroundResource(R.drawable.avatar_1); break;
			case 2: mAvatar.setBackgroundResource(R.drawable.avatar_2);break;
			case 3: mAvatar.setBackgroundResource(R.drawable.avatar_3); break;
			case 4: mAvatar.setBackgroundResource(R.drawable.avatar_4); break;
			case 5: mAvatar.setBackgroundResource(R.drawable.avatar_5); break;
			case 6: mAvatar.setBackgroundResource(R.drawable.avatar_6); break;
			case 7: mAvatar.setBackgroundResource(R.drawable.avatar_7); break;
			case 8: mAvatar.setBackgroundResource(R.drawable.avatar_8); break;
			case 9: mAvatar.setBackgroundResource(R.drawable.avatar_9); break;
			case 10: mAvatar.setBackgroundResource(R.drawable.avatar_10); break;
			case 11: mAvatar.setBackgroundResource(R.drawable.avatar_11); break;
			case 12: mAvatar.setBackgroundResource(R.drawable.avatar_12); break;
			case 13: mAvatar.setBackgroundResource(R.drawable.avatar_13); break;
			case 14: mAvatar.setBackgroundResource(R.drawable.avatar_14); break;
			case 15: mAvatar.setBackgroundResource(R.drawable.avatar_15); break;
			case 16: mAvatar.setBackgroundResource(R.drawable.avatar_16); break;
			case 17: mAvatar.setBackgroundResource(R.drawable.avatar_17); break;
			case 18: mAvatar.setBackgroundResource(R.drawable.avatar_18); break;
			case 19: mAvatar.setBackgroundResource(R.drawable.avatar_19); break;
			default: break;
		}
	}
	public void initAnimation() {
		mVoice.setAnimation(MyAnimations.fromUp(100, 1000));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exam_dialog_voice:
			startListener();
			break;
		}
	}
	
	protected static final int RESULT_SPEECH = 1;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case RESULT_SPEECH: {
				if (resultCode == -1 && null != data) {
					ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					mAnswer.append("You: " + text.get(0) + "\n");
					String ss = mRobotActivity.getActionFromQuery(text.get(0));
					switch (ss) {
					case "THI LỊCH SỬ":
						mAnswer.append("Bottono: Bắt đầu bài thi Lịch Sử\n");
						new Thread(new Runnable() {
							@Override
							public void run() {
								mRobotActivity.showExamDetailDialog(mRobotActivity, mStudent);
							}
						}).start();
						break;
					case "TRA ĐIỂM THI":
						double sum = Double.parseDouble(mStudent.chuyenCan) * 0.1
						+ Double.parseDouble(mStudent.giuaKi) * 0.2
						+ Double.parseDouble(mStudent.cuoiKi) * 0.3
						+ Double.parseDouble(mStudent.tongKet) * 0.4;
						String content = "Kết quả học tập của bạn như sau: \n + Điểm chuyên cần: " + mStudent.chuyenCan + "\n" +
								" + Điểm bài tập: " + mStudent.giuaKi + "\n" +
								" + Điểm giữa kì: " + mStudent.cuoiKi + "\n" +
								" + Điểm cuối kì: " + mStudent.tongKet + "\n" +
								" + Tổng kết: " + sum + "\n" +
								" - ĐÁNH GIÁ: ";
								if(sum < 6) content += "Học lực trung bình, cần phải cố gắng rất nhiều."; else
								if(sum < 8) content += "Học lực khá. Cần cố gắng hơn để đạt điểm cao."; else
								if(sum < 9) content += "Học lực giỏi. Cố gắng phát huy nhé"; else
								content += "Học lực xuất sắc, chúc mừng bạn !";
						mAnswer.append("Bottono: " + content + "\n");
						break;
					case "ĐIỂM DANH":
						final String toDay = SystemUtil.getDate();
						for(int i=0; i<Saved.studentList.get(mStudentID).checkinList.size(); i++) {
							if(Saved.studentList.get(mStudentID).checkinList.get(i).date.equals(toDay)) {
								if(Saved.studentList.get(mStudentID).checkinList.get(i).status == false) {
									mAnswer.append("Bottono: Thực hiện điểm danh cho sinh viên " + mStudent.name + "\n");
									final int ii = i;
									new Thread(new Runnable() {
										@Override
										public void run() {
											Saved.studentList.get(mStudentID).checkinList.get(ii).status = true;
											mRobotActivity.showProgressDialogFromUIThread("Waiting ...");
											final CypherResult result = CypherRequest.makeRequest(mRobotActivity.accessToken, 
													"match m, n, m-[r]->n where m.label='student' and m.uId=\'" 
													+ Saved.studentList.get(mStudentID).uId + "\' and n.label='rollup' and n.date=\'" 
													+ SystemUtil.getDate() + "\' set r.status = 'true'");
											if(result == null) {
												mRobotActivity.removePreviousDialog();
												mRobotActivity.showToastFromUIThread("Result null");
											} else {
												if(!result.status.equals("success")) {
													mRobotActivity.showToastFromUIThread("Request failed");
												} else {
													AssistantRollupFragment.initStudentListView();
													mRobotActivity.removePreviousDialog();
												}
											}
										}
									}).start();
								} else {
									mAnswer.append("Bottono: Hôm nay bạn đã điểm danh rồi.\n");
								}
							}
						}
						break;
					case "GỬI MAIL KẾT QUẢ":
						new Thread(new Runnable() {
							@Override
							public void run() {
								mAnswer.append("Bottono: Thực hiện gửi email thông báo kết quả cho sinh viên " + mStudent.name +"\n");
								mRobotActivity.showProgressDialog("Sending email...");
								double sumsum = Double.parseDouble(mStudent.chuyenCan) * 0.1
										+ Double.parseDouble(mStudent.giuaKi) * 0.2
										+ Double.parseDouble(mStudent.cuoiKi) * 0.3
										+ Double.parseDouble(mStudent.tongKet) * 0.4;
								String contentr = "Kết quả học tập của bạn như sau: \n + Điểm chuyên cần: " + mStudent.chuyenCan + "\n" +
								" + Điểm bài tập: " + mStudent.giuaKi + "\n" +
								" + Điểm giữa kì: " + mStudent.cuoiKi + "\n" +
								" + Điểm cuối kì: " + mStudent.tongKet + "\n" +
								" + Tổng kết: " + sumsum + "\n" +
								" - ĐÁNH GIÁ: ";
								if(sumsum < 6) contentr += "Học lực trung bình, cần phải cố gắng rất nhiều."; else
								if(sumsum < 8) contentr += "Học lực khá. Cần cố gắng hơn để đạt điểm cao."; else
								if(sumsum < 9) contentr += "Học lực giỏi. Cố gắng phát huy nhé"; else
								contentr += "Học lực xuất sắc, chúc mừng bạn !";
								
								String[] list = new String[1];
								list[0] = mStudent.email;
								Mail m = new Mail("bottonouet@gmail.com", "tu1denchin");
								m.setTo(list);
								m.setFrom("bottonouet@gmail.com");
								m.setSubject("Thông báo kết quả học tập");
								m.setBody(contentr);
								try {
									if(m.send()) {
										mRobotActivity.showToastFromUIThread("Send email to " + mStudent.name + " successful !");
									} else {
										mRobotActivity.showToastFromUIThread("Send email to " + mStudent.name + " failed !");
									}
								} catch(Exception e) {
									mRobotActivity.showToastFromUIThread("Exeption send email to " + mStudent.name + " failed !");
									mRobotActivity.showToastFromUIThread("Exeption: " + e.getMessage());
									e.printStackTrace();
								}
								mRobotActivity.removePreviousDialog();
							}
						}).start();
						break;
					case "THÔNG TIN CÁ NHÂN":
						String contentx = "Thông tin sinh viên " + mStudent.name + ": \n" 
								+ "Tên: " + mStudent.name + "\n"
								+ "Mã sinh viên: " + mStudent.studentCode + "\n"
								+ "Lớp: " + mStudent.classs + "\n"
								+ "Email: " + mStudent.email + "\n"
								+ "Số điện thoại: " + mStudent.mobilePhone + "\n"
								+ "Địa chỉ: " + mStudent.address + "\n";
						mAnswer.append("Bottono: " + contentx + "\n");
						break;
					default:
						mAnswer.append("Bottono: " + ss + "\n");
						break;
					}
				}
				break;
			}
		}
	}
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public void startListener() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		try {			
			startActivityForResult(intent, RESULT_SPEECH);
		} catch (ActivityNotFoundException a) {
			a.printStackTrace();
		}
	}
	public void onGifClick(View v) {
		MyGifView gif = (MyGifView) v;
		gif.setPaused(!gif.isPaused());
	}
}
