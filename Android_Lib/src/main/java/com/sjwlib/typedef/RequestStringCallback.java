package com.sjwlib.typedef;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yangzhixi on 2016/4/15.
 */

public abstract class RequestStringCallback extends RequestCallbackBase {

    //  params接收response_params
    public void onSuccess(String result) {

    }

    //  params接收response_params
    public void onSuccess(String result, String params) {

    }

    // params接收response_params，data接收response_data
    public void onSuccess(String result, String params, String data) {

    }

}
