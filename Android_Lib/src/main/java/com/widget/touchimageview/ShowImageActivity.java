package com.widget.touchimageview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.sjwlib.R;

import java.io.File;

public class ShowImageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);

        // 加载指定本地图片
        TouchImageView image1 = (TouchImageView)findViewById(R.id.image1);
        if(getIntent().hasExtra("path")){
            String path = getIntent().getStringExtra("path");
            if(path.equals("")==false){
                File file = new File(path);
                if(file.exists()){
                    Uri uri = Uri.fromFile(file);
                    image1.setImageURI(uri);
                }
            }
        }

    }
}
