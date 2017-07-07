package seop.gyun.recipedia.recipe;

import java.util.ArrayList;

import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import seop.gyun.recipedia.R;

public class ReplyAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<ReplyData> mData;
	LayoutInflater inflater;

	public ReplyAdapter(Context context, ArrayList<ReplyData> data) {
		mContext = context;
		mData = data;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public ReplyData getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		ViewHolder viewHolder = null;

		Uri uri = Uri.parse(mData.get(position).profileImgUrl);

		if (itemView == null) {
			itemView = inflater.inflate(R.layout.list_reply_item, null);

			viewHolder = new ViewHolder();
			viewHolder.img = (SimpleDraweeView) itemView.findViewById(R.id.profile_img);
			viewHolder.date = (TextView) itemView.findViewById(R.id.reply_date_text);
			viewHolder.content = (TextView) itemView
					.findViewById(R.id.reply_content_text);

			itemView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) itemView.getTag();
		}
		
		String date = new ReplyDateFormat(mData.get(position).replyDate).create();
		
		viewHolder.img.setImageURI(uri);
		viewHolder.date.setText(date);
		viewHolder.content.setText(mData.get(position).replyContent);
		
		return itemView;
	}

	private class ViewHolder {
		SimpleDraweeView img;
		TextView date, content;
	}

	public void add(ReplyData item) {
		mData.add(item);
		notifyDataSetChanged();
	}

}
