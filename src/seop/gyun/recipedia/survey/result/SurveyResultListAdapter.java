package seop.gyun.recipedia.survey.result;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SurveyResultListAdapter extends BaseAdapter{
	ArrayList<SurveyResultListItemData> items = new ArrayList<SurveyResultListItemData>();
	Context mContext;
	
	public SurveyResultListAdapter(Context context) {
		// TODO Auto-generated constructor stub

		mContext = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SurveyResultListItem v = new SurveyResultListItem(mContext);
		v.setItemData(items.get(position));

		return v;
	}
	public void add(SurveyResultListItemData item) {
		items.add(item);
	}
}
