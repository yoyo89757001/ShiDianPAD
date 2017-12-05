package com.example.xiaojun.shidianpad.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xiaojun.shidianpad.MyAppLaction;
import com.example.xiaojun.shidianpad.R;
import com.example.xiaojun.shidianpad.beans.BaoCunBean;
import com.example.xiaojun.shidianpad.beans.BaoCunBeanDao;
import com.example.xiaojun.shidianpad.beans.ChaXunBean;
import com.example.xiaojun.shidianpad.beans.MyAdapter;
import com.example.xiaojun.shidianpad.beans.Photos;
import com.example.xiaojun.shidianpad.beans.ShouFangBean;
import com.example.xiaojun.shidianpad.dialog.TiJIaoDialog;
import com.example.xiaojun.shidianpad.dialog.YuYueDialog;
import com.example.xiaojun.shidianpad.utils.DateUtils;
import com.example.xiaojun.shidianpad.utils.FileUtil;
import com.example.xiaojun.shidianpad.utils.GsonUtil;
import com.example.xiaojun.shidianpad.view.GlideCircleTransform;
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

public class ShuangPingRenGongFuWuActivity extends Activity implements View.OnClickListener,SurfaceHolder.Callback {
    private EditText name,shouji,beifangrenshouji,ren,renshu,bumen;
    private ImageView touxiang,jiahao;
    private TextView riqi;
    private Button baocun;
    private  String zhuji=null;
    private SurfaceView surfaceView;
    private FaceDet mFaceDet;
    private RelativeLayout ggg;
   // private JiaZaiDialog jiaZaiDialog=null;
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
    public static boolean isT1=true,isT2=false;
    private Button paizhao,baocun2;
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;
    private ImageView  xianshi_im;
    private TextView tishi;
    private Camera mCamera;
    private SurfaceHolder sh;
    private android.view.ViewGroup.LayoutParams lp;
    private Bitmap bmp2=null;
    private static boolean isTrue4=false;
    private static int count=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_gong_fu_wu);
        baoCunBeanDao= MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);
        if (baoCunBean!=null){
            zhuji=baoCunBean.getZhuji();
        }else {
            Toast tastyToast= TastyToast.makeText(ShuangPingRenGongFuWuActivity.this,"请先设置主机地址",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            tastyToast.setGravity(Gravity.CENTER,0,0);
            tastyToast.show();
        }


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

        mFaceDet = MyAppLaction.mFaceDet;
        surfaceView = (SurfaceView) findViewById(R.id.fff);
        lp = surfaceView.getLayoutParams();
        sh = surfaceView.getHolder();
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sh.addCallback(this);

        baocun = (Button) findViewById(R.id.baocun);
        paizhao = (Button) findViewById(R.id.paizhao1);
        paizhao.setOnClickListener(this);
        baocun2 = (Button) findViewById(R.id.queding);
        baocun2.setOnClickListener(this);
        xianshi_im= (ImageView) findViewById(R.id.image);
        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baocun.setEnabled(false);
                if (name.getText().toString().trim().equals("") ) {
                    Toast tastyToast = TastyToast.makeText(ShuangPingRenGongFuWuActivity.this, "请填完整信息!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER, 0, 0);
                    tastyToast.show();
                    baocun.setEnabled(true);
                }else{
                    if (isA && isTiJiao){

                        if (!shouji.getText().toString().trim().equals("")){
                            if (isMobile(shouji.getText().toString().trim())){
                                link_save();
                            }else {
                                Toast tastyToast= TastyToast.makeText(ShuangPingRenGongFuWuActivity.this,"手机号码格式不正确",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                tastyToast.setGravity(Gravity.CENTER,0,0);
                                tastyToast.show();
                            }

                        }else {

                            link_save();
                        }

                    }else {
                        baocun.setEnabled(true);
                        Toast tastyToast = TastyToast.makeText(ShuangPingRenGongFuWuActivity.this, "还没有拍照,请拍照!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        tastyToast.setGravity(Gravity.CENTER, 0, 0);
                        tastyToast.show();
                    }
                    baocun.setEnabled(true);
                }
            }
        });


        tishi= (TextView) findViewById(R.id.tishi);
        name = (EditText) findViewById(R.id.name);
        bumen= (EditText) findViewById(R.id.bumen);
        //shiping = (RelativeLayout) findViewById(R.id.shiping_rl);
        ggg = (RelativeLayout) findViewById(R.id.ggg);
        shouji = (EditText) findViewById(R.id.shoujihao);
        beifangrenshouji = (EditText) findViewById(R.id.gonghao);
        riqi = (TextView) findViewById(R.id.riqi);
        riqill= (LinearLayout) findViewById(R.id.riqill);
        riqill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShuangPingRenGongFuWuActivity.this, DatePickActivity2.class);
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
                mCamera.startPreview();
                ggg.setVisibility(View.GONE);
            }
        });

        jiahao= (ImageView) findViewById(R.id.jiahao);


        myAdapter=new MyAdapter(ShuangPingRenGongFuWuActivity.this,objectsBeanList);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                beifangrenshouji.setText(objectsBeanList.get(position).getPhone()+"");
                ren.setText(objectsBeanList.get(position).getName()+"");
                bumen.setText(objectsBeanList.get(position).getDepartment()+"");
                if (objectsBeanList.size()!=0){
                    objectsBeanList.clear();
                    myAdapter.notifyDataSetChanged();

                }
                listView.setVisibility(View.GONE);
            }
        });

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
                        isTrue4=false;


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<VisionDetRet> results = mFaceDet.detect(bmp2);

                                if (results!=null) {

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
                                                xianshi_im.setImageBitmap(bitmap);
                                            }
                                        });

                                        String fn = "bbbb.jpg";
                                        FileUtil.isExists(FileUtil.PATH, fn);
                                        saveBitmap2File(bitmap.copy(Bitmap.Config.ARGB_8888,false), FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn, 100);

                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (tiJIaoDialog!=null && tiJIaoDialog.isShowing()){
                                                    tiJIaoDialog.dismiss();
                                                    tiJIaoDialog=null;
                                                }
                                                tishi.setVisibility(View.VISIBLE);
                                                tishi.setText("没有检查到人脸,请重拍");
                                            }
                                        });

                                    }

                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (tiJIaoDialog!=null && tiJIaoDialog.isShowing()){
                                                tiJIaoDialog.dismiss();
                                                tiJIaoDialog=null;
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


    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
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


    @Override
    protected void onPause() {
        isT2=false;
        isT1=false;
        count=1;
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {


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
    protected void onStop() {
        kill_camera();
        super.onStop();
    }

    private void link_save() {

        if (tiJIaoDialog==null){
            tiJIaoDialog=new TiJIaoDialog(ShuangPingRenGongFuWuActivity.this);
            if (!ShuangPingRenGongFuWuActivity.this.isFinishing())
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
                .add("visitDepartment",bumen.getText().toString().trim()+"")
                .add("homeNumber",beifangrenshouji.getText().toString().trim()+"")
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
                                final YuYueDialog dialog=new YuYueDialog(ShuangPingRenGongFuWuActivity.this,"你已成功预约,感谢你的来访!");
                                dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                                dialog.setOnPositiveListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                if (!ShuangPingRenGongFuWuActivity.this.isFinishing())
                                dialog.show();
                            }
                        });


                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast= TastyToast.makeText(ShuangPingRenGongFuWuActivity.this,"提交失败,请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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
        if (!ShuangPingRenGongFuWuActivity.this.isFinishing())
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                touxiang.setEnabled(true);
                Glide.with(ShuangPingRenGongFuWuActivity.this)
                        .load(shengfenzhengPath)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transform(new GlideCircleTransform(ShuangPingRenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                        .into(touxiang);
                Glide.with(ShuangPingRenGongFuWuActivity.this)
                        .load(shengfenzhengPath)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                      //  .transform(new GlideCircleTransform(RenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                        .into(xianshi_im);
                jiahao.setVisibility(View.GONE);

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tiJIaoDialog!=null){
                            tiJIaoDialog.dismiss();
                            tiJIaoDialog=null;
                        }
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

                  //  Log.d("AllConnects", "aa   "+ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
                    touxiangPath=zhaoPianBean.getExDesc();
                   link_zhiliang(touxiangPath);

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
    private void link_zhiliang(String touxiangPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tiJIaoDialog==null && !ShuangPingRenGongFuWuActivity.this.isFinishing()){
                    tiJIaoDialog=new TiJIaoDialog(ShuangPingRenGongFuWuActivity.this);
                    if (!ShuangPingRenGongFuWuActivity.this.isFinishing())
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
                       // isT2=true;
                     //   Log.d("RenGongFuWuActivity", "继续抓拍4");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!ShuangPingRenGongFuWuActivity.this.isFinishing()){

                                   tishi.setVisibility(View.VISIBLE);
                                   tishi.setText("照片质量不符合入库要求,请重新拍照!");

                                }

                            }
                        });

                    }else {
                        //link_save();
                        isTiJiao=true;
                      //  isT1=false;

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

                            Toast tastyToast= TastyToast.makeText(ShuangPingRenGongFuWuActivity.this,"提交失败,请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paizhao1:

                if (tiJIaoDialog==null){
                    tiJIaoDialog=new TiJIaoDialog(ShuangPingRenGongFuWuActivity.this);
                    tiJIaoDialog.setT1("检测人脸质量中,请稍后...");
                    if (!ShuangPingRenGongFuWuActivity.this.isFinishing())
                        tiJIaoDialog.show();
                }

                isTrue4=true;


                break;

            case R.id.queding:
                ggg.setVisibility(View.VISIBLE);

                break;

        }


    }
}
