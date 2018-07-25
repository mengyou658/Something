package com.moons.xst.track.communication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import com.google.gson.Gson;
import com.moons.xst.buss.OverhaulHelper;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.ResponseLogInfo;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.ResponseLogInfoHelper;
import com.moons.xst.track.common.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OverhaulDownLoad {

	private Context mContext;

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private static final int DOWN_FALL = 3;

	private static final int DIALOG_TYPE_INIT_FAIL = 1;// 标准化chucuo
	private static final int DIALOG_TYPE_FAIL_WS = 2;// web服务不可用
	private static final int DIALOG_TYPE_UPLOADING_FAIL = 3;// 上传失败

	private static OverhaulDownLoad OverhaulDownLoad;

	// 终止标记
	private boolean interceptFlag;

	// 下载对话框
	com.moons.xst.track.widget.AlertDialog Dialog;
	// 进度条
	private ProgressBar mProgress;
	// 显示下载数值
	private TextView mProgressText;
	// 查询动画
	private ProgressDialog mProDialog;
	// 错误对话框
	private com.moons.xst.track.widget.AlertDialog FailDialog;
	// 进度值
	private int progress;
	// 下载线程
	private Thread downLoadThread;

	// 返回的数据库url
	private String dbUrl = "";
	// 下载数据库文件保存路径
	private String savePath = "";
	// DB保存完整路径
	private String dbFilePath = "";
	// 临时下载文件路径
	private String tmpFilePath = "";
	// 下载文件大小
	private String dbFileSize;
	// 已下载文件大小
	private String tmpFileSize;

	Handler handler;

	public static OverhaulDownLoad getOverhaulDownLoad() {
		if (OverhaulDownLoad == null) {
			OverhaulDownLoad = new OverhaulDownLoad();
		}
		OverhaulDownLoad.interceptFlag = false;
		return OverhaulDownLoad;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + dbFileSize);
				break;
			case DOWN_OVER:
				Dialog.dismiss();
				((AppContext) mContext.getApplicationContext())
						.refreshOverhaul();
				break;
			case DOWN_NOSDCARD:
				Dialog.dismiss();
				Toast.makeText(
						mContext,
						mContext.getString(R.string.operateDownLoad_cannot_download_hint),
						3000).show();
				break;
			case DOWN_FALL:
				Dialog.dismiss();
				Toast.makeText(mContext,
						mContext.getString(R.string.download_state_downErr),
						3000).show();
				break;
			}

		};
	};

	// 上传handler
	Handler HDUploading = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mProDialog.setMessage(mContext
						.getString(R.string.download_state_encode));
				downloadInit();
				Toast.makeText(
						mContext,
						mContext.getString(R.string.cumm_cummdownload_uploadfinished),
						Toast.LENGTH_SHORT).show();
				/*
				 * UIHelper.ToastMessage(mContext, mContext
				 * .getString(R.string.cumm_cummdownload_uploadfinished));
				 */
			} else {
				if (msg.what == -1) {
					showFailDialog(DIALOG_TYPE_FAIL_WS, "");
				} else
					showFailDialog(DIALOG_TYPE_UPLOADING_FAIL,
							msg.obj.toString());
			}
		}
	};

	/**
	 * 上传检修票
	 * 
	 * **/
	public void Overhauluploading() {
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				if (!WebserviceFactory.checkWS()) {
					msg.what = -1;
					HDUploading.sendMessage(msg);
					return;
				}
				try {
					// 上传检修计划
					ReturnResultInfo detail = OverhaulHelper.getInstance()
							.JXuploading(mContext);
					if (!detail.getResult_YN()) {// 上传失败
						msg.what = 0;
						String error=StringUtils.isEmpty(detail
								.getError_Message_TX()) ? "" : detail
								.getError_Message_TX();
						msg.obj = error;
						// 插入操作日志
						OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI +
								AppConst.COMMTYPE_OVERHAUL_UPLOADING,
								AppConst.LOGSTATUS_ERROR,
								error);
					} else {// 上传成功
						msg.what = 1;
						String DataBaseName = AppConst.XSTDBPath()
								+ "Overhaul.sdf";
						File file = new File(DataBaseName);
						file.delete();
						// 插入操作日志
						OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI +
								AppConst.COMMTYPE_OVERHAUL_UPLOADING,
								AppConst.LOGSTATUS_NORMAL,
								"");
					}
				} catch (Exception e) {
					msg.what = 0;
					msg.obj = e.getMessage();
					e.printStackTrace();
					// 插入操作日志
					OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI +
							AppConst.COMMTYPE_OVERHAUL_UPLOADING,
							AppConst.LOGSTATUS_ERROR,
							e.getMessage());
				}
				HDUploading.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 下载检修票
	 * 
	 * @param isUploading
	 *            是否需要上传计划
	 * **/
	public void encodeAndDownloadOverhaul(final Context context,
			boolean isUploading) {
		this.mContext = context;
		if (mProDialog == null) {
			mProDialog = ProgressDialog.show(mContext, null,
					mContext.getString(R.string.download_state_encode), true,
					true);
			mProDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO 自动生成的方法存根
					mProDialog.dismiss();
					mProDialog = null;
				}
			});
			mProDialog.setCanceledOnTouchOutside(false);
			mProDialog.setCancelable(false);
		} else if (mProDialog.isShowing()
				|| (FailDialog != null && FailDialog.isShowing()))
			return;

		handler = new Handler() {
			public void handleMessage(Message msg) {
				// 进度条对话框不显示 - 标准化信息也不显示
				if (mProDialog != null && !mProDialog.isShowing()) {
					return;
				}
				// 关闭并释放进度条对话框
				if (mProDialog != null) {
					mProDialog.dismiss();
					mProDialog = null;
				}
				// 开始下载检修数据
				if (msg.what == 1) {
					dbUrl = msg.obj.toString();
					showDownloadDialog();
				} else if (msg.what == -2) {
					String msgString = String.valueOf(msg.obj);
					LayoutInflater factory = LayoutInflater.from(mContext);
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					new com.moons.xst.track.widget.AlertDialog(mContext)
							.builder()
							.setTitle(mContext.getString(R.string.tips))
							.setView(view)
							.setMsg(msgString)
							.setPositiveButton(mContext.getString(R.string.sure),
								new OnClickListener() {								
								@Override
								public void onClick(View v) {
								}
					}).setCanceledOnTouchOutside(false).setCancelable(false).show();
				} else {
					if (msg.what == -1) {
						showFailDialog(DIALOG_TYPE_FAIL_WS, "");
					} else {
						showFailDialog(DIALOG_TYPE_INIT_FAIL,
								msg.obj.toString());
					}
				}
			}
		};

		if (isUploading) {
			mProDialog.setMessage(mContext
					.getString(R.string.overhaul_download_underway_uploading));
			Overhauluploading();
		} else {
			mProDialog.setMessage(mContext
					.getString(R.string.download_state_encode));
			downloadInit();
		}

	}

	// 检修下载标准化
	private void downloadInit() {
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				if (!WebserviceFactory.checkWS()) {
					msg.what = -1;
					msg.obj = mContext
							.getString(R.string.setting_sys_aboutus_checkupdata_webaddresscanotuse);
					handler.sendMessage(msg);
					return;
				} else {
					try {
						DownLoad download = new DownLoad(mContext);
						// 进行授权个数的判断
						ReturnResultInfo returnInfo = 
								download.APClientInfo(AppContext.getAppVersion(mContext), 
										mContext, AppContext.GetxjqCD());
						if (!returnInfo.getResult_YN()) {
							msg.what = -2;
							msg.obj = returnInfo.getError_Message_TX();
						} else {
							// 下载标准化操作
							ReturnResultInfo detail = WebserviceFactory
									.encodeOverhaulInfo(mContext);
							if (detail.getResult_YN()) {
								msg.what = 1;
								msg.obj = detail.getResult_Content_TX();
							} else {
								msg.what = 0;
								msg.obj = StringUtils.isEmpty(detail
										.getError_Message_TX()) ? "" : detail
										.getError_Message_TX();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	/**
	 * 显示下载失败对话框
	 */
	private void showFailDialog(int dialogType, String prompt) {
		try {
			if (FailDialog != null) {
				// 关闭并释放之前的对话框
				FailDialog.dismiss();
				FailDialog = null;
			}
			if (mProDialog != null) {
				mProDialog.dismiss();
				mProDialog = null;
			}
			if (mContext == null)
				return;
			String message = "";
			if (dialogType == DIALOG_TYPE_INIT_FAIL) {// 标准化出错
				message = mContext
						.getString(R.string.operateDownLoad_standardization_mistake)
						+ "\n" + prompt;
			} else if (dialogType == DIALOG_TYPE_FAIL_WS) {// web服务不可用
				message = mContext
						.getString(R.string.setting_sys_aboutus_checkupdata_webaddresscanotuse);
			} else if (dialogType == DIALOG_TYPE_UPLOADING_FAIL) {// 上传出错
				message = mContext
						.getString(R.string.overhaul_download_uploading_defeated)
						+ "\n" + prompt;
			}

			LayoutInflater factory = LayoutInflater.from(mContext);
			final View view = factory.inflate(R.layout.textview_layout, null);
			FailDialog = new com.moons.xst.track.widget.AlertDialog(mContext)
					.builder()
					.setTitle(mContext.getString(R.string.datasync_msg))
					.setView(view)
					.setMsg(message)
					.setPositiveButton(mContext.getString(R.string.sure),
							new android.view.View.OnClickListener() {
								@Override
								public void onClick(View v) {

								}
							});
			FailDialog.show();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 显示下载对话框
	 */
	public void showDownloadDialog() {
		LayoutInflater factory = LayoutInflater.from(mContext);
		final View view = factory.inflate(R.layout.progressbar_layout, null);
		mProgressText = (TextView) view.findViewById(R.id.update_progress_text);
		mProgress = (ProgressBar) view.findViewById(R.id.update_progress);

		Dialog = new com.moons.xst.track.widget.AlertDialog(mContext)
				.builder()
				.setTitle(mContext.getString(R.string.overhaul_download_underway_download))
				.setView(view)
				.setPositiveButton(mContext.getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								interceptFlag = true;
							}
						}).setCancelable(false)
				.setCanceledOnTouchOutside(false);
		Dialog.show();
		
		if (!((Activity) mContext).isFinishing()) {
			Dialog.show();
			downloadDB();
		}
	}

	/**
	 * 下载数据库
	 * 
	 */
	private void downloadDB() {
		downLoadThread = new Thread(mdownDBRunnable);
		downLoadThread.start();
	}

	private Runnable mdownDBRunnable = new Runnable() {
		@Override
		public void run() {
			try {

				String dbName = "Overhaul.sdf";
				String tmpDB = "Overhaul.tmp";
				// 判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = AppConst.XSTDBPath();
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					dbFilePath = savePath + dbName;
					tmpFilePath = savePath + tmpDB;
				}

				// 没有挂载SD卡，无法下载文件
				if (dbFilePath == null || dbFilePath == "") {
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}

				File dbFile = new File(dbFilePath);
				if (dbFile.exists())
					dbFile.delete();

				// 输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);

				URL url = new URL(dbUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// conn.connect();
				conn.setRequestProperty("Accept-Encoding", "identity");
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				// 显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				// 进度条下面显示的总文件大小
				dbFileSize = df.format((float) length / 1024 / 1024) + "MB";

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					// 进度条下面显示的当前下载文件大小
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					// 当前进度值
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成 - 将临时下载文件转成APK文件
						if (tmpFile.renameTo(dbFile)) {
							
							mHandler.sendEmptyMessage(DOWN_OVER);
							
							// 插入操作日志
							OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI 
									+ AppConst.COMMTYPE_OVERHAUL_DOWNLOAD,
									AppConst.LOGSTATUS_NORMAL,
									"");
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载

				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(DOWN_FALL);
				
				// 插入操作日志
				OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI 
						+ AppConst.COMMTYPE_OVERHAUL_DOWNLOAD,
						AppConst.LOGSTATUS_ERROR,
						e.getMessage());
			}
		}
	};

	public void destroy() {
		if (mProDialog != null)
			mProDialog.dismiss();
		if (Dialog != null)
			Dialog.dismiss();
	}
}
