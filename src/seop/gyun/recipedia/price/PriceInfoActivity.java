package seop.gyun.recipedia.price;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import seop.gyun.recipedia.HttpConnectionManager;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.RecipediaApplication;
import seop.gyun.recipedia.RecipediaConstant;
import seop.gyun.recipedia.RecipediaContainerActivity;
import seop.gyun.recipedia.RecipediaDBHelper;
import seop.gyun.recipedia.grid.GridItemData;
import seop.gyun.recipedia.recipe.IngredientData;

public class PriceInfoActivity extends AppCompatActivity
		implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	private static final String LOG_TAG = "RecipePriceInfoActivity";
	private static final String MARKET_DATA_EXTRA = "market_data_extra";
	private static final LocationRequest HIGH_ACCURACY_REQUEST = LocationRequest.create().setInterval(5000)
			.setFastestInterval(16).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	protected GoogleApiClient mGoogleApiClient;

	private Button currentLocationBtn;
	private ArrayList<IngredientData> mIngredientData;
	private ArrayList<MarketData> mMarketData;
	private GridItemData mData;
	private GoogleMap gMap;

	private boolean isLocationInit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_price_info);

		mData = getIntent().getParcelableExtra("gridlist");

		initGoogleApiClient();

		if (savedInstanceState == null) {

			// mMarketData = new MarketDataParser("market.csv").getData();

			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			Cursor cursor = RecipediaDBHelper.getDatabase(this)
					.rawQuery("SELECT * FROM ingredient WHERE IRDNT_RECIPE_ID = \"" + mData.recipeId + "\"", null); // 재료정보
			if (cursor.moveToFirst()) { // ingredient Table
				mIngredientData = new ArrayList<IngredientData>();
				int i = 0;
				do {
					IngredientData d = new IngredientData();
					d.IRD_ID = cursor.getString(0);
					d.IRD_Name = cursor.getString(2);
					d.IRD_Capacity = cursor.getString(3);
					d.IRD_Type = cursor.getString(5);
					mIngredientData.add(d);
					Cursor cursor2 = RecipediaDBHelper.getDatabase(this).rawQuery(
							"SELECT * FROM itemcode WHERE ITEMCODE_NAME = \"" + cursor.getString(2) + "\"", null); // 재료코드받아오기

					if (cursor2.moveToFirst())
						d.IRD_Code = cursor2.getString(0);
					i++;
				} while (cursor.moveToNext());

			}

			Cursor addressCursor = RecipediaDBHelper.getDatabase(this).rawQuery(
					"SELECT * FROM location WHERE AREA_NM = \"" + RecipediaContainerActivity.mAddressOutput + "\"",
					null);
			String areaCode = "";
			if (addressCursor.moveToFirst()) {
				areaCode = addressCursor.getString(0);
			}

			Toast.makeText(getApplicationContext(), areaCode + "", Toast.LENGTH_SHORT).show();

			for (int i = 0; i < mIngredientData.size(); i++) {
				new PriceAsync().execute(mIngredientData.get(i).IRD_Code, areaCode, String.valueOf(i)); // 재료의
																										// 갯수만큼
																										// 요청해야함
			}

		} else {
			// mMarketData =
			// savedInstanceState.getParcelableArrayList(MARKET_DATA_EXTRA);
		}

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mGoogleApiClient == null) {
			return;
		}

		if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		} else if (mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, HIGH_ACCURACY_REQUEST, this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap map) {
		gMap = map;
		// LatLng sydney = new LatLng(-34, 151);
		// map.addMarker(new MarkerOptions().position(sydney).title("Marker in
		// Sydney"));
		// map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

		LatLng mLatLng = null;
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (location == null) {
			mLatLng = new LatLng(37.5651, 126.98955);
			Log.e("location", "null");
		} else {
			mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
			Log.e("location", "not null");
			isLocationInit = true;
		}

		

		// map.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15f));
		// map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15f));
		map.setMyLocationEnabled(true);
		map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {
				// isLocationUpdateState = true;
				return false;
			}
		});

		currentLocationBtn = (Button) findViewById(R.id.current_location);
		currentLocationBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LatLng mLatLng = gMap.getCameraPosition().target;
				new RequestMarketData().execute(mLatLng.latitude, mLatLng.longitude);
			}
		});

	}

	protected synchronized void initGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(RecipediaApplication.getContext(), this, this)
				.addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (!isLocationInit) {
			LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
			gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
			new RequestMarketData().execute(location.getLatitude(), location.getLongitude());
			isLocationInit = true;
			return;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, HIGH_ACCURACY_REQUEST, this);
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	private boolean isMapReady() {
		if (gMap == null) {
			Toast.makeText(this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private class RequestMarketData extends AsyncTask<Double, Void, ArrayList<MarketData>> {

		@Override
		protected ArrayList<MarketData> doInBackground(Double... params) {
			ArrayList<MarketData> MarketArray = new ArrayList<MarketData>();
			// TODO Auto-generated method stub
			HttpURLConnection urlConn = null;
			BufferedReader jsonStreamData = null;

			/*
			 * itemCode 111
			 */
			String server = new StringBuilder(RecipediaConstant.SERVER_TRADITIONAL).append("?latitude=")
					.append(params[0]).append("&longitude=").append(params[1]).toString();

			Log.e("TT", server.toString());

			urlConn = HttpConnectionManager.getHttpURLConnection(server, "GET");

			try {

				int responseCode = urlConn.getResponseCode();
				if (responseCode >= 200 && responseCode < 300) {

					jsonStreamData = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				}

				else {
					return null;
				}
				String line = "";
				StringBuilder buf = new StringBuilder();
				while ((line = jsonStreamData.readLine()) != null) {

					buf.append(line);

				}


				JSONObject mainobj = new JSONObject(buf.toString());

				JSONArray firstarr = new JSONArray();
				firstarr = mainobj.getJSONArray("traditional");

				for (int j = 0; j < firstarr.length() - 1; j++) {
					JSONObject trdobject = new JSONObject();

					trdobject = firstarr.getJSONObject(j);
					MarketData md = new MarketData();
					md.name = trdobject.getString("name");
					JSONObject loc = trdobject.getJSONObject("location");

					md.lat = loc.getString("x");
					md.lng = loc.getString("y");
					MarketArray.add(md);

					Log.e(md.lat + "", md.lng + "");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				HttpConnectionManager.setDismissConnection(urlConn, jsonStreamData, null);
			}

			return MarketArray;
		}

		@Override
		protected void onPostExecute(ArrayList<MarketData> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			gMap.clear();
			for (MarketData d : result) {
				// Log.i(LOG_TAG, "시장이름 : " + d.name + ", 위도 : " + d.lat + ", 경도
				// : "
				// + d.lng);
				gMap.addMarker(new MarkerOptions()
						.position(new LatLng(Double.parseDouble(d.lat), Double.parseDouble(d.lng))).title(d.name));
			}
		}

	}

	private class PriceAsync extends AsyncTask<String, Void, String> {

		int i;
		LinearLayout container;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			container = (LinearLayout) findViewById(R.id.recipe_ingredient_container);
		}

		@Override
		protected String doInBackground(String... params) {
			i = Integer.parseInt(params[2]);
			return GetPrice.getPrice(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			StringBuilder sb = new StringBuilder();
			sb.append("IRD_ID : ").append(mIngredientData.get(i).IRD_ID).append("\nIRD_Name : ")
					.append(mIngredientData.get(i).IRD_Name).append("\nIRD_Capacity : ")
					.append(mIngredientData.get(i).IRD_Capacity).append("\nIRD_Type : ")
					.append(mIngredientData.get(i).IRD_Type).append("\nIRD_Code : ")
					.append(mIngredientData.get(i).IRD_Code).append("\n가격 : ").append(result);

			TextView view = new TextView(PriceInfoActivity.this);
			view.setText(sb.toString());
			container.addView(view, i, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// outState.putParcelableArrayList(MARKET_DATA_EXTRA, mMarketData);
		super.onSaveInstanceState(outState);
	}

}
