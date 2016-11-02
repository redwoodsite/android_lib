package com.sjwlib.core.typedef;

/**
 * Created by Administrator on 2016/10/13.
 */
public abstract class ApiManagerAbstract {

    public abstract void initApiInstance();

    protected abstract ApiBaseAbstract apiFactroy(String apiType);

    public URLData getUrlData(String apiKey){
        String type = apiKey.split("#")[0];
        String key = apiKey.split("#")[1];
        ApiBaseAbstract apiBase = apiFactroy(type);
        return apiBase.getUrlData(key);
    }

}
