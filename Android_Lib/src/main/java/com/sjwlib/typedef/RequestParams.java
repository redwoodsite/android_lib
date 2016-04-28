package com.sjwlib.typedef;

import java.util.HashMap;

/**
 * Created by yangzhixi on 2016/4/8.
 */
public class RequestParams {
    private HashMap<String,String> hashMap = new HashMap<String, String>();
    public void put(String key, String value){
        hashMap.put(key,value);
    }
    public HashMap<String,String> getHasMap(){
        return hashMap;
    }
}
