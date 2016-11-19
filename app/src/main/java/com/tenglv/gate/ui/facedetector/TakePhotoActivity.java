package com.tenglv.gate.ui.facedetector;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.TextView;

import com.tencent.youtulivecheck.YoutuLiveCheck;
import com.tenglv.gate.R;
import com.tenglv.gate.ui.AppBarActivity;
import com.tenglv.gate.utils.BitmapUtils;
import com.tenglv.gate.widget.CameraPreview;

import java.io.File;
import java.util.Locale;

public class TakePhotoActivity extends AppBarActivity implements Camera.PreviewCallback {

    TextView tvInfo;
    CameraPreview cameraPreview;
    TextView tvState;
    TextView tvTimer;

    private boolean isStartTimer;

    private byte[] frameData = null;

    private int facecheck_threshold;

    private YoutuLiveCheck.FaceStatus faceStatus = null;

    private boolean isProcessing = false;

    private boolean isTakePhotoSuccess = false;
    private boolean isAutoMode = true;

    private YoutuLiveCheck facecheck = YoutuLiveCheck.getInstance();

    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected int getContentViewId() {
        return R.layout.activity_take_photo;
    }

    @Override
    protected void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTitle(R.string.face_detector);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        cameraPreview = (CameraPreview) findViewById(R.id.camPreview);
        tvState = (TextView) findViewById(R.id.tvState);
        tvTimer = (TextView) findViewById(R.id.tvTimer);

    }

    @Override
    protected void initData() {
        facecheck_threshold = 70;
        cameraPreview.setCameraPreviewCallback(this);
        facecheck.init(this, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraPreview.onResume();
        startDetection();
    }

    @Override
    public void onPause() {
        endDetection();
        cameraPreview.onPause();
        super.onPause();
    }


    public void startDetection() {
        faceStatus = null;
        tvInfo.setText(formatScore(0));
        facecheck.DoDetectionInit();
    }

    private void endDetection() {
        tvInfo.setText("");
    }


    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        // At preview mode, the frame data will push to here.

        if (bytes != null && !isStartTimer) {
            startCountdownTimer();
        }

        if (!isProcessing && bytes != null) {
            correctOrientation(bytes, camera);
            mHandler.post(imageProcessing);
        }
    }


    private void correctOrientation(byte[] bytes, Camera camera) {
        if (bytes == null || bytes.length <= 0) {
            return;
        }

        frameData = new byte[bytes.length];
        Camera.Size previewSize = cameraPreview.getCameraPreviewSize();
        int imageWidth = previewSize.width;
        int imageHeight = previewSize.height;

        if (camera.getParameters().getPreviewFormat() == android.graphics.ImageFormat.YV12) {

            if (!cameraPreview.isFrontCamera()) {
                BitmapUtils.YUV420pRotate90(frameData, bytes, imageWidth, imageHeight);

            } else {

                BitmapUtils.YUV420pRotate270(frameData, bytes, imageWidth, imageHeight);
            }

        } else if (camera.getParameters().getPreviewFormat() == android.graphics.ImageFormat.NV21) {
            if (!cameraPreview.isFrontCamera()) {

                //后置摄像头测试通过
                BitmapUtils.YUV420spRotate180(frameData, bytes, imageWidth, imageHeight);

            } else {
                //前置摄像头未调试过
                frameData = bytes;
            }
        }
    }

    private int sw;
    private int sh;
    private Runnable imageProcessing = new Runnable() {
        public void run() {
            isProcessing = true;
            Camera.Size previewSize = cameraPreview.getCameraPreviewSize();
            int imageWidth = previewSize.width;
            int imageHeight = previewSize.height;

            if (frameData == null) {
                isProcessing = false;
                return;
            }

            faceStatus = facecheck.DoDetectionProcess(frameData, imageWidth, imageHeight);

            sw = imageWidth;
            sh = imageHeight;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (faceStatus == null) {
                        tvInfo.setText(formatScore(0));
                        return;
                    }

//                    tvInfo.setText(formatScore(faceStatus.illumination_score));

//                    if (faceStatus.x < 0 || faceStatus.y < 0 || faceStatus.x + faceStatus.w > sw || faceStatus.y + faceStatus.h > sh)
//                    {
//                        tvInfo.setText(String.valueOf(faceStatus.illumination_score) + " 超出屏幕");
//                    }
//                    else if (faceStatus.w < 96 || faceStatus.h < 96)
//                    {
//                        tvInfo.setText(String.valueOf(faceStatus.illumination_score) + " 人脸太小");
//                    }
//                    else if (faceStatus.save_photo == 1)
//                    {
//                        tvInfo.setText(String.valueOf(faceStatus.illumination_score) + " 保存照片 :D");
//                    }
//                    else if (faceStatus.illumination_score < 85)
//                    {
//                        tvInfo.setText(String.valueOf(faceStatus.illumination_score) + " 太暗或不均匀");
//                    }
//                    else
//                    {
//                        tvInfo.setText(String.valueOf(faceStatus.illumination_score) + " 姿态不端正");
//                    }

                    if (faceStatus.liveness_head == 1)
                    {
                        tvInfo.setText(formatScore(faceStatus.illumination_score) + " 摇头");
                    }
                    else if (faceStatus.liveness_mouth == 1)
                    {
                        tvInfo.setText(formatScore(faceStatus.illumination_score) + " 张嘴");
                    }
                    else if (faceStatus.liveness_eye == 1)
                    {
                        tvInfo.setText(formatScore(faceStatus.illumination_score) + " 眨眼");
                    }

                    if (isAutoMode && faceStatus.illumination_score >= facecheck_threshold) {
                        handleTakePhoto();
                    }
                }
            });

            isProcessing = false;
        }
    };


    private void handleTakePhoto() {
        cameraPreview.takePhoto();
    }

    private boolean isFaceComparing;
    private File headFile;

    private void facecompare(File file, boolean auto) {
        if (isFaceComparing) {
            return;
        }
        isFaceComparing = true;
        headFile = file;
    }


    //开始倒计时
    private CountDownTimer mTimer;

    private void startCountdownTimer() {
        mTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("(" + millisUntilFinished / 1000 + "S)");
            }

            @Override
            public void onFinish() {
                if (!isTakePhotoSuccess) {
                    stopAutoMode();
                }
            }
        };
        mTimer.start();
        isStartTimer = true;
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }


    private void stopAutoMode() {
        isAutoMode = false;
        stopTimer();
        tvTimer.setText("");
        tvState.setText(R.string.hand_mode);
    }

    private String formatScore(int score) {
        return String.format(Locale.CHINA, "匹配度(%d)", score);
    }


    @Override
    protected void onDestroy() {
        cameraPreview.onPause();
        stopTimer();
        super.onDestroy();
    }
}
