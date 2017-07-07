package seop.gyun.recipedia.recipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import seop.gyun.recipedia.HttpConnectionManager;
import seop.gyun.recipedia.JsonBuilder;
import seop.gyun.recipedia.QueryBuilder;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.RecipediaApplication;
import seop.gyun.recipedia.RecipediaConstant;

public class ReplyActivity extends AppCompatActivity {

	private static final String LOG_TAG = "ReplyActivity";

	String recipeId;
	EditText replyText;
	ReplyAdapter adapter;
	ListView mListView;
	ArrayList<ReplyData> mData;
	TextView nullText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_reply);

		recipeId = getIntent().getStringExtra("recipe_id");
		Toast.makeText(this, "recipeId : " + recipeId, Toast.LENGTH_SHORT).show();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		nullText = (TextView) findViewById(R.id.reply_null_text);

		replyText = (EditText) findViewById(R.id.reply_text);

		mListView = (ListView) findViewById(R.id.reply_listview);

		new ReplyAsyncTask().execute(RecipediaConstant.SERVER_REPLY
				+ new QueryBuilder("recipeid", recipeId, false).toString(), "GET");

		/*
		 * 댓글 더미데이터
		 */
		// mData = new ArrayList<ReplyData>();
		// ReplyData d = new ReplyData();
		// d.replyContent = "첫번째 댓글내용";
		// d.replyDate = "2015.09.24";
		// d.profileImgUrl =
		// "http://cfs9.tistory.com/upload_control/download.blog?fhandle=YmxvZzIxNzU2QGZzOS50aXN0b3J5LmNvbTovYXR0YWNoLzAvMTguYm1w";
		// mData.add(d);
		// d = new ReplyData();
		// d.replyContent = "두번째 댓글내용";
		// d.replyDate = "2015.08.24";
		// d.profileImgUrl =
		// "http://img.mimint.co.kr/know/bbs/2012/12/28/THQSWI2R5I5I8Q90M3VJ.jpg";
		// mData.add(d);
		//////////////////////////////////

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 댓글 입력 버튼 클릭
	 * 
	 * @param v
	 *            {@link Button}
	 */
	public void onReplySubmitBtnClick(View v) {
		String text = replyText.getText().toString();
		if (text == null || text.equals("")) {
			return;
		}

		/*
		 * 멤버아이디 임의로 1로 지정 !! 추후 수정 요
		 */
		String body = new QueryBuilder("memberid", RecipediaApplication.getId(), true).append("replybody", text)
				.append("recipeid", recipeId).toString();
		

		new ReplyPostAsyncTask().execute(RecipediaConstant.SERVER_REPLY_POST, "BODY",
				body);
	}

	private class ReplyPostAsyncTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			boolean isResultSuccess;
			String response = HttpConnectionManager.getResponse(params[0], params[1],
					params[2]);

			JSONObject wholeJsonObject = null;
			try {
				wholeJsonObject = new JSONObject(response);
				isResultSuccess = wholeJsonObject.getString("result")
						.equalsIgnoreCase("success");

			} catch (JSONException e) {
				e.printStackTrace();
				isResultSuccess = false;
			} catch (NullPointerException e) {
				e.printStackTrace();
				isResultSuccess = false;
			}

			return isResultSuccess;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			if (!result) {
				Toast.makeText(getApplicationContext(), "댓글 전송 실패.\n네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
				return;
			}
			
			ReplyData item = new ReplyData();
			item.replyContent = replyText.getText().toString();
			item.userName = RecipediaApplication.getName();
			item.replyDate = "방금 전";
			item.profileImgUrl = RecipediaApplication.getImgUrl();
			replyText.setText("");
			
			if (adapter != null) {
				adapter.add(item);
			} else {
				nullText.setVisibility(View.GONE);
				mData = new ArrayList<ReplyData>();
				mData.add(item);
				adapter = new ReplyAdapter(ReplyActivity.this, mData);
				mListView.setAdapter(adapter);
				//adapter.notifyDataSetChanged();
			}
		}

	}

	private class ReplyAsyncTask
			extends AsyncTask<String, Boolean, ArrayList<ReplyData>> {

		@Override
		protected ArrayList<ReplyData> doInBackground(String... params) {
			HttpURLConnection urlConn = null;
			BufferedReader bufReader = null;
			StringBuilder strBuf = null;
			JSONObject wholeJsonObject = null;
			JSONObject jsonObject = null;
			JSONArray jsonArray = null;
			ReplyData item = null;
			ArrayList<ReplyData> itemArray = null;

			urlConn = HttpConnectionManager.getHttpURLConnection(params[0], params[1]);

			try {
				int responseCode = urlConn.getResponseCode();
				if (responseCode >= 200 && responseCode < 300) {
					bufReader = new BufferedReader(
							new InputStreamReader(urlConn.getInputStream()));
				} else {
					return null;
				}
				String line = "";
				strBuf = new StringBuilder();
				while ((line = bufReader.readLine()) != null) {
					strBuf.append(line);
				}
				Log.e(LOG_TAG + " ReplyAsyncTask", strBuf.toString());

				wholeJsonObject = new JSONObject(strBuf.toString());
				boolean isResultSuccess = wholeJsonObject.getString("result")
						.equalsIgnoreCase("success");

				if (isResultSuccess) {
					jsonArray = wholeJsonObject.getJSONArray("replylist");
				} else {
					throw new JSONException("not success");
				}

				itemArray = new ArrayList<ReplyData>();

				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);

					item = new ReplyData();

					item.userName = jsonObject.getString("MEM_NICK");
					item.profileImgUrl = jsonObject.getString("MEM_PIC");
					item.replyDate = jsonObject.getString("REP_TIME");
					item.replyContent = jsonObject.getString("REP_BODY");

					itemArray.add(item);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				HttpConnectionManager.setDismissConnection(urlConn, bufReader, null);
			}
			return itemArray;
		}

		@Override
		protected void onPostExecute(ArrayList<ReplyData> result) {
			super.onPostExecute(result);

			if (result == null) {
				nullText.setVisibility(View.VISIBLE);
				return;
			}

			mData = result;

			adapter = new ReplyAdapter(ReplyActivity.this, mData);
			mListView.setAdapter(adapter);
		}
	}

}
