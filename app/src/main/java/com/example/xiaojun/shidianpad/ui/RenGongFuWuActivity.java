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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xiaojun.shidianpad.MyAppLaction;
import com.example.xiaojun.shidianpad.R;
import com.example.xiaojun.shidianpad.dialog.JiaZaiDialog;
import com.example.xiaojun.shidianpad.dialog.QueRenDialog;
import com.example.xiaojun.shidianpad.kaer.BeepManager;
import com.example.xiaojun.shidianpad.utils.DateUtils;
import com.example.xiaojun.shidianpad.utils.FileUtil;
import com.example.xiaojun.shidianpad.utils.LibVLCUtil;
import com.example.xiaojun.shidianpad.view.AutoFitTextureView;
import com.example.xiaojun.shidianpad.view.GlideCircleTransform;
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
import java.lang.reflect.Type;
import java.util.List;

public class RenGongFuWuActivity extends Activity {
    private EditText name,shouji,beifangrenshouji,ren,renshu;
    private ImageView touxiang,jiahao;
    private TextView riqi;
    private Button baocun,paizhao;
    private  String zhuji=null;
    private AutoFitTextureView videoView;
    private MediaPlayer mediaPlayer=null;
    private IVLCVout vlcVout=null;
    private IVLCVout.Callback callback;
    private LibVLC libvlc;
    private Media media;
    private FaceDet mFaceDet;
    private RelativeLayout ggg,shiping;
    private  String ip=null;
    private JiaZaiDialog jiaZaiDialog=null;
    private BeepManager _beepManager;
    private Bitmap bitmap=null;
    private String shengfenzhengPath=null;
    private LinearLayout riqill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_gong_fu_wu);

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
        videoView.setAspectRatio(5, 3);
        baocun = (Button) findViewById(R.id.baocun);
        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().equals("")) {
                    Toast tastyToast = TastyToast.makeText(RenGongFuWuActivity.this, "请填写姓名!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    tastyToast.setGravity(Gravity.CENTER, 0, 0);
                    tastyToast.show();

                }
            }
        });
        _beepManager = new BeepManager(RenGongFuWuActivity.this);

        libvlc = LibVLCUtil.getLibVLC(RenGongFuWuActivity.this);
        mediaPlayer = new MediaPlayer(libvlc);
        vlcVout = mediaPlayer.getVLCVout();
        name = (EditText) findViewById(R.id.name);
        paizhao= (Button) findViewById(R.id.paizhao);
        paizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paizhao.setClickable(false);

                bitmap=videoView.getBitmap();
                if (bitmap!=null){
                    List<VisionDetRet> results = mFaceDet.detect(bitmap);

                    if (results != null) {
                        int s = results.size();
                        if (s>0){
                            paizhao.setClickable(true);
                            ggg.setVisibility(View.VISIBLE);

                            String fn="ccc.jpg";
                            FileUtil.isExists(FileUtil.PATH,fn);
                            saveBitmap2File(bitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);

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


                        }else {
                            final QueRenDialog dialog=new QueRenDialog(RenGongFuWuActivity.this,"没有检测到人脸,请重新拍摄!");
                            dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                            dialog.setOnPositiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    paizhao.setClickable(true);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }

                    }
                }

            }
        });
        shiping = (RelativeLayout) findViewById(R.id.shiping_rl);
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

    }

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
        super.onPause();


            if (vlcVout!=null & mediaPlayer!=null){
                vlcVout.detachViews();
                vlcVout.removeCallback(callback);
                vlcVout=null;
            }
            if (videoView!=null){
                videoView.setSurfaceTextureListener(null);
            }

        if (_beepManager!=null){
            _beepManager.close();
            _beepManager=null;
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

            Glide.with(RenGongFuWuActivity.this)
                    .load(shengfenzhengPath)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                     .transform(new GlideCircleTransform(RenGongFuWuActivity.this,1, Color.parseColor("#ffffffff")))
                    .into(touxiang);
            jiahao.setVisibility(View.GONE);
//            kaishiPaiZhao();


        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (!bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }
    }

}
