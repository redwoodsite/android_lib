package com.sjw.example.okhttpapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.sjw.example.okhttpapp.R;
import com.sjw.example.okhttpapp.bean.Address;
import com.sjwlib.net.WebApi;
import com.sjwlib.typedef.SuccessCallback;

import org.apache.http.HttpException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XUtilsActivity extends AppDataActivity {

    @Bind(R.id.edtResult) EditText edtResult;
    @Bind(R.id.btnParams_a) Button btn_Params_a;
    @Bind(R.id.btnParams_b) Button btn_Params_b;
    @Bind(R.id.btnParams_c) Button btn_Params_c;

    @OnClick(R.id.btnParams_a) void params0_click(){
//        HttpUtils http = new HttpUtils();
//        HttpHandler handler = http.download("http://apache.dataguru.cn/httpcomponents/httpclient/source/httpcomponents-client-4.2.5-src.zip",
//                "/sdcard/httpcomponents-client-4.2.5-src.zip",
//                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
//                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
//                new RequestCallBack<File>() {
//
//                    @Override
//                    public void onStart() {
//                        testTextView.setText("conn...");
//                    }
//
//                    @Override
//                    public void onLoading(long total, long current, boolean isUploading) {
//                        testTextView.setText(current + "/" + total);
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<File> responseInfo) {
//                        testTextView.setText("downloaded:" + responseInfo.result.getPath());
//                    }
//
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        testTextView.setText(msg);
//                    }
//                });

    }
    @OnClick(R.id.btnParams_b) void params1_click(){

    }
    @OnClick(R.id.btnParams_c) void params2_click(){

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
        setContentView(R.layout.activity_xutils);
        ButterKnife.bind(this);
        btn_Params_a.setText("xUtils下载文件");
        btn_Params_b.setText("");
        btn_Params_c.setText("");
    }

    @Override
    protected void loadData() {

    }

}
