package com.moons.xst.track.ui.operatebill_fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.moons.xst.buss.OperateHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.ui.AudioPlayerDialog;
import com.moons.xst.track.ui.OperationDetailsAty;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 危险点分析fragment
 * **/
public class DangerousAnalysisFragment extends Fragment implements
		OnClickListener {
	View DangerousAnalysis;

	ImageButton home_head_Rebutton, play_button, save_button;
	ImageView iv_record_delete, iv_record_state;
	Button btn_nextstep;
	TextView tv_record_item, tv_record_time, tv_layout_dangerpoint;
	private String Code;
	private OperateHelper operateHelper;
	private String dangerpoint;

	private String path = AppConst.TwoTicketRecordPath();// 录音保存路径
	private String filename = null;// 语音文件
	private ArrayList<String> mList = new ArrayList<String>();// 待合成的音乐片段
	private ArrayList<String> list = new ArrayList<String>();// 合成好后的录音名
	private boolean isPause = false;// 当前是否处于暂停状态
	private boolean ispausePlay = false;// 当前播放是否处于暂停状态
	private Timer timer;
	private long limitTime = 0;// 录音文件最短时间1秒
	// 计时用的的变量
	private int second = 0;
	private int minute = 0;
	private int hour = 0;

	private MediaPlayer mPlayer = null;// 播放器
	private MediaRecorder mRecorder = null;// 录音器

	public static DangerousAnalysisFragment instance;

	// fragment单例
	public static DangerousAnalysisFragment instance() {
		if (instance == null) {
			instance = new DangerousAnalysisFragment();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (DangerousAnalysis == null) {
			DangerousAnalysis = inflater.inflate(R.layout.dangerpoint_analysis,
					container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) DangerousAnalysis.getParent();
		if (parent != null) {
			parent.removeView(DangerousAnalysis);
		}
		return DangerousAnalysis;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		operateHelper = new OperateHelper();
		OperationDetailsAty activity = (OperationDetailsAty) getActivity();
		Code = activity.Code;
		dangerpoint = operateHelper.dangerpointQuery(getActivity(), Code);
		initView();
	}

	private void initView() {

		// 危险分析
		tv_layout_dangerpoint = (TextView) DangerousAnalysis
				.findViewById(R.id.tv_layout_dangerpoint);
		tv_layout_dangerpoint.setText(dangerpoint);
		tv_layout_dangerpoint.setMovementMethod(ScrollingMovementMethod.getInstance());
		// 开始 暂停
		play_button = (ImageButton) DangerousAnalysis
				.findViewById(R.id.play_button);
		play_button.setOnClickListener(this);

		// 保存
		save_button = (ImageButton) DangerousAnalysis
				.findViewById(R.id.save_button);
		save_button.setEnabled(false);
		save_button.setOnClickListener(this);

		// 删除记录
		iv_record_delete = (ImageView) DangerousAnalysis
				.findViewById(R.id.imageview_record_delete);
		iv_record_delete.setOnClickListener(this);
		// 录音状态
		iv_record_state = (ImageView) DangerousAnalysis
				.findViewById(R.id.imageview_record_state);

		// 文件名
		tv_record_item = (TextView) DangerousAnalysis
				.findViewById(R.id.textview_record_item);
		tv_record_item.setOnClickListener(this);
		// 时间
		tv_record_time = (TextView) DangerousAnalysis
				.findViewById(R.id.textview_record_time);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.play_button:// 开始 暂停
			save_button.setEnabled(true);
			if (play_button.getTag().equals("true")) {// 点击开始
				play_button.setTag("false");
				starRecord();
				recordTime();
				iv_record_state.setImageResource(R.drawable.ic_record_start);
				play_button.setBackgroundResource(R.drawable.pause);
			} else if (play_button.getTag().equals("false")) {// 点击暂停

				pauseRecorder();
			}
			break;
		case R.id.textview_record_item:// 点击文件名听录音
			if (tv_record_item.getText().toString().length() > 2) {
				list.clear();
				list.add(path + Code + ".wav");
				AudioPlayerDialog Audialog = new AudioPlayerDialog(
						getActivity(), path + Code + ".wav", list);
				Audialog.show();
			}
			break;
		case R.id.imageview_record_delete:// 删除录音
			if (tv_record_item.getText().length() > 0) {
				LayoutInflater factory = LayoutInflater.from(getActivity());
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(getActivity())
						.builder()
						.setTitle(getString(R.string.plan_del_notice))
						.setView(view)
						.setMsg(getActivity().getString(
								R.string.operationDetails_whether_delete))
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										deleteRecord();
										recordJudge();
									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
									}
								}).show();

			}
			break;
		case R.id.save_button:// 保存录音
			// save_button.setEnabled(false);
			LayoutInflater factory = LayoutInflater.from(getActivity());
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(getActivity())
					.builder()
					.setTitle(getString(R.string.operationDetails_save_hint))
					.setView(view)
					.setMsg(getString(R.string.operationDetails_whether_save))
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									if (play_button.getTag().equals("false")) {// 先暂停再合成录音
										pauseRecorder();
									}
									stopRecord();
									recordJudge();
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {

									if (play_button.getTag().equals("false")) {// 先暂停再合成录音
										pauseRecorder();
									}
									// 删除录音片段
									for (int i = 0; i < mList.size(); i++) {
										File file = new File(mList.get(i));
										if (file.exists()) {
											file.delete();
										}
									}
									mList.clear();
									tv_record_time.setText("00:00");
									// 录音结束 、时间归零
									minute = 0;
									hour = 0;
									second = 0;
								}
							}).show();
			break;
		}
	}

	// 开始录音
	private void starRecord() {
		if (!isPause) {
			mList.clear();
		}
		// filename = path + "dangerous_" + Code + ".wav";
		filename = path + "/" + getTime() + ".wav";
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mRecorder.setOutputFile(filename);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (Exception e) {
			e.printStackTrace();
			UIHelper.ToastMessage(getActivity(),
					R.string.operationDetails_record_defeated);
		}
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	@Override
	public void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		viewPause();
	}

	public void viewPause() {
		// 如果退出时还在录音就先暂停录音
		if (play_button.getTag().equals("false")) {
			pauseRecorder();
		}
		if (mList.size() > 0) {
			stopRecord();
			recordJudge();
		}
		/*
		 * // 删除录音片段 for (int i = 0; i < mList.size(); i++) { File file = new
		 * File(mList.get(i)); if (file.exists()) { file.delete(); } }
		 * mList.clear(); tv_record_time.setText("00:00"); // 录音结束 、时间归零 minute
		 * = 0; hour = 0; second = 0; save_button.setEnabled(false);
		 */
	}

	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		recordJudge();
	}

	// 暫停録音
	private void pauseRecorder() {
		play_button.setTag("true");
		play_button.setBackgroundResource(R.drawable.play);
		iv_record_state.setImageResource(R.drawable.ic_record_stop);
		mRecorder.stop();
		mRecorder.release();
		timer.cancel();
		isPause = true;
		// 将录音片段加入列表
		mList.add(filename);
	}

	// 删除录音
	private void deleteRecord() {
		File file = new File(path + "/" + Code + ".wav");
		if (file.exists()) {
			file.delete();
			tv_record_item.setText("");
		}
	}

	// 完成録音
	private void stopRecord() {
		play_button.setTag("true");
		iv_record_state.setImageResource(R.drawable.ic_record_stop);
		play_button.setBackgroundResource(R.drawable.play);
		tv_record_time.setText("00:00");
		save_button.setEnabled(false);

		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
		isPause = false;

		timer.cancel();
		// 最后合成的音频文件
		filename = path + "/" + Code + ".wav";

		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		FileInputStream fileInputStream = null;
		try {
			for (int i = 0; i < mList.size(); i++) {
				File file = new File(mList.get(i));
				// 把因为暂停所录出的多段录音进行读取
				fileInputStream = new FileInputStream(file);
				byte[] mByte = new byte[fileInputStream.available()];
				int length = mByte.length;
				// 第一个录音文件的前六位是不需要删除的
				if (i == 0) {
					while (fileInputStream.read(mByte) != -1) {
						fileOutputStream.write(mByte, 0, length);
					}
				}
				// 之后的文件，去掉前六位
				else {
					while (fileInputStream.read(mByte) != -1) {

						fileOutputStream.write(mByte, 6, length - 6);
					}
				}
			}
		} catch (Exception e) {
			// 这里捕获流的IO异常，万一系统错误需要提示用户
			e.printStackTrace();
			UIHelper.ToastMessage(getActivity(),
					R.string.operationDetails_record_defeated_hint);

		} finally {
			try {
				// fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 录音结束 、时间归零
			minute = 0;
			hour = 0;
			second = 0;
		}
		// 不管合成是否成功、删除录音片段
		for (int i = 0; i < mList.size(); i++) {
			File file = new File(mList.get(i));
			if (file.exists()) {
				file.delete();
			}
		}
		mList.clear();
	}

	// 计时器异步更新界面
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			tv_record_time.setText(String.format("%1$02d:%2$02d", minute,
					second));
			super.handleMessage(msg);
		}
	};

	// 录音计时
	private void recordTime() {
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				second++;
				if (second >= 60) {
					second = 0;
					minute++;
				}
				if (minute >= 60) {
					minute = 0;
					hour++;
				}
				handler.sendEmptyMessage(1);
			}

		};
		timer = new Timer();
		timer.schedule(timerTask, 1000, 1000);
	}

	// 获得当前时间
	private String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH：mm：ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}

	// 判断之前有没有录音
	public void recordJudge() {
		File file = new File(path + "/" + Code + ".wav");
		if (!file.exists()) {
			tv_record_item.setText("");
		} else {
			tv_record_item.setText(Code + ".wav");
		}
	}
}
