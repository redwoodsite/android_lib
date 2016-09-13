package com.sjwlib.typedef;

import com.alibaba.fastjson.JSON;

/**
 * Created by yangzhixi on 2016/4/30.
 */

/*
返回参数回调类
注：T一般为单个对象类型，如Map<String,String>、 Gps
 */
public abstract class RequestParamsCallback<T> extends RequestCallbackBase {

    public void onSuccess(T params){

    }

}
