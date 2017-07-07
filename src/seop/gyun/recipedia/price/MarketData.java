package seop.gyun.recipedia.price;

import android.os.Parcel;
import android.os.Parcelable;

public class MarketData implements Parcelable {

	String name, lat, lng;
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(lat);
		dest.writeString(lng);
	}
	
	public static final Parcelable.Creator<MarketData> CREATOR = new Parcelable.Creator<MarketData>() {
		public MarketData createFromParcel(Parcel in) {
			return new MarketData(in);
		}

		public MarketData[] newArray(int size) {
			return new MarketData[size];
		}
	};

	public MarketData() {
	}

	private MarketData(Parcel in) {
		name = in.readString();
		lat = in.readString();
		lng = in.readString();
	}

}
