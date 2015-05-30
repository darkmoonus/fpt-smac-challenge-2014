package uet.invincible.adapters;

import java.util.ArrayList;
import java.util.List;

import uet.invincible.activities.RobotMainActivity;
import uet.invincible.customize.MyAnimations;
import uet.invincible.fragments.AssistantExamFragment;
import uet.invincible.fragments.AssistantRollupFragment;
import uet.invincible.listeners.TwoChosenDialogListener;
import uet.invincible.mobile.R;
import uet.invincible.models.StudentModel;
import uet.invincible.savedata.Saved;
import uet.invincible.utilities.Mail;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fpt.api.cypher.CypherRequest;
import com.fpt.api.cypher.CypherResult;

@SuppressLint("InflateParams")
public class StudentListAdapter extends ArrayAdapter<StudentModel> {
	public List<StudentModel> mStudentModel = new ArrayList<StudentModel>();
	public RobotMainActivity mRobotActivity;
	public Context mContext;
	public LayoutInflater mInflate;
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
	public void remove(int id) {
		this.mStudentModel.remove(id);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
	            convertView = new View(mContext);
	            convertView = mInflate.inflate(R.layout.item_row_student_list, null);
	            holder.mEmail = (EditText)convertView.findViewById(R.id.item_row_student_list_email);
	            holder.mMobile = (EditText)convertView.findViewById(R.id.item_row_student_list_mobile);
	            holder.mAddress = (EditText)convertView.findViewById(R.id.item_row_student_list_address);
	            holder.mChuyenCan = (EditText)convertView.findViewById(R.id.item_row_student_list_update_chuyencan);
	            holder.mGiuaKi = (EditText)convertView.findViewById(R.id.item_row_student_list_update_giuaki);
	            holder.mCuoiKi = (EditText)convertView.findViewById(R.id.item_row_student_list_send_cuoiki);
	            holder.mTongKet = (EditText)convertView.findViewById(R.id.item_row_student_list_tongket);
	            holder.mUpdate = (TextView)convertView.findViewById(R.id.item_row_student_list_update_info);
	            holder.mDelete = (TextView)convertView.findViewById(R.id.item_row_student_list_delete);
	            holder.mSendEmail = (TextView)convertView.findViewById(R.id.item_row_Student_list_send_email);
	            holder.mHiddenInfo = (LinearLayout)convertView.findViewById(R.id.item_row_student_list_hidden_info);
	            holder.mStudentLayout = (LinearLayout)convertView.findViewById(R.id.student_list_layout);
	            holder.mLine = (TextView)convertView.findViewById(R.id.item_student_list_line);
	            holder.mStudentList = (LinearLayout)convertView.findViewById(R.id.item_student_list);
	            holder.mRow = (TextView)convertView.findViewById(R.id.arrow);
	            holder.mStudentName = (TextView)convertView.findViewById(R.id.item_row_student_name);
	            holder.mStudentInfo = (TextView)convertView.findViewById(R.id.item_row_student_info);
	            holder.mStudentAvatar = (TextView)convertView.findViewById(R.id.item_row_student_avatar);
	            convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final ViewHolder holderFinal = holder;
            holderFinal.mDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mRobotActivity.showTwoChosenDialog("", "Are you sure to delete ?", "OK", "Cancel", new TwoChosenDialogListener() {
						@Override
						public void onRightClick(Dialog dialog) {
							dialog.dismiss();
						}
						@Override
						public void onLeftClick(Dialog dialog) {
							holderFinal.mStudentList.setVisibility(View.GONE);
							dialog.dismiss();
							new Thread(new Runnable() {
								@Override
								public void run() {
									mRobotActivity.showProgressDialogFromUIThread("Deleting ...");
									String cypher = "match m where m.label='student' and m.uId=\'" 
											+ Saved.studentList.get(position).uId + "\' delete m";
									final CypherResult result = CypherRequest.makeRequest(mRobotActivity.accessToken, cypher);
									if(result == null) {
										mRobotActivity.showToastFromUIThread("Result null");
										mRobotActivity.removePreviousDialog();
									} else {
										if(!result.status.equals("success")) {
											mRobotActivity.showToastFromUIThread("Request failed");
											mRobotActivity.removePreviousDialog();
										}
										else {
											mRobotActivity.removePreviousDialog();
											mRobotActivity.showToastFromUIThread("Delete " + mStudentModel.get(position).name + " successful !");
											Saved.studentList.remove(position);
											mStudentModel.remove(position);
											mRobotActivity.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													AssistantExamFragment.initStudentListView();
													AssistantRollupFragment.initStudentListView();
												}
											});
										}
									}
								}
							}).start();
						}
					});
				}
			});
            holderFinal.mUpdate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mRobotActivity.showTwoChosenDialog("", "Are you sure to update ?", "OK", "Cancel", new TwoChosenDialogListener() {
						@Override
						public void onRightClick(Dialog dialog) {
							dialog.dismiss();
						}
						@Override
						public void onLeftClick(Dialog dialog) {
							dialog.dismiss();
							new Thread(new Runnable() {
								@Override
								public void run() {
									mRobotActivity.showProgressDialogFromUIThread("Updating ...");
									String cypher = "match m where m.label='student' and m.uId=\'" 
											+ Saved.studentList.get(position).uId + 
											"\' set m.email = \'" + holderFinal.mEmail.getText().toString() + "\'," + 
											"m.mobilePhone = \'" + holderFinal.mMobile.getText().toString() + "\'," + 
											"m.address = \'" + holderFinal.mAddress.getText().toString() + "\'," + 
											"m.chuyenCan = \'" + holderFinal.mChuyenCan.getText().toString() + "\'," + 
											"m.giuaKi = \'" + holderFinal.mGiuaKi.getText().toString() + "\'," + 
											"m.cuoiKi = \'" + holderFinal.mCuoiKi.getText().toString() + "\'," +
											"m.tongKet = \'" + holderFinal.mTongKet.getText().toString() + "\'";
									final CypherResult result = CypherRequest.makeRequest(mRobotActivity.accessToken, cypher);
									if(result == null) {
										mRobotActivity.showToastFromUIThread("Result null");
									} else {
										if(!result.status.equals("success")) mRobotActivity.showToastFromUIThread("Request failed");
										else mRobotActivity.showToastFromUIThread("Update " + mStudentModel.get(position).name + " information successful !");
										mStudentModel.get(position).email = holderFinal.mEmail.getText().toString();
										mStudentModel.get(position).mobilePhone = holderFinal.mMobile.getText().toString();
										mStudentModel.get(position).address = holderFinal.mAddress.getText().toString();
										mStudentModel.get(position).chuyenCan = holderFinal.mChuyenCan.getText().toString();
										mStudentModel.get(position).giuaKi = holderFinal.mGiuaKi.getText().toString();
										mStudentModel.get(position).cuoiKi = holderFinal.mCuoiKi.getText().toString();
										mStudentModel.get(position).tongKet = holderFinal.mTongKet.getText().toString();
										Saved.studentList.get(position).email = holderFinal.mEmail.getText().toString();
										Saved.studentList.get(position).mobilePhone = holderFinal.mMobile.getText().toString();
										Saved.studentList.get(position).address = holderFinal.mAddress.getText().toString();
										Saved.studentList.get(position).chuyenCan = holderFinal.mChuyenCan.getText().toString();
										Saved.studentList.get(position).giuaKi = holderFinal.mGiuaKi.getText().toString();
										Saved.studentList.get(position).cuoiKi = holderFinal.mCuoiKi.getText().toString();
										Saved.studentList.get(position).tongKet = holderFinal.mTongKet.getText().toString();
										AssistantExamFragment.initStudentListView();
										AssistantRollupFragment.initStudentListView();
										mRobotActivity.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												holderFinal.isFullInfo = false;
												holderFinal.mRow.setBackgroundResource(R.drawable.arrow);
												holderFinal.mHiddenInfo.setVisibility(View.GONE);
												holderFinal.mLine.setVisibility(View.VISIBLE);
											}
										});
									}
									mRobotActivity.removePreviousDialog();
								}
							}).start();
						}
					});
				}
			});
            holderFinal.mSendEmail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mRobotActivity.showTwoChosenDialog("", "Are you sure to send email ?", "OK", "Cancel", new TwoChosenDialogListener() {
						@Override
						public void onRightClick(Dialog dialog) {
							dialog.dismiss();
						}
						@Override
						public void onLeftClick(Dialog dialog) {
							dialog.dismiss();
							new Thread(new Runnable() {
								@Override
								public void run() {
									mRobotActivity.showProgressDialog("Sending email...");
									double sum = Double.parseDouble(mStudentModel.get(position).chuyenCan) * 0.1
											+ Double.parseDouble(mStudentModel.get(position).giuaKi) * 0.2
											+ Double.parseDouble(mStudentModel.get(position).cuoiKi) * 0.3
											+ Double.parseDouble(mStudentModel.get(position).tongKet) * 0.4;
									String content = "Kết quả học tập của bạn như sau: \n + Điểm chuyên cần: " + mStudentModel.get(position).chuyenCan + "\n" +
									" + Điểm bài tập: " + mStudentModel.get(position).giuaKi + "\n" +
									" + Điểm giữa kì: " + mStudentModel.get(position).cuoiKi + "\n" +
									" + Điểm cuối kì: " + mStudentModel.get(position).tongKet + "\n" +
									" + Tổng kết: " + sum + "\n" +
									" - ĐÁNH GIÁ: ";
									if(sum < 6) content += "Học lực trung bình, cần phải cố gắng rất nhiều."; else
									if(sum < 8) content += "Học lực khá. Cần cố gắng hơn để đạt điểm cao."; else
									if(sum < 9) content += "Học lực giỏi. Cố gắng phát huy nhé"; else
									content += "Học lực xuất sắc, chúc mừng bạn !";
									
									String[] list = new String[1];
									list[0] = mStudentModel.get(position).email;
									Mail m = new Mail("bottonouet@gmail.com", "tu1denchin");
									m.setTo(list);
									m.setFrom("bottonouet@gmail.com");
									m.setSubject("Thông báo kết quả học tập");
									m.setBody(content);
									try {
										if(m.send()) {
											mRobotActivity.showToastFromUIThread("Send email to " + mStudentModel.get(position).name + " successful !");
										} else {
											mRobotActivity.showToastFromUIThread("Send email to " + mStudentModel.get(position).name + " failed !");
										}
									} catch(Exception e) {
										mRobotActivity.showToastFromUIThread("Exeption send email to " + mStudentModel.get(position).name + " failed !");
										mRobotActivity.showToastFromUIThread("Exeption: " + e.getMessage());
										e.printStackTrace();
									}
									mRobotActivity.removePreviousDialog();
								}
							}).start();
						}
					});
				}
			});
            holderFinal.mStudentLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!holderFinal.isFullInfo) {
						holderFinal.isFullInfo = true;
						holderFinal.mHiddenInfo.setVisibility(View.VISIBLE);
						holderFinal.mHiddenInfo.startAnimation(MyAnimations.fromLeft(200, 500));
						holderFinal.mLine.setVisibility(View.GONE);
						holderFinal.mRow.setBackgroundResource(R.drawable.arrow_down);
					} else {
						holderFinal.isFullInfo = false;
						holderFinal.mHiddenInfo.setVisibility(View.GONE);
						holderFinal.mLine.setVisibility(View.VISIBLE);
						holderFinal.mRow.setBackgroundResource(R.drawable.arrow);
					}
				}
			});
			holder.mStudentName.setText(mStudentModel.get(position).name);
			holder.mStudentInfo.setText("Mã sinh viên: " + mStudentModel.get(position).studentCode + ", " + mStudentModel.get(position).classs);
			holder.mEmail.setText(mStudentModel.get(position).email);
			holder.mAddress.setText(mStudentModel.get(position).address);
			holder.mMobile.setText(mStudentModel.get(position).mobilePhone);
			holder.mChuyenCan.setText("" + mStudentModel.get(position).chuyenCan);
			holder.mGiuaKi.setText("" + mStudentModel.get(position).giuaKi);
			holder.mCuoiKi.setText("" + mStudentModel.get(position).cuoiKi);
			holder.mTongKet.setText("" + mStudentModel.get(position).tongKet);
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

	public class ViewHolder {
		public boolean isFullInfo = false;
		public TextView mStudentName;
		public TextView mStudentInfo;
		public TextView mStudentAvatar;
		public LinearLayout mHiddenInfo;
		public LinearLayout mStudentLayout;
		public LinearLayout mStudentList;
		public EditText mEmail;
		public EditText mMobile;
		public EditText mAddress;
		public EditText mChuyenCan;
		public EditText mGiuaKi;
		public EditText mCuoiKi;
		public EditText mTongKet;
		public TextView mUpdate;
		public TextView mSendEmail;
		public TextView mDelete;
		public TextView mLine;
		public TextView mRow;
	}
}
