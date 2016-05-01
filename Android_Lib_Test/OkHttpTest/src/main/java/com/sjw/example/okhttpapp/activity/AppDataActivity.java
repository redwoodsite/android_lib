package com.sjw.example.okhttpapp.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.sjw.example.okhttpapp.AppManager;
import com.sjwlib.activity.DataActivity;
import com.sjwlib.net.WebApi;

/**
 * Created by yangzhixi on 2016/4/18.
 */
public abstract class AppDataActivity extends DataActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加入Activty管理器
        AppManager.getInstance().addActivity(this);
    }

    protected void onDestroy() {
        /**
         * 在activity销毁的时候同时设置停止请求，停止线程请求回调
         */
        WebApi.getInstance().cancelRequest();
        super.onDestroy();
    }

    protected void onPause() {
        /**
         * 在activity暂停的时候同时设置停止请求，停止线程请求回调
         */
        WebApi.getInstance().cancelRequest();
        super.onPause();
    }

    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

        }
        return false;
    }*/

}
