package com.gypsophila.androidpro;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.view.Display;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by Gypsophila on 2016/7/28.
 */
public class PictureUtils {
    public static BitmapDrawable getScaledDrawable(Activity activity, String path, float rotateDegrees) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Bitmap bmp = rotateBitmap(bitmap, rotateDegrees);
        return new BitmapDrawable(activity.getResources(), bmp);
    }

    public static void cleanImageView(ImageView imageView) {
        if (imageView != null) {
            if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
                return;
            }
            BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
            b.getBitmap().recycle();
            imageView.setImageDrawable(null);
        }
    }

//    private static int getRotateDegreee(String path) {
//        int degree = 0;
//        try {
//            ExifInterface exifInterface = new ExifInterface(path);
//            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    degree = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    degree = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    degree = 270;
//                    break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return degree;
//        }
//        return degree;
//    }

    private static Bitmap rotateBitmap(Bitmap bitmap, float degree) {
//        if (0 == degree) {
//            return bitmap;
//        }
        Matrix matrix = new Matrix();
        matrix.setRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }
}
