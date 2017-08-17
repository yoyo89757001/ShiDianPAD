package com.example.xiaojun.shidianpad.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;

import com.example.xiaojun.shidianpad.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CameraActivity extends Activity implements Callback {

    private SurfaceView surfaceView;
    private Camera mCamera;
    private SurfaceHolder sh;
    private int cameraId;
    private android.view.ViewGroup.LayoutParams lp;
    private ImageView ceshi_im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);
        ceshi_im= (ImageView) findViewById(R.id.ceshi);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        lp = surfaceView.getLayoutParams();
        sh = surfaceView.getHolder();
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sh.addCallback(this);



    }

    @Override
    protected void onResume() {

        super.onResume();
        cameraId = 1;// default id
        OpenCameraAndSetSurfaceviewSize(cameraId);
    }

    @Override
    protected void onPause() {

        super.onPause();
        kill_camera();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        kill_camera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        SetAndStartPreview(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private Void SetAndStartPreview(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(0);
            mCamera.startPreview();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    private Void OpenCameraAndSetSurfaceviewSize(int cameraId) {
        mCamera = Camera.open(cameraId);
        Camera.Parameters parameters = mCamera.getParameters();
        Size pre_size = parameters.getPreviewSize();
        Size pic_size = parameters.getPictureSize();
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);

        lp.height =pre_size.height*2;
        lp.width = pre_size.width*2;
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Size size = camera.getParameters().getPreviewSize();
                try{
                    YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                    if(image!=null){
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);

                        final Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());

                        //**********************
                        //因为图片会放生旋转，因此要对图片进行旋转到和手机在一个方向上
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               ceshi_im.setImageBitmap(bmp);


                           }
                       });
                        //**********************************

                        stream.close();
                    }
                }catch(Exception ex){
                    Log.e("Sys","Error:"+ex.getMessage());
                }
            }
        });


        return null;
    }

    private Void kill_camera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        return null;
    }
}