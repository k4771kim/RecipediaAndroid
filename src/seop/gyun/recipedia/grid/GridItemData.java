package seop.gyun.recipedia.grid;

import android.os.Parcel;
import android.os.Parcelable;

public class GridItemData implements Parcelable {

	// public String name, RecipeID, Summary, Nation, Type, CookingTime,
	// Calorie, Level, Price;

	public String recipeId, recipeName, summary, nationName, typeName, cookingTime,
			calorie, levelName, price, imgUrl;
	public int countReply, countLike, countRequest;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(recipeId);
		dest.writeString(recipeName);
		dest.writeString(summary);
		dest.writeString(nationName);
		dest.writeString(typeName);
		dest.writeString(cookingTime);
		dest.writeString(calorie);
		dest.writeString(levelName);
		dest.writeString(price);
		dest.writeString(imgUrl);
		dest.writeInt(countReply);
		dest.writeInt(countLike);
		dest.writeInt(countRequest);
	}

	public static final Parcelable.Creator<GridItemData> CREATOR = new Parcelable.Creator<GridItemData>() {
		public GridItemData createFromParcel(Parcel in) {
			return new GridItemData(in);
		}

		public GridItemData[] newArray(int size) {
			return new GridItemData[size];
		}
	};

	public GridItemData() {
	}

	private GridItemData(Parcel in) {
		recipeId = in.readString();
		recipeName = in.readString();
		summary = in.readString();
		nationName = in.readString();
		typeName = in.readString();
		cookingTime = in.readString();
		calorie = in.readString();
		levelName = in.readString();
		price = in.readString();
		imgUrl = in.readString();
		countReply = in.readInt();
		countLike = in.readInt();
		countRequest = in.readInt();
	}

}
