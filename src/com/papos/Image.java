package com.papos;

import android.net.*;

public class Image implements java.io.Serializable  {

	private static final long serialVersionUID = 1L;
	
	private String mName;
	private String mPath;
	
	public Image(String path) {
		mPath = path;
		mName = Uri.parse(path).getLastPathSegment();
	}
	
	public String getName() {
		return mName;
	}
	
	public String getPath() {
		return mPath;
	}
	
	public void setPath(String path) {
		mPath = path;
	}
	
}
