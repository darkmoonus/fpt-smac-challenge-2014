package uet.invincible.assistant.adapters;


import java.util.ArrayList;
import java.util.List;

import uet.invincible.assistant.R;
import uet.invincible.assistant.listeners.ChattingBottonoListener;
import uet.invincible.assistant.models.MessageModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class BottonoChatAdapter extends ArrayAdapter<MessageModel> {
	public List<MessageModel> mMessageModels = new ArrayList<MessageModel>();
	public Context mContext;
	public ChattingBottonoListener mListener;
	public ArrayList<ViewHolder> mHolderList = new ArrayList<ViewHolder>();
	public BottonoChatAdapter(Context context, int textViewResourceId, List<MessageModel> objects, ChattingBottonoListener listener) {
		super(context, textViewResourceId, objects);
		mContext = context;
		mInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mListener = listener;
		for (MessageModel MessageModel : objects) {
			mMessageModels.add(MessageModel);
		}
	}

	@Override
	public MessageModel getItem(int position) {
		return mMessageModels.get(position);
	}
	
	public void addItem(MessageModel model) {
         this.mMessageModels.add(model);
         notifyDataSetChanged();
     }
	
	
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return mMessageModels.get(position).tag;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}


	public LayoutInflater mInflate;
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		final MessageModel model = getItem(position);
		final int type = getItemViewType(position);
		
		if (convertView == null) {
			holder = new ViewHolder();
            switch (type) {
                case 1:
                    convertView = new View(mContext);
                    convertView = mInflate.inflate(R.layout.item_row_bottono_chat, null);
                    holder.mMessage = (TextView)convertView.findViewById(R.id.item_row_bottono_chat_message);
                    holder.mTime = (TextView)convertView.findViewById(R.id.item_row_bottono_chat_time);
                    break;
                case 0:
                	 convertView = new View(mContext);
                	 convertView = mInflate.inflate(R.layout.item_row_human_chat, null);
                     holder.mMessage = (TextView)convertView.findViewById(R.id.item_row_human_chat_message);
                     holder.mTime = (TextView)convertView.findViewById(R.id.item_row_human_chat_time);
                    break;
            }
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.mMessage.setText(model.mMessage);
		holder.mTime.setText(model.mTime);
		
		return convertView;
	}

	class ViewHolder {
		public TextView mMessage;
		public TextView mTime;
	}
}
