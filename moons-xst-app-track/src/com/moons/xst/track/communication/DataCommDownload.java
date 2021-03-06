package com.moons.xst.track.communication;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moons.xst.buss.DJLineHelper;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.ResponseLogInfoHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.dao.DataTransHelper;
import com.moons.xst.track.ui.Main_Page;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.ProgressButton;

public class DataCommDownload {

	private Context mContext;
	private AppContext appContext;// 全局Context
	// 透明的dialog(主要用于屏蔽屏幕的所有点击事件和返回事件)
	private LoadingDialog loading;// 加载中dialog
	private LinearLayout ll_comm_hint;
	private ProgressButton progress_button;
	//xml文件是否已经修复
	private boolean isRepairXML=false;
	
	// 是否真正同步
	private boolean isUnderwayComm = false;
	// 版本号
	private String appVersion = "";

	HashMap<Integer, DJLine> _hashMap = new HashMap<Integer, DJLine>();

	private String[] objStr = new String[2];

	private static DataCommDownload dataCommDownload;

	public static DataCommDownload getDataCommDownload() {
		if (dataCommDownload == null) {
			dataCommDownload = new DataCommDownload();
		}
		return dataCommDownload;
	}

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

	Handler mCommProgBarHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (msg.obj == null) {// 下载
					int precent = (int) (msg.arg1 / commLineSum + 100
							/ commLineSum * (underwayComm - 1));
					progress_button.setText(precent + "%");
					progress_button.setProgress(precent);
				} else {// 上传
					int precent = (int) (msg.arg1 / commLineSum + 100
							/ commLineSum * (underwayComm - 1));
					progress_button.setText(precent + "%");
					progress_button.setProgress(precent);
				}
			}
			if (msg.what == -1) {// 下载失败
				addCommHint(msg.obj.toString(), true, "true");
			}
		};
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -1 && msg.obj != null) {// 同步失败
				isUnderwayComm = false;
				addCommHint(msg.obj.toString(), true, "true");
				progress_button.setText(mContext.getString(R.string.drawleft_btn_syncdata));
				if (loading != null) {
					loading.dismiss();
				}
			}
			if (msg.what == 1) {// 加载路线数据成功
				addCommHint(mContext.getString(R.string.drawleft_msg_loadlineinfosuccess), false, "true");
				AppContext.SetDownLoadYN(true);
				// loading消失的时候触发
				loading.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO 自动生成的方法存根
						refreshLogin();
					}
				});
				appContext.refreshDao();
				if (AppContext.resultDataFileListBuffer != null
						&& AppContext.resultDataFileListBuffer.size() > 0) {// 上传数据
					upLoadResultThread();
				} else {// 下载数据
					downLoadCommLineThread();
				}
			}

			if (msg.what == 2) {// 同步提示信息
				addCommHint(((String[]) (msg.obj))[0].toString(), false,
						((String[]) (msg.obj))[1].toString());
			}
			if (msg.what == 3) {// 上传完成
				// addCommHint(msg.obj.toString(), false);
				AppContext.resultDataFileListBuffer.clear();
				downLoadCommLineThread();
			}

			// 数据同步完成
			if (msg.what == 100) {
				addCommHint(msg.obj.toString(), false, "false");
				progress_button.setText(mContext.getString(R.string.drawleft_btn_syncdata));
				loading.dismiss();
			}
		};
	};

	// 主页数据同步入口
	public void dataCommEntrance(Context context, LinearLayout ll_hint,
			ProgressButton progress) {
		if (isUnderwayComm) {
			return;
		}
		isUnderwayComm = true;
		isRepairXML = false;
		mContext = context;
		appContext = (AppContext) context.getApplicationContext();
		ll_comm_hint = ll_hint;
		ll_comm_hint.removeAllViews();
		progress_button = progress;
		progress_button.setProgress(0);
		appVersion = AppContext.getAppVersion(context);

		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(mContext);
			loading.setLoadText(mContext.getString(R.string.drawleft_msg_syncing));
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		progress_button.setText("0%");
		addCommHint(mContext.getString(R.string.drawleft_msg_startloadlineinfo), false, "false");
		// 加载路线数据
		loadCommLineStatusThread();

	}

	/**
	 * 加载路线数据的线程
	 */
	private void loadCommLineStatusThread() {
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				if (!WebserviceFactory.checkWS()) {
					msg = Message.obtain();
					msg.what = -1;
					msg.obj = mContext
							.getString(R.string.cumm_twobill_webcheckPromptinfo);
					handler.sendMessage(msg);
				} else {
					try {
						// 重新加载数据
						loadLineListData();
						loadCommdate();
						underwayComm++;
						Message _msg = Message.obtain();
						_msg.what = 1;
						_msg.arg1 = 100;
						mCommProgBarHandler.sendMessage(_msg);
						msg = Message.obtain();
						msg.what = 1;
						msg.obj = AppContext.CommDJLineBuffer;
						handler.sendMessage(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg = Message.obtain();
						msg.what = -1;
						msg.obj = e;
						handler.sendMessage(msg);
					}
				}
			}
		}.start();
	}

	// 需要上传和下载的路线数量
	private int commLineSum = 0;
	// 当前正在同步第几条路线
	private int underwayComm = 0;
	// 需要上传的路线数量
	private int uploadingSum = 0;
	// 需要下载的路线数量
	private int downloadSum = 0;

	/**
	 * 加载通信数据（需要上传和下载的数据）
	 */
	private void loadCommdate() {
		// 获取需要下载的路线
		_hashMap.clear();
		List<DJLine> list = AppContext.CommDJLineBuffer;
		for (int i = 0; i < list.size(); i++) {
			DJLine _djLine = list.get(i);
			if (_djLine.getneedUpdate()) {
				if (!_hashMap.containsKey(i)) {
					_hashMap.put(i, _djLine);
				}
			}
		}
		commLineSum = _hashMap.size();
		// 需要下载的路线数量
		downloadSum = _hashMap.size();
		underwayComm = 0;
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

	/**
	 * 加载路线数据
	 */
	private DJLineHelper djLineHelper;

	private void loadLineListData() {
		// 加载数据
		if (Thread.currentThread().isInterrupted())
			return;
		Context context = AppContext.baseContext;

		djLineHelper = new DJLineHelper();
		djLineHelper.loadCommLineData(appVersion, context);
	}

	private void upLoadResultThread() {
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				if (!WebserviceFactory.checkWS()) {
					msg = Message.obtain();
					msg.what = -1;
					msg.obj = mContext
							.getString(R.string.cumm_twobill_webcheckPromptinfo);
					handler.sendMessage(msg);
				} else {
					try {
						msg = Message.obtain();
						msg.what = 2;
						objStr[0] = mContext.getString(R.string.drawleft_msg_uploadcount, uploadingSum);
						objStr[1] = "false";
						msg.obj = objStr;
						handler.sendMessage(msg);
						if (upLoadResultListData()) {// 上传数据
							msg = Message.obtain();
							msg.what = 3;
							msg.obj = mContext
									.getString(R.string.cumm_cummdownload_uploadfinished);
							handler.sendMessage(msg);
						} else {
							msg.what = -1;
							msg.obj = mContext
									.getString(R.string.cumm_cummdownload_uploadresultfilefailed);
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						msg = Message.obtain();
						e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
						handler.sendMessage(msg);
					}
				}
			}
		}.start();
	}

	/**
	 * 上传结果数据
	 * 
	 */
	private boolean upLoadResultListData() {
		Context context = AppContext.baseContext;
		UpLoad uploadRes = new UpLoad(context);
		if (AppContext.resultDataFileListBuffer != null
				&& AppContext.resultDataFileListBuffer.size() > 0) {
			boolean isUpPlan = true;
			for (DJLine _djLine : AppContext.resultDataFileListBuffer) {
				try {
					File _file = new File(_djLine.getResultDBPath());
					// 判断有没有结果库，有的话上传
					if (_file.exists()) {
						if (isUpPlan) {
							int lineID = _djLine.getLineID();
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

						Message _msg = Message.obtain();

						_msg.what = 2;
						objStr[0] = mContext.getString(
								R.string.cumm_cummdownload_iszipfile,
								_djLine.getLineID());
						objStr[1] = "false";
						_msg.obj = objStr;
						handler.sendMessage(_msg);
						ZipUtils.zipFolder(
								_file.getParent(),
								AppConst.XSTZipResultPath()
										+ _djLine.getLineID());
						_msg = Message.obtain();
						_msg.what = 2;
						objStr[0] = mContext.getString(
								R.string.cumm_cummdownload_isuploadfile,
								_djLine.getLineID());
						objStr[1] = "false";
						_msg.obj = objStr;
						handler.sendMessage(_msg);
						underwayComm++;
						if (uploadRes.UploadResultDB(
								appVersion,
								context,
								mCommProgBarHandler,
								AppConst.XSTZipResultPath()
										+ _djLine.getLineID())) {
							_msg = Message.obtain();
							_msg.what = 2;
							objStr[0] = mContext.getString(
									R.string.cumm_cummdownload_uploadfilesucced,
									_djLine.getLineID());
							objStr[1] = "true";
							_msg.obj = objStr;
	
							// 上传完临时数据后，有视频缩略图的话，删除缩略图
							File file = new File(
									AppConst.XSTTempThumbnailImagePath());
							if (_djLine.getLineID() == 0 && file.list().length > 0) {
								File[] files = file.listFiles();
								for (File f : files) {
									f.delete();
								}
							}
	
							handler.sendMessage(_msg);
							Thread.sleep(500);
						} else {
							return false;
						}
					}
				} catch (Exception e) {
					Message _msg = Message.obtain();
					_msg.what = -1;
					_msg.obj = mContext
							.getString(R.string.cumm_cummdownload_uploadresultfilefailed)
							+ e.getMessage();
					_msg.arg1 = 0;
					handler.sendMessage(_msg);
					
					return false;
				}
			}
			return true;
		}
		return true;
	}

	/**
	 * 下载路线数据的线程
	 */
	private void downLoadCommLineThread() {
		if (downloadSum > 0) {
			new Thread() {
				public void run() {
					if (!WebserviceFactory.checkWS()) {
						Message msg = Message.obtain();
						msg.what = -1;
						msg.obj = mContext
								.getString(R.string.cumm_twobill_webcheckPromptinfo);
						handler.sendMessage(msg);
					} else {
						try {
							downLoadLineListData();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Message msg = Message.obtain();
							msg.what = -1;
							msg.obj = e;
							handler.sendMessage(msg);
						}

					}
					AppContext.SetRefreshLineYN(true);
				};
			}.start();
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
				addCommHint(mContext.getString(R.string.drawleft_msg_noline), false, "false");
			}
			progress_button.setText(mContext.getString(R.string.drawleft_btn_syncdata));
			loading.dismiss();
		}
	}

	/**
	 * 下载计划数据
	 */
	private void downLoadLineListData() {
		// 下载
		Message msg = Message.obtain();
		Context context = AppContext.baseContext;
		DownLoad downLoad = new DownLoad(context);
		HashMap<Integer, DJLine> _hashHashMap = _hashMap;

		try {
			Message m = Message.obtain();
			m = Message.obtain();
			m.what = 2;
			objStr[0] = mContext.getString(R.string.drawleft_msg_downloadcount, downloadSum);
			objStr[1] = "false";
			m.obj = objStr;
			handler.sendMessage(m);
			DJLine value = null;
			int i = 0;
			for (i = 0; i < AppContext.CommDJLineBuffer.size(); i++) {

				if (AppContext.CommDJLineBuffer.get(i).getneedUpdate() == true) {
					value = AppContext.CommDJLineBuffer.get(i);
					underwayComm++;
					Message _msg = Message.obtain();
					try {
						Thread.sleep(500);
						_msg = Message.obtain();
						_msg.what = 2;
						objStr[0] = mContext.getString(R.string.drawleft_msg_encoding, value.getLineID());
						objStr[1] = "false";
						_msg.obj = objStr;
						handler.sendMessage(_msg);
						String lineNameString = value.getLineName();
						String dbFileNameString = WebserviceFactory
								.RequestPlanDownLoad(context,
										AppContext.GetxjqCD(),
										value.getLineID(), "");
						Thread.sleep(500);
						if (dbFileNameString != null) {
							_msg = Message.obtain();
							_msg.what = 2;
							objStr[0] = mContext.getString(R.string.drawleft_msg_startdownload, value.getLineID());
							objStr[1] = "false";
							_msg.obj = objStr;
							handler.sendMessage(_msg);
							downLoad.DownLoadPlanDB(context, null, 0, value,
									mCommProgBarHandler, dbFileNameString);
							Thread.sleep(500);
							_msg = Message.obtain();
							_msg.what = 2;
							objStr[0] = mContext.getString(R.string.drawleft_msg_downloadsucess, String.valueOf(value.getLineID()));
							objStr[1] = "true";
							_msg.obj = objStr;
							handler.sendMessage(_msg);
						} else {
							_msg = Message.obtain();
							_msg.what = -1;
							_msg.obj = mContext.getString(R.string.drawleft_msg_downloadfail, String.valueOf(value.getLineID()));
							mCommProgBarHandler.sendMessage(_msg);
						}

					} catch (Exception e) {
						_msg = Message.obtain();
						_msg.what = -1;
						mCommProgBarHandler.sendMessage(_msg);
						msg = Message.obtain();
						msg.what = -1;
						msg.obj = mContext
								.getString(R.string.cumm_cummdownload_downloadfail)
								+ e.getMessage();
						handler.sendMessage(msg);
						return;
					}
					if(!isRepairXML){
						boolean Line=DownLoad.UpdateLineListFile(false);
						boolean User=DataTransHelper.RepairUserListXML();
						isRepairXML=Line&&User;
					}
				}
				if (i + 1 == AppContext.CommDJLineBuffer.size()) {
					msg = Message.obtain();
					msg.what = 100;
					msg.obj = mContext.getString(R.string.drawleft_msg_syncsuccess);
					handler.sendMessage(msg);
				}
			}

		} catch (Exception e) {
			msg = Message.obtain();
			msg.what = -1;
			msg.obj = e;
			handler.sendMessage(msg);
		}
	}

	/**
	 * 刷新登录人和路线信息
	 * 
	 */
	private void refreshLogin() {
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
				// Main_Page.instance().refreshLineAfterCommunication();
			}
			Main_Page.instance().setDrawleftVisible();
		}
	}

}
