package com.sjwlib111.image.touchimageview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.sjwlib111.R;

import java.io.File;

/*  调用

    @OnClick(R.id.img_tx) void tx_click(){
        if(img_tx.getDrawable() != null){
            Bitmap bitmap = ((BitmapDrawable)img_tx.getDrawable()).getBitmap();
            Intent intent = new Intent(this, Show_Picture_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("bitmap", bitmap);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

*/

public class Show_Picture_Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

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
