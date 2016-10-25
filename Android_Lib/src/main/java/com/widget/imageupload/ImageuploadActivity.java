package com.widget.imageupload;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.sjwlib.R;

import java.io.File;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ImageuploadActivity extends Activity {

    public final static int RESULT_CODE_IMAGEUPLOAD = 1111;

    //LinearLayout llContainer ;
    ProgressBar pbProgressBar;
    //TextView tvCurrentSize;
    //TextView tvTotalSize;
    TextView tvInfo;
    /*
        客户端向intent传入如下参数
            url(*):      上传网址
            filepath(*)：本地图片路径
            headers：    头参数
            params：     表单参数
            token：      令牌，用于校验身份
            cache：     鲁班压缩后存放的路径，位于app.cache目录下
            title:      上传的图片标题
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageupload);
        initViews();
        if(!getIntent().hasExtra("url")){
            Toast.makeText(ImageuploadActivity.this, "未指定url", Toast.LENGTH_SHORT).show();
            finish();
        }
        if(!getIntent().hasExtra("filepath")){
            Toast.makeText(ImageuploadActivity.this, "未指定filepath", Toast.LENGTH_SHORT).show();
            finish();
        }
        File oldFile = new File(getIntent().getStringExtra("filepath"));
        lubanCompress(oldFile);
        startUpload(oldFile);
    }

    private void initViews() {
        getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = ActionBar.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(p);

        //llContainer = (LinearLayout)findViewById(R.id.llContainer);
        pbProgressBar = (ProgressBar)findViewById(R.id.pbProgressBar);
        //tvCurrentSize = (TextView)findViewById(R.id.tvCurrentSize);
        //tvTotalSize = (TextView)findViewById(R.id.tvTotalSize);
        tvInfo = (TextView)findViewById(R.id.tvTitle);

        pbProgressBar.setProgress(0);
        pbProgressBar.setMax(0);
        //tvCurrentSize.setText("");
        //tvTotalSize.setText("");
    }

    //extras: p1 heads p2 params p3 url（requried） p4 filename(requried)
    // note: p2 is imageData member
    private void startUpload(final File newFile) {
        final String url = getIntent().getStringExtra("url");
        HttpHeaders headers = null;
        if(getIntent().hasExtra("headers")) headers = (HttpHeaders)getIntent().getSerializableExtra("headers");
        HttpParams params = null;
        if(getIntent().hasExtra("params")) params = (HttpParams)getIntent().getSerializableExtra("params");
        String token = "";
        if(getIntent().hasExtra("token")) token = getIntent().getStringExtra("token");
        final ArrayList<File> files = new ArrayList<>();
        files.add(new File(newFile.getPath()));
        //tbar.setTitleTxt(bundle.get("title").toString());
        HttpParams commParams = new HttpParams();
        commParams.put("token", token);
        OkGo.getInstance().addCommonParams(commParams);
        OkGo.post(url)//
                .tag(this)//
                .headers(headers)
                .params(params) // 附加参数
                .addFileParams("files", files) // 上传文件
                .execute(new FileCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        if(getIntent().hasExtra("title"))
                            tvInfo.setText("正在上传文件:" + getIntent().getStringExtra("title"));
                        else
                            tvInfo.setText("正在上传文件");
                        //llContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(File file, okhttp3.Call call, okhttp3.Response response) {
                        //Toast.makeText(ImageuploadActivity.this, "已上传", Toast.LENGTH_SHORT).show();
                        // 回传intent
                        Intent intent = new Intent();
                        intent.putExtra("filepath", newFile.getPath());
                        setResult(RESULT_CODE_IMAGEUPLOAD, intent);
                        finish();
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);
                        //tvCurrentSize.setText(ImageUtils.convertFileSize(currentSize));
                        //tvTotalSize.setText(ImageUtils.convertFileSize(totalSize));
                        pbProgressBar.setProgress((int)currentSize*100);
                        pbProgressBar.setMax(100);
                    }

                    @Override
                    public void onError(okhttp3.Call call, okhttp3.Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(ImageuploadActivity.this, "出错了：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 对原图使用鲁班压缩，存储于指定路径下
    private void lubanCompress(File oldFile){
        if(getIntent().hasExtra("cache"))
            Luban.DEFAULT_DISK_CACHE_DIR = getIntent().getStringExtra("cache");
        Luban.get(this)
                .load(oldFile)                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        tvInfo.setText("开始压缩图片");
                        //llContainer.setVisibility(View.GONE);
                    }
                    @Override
                    public void onSuccess(File newFile) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        startUpload(newFile);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ImageuploadActivity.this, "压缩出错了", Toast.LENGTH_SHORT).show();
                    }
                }).launch();    //启动压缩
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
