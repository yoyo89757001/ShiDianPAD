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

import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.media.FaceDetector;
import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
import com.example.xiaojun.shidianpad.utils.AppendAPI;
import com.example.xiaojun.shidianpad.utils.FileUtil;
import com.example.xiaojun.shidianpad.utils.GsonUtil;
import com.example.xiaojun.shidianpad.utils.LibVLCUtil;
import com.example.xiaojun.shidianpad.utils.Utils;
import com.example.xiaojun.shidianpad.view.AutoFitTextureView;
import com.example.xiaojun.shidianpad.view.HorizontalProgressBarWithNumber;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.kaeridcard.tools.Tool;
import com.lzw.qlhs.Wlt2bmp;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import slam.ajni.JniCall;

public class InFoActivity2 extends Activity {
    private EditText name,shenfengzheng,xingbie,mingzu,chusheng,dianhua,fazhengjiguan,
            youxiaoqixian,zhuzhi,fanghao,chepaihao,shibiejieguo,xiangsifdu;
    private ImageView zhengjianzhao,xianchengzhao;
    private Button button;
    private HorizontalProgressBarWithNumber progressBarWithNumber;
    private BeepManager _beepManager;
   // public static final String HOST="http://192.168.0.104:8080";
    private JiaZaiDialog jiaZaiDialog=null;
    private String xiangsi="";
    private String biduijieguo="";
    private TiJIaoDialog tiJIaoDialog=null;
    private Button quxiao;
    public static final String HOST="http://192.168.2.101:8081";
   // public static final String HOST="http://174p2704z3.51mypc.cn:11100";
  //  public static final String HOST="http://192.168.2.43:8080";
    private MediaPlayer mediaPlayer=null;
    private IVLCVout vlcVout=null;
    private IVLCVout.Callback callback;
    private LibVLC libvlc;
    private Media media;
    private static int cishu=0;
//    private static boolean isTrue=true;
//    private static boolean isTrue2=true;
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
    private AutoFitTextureView videoView;
    long c=0;
    private  String zhuji=null;
    private boolean bidui=false;
    private String shengfenzhengPath=null;
    private LinearLayout jiemian;
    private RelativeLayout shipingRL;
    private FaceDet mFaceDet;
    private float    mRelativeFaceSize   = 0.2f;
    private int      mAbsoluteFaceSize   = 0;
    private static int lian=0;
    private static JniCall myJni;
    private Handler mhandler = null;
    private int iDetect = 0;
    private static boolean isPaiZhao=true;
    private static boolean isPaiZhao2=true;
    ToastShow toastShow1 = null;
    private int iInit;
    byte[] cardinfo = new byte[256 * 8 + 1024];

//    static {
//        System.loadLibrary("SeetafaceSo");
//    }

    // 性别数组
    private String[] strSex = new String[10];
    // 民簇数组
    private String[] strCluster = { "汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮",
            "布依", "朝鲜", "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳",
            "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "柯尔克孜", "土", "达斡尔",
            "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米", "塔吉克", "怒",
            "乌孜别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔", "独龙", "鄂伦春",
            "赫哲", "门巴", "珞巴", "基诺"

    };


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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian2);

        mFaceDet= MyAppLaction.mFaceDet;

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
                        Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"请先设置主机地址",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();

                    }
                });


            }

        });

        isTrue3=true;
        isTrue4=true;
        isPaiZhao=true;
        isPaiZhao2=true;
        cishu=0;

        strSex[0] = "未知";
        strSex[1] = "男";
        strSex[2] = "女";
        strSex[9] = "未说明";

        toastShow1 = new ToastShow(InFoActivity2.this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                myJni = JniCall.getInstance();
                 iInit = myJni.Mini_Init(0x01);

                // 4.8.4 探测二代证阅读器
                iDetect = myJni.Mini_idcard_device_detect();
                if (iDetect == -1) {
                    toastShow1.toastShow("请确认二代证阅读器是否存在！");

                }
            }
        }).start();
        // 创建、初始化接口类


        if (iInit == -1) {
            toastShow1.toastShow("JniCall初始化失败！");

        } else {
            mhandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            String strMsg1 = (String) msg.obj;
                            toastShow1.toastShow(strMsg1);

                            break;
                        case 14: //姓名

                            String strObject = (String) msg.obj;
                         //   Log.d("InFoActivity2", strObject);

                            try {
                                Pattern p = Pattern.compile("\\s+");
                                Matcher m = p.matcher(strObject);
                                strObject = m.replaceAll(" ");
                                // 显示身份证信息
                                String[] strRfidCardArray = strObject.split(" ");
                                name.setText(strRfidCardArray[0]); // 姓名
                                userInfoBena.setPartyName(strRfidCardArray[0]);

                                String strTmp = strRfidCardArray[1];
                                // 性别
                                String strTmp3 = strTmp.substring(0, 1);
                                int i1 = Integer.parseInt(strTmp3);
                                xingbie.setText(strSex[i1]); // 性别 民簇 出生日期 地址
                                if (strSex[i1].equals("男")){

                                    userInfoBena.setGender("1");
                                }else {
                                    userInfoBena.setGender("2");
                                }

                                // 民簇
                                strTmp3 = strTmp.substring(1, 3);
                                i1 = Integer.parseInt(strTmp3);
                                mingzu.setText(strCluster[i1 - 1]);
                                userInfoBena.setNation(strCluster[i1 - 1]);
                                // 出生日期
                                String  strTmp4 = strTmp.substring(3, 7) + "-"
                                        + strTmp.substring(7, 9) + "-"
                                        + strTmp.substring(9, 11);
                                chusheng.setText(strTmp4);
                                userInfoBena.setBornDay(strTmp4);
                                // 地址
                                String strTmp5 = strTmp.substring(11, strTmp.length());
                                zhuzhi.setText(strTmp5);
                                userInfoBena.setCertAddress(strTmp5);

                                strTmp = strRfidCardArray[2];
                                String strTmp6 = strTmp.substring(0, 18);
                                strTmp6 = strTmp6.substring(0, 2) + "**************" + strTmp6.substring(16, 18);
                                shenfengzheng.setText(strTmp6);
                                userInfoBena.setCertNumber(strTmp6);

                                String strTmp7 = strTmp.substring(18, strTmp.length());
                                fazhengjiguan.setText(strTmp7);
                                userInfoBena.setCertOrg(strTmp7);

                                strTmp = strRfidCardArray[3];
                                String kaishi=strTmp.substring(0,4)+"-"+strTmp.substring(4,6)+"-"+strTmp.substring(6,8);

                                String jieshu=strTmp.substring(8,12)+"-"+strTmp.substring(12,14)+"-"+strTmp.substring(14,16);

                                youxiaoqixian.setText(kaishi+" 至 "+jieshu);
                                userInfoBena.setEffDate(kaishi);
                                userInfoBena.setExpDate(jieshu);

                            } catch (Exception e) {
                                Log.d("InFoActivity2", ""+e.getMessage());
                                toastShow1.toastShow("身份证件信息不正确，请联系软件开发人员！");

                            }

                            break;
                        case 15:
                            String strMsg4 = (String) msg.obj;
                            toastShow1.toastShow(strMsg4);
                            break;
                        case 16:
                            String strMsg16 = (String) msg.obj;
                            toastShow1.toastShow(strMsg16);

                            break;
                        case 144:
                            //读取成功
//                            String strMsg144 = (String) msg.obj;
//                            toastShow1.toastShow(strMsg144);
                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                jiaZaiDialog.setText("开启摄像头中,请稍后...");
                            }


                            break;

                        case 155:

                            String strMsg155 = (String) msg.obj;
                            toastShow1.toastShow(strMsg155);

                            break;
                        case 188:

                            String strMsg188 = (String) msg.obj;
                            toastShow1.toastShow(strMsg188);
                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                jiaZaiDialog.setText("开启摄像失败,返回后重试");
                            }

                            break;
                    }
                    super.handleMessage(msg);
                }
            };
        }



        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("guanbi");
        intentFilter1.addAction("guanbi2");
        sensorInfoReceiver = new SensorInfoReceiver();
        registerReceiver(sensorInfoReceiver, intentFilter1);

        videoView= (AutoFitTextureView) findViewById(R.id.fff);
        videoView.setAspectRatio(5,3);
        imageView= (ImageView) findViewById(R.id.ffff);

        jiemian= (LinearLayout) findViewById(R.id.jiemian);
        shipingRL= (RelativeLayout) findViewById(R.id.shiping_rl);
        tishi= (TextView) findViewById(R.id.tishi);
        libvlc= LibVLCUtil.getLibVLC(InFoActivity2.this);
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
                        Log.d("InFoActivity2", "onSurfaceTextureDestroyed销毁");

                        return true;
                    }

                    @Override
                    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                        Log.d("InFoActivity2", "ddddd"+surface.getTimestamp());

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
                       Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"请先设置摄像头IP",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                       tastyToast.setGravity(Gravity.CENTER,0,0);
                       tastyToast.show();
                   }
               });


            }

        });

        userInfoBena=new UserInfoBena();

       // ip = Utils.getIp(this);
        //String source = Utils.getPassword(this);

        ImageView imageView= (ImageView) findViewById(R.id.dd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        initView();

        _beepManager = new BeepManager(InFoActivity2.this);

        jiaZaiDialog=new JiaZaiDialog(InFoActivity2.this);
        jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        jiaZaiDialog.show();

        connectDevice();
    }

    @Override
    protected void onResume() {
        super.onResume();




    }

    private Handler handlerGongGao = new Handler();
    private Runnable runnableGongGao = new Runnable() {

        @Override
        public void run() {
            // //【功能】探测身份证卡片是否存在

             int iRfidCardIsFind = myJni.Mini_rfid_card_is_find (600);
           // Log.d("InFoActivity2", "iRfidCardIsFind:" + iRfidCardIsFind);
            if (iRfidCardIsFind==1){
                //读卡
                ReadRfidCardThread readSimCardThread = new ReadRfidCardThread();
                readSimCardThread.run();

            }else {

                handlerGongGao.postDelayed(runnableGongGao,1000);//4秒后再次执行
            }

        }
    };

    private void  connectDevice() {
        try {

            handlerGongGao.postDelayed(runnableGongGao,800);//4秒后再次执行

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // 读取身份证信息的线程
    public class ReadRfidCardThread extends Thread {
        //public Context ywclActivity = null;
        //public JniCall myJni = null;
        final int iReadTimeOut = 3000;
        private String strMsg3 = "";

        @Override
        public void run() {

                Arrays.fill(cardinfo, (byte) 32);
                short[] len = new short[4];
                int iCardRead = myJni.Mini_idcard_read((short) iReadTimeOut,
                        cardinfo, len);

                if (iCardRead == -1) {
                    strMsg3 = "搜寻设备失败！";
                    //Toast.makeText(ywclActivity, "搜寻设备失败！", Toast.LENGTH_SHORT).show();
                } else if (iCardRead == -2) {
                    strMsg3 = "搜寻身份证失败！";
                    //Toast.makeText(ywclActivity, "搜寻身份证失败！", Toast.LENGTH_SHORT).show();
                } else if (iCardRead == -3) {
                    strMsg3 = "读取身份证信息失败！";
                    //Toast.makeText(ywclActivity, "读取身份证信息失败！", Toast.LENGTH_SHORT).show();
                } else if (iCardRead == -4) {
                    strMsg3 = "用户取消身份证识别！";
                    //Toast.makeText(ywclActivity, "用户取消身份证识别！", Toast.LENGTH_SHORT).show();
                } else if (iCardRead == 1) {


                    // 文字信息长度
                    int iDataLen3 = cardinfo[10] * 256 + cardinfo[11];
                    // 照片信息长度
                    int iDataLen4 = cardinfo[12] * 256 + cardinfo[13];
                    String strRfidCard = "";
                    strRfidCard = AppendAPI.Byte2Unicode(cardinfo, 14, iDataLen3);

                    Message message = new Message();
                    message.what = 14;
                    message.obj = strRfidCard;

                    mhandler.sendMessage(message);

                    try {
                        //照片信息解码
                        byte[] bPicBitmap = new byte[iDataLen4]; // src, srcPos, dest, destPos, length
                        System.arraycopy(cardinfo, 14 + iDataLen3, bPicBitmap, 0,
                                iDataLen4);
                        final Bitmap bitmap1 = getPicBitmap(bPicBitmap);

                        zhengjianzhao.setImageBitmap(bitmap1);

                        String fn="aaaa.jpg";
                        FileUtil.isExists(FileUtil.PATH,fn);
                        saveBitmap2File(bitmap1.copy(Bitmap.Config.ARGB_8888, true), FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    strMsg3 = "读取身份证信息成功！";
                }

            Message message3 = new Message();
            message3.what = 144;
            message3.obj = strMsg3;
            mhandler.sendMessage(message3);


        }
    }


    // 身份证图片解码
    private Bitmap getPicBitmap(byte[] pic_src) {
        final int HEAD_LEN = 0x132;
        final int BMP_LEN = 38556;

        byte[] tmp_data = new byte[40 * 1024];
        int result = 0;
        try {
           // System.out.println("before unpack");
            // Wlt2bmp.kaerunpack();
            result = Wlt2bmp.picUnpack(pic_src, tmp_data);
           // System.out.println("after unpack");

        } catch (Exception e) {

            e.printStackTrace();
        }

        if (result != 1)
            return null;

        byte[] bmp_data = new byte[BMP_LEN];
        System.arraycopy(tmp_data, 0, bmp_data, 0, BMP_LEN);
        return Tool.createRgbBitmap(bmp_data, 102, 126);

    }

    public class ToastShow {
        private Context context;
        private Toast toast = null;
        public ToastShow(Context context) {
            this.context = context;

        }
        public void toastShow(String text) {


                toast = TastyToast.makeText(InFoActivity2.this,text,TastyToast.LENGTH_LONG,TastyToast.INFO);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

        }
    }


//    public String getMD5(byte[] source) {
//        String s = null;
//        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
//                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
//                'e', 'f' };
//        try {
//            java.security.MessageDigest md = java.security.MessageDigest
//                    .getInstance("MD5");
//            md.update(source);
//            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
//            // 用字节表示就是 16 个字节
//            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
//            // 所以表示成 16 进制需要 32 个字符
//            int k = 0; // 表示转换结果中对应的字符位置
//            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
//                // 转换成 16 进制字符的转换
//                byte byte0 = tmp[i]; // 取第 i 个字节
//                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
//                // >>> 为逻辑右移，将符号位一起右移
//                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
//            }
//            s = new String(str); // 换后的结果转换为字符串
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return s;
//    }

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
                            final QueRenDialog dialog=new QueRenDialog(InFoActivity2.this,"比对不通过,请返回后重试");
                            dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                            dialog.setOnPositiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                            dialog.show();
                        }


                    }catch (Exception e){
                        Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"数据异常,请返回后重试",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();
                        Log.d("InFoActivity", e.getMessage());
                    }

                }else {
                    Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"请先读取身份证信息",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }

            }
        });
        quxiao= (Button) findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBarWithNumber= (HorizontalProgressBarWithNumber) findViewById(R.id.id_progressbar01);

    }


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
                   quxiao.setVisibility(View.GONE);

               }else {
                   shibiejieguo.setText("比对不通过");
                   biduijieguo="比对不通过";
                   quxiao.setVisibility(View.VISIBLE);

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


    /***
     *保存bitmap对象到文件中
     * @param bm
     * @param path
     * @param quality
     * @return
     */
    public  void saveBitmap2File(Bitmap bm, final String path, int quality) {
        if (null == bm || bm.isRecycled()) {
            Log.d("InFoActivity", "回收|空");
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
                                Thread.sleep(15000);
                                if (mediaPlayer.isPlaying()) {

                                    startThread();

                                } else {

                                    cishu++;
                                    Log.d("InFoActivity", "444444   " + cishu);
                                    if (cishu == 2) {

                                        isPaiZhao2=false;
                                        isPaiZhao = false;

                                        Message message3 = new Message();
                                        message3.what = 188;
                                        message3.obj = "开启摄像头失败，请返回后重试";
                                        mhandler.sendMessage(message3);

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
        super.onPause();

        Log.d("InFoActivity2", "暂停");

        if (handlerGongGao!=null){
            handlerGongGao.removeCallbacks(runnableGongGao);
            handlerGongGao=null;
        }

        if (myJni!=null)
            myJni.Mini_release();

        count=1;

        isTrue4=false;
        isTrue3=false;
        cishu=0;
        isPaiZhao=false;
        isPaiZhao2=false;
            if (_beepManager!=null){
                _beepManager.close();
                _beepManager=null;
            }


        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
            jiaZaiDialog.dismiss();
        }

    }

    @Override
    protected void onDestroy() {

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
            tiJIaoDialog=new TiJIaoDialog(InFoActivity2.this);
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
                    Log.d("InFoActivity2", ss);
                    if (Long.parseLong(ss)>0){

                        startActivity(new Intent(InFoActivity2.this,ShiYouActivity.class)
                                .putExtra("name",name.getText().toString())
                                .putExtra("biduijieguo",bidui)
                                .putExtra("id",ss+""));

                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast= TastyToast.makeText(InFoActivity2.this,"保存失败",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
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
                    }
                });

        Log.d("InFoActivity2", "fff");


        Thread thread=  new Thread(new Runnable() {
            @Override
            public void run() {

                while (isTrue3){

                    if (isTrue4){
                        isTrue4=false;
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    bitmapBig=videoView.getBitmap();

                                }
                            });
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (bitmapBig!=null){

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
                                        if (face.getTop() - 220 >= 0) {
                                            yy = face.getTop() - 220;
                                        } else {
                                            yy = 0;
                                        }
                                        if (xx + 430 <= ww) {
                                            xx2 = 430;
                                        } else {
                                            xx2 = ww - xx - 1;
                                        }
                                        if (yy + 430 <= hh) {
                                            yy2 = 430;
                                        } else {
                                            yy2 = hh - yy - 1;
                                        }

//                                               Bitmap bmpf = bitmapBig.copy(Bitmap.Config.RGB_565, true);
//
//                                               //返回识别的人脸数
//                                               //	int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 1).findFaces(bmpf, facess);
//                                               //	FaceDetector faceCount2 = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 2);
//
//                                               myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
//                                               myFaceDetect = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), numberOfFace);
//                                               numberOfFaceDetected = myFaceDetect.findFaces(bmpf, myFace);    //FaceDetector 构造实例并解析人脸
//
//                                               if (numberOfFaceDetected > 0) {
//
//                                                   FaceDetector.Face face;
//                                                   if (numberOfFaceDetected>count-1){
//                                                       face = myFace[count-1];
//
//                                                   }else {
//                                                       face = myFace[0];
//
//                                                   }
//
//                                                   PointF pointF = new PointF();
//                                                   face.getMidPoint(pointF);
//
//
//                                                 //  myEyesDistance = (int)face.eyesDistance();
//
//                                                   int xx=0;
//                                                   int yy=0;
//                                                   int xx2=0;
//                                                   int yy2=0;
//
//                                                   if ((int)pointF.x-200>=0){
//                                                       xx=(int)pointF.x-200;
//                                                   }else {
//                                                       xx=0;
//                                                   }
//                                                   if ((int)pointF.y-320>=0){
//                                                       yy=(int)pointF.y-320;
//                                                   }else {
//                                                       yy=0;
//                                                   }
//                                                   if (xx+350 >=bitmapBig.getWidth()){
//                                                       xx2=bitmapBig.getWidth()-xx;
//
//                                                   }else {
//                                                       xx2=350;
//                                                   }
//                                                   if (yy+500>=bitmapBig.getHeight()){
//                                                       yy2=bitmapBig.getHeight()-yy;
//
//                                                   }else {
//                                                       yy2=500;
//                                                   }

                                        Bitmap bitmap = Bitmap.createBitmap(bitmapBig, xx, yy, xx2, yy2);

                                        // Bitmap bitmap = Bitmap.createBitmap(bitmapBig,0,0,bitmapBig.getWidth(),bitmapBig.getHeight());

                                        Message message3 = Message.obtain();
                                        message3.what = MESSAGE_QR_SUCCESS;
                                        message3.obj = bitmap;
                                        mHandler2.sendMessage(message3);


                                        String fn = "bbbb.jpg";
                                        FileUtil.isExists(FileUtil.PATH, fn);
                                        saveBitmap2File2(bitmap, FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn, 100);


                                    } else {
                                        isTrue4 = true;
                                    }

                                    //  bmpf.recycle();
                                    //  bmpf = null;
                                }else {
                                    isTrue4 = true;
                                }


                            }else {
                                isTrue4 = true;
                            }

                        }catch (IllegalStateException e){
                            Log.d("InFoActivity2", e.getMessage()+"");
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

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (vlcVout!=null){
                    vlcVout.detachViews();
                    vlcVout.removeCallback(callback);
                    vlcVout=null;
                }
                if (videoView!=null){
                    videoView.setSurfaceTextureListener(null);
                }

            }
        }).start();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                videoView.setVisibility(View.GONE);
                jiemian.setVisibility(View.VISIBLE);
                isTrue4=false;
                isTrue3=false;

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
