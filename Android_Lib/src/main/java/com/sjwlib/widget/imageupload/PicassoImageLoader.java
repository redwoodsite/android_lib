package com.sjwlib.widget.imageupload;

import android.app.Activity;
import android.widget.ImageView;

import com.lzy.imagepicker.loader.ImageLoader;
import com.sjwlib.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Administrator on 2016/10/16.
 */
public class PicassoImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Picasso.with(activity)//
                .load(new File(path))//
                .placeholder(R.mipmap.default_image)//
                .error(R.mipmap.default_image)//
                .resize(width, height)//
                .centerInside()//
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }
}
