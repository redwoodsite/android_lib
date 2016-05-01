package com.sjw.example.okhttpapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sjw.example.okhttpapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XUtilsActivity extends AppDataActivity {

    @Bind(R.id.edtResult) EditText edtResult;
    @Bind(R.id.btn_none) Button btn_Params_a;
    @Bind(R.id.btn_string_result) Button btn_Params_b;
    @Bind(R.id.btn_string_result_params) Button btn_Params_c;

    @OnClick(R.id.btn_none) void params0_click(){
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
    @OnClick(R.id.btn_string_result) void params1_click(){

    }
    @OnClick(R.id.btn_string_result_params) void params2_click(){

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
