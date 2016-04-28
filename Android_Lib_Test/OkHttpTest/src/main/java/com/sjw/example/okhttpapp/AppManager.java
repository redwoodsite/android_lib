package com.sjw.example.okhttpapp;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.Stack;

/**
 * Created by yangzhixi on 2016/4/7.
 */
public class AppManager extends Application {

    private static AppManager mAppManager;
    private Stack<Activity> mActivityStack;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppManager = this;
    }

    @Override
    public void onTerminate() {
        finishActivity();
        super.onTerminate();
    }

    // 单例
    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 关闭Activity列表中的所有Activity*/
    public void finishActivity(){
        try{
            for (Activity activity : mActivityStack) {
                if (null != activity) {
                    activity.finish();
                }
            }
            mActivityStack.clear();
            //杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
        }catch (Exception ex){
            Log.e("【FinishActivity】", ex.getMessage());
        }

    }

}
