package com.moons.xst.track.ui;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.common.DateTimeHelper;

public class MoonsMediaRecorder extends Activity implements
		SurfaceHolder.Callback {

	private static final String TAG = "MainActivity";
	private SurfaceView mSurfaceview;
	private Button mBtnStartStop;
	private boolean mStartedFlg = false;
	private MediaRecorder mRecorder;
	private SurfaceHolder mSurfaceHolder;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

		// 设置横屏显示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// 选择支持半透明模式,在有surfaceview的activity中使用。
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		setContentView(R.layout.mediarecorder);

		mSurfaceview = (SurfaceView) findViewById(R.id.surfaceview);
		mBtnStartStop = (Button) findViewById(R.id.btnStartStop);
		mBtnStartStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mStartedFlg) {
					// Start
					if (mRecorder == null) {
						mRecorder = new MediaRecorder(); // Create MediaRecorder
					}
					try {

						// Set audio and video source and encoder
						// 这两项需要放在setOutputFormat之前
						mRecorder
								.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
						mRecorder
								.setVideoSource(MediaRecorder.VideoSource.CAMERA);

						// Set output file format
						mRecorder
								.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

						// 这两项需要放在setOutputFormat之后
						mRecorder
								.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
						mRecorder
								.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
						mRecorder.setVideoEncodingBitRate(8 * 640 * 480);
						mRecorder.setVideoSize(640, 480);
						mRecorder.setVideoFrameRate(60);
						mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
						// Set output file path
						String path = AppConst.XSTTempVideoPath();
						String filename = AppContext.GetIMEI() + "_"
								+ DateTimeHelper.getDateTimeNow1();
						if (path != null) {

							File dir = new File(path);
							if (!dir.exists()) {
								dir.mkdir();
							}
							path = dir + "/" + filename + ".mp4";
							mRecorder.setOutputFile(path);
							mRecorder.prepare();
							mRecorder.start();
							mStartedFlg = true;
							mBtnStartStop.setText("Stop");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// stop
					if (mStartedFlg) {
						try {
							mRecorder.stop();
							mRecorder.reset();
							mBtnStartStop.setText("Start");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mStartedFlg = false;
				}
			}

		});
		SurfaceHolder holder = mSurfaceview.getHolder();
		holder.addCallback(this); // holder加入回调接口
		// setType必须设置，要不出错.
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public static String getDate() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR); // 获取年份
		int month = ca.get(Calendar.MONTH); // 获取月份
		int day = ca.get(Calendar.DATE); // 获取日
		int minute = ca.get(Calendar.MINUTE); // 分
		int hour = ca.get(Calendar.HOUR); // 小时
		int second = ca.get(Calendar.SECOND); // 秒

		String date = "" + year + (month + 1) + day + hour + minute + second;
		Log.d(TAG, "date:" + date);

		return date;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		// 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
		mSurfaceHolder = holder;
		Log.d(TAG, "surfaceChanged 1");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
		mSurfaceHolder = holder;
		Log.d(TAG, "surfaceChanged 2");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// surfaceDestroyed的时候同时对象设置为null
		mSurfaceview = null;
		mSurfaceHolder = null;
		if (mRecorder != null) {
			mRecorder.release(); // Now the object cannot be reused
			mRecorder = null;
			Log.d(TAG, "surfaceDestroyed release mRecorder");
		}
	}
}
