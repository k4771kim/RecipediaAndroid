package seop.gyun.recipedia.recipe;

import java.util.ArrayList;

import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import seop.gyun.recipedia.R;

public class ProgressAdapter extends ArrayAdapter<ProgressData> {
	Context mContext;
	ArrayList<ProgressData> mData;
	LayoutInflater inflater;

	public ProgressAdapter(Context context, int resource,
			ArrayList<ProgressData> data) {
		super(context, resource, data);
		mContext = context;
		mData = data;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		ViewHolder viewHolder = null;

		Uri uri = Uri.parse(mData.get(position).PRG_ImageURL);
//		
//		if (itemView == null) {
//		itemView = inflater.inflate(R.layout.grid_item, null);
//		
//		viewHolder = new ViewHolder();
//		viewHolder.img = (SimpleDraweeView) itemView.findViewById(R.id.profile_img);
//		viewHolder.date = (TextView) itemView.findViewById(R.id.reply_date_text);
//		viewHolder.content = (TextView) itemView.findViewById(R.id.reply_content_text);
//		
//		itemView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) itemView.getTag();
//		}
//	
//		viewHolder.img.setImageURI(uri);
//		viewHolder.date.setText(mData.get(position).replyDate);
//		viewHolder.content.setText(mData.get(position).replyContent);
//		
		return itemView;
	}





	private class ViewHolder {
//		SimpleDraweeView img;
//		TextView date, content;
	}
	
	
}
