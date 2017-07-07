package seop.gyun.recipedia.recipe;

import android.os.Parcel;
import android.os.Parcelable;

public class ReplyData implements Parcelable {

	String userName, profileImgUrl, replyDate, replyContent;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userName);
		dest.writeString(profileImgUrl);
		dest.writeString(replyDate);
		dest.writeString(replyContent);
	}
	
	public static final Parcelable.Creator<ReplyData> CREATOR = new Parcelable.Creator<ReplyData>() {
		public ReplyData createFromParcel(Parcel in) {
			return new ReplyData(in);
		}

		public ReplyData[] newArray(int size) {
			return new ReplyData[size];
		}
	};

	public ReplyData() {
	}
	
	private ReplyData(Parcel in) {
		userName = in.readString();
		profileImgUrl = in.readString();
		replyDate = in.readString();
		replyContent = in.readString();
	}
	
}
