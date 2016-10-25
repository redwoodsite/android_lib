package com.sjwlib.db.imageupload;

import android.widget.ImageView;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ImageData {
    private String desc;
    private ImageView image;
    private String type;

    public ImageData(String desc, String type, ImageView image) {
        this.desc = desc;
        this.type = type;
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
