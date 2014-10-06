package com.papos;

import java.util.*;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.*;
import android.widget.*;

public class ImageGridFragment extends Fragment {


	public static final String EXTRA_GROUP = "com.papos.group";
	
	private static final String DIALOG_GROUP = "group";
	
	private static final int REQUEST_GROUP = 0;
	
	
	private GridView mImageGrid;
	private ImageDB mImageDB;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		mImageDB = ImageDB.get(getActivity());
		
		//setRetainInstance(true);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_image_grid, null);
		
		mImageGrid = (GridView)v.findViewById(R.id.image_grid_imageGrid);
		mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Cursor c = mImageDB.getImages();
				c.moveToPosition(position);
				
				String path = c.getString(c.getColumnIndex(Images.Thumbnails.DATA));
				
				Image image = new Image(path);
			
				Intent i = new Intent(getActivity(), ImagePagerActivity.class);
				i.putExtra(ImageFragment.EXTRA_IMAGE, image);
				startActivity(i);
				
			}
		
		});
		
		ImageAdapter adapter = new ImageAdapter(getActivity(), mImageDB.getImages());
		
		mImageGrid.setAdapter(adapter);
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar a = getActivity().getActionBar();
			
			a.setTitle(R.string.app_name);
			
			//if (mSubtitleVisible)
				//getActivity().getActionBar().setSubtitle(R.string.subtitle);
		}
		
		return v;
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
		
	}
	private void openCamera() {	
		Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA); 
		startActivity(intent);
	}
	
	private void popupGroupDialog() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		GroupSelectFragment dialog = GroupSelectFragment.newInstance("", "", "");
		dialog.setTargetFragment(this, REQUEST_GROUP);
		dialog.show(fm,  DIALOG_GROUP);	
	}
	
	private void popupSearchDialog() {
		/*FragmentManager fm = getSupportFragmentManager();
		SearchSelectFragment dialog = SearchSelectFragment.newInstance("", "", "");
		//dialog.setTargetFragment(this, REQUEST_GROUP);
		
		dialog.show(fm,  DIALOG_GROUP);
		*/	
	}
	
	class ImageAdapter extends BaseAdapter {
		
		private Context mContext;
		private Cursor mCursor;
		
		//private LRUCache<String, Bitmap> mCache;
		

		public ImageAdapter(Context c, Cursor cursor) {
			mContext = c;
			mCursor = cursor;
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
			
			Uri uri = Uri.withAppendedPath(Images.Thumbnails.EXTERNAL_CONTENT_URI,
							mCursor.getString(mCursor.getColumnIndex(Images.Thumbnails._ID)));
			
			/*
			String ImagePath = mCursor.getString(mCursor.getColumnIndex("full_path"));
			Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(ImagePath), 150, 150);
			*/
			
			//Uri uri = Uri.parse(mCursor.getString(1));
			//imageView.setImageBitmap(ThumbImage);
			
			imageView.setImageURI(uri);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			//imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
			
			return imageView;
		}
	}
}
