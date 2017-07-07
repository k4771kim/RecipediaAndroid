package seop.gyun.recipedia.grid;

import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import seop.gyun.recipedia.R;

public class GridFragmentItem extends LinearLayout {
	public GridFragmentItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	public GridFragmentItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	// ImageView gridPic;
	TextView gridTxt;
	SimpleDraweeView gridImg;

	private void init() {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, this);

		//gridPic = (ImageView) findViewById(R.id.grid_item_pic);
		gridTxt = (TextView) v.findViewById(R.id.grid_item_name);
		gridImg = (SimpleDraweeView) v.findViewById(R.id.grid_item_img);

	}

	GridItemData mData;

	public void setItemData(GridItemData data) {

		mData = data;

		//gridTxt.setText(data.name);
		gridTxt.setText(data.recipeName);	
		
		// ImageLoader.getInstance().displayImage(data.imgUrl, gridPic, ImageLoaderManager.getDisplayImageOptions());
		Uri uri = Uri.parse(data.imgUrl);
		gridImg.setImageURI(uri);
		//gridImg.setAspectRatio(1.33f);

	}

}
