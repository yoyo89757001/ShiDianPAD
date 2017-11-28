package com.example.xiaojun.shidianpad.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DengJiActivity extends Activity implements View.OnClickListener {
    private ImageView riqi_im,touxiang;
    private TextView riqi_tv,name,bidui_tv;
    private EditText shoufangren,shoufangrenshu,bumen_ET,bfr_dianhua;
//    private Myadapter myadapter;
    private List<String> stringList;
   // private String[] datas = {"后勤", "人事部", "生产部", "部门4", "部门666"};
 //   private int xuanzhong;
  //  private  PopupWindow window;
    private Button wancheng;
    private String nameS;
    private int type;
    private SensorInfoReceiver sensorInfoReceiver;
    private boolean bidui;
    public static final int TIMEOUT = 1000 * 60;
    private TiJIaoDialog tiJIaoDialog=null;
    private String id;
    private String shiyou;
    private String zhuji=null;
    private MyAdapter myAdapter=null;
    private ListView listView;
    private RelativeLayout rl,fffff;
    private List<ChaXunBean.ObjectsBean> objectsBeanList=new ArrayList<>();
    private BaoCunBeanDao baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baoCunBeanDao= MyAppLaction.myAppLaction.getDaoSession().getBaoCunBeanDao();
        baoCunBean=baoCunBeanDao.load(123456L);
        zhuji=baoCunBean.getZhuji();
        setContentView(R.layout.activity_deng_ji);
        nameS=getIntent().getStringExtra("name");
        type=getIntent().getIntExtra("type",0);
        bidui=getIntent().getBooleanExtra("bidui",false);
        id=getIntent().getStringExtra("id");
        switch (type){
            case 1:
                shiyou="业务";
                break;
            case 2:
                shiyou="合作";
                break;
            case 3:
                shiyou="面试";
                break;
            case 4:
                shiyou="其它";
                break;
        }


        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("guanbi2");
        sensorInfoReceiver = new SensorInfoReceiver();
        registerReceiver(sensorInfoReceiver, intentFilter1);
        listView= (ListView) findViewById(R.id.lsvMore);
        rl= (RelativeLayout) findViewById(R.id.dddd);
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
        myAdapter=new MyAdapter(DengJiActivity.this,objectsBeanList);
        fffff= (RelativeLayout) findViewById(R.id.juju);
        fffff.setOnClickListener(this);
        touxiang= (ImageView) findViewById(R.id.touxiang);
        bidui_tv= (TextView) findViewById(R.id.bidui_tv);
        name= (TextView) findViewById(R.id.editText);
        bfr_dianhua= (EditText) findViewById(R.id.beifangrendianhua);
        bumen_ET= (EditText) findViewById(R.id.bumen);
        riqi_tv= (TextView) findViewById(R.id.riqi);
        shoufangren= (EditText) findViewById(R.id.shoufang);
        shoufangren.addTextChangedListener(textWatcher);
        shoufangrenshu= (EditText) findViewById(R.id.renshu);
        shoufangrenshu.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (zhuji!=null)
                    link_save();
                }
                return false;
            }
        });
      //  riqi_im= (ImageView) findViewById(R.id.imageView);
       // riqi_im.setOnClickListener(this);
        //bumen_im= (ImageView) findViewById(R.id.imageView2);
      //  bumen_im.setOnClickListener(this);
        wancheng= (Button) findViewById(R.id.queren2);
        wancheng.setOnClickListener(this);

     //   stringList=new ArrayList<>();


      //  myadapter=new Myadapter(DengJiActivity.this,stringList);

        riqi_tv.setText(DateUtils.timet2(System.currentTimeMillis()+""));

        name.setText(nameS+"");
        if (bidui){
            bidui_tv.setText("比对通过!");
        }else {
            bidui_tv.setText("比对不通过!");
        }

        Glide.with(DengJiActivity.this)
                .load(FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + "bbbb.jpg")
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new GlideCircleTransform(DengJiActivity.this,2,Color.parseColor("#ffffffff")))
                .into(touxiang);

        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bfr_dianhua.setText(objectsBeanList.get(position).getPhone()+"");
                shoufangren.setText(objectsBeanList.get(position).getName()+"");
                bumen_ET.setText(objectsBeanList.get(position).getDepartment()+"");

                if (objectsBeanList.size()!=0){
                    objectsBeanList.clear();
                    myAdapter.notifyDataSetChanged();

                }
                listView.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.juju: //日期

                Intent intent = new Intent(DengJiActivity.this, DatePickActivity2.class);
                startActivityForResult(intent,2);

                break;
//            case R.id.imageView2: //部门
//                View popupView =getLayoutInflater().inflate(R.layout.popupwindow, null);
//                //  2016/5/17 为了演示效果，简单的设置了一些数据，实际中大家自己设置数据即可，相信大家都会。
//                ListView lsvMore = (ListView) popupView.findViewById(R.id.lsvMore);
//                lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        xuanzhong=position;
//                        window.dismiss();
//
//                    }
//                });
//                lsvMore.setAdapter(myadapter);
//                //  2016/5/17 创建PopupWindow对象，指定宽度和高度
//                 window = new PopupWindow(popupView, 200, 340);
//                //  2016/5/17 设置动画
//               // window.setAnimationStyle(R.style.AnimBottom2);
//                //  2016/5/17 设置背景颜色
//                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cc4285f4")));
//                //  2016/5/17 设置可以获取焦点
//                window.setFocusable(true);
//                //  2016/5/17 设置可以触摸弹出框以外的区域
//                window.setOutsideTouchable(true);
//                // 更新popupwindow的状态
//                window.update();
//                //  2016/5/17 以下拉的方式显示，并且可以设置显示的位置
//                window.showAsDropDown(bumen_im, -200,0);
//
//
//                break;
            case R.id.queren2:

                if (zhuji!=null){
                    if (!bfr_dianhua.getText().toString().trim().equals("")){
                        if (isMobile(bfr_dianhua.getText().toString().trim())){
                            link_save();
                        }else {
                            Toast tastyToast= TastyToast.makeText(DengJiActivity.this,"手机号码格式不正确",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            tastyToast.setGravity(Gravity.CENTER,0,0);
                            tastyToast.show();
                        }

                    }else {

                        link_save();
                    }

                }


                break;

        }

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


    private class Myadapter extends BaseAdapter{
        Context mContext;

        LayoutInflater mInflater;

        List<String> mDataList;

        /**
         * @param context
         * @param data
         */
        public Myadapter(Context context, List<String> data) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mDataList = data;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public String getItem(int position) {

            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tachuangdialog, null, false);
                viewHolder = new ViewHolder();
                viewHolder.mTextView = (TextView) convertView.findViewById(R.id.fffff);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.mTextView.setText(getItem(position));
            return convertView;
        }

        /**
         * ViewHolder
         *
         * @author mrsimple
         */
        private class ViewHolder {

            TextView mTextView;
        }

    }

    private class SensorInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals("guanbi2")) {
                finish();

            }
        }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            // 选择预约时间的页面被关闭
            String date = data.getStringExtra("date");
            riqi_tv.setText(date);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sensorInfoReceiver);
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
                .add("id",id)
                .add("visitIncident",shiyou)
                .add("visitDate2",riqi_tv.getText().toString().trim())
                .add("visitDepartment",bumen_ET.getText().toString().trim()+"-"+bfr_dianhua.getText().toString().trim())
                .add("visitPerson",shoufangren.getText().toString().trim()+"")
                .add("visitNum",shoufangrenshu.getText().toString().trim()+"")
                .build();

      //  Log.d("DengJiActivity", DateUtils.dataOne(riqi_tv.getText().toString().trim()));

        Request.Builder requestBuilder = new Request.Builder()
                // .header("Content-Type", "application/json")
                .post(body)
                .url(zhuji + "/saveCompareVisit.do");

        if (tiJIaoDialog==null){
            tiJIaoDialog=new TiJIaoDialog(DengJiActivity.this);
            if (!DengJiActivity.this.isFinishing())
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
                    Log.d("DengJiActivity", ss);

                    JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson=new Gson();
                    ShouFangBean zhaoPianBean=gson.fromJson(jsonObject,ShouFangBean.class);

                    if (zhaoPianBean.getDtoResult()==0){
                       // Log.d("DengJiActivity", "dddd");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final YuYueDialog dialog=new YuYueDialog(DengJiActivity.this,"你已成功预约,感谢你的来访!");
                                dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                                dialog.setOnPositiveListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent intent=new Intent("guanbi2");
                                        sendBroadcast(intent);
                                    }
                                });
                                if (!DengJiActivity.this.isFinishing())
                                dialog.show();
                            }
                        });


                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast tastyToast= TastyToast.makeText(DengJiActivity.this,"提交失败,请检查网络",TastyToast.LENGTH_LONG,TastyToast.ERROR);
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
                    Log.d("DengJiActivity", ss);

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
                                shoufangren.getLocationOnScreen(location);

                                p.topMargin= (7-objectsBeanList.size())*listItem.getMeasuredHeight();
                                p.bottomMargin= location[1]-220;
                                listView.setLayoutParams(p);
                                listView.invalidate();
                                //  Log.d("RenGongFuWuActivity", "fdsfdfd");
                            }else {
                                RelativeLayout.LayoutParams p= (RelativeLayout.LayoutParams) listView.getLayoutParams();
                                int[] location = new int[2];
                                shoufangren.getLocationOnScreen(location);
                                p.topMargin= 120;
                                p.bottomMargin= location[1]-220;
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
