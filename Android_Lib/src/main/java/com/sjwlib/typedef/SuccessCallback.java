package com.sjwlib.typedef;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yangzhixi on 2016/4/15.
 */

public abstract class SuccessCallback {

    public void onCallback() {

    }

    public void onCallback(String result) {

    }

    //  params接收response_params
    public void onCallback(String result, String params) {

    }

    // params接收response_params，data接收response_data
    public void onCallback(String result, String params, String data) {

    }

    // resp_params接收HasMap<~>
    public void onCallback(HashMap<String,String> resp_params) {

    }

    public void onCallback(HashMap<String,String> resp_params, ArrayList<HashMap<String, String>> resp_data) {

    }

    // 全接口
    public void onCallback(String params, String data,
                           HashMap<String,String> resp_params, ArrayList<HashMap<String, String>> resp_data) {

    }

}
