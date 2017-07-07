package seop.gyun.recipedia;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.util.Log;

public class HttpConnectionManager {
	private static final String DEBUG_TAG = "HttpConnectionManager";
	private static final MediaType BODY
    = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");


	/**
	 * {@link OkHttpClient} 의 {@link Response} 값을 리턴한다.
	 * @param url 연결 요청할 주소
	 * @return response 스트링
	 */
	public static String getResponse(String url, String reqMethod, String query) {

		String result = null;
		
		OkHttpClient client = new OkHttpClient();
		
		Request request = null;
		if (reqMethod.equalsIgnoreCase("GET")) {
			request = new Request.Builder()
					.url(url)
					.build();
		} else if (reqMethod.equalsIgnoreCase("QUERY")) {
			request = new Request.Builder()
				      .url(url + query)
				      .build();
		} else if (reqMethod.equalsIgnoreCase("BODY")) {
			RequestBody body = RequestBody.create(BODY, query);
			request = new Request.Builder()
				      .url(url)
				      .post(body)
				      .build();
		}
		
		Response response = null;
		try {
			response = client.newCall(request).execute();
			result = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static HttpURLConnection getHttpURLConnection(String targetURL,
			String reqMethod) {
		HttpURLConnection httpConnection = null;
		try {
			URL url = new URL(targetURL);

			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestMethod(reqMethod);
			httpConnection.setDoInput(true);
			httpConnection.setConnectTimeout(15000);

		} catch (Exception e) {
			Log.e(DEBUG_TAG, "getHttpURLConnection() -- ���� �߻� -- ", e);
		}
		return httpConnection;
	}

	public static HttpURLConnection postHttpURLConnection(String targetURL,
			String reqMethod) {
		HttpURLConnection httpConnection = null;
		try {
			URL url = new URL(targetURL);

			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestMethod(reqMethod);
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setReadTimeout(15000);

			httpConnection.setConnectTimeout(15000);

			httpConnection.setRequestProperty("Connection", "Keep-Alive");
			// httpConnection.setRequestProperty("Content-Type",
			// "application/json; charset=utf-8");
			/*
			 * httpConnection.setRequestProperty("Content-Type",
			 * "application/x-www-form-urlencoded");
			 */

		} catch (Exception e) {
			Log.e(DEBUG_TAG, "getHttpURLConnection() -- ���� �߻� -- ", e);
		}
		return httpConnection;
	}

	public static HttpURLConnection DeleteHttpURLConnection(String targetURL,
			String reqMethod) {
		HttpURLConnection httpConnection = null;
		try {
			URL url = new URL(targetURL);

			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			httpConnection.setRequestMethod(reqMethod);
			httpConnection.setConnectTimeout(15000);

		} catch (Exception e) {
			Log.e(DEBUG_TAG, "getHttpURLConnection() -- ���� �߻� -- ", e);
		}
		return httpConnection;
	}

	public static void setDismissConnection(HttpURLConnection returnedConn, Reader inR,
			Writer outW) {

		if (inR != null) {
			try {
				inR.close();
			} catch (IOException ioe) {

			}
		}
		if (outW != null) {
			try {
				outW.close();
			} catch (IOException ioe) {

			}
		}
		if (returnedConn != null) {

			returnedConn.disconnect();
		}
	}
}
