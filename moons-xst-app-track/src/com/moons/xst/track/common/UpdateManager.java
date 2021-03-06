package com.moons.xst.track.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.pano.platform.http.r;
import com.google.gson.Gson;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.api.ApiClient;
import com.moons.xst.track.bean.ResponseLogInfo;
import com.moons.xst.track.bean.Update;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.ui.CommDJPCDownload;
import com.moons.xst.track.ui.Main_Page;

/**
 * 应用程序更新工具包
 * 
 * @author gaojun
 * @version 1.1
 * @created 2014-9-29
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static final int DIALOG_TYPE_LATEST = 0;
	private static final int DIALOG_TYPE_FAIL = 1;
	private static final int DIALOG_TYPE_FAIL_WS = 2;

	private static UpdateManager updateManager;

	private Context mContext;
	// 通知对话框
	private Dialog noticeDialog;
	// 下载对话框
	com.moons.xst.track.widget.AlertDialog Dialog;
	// '已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
	// 进度条
	private ProgressBar mProgress;
	// 显示下载数值
	private TextView mProgressText;
	// 查询动画
	private ProgressDialog mProDialog;
	// 进度值
	private int progress;
	// 下载线程
	private Thread downLoadThread;
	// 终止标记
	private boolean interceptFlag;
	// 提示语
	private String updateMsg = "";
	// 返回的安装包url
	private String apkUrl = "";
	// 下载包保存路径
	private String savePath = "";
	// apk保存完整路径
	private String apkFilePath = "";
	// 临时下载文件路径
	private String tmpFilePath = "";
	// 下载文件大小
	private String apkFileSize;
	// 已下载文件大小
	private String tmpFileSize;

	private String curVersionName = "";
	private int curVersionCode;
	private Update mUpdate;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				Dialog.dismiss();
				installApk();

				break;
			case DOWN_NOSDCARD:
				Dialog.dismiss();
				Toast.makeText(mContext,
						R.string.setting_sys_aboutus_checkupdata_nosdprompt,
						3000).show();
				break;
			}

		};
	};

	public static UpdateManager getUpdateManager() {
		if (updateManager == null) {
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}

	public void homeDownloadApk(Context context, String downloadUrl) {
		this.mContext = context;
		apkUrl = downloadUrl;
		getCurrentVersion();
		showDownloadDialog();
	}

	/**
	 * 检查App更新
	 * 
	 * @param context
	 * @param isShowMsg
	 *            是否显示提示消息
	 */
	public void checkAppUpdate(final Context context, final boolean isShowMsg) {
		this.mContext = context;
		getCurrentVersion();
		if (isShowMsg) {
			if (mProDialog == null) {
				mProDialog = ProgressDialog
						.show(mContext,
								null,
								context.getString(R.string.setting_sys_aboutus_checkupdata_diapromptinfo),
								true, true);
				mProDialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO 自动生成的方法存根
						mProDialog.dismiss();
						mProDialog = null;
					}
				});
			} else if (mProDialog.isShowing()
					|| (latestOrFailDialog != null && latestOrFailDialog
							.isShowing()))
				return;
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				// 进度条对话框不显示 - 检测结果也不显示
				if (mProDialog != null && !mProDialog.isShowing()) {
					return;
				}
				// 关闭并释放释放进度条对话框
				if (isShowMsg && mProDialog != null) {
					mProDialog.dismiss();
					mProDialog = null;
				}
				// 显示检测结果
				if (msg.what == 1) {
					mUpdate = (Update) msg.obj;
					if (mUpdate != null) {
						if (curVersionCode < mUpdate.getVersionCode()
								&& mUpdate.getVersionCode() != 0) {
							apkUrl = mUpdate.getDownloadUrl();
							String updateMsgBeforeReplace = mUpdate
									.getUpdateLog();
							updateMsg = updateMsgBeforeReplace.replace("|", "");
							showNoticeDialog();
						} else if (isShowMsg) {
							showLatestOrFailDialog(DIALOG_TYPE_LATEST);
						}
					}
				} else if (isShowMsg) {
					if (msg.what == -1) {
						showLatestOrFailDialog(DIALOG_TYPE_FAIL_WS);
					} else
						showLatestOrFailDialog(DIALOG_TYPE_FAIL);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				if (!WebserviceFactory.checkWS()) {
					msg.what = -1;
					msg.obj = context
							.getString(R.string.setting_sys_aboutus_checkupdata_webaddresscanotuse);
					handler.sendMessage(msg);
					return;
				}
				try {
					Update update = ApiClient
							.checkVersion((AppContext) mContext
									.getApplicationContext());
					msg.what = 1;
					msg.obj = update;
				} catch (AppException e) {
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	private void showLatestOrFailDialog(int dialogType) {
		try {
			if (mContext == null)
				return;
			LayoutInflater factory = LayoutInflater.from(mContext);
			final View view = factory.inflate(R.layout.textview_layout, null);
			String msg = "";
			if (dialogType == DIALOG_TYPE_LATEST) {
				msg = mContext
						.getString(R.string.setting_sys_aboutus_checkupdata_diamsg_islast);
			} else if (dialogType == DIALOG_TYPE_FAIL) {
				msg = mContext
						.getString(R.string.setting_sys_aboutus_checkupdata_diamsg_canotobtainupdateinfo);
			} else if (dialogType == DIALOG_TYPE_FAIL_WS) {
				msg = mContext
						.getString(R.string.setting_sys_aboutus_checkupdata_webaddresscanotuse);
			}
			new com.moons.xst.track.widget.AlertDialog(mContext).builder()
					.setTitle(mContext.getString(R.string.system_notice))
					.setView(view).setMsg(msg)
					.setPositiveButton(mContext.getString(R.string.sure), null)
					.show();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion() {
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog() {
		LayoutInflater factory = LayoutInflater.from(mContext);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(mContext)
				.builder()
				.setTitle(mContext.getString(R.string.software_version_update))
				.setView(view)
				.setMsg(updateMsg)
				.setPositiveButton(mContext.getString(R.string.update_now),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO 自动生成的方法存根
								showDownloadDialog();
							}
						})
				.setNegativeButton(
						mContext.getString(R.string.update_talklater),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
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
				.setTitle(
						mContext.getString(R.string.setting_sys_aboutus_checkupdata_diamsg_prompttoupdate_isupdating))
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

		downloadApk();
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Dialog.dismiss();
				UIHelper.ToastMessage(
						mContext,
						R.string.setting_sys_aboutus_checkupdata_diamsg_prompttoupdate_updatefail);
				break;
			}
			super.handleMessage(msg);
		}
	};
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				if (apkUrl == "") {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
					return;
				}
				String apkName = "moons-xst-app-track.apk";
				String tmpApk = "moons-xst-app-track.tmp";
				// 判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = AppConst.XSTBasePath();
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}

				// 没有挂载SD卡，无法下载文件
				if (apkFilePath == null || apkFilePath == "") {
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}

				File ApkFile = new File(apkFilePath);
				if (ApkFile.exists())
					ApkFile.delete();

				// 输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);

				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// conn.connect();
				conn.setRequestProperty("Accept-Encoding", "identity");
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				// 显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				// 进度条下面显示的总文件大小
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

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
						if (tmpFile.renameTo(ApkFile)) {
							// 通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载

				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();

				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Editor editor = ((Main_Page) mContext).getPreferences().edit();
		editor.putBoolean("isFirstStart", true);
		editor.commit();
		
		// 插入操作日志
		OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI + 
				AppConst.COMMTYPE_APKUPGRADE,
				AppConst.LOGSTATUS_NORMAL,
				"");
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		// mContext.startActivity(i);
		((FragmentActivity) mContext).startActivityForResult(i, 0);
	}

	
}
