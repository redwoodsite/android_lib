package com.sjwlib.core.utils;

import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static void saveCachePath(Bitmap bitmap, String path, String imgName){
        File file = new File(path);
        if (!file.exists())
            file.mkdir();

        String newFilePath = path + imgName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    public static void clearImageCache(){
//        File file = new File(AppConst.USER_IMAGE_CACHE);
//        if (file.exists())
//            file.delete();
//    }


}
