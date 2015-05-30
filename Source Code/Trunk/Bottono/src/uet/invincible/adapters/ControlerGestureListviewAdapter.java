package uet.invincible.adapters;

import java.util.ArrayList;
import java.util.List;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ControlerGestureListviewAdapter extends ArrayAdapter<String> {
	public List<String> mGestureName = new ArrayList<String>();
	public RobotMainActivity mRobotActivity;
	public Context mContext;
	public LayoutInflater mInflate;
	public ArrayList<ViewHolder> mHolderList = new ArrayList<ViewHolder>();
	public ControlerGestureListviewAdapter(RobotMainActivity mActivity, Context context, int textViewResourceId, List<String> mGestureName) {
		super(context, textViewResourceId, mGestureName);
		mRobotActivity = mActivity;
		mContext = context;
		mInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (String String : mGestureName) {
			this.mGestureName.add(String);
		}
	}
	
	public void add(String name) {
		this.mGestureName.add(name);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
            convertView = new View(mContext);
            convertView = mInflate.inflate(R.layout.item_controler_gesture_listview, null);
            holder.mGestureName = (TextView)convertView.findViewById(R.id.item_controler_gesture_listview_name);
            holder.mPlay = (TextView)convertView.findViewById(R.id.item_controler_gesture_listview_play);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mGestureName.setText(mGestureName.get(position));
		holder.mPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRobotActivity.runGesture(mGestureName.get(position));
			}
		});
		return convertView;
	}

	class ViewHolder {
		public TextView mGestureName;
		public TextView mPlay;
	}
}
