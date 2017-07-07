package seop.gyun.recipedia.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.survey.result.SurveyResultActivity;

public class SurveySixthFragment extends Fragment {

	Button surveyCommitBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.fragment_survey_sixth, container, false);
		surveyCommitBtn = (Button) v.findViewById(R.id.survey_commit_btn);
		surveyCommitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SurveyResultActivity.class);
				startActivity(intent);
			}
		});
		return v;
	}

}
