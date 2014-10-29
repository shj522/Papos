package com.papos;

import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.DialogFragment;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class GroupSelectFragment extends DialogFragment {

	public static final String EXTRA_GROUP_FIRST = "com.papos.group_first";
	public static final String EXTRA_GROUP_SECOND = "com.papos.group_second";
	public static final String EXTRA_GROUP_THIRD = "com.papos.group_third";

	private String mFirst;
	private String mSecond;
	private String mThird;

	private Spinner mFirstSpinner;
	private Spinner mSecondSpinner;
	private Spinner mThirdSpinner;

	private String[] mGroupList;
	private ArrayList<String> mFirstList;
	private ArrayList<String> mSecondList;
	private ArrayList<String> mThirdList;


	public static GroupSelectFragment newInstance() {
		return newInstance("", "", "");
	}
	
	public static GroupSelectFragment newInstance(String first, String second, String third) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_GROUP_FIRST, first);
		args.putSerializable(EXTRA_GROUP_SECOND, second);
		args.putSerializable(EXTRA_GROUP_THIRD, third);

		GroupSelectFragment fragment = new GroupSelectFragment();
		fragment.setArguments(args);

		return fragment;
	}

	private void sendResult(int resultCode) {

		if (getTargetFragment() == null)
			return;

		Intent i = new Intent();
		i.putExtra(EXTRA_GROUP_FIRST, mFirst);
		i.putExtra(EXTRA_GROUP_SECOND, mSecond);
		i.putExtra(EXTRA_GROUP_THIRD, mThird);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mFirst = (String)getArguments().getSerializable(EXTRA_GROUP_FIRST);
		mSecond = (String)getArguments().getSerializable(EXTRA_GROUP_SECOND);
		mThird = (String)getArguments().getSerializable(EXTRA_GROUP_THIRD);

		mGroupList = getResources().getStringArray(R.array.group_list);

		mFirstList = new ArrayList<String>();
		mSecondList = new ArrayList<String>();
		mThirdList = new ArrayList<String>();

		for (int i = 0; i < mGroupList.length; i++)
			mFirstList.add(mGroupList[i]);

		mSecondList.add("");
		mThirdList.add("");


		final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),R.layout.custom_spinner_text, mFirstList);
		final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),R.layout.custom_spinner_text, mSecondList);
		final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(),R.layout.custom_spinner_text, mThirdList);
		adapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown);
		adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown);
		adapter3.setDropDownViewResource(R.layout.custom_spinner_dropdown);

		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_group_select, null);

		mFirstSpinner = (Spinner)v.findViewById(R.id.dialog_group_select_firstspinner);
		mSecondSpinner = (Spinner)v.findViewById(R.id.dialog_group_select_secondspinner);
		mThirdSpinner = (Spinner)v.findViewById(R.id.dialog_group_select_thirdspinner);

		mFirstSpinner.setAdapter(adapter1);
		mSecondSpinner.setAdapter(adapter2);
		mThirdSpinner.setAdapter(adapter3);


		// TODO : 화면 회전 처리
		mFirstSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				mFirst = adapter1.getItem(position);

				mSecondList.clear();
				mThirdList.clear(); mThirdList.add(""); adapter3.notifyDataSetChanged();

				if (!mFirst.equals("")){
					for (int i = 0; i < mGroupList.length; i++)
						mSecondList.add(mGroupList[i]);
					mSecondList.remove(mFirst);
				}
				else 
					mSecondList.add("");

				adapter2.notifyDataSetChanged();

				mSecondSpinner.setSelection(0);
				mThirdSpinner.setSelection(0);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});


		mSecondSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				mSecond = adapter2.getItem(position);

				mThirdList.clear(); 
				if (!mSecond.equals("")){
					for (int i = 0; i < mGroupList.length; i++)
						mThirdList.add(mGroupList[i]);
					mThirdList.remove(mFirst);
					mThirdList.remove(mSecond);
				}
				else
					mThirdList.add("");

				adapter3.notifyDataSetChanged();
				mThirdSpinner.setSelection(0);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});

		mThirdSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				mThird = adapter3.getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});

		for (int i = 0; i < mFirstList.size(); i++)
			if (mFirstList.get(i).equals(mFirst))
				mFirstSpinner.setSelection(i);

		for (int i = 0; i < mSecondList.size(); i++)
			if (mSecondList.get(i).equals(mSecond))
				mSecondSpinner.setSelection(i);

		for (int i = 0; i < mThirdList.size(); i++)
			if (mThirdList.get(i).equals(mThird))
				mThirdSpinner.setSelection(i);

		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.group_select_title)
		.setPositiveButton(R.string.group, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);
			}
		})
		.setNegativeButton(R.string.cancel, null)
		.create();
	}



}
