package com.papos;

import android.annotation.*;
import android.app.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

public class ImageFragment extends Fragment {

	public static final String EXTRA_IMAGE_NAME = "com.papos.image_name";
	public static final String EXTRA_IMAGE_PATH = "com.papos.image_path";
	public static final String EXTRA_IMAGE = "com.papos.image";
	
	private static final String DIALOG_DATE_AND_TIME = "date & time";
	///private static final String DIALOG_TIME = "time";
	
	
	private static final int REQUEST_DATE_AND_TIME = 0;
	//private static final int REQUEST_TIME = 1;

	private Image mImage;
	private ImageView mImageView;


	public static ImageFragment newInstance(Image image) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_IMAGE, image);
		
		ImageFragment fragment = new ImageFragment();
		fragment.setArguments(args);

		return fragment;
	}

	public ImageFragment() {
		
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		
		mImage = (Image) getArguments().getSerializable(EXTRA_IMAGE);
		
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_image, container, false);

		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null)
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		mImageView = (ImageView)v.findViewById(R.id.image_imageView);
		
		mImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActionBar actionbar = getActivity().getActionBar();
				if (actionbar.isShowing())
					actionbar.hide();
				else
					actionbar.show();
				
			}
		});

		
		
		try {
			Bitmap bm = BitmapFactory.decodeFile(mImage.getPath());
			mImageView.setImageBitmap(bm);
		}
		catch (OutOfMemoryError e) {
			Toast.makeText(getActivity(), "이미지가 너무 큽니다.", Toast.LENGTH_SHORT).show();
		}
		
		return v;
	}
}
