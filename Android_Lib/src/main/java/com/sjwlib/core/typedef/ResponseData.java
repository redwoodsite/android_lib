package com.sjwlib.core.typedef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/10.
 */
public class ResponseData extends ResponseBase {
    //public String request_id = "";
    /*public String getrequest_id(){
        return this.request_id;
    }
    public void setrequest_id(String request_id){
        this.request_id = request_id;
    }*/

    //public String response_status = "";
    /*public String getresponse_status(){
        return this.response_status;
    }
    public void setresponse_status(String response_status){
        this.response_status = response_status;
    }*/

    public Map<String,String> response_params = new HashMap<String, String>();
    /*public String getresponse_params(){
        return this.response_params;
    }
    public void setresponse_params(String response_params){
        this.response_params = response_params;
    }*/

    public ArrayList<HashMap<String,String>> response_data = new ArrayList<HashMap<String,String>>();
    /*public String getresponse_data(){
        return this.response_data;
    }
    public void setresponse_data(String response_data){
        this.response_data = response_data;
    }*/

    public HashMap<String,Object> page_params = new HashMap<String,Object>();
    /*public String getpage_params(){
        return this.page_params;
    }
    public void setpage_params(String page_params){
        this.page_params = page_params;
    }*/


}
