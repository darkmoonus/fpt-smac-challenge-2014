package uet.invincible.dialogs;

import uet.invincible.listeners.OptionsDialogListener;
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
import android.widget.LinearLayout;

public class OptionsDialog extends DialogFragment implements OnClickListener {
	public Context mContext;
	public LayoutInflater mInflater;
	protected OptionsDialogListener mListener;
	protected Dialog mDialog;
	protected LinearLayout mLayout;
	
	private OptionsDialog(Context context, OptionsDialogListener listener) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mListener = listener;
	}

	public static OptionsDialog newInstance(Context context, OptionsDialogListener listener) {
		OptionsDialog dialog = new OptionsDialog(context, listener);
		Bundle bundle = new Bundle();
		dialog.setArguments(bundle);
		return dialog;
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
		View view = mInflater.inflate(R.layout.dialog_options, null);
	
		initViews(view);
		setViews();
		initOnClick();
		initAnimation();
		
		mDialog.setContentView(view);
		return mDialog;
	}
	
	public void initViews(View view) {
		mLayout = (LinearLayout) view.findViewById(R.id.dialog_options_layout);
		mLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onExitClick(mDialog);
			}
		});
	}
	public void setViews() {
		
	}
	
	public void initOnClick() {
		
	}
	
	public void initAnimation() {
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void show(android.app.FragmentManager fragmentManager, String string) {
		show(fragmentManager, string);
		
	}

	
}
