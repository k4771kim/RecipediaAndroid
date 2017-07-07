package seop.gyun.recipedia;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecipediaDBHelper extends SQLiteOpenHelper {
	Context mContext;
	public static SQLiteDatabase db;
	// private static SQLiteDatabase db;
	// ContentValues row;
	private static RecipediaDBHelper mHelper = null;

	   public static RecipediaDBHelper getInstance(Context context) {
		      if (mHelper == null) {
		         mHelper = new RecipediaDBHelper(context, RecipediaConstant.RECIPE_DB_BASIC_INFO, null, 1);
		      }
		      return mHelper;
		   }
		   
		   public static SQLiteDatabase getDatabase(Context context) {
		      if (db == null) {
		         db = getInstance(context).getReadableDatabase();
		      }
		        return db;
		     }

	/*
	 * public static SQLiteDatabase getDatabase() { if (db == null) { db =
	 * mHelper.getReadableDatabase(); } return db; }
	 */

	private RecipediaDBHelper(Context context, String dbName, CursorFactory factory, int version) {
		super(context, dbName, factory, version);
		mContext = context;
	}

	// public class RecipediaDBHelper extends SQLiteOpenHelper {
	// Context mContext;
	// public static SQLiteDatabase db;
	// // ContentValues row;
	// public static RecipediaDBHelper mHelper = null;
	//
	// public RecipediaDBHelper(Context context) {
	// super(context, RecipediaConstant.RECIPE_DB_BASIC_INFO, null, 1);
	// mContext = context;
	// mHelper = this;
	// db = mHelper.getReadableDatabase();
	//
	// }
	//
	public static boolean isCheckDB(Context mContext) {
		String filePath = "/data/data/" + RecipediaConstant.PACKAGE_NAME + "/databases";
		File file = new File(filePath);

		if (file.exists()) {
			return true;
		}

		return false;

	}

	// DB를 복사하기
	// assets의 /db/xxxx.db 파일을 설치된 프로그램의 내부 DB공간으로 복사하기
	public static void copyDB(Context mContext) {
		Log.d("Recipedia", "copyDB");
		AssetManager manager = mContext.getAssets();
		String folderPath = "/data/data/" + RecipediaConstant.PACKAGE_NAME + "/databases";
		String filePath = "/data/data/" + RecipediaConstant.PACKAGE_NAME + "/databases/"
				+ RecipediaConstant.RECIPE_DB_BASIC_INFO;
		File folder = new File(folderPath);
		File file = new File(filePath);

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			InputStream is = manager.open("db/" + RecipediaConstant.RECIPE_DB_BASIC_INFO);
			BufferedInputStream bis = new BufferedInputStream(is);

			if (folder.exists()) {
			} else {
				folder.mkdirs();
			}

			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int read = -1;
			byte[] buffer = new byte[1024];
			while ((read = bis.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, read);
			}

			bos.flush();

			bos.close();
			fos.close();
			bis.close();
			is.close();

		} catch (IOException e) {
			Log.e("RecipediaDBHelper.copyDB", e.getMessage());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
