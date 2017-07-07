package seop.gyun.recipedia;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

import android.app.Application;
import android.content.Context;

public class RecipediaApplication extends Application {
	private static Context mContext;
	private static String userId = "1";
	private static String userName = "비회원";
	private static String userImgUrl = "http://s3.amazonaws.com/tcusenate/ecom_members/photos/000/000/005/square/no-profile-img.jpg";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		
		OkHttpClient okHttpClient = new OkHttpClient();
		
		ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
		    .newBuilder(mContext, okHttpClient)
		    .build();
		Fresco.initialize(mContext, config);
		
		// Fresco.initialize(mContext);
		
	}

	public static Context getContext() {
		return mContext;
	}
	
	public static String getId() {
		return userId;
	}
	public static void setId(String id) {
		userId = id;
	}

	public static String getName() {
		return userName;
	}

	public static void setName(String name) {
		userName = name;
	}

	public static String getImgUrl() {
		return userImgUrl;
	}

	public static void setImgUrl(String imgUrl) {
		userImgUrl = imgUrl;
	}
	
	
	
	
}
