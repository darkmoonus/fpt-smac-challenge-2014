package uet.invincible.adapters;

import java.util.ArrayList;
import java.util.List;

import uet.invincible.R;
import uet.invincible.activities.RobotMainActivity;
import uet.invincible.models.StudentModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class StudentListAdapter extends ArrayAdapter<StudentModel> {
	public List<StudentModel> mStudentModel = new ArrayList<StudentModel>();
	public RobotMainActivity mRobotActivity;
	public Context mContext;
	public LayoutInflater mInflate;
	public ArrayList<ViewHolder> mHolderList = new ArrayList<ViewHolder>();
	public StudentListAdapter(RobotMainActivity mActivity, Context context, int textViewResourceId, List<StudentModel> mStudentModel) {
		super(context, textViewResourceId, mStudentModel);
		mRobotActivity = mActivity;
		mContext = context;
		mInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (StudentModel studentModel : mStudentModel) {
			this.mStudentModel.add(studentModel);
		}
	}
	
	public void removeAll() {
		mStudentModel = new ArrayList<StudentModel>();
		notifyDataSetChanged();
	}
	public void add(StudentModel student) {
		this.mStudentModel.add(student);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
	            convertView = new View(mContext);
	            convertView = mInflate.inflate(R.layout.item_row_student_list, null);
	            holder.mStudentName = (TextView)convertView.findViewById(R.id.item_row_student_name);
	            holder.mStudentInfo = (TextView)convertView.findViewById(R.id.item_row_student_info);
	            holder.mStudentAvatar = (TextView)convertView.findViewById(R.id.item_row_student_avatar);
	            convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mStudentName.setText(mStudentModel.get(position).name);
			holder.mStudentInfo.setText(mStudentModel.get(position).address);
			switch (position % 20) {
				case 0: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_0); break;
				case 1: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_1); break;
				case 2: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_2);break;
				case 3: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_3); break;
				case 4: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_4); break;
				case 5: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_5); break;
				case 6: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_6); break;
				case 7: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_7); break;
				case 8: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_8); break;
				case 9: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_9); break;
				case 10: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_10); break;
				case 11: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_11); break;
				case 12: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_12); break;
				case 13: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_13); break;
				case 14: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_14); break;
				case 15: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_15); break;
				case 16: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_16); break;
				case 17: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_17); break;
				case 18: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_18); break;
				case 19: holder.mStudentAvatar.setBackgroundResource(R.drawable.avatar_19); break;
				default: break;
		}
		return convertView;
	}

	class ViewHolder {
		public TextView mStudentName;
		public TextView mStudentInfo;
		public TextView mStudentAvatar;
	}
}
