package seop.gyun.recipedia;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.TextView;
import seop.gyun.recipedia.R;


public class SearchCursorAdapter extends CursorAdapter {
	
	public SearchCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.search_item, parent, false);
		TextView t = (TextView)v.findViewById(R.id.search_text);
		t.setText(cursor.getString(1));
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
		// TODO Auto-generated method stub
		super.setFilterQueryProvider(filterQueryProvider);
	}
	
	
	
}
