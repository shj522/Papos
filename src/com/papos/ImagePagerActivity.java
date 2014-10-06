package com.papos;

import android.database.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;

public class ImagePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private Cursor mImageCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mImageCursor = ImageDB.get(this).getImages();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int position) {
				
				// TODO : 차후 수정
				String path = getPathFromCursor(position);
				Image image = new Image(path);
				
				return ImageFragment.newInstance(image);
			}

			@Override
			public int getCount() {
				return mImageCursor.getCount();
			}
			
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				String path = getPathFromCursor(position);
				Image image = new Image(path);
				
				if (image.getName() != null) {
					setTitle(image.getName());
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
			
		});
		
		Image image = (Image)getIntent().getSerializableExtra(ImageFragment.EXTRA_IMAGE);
		
		for (int i = 0; i < mImageCursor.getCount(); i++) {
			
			String path = getPathFromCursor(i);
			
			if (path.equals(image.getPath())) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
		
	}
	
	private String getPathFromCursor(int position) {
		mImageCursor.moveToPosition(position);
		
		String path = mImageCursor.getString(mImageCursor.getColumnIndex("_data"));
		
		return path;
	}
}