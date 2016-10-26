package com.sjwlib.widget.versionupgrade;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sjwlib.R;
import com.sjwlib.widget.titlebar.Titlebar;
import com.cundong.utils.ApkUtils;
import com.cundong.utils.PatchUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DownLoadActivity extends Activity{
	Titlebar titlebar;
	private ImageView download_back;//返回
	private Button download_install;//安装
	private TextView downfileName;//名字显示
	private TextView downProgress;//当前下载大小显示
	private TextView downRatio;//百分比显示
	private TextView downsuccess;//下载成功或失败显示
	private TextView downFileSize;//成功后文件大小
	private ProgressBar downProgressBar;//下载的进度条
	public static final int DOWN_LOADING = 1;//正在下载
	public static final int DOWN_FAILD = 2;//下载失败
	public static final int DOWN_SUCCESS = 3;//下载成功
	DownLoadItem downLoadFileItem;
	String filePathPhoto= Environment.getExternalStorageDirectory().getAbsolutePath();
	private String file;
	private String applicationId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_versiondownload);

		// 如果为竖屏显示
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		init_views();
		// 增量升级
		file = getIntent().getStringExtra("file");
		String url = getIntent().getStringExtra("url") + "?file=" + this.file;
		applicationId = getIntent().getStringExtra("applicationId");
		init_data(url, this.file);

		new DownLoadThread().start();
	}
	private void init_data(String url, String fileName){
		downLoadFileItem=new DownLoadItem();
		downLoadFileItem.setDownFileUrl(url);
		downLoadFileItem.setDownFileName(fileName);
//        if(filePathPhoto.substring(filePathPhoto.length()-1,1)!= "/")
		filePathPhoto += "/";
		filePathPhoto = filePathPhoto + fileName;
		downLoadFileItem.setFileSdPath(filePathPhoto);
		downLoadFileItem.setTemp_downMaxValues((int)(2.5*1024*1024)); // 设置下载文件大小
		if(downLoadFileItem.getDownFileName().toLowerCase().endsWith(".apk")) { // apk文件，直接安装
			downfileName.setText(downLoadFileItem.getDownFileName());
		}else{
			downfileName.setText(downLoadFileItem.getDownFileName().replace("-release",""));
		}
	}
	private void init_views() {
		titlebar = (Titlebar)findViewById(R.id.tbTitleBar);
		titlebar.setBackOnClick(new Titlebar.IBack() {
			@Override
			public void onClick() {
				finish();
			}
		});
		// 如果为竖屏显示
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		// install button
		download_install =(Button)findViewById(R.id.download_install);
		download_install.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(file.toLowerCase().endsWith(".apk")){ // apk文件，直接安装
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(filePathPhoto)), "application/vnd.android.package-archive");
					startActivity(intent);
				}else{ // patch文件，需要合并再安装
					new PatchApkTask().execute();
				}
			}
		});

		downfileName=(TextView)findViewById(R.id.download_main_fileName);
		downProgress=(TextView)findViewById(R.id.download_main_tvProgress);
		downRatio=(TextView)findViewById(R.id.download_main_tvRatio);
		downsuccess=(TextView)findViewById(R.id.download_main_success);
		downFileSize=(TextView)findViewById(R.id.download_main_fileSize);
		downProgressBar=(ProgressBar)findViewById(R.id.download_main_progressBarlist);
	}

	static {
		System.loadLibrary("ApkPatchLibrary");
	}

	private class PatchApkTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			PackageInfo packageInfo = ApkUtils.getInstalledApkPackageInfo(getApplicationContext(), applicationId);
			if (packageInfo != null) {
				String oldApkSource = ApkUtils.getSourceApkPath(getApplicationContext(), applicationId);
				if (!TextUtils.isEmpty(oldApkSource)) {
					//String PATH = downLoadFileItem.getFileSdPath();
					String PATH = Environment.getExternalStorageDirectory() + File.separator;
					String newApkSource = PATH + applicationId + "new.apk";
					//String patchApkSource = PATH + AppConst.APP_PACKAGENAME + ".patch";
					String patchApkSource = filePathPhoto;
					File patchFile = new File(patchApkSource);
					if (patchFile.exists()) {
						int patchReault = PatchUtils.patch(oldApkSource, newApkSource, patchApkSource);
						if (patchReault == 0) {
							//Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(new File(newApkSource)), "application/vnd.android.package-archive");
							startActivity(intent);
						} else {
							Toast.makeText(getApplicationContext(), "安装失败！", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "下载失败！", Toast.LENGTH_SHORT).show();
				}
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
		}
	}

	class DownLoadThread extends Thread {
		@Override
		public void run() {
			downLoadFileItem.setTemp_downLoading(true);
			Message message=myHandler.obtainMessage();
			message.what=DOWN_LOADING;
			message.obj=downLoadFileItem;
			message.sendToTarget();
			UpdateProgress updateProgress=new UpdateProgress(downLoadFileItem);
			boolean is_success=DownloadUtil.download(downLoadFileItem.getDownFileUrl(),
					downLoadFileItem.getFileSdPath(), updateProgress);
			if(is_success){
				downLoadFileItem.setTemp_downLoading(false);
				downLoadFileItem.setTemp_downSuccess(true);
				Message message2=myHandler.obtainMessage();
				message2.what=DOWN_SUCCESS;
				message2.sendToTarget();

			}else{
				downLoadFileItem.setTemp_downLoading(false);
				downLoadFileItem.setTemp_downSuccess(false);
				Message message2=myHandler.obtainMessage();
				message2.what=DOWN_FAILD;
				message2.sendToTarget();
			}
		}
	}

	/**
	 * 用于更新进度条和更新界面显示的Handler
	 */
	Handler myHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DOWN_LOADING:
					DownLoadItem downLoadItem=(DownLoadItem)msg.obj;
					downLoading(downLoadItem);
					break;
				case DOWN_FAILD:
					downLoadFailed();
					break;
				case DOWN_SUCCESS:
					downLoadSuccess();
					break;
				default:
					break;
			}

		}
	};

	/**
	 * 正在下载的界面显示
	 */
	private void downLoading(DownLoadItem downLoadItem){
		downProgressBar.setVisibility(View.VISIBLE);
		downProgress.setVisibility(View.VISIBLE);
		downRatio.setVisibility(View.VISIBLE);
		downsuccess.setVisibility(View.GONE);
		downFileSize.setVisibility(View.GONE);
		download_install.setVisibility(View.GONE);
		downProgressBar.setMax((int)downLoadItem.getTemp_downMaxValues());
		downProgressBar.setProgress((int)downLoadItem.getTemp_downProgress());
		downProgressBar.setProgress((int)downLoadItem.getTemp_downProgress());
		downProgress.setText(format2f((double)downLoadItem.getTemp_downProgress()/1024/1024)+"M"+" / "+
				format2f((double)downLoadItem.getTemp_downMaxValues()/1024/1024)+"M");
		//下载的百分比
		String strRatio=percent(downProgressBar.getProgress(), downLoadItem.getTemp_downMaxValues());
		if(!strRatio.contains("%"))
			downRatio.setText("0%");
		else
			downRatio.setText(strRatio);
	}
	private String format2f(double value){
		DecimalFormat df = new DecimalFormat("#.00");
		String result = df.format(value);
		if(result.startsWith(".")) result = "0" + result;
		return result;
	}
	/**
	 * 下载成功的界面显示
	 */
	private void downLoadSuccess(){
		downProgressBar.setVisibility(View.GONE);
		downProgress.setVisibility(View.GONE);
		downRatio.setVisibility(View.GONE);
		download_install.setVisibility(View.GONE);
		downsuccess.setVisibility(View.VISIBLE);
		downFileSize.setVisibility(View.VISIBLE);
		downsuccess.setText("下载成功!");
		downFileSize.setText("("+format2f((double)downLoadFileItem.getTemp_downMaxValues()/1024/1024)+"MB)");
		downsuccess.setVisibility(View.INVISIBLE);
		downFileSize.setVisibility(View.INVISIBLE);
		download_install.setVisibility(View.VISIBLE);
	}
	/**
	 * 下载失败的界面显示
	 */
	private void downLoadFailed(){
		download_install.setVisibility(View.GONE);
		downProgressBar.setVisibility(View.GONE);
		downProgress.setVisibility(View.GONE);
		downRatio.setVisibility(View.GONE);
		downFileSize.setVisibility(View.GONE);
		downsuccess.setVisibility(View.VISIBLE);
		downsuccess.setText("下载失败!");
	}
	class UpdateProgress extends DownloadFileListener{
		DownLoadItem downLoadItem;
		public UpdateProgress(DownLoadItem downLoadItem){
			this.downLoadItem=downLoadItem;
		}
		@Override
		public void downLoadNotify(long currentNumber, long fileSize) {
			super.downLoadNotify(currentNumber, fileSize);
			downLoadItem.setTemp_downProgress(currentNumber);
			downLoadItem.setTemp_downMaxValues(fileSize);
			Message message=myHandler.obtainMessage();
			message.what=DOWN_LOADING;
			message.obj=downLoadItem;
			message.sendToTarget();
		}
	}
	private String percent(double d1, double d2) {
		String strRatio;
		double p3 = d1 / d2;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		strRatio = nf.format(p3);
		return strRatio;
	}
	public String getFileNameByUrl(String downFileUrl){
		int index = downFileUrl.lastIndexOf("/");
		String fileName=downFileUrl.substring(index+1);
		return fileName;
	}
}