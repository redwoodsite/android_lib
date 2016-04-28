package com.sjwlib.typedef;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yangzhixi on 2016/4/18.
 */
public class ResponseResult {
    private String params = "";
    private String data = "";
    private HashMap<String,String> respParams = new HashMap<String, String>();
    private ArrayList<HashMap<String,String>> respData = new ArrayList<HashMap<String, String>>();

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public HashMap<String, String> getRespParams() {
        return respParams;
    }

    public void setRespParams(HashMap<String, String> respParams) {
        this.respParams = respParams;
    }

    public ArrayList<HashMap<String, String>> getRespData() {
        return respData;
    }

    public void setRespData(ArrayList<HashMap<String, String>> respData) {
        this.respData = respData;
    }
}
