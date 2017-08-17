package com.example.xiaojun.shidianpad.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.example.xiaojun.shidianpad.MyAppLaction;
import com.example.xiaojun.shidianpad.R;
import com.example.xiaojun.shidianpad.beans.Photos;
import com.example.xiaojun.shidianpad.beans.ShiBieBean;
import com.example.xiaojun.shidianpad.beans.UserInfoBena;
import com.example.xiaojun.shidianpad.dialog.JiaZaiDialog;
import com.example.xiaojun.shidianpad.dialog.QueRenDialog;
import com.example.xiaojun.shidianpad.dialog.TiJIaoDialog;
import com.example.xiaojun.shidianpad.kaer.BeepManager;
import com.example.xiaojun.shidianpad.utils.FileUtil;
import com.example.xiaojun.shidianpad.utils.GsonUtil;
import com.example.xiaojun.shidianpad.utils.LibVLCUtil;
import com.example.xiaojun.shidianpad.utils.Utils;
import com.example.xiaojun.shidianpad.view.AutoFitTextureView;
import com.example.xiaojun.shidianpad.view.HorizontalProgressBarWithNumber;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.OnClientCallback;
import com.kaer.sdk.serial.SerialReadClient;
import com.kaer.sdk.serial.update.UpdateArmHelper;
import com.kaer.sdk.utils.CardCode;
import com.sdsmdg.tastytoast.TastyToast;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class InFoActivity extends Activity implements OnClientCallback {
    private EditText name,shenfengzheng,xingbie,mingzu,chusheng,dianhua,fazhengjiguan,
            youxiaoqixian,zhuzhi,fanghao,chepaihao,shibiejieguo,xiangsifdu;
    private ImageView zhengjianzhao,xianchengzhao;
    private Button button;
    private HorizontalProgressBarWithNumber progressBarWithNumber;
    private SerialReadClient mSerialPortReadClient;
    private int port;
    private String account;
    private String password;
    private BeepManager _beepManager;
    private ReadAsync async=null;
    private boolean bidui=false;
    private long startTime;
    private JiaZaiDialog jiaZaiDialog=null;
    private String xiangsi="";
    private String biduijieguo="";
    private TiJIaoDialog tiJIaoDialog=null;
    private Thread thread;
    private FaceDet mFaceDet;
    private MediaPlayer mediaPlayer=null;
    private IVLCVout vlcVout=null;
    private IVLCVout.Callback callback;
    private LibVLC libvlc;
    private Media media;
    private static int cishu=0;
    private static boolean isTrue=true;
    private static boolean isTrue2=true;
    private static boolean isTrue3=true;
    private static boolean isTrue4=true;
    private Bitmap bitmapBig=null;
    private ImageView imageView;
    private int numberOfFace = 4;       //最大检测的人脸数
    private FaceDetector myFaceDetect;  //人脸识别类的实例
    private FaceDetector.Face[] myFace; //存储多张人脸的数组变量
    int myEyesDistance;           //两眼之间的距离
    int numberOfFaceDetected=0;       //实际检测到的人脸数
    private static final int MESSAGE_QR_SUCCESS = 1;
    private TextView tishi;
    private static int count=1;
    private UserInfoBena userInfoBena=null;
    private SensorInfoReceiver sensorInfoReceiver;
    private String filePath=null;
    private String filePath2=null;
    private File file1=null;
    private File file2=null;
    private  String ip=null;
    private  String ip2=null;
    private  String zhuji=null;
    private AutoFitTextureView videoView;
    long c=0;
    private String shengfenzhengPath=null;
    private LinearLayout jiemian;
    private RelativeLayout shipingRL;
    private static int lian=0;
    private static boolean isPaiZhao=true;
    private static boolean isPaiZhao2=true;



    Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_QR_SUCCESS:

                    Bitmap bitmap= (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);
                    Log.d("InFoActivity", "设置图片");

                    break;
                case 22:

                    tishi.setVisibility(View.VISIBLE);
                    tishi.setText("比对失败,开始第"+count+"次比对");

                    break;

            }

        }
    };


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                progressBarWithNumber.setVisibility(View.VISIBLE);
                progressBarWithNumber.setProgress(msg.arg1);
            } else if (msg.what == 200) {
                //   updateResult((IDCardItem) msg.obj);
            } else if (msg.what == 300) {
                isTrue=false;

                //  Log.d("InFoActivity", "3000");
                if (jiaZaiDialog!=null){
                    jiaZaiDialog.dismiss();
                    jiaZaiDialog=null;
                }

                isTrue=true;
                isTrue2=true;
                finish();

            } else if (msg.what == 400) {
                Log.d("InFoActivity", "4000");
                if (msg.arg1!=1){

                    if (jiaZaiDialog!=null){
                        jiaZaiDialog.dismiss();
                        jiaZaiDialog=null;
                    }

                    Toast tastyToast= TastyToast.makeText(InFoActivity.this,"网络未连接，请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }


            } else if (msg.what == 500) {
                Toast tastyToast= TastyToast.makeText(InFoActivity.this,msg.obj.toString(),TastyToast.LENGTH_LONG,TastyToast.ERROR);
                tastyToast.setGravity(Gravity.CENTER,0,0);
                tastyToast.show();
            } else if (msg.what == 600) {
                Log.d("InFoActivity", "6000"+msg.arg1);
//                Toast tastyToast= TastyToast.makeText(InFoActivity.this,getEInfoByCode(msg.arg1),TastyToast.LENGTH_LONG,TastyToast.ERROR);
//                tastyToast.setGravity(Gravity.CENTER,0,0);
//                tastyToast.show();
                if (getEInfoByCode(msg.arg1).equals("操作成功")){
                    if (mSerialPortReadClient.getSerialState() != mSerialPortReadClient.Connected) {

                       //connectDevice();

                    } else {

//            if (mSerialPortReadClient.closeSerialPort()) {
//                print("串口已关闭");
//                // btnConnect.setText("连接");
//            } else {
//                print("串口关闭失败");
//            }
                        // mBtReadClient.disconnect(false);
                    }
                }else {

                    Toast tastyToast= TastyToast.makeText(InFoActivity.this,"连接异常,请重新进入",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                    finish();
                }

            } else if (msg.what == UpdateArmHelper.UPDATE_SUCCESS) {
                Log.d("InFoActivity", "1111");
                // mSerialPortReadClient.closeSerialPort();
                // sendEmptyMessageDelayed(700, 10000);
                //  LogUtils.i("收到UpdateArmHelper.UPDATE_SUCCESS");
            } else if (msg.what == UpdateArmHelper.UPDATE_ERROR) {
                Log.d("InFoActivity", "22222");
                // print(getEInfoByCode(msg.arg1));
                // btnCheckUpdate.setEnabled(true);
                // mSerialPortReadClient.closeSerialPort();
            } else if (msg.what == 700) {
                Log.d("InFoActivity", "44444");
                //  print("单片机升级成功");
                //  btnCheckUpdate.setEnabled(true);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian);

        isTrue=true;
        isTrue2=true;
        isTrue3=true;
        isTrue4=true;
         isPaiZhao=true;
         isPaiZhao2=true;
        mFaceDet= MyAppLaction.mFaceDet;

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("guanbi");
        intentFilter1.addAction("guanbi2");
        sensorInfoReceiver = new SensorInfoReceiver();
        registerReceiver(sensorInfoReceiver, intentFilter1);

        videoView= (AutoFitTextureView) findViewById(R.id.fff);

        imageView= (ImageView) findViewById(R.id.ffff);

        jiemian= (LinearLayout) findViewById(R.id.jiemian);
        shipingRL= (RelativeLayout) findViewById(R.id.shiping_rl);
        tishi= (TextView) findViewById(R.id.tishi);
        libvlc= LibVLCUtil.getLibVLC(InFoActivity.this);
        mediaPlayer=new MediaPlayer(libvlc);
        vlcVout = mediaPlayer.getVLCVout();


        Type resultType = new TypeToken<String>() {
        }.getType();
        Reservoir.getAsync("ipipip", resultType, new ReservoirGetCallback<String>() {
            @Override
            public void onSuccess(final String i) {
                ip=i;
                callback=new IVLCVout.Callback() {
                    @Override
                    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

                    }

                    @Override
                    public void onSurfacesCreated(IVLCVout vlcVout) {
                        if (mediaPlayer != null) {
                            final Uri uri=Uri.parse("rtsp://"+ip+"/user=admin&password=&channel=1&stream=0.sdp");
                            media = new Media(libvlc, uri);
                            mediaPlayer.setMedia(media);
                            videoView.setKeepScreenOn(true);
                            mediaPlayer.play();
                            Log.d("InFoActivity", "播放");

                        }

                    }

                    @Override
                    public void onSurfacesDestroyed(IVLCVout vlcVout) {

                    }

                    @Override
                    public void onHardwareAccelerationError(IVLCVout vlcVout) {

                    }
                };
                videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                    @Override
                    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                        vlcVout.attachViews();
                        Log.d("InFoActivity", "播放222");
                    }

                    @Override
                    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                    }

                    @Override
                    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                        return false;
                    }

                    @Override
                    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                    }
                });
                vlcVout.addCallback(callback);
                vlcVout.setVideoView(videoView);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("InFoActivity", "获取本地异常ip:"+e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast tastyToast= TastyToast.makeText(InFoActivity.this,"请先设置摄像头IP",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();
                        Toast tastyToast2= TastyToast.makeText(InFoActivity.this,"请先设置摄像头IP",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast2.setGravity(Gravity.CENTER,0,0);
                        tastyToast2.show();
                    }
                });


            }

        });

        Type resultType2 = new TypeToken<String>() {
        }.getType();
        Reservoir.getAsync("zhuji", resultType2, new ReservoirGetCallback<String>() {
            @Override
            public void onSuccess(final String i) {
                zhuji=i;

            }

            @Override
            public void onFailure(Exception e) {
                Log.d("InFoActivity", "获取本地异常ip:"+e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast tastyToast= TastyToast.makeText(InFoActivity.this,"请先设置主机地址",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();
                        Toast tastyToast2= TastyToast.makeText(InFoActivity.this,"请先设置主机地址",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast2.setGravity(Gravity.CENTER,0,0);
                        tastyToast2.show();
                    }
                });


            }

        });

        userInfoBena=new UserInfoBena();

        ip2 = Utils.getIp(this);
        port = Utils.getPort(this);
        account = Utils.getAccount(this);
        String source = Utils.getPassword(this);
        password = getMD5(source.getBytes());
        mSerialPortReadClient = SerialReadClient.getInstance();
        mSerialPortReadClient.setClientCallback(this);

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                int result = mSerialPortReadClient.init(InFoActivity.this, ip2, port, account, password,
//                        Utils.getIsWss(InFoActivity.this));
//                mHandler.obtainMessage(600, result, result).sendToTarget();
//            }
//        }).start();

        ImageView imageView= (ImageView) findViewById(R.id.dd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        initView();


        // initComData();
        _beepManager = new BeepManager(InFoActivity.this);

//        mScanner = new FingerprintScanner(this);
//        mScanner.setOnUsbPermissionGrantedListener(new OnUsbPermissionGrantedListener() {
//            @Override
//            public void onUsbPermissionGranted(boolean isGranted) {
//                if (isGranted) {
//                    print(getString(R.string.fps_sn, (String) mScanner.getSN().data));
//                    print(getString(R.string.fps_fw, (String) mScanner.getFirmwareVersion().data));
//                } else {
//                    print(getString(R.string.fps_sn, "null"));
//                    print(getString(R.string.fps_fw, "null"));
//                }
//            }
//        });

        jiaZaiDialog=new JiaZaiDialog(InFoActivity.this);
        jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        jiaZaiDialog.show();

        connectDevice();
    }

    public String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f' };
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private void initView() {
        name= (EditText) findViewById(R.id.name);
        shenfengzheng= (EditText) findViewById(R.id.shenfenzheng);
        xingbie= (EditText) findViewById(R.id.xingbie);
        mingzu= (EditText) findViewById(R.id.mingzu);
        chusheng= (EditText) findViewById(R.id.chusheng);
        dianhua= (EditText) findViewById(R.id.dianhua);
        fazhengjiguan= (EditText) findViewById(R.id.jiguan);
        youxiaoqixian= (EditText) findViewById(R.id.qixian);
        zhuzhi= (EditText) findViewById(R.id.dizhi);
        fanghao= (EditText) findViewById(R.id.fanghao);
        chepaihao= (EditText) findViewById(R.id.chepai);
        xiangsifdu= (EditText) findViewById(R.id.xiangsidu);
        shibiejieguo= (EditText) findViewById(R.id.jieguo);
        zhengjianzhao= (ImageView) findViewById(R.id.zhengjian);
        xianchengzhao= (ImageView) findViewById(R.id.paizhao);
        button= (Button) findViewById(R.id.wancheng);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!userInfoBena.getCertNumber().equals("")){
                    try {
                        if (bidui){
                            link_save();
                        }else {
                            final QueRenDialog dialog=new QueRenDialog(InFoActivity.this,"比对不通过,你确定要进行下一步");
                            dialog.setOnPositiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    link_save();
                                    dialog.dismiss();
                                }
                            });
                            dialog.setOnQuXiaoListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();

                                }
                            });
                            dialog.show();
                        }


                    }catch (Exception e){
                        Toast tastyToast= TastyToast.makeText(InFoActivity.this,"数据异常,请返回后重试",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();
                        Log.d("InFoActivity", e.getMessage());
                    }

                }else {
                    Toast tastyToast= TastyToast.makeText(InFoActivity.this,"请先读取身份证信息",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }

            }
        });


        progressBarWithNumber= (HorizontalProgressBarWithNumber) findViewById(R.id.id_progressbar01);

    }

    private void  connectDevice() {
        try {

            int ret = mSerialPortReadClient.openSerialPort();
            if (ret == CardCode.KT8000_Success) {
                // print("串口已打开");
                //btnConnect.setText("断开");

                Log.d("InFoActivity", "进来了 循环");
                isTrue=true;
                isTrue2=true;

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (isTrue) {

                            if (isTrue2){

                                isTrue2=false;

                                try {

                                    if (mSerialPortReadClient.getSerialState() == mSerialPortReadClient.Connected) {
                                        async = new ReadAsync();
                                        async.execute();
                                    } else {

                                        Toast tastyToast = TastyToast.makeText(InFoActivity.this, "串口未打开,请关闭页面后重试！", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                        tastyToast.setGravity(Gravity.CENTER, 0, 0);
                                        tastyToast.show();

                                    }

                                } catch (Exception e) {
                                    isTrue=false;
                                    Log.d("SerialReadActivity", e.getMessage());
                                    mHandler.obtainMessage(300, e.getMessage()).sendToTarget();

                                }

                            }


                        }
                    }

                });
                thread.start();


            } else{

                if (jiaZaiDialog!=null){
                    jiaZaiDialog.dismiss();
                    jiaZaiDialog=null;
                }
                Toast tastyToast= TastyToast.makeText(InFoActivity.this,CardCode.errorCodeDescription(ret),TastyToast.LENGTH_LONG,TastyToast.ERROR);
                tastyToast.setGravity(Gravity.CENTER,0,0);
                tastyToast.show();


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void preExcute(long arg0) {
        startTime = arg0;
    }

    @Override
    public void updateProgress(int arg0) {
        mHandler.obtainMessage(100, arg0, arg0).sendToTarget();
    }

    @Override
    public void onConnectChange(int arg0) {

        //  mHandler.obtainMessage(400, arg0, arg0).sendToTarget();
    }

//    private void initComData() {
//
//        mPreferData = new PreferData(InFoActivity.this);
//        mSerialPortFinder = new SerialPortFinder();
//        deviceNameArr = mSerialPortFinder.getAllDevices();
//        devicePathArr = mSerialPortFinder.getAllDevicesPath();
//        baudrateArr = getResources().getStringArray(R.array.baudrates_value);
////        for (String str : deviceNameArr) {
////            System.out.println("串口名称 " + str);
////
////
////        }
////        for (String str : devicePathArr) {
////            System.out.println("串口地址 " + str);
////
////
////        }
////        for (String str : baudrateArr) {
////            System.out.println("串口号 " + str);
////
////        }
//        // deviceArrp=new ArrayAdapter<String>(SerialReadActivity.this)
//        deviceArrp = new ArrayAdapter<String>(InFoActivity.this, android.R.layout.simple_spinner_item,
//                deviceNameArr);
//        deviceArrp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spDevices.setAdapter(deviceArrp);
//        spDevices.setOnItemSelectedListener(this);
//        baudrateArrp = new ArrayAdapter<String>(InFoActivity.this, android.R.layout.simple_spinner_item,
//                baudrateArr);
//        baudrateArrp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spBaudRate.setAdapter(baudrateArrp);
//        spBaudRate.setOnItemSelectedListener(this);
//        List<String> deviceNameList = Arrays.asList(deviceNameArr);
//        List<String> baudrateList = Arrays.asList(baudrateArr);
//
//        String device = mPreferData.readString("device", "ttyS3(rk_serial)");
//        String baudrate = mPreferData.readString("baudrate", "115200");
//        if (deviceNameList.contains(device)) {
//            spDevices.setSelection(deviceNameList.indexOf(device));
//        }
//        if (baudrateList.contains(baudrate)) {
//            spBaudRate.setSelection(baudrateList.indexOf(baudrate));
//        }
//    }

    private class SensorInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals("guanbi")) {
                userInfoBena.setCardPhoto(intent.getStringExtra("cardPath"));
                userInfoBena.setScanPhoto(intent.getStringExtra("saomiaoPath"));
                bidui=intent.getBooleanExtra("biduijieguo",false);

                if (bidui){
                    shibiejieguo.setText("比对通过");
                    biduijieguo="比对通过";

                }else {
                    shibiejieguo.setText("比对不通过");
                    biduijieguo="比对不通过";
                }
                xiangsi=intent.getStringExtra("xiangsidu");
                xiangsifdu.setText(intent.getStringExtra("xiangsidu")+"");

                Bitmap bitmap= BitmapFactory.decodeFile(FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+"bbbb.jpg");
                xianchengzhao.setImageBitmap(bitmap);

            }
            if (action.equals("guanbi2")){
                finish();
            }
        }
    }

    private   class ReadAsync extends AsyncTask<Intent, Integer, IDCardItem> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            //clear();
            //  btnRead.setEnabled(false);
            startTime = System.currentTimeMillis();

        }

        @Override
        protected IDCardItem doInBackground(Intent... params) {
            //在线
          //  IDCardItem item = mSerialPortReadClient.readCardWithSync();

          IDCardItem item = mSerialPortReadClient.readCardWithoutNet();

            return item;
        }

        @Override
        protected void onPostExecute(IDCardItem result) {

            super.onPostExecute(result);
            Log.d("ReadAsync", "返回数据"+result.retCode);
//            if (mSerialPortReadClient.closeSerailPort()) {
//                print("串口已关闭");
//                btnConnect.setText("连接");
//            }
            updateResult(result);
            //  btnRead.setEnabled(true);


        }

    }
    protected String getEInfoByCode(int code) {
//
//		String str = "登陆失败";
//		switch (code) {
//		case 0:
//			str = "账号登陆成功";
//			break;
//		case 1:
//			str = "服务器未应答";
//			break;
//		case 2:
//			str = "登陆失败";
//			break;
//		case 3:
//			str = "密码错误,登陆失败";
//			break;
//		case 4:
//			str = "服务器未连接 ";
//			break;
//		case 8:
//			str = "用户不存在,登陆失败 ";
//			break;
//		case 17:
//			str = "用户被限制登陆";
//			break;
//		default:
//			break;
//		}
        return CardCode.errorCodeDescription(code);
    }

    private void updateResult(IDCardItem arg0) {
//        if (arg0.retCode != 1) {
//           clear();
//        }

        if (arg0.retCode == 1) {

            if (jiaZaiDialog!=null){
                jiaZaiDialog.dismiss();
                jiaZaiDialog=null;
            }

            ObjectAnimator animator2 = ObjectAnimator.ofFloat(jiemian, "scaleY", 1f, 0f);
            animator2.setDuration(600);//时间1s
            animator2.start();
            //起始为1，结束时为0
            ObjectAnimator animator = ObjectAnimator.ofFloat(jiemian, "scaleX", 1f, 0f);
            animator.setDuration(600);//时间1s
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    jiemian.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();


            if (async!=null){
                async.cancel(true);
                async=null;
            }

            isTrue2=false;
            isTrue=false;


            Log.d("ReadAsync", "返回数据22222");
            _beepManager.playBeepSoundAndVibrate();


            updateView(arg0);



        } else {
            isTrue2=true;
//            print("自动读取中...请放入身份证后稍等片刻");
        }

    }

    private void updateView(IDCardItem item) {

        // StringBuilder sb = new StringBuilder();
//        sb.append("姓名:" + item.partyName + "\n");
//        sb.append("性别:" + item.gender + "\n");
//        sb.append("民族:" + item.nation + "\n");
//        sb.append("出生:" + item.bornDay + "\n");
//        sb.append("住址:" + item.certAddress + "\n");
//        sb.append("证件号:" + item.certNumber + "\n");
//        sb.append("签发机关:" + item.certOrg + "\n");
        String effDate = item.effDate;
        String expDate = item.expDate;
//        sb.append("有效期限:" + effDate.substring(0, 4) + "." + effDate.substring(4, 6) + "." + effDate.substring(6, 8)
//                + "-" + expDate.substring(0, 4) + "." + expDate.substring(4, 6) + "." + expDate.substring(6, 8) + "\n");

        zhengjianzhao.setImageBitmap(scale(item.picBitmap));

        String fn="aaaa.jpg";
        FileUtil.isExists(FileUtil.PATH,fn);

        saveBitmap2File(item.picBitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);

        String time1=item.bornDay.substring(0, 4) + "-" + item.bornDay.substring(4, 6) + "-" + item.bornDay.substring(6, 8);
        String time2=effDate.substring(0, 4) + "-" + effDate.substring(4, 6) + "-" + effDate.substring(6, 8);
        String time3=expDate.substring(0, 4) + "-" + expDate.substring(4, 6) + "-" + expDate.substring(6, 8);

        initInFo(item,time1);

        userInfoBena=new UserInfoBena(item.partyName,item.gender.equals("男")?1+"":2+"",item.nation,time1,item.certAddress,item.certNumber,item.certOrg,time2,time3,null,null,null);


        //  idCardItem = item;
    }


    protected Bitmap scale(Bitmap bitmap) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);

        int width = displaysMetrics.widthPixels;
        Matrix matrix = new Matrix();
        float scale = width / (8.0f * bitmap.getWidth());
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }



    private void initInFo(IDCardItem item, String time1) {
        name.setText(item.partyName);
        shenfengzheng.setText(item.certNumber);
        xingbie.setText(item.gender);
        mingzu.setText(item.nation);
        chusheng.setText(time1);
        fazhengjiguan.setText(item.certOrg);
        String time2=item.effDate.substring(0, 4) + "." + item.effDate.substring(4, 6) + "." + item.effDate.substring(6, 8);
        String time3=item.expDate.substring(0, 4) + "." + item.expDate.substring(4, 6) + "." + item.expDate.substring(6, 8);
        youxiaoqixian.setText(time2+" -- "+time3);
        zhuzhi.setText(item.certAddress);
        progressBarWithNumber.setVisibility(View.GONE);


    }

    /***
     *保存bitmap对象到文件中
     * @param bm
     * @param path
     * @param quality
     * @return
     */
    public  void saveBitmap2File(Bitmap bm, final String path, int quality) {
        if (null == bm || bm.isRecycled()) {

            return ;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            shengfenzhengPath=path;

            kaishiPaiZhao();


        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (!bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }
    }

    private void kaishiPaiZhao(){

        if (mediaPlayer.isPlaying()){

            startThread();

        }else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isPaiZhao) {

                        if (isPaiZhao2) {
                            isPaiZhao2 = false;

                            try {
                                Thread.sleep(5000);

                                if (mediaPlayer != null) {
                                    final Uri uri=Uri.parse("rtsp://"+ip+"/user=admin&password=&channel=1&stream=0.sdp");
                                    media=null;
                                    media = new Media(libvlc, uri);
                                    mediaPlayer.setMedia(media);
                                    videoView.setKeepScreenOn(true);
                                    mediaPlayer.play();
                                    Log.d("InFoActivity", "播放");

                                }

                                if (mediaPlayer.isPlaying()) {

                                    startThread();

                                } else {

                                    cishu++;
                                    Log.d("InFoActivity", "444444   " + cishu);
                                    if (cishu == 3) {

                                        isPaiZhao2=false;
                                        isPaiZhao = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast tastyToast= TastyToast.makeText(InFoActivity.this,"开启摄像头失败，请返回后重试!",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                                tastyToast.setGravity(Gravity.CENTER,0,0);
                                                tastyToast.show();
                                            }
                                        });

                                    } else {

                                        isPaiZhao2=true;

                                    }

                                }


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onPause() {

         isPaiZhao=false;
         isPaiZhao2=false;
        isTrue2=false;
        isTrue=false;

        if (async!=null){
            async.cancel(true);
            async=null;
        }
        isTrue4=false;
        isTrue3=false;

        count=1;
        cishu=1;
        mSerialPortReadClient.closeSerialPort();
        mSerialPortReadClient.disconnect();

        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
            jiaZaiDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        _beepManager.close();
        unregisterReceiver(sensorInfoReceiver);

        super.onDestroy();

    }

    private void link_save() {
        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();


//    /* form的分割线,自己定义 */
//        String boundary = "xx--------------------------------------------------------------xx";
        RequestBody body = new FormBody.Builder()
                .add("cardNumber",userInfoBena.getCertNumber())
                .add("name",userInfoBena.getPartyName())
                .add("gender",userInfoBena.getGender())
                .add("birthday",userInfoBena.getBornDay())
                .add("address",userInfoBena.getCertAddress())
                .add("cardPhoto",userInfoBena.getCardPhoto())
                .add("scanPhoto",userInfoBena.getScanPhoto())
                .add("organ",userInfoBena.getCertOrg())
                .add("termStart",userInfoBena.getEffDate())
                .add("termEnd",userInfoBena.getExpDate())
                .add("accountId","1")
                .add("result",biduijieguo)
                .add("homeNumber",fanghao.getText().toString().trim())
                .add("phone",dianhua.getText().toString().trim())
                .add("carNumber",chepaihao.getText().toString().trim())
                .add("score",xiangsi)
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji + "/saveCompareResult.do");

        if (tiJIaoDialog==null){
            tiJIaoDialog=new TiJIaoDialog(InFoActivity.this);
            tiJIaoDialog.show();
        }

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求识别失败"+e.getMessage());
                if (tiJIaoDialog!=null){
                    tiJIaoDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (tiJIaoDialog!=null){
                    tiJIaoDialog.dismiss();
                }
                Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss=body.string().trim();
                    Log.d("InFoActivity", ss);

                    if (Long.parseLong(ss)>0){

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                Toast tastyToast= TastyToast.makeText(InFoActivity.this,"保存成功",TastyToast.LENGTH_LONG,TastyToast.INFO);
//                                tastyToast.setGravity(Gravity.CENTER,0,0);
//                                tastyToast.show();
//
//                            }
//                        });
                        startActivity(new Intent(InFoActivity.this,ShiYouActivity.class)
                                .putExtra("name",name.getText().toString())
                                .putExtra("biduijieguo",bidui)
                                .putExtra("id",ss+""));

                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast= TastyToast.makeText(InFoActivity.this,"保存失败,请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                tastyToast.setGravity(Gravity.CENTER,0,0);
                                tastyToast.show();

                            }
                        });


                    }

                }catch (Exception e){

                    if (tiJIaoDialog!=null){
                        tiJIaoDialog.dismiss();
                    }
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


    }



    private void startThread(){

        Thread thread=  new Thread(new Runnable() {
            @Override
            public void run() {

                while (isTrue3){

                    if (isTrue4){

                        isTrue4=false;

                        bitmapBig=videoView.getBitmap();

                        if (bitmapBig!=null) {

                            List<VisionDetRet> results = mFaceDet.detect(bitmapBig);

                            if (results != null) {

                                int s = results.size();
                                VisionDetRet face;
                                if (s > 0) {

                                if (s > count - 1) {

                                    face = results.get(count - 1);

                                } else {

                                    face = results.get(0);

                                }

                                int xx = 0;
                                int yy = 0;
                                int xx2 = 0;
                                int yy2 = 0;
                                int ww = bitmapBig.getWidth();
                                int hh = bitmapBig.getHeight();
                                if (face.getRight() - 300 >= 0) {
                                    xx = face.getRight() - 300;
                                } else {
                                    xx = 0;
                                }
                                if (face.getTop() - 200 >= 0) {
                                    yy = face.getTop() - 200;
                                } else {
                                    yy = 0;
                                }
                                if (xx + 380 <= ww) {
                                    xx2 = 380;
                                } else {
                                    xx2 = ww - xx-1;
                                }
                                if (yy + 500 <= hh) {
                                    yy2 =  500;
                                } else {
                                    yy2 = hh - yy-1;
                                }


//                            Bitmap bmpf = bitmapBig.copy(Bitmap.Config.RGB_565, true);
//                            //返回识别的人脸数
//                            //	int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 1).findFaces(bmpf, facess);
//                            //	FaceDetector faceCount2 = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 2);
//
//                            myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
//                            myFaceDetect = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), numberOfFace);
//                            numberOfFaceDetected = myFaceDetect.findFaces(bmpf, myFace);    //FaceDetector 构造实例并解析人脸
//
//                            if (numberOfFaceDetected > 0) {
//
//                                FaceDetector.Face face;
//                                if (numberOfFaceDetected>count-1){
//                                    face = myFace[count-1];
//
//                                }else {
//                                    face = myFace[0];
//
//                                }
//
//                                PointF pointF = new PointF();
//                                face.getMidPoint(pointF);
//                                myEyesDistance = (int)face.eyesDistance();
//
//                                int xx=0;
//                                int yy=0;
//                                int xx2=0;
//                                int yy2=0;
//
//                                if ((int)pointF.x-200>=0){
//                                    xx=(int)pointF.x-200;
//                                }else {
//                                    xx=0;
//                                }
//                                if ((int)pointF.y-200>=0){
//                                    yy=(int)pointF.y-200;
//                                }else {
//                                    yy=0;
//                                }
//                                if (xx+350 >=bitmapBig.getWidth()){
//                                    xx2=bitmapBig.getWidth()-xx;
//
//                                }else {
//                                    xx2=350;
//                                }
//                                if (yy+350>=bitmapBig.getHeight()){
//                                    yy2=bitmapBig.getHeight()-yy;
//
//                                }else {
//                                    yy2=350;
//                                }


                                Bitmap bitmap = Bitmap.createBitmap(bitmapBig, xx, yy, xx2, yy2);

                                Message message = Message.obtain();
                                message.what = MESSAGE_QR_SUCCESS;
                                message.obj = bitmap;
                                mHandler2.sendMessage(message);


                                String fn = "bbbb.jpg";
                                FileUtil.isExists(FileUtil.PATH, fn);
                                saveBitmap2File2(bitmap, FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn, 100);


                            } else {
                                isTrue4 = true;
                            }
                        }

//                            bmpf.recycle();
//                            bmpf = null;

                        }

                    }

                }


            }
        });

        thread.start();

    }

    public  void saveBitmap2File2(Bitmap bm, final String path, int quality) {
        try {
            filePath2=path;
            if (null == bm) {
                Log.d("InFoActivity", "回收|空");
                return ;
            }

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tishi.setVisibility(View.VISIBLE);
                    tishi.setText("上传图片中。。。");
                }
            });

            link_P1(shengfenzhengPath,filePath2);


        } catch (Exception e) {
            e.printStackTrace();

        } finally {

//			if (!bm.isRecycled()) {
//				bm.recycle();
//			}
            bm = null;
        }
    }


    public static final int TIMEOUT = 1000 * 60;
    private void link_P1(String filename1, final String fileName2) {


        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

         /* 第一个要上传的file */
        file1 = new File(filename1);
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
        final String file1Name = System.currentTimeMillis()+"testFile1.jpg";

//    /* 第二个要上传的文件,*/
//        File file2 = new File(fileName2);
//        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
//        String file2Name =System.currentTimeMillis()+"testFile2.jpg";


//    /* form的分割线,自己定义 */
//        String boundary = "xx--------------------------------------------------------------xx";

        MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            /* 底下是上传了两个文件 */
                .addFormDataPart("voiceFile" , file1Name , fileBody1)
                  /* 上传一个普通的String参数 */
                //  .addFormDataPart("subject_id" , subject_id+"")
                //  .addFormDataPart("image_2" , file2Name , fileBody2)
                .build();
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(mBody)
                .url(zhuji + "/AppFileUploadServlet?FilePathPath=cardFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tishi.setText("上传图片出错，请返回后重试！");
                    }
                });
                Log.d("AllConnects", "请求识别失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss=body.string();

                    Log.d("AllConnects", "aa   "+ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    userInfoBena.setCardPhoto(zhaoPianBean.getExDesc());
                    link_P2(fileName2);


                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tishi.setText("上传图片出错，请返回后重试！");
                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                    isTrue4=false;
                    isTrue3=false;
                }
            }
        });


    }

    private void link_P2( String fileName2) {
        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

//         /* 第一个要上传的file */
//        File file1 = new File(filename1);
//        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
//        final String file1Name = System.currentTimeMillis()+"testFile1.jpg";

    /* 第二个要上传的文件,*/
        file2 = new File(fileName2);
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
        String file2Name =System.currentTimeMillis()+"testFile2.jpg";


//    /* form的分割线,自己定义 */
//        String boundary = "xx--------------------------------------------------------------xx";

        MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            /* 底下是上传了两个文件 */
                //  .addFormDataPart("image_1" , file1Name , fileBody1)
                  /* 上传一个普通的String参数 */
                //  .addFormDataPart("subject_id" , subject_id+"")
                .addFormDataPart("voiceFile" , file2Name , fileBody2)
                .build();
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(mBody)
                .url(zhuji + "/AppFileUploadServlet?FilePathPath=compareFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");


        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求识别失败"+e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tishi.setText("上传图片出错，请返回后重试！");
                    }
                });
                isTrue4=false;
                isTrue3=false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //删掉文件

                //获得返回体
                try {

                    ResponseBody body = response.body();
                    // Log.d("AllConnects", "aa   "+response.body().string());

                    JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    userInfoBena.setScanPhoto(zhaoPianBean.getExDesc());

                    link_tianqi3();


                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tishi.setText("上传图片出错，请返回后重试！");
                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


    }

    private void link_tianqi3() {
        //final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        //http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();


//    /* form的分割线,自己定义 */
//        String boundary = "xx--------------------------------------------------------------xx";
        RequestBody body = new FormBody.Builder()
                .add("cardPhoto",userInfoBena.getCardPhoto())
                .add("scanPhoto",userInfoBena.getScanPhoto())
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji + "/compare.do");


        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tishi.setText("上传图片出错，请返回后重试！");
                    }
                });
                Log.d("AllConnects", "请求识别失败"+e.getMessage());
                isTrue4=false;
                isTrue3=false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //获得返回体
                try {
                    count++;

                    ResponseBody body = response.body();
                    // Log.d("AllConnects", "识别结果返回"+response.body().string());
                    String ss=body.string();
                    Log.d("InFoActivity", ss);
                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    final ShiBieBean zhaoPianBean=gson.fromJson(jsonObject,ShiBieBean.class);

                    if (zhaoPianBean.getScore()>=75.0) {

                        //比对成功
                        sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",true)
                                .putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
                                .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                        count=1;

                        qiehuan();

                    }else {


                        if (count<=3){

                            Message message=Message.obtain();
                            message.what=22;
                            mHandler2.sendMessage(message);

                            isTrue4=true;


                        }else {

                            sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false)
                                    .putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
                                    .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                            count=1;

                            qiehuan();
                        }

                    }


                }catch (Exception e){

                    if (count<=3){

                        Message message=Message.obtain();
                        message.what=22;
                        mHandler2.sendMessage(message);

                        isTrue4=true;


                    }else {

                        sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false).putExtra("xiangsidu","43.21")
                                .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                        count=1;

                        qiehuan();

                    }

                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


    }

    private void qiehuan(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                jiemian.setVisibility(View.VISIBLE);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(jiemian, "scaleY", 0f, 1f);
                animator2.setDuration(600);//时间1s
                animator2.start();
                //起始为1，结束时为0
                ObjectAnimator animator = ObjectAnimator.ofFloat(jiemian, "scaleX", 0f, 1f);
                animator.setDuration(600);//时间1s
                animator.start();
            }
        });

    }
}
