package com.sjwlib.typedef;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yangzhixi on 2016/4/15.
 */

public abstract class SuccessCallback extends RequestCallbackBase {

    //  params接收response_params
    public void onCallback(String result) {

    }

    //  params接收response_params
    public void onCallback(String result, String params) {

    }

    // params接收response_params，data接收response_data
    public void onCallback(String result, String params, String data) {

    }

}
