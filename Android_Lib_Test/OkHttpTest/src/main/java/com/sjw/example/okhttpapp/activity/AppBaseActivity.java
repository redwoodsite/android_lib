package com.sjw.example.okhttpapp.activity;

import android.os.Bundle;

import com.sjw.example.okhttpapp.AppManager;
import com.sjwlib.activity.BaseActivity;

/**
 * Created by yangzhixi on 2016/4/18.
 */
public abstract class AppBaseActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加入Activty管理器
        AppManager.getInstance().addActivity(this);
    }
}
