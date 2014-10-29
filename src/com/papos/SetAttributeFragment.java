package com.papos;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class SetAttributeFragment extends DialogFragment {

	private static final int REQUEST_PERSON = 0;	
	private static final int REQUEST_PLACE = 0;
	private static final int REQUEST_EVENT = 0;

	private static final String DIALOG_PERSON = "person";
	private static final String DIALOG_PLACE = "place";
	private static final String DIALOG_EVENT = "event";

	public static final String EXTRA_SET_PERSON = "com.papos.set_person";
	public static final String EXTRA_SET_PLACE = "com.papos.set_place";
	public static final String EXTRA_SET_EVENT = "com.papos.set_event";

	private Spinner mPersonSpinner;
	private Spinner mPlaceSpinner;
	private Spinner mEventSpinner;

	private ImageButton mAddPersonButton;
	private ImageButton mAddPlaceButton;
	private ImageButton mAddEventButton;
	
	private String mPerson;
	private String mPlace;
	private String mEvent;

	public static SetAttributeFragment newInstance() {
		return newInstance("", "", "");
	}

	public static SetAttributeFragment newInstance(String people, String place, String event) {
		Bundle args = new Bundle();

		args.putSerializable(EXTRA_SET_PERSON, people);
		args.putSerializable(EXTRA_SET_PLACE, place);
		args.putSerializable(EXTRA_SET_EVENT, event);

		SetAttributeFragment fragment = new SetAttributeFragment();
		fragment.setArguments(args);

		return fragment;
	}

	private void sendResult(int resultCode) {

		if (getTargetFragment() == null)
			return;

		Intent i = new Intent();
		i.putExtra(EXTRA_SET_PERSON, mPerson);
		i.putExtra(EXTRA_SET_PLACE, mPlace);
		i.putExtra(EXTRA_SET_EVENT, mEvent);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mPerson = (String)getArguments().getSerializable(EXTRA_SET_PERSON);
		mPlace = (String)getArguments().getSerializable(EXTRA_SET_PLACE);
		mEvent = (String)getArguments().getSerializable(EXTRA_SET_EVENT);


		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_set_attribute, null);

		mPersonSpinner = (Spinner)v.findViewById(R.id.dialog_set_attribute_personSpinner);
		mPlaceSpinner = (Spinner)v.findViewById(R.id.dialog_set_attribute_placeSpinner);
		mEventSpinner = (Spinner)v.findViewById(R.id.dialog_set_attribute_eventSpinner);

		
		
		mAddPersonButton = (ImageButton)v.findViewById(R.id.dialog_set_attribute_addpersonButton);
		mAddPlaceButton = (ImageButton)v.findViewById(R.id.dialog_set_attribute_addplaceButton);
		mAddEventButton = (ImageButton)v.findViewById(R.id.dialog_set_attribute_addeventButton);
		
		
		
		mAddPersonButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				AddPersonFragment dialog = AddPersonFragment.newInstance();
				dialog.setTargetFragment(SetAttributeFragment.this, REQUEST_PERSON);
				dialog.show(fm,  DIALOG_PERSON);	
			}
		});
		
		mAddPlaceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		mAddEventButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		
		
		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.set_attribute_title)
		.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);
			}
		})
		.setNegativeButton(R.string.cancel, null)
		.create();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;

		if (requestCode == REQUEST_PERSON) {
			mPerson = (String)data.getSerializableExtra(AddPersonFragment.EXTRA_PERSON);
			
			GalleryDBHelper gdbh = GalleryDBHelper.getGalleryDBHelper(getActivity());
			
			gdbh.addPerson(mPerson);
			
			
		}
		else if (requestCode == REQUEST_PLACE) {
			//mPlace = (Date)data.getSerializableExtra(PlacePickerFragment.EXTRA_PLACE);

		}
		else if (requestCode == REQUEST_EVENT) {
			//mEvent = (Date)data.getSerializableExtra(EventPickerFragment.EXTRA_EVENT);
		}


	}

}
