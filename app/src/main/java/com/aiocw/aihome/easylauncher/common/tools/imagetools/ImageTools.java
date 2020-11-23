package com.aiocw.aihome.easylauncher.common.tools.imagetools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageTools {

    public static Drawable loadImage(String path){
        Drawable drawable = Drawable.createFromPath(path);
        return  drawable;
    }

    public static boolean drawableSave(Drawable drawable, String filePath) {
        if(drawable == null) {
            return false;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(file);
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void test() {
        // 在屏幕显示图片
//                Uri filepath = Uri.fromFile(new File(filename));
//                Bitmap bitmap = null;
//                try {
//                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(filepath));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                imageViewShow = findViewById(R.id.im_show);
//                imageViewShow.setImageBitmap(bitmap);
    }
}
