package com.papos;

import java.text.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class SearchSelectFragment extends DialogFragment {

	private static final int REQUEST_DATE = 0;
	private static final String DIALOG_DATE = "date";
	
	public static final String EXTRA_SEARCH_DATE = "com.papos.search_date";
	public static final String EXTRA_SEARCH_TIMESLOT = "com.papos.search_timeslot";
	public static final String EXTRA_SEARCH_SEASON = "com.papos.search_season";
	public static final String EXTRA_SEARCH_RECENT = "com.papos.search_recent";
	public static final String EXTRA_SEARCH_FORM = "com.papos.search_form";
	public static final String EXTRA_SEARCH_PERSON = "com.papos.search_person";
	//public static final String EXTRA_SEARCH_GROUP = "com.papos.search_group";
	//public static final String EXTRA_SEARCH_DISTANCE = "com.papos.search_distance";
	public static final String EXTRA_SEARCH_ADDRESS = "com.papos.search_address";
	public static final String EXTRA_SEARCH_PLACE = "com.papos.search_place";
	public static final String EXTRA_SEARCH_EVENT = "com.papos.search_event";
	
	private Button mDateButton;
	private Spinner mTimeSlotSpinner;
	private Spinner mSeasonSpinner;
	private Spinner mRecentSpinner;
	private Spinner mFormSpinner;
	private Spinner mPersonSpinner;
	//private Spinner mGroupSpinner;
	//private Spinner mDistanceSpinner;
	private Spinner mAddressSpinner;
	private Spinner mPlaceSpinner;
	private Spinner mEventSpinner;
	
	private Date mDate;
	private String mTimeSlot;
	private String mSeason;
	private String mRecent;
	private String mForm;
	private String mPerson;
	//private String mGroup;
	//private String mDistance;
	private String mAddress;
	private String mPlace;
	private String mEvent;
	
	
	public static SearchSelectFragment newInstance() {
		return newInstance(new Date(), "", "", "", "", "", "", "", "", "", "");
	}
	
	public static SearchSelectFragment newInstance(Date date, String timeslot, String season,
			String recent, String form, String person, String group, String distance,
			String address, String place, String event) {
		Bundle args = new Bundle();
		
		args.putSerializable(EXTRA_SEARCH_DATE, date);
		args.putSerializable(EXTRA_SEARCH_TIMESLOT, timeslot);
		args.putSerializable(EXTRA_SEARCH_SEASON, season);
		args.putSerializable(EXTRA_SEARCH_RECENT, recent);
		args.putSerializable(EXTRA_SEARCH_FORM, form);
		args.putSerializable(EXTRA_SEARCH_PERSON, person);
		//args.putSerializable(EXTRA_SEARCH_GROUP, group);
		//args.putSerializable(EXTRA_SEARCH_DISTANCE, distance);
		args.putSerializable(EXTRA_SEARCH_ADDRESS, address);
		args.putSerializable(EXTRA_SEARCH_PLACE, place);
		args.putSerializable(EXTRA_SEARCH_EVENT, event);
		
		SearchSelectFragment fragment = new SearchSelectFragment();
		fragment.setArguments(args);

		return fragment;
	}
	
	private void sendResult(int resultCode) {

		if (getTargetFragment() == null)
			return;

		Intent i = new Intent();
		i.putExtra(EXTRA_SEARCH_DATE, mDate);
		i.putExtra(EXTRA_SEARCH_TIMESLOT, mTimeSlot);
		i.putExtra(EXTRA_SEARCH_SEASON, mSeason);
		i.putExtra(EXTRA_SEARCH_RECENT, mRecent);
		i.putExtra(EXTRA_SEARCH_FORM, mForm);
		i.putExtra(EXTRA_SEARCH_PERSON, mPerson);
		//i.putExtra(EXTRA_SEARCH_GROUP, mGroup);
		//i.putExtra(EXTRA_SEARCH_DISTANCE, mDistance);
		i.putExtra(EXTRA_SEARCH_ADDRESS, mAddress);
		i.putExtra(EXTRA_SEARCH_PLACE, mPlace);
		i.putExtra(EXTRA_SEARCH_EVENT, mEvent);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mDate = (Date)getArguments().getSerializable(EXTRA_SEARCH_DATE);
		mTimeSlot = (String)getArguments().getSerializable(EXTRA_SEARCH_TIMESLOT);
		mSeason = (String)getArguments().getSerializable(EXTRA_SEARCH_SEASON);
		mRecent = (String)getArguments().getSerializable(EXTRA_SEARCH_RECENT);
		mForm = (String)getArguments().getSerializable(EXTRA_SEARCH_FORM);
		mPerson = (String)getArguments().getSerializable(EXTRA_SEARCH_PERSON);
		//mGroup = (String)getArguments().getSerializable(EXTRA_SEARCH_GROUP);
		//mDistance = (String)getArguments().getSerializable(EXTRA_SEARCH_DISTANCE);
		mAddress = (String)getArguments().getSerializable(EXTRA_SEARCH_ADDRESS);
		mPlace = (String)getArguments().getSerializable(EXTRA_SEARCH_PLACE);
		mEvent = (String)getArguments().getSerializable(EXTRA_SEARCH_EVENT);

		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_search_select, null);
		
		mDateButton = (Button)v.findViewById(R.id.dialog_search_select_dateButton);
		mTimeSlotSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_timeslotSpinner);
		mSeasonSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_seasonSpinner);
		mRecentSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_recentSpinner);
		mFormSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_formSpinner);
		mPersonSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_peopleSpinner);
		//mGroupSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_groupSpinner);
		//mDistanceSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_distanceSpinner);
		mAddressSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_locationSpinner);
		mPlaceSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_placeSpinner);
		mEventSpinner = (Spinner)v.findViewById(R.id.dialog_search_select_eventSpinner);
		

		updateDate();
		mDateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mDate);
				dialog.setTargetFragment(SearchSelectFragment.this, REQUEST_DATE);
				dialog.show(fm,  DIALOG_DATE);	
			}
			
		});
		
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

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mDate.setTime(date.getTime());
			updateDate();
		}
		
	}
	
	private void updateDate() {
		DateFormat dateformat = DateFormat.getDateInstance();
		mDateButton.setText(dateformat.format(mDate));
	}
}
