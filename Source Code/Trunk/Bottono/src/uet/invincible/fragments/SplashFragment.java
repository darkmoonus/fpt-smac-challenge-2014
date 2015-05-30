package uet.invincible.fragments;

import java.io.File;
import java.util.List;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.bases.BaseFragment;
import uet.invincible.customize.MyGifView;
import uet.invincible.savedata.Saved;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fpt.api.skybiometry.FaceApiClient;
import com.fpt.api.skybiometry.FaceApiClientListener;
import com.fpt.api.skybiometry.model.Face;
import com.fpt.api.skybiometry.model.Guess;
import com.fpt.api.skybiometry.model.Namespace;
import com.fpt.api.skybiometry.model.Photo;
import com.fpt.api.skybiometry.model.RemovedTag;
import com.fpt.api.skybiometry.model.SavedTag;
import com.fpt.api.skypebiometry.response.TrainResponse;
import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.sensors.RobotSonar.Monitor;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.robot.vision.RobotCamera;
import com.fpt.robot.vision.RobotCameraPreview;
import com.fpt.robot.vision.RobotCameraPreviewView;
import com.fpt.robot.vision.RobotFaceDetection;
import com.fpt.robot.vision.RobotFaceDetection.FaceDetectedInfo;

public class SplashFragment extends BaseFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_splash, container, false);
		initViews(view);
		initModels();
		initAnimations();
		return view;
	}
	
	public void onGifClick(View v) {
		MyGifView gif = (MyGifView) v;
		gif.setPaused(!gif.isPaused());
	}
	
	private Monitor monitor;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.splash_start_button:
//			if(startState) {
//				if(mRobotActivity.getRobot() == null) {
//					mRobotActivity.scan();
//				} else {
//					startState = false;
//					mBtStart.setBackgroundResource(R.drawable.floating_button_white_exit);
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							mRobotActivity.standUp();
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							mRobotActivity.runGesture("Give");
//						}
//					}).start();
//					
//					mAvatarDemo.setVisibility(View.GONE);
//					mCameraPreview.setVisibility(View.VISIBLE);
//					startCameraPreview();
//					robotCamera = RobotCamera.getCamera(mRobotActivity.getRobot(),RobotCamera.CAMERA_TOP);
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							if (mRobotActivity.getRobot() == null) {
//								mRobotActivity.scan();
//					 		} else {
//								try {
//									if(monitor == null) {
//										monitor = new Monitor(mRobotActivity.getRobot(),new Listener() {
//											@Override
//											public void onSonarRightNothingDetected() {
//											}
//											@Override
//											public void onSonarRightDetected(float distance) {
//											}
//											@Override
//											public void onSonarLeftNothingDetected() {
//											}
//											@Override
//											public void onSonarLeftDetected(float distance) {
//											}
//										});
//									}
//									monitor.start();
//								} catch (RobotException e) {
//									e.printStackTrace();
//								}
//								
//								boolean isSpeaking = false;
//								while(true) {
//									float leftValue = mRobotActivity.getLeftSonarValue();
//									float rightValue = mRobotActivity.getRightSonarValue();
//									if( !isSpeaking && (leftValue <= 1 || rightValue <= 1)) {
//										isSpeaking = true;
//										mRobotActivity.speak("Hãy đứng gần lại 1 chút để mình kiểm tra nhé .", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
//									}
//									if((leftValue >= 0.1 && leftValue <= 0.5) && rightValue >= 0.1 && rightValue <= 0.5) {
//										try {
//											monitor.stop();
//										} catch (RobotException e) {
//											mRobotActivity.showToastFromUIThread("Moniter stop error: " + e.getMessage().toString());
//											e.printStackTrace();
//										}
//										if (robotCamera == null) {
//											robotCamera = RobotCamera.getCamera(mRobotActivity.getRobot(),RobotCamera.CAMERA_TOP);
//										} else {
//											detect = false;
//											startFaceDetection();
//										}
//										break;
//									}
//									try {
//										Thread.sleep(1000);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//								}
//							}
//						}
//					}).start();
//				}
//			} else {
//				System.exit(0);
//			}
//			
			new Thread(new Runnable() {
				@Override
				public void run() {
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mRobotActivity.initStudentList();
							mRobotActivity.loadExamData();
							mRobotActivity.loadRollUpDailyData();
							gif.setVisibility(View.VISIBLE);
						}
					});
				}
			}).start();
			
			break;
		case R.id.splash_trick:
//			mRobotActivity.speakRandomBeforeLoadStudentData("Vũ Việt Anh", "giảng viên");
			new Thread(new Runnable() {
				@Override
				public void run() {
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
//							mRobotActivity.initStudentList();
//							mRobotActivity.loadExamData();
//							mRobotActivity.loadRollUpDailyData();
							gif.setVisibility(View.VISIBLE);
							mRobotActivity.BEGIN();
						}
					});
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				mRobotActivity.loadAccountData();
			}
		}).start();
		if(mRobotActivity.getRobot() == null) {
			mRobotActivity.scan();
		} else {
//			monitor = new Monitor(mRobotActivity.getRobot(),new Listener() {
//				@Override
//				public void onSonarRightNothingDetected() {
//				}
//				@Override
//				public void onSonarRightDetected(float distance) {
//					mRobotActivity.showToast("Sonar right: " + distance + "");
//				}
//				@Override
//				public void onSonarLeftNothingDetected() {
//				}
//				@Override
//				public void onSonarLeftDetected(float distance) {
//					mRobotActivity.showToast("Sonar left: " + distance + "");
//				}
//			});
		}
		
		gif.setMovieResource(R.drawable.loadding_metro_white);
		faceApi = new FaceApiClient(mRobotActivity.apiKey, mRobotActivity.apiSecret);
		faceApi.setListener(new FaceApiClientListener() {
			@Override
			public void onTrained(TrainResponse arg) {
				mRobotActivity.showProgressDialog(arg.toString());
			}
			@Override
			public void onTagSaved(List<SavedTag> savedtags) {
			}
			@Override
			public void onRemoveTags(List<RemovedTag> removedTags) {
				
			}@Override
			public void onRecognized(Photo photo) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						mRobotActivity.runGesture("Give");
					}
				}).start();
				String text = "";
				int confidence = 0;
				String uid = "";
				int numberFace = photo.getFaceCount();
				text += "Number of face: " + numberFace + "\n";
				List<Face> listFaces = photo.getFaces();
				for (Face face : listFaces) {
					text += "FACE\n";
					text += "Confidence: " + face.getFaceConfidence() + "\n";
					text += "Gender: " + face.getGender() + "\n";
					text += "Smile: " + face.isSmiling() + "\n";
					text += "Glass: " + face.isWearingGlasses() + "\n";
					text += "Mood: " + face.getMood() + "\n";
					List<Guess> guesses = face.getGuesses();
					text += "GUESS\n";
					for (Guess g : guesses) {
						text += "uid: " + g.first + "\n";
						text += "confidence: " + g.second + "\n";
						if (g.second.intValue() >= confidence) {
							uid = g.first;
							confidence = g.second.intValue();
						}
					}
				}
				if (confidence > 30) {
					String[] s = uid.split("@");
					boolean recognize = false;
					for(int i=0; i<Saved.accountList.size(); i++) {
						if(s[0].equals(Saved.accountList.get(i).uId)) {
							mRobotActivity.showToastFromUIThread(s[0]);
							Saved.account = Saved.accountList.get(i);
							String name = Saved.account.name;
							recognize = true;
							mRobotActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mEmail.setText(Saved.account.email);
									mMobile.setText(Saved.account.mobilePhone);
									mName.setText(Saved.account.name);
									mLevel.setText(Saved.account.name);
								}
							});
							greet(name);
							break;
						}
					}
					if(recognize == false) {
						mRobotActivity.speak("Rất tiếc . Bạn không có quyền truy cập . Bạn có thể liên hệ với ban quản lý để biết thêm chi tiết .", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
					}
				}
				else {
					String name = "Xin lỗi, tôi không nhận ra bạn. Vui lòng thử lại!";
				}
			}
			public void greet(String name) {
				String level = null;
				if(Saved.account.level.equals("teacher")) level = "giảng viên";
				else
				if(Saved.account.level.equals("training")) level = "phòng đào tạo";
				mRobotActivity.speakRandomBeforeLoadStudentData(Saved.account.name, level);
				mRobotActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mRobotActivity.initStudentList();
						mRobotActivity.loadExamData();
						mRobotActivity.loadRollUpDailyData();
						mAvatarDemo.setVisibility(View.GONE);
						mCameraPreview.setVisibility(View.VISIBLE);
						gif.setVisibility(View.VISIBLE);
					}
				});
			}
			
			@Override
			public void onGetNameSpace(final List<Namespace> nameSpaces) {
			}
			@Override
			public void onError(Exception ex) {
				mRobotActivity.showToastFromUIThread( "Unknow error");
			}
			@Override
			public void onDetected(Photo photo) {
			}
		});
	}

	@Override
	protected void initViews(View view) {
		gif = (MyGifView) view.findViewById(R.id.splash_logo);
		mBtStart = (TextView) view.findViewById(R.id.splash_start_button);
		mBtStart.setOnClickListener(this);
		mAvatarDemo = (TextView) view.findViewById(R.id.fragment_splash_avatar_demo);
		mAvatarDemo.setOnClickListener(this);
		mCameraPreview = (RobotCameraPreviewView) view.findViewById(R.id.fragment_splash_camara_preview);
		mTestMode = (TextView) view.findViewById(R.id.splash_trick);
		mTestMode.setOnClickListener(this);
		mName = (TextView) view.findViewById(R.id.fragment_splash_txt_name);
		mLevel = (TextView) view.findViewById(R.id.fragment_splash_txt_level);
		mMobile = (TextView) view.findViewById(R.id.fragment_splash_txt_mobile);
		mEmail = (TextView) view.findViewById(R.id.fragment_splash_txt_email);
	}

	@Override
	protected void initAnimations() {
		// TODO Auto-generated method stub
		
	}
	
	public RobotFaceDetection.Monitor faceMonitor;
	public RobotCameraPreviewView mCameraPreview;
	public RobotCameraPreview mRobotCameraPreview;
	public RobotCamera robotCamera;
	public FaceApiClient faceApi;
	public boolean detect = false;
	public void recognize(final String namespace) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String fileName = robotCamera.takePicture( RobotCamera.PICTURE_RESOLUTION_VGA, RobotCamera.PICTURE_FORMAT_JPG);
					stopCameraPreview();
					mRobotActivity.showProgressDialog("Recognizing...");
					faceApi.recognize(new File(fileName), namespace);
				} catch (RobotException e) {
					mRobotActivity.removePreviousDialog();
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void detect() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String fileName = robotCamera.takePicture(RobotCamera.PICTURE_RESOLUTION_VGA, RobotCamera.PICTURE_FORMAT_JPG);
					stopCameraPreview();
					mRobotActivity.showProgressDialog( "Detecting ...");
					faceApi.detect(new File(fileName));
				} catch (RobotException e) {
					mRobotActivity.removePreviousDialog();
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void startFaceDetection() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (mRobotActivity.getRobot() == null) {
					mRobotActivity.scan();
				} else {
					try {
						if (faceMonitor == null) {
							mRobotActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									faceMonitor = new RobotFaceDetection.Monitor(mRobotActivity.getRobot(), new RobotFaceDetection.Listener() {
										@Override
										public void onFaceDetected(FaceDetectedInfo info) {

										}
										@Override
										public void onFaceCropDetected() {
											if (detect) {
												detect();
											} else {
												String nameSpace = "invincible";
												mRobotActivity.showToastFromUIThread("Start recognize...");
												recognize(nameSpace);
											}
										}
									});
								}
							});
						}
						// check if face detection is running or not
						if (RobotFaceDetection.isDetecting(mRobotActivity.getRobot())) {
							// mRobotActivity.showToast("Face detection is running...");
							return;
						}
						mRobotActivity.showProgressDialog( "Start face detection...");
						faceMonitor.start();
						RobotFaceDetection.startDetection(mRobotActivity.getRobot());
						RobotAudioPlayer.beep(mRobotActivity.getRobot());
						mRobotActivity.removePreviousDialog();
					} catch (RobotException e) {
						mRobotActivity.removePreviousDialog();
						// mRobotActivity.showToast("Start Face detection failed: ");
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	public void startCameraPreview() {
		if (mRobotActivity.getRobot() == null) {
			mRobotActivity.scan();
		} else {
			if (mRobotCameraPreview == null) {
				mRobotCameraPreview = RobotCamera.getCameraPreview(mRobotActivity.getRobot(), RobotCamera.CAMERA_TOP);
			}
			mRobotCameraPreview.setPreviewDisplay(mCameraPreview);
			mRobotCameraPreview.setQuality(mRobotCameraPreview.QUALITY_MEDIUM);
			mRobotCameraPreview.setSpeed(mRobotCameraPreview.SPEED_MEDIUM);
			mRobotActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					boolean result = false;
					try {
						result = mRobotCameraPreview.startPreview();
					} catch (final RobotException e) {
						e.printStackTrace();
						 mRobotActivity.showToastFromUIThread("Start camera preview failed: " + e.getMessage().toString());
						return;
					}
					if (result) {
						 mRobotActivity.showToastFromUIThread("Camera preview started!");
					} else {
						 mRobotActivity.showToastFromUIThread("Start camera preview failed!");
					}
				}
			});
		}
	}
	public void stopCameraPreview() {
		if (mRobotCameraPreview == null) {
			return;
		}
		mRobotActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				try {
					result = mRobotCameraPreview.stopPreview();
				} catch (final RobotException e) {
					 mRobotActivity.showToastFromUIThread("Stop camera preview failed: " + e.getMessage().toString());
					return;
				}
				if (result) {
					 mRobotActivity.showToastFromUIThread("Camera preview stopped!");
				} else {
					 mRobotActivity.showToastFromUIThread("Stop camera preview failed!");
				}
			}
		});
	}
	
	private TextView mBtStart;
	private TextView mAvatarDemo;
	private int mAvatarStatus = 0;
	private TextView mTestMode;
	private TextView mName;
	private TextView mLevel;
	private TextView mMobile;
	private TextView mEmail;
	private boolean startState = true;
	private MyGifView gif;
	private TextView mLoginLayout;
	
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
