package uet.invincible.fragments;

import java.util.ArrayList;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.adapters.StudentExamAdapter;
import uet.invincible.bases.BaseFragment;
import uet.invincible.libs.pulltorefresh.PullToRefreshListView;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fpt.robot.tts.RobotTextToSpeech;

public class AssistantExamFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_exam, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	public static void startExamination() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<Saved.studentList.size()-18; i++){
					mRobotActivity.speak("Xin mời " + Saved.studentList.get(i).name + " vào thi .", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					mRobotActivity.speak("Chào mừng thí sinh " + Saved.studentList.get(i).name + " đến với kỳ thi cuối kì môn Lịch Sử . Hãy ghi nhớ kĩ những điều sau : Mỗi câu hỏi và câu trả lời chỉ được đọc một lần , sau mỗi câu hỏi , sẽ có 1 tiếng bip , khi đó câu trả lời của bạn mới được ghi nhận . Mỗi câu hỏi sẽ có 10 giây để trả lời . 3 giây nữa phần thi của bạn sẽ bắt đầu . Chúc bạn may mắn .", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
					try {
						Thread.sleep(1000);
						mRobotActivity.speak("Bắt đầu", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					for(int j=1; j<=2; j++) {
						final int ran = j;
						mRobotActivity.removePreviousDialog();
						mRobotActivity.speak("Câu hỏi số " + j + " : " + Saved.questionList.get(ran).question , RobotTextToSpeech.ROBOT_TTS_LANG_VI);
						// ERROR ??
//						Thread x = new Thread(new Runnable() {
//							@Override
//							public void run() {
//								new SpeakToText(Languages.VIETNAMESE, new SpeakToTextListener() {
//									@Override
//									public void onWaiting() {
//										mRobotActivity.showProgressDialog( "Speak now ...");
//									}
//									@Override
//									public void onTimeout() {
//										mRobotActivity.removePreviousDialog();
//										mRobotActivity.speak("Mình chưa nhận được câu trả lời của bạn . Coi như bạn trả lời sai câu này nhé . Chúng ta sẽ đến với câu tiếp theo . Hãy cố gắng lên .", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
//									}
//									@Override
//									public void onStopped() {
//										mRobotActivity.removePreviousDialog();
//									}
//									@Override
//									public void onResult(final Result result) {
//										mRobotActivity.removePreviousDialog();
//										String s = result.result[0].alternative[0].transcript.toString();
//										mRobotActivity.showToastFromUIThread(s);
//										if(mRobotActivity.contain(s, Saved.questionList.get(ran).answer.toLowerCase())) {
//											mRobotActivity.speak("Chuẩn cơm mẹ nấu luôn !", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
//										} else {
//											mRobotActivity.speak("Rất tiếc , sai!", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
//										}
//									}
//									@Override
//									public void onRecording() {
//										mRobotActivity.removePreviousDialog();
//										mRobotActivity.showProgressDialog( "Recording...");
//									}
//									
//									@Override
//									public void onProcessing() {
//										mRobotActivity.removePreviousDialog();
//										mRobotActivity.showProgressDialog( "Processing...");
//									}
//									
//									@Override
//									public void onError(Exception ex) {
//										ex.printStackTrace();
//									}
//								}).recognize(2500, 15000);
//							}
//						});
//						try {
//							Thread.sleep(400);
//							RobotAudioPlayer.beep(mRobotActivity.getRobot());
//							x.start();
//							Thread.sleep(20000);
//							x.interrupt();
//						} catch (InterruptedException e1) {
//							e1.printStackTrace();
//						} catch (RobotException e) {
//							e.printStackTrace();
//						}
					}
					mRobotActivity.speak("Phần thi của bạn đã kết thúc . Hãy yên tâm , mình sẽ gửi kết quả thi qua email hoặc điện thoại cho bạn sau . Bây giờ bạn có thể ra về được rồi. Gặp lại bạn sau nhé .", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mRobotActivity.speak("Kiểm tra cuối kì kết thúc . Xin mời tất cả các bạn ra về. ", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
				try {
					Thread.sleep(2000);
					mRobotActivity.speak("Mình đã lưu lại toàn bộ kết quả thi và gửi vào email cho bạn rồi đó " + Saved.account.name + " . Mình nghỉ 1 chút rồi chúng ta lại tiếp tục công việc nhé .", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public ArrayList<StudentModel> mStudentList;
	public static StudentExamAdapter mStudentExamListAdapter;
	public static PullToRefreshListView mStudentExamListView;
	public static void initStudentListView() {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				ArrayList<CheckinModel> checkinList = new ArrayList<CheckinModel>();
//				for(int i=0; i<20; i++) {
//					StudentModel newStudent = new StudentModel("Việt Anh Vũ", "Male", "vav", "12020010", "vietanhuet@gmail.com", "01684015252", "10", "9.5", "9", "9.7", "20", "K57CLC", "Mã sinh viên: 12020010, K57CLC", checkinList);
//					Saved.studentList.add(newStudent);
//				}
				mStudentExamListAdapter = new StudentExamAdapter(mRobotActivity, mRobotActivity, 0, Saved.studentList);
				mStudentExamListView.setAdapter(mStudentExamListAdapter);
				mStudentExamListAdapter.setNotifyOnChange(true);
			}
		});
	}
	public void addStudentIntoListView(final StudentModel student) {
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStudentExamListAdapter.add(student);
				mStudentExamListAdapter.setNotifyOnChange(true);
			}
		});
	}
	@Override
	protected void initModels() {
		initStudentListView();
	}
	@Override
	protected void initViews(View view) {
		mStudentExamListView = (PullToRefreshListView) view.findViewById(R.id.fragment_student_exam_listview);
	}
	@Override
	protected void initAnimations() {
	}
	private static final long serialVersionUID = -1379377360733503854L;
	private static AssistantExamFragment mInstance;
	public static RobotMainActivity mRobotActivity;
	public static AssistantExamFragment getInstance(RobotMainActivity mRobotActivity2) {
		if (mInstance == null) {
			mInstance = new AssistantExamFragment();
		}
		mRobotActivity = mRobotActivity2;
		return mInstance;
	}
}
