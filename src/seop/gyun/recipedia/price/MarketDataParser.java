package seop.gyun.recipedia.price;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import android.content.res.AssetManager;
import seop.gyun.recipedia.RecipediaApplication;

public class MarketDataParser {

	AssetManager manager;
	ArrayList<MarketData> mData;
	String path;

	/**
	 * {@link MarketDataParser}의 생성자.
	 * 
	 * @param assetPath
	 *            assets 폴더 내의 {@link MarketData} 경로를 입력.
	 */
	public MarketDataParser(String assetPath) {
		path = assetPath;
		manager = RecipediaApplication.getContext().getAssets();
		mData = new ArrayList<MarketData>();

	}

	/**
	 * 생성한 경로의 csv 파일을 파싱.
	 * 
	 * @return {@link MarketData}의 {@link ArrayList}를 리턴
	 */
	public ArrayList<MarketData> getData() {
		InputStream is = null;
		InputStreamReader isr = null;
		StringTokenizer strT = null;
		StringBuffer strB = new StringBuffer();
		BufferedReader bufR = null;

		try {
			is = manager.open(path, AssetManager.ACCESS_BUFFER);
			isr = new InputStreamReader(is, Charset.forName("EUC-KR"));
			bufR = new BufferedReader(isr);
			while ((strB.append(bufR.readLine())) != null) {
				strT = new StringTokenizer(strB.toString(), ",");

				String strName = strT.nextToken();
				if (!strName.equals("MK_NM")) {
					MarketData tempData = new MarketData();
					tempData.name = strName;
					tempData.lat = strT.nextToken();
					tempData.lng = strT.nextToken();
					mData.add(tempData);
				}
				strB.delete(0, strB.length());
				strT = null;
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (IndexOutOfBoundsException ioobe) {
			ioobe.printStackTrace();
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (NoSuchElementException nsee) {
			nsee.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		} finally {

			try {
				if (isr != null) {
					isr.close();
				}
				if (bufR != null) {
					bufR.close();
				}
			} catch (IOException e) {
			}

		}
		return mData;
	}
}
