package uet.invincible.wabo.mission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uet.invincible.bases.BaseActivity;
import uet.invincible.listeners.MyAsyncHttpResponseListener;
import uet.invincible.wabo.R;
import uet.invincible.webservice.MyAsyncHttpBase;
import uet.invincible.webservice.MyAsyncHttpPost;
import uet.invincible.webservice.Parrams;
import uet.invincible.webservice.ServiceConfigure;
import android.R.bool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.RobotInfo;
import com.fpt.robot.motion.RobotMotionLocomotionController;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.types.RobotMoveTargetPosition;
import com.fpt.robot.types.RobotPose2D;
import com.fpt.robot.vision.RobotObjectDetection;
import com.fpt.robot.vision.RobotObjectDetection.DetectedObject;
import com.fpt.robot.vision.RobotObjectDetection.ObjectPose;
import com.fpt.robot.wabo.WaboArm;
import com.fpt.robot.wabo.WaboSMAC;
import com.fpt.robot.wabo.WaboSMAC.DetectedMarker;
import com.fpt.robot.wabo.WaboSMAC.MarkerPose;
import com.fpt.robot.wabo.WaboSensors;
import com.fpt.robot.wabo.WaboSensors.BumperPosition;
import com.fpt.robot.wabo.WaboSensors.ButtonPosition;
import com.fpt.robot.wabo.WaboSensors.CliffPosition;
import com.fpt.robot.wabo.WaboSensors.ProximityPosition;
import com.fpt.robot.wabo.WaboSensors.ProximityState;
import com.fpt.robot.wabo.WaboSensors.SonarPosition;
import com.fpt.robot.wabo.WaboSensors.WheelPosition;

public class WaboActivity extends BaseActivity implements 
	RobotObjectDetection.Listener, OnClickListener,
	WaboSensors.Listener, WaboSMAC.Listener {
	
	public Timer timerDetect;
	public Timer timerMarker;

	public boolean wakeUp;

	public Button start, retry, scan, get, put, armInit;
	public EditText x, y, theta, log;
	public ObjectPose mPose;
	
	public boolean isBumperWhenGetAndPutObject = false;
	
	public boolean isLongMove = false;
	public boolean canMoveCloserToObject = false;
	@Override
	public void onObjectsDetected(final ArrayList<DetectedObject> objects) {
		for (DetectedObject obj : objects) {
			final ObjectPose pose = obj.getPose();
			int id = obj.getObjectId();
			addToLog("Detect ID: " + id);
			if (id == WaboState.currentPath.targetid && !mDetectedObject) {
				addToLog("Found object: " + id);
				mDetectedObject = true;
				if(canMoveCloserToObject) {
					mPose = pose;
					timerDetect.cancel();
					try {
						addToLog("Detect " + id + " success, move to get object");
						RobotObjectDetection.stopDetection(getRobot());
						RobotMotionLocomotionController.moveStop(getRobot());
						
						moveCloserToObject(id);
						
						getObject(getRobot());
						
						move(-0.7f, 0, 0, wakeUp, getRobot());
						
						WaboState.currentPos = relocation(getRobot());
//						WaboState.currentPath = WaboAlgorithm.getPath(WaboState.currentPos, getTablePos(WaboState.currentOrder.tableId));
						WaboState.currentPath = WaboAlgorithm.getPath_ver2(WaboState.currentPos, getTablePos(WaboState.currentOrder.tableId));						
						WaboState.currentPath.targetid = WaboState.currentOrder.tableId;
						runPath(WaboState.currentPath);
						
//						startDetectMarker();
						boolean b = moveCloserToTable(WaboState.currentPath.targetid);
						
					} catch (Exception e) {
						addToLog("On object detect exeption: " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					canMoveCloserToObject = true;
					ObjectPose mPose = pose;
					addToLog("Can move closer to object.");
					try {
						RobotObjectDetection.stopDetection(getRobot());
					} catch (RobotException e) {
						e.printStackTrace();
					}
					if(!isLongMove) {
						move(0.2f, 0, 0, wakeUp, getRobot());
					} else {
						move(0.3f, 0, 0, wakeUp, getRobot());
					}
					mDetectedObject = false;
					startDetectObject();
				}
			}
		}
	}
	public boolean isStarted = false;
	public RobotPose2D getTablePos(int id) {
		if(id == 1) return WaboAlgorithm.table1;
		if(id == 2) return WaboAlgorithm.table2;
		if(id == 3) return WaboAlgorithm.table3;
		if(id == 4) return WaboAlgorithm.table4;
		if(id == 5) return WaboAlgorithm.table5;
		return null;
	}
	public void runPath(Path waboPath) {
		for (int i = 0; i < waboPath.listPoint.size(); i++) {
			RobotPose2D m = convertCoordirates(WaboState.currentPos, waboPath.listPoint.get(i));
			addToLog("Run: " + m.x + " " + m.y + " " + m.theta);
			move(m.x, m.y, m.theta, wakeUp, getRobot());
			WaboState.currentPos = waboPath.listPoint.get(i);
//			WaboState.currentPos = WaboSMAC.getCurrentPose(getRobot());
		}
	}
	public void serveOrder(Order order) {
		addToLog("Serve order : Food " + order.foodId + ", Table " + order.tableId);
		if(isStarted) WaboState.currentPos = relocation(getRobot());
		
		WaboState.currentOrder = order;
		float min = WaboAlgorithm.distance(WaboState.currentPos, WaboAlgorithm.safeZone.get(6));
		RobotPose2D minDes = WaboAlgorithm.dishesTableLeft;
		for(int i=7; i<10; i++) {
			if(min > WaboAlgorithm.distance(WaboState.currentPos, WaboAlgorithm.safeZone.get(i))) {
				min = WaboAlgorithm.distance(WaboState.currentPos, WaboAlgorithm.safeZone.get(i));
				minDes = WaboAlgorithm.safeZone.get(i);
			}
		}
//		WaboState.currentPath = WaboAlgorithm.getPath(WaboState.currentPos, minDes);
		WaboState.currentPath = WaboAlgorithm.getPath_ver2(WaboState.currentPos, minDes);		
		WaboState.currentPath.targetid = order.foodId;
		runPath(WaboState.currentPath);
		startDetectObject();
	}
	
	public int crrOrderIndex = 0;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scan:
			scan();
			break;
		case R.id.get:
			getObject(getRobot());
			break;
		case R.id.put:
			putObject(getRobot());
			break;
		case R.id.arm_init:
			armInit(getRobot());
			break;
		case R.id.start:
			wakeUp = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					
					int startPointID = mGroupStartPoint.getCheckedRadioButtonId();
					if(startPointID == R.id.radio_group_start_point_1){
						WaboState.currentPos = WaboAlgorithm.startPoint1;
						WaboState.startPoint = 1;
					} else 
					if(startPointID == R.id.radio_group_start_point_2){
						WaboState.currentPos = WaboAlgorithm.startPoint2;
						WaboState.startPoint = 2;
					}
					
					int sortTacticID = mGroupSortTactic.getCheckedRadioButtonId();
					if(sortTacticID == R.id.radio_group_sort_tactic_1) {
						addToLog("Sort tactic table one by one");
						sortTacticTableOneByOne();
					} else
					if(sortTacticID == R.id.radio_group_sort_tactic_2) {
						addToLog("Sort tactic by table");
						sortTacticByTable();
					} else 
					if(sortTacticID == R.id.radio_group_sort_tactic_3) {
						addToLog("Sort tactic normal");
					}
					
					try {
						startMarkersMonitor();
						startDetectMonitor();
					} catch (RobotException e) {
						e.printStackTrace();
					}
					serveOrder(WaboState.ordersList.get(crrOrderIndex));
				}
			}).start();
			break;
		case R.id.retry:
			armInit(getRobot());
			System.exit(0);
			break;
		default:
			break;
		}
	}

	public void startDetectObject() {
		if (getRobot() == null) {
			scanRobot();
			return;
		}
		mDetectedObject = false;
		addToLog("Start detection");
		try {
			RobotObjectDetection.startDetection(getRobot());
			timerDetect = new Timer();
			timerDetect.schedule(new TimerTask() {
				@Override
				public void run() {
					detectWhenTimeout();
				}
			}, 1500);
		} catch (RobotException e) {
			e.printStackTrace();
		}
	}
	public void detectWhenTimeout() {
		isLongMove = true;
		while (!mDetectedObject) {
			addToLog("Cannot reach object, LEFT");
			move(0, 0, 0.785f, true, getRobot());
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!mDetectedObject) {
				addToLog("Cannot reach object, RIGTH");
				move(0, 0, -1.57f, true, getRobot());
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!mDetectedObject) {
					move(0, 0, 0.785f, true, getRobot());
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!mDetectedObject) {
						if(crrOrderIndex < WaboState.ordersList.size() - 1) {
							serveOrder(WaboState.ordersList.get(++crrOrderIndex));
						}
					}
				}
			}
		}
	}
	public void startDetectMarker() throws Exception {
		if (getRobot() == null) {
			scanRobot();
			return;
		}
		addToLog("Start marker detection");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					WaboSMAC.enableMarkerDetection(getRobot());
					mMarkerDetect = false;
				} catch (RobotException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		timerDetect = new Timer();
		timerDetect.schedule(new TimerTask() {
			@Override
			public void run() {
				startDetectMarkerWhenTimeout();
			}
		}, 1500);
	}
	public void startDetectMarkerWhenTimeout() {
		addToLog("Start marker detection when timeout");
		if (!mMarkerDetect) {
			addToLog("Time out left");
			move(0, 0, 0.5f, wakeUp, getRobot());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!mMarkerDetect) {
				addToLog("Time out right");
				move(0, 0, -1.0f, wakeUp, getRobot());
				if(!mMarkerDetect) {
					move(0, 0, 0.5f, wakeUp, getRobot());
					move(-0.1f, 0, 0, wakeUp, getRobot());
				}
			}
		}
	}
	
	@Override
	public void onRobotServiceConnected() {
		super.onRobotServiceConnected();
		getRobot();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() != null) {
					try {
						addToLog("On Service connected Register monitor");
						mDetectMonitor = new RobotObjectDetection.Monitor(getRobot(), WaboActivity.this);
						mDetectMonitor.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void onNetworkConnected(boolean connected) {
		super.onNetworkConnected(connected);
		if (!connected) {
			wakeUp = false;
		}
		if (mSensorsMonitor != null) {
			try {
				stopSensorsMonitor();
			} catch (RobotException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		try {
			if (mDetectMonitor != null) {
				mDetectMonitor.stop();
				mDetectMonitor = null;
			}
			if (mSensorsMonitor != null) {
				mSensorsMonitor.stop();
				mSensorsMonitor = null;
			}
			if (mMarkerMonitor != null) {
				mMarkerMonitor.stop();
				mMarkerMonitor = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (getRobot() != null) {
			try {
				RobotObjectDetection.stopDetection(getRobot());
			} catch (RobotException e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
		wakeUp = false;
	}
	
	
	@Override
	public void onBumperPressed(BumperPosition bumper) {
		if (bumper == BumperPosition.LEFT) {
			addToLog("Left bumper was pressed.");
		} else if (bumper == BumperPosition.CENTER) {
			addToLog("Center bumper was pressed.");
		} else if (bumper == BumperPosition.RIGHT) {
			addToLog("Right bumper was pressed.");
		}
		addToLog("Bumper pressed: " + bumper.name());
		if (getRobot() == null) {
			return;
		}
		if(mBumperTag == BumperTag.NORMAL) {
			if(!isBumperWhenGetAndPutObject) {
				move(-0.1f, 0, 0, wakeUp, getRobot());
				WaboState.currentPos = relocation(getRobot());
				serveOrder(WaboState.ordersList.get(crrOrderIndex));
			} else {
				
			}
		}
	}
	
	public enum BumperTag {
		NORMAL, MARKER_EXEPTION
	}
	public BumperTag mBumperTag = BumperTag.NORMAL;
	public boolean isTalbleMarker(int markerID) {
		if(markerID >= 6 && markerID <= 10) return true;
		return false;
	}
	@Override
	public void onMarkersDetected(ArrayList<DetectedMarker> arg0) {
		for (final DetectedMarker detectedMarker : arg0) {
			addToLog("Marker ID JUST detected : " + detectedMarker.getMarkerId());
			if(isTalbleMarker(detectedMarker.getMarkerId()) && getTableIdFromMarker(
					detectedMarker.getMarkerId()) == WaboState.currentPath.targetid && !mMarkerDetect) {

				mMarkerDetect = true;
				MarkerPose mMarkerPose = detectedMarker.getPose();
				addToLog("Marker POS: " + mMarkerPose.x + " " + mMarkerPose.y + " " + mMarkerPose.theta);
				addToLog("Target marker found: " + detectedMarker.getMarkerId() + ", table: " + getTableIdFromMarker(detectedMarker.getMarkerId()));
				
				boolean b = moveCloserToTable(detectedMarker.getMarkerId());
				if(b) {
					isBumperWhenGetAndPutObject = true;
					putObject(getRobot());
					isBumperWhenGetAndPutObject = false;
					try {
						WaboSMAC.disableMarkerDetection(getRobot());
					} catch (RobotException e1) {
						e1.printStackTrace();
					}
					move(-0.4f, 0, 0, wakeUp, getRobot());
					WaboState.currentPos = relocation(getRobot());
					if(crrOrderIndex < WaboState.ordersList.size() - 1) {
						serveOrder(WaboState.ordersList.get(++crrOrderIndex));
					}
				} else {
					while(!b) {
						try {
							startDetectMarker();
						} catch (Exception e) {
							e.printStackTrace();
						}
						move(0.1f, 0, 0, wakeUp, getRobot());
						b = moveCloserToTable(detectedMarker.getMarkerId());
						if(b) {
							putObject(getRobot());
							try {
								WaboSMAC.disableMarkerDetection(getRobot());
							} catch (RobotException e1) {
								e1.printStackTrace();
							}
							move(-0.5f, 0, 0, wakeUp, getRobot());
							WaboState.currentPos = relocation(getRobot());
							if(crrOrderIndex < WaboState.ordersList.size() - 1) {
								serveOrder(WaboState.ordersList.get(++crrOrderIndex));
							}
							break;
						}
					}
				}
			}
		}
	}

	
	
	public void initViews() {
		mGroupStartPoint = (RadioGroup) findViewById(R.id.radio_group_start_point);
		mGroupSortTactic = (RadioGroup) findViewById(R.id.radio_group_sort_tactic);
		start = (Button) findViewById(R.id.start);
		start.setOnClickListener(this);
		retry = (Button) findViewById(R.id.retry);
		retry.setOnClickListener(this);
		scan = (Button) findViewById(R.id.scan);
		scan.setOnClickListener(this);
		get = (Button) findViewById(R.id.get);
		get.setOnClickListener(this);
		put = (Button) findViewById(R.id.put);
		put.setOnClickListener(this);
		armInit = (Button) findViewById(R.id.arm_init);
		armInit.setOnClickListener(this);
		log = (EditText) findViewById(R.id.ed_log);
	}
	public void initData() {
		showProgressDialog("Loading data...");

		WaboState.ordersList.add(new Order(3, 4));
		WaboState.ordersList.add(new Order(2, 3));
		WaboState.ordersList.add(new Order(4, 5));
		WaboState.ordersList.add(new Order(5, 2));

		queryLogin();
	}
	public void initState() {
		new WaboState();
	}
	
	public RobotPose2D convertCoordirates(RobotPose2D currentPos, RobotPose2D destinationPos) {
		float x = (float) ((destinationPos.x - currentPos.x)
				* Math.cos(currentPos.theta) + (destinationPos.y - currentPos.y)
				* Math.sin(currentPos.theta));
		float y = (float) (-(destinationPos.x - currentPos.x)
				* Math.sin(currentPos.theta) + (destinationPos.y - currentPos.y)
				* Math.cos(currentPos.theta));
		float theta = destinationPos.theta - currentPos.theta;
		addToLog("\nAbsolute: " + currentPos.x + " " + currentPos.y + " "
				+ currentPos.theta + " || " + destinationPos.x + " "
				+ destinationPos.y + " " + destinationPos.theta
				+ "\nConverted: " + x + " " + y + " " + theta);
		return new RobotPose2D(x, y, theta, true);
	}
	
	public RobotPose2D relocation(final Robot robot) {
		addToLog("Relocation start. ");
		try {
			move(0, 0, -0.7f, wakeUp, getRobot());
			RobotPose2D rel = WaboSMAC.relocation(getRobot());
			addToLog("Relocation: " + rel.x + " " + rel.y + " " + rel.theta + " SUCCESS !!");
			
			return rel;
		} catch (RobotException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public RadioGroup mGroupStartPoint;
	public RadioGroup mGroupSortTactic;
	public static final long serialVersionUID = 5557702737202316791L;
	public RobotObjectDetection.Monitor mDetectMonitor;
	public WaboSensors.Monitor mSensorsMonitor;
	public WaboSMAC.Monitor mMarkerMonitor;
	public boolean mDetectedObject = false;
	public boolean mMarkerDetect = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wakeUp = false;
		timerDetect = new Timer();
		timerMarker = new Timer();
		setContentView(R.layout.activity_test);
		initState();
		initViews();
		initData();
	}
	public void addToLog(final String s) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				log.append(s + "\n");
				Log.e("Wabo Bottono", s);
			}
		});
	}
	public int getTableIdFromMarker(int markerId) {
		switch (markerId) {
			case 10: return 1;
			case 7: return 2;
			case 8: return 5;
			case 9: return 4;
			case 6: return 3;
			default: break;
		}
		return -1;
	}

	public class AsyncGetOrders extends AsyncTask<String, Void, String> {
		public JSONObject jSon;
		@Override
		protected void onPreExecute() {
			addToLog("Getting orders...");
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			try {
				jSon = new JSONObject(GET(ServiceConfigure.getOrders + WaboState.token));
				if(jSon.getBoolean("status")) {
					addToLog("Json: " + jSon.toString());
					JSONArray data = jSon.getJSONArray("data");
					for(int i=0; i<data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						String strTableID = obj.getString("table_id");
						int tableId = 0;
						for(int ii=0; ii<WaboState.tableName.size(); ii++) {
							if(strTableID.equals(WaboState.tableName.get(ii))) {
								tableId = ii+1;
							}
						}
						
						JSONArray orderArr = obj.getJSONArray("order_items");
						for(int j=0; j<orderArr.length(); j++) {
							JSONObject order = orderArr.getJSONObject(j);
							int quantity = order.getInt("quantity");
							int served = order.getInt("served");
							int remain = quantity - served;
							int foodId = 0;
							String sType = order.getString("type");
							if(sType.equals("FoodOne")) foodId = 1; else
							if(sType.equals("FoodTwo")) foodId = 2; else
							if(sType.equals("FoodThree")) foodId = 3; else
							if(sType.equals("FoodFour")) foodId = 4; else
							if(sType.equals("FoodFive")) foodId = 5;		

							for(int k=0; k<remain; k++) {
								WaboState.ordersList.add(new Order(foodId, tableId));
							}
						}
					}
				} else {
					addToLog("Get orders: response status failed.");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}		
			return null; 
		}
		@Override
		protected void onPostExecute(final String result) {
			addToLog(jSon.toString());
			removePreviousDialog();
			for(int i=0; i<WaboState.ordersList.size(); i++) {
				addToLog("Order " + i + ": (food " + WaboState.ordersList.get(i).foodId + ", table " +  WaboState.ordersList.get(i).tableId + ")");
			}
		}
	}
	public void queryOrders() {
		new AsyncGetOrders().execute();
	}
	public class AsyncGetTablesName extends AsyncTask<String, Void, String> {
		public JSONObject jSon;
		@Override
		protected void onPreExecute() {
			addToLog("Getting tables name...");
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			try {
				jSon = new JSONObject(GET(ServiceConfigure.getTablesName + WaboState.token));
				addToLog("Json: " + jSon.toString());
				if(jSon.getBoolean("status")) {
					addToLog("Get tables name success.");
					JSONArray data = jSon.getJSONArray("data");
					addToLog(data.toString());
					for(int i=0; i<data.length(); i++) {
						WaboState.tableName.add(data.getString(i));
					}
				} else {
					addToLog("Get tables name status failed.");
				}
			} catch (JSONException e) {
				addToLog("Get tables name exeption: " + e.getMessage());
				e.printStackTrace();
			}		
			return null; 
		}
		@Override
		protected void onPostExecute(final String result) {
			removePreviousDialog();
			String s = "";
			for(int i=0; i<WaboState.tableName.size(); i++) {
				s += WaboState.tableName.get(i) + " ";
			}
			addToLog("WaboState tables name: " + s);
			queryOrders();
		}
	}
	public void queryTablesName() {
		new AsyncGetTablesName().execute();
	}
	public void queryTokenValidate() {
		new MyAsyncHttpPost(WaboActivity.this, new MyAsyncHttpResponseListener() {
			@Override
			public void before() {
				addToLog("Token validating...");
			}
			@Override
			public void after(int statusCode, String response) {
				if (statusCode == MyAsyncHttpBase.NETWORK_STATUS_OK) {
					addToLog(response);
					try {
						JSONObject res = new JSONObject(response);
						addToLog("Json: " + res.toString());
						if(res.getBoolean("status") == true) {
							addToLog("Token validate success.");
							queryTablesName();
						} else {
							addToLog("Token validate failed.");
						}
					} catch (JSONException e) {
						addToLog("Token validate exeption: " + e.getMessage());
						e.printStackTrace();
					}
					
				} else {
					addToLog("Disconnected to network.");
				}
			}
		}, Parrams.tokenValidate(WaboState.token)).execute(ServiceConfigure.tokenValidateURL);
	}
	public void queryLogin() {
		new MyAsyncHttpPost(WaboActivity.this, new MyAsyncHttpResponseListener() {
			@Override
			public void before() {
				addToLog("Start login... ");
			}
			@Override
			public void after(int statusCode, String response) {
				if (statusCode == MyAsyncHttpBase.NETWORK_STATUS_OK) {
					addToLog(response);
					try {
						JSONObject res = new JSONObject(response);
						addToLog("Json: " + res.toString());
						if(res.getBoolean("status") == true) {
							JSONObject data = res.getJSONObject("data");
							WaboState.token = data.getString("token");
							addToLog("Login success.\nNew token: " + WaboState.token);
							queryTokenValidate();
						} else {
							addToLog("Status failed");
						}
					} catch (JSONException e) {
						addToLog("Login exeption: " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					addToLog("Disconnected to network");
				}
			}
		}, Parrams.loginParrams(ServiceConfigure.username, ServiceConfigure.password)).execute(ServiceConfigure.loginURL);
	}

	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
			if(inputStream != null) result = convertInputStreamToString(inputStream);
			else result = "Did not work!";
		} catch (Exception e) {
			Log.d("GET Log",e.getLocalizedMessage());
		}
		return result;
	}
	public static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        return result;
    }
	
	public void move(final float x, final float y, final float theta, final boolean wakeUp, final Robot robot) {
		addToLog("Move: " + x + " " + y + " " + theta);
		try {
			if (!wakeUp) {
				RobotMotionStiffnessController.wakeUp(getRobot());
			}
			RobotMoveTargetPosition position = new RobotMoveTargetPosition(x, y, theta);
			RobotMotionLocomotionController.moveTo(getRobot(), position);
		} catch (RobotException e) {
			addToLog("Move to: " + x + " " + y + " " + theta + " exeption: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// Lấy các bàn gần bên mình trước.
	public void sortTacticByTable() {
		addToLog("Sort by table priority");
		ArrayList<Order> listOrder = new ArrayList<Order>();
		if(WaboState.startPoint == 1) {
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 5) listOrder.add(WaboState.ordersList.get(i)); 
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 1) listOrder.add(WaboState.ordersList.get(i)); 
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 4) listOrder.add(WaboState.ordersList.get(i)); 
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 2) listOrder.add(WaboState.ordersList.get(i)); 
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 3) listOrder.add(WaboState.ordersList.get(i)); 
		} else 
		if(WaboState.startPoint == 2) {
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 2) listOrder.add(WaboState.ordersList.get(i)); 
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 1) listOrder.add(WaboState.ordersList.get(i)); 
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 3) listOrder.add(WaboState.ordersList.get(i)); 
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 5) listOrder.add(WaboState.ordersList.get(i)); 
			for(int i=0; i<WaboState.ordersList.size(); i++) if(WaboState.ordersList.get(i).tableId == 4) listOrder.add(WaboState.ordersList.get(i)); 
		}
		WaboState.ordersList = listOrder;
		addToLog("After sort: ");
		for(int i=0; i<WaboState.ordersList.size(); i++) {
			addToLog("Table: Food " + WaboState.ordersList.get(i).foodId + " Table" + WaboState.ordersList.get(i).tableId);
		}
	}
	// Lấy các bàn, mỗi bàn 1 cái
	public boolean isChoosenStop(ArrayList<Boolean> isChoosen) {
		for(int i=0; i<isChoosen.size(); i++) {
			if(isChoosen.get(i) == false) {
				return false;
			}
		}
		return true;
	}
	public void sortTacticTableOneByOne() {
		addToLog("Sort by table one by one");
		ArrayList<Order> listOrder = new ArrayList<Order>();
		ArrayList<Boolean> isChoosen = new ArrayList<Boolean>();
		for(int i=0; i<isChoosen.size(); i++) isChoosen.set(i, false);
		
		if(WaboState.startPoint == 1) {
			while(!isChoosenStop(isChoosen)) {
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 5 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 1 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 4 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 2 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 3 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
			}
		} else 
		if(WaboState.startPoint == 2) {
			while(!isChoosenStop(isChoosen)) {
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 2 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 1 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 3 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 5 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
				for(int i=0; i<WaboState.ordersList.size(); i++) {
					if(WaboState.ordersList.get(i).tableId == 4 && isChoosen.get(i) == false) {
						listOrder.add(WaboState.ordersList.get(i)); 
						isChoosen.set(i, true);
						break;
					}
				}
			}
		}
		
		WaboState.ordersList = listOrder;
		addToLog("After sort: ");
		for(int i=0; i<WaboState.ordersList.size(); i++) {
			addToLog("Table: Food " + WaboState.ordersList.get(i).foodId + " Table" + WaboState.ordersList.get(i).tableId);
		}
	}
	
	
	
	public void armInit(Robot robot) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				try {
					addToLog("Arm init");
					WaboArm.runGesture(getRobot(), "RETURN_INIT");
				} catch (RobotException e) {
					addToLog("Arm init exeption: " + e.getMessage());
					e.printStackTrace();
				}
//			}
//		}).start();
	}
	public boolean moveCloserToTable(final int tableId) {
		boolean b = false;
		try {
			b = WaboSMAC.moveCloserToTable(getRobot(), "Table_" + getTableIdFromMarker(tableId));
		} catch (RobotException e) {
			e.printStackTrace();
		}
		addToLog("onMoveCloserToTable " + b);
		return b;
	}
	public void moveCloserToObject(final int objectId) {
		if (mDetectedObject) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
					WaboUtils.moveCloserObject(mPose, objectId, getRobot());
//				}
//			}).start();
		}
	}
	public void putObject(final Robot robot) {
		addToLog("Put object");
		boolean result = false;
		try {
			isBumperWhenGetAndPutObject = true;
			result = WaboArm.runGesture(getRobot(), "PUT_OBJ_ON_TABLE");
			addToLog("Place down: " + result);
			addToLog("Push 0.05");
			move(0.05f, 0, 0, wakeUp, getRobot());
			result = WaboSMAC.detachObject(getRobot());
			addToLog("Detect object: " + result);
			result = WaboArm.runGesture(getRobot(), "RETURN_INIT");
			addToLog("Return init: " + result);
			isBumperWhenGetAndPutObject = false;
		} catch (RobotException e) {
			addToLog("Put object exeption: " + e.getMessage());
			e.printStackTrace();
		}
	}
	public void getObject(final Robot robot) {
		addToLog("Get object");
		boolean result = false;
		try {
			isBumperWhenGetAndPutObject = true;
			result = WaboArm.runGesture(getRobot(), "APPROACH_OBJ");
			addToLog("Aproach object: " + result);
			result = WaboSMAC.attachObject(getRobot());
			addToLog("Attach object: " + result);
			result = WaboArm.runGesture(getRobot(), "GET_OBJ_FROM_TABLE");
			addToLog("Get object: " + result);
			isBumperWhenGetAndPutObject = false;
		} catch (RobotException e) {
			addToLog("Get object exeption:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean startDetectMonitor() throws RobotException {
		boolean result = mDetectMonitor.start();
		addToLog("Start detect monitor: " + result);
		return result;
	}
	public boolean startSensorsMonitor() throws RobotException {
		boolean result = mSensorsMonitor.start();
		addToLog("Start sensor monitor: " + result);
		return result;
	}
	public boolean startMarkersMonitor() throws RobotException {
		boolean result = mMarkerMonitor.start();
		addToLog("Start marker monitor " + result);
		return result;
	}
	public boolean stopDetectMonitor() throws RobotException {
		boolean result = mDetectMonitor.stop();
		addToLog("Stop detect monitor: " + result);
		return result;
	}
	public boolean stopSensorsMonitor() throws RobotException {
		boolean result = mSensorsMonitor.stop();
		addToLog("Stop markers monitor: " + result);
		return result;
	}
	public boolean stopMarkersMonitor() throws RobotException {
		boolean result = mMarkerMonitor.stop();
		addToLog("Stop markers monitor: " + result);
		return result;
	}
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}
	@Override
	public void onRobotConnected(String addr, int port) {
		super.onRobotConnected(addr, port);
		try {
			if (getRobot() != null) {
				if (mDetectMonitor != null) {
					RobotObjectDetection.stopDetection(getRobot());
					stopDetectMonitor();
				}
				if (mSensorsMonitor != null) {
					stopSensorsMonitor();
				}
				if (mMarkerMonitor != null) {
					WaboSMAC.disableMarkerDetection(getRobot());
					stopMarkersMonitor();
				}
				addToLog("On robot connected register monitor");
				mDetectMonitor = new RobotObjectDetection.Monitor(getRobot(), this);
				mSensorsMonitor = new WaboSensors.Monitor(getRobot(), this);
				addToLog("Start sensor monitor.");
				try {
					startSensorsMonitor();
				} catch (RobotException e) {
					addToLog("Start sensor monitor exeption: " + e.getMessage());
					e.printStackTrace();
				}
				mMarkerMonitor = new WaboSMAC.Monitor(getRobot(), this);
			}
		} catch (Exception e) {
			addToLog("On robot connected exeption: " + e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void onRobotDisconnected(String addr, int port) {
//		super.onRobotDisconnected(addr, port);
		if (getRobot() != null) {
			RobotInfo info = getRobot().getInfo();
			if (info != null) {
				if (info.getIpAddress().equalsIgnoreCase(addr)) {
					
				}
			}
		}
		try {
			if (mDetectMonitor != null) {
				mDetectMonitor.stop();
			}
			if (mMarkerMonitor != null) {
				mMarkerMonitor.stop();
			}
			if (mSensorsMonitor != null) {
				mSensorsMonitor.stop();
			}
		} catch (Exception e) {
			addToLog("On robot disconnected exeption: " + e.getMessage());
			e.printStackTrace();
		}
	}


	@Override
	public void onButtonPressed(ButtonPosition arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onCliffDetected(CliffPosition arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onProximityChanged(ProximityPosition arg0, ProximityState arg1) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onSonarChanged(SonarPosition arg0, double arg1) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onWheelDropped(WheelPosition arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onWheelRaised(WheelPosition arg0) {
		// TODO Auto-generated method stub
	}
}
