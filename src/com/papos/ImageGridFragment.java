package com.papos;

import java.util.*;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.database.*;
import android.graphics.*;
import android.os.*;
import android.provider.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.papos.BitmapWorkerTask.AsyncDrawable;
import com.papos.GalleryDBHelper.ImagesTable;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ImageGridFragment extends Fragment {


	public static final String EXTRA_GROUP = "com.papos.group";
	public static final String EXTRA_SEARCH = "com.papos.search";

	private static final String DIALOG_GROUP = "group";
	private static final String DIALOG_SEARCH = "search";

	private static final int REQUEST_GROUP = 0;
	private static final int REQUEST_SEARCH = 1;


	private GridView mImageGrid;

	private GalleryDBHelper mDBHelper;

	//private ImageDB mImageDB;

	private LruCache<String, Bitmap> mMemoryCache;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//TODO : Network 문제, AsyncTask로 수정해야함
		if(android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setHasOptionsMenu(true);

		//mDBHelper = new GalleryDBHelper(this.getActivity());
		mDBHelper = GalleryDBHelper.getGalleryDBHelper(this.getActivity());
		
		//TODO : AsyncTask 로 처리해야함
		mDBHelper.syncFromMediaStore();

		//mImageDB = ImageDB.get(getActivity());

		RetainFragment retainFragment = RetainFragment.findOrCreateRetainFragment(getFragmentManager());
		mMemoryCache = retainFragment.mRetainedCache;

		if(mMemoryCache == null) {

			// Get max available VM memory, exceeding this amount will throw an
			// OutOfMemory exception. Stored in kilobytes as LruCache takes an
			// int in its constructor.
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = maxMemory / 16;

			mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					// The cache size will be measured in kilobytes rather than
					// number of items.
					return bitmap.getByteCount() / 1024;
				}

			};
			retainFragment.mRetainedCache = mMemoryCache;

		}

		//setRetainInstance(true);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_image_grid, null);

		mImageGrid = (GridView)v.findViewById(R.id.image_grid_imageGrid);
		mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Cursor c = mDBHelper.getImagesFullPath();
				c.moveToPosition(position);

				String path = c.getString(c.getColumnIndex(ImagesTable.FULL_PATH));

				Image image = new Image(path);

				Intent i = new Intent(getActivity(), ImagePagerActivity.class);
				i.putExtra(ImageFragment.EXTRA_IMAGE, image);
				startActivity(i);

			}

		});

		ImageAdapter adapter = new ImageAdapter(getActivity(), mDBHelper.getImagesFullPath());

		mImageGrid.setAdapter(adapter);


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar a = getActivity().getActionBar();

			a.setTitle(R.string.app_name);

			//if (mSubtitleVisible)
			//getActivity().getActionBar().setSubtitle(R.string.subtitle);
		}



		return v;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.image_grid, menu);


		//if (mSubtitleVisible && showSubtitle != null)
		//	showSubtitle.setTitle(R.string.hide_subtitle);
	}


	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_image_grid_camera:
			/* ** todo **
			 *  사진 촬영 후 사진 분석		
			 *  데이터베이스 수정
			 *  이름 입력받기
			 */

			/* 
			 * revised
			 * 카메라앱의 기능과 갤러리앱의 기능을 분리
			 * 여기서는 카메라를 작동시키는 기능만
			 * 사진 분석 기능은 갤러리를 오픈할때.(어떤 조건 충족시)
			 */

			openCamera();				// 카메라 열기
			break;
		case R.id.menu_image_grid_group:
			popupGroupDialog();
			break;
		case R.id.menu_image_grid_search:
			popupSearchDialog();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;

		if (requestCode == REQUEST_GROUP) {
			String first = (String)data.getSerializableExtra(GroupSelectFragment.EXTRA_GROUP_FIRST);
			String second = (String)data.getSerializableExtra(GroupSelectFragment.EXTRA_GROUP_SECOND);
			String third = (String)data.getSerializableExtra(GroupSelectFragment.EXTRA_GROUP_THIRD);

			//mCrime.setDate(date);
			//updateDate();
		}
		else if (requestCode == REQUEST_SEARCH) {
			Date date = (Date)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_DATE);
			String timeslot = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_TIMESLOT);
			String recent = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_SEASON);
			String form = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_SEASON);
			String people = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_SEASON);
			//String group = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_SEASON);
			///String distance = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_SEASON);
			String address = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_SEASON);
			String place = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_SEASON);
			String event = (String)data.getSerializableExtra(SearchSelectFragment.EXTRA_SEARCH_SEASON);
			

			//mCrime.setDate(date);
			//updateDate();
		}


	}
	private void openCamera() {	
		Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA); 
		startActivity(intent);
	}

	private void popupGroupDialog() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		GroupSelectFragment dialog = GroupSelectFragment.newInstance();
		dialog.setTargetFragment(this, REQUEST_GROUP);
		dialog.show(fm, DIALOG_GROUP);	
	}

	private void popupSearchDialog() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		SearchSelectFragment dialog = SearchSelectFragment.newInstance();
		dialog.setTargetFragment(this, REQUEST_SEARCH);
		dialog.show(fm, DIALOG_SEARCH);

	}

	class ImageAdapter extends BaseAdapter {

		private Context mContext;
		private Cursor mCursor;
		private Bitmap mPlaceHolderBitmap;

		//private LRUCache<String, Bitmap> mCache;


		public ImageAdapter(Context c, Cursor cursor) {
			mContext = c;
			mCursor = cursor;

			Resources rs = c.getApplicationContext().getResources();
			mPlaceHolderBitmap = BitmapFactory.decodeResource(rs, R.drawable.placeholder);
			//mCache = new LRUCache<String, Bitmap>(30);

		}

		public int getCount() {
			return mCursor.getCount();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
			}
			else {
				imageView = (ImageView) convertView;
			}

			// Thumbnails DB를 순회하며 ImageView에 아이템 추가 
			mCursor.moveToPosition(position);

			String path = mCursor.getString(mCursor.getColumnIndex(ImagesTable.FULL_PATH));

			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new GridView.LayoutParams(160, 160));
			imageView.setPadding(1, 1, 1, 1);

			final String imageKey = path;
			final Bitmap bitmap = getBitmapFromMemCache(imageKey);

			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
			else {
				if (BitmapWorkerTask.cancelPotentialWork(path, imageView)) {
					final BitmapWorkerTask task = new BitmapWorkerTask(imageView, mMemoryCache);
					final AsyncDrawable asyncDrawable = new AsyncDrawable(mPlaceHolderBitmap, task);
					imageView.setImageDrawable(asyncDrawable);
					task.execute(path);
				}
			}

			Log.i("img", "" + position);
			return imageView;
		}

	}
}
