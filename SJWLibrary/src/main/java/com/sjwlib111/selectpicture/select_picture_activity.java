package com.sjwlib111.selectpicture;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjwlib111.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 类描述:  主函数
 * 功能详细描述:Android 4.4前后版本读取图库图片方式的变化
 */
public class select_picture_activity extends Activity {

	// 设置裁剪默认大小
	private final int default_width = 320;
	private final int default_height = 320;
	private final int quality = 100;

	private RelativeLayout mAcountHeadIconLayout;

	//保存图片本地路径
	private static String app_path = "";
	private static String img_cache_path = "";

	private static String img_filename = "photo.jpeg";
	private static String img_new_filename = "s_photo.jpeg";

	//常量定义
	private static final int REQCODE_CAMERA_A_PICTURE = 10;
	private static final int REQCODE_SELECT_A_PICTURE = 20;
	private static final int REQCODE_SELECT_A_PICTURE_AFTER_KITKAT = 50;
	private static final int REQCODE_SHOWIMG_FROM_CAMERA_CORPED = 30;
	private static final int REQCODE_SHOWIMG_FROM_ALBUM_CORPED_KITKAT = 40;

	// extra 定义 注：此4个extra需要由调用activity传递，否则会报错
	public static final String EXTRA_APP_PATH = "app_path";
	public static final String EXTRA_IMG_CACHE = "img_cache";
	public static final String EXTRA_IMG_CACHE_PATH = "img_cache_path";
	public static final String EXTRA_FILENAME = "filename";

	private String mAlbumPicturePath = null;

	File fileone = null;
	File filetwo = null;

	//版本比较：是否是4.4及以上版本
	final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	private Bitmap g_bitmap;
	//private ImageView img_picture;
	LinearLayout ll_sel_pic;

	//LinearLayout ll_sel_pic = (LinearLayout) findViewById(R.id.ll_sel_pic);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_picture);

		getWindow().setGravity(Gravity.BOTTOM);

		//WindowManager m = getWindowManager();
		//Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
		p.width = ActionBar.LayoutParams.MATCH_PARENT;
		//p.height = (int) (d.getHeight() * 1); // 宽度设置为屏幕的0.7
		getWindow().setAttributes(p);

		app_path = getIntent().getStringExtra(EXTRA_APP_PATH);
		img_cache_path = getIntent().getStringExtra(EXTRA_IMG_CACHE_PATH);
		File directory = new File(app_path);
		File imagepath = new File(img_cache_path);
		if (!directory.exists()) {
			Log.i("debug", "directory.mkdir()");
			directory.mkdir();
		}
		if (!imagepath.exists()) {
			Log.i("debug", "imagepath.mkdir()");
			imagepath.mkdir();
		}

		String filename = getIntent().getStringExtra(EXTRA_FILENAME);
		if(false == filename.equals("")) {
			img_new_filename = filename;
		}

		fileone = new File(img_cache_path, img_filename);
		filetwo = new File(img_cache_path, img_new_filename);

		try {
			if (!fileone.exists() && !filetwo.exists()) {
				fileone.createNewFile();
				filetwo.createNewFile();
			}
		} catch (Exception e) {

		}

		ll_sel_pic = (LinearLayout) findViewById(R.id.ll_sel_pic);
		ll_sel_pic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	protected void onDestroy() {
		if(g_bitmap != null && g_bitmap.isRecycled() == false)
			g_bitmap.recycle();
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return false;
		}
		return true;
	}

	public void back(View v) {
		finish();
	}

	public void saveGoBack() {

		// 压缩并保存图片
		if(g_bitmap != null) {
			//CompressSaveBitmap();

			Intent intent = new Intent();
			intent.putExtra("filefullname", img_cache_path + img_new_filename);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}else{
			Toast.makeText(this, "保存图片失败!", Toast.LENGTH_SHORT).show();
		}
	}

	// bitmap设置采样率重新压缩图片大小
	private void CompressSaveBitmap() {
		File file = new File(img_cache_path + img_new_filename);
		if(file.exists()) file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			g_bitmap.compress(Bitmap.CompressFormat.JPEG, quality ,out);
			out.flush();
			out.close();
			Log.i("SJW-savejpg","已保存");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	private Bitmap compressImageFromFile(final String srcPath) {
//		BitmapFactory.Options newOpts = new BitmapFactory.Options();
//		newOpts.inJustDecodeBounds = true;//只读边,不读内容
//		//Bitmap g_bitmap = getBitMapByUri(Uri.fromFile(new File(img_cache_path, img_new_filename))); // 显示照片
//
//
//		newOpts.inJustDecodeBounds = false;
//		int w = newOpts.outWidth;
//		int h = newOpts.outHeight;
//		float hh = default_height;//
//		float ww = default_width;//
//		float be = recent;
//		if (w > h && w > ww) {
//			be = (int) (newOpts.outWidth / ww);
//		} else if (w < h && h > hh) {
//			be = (int) (newOpts.outHeight / hh);
//		}
//		if (be <= 0)
//			be = 1;
//		newOpts.inSampleSize = 1;//设置采样率
//
//		newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
//		newOpts.inPurgeable = true;// 同时设置才会有效
//		newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
//
//		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//		//		return compressBmpFromBmp(g_bitmap);//原来的方法调用了这个方法企图进行二次压缩
//		//其实是无效的,大家尽管尝试
//		return bitmap;
//	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}


	public void camera(View v) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(img_cache_path, img_filename)));
		startActivityForResult(intent, REQCODE_CAMERA_A_PICTURE);
		Log.i("debug", "REQCODE_CAMERA_A_PICTURE");

	}

	public void album(View v) {
		if (mIsKitKat) {
			selectImageUriAfterKikat();
		} else {
			cropImageUri();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if  (requestCode == REQCODE_CAMERA_A_PICTURE) { // 相机拍照
			Log.i("debug", "REQCODE_CAMERA_A_PICTURE-resultCode:" + resultCode);
			if (resultCode == RESULT_OK) {
				cameraCropImageUri(Uri.fromFile(new File(img_cache_path, img_filename)));
			} else {
				//Toast.makeText(select_picture_activity.this, "已取消设置！", Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == REQCODE_SELECT_A_PICTURE) { // 显示图片：4.4以下版本
			if (resultCode == RESULT_OK && null != data) {
				Log.i("debug", "4.4以下的");
				g_bitmap = getBitMapByUri(Uri.fromFile(new File(img_cache_path, img_new_filename))); // 显示照片
				//img_picture.setImageBitmap(g_bitmap);
				saveGoBack();
			} else if (resultCode == RESULT_CANCELED) {
//				Toast.makeText(select_picture_activity.this, "已取消设置！", Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == REQCODE_SELECT_A_PICTURE_AFTER_KITKAT) { // 从相册选择图片：4.4及以上版本
			if (resultCode == RESULT_OK && null != data) {
				Log.i("debug", "4.4以上的");
				mAlbumPicturePath = getPath(getApplicationContext(), data.getData());
				cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath))); // 开始裁剪图片
			} else if (resultCode == RESULT_CANCELED) {
//				Toast.makeText(select_picture_activity.this, "已取消设置！", Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == REQCODE_SHOWIMG_FROM_ALBUM_CORPED_KITKAT) { // 裁剪之后显示图片（针对安卓4.4从相册选择图片并裁剪）
			Log.i("debug", "4.4以上上的 RESULT_OK");


			g_bitmap = getBitMapByUri(Uri.fromFile(new File(img_cache_path, img_new_filename))); // 显示照片
			//img_picture.setImageBitmap(g_bitmap);
			saveGoBack();

		}else if (requestCode == REQCODE_SHOWIMG_FROM_CAMERA_CORPED) { // 相机拍照并裁剪后
			//拍照的设置头像  不考虑版本

			if (resultCode == RESULT_OK && null != data) {
				g_bitmap = getBitMapByUri(Uri.fromFile(new File(img_cache_path, img_new_filename))); // 显示照片
				//img_picture.setImageBitmap(g_bitmap);
				saveGoBack();
			} else if (resultCode == RESULT_CANCELED) {
//				Toast.makeText(select_picture_activity.this, "已取消设置！", Toast.LENGTH_SHORT).show();
			} else {
//				Toast.makeText(select_picture_activity.this, "已取消设置！", Toast.LENGTH_SHORT).show();
			}
			//			}
		}
	}

	/** <br>功能简述:裁剪图片方法实现---------------------- 相册
	 * <br>功能详细描述:
	 * <br>注意: 安卓4.4以下版本已经集成裁剪功能，需要传入参数指定
	 */
	private void cropImageUri() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(img_cache_path, img_new_filename)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, REQCODE_SELECT_A_PICTURE);
	}


	/**
	 *  <br>功能简述:4.4以上裁剪图片方法实现---------------------- 相册
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void selectImageUriAfterKikat() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(intent, REQCODE_SELECT_A_PICTURE_AFTER_KITKAT);
	}

	/**
	 * <br>功能简述:裁剪图片方法实现----------------------相机
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param uri
	 */
	private void cameraCropImageUri(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", default_width);
		intent.putExtra("outputY", default_height);
		intent.putExtra("scale", true);
		//		if (mIsKitKat) {
		//			intent.putExtra("return-data", true);
		//			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		//		} else {
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(img_cache_path, img_new_filename)));
		//		}
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, REQCODE_SHOWIMG_FROM_CAMERA_CORPED);
	}

	/**
	 * <br>功能简述: 4.4及以上改动版裁剪图片方法实现 --------------------相机
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param uri
	 */
	private void cropImageUriAfterKikat(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", default_width);
		intent.putExtra("outputY", default_height);
		intent.putExtra("scale", true);
		//		intent.putExtra("return-data", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(img_cache_path, img_new_filename)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, REQCODE_SHOWIMG_FROM_ALBUM_CORPED_KITKAT);
	}

	/**
	 * <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param uri
	 * @return
	 */
	private Bitmap getBitMapByUri(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * <br>功能简述:4.4及以上获取图片的方法
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}


}
