package seop.gyun.recipedia.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.RecipediaDBHelper;
import seop.gyun.recipedia.grid.GridItemData;

public class RecipeSecondFragment extends Fragment {
	
	private ArrayList<ProgressData> mProgressData;
	private GridItemData mData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = getArguments().getParcelable("gridlist");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_recipe_second, container, false);
		
		Cursor cursor = RecipediaDBHelper.getDatabase(getActivity())
				.rawQuery("SELECT * FROM progress WHERE PROGRESS_RECIPE_ID = " + mData.recipeId + "", null); // 과정정보
		
		if (cursor.moveToFirst()) { // progress Table
			mProgressData = new ArrayList<ProgressData>();
			int i = 0;
			do {
				ProgressData d = new ProgressData();
				d.PRG_ID = cursor.getString(0);
				d.PRG_NO = Integer.parseInt(cursor.getString(1));
				d.PRG_Detail = cursor.getString(2);
				d.PRG_ImageURL = cursor.getString(3);
				d.PRG_Tip = cursor.getString(4);
				mProgressData.add(d);
				i++;
			} while (cursor.moveToNext());
		}
		
		Collections.sort(mProgressData, new Comparator<ProgressData>() {

			@Override
			public int compare(ProgressData lhs, ProgressData rhs) {
				return (lhs.PRG_NO < rhs.PRG_NO)?-1:(lhs.PRG_NO > rhs.PRG_NO)?1:0;
			}
			
		});
		
		final LinearLayout list = (LinearLayout) v.findViewById(R.id.recipe_progress_container);
		
		for (ProgressData data : mProgressData) {
			LinearLayout item = (LinearLayout) inflater.inflate(R.layout.view_progress_item, null);
			if (!data.PRG_ImageURL.equalsIgnoreCase("null")) {
				SimpleDraweeView imgView = (SimpleDraweeView) inflater.inflate(R.layout.view_progress_img, null);
				Uri uri = Uri.parse(data.PRG_ImageURL);
				imgView.setImageURI(uri);
				item.addView(imgView);
			}
			if (!data.PRG_Detail.equalsIgnoreCase("null")) {
				TextView detailText = new TextView(getContext());
				detailText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				detailText.setText(data.PRG_Detail);
				item.addView(detailText);
			}
			if (!data.PRG_Tip.equalsIgnoreCase("null")) {
				TextView tipText = new TextView(getContext());
				tipText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				tipText.setText(data.PRG_Tip);
				item.addView(tipText);
			}
			list.addView(item);
		}
		
		return v;
	}

}
