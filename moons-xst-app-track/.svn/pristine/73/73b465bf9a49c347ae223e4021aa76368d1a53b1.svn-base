package com.moons.xst.track.ui;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommNew_DJLine_ListViewAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.SystemControlHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.dao.DataTransHelper;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenu;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuListView;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuListView.OnMenuItemClickListener;

public class CommUSBDownloadNew extends BaseActivity {
	
	public static final int REQCODE_BACKUPERRORRESULT = 1;// 备份错误结果
	private String appVersion = "";// 版本号
	private AppContext appContext;// 全局Context
	private boolean isPCDownloading=false;//pc端是否在下载
	private boolean downloading = false;// 是否正在下载标记
	private boolean uploading = false;// 是否正在上传标记
	private boolean isConnect=false;//Socket是否连接
	private boolean isRepairXML=false;//userlist是否修复
	private boolean isDJPC=false;//当前通信是否是点检排程
	// 保存每条路线下载状态，key为下载的第几条，value为下载了多少-1为正在标准化-2为标准化错误-3为下载出错
	HashMap<Integer, Integer> downloadState = new HashMap<Integer, Integer>();
	// 保存上传失败信息，key为路线value为信息
	private HashMap<DJLine, String> upLoadFailingMap = new HashMap<DJLine, String>();
	// 记录当前是数据同步还是侧滑下载（true为数据同步）
	private boolean _UpAndDownLoadAllData = true;
	// 当前正在下载的路线
	int mCurrentDownLoadPos = -1;
	//需要上传哪些文件
	String NeedUploadFile="";
	TextView head_title, tv_defeated;
	ImageButton returnButton;
	private Button downloadPlanButton;
	TextView commLineNeednedCount, commLineCount;
	LinearLayout ll_upLoad_statistics;
	private SwipeMenuListView lvCommLineListView;
	private CommNew_DJLine_ListViewAdapter lvDJLineAdapter;

	private int needDownLineCount = 0;
	HashMap<Integer, DJLine> needDownLineList = new HashMap<Integer, DJLine>();
	private ArrayList<DJLine> uploadResultLineIDs = new ArrayList<DJLine>();

	UsbStatesReceiver mUsbStatesReceiver = null;// 广播监听器

	public static CommUSBDownloadNew instance = null;

	// 是否允许侧滑
	public static boolean isDownload() {
		if (instance != null) {
			if(instance.isConnect){
				if(instance.downloading){
					return false;
				}else{
					return true;
				}
			}else{
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_download_new);
		instance = this;
		appVersion = AppContext.getAppVersion(this);
		appContext = (AppContext) getApplication();
		// 删除xstDB文件，创建xstdb文件夹
		AppConst.XSTDBPath();
		initView();
		appContext.refreshDao();
		// 注册监听USB插拔广播
		if (mUsbStatesReceiver == null) {
			mUsbStatesReceiver = new UsbStatesReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.hardware.usb.action.USB_STATE");
			registerReceiver(mUsbStatesReceiver, filter);
		}
		// 初始化上传提示框
		initPopupwindow();
		// 启动Socket
		Thread th = new Thread(networkTask);
		th.start();
	}

	private void initView() {
		ll_upLoad_statistics = (LinearLayout) findViewById(R.id.ll_upLoad_statistics);
		//上传失败提示
		tv_defeated = (TextView) findViewById(R.id.tv_defeated);
		tv_defeated.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(CommUSBDownloadNew.this, CommDownloadErrorResultBack.class);
				Bundle bundle = new Bundle();
                bundle.putSerializable("errorResult",upLoadFailingMap);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQCODE_BACKUPERRORRESULT);
			}
		});
		head_title = (TextView) findViewById(R.id.ll_comm_download_new_head_title);
		head_title.setText(getString(R.string.commu_receive_download_title));
		// 返回按钮
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				goBack();
			}
		});
		// 数据同步按钮
		downloadPlanButton = (Button) findViewById(R.id.btn_linedownload);
		downloadPlanButton.setEnabled(false);
		downloadPlanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isPCDownloading){
					UIHelper.ToastMessage(CommUSBDownloadNew.this, getString(R.string.download_state_await));
					return;
				}
				if (!downloading&&!uploading) {
					ll_upLoad_statistics.setVisibility(View.GONE);
					uploadResultLineIDs.clear();
					// 装载需要上传的路线
					if (AppContext.resultDataFileListBuffer != null
							&& AppContext.resultDataFileListBuffer.size() > 0) {
						for (DJLine djLine : AppContext.resultDataFileListBuffer) {
							int lineID = djLine.getLineID();
							String DataBaseName = AppConst
									.CurrentResultPath(lineID)
									+ AppConst.ResultDBName(lineID);
							File file = new File(DataBaseName);
							if (file.exists()) {
								uploadResultLineIDs.add(djLine);
							}
						}
					}
					_UpAndDownLoadAllData = true;
					upLoadFailingMap.clear();
					downloadLine.clear();
					UpLoadAllData();
				}
			}
		});
		// 全部
		commLineCount = (TextView) findViewById(R.id.comm_download_new_needCount);
		// 需下载
		commLineNeednedCount = (TextView) findViewById(R.id.comm_download_new_count);
		// 路线listview
		lvCommLineListView = (SwipeMenuListView) findViewById(R.id.comm_download_new_listview_status);
		// 绑定列表
		lvDJLineAdapter = new CommNew_DJLine_ListViewAdapter(this,
				AppContext.CommDJLineBuffer, R.layout.listitem_comm_djline,
				true, lvCommLineListView);
		lvCommLineListView.setAdapter(lvDJLineAdapter);
		lvCommLineListView.setTag(downloadState);
		lvCommLineListView
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						// TODO Auto-generated method stub
						switch (index) {
						case 0:// 下载单条路线
							if(isPCDownloading){
								UIHelper.ToastMessage(CommUSBDownloadNew.this, getString(R.string.download_state_await));
								break;
							}
							try {
								ll_upLoad_statistics.setVisibility(View.GONE);
								DJLine adjline = null;
								int po = lvCommLineListView
										.getFirstVisiblePosition();
								View v = (View) lvCommLineListView
										.getChildAt(position - po);
								TextView aTitle = (TextView) v
										.findViewById(R.id.comm_djline_listitem_title);
								adjline = (DJLine) aTitle.getTag();
								if (adjline == null) {
									break;
								}

								_UpAndDownLoadAllData = false;
								upLoadFailingMap.clear();
								uploadResultLineIDs.clear();
								uploadResultLineIDs.add(adjline);
								downLoadCommLine(adjline, position, true);
							} catch (Exception e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
								UIHelper.ToastMessage(CommUSBDownloadNew.this,
										e.getMessage());
							}
							break;
						}
						return false;
					}
				});
	}
	
	 

	private final int SERVER_PORT = 10086;
	private ServerSocket mServerSocket = null;
	private boolean IsRun = false;
	CommUSBTcpConnect tcpConnect = null;
	/**
	 * 网络操作相关的子线程
	 */
	Runnable networkTask = new Runnable() {
		@Override
		public void run() {
			try {
				InetAddress localhost = InetAddress.getLocalHost();
				String ip = localhost.getHostAddress();
				mServerSocket = new ServerSocket(SERVER_PORT);
				Socket client = null;
				IsRun = true;
				while (IsRun) {
					client = mServerSocket.accept();
					if (IsRun) {
						tcpConnect = new CommUSBTcpConnect(client,
								mSocketRevHandler, appVersion, false);
						new Thread(tcpConnect).start();
					} else {
						break;
					}
				}
				if (tcpConnect != null) {
					tcpConnect.CloseRecSocket();
					tcpConnect = null;
				}
				mServerSocket.close();
				mServerSocket = null;
			} catch (Exception e) {
				restoreStatus();
				UIHelper.ThreadToastMessage(
						CommUSBDownloadNew.this,
						getString(
								R.string.cumm_cummdownload_usbdownload_socketerror,
								e.getMessage()), mLoadHandler);
			}
		}
	};

	// 关闭Socket
	private void CloseSocket() {
		if (IsRun == false)
			return;
		if (tcpConnect != null) {
			tcpConnect.SendDisConnect();
		}

		IsRun = false;

		Thread th = new Thread(closeSocketTask);
		th.start();
	}

	// 关闭Socket的线程
	Runnable closeSocketTask = new Runnable() {
		@Override
		public void run() {
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
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	};

	// 加载路线Hander
	Handler mLoadHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && msg.obj != null) {
				// SetDownLoadYN设为true回主页面后判断是否注销登录
				AppContext.SetDownLoadYN(true);
				bindingCommLineData();// 获取的数据绑定到列表中
			}
		}
	};

	// Socket相关操作Handler
	Handler mSocketRevHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				String info = msg.obj.toString();
				if (!StringUtils.isEmpty(info)) {
					if (msg.arg1 == 1) {
						String[] tempList = info.split("@");
						String isSuc = "false";
						String cmd = "";
						String data = "";
						String attach="";
						if (tempList.length >= 1)
							isSuc = tempList[0];
						if (tempList.length >= 2)
							cmd = tempList[1];
						if (tempList.length >= 3)
							data = tempList[2];
						if(tempList.length>=4)
							attach=tempList[3];
						if (cmd.equals("pc-initdataend")) {// 初始化路线完成
							isPCDownloading=false;
							isDJPC=data.equals("true");
							//获取服务器时间
							String time=attach.split(",")[0];
							loadLineListData(time);
						}else if(cmd.equals("pc-underwaydownload")){//初始化路线完成但pc端正在下载路线
							isPCDownloading=true;
							isDJPC=data.equals("true");
							loadLineListData("");
						} else if(cmd.equals("pc-needuploadfile")){//需要上传哪些文件
							NeedUploadFile=data;
						}else if (cmd.equals("pc-uploaddataend")) {// 上传单条数据完成
							upLoadEndOneSelectedLineData(data);
						} else if (cmd.equals("pc-downloadplanend")) {// 下载一条路线完成
							Message _msg = Message.obtain();
							_msg.what = 2;
							_msg.obj = data;
							mDwonHandler.sendMessage(_msg);
						}else if(cmd.equals("pc-uploaderror")){//上传出错
							Message _msg = Message.obtain();
							_msg.what = -1;
							_msg.obj = data + ";" + attach;
							mUploadHandler.sendMessage(_msg);
						}else if(cmd.equals("pc-downloaderror")){//下载出错
							Message _msg = Message.obtain();
							_msg.what = -1;
							_msg.obj = data+";"+attach;
							mDwonHandler.sendMessage(_msg);
						}else if (cmd.equals("pc-error")) {// 通信程序出错
							restoreStatus();
							UIHelper.ToastMessage(CommUSBDownloadNew.this, data);
						}else if(cmd.equals("pc-noauthorization")){//没有授权
							LayoutInflater factory = LayoutInflater.from(CommUSBDownloadNew.this);
							final View view = factory.inflate(R.layout.textview_layout,
									null);
							new com.moons.xst.track.widget.AlertDialog(CommUSBDownloadNew.this)
									.builder()
									.setTitle(getString(R.string.tips))
									.setView(view)
									.setMsg(data)
									.setPositiveButton(getString(R.string.sure),
										new OnClickListener() {								
										@Override
										public void onClick(View v) {
											downloading=false;
											uploading=false;
											goBack();
										}
							}).setCanceledOnTouchOutside(false).setCancelable(false).show();
						}
					} else if (msg.arg1 == 3) {
						if (info.equals("usbdisconnect")) {// usb拔出
							restoreStatus();
							UIHelper.ToastMessage(CommUSBDownloadNew.this,
									getString(R.string.cumm_cummdownload_usbdisconnect_hint));
						}
					}
				}
			}
		}
	};

	// 上传数据Handler
	Handler mUploadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 单条开始压缩
				if (uploadResultPW != null && !uploadResultPW.isShowing()) {
					uploadResultPW.showAsDropDown(parentView, 0, 0);
					popupload_comm_hint.removeAllViews();
					pop_upload_progressBar.setProgress(0);
					popStartupload
							.setText(getString(R.string.cumm_cummdownload_headtitle_promptinfo));
				}
				addCommHint(true, msg.obj.toString(), false, "false");
			} else if (msg.what == 2) {// 压缩失败
				addCommHint(true, msg.obj.toString(), true, "true");
				// 插入操作日志
				OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_USB 
						+ AppConst.ANDROID_DATA_UPLOADING,
						AppConst.LOGSTATUS_ERROR,
						msg.obj.toString().split(";")[0]);
			} else if (msg.what == 3) {// 单条开始上传
				addCommHint(true, msg.obj.toString(), false, "false");
			} else if (msg.what == 4) {// 单条上传成功
				addCommHint(true, msg.obj.toString(), false, "true");
				// 插入操作日志
				OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_USB 
						+ AppConst.ANDROID_DATA_UPLOADING,
						AppConst.LOGSTATUS_NORMAL,
						msg.obj.toString());
				
				if (uploadResultPW != null && uploadResultPW.isShowing()
						&& uploadResultLineIDs.size() <= 0) {
					delayedDismissPW(500);
				}
			}else if(msg.what==5){//关闭上传提示框
				uploadResultPW.dismiss();
			} else if (msg.what == -1) {// 上传出错
				addCommHint(true, getString(R.string.cumm_cummdownload_uploadfilefailed, msg.obj.toString().split(";")[1]), true, "true");
				// 插入操作日志
				OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_USB 
						+ AppConst.ANDROID_DATA_UPLOADING,
						AppConst.LOGSTATUS_ERROR,
						getString(R.string.cumm_cummdownload_uploadfilefailed, msg.obj.toString().split(";")[1])
						+ "\n["
						+ msg.obj.toString().split(";")[0] 
						+ "]");
			}
			
			if (msg.what == -1 || msg.what == 2) {// 压缩失败或上传出错
				String errMsg = msg.obj.toString().split(";")[0];
				String lineID = msg.obj.toString().split(";")[1];
				if (uploadResultLineIDs.size() > 0) {
					upLoadFailingMap.put(uploadResultLineIDs.get(0), errMsg);
					uploadResultLineIDs.remove(0);
				}
				// 压缩或上传失败，如果下载库在结果库文件夹中，则删除
				File file = new File(AppConst.XSTResultPath() + lineID + File.separator
						+ AppConst.PlanDBName(Integer.parseInt(lineID)));
				if (file.exists()) {
					file.delete();
				}
				if (_UpAndDownLoadAllData && uploadResultLineIDs.size() > 0) {// 如果是数据上传，且还有数据需要上传则继续上传
					UpLoadAllData();
				} else {// 否则提示上传失败提示框
					uploading=false;
					ll_upLoad_statistics.setVisibility(View.VISIBLE);
					tv_defeated.setText(getString(
							R.string.cumm_cummdownload_downloadfail_count,
							upLoadFailingMap.size()));
					if (uploadResultPW != null && uploadResultPW.isShowing()
							&& uploadResultLineIDs.size() <= 0) {
						delayedDismissPW(500);
					}
				}
			}
		}
	};
	
	// 下载Handler
	Handler mDwonHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == -1) {// 下载失败
				isPCDownloading=false;
				String obj=msg.obj.toString();
				downloadState.put(mCurrentDownLoadPos, -3);
				// 插入操作日志
				OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_USB 
						+ AppConst.ANDROID_DATA_DOWNLOAD,
						AppConst.LOGSTATUS_ERROR,
						obj.split(";")[2]
						+ "\n["
						+ obj.split(";")[0] 
						+ "]");
				if(_UpAndDownLoadAllData){//数据同步则继续下载
					downLoadEndOneSelectedLineData(obj.split(";")[1],false);
				} else {
					downloading = false;
				}
			} else if (msg.what == 1) {// 开始下载
				downloadState.put(mCurrentDownLoadPos, 50);
			} else if (msg.what == 2) {// 下载单条完成
				isPCDownloading=false;
				String lineID=msg.obj.toString().split(";")[0];
				String lineName = msg.obj.toString().split(";")[1];
				downloadState.put(mCurrentDownLoadPos, 100);
				if (needDownLineCount > 0)
					needDownLineCount = needDownLineCount - 1;
				commLineCount.setText(getString(
						R.string.download_message_count, needDownLineCount));
				// 插入操作日志
				OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_USB 
						+ AppConst.ANDROID_DATA_DOWNLOAD,
						AppConst.LOGSTATUS_NORMAL,
						lineName);
				
				downLoadEndOneSelectedLineData(lineID,true);
			}
			lvCommLineListView.setTag(downloadState);
			lvDJLineAdapter.notifyDataSetChanged();
		}
	};

	/**
	 * 下载完成初始化数据后加载路线数据
	 */
	private void loadLineListData(String time) {
		// 为上位机配置，APP中不可见的配置项赋值，下载成功后就赋值
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.Invisible.toString()).LoadConfigByType();

		// 读取两票模块是否可见
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.TwoBill.toString()).LoadConfigByType();
		// 读取检修模块是否可见
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.Overhaul.toString()).LoadConfigByType();

		// 更新时间
		if (!StringUtils.isEmpty(time) 
				&& AppContext.getUpdateSysDateType()
					.equals(AppConst.UpdateSysDate_Commu)) {
			if (!DateTimeHelper.getCurrentTimeZone().equalsIgnoreCase(AppContext.getTimeZone())) {
				SystemControlHelper.setSystemTimeZone(AppContext.getTimeZone());
			}
			
			DateTimeHelper.setSystemDatatime(DateTimeHelper
					.StringToDate(time).getTime());
		}
		
		downloadPlanButton.setEnabled(true);
		downloading = false;
		isConnect=true;
		downloadState.clear();
		lvCommLineListView.setTag(downloadState);

		Message msg = Message.obtain();
		msg.what = 1;
		msg.obj = AppContext.CommDJLineBuffer;
		mLoadHandler.sendMessage(msg);
	}

	/**
	 * 绑定到列表
	 */
	private void bindingCommLineData() {
		// 绑定列表
		lvDJLineAdapter = new CommNew_DJLine_ListViewAdapter(this,
				AppContext.CommDJLineBuffer, R.layout.listitem_comm_djline,
				true, lvCommLineListView);
		lvCommLineListView.setAdapter(lvDJLineAdapter);
		needDownLineList.clear();
		for (DJLine _djline : AppContext.CommDJLineBuffer) {
			if (_djline.getneedUpdate())
				needDownLineList.put(_djline.getLineID(), _djline);
		}
		needDownLineCount = needDownLineList.size();
		// 显示汇总信息
		commLineNeednedCount.setText(getString(R.string.download_message_count,
				String.valueOf(AppContext.CommDJLineBuffer.size())));
		commLineCount.setText(getString(R.string.download_message_count,
				String.valueOf(needDownLineCount)));
	}

	/*
	 * 上传路线，没有需要上传的则下载
	 */
	private void UpLoadAllData() {
		if (uploadResultLineIDs != null && uploadResultLineIDs.size() > 0) {
			uploading=true;
			upLoadResultData(uploadResultLineIDs.get(0));
		} else {
			uploading=false;
			downloading=true;
			DownLoadAllData();
		}
	}

	/*
	 * 点击数据同步下载计划
	 */
	private void DownLoadAllData() {
		try {
			
			DJLine djLine = null;
			int i = 0;
			for (i = 0; i < AppContext.CommDJLineBuffer.size(); i++) {
				if (AppContext.CommDJLineBuffer.get(i).getneedUpdate() == true
						&&!downloadLine.contains(String.valueOf(AppContext.CommDJLineBuffer.get(i).getLineID()))) {
					djLine = AppContext.CommDJLineBuffer.get(i);
					break;
				}
			}
			if (djLine != null) {
				int _pos = i;
				try {
					downLoadCommLine(djLine, _pos, false);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			} else {
				if (tcpConnect == null) {
					restoreStatus();
					UIHelper.ToastMessage(
							this,
							R.string.cumm_cummdownload_usbuploadfailed_socketcanotused);
					return;
				}
				if(downloadLine.size()>0){
					// 告诉通信程序所有路线下载结束
					new Thread() {
						public void run() {
							tcpConnect.SendDownLoadEnd();
						}
					}.start();
				}else{
					if (AppContext.CommDJLineBuffer.size() > 0)
						UIHelper.ToastMessage(CommUSBDownloadNew.this, getString(R.string.cumm_cummdownload_planislast));
					else 
						UIHelper.ToastMessage(CommUSBDownloadNew.this, getString(R.string.drawleft_msg_noline));
				}
				
				downloading=false;
			}
		} catch (Exception e) {
			restoreStatus();
			UIHelper.ToastMessage(
					this,
					getString(
							R.string.cumm_cummdownload_usbdownload_socketerror,
							e.getMessage()));
		}
	}
	
	//记录下载过的路线id
	List<String> downloadLine=new ArrayList<String>();
	/*
	 * 下载一条路线结束
	 */
	private void downLoadEndOneSelectedLineData(String djLineID,boolean isSucceed) {
		
		for (DJLine mLine : AppContext.CommDJLineBuffer) {
			if (String.valueOf(mLine.getLineID()).equals(djLineID)){
				if(isSucceed){
					mLine.setneedUpdate(false);
				}
			}
		}
		
		downloadLine.add(djLineID);

		// 刷新路线列表是否需要下载标示
		lvDJLineAdapter.notifyDataSetChanged();

		if (_UpAndDownLoadAllData) // 如果是点击数据同步，则继续下载
			DownLoadAllData();
		else { // 如果是从菜单下载单条路线，则发送下载结束命令
			new Thread() {
				public void run() {
					downloading=false;
					tcpConnect.SendDownLoadEnd();
				}
			}.start();
		}
	}

	/*
	 * 上传一条路线结果
	 */
	private void upLoadResultData(final DJLine djLine) {
		new Thread() {
			@Override
			public void run() {
				try {
					int lineID = djLine.getLineID();
					String DataBaseName = AppConst.CurrentResultPath(lineID)
							+ AppConst.ResultDBName(lineID);
					File _file = new File(DataBaseName);
					boolean isUpPlan = NeedUploadFile.toUpperCase().contains(
							"|NEEDPLAN");
					if (isUpPlan) {
						String planFileName = AppConst.XSTDBPath()
								+ File.separator + AppConst.PlanDBName(lineID);
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
					_msg.what = 1;
					_msg.obj = getString(R.string.cumm_cummdownload_iszipfile,
							lineID);
					mUploadHandler.sendMessage(_msg);
					boolean b = ZipUtils.zipFolder(_file.getParent(),
							AppConst.XSTZipResultPath() + lineID);
					if (!b) {
						_msg = Message.obtain();
						_msg.what = 2;
						_msg.obj = getString(
								R.string.cumm_cummdownload_iszipfileerror,
								lineID) + ";" + String.valueOf(djLine.getLineID());
						mUploadHandler.sendMessage(_msg);
					}else{
						_msg = Message.obtain();
						_msg.what = 3;
						_msg.obj = getString(
								R.string.cumm_cummdownload_isuploadfile, lineID);
						mUploadHandler.sendMessage(_msg);
						String pdaFilePathString = AppConst.XSTZipResultPath()
								+ String.valueOf(djLine.getLineID());
						tcpConnect.UpLoadLineData(djLine, pdaFilePathString);
					}
				} catch (Exception e) {
					Message _msg = Message.obtain();
					_msg.what = -1;
					_msg.obj = e.getMessage() + ";" + String.valueOf(djLine.getLineID());
					mUploadHandler.sendMessage(_msg);
				}
			}
		}.start();
	}

	/*
	 * 上传完成单条结果后执行
	 */
	private void upLoadEndOneSelectedLineData(String data) {
		String lineID = data.split(";")[0];
		String lineName = data.split(";")[1];

		// 清空路线相关文件
		String path = AppConst.XSTResultPath() + lineID;
		FileUtils.clearFileWithPath(path);

		// 如果上传的是0,则删除照片缩略图
		if (lineID.equals("0")) {
			File file = new File(AppConst.XSTTempThumbnailImagePath());
			if (file.list().length > 0) {
				File[] files = file.listFiles();
				for (File f : files) {
					f.delete();
				}
			}
		}

		if (uploadResultLineIDs.size() > 0) {
			for(int i=0;i<uploadResultLineIDs.size();i++){
				if(lineID.equals(String.valueOf(uploadResultLineIDs.get(i).getLineID()))){
					uploadResultLineIDs.remove(i);
					break;
				}
			}
		}
		
		Message _msg = Message.obtain();
		_msg.what = 4;
		_msg.obj = getString(R.string.cumm_cummdownload_uploadfilesucced,
				lineID);
		mUploadHandler.sendMessage(_msg);

		if (_UpAndDownLoadAllData == true) // true为数据同步
		{
			UpLoadAllData();
		} else { // false为侧滑下载
			DJLine djLine = new DJLine();
			djLine.setLineID(Integer.parseInt(lineID));
			djLine.setLineName(lineName);
			tcpdownloadThread(djLine);
		}
	}

	/*
	 * 下载一条路线（先上传再下载）
	 */
	private void downLoadCommLine(final DJLine djLine, final Integer pos,
			final boolean checkupData) {
		mCurrentDownLoadPos = pos;
		if (tcpConnect == null) {
			downloading=false;
			Message msg = Message.obtain();
			msg.what = -1;
			msg.obj = getString(R.string.cumm_cummdownload_usbuploadfailed_socketcanotused) + ";" + djLine.getLineID();
			mDwonHandler.sendMessage(msg);
		} else {
			try {
				if (checkupData && existLineData(djLine.getLineID())) {// 上传路线
					uploading=true;
					upLoadResultData(djLine);
				} else {
					if (!djLine.getBuidYN()) {
						UIHelper.ThreadToastMessage(CommUSBDownloadNew.this, getString(R.string.line_list_buildTime_noBuild), mDwonHandler);
						return;
					}
					tcpdownloadThread(djLine);// 下载路线
				}
			} catch (Exception e) {
				downloading=false;
				Message msg = Message.obtain();
				msg.what = -1;
				msg.obj = e.getMessage() + ";" + djLine.getLineID();
				mDwonHandler.sendMessage(msg);
			}

		}
		AppContext.SetRefreshLineYN(true);
	}

	private void tcpdownloadThread(DJLine djLine) {
		uploading=false;
		downloading=true;
		myRun r = new myRun(djLine);
		Thread t = new Thread(r);
		t.start();
	}

	class myRun implements Runnable {
		private DJLine djLine;

		public myRun(DJLine djLine) {
			this.djLine = djLine;
		}

		@Override
		public void run() {
			try {
				Message _msg = Message.obtain();
				_msg.what = 1;
				_msg.arg1 = djLine.getLineID();
				mDwonHandler.sendMessage(_msg);
				if(!isRepairXML){
					isRepairXML=DataTransHelper.RepairUser(isDJPC?AppConst.PlanType_DJPC:AppConst.PlanType_XDJ);
				}
				// 发送下载路线指令
				tcpConnect.DownloadPlan(djLine);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//延时关闭上传框
	private void delayedDismissPW(final long time){
		new Thread() {
			public void run() {
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				Message _msg = Message.obtain();
				_msg.what=5;
				mUploadHandler.sendMessage(_msg);
			}
		}.start();
	}	
	
	private void restoreStatus(){
		isConnect=false;
		//lvCommLineListView.smoothOpenMenu(arg2);
		if(lvDJLineAdapter!=null){
			lvDJLineAdapter.notifyDataSetChanged();
		}
		if (uploadResultPW != null && uploadResultPW.isShowing()){
			uploadResultPW.dismiss();
		}
		downloadPlanButton.setEnabled(false);
		downloading=false;
		uploading=false;
		if (tcpConnect != null) {
			tcpConnect.CloseRecSocket();
			tcpConnect = null;
		}
	}

	/*
	 * 判断路线结果数据是否存在
	 */
	private boolean existLineData(int lineID) {
		try {
			String DataBaseName = AppConst.CurrentResultPath(lineID)
					+ AppConst.ResultDBName(lineID);
			File _file = new File(DataBaseName);
			return _file.exists();
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * 初始化上传提示框
	 */
	private LayoutInflater lm_uploadResultinflater;
	private View lm_uploadResultlayout;
	private PopupWindow uploadResultPW;
	private View parentView;
	private TextView popStartupload;
	private ProgressBar pop_upload_progressBar;
	private LinearLayout popupload_comm_hint;

	private void initPopupwindow() {
		// 获取LayoutInflater实例
		lm_uploadResultinflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 获取弹出菜单的布局
		lm_uploadResultlayout = lm_uploadResultinflater.inflate(
				R.layout.pop_uploadresult, null);
		// 设置popupWindow的布局
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		int height = dm.heightPixels;// 高度
		uploadResultPW = new PopupWindow(lm_uploadResultlayout, width, height,
				true);
		uploadResultPW.setFocusable(false);
		uploadResultPW.setOutsideTouchable(true);

		lm_uploadResultlayout.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_BACK) {
					if (uploading)
						return false;
				}

				return false;
			}
		});
		parentView = findViewById(R.id.ll_comm_download_new_head);
		popStartupload = (TextView) lm_uploadResultlayout
				.findViewById(R.id.tv_startupload);
		pop_upload_progressBar = (ProgressBar) lm_uploadResultlayout
				.findViewById(R.id.pop_upload_progressBar);
		popupload_comm_hint = (LinearLayout) lm_uploadResultlayout
				.findViewById(R.id.popupload_comm_hint);
	}

	// 添加通信提示信息
	private void addCommHint(boolean isAdd, String hint, boolean isError,
			String drawableYn) {
		if (isAdd) {
			TextView tv = new TextView(this);
			if (StringUtils.toBool(drawableYn)) {
				Drawable drawable = !isError ? getResources().getDrawable(
						R.drawable.commu_success) : getResources().getDrawable(
						R.drawable.commu_fail);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				tv.setCompoundDrawables(null, null, drawable, null);
			}
			tv.setText(hint);
			tv.setSingleLine();
			if (isError) {
				tv.setTextColor(getResources().getColor(R.color.red));
			} else {
				tv.setTextColor(getResources().getColor(R.color.black));
			}

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			// 设置textview的左上右下的边距
			params.setMargins(5, 5, 0, 0);
			tv.setLayoutParams(params);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			popupload_comm_hint.addView(tv, 0);
		} else {
			View view = popupload_comm_hint.getChildAt(0);
			if (view != null) {
				((TextView) (view)).setText(hint);
			}
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 退出页面
	private void goBack() {
		if (uploading||downloading) {// 如果正在上传或下载不能退出
			if(uploadResultPW==null||!uploadResultPW.isShowing()){
				UIHelper.ToastMessage(CommUSBDownloadNew.this,
						getString(R.string.cumm_usb_communicating));
			}
			return;
		}
		// 如果正在下载提示是否退出
		if (downloading) {
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(this)
					.builder()
					.setTitle(getString(R.string.goback))
					.setView(view)
					.setMsg(getString(R.string.download_state_cancel_notice))
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									// 关闭Socket
									CloseSocket();
									// 注销广播
									if (mUsbStatesReceiver != null) {
										unregisterReceiver(mUsbStatesReceiver);
										mUsbStatesReceiver = null;
									}
									instance = null;
									AppContext.CommDJLineBuffer.clear();
									AppManager.getAppManager().finishActivity(
											CommUSBDownloadNew.this);
								}
							})
					.setNegativeButton(getString(R.string.cancel), null).show();
		} else {
			// 关闭Socket
			CloseSocket();
			// 注销广播
			if (mUsbStatesReceiver != null) {
				unregisterReceiver(mUsbStatesReceiver);
				mUsbStatesReceiver = null;
			}
			instance = null;
			AppContext.CommDJLineBuffer.clear();
			
			if(isRepairXML){//如果做过通信则清理数据
				DataTransHelper.cleanUserListAndDB(isDJPC?AppConst.PlanType_DJPC:AppConst.PlanType_XDJ);
			}
			AppManager.getAppManager().finishActivity(CommUSBDownloadNew.this);
		}
	}

	// 广播监听器
	public class UsbStatesReceiver extends BroadcastReceiver {
		public UsbStatesReceiver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals("android.hardware.usb.action.USB_STATE")) {
				Message msg = new Message();
				msg.arg1 = 3;
				if (intent.getExtras().getBoolean("connected")) { // usb插入
					msg.obj = "usbconnect";

				} else {// usb拔出
					msg.obj = "usbdisconnect";
				}
				mSocketRevHandler.sendMessage(msg);
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQCODE_BACKUPERRORRESULT) {
			if (resultCode == Activity.RESULT_OK) {
				upLoadFailingMap = (HashMap<DJLine, String>)(intent.getExtras().get("errorResult"));
				if (upLoadFailingMap != null && upLoadFailingMap.size() <= 0)
					ll_upLoad_statistics.setVisibility(View.GONE);
			}
		}
	}
}
