package seop.gyun.recipedia.price;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import seop.gyun.recipedia.HttpConnectionManager;
import seop.gyun.recipedia.RecipediaConstant;

public class GetPrice {

	private static final String LOG_TAG = "GetPrice";

	public static String getPrice(String itemCode, String areaCode) {
		HttpURLConnection urlConn = null;
		BufferedReader jsonStreamData = null;
		String returnedPrice = null;

		/*
		 * itemCode 111
		 */
		String server = new StringBuilder(RecipediaConstant.GET_PRICE_URL)
				.append(getDate()).append("&PRDLST_CD=").append(itemCode)
				.append("&AREA_CD=").append(areaCode).toString();

		urlConn = HttpConnectionManager.getHttpURLConnection(server, "GET");

		try {

			int responseCode = urlConn.getResponseCode();
			if (responseCode >= 200 && responseCode < 300) {

				jsonStreamData = new BufferedReader(
						new InputStreamReader(urlConn.getInputStream()));
			}

			else {
				return null;
			}
			String line = "";
			StringBuilder buf = new StringBuilder();
			while ((line = jsonStreamData.readLine()) != null) {

				buf.append(line);

			}

			Log.e(LOG_TAG, buf.toString());

			JSONObject mainobj = new JSONObject(buf.toString());
			JSONObject gridobj = mainobj.getJSONObject("Grid_20141225000000000163_1");
			JSONArray firstarr = new JSONArray();
			firstarr = gridobj.getJSONArray("row");

			// JSONArray firstarr = new
			// JSONArray(mainobj.getJSONArray("row"));
			JSONObject firstobj = firstarr.getJSONObject(0);
			returnedPrice = firstobj.getString("AMT");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HttpConnectionManager.setDismissConnection(urlConn, jsonStreamData, null);
		}
		return returnedPrice;
	}

	private static String getDate() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		cal.add(Calendar.DAY_OF_YEAR, -7);

		return s.format(new Date(cal.getTimeInMillis()));

	}
}
