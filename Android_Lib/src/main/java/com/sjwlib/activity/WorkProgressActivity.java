package com.sjwlib.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sjwlib.R;

/**
 * Created by yangzhixi on 2016/4/15.
 */

public class WorkProgressActivity extends AlertDialog {
    private Handler handler = new Handler();

    TextView lbl_info;
    Activity parentActivity = null;

    public WorkProgressActivity(Context context) {
        super(context);
        if(context!=null)
            if(context.getClass().getSimpleName().toLowerCase().contains("activity"))
                parentActivity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workprogress);
        initViews();
    }

    private void initViews() {
        // 显示于parent_activity底部，类似于小米MIUI效果
        getWindow().setGravity(Gravity.BOTTOM);
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = ActionBar.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(p);

        lbl_info = (TextView)findViewById(R.id.lbl_info);
        lbl_info.setText(info);
        // 如果消息为空串，则不显示消息，同时进度框也要缩小
        if(info.equals("")){
            // 消息不显示
            lbl_info.setVisibility(View.INVISIBLE);
            // 进度窗口调整为自适应
            LinearLayout ll_body = (LinearLayout) findViewById(R.id.ll_body);
            /*ViewGroup.LayoutParams params = ll_body.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            ll_body.setLayoutParams(params);*/

            ll_body.setAlpha(0.4f);
        }
    }

    @Override
    public void show(){
        this.show("");
    }

    public void showTips(){
        show("请求中...");
    }

    String info;
    public void show(String info){
        this.info = info;
        if(info.equals("")){
            super.show();
        }
        else {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if(parentActivity!=null && !parentActivity.isFinishing())
            super.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.dismiss();
            return false;
        }
        return true;
    }
}

