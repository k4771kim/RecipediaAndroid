package seop.gyun.recipedia;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import seop.gyun.recipedia.survey.SurveyActivity;

public class RecipediaContainerActivity extends AppCompatActivity
		implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
	protected static final String LOCATION_ADDRESS_KEY = "location-address";

	private static final String LOG_TAG = "RecipediaContainerActivity";
	private static final LocationRequest BALANCED_POWER_REQUEST = LocationRequest.create()
			.setInterval(20000).setFastestInterval(5000)
			.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

	public static boolean isSearchEnabled = false;

	public static String mAddressOutput;

	protected GoogleApiClient mGoogleApiClient;
	protected Location mLastLocation;
	protected boolean mAddressRequested;

	private AddressResultReceiver mResultReceiver;
	private BackPressCloseHandler backPressCloseHandler;

	private SimpleDraweeView profileImg;

	SearchView searchView;
	ActionBarDrawerToggle dtToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recipedia_container);

		String loginResult = getIntent()
				.getStringExtra(RecipediaConstant.LOGIN_RESPONSE_EXTRA);
		Log.e(LOG_TAG, "loginResult : " + loginResult);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		DrawerLayout dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		// toolbar.inflateMenu(R.menu.menu_search);

		/*
		 * 프로필 이미지 설정
		 */
		Uri profileUri = Uri.parse(
				"http://s3.amazonaws.com/tcusenate/ecom_members/photos/000/000/005/square/no-profile-img.jpg");
		profileImg = (SimpleDraweeView) dlDrawer.findViewById(R.id.profile_img);
		profileImg.setImageURI(profileUri);
		RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
		roundingParams.setBorder(0xFFFFFFFF, 5.0f);
		roundingParams.setRoundAsCircle(true);
		profileImg.getHierarchy().setRoundingParams(roundingParams);
		profileImg.setOnClickListener(new View.OnClickListener() {

			@TargetApi(Build.VERSION_CODES.M)
			@Override
			public void onClick(View v) {
				Intent intent;

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
					intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "프로필 이미지 설정"),
							RecipediaConstant.GALLERY_REQUEST_CODE);
				} else {
					intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(intent,
							RecipediaConstant.GALLERY_REQUEST_CODE_KITKAT);
				}

			}
		});

		// dlDrawer.setStatusBarBackgroundColor(0xffe35259);

		// searchView = (SearchView)
		// toolbar.getMenu().findItem(R.id.menu_search).getActionView();

		setSupportActionBar(toolbar);
		ActionBar ab = getSupportActionBar();
		if (null != ab) {
			ab.setDisplayHomeAsUpEnabled(true);
		}

		dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name,
				R.string.app_name);
		dlDrawer.setDrawerListener(dtToggle);

		mResultReceiver = new AddressResultReceiver(new Handler());

		mAddressRequested = false;
		mAddressOutput = "";
		updateValuesFromBundle(savedInstanceState);
		initGoogleApiClient();

		backPressCloseHandler = new BackPressCloseHandler(this);
		Cursor cursor = RecipediaDBHelper.getDatabase(getApplicationContext())
				.rawQuery("SELECT * FROM search", null);
		cursor.moveToFirst();
		// searchView = (SearchView) findViewById(R.id.search_view);
		// searchView.SearchAutoComplete.setThreshold(1);

		// searchView.setSuggestionsAdapter(new
		// SearchCursorAdapter(getApplicationContext(), cursor,
		// CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
		// searchView.setOnQueryTextListener(new
		// SearchView.OnQueryTextListener() {
		//
		// @Override
		// public boolean onQueryTextSubmit(String query) {
		// // TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "onQueryTextSubmit" + query,
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }
		//
		// @Override
		// public boolean onQueryTextChange(String newText) {
		// // TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "onQueryTextChange : " +
		// newText, Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// });

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			mGoogleApiClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		backPressCloseHandler.onBackPressed();
	}

	/**
	 * 레시피 보관함 클릭
	 * 
	 * @param v
	 */
	public void onRecipeBoxBtnClick(View v) {
		Intent intent = new Intent(this, RecipeBoxActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.abc_fade_in, android.R.anim.fade_out);
	}

	/**
	 * 위치정보 설정 클릭
	 * 
	 * @param v
	 */
	public void onLocationSettingBtnClick(View v) {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	private void updateValuesFromBundle(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
				mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
			}
			if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
				mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
			}
		}
	}

	protected synchronized void initGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(RecipediaApplication.getContext(),
				this, this).addApi(LocationServices.API).addConnectionCallbacks(this)
						.addOnConnectionFailedListener(this).build();
	}

	protected void startIntentService() {
		Intent intent = new Intent(this, AddressIntentService.class);
		intent.putExtra(RecipediaConstant.RECEIVER, mResultReceiver);
		intent.putExtra(RecipediaConstant.LOCATION_DATA_EXTRA, mLastLocation);
		startService(intent);
	}

	public void addressServiceHandler() {
		if (mGoogleApiClient.isConnected() && mLastLocation != null) {
			startIntentService();
		}
		mAddressRequested = true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mLastLocation = location;
		addressServiceHandler();
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
				BALANCED_POWER_REQUEST, this);

		if (mLastLocation != null) {
			if (!Geocoder.isPresent()) {
				Log.e(LOG_TAG, "Geocoder 사용불가");
				return;
			}

			if (mAddressRequested) {
				startIntentService();
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("onActivityResult", "requestCode : " + String.valueOf(requestCode)
				+ ", resultCode : " + String.valueOf(resultCode));
		if (resultCode != RESULT_OK) {
			return;
		}
		if (data == null) {
			return;
		}

		Uri originalUri = null;
		if (requestCode == RecipediaConstant.GALLERY_REQUEST_CODE) {
			originalUri = data.getData();
		} else if (requestCode == RecipediaConstant.GALLERY_REQUEST_CODE_KITKAT) {
			originalUri = data.getData();
			final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION
					| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
		} else {
			return;
		}

		String uriString = "file://" + Chooser.getPath(this, originalUri);
		Log.d(LOG_TAG, uriString);

		new ImageUploadAsyncTask().execute(RecipediaApplication.getId(), uriString);

		
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);
		savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.menu_recipedia, menu);
		getMenuInflater().inflate(R.menu.menu_recipedia, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		dtToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		dtToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (dtToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == R.id.action_survey) {
			Intent intent = new Intent(this, SurveyActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			Toast.makeText(this, "FLAG_ACTIVITY_NO_HISTORY !!!", Toast.LENGTH_SHORT)
					.show();
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	class AddressResultReceiver extends ResultReceiver {

		public AddressResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			mAddressOutput = resultData.getString(RecipediaConstant.RESULT_DATA_KEY);
			Log.i(LOG_TAG, "mAddressOutput : " + mAddressOutput);

			if (resultCode == RecipediaConstant.SUCCESS_RESULT) {
				Log.i(LOG_TAG, "SUCCESS_RESULT");
			}

			mAddressRequested = false;
		}
	}

	private class ImageUploadAsyncTask extends AsyncTask<String, Boolean, String> {

		@Override
		protected String doInBackground(String... params) {
			

			/*
			 * params[1]은 이미지 경로.
			 * 여기에 webp로 압축하는 코드를 작성해야 함.
			 */
			
			OkHttpClient client = new OkHttpClient();

			Request request = null;
			RequestBody body = new MultipartBuilder().type(MultipartBuilder.FORM)
					.addPart(
							Headers.of("Content-Disposition", "form-data; name=\"memberid\""),
							RequestBody.create(null, params[0]))
					.addPart(
							Headers.of("Content-Disposition", "form-data; name=\"value\""),
							RequestBody.create(MediaType.parse("image/webp"), new File("webp파일경로!")))
					.build();
			request = new Request.Builder().url(RecipediaConstant.SERVER_IMG_UPLOAD).post(body).build();

			
			Response response = null;
			String result = null;
			String path = null;
			JSONObject wholeJsonObject = null;
			try {
				response = client.newCall(request).execute();
				result = response.body().string();

				wholeJsonObject = new JSONObject(result);
				boolean isResultSuccess = wholeJsonObject.getString("result").equalsIgnoreCase("success");
				
				if (isResultSuccess) {
					path = params[1];
				} else {
					path = null;
				}

				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return path;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null) {
				return;
			}
			Uri uri = Uri.parse(result);
			profileImg.setImageURI(uri);
		}

	}

}
