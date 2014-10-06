package com.papos;

public class ImageGroup {

	private String mGroup1;
	private String mGroup2;
	private String mGroup3;
	
	private int mCount;
	
	public ImageGroup(String group1, String group2, String group3) {
		mGroup1 = group1;
		mGroup2 = group2;
		mGroup3 = group3;
	}

	public String getGroup1() {
		return mGroup1;
	}

	public String getGroup2() {
		return mGroup2;
	}

	public String getGroup3() {
		return mGroup3;
	}
	
	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		mCount = count;
	}	
	
}
