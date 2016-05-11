package com.sjw.example.okhttpapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sjw.example.okhttpapp.R;
import com.sjwlib.net.WebApi;
import com.sjwlib.typedef.RequestStringCallback;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VolleyActivity extends AppDataActivity {

    @Bind(R.id.edtResult) EditText edtResult;
    @Bind(R.id.btn_none) Button btn_none;
    @OnClick(R.id.btn_none) void string_none_click(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top1", "1");
        // callback
        RequestStringCallback callback = new RequestStringCallback() {
            @Override
            public void onSuccess(String result) {
                edtResult.setText("执行成功了!");
            }
        };
        // invoke webapi
        //WebApi.getInstance().setAddress("192.168.0.1"); // 开启另一个IP
        WebApi.getInstance().invokeOkhttp(this, "gps.gettopgps", params, callback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_volley);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        btn_none.setText("onSuccess()");
    }

    @Override
    protected void loadData() {

    }

}
