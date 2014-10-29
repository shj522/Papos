package com.papos;
import java.io.*;
import java.text.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.json.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.location.*;
import android.provider.*;
import android.provider.MediaStore.Images;
import android.util.*;

class GalleryDBHelper extends SQLiteOpenHelper
{
	 
	private static String DB_PATH = "/data/data/com.papos/databases/";
	private static String DB_NAME = "GalleryDB.db";
	
	private static final int DB_VERSION = 1;
	
	private SQLiteDatabase mGalleryDB;
	private final Context mContext;
	
	private static GalleryDBHelper sGalleryDBHelper;
	
	//Geocoder mGeocoder;
	
	public class ImagesTable {
		// Table name
		public static final String TABLE_NAME = "Images";
		
		// Field name
		public static final String FULL_PATH = "full_path";
		public static final String FILE_NAME = "file_name";
		public static final String WIDTH = "width";
		public static final String HEIGHT = "height";
		public static final String DATE_TAKEN = "date_taken";
		
		public static final String ADDRESS = "address";
		public static final String SEASON = "season";
		public static final String RECENT = "recent";
		public static final String TIMESLOT = "timeslot";
		public static final String FORM = "form";
		
		public static final String FACE_COUNT = "face_count";
		public static final String FACE_NO = "face_no";
		public static final String GROUP_FACES_NO = "group_faces_no";
		public static final String DISTANCE = "distance";
		public static final String ADDRESS_NO = "address_no";
		public static final String EVENT = "event";
		
	}
	
	public class FaceTable {
		public static final String TABLE_NAME = "Face";
		
		public static final String FACE_NO = "face_no";
		public static final String FACE = "face_path";
		public static final String COUNT = "count";
		public static final String IGNORED = "ignored";
		public static final String PERSON_NO = "person_no";
	}

	public class GroupFacesTable {
		public static final String TABLE_NAME = "GroupFaces";
		
		public static final String FACE_NO = "group_faces_no";
		public static final String PERSON1_NO = "person1_no";
		public static final String PERSON2_NO = "person2_no";
		public static final String PERSON3_NO = "person3_no";
		public static final String IGNORED = "ignored";
		public static final String GROUP_NO = "group_no";
	}

	public class AddressTable {
		public static final String TABLE_NAME = "Address";
		
		public static final String ADDRESS_NO = "address_no";
		public static final String ADDRESS = "address";
		public static final String COUNT = "count";
		public static final String IGNORED = "ignored";
		public static final String PLACE_NO = "place_no";
	}

	public class PersonTable {
		public static final String TABLE_NAME = "Person";
		
		public static final String PERSON_NO = "person_no";
		public static final String NAME = "name";
	}
	
	public class GroupTable {
		public static final String TABLE_NAME = "Group";
		
		public static final String GROUP_NO = "group_no";
		public static final String GROUP_NAME = "group_name";
	}
	
	public class PlaceTable {
		public static final String TABLE_NAME = "Place";
		
		public static final String PLACE_NO = "place_no";
		public static final String PLACE_NAME = "place_name";
	}
	
	public class EventTable {
		public static final String TABLE_NAME = "Event";
		
		public static final String EVENT_NO = "event_no";
		public static final String EVENT_NAME = "event_name";
	}
	
	public class ImageGroupTable {
		public static final String TABLE_NAME = "ImageGroup";
		
		public static final String GROUP1 = "group1";
		public static final String GROUP2 = "group2";
		public static final String GROUP3 = "group3";
		public static final String COUNT = "count";
		public static final String COVER_IMAGE = "cover_image";
	}
	
	
	private Cursor mImagesCursor;
	
	// Constructor
	private GalleryDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context; 
	}
	
	public static GalleryDBHelper getGalleryDBHelper(Context context) {
		if (sGalleryDBHelper == null) 
			sGalleryDBHelper = new GalleryDBHelper(context.getApplicationContext());
		
		return sGalleryDBHelper;
	}
	/*
	public SQLiteDatabase getDB() {
		return mGalleryDB;
	}
	*/
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 업그레이드 작업을 처리하는 코드
		Log.w("DB upgrade", "oldver = " + oldVersion + " newver = " + newVersion);
		/*
		db.execSQL("DROP TABLE IF EXISTS MyImageDB");
		onCreate(db);
		*/
	}
	
	
	// create empty database and copy select database
	public void createDatabase() throws IOException {
		if (!checkDatabase()) {
			this.getWritableDatabase();
			try {
				copyDatabase();
			}
			catch (IOException e) {
				throw new Error("Error copying database from system assets");
			}
		}
	}
	
	private boolean checkDatabase() {
		SQLiteDatabase checkableDatabase = null;
		try {
			checkableDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
		}
		catch (SQLiteException e) {
			// return false if myDB is not exist
		}
		if (checkableDatabase != null) {
			checkableDatabase.close();
		}
		return checkableDatabase != null ? true : false;
	}
	
	private void copyDatabase() throws IOException {
		
		InputStream input = mContext.getAssets().open(DB_NAME);
		OutputStream output = new FileOutputStream(DB_PATH + DB_NAME);
		
		byte[] buffer = new byte[1024];
		int length;
		while ( (length = input.read(buffer)) > 0 ){
			output.write(buffer, 0, length);
		}
		
		output.flush();
		output.close();
		input.close();
		
	}
	
	
public Cursor getImagesFullPath() {
		
		String[] columns = { ImagesTable.FULL_PATH };
		
		SQLiteDatabase sd = getReadableDatabase();
		
		Cursor cursor = sd.query(ImagesTable.TABLE_NAME, columns, null, null, null, null, null);
		
		//sd.close();
		
		return cursor;
		
	}
	
	public Cursor getImagesFullPath(Date date, String timeslot, String season, String recent, 
			String form, String person, String address, String place, String event) {
		
		
		String[] columns = { ImagesTable.FULL_PATH };
		
		String selection = "";
		
		List<String> where = new ArrayList<String>();
		

		//if (date != null)
		//	selection = "";
		
		if (timeslot != null) {
			if (!selection.equals(""))
				selection += "AND ";
			
			selection += "timeslot = ? ";
			where.add(timeslot);
		}
		
		if (season != null) {
			if (!selection.equals(""))
				selection += "AND ";
		
			selection += "season = ? ";
			where.add(season);
		}
		
		if (form != null) {
			if (!selection.equals(""))
				selection += "AND ";
		
			selection += "form = ?";
			where.add(form);
		}
		
		if (person != null) {
			if (!selection.equals(""))
				selection += "AND ";
			
			selection += "person = ? ";
			where.add(person);
		}
			
		
		if (address != null) {
			if (!selection.equals(""))
				selection += "AND ";
			
			selection += "address = ? ";
			where.add(address);
		}
		
		if (event != null) {
			if (!selection.equals(""))
				selection += "AND ";
			
			selection += "event = ? ";
			where.add(event);
		}
			
		String[] selectionArgs = new String[where.size()];
		where.toArray(selectionArgs);
		
		SQLiteDatabase sd = getReadableDatabase();
		
		Log.i("query", selection);
		
		Cursor cursor = sd.query(ImagesTable.TABLE_NAME, columns, 
				selection, selectionArgs, null, null, null);
		
		return cursor; 
	}
	
	
	public long addPerson(String name) {
		
		ContentValues cv = new ContentValues();
		cv.put(PersonTable.NAME, name);
	
		SQLiteDatabase wdb = getWritableDatabase();
		
		
		long result = wdb.insert(PersonTable.TABLE_NAME, null, cv);
		
		
		return result;
		
	}
	
	// Add record to the Image table
	public long addImage(String filename) {
		
		ContentValues cv = new ContentValues();
		cv.put(ImagesTable.FILE_NAME, filename);
		
		SQLiteDatabase wdb = getWritableDatabase();
		long result = wdb.insert(ImagesTable.TABLE_NAME, ImagesTable.FILE_NAME, cv);
		
		return result;
	}
	
	// Add record to the Oneface table
	public long addFace(String face) {
		
		ContentValues cv = new ContentValues();
		cv.put(FaceTable.FACE, face);
		
		//
		return 0;
	}
	
	/*
	public void openDatabase() throws SQLException {
		mGalleryDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
	}
	*/
	
	@Override
	public synchronized void close() {
		if(mGalleryDB != null)
			mGalleryDB.close();
		super.close();
	}
	
	
	// TODO : AsyncTask로 처리해야
	public void syncFromMediaStore() {
		
		long s = System.currentTimeMillis();
		
		
		try {
			createDatabase();
		} catch (IOException e) {
			
		}
		
		//openDatabase();
		//mGalleryDB = getWritableDatabase();
		SQLiteDatabase sd = getWritableDatabase();
		
		sd.execSQL("delete from Images;");
		
		
		String[] projection = 
			{ Images.ImageColumns.DATA,
				Images.ImageColumns.DISPLAY_NAME,
				Images.ImageColumns.WIDTH,
				Images.ImageColumns.HEIGHT,
				Images.ImageColumns.DATE_TAKEN,
				Images.ImageColumns.LATITUDE,
				Images.ImageColumns.LONGITUDE
			};
		
		
		//printCursor(c);
		Cursor c = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
		//Cursor tc = getContentResolver().query(Images.Thumbnails.EXTERNAL_CONTENT_URI, null, null, null, null);	
		//printCursor(tc);
		//Log.i("media", "" + tc.getCount());
		//printCursor(tc);
		
		c.moveToPosition(-1);
		
		int COL_DATA = c.getColumnIndex(Images.ImageColumns.DATA);
		int COL_DISPLAY_NAME = c.getColumnIndex(Images.ImageColumns.DISPLAY_NAME);
		int COL_WIDTH = c.getColumnIndex(Images.ImageColumns.WIDTH);
		int COL_HEIGHT = c.getColumnIndex(Images.ImageColumns.HEIGHT);
		int COL_DATE_TAKEN = c.getColumnIndex(Images.ImageColumns.DATE_TAKEN);
		int COL_LATITUDE = c.getColumnIndex(Images.ImageColumns.LATITUDE);
		int COL_LONGITUDE = c.getColumnIndex(Images.ImageColumns.LONGITUDE);
		
		// TODO : 성능 개선 작업 필요 .. 현재 13초
		while (c.moveToNext()) {
		
			Log.i("sync", "" + c.getPosition());
			
			ContentValues cv = new ContentValues();
			
			
			String fullPath = c.getString(COL_DATA);
			String fileName = c.getString(COL_DISPLAY_NAME);
			
			int width = c.getInt(COL_WIDTH);
			int height = c.getInt(COL_HEIGHT);
			
			
			long date_taken = c.getLong(COL_DATE_TAKEN);
			Date date = new Date(date_taken);
			
			
			double latitude = c.getDouble(COL_LATITUDE);
			double longitude = c.getDouble(COL_LONGITUDE);
			
			String season = getSeasonFromDate(date);
			String recent = getRecentFromDate(date);
			String time_slot = getTimeslotFromDate(date);
			
			String form = getFormFromWidthHeight(width, height);

			String location = getLocationFromGeoPoint(latitude, longitude);
		
			
			cv.put(ImagesTable.FULL_PATH, fullPath);
			cv.put(ImagesTable.FILE_NAME, fileName);
			cv.put(ImagesTable.WIDTH, width);
			cv.put(ImagesTable.HEIGHT, height);
			cv.put(ImagesTable.DATE_TAKEN, date_taken);
			
			cv.put(ImagesTable.ADDRESS, location);
			
			cv.put(ImagesTable.SEASON, season);
			cv.put(ImagesTable.RECENT, recent);
			cv.put(ImagesTable.TIMESLOT, time_slot);
			
			cv.put(ImagesTable.FORM, form);
			
			sd.insert(ImagesTable.TABLE_NAME, null, cv);
			
		}

		
		c.close();
		
		
		sd.close();
		
		Log.i("after insert", "" + (System.currentTimeMillis() - s));
		
	}
	
	private String getLocationFromGeoPoint(double latitude, double longitude) {
		
		String address = "";
		
		if (latitude == 0.0d || longitude == 0.0d)
			return address;
		
		List<Address> addresses = null;
		
		try {
			addresses = getStringFromLocation(latitude, longitude); //mGeocoder.getFromLocation(latitude, longitude, 1);
			
			if (addresses.size() > 0)
				address = addresses.get(0).getAddressLine(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return address;
	}

	private static List<Address> getStringFromLocation(double lat, double lng) throws ClientProtocolException, IOException, JSONException {

	    String address = String.format(Locale.KOREAN,
	    		"http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language=" + Locale.getDefault().getCountry(), lat, lng); 
	    HttpGet httpGet = new HttpGet(address);
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();

	    List<Address> retList = null;

	    response = client.execute(httpGet);
	    HttpEntity entity = response.getEntity();
	    InputStream stream = entity.getContent();
	    int b;
	    while ((b = stream.read()) != -1) {
	        stringBuilder.append((char) b);
	    }

	    JSONObject jsonObject = new JSONObject();
	    jsonObject = new JSONObject(stringBuilder.toString());

	    retList = new ArrayList<Address>();

	    if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
	        JSONArray results = jsonObject.getJSONArray("results");
	        for (int i = 0; i < results.length(); i++) {
	            JSONObject result = results.getJSONObject(i);
	            String indiStr = result.getString("formatted_address");
	            Address addr = new Address(Locale.getDefault());
	            addr.setAddressLine(0, indiStr);
	            retList.add(addr);
	        }
	    }

	    return retList;
	}
	
	private String getFormFromWidthHeight(int width, int height) {
		String form = "Square";
		
		if (width > height && Double.compare((double)width/(double)height, 1.1d) == 1)
			form = "Wide";
		else if (width < height && Double.compare((double)height/(double)width, 1.1d) == 1)
			form = "Height";
			
		return form;
	}

	private String getTimeslotFromDate(Date date) {
		String timeslot = "";
		
		int hour = date.getHours();	// TODO : deprecated
		
		if (hour >= 0 && hour < 7)
			timeslot = "Dawn";
		else if (hour >= 7 && hour < 12)
			timeslot = "Morning";
		else if (hour >= 12 && hour < 17)
			timeslot = "Afternoon";
		else if (hour >= 17 && hour < 19)
			timeslot = "Evening";
		else if (hour >= 19 && hour <= 23)
			timeslot = "Night";
		
		return timeslot;
	}

	private String getRecentFromDate(Date date) {
		
		/*
		int HOURS = 24;
		int MINS = 60;
		int SECS = 60;
		int MILLIS = 1000;
		
		int DAY_TIME = HOURS * MINS * SECS * MILLIS;
		
		long YEAR = 365 * DAY_TIME;						// 1 year = 365days * 24hours * 60mins * 60secs * 1000millis
		long SIX_MONTH = 183 * DAY_TIME;				// 6 month = 181days * 24hours * 60mins * 60secs * 1000millis
		long THREE_MONTH = 92 * DAY_TIME;
		long MONTH = 31 * DAY_TIME;
		long WEEK = 7 * DAY_TIME;
		*/
		
		String recent = "";
		
		long date_taken = date.getTime();
		long current_time = System.currentTimeMillis();		// TODO : 시간 최소화
		long time_span = current_time - date_taken;
		
		
		long diffInSeconds = time_span / 1000;

	    long diff[] = new long[] { 0, 0, 0, 0 };
	    /* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
	    /* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
	    /* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
	    /* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));
	    
	    
	    
	    // TODO : 검색 문제 있음..
		if (diff[0] <= 365) {
			recent = "In 1 year";	// 5
			
			if (diff[0] <= 183) {
				recent = "In 6 month";	// 4
				
				if (diff[0] <= 92) {
					recent = "In 3 month";	// 3
					
					if (diff[0] <= 31) {
						recent = "In 1 month";	// 2
						
						if (diff[0] <= 7)
							recent = "In 1 week";	// 1
					}
				}
			}
		}
		
		return recent;
	}

	private void printDate(long dm) {
		Date date = new Date(dm);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		
		Log.i("Date", sdf.format(date));
		
	}
	
	public String getSeasonFromDate(Date date) {
		String season = "";
		
		int month = date.getMonth();		// TODO : deprecated
		
		if (month >= 3 && month <= 5)
			season = "Spring";
		else if (month >= 6 && month <= 8)
			season = "Summer";
		else if (month >= 9 && month <= 11)
			season = "Autumn";
		else if ((month >= 1 && month <= 2) || month == 12)
			season = "Winter";
		
		return season;
	}
	
	
}