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
public class ControlerBehaviorListviewAdapter extends ArrayAdapter<String> {
	public List<String> mBehaviorName = new ArrayList<String>();
	public RobotMainActivity mRobotActivity;
	public Context mContext;
	public LayoutInflater mInflate;
	public ArrayList<ViewHolder> mHolderList = new ArrayList<ViewHolder>();
	public ControlerBehaviorListviewAdapter(RobotMainActivity mActivity, Context context, int textViewResourceId, List<String> mBehaviorName) {
		super(context, textViewResourceId, mBehaviorName);
		mRobotActivity = mActivity;
		mContext = context;
		mInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (String String : mBehaviorName) {
			this.mBehaviorName.add(String);
		}
	}
	
	public void add(String name) {
		this.mBehaviorName.add(name);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
            convertView = new View(mContext);
            convertView = mInflate.inflate(R.layout.item_controler_behavior_listview, null);
            holder.mBehaviorName = (TextView)convertView.findViewById(R.id.item_controler_behavior_listview_name);
            holder.mPlay = (TextView)convertView.findViewById(R.id.item_controler_behavior_listview_play);
            holder.mStop = (TextView)convertView.findViewById(R.id.item_controler_behavior_listview_stop);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mBehaviorName.setText(mBehaviorName.get(position));
		holder.mPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRobotActivity.runBehavior(mBehaviorName.get(position));
			}
		});
		holder.mStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRobotActivity.stopBehavior(mBehaviorName.get(position));
			}
		});
		return convertView;
	}

	class ViewHolder {
		public TextView mBehaviorName;
		public TextView mPlay;
		public TextView mStop;
	}
}
