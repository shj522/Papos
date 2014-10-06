package com.papos;

import android.content.*;
import android.database.*;
import android.provider.MediaStore.Images;

public class ImageDB {

	private Cursor mImages;
	
	private static ImageDB sImageDB;
	private Context mContext;
	
	private ImageDB(Context context) {
		mContext = context;
		
		ContentResolver cr = context.getContentResolver();
		mImages =  cr.query(Images.Thumbnails.EXTERNAL_CONTENT_URI, null, null, null, null);
		
	}
	
	public static ImageDB get(Context context) {
		if (sImageDB == null)
			sImageDB = new ImageDB(context.getApplicationContext());
		
		return sImageDB;
	}
	
	// TODO : Reflesh DB
	
	public Cursor getImages() {
		return mImages;
	}
	
	// TODO : Search & Group
	//public Cursor getImage()
}
