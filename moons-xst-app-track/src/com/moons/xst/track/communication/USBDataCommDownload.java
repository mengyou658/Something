package com.moons.xst.track.communication;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.dao.DataTransHelper;
import com.moons.xst.track.ui.CommUSBTcpConnect;
import com.moons.xst.track.ui.Main_Page;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.ProgressButton;

public class USBDataCommDownload {
	private Context mContext;
	private AppContext appContext;// 全局Context
	// 透明的dialog(主要用于屏蔽屏幕的所有点击事件和返回事件)
	private LoadingDialog loading;// 加载中dialog
	private LinearLayout ll_comm_hint;
	private ProgressButton progress_button;
	
	// 是否真正同步
	private boolean isUnderwayComm = false;
	// 版本号
	private String appVersion = "";
	DownLoad downLoad;

	private ArrayList<String> uploadResultLineIDs = new ArrayList<String>();
	private static USBDataCommDownload USBDataCommDownload;
	private String[] objStr = new String[2];

	public static USBDataCommDownload getUSBDataCommDownload() {
		if (USBDataCommDownload == null) {
			USBDataCommDownload = new USBDataCommDownload();
		}
		return USBDataCommDownload;
	}

	// 刷新进度条
	public void RefreshCommProgBar() {
		int precent = (int) (((double) underwayComm / (double) commLineSum) * 100.0);
		progress_button.setText(precent + "%");
		progress_button.setProgress(precent);
		underwayComm++;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -1 && msg.obj != null) {// 同步失败
				isUnderwayComm = false;
				addCommHint(msg.obj.toString(), true, "true");
				progress_button.setText(mContext
						.getString(R.string.drawleft_btn_syncdata));
				if (loading != null) {
					loading.dismiss();
				}
			}
			if (msg.arg1 == 1) {// usb回传信息
				String info = msg.obj.toString();
				String[] tempList = info.split("@");
				String isSuc = "false";
				String cmd = "";
				String data = "";
				String time = "";
				if (tempList.length >= 1)
					isSuc = tempList[0];
				if (tempList.length >= 2)
					cmd = tempList[1];
				if (tempList.length >= 3)
					data = tempList[2];
				if (tempList.length >= 4) {
					time = tempList[3];
				}
				if (cmd.equals("pc-uploaddataend")) {// 单条上传成功
					RefreshCommProgBar();
				} else if (cmd.equals("pc-initdataend")) {// 初始化成功
					addCommHint(
							mContext.getString(R.string.drawleft_msg_loadlineinfosuccess),
							false, "true");
					loadCommdate();
					RefreshCommProgBar();
					AppContext.SetDownLoadYN(true);
					// loading消失的时候触发
					loading.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							// TODO 自动生成的方法存根
							refreshLogin();
						}
					});
					// 清空dao
					appContext.refreshDao();
					// 数据同步
					DataSynchronous();
				} else if (cmd.equals("pc-downloadplanend")) {// 下载一条路线完成
					RefreshCommProgBar();
					addCommHint(mContext.getString(
							R.string.drawleft_msg_downloadsucess,
							data), false, "true");
					downloadList.remove(0);
					DownLoadAllData();
				}
			}

			if (msg.what == 2) {// 同步提示信息
				addCommHint(((String[]) (msg.obj))[0].toString(), false,
						((String[]) (msg.obj))[1].toString());
			}
			if (msg.what == 3) {// 上传完成
				tcpConnect.SendUpLoadEnd();
				if (downloadSum > 0) {
					addCommHint(mContext.getString(
							R.string.drawleft_msg_downloadcount, downloadSum),
							false, "false");
				}
				DownLoadAllData();
			}
			// 数据同步完成
			if (msg.what == 100) {
				addCommHint(msg.obj.toString(), false, "false");
				progress_button.setText(mContext
						.getString(R.string.drawleft_btn_syncdata));
				if (loading != null) {
					loading.dismiss();
				}
			}
			// 断开连接
			if (msg.what == -2 && isUnderwayComm) {
				// 关闭socket
				CloseSocket();
				isUnderwayComm = false;
				addCommHint(
						mContext.getString(R.string.cumm_usb_connectdisconnect_hint),
						true, "true");
				progress_button.setText(mContext
						.getString(R.string.drawleft_btn_syncdata));
				if (loading != null) {
					loading.dismiss();
				}
			}
		}
	};

	// 添加通信提示信息
	private void addCommHint(String hint, boolean isError, String drawableYn) {
		TextView tv = new TextView(mContext);
		if (StringUtils.toBool(drawableYn)) {
			Drawable drawable = !isError ? mContext.getResources().getDrawable(
					R.drawable.commu_success) : mContext.getResources()
					.getDrawable(R.drawable.commu_fail);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			tv.setCompoundDrawables(null, null, drawable, null);
		}
		tv.setText(hint);
		tv.setSingleLine();
		if (isError) {
			tv.setTextColor(mContext.getResources().getColor(R.color.red));
		} else {
			tv.setTextColor(mContext.getResources().getColor(R.color.black));
		}

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置textview的左上右下的边距
		params.setMargins(5, 5, 0, 0);
		tv.setLayoutParams(params);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		ll_comm_hint.addView(tv, 0);
	}

	// 主页数据同步入口
	public void dataCommEntrance(Context context, LinearLayout ll_hint,
			ProgressButton progress) {
		if (isUnderwayComm) {
			return;
		}
		isUnderwayComm = true;
		mContext = context;
		downLoad = new DownLoad(mContext);
		appContext = (AppContext) context.getApplicationContext();
		ll_comm_hint = ll_hint;
		ll_comm_hint.removeAllViews();
		progress_button = progress;
		progress_button.setProgress(0);
		appVersion = AppContext.getAppVersion(context);

		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(mContext);
			loading.setLoadText(mContext
					.getString(R.string.drawleft_msg_syncing));
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		progress_button.setText("0%");
		addCommHint(
				mContext.getString(R.string.drawleft_msg_startloadlineinfo),
				false, "false");
		connectSocket();
	}

	// 需要上传和下载的路线数量
	private int commLineSum = 0;
	// 当前正在同步第几条路线
	private int underwayComm = 1;
	// 需要上传的路线数量
	private int uploadingSum = 0;
	// 需要下载的路线数量
	private int downloadSum = 0;

	List<DJLine> downloadList = new ArrayList<DJLine>();

	private void loadCommdate() {
		// 获取需要下载的路线
		downloadList.clear();
		List<DJLine> list = AppContext.CommDJLineBuffer;
		for (int i = 0; i < list.size(); i++) {
			DJLine _djLine = list.get(i);
			if (_djLine.getneedUpdate()) {
				downloadList.add(_djLine);
			}
		}
		commLineSum = downloadList.size();
		// 需要下载的路线数量
		downloadSum = downloadList.size();
		underwayComm = 1;
		if (AppContext.resultDataFileListBuffer != null
				&& AppContext.resultDataFileListBuffer.size() > 0) {
			for (DJLine _djLine : AppContext.resultDataFileListBuffer) {
				File _file = new File(_djLine.getResultDBPath());
				// 判断有没有结果库，有的话上传
				if (_file.exists()) {
					commLineSum++;
				}
			}
		}
		// 需上传的路线数量
		uploadingSum = commLineSum - downloadSum;
		// 加上加载中这一步
		commLineSum++;
	}

	// 数据同步
	private void DataSynchronous() {
		uploadResultLineIDs.clear();
		if (AppContext.resultDataFileListBuffer != null
				&& AppContext.resultDataFileListBuffer.size() > 0) {
			for (DJLine djLine : AppContext.resultDataFileListBuffer) {
				int lineID = djLine.getLineID();
				String DataBaseName = AppConst.CurrentResultPath(lineID)
						+ AppConst.ResultDBName(lineID);
				File file = new File(DataBaseName);
				if (file.exists()) {
					uploadResultLineIDs.add(String.valueOf(lineID));
				}
			}

		}
		if (uploadResultLineIDs != null && uploadResultLineIDs.size() > 0) {// 上传数据
			addCommHint(mContext.getString(
					R.string.drawleft_msg_uploadcount, uploadingSum),
					false, "false");
			upLoadResultData();
		} else {// 下载路线
			if (downloadSum > 0) {
				addCommHint(mContext.getString(
						R.string.drawleft_msg_downloadcount, downloadSum),
						false, "false");
			}
			DownLoadAllData();
		}
	}

	/*
	 * 上传制定路线结果
	 */
	private void upLoadResultData() {
		new Thread() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < uploadResultLineIDs.size(); i++) {
						String lineIDStr = uploadResultLineIDs.get(i);
						int lineID = Integer.valueOf(lineIDStr);
						String DataBaseName = AppConst
								.CurrentResultPath(lineID)
								+ AppConst.ResultDBName(lineID);
						File _file = new File(DataBaseName);
						boolean isUpPlan = true;
						if (isUpPlan) {
							String planFileName = AppConst.XSTDBPath()
									+ File.separator
									+ AppConst.PlanDBName(lineID);
							File planFile = new File(planFileName);
							if (planFile.exists()) {
								String resultPathString = _file.getParent();
								String sPlanFileNameString = resultPathString
										+ File.separator
										+ AppConst.PlanDBName(lineID);
								FileUtils.copyFile(planFileName,
										sPlanFileNameString);
							}
						}
						// 开始压缩路线
						Message _msg = Message.obtain();
						_msg.what = 2;
						objStr[0] = mContext.getString(
								R.string.cumm_cummdownload_iszipfile, lineID);
						objStr[1] = "false";
						_msg.obj = objStr;
						handler.sendMessage(_msg);
						ZipUtils.zipFolder(_file.getParent(),
								AppConst.XSTZipResultPath() + lineID);
						// 开始上传路线
						_msg = Message.obtain();
						_msg.what = 2;
						objStr[0] = mContext
								.getString(
										R.string.cumm_cummdownload_isuploadfile,
										lineID);
						objStr[1] = "false";
						_msg.obj = objStr;
						handler.sendMessage(_msg);
						Thread.sleep(500);
						String pdaFilePathString = AppConst.XSTZipResultPath()
								+ lineIDStr;
						// 这里暂时传空，后面需要的时候再改
						tcpConnect.UpLoadLineData(null, pdaFilePathString);
						if (lineID == 0) {
							// 上传完临时数据后，有视频缩略图的话，删除缩略图
							File file = new File(
									AppConst.XSTTempThumbnailImagePath());
							if (file.list().length > 0) {
							}
							File[] files = file.listFiles();
							for (File f : files) {
								f.delete();
							}
						}
						// 清空该路线下文件夹
						String path = AppConst.XSTResultPath() + lineID;
						FileUtils.clearFileWithPath(path);

						_msg = Message.obtain();
						_msg.what = 2;
						objStr[0] = mContext.getString(
								R.string.cumm_cummdownload_uploadfilesucced,
								lineID);
						objStr[1] = "true";
						_msg.obj = objStr;
						handler.sendMessage(_msg);
					}

					// 全部上传完成
					Message _msg = Message.obtain();
					_msg = Message.obtain();
					_msg.what = 3;
					handler.sendMessage(_msg);
				} catch (Exception e) {
					Message _msg = Message.obtain();
					_msg.what = -1;
					_msg.obj = R.string.cumm_cummdownload_uploadresultfilefailed
							+ e.getMessage();
					handler.sendMessage(_msg);
				}
			}
		}.start();
	}

	// 下载路线
	private void DownLoadAllData() {
		try {
			// 延时0.5秒，避免跑得太快二条指令一次发送过去了
			Thread.sleep(500);
		} catch (Exception e) {
		}
		if (downloadSum > 0) {
			if (downloadList.size() > 0) {
				int LineID = downloadList.get(0).getLineID();
				addCommHint(mContext.getString(
						R.string.drawleft_msg_startdownload, LineID), false,
						"false");
				tcpConnect.DownloadPlan(downloadList.get(0));
			} else {
				Message msg = Message.obtain();
				msg.what = 100;
				msg.obj = mContext.getString(R.string.drawleft_msg_syncsuccess);
				handler.sendMessage(msg);
			}
		} else {
			if (AppContext.CommDJLineBuffer.size() > 0) {
				addCommHint(
						mContext.getString(R.string.cumm_cummdownload_planislast),
						false, "false");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				Message msg = Message.obtain();
				msg.what = 100;
				msg.obj = mContext.getString(R.string.drawleft_msg_syncsuccess);
				handler.sendMessage(msg);
			} else {
				addCommHint(mContext.getString(R.string.drawleft_msg_noline),
						false, "false");
			}
			progress_button.setText(mContext
					.getString(R.string.drawleft_btn_syncdata));
			loading.dismiss();
		}
	}

	private final int SERVER_PORT = 10086;
	private ServerSocket mServerSocket = null;
	CommUSBTcpConnect tcpConnect = null;

	private void connectSocket() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InetAddress localhost = InetAddress.getLocalHost();
					String ip = localhost.getHostAddress();
					// String ip = "127.0.0.1";
					System.out.println("TcpConnect ip地址是: " + ip);

					mServerSocket = new ServerSocket(SERVER_PORT);
					Socket client = null;
					mServerSocket.setSoTimeout(10000);
					client = mServerSocket.accept();
					tcpConnect = new CommUSBTcpConnect(client, handler,
							appVersion,true);
					new Thread(tcpConnect).start();

				} catch (SocketTimeoutException e) {
					CloseSocket();
					Message msg = new Message();
					msg.what = -1;
					msg.obj = mContext
							.getString(R.string.cumm_cummdownload_usbdownload_sockettimeout);
					handler.sendMessage(msg);
				} catch (Exception e) {
					CloseSocket();
					Message msg = new Message();
					msg.what = -1;
					msg.obj = e;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	// 关闭socket
	private void CloseSocket() {
		if (tcpConnect != null) {
			tcpConnect.SendDisConnect();
		}
		Thread th = new Thread(closeSocketTask);
		th.start();

	}

	Runnable closeSocketTask = new Runnable() {
		@Override
		public void run() {
			isUnderwayComm = false;
			Socket closeSocket = new Socket();
			try {
				InetAddress localhost = InetAddress.getLocalHost();
				SocketAddress address = new InetSocketAddress(localhost,
						SERVER_PORT);
				closeSocket.connect(address, 100);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} finally {
				try {
					closeSocket.close();

					if (tcpConnect != null) {
						tcpConnect.CloseRecSocket();
						tcpConnect = null;
					}
					if (mServerSocket != null) {
						mServerSocket.close();
						mServerSocket = null;
					}
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	};

	/**
	 * 刷新登录人和路线信息
	 * 
	 */
	private void refreshLogin() {
		// 关闭socket
		CloseSocket();
		isUnderwayComm = false;
		progress_button.setProgress(0);
		if (Main_Page.instance() != null) {
			// 为上位机配置，APP中不可见的配置项赋值
			/*LoadAppConfigHelper.getAppConfigHelper(
					AppConst.AppConfigType.Invisible.toString())
					.LoadConfigByType();*/
			// 更新自定义快捷栏
			Main_Page.instance().refreshCustomShortcut();
			// 更新手势密码配置文件
			Main_Page.instance().updateGestureInfo();

			if (AppContext.getCheckInAfterEnterLine()) {
				Main_Page.instance().checkInAfterEnterLine();
			} else {
				if (AppContext.GetDownLoadYN()) {
					AppContext.SetlineList(appContext.getLoginInfo()
							.getUserLineListStr());
					Main_Page.instance().sendBroadcast();
				}
				Main_Page.instance().checkLoginAfterCommunication();
			}
			Main_Page.instance().setDrawleftVisible();
		}
	}

}
