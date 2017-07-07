package seop.gyun.recipedia;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import android.graphics.Bitmap;

public class ImageLoaderManager {

	private static ImageLoader imageLoader;
	private static DisplayImageOptions imageLoaderOptions;

	static {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				
				RecipediaApplication.getContext())
						.threadPriority(Thread.NORM_PRIORITY + 2)
						.tasksProcessingOrder(QueueProcessingType.LIFO)
						.diskCacheSize(50 * 1024 * 1024)
						.diskCacheFileCount(50)
						.memoryCacheSizePercentage(25)
						.build();
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();
		
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(RecipediaApplication.getContext())
//	    	      .threadPriority(Thread.NORM_PRIORITY + 2)
//	    	      .tasksProcessingOrder(QueueProcessingType.LIFO)
//	    	      .threadPoolSize(5)
//	    	      .diskCacheFileCount(70)
//	    	      .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
//	    	      .memoryCacheSizePercentage(30)
//	    	      .memoryCache(new WeakMemoryCache()) 
//	    	      .build();
//
//	    	      ImageLoader.getInstance().init(config);
//	    	      imageLoader = ImageLoader.getInstance();
		
		
	}

	public static ImageLoader getImageLoaderManager() {
		return imageLoader;
	}

	public static DisplayImageOptions getDisplayImageOptions() {
		if (imageLoaderOptions != null) {
			return imageLoaderOptions;
		} else {
			return imageLoaderOptions = new DisplayImageOptions.Builder()
					.displayer(new SimpleBitmapDisplayer())
					//.displayer(new RoundedBitmapDisplayer(0))
				
					// .showImageOnLoading(R.drawable.ic_loading)
					// .showImageForEmptyUri(R.drawable.ic_loading)
					// .showImageOnFail(R.drawable.ic_loading)
					.cacheOnDisk(true)
					.cacheInMemory(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.build();
//			return imageLoaderOptions  = new DisplayImageOptions.Builder()
//			         //.showImageForEmptyUri(R.drawable.non_profile)
//			         //.showImageOnFail(R.drawable.non_profile)
//			        
//			         .resetViewBeforeLoading(false)
//			         .cacheOnDisk(true)
//			         .cacheInMemory(true)
//			         .imageScaleType(ImageScaleType.EXACTLY)
//			         .build();
//			
			
		}

	}
}
