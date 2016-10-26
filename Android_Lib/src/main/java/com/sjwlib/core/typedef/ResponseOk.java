package com.sjwlib.core.typedef;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/10.
 */
public class ResponseOk extends ResponseBase {
    //public String request_id = "";
    //public String getrequest_id(){
        //return this.request_id;
    //}
    //public void setrequest_id(String request_id){
      //  this.request_id = request_id;
    //}

    //public String response_status = "";
    //public String getresponse_status(){
    //    return this.response_status;
    //}
    //public void setresponse_status(String response_status){
    //    this.response_status = response_status;
    //}

    public Map<String,String> response_params = new HashMap<String,String>();
    //public Map getresponse_params(){
    //    return this.response_params;
    //}

}
