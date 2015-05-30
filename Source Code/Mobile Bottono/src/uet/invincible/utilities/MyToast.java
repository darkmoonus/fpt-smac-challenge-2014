package uet.invincible.utilities;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MyToast {

	public static void show(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public static void showToastMessage(Context context, String message) {
		if (context != null && message != null) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

	public static void showToastMessage(Context context, int messageId) {
		if (context != null) {
			Toast.makeText(context, context.getString(messageId),
					Toast.LENGTH_LONG).show();
		}
	}

	public static void showShortToastMessage(Context context, String message) {
		if (context != null && message != null) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showShortToastMessage(Context context, int messageId) {
		if (context != null) {
			Toast.makeText(context, context.getString(messageId),
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void showShortToastMessageWithPosition(Context context,
			int messageId) {
		if (context != null) {
			Toast toast = Toast.makeText(context, context.getString(messageId),
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}
	}

	public static void showToastMessage(Context context, String message,
			int duration) {
		if (context != null && message != null) {
			Toast.makeText(context, message, duration).show();
		}
	}

	public static void showToastMessage(Context context, int messageId,
			int duration) {
		if (context != null) {
			Toast.makeText(context, context.getString(messageId), duration)
					.show();
		}
	}

	public static void showComingSoonMessage(Context context) {
		showToastMessage(context, "Comming soon!");
	}
}
