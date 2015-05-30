package uet.invincible.dialogs;

import java.util.ArrayList;
import java.util.Collections;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.customize.MyAnimations;
import uet.invincible.customize.MyGifView;
import uet.invincible.fragments.AssistantExamFragment;
import uet.invincible.fragments.AssistantStudentFragment;
import uet.invincible.mobile.R;
import uet.invincible.models.QuestionModel;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;

public class ExamDialog extends DialogFragment implements OnClickListener {
	public Context mContext;
	public LayoutInflater mInflater;
	protected Dialog mDialog;
	
	public RobotMainActivity mRobotActivity;
	public StudentModel mStudent;
	public int questionNumber = 0;
	public double score = 0;
	public int MAX;
	public int MAX_TIME;
	public int studentId = 0;
	
	public ArrayList<QuestionModel>questionList = Saved.questionList;

	public TextView mQtt;
	public TextView mTimer;
	public TextView mExamName;
	public TextView mStudentName;
	public TextView mStudentCode;
	public TextView mStudentClass;
	public TextView mSubmit;
	public TextView mVoice;
	public TextView mQuestion;
	public TextView mAvatar;
	public TextView mQuestionContent;
	public EditText mAnswer;
	public LinearLayout mQABlock;
	public TextView mLoadingGif;
	public TextView mResult;
	public LinearLayout mSubmitBlock;
	
	private ExamDialog(Context context, RobotMainActivity mRobotActivity, StudentModel mStudent, int MAX, int MAX_TIMER) {
		this.mContext = context;
		this.mStudent = mStudent;
		this.mInflater = LayoutInflater.from(context);
		this.mRobotActivity = mRobotActivity;
		this.MAX = MAX;
		this.MAX_TIME = MAX_TIMER;
	}

	public static ExamDialog newInstance(Context context, RobotMainActivity mRobotActivity, StudentModel mStudent, int MAX, int MAX_TIMER) {
		ExamDialog alertDialog = new ExamDialog(context, mRobotActivity, mStudent, MAX, MAX_TIMER);
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
		View view = mInflater.inflate(R.layout.dialog_exam, null);
		initViews(view);
		setViews();
		initAnimation();
		mDialog.setContentView(view);
		return mDialog;
	}
	
	public void initViews(View view) {
		mQtt = (TextView) view.findViewById(R.id.exam_dialog_quantity);
		mTimer = (TextView) view.findViewById(R.id.exam_dialog_timer);
		mExamName = (TextView) view.findViewById(R.id.exam_dialog_exam_name);
		mStudentName = (TextView) view.findViewById(R.id.exam_dialog_student_name);
		mStudentCode = (TextView) view.findViewById(R.id.exam_dialog_student_code);
		mStudentClass = (TextView) view.findViewById(R.id.exam_dialog_student_class);
		mQuestion = (TextView) view.findViewById(R.id.exam_dialog_question);
		mQuestionContent = (TextView) view.findViewById(R.id.exam_dialog_question_content);
		mSubmit = (TextView) view.findViewById(R.id.exam_dialog_submit);
		mSubmit.setOnClickListener(this);
		mVoice = (TextView) view.findViewById(R.id.exam_dialog_voice);
		mVoice.setOnClickListener(this);
		mAvatar = (TextView) view.findViewById(R.id.exam_dialog_avatar);
		mAnswer = (EditText) view.findViewById(R.id.exam_dialog_answer);
		mSubmitBlock = (LinearLayout) view.findViewById(R.id.dialog_exam_submit_block);
		mResult = (TextView) view.findViewById(R.id.dialog_exam_score);
		mQABlock = (LinearLayout) view.findViewById(R.id.dialog_exam_qa_block);
		mLoadingGif = (TextView) view.findViewById(R.id.dialog_exam_loading);
	}
	
	class Inte {
		public int x;
		public Inte(int x) {
			this.x = x;
		}
	}
	public void setViews() {
		mStudentName.setText(mStudent.name);
		mStudentCode.setText(mStudent.studentCode);
		mStudentClass.setText(mStudent.classs);
		
		Collections.shuffle(questionList);
		
		final Inte timer = new Inte(MAX_TIME);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(timer.x > 0) {
					try {
						mRobotActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								int hour = timer.x / 60;
								int min = timer.x % 60;
								String s = "";
								if(hour < 10) s += "0" + hour + ":"; else s += hour + ":";
								if(min < 10) s += "0" + min; else s += min + "";
								mTimer.setText(s);
							}
						});
						Thread.sleep(1000);
						timer.x--;
						if(timer.x == 0) {
							doWhenFinishExam();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		int hour = timer.x / 60;
		int min = timer.x % 60;
		String s = "";
		if(hour < 10) s += "0" + hour + ":"; else s += hour + ":";
		if(min < 10) s += "0" + min; else s += min + "";
		mTimer.setText(s);
		String qtt = "";
		int x = questionNumber + 1;
		if(x < 10) qtt += "0" + x + "/"; else qtt += questionNumber + "/";
		if(MAX < 10) qtt += "0" + MAX; else qtt += MAX;
		mQtt.setText(qtt);
		
		int x1 = questionNumber + 1;
		mQuestion.setText("Question " + x1 + ": ");
		mQuestionContent.setText(questionList.get(questionNumber).question);
		
		for(int i=0; i<Saved.studentList.size(); i++) {
			if(Saved.studentList.get(i).uId.equals(mStudent.uId)) {
				studentId = i;
				break;
			}
		}
		switch (studentId % 20) {
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
		mVoice.setAnimation(MyAnimations.fromLeft(100, 500));
		mSubmit.setAnimation(MyAnimations.fromRight(100, 500));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exam_dialog_voice:
			if(mSubmitFlag) startListener();
			break;
		case R.id.exam_dialog_submit:
			if(mSubmitFlag) {
				SystemUtil.hideSoftKeyboard(mRobotActivity);
				submitAnswer(mAnswer.getText().toString());
			}
		default:
			break;
		}
	}
	
	public void doWhenFinishExam() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mRobotActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mQABlock.setVisibility(View.GONE);
						mResult.setVisibility(View.VISIBLE);
						mLoadingGif.setVisibility(View.GONE);
						mResult.setText("Score: " + score);
						mTimer.setText("00:00");
						mSubmitBlock.setVisibility(View.GONE);
					}
				});
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mRobotActivity.showProgressDialog("Sending score ...");
				String cypher = "match m where m.label='student' and m.uId=\'" + mStudent.uId + "\' set m.tongKet = \'" + score + "\'";
				final CypherResult result = CypherRequest.makeRequest(mRobotActivity.accessToken, cypher);
				if(result == null) {
					mRobotActivity.showToastFromUIThread("Result null");
				} else {
					if(!result.status.equals("success")) mRobotActivity.showToastFromUIThread("Request failed");
					else mRobotActivity.showToastFromUIThread("Send score successful !");
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Saved.studentList.get(studentId).tongKet = score + "";
							AssistantExamFragment.initStudentListView();
							AssistantStudentFragment.initStudentListView();
						}
					});
				}
				mRobotActivity.removePreviousDialog();
				mDialog.dismiss();
			}
		}).start();
	}
	
	public boolean mSubmitFlag = true;
	public void submitAnswer(String answer) {
		if(answer.contains(questionList.get(questionNumber).answer)) score += 10 / MAX;
		questionNumber++;
		if(questionNumber >= MAX) {
			doWhenFinishExam();
		} else {
			mSubmitFlag = false;
			mAnswer.setText("");
			String qtt = "";
			int x = questionNumber + 1;
			if(x < 10) qtt += "0" + x + "/"; else qtt += questionNumber + "/";
			if(MAX < 10) qtt += "0" + MAX; else qtt += MAX;
			
			final String qttt = qtt;
			final int xx = x;
			mRobotActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mQABlock.setVisibility(View.GONE);
					mLoadingGif.setVisibility(View.VISIBLE);
				}
			});
			new Thread(new Runnable() {
				@Override
				public void run() {
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// Tinh toan cau hoi tiep theo
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							mQABlock.setVisibility(View.VISIBLE);
							mLoadingGif.setVisibility(View.GONE);
							mQtt.setText(qttt);
							mQuestion.setText("Question " + xx + ": ");
							mQuestionContent.setText(questionList.get(questionNumber).question);
						}
					});
					mSubmitFlag = true;
				}
			}).start();
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
					mAnswer.append(text.get(0));
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
