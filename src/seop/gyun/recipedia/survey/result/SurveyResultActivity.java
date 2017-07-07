package seop.gyun.recipedia.survey.result;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.recipe.RecipeActivity;

public class SurveyResultActivity extends Activity {
	ListView surveyResultListView;
	ImageButton surveyBackBtn;
	SurveyResultListAdapter mSurveyResultListAdpater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey_result);
		surveyBackBtn = (ImageButton) findViewById(R.id.survey_result_backbtn);
		surveyBackBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();

			}
		});
		surveyResultListView = (ListView) findViewById(R.id.survey_result_listview);
		mSurveyResultListAdpater = new SurveyResultListAdapter(getApplicationContext());
		surveyResultListView.setAdapter(mSurveyResultListAdpater);
		//////////////////////////////////////////////////////////////////
		SurveyResultListItemData d = new SurveyResultListItemData();
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		mSurveyResultListAdpater.add(d);
		//////////////////////////////////////////////////////////////////
		surveyResultListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);

				startActivity(intent);

				Toast.makeText(getApplicationContext(), position + "번째 아이템 클릭", Toast.LENGTH_SHORT).show();


			}
		});

	}

}
