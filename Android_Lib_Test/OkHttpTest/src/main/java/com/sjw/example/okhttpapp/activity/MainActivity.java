package com.sjw.example.okhttpapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.sjw.example.okhttpapp.R;
import com.sjw.example.okhttpapp.bean.Address;
import com.sjw.example.okhttpapp.bean.Gps;
import com.sjwlib.activity.DataActivity;
import com.sjwlib.typedef.SuccessCallback;
import com.sjwlib.net.WebApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppDataActivity {

    @Bind(R.id.edtResult) EditText edtResult;
    @Bind(R.id.btnParams_a) Button btn_Params_a;
    @Bind(R.id.btnParams_b) Button btn_Params_b;
    @Bind(R.id.btnParams_c) Button btn_Params_c;
    @Bind(R.id.btnParams_d) Button btn_Params_d;
    @Bind(R.id.btnParams_e) Button btn_Params_e;
    @Bind(R.id.btnParams_f) Button btn_Params_f;
    @Bind(R.id.btnParams_g) Button btn_Params_g;

    @OnClick(R.id.btnParams_a) void params0_click(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("b_lng", "117.699041");
        params.put("b_lat", "39.030338");
        // callback
        SuccessCallback callback = new SuccessCallback() {
            @Override
            public void onCallback() {
                edtResult.setText("执行成功了!");
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gps/getaddress", params, callback,true);
    }
    @OnClick(R.id.btnParams_b) void params1_click(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("b_lng", "117.699041");
        params.put("b_lat", "39.030338");
        // callback
        SuccessCallback callback = new SuccessCallback() {
            @Override
            public void onCallback(String result) {
                edtResult.setText(result);
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gps/getaddress", params, callback,true);
    }
    @OnClick(R.id.btnParams_c) void params2_click(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("b_lng", "117.699041");
        params.put("b_lat", "39.030338");
        // callback
        SuccessCallback callback = new SuccessCallback() {
            @Override
            public void onCallback(String result, String params) {
                Address address = JSON.parseObject(params,Address.class);
                String value = "area=" + address.getArea() + ",address=" + address.getAddress();
                edtResult.setText(value);
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gps/getaddress", params, callback,true);
    }
    @OnClick(R.id.btnParams_d) void params3_click(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("b_lng", "117.699041");
        params.put("b_lat", "39.030338");
        // callback
        SuccessCallback callback = new SuccessCallback() {
            @Override
            public void onCallback(String result, String params, String data) {
                Address address = JSON.parseObject(params,Address.class);
                String value = "area=" + address.getArea() + ",address=" + address.getAddress();
                edtResult.setText(value);
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gps/getaddress", params, callback,true);
    }
    @OnClick(R.id.btnParams_e) void params4_click() {
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "5");
        // callback
        SuccessCallback callback =  new SuccessCallback() {
            @Override
            public void onCallback(HashMap<String,String> resp_params) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[resp_params]=");
                stringBuilder.append("cph=" + resp_params.get("cph"));
                stringBuilder.append("phone="+resp_params.get("phone"));
                stringBuilder.append("top="+resp_params.get("top"));
                edtResult.setText(stringBuilder.toString());
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback,true);
    }
    @OnClick(R.id.btnParams_f) void params5_click() {
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "5");
        // callback
        SuccessCallback callback =  new SuccessCallback() {
            public void onCallback(HashMap<String,String> resp_params, ArrayList<HashMap<String,String>> resp_data) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[resp_params]=");
                stringBuilder.append("cph=" + resp_params.get("cph"));
                stringBuilder.append("phone="+resp_params.get("phone"));
                stringBuilder.append("top="+resp_params.get("top"));
                stringBuilder.append("\n[resp_data]=");
                for( HashMap<String,String> gps : resp_data){
                    String value = String.format(gps.get("cph") + "\narea=%s,address=%s", gps.get("area"), gps.get("address"));
                    stringBuilder.append(value);
                }
                edtResult.setText(stringBuilder.toString());
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback,true);
    }
    @OnClick(R.id.btnParams_g) void params6_click() {
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "5");
        // callback
        SuccessCallback callback =  new SuccessCallback() {
            public void onCallback(String params, String data, HashMap<String,String> resp_params, ArrayList<HashMap<String,String>> resp_data) {
                StringBuilder stringBuilder = new StringBuilder();
                String value = String.format("[resp_params]=top=%s,cph=%s, phone=%s\n", resp_params.get("top"), resp_params.get("cph"), resp_params.get("phone"));
                stringBuilder.append(value + "[resp_data]=");
                for( HashMap<String,String> gps : resp_data){
                    value = String.format(gps.get("cph") + "\narea=%s,address=%s", gps.get("area"), gps.get("address"));
                    stringBuilder.append(value);
                }
                edtResult.setText(stringBuilder.toString());
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback,true);
    }
    @OnClick(R.id.btnXUtils) void xutils_click(){
        Intent activtiy = new Intent(this, XUtilsActivity.class);
        startActivity(activtiy);
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
        btn_Params_a.setText("onCallback()");
        btn_Params_b.setText("String接口: onCallback(String result)");
        btn_Params_c.setText("String接口: onCallback(String result, String params)");
        btn_Params_d.setText("String接口: onCallback(String result, String params, String data)");
        btn_Params_e.setText("HashMap接口：onCallback(HasMap<String,String> resp_params)");
        btn_Params_f.setText("HashMap接口：onCallback(HasMap<String,String> resp_params, ArrayList<HashMap<String,String>> resp_data)");
        btn_Params_g.setText("HashMap接口：onCallback(String params, String data, HasMap<String,String> resp_params, ArrayList<HashMap<String,String>> resp_data)");
    }

    @Override
    protected void loadData() {

    }

}
