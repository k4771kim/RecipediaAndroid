package seop.gyun.recipedia.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import seop.gyun.recipedia.grid.GridItemData;

public class RecipePagerAdapter extends FragmentPagerAdapter {

	GridItemData mData;
	
	public RecipePagerAdapter(FragmentManager fm, GridItemData mData) {
		super(fm);
		this.mData = mData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment f;
		if (position == 0) {
			f = new RecipeFirstFragment();
		} else {
			f = new RecipeSecondFragment();
		}
		
		Bundle bundle = new Bundle();
		bundle.putParcelable("gridlist", mData);
		f.setArguments(bundle);
		
		return f;
	}

	

}
