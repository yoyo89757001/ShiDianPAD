package com.example.xiaojun.shidianpad.ui;



import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;

import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.com.aratek.idcard.IDCard;
import cn.com.aratek.idcard.IDCardReader;
import cn.com.aratek.util.Result;
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


public class ShuangPingInFoActivity extends Activity implements View.OnClickListener,SurfaceHolder.Callback {
    private EditText shenfengzheng,xingbie,mingzu,chusheng,dianhua,fazhengjiguan,
            youxiaoqixian,zhuzhi,shibiejieguo,xiangsifdu;
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
    public static final int TIMEOUT = 1000 * 60;
    private static boolean isTrue=true;
    private static boolean isTrue2=true;
    private boolean bidui=false;

    private UserInfoBena userInfoBena=null;
    private SensorInfoReceiver sensorInfoReceiver;
    private SurfaceView surfaceView;
    private String filePath2=null;
    private File file1=null;
 //   private File file2=null;
    long c=0;
    private Thread thread;
    private String shengfenzhengPath=null;
  //  private IdentityInfo info;
   // private Bitmap zhengjianBitmap;
   //// private byte[] images;
    private  String zhuji=null;
  //  private static boolean isTrue3=true;
    private static boolean isTrue4=false;
    private FaceDet mFaceDet=null;
    private ImageView imageView;
    private TextView tishi;
    private LinearLayout jiemian;
    private static int count=1;
   // private static final int MESSAGE_QR_SUCCESS = 1;
    private boolean isTiJiao=false;
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;
    private  String ip=null;
    private Button paizhao,baocun2;
    private IDCardReader mReader;
    private Camera mCamera;
    private SurfaceHolder sh;
    private android.view.ViewGroup.LayoutParams lp;
    private Bitmap bmp2=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian_shuangping);
        baoCunBeanDao= MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);
        if (baoCunBean!=null){
            zhuji=baoCunBean.getZhuji();
        }else {
            Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"请先设置主机地址",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            tastyToast.setGravity(Gravity.CENTER,0,0);
            tastyToast.show();
        }

        mFaceDet= MyAppLaction.mFaceDet;



        isTrue4=false;


        String fn = "bbbb.jpg";
        FileUtil.isExists(FileUtil.PATH, fn);
        //mSavePhotoFile=new File( FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn);

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("guanbi");
        intentFilter1.addAction("guanbi2");
        sensorInfoReceiver = new SensorInfoReceiver();
        registerReceiver(sensorInfoReceiver, intentFilter1);
        mReader = IDCardReader.getInstance();

        userInfoBena=new UserInfoBena();


        ImageView imageView= (ImageView) findViewById(R.id.dd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mReader.powerOn();
        mReader.open();

        initView();
        lp = surfaceView.getLayoutParams();
        sh = surfaceView.getHolder();
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sh.addCallback(this);


        jiaZaiDialog=new JiaZaiDialog(ShuangPingInFoActivity.this);
        jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if (!ShuangPingInFoActivity.this.isFinishing())
        jiaZaiDialog.show();

        try {

            startReadCard();

        } catch (Exception e) {
            Log.d("InFoActivity2", e.getMessage()+"读卡");

            Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"无法连接读卡器",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            tastyToast.setGravity(Gravity.CENTER,0,0);
            tastyToast.show();

        }

        OpenCameraAndSetSurfaceviewSize(0);

    }

    private Void OpenCameraAndSetSurfaceviewSize(int cameraId) {
        mCamera = Camera.open(cameraId);
        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size pre_size = parameters.getPreviewSize();
        //  Camera.Size pic_size = parameters.getPictureSize();
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);

        lp.height =pre_size.height*2;
        lp.width = pre_size.width*2;

        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {

                Camera.Size size = camera.getParameters().getPreviewSize();
                try{
                    YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new android.graphics.Rect(0, 0, size.width, size.height), 100, stream);

                    bmp2 = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());

                    if (isTrue4) {
                        isTrue4 = false;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<VisionDetRet> results = mFaceDet.detect(bmp2);

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
                                        int ww = bmp2.getWidth();
                                        int hh = bmp2.getHeight();
                                        if (face.getRight() - 340 >= 0) {
                                            xx = face.getRight() - 340;
                                        } else {
                                            xx = 0;
                                        }
                                        if (face.getTop() - 3400 >= 0) {
                                            yy = face.getTop() - 340;
                                        } else {
                                            yy = 0;
                                        }
                                        if (xx + 760 <= ww) {
                                            xx2 = 760;
                                        } else {
                                            xx2 = ww - xx;
                                        }
                                        if (yy + 660 <= hh) {
                                            yy2 = 660;
                                        } else {
                                            yy2 = hh - yy;
                                        }

                                        final Bitmap bitmap = Bitmap.createBitmap(bmp2, xx, yy, xx2, yy2);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView.setImageBitmap(bitmap);
                                            }
                                        });

                                        String fn = "bbbb.jpg";
                                        FileUtil.isExists(FileUtil.PATH, fn);
                                        saveBitmap2File2(bitmap.copy(Bitmap.Config.ARGB_8888, false), FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn, 100);

                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tishi.setVisibility(View.VISIBLE);
                                                tishi.setText("没有检查到人脸,请重拍");
                                                if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                                    jiaZaiDialog.dismiss();
                                                    jiaZaiDialog=null;
                                                }
                                            }
                                        });
                                    }

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                                                jiaZaiDialog.dismiss();
                                                jiaZaiDialog=null;
                                            }
                                            tishi.setVisibility(View.VISIBLE);
                                            tishi.setText("没有检查到人脸,请重拍");
                                        }
                                    });
                                }

                            }

                        }).start();
                    }
                    stream.close();

                }catch(Exception ex){
                    Log.e("Sys","Error:"+ex.getMessage());
                }
            }
        });

        return null;
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
                            Result res = mReader.read();
                            if (res.error == IDCardReader.RESULT_OK) {
                              //  Log.d("InFoActivity2", "成功");
                                try {
                                    showPeopleInfo((IDCard) res.data);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            } else if (res.error == IDCardReader.NO_CARD) {

                                isTrue2=true;
                            } else {
                                Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"读卡出现错误",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                tastyToast.setGravity(Gravity.CENTER,0,0);
                                tastyToast.show();
                            }

                            Thread.sleep(800);

                        } catch (Exception e) {
                            isTrue=false;
                            Log.d("SerialReadActivity", e.getMessage());

                        }

                    }


                }
            }

        });
        thread.start();
    }


    private void kill_camera() {
        //  Log.d("InFoActivity3", "销毁");
        try {
            isTrue4=false;
            surfaceView.setVisibility(View.GONE);
            if (mCamera!=null){
                mCamera.stopPreview();
                mCamera.release();
            }

        }catch (Exception e){
            Log.d("InFoActivity2", e.getMessage()+"销毁");
        }
    }

    private void showPeopleInfo(final IDCard card) {
        isTrue=false;
        if (!ShuangPingInFoActivity.this.isFinishing())
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    name.setText(card.getName());
                    xingbie.setText(card.getSex().toString());
                    mingzu.setText(card.getNationality().toString());
                    chusheng.setText(df.format(card.getBirthday()));
                    zhuzhi.setText(card.getAddress());
                    shenfengzheng.setText(card.getNumber());
                    fazhengjiguan.setText(card.getAuthority());
                    youxiaoqixian.setText(df.format(card.getValidFrom()) + " 到 " + (card.getValidTo() == null ? "长期" : df.format(card.getValidTo())));
                    //  mFinger.setText(card.isSupportFingerprint() ? R.string.exist : R.string.not_exist);//指纹
                    if (card.getPhoto() != null) {
                        zhengjianzhao.setImageBitmap(card.getPhoto());

                        String fn="aaaa.jpg";
                        FileUtil.isExists(FileUtil.PATH,fn);
                        userInfoBena = new UserInfoBena(card.getName(), card.getSex().toString().equals("男") ? 1 + "" : 2 + "", card.getNationality().toString(),
                                df.format(card.getBirthday()), card.getAddress(), card.getNumber(), card.getAuthority(), df.format(card.getValidFrom()), df.format(card.getValidTo()), null, null, null);

                        saveBitmap2File(card.getPhoto().copy(Bitmap.Config.ARGB_8888,false), FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);

                    }
                    if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
                        jiaZaiDialog.dismiss();
                        jiaZaiDialog=null;
                    }
                    jiemian.setVisibility(View.GONE);

                }
            });

        mReader.close();
    }


    private void initView() {
        paizhao = (Button) findViewById(R.id.paizhao1);
        paizhao.setOnClickListener(this);
        baocun2 = (Button) findViewById(R.id.queding);
        baocun2.setOnClickListener(this);
        surfaceView= (SurfaceView) findViewById(R.id.fff);
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
                                Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"照片入库质量未达到要求,请拍正面照,并注意光线!",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                tastyToast.setGravity(Gravity.CENTER,0,0);
                                tastyToast.show();
                            }

                        }else {

                            final XuanZeDialog dialog=new XuanZeDialog(ShuangPingInFoActivity.this,"比对未通过,你确定要进行下一步吗");
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

                        }


                    }catch (Exception e){
                        Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"数据异常,请返回后重试",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();
                        Log.d("InFoActivity", e.getMessage());
                    }

                }else {
                    Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"请先读取身份证信息",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }

            }
        });



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.paizhao1:

                jiaZaiDialog = new JiaZaiDialog(ShuangPingInFoActivity.this);
                jiaZaiDialog.setText("检查人脸质量中,请稍后...");
                if (!ShuangPingInFoActivity.this.isFinishing())
                    jiaZaiDialog.show();
                isTrue4=true;


                break;

            case R.id.queding:

                link_P1(shengfenzhengPath,filePath2);

                jiemian.setVisibility(View.VISIBLE);
//                ObjectAnimator animator2 = ObjectAnimator.ofFloat(jiemian, "scaleY", 0f, 1f);
//                animator2.setDuration(600);//时间1s
//                animator2.start();
//                //起始为1，结束时为0
//                ObjectAnimator animator = ObjectAnimator.ofFloat(jiemian, "scaleX", 0f, 1f);
//                animator.setDuration(600);//时间1s
//                animator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
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
                kill_camera();
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
            mCamera.startPreview();

            shengfenzhengPath=path;


        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (!bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }
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
    public void surfaceCreated(SurfaceHolder holder) {

        SetAndStartPreview(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.d("InFoActivity3", "surfaceView销毁");

        if (mCamera != null) {
            mCamera.setPreviewCallback(null) ;
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

    }

    private Void SetAndStartPreview(SurfaceHolder holder) {
        try {
            if (mCamera!=null){
                mCamera.setPreviewDisplay(holder);
                mCamera.setDisplayOrientation(0);
            }


        } catch (IOException e) {
            Log.d("InFoActivity2", e.getMessage()+"相机");

        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isTrue4=false;
        count=1;

        isTrue2=false;
        isTrue=false;


    }

    @Override
    protected void onDestroy() {


        if (jiaZaiDialog!=null && jiaZaiDialog.isShowing()){
            jiaZaiDialog.dismiss();
            jiaZaiDialog=null;
        }
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
               // .add("homeNumber",fanghao.getText().toString().trim())
                .add("phone",dianhua.getText().toString().trim())
               // .add("carNumber",chepaihao.getText().toString().trim())
                .add("score",xiangsi)
                .build();
        // Log.d("InFoActivity3", userInfoBena.getGender());
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji + "/saveCompareResult.do");

        if (tiJIaoDialog==null){
            tiJIaoDialog=new TiJIaoDialog(ShuangPingInFoActivity.this);
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

                        startActivity(new Intent(ShuangPingInFoActivity.this,ShiYouActivity.class)
                                .putExtra("name",name.getText().toString())
                                .putExtra("biduijieguo",bidui)
                                .putExtra("id",ss+""));

                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"保存失败",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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


    private void link_P1(String filename1, final String fileName2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jiaZaiDialog=new JiaZaiDialog(ShuangPingInFoActivity.this);
                jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                jiaZaiDialog.setText("上传图片中...");
                if (!ShuangPingInFoActivity.this.isFinishing()){
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
                        Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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
                            Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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

                Glide.with(ShuangPingInFoActivity.this)
                        .load(fileName2)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        //  .transform(new GlideCircleTransform(RenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                        .into(imageView);
               tishi.setVisibility(View.VISIBLE);
               tishi.setText("检查人脸质量中...");

//                jiaZaiDialog=new JiaZaiDialog(ShuangPingInFoActivity.this);
//
//                jiaZaiDialog.setText("检查人脸质量中...");
//                if (!ShuangPingInFoActivity.this.isFinishing())
//                jiaZaiDialog.show();
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
                        Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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
                            Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"上传图片出错，请返回后重试！",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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
                    Log.d("InFoActivity", ss);
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

                surfaceView.setVisibility(View.GONE);
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
                if (tiJIaoDialog==null && !ShuangPingInFoActivity.this.isFinishing()){
                    tiJIaoDialog=new TiJIaoDialog(ShuangPingInFoActivity.this);
                    if (!ShuangPingInFoActivity.this.isFinishing())
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

                                if (!ShuangPingInFoActivity.this.isFinishing()){

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

                            Toast tastyToast= TastyToast.makeText(ShuangPingInFoActivity.this,"提交失败,请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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
