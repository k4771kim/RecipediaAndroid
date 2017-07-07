package seop.gyun.recipedia.survey;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import seop.gyun.recipedia.R;

public class SurveyActivity extends AppCompatActivity {
	ViewPager surveyViewPager;
	SurveyAdapter mSurveyAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		surveyViewPager = (ViewPager) findViewById(R.id.survey_pager);
		mSurveyAdapter = new SurveyAdapter(getSupportFragmentManager());
		surveyViewPager.setAdapter(mSurveyAdapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}
	
}
