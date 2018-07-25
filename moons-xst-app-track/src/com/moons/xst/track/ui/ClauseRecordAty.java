package com.moons.xst.track.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.moons.xst.buss.OperateHelper;
import com.moons.xst.buss.OperateResultHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.OperDetailResult;
import com.moons.xst.track.bean.OperMainResult;
import com.moons.xst.track.bean.Operate_Bill;
import com.moons.xst.track.bean.Operate_Detail_Bill;
import com.moons.xst.track.common.UIHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ClauseRecordAty extends BaseActivity implements OnClickListener {
	ImageButton home_head_Rebutton;
	TextView tv_content, tv_record_item, tv_record_time;
	ImageView iv_record_delete, iv_record_state;
	ImageButton play_button, save_button;
	Button btn_save;
	private RadioButton radiowireless, radiousb;
	// private OperateHelper operateHelper;
	private OperateResultHelper operateResultHelper;
	private Operate_Detail_Bill operate_bill;
	String State = "";// 操作状态
	String Explain = "";// 操作内容
	String operate_id = "";// 操作id
	String Code = "";// 操作编号
	String Order = "";// 操作序号

	private String path = AppConst.TwoTicketRecordPath();// 录音保存路径
	private String name = "";// 录音名

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clause_record);
		// operateHelper = new OperateHelper();
		operateResultHelper = new OperateResultHelper();
		Code = getIntent().getStringExtra("Code");
		Order = getIntent().getStringExtra("Order");
		operate_bill = OperateHelper.GetIntance().clauseDetailQuery(this, Code,
				Order);
		State = operate_bill.getOperate_Detail_Item_State() + "";
		Explain = operate_bill.getOperate_Detail_Item_Content();
		operate_id = operate_bill.getOperate_Detail_Item_ID();
		name = "OperaionBill_" + Code + ".wav";
		initview();
	}

	private void initview() {
		// 操作内容
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText(Explain);
		// 文件名
		tv_record_item = (TextView) findViewById(R.id.textview_record_item);
		recordJudge();
		tv_record_item.setOnClickListener(this);
		// 录音时间
		tv_record_time = (TextView) findViewById(R.id.textview_record_time);
		// 保存状态
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);

		// 删除
		iv_record_delete = (ImageView) findViewById(R.id.imageview_record_delete);
		iv_record_delete.setOnClickListener(this);
		// 录音状态
		iv_record_state = (ImageView) findViewById(R.id.imageview_record_state);
		// 播放 暂停
		play_button = (ImageButton) findViewById(R.id.play_button);
		play_button.setOnClickListener(this);
		// 保存录音
		save_button = (ImageButton) findViewById(R.id.save_button);
		save_button.setEnabled(false);
		save_button.setOnClickListener(this);
		// 操作状态
		radiowireless = (RadioButton) findViewById(R.id.radiowireless);
		radiousb = (RadioButton) findViewById(R.id.radiousb);
		if (State.equals("0")) {
			radiowireless.setChecked(true);
			clickSield();
		} else if (State.equals("1")) {
			radiousb.setChecked(true);
			clickSield();
		}
		// 点击返回
		home_head_Rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		home_head_Rebutton.setOnClickListener(this);

	}

	// 屏蔽点击事件
	public void clickSield() {
		radiowireless.setEnabled(false);
		radiousb.setEnabled(false);
		btn_save.setEnabled(false);
		iv_record_delete.setEnabled(false);
		play_button.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.imageview_record_delete:// 删除录音
			if (tv_record_item.getText().length() > 0) {
				LayoutInflater factory = LayoutInflater.from(this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(this)
						.builder()
						.setTitle(getString(R.string.plan_del_notice))
						.setView(view)
						.setMsg(getString(R.string.operationDetails_whether_delete))
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
		case R.id.btn_save:// 保存信息
			if (mList.size() > 0 || play_button.getTag().equals("false")) {
				UIHelper.ToastMessage(this, R.string.clause_must_save_record);
			} else {
				btn_save.setEnabled(false);
				if (radiowireless.isChecked()) {
					OperateHelper.GetIntance().updateOperateState(this,
							operate_id, "0", getTime());
				} else if (radiousb.isChecked()) {
					OperateHelper.GetIntance().updateOperateState(this,
							operate_id, "1", getTime());
				}
				// 如果后面还有操作项，条到下一个操作项
				if (OperateHelper.GetIntance().operateJudge(this, Code, Order)) {
					InsertResult(Code, Order, true);
					refresh();
				} else {
					InsertResult(Code, Order, false);
				}
				AppManager.getAppManager().finishActivity(ClauseRecordAty.this);
			}
			break;
		case R.id.textview_record_item:// 文件名
			if (tv_record_item.getText().toString().length() > 2) {
				list.clear();
				list.add(path + name);
				AudioPlayerDialog Audialog = new AudioPlayerDialog(this, path
						+ name, list);
				Audialog.show();
			}
			break;
		case R.id.play_button:// 开始暂停 录音
			save_button.setEnabled(true);
			if (play_button.getTag().equals("true")) {// 点击开始
				play_button.setTag("false");
				starRecord();
				recordTime();
				play_button.setBackgroundResource(R.drawable.pause);
				iv_record_state.setImageResource(R.drawable.ic_record_start);
			} else if (play_button.getTag().equals("false")) {// 点击暂停
				pauseRecorder();
			}
			break;
		case R.id.save_button:// 保存录音
			// save_button.setEnabled(false);
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(this)
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
		case R.id.home_head_Rebutton:// 点击返回
			AppManager.getAppManager().finishActivity(ClauseRecordAty.this);
			break;
		}
	}

	// 开始录音
	private void starRecord() {
		if (!isPause) {
			mList.clear();
		}
		// filename = path + "dangerous_" + Code + ".amr";
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
			UIHelper.ToastMessage(this,
					R.string.operationDetails_record_defeated);
		}
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	// 暫停録音
	private void pauseRecorder() {
		play_button.setTag("true");
		iv_record_state.setImageResource(R.drawable.ic_record_stop);
		play_button.setBackgroundResource(R.drawable.play);

		mRecorder.stop();
		mRecorder.release();
		timer.cancel();
		isPause = true;
		// 将录音片段加入列表
		mList.add(filename);
	}

	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		// 如果退出时还在录音就先暂停录音
		if (play_button.getTag().equals("false")) {
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
		save_button.setEnabled(false);
	}

	// 删除录音
	private void deleteRecord() {
		File file = new File(path + name);
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

		mRecorder.release();
		mRecorder = null;
		isPause = false;

		timer.cancel();
		// 最后合成的音频文件
		// filename = path + "/" + operate_id + ".amr";
		filename = path + name;
		File mFile = new File(filename);
		if (mFile.exists()) {// 之前有这名字的录音文件就在后面继续拼接
			File file2 = new File(path + Code + ".wav");
			mFile.renameTo(file2);
			mList.add(0, path + Code + ".wav");
		}

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
				fileInputStream.close();
			}

		} catch (Exception e) {
			// 这里捕获流的IO异常，万一系统错误需要提示用户
			e.printStackTrace();
			UIHelper.ToastMessage(this,
					R.string.operationDetails_record_defeated_hint);

		} finally {
			try {
				// fileOutputStream.flush();
				if (fileInputStream != null)
					fileInputStream.close();
				fileOutputStream.close();

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

	// 添加结果库isLast参数是当前操作项是不是最后一个 ,
	private void InsertResult(String code, String Order, Boolean isLast) {
		// operateHelper = new OperateHelper();
		// 添加OperDetailResult表到返回库
		Operate_Detail_Bill detail = OperateHelper.GetIntance()
				.clauseDetailQuery(this, code, Order);
		OperDetailResult detailResult = new OperDetailResult();
		detailResult.setOperate_Bill_Code(detail.getOperate_Bill_Code());
		detailResult.setOperate_Detail_Item_ID(Integer.valueOf(detail
				.getOperate_Detail_Item_ID()));
		detailResult.setOperate_Detail_Item_State(Integer.valueOf(detail
				.getOperate_Detail_Item_State()));
		detailResult.setOperate_Detail_Item_Exe_Time(detail
				.getOperate_Detail_Item_Exe_Time());
		operateResultHelper.InsertOperDetail(this, detailResult);

		// 添加mainResult表到放回库
		if (Order.equals("1")) {// 如果当前操作是第一条的话就添加一条数据到结果库
			// Operate_Bill main = operateHelper.aOperateQuery(this, Code);
			OperMainResult mainResult = new OperMainResult();
			mainResult.setOperate_Bill_Code(detail.getOperate_Bill_Code());
			mainResult.setOperate_Begin_Time(getTime());

			operateResultHelper.InsertOperMain(this, mainResult);
		}

		if (!isLast) {// 如果当前操作项是最后一条就保存结束时间为当前时间
			// 回调RESULT_OK直接返回的操作票页面
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			operateResultHelper.updateOperMain(this, code, getTime());
		}
	}

	// 获得当前时间
	private String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}

	// 判断之前有没有录音
	private void recordJudge() {
		File file = new File(path + name);
		if (!file.exists()) {
			tv_record_item.setText("");
		} else {
			tv_record_item.setText(name);
		}
	}

	// 跳到下一个操作项
	private void refresh() {
		int count = Integer.valueOf(Order).intValue();
		count += 1;
		/*
		 * //用OperateClauseAty实例跳转，为了OperateClauseAty能收到回调函数
		 * if(OperateClauseAty.instance()!=null){ Intent intent = new
		 * Intent(OperateClauseAty.instance(), ClauseRecordAty.class);
		 * intent.putExtra("Code", Code); intent.putExtra("Order", "" + count);
		 * OperateClauseAty.instance().startActivityForResult(intent,
		 * OperateClauseAty.REQCODE_OPERATE); }
		 */
		UIHelper.showClauseRecord(this, Code, "" + count);
	}
}
