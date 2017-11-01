package com.example.xiaojun.shidianpad.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xiaojun.shidianpad.MyAppLaction;
import com.example.xiaojun.shidianpad.R;
import com.example.xiaojun.shidianpad.beans.ChaXunBean;
import com.example.xiaojun.shidianpad.beans.MyAdapter;
import com.example.xiaojun.shidianpad.beans.Photos;
import com.example.xiaojun.shidianpad.beans.ShouFangBean;
import com.example.xiaojun.shidianpad.dialog.QueRenDialog;
import com.example.xiaojun.shidianpad.dialog.TiJIaoDialog;
import com.example.xiaojun.shidianpad.dialog.YuYueDialog;
import com.example.xiaojun.shidianpad.utils.DateUtils;
import com.example.xiaojun.shidianpad.utils.FileUtil;
import com.example.xiaojun.shidianpad.utils.GsonUtil;
import com.example.xiaojun.shidianpad.view.AutoFitTextureView;
import com.example.xiaojun.shidianpad.view.GlideCircleTransform;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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
import java.util.ArrayList;
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

public class RenGongFuWuActivity extends Activity {
    private EditText name,shouji,beifangrenshouji,ren,renshu;
    private ImageView touxiang,jiahao;
    private TextView riqi;
    private Button baocun;
    private  String zhuji=null;
    private AutoFitTextureView videoView;
    private MediaPlayer mediaPlayer=null;
    private IVLCVout vlcVout=null;
    private IVLCVout.Callback callback;
    private LibVLC libvlc;
    private Media media;
    private FaceDet mFaceDet;
    private RelativeLayout ggg;
    private  String ip=null;
   // private JiaZaiDialog jiaZaiDialog=null;
    private Bitmap bitmap2=null;
    private String shengfenzhengPath=null;
    private LinearLayout riqill;
    public static final int TIMEOUT = 1000 * 60;
    private TiJIaoDialog tiJIaoDialog=null;
    private boolean isA=false;
    private String touxiangPath=null;
    private boolean isTiJiao=false;
    private List<ChaXunBean.ObjectsBean> objectsBeanList=new ArrayList<>();
    private MyAdapter myAdapter=null;
    private ListView listView;
    private RelativeLayout rl;
    public static boolean isT1=true,isT2=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_gong_fu_wu);


        listView= (ListView) findViewById(R.id.lsvMore);
        rl= (RelativeLayout) findViewById(R.id.tttttt);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.GONE);
                if (objectsBeanList.size()!=0){
                    objectsBeanList.clear();
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        Type resultType2 = new TypeToken<String>() {
        }.getType();
        Reservoir.getAsync("zhuji", resultType2, new ReservoirGetCallback<String>() {
            @Override
            public void onSuccess(final String i) {
                zhuji = i;
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast tastyToast = TastyToast.makeText(RenGongFuWuActivity.this, "请先设置后台地址", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER, 0, 0);
                        tastyToast.show();
                    }
                });
            }
        });
        mFaceDet = MyAppLaction.mFaceDet;


        videoView = (AutoFitTextureView) findViewById(R.id.fff);
        videoView.setAspectRatio(4, 3);
        baocun = (Button) findViewById(R.id.baocun);
        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baocun.setEnabled(false);
                if (name.getText().toString().trim().equals("") ) {
                    Toast tastyToast = TastyToast.makeText(RenGongFuWuActivity.this, "请填完整信息!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER, 0, 0);
                    tastyToast.show();
                    baocun.setEnabled(true);
                }else{
                    if (isA && isTiJiao){

                        link_save();

                    }else {
                        baocun.setEnabled(true);
                        Toast tastyToast = TastyToast.makeText(RenGongFuWuActivity.this, "还没有拍照,请拍照!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER, 0, 0);
                        tastyToast.show();
                    }

                }
            }
        });

        libvlc = new LibVLC(RenGongFuWuActivity.this);
        mediaPlayer = new MediaPlayer(libvlc);
        vlcVout = mediaPlayer.getVLCVout();
        name = (EditText) findViewById(R.id.name);
     //   paizhao= (Button) findViewById(R.id.paizhao);
        //shiping = (RelativeLayout) findViewById(R.id.shiping_rl);
        ggg = (RelativeLayout) findViewById(R.id.ggg);
        shouji = (EditText) findViewById(R.id.shoujihao);
        beifangrenshouji = (EditText) findViewById(R.id.gonghao);
        riqi = (TextView) findViewById(R.id.riqi);
        riqill= (LinearLayout) findViewById(R.id.riqill);
        riqill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenGongFuWuActivity.this, DatePickActivity2.class);
                startActivityForResult(intent,2);
            }
        });
        riqi.setText(DateUtils.timet2(System.currentTimeMillis() + ""));
        ren = (EditText) findViewById(R.id.yuyueren);
        ren.addTextChangedListener(textWatcher);
        renshu = (EditText) findViewById(R.id.yuyuerenshu);
        touxiang = (ImageView) findViewById(R.id.touxiang);
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (jiaZaiDialog == null) {
//                    jiaZaiDialog = new JiaZaiDialog(RenGongFuWuActivity.this);
//                    jiaZaiDialog.setText("开启摄像头中。。。");
//                    jiaZaiDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
//                    jiaZaiDialog.show();
//                }
                isT1=true;
                isT2=true;
                touxiang.setEnabled(false);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(ggg, "scaleY", 1f, 0f);
                animator2.setDuration(600);//时间1s
                animator2.start();
                //起始为1，结束时为0
                ObjectAnimator animator = ObjectAnimator.ofFloat(ggg, "scaleX", 1f, 0f);
                animator.setDuration(600);//时间1s
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ggg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (isT1){
                            if (isT2){
                                isT2=false;
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

                                            String fn="ccc.jpg";
                                            FileUtil.isExists(FileUtil.PATH,fn);
                                            saveBitmap2File(bitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);


                                        }else {
                                            isT2=true;
                                           // Log.d("RenGongFuWuActivity", "继续抓拍1");
                                        }
                                    }else {
                                        isT2=true;
                                       // Log.d("RenGongFuWuActivity", "继续抓拍2");
                                    }
                                }else {
                                    isT2=true;
                                   // Log.d("RenGongFuWuActivity", "继续抓拍3");
                                }


                            }

                        }



                    }
                }).start();

            }
        });

        jiahao= (ImageView) findViewById(R.id.jiahao);

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
                        if (mediaPlayer != null && ip !=null) {
                            final Uri uri=Uri.parse("rtsp://"+ip+"/user=admin&password=&channel=1&stream=0.sdp");
                            media = new Media(libvlc, uri);
                            mediaPlayer.setMedia(media);
                            videoView.setKeepScreenOn(true);
                            mediaPlayer.play();

                        }else {
                            Toast tastyToast= TastyToast.makeText(RenGongFuWuActivity.this,"请先设置摄像头IP",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
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
                        Toast tastyToast= TastyToast.makeText(RenGongFuWuActivity.this,"请先设置摄像头IP",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER,0,0);
                        tastyToast.show();

                    }
                });

            }

        });

        myAdapter=new MyAdapter(RenGongFuWuActivity.this,objectsBeanList);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                beifangrenshouji.setText(objectsBeanList.get(position).getPhone()+"");
                ren.setText(objectsBeanList.get(position).getName()+"");
                if (objectsBeanList.size()!=0){
                    objectsBeanList.clear();
                    myAdapter.notifyDataSetChanged();

                }
                listView.setVisibility(View.GONE);
            }
        });


    }


    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")){
                link_chaxun(s.toString());
                listView.setVisibility(View.VISIBLE);
            }else {
                listView.setVisibility(View.GONE);
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            // 选择预约时间的页面被关闭
            String date = data.getStringExtra("date");
            riqi.setText(date);
        }

    }

    @Override
    protected void onPause() {
        isT2=false;
        isT1=false;
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

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
        super.onDestroy();
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
            link_P1(shengfenzhengPath);

            isA=true;





        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (!bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }
    }


    private void link_save() {

        if (tiJIaoDialog==null){
            tiJIaoDialog=new TiJIaoDialog(RenGongFuWuActivity.this);
            if (!RenGongFuWuActivity.this.isFinishing())
                tiJIaoDialog.show();
        }

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
                .add("name",name.getText().toString().trim()+"")
                .add("phone",shouji.getText().toString().trim()+"")
                .add("visitDate2",riqi.getText().toString().trim()+"")
                .add("visitPerson",ren.getText().toString().trim()+"")
                .add("visitDepartment",beifangrenshouji.getText().toString().trim()+"")
                .add("visitNum",renshu.getText().toString().trim()+"")
                .add("accountId","1")
                .add("scanPhoto",touxiangPath)
                .add("visitIncident","1")
                .add("cardNumber","rt"+System.currentTimeMillis())
                .add("source","1")
                .build();

      //  Log.d("DengJiActivity", riqi.getText().toString().trim());
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji + "/saveCompareVisit.do");

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

                    if (zhaoPianBean.getDtoResult()==0){
                     //   Log.d("DengJiActivity", "dddd");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final YuYueDialog dialog=new YuYueDialog(RenGongFuWuActivity.this,"你已成功预约,感谢你的来访!");
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
                        });


                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast= TastyToast.makeText(RenGongFuWuActivity.this,"提交失败,请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                tastyToast.setGravity(Gravity.CENTER,0,0);
                                tastyToast.show();

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
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


        baocun.setEnabled(true);

    }

    private void link_P1(String filename1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                touxiang.setEnabled(true);
                Glide.with(RenGongFuWuActivity.this)
                        .load(shengfenzhengPath)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transform(new GlideCircleTransform(RenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                        .into(touxiang);
                jiahao.setVisibility(View.GONE);
                if (tiJIaoDialog==null){
                    tiJIaoDialog=new TiJIaoDialog(RenGongFuWuActivity.this);
                    tiJIaoDialog.setT1("检测人脸质量中,请稍后...");
                    if (!RenGongFuWuActivity.this.isFinishing())
                        tiJIaoDialog.show();
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
       File file1 = new File(filename1);

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
                .url(zhuji + "/AppFileUploadServlet?FilePathPath=compareFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("AllConnects", "请求识别失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求识别成功"+call.request().toString());
                //获得返回体
                try {

                    ResponseBody body = response.body();
                    String ss=body.string();

                  //  Log.d("AllConnects", "aa   "+ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    touxiangPath=zhaoPianBean.getExDesc();
                   link_zhiliang(touxiangPath);

                }catch (Exception e){

                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });

    }
    private void link_zhiliang(String touxiangPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tiJIaoDialog==null && !RenGongFuWuActivity.this.isFinishing()){
                    tiJIaoDialog=new TiJIaoDialog(RenGongFuWuActivity.this);
                    if (!RenGongFuWuActivity.this.isFinishing())
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
                .add("scanPhoto",touxiangPath)
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
                 //   Log.d("DengJiActivity", ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    final Gson gson=new Gson();
                    ShouFangBean zhaoPianBean=gson.fromJson(jsonObject,ShouFangBean.class);

                    if (zhaoPianBean.getDtoResult()!=0){
                        isT2=true;
                        Log.d("RenGongFuWuActivity", "继续抓拍4");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                if (!RenGongFuWuActivity.this.isFinishing()){
//                                    Toast tastyToast= TastyToast.makeText(RenGongFuWuActivity.this,"照片质量不符合入库要求,请拍正面照!",TastyToast.LENGTH_LONG,TastyToast.ERROR);
//                                    tastyToast.setGravity(Gravity.CENTER,0,0);
//                                    tastyToast.show();
//                                }
//
//
//                            }
//                        });

                    }else {
                        //link_save();
                        isTiJiao=true;
                        isT1=false;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ggg.setVisibility(View.VISIBLE);
                                ObjectAnimator animator2 = ObjectAnimator.ofFloat(ggg, "scaleY", 0f, 1f);
                                animator2.setDuration(600);//时间1s
                                animator2.start();
                                //起始为1，结束时为0
                                ObjectAnimator animator = ObjectAnimator.ofFloat(ggg, "scaleX", 0f, 1f);
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

                            Toast tastyToast= TastyToast.makeText(RenGongFuWuActivity.this,"提交失败,请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();

                        }
                    });
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });
    }



    private void link_chaxun(String names) {
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
                .add("visitPerson",names)
                .add("visitDepartment","")
                .build();

        Log.d("DengJiActivity", riqi.getText().toString().trim());
        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji + "/searchPhone.do");

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
                  //  Log.d("DengJiActivity", ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    final ChaXunBean zhaoPianBean=gson.fromJson(jsonObject,ChaXunBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (objectsBeanList.size()!=0){
                                objectsBeanList.clear();
                            }
                            objectsBeanList.addAll(zhaoPianBean.getObjects()==null?new ArrayList<ChaXunBean.ObjectsBean>():zhaoPianBean.getObjects());

                            myAdapter.notifyDataSetChanged();

                            ListAdapter listAdapter = listView.getAdapter();
                            if (listAdapter == null) {
                                return;
                            }
                            View listItem = listAdapter.getView(0, null, listView);
                            listItem.measure(0, 0);
                          //  Log.d("RenGongFuWuActivity", "listItem.getMeasuredHeight():" + );
                            if (objectsBeanList.size()<7){
                                RelativeLayout.LayoutParams p= (RelativeLayout.LayoutParams) listView.getLayoutParams();
                                int[] location = new int[2];
                                ren.getLocationOnScreen(location);

                                p.topMargin= (7-objectsBeanList.size())*listItem.getMeasuredHeight();
                                p.bottomMargin= location[1]-216;
                                listView.setLayoutParams(p);
                                listView.invalidate();
                              //  Log.d("RenGongFuWuActivity", "fdsfdfd");
                            }else {
                                RelativeLayout.LayoutParams p= (RelativeLayout.LayoutParams) listView.getLayoutParams();
                                int[] location = new int[2];
                                ren.getLocationOnScreen(location);
                                p.topMargin= 92;
                                p.bottomMargin= location[1]-216;
                                listView.setLayoutParams(p);
                                listView.invalidate();
                               // Log.d("RenGongFuWuActivity", "fdsfdfd222222");
                            }


                        }
                    });




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
                    Log.d("WebsocketPushMsg", e.getMessage());
                }
            }
        });


    }
}
