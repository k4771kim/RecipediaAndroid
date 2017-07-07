package seop.gyun.recipedia;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;
import seop.gyun.recipedia.R;

public class SplashActivity extends Activity {
	
	private static final String LOG_TAG = "SplashActivity";
	private static final int SFLASH_DELAY = 2000; // ms

	private String response;
	private HttpAsyncTask mHttpAsynkTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// Context mContext = this;
		// RecipediaDBHelper.getInstance(this);
		try {
			// Log.d("SplashActivity.onCreate", "DB Check=" + bResult);
			if (!RecipediaDBHelper.isCheckDB(this)) {  // DB가 없으면 복사
				RecipediaDBHelper.copyDB(this);
			} else {

			}
		} catch (Exception e) {
			Log.e("SplashActivity.onCreate", e.getMessage());

		}
		
		response = "";
		String androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		String query = new QueryBuilder("uid", androidId, false).toString();
		Log.d(LOG_TAG, "query : " + query);
		
		mHttpAsynkTask = new HttpAsyncTask();
		mHttpAsynkTask.execute(RecipediaConstant.SERVER_LOGIN, "QUERY", query);
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, RecipediaContainerActivity.class);
				intent.putExtra(RecipediaConstant.LOGIN_RESPONSE_EXTRA, response);
				Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
				startActivity(intent);

				finish();

			}
		}, SFLASH_DELAY);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.abc_fade_in, android.R.anim.fade_out);
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		mHttpAsynkTask.cancel(true);
	}

	private class HttpAsyncTask extends AsyncTask<String, Boolean, String> {

		@Override
		protected String doInBackground(String... params) {
			return HttpConnectionManager.getResponse(params[0], params[1], params[2]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			response = result;
		}	
	}
	
}
