package seop.gyun.recipedia.grid;

import java.util.ArrayList;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import seop.gyun.recipedia.ImageLoaderManager;
import seop.gyun.recipedia.R;

public class GridAdapter extends BaseAdapter {
	
	private static final String LOG_TAG = "GridAdapter";
	
	private ArrayList<GridItemData> mData = null;
	private Context mContext;
	private LayoutInflater inflater = null;
	private float aspectRatio;

	public GridAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
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
		// TODO Auto-generated method stub
//		View itemView = convertView;
//		ViewHolder viewHolder = null;
//		
		Uri uri = Uri.parse(mData.get(position).imgUrl);
		Log.wtf(LOG_TAG, mData.get(position).imgUrl);

		View itemView = inflater.inflate(R.layout.grid_item, null);
		TextView gridTxt = (TextView) itemView.findViewById(R.id.grid_item_name);
		ImageView gridImg = (ImageView) itemView.findViewById(R.id.grid_item_img);
		gridTxt.setText(mData.get(position).recipeName);
		
		ImageLoader imageLoader = ImageLoaderManager.getImageLoaderManager();
		ImageAware imageAware = new ImageViewAware(gridImg, false);
		imageLoader.displayImage(uri.toString(), imageAware, ImageLoaderManager.getDisplayImageOptions());
		
		
//		if (itemView == null) {
//			itemView = inflater.inflate(R.layout.grid_item, null);
//			
//			viewHolder = new ViewHolder();
//			viewHolder.gridTxt = (TextView) itemView.findViewById(R.id.grid_item_name);
//			viewHolder.gridImg = (SimpleDraweeView) itemView.findViewById(R.id.grid_item_img);
//			
//			itemView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) itemView.getTag();
//		}
//		
//		viewHolder.gridTxt.setText(mData.get(position).recipeName);
//		viewHolder.gridImg.setImageURI(uri);
		
//		GridFragmentItem v = new GridFragmentItem(mContext);
//		v.setItemData(mData.get(position));

		return itemView;
	}

//	public void add(GridFragmentItemData item) {
//		items.add(item);
//	}
	
	public void setItem(ArrayList<GridItemData> itemArray) {
		mData = itemArray;
	}
	
	private class ViewHolder {
		TextView gridTxt;
		SimpleDraweeView gridImg;
	}
}
