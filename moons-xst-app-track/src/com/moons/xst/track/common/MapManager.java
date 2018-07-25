package com.moons.xst.track.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.BaiduMapInfo;
import com.moons.xst.track.communication.WebserviceFactory;

public class MapManager {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static final int DIALOG_TYPE_LATEST = 0;
	private static final int DIALOG_TYPE_FAIL = 1;
	private static final int DIALOG_TYPE_FAIL_WS = 2;

	private Context mContext;
	// 查询动画
	private ProgressDialog mProDialog;
	// '已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
	// 终止标记
	private boolean interceptFlag;
	// 下载对话框
	private Dialog downloadDialog;
	// 进度条
	private ProgressBar mProgress;
	// 显示下载数值
	private TextView mProgressText;
	// 下载文件大小
	private String apkFileSize;
	// 已下载文件大小
	private String tmpFileSize;
	// 进度值
	private int progress;
	// 下载线程
	private Thread downLoadThread;
	private String downloadURL;
	private int cityID;
	private boolean isLastMap;
	private String versionXML;
	List<BaiduMapInfo> updateMapList;

	private static MapManager mapManager;

	public static MapManager getMapManager() {
		if (mapManager == null) {
			mapManager = new MapManager();
		}
		mapManager.interceptFlag = false;
		return mapManager;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				// installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", 3000).show();
				break;
			case -1:
				downloadDialog.dismiss();
				Toast.makeText(mContext, String.valueOf(msg.obj), 3000).show();
				break;
			}
		};
	};

	/**
	 * 检查地图更新
	 * 
	 * @param context
	 * @param isShowMsg
	 *            是否显示提示消息
	 */
	public void UpdateBaiduMap(Context context, final int cityID,
			final boolean isShowMsg) {
		this.mContext = context;
		getCurrentVersion();
		if (isShowMsg) {
			if (mProDialog == null) {
				mProDialog = ProgressDialog.show(mContext, null, "正在检测，请稍等...",
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
					BaiduMapInfo[] baiduMapInfos = (BaiduMapInfo[]) msg.obj;
					if (baiduMapInfos != null && baiduMapInfos.length == 2) {
						BaiduMapInfo[] currentMapInfos = getCurrentVersion();
						CompareMapVersion(currentMapInfos, baiduMapInfos);
					} else if (isShowMsg) {
						// showLatestOrFailDialog(DIALOG_TYPE_LATEST);
						showLatestOrFailDialog(DIALOG_TYPE_FAIL);
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
					msg.obj = "WEB服务不可用！";
					handler.sendMessage(msg);
					return;
				}
				try {
					BaiduMapInfo[] baiduMapInfos = checkVersion(
							(AppContext) mContext.getApplicationContext(),
							cityID);
					msg.what = 1;
					msg.obj = baiduMapInfos;
				} catch (AppException e) {
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	private BaiduMapInfo[] getCurrentVersion() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.XSTBasePath() + AppConst.BaiduMapVersionXML;
			File f = new File(path);
			if (!f.exists()) {
				return null;
			}
			Document doc = db.parse(f);
			doc.normalize();
			return BaiduMapInfo.parse(doc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 检查版本更新
	 * 
	 * @param url
	 * @return
	 */
	public BaiduMapInfo[] checkVersion(AppContext appContext, int cityID)
			throws AppException {
		try {
			String xml = WebserviceFactory
					.getCityMapVersion(appContext, cityID);
			versionXML = xml;
			return BaiduMapInfo.parse(xml);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	private void showLatestOrFailDialog(int dialogType) {
		try {

			if (latestOrFailDialog != null) {
				// 关闭并释放之前的对话框
				latestOrFailDialog.dismiss();
				latestOrFailDialog = null;
			}
			if (mContext == null)
				return;
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle("系统提示");
			if (dialogType == DIALOG_TYPE_LATEST) {
				builder.setMessage("当前地图已经是最新版本");
			} else if (dialogType == DIALOG_TYPE_FAIL) {
				builder.setMessage("未获取到当前城市离线地图包，请检查该地图包是否已发布");
			} else if (dialogType == DIALOG_TYPE_FAIL_WS) {
				builder.setMessage("WEB服务不可用");
			}
			builder.setPositiveButton("确定", null);
			latestOrFailDialog = builder.create();
			latestOrFailDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void CompareMapVersion(BaiduMapInfo[] currentInfos,
			BaiduMapInfo[] newInfos) {
		updateMapList = new ArrayList<BaiduMapInfo>();
		boolean onemapisnew = false;
		for (int i = 0; i < newInfos.length; i++) {
			BaiduMapInfo newmap = newInfos[i];
			if (currentInfos != null) {
				for (int j = 0; j < currentInfos.length; j++) {
					BaiduMapInfo currentmap = currentInfos[j];
					if (currentmap.getCityID() == newmap.getCityID()) {
						if (currentmap.getVersion().equalsIgnoreCase(
								newmap.getVersion())) {
							onemapisnew = true;
							break;
						}
					} else {
						continue;
					}
				}
			}
			if (!onemapisnew) {
				updateMapList.add(newmap);
				// downloadURL = newmap.getDownloadUrl();
				// cityID = newmap.getCityID();

				// showDownloadDialog();
			}
			onemapisnew = false;
		}
		if (updateMapList.size() == 0) {
			showLatestOrFailDialog(DIALOG_TYPE_LATEST);
		} else {
			showDownloadDialog();
		}
	}

	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在下载新版本");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();

		downloadMap();
	}

	private void downloadMap() {
		downLoadThread = new Thread(mdownMapRunnable);
		downLoadThread.start();
	}

	/**
	 * 下载map
	 * 
	 * @param url
	 */
	private Runnable mdownMapRunnable = new Runnable() {
		@Override
		public void run() {
			isLastMap = false;
			for (int i = 0; i < updateMapList.size(); i++) {
				if (i == updateMapList.size() - 1) {
					isLastMap = true;
				}
				BaiduMapInfo info = updateMapList.get(i);
				cityID = info.getCityID();
				downloadURL = info.getDownloadUrl();
				downLoad();
			}
		}
	};

	private void downLoad() {
		try {
			String tempPath = "";
			String mapPath = "";
			String mapName = downloadURL
					.substring(downloadURL.lastIndexOf("/") + 1);
			String tmpName = mapName.split("\\.")[0] + ".tmp";
			String saveName = "";
			if (cityID == 1) {
				saveName = mapName.split("_")[0] + ".dat";
			} else {
				saveName = mapName.split("_")[0] + "_" + mapName.split("_")[1]
						+ ".dat";
			}
			// 判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				String savePath = AppConst.XSTBasePath() + "/Map/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				tempPath = savePath + tmpName;
				mapPath = AppConst.BaiduMapOffLineDatPath() + saveName;
			}

			// 没有挂载SD卡，无法下载文件
			if (mapPath == null || mapPath == "") {
				mHandler.sendEmptyMessage(DOWN_NOSDCARD);
				return;
			}

			File mapFile = new File(mapPath);

			// 是否已下载更新文件
			/*
			 * if(mapFile.exists()){ downloadDialog.dismiss(); installApk();
			 * return; }
			 */

			// 输出临时下载文件
			File tmpFile = new File(tempPath);
			FileOutputStream fos = new FileOutputStream(tmpFile);

			URL url = new URL(downloadURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
					if (tmpFile.renameTo(mapFile)) {
						copyFile(mapFile, saveName);
						if (isLastMap) {
							// 通知安装
							saveXML();
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
					}
					break;
				}
				fos.write(buf, 0, numread);
			} while (!interceptFlag);// 点击取消就停止下载

			fos.close();
			is.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
	}

	private void saveXML() {
		try {
			FileWriter fw = new FileWriter(AppConst.XSTBasePath()
					+ AppConst.BaiduMapVersionXML);
			fw.write(versionXML);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void copyFile(File sourceFile, String saveName) {
		String targetPath = AppConst.BaiduMapOffLineDatPath_Ex() + saveName;
		File targetFile = new File(targetPath);
		try {
			copyFile(sourceFile, targetFile);
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}
	}

	// 复制文件
	private void copyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
}
