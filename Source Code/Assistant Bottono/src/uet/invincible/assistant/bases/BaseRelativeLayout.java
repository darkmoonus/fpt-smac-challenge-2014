package uet.invincible.assistant.bases;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * BaseRelativeLayout
 * 
 * @author khanhnv
 */
public class BaseRelativeLayout extends RelativeLayout {
	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public BaseRelativeLayout(Context context) {
		super(context);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public BaseRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * Initial inflater layout
	 * 
	 * @param reID
	 * @param context
	 */
	protected void initLayout(Context context, int reID) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(reID, this, true);
	}

	/**
	 * Get parent activity
	 * 
	 * @return
	 */
	public Activity getActivity() {
		return (Activity) this.getContext();
	}

}