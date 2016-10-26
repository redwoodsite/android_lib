package com.sjwlib.core.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
	/**
	 * 请求列表管理器
	 */
//	protected RequestManager requestManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
//		requestManager = new RequestManager(this);

		super.onCreate(savedInstanceState);

		// 如果为竖屏显示
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		initVariables();
		initViews(savedInstanceState);
	}

	protected abstract void initVariables();

	protected abstract void initViews(Bundle savedInstanceState);

	protected void onDestroy() {
		/**
		 * 在activity销毁的时候同时设置停止请求，停止线程请求回调
		 */
//		if (requestManager != null) {
//			requestManager.cancelRequest();
//		}
		super.onDestroy();
	}

	protected void onPause() {
		/**
		 * 在activity停止的时候同时设置停止请求，停止线程请求回调
		 */
//		if (requestManager != null) {
//			requestManager.cancelRequest();
//		}
		super.onPause();
	}



//	public RequestManager getRequestManager() {
//		return requestManager;
//	}
}