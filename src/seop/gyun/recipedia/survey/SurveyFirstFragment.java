package seop.gyun.recipedia.survey;

import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import seop.gyun.recipedia.R;

public class SurveyFirstFragment extends Fragment {
	private SeekArc mSeekArc;
	private TextView difficulty;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.fragment_survey_first, container, false);
		
		mSeekArc = (SeekArc) v.findViewById(R.id.seekArc);
		mSeekArc.setArcWidth(20);
		mSeekArc.setProgressWidth(30);
		difficulty = (TextView)v.findViewById(R.id.difficulty);
		mSeekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekArc seekArc) {}
			
			@Override
			public void onStartTrackingTouch(SeekArc seekArc) {}
			
			@Override
			public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if (progress <=90) {
					difficulty.setText("쉬움");
					mSeekArc.setColor(0xff66ccff, 0xffffcc99);
				}
				else if (progress <= 180) {
					difficulty.setText("보통");
					mSeekArc.setColor(0xffcc66ff, 0xffcc99ff);
				}
				else if (progress <= 270) {
					difficulty.setText("어려움");
					mSeekArc.setColor(0xffff66ff, 0xff99ffcc);
				}
			}
		});
		
		
		return v;
	}

}
