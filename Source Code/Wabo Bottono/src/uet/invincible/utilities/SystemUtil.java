package uet.invincible.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SystemUtil {
	
	public static int getRandom(int x) {
		return (int)(Math.random() * x + 1);
	}
	@SuppressLint("SimpleDateFormat")
	public static String getFullDate() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getTimeHM_FullDate() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		View view = activity.getCurrentFocus();
		if (view != null) {
			IBinder binder = view.getWindowToken();
			if (binder != null) {
				inputMethodManager.hideSoftInputFromWindow(binder, 0);
			}
		}

	}
}
