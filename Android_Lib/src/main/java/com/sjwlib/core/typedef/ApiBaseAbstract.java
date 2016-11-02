package com.sjwlib.core.typedef;

import com.sjwlib.core.typedef.URLData;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/13.
 */
public abstract class ApiBaseAbstract {
    private HashMap<String,URLData> urlHashMap = new HashMap<String, URLData>();

    public ApiBaseAbstract(){
        initUrlData();
    }

    // 派生类重写 举例：http://123.57.24.26 或 123.57.24.26
    protected abstract String getApiAddress();

    // 派生类重写 举例：verwebapi
    protected abstract String getApiName();

    // 派生类重写 举例：
    protected abstract void initUrlData();

    protected void setUrlData(String apiKey){
        setUrlData(apiKey, "get");
    }

    protected void setUrlData(String apiKey, String netType){
        if(!urlHashMap.containsKey(apiKey)){
            URLData urlData = new URLData();
            urlData.setKey(apiKey);
            urlData.setNetType(netType);
            urlHashMap.put(apiKey, urlData);
        }
    }

    // 只支持http://协议
    public URLData getUrlData(String apiKey){
        URLData data = urlHashMap.get(apiKey);
        if(data != null){
            if(getApiAddress().startsWith("http://")){
                if(getApiAddress().endsWith("/"))
                    data.setAddress(getApiAddress() + getApiName());
                else
                    data.setAddress(getApiAddress() + "/" + getApiName());
            }
            else{
                if(getApiAddress().endsWith("/"))
                    data.setAddress("http://" + getApiAddress() + getApiName());
                else
                    data.setAddress("http://" + getApiAddress() + "/" + getApiName());
            }
        }
        else{
           System.out.println("没有找到apkKey：" + apiKey);
        }

        return data;
    }
}
