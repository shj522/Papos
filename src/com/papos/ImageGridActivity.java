package com.papos;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;

public class ImageGridActivity extends FragmentActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.container);
		
		if (fragment == null) {
			fragment = new ImageGridFragment();
			fm.beginTransaction()
				.add(R.id.container, fragment)
				.commit();
		}
	}
	
}
