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
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xiaojun.shidianpad.MyAppLaction;
import com.example.xiaojun.shidianpad.R;
import com.example.xiaojun.shidianpad.beans.BaoCunBean;
import com.example.xiaojun.shidianpad.beans.BaoCunBeanDao;
import com.example.xiaojun.shidianpad.beans.Photos;
import com.example.xiaojun.shidianpad.beans.ShiBieBean;
import com.example.xiaojun.shidianpad.beans.ShouFangBean;
import com.example.xiaojun.shidianpad.beans.UserInfoBena;
import com.example.xiaojun.shidianpad.dialog.JiaZaiDialog;
import com.example.xiaojun.shidianpad.dialog.TiJIaoDialog;
import com.example.xiaojun.shidianpad.dialog.XuanZeDialog;
import com.example.xiaojun.shidianpad.utils.FileUtil;
import com.example.xiaojun.shidianpad.utils.GsonUtil;
import com.example.xiaojun.shidianpad.view.AutoFitTextureView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.idcard.IdCard;
import com.telpo.tps550.api.idcard.IdentityInfo;
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


public class InFoActivity3 extends Activity implements View.OnClickListener {
    private EditText shenfengzheng,xingbie,mingzu,chusheng,dianhua,fazhengjiguan,
            youxiaoqixian,zhuzhi,fanghao,chepaihao,shibiejieguo,xiangsifdu;
    private ImageView zhengjianzhao,xianchengzhao;
    private Button button;
    private TextView name;
 //   private File mSavePhotoFile;
  //  private HorizontalProgressBarWithNumber progressBarWithNumber;
   // public static final String HOST="http://192.168.0.104:8080";
    private JiaZaiDialog jiaZaiDialog=null;
    private String xiangsi="";
    private String biduijieguo="";
    private TiJIaoDialog tiJIaoDialog=null;
    private Bitmap bitmap2=null;
    public static final int TIMEOUT = 1000 * 60;
    private static boolean isTrue=true;
    private static boolean isTrue2=true;
    private boolean bidui=false;
    private Bitmap bitmapBig=null;
    private GetIDInfoTask async=null;
    private UserInfoBena userInfoBena=null;
    private SensorInfoReceiver sensorInfoReceiver;
  //  private String filePath=null;
    private String filePath2=null;
    private File file1=null;
 //   private File file2=null;
    long c=0;
    private Thread thread;
    private String shengfenzhengPath=null;
//    private static int lian=0;
//    private Handler mhandler = null;
//    private int iDetect = 0;
    private IdentityInfo info;
    private Bitmap zhengjianBitmap;
    private byte[] images;
  //  private byte[] fringerprint;
  //  private String fringerprintData;
  //  private final int REQUEST_TAKE_PHOTO=33;
    private  String zhuji=null;
    private static boolean isTrue3=true;
    private static boolean isTrue4=true;
    private FaceDet mFaceDet=null;
    private AutoFitTextureView videoView;
    private ImageView imageView;
    private MediaPlayer mediaPlayer=null;
    private IVLCVout vlcVout=null;
    private IVLCVout.Callback callback;
    private Media media;
    private TextView tishi;
    private LinearLayout jiemian;
    private static int count=1;
    private static final int MESSAGE_QR_SUCCESS = 1;
    private LibVLC libvlc;
    private boolean isTiJiao=false;
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;
    private  String ip=null;
    private Button paizhao,baocun2;





    Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_QR_SUCCESS:

                    Bitmap bitmap= (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);

                    break;
                case 22:

                    tishi.setVisibility(View.VISIBLE);
                    tishi.setText("比对失败,开始第"+count+"次比对,请再次看下摄像头");

                    break;

            }

        }
    };


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
             if (msg.what == 300) {

                 Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"开启读卡失败",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                 tastyToast.setGravity(Gravity.CENTER,0,0);
                 tastyToast.show();

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian2);
        baoCunBeanDao= MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);

        mFaceDet= MyAppLaction.mFaceDet;
        libvlc=new LibVLC(InFoActivity3.this);

        if (baoCunBean==null){
            Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"请先设置主机地址和摄像头IP",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            tastyToast.setGravity(Gravity.CENTER,0,0);
            tastyToast.show();
        }else {
            zhuji=baoCunBean.getZhuji();
            ip=baoCunBean.getCameraIP();
        }


        isTrue3=true;
        isTrue4=true;



        String fn = "bbbb.jpg";
        FileUtil.isExists(FileUtil.PATH, fn);
        //mSavePhotoFile=new File( FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn);


        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                   IdCard.open(InFoActivity3.this);

                    startReadCard();

                } catch (Exception e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"无法连接读卡器",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }
                    });
                }
            }
        }).start();



        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("guanbi");
        intentFilter1.addAction("guanbi2");
        sensorInfoReceiver = new SensorInfoReceiver();
        registerReceiver(sensorInfoReceiver, intentFilter1);


        userInfoBena=new UserInfoBena();


        ImageView imageView= (ImageView) findViewById(R.id.dd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        initView();

        bofang(ip);


        jiaZaiDialog=new JiaZaiDialog(InFoActivity3.this);
        jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if (!InFoActivity3.this.isFinishing())
        jiaZaiDialog.show();

    }

    private void bofang(final String i) {
        videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

             ///   Log.d("InFoActivity3", "改222222"+width+"   "+height);

                vlcVout.attachViews();

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

              //  Log.d("InFoActivity3", "改222222变");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {


                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {


            }
        });

                callback=new IVLCVout.Callback() {


                    @Override
                    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

                    }

                    @Override
                    public void onSurfacesCreated(IVLCVout vlcVout) {


                                if (mediaPlayer != null) {
                                    final Uri uri=Uri.parse("rtsp://"+i+"/user=admin&password=&channel=1&stream=0.sdp");
                                    media = new Media(libvlc, uri);
                                    mediaPlayer.setMedia(media);
                                    mediaPlayer.play();
                                }

                    }

                    @Override
                    public void onSurfacesDestroyed(IVLCVout vlcVout) {
                        vlcVout.removeCallback(callback);
                    }

                    @Override
                    public void onHardwareAccelerationError(IVLCVout vlcVout) {

                    }

                };


                if (vlcVout!=null){
                    vlcVout.setVideoView(videoView);
                    vlcVout.addCallback(callback);
                }

    }

    private void startReadCard() {

        isTrue=true;
        isTrue2=true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (isTrue) {
                    if (isTrue2){
                        isTrue2=false;
                        try {

                             async= new GetIDInfoTask();
                             async.execute();

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


    }



    private void initView() {
        paizhao = (Button) findViewById(R.id.paizhao1);
        paizhao.setOnClickListener(this);
        baocun2 = (Button) findViewById(R.id.queding);
        baocun2.setOnClickListener(this);
        videoView= (AutoFitTextureView) findViewById(R.id.fff);
        videoView.setAspectRatio(4,3);
        imageView= (ImageView) findViewById(R.id.image);
        jiemian= (LinearLayout) findViewById(R.id.jiemian);
        tishi= (TextView) findViewById(R.id.tishi);

        name= (TextView) findViewById(R.id.name);
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
                            if (isTiJiao){
                                link_save();
                            }else {
                                Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"照片入库质量未达到要求,请拍正面照,并注意光线!",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                tastyToast.setGravity(Gravity.CENTER,0,0);
                                tastyToast.show();
                            }


                        }else {

                            final XuanZeDialog dialog=new XuanZeDialog(InFoActivity3.this,"比对未通过,你确定要进行下一步吗");
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
                                }
                            });
                            dialog.show();

//                            Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"比对未通过,请重新验证!",TastyToast.LENGTH_LONG,TastyToast.ERROR);
//                            tastyToast.setGravity(Gravity.CENTER,0,0);
//                            tastyToast.show();

                        }


                    }catch (Exception e){
                        Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"数据异常,请返回后重试",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();
                        Log.d("InFoActivity", e.getMessage());
                    }

                }else {
                    Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"请先读取身份证信息",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }

            }
        });

        //progressBarWithNumber= (HorizontalProgressBarWithNumber) findViewById(R.id.id_progressbar01);

        mediaPlayer=new MediaPlayer(libvlc);
        vlcVout = mediaPlayer.getVLCVout();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.paizhao1:
                paizhao.setEnabled(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (videoView!=null)
                            bitmap2=videoView.getBitmap();
                        if (bitmap2!=null){
                            List<VisionDetRet> results = mFaceDet.detect(bitmap2);

                            if (results != null) {
                                int s = results.size();
                                VisionDetRet face;
                                if (s>0){
                                    face = results.get(0);
                                    int xx = 0;
                                    int yy = 0;
                                    int xx2 = 0;
                                    int yy2 = 0;
                                    int ww = bitmap2.getWidth();
                                    int hh = bitmap2.getHeight();
                                    if (face.getRight() - 240 >= 0) {
                                        xx = face.getRight() - 240;
                                    } else {
                                        xx = 0;
                                    }
                                    if (face.getTop() - 210 >= 0) {
                                        yy = face.getTop() - 210;
                                    } else {
                                        yy = 0;
                                    }
                                    if (xx + 420 <= ww) {
                                        xx2 = 420;
                                    } else {
                                        xx2 = ww - xx ;
                                    }
                                    if (yy + 420 <= hh) {
                                        yy2 = 420;
                                    } else {
                                        yy2 = hh - yy ;
                                    }
                                    Bitmap bitmap = Bitmap.createBitmap(bitmap2, xx, yy, xx2, yy2);

                                    String fn="bbbb.jpg";
                                    FileUtil.isExists(FileUtil.PATH,fn);
                                    saveBitmap2File2(bitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);


                                }else {
                                   // isT2=true;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tishi.setVisibility(View.VISIBLE);
                                            tishi.setText("没有检查到人脸,请重拍");
                                        }
                                    });
                                    // Log.d("RenGongFuWuActivity", "继续抓拍1");
                                }
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tishi.setVisibility(View.VISIBLE);
                                        tishi.setText("没有检查到人脸,请重拍");
                                    }
                                });

                            }
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tishi.setVisibility(View.VISIBLE);
                                    tishi.setText("没有检查到人脸,请重拍");
                                }
                            });
                            // Log.d("RenGongFuWuActivity", "继续抓拍3");
                        }

                        try {
                            Thread.sleep(1200);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    paizhao.setEnabled(true);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();


                break;

            case R.id.queding:

                link_P1(shengfenzhengPath,filePath2);

                jiemian.setVisibility(View.VISIBLE);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(jiemian, "scaleY", 0f, 1f);
                animator2.setDuration(600);//时间1s
                animator2.start();
                //起始为1，结束时为0
                ObjectAnimator animator = ObjectAnimator.ofFloat(jiemian, "scaleX", 0f, 1f);
                animator.setDuration(600);//时间1s
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
                break;

        }

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

               }else {
                   shibiejieguo.setText("比对不通过");
                   biduijieguo="比对不通过";

               }
               xiangsi=intent.getStringExtra("xiangsidu");
                xiangsifdu.setText(intent.getStringExtra("xiangsidu")+"");

                Bitmap bitmap= BitmapFactory.decodeFile(FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+"bbbb.jpg");
                xianchengzhao.setImageBitmap(bitmap);
                //link_zhiliang();
            }
            if (action.equals("guanbi2")){
                finish();
            }
        }
    }


    private class GetIDInfoTask extends
            AsyncTask<Void, Integer, TelpoException> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始
            info = null;
            zhengjianBitmap = null;

        }

        @Override
        protected TelpoException doInBackground(Void... arg0) {
            TelpoException result = null;
            try {
                publishProgress(1);
//				info = IdCard.checkIdCard(4000);
                info = IdCard.checkIdCard(1600);//luyq modify
                if (info != null) {
                    images = IdCard.getIdCardImage();
                    zhengjianBitmap = IdCard.decodeIdCardImage(images);
                    // luyq add 增加指纹信息
                    //fringerprint = IdCard.getFringerPrint();
                    //fringerprintData = Utils.getFingerInfo(fringerprint, InFoActivity3.this);
                }
            } catch (TelpoException e) {
                Log.d("GetIDInfoTask", "异常" + e.getMessage());
                result = e;
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(TelpoException result) {
            super.onPostExecute(result);

            if (result == null && !info.getName().equals("timeout")) {
                isTrue2 = false;
                isTrue = false;

                if (async != null) {
                    async.cancel(true);
                    async = null;
                }

                //设置信息
                name.setText(info.getName().trim());
                xingbie.setText(info.getSex());
                shenfengzheng.setText(info.getNo());
                mingzu.setText(info.getNation());
                String time = info.getBorn().substring(0, 4) + "-" + info.getBorn().substring(4, 6) + "-" + info.getBorn().substring(6, 8);
                chusheng.setText(time);
                fazhengjiguan.setText(info.getApartment());

                String time2 = info.getPeriod().substring(0, 4) + "-" + info.getPeriod().substring(4, 6) + "-" + info.getPeriod().substring(6, 8);
                String time3 = info.getPeriod().substring(9, 13) + "-" + info.getPeriod().substring(13, 15) + "-" + info.getPeriod().substring(15, 17);
                youxiaoqixian.setText(time2 + " " + time3);
                zhuzhi.setText(info.getAddress());

                if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                    jiaZaiDialog.setText("开启摄像头中,请稍后...");
                }

                zhengjianzhao.setImageBitmap(zhengjianBitmap);
                String fn="aaaa.jpg";
                FileUtil.isExists(FileUtil.PATH,fn);

                saveBitmap2File(zhengjianBitmap.copy(Bitmap.Config.ARGB_8888,false), FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);

                userInfoBena = new UserInfoBena(info.getName(), info.getSex().equals("男") ? 1 + "" : 2 + "", info.getNation(), time, info.getAddress(), info.getNo(), info.getApartment(), time2, time3, null, null, null);
                IdCard.close();

            } else {
                isTrue2 = true;
//                Toast tastyToast = TastyToast.makeText(InFoActivity3.this, "读取身份证信息失败", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                tastyToast.setGravity(Gravity.CENTER, 0, 0);
//                tastyToast.show();

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

            //开启摄像头
          //  kaishiPaiZhao();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startThread();
                }
            });

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
        if (mediaPlayer==null){
            finish();
            Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"开启摄像头失败,请重新读卡",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            tastyToast.setGravity(Gravity.CENTER,0,0);
            tastyToast.show();
            return;
        }

        if (mediaPlayer.isPlaying()){

            videoView.invalidate();

            startThread();

        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(6000);
                        if (mediaPlayer.isPlaying()) {

                            startThread();

                        } else {

                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"当前网络不稳定,需要重新读卡开启摄像头,请稍候",TastyToast.LENGTH_LONG,TastyToast.INFO);
                                   tastyToast.setGravity(Gravity.CENTER,0,0);
                                   tastyToast.show();
                               }
                           });

                            Intent intent=new Intent();
                            intent.putExtra("date", "11");
                            setResult(Activity.RESULT_OK, intent);

                            finish();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    private void startThread(){

        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
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



//        Thread thread=  new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (isTrue3){
//
//                    if (isTrue4){
//                        isTrue4=false;
//
//                        try {
//                           bitmapBig=videoView.getBitmap();
//
//
//                            if (bitmapBig!=null){
//
//                                List<VisionDetRet> results = mFaceDet.detect(bitmapBig);
//
//                                if (results != null) {
//
//                                    int s = results.size();
//
//                                    VisionDetRet face;
//                                    if (s > 0) {
//
//                                        if (s > count - 1) {
//
//                                            face = results.get(count - 1);
//
//                                        } else {
//
//                                            face = results.get(0);
//
//                                        }
//
//                                        int xx = 0;
//                                        int yy = 0;
//                                        int xx2 = 0;
//                                        int yy2 = 0;
//                                        int ww = bitmapBig.getWidth();
//                                        int hh = bitmapBig.getHeight();
//                                        if (face.getRight() - 240 >= 0) {
//                                            xx = face.getRight() - 240;
//                                        } else {
//                                            xx = 0;
//                                        }
//                                        if (face.getTop() - 210 >= 0) {
//                                            yy = face.getTop() - 210;
//                                        } else {
//                                            yy = 0;
//                                        }
//                                        if (xx + 420 <= ww) {
//                                            xx2 = 420;
//                                        } else {
//                                            xx2 = ww - xx ;
//                                        }
//                                        if (yy + 430 <= hh) {
//                                            yy2 = 430;
//                                        } else {
//                                            yy2 = hh - yy ;
//                                        }
//
//                                        //     Bitmap bmpf = bitmapBig.copy(Bitmap.Config.RGB_565, true);
////
////                                               //返回识别的人脸数
////                                               //	int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 1).findFaces(bmpf, facess);
////                                               //	FaceDetector faceCount2 = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 2);
////
////                                               myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
////                                               myFaceDetect = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), numberOfFace);
////                                               numberOfFaceDetected = myFaceDetect.findFaces(bmpf, myFace);    //FaceDetector 构造实例并解析人脸
////
////                                               if (numberOfFaceDetected > 0) {
////
////                                                   FaceDetector.Face face;
////                                                   if (numberOfFaceDetected>count-1){
////                                                       face = myFace[count-1];
////
////                                                   }else {
////                                                       face = myFace[0];
////
////                                                   }
////
////                                                   PointF pointF = new PointF();
////                                                   face.getMidPoint(pointF);
////
////
////                                                 //  myEyesDistance = (int)face.eyesDistance();
////
////                                                   int xx=0;
////                                                   int yy=0;
////                                                   int xx2=0;
////                                                   int yy2=0;
////
////                                                   if ((int)pointF.x-200>=0){
////                                                       xx=(int)pointF.x-200;
////                                                   }else {
////                                                       xx=0;
////                                                   }
////                                                   if ((int)pointF.y-320>=0){
////                                                       yy=(int)pointF.y-320;
////                                                   }else {
////                                                       yy=0;
////                                                   }
////                                                   if (xx+350 >=bitmapBig.getWidth()){
////                                                       xx2=bitmapBig.getWidth()-xx;
////
////                                                   }else {
////                                                       xx2=350;
////                                                   }
////                                                   if (yy+500>=bitmapBig.getHeight()){
////                                                       yy2=bitmapBig.getHeight()-yy;
////
////                                                   }else {
////                                                       yy2=500;
////                                                   }
//
//                                        Bitmap bitmap = Bitmap.createBitmap(bitmapBig, xx, yy, xx2, yy2);
//
//                                        // Bitmap bitmap = Bitmap.createBitmap(bitmapBig,0,0,bitmapBig.getWidth(),bitmapBig.getHeight());
//
//                                        Message message3 = Message.obtain();
//                                        message3.what = MESSAGE_QR_SUCCESS;
//                                        message3.obj = bitmap;
//                                        mHandler2.sendMessage(message3);
//
//
//                                        String fn = "bbbb.jpg";
//                                        FileUtil.isExists(FileUtil.PATH, fn);
//                                        saveBitmap2File2(bitmap.copy(Bitmap.Config.ARGB_8888,false), FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn, 100);
//                                        bitmapBig.recycle();
//                                        bitmapBig=null;
//
//                                    } else {
//                                        isTrue4 = true;
//                                    }
//
//                                    //  bmpf.recycle();
//                                    //  bmpf = null;
//                                }else {
//                                    isTrue4 = true;
//                                }
//
//
//                            }else {
//                                isTrue4 = true;
//                            }
//
//
//                        }catch (Exception e){
//                            Log.d("InFoActivity3", e.getMessage()+"");
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast tastyToast = TastyToast.makeText(InFoActivity3.this, "截图时发生未知错误,请关闭后重试", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                                     tastyToast.setGravity(Gravity.CENTER, 0, 0);
//                                      tastyToast.show();
//                                }
//                            });
//                        }
//
//
//
//                    }
//
//                }
//
//
//            }
//        });
//
//        thread.start();

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
                    tishi.setText("检测人脸质量中...");
                    link_P2(filePath2);
                   // link_P1(shengfenzhengPath,filePath2);
                }
            });




        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (!bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        isTrue4=false;
        isTrue3=false;
        count=1;

        isTrue2=false;
        isTrue=false;


    }

    @Override
    protected void onDestroy() {

        if (vlcVout!=null){
            vlcVout.removeCallback(callback);
            callback=null;
            vlcVout=null;
        }
        if(media!=null){
            media.release();
            media=null;
        }
        if (mediaPlayer!=null){
           // mediaPlayer.release();
            mediaPlayer=null;
        }
        if (libvlc!=null){
            libvlc.release();
            libvlc=null;
        }

        if (async!=null){
            async.cancel(true);
            async=null;
        }

        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
            jiaZaiDialog.dismiss();
            jiaZaiDialog=null;
        }
        unregisterReceiver(sensorInfoReceiver);
        super.onDestroy();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_TAKE_PHOTO:  //拍照
//                    //注意，如果拍照的时候设置了MediaStore.EXTRA_OUTPUT，data.getData=null
//                    xianchengzhao.setImageURI(Uri.fromFile(mSavePhotoFile));
//
//                    link_P1(shengfenzhengPath,filePath2);
//
//                    break;
//
//            }
//        }
//    }

//    /**
//     * 启动拍照
//     * @param
//     */
//    private void startCamera() {
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Continue only if the File was successfully created
//            if (mSavePhotoFile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(mSavePhotoFile));//设置文件保存的URI
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }

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
        // Log.d("InFoActivity3", userInfoBena.getGender());
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji + "/saveCompareResult.do");

        if (tiJIaoDialog==null){
            tiJIaoDialog=new TiJIaoDialog(InFoActivity3.this);
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
                    tiJIaoDialog=null;
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (tiJIaoDialog!=null){
                    tiJIaoDialog.dismiss();
                    tiJIaoDialog=null;
                }
                Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss=body.string().trim();
                    Log.d("InFoActivity2", ss);
                    if (Long.parseLong(ss)>0){

                        startActivity(new Intent(InFoActivity3.this,ShiYouActivity.class)
                                .putExtra("name",name.getText().toString())
                                .putExtra("biduijieguo",bidui)
                                .putExtra("id",ss+""));

                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"保存失败",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                tastyToast.setGravity(Gravity.CENTER,0,0);
                                tastyToast.show();

                            }
                        });

                    }

                }catch (Exception e){

                    if (tiJIaoDialog!=null){
                        tiJIaoDialog.dismiss();
                        tiJIaoDialog=null;
                    }
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


    }


//
//    private void startThread() {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (jiaZaiDialog != null && jiaZaiDialog.isShowing()) {
//                    jiaZaiDialog.dismiss();
//                }
//
//                ObjectAnimator animator2 = ObjectAnimator.ofFloat(jiemian, "scaleY", 1f, 0f);
//                animator2.setDuration(600);//时间1s
//                animator2.start();
//                //起始为1，结束时为0
//                ObjectAnimator animator = ObjectAnimator.ofFloat(jiemian, "scaleX", 1f, 0f);
//                animator.setDuration(600);//时间1s
//                animator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        jiemian.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//                animator.start();
//            }
//        });
//    }



//        Thread thread=  new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (isTrue3){
//
//                    if (isTrue4){
//                        isTrue4=false;
//                        try {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    bitmapBig=videoView.getBitmap();
//
//                                    if (bitmapBig!=null){
//
//                                       new Thread(new Runnable() {
//                                           @Override
//                                           public void run() {
//
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
//
//
//                                                   Bitmap bitmap = Bitmap.createBitmap(bitmapBig,xx,yy,xx2,yy2);
//
//                                                 //  Bitmap bitmap = Bitmap.createBitmap(bitmapBig,0,0,bitmapBig.getWidth(),bitmapBig.getHeight());
//
//                                                   Message message=Message.obtain();
//                                                   message.what=MESSAGE_QR_SUCCESS;
//                                                   message.obj=bitmap;
//                                                   mHandler2.sendMessage(message);
//
//
//                                                   String fn="bbbb.jpg";
//                                                   FileUtil.isExists(FileUtil.PATH,fn);
//                                                   saveBitmap2File2(bitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);
//
//
//                                               }else {
//                                                   isTrue4=true;
//                                               }
//
//                                               bmpf.recycle();
//                                               bmpf = null;
//                                           }
//                                       }).start();
//
//
//                                    }
//                                }
//                            });
//
//                        }catch (IllegalStateException e){
//                            Log.d("InFoActivity3", e.getMessage()+"");
//                        }
//
//
//
//                    }
//
//                }
//
//
//            }
//        });
//
//        thread.start();
//
//    }

//    public  void saveBitmap2File2(Bitmap bm, final String path, int quality) {
//        try {
//            filePath2=path;
//            if (null == bm) {
//                Log.d("InFoActivity", "回收|空");
//                return ;
//            }
//
//            File file = new File(path);
//            if (file.exists()) {
//                file.delete();
//            }
//            BufferedOutputStream bos = new BufferedOutputStream(
//                    new FileOutputStream(file));
//            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
//            bos.flush();
//            bos.close();
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//
//            link_P1(shengfenzhengPath,filePath2);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//
////			if (!bm.isRecycled()) {
////				bm.recycle();
////			}
//            bm = null;
//        }
//    }


    private void link_P1(String filename1, final String fileName2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jiaZaiDialog=new JiaZaiDialog(InFoActivity3.this);
                jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                jiaZaiDialog.setText("上传图片中...");
                if (!InFoActivity3.this.isFinishing()){
                    jiaZaiDialog.show();
                }
            }
        });



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
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
                            jiaZaiDialog=null;
                        }
                        Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();

                    }
                });
               // Log.d("AllConnects", "请求识别失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              //  Log.d("AllConnects", "请求识别成功"+call.request().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
                            jiaZaiDialog=null;
                        }
                    }
                });
                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss=body.string();

                  //  Log.d("AllConnects", "aa   "+ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    userInfoBena.setCardPhoto(zhaoPianBean.getExDesc());

                    link_tianqi3();

                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                jiaZaiDialog.dismiss();
                                jiaZaiDialog=null;
                            }
                            Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });

    }

    private void link_P2(final String fileName2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(InFoActivity3.this)
                        .load(fileName2)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        //  .transform(new GlideCircleTransform(RenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                        .into(imageView);

                jiaZaiDialog=new JiaZaiDialog(InFoActivity3.this);
                jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                jiaZaiDialog.setText("检查人脸质量中...");
                if (!InFoActivity3.this.isFinishing())
                jiaZaiDialog.show();
            }
        });

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
        File file2 = new File(fileName2);

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
              //  Log.d("AllConnects", "请求识别失败"+e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
                            jiaZaiDialog=null;
                        }
                        Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              //  Log.d("AllConnects", "请求识别成功"+call.request().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                            jiaZaiDialog.dismiss();
                            jiaZaiDialog=null;
                        }
                    }
                });

                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss=body.string();
                   //  Log.d("AllConnects", "aa   "+ss);
                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    userInfoBena.setScanPhoto(zhaoPianBean.getExDesc());
                    link_zhiliang();

                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                jiaZaiDialog.dismiss();
                                jiaZaiDialog=null;
                            }
                            Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
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
                   // count++;

                    ResponseBody body = response.body();
                    // Log.d("AllConnects", "识别结果返回"+response.body().string());
                    String ss=body.string();
                  //  Log.d("InFoActivity", ss);
                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    final ShiBieBean zhaoPianBean=gson.fromJson(jsonObject,ShiBieBean.class);

                    if (zhaoPianBean.getScore()>=65.0) {

                        //比对成功
                        sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",true)
                                .putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
                                .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                      //  count=1;

                        //qiehuan();

                    }else {


//                        if (count<=3){
//
//                            Message message=Message.obtain();
//                            message.what=22;
//                            mHandler2.sendMessage(message);
//
//                            isTrue4=true;
//
//
//                        }else {

                            sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false)
                                    .putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
                                    .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                            count=1;

                          //  qiehuan();
                      //  }

                    }


                }catch (Exception e){

//                    if (count<=3){
//
//                        Message message=Message.obtain();
//                        message.what=22;
//                        mHandler2.sendMessage(message);
//
//                        isTrue4=true;
//
//
//                    }else {

                        sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false).putExtra("xiangsidu","43.21")
                                .putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
                        count=1;

                     //   qiehuan();

                 //   }

                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


    }

    private void qiehuan(){
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//                if (mediaPlayer!=null){
//                    mediaPlayer=null;
//                    media=null;
//                }
//                if (vlcVout!=null){
//                    vlcVout.detachViews();
//                    vlcVout.removeCallback(callback);
//                    callback=null;
//                    vlcVout=null;
//                }
//                if (libvlc!=null){
//                    libvlc.release();
//                }
//                if (videoView!=null){
//                    videoView.setSurfaceTextureListener(null);
//                }
//
//
//            }
//        }).start();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                videoView.setVisibility(View.GONE);
                jiemian.setVisibility(View.VISIBLE);
              //  isTrue4=false;
               // isTrue3=false;

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

    private void link_zhiliang() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tiJIaoDialog==null && !InFoActivity3.this.isFinishing()){
                    tiJIaoDialog=new TiJIaoDialog(InFoActivity3.this);
                    if (!InFoActivity3.this.isFinishing())
                    tiJIaoDialog.show();
                }
            }
        });

        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("scanPhoto",userInfoBena.getScanPhoto())
                .add("accountId","1")
                .build();


        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji + "/faceQuality.do");

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
                        if (tiJIaoDialog!=null){
                            tiJIaoDialog.dismiss();
                            tiJIaoDialog=null;
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tiJIaoDialog!=null){
                            tiJIaoDialog.dismiss();
                            tiJIaoDialog=null;
                        }
                    }
                });
                Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss=body.string().trim();
                    Log.d("DengJiActivity", ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    ShouFangBean zhaoPianBean=gson.fromJson(jsonObject,ShouFangBean.class);

                    if (zhaoPianBean.getDtoResult()!=0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!InFoActivity3.this.isFinishing()){

                                    tishi.setVisibility(View.VISIBLE);
                                    tishi.setText("照片质量不符合入库要求,请重新拍照!");

                                }

                            }
                        });

                    }else {
                       // isTrue3=false;
                        isTiJiao=true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                baocun2.setVisibility(View.VISIBLE);
                                tishi.setVisibility(View.GONE);

                            }
                        });

                    }

                }catch (Exception e){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tiJIaoDialog!=null){
                                tiJIaoDialog.dismiss();
                                tiJIaoDialog=null;
                            }
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast tastyToast= TastyToast.makeText(InFoActivity3.this,"提交失败,请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();

                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });
    }

}
