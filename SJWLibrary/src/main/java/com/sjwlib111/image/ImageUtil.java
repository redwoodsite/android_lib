package com.sjwlib111.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2015/12/17.
 */
public class ImageUtil {
    public static void displayImage(Context context, File file, ImageView img_pic) {
        Uri uri = Uri.fromFile(file);
        displayImage(context,uri,img_pic);
    }
    public static void displayImage(Context context, String filefullname, ImageView img_pic) {
        Uri uri = Uri.fromFile(new File(filefullname));
        displayImage(context,uri,img_pic);
    }
    public static void displayImage(Context context, Uri uri, ImageView img_pic) {
        Bitmap bitmap = decodeUriAsBitmap(context, uri);
        if (bitmap != null) {
            img_pic.setImageBitmap(bitmap);
        }
    }

    private static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
