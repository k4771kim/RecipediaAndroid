package seop.gyun.recipedia.survey.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import seop.gyun.recipedia.R;

public class SurveyResultListItem extends LinearLayout {

	public SurveyResultListItem(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		// TODO Auto-generated method stub
		LayoutInflater.from(getContext()).inflate(R.layout.list_survey_result_item, this);

	}

	SurveyResultListItemData mData;

	public void setItemData(SurveyResultListItemData data) {

		mData = data;

	}
}
