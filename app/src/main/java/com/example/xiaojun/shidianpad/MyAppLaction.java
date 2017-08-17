package com.example.xiaojun.shidianpad;

import android.app.Application;

import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.example.xiaojun.shidianpad.dialog.JiaZaiDialog;
import com.tencent.smtt.sdk.QbSdk;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import java.io.File;

import java.io.IOException;


/**
 * Created by Administrator on 2017/7/5.
 */

public class MyAppLaction extends Application{
    private File mCascadeFile;
    public static FaceDet mFaceDet;

   // public static CascadeClassifier mJavaDetector;

//    static {
//
//        System.loadLibrary("opencv_java3");
//    }

    @Override
    public void onCreate() {
        super.onCreate();


        mFaceDet = new FaceDet(Constants.getFaceShapeModelPath());

        try {
            Reservoir.init(this, 900*1024); //in bytes 1M

            //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

                @Override
                public void onViewInitFinished(boolean arg0) {

                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    Log.d("app", " onViewInitFinished is " + arg0);
                }

                @Override
                public void onCoreInitFinished() {
                    QbSdk.reset(getApplicationContext());
                }
            };
            //x5内核初始化接口
            QbSdk.initX5Environment(getApplicationContext(),  cb);
        } catch (IOException e) {
            Log.d("gggg", e.getMessage());

        }




//        Log.d("MainActivity", "OpenCVLoader.initDebug():" + OpenCVLoader.initDebug());
        // Example of a call to a native method
//        try {
//            // load cascade file from application resources
//            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
//            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
//            mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt2.xml");
//            FileOutputStream os = new FileOutputStream(mCascadeFile);
//
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = is.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//            is.close();
//            os.close();
//
//            mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
//            if (mJavaDetector.empty()) {
//
//                mJavaDetector = null;
//            }
//            cascadeDir.delete();
//
//
//        } catch (IOException e) {
//            Log.d("InFoActivity2", e.getMessage());
//        }


    }



}
