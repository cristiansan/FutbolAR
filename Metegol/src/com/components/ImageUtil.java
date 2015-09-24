package com.components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

public class ImageUtil {

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmap(String pathName, int reqWidth,
                                             int reqHeight) {

        return BitmapSizeHelper.getBitmapFromPath(pathName, reqWidth,
                reqHeight, BitmapSizeHelper.ScalingLogic.CROP);
        // First decode with inJustDecodeBounds=true to check dimensions
        // final BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inJustDecodeBounds = true;
        //
        // BitmapFactory.decodeFile(pathName, options);
        // // Calculate inSampleSize
        // options.inSampleSize = calculateInSampleSize(options, reqWidth,
        // reqHeight);
        // // Decode bitmap with inSampleSize set
        // options.inJustDecodeBounds = false;
        //
        // return BitmapFactory.decodeFile(pathName, options);
    }

    public static Bitmap decodeDisplaySizeBitmapFromResource(Resources res,
                                                             int resId, Context context) {
        return decodeSampledBitmapFromResource(res, resId,
                getWidthDisplay(context), getHeightDisplay(context));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap getResizedBitmapDisplaySize(Bitmap bm, Context context) {

        return getResizedBitmap(bm, getHeightDisplay(context),
                getWidthDisplay(context));
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    @SuppressWarnings("deprecation")
    public static int getWidthDisplay(Context mContext) {
        int width = 0;
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            width = metrics.widthPixels;
        } else {
            width = display.getWidth(); // deprecated
        }
        return width;
    }

    @SuppressWarnings("deprecation")
    public static int getHeightDisplay(Context mContext) {
        int height = 0;
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            height = metrics.heightPixels;
        } else {
            height = display.getHeight(); // deprecated
        }
        return height;
    }

    public static void unbindDrawables(View view) {
        if (view == null)
            return;
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }

            if (view instanceof AdapterView) {
                ((AdapterView<?>) view).removeAllViewsInLayout();
                ////Log.i("UnbindDrawbles", "AdapterView Succefull");
            } else {
                ((ViewGroup) view).removeAllViews();
                ////Log.i("UnbindDrawbles", "ViewGroup Succefull");
            }
        }
    }

    public static float getPixel(Context context, int dpi) {
        Resources resources = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi,
                resources.getDisplayMetrics());
    }

}
