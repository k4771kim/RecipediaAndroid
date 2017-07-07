package seop.gyun.recipedia;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class AddressIntentService extends IntentService {

	private static final String LOG_TAG = "AddressIntentService";

	protected ResultReceiver mReceiver;

	public AddressIntentService() {
		super(LOG_TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String errorMessage = "";

		mReceiver = intent.getParcelableExtra(RecipediaConstant.RECEIVER);

		if (mReceiver == null) {
			Log.wtf(LOG_TAG, "Receiver == null !!!");
			return;
		}
		Location location = intent
				.getParcelableExtra(RecipediaConstant.LOCATION_DATA_EXTRA);

		if (location == null) {
			errorMessage = "location == null !!!";
			Log.wtf(LOG_TAG, errorMessage);
			deliverResultToReceiver(RecipediaConstant.FAILURE_RESULT, errorMessage);
			return;
		}

		Log.d(LOG_TAG, "latitude : " + String.valueOf(location.getLatitude())
				+ "\nlongitude : " + String.valueOf(location.getLongitude()));

		Geocoder geocoder = new Geocoder(this, Locale.KOREA);

		List<Address> addresses = null;

		try {
			addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
		} catch (IOException e) {
			errorMessage = "네트워크 또는 io 예외";
			Log.e(LOG_TAG, errorMessage, e);
		} catch (IllegalArgumentException e) {
			errorMessage = "잘못된 위도, 경도";
			Log.e(LOG_TAG, errorMessage + ". " + "Latitude = " + location.getLatitude()
					+ ", Longitude = " + location.getLongitude(), e);
		}

		if (addresses == null || addresses.size() == 0) {
			if (errorMessage.isEmpty()) {
				errorMessage = "주소를 받아올 수 없습니다.";
				Log.e(LOG_TAG, errorMessage);
			}
			deliverResultToReceiver(RecipediaConstant.FAILURE_RESULT, errorMessage);
		} else {
			Address address = addresses.get(0);
			String result = address.getAdminArea();
			Log.d(LOG_TAG, "도시 : " + result);
			deliverResultToReceiver(RecipediaConstant.SUCCESS_RESULT, result);
		}
	}

	private void deliverResultToReceiver(int resultCode, String message) {
		Bundle bundle = new Bundle();
		bundle.putString(RecipediaConstant.RESULT_DATA_KEY, message);
		mReceiver.send(resultCode, bundle);
	}

}
