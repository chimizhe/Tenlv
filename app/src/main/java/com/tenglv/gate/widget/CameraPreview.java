package com.tenglv.gate.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tenglv.gate.utils.BitmapUtils;
import com.tenglv.gate.utils.FileUtils;

import java.io.File;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";

    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera = null;
    Camera.CameraInfo mCameraInfo = null;
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;
    private int mImageFormat;
    private int mPreviewImageFormat;
    private Camera.PreviewCallback mPreviewCallback;

    private boolean frontcamera = true;//false;

    private int retryCount = 0;
    private final int MAX_RETRY = 10;

    public CameraPreview(Context context) {
        super(context);
        mContext = context;
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setCameraPreviewCallback(Camera.PreviewCallback callback) {
        mPreviewCallback = callback;
    }

    public Camera.Size getCameraPreviewSize() {
        return mPreviewSize;
    }

    public int getCameraImageFormat() {
        return mImageFormat;
    }

    public int getCameraPreviewImageFormat() {
        return mPreviewImageFormat;
    }

    public boolean isFrontCamera() {
        return frontcamera;
    }


    public void switchCamera() {
        frontcamera = !frontcamera;
        onPause();
        onResume();
    }


    public void onResume() {
        if (frontcamera)
            openFrontCamera();
        else
            openBackCamera();

        try {

            // supported preview sizes
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();

            if (mPreviewSize == null) {
                mPreviewSize = mCamera.getParameters().getPreviewSize();
            }

            if (frontcamera) {
                mPreviewSize.width = 640;
                mPreviewSize.height = 480;

            } else {
                mPreviewSize.width = 640;
                mPreviewSize.height = 480;
            }


            for (Camera.Size str : mSupportedPreviewSizes)
                Log.d(TAG, str.width + "/" + str.height);

            mHolder = getHolder();
            mHolder.addCallback(this);

            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            Camera.Parameters parameters = mCamera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            parameters.setPictureSize(mPreviewSize.width, mPreviewSize.height);

            parameters.setPreviewFormat(ImageFormat.NV21);

            mCamera.setParameters(parameters);
            setCorrectCameraOrientation(mCameraInfo, mCamera);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.startPreview();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

    }


    private void openFrontCamera() {
        try {
            mCameraInfo = new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, mCameraInfo);
                if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    mCamera = Camera.open(camIdx);
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openBackCamera() {
        mCameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {

            try {
                Camera.getCameraInfo(camIdx, mCameraInfo);
                if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mCamera = Camera.open(camIdx);
                    return;
                }
            } catch (Exception e) {
                retryCount++;
                if (retryCount <= MAX_RETRY) {
                    openBackCamera();
                }
                e.getMessage();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // empty. surfaceChanged will take care of stuff
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (mHolder.getSurface() == null) {
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set preview size and make any resize, rotate or reformatting changes here
        // start preview with new settings
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            parameters.setPictureSize(mPreviewSize.width, mPreviewSize.height);


            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }

//            parameters.setRotation(setCorrectCameraOrientation(mCameraInfo, mCamera));

            mImageFormat = parameters.getPictureFormat();
            mPreviewImageFormat = parameters.getPreviewFormat();
            mCamera.setParameters(parameters);
            setCorrectCameraOrientation(mCameraInfo, mCamera);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    //    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//
//        Log.e(TAG, "resolveSize " + width + "/" + height);
//
//        mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
//
////        if (frontcamera) {
////            mPreviewSize.width = 640;
////            mPreviewSize.height = 480;
////
////        } else {
////            mPreviewSize.width = 980;
////            mPreviewSize.height = 720;
////        }
//
//        float mr = (float) width / (float) height;
//        float mpr = (float) mPreviewSize.width / (float) mPreviewSize.height;
//
//        mpr = 1 / mpr; // rotate 90
//
//        if (mr > mpr) {
//            setMeasuredDimension((int) (height * mpr), height);
//        } else {
//            setMeasuredDimension(width, (int) (width / mpr));
//        }
//    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    public void setCorrectCameraOrientation(CameraInfo info, Camera camera) {

        int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;

        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;

        }

        camera.setDisplayOrientation(result);
    }


    private boolean isTakePhotoGoing;

    public void takePhoto() {
        if (mCamera == null) {
            return;
        }

        if (isTakePhotoGoing) {
            return;
        }

        isTakePhotoGoing = true;
        try {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        try {
                            mCamera.takePicture(new Camera.ShutterCallback() {
                                @Override
                                public void onShutter() {

                                }
                            }, null, new Camera.PictureCallback() {
                                @Override
                                public void onPictureTaken(byte[] data, Camera camera) {
                                    File file = new File(FileUtils.getCropPicturesDir(mContext), FileUtils.getPhotoFileName());
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    bitmap = BitmapUtils.rotateBitmapByDegree(bitmap, 90);
                                    BitmapUtils.saveBitmap(bitmap, file.getAbsolutePath());
                                    mCamera.startPreview();

                                }
                            });
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            isTakePhotoGoing = false;
                        }
                    }
                    isTakePhotoGoing = false;

                    mCamera.cancelAutoFocus();
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
            isTakePhotoGoing = false;
        }

    }
}

