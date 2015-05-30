package uet.invincible.dialogs;


import uet.invincible.customize.MyAnimations;
import uet.invincible.listeners.TwoChosenDialogListener;
import uet.invincible.mobile.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class TwoChosenDialog extends DialogFragment {
	public Context mContext;
	public LayoutInflater mInflater;
	protected TwoChosenDialogListener mListener;
	protected Dialog mDialog;
	
	protected TextView mLeftButton;
	protected TextView mRightButton;
	protected TextView mContent;
	
	protected String mContentText;
	protected String mLeftButtonText;
	protected String mRightButtonText;
	
	private TwoChosenDialog(Context context, String content, String leftButton, String rightButton,TwoChosenDialogListener listener) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mListener = listener;
		this.mContentText = content;
		this.mLeftButtonText = leftButton;
		this.mRightButtonText = rightButton;
	}

	public static TwoChosenDialog newInstance(Context context, String content, String leftButton, String rightButton,
			TwoChosenDialogListener listener) {
		TwoChosenDialog alertDialog = new TwoChosenDialog(context, content, leftButton, rightButton, listener);
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
		View view = mInflater.inflate(R.layout.dialog_two_chosen, null);
	
		initViews(view);
		setViews();
		initOnClick();
		initAnimation();
		
		mDialog.setContentView(view);
		return mDialog;
	}
	
	public void initViews(View view) {
		mLeftButton = (TextView) view.findViewById(R.id.dialog_two_chosen_ok);
		mRightButton = (TextView) view.findViewById(R.id.dialog_two_chosen_cancel);
		mContent = (TextView) view.findViewById(R.id.dialog_two_chosen_content);
	}
	public void setViews() {
		mLeftButton.setText(mLeftButtonText);
		mRightButton.setText(mRightButtonText);
		mContent.setText(mContentText);
	}
	
	public void initOnClick() {
		mLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onLeftClick(mDialog);
			}
		});
		mRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onRightClick(mDialog);
			}
		});
	}
	
	public void initAnimation() {
		mLeftButton.setAnimation(MyAnimations.fromLeft(100, 500));
		mRightButton.setAnimation(MyAnimations.fromRight(100, 500));
	}

}
