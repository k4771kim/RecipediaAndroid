package seop.gyun.recipedia.column;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import seop.gyun.recipedia.QueryBuilder;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.RecipediaConstant;

public class ColumnActivity extends AppCompatActivity {

	String columnId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_column);
		
		columnId = getIntent().getStringExtra("column_id");
		QueryBuilder query = new QueryBuilder("columnid", columnId, false);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		WebView wv = (WebView) findViewById(R.id.column_webview);
		//wv.loadUrl("file:///android_asset/column_01.html");
		wv.loadUrl(RecipediaConstant.SERVER_COLUMN + query);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}
	
}
