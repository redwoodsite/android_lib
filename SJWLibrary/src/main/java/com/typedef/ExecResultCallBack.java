package com.typedef;

/**
 * Created by Administrator on 2015/12/3.
 */
public class ExecResultCallBack {
    /**
     * 执行结果的回调接口
     */
    public interface IExecResultCallBack {
        public void onSuccess();
        public void onFail();
    }
}
