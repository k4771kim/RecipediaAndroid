package seop.gyun.recipedia.grid;

import java.util.ArrayList;

import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.column.ColumnActivity;

public class GridHeaderAdapter extends PagerAdapter {
	
	private static final String LOG_TAG = "GridHeaderAdapter";

	private Context mContext;
	private ArrayList<GridItemData> mData;
	private LayoutInflater inflater;
	
	public GridHeaderAdapter(Context context, ArrayList<GridItemData> mData) {
		super();
		this.mContext = context;
		this.mData = mData;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Uri uri = Uri.parse(mData.get(position).imgUrl);
		// Log.d(LOG_TAG, mData.get(position).imgUrl);
		View v = inflater.inflate(R.layout.grid_header_item, null);
		SimpleDraweeView draweeView = (SimpleDraweeView) v.findViewById(R.id.grid_header_item_img);
		draweeView.setImageURI(uri);
		container.addView(draweeView, position);
		
		final String columnId = mData.get(position).recipeId;
		draweeView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ColumnActivity.class);
				intent.putExtra("column_id", columnId);
				Toast.makeText(mContext, "column_id : " + columnId, Toast.LENGTH_SHORT).show();
				mContext.startActivity(intent);
			}
		});
		
		return draweeView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView((View)object);
	}
	

}
