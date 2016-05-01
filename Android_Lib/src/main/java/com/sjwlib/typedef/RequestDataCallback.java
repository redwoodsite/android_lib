package com.sjwlib.typedef;

/**
 * Created by yangzhixi on 2016/4/30.
 */
public abstract class RequestDataCallback<E,F> extends RequestCallbackBase {

    public void onSuccess(F data){

    }

    public void onSuccess(E params, F data){

    }

}
