package com.sjwlib.typedef;

import com.zhy.http.okhttp.callback.StringCallback;

public class ApiRequestParams{
        private String url = "";
        private boolean paramsOk =false;
        private String netType = "";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


        public String getNetType() {
            return netType;
        }

        public void setNetType(String netType) {
            this.netType = netType;
        }

        public boolean isParamsOk() {
            return paramsOk;
        }

        public void setParamsOk(boolean paramsOk) {
            this.paramsOk = paramsOk;
        }

        private StringCallback resultCallback;

        public StringCallback getResultCallback() {
            return resultCallback;
        }

        public void setResultCallback(StringCallback resultCallback) {
            this.resultCallback = resultCallback;
        }
    }