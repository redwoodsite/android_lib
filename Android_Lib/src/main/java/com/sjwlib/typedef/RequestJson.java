package com.sjwlib.typedef;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/9/10.
 */
public class RequestJson {
    public RequestJson(){

    }
    public RequestJson(String method, String request_id){
        this.method = method;
        this.request_id = request_id;
    }
    public String method = "";
    /*public String getMethod(){
        return method;
    }
    public void setMethod(String method){
        this.method = method;
    }*/

    public String request_id = "";
    /*public String getRequest_id(){
        return request_id;
    }
    public void setRequest_id(String request_id){
        this.request_id = request_id;
    }*/

    public HashMap<String,String> request_params = new HashMap<String,String>();

    public HashMap<String,String> request_params1 = new HashMap<String,String>();

    /*public Map getRequest_params(){
        return request_params;
    }*/
}

/*  调用示例1
     RequestJson req_object = new RequestJson();
        req_object.setMethod("driver_db/login");
        req_object.setRequest_id("android:100");
        req_object.getRequest_params().put("userid", userid);
        req_object.getRequest_params().put("password", password);
        String request_params = JsonConvert.seralizeObject(req_object);
        String result = NetUtil.getRequest(Service_Config.SERVICE_WEBAPI_SJW,request_params);
        if(result.contains("exec_status_fail")){
            resp_error = JsonConvert.readValue(result, ResponseJsonError.class);
            return false;
        }else{
            return true;
        }
 */

/*  调用示例2
     RequestJson req_object = new RequestJson("driver_db/login","android:100");
        req_object.getRequest_params().put("userid", userid);
        req_object.getRequest_params().put("password", password);
        String request_params = JsonConvert.seralizeObject(req_object);
        String result = NetUtil.getRequest(Service_Config.SERVICE_WEBAPI_SJW,request_params);
        if(result.contains("exec_status_fail")){
            resp_error = JsonConvert.readValue(result, ResponseJsonError.class);
            return false;
        }else{
            return true;
        }
 */