package seop.gyun.recipedia.grid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import com.etsy.android.grid.StaggeredGridView;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.RecipediaConstant;
import seop.gyun.recipedia.recipe.RecipeActivity;

public class GridFragment extends Fragment {
	
	private static final String LOG_TAG = "GridFragment";
	
	StaggeredGridView recipeGridView;
	GridAdapter mGridAdapter;
	GridHeaderAdapter mGridHeaderAdapter;
	// ArrayList<GridItemData> itemDataArray = null;
	ArrayList<GridItemData> columnArray;
	public static ArrayList<GridItemData> gridArray;
	
	ViewPager headerPager;
	CirclePageIndicator indicator;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_grid, container, false);
	
		View header = inflater.inflate(R.layout.grid_header, null);
		
		
		
		headerPager = (ViewPager) header.findViewById(R.id.grid_header_pager);
		
		indicator = (CirclePageIndicator) header.findViewById(R.id.grid_header_indicator);
		
		final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        indicator.setPageColor(0x00FFFFFF);
        indicator.setFillColor(0xDDFFFFFF);
        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWidth(1 * density);
		
		recipeGridView = (StaggeredGridView) v.findViewById(R.id.grid_view);
		recipeGridView.addHeaderView(header);
		recipeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), RecipeActivity.class);
				//intent.putExtra("columnlist", columnArray.get(position));
				intent.putExtra("gridlist", gridArray.get(position-1));
				startActivity(intent);
			}

		});
		
		
		
		
		
//		recipeGridView = (StaggeredGridView) v.findViewById(R.id.container_pager_grid);
//		recipeGridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(getActivity(), RecipeActivity.class);
//
//				String[] a = { RecipeItemList.get(position).RecipeID, RecipeItemList.get(position).name,
//						RecipeItemList.get(position).Summary, RecipeItemList.get(position).Nation,
//						RecipeItemList.get(position).Type, RecipeItemList.get(position).CookingTime,
//						RecipeItemList.get(position).Calorie, RecipeItemList.get(position).Level,
//						RecipeItemList.get(position).Price, RecipeItemList.get(position).imgUrl };
//				intent.putExtra("GridItemData", a);
//				startActivity(intent);
//
//				Toast.makeText(getActivity(),
//						position + "번째 아이템 클릭\n현재위치 : " + RecipediaContainerActivity.mAddressOutput, Toast.LENGTH_SHORT)
//						.show();
//			}
//		});

//		int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);
//		recipeGridView.setItemMargin(margin);
//		recipeGridView.setPadding(margin, 0, margin, 0);
//
//		recipeGridView.setHeaderView(new Button(getContext()));
//        View footerView;
//        LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        footerView = inflater.inflate(R.layout.layout_loading_footer, null);
//        recipeGridView.setFooterView(footerView);
//		

		
	


		
//		///////////////////////////////////////////////////// .
//		Cursor cursor = RecipediaDBHelper.getDatabase(mContext).rawQuery("SELECT * FROM recipe", null);
//
//		// Cursor cursor2 =
//		// RecipediaDBHelper.getDatabase(mContext).rawQuery("SELECT RECIPE_NM_KO
//		// FROM recipe",null);
//		// String[] Recipe_Name;
//
//		int i = 0;
//		if (cursor.moveToFirst()) {
//			RecipeItemList = new ArrayList<GridFragmentItemData>();
//			do {
//				GridFragmentItemData d = new GridFragmentItemData();
//				d.RecipeID = cursor.getString(0);
//				d.name = cursor.getString(1);
//				d.Summary = cursor.getString(2);
//				d.Nation = cursor.getString(4);
//				d.Type = cursor.getString(6);
//				d.CookingTime = cursor.getString(7);
//				d.Calorie = cursor.getString(8);
//				d.Level = cursor.getString(10);
//				d.Price = cursor.getString(12);
//				d.imgUrl = cursor.getString(13);
//				RecipeItemList.add(d);
//
//				i++;
//			} while (cursor.moveToNext());
//		} else {
//		}
//		cursor.close();
//		/////////////////////////////////////////////////////
//		GridFragmentItemData ItemData = null;
//		for (int j = 0; j < RecipeItemList.size(); j++) {
//			ItemData = new GridFragmentItemData();
//			ItemData = RecipeItemList.get(j);
//			mGridAdapter.add(ItemData);
//		}
//		mGridAdapter.notifyDataSetChanged();
//		//////////////////////////////////////////////////////
		
		
		new HttpAsyncTask().execute(RecipediaConstant.SERVER_GRID, "GET");
		
		
		
		return v;

	}
	
	private boolean isResultSuccess(JsonParser jp) {
		
		boolean flag = false;
		
		try {
			if (jp.nextFieldName() == null) {
				flag = isResultSuccess(jp);
			} else if (jp.nextTextValue().equalsIgnoreCase("success")) {
				flag = true;
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag;
	}

	private class HttpAsyncTask extends AsyncTask<String, Boolean, Bundle> {

		@Override
		protected Bundle doInBackground(String... params) {
//			HttpURLConnection urlConn = null;
//			BufferedReader bufReader = null;
//			StringBuilder strBuf = null;
//			String result = null;
//			JSONObject wholeJsonObject = null;
//			JSONObject jsonObject = null;
//			JSONArray jsonArray = null;
//			GridFragmentItemData item = null;
//			ArrayList<GridFragmentItemData> itemArray = null;
//			
//			urlConn = HttpConnectionManager.getHttpURLConnection(params[0], params[1]);
//
//			try {
//				int responseCode = urlConn.getResponseCode();
//				if (responseCode >= 200 && responseCode < 300) {
//					bufReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//				}
//				else {
//					return null;
//				}
//				String line = "";
//				strBuf = new StringBuilder();
//				while ((line = bufReader.readLine()) != null) {
//					strBuf.append(line);
//				}
//				Log.e(LOG_TAG + " HttpAsyncTask", strBuf.toString());
//				
//				wholeJsonObject = new JSONObject(strBuf.toString());
//				boolean isResultSuccess = wholeJsonObject.getString("result").equalsIgnoreCase("success");
//				
//				if (isResultSuccess) {
//					jsonArray = wholeJsonObject.getJSONArray("gridlist");
//				} else {
//					throw new JSONException("not success");
//				}
//				
//				itemArray = new ArrayList<GridFragmentItemData>();
//				
//				for (int i = 0; i < jsonArray.length(); i++) {
//					jsonObject = jsonArray.getJSONObject(i);
//					
//					item = new GridFragmentItemData();
//					
//					item.recipeId = jsonObject.getString("RECIPE_ID");
//					item.recipeName = jsonObject.getString("RECIPE_NM_KO");
//					item.summary = jsonObject.getString("SUMRY");
//					item.nationName = jsonObject.getString("NATION_NM");
//					item.typeName = jsonObject.getString("TY_NM");
//					item.cookingTime = jsonObject.getString("COOKING_TIME");
//					item.calorie = jsonObject.getString("CALORIE");
//					item.levelName = jsonObject.getString("LEVEL_NM");
//					item.price = jsonObject.getString("PC_NM");
//					item.imgUrl = jsonObject.getString("IMG_URL");
//					item.countReply = jsonObject.getInt("COUNT_REPLY");
//					item.countLike = jsonObject.getInt("COUNT_LIKE");
//					item.countRequest = jsonObject.getInt("COUNT_REQUEST");
//				
//					itemArray.add(item);
//				}
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally {
//				HttpConnectionManager.setDismissConnection(urlConn, bufReader, null);
//			}
			
			
			Bundle bundle = new Bundle();
			JsonFactory jsonFactory = new JsonFactory();
			JsonParser jp = null;
			URL url = null;
			
			ArrayList<GridItemData> itemArray = null;
			GridItemData item = null;
			try {
				url = new URL(params[0]);
				jp = jsonFactory.createParser(url);
				
				if (!isResultSuccess(jp)) {
					throw new JSONException("not success");
				}
				
//				jp.nextToken();
//				jp.nextToken();
//				jp.nextToken();
//				boolean isResultSuccess = jp.getCurrentName().equalsIgnoreCase("success");
//				if (!isResultSuccess) {
//					throw new JSONException("not success");
//				}
				
				while (jp.nextToken() != JsonToken.START_ARRAY) {}
				
				itemArray = new ArrayList<GridItemData>();
				while (jp.nextToken() != JsonToken.END_ARRAY) {
					if (item != null) {
						itemArray.add(item);
					}
					item = new GridItemData();
					while (jp.nextToken() != JsonToken.END_OBJECT) {
						String nameField = jp.getCurrentName();
						jp.nextToken();
						if (nameField.equalsIgnoreCase("COLUMN_ID")) {
							item.recipeId = jp.getText();
						} else if (nameField.equalsIgnoreCase("COLUMN_IMG_URL")) {
							item.imgUrl = jp.getText();
						}
					}
				}
				itemArray.add(item);
				bundle.putParcelableArrayList("columnlist", itemArray);	
				item = null;
				itemArray = new ArrayList<GridItemData>();
				while (jp.nextToken() != JsonToken.END_ARRAY) {
					if (item != null) {
						itemArray.add(item);
					}
					item = new GridItemData();
					while (jp.nextToken() != JsonToken.END_OBJECT) {
						String nameField = jp.getCurrentName();
						jp.nextToken();
						if (nameField.equalsIgnoreCase("RECIPE_ID")) {
							item.recipeId = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_NM_KO")) {
							item.recipeName = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_SUMRY")) {
							item.summary = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_NATION_NM")) {
							item.nationName = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_TY_NM")) {
							item.typeName = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_COOKING_TIME")) {
							item.cookingTime = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_CALORIE")) {
							item.calorie = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_LEVEL_NM")) {
							item.levelName = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_PC_NM")) {
							item.price = jp.getText();
						} else if (nameField.equalsIgnoreCase("RECIPE_IMG_URL")) {
							item.imgUrl = jp.getText();
						} else if (nameField.equalsIgnoreCase("COUNT_REPLY")) {
							item.countReply = jp.getIntValue();
						} else if (nameField.equalsIgnoreCase("COUNT_LIKE")) {
							item.countLike = jp.getIntValue();
						} else if (nameField.equalsIgnoreCase("COUNT_REQUEST")) {
							item.countRequest = jp.getIntValue();
						}
					}
				}
				itemArray.add(item);
				bundle.putParcelableArrayList("gridlist", itemArray);
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bundle;
		}

		@Override
		protected void onPostExecute(Bundle result) {
			super.onPostExecute(result);
			
			if (result == null) {
				return;
			}
			
			columnArray = result.getParcelableArrayList("columnlist");
			gridArray = result.getParcelableArrayList("gridlist");
			
			/*
			 * 임시 데이터
			 */
//			itemDataArray = new ArrayList<GridItemData>();
//			GridItemData iData = new GridItemData();
//			iData.imgUrl = "http://cfs9.tistory.com/upload_control/download.blog?fhandle=YmxvZzIxNzU2QGZzOS50aXN0b3J5LmNvbTovYXR0YWNoLzAvMTguYm1w";
//			itemDataArray.add(iData);
//			iData = new GridItemData();
//			iData.imgUrl = "http://img.mimint.co.kr/know/bbs/2012/12/28/THQSWI2R5I5I8Q90M3VJ.jpg";
//			itemDataArray.add(iData);
			
			mGridHeaderAdapter = new GridHeaderAdapter(getContext(), columnArray);
			headerPager.setAdapter(mGridHeaderAdapter);
			indicator.setViewPager(headerPager);
			
			mGridAdapter = new GridAdapter(getContext());
			mGridAdapter.setItem(gridArray);
			mGridAdapter.notifyDataSetChanged();
			
			recipeGridView.setAdapter(mGridAdapter);
						
		}		
	}
	
}
