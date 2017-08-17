package com.example.xiaojun.shidianpad.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.ImageView;
import com.example.xiaojun.shidianpad.beans.Photos;
import com.example.xiaojun.shidianpad.beans.ShiBieBean;
import com.example.xiaojun.shidianpad.beans.UserInfoBena;
import com.example.xiaojun.shidianpad.utils.FileUtil;
import com.example.xiaojun.shidianpad.utils.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
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

class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Runnable {

//	adb root
//	adb remount
//	adb shell
//	chmod 777 /dev/video1
	public static boolean isTrue=true;
	private static final int MESSAGE_QR_SUCCESS = 1;
	private static final boolean DEBUG = true;
	private static final String TAG = "WebCam";
	protected Context context;
	private SurfaceHolder holder;
	public static final String HOST="http://192.166.2.100:8080";
	Thread mainLoop = null;
	private Bitmap bmp = null;
	private File file1=null;
	private File file2=null;
	private boolean cameraExists = false;
	private boolean shouldStop = false;
	private static int count=1;

	private int numberOfFace = 1;       //最大检测的人脸数
	private FaceDetector myFaceDetect;  //人脸识别类的实例
	private FaceDetector.Face[] myFace; //存储多张人脸的数组变量
	int myEyesDistance;           //两眼之间的距离
	int numberOfFaceDetected;       //实际检测到的人脸数

	// /dev/videox (x=cameraId+cameraBase) is used.
	// In some omap devices, system uses /dev/video[0-3],
	// so users must use /dev/video[4-].
	// In such a case, try cameraId=0 and cameraBase=4
	private int cameraId = 0;
	private int cameraBase = 0;

	// This definition also exists in ImageProc.h.
	// Webcam must support the resolution 640x480 with YUYV format.
	static final int IMG_WIDTH = 640;
	static final int IMG_HEIGHT = 480;

	// The following variables are used to draw camera images.
	private int winWidth = 0;
	private int winHeight = 0;
	private Rect rect;
	private int dw, dh;
	private float rate;
	private ImageView imageView=null;
	private String filePath=null;
	private String filePath2=null;
	private Handler mHandler;
	private UserInfoBena userInfoBena=null;

	// JNI functions
	public native int prepareCamera(int videoid);

	public native int prepareCameraWithBase(int videoid, int camerabase);

	public native void processCamera();

	public native void stopCamera();

	public native void pixeltobmp(Bitmap bitmap);

	static {
		System.loadLibrary("ImageProc");

	}

	public CameraPreview(Context context) {
		super(context);
		this.context = context;

		setFocusable(true);
		initHandler();
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
	}

	public void setIV(ImageView iv,String filePath){
		this.filePath=filePath;
		imageView=iv;
		userInfoBena=new UserInfoBena();
	}

	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;


		setFocusable(true);
		initHandler();
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
	}

	public void setisTrue(){

		 isTrue=true;
	}
	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case MESSAGE_QR_SUCCESS:
						Bitmap bitmap= (Bitmap) msg.obj;
						imageView.setImageBitmap(bitmap);

						break;
					case 22:

						TastyToast.makeText(context,"比对不成功，开始第"+count+"次比对",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();

						break;

				}

			}
		};
	}


	public  void saveBitmap2File(Bitmap bm, final String path, int quality) {
			filePath2=path;

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

			link_P1(filePath,filePath2);

			//比对图片
//            if (smallFilePath!=null) {
//                // Log.d("SerialReadActivity", "靠靠靠靠靠");
//                link_tianqi(file, path, smallFilePath);
//            }


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
	public void run() {
		while (cameraExists) {
			// obtaining display area to draw a large image
			if (winWidth == 0) {
				winWidth = this.getWidth();
				winHeight = this.getHeight();

//				if (winWidth * 3 / 4 <= winHeight) {
//					dw = 0;
//					dh = (winHeight - winWidth * 3 / 4) / 2;
//					rate = ((float) winWidth) / IMG_WIDTH;
//					rect = new Rect(dw, dh, dw + winWidth - 1, dh + winWidth * 3 / 4 - 1);
//				} else {
//					dw = (winWidth - winHeight * 4 / 3) / 2;
//					dh = 0;
//					rate = ((float) winHeight) / IMG_HEIGHT;
//					rect = new Rect(dw, dh, dw + winHeight * 4 / 3 - 1, dh + winHeight - 1);
//				}
				rect = new Rect(dw, dh,  winWidth ,  winHeight );
			}

			// obtaining a camera image (pixel data are stored in an array in
			// JNI).
			processCamera();
			// camera image to bmp
			pixeltobmp(bmp);

		    shibie();

			Canvas canvas = getHolder().lockCanvas();
			if (canvas != null) {
				// draw camera bmp on canvas
				canvas.drawBitmap(bmp, null, rect, null);
				getHolder().unlockCanvasAndPost(canvas);
			}

			if (shouldStop) {
				shouldStop = false;
				break;
			}
		}
	}

	private void shibie(){

		if (isTrue){

			Thread thread=	new Thread(new Runnable() {
				@Override
				public void run() {
					isTrue=false;
					FaceDetector.Face[] facess = new FaceDetector.Face[1];
					//格式必须为RGB_565才可以识别
					Bitmap bmpf = bmp.copy(Bitmap.Config.RGB_565, true);
					//返回识别的人脸数
					//	int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 1).findFaces(bmpf, facess);
					//	FaceDetector faceCount2 = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 2);


					myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
					myFaceDetect = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), numberOfFace);
					numberOfFaceDetected = myFaceDetect.findFaces(bmpf, myFace);    //FaceDetector 构造实例并解析人脸


					bmpf.recycle();
					bmpf = null;

					if (numberOfFaceDetected > 0) {

						FaceDetector.Face face = myFace[0];
						PointF pointF = new PointF();
						face.getMidPoint(pointF);
						myEyesDistance = (int)face.eyesDistance();
						int xx=0;
						int yy=0;
						if ((int)pointF.x-120>=0){
							xx=(int)pointF.x-120;
						}else {
							xx=0;
						}
						if ((int)pointF.y-130>=0){
							yy=(int)pointF.y-130;
						}else {
							yy=0;
						}
						if (xx + myEyesDistance + 160>640){
							xx=640-(myEyesDistance + 160);
							Log.d(TAG, "bmp.getWidth():" + bmp.getWidth());
						}
						if (yy + myEyesDistance + 180>480){
							yy=480-(myEyesDistance + 180);
							Log.d(TAG, "bmp.getWidth():" + bmp.getWidth());
						}


						Bitmap bitmap = Bitmap.createBitmap(bmp,xx,yy,myEyesDistance+160,myEyesDistance+180);

						Message message=Message.obtain();
						message.what=MESSAGE_QR_SUCCESS;
						message.obj=bitmap;
						mHandler.sendMessage(message);

						String fn="bbbb.jpg";
						FileUtil.isExists(FileUtil.PATH,fn);
						saveBitmap2File(bitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);



					}else {
						isTrue=true;
					}
				}
			});
			thread.start();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		if (bmp == null) {
			bmp = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT, Bitmap.Config.ARGB_8888);
		}
		// /dev/videox (x=cameraId + cameraBase) is used
		// int ret = prepareCameraWithBase(cameraId, cameraBase);
		int ret = prepareCameraWithBase(cameraId, 0);

		if (ret != -1)
			cameraExists = true;

		mainLoop = new Thread(this);
		mainLoop.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (DEBUG)
			Log.d(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (DEBUG)
			Log.d(TAG, "surfaceDestroyed");
		if (cameraExists) {
			shouldStop = true;
			while (shouldStop) {
				try {
					Thread.sleep(100); // wait for thread stopping
				} catch (Exception e) {
					Log.d(TAG, e.getMessage());
				}
			}
		}
		stopCamera();
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
				.url(HOST + "/AppFileUploadServlet?FilePathPath=cardFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");


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
					//  Log.d("AllConnects", "aa   "+response.body().string());

					JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
					Gson gson=new Gson();
					Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
					userInfoBena.setCardPhoto(zhaoPianBean.getExDesc());
					link_P2(fileName2);


				}catch (Exception e){
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
				.url(HOST + "/AppFileUploadServlet?FilePathPath=scanFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");


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
				.url(HOST + "/compare.do");


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
					count++;

					ResponseBody body = response.body();
					//  Log.d("AllConnects", "识别结果返回"+response.body().string());

					JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
					Gson gson=new Gson();
					final ShiBieBean zhaoPianBean=gson.fromJson(jsonObject,ShiBieBean.class);

					if (zhaoPianBean.getScore()>=75.0) {
						//比对成功

						context.sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",true).putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5)));
						count=1;

					}else {


					if (count<=3){

						Message message=Message.obtain();
						message.what=22;
						mHandler.sendMessage(message);

						Thread.sleep(2000);

						isTrue=true;
						shibie();

					}else {

						context.sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false).putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5)).putExtra("type",1));
						count=1;
					}

					}


				}catch (Exception e){

					if (count<=3){

						Message message=Message.obtain();
						message.what=22;
						mHandler.sendMessage(message);

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}

						isTrue=true;
						shibie();

					}else {

						context.sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false).putExtra("xiangsidu","43.21").putExtra("type",1));
						count=1;
					}

					Log.d("WebsocketPushMsg", e.getMessage());
				}
			}
		});


	}
}
