package com.kit.imagelib.imagelooker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kit.imagelib.R;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.transformer.DepthPageTransformer;
import com.kit.imagelib.view.PhotoTextView;
import com.kit.imagelib.view.ScrollViewPager;

import java.io.Serializable;
import java.util.List;

public class ImageBrowserActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {


	public static final String POSITION = "position";
	public static final String ISDEL = "isdel";
	public static final String IMAGES = "images";


	private ScrollViewPager mSvpPager;
	private PhotoTextView mPtvPage;
	private int mTotal;

	ImageView delete;
	LinearLayout back;

	private ImageBrowserAdapter mAdapter;
	private int mPosition;
	List<ImageBean> imagesList;
	boolean isDel = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_imagebrowser);
		initViews();
		initEvents();
		init();
	}

	private void initViews() {
		mSvpPager = (ScrollViewPager) findViewById(R.id.imagebrowser_svp_pager);
		mPtvPage = (PhotoTextView) findViewById(R.id.imagebrowser_ptv_page);
		delete = (ImageView) findViewById(R.id.delete);
		back = (LinearLayout) findViewById(R.id.back);
	}

	private void initEvents() {
		mSvpPager.setOnPageChangeListener(this);
		delete.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	private void init() {
		isDel = getIntent().getBooleanExtra(ISDEL, false);
		mPosition = getIntent().getIntExtra(POSITION, 0);
		if (isDel)
			delete.setVisibility(View.VISIBLE);
		imagesList = (List<ImageBean>) getIntent().getSerializableExtra(IMAGES);
		mTotal = imagesList.size();
		if (mPosition > mTotal) {
			mPosition = mTotal - 1;
		}
		if (mTotal > 1) {
			mPosition += 1000 * mTotal;
			mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
			mAdapter = new ImageBrowserAdapter(this, imagesList);
			mSvpPager.setAdapter(mAdapter);
			mSvpPager.setPageTransformer(true, new DepthPageTransformer());
			mSvpPager.setCurrentItem(mPosition, false);
		}
		if (mTotal == 1) {
			mPtvPage.setText("1/1");
			mAdapter = new ImageBrowserAdapter(this, imagesList);
			mSvpPager.setAdapter(mAdapter);
			mSvpPager.setPageTransformer(true, new DepthPageTransformer());
			mSvpPager.setCurrentItem(mPosition, false);
		}

	}

	@Override
	public void onPageScrollStateChanged(int position) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mPosition = arg0;
		mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.delete) {
			mPosition = (mSvpPager.getCurrentItem() % mTotal);
			imagesList.remove(mPosition);
			mTotal = imagesList.size();
			if (mPosition > mTotal) {
				mPosition = mTotal - 1;
			}
			if (mTotal >= 1) {
				mPosition += 1000 * mTotal;
				mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
				mAdapter = new ImageBrowserAdapter(this, imagesList);
				mSvpPager.setAdapter(mAdapter);
				mSvpPager.setCurrentItem(mPosition, false);
			} else {
				onBackPressed();
			}
		} else if (view.getId() == R.id.back) {
			onBackPressed();
		}
	}

	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		data.putExtra("M_LIST", (Serializable) imagesList);
		setResult(RESULT_OK, data);
		ImageBrowserActivity.this.finish();
	}

}
