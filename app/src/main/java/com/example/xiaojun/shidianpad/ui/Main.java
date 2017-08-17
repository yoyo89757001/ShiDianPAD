//package com.example.xiaojun.shidianpad.ui;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.PointF;
//import android.media.FaceDetector;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.example.xiaojun.shidianpad.MyAppLaction;
//import com.example.xiaojun.shidianpad.R;
//import com.example.xiaojun.shidianpad.beans.Photos;
//import com.example.xiaojun.shidianpad.beans.ShiBieBean;
//import com.example.xiaojun.shidianpad.beans.UserInfoBena;
//import com.example.xiaojun.shidianpad.utils.FileUtil;
//import com.example.xiaojun.shidianpad.utils.GsonUtil;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.JavaCameraView;
//import org.opencv.android.Utils;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfRect;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.objdetect.CascadeClassifier;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
//public class Main extends Activity implements CameraBridgeViewBase.CvCameraViewListener2{
//
//
//	public static final String HOST="http://174p2704z3.51mypc.cn:11100";
////	public static final String HOST="http://192.168.0.104:8080";
//	private static final String  TAG    = "dddddddddddddddddddd";
//	ImageView imageView;
//	private SensorInfoReceiver sensorInfoReceiver;
//	private JavaCameraView mOpenCvCameraView;
//
//	private static final Scalar FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
//	private Mat mRgba;
//	private Mat      mGray;
//	//private Mat        mRgbaT;
//
//	private float    mRelativeFaceSize   = 0.2f;
//	private int      mAbsoluteFaceSize   = 0;
//	private CascadeClassifier mJavaDetector=null;
//	private int numberOfFace = 4;       //最大检测的人脸数
//	private FaceDetector myFaceDetect;  //人脸识别类的实例
//	private FaceDetector.Face[] myFace; //存储多张人脸的数组变量
//	int myEyesDistance;           //两眼之间的距离
//	int numberOfFaceDetected=0;       //实际检测到的人脸数
//	private static boolean isTrue=true;
//	private Bitmap bmp=null;
//	private static final int MESSAGE_QR_SUCCESS = 1;
//	private static int count=1;
//	private Handler mHandler;
//	private String filePath=null;
//	private String filePath2=null;
//	private File file1=null;
//	private File file2=null;
//	private UserInfoBena userInfoBena=null;
//	private TextView tishi;
//
//
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//
//		initHandler();
//
//		userInfoBena=new UserInfoBena();
//
//		mOpenCvCameraView = (JavaCameraView) findViewById(R.id.cp);
//
//		mOpenCvCameraView.enableView();
//		mOpenCvCameraView.setCameraIndex(1);
//		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
//		mOpenCvCameraView.setCvCameraViewListener(this);
//		imageView= (ImageView) findViewById(R.id.ffff);
//		tishi= (TextView) findViewById(R.id.tishi);
//
//
//		filePath=getIntent().getStringExtra("filePath");
//		IntentFilter intentFilter1 = new IntentFilter();
//		intentFilter1.addAction("guanbi");
//		sensorInfoReceiver = new SensorInfoReceiver();
//		registerReceiver(sensorInfoReceiver, intentFilter1);
//		mJavaDetector= MyAppLaction.mJavaDetector;
//	}
//
//	@Override
//	protected void onDestroy() {
//		isTrue=true;
//		mOpenCvCameraView.disableView();
//		unregisterReceiver(sensorInfoReceiver);
//		super.onDestroy();
//
//	}
//
//	private void initHandler() {
//		mHandler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//					case MESSAGE_QR_SUCCESS:
//
//						Bitmap bitmap= (Bitmap) msg.obj;
//						imageView.setImageBitmap(bitmap);
//
//						break;
//					case 22:
//
//						tishi.setVisibility(View.VISIBLE);
//						tishi.setText("比对失败,开始第"+count+"次比对");
//
//						break;
//
//				}
//
//			}
//		};
//	}
//
//	private class SensorInfoReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//
//			String action = intent.getAction();
//			if (action.equals("guanbi")) {
//
//				finish();
//			}
//		}
//	}
//
//	@Override
//	public void onCameraViewStarted(int width, int height) {
//
//		mGray = new Mat();
//		mRgba = new Mat();
//
//	}
//
//	@Override
//	public void onCameraViewStopped() {
//		mGray.release();
//		mRgba.release();
//
//
//		Log.d(TAG, "预览停止了");
//	}
//
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		Log.d(TAG, "暂停了");
//	}
//
//	@Override
//	public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//
//		mRgba = inputFrame.rgba();
//		mGray = inputFrame.gray();
//
////		Core.transpose(mRgba,mRgbaT); //转置函数，可以水平的图像变为垂直
////		Imgproc.resize(mRgbaT,mRgba, mRgba.size(), 0.0D, 0.0D, 0); //将转置后的图像缩放为mRgbaF的大小
////		Core.flip(mRgba, mRgba,0); //根据x,y轴翻转，0-x 1-y
////
////		Core.transpose(mGray,mRgbaT); //转置函数，可以水平的图像变为垂直
////		Imgproc.resize(mRgbaT,mGray, mGray.size(), 0.0D, 0.0D, 0); //将转置后的图像缩放为mRgbaF的大小
////		Core.flip(mGray, mGray,0); //根据x,y轴翻转，0-x 1-y
//
//		if (mAbsoluteFaceSize == 0) {
//			int height = mGray.rows();
//			if (Math.round(height * mRelativeFaceSize) > 0) {
//				mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
//			}
//		}
//
//		MatOfRect faces = new MatOfRect();
//
//		if (mJavaDetector != null)
//			mJavaDetector.detectMultiScale(mGray, faces, 1.2, 3, 1,new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
//
//
//		Rect[] facesArray = faces.toArray();
//
//
//		for (int i = 0; i < facesArray.length; i++) {  //矩形区域画框
//			// Log.i("a","face "+facesArray[i]);
//			  bmp= Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
//			Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 1);
//
//			Utils.matToBitmap(mRgba,bmp);
//
//
////			FaceDetector.Face[] facess = new FaceDetector.Face[2];
////			//格式必须为RGB_565才可以识别
////			Bitmap bmpf = bmp.copy(Bitmap.Config.RGB_565, true);
////			//返回识别的人脸数
////			final int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(),2).findFaces(bmpf, facess);
////
////			bmpf.recycle();
////			bmpf = null;
//
//		//	Log.d(TAG, "faceCount:" + faceCount);
//			shibie();
//
//
//
//		}
//		//Log.d(TAG, "mRgba.cols():" + mRgba.cols());
//
//		return mRgba;
//	}
//
//
//	private void shibie(){
//
//		if (isTrue){
//
//			Thread thread=	new Thread(new Runnable() {
//				@Override
//				public void run() {
//					isTrue=false;
//					//FaceDetector.Face[] facess = new FaceDetector.Face[1];
//					//格式必须为RGB_565才可以识别
//					Bitmap bmpf = bmp.copy(Bitmap.Config.RGB_565, true);
//					//返回识别的人脸数
//					//	int faceCount = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 1).findFaces(bmpf, facess);
//					//	FaceDetector faceCount2 = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), 2);
//
//					myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
//					myFaceDetect = new FaceDetector(bmpf.getWidth(), bmpf.getHeight(), numberOfFace);
//					numberOfFaceDetected = myFaceDetect.findFaces(bmpf, myFace);    //FaceDetector 构造实例并解析人脸
//
//
//					bmpf.recycle();
//					bmpf = null;
//
//					if (numberOfFaceDetected > 0) {
//
//
//						FaceDetector.Face face = myFace[0];
//
//						PointF pointF = new PointF();
//						face.getMidPoint(pointF);
//						myEyesDistance = (int)face.eyesDistance();
//						int xx=0;
//						int yy=0;
//						int xx2=0;
//						int yy2=0;
//						int w=bmp.getWidth();
//						int h=bmp.getHeight();
//
//
//						if ((int)pointF.x-200>=0){
//							xx=(int)pointF.x-200;
//						}else {
//							xx=0;
//						}
//						if ((int)pointF.y-200>=0){
//							yy=(int)pointF.y-200;
//						}else {
//							yy=0;
//						}
//						if (xx+350 >=w){
//							xx2=w-xx;
//							Log.d("fff", "xxxxxxxxxx:" + xx2);
//						}else {
//							xx2=350;
//						}
//						if (yy+350>=h){
//							yy2=h-yy;
//							Log.d("fff", "yyyyyyyyy:" + yy2);
//						}else {
//							yy2=350;
//						}
//
//
//						Bitmap bitmap = Bitmap.createBitmap(bmp,xx,yy,xx2,yy2);
//
//						Message message=Message.obtain();
//						message.what=MESSAGE_QR_SUCCESS;
//						message.obj=bitmap;
//						mHandler.sendMessage(message);
//
//
//
//						String fn="bbbb.jpg";
//						FileUtil.isExists(FileUtil.PATH,fn);
//						saveBitmap2File(bitmap, FileUtil.SDPATH+ File.separator+FileUtil.PATH+File.separator+fn,100);
//
//
//					}else {
//						isTrue=true;
//					}
//				}
//			});
//			thread.start();
//		}
//	}
//
//	public  void saveBitmap2File(Bitmap bm, final String path, int quality) {
//		try {
//		filePath2=path;
//		if (null == bm) {
//			Log.d("InFoActivity", "回收|空");
//			return ;
//		}
//
//			File file = new File(path);
//			if (file.exists()) {
//				file.delete();
//			}
//			BufferedOutputStream bos = new BufferedOutputStream(
//					new FileOutputStream(file));
//			bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
//			bos.flush();
//			bos.close();
//
//			link_P1(filePath,filePath2);
//
//			//比对图片
////            if (smallFilePath!=null) {
////                // Log.d("SerialReadActivity", "靠靠靠靠靠");
////                link_tianqi(file, path, smallFilePath);
////            }
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//
////			if (!bm.isRecycled()) {
////				bm.recycle();
////			}
//			bm = null;
//		}
//	}
//
//
//	public static final int TIMEOUT = 1000 * 60;
//	private void link_P1(String filename1, final String fileName2) {
//		//final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
//		//http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
//		OkHttpClient okHttpClient= new OkHttpClient.Builder()
//				.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.retryOnConnectionFailure(true)
//				.build();
//
//         /* 第一个要上传的file */
//		file1 = new File(filename1);
//		RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
//		final String file1Name = System.currentTimeMillis()+"testFile1.jpg";
//
////    /* 第二个要上传的文件,*/
////        File file2 = new File(fileName2);
////        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
////        String file2Name =System.currentTimeMillis()+"testFile2.jpg";
//
//
////    /* form的分割线,自己定义 */
////        String boundary = "xx--------------------------------------------------------------xx";
//
//		MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//            /* 底下是上传了两个文件 */
//				.addFormDataPart("voiceFile" , file1Name , fileBody1)
//                  /* 上传一个普通的String参数 */
//				//  .addFormDataPart("subject_id" , subject_id+"")
//				//  .addFormDataPart("image_2" , file2Name , fileBody2)
//				.build();
//		Request.Builder requestBuilder = new Request.Builder()
//				// .header("Content-Type", "application/json")
//				.post(mBody)
//				.url(HOST + "/AppFileUploadServlet?FilePathPath=cardFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");
//
//		// step 3：创建 Call 对象
//		Call call = okHttpClient.newCall(requestBuilder.build());
//
//		//step 4: 开始异步请求
//		call.enqueue(new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//				Log.d("AllConnects", "请求识别失败"+e.getMessage());
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				Log.d("AllConnects", "请求识别成功"+call.request().toString());
//				//获得返回体
//				try {
//
//					ResponseBody body = response.body();
//					String ss=body.string();
//
//					Log.d("AllConnects", "aa   "+ss);
//
//					JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
//					Gson gson=new Gson();
//					Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
//					userInfoBena.setCardPhoto(zhaoPianBean.getExDesc());
//					link_P2(fileName2);
//
//
//				}catch (Exception e){
//
//					Log.d("WebsocketPushMsg", e.getMessage());
//				}
//			}
//		});
//
//
//	}
//
//	private void link_P2( String fileName2) {
//		//final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
//		//http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
//		OkHttpClient okHttpClient= new OkHttpClient.Builder()
//				.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.retryOnConnectionFailure(true)
//				.build();
//		Log.d(TAG, "第二个上传");
//
////         /* 第一个要上传的file */
////        File file1 = new File(filename1);
////        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
////        final String file1Name = System.currentTimeMillis()+"testFile1.jpg";
//
//    /* 第二个要上传的文件,*/
//		file2 = new File(fileName2);
//		RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
//		String file2Name =System.currentTimeMillis()+"testFile2.jpg";
//
//
////    /* form的分割线,自己定义 */
////        String boundary = "xx--------------------------------------------------------------xx";
//
//		MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//            /* 底下是上传了两个文件 */
//				//  .addFormDataPart("image_1" , file1Name , fileBody1)
//                  /* 上传一个普通的String参数 */
//				//  .addFormDataPart("subject_id" , subject_id+"")
//				.addFormDataPart("voiceFile" , file2Name , fileBody2)
//				.build();
//		Request.Builder requestBuilder = new Request.Builder()
//				// .header("Content-Type", "application/json")
//				.post(mBody)
//				.url(HOST + "/AppFileUploadServlet?FilePathPath=scanFilePath&AllowFileType=.jpg,.gif,.jpeg,.bmp,.png&MaxFileSize=10");
//
//
//		// step 3：创建 Call 对象
//		Call call = okHttpClient.newCall(requestBuilder.build());
//
//		//step 4: 开始异步请求
//		call.enqueue(new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//				Log.d("AllConnects", "请求识别失败"+e.getMessage());
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				Log.d("AllConnects", "请求识别成功"+call.request().toString());
//				//删掉文件
//
//				//获得返回体
//				try {
//
//					ResponseBody body = response.body();
//					// Log.d("AllConnects", "aa   "+response.body().string());
//
//					JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
//					Gson gson=new Gson();
//					Photos zhaoPianBean=gson.fromJson(jsonObject,Photos.class);
//					userInfoBena.setScanPhoto(zhaoPianBean.getExDesc());
//
//					link_tianqi3();
//
//
//				}catch (Exception e){
//					Log.d("WebsocketPushMsg", e.getMessage());
//				}
//			}
//		});
//
//
//	}
//
//	private void link_tianqi3() {
//		//final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
//		//http://192.168.2.4:8080/sign?cmd=getUnSignList&subjectId=jfgsdf
//		OkHttpClient okHttpClient= new OkHttpClient.Builder()
//				.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.retryOnConnectionFailure(true)
//				.build();
//
//
////    /* form的分割线,自己定义 */
////        String boundary = "xx--------------------------------------------------------------xx";
//		RequestBody body = new FormBody.Builder()
//				.add("cardPhoto",userInfoBena.getCardPhoto())
//				.add("scanPhoto",userInfoBena.getScanPhoto())
//				.build();
//
//		Request.Builder requestBuilder = new Request.Builder()
//				// .header("Content-Type", "application/json")
//				.post(body)
//				.url(HOST + "/compare.do");
//
//
//		// step 3：创建 Call 对象
//		Call call = okHttpClient.newCall(requestBuilder.build());
//
//		//step 4: 开始异步请求
//		call.enqueue(new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//				Log.d("AllConnects", "请求识别失败"+e.getMessage());
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				Log.d("AllConnects", "请求识别成功"+call.request().toString());
//				//获得返回体
//				try {
//					count++;
//
//					ResponseBody body = response.body();
//					//  Log.d("AllConnects", "识别结果返回"+response.body().string());
//
//					JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
//					Gson gson=new Gson();
//					final ShiBieBean zhaoPianBean=gson.fromJson(jsonObject,ShiBieBean.class);
//
//					if (zhaoPianBean.getScore()>=75.0) {
//
//						//比对成功
//						sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",true)
//								.putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
//								.putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
//						count=1;
//
//					}else {
//
//
//						if (count<=3){
//
//							Message message=Message.obtain();
//							message.what=22;
//							mHandler.sendMessage(message);
//
//							Thread.sleep(2000);
//
//							isTrue=true;
//							shibie();
//
//						}else {
//
//							sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false)
//									.putExtra("xiangsidu",(zhaoPianBean.getScore()+"").substring(0,5))
//									.putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
//							count=1;
//						}
//
//					}
//
//
//				}catch (Exception e){
//
//					if (count<=3){
//
//						Message message=Message.obtain();
//						message.what=22;
//						mHandler.sendMessage(message);
//
//						try {
//							Thread.sleep(1500);
//						} catch (InterruptedException e1) {
//							e1.printStackTrace();
//						}
//
//						isTrue=true;
//						shibie();
//
//					}else {
//
//						sendBroadcast(new Intent("guanbi").putExtra("biduijieguo",false).putExtra("xiangsidu","43.21")
//								.putExtra("cardPath",userInfoBena.getCardPhoto()).putExtra("saomiaoPath",userInfoBena.getScanPhoto()));
//						count=1;
//					}
//
//					Log.d("WebsocketPushMsg", e.getMessage());
//				}
//			}
//		});
//
//
//	}
//
//
//}
