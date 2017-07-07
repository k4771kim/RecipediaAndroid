package seop.gyun.recipedia.recipe;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.RecipediaContainerActivity;
import seop.gyun.recipedia.grid.GridItemData;

public class RecipeFirstFragment extends Fragment {

	private GridItemData mData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = getArguments().getParcelable("gridlist");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_recipe_first, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		StringBuilder sb = new StringBuilder();
		sb.append("현위치 : ").append(RecipediaContainerActivity.mAddressOutput)
			.append("\nRECIPE_ID : ").append(mData.recipeId).append("\nRECIPE_NM_KO : ").append(mData.recipeName)
			.append("\nSUMRY : ").append(mData.summary).append("\nNATION_NM : ").append(mData.nationName)
			.append("\nTY_NM : ").append(mData.typeName).append("\nCOOKING_TIME : ").append(mData.cookingTime)
			.append("\nCALORIE : ").append(mData.calorie).append("\nLEVEL_NM : ").append(mData.levelName)
			.append("\nPC_NM : ").append(mData.price).append("\nCOUNT_REPLY : ").append(String.valueOf(mData.countReply))
			.append("\nCOUNT_LIKE : ").append(String.valueOf(mData.countLike)).append("\nCOUNT_REQUEST : ")
			.append(String.valueOf(mData.countRequest));
		
		final TextView tv = (TextView) getView().findViewById(R.id.recipe_info_text); 
		tv.setText(sb.toString());	
	}

}
