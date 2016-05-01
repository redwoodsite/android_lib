package com.sjw.example.okhttpapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sjw.example.okhttpapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppBaseActivity {

    @OnClick(R.id.btn_okhttp) void okhttp(){
        Intent activity = new Intent(this, OkHttpActivity.class);
        startActivity(activity);
    }

    @OnClick(R.id.btn_xutils) void xutils(){
        Intent activity = new Intent(this, XUtilsActivity.class);
        startActivity(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

}
