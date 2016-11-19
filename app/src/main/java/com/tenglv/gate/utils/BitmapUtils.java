package com.tenglv.gate.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jiang on 8/29/16.
 */
public class BitmapUtils {
    private final static String TAG = BitmapUtils.class.getSimpleName();

    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static void YUV420spRotate90(byte[] dst, byte[] src, int srcWidth, int srcHeight) {
        int nWidth = 0, nHeight = 0;
        int wh = 0;
        int uvHeight = 0;
        if (srcWidth != nWidth || srcHeight != nHeight) {
            nWidth = srcWidth;
            nHeight = srcHeight;
            wh = srcWidth * srcHeight;
            uvHeight = srcHeight >> 1;//uvHeight = height / 2
        }
        //旋转Y
        int k = 0;
        for (int i = 0; i < srcWidth; i++) {
            int nPos = 0;
            for (int j = 0; j < srcHeight; j++) {
                dst[k] = src[nPos + i];
                k++;
                nPos += srcWidth;
            }
        }

        for (int i = 0; i < srcWidth; i += 2) {
            int nPos = wh;
            for (int j = 0; j < uvHeight; j++) {
                dst[k] = src[nPos + i];
                dst[k + 1] = src[nPos + i + 1];
                k += 2;
                nPos += srcWidth;
            }
        }
        return;
    }

    public static void YUV420spRotate180(byte[] dst, byte[] src, int srcWidth, int srcHeight) {
        int nWidth = 0, nHeight = 0;
        int wh = 0;
        int uvsize = 0;
        int uvHeight = 0;
        if (srcWidth != nWidth || srcHeight != nHeight) {
            nWidth = srcWidth;
            nHeight = srcHeight;
            wh = srcWidth * srcHeight;
            uvHeight = srcHeight >> 1;//uvHeight = height / 2
        }
        uvsize = wh >> 1;
        for (int i = 0; i < wh; i++) {
            dst[wh - 1 - i] = src[i];
        }
        for (int i = 0; i < uvsize; i += 2) {
            dst[wh + uvsize - 2 - i] = src[wh + i];
            dst[wh + uvsize - 1 - i] = src[wh + i + 1];
        }
        return;
    }


    public void YUV420spRotate270(byte[] dst, byte[] src, int srcWidth, int srcHeight) {
        int nWidth = 0, nHeight = 0;
        int wh = 0;
        int uvHeight = 0;
        if (srcWidth != nWidth || srcHeight != nHeight) {
            nWidth = srcWidth;
            nHeight = srcHeight;
            wh = srcWidth * srcHeight;
            uvHeight = srcHeight >> 1;//uvHeight = height / 2
        }

        int k = 0;
        for (int i = 0; i < srcWidth; i++) {
            int nPos = srcWidth - 1;
            for (int j = 0; j < srcHeight; j++) {
                dst[k] = src[nPos - i];
                k++;
                nPos += srcWidth;
            }
        }

        for (int i = 0; i < srcWidth; i += 2) {
            int nPos = wh + srcWidth - 1;
            for (int j = 0; j < uvHeight; j++) {
                dst[k] = src[nPos - i - 1];
                dst[k + 1] = src[nPos - i];
                k += 2;
                nPos += srcWidth;
            }
        }
        return;
    }


    public static void YUV420spRotateNegative90(byte[] dst, byte[] src,
                                                int srcWidth, int height) {
        int nWidth = 0, nHeight = 0;
        int wh = 0;
        int uvHeight = 0;
        if (srcWidth != nWidth || height != nHeight) {
            nWidth = srcWidth;
            nHeight = height;
            wh = srcWidth * height;
            uvHeight = height >> 1;// uvHeight = height / 2
        }

        // 旋转Y
        int k = 0;
        for (int i = 0; i < srcWidth; i++) {
            int nPos = srcWidth - 1;
            for (int j = 0; j < height; j++) {
                dst[k] = src[nPos - i];
                k++;
                nPos += srcWidth;
            }
        }

        for (int i = 0; i < srcWidth; i += 2) {
            int nPos = wh + srcWidth - 1;
            for (int j = 0; j < uvHeight; j++) {
                dst[k] = src[nPos - i - 1];
                dst[k + 1] = src[nPos - i];
                k += 2;
                nPos += srcWidth;
            }
        }

        return;
    }

    public static void YUV420spMirrorY(byte[] dst, byte[] src, int srcWidth,
                                       int srcHeight) {
        // 镜像Y
        int k = 0;
        int nPos = -1;
        for (int j = 0; j < srcHeight; j++) {
            nPos += srcWidth;
            for (int i = 0; i < srcWidth; i++) {
                dst[k] = src[nPos - i];
                k++;
            }
        }

        int uvHeight = srcHeight >> 1; // uvHeight = height / 2
        for (int j = 0; j < uvHeight; j++) {
            nPos += srcWidth;
            for (int i = 0; i < srcWidth; i += 2) {
                dst[k] = src[nPos - i - 1];
                dst[k + 1] = src[nPos - i];
                k += 2;
            }
        }
    }

    public static void YUV420pRotate90(byte[] des, byte[] src, int width,
                                       int height) {
        int n = 0;
        int hw = width / 2;
        int hh = height / 2;
        // copy y
        for (int j = 0; j < width; j++) {
            for (int i = height - 1; i >= 0; i--) {
                des[n++] = src[width * i + j];
            }
        }

        // copy u
        int uPos = width * height;
        for (int j = 0; j < hw; j++) {
            for (int i = hh - 1; i >= 0; i--) {
                des[n++] = src[uPos + hw * i + j];
            }
        }

        // copy v
        int vPos = uPos + width * height / 4;
        for (int j = 0; j < hw; j++) {
            for (int i = hh - 1; i >= 0; i--) {
                des[n++] = src[vPos + hw * i + j];
            }
        }
    }


    public static void YUV420pRotate180(byte[] des, byte[] src, int width,
                                        int height) {
        int n = 0;
        int hw = width / 2;
        int hh = height / 2;
        // copy y
        for (int j = height - 1; j >= 0; j--) {
            for (int i = width; i > 0; i--) {
                des[n++] = src[width * j + i];
            }
        }

        // copy u
        int uPos = width * height;
        for (int j = hh - 1; j >= 0; j--) {
            for (int i = hw; i > 0; i--) {
                des[n++] = src[uPos + hw * i + j];
            }
        }

        // copy v
        int vPos = uPos + width * height / 4;
        for (int j = hh - 1; j >= 0; j--) {
            for (int i = hw; i > 0; i--) {
                des[n++] = src[vPos + hw * i + j];
            }
        }
    }

    public static void YUV420pRotate270(byte[] des, byte[] src, int width,
                                        int height) {
        int n = 0;
        int hw = width / 2;
        int hh = height / 2;
        // copy y
        for (int j = width - 1; j >= 0; j--) {
            for (int i = 0; i < height; i++) {
                des[n++] = src[width * i + j];
            }
        }

        // copy u
        int uPos = width * height;
        for (int j = hw - 1; j >= 0; j--) {
            for (int i = 0; i < hh; i++) {
                des[n++] = src[uPos + hw * i + j];
            }
        }

        // copy v
        int vPos = uPos + width * height / 4;
        for (int j = hw - 1; j >= 0; j--) {
            for (int i = 0; i < hh; i++) {
                des[n++] = src[vPos + hw * i + j];
            }
        }
    }

    public static void YUV420pMirrorY(byte[] des, byte[] src, int width,
                                      int height) {
        int n = 0;
        int hw = width / 2;
        int hh = height / 2;
        // copy y
        for (int j = 0; j < height; j++) {
            for (int i = width - 1; i >= 0; i--) {
                des[n++] = src[width * j + i];
            }
        }

        // copy u
        int uPos = width * height;
        for (int j = 0; j < hh; j++) {
            for (int i = hw - 1; i >= 0; i--) {
                des[n++] = src[uPos + hw * j + i];
            }
        }

        // copy v
        int vPos = uPos + width * height / 4;
        for (int j = 0; j < hh; j++) {
            for (int i = hw - 1; i >= 0; i--) {
                des[n++] = src[vPos + hw * j + i];
            }
        }
    }

    public static void YUV420pMirrorX(byte[] des, byte[] src, int width,
                                      int height) {
        int n = 0;
        int hw = width / 2;
        int hh = height / 2;

        int nPos = width * height;
        for (int j = 0; j < height; j++) {
            nPos -= width;
            for (int i = 0; i < width; i++) {
                des[n++] = src[nPos + i];
            }
        }

        nPos = width * height + width * height / 4;
        for (int j = 0; j < hh; j++) {
            nPos -= hw;
            for (int i = 0; i < hw; i++) {
                des[n++] = src[nPos + i];
            }
        }

        nPos = width * height + width * height / 2;
        for (int j = 0; j < hh; j++) {
            nPos -= hw;
            for (int i = 0; i < hw; i++) {
                des[n++] = src[nPos + i];
            }
        }
    }


    /**
     * 为什么要旋转YuvImage，因为安卓的摄像Camera天生是横的，当用竖屏牌照或者摄像的时候，需要设置
     * Camera.setDisplayOrientation(90);照片的方向是对的,但是相机真正保存的还是倒置的图片。这个方法只是让用户觉得方向对了。
     * 所以我们需要处理YuvImage旋转
     *
     * @param data
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    public static byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }


    public static void rotateYUV420pRotate180(byte[] des, byte[] src, int width, int height) {
        int n = 0;
        int hw = width / 2;
        int hh = height / 2;
        // copy y
        for (int j = height - 1; j >= 0; j--) {
            for (int i = width; i > 0; i--) {
                des[n++] = src[width * j + i];
            }
        }

        // copy u
        int uPos = width * height;
        for (int j = hh - 1; j >= 0; j--) {
            for (int i = hw; i > 0; i--) {
                des[n++] = src[uPos + hw * i + j];
            }
        }

        // copy v
        int vPos = uPos + width * height / 4;
        for (int j = hh - 1; j >= 0; j--) {
            for (int i = hw; i > 0; i--) {
                des[n++] = src[vPos + hw * i + j];
            }
        }
    }


    /**
     * 画圆形图片
     *
     * @param bitmap 输入的图片bitmap
     * @param pixels 圆形弧度
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 压缩图片到指定dax
     *
     * @param image        压缩的图片
     * @param format       图片格式
     * @param compressSize 压缩到的大小 byte[].length
     * @return
     */
    public static Bitmap compressImage(Bitmap image, Bitmap.CompressFormat format, int compressSize) {
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(format, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        while (baos.toByteArray().length > compressSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(format, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            quality -= 5;// 每次都减少5
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, Bitmap.CompressFormat format,
                                        final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(format, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 压缩图片
     *
     * @params input 输入的图片路径
     * @params output 输出的图片路径
     */
    public static boolean compressImage(String input, String output) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        int sampleSize = 100;
        opts.inSampleSize = sampleSize;
        Bitmap inImage = BitmapFactory.decodeFile(input, opts);
        return true;
    }

    /**
     * 获取图片信息，主要是宽，高和大小
     *
     * @param input
     * @return int[] [0] 宽|[1] 高 |[2] 文件大小
     */
    public static int[] getImageInfo(String input) {
        int[] values = new int[3];
        Bitmap inImage = null;
        try {
            File f = new File(input);
            if (f.exists()) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                inImage = BitmapFactory.decodeFile(f.getAbsolutePath(), opts);
                values[0] = opts.outWidth;
                values[1] = opts.outHeight;
                values[2] = (int) f.length();
            }
        } catch (Exception e) {
            Log.e(TAG, ">>>>>> getImageWH.error:" + e.getMessage());
        } finally {
            if (null != inImage) {
                if (!inImage.isRecycled()) {
                    inImage.recycle();
                    System.gc();
                }
            }
        }
        return values;
    }

    /**
     * 载入指定大小的图片
     *
     * @param path 图片路径
     * @param size 图片的显示比例
     * @return
     */
    public static Bitmap getBitmap(String path, int size) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = size;
        Bitmap bt = BitmapFactory.decodeFile(path, op);
        return bt;
    }

    /**
     * 按宽高比压缩图片
     *
     * @param path  图片路径
     * @param width 指定的宽
     * @param heigh 指定的高
     * @return
     */
    public static Bitmap getBitmap(String path, int width, int heigh) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        Bitmap bt = BitmapFactory.decodeFile(path, op);
        int xScale = op.outWidth / width;
        int yScale = op.outHeight / heigh;
        op.inSampleSize = xScale > yScale ? xScale : yScale;
        op.inJustDecodeBounds = false;
        bt = BitmapFactory.decodeFile(path, op);
        return bt;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 放大/缩小位图
     *
     * @param bitmap
     * @param scaleWidth
     * @param scaleHeight
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float scaleWidth, float scaleHeight) {
        if (bitmap == null || scaleWidth <= 0 || scaleHeight <= 0)
            return null;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
    }


    public static void saveBitmap(Bitmap bitmap, String path) {
        saveBitmap(bitmap, path, 90);
    }


    public static void saveBitmap(Bitmap bitmap, String path, int quality) {
        File f = new File(path);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
