/*
 * Name: $RCSfile: MyAsyncHttpResponseProcess.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Dec 8, 2011 9:54:28 AM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package uet.invincible.webservice;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MyAsyncHttpResponsePostProcess implements
		MyAsyncHttpResponseListener {

	FragmentManager mFragmentManager;
	String mTimestamp;
	int mErrorId;
	String mErrorMessage;
	Context mContext;

	public MyAsyncHttpResponsePostProcess(Context context,
			FragmentManager fragmentManager) {
		this.mFragmentManager = fragmentManager;
		this.mContext = context;
	}

	@Override
	public void before() {
	}

	@Override
	public void after(int statusCode, String responseStr) {
		// Process server response
		if (responseStr == null)
			return;

		switch (statusCode) {
		case MyAsyncHttpBase.NETWORK_STATUS_OFF:
//			showAlert(mContext,
//					mContext.getString(R.string.message_network_is_unavailable));
			break;
		case MyAsyncHttpBase.NETWORK_STATUS_OK:
			processHttpResponse(responseStr);
			break;
		case MyAsyncHttpBase.NETWORK_STATUS_ERROR:
			processIfNetworkError(responseStr);
			break;
		default:
//			showAlert(mContext,
//					mContext.getString(R.string.message_server_error));
			break;
		}
	}

	/**
	 * Process HttpResponse
	 * 
	 * @param response
	 */
	public void processHttpResponse(String responseStr) {
		String json = "";
		try {
			// Get json response
			json = responseStr;
			if (json == null) {
//				showAlert(
//						mContext,
//						mContext.getString(R.string.message_error_cant_extract_server_response));
				return;
			}

			Object object = new JSONTokener(json).nextValue();
			if (object instanceof JSONObject) {
				JSONObject root = new JSONObject(json);
				mTimestamp = root.getString("timestamp");
				mErrorId = root.getInt("error_id");
				mErrorMessage = root.getString("error_message");

				if (mErrorMessage.length() > 0 && mErrorId != 0) {
					processIfResponseFail(mErrorId, mErrorMessage);
				} else {
					processIfResponseSuccess(json);
				}
			} else if (object instanceof JSONArray) {
				// you have an array
				processIfResponseSuccess(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
			processIfServerError("Error");
		}
	}

	/**
	 * Interface function
	 */
	public void processIfResponseSuccess(String response) {
		// Process if response is success
	}

	/**
	 * Interface function
	 */
	public void processIfResponseFail(int errorId, String message) {
		// Process if response is fail
	}

	/**
	 * Interface function
	 */
	public void processIfServerError(String message) {
		// Process if server error
	}
	
	public void processIfNetworkError(String message) {
		//Proces if network error, example: time out
	}

	/**
	 * 
	 * @param context
	 * @param message
	 */
//	public void showAlert(Context context, String message) {
//		// clear all state previous
//		removePreviousDialog("alert_dialog");
//		mAlertDialog = null;
//		// create dialog
//		mAlertDialog = MyAlertDialogFragment.newInstance(context,
//				mContext.getString(R.string.app_name), message,
//				mContext.getString(android.R.string.ok),
//				mContext.getString(android.R.string.ok), new MyAlertListener() {
//
//					@Override
//					public void onRightClick(Dialog dialog) {
//						mAlertDialog.dismissAllowingStateLoss();
//					}
//
//					@Override
//					public void onLeftClick(Dialog dialog) {
//					}
//				});
//		mAlertDialog.showOnlyOneButton(true);
//		// show dialog
//		mAlertDialog.show(mFragmentManager, "alert_dialog");
//	}

	
	protected void removePreviousDialog(String tag) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		Fragment prev = mFragmentManager.findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.commit();
	}
}
