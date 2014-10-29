package com.papos;

import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.DialogFragment;
import android.view.*;
import android.widget.*;
import android.widget.DatePicker.OnDateChangedListener;

public class AddPersonFragment extends DialogFragment {

	public static final String EXTRA_PERSON = "com.papos.person";
	
	private String mPerson;
	EditText mPersonEdit;
	
	public static AddPersonFragment newInstance() {
		return newInstance("");
	}
	
	public static AddPersonFragment newInstance(String person) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_PERSON, person);
		
		AddPersonFragment fragment = new AddPersonFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private void sendResult(int resultCode) {
	
		if (getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_PERSON, mPerson);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mPerson = (String)getArguments().getSerializable(EXTRA_PERSON);
				
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_person, null);
		
		mPersonEdit = (EditText)v.findViewById(R.id.dialog_add_person_name);
		
		
		return new AlertDialog.Builder(getActivity())
			.setView(v)	
			.setTitle(R.string.add_person_title)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					sendResult(Activity.RESULT_OK);
					
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.create();
	}
	
}
