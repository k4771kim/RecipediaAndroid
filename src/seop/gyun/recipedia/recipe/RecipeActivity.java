package seop.gyun.recipedia.recipe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import seop.gyun.recipedia.R;
import seop.gyun.recipedia.RecipediaContainerActivity;
import seop.gyun.recipedia.grid.GridFragment;
import seop.gyun.recipedia.grid.GridItemData;
import seop.gyun.recipedia.price.PriceInfoActivity;

public class RecipeActivity extends AppCompatActivity {

	// String[] GridItemData;
	ImageButton backBtn;
	Cursor cursor, cursor2;
	ArrayList<ProgressData> ProgressData;
	ArrayList<IngredientData> IngredientData;

	private ArrayList<GridItemData> mDataArray;
	private GridItemData mData;
	private RecipePagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);

		Uri uriData = getIntent().getData();
		if (uriData != null) {
			String recipeId = uriData.getQueryParameter("id");
			mDataArray = GridFragment.gridArray;
			for (GridItemData d : mDataArray) {
				if (d.recipeId.equalsIgnoreCase(recipeId)) {
					mData = d;
					break;
				}
			}
		} else {
			mData = getIntent().getParcelableExtra("gridlist");
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Uri uri = Uri.parse(mData.imgUrl);
		Toast.makeText(this, mData.imgUrl, Toast.LENGTH_SHORT).show();
		SimpleDraweeView bgImgView = (SimpleDraweeView) findViewById(R.id.recipe_bg_img);

		Postprocessor processor = new BasePostprocessor() {
			@Override
			public String getName() {
				return "processor";
			}

			@Override
			public void process(Bitmap bitmap) {
				WeakReference<Bitmap> tempBitmap = new WeakReference<Bitmap>(
						blur((Context) RecipeActivity.this, bitmap, 7.0f));

				for (int x = 0; x < bitmap.getWidth(); x++) {
					for (int y = 0; y < bitmap.getHeight(); y++) {
						bitmap.setPixel(x, y, tempBitmap.get().getPixel(x, y));
					}
				}
			}
		};

		ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
				.setPostprocessor(processor).build();

		PipelineDraweeController controller = (PipelineDraweeController) Fresco
				.newDraweeControllerBuilder().setImageRequest(request)
				.setOldController(bgImgView.getController()).build();

		bgImgView.setController(controller);

		/////////////////////////////////////////////////////////////////////////////////////////// 테스트

		// backBtn = (ImageButton) findViewById(R.id.recipe_backbtn);
		// backBtn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// onBackPressed();// or Finish();
		// }
		// });

		final ViewPager vp = (ViewPager) findViewById(R.id.recipe_pager);
		adapter = new RecipePagerAdapter(getSupportFragmentManager(), mData);
		vp.setAdapter(adapter);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * {@link RecipeFirstFragment} 가격 정보 버튼 클릭
	 * 
	 * @param v
	 */
	public void onPriceBtnClick(View v) {
		Intent intent = new Intent(this, PriceInfoActivity.class);
		intent.putExtra("gridlist", mData);
		startActivity(intent);
	}
	
	/**
	 * {@link RecipeSecondFragment} 댓글 보기 버튼 클릭
	 * 
	 * @param v
	 */
	public void onReplyBtnClick(View v) {
		Intent intent = new Intent(this, ReplyActivity.class);
		intent.putExtra("recipe_id", mData.recipeId);
		startActivity(intent);
	}

	private static Bitmap blur(Context context, Bitmap sentBitmap, float radius) {

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		final RenderScript rs = RenderScript.create(context);
		final Allocation input = Allocation.createFromBitmap(rs, sentBitmap,
				Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
				Element.U8_4(rs));
		script.setRadius(radius); // 0.0f ~ 25.0f
		script.setInput(input);
		script.forEach(output);
		output.copyTo(bitmap);
		return bitmap;
	}

}
