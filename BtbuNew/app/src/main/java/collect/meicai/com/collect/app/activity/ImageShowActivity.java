package collect.meicai.com.collect.app.activity;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import collect.meicai.com.collect.R;
import collect.meicai.com.collect.app.adapter.ImagePagerAdapter;
import collect.meicai.com.collect.app.imageshow.ImageShowViewPager;
import collect.meicai.com.collect.app.utils.ToastUtil;

/**
 * 图片展示
 * 用户可以点击下载
 */
public class ImageShowActivity extends BaseActivity {
	/** 图片展示 */
	private ImageShowViewPager image_pager;
	private TextView page_number;
	/** 图片下载按钮 */
	private ImageView download;
	/** 图片列表 */
	private ArrayList<String> imgsUrl;
	/** PagerAdapter */
	private ImagePagerAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_imageshow);
		initView();
		initData();
		initViewPager();
	}


	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initData() {
		imgsUrl = getIntent().getStringArrayListExtra("infos");
		page_number.setText("1" + "/" + imgsUrl.size());
	}

	private void initView() {
		image_pager = (ImageShowViewPager) findViewById(R.id.image_pager);
		page_number = (TextView) findViewById(R.id.page_number);
		download = (ImageView) findViewById(R.id.download);

		image_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(final int arg0) {
				page_number.setText((arg0 + 1) + "/" + imgsUrl.size());
				download.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageLoader.getInstance().loadImage(imgsUrl.get(arg0),new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String s, View view) {

							}

							@Override
							public void onLoadingFailed(String s, View view, FailReason failReason) {

							}

							@Override
							public void onLoadingComplete(String s, View view, Bitmap bitmap) {
								ToastUtil.show(ImageShowActivity.this,"保存图片成功" + s);
							}

							@Override
							public void onLoadingCancelled(String s, View view) {

							}
						});
					}
				});
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initViewPager() {
		if (imgsUrl != null && imgsUrl.size() != 0) {
			mAdapter = new ImagePagerAdapter(getApplicationContext(), imgsUrl);
			image_pager.setAdapter(mAdapter);
		}
	}
}
