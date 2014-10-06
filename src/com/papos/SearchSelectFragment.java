package com.papos;

import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.DialogFragment;
import android.view.*;

public class SearchSelectFragment extends DialogFragment {

	public static final String EXTRA_SEARCH_DATE = "com.papos.search_date";

	private Date mDate;
	
	public static SearchSelectFragment newInstance() {
		Bundle args = new Bundle();
		
		//args.putSerializable(EXTRA_SEARCH_DATE, mDate);
		
		SearchSelectFragment fragment = new SearchSelectFragment();
		fragment.setArguments(args);

		return fragment;
	}
	
	private void sendResult(int resultCode) {

		if (getTargetFragment() == null)
			return;

		Intent i = new Intent();
		i.putExtra(EXTRA_SEARCH_DATE, mDate);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mDate = (Date)getArguments().getSerializable(EXTRA_SEARCH_DATE);

		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_search_select, null);

		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.search_select_title)
		.setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);
			}
		})
		.setNegativeButton(R.string.cancel, null)
		.create();
	}

	
}
