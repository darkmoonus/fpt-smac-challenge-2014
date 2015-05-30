package uet.invincible.dialogs;


import uet.invincible.R;
import uet.invincible.customize.MyGifView;
import uet.invincible.customize.MyTextView;
import uet.invincible.listeners.ProgressDialogListenter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class ProgressDialog extends DialogFragment {
	public Context mContext;
	public LayoutInflater mInflater;
	protected ProgressDialogListenter mListener;
	protected Dialog mDialog;
	protected MyGifView mLoadingView;
	protected MyTextView mContent;
	protected String mContentText;
	
	private ProgressDialog(Context context, String content, ProgressDialogListenter listener) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mListener = listener;
		this.mContentText = content;
	}

	public static ProgressDialog newInstance(Context context, String content,
			ProgressDialogListenter listener) {
		ProgressDialog alertDialog = new ProgressDialog(context, content, listener);
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
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = mInflater.inflate(R.layout.dialog_progress, null);
	
		initViews(view);
		setViews();
		initOnClick();
		initAnimation();
		
		mDialog.setContentView(view);
		return mDialog;
	}
	
	public void initViews(View view) {
		mContent = (MyTextView) view.findViewById(R.id.dialog_progress_content);
		mLoadingView = (MyGifView) view.findViewById(R.id.dialog_progress_loading);
	}
	public void setViews() {
		mContent.setText(mContentText);
		mLoadingView.setMovieResource(R.drawable.loading_setting);
	}
	
	public void initOnClick() {
		
	}
	
	public void initAnimation() {
		
	}
}
