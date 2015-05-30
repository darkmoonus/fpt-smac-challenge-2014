package uet.invincible.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.bases.BaseFragment;
import uet.invincible.libs.pulltorefresh.PullToRefreshListView;
import uet.invincible.utilities.SystemUtil;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.robot.vision.RobotCamera;
import com.fpt.robot.vision.RobotCameraPreview;
import com.fpt.robot.vision.RobotCameraPreviewView;
import com.fpt.robot.vision.RobotFaceDetection;
import com.fpt.robot.vision.RobotFaceDetection.FaceDetectedInfo;

@SuppressLint("NewApi")
public class ControlFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_control, container, false);
		initViews(view);
		initAnimations();
		initModels();
		return view;
	}
	@Override
	public void initModels() {
		faceApi = new FaceApiClient(mRobotActivity.apiKey, mRobotActivity.apiSecret);
		faceApi.setListener(new FaceApiClientListener() {
			@Override
			public void onTrained(TrainResponse arg) {
				mRobotActivity.showProgressDialog(arg.toString());
			}
			@Override
			public void onTagSaved(List<SavedTag> savedtags) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onRemoveTags(List<RemovedTag> removedTags) {
				
			}@Override
			public void onRecognized(Photo photo) {
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
				if (confidence > 20) {
					String name = "";
					if (uid.equals("tqt@invincible")) name = "Trương Quốc Tuấn";
					else if (uid.equals("hl@invincible")) name = "Hoàng Hà"; 
					else if (uid.equals("va@invincible")) name = "Vũ Việt Anh";
					else if (uid.equals("nvg@invincible")) name = "Nguyễn Văn Giáp";
					greet(name);
				}
				else {
					String name = "Xin lỗi, tôi không nhận ra bạn. Vui lòng thử lại!";
				}
				mRobotActivity.showProgressDialog(text);
			}
			public void greet(String name) {
				final String greeting = "Chào " + name;
				// mRobotActivity.showToast(greeting);
				final String goIntoClassroom = "Mời bạn về chỗ.";
				final String gesture1 = "Give";
				final String gesture2 = "Far";
				new Thread(new Runnable() {
					@Override
					public void run() {
						Robot robot = mRobotActivity.getRobot();
						if (robot != null) {
							String language = RobotTextToSpeech.ROBOT_TTS_LANG_VI;
							boolean result1 = false;
							boolean result2 = false;
							mRobotActivity.showProgressDialog( "Greeting ...");
							try {
								result1 = RobotTextToSpeech.say(robot, greeting, language);
								RobotGesture.runGesture(mRobotActivity.getRobot(),gesture1);
								result2 = RobotTextToSpeech.say(robot, goIntoClassroom, language);
								RobotGesture.runGesture(mRobotActivity.getRobot(),gesture2);
							} catch (final RobotException e) {
								e.printStackTrace();
								// mRobotActivity.showToast("greeting failed! " + e.getMessage());
								return;
							}
							if (!result1 || !result2) {
								// mRobotActivity.showToast("greeting failed!");
							}
						}
					}
				}).start();
			}
			
			@Override
			public void onGetNameSpace(final List<Namespace> nameSpaces) {
				mRobotActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						listNameSpaces.clear();
						for (Namespace n : nameSpaces) {
							listNameSpaces.add(n.getName());
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(mRobotActivity,R.layout.spinner_item,listNameSpaces);
						mCamSpinner.setAdapter(adapter);
						mRobotActivity.removePreviousDialog();
					}
				});
			}
			@Override
			public void onError(Exception ex) {
				mRobotActivity.removePreviousDialog();
				mRobotActivity.showProgressDialog( "Unknow error");
			}
			@Override
			public void onDetected(Photo photo) {
				String text = "";
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
				}
				mRobotActivity.showProgressDialog( text);
			}
		});
	}
	@Override
	public void initViews(final View view) {
		// FACE DETECTION VIEWS
		mFaceRecognitionRow = (LinearLayout) view.findViewById(R.id.controler_face_recognition_training_row);
		mFaceRecognitionRow.setOnClickListener(this);
		mFaceRecognitionContent = (LinearLayout) view.findViewById(R.id.controler_face_recognition_training_content);
		mCamRowArrow = (TextView) view.findViewById(R.id.controler_face_recognition_training_arrow);
		mCamPreviewCapture = (ImageView) view.findViewById(R.id.controler_face_recognition_camara_preview_capture);
		mCameraPreview = (RobotCameraPreviewView) view.findViewById(R.id.controler_face_recognition_camara_preview);
		mCamPlay = (TextView) view.findViewById(R.id.controler_face_recognition_camera_play);
		mCamPlay.setOnClickListener(this);
		mCamSpinner = (Spinner) view.findViewById(R.id.controler_face_recognition_camera_spinner);
		mCamUserId = (EditText) view.findViewById(R.id.controler_face_recognition_camera_userid);
		mCamTrain = (TextView) view.findViewById(R.id.controler_face_recognition_camera_train);
		mCamTrain.setOnClickListener(this);
		mCamDetect = (TextView) view.findViewById(R.id.controler_face_recognition_camera_detect);
		mCamDetect.setOnClickListener(this);
		mCamRecognize = (TextView) view.findViewById(R.id.controler_face_recognition_camera_recognize);
		mCamRecognize.setOnClickListener(this);
		mCamCapture = (TextView) view.findViewById(R.id.controler_face_recognition_camera_capture);
		mCamCapture.setOnClickListener(this);
		mCamTrainURL = (TextView) view.findViewById(R.id.controler_face_recognition_camera_train_URLtrain);
		mCamTrainURL.setOnClickListener(this);
		mCamTrainURLId = (EditText) view.findViewById(R.id.controler_face_recognition_camera_userid_URLtrain);
		mCamTrainURLLink = (EditText) view.findViewById(R.id.controler_face_recognition_camera_urllink_URLtrain);
		// DATABASE VIEWS
		mControlDatabaseRow = (LinearLayout) view.findViewById(R.id.controler_database_row);
		mControlDatabaseRow.setOnClickListener(this);
		mControlDatabaseContent = (LinearLayout) view.findViewById(R.id.controler_database_content);
		mControlDatabaseArrow = (TextView) view.findViewById(R.id.controler_database_arrow);
		mControlDatabaseQuery = (TextView) view.findViewById(R.id.controler_database_query_button);
		mControlDatabaseQuery.setOnClickListener(this);
		mControlDatabaseQueryText = (EditText) view.findViewById(R.id.controler_database_query_text);
		mControlDatabaseClear = (TextView) view.findViewById(R.id.controler_database_clear_button);
		mControlDatabaseClear.setOnClickListener(this);
		// BEHAVIOR
		mControlBehaviorRow = (LinearLayout) view.findViewById(R.id.controler_behavior_row);
		mControlBehaviorRow.setOnClickListener(this);
		mControlBehaviorContent = (LinearLayout) view.findViewById(R.id.controler_behavior_content);
		mControlBehaviorArrow = (TextView) view.findViewById(R.id.controler_behavior_arrow);
		mControlBehaviorListview = (PullToRefreshListView) view.findViewById(R.id.controler_behavior_content_listview);
		// GESTURE
		mControlGestureRow = (LinearLayout) view.findViewById(R.id.controler_gesture_row);
		mControlGestureRow.setOnClickListener(this);
		mControlGestureContent = (LinearLayout) view.findViewById(R.id.controler_gesture_content);
		mControlGestureArrow = (TextView) view.findViewById(R.id.controler_gesture_arrow);
		mControlGestureListview = (PullToRefreshListView) view.findViewById(R.id.controler_gesture_content_listview);
	}
	@Override
	public void initAnimations() {
		// TODO Auto-generated method stub
		
	}
	public void changeRowsStateVisible(boolean faceDetection, boolean database, boolean behavior, boolean gesture) {
		if(!faceDetection) {
			mFaceRecognitionContent.setVisibility(View.GONE);
			mCamRowArrow.setBackgroundResource(R.drawable.arrow);
		}  else {
			mFaceRecognitionContent.setVisibility(View.VISIBLE);
			mCamRowArrow.setBackgroundResource(R.drawable.arrow_down);
		}
		if(!database) {
			mControlDatabaseContent.setVisibility(View.GONE); 
			mControlDatabaseArrow.setBackgroundResource(R.drawable.arrow);
		} else {
			mControlDatabaseContent.setVisibility(View.VISIBLE);
			mControlDatabaseArrow.setBackgroundResource(R.drawable.arrow_down);
		}
		if(!behavior) {
			mControlBehaviorContent.setVisibility(View.GONE); 
			mControlBehaviorArrow.setBackgroundResource(R.drawable.arrow);
		} else {
			mControlBehaviorContent.setVisibility(View.VISIBLE);
			mControlBehaviorArrow.setBackgroundResource(R.drawable.arrow_down);
		}
		if(!gesture) {
			mControlGestureContent.setVisibility(View.GONE); 
			mControlGestureArrow.setBackgroundResource(R.drawable.arrow);
		} else {
			mControlGestureContent.setVisibility(View.VISIBLE);
			mControlGestureArrow.setBackgroundResource(R.drawable.arrow_down);
		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		
		// FACE DETECTION 
		
		case R.id.controler_face_recognition_training_row:
			changeRowsStateVisible(true, false, false, false);
			if(mFaceRecognitionStatus == 0) {
				mRobotActivity.showProgressDialog("Getting namespace...");
				faceApi.getNameSpaces();
				mFaceRecognitionStatus = 1;
				mFaceRecognitionContent.setVisibility(View.VISIBLE);
				mCamRowArrow.setBackgroundResource(R.drawable.arrow_down);
			} else {
				mFaceRecognitionStatus = 0;
				mFaceRecognitionContent.setVisibility(View.GONE);
				mCamRowArrow.setBackgroundResource(R.drawable.arrow);
			}
			break;
		case R.id.controler_face_recognition_camera_play:
			if(mCamPlayStatus == 0) {
				if(mRobotActivity.getRobot() == null) {
					mRobotActivity.scan();
				} else {
					mCamPlayStatus = 1;
					mCamPlay.setBackgroundResource(R.drawable.control_camera_pause);
					mCamPreviewCapture.setVisibility(View.GONE);
					mCameraPreview.setVisibility(View.VISIBLE);
					startCameraPreview();
				}
			} else {
				if(mRobotActivity.getRobot() == null) {
					mRobotActivity.scan();
				} else {
					mCamPlayStatus = 0;
					mCamPlay.setBackgroundResource(R.drawable.control_camera_play);
					mCameraPreview.setVisibility(View.GONE);
					mCamPreviewCapture.setVisibility(View.VISIBLE);
					stopCameraPreview();
				}
			}
			break;
		case R.id.controler_face_recognition_camera_recognize:
			Robot robot = mRobotActivity.getRobot();
			if (robot == null) {
				mRobotActivity.scan();
			}
			if (robotCamera == null) {
				robotCamera = RobotCamera.getCamera(robot,RobotCamera.CAMERA_TOP);
			}
			else {
				detect = false;
				startFaceDetection();
			}
			break;
		case R.id.controler_face_recognition_camera_detect:
			if (mRobotActivity.getRobot() == null) {
				mRobotActivity.scan();
			} else {
				if (robotCamera == null) {
					robotCamera = RobotCamera.getCamera(mRobotActivity.getRobot(), RobotCamera.CAMERA_TOP);
				}
				if (robotCamera == null) {
					mRobotActivity.scan();
				} else {
					detect = false;
					startFaceDetection();
				}
			}
			break;
		case R.id.controler_face_recognition_camera_capture:
			if (mRobotActivity.getRobot() == null) {
				mRobotActivity.scan();
			} else {
				if (robotCamera == null) {
					robotCamera = RobotCamera.getCamera(mRobotActivity.getRobot(), RobotCamera.CAMERA_TOP);
				}
				if (robotCamera == null) {
					mRobotActivity.scan();
				} else {
					takePicture(0, 1, "VGA", false, false);
				}
			}
			break;
		case R.id.controler_face_recognition_camera_train_URLtrain:
			if (mRobotActivity.getRobot() == null) {
				mRobotActivity.scan();
			} else {
					String nameSpace = mCamSpinner.getSelectedItem().toString();
					String ss = mCamTrainURLLink.getText().toString();
					ArrayList<String> urls = new ArrayList<String>();
					String[] parts = ss.split(" ");
					String label = "";
					for(int i=0; i<parts.length; i++) {
						urls.add(parts[i]);
					}
					mRobotActivity.showProgressDialog( urls.toString() + mCamTrainURLId.getText().toString());
					mRobotActivity.trainUrl(urls, mCamTrainURLId.getText().toString(), nameSpace, label);
			}
			break;
		case R.id.controler_face_recognition_camera_train:
			if (mRobotActivity.getRobot() == null) {
				mRobotActivity.scan();
			} else {
				if (robotCamera == null) {
					robotCamera = RobotCamera.getCamera(mRobotActivity.getRobot(),RobotCamera.CAMERA_TOP);
				}
				if (robotCamera == null) {
					mRobotActivity.scan();
				} else {
					String uid = mCamUserId.getText().toString();
					if (uid == null || TextUtils.isEmpty(uid)) {
						return;
					}
					String nameSpace = mCamSpinner.getSelectedItem().toString();
					train(uid, nameSpace, null, 5);
				}
			}
			break;
			
		// DATABASE
		case R.id.controler_database_row:
			changeRowsStateVisible(false, true, false, false);
			if(mControlDatabaseStatus == 0) {
				mControlDatabaseStatus = 1;
				mControlDatabaseContent.setVisibility(View.VISIBLE);
				mControlDatabaseArrow.setBackgroundResource(R.drawable.arrow_down);
			} else {
				mControlDatabaseStatus = 0;
				mControlDatabaseContent.setVisibility(View.GONE);
				mControlDatabaseArrow.setBackgroundResource(R.drawable.arrow);
			}
			break;
		case R.id.controler_database_query_button:
			SystemUtil.hideSoftKeyboard(mRobotActivity);
//			mRobotActivity.queryNeo4j(mControlDatabaseQueryText.getText().toString());
			break;
		case R.id.controler_database_clear_button:
			mControlDatabaseQueryText.setText("");
			break;
		// BEHAVIOR
		case R.id.controler_behavior_row:
			if(mRobotActivity.getRobot() == null) {
				mRobotActivity.scan();
			} else {
				changeRowsStateVisible(false, false, true, false);
				if(mControlBehaviorStatus == 0) {
					mControlBehaviorStatus = 1;
					mControlBehaviorContent.setVisibility(View.VISIBLE);
					mControlBehaviorArrow.setBackgroundResource(R.drawable.arrow_down);
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mRobotActivity.initBehaviorModule(mControlBehaviorListview);
						}
					});
				} else {
					mControlBehaviorStatus = 0;
					mControlBehaviorContent.setVisibility(View.GONE);
					mControlBehaviorArrow.setBackgroundResource(R.drawable.arrow);
				}
			}
			break;
		// GESTURE
		case R.id.controler_gesture_row:
			if(mRobotActivity.getRobot() == null) {
				mRobotActivity.scan();
			} else {
				changeRowsStateVisible(false, false, false, true);
				if(mControlGestureStatus == 0) {
					mControlGestureStatus = 1;
					mControlGestureContent.setVisibility(View.VISIBLE);
					mControlGestureArrow.setBackgroundResource(R.drawable.arrow_down);
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mRobotActivity.initGestureModule(mControlGestureListview);
						}
					});
				} else {
					mControlGestureStatus = 0;
					mControlGestureContent.setVisibility(View.GONE);
					mControlGestureArrow.setBackgroundResource(R.drawable.arrow);
				}
			}
			break;
		default:
			break;
		}
	}
	// DATABASE
	public LinearLayout mControlDatabaseRow;
	public LinearLayout mControlDatabaseContent;
	public TextView mControlDatabaseArrow;
	public TextView mControlDatabaseQuery;
	public EditText mControlDatabaseQueryText;
	public TextView mControlDatabaseClear;
	public static int mControlDatabaseStatus = 0;
	// GESTURE
	public LinearLayout mControlGestureRow;
	public LinearLayout mControlGestureContent;
	public TextView mControlGestureArrow;
	public static int mControlGestureStatus = 0;
	public PullToRefreshListView mControlGestureListview;
	// BEHAVIOR
	public LinearLayout mControlBehaviorRow;
	public LinearLayout mControlBehaviorContent;
	public TextView mControlBehaviorArrow;
	public static int mControlBehaviorStatus = 0;
	public PullToRefreshListView mControlBehaviorListview;
	// FACE RECOGNITION
	public LinearLayout mFaceRecognitionRow;
	public int mFaceRecognitionStatus = 0;
	public LinearLayout mFaceRecognitionContent;
	public TextView mCamRowArrow;
	public ImageView mCamPreviewCapture;
	public TextView mCamPlay;
	public static int mCamPlayStatus = 0;
	public Spinner mCamSpinner;
	public EditText mCamUserId;
	public TextView mCamTrain;
	public TextView mCamTrainURL;
	public EditText mCamTrainURLId;
	public EditText mCamTrainURLLink;
	public TextView mCamDetect;
	public TextView mCamCapture;
	public TextView mCamRecognize;
	public RobotFaceDetection.Monitor faceMonitor;
	public RobotCameraPreviewView mCameraPreview;
	public RobotCameraPreview mRobotCameraPreview;
	public RobotCamera robotCamera;
	public FaceApiClient faceApi;
	public boolean detect = false;
	
	public void detect() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String fileName = robotCamera.takePicture(RobotCamera.PICTURE_RESOLUTION_VGA, RobotCamera.PICTURE_FORMAT_JPG);
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mCameraPreview.setVisibility(View.GONE);
							mCamPreviewCapture.setVisibility(View.VISIBLE);
						}
					});
					stopCameraPreview();
					displayPicture(fileName);
					mRobotActivity.showProgressDialog( "Detecting ...");
					faceApi.detect(new File(fileName));
				} catch (RobotException e) {
					mRobotActivity.removePreviousDialog();
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void train(final String uid, final String namespace, final String label, final int numberOfPictures) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String[] fileNames = robotCamera.takePictures(RobotCamera.PICTURE_RESOLUTION_VGA, RobotCamera.PICTURE_FORMAT_JPG, numberOfPictures, 1000);
					ArrayList<File> imageFiles = new ArrayList<File>();
					for (String s : fileNames) {
						imageFiles.add(new File(s));
					}
					mRobotActivity.showProgressDialog( "Training ...");
					faceApi.trainFiles(imageFiles, uid, namespace, label);

				} catch (RobotException e) {
					mRobotActivity.removePreviousDialog();
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void recognize(final String namespace) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String fileName = robotCamera.takePicture( RobotCamera.PICTURE_RESOLUTION_VGA, RobotCamera.PICTURE_FORMAT_JPG);
					mRobotActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mCameraPreview.setVisibility(View.GONE);
							mCamCapture.setVisibility(View.VISIBLE);
						}
					});
					stopCameraPreview();
					displayPicture(fileName);
					mRobotActivity.showProgressDialog("Recognizing...");
					faceApi.recognize(new File(fileName), namespace);
				} catch (RobotException e) {
					mRobotActivity.removePreviousDialog();
					e.printStackTrace();
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
						 mRobotActivity.showToast("Start camera preview failed: " + e.getMessage().toString());
						return;
					}
					if (result) {
						 mRobotActivity.showToast("Camera preview started!");
					} else {
						 mRobotActivity.showToast("Start camera preview failed!");
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
					// mRobotActivity.showToast("Stop camera preview failed: " + e.getMessage().toString());
					return;
				}
				if (result) {
					// mRobotActivity.showToast("Camera preview stopped!");
				} else {
					// mRobotActivity.showToast("Stop camera preview failed!");
				}
			}
		});
	}
	public void takePicture(final int selectedCameraIndex, final int numberOfPictures, final String resolutionStr, final boolean enableAction, final boolean morePictures) {
		final RobotCamera[] mCamera = new RobotCamera[2];
		mCamera[0] = RobotCamera.getCamera(mRobotActivity.getRobot(),RobotCamera.CAMERA_TOP);
		mCamera[1] = RobotCamera.getCamera(mRobotActivity.getRobot(),RobotCamera.CAMERA_BOTTOM);
		if (selectedCameraIndex < 0 && selectedCameraIndex > 1) {
			mRobotActivity.showProgressDialogFromUIThread( "Invalid camera index. " + "Please select Camera Top or Camera Bottom");
			return;
		}
		new Thread(new Runnable() {
			boolean SET_PICTURE_FILE_NAME = true;
			@Override
			public void run() {
				// if take more picture, name of pictures which are saved in
				// sdcard will be set to default
				if (morePictures) {
					SET_PICTURE_FILE_NAME = false;
				}
				String picture = null;
				mRobotActivity.showProgressDialog("taking picture...");
				int resolution = RobotCamera.PICTURE_RESOLUTION_VGA;
				String pictureFormat = RobotCamera.PICTURE_FORMAT_BMP;
				if (resolutionStr.equals("QQVGA")) {
					resolution = RobotCamera.PICTURE_RESOLUTION_QQVGA;
				} else if (resolutionStr.equals("QVGA")) {
					resolution = RobotCamera.PICTURE_RESOLUTION_QVGA;
				} else if (resolutionStr.equals("VGA")) {
					resolution = RobotCamera.PICTURE_RESOLUTION_VGA;
				} else if (resolutionStr.equals("4VGA")) {
					resolution = RobotCamera.PICTURE_RESOLUTION_4VGA;
				} else {
					mRobotActivity.showToast("Invalid selected resolution!");
					return;
				}

				try {
					if (SET_PICTURE_FILE_NAME) {
						// default name to save picture
						String savedPath = Environment.getExternalStorageDirectory().getPath()+ "/picture";
						if (pictureFormat == RobotCamera.PICTURE_FORMAT_JPG) {
							savedPath = savedPath + ".jpg";
						} else if (pictureFormat == RobotCamera.PICTURE_FORMAT_PNG) {
							savedPath = savedPath + ".png";
						} else if (pictureFormat == RobotCamera.PICTURE_FORMAT_BMP) {
							savedPath = savedPath + ".bmp";
						}
						boolean result = false;
						if (enableAction) {
							// take picture with action
							result = mCamera[selectedCameraIndex].takePictureWithAction(resolution,pictureFormat, savedPath);
						} else {
							// take picture no action
							result = mCamera[selectedCameraIndex].takePicture(resolution, pictureFormat, savedPath);
						}
						if (result) {
							picture = savedPath;
						}
					} else {
						// take picture with specified resolution and format
						if (enableAction) {
							if (morePictures) {
								// get number of pictures will be taken
								// time delay for each picture
								long delay = 1000;
								// take more picture with action
								String[] path = mCamera[selectedCameraIndex].takePicturesWithAction(resolution,pictureFormat,numberOfPictures, delay);
								if (path != null && path[0] != null) {
									picture = path[0];
								}
							} else {
								// take 1 picture with action
								picture = mCamera[selectedCameraIndex].takePictureWithAction(resolution,pictureFormat);
							}
						} else {
							if (morePictures) {
								// get number of pictures will be taken
								// time delay for each picture
								long delay = 1000;
								// taken more pictures no action
								String[] path = mCamera[selectedCameraIndex].takePictures(resolution,pictureFormat,numberOfPictures, delay);
								if (path != null && path[0] != null) {
									picture = path[0];
								}
							} else {
								// take one picture no action
								picture = mCamera[selectedCameraIndex].takePicture(resolution, pictureFormat);
							}
						}
					}
					mRobotActivity.removePreviousDialog();
				} catch (RobotException e) {
					e.printStackTrace();
					mRobotActivity.showProgressDialogFromUIThread("Take picture from camera failed! " + e.getMessage());
					return;
				}
				
				if (picture == null || picture.isEmpty()) {
					Log.e(TAG, "can not take picture!");
					mRobotActivity.showProgressDialogFromUIThread("Cannot take picture from camera!");
					return;
				} else {
					Log.d(TAG, "Picture saved to " + picture + "!");
//					mRobotActivity.showToast("Picture saved to " + picture + "!");
					displayPicture(picture);
				}
			}
		}).start();
	}

	public void displayPicture(final String picture) {
		final String picturePath = picture;
		final Bitmap bm = BitmapFactory.decodeFile(picturePath);
		if (bm != null) {
			mRobotActivity.runOnUiThread(new Runnable() {
				public void run() {
					mCamPreviewCapture.setVisibility(View.VISIBLE);
					mCamPreviewCapture.setImageBitmap(bm);
				}
			});
		} else {
			 mRobotActivity.showProgressDialog("Picture saved to " + picturePath + "!");
		}
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
											// TODO Auto-generated method stub
										}
										@Override
										public void onFaceCropDetected() {
											if (detect) {
												detect();
											} else {
												String nameSpace = mCamSpinner.getSelectedItem().toString();
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
	
	ArrayList<String> listNameSpaces = new ArrayList<String>();

	final static String TAG = "NAO-CONTROLER";
	public static final long serialVersionUID = -33585526885386682L;
	public static RobotMainActivity mRobotActivity;
	public static ControlFragment mInstance;
	public static ControlFragment getInstance(RobotMainActivity activity) {
		if (mInstance == null) {
			mInstance = new ControlFragment();
		}
		mRobotActivity = activity;
		return mInstance;
	}
}
