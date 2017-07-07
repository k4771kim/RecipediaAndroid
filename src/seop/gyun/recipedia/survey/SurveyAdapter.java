package seop.gyun.recipedia.survey;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SurveyAdapter extends FragmentPagerAdapter {

	public SurveyAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Fragment f = null;
		if (arg0 == 0) {
			f = new SurveyFirstFragment();
		} else if (arg0 == 1) {
			f = new SurveySecondFragment();
		} else if (arg0 == 2) {
			f = new SurveyThirdFragment();
		} else if (arg0 == 3) {
			f = new SurveyForthFragment();
		} else if (arg0 == 4) {
			f = new SurveyFifthFragment();
		} else if (arg0 == 5) {
			f = new SurveySixthFragment();
		}
		return f;
	}
	
	

}
