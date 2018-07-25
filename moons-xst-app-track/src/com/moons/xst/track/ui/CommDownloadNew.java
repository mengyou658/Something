package com.moons.xst.track.ui;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moons.xst.buss.DJLineHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommNew_DJLine_ListViewAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UpdateManager;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.communication.DownLoad;
import com.moons.xst.track.communication.UpLoad;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.dao.DataTransHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenu;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuListView;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuListView.OnMenuItemClickListener;

@SuppressLint("UseSparseArrays")
public class CommDownloadNew extends BaseActivity {

	private final static String TAG = "CommDownloadNew";
	public static final int REQCODE_BACKUPERRORRESULT = 1;// 备份错误结果
	// 控件
	ImageButton returnButton;
	TextView head_title;

	TextView commLineCount, commLineNeednedCount;
	private SwipeMenuListView lvCommLineListView;

	Button downloadPlanButton;

	private TextView popStartupload;
	private LinearLayout popupload_comm_hint, ll_upLoad_statistics;
	private TextView tv_defeated;
	private ProgressBar pop_upload_progressBar;
	private PopupWindow uploadResultPW;
	private LayoutInflater lm_uploadResultinflater;
	private View lm_uploadResultlayout;
	private View parentView;

	private AppContext appContext;// 全局Context
	HashMap<Integer, DJLine> needDownLineList = new HashMap<Integer, DJLine>();
	private CommNew_DJLine_ListViewAdapter lvDJLineAdapter;

	private LoadingDialog loading;

	// newUserList是否合并
	private boolean isRepairXML = false;
	private String appVersion = "";
	// 是否正在下载标记
	private boolean downloading = false;
	// 是否正在上传标记
	private boolean uploading = false;

	Thread loadDataThread, downloadThread, uploadThread;
	private Handler mLoadHandler, mUploadHandler, mUploadProgBarHandler;
	// 保存每条路线下载状态，key为下载的第几条，value为下载了多少-1为正在标准化-2为标准化错误-3为下载出错
	HashMap<Integer, Integer> downloadState = new HashMap<Integer, Integer>();
	// 保存上传失败信息，key为路线value为信息
	private HashMap<DJLine, String> upLoadFailingMap = new HashMap<DJLine, String>();

	public static CommDownloadNew instance = null;

	// 是否允许侧滑
	public static boolean isDownload() {
		if (instance != null) {
			return instance.downloading;
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_download_new);
		instance = this;
		appContext = (AppContext) getApplication();
		appVersion = AppContext.getAppVersion(this);
		try {
			initHeadViewAndMoreBar();
			initView();
			initDownLoadButton();
			initPopupwindow();

			loadCommLineToListViewHander();
			loadUploadHander();
			loadUploadProgBarHandler();

			loadCommLineStatusThread(false);
		} catch (AppException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		appContext.refreshDao();
	}

	/**
	 * 加载路线Hander初始化 what含义： 0：Webservice不可用 1：正常获取，加载路线 -1：获取数据出错
	 */
	private void loadCommLineToListViewHander() {
		mLoadHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 0) {
					UIHelper.ToastMessage(AppContext.baseContext,
							getString(R.string.cumm_twobill_webcheckPromptinfo));
					AppManager.getAppManager().finishActivity(
							CommDownloadNew.this);
				} else if (msg.what == 1) {
					bindingCommLineData();
				} else if (msg.what == -1 || msg.what == -2) {
					String msgString = String.valueOf(msg.obj);
					LayoutInflater factory = LayoutInflater.from(CommDownloadNew.this);
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					new com.moons.xst.track.widget.AlertDialog(CommDownloadNew.this)
							.builder()
							.setTitle(getString(R.string.tips))
							.setView(view)
							.setMsg(msgString)
							.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {								
								@Override
								public void onClick(View v) {
									AppManager.getAppManager().finishActivity(
											CommDownloadNew.this);
								}
					}).setCanceledOnTouchOutside(false).setCancelable(false).show();
				} else if (msg.what == -3) {
					String msgString = String.valueOf(msg.obj);
					UIHelper.ToastMessage(AppContext.baseContext, msgString);
					AppManager.getAppManager().finishActivity(
							CommDownloadNew.this);
				} 
			}
		};
	}

	private void loadUploadHander() {
		mUploadHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 100) {
					if (msg.arg1 == 1) {// 全部路线上传成功
						AppContext.resultDataFileListBuffer.clear();
						try {
							uploading = false;
							downloading = true;
							downLoadCommLineThread(false, null, null);
						} catch (Exception e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					} else if (msg.arg1 == 2) {// 上传失败
						ll_upLoad_statistics.setVisibility(View.VISIBLE);
						if (upLoadFailingMap.size() <= 0) {
							tv_defeated
									.setText(getString(R.string.cumm_cummdownload_downloadfail_hint));
						} else {
							tv_defeated
									.setText(getString(
											R.string.cumm_cummdownload_downloadfail_count,
											upLoadFailingMap.size()));
						}
						uploading = false;
					} else if (msg.arg1 == 3) {// 单条路线上传成功
						try {
							int position = msg.arg2;
							DJLine adjline = (DJLine) msg.obj;
							uploading = false;
							downloading = true;
							downLoadCommLineThread(true, adjline, position);
						} catch (Exception e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}
				if (uploadResultPW != null && uploadResultPW.isShowing())
					uploadResultPW.dismiss();
				if (msg.what == 0) {
					// uploading = false;
					// downloading = false;
					UIHelper.ToastMessage(AppContext.baseContext,
							getString(R.string.cumm_twobill_webcheckPromptinfo));
				} else if (msg.what == 1) {
					ll_upLoad_statistics.setVisibility(View.GONE);
					uploadResultPW.showAsDropDown(parentView, 0, 0);
					popupload_comm_hint.removeAllViews();
					pop_upload_progressBar.setProgress(0);
					popStartupload
							.setText(getString(R.string.cumm_cummdownload_headtitle_promptinfo));
				} else if (msg.what == -1 && msg.obj != null) {
					String msgString = String.valueOf(msg.obj);
					UIHelper.ToastMessage(AppContext.baseContext, msgString);
				}
			}
		};
	}

	/**
	 * 加载路线数据的线程
	 */
	private void loadCommLineStatusThread(final boolean isRefresh)
			throws AppException {
		if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
			UIHelper.ToastMessage(getBaseContext(),
					R.string.network_not_connected);
			return;
		}
		loading = new LoadingDialog(this);
		loading.setCanceledOnTouchOutside(false);
		loading.setCancelable(false);
		loading.show();
		loadDataThread = new Thread() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				if (!WebserviceFactory.checkWS()) {
					msg = Message.obtain();
					msg.what = 0;
					mLoadHandler.sendMessage(msg);
				} else {
					try {
						DownLoad download = new DownLoad(CommDownloadNew.this);
						// 进行授权个数的判断
						ReturnResultInfo returnInfo = 
								download.APClientInfo(appVersion, CommDownloadNew.this, AppContext.GetxjqCD());
						if (!returnInfo.getResult_YN()) {
							msg = Message.obtain();
							msg.what = -2;
							msg.obj = returnInfo.getError_Message_TX();
							mLoadHandler.sendMessage(msg);
						} else {
							// 加载数据
							if (Thread.currentThread().isInterrupted())
								return;
							Context context = getBaseContext();
							DJLineHelper djLineHelper = new DJLineHelper();
							ReturnResultInfo returnInitInfo = djLineHelper.loadCommLineDataNew(context, appVersion);
							if (returnInitInfo.getResult_YN()) {
							//loadLineListData();
								Thread.sleep(1000);
								msg = Message.obtain();
								msg.what = 1;
								msg.obj = AppContext.CommDJLineBuffer;
								mLoadHandler.sendMessage(msg);
							} else {
								msg = Message.obtain();
								msg.what = -1;
								msg.obj = returnInitInfo.getError_Message_TX();
								mLoadHandler.sendMessage(msg);
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg = Message.obtain();
						msg.what = -3;
						msg.obj = e.getMessage();
						mLoadHandler.sendMessage(msg);
					}
				}

			}
		};
		loadDataThread.start();
	}

	/**
	 * 绑定到列表
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void bindingCommLineData() {
		// 绑定列表
		lvDJLineAdapter = new CommNew_DJLine_ListViewAdapter(this,
				AppContext.CommDJLineBuffer, R.layout.listitem_comm_djline,
				false, lvCommLineListView);
		lvCommLineListView.setAdapter(lvDJLineAdapter);

		needDownLineList.clear();
		for (DJLine _djline : AppContext.CommDJLineBuffer) {
			if (_djline.getneedUpdate())
				needDownLineList.put(_djline.getLineID(), _djline);
		}
		// 显示汇总信息
		commLineCount = (TextView) findViewById(R.id.comm_download_new_count);
		commLineCount.setText(getString(R.string.download_message_count,
				String.valueOf(AppContext.CommDJLineBuffer.size())));
		commLineNeednedCount = (TextView) findViewById(R.id.comm_download_new_needCount);
		commLineNeednedCount.setText(getString(R.string.download_message_count,
				String.valueOf(needDownLineList.size())));
	}

	// 上传单条路线
	private void upLoadOneSelectedLineData(final int position) {
		int po = lvCommLineListView.getFirstVisiblePosition();
		View v = (View) lvCommLineListView.getChildAt(position - po);
		if (v == null) {
			return;
		}
		TextView aTitle = (TextView) v
				.findViewById(R.id.comm_djline_listitem_title);
		if (aTitle == null) {
			return;
		}
		final DJLine adjline = (DJLine) aTitle.getTag();
		uploading = true;
		if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
			UIHelper.ToastMessage(getBaseContext(),
					R.string.network_not_connected);
			uploading = false;
			return;
		}
		new Thread() {
			public void run() {
				try {
					Message msg;
					if (!WebserviceFactory.checkWS()) {
						msg = Message.obtain();
						msg.what = 0;
						msg.obj = getString(R.string.cumm_twobill_webcheckPromptinfo);
						uploading = false;
						mUploadHandler.sendMessage(msg);
						return;
					}
					boolean isUpLoad = false;
					for (DJLine djLine : AppContext.resultDataFileListBuffer) {
						if (djLine.getLineID() == adjline.getLineID()) {
							isUpLoad = true;
							break;
						}
					}
					if (isUpLoad) {
						msg = Message.obtain();
						msg.what = 1;
						msg.obj = getString(R.string.cumm_cummdownload_startupload);
						mUploadHandler.sendMessage(msg);
						if (upLoadResultListData(adjline.getLineID())) {
							msg = Message.obtain();
							msg.what = 100;
							msg.arg1 = 3;
							msg.arg2 = position;
							msg.obj = adjline;
							mUploadHandler.sendMessage(msg);
						} else {
							msg = Message.obtain();
							msg.what = 100;
							msg.arg1 = 2;
							msg.obj = getString(R.string.cumm_cummdownload_uploadfinished);
							mUploadHandler.sendMessage(msg);
						}
					} else {
						msg = Message.obtain();
						msg.what = 100;
						msg.arg1 = 3;
						msg.arg2 = position;
						msg.obj = adjline;
						mUploadHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void initView() {
		ll_upLoad_statistics = (LinearLayout) findViewById(R.id.ll_upLoad_statistics);
		tv_defeated = (TextView) findViewById(R.id.tv_defeated);
		tv_defeated.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(CommDownloadNew.this,
						CommDownloadErrorResultBack.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("errorResult", upLoadFailingMap);
				intent.putExtras(bundle);
				startActivityForResult(intent, REQCODE_BACKUPERRORRESULT);
			}
		});

		lvCommLineListView = (SwipeMenuListView) findViewById(R.id.comm_download_new_listview_status);
		lvCommLineListView.setTag(downloadState);
		lvDJLineAdapter = new CommNew_DJLine_ListViewAdapter(this,
				AppContext.CommDJLineBuffer, R.layout.listitem_comm_djline,
				false, lvCommLineListView);
		lvCommLineListView.setAdapter(lvDJLineAdapter);
		lvCommLineListView
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						// TODO Auto-generated method stub
						switch (index) {
						case 0:// 下载
							ll_upLoad_statistics.setVisibility(View.GONE);
							upLoadOneSelectedLineData(position);
							break;
						}
						return false;
					}

				});
		
		//liseview点击事件
		lvCommLineListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//刷新item的Checkbox(是否勾选)
				if(AppContext.getPlanType().equals(
						AppConst.PlanType_DJPC)){
					lvDJLineAdapter.refreshItemCheckbox(position, lvCommLineListView);
					lvDJLineAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	/**
	 * 初始化头部视图
	 */
	private void initHeadViewAndMoreBar() {
		head_title = (TextView) findViewById(R.id.ll_comm_download_new_head_title);
		head_title.setText(getString(R.string.commu_receive_download_title));
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				goBack();
			}
		});
	}

	/**
	 * 初始化下载button
	 */
	private void initDownLoadButton() {
		downloadPlanButton = (Button) findViewById(R.id.btn_linedownload);
		downloadPlanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (uploading || downloading)
					return;
				if (AppContext.resultDataFileListBuffer != null
						&& AppContext.resultDataFileListBuffer.size() > 0
						&& !AppContext.getPlanType().equals(
								AppConst.PlanType_DJPC)) {
					uploading = true;
					upLoadResultThread();
				} else {
					try {
						downloading = true;
						downLoadCommLineThread(false, null, null);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void loadLineListData() {
		// 加载数据
		if (Thread.currentThread().isInterrupted())
			return;
		Context context = getBaseContext();
		DJLineHelper djLineHelper = new DJLineHelper();
		ReturnResultInfo returnInfo = djLineHelper.loadCommLineDataNew(context, appVersion);
	}

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

	/**
	 * 数据上传线程
	 */
	private void upLoadResultThread() {
		if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
			UIHelper.ToastMessage(getBaseContext(),
					R.string.network_not_connected);
			uploading = false;
			// downloading = false;
			return;
		}
		uploadThread = new Thread() {
			public void run() {
				Message msg = Message.obtain();
				if (!WebserviceFactory.checkWS()) {
					msg = Message.obtain();
					msg.what = 0;
					msg.obj = getString(R.string.cumm_twobill_webcheckPromptinfo);
					uploading = false;
					mUploadHandler.sendMessage(msg);
				} else {
					try {
						msg = Message.obtain();
						msg.what = 1;
						msg.obj = getString(R.string.cumm_cummdownload_startupload);
						mUploadHandler.sendMessage(msg);

						if (upLoadResultListData(-1)) {// 注释掉上传，现在是实时上传
							msg = Message.obtain();
							msg.what = 100;
							msg.arg1 = 1;
							msg.obj = getString(R.string.cumm_cummdownload_uploadfinished);
							mUploadHandler.sendMessage(msg);
						} else {
							msg = Message.obtain();
							msg.what = 100;
							msg.arg1 = 2;
							msg.obj = getString(R.string.cumm_cummdownload_uploadfinished);
							mUploadHandler.sendMessage(msg);
						}
						uploading = false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						msg = Message.obtain();
						e.printStackTrace();
						msg.what = -1;
						msg.obj = e;

						mUploadHandler.sendMessage(msg);
						uploading = false;
					}
				}
			}
		};
		uploadThread.start();
	}

	private void loadUploadProgBarHandler() {
		mUploadProgBarHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String mesString = String.valueOf(msg.obj);
					int progBarDoNum = (int) msg.arg1;
					pop_upload_progressBar.setProgress(progBarDoNum);
					if (msg.arg2 == 0) {
						addCommHint(true, mesString, false, "false");
					} else if (msg.arg2 == 1) {
						addCommHint(false, mesString, false, "false");
					} else if (msg.arg2 == 2) {
						addCommHint(true, mesString, false, "true");
					}
				} else if (msg.what == 2) {
					String mesString = String.valueOf(msg.obj);
					int progBarDoNum = (int) msg.arg1;
					pop_upload_progressBar.setProgress(progBarDoNum);
					addCommHint(true, mesString, false, "false");
				} else if (msg.what == -1) {
					// uploading = false;
					int progBarDoNum = (int) msg.arg1;
					pop_upload_progressBar.setProgress(progBarDoNum);
					addCommHint(true, String.valueOf(msg.obj), true, "true");
				} else if (msg.what == -2) {
					String mesString = String.valueOf(msg.obj);
					int progBarDoNum = (int) msg.arg1;
					pop_upload_progressBar.setProgress(progBarDoNum);
					addCommHint(true, mesString, true, "true");
				} else if (msg.what == -3) {
					String mesString = String.valueOf(msg.obj);
					int progBarDoNum = (int) msg.arg1;
					pop_upload_progressBar.setProgress(progBarDoNum);
					addCommHint(true, mesString, true, "true");
				}
			}
		};
	}

	Handler mProgBarHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				int progBarDoNum = (int) msg.arg1;
				int _pos = (int) msg.arg2;
				
				if(progBarDoNum != 100){//进度条为100后还要拼接lineList,所以不刷新
					downloadState.put(_pos, progBarDoNum);
					lvCommLineListView.setTag(downloadState);
					
					if (lvDJLineAdapter != null) {
						lvDJLineAdapter.notifyDataSetChanged();
					}
				}
			}
			if (msg.what == 2) {// 提示信息
				String obj = (String) msg.obj;
				if (!StringUtils.isEmpty(obj)) {
					UIHelper.ToastMessage(CommDownloadNew.this, obj);
				}
			}
			if(msg.what == 4){//单条下载完成
				int progBarDoNum = (int) msg.arg1;
				int _pos = (int) msg.arg2;
				
				downloadState.put(_pos, progBarDoNum);
				lvCommLineListView.setTag(downloadState);
				
				if (lvDJLineAdapter != null)
					lvDJLineAdapter.notifyDataSetChanged();
				
				commLineNeednedCount.setText(getString(
						R.string.download_message_count,
						String.valueOf(needDownLineList.size())));
				/*int _count = lvDJLineAdapter.getLineCount(_pos);
				commLineNeednedCount.setText(getString(
					R.string.download_message_count,_count));*/
			}
			if (msg.what == 3) {// 刷新路线信息（最后下载完的时候调1次）
				if (lvDJLineAdapter != null)
					lvDJLineAdapter.notifyDataSetChanged();
			}
			if(msg.what == -1){//lineList拼接失败
				int progBarDoNum = (int) msg.arg1;
				int _pos = (int) msg.arg2;

				downloadState.put(_pos, progBarDoNum);
				lvCommLineListView.setTag(downloadState);
				
				if (lvDJLineAdapter != null) {
					lvDJLineAdapter.notifyDataSetChanged();
				}
			}
		}
	};

	Handler mDwonHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == -1 && msg.obj != null) {
				UIHelper.ToastMessage(CommDownloadNew.this,
						getString(R.string.cumm_cummdownload_fail_promptinfo)
								+ msg.obj.toString());
			} else if (msg.what == -2 && msg.obj != null) {
				UIHelper.ToastMessage(CommDownloadNew.this, msg.obj.toString());
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 上传结果数据
	 * 
	 * @param upLoadLineId
	 *            (全部上传传-1,上传单条传需上传的路线id)
	 * @throws InterruptedException
	 */
	private boolean upLoadResultListData(int upLoadLineId)
			throws InterruptedException {
		Context context = getBaseContext();
		UpLoad uploadRes = new UpLoad(context);
		boolean allSuccess = true;
		upLoadFailingMap.clear();
		if (AppContext.resultDataFileListBuffer != null
				&& AppContext.resultDataFileListBuffer.size() > 0) {
			String tempString = uploadRes.GetNeedUploadFileInfo();
			boolean isUpPlan = tempString.toUpperCase().contains("|NEEDPLAN");
			for (DJLine _djLine : AppContext.resultDataFileListBuffer) {
				try {
					File _file = new File(_djLine.getResultDBPath());
					// 判断有没有结果库，有的话上传
					if (_file.exists()) {
						// 如果是单条上传，且lineID不是需要上传的路线id则跳过
						if (upLoadLineId != -1
								&& upLoadLineId != _djLine.getLineID()) {
							continue;
						}
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
						Thread.sleep(500);
						Message _msg = Message.obtain();
						_msg.what = 2;
						_msg.obj = getString(
								R.string.cumm_cummdownload_iszipfile,
								_djLine.getLineID());
						_msg.arg1 = 0;
						mUploadProgBarHandler.sendMessage(_msg);
						if (ZipUtils.zipFolder(
								_file.getParent(),
								AppConst.XSTZipResultPath()
										+ _djLine.getLineID())) {
							_msg = Message.obtain();
							_msg.what = 1;
							_msg.obj = getString(
									R.string.cumm_cummdownload_isuploadfile,
									String.valueOf(_djLine.getLineID()), "0/0");
							_msg.arg1 = 0;
							_msg.arg2 = 0;
							mUploadProgBarHandler.sendMessage(_msg);
						} else {
							_msg = Message.obtain();
							_msg.what = -2;
							_msg.obj = getString(
									R.string.cumm_cummdownload_iszipfileerror,
									_djLine.getLineID());
							_msg.arg1 = 0;
							mUploadProgBarHandler.sendMessage(_msg);
							allSuccess = false;
							upLoadFailingMap
									.put(_djLine,
											getString(
													R.string.cumm_cummdownload_iszipfileerror,
													_djLine.getLineID()));

							// 压缩失败，如果下载库在结果库文件夹中，则删除
							File file = new File(_file.getParent()
									+ File.separator
									+ AppConst.PlanDBName(_djLine.getLineID()));
							if (file.exists()) {
								file.delete();
							}
							// 插入操作日志
							OperatingConfigHelper
									.getInstance()
									.i(AppConst.CONNECTIONTYPE_WIFI
											+ AppConst.ANDROID_DATA_UPLOADING,
											AppConst.LOGSTATUS_ERROR,
											getString(
													R.string.cumm_cummdownload_iszipfileerror,
													_djLine.getLineID()));
							continue;
						}
						ReturnResultInfo returnInfo = uploadRes
								.UploadResultDBNew(
										appVersion,
										context,
										mUploadProgBarHandler,
										AppConst.XSTZipResultPath()
												+ _djLine.getLineID());
						if (returnInfo.getResult_YN()) {
							_msg = Message.obtain();
							_msg.what = 1;
							_msg.obj = getString(
									R.string.cumm_cummdownload_uploadfilesucced,
									_djLine.getLineID());
							_msg.arg1 = 0;
							_msg.arg2 = 2;

							// 清除上传文件夹
							String path = AppConst.XSTResultPath()
									+ _djLine.getLineID();
							FileUtils.clearFileWithPath(path);

							// 上传完临时数据后，有视频缩略图的话，删除缩略图
							File file = new File(
									AppConst.XSTTempThumbnailImagePath());
							if (_djLine.getLineID() == 0
									&& file.list().length > 0) {
								File[] files = file.listFiles();
								for (File f : files) {
									f.delete();
								}
							}
							mUploadProgBarHandler.sendMessage(_msg);

							// 插入操作日志
							OperatingConfigHelper
									.getInstance()
									.i(AppConst.CONNECTIONTYPE_WIFI
											+ AppConst.ANDROID_DATA_UPLOADING,
											AppConst.LOGSTATUS_NORMAL,
											getString(
													R.string.cumm_cummdownload_uploadfilesucced,
													_djLine.getLineID()));
						} else {
							_msg = Message.obtain();
							_msg.what = -1;
							_msg.obj = getString(
									R.string.cumm_cummdownload_uploadfilefailed,
									String.valueOf(_djLine.getLineID()));
							_msg.arg1 = 0;
							mUploadProgBarHandler.sendMessage(_msg);
							allSuccess = false;
							upLoadFailingMap.put(_djLine,
									returnInfo.getError_Message_TX());

							// 上传失败，如果下载库在结果库文件夹中，则删除
							File file = new File(_file.getParent()
									+ File.separator
									+ AppConst.PlanDBName(_djLine.getLineID()));
							if (file.exists()) {
								file.delete();
							}
							// 插入操作日志
							OperatingConfigHelper
									.getInstance()
									.i(AppConst.CONNECTIONTYPE_WIFI
											+ AppConst.ANDROID_DATA_UPLOADING,
											AppConst.LOGSTATUS_ERROR,
											getString(
													R.string.cumm_cummdownload_uploadfilefailed,
													String.valueOf(_djLine
															.getLineID()))
													+ "\n["
													+ returnInfo
															.getError_Message_TX()
													+ "]");
							continue;
						}
					}
				} catch (Exception e) {
					upLoadFailingMap.clear();
					Message _msg = Message.obtain();
					_msg.what = -3;
					_msg.obj = getString(R.string.cumm_cummdownload_uploadresultfilefailed)
							+ e.getMessage();
					_msg.arg1 = 0;
					mUploadProgBarHandler.sendMessage(_msg);
					// 插入操作日志
					OperatingConfigHelper.getInstance().i(
							AppConst.CONNECTIONTYPE_WIFI
									+ AppConst.ANDROID_DATA_UPLOADING,
							AppConst.LOGSTATUS_ERROR, e.getMessage());
					return false;
				}
			}
			Thread.sleep(800);
			return allSuccess;
		}
		return allSuccess;
	}

	/**
	 * 下载路线数据的线程
	 */
	private void downLoadCommLineThread(final boolean isSelected,
			final DJLine djLine, final Integer pos) throws AppException {
		if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
			UIHelper.ToastMessage(getBaseContext(),
					R.string.network_not_connected);
			downloading = false;
			return;
		}
		
		downloadThread = new Thread() {
			public void run() {
				if (!WebserviceFactory.checkWS()) {
					Message msg = Message.obtain();
					msg.what = -2;
					msg.obj = getString(R.string.cumm_twobill_webcheckPromptinfo);
					mDwonHandler.sendMessage(msg);
					downloading = false;
				} else {
					if (!isSelected)
						downLoadLineListData();
					else {
						if (!djLine.getBuidYN()) {
							UIHelper.ThreadToastMessage(CommDownloadNew.this, getString(R.string.line_list_buildTime_noBuild), mProgBarHandler);
							downloading = false;
							return;
						}
						downLoadOneSelectedLineData(djLine, pos);
					}
					Message msg = Message.obtain();
					msg.what = 3;
					mProgBarHandler.sendMessage(msg);
				}
				AppContext.SetRefreshLineYN(true);
			}
		};
		downloadThread.start();
	}

	/**
	 * 下载计划数据
	 */
	@SuppressWarnings("unchecked")
	private void downLoadLineListData() {
		// 下载
		Message msg = Message.obtain();
		Context context = getBaseContext();
		DownLoad downLoad = new DownLoad(context);

		try {
			if (needDownLineList != null && needDownLineList.size() > 0) {
				DJLine value = null;
				int i = 0;
				// 获取勾选的路线
				List<String> SelectedList = lvDJLineAdapter.getSelectedLineID();
				if (SelectedList.size() == 0
						&& AppContext.getPlanType().equals(
								AppConst.PlanType_DJPC)) {
					Message _msg = Message.obtain();
					_msg.what = 2;
					_msg.obj = getString(R.string.download_message_selected_hint);
					mProgBarHandler.sendMessage(_msg);
				}
				
				for (i = 0; i < AppContext.CommDJLineBuffer.size(); i++) {
					if (this.isFinishing())
						return;
					if (AppContext.CommDJLineBuffer.get(i).getneedUpdate() == true) {
						// 如果是点检排程,且没勾选当前任务，则跳过该路线
						if (!SelectedList.contains(Integer
								.toString(AppContext.CommDJLineBuffer.get(i)
										.getLineID()))
								&& AppContext.getPlanType().equals(
										AppConst.PlanType_DJPC)) {
							continue;
						}
						// 合并newUserList
						if (!isRepairXML) {
							isRepairXML = DataTransHelper
									.RepairUser(AppContext.getPlanType().equals(
											AppConst.PlanType_XDJ) ? AppConst.PlanType_XDJ
											: AppConst.PlanType_DJPC);
						}
						value = AppContext.CommDJLineBuffer.get(i);

						int _pos = i;

						View currView = new View(CommDownloadNew.this);
						Message _msg = Message.obtain();
						try {
							_msg = Message.obtain();
							_msg.what = 1;
							_msg.obj = currView;
							_msg.arg1 = -1;
							_msg.arg2 = _pos;
							mProgBarHandler.sendMessage(_msg);
							ReturnResultInfo returnInfo = new ReturnResultInfo();
							if (AppContext.getPlanType().equals(
									AppConst.PlanType_XDJ)) {// 巡检下载
								returnInfo = downLoad.RequestPlanDownLoad(
										AppContext.GetxjqCD(), value);
							} else if (AppContext.getPlanType().equals(
									AppConst.PlanType_DJPC)) {// 点检排程
								String Value = AppContext.DJPCLineMap
										.get(String.valueOf(value.getLineID()));
								if (Value
										.contains(getString(R.string.download_state_encodeErr))) {
									returnInfo.setResult_YN(false);
									returnInfo.setError_Message_TX(Value);
								} else {
									returnInfo.setResult_YN(true);
									returnInfo.setResult_Content_TX(Value);
								}
							}
							
							if (returnInfo.getResult_YN()) {
								_msg = Message.obtain();
								_msg.what = 1;
								_msg.obj = currView;
								_msg.arg1 = 0;
								_msg.arg2 = _pos;
								mProgBarHandler.sendMessage(_msg);
								boolean b = downLoad.DownLoadPlanDB(context,
										currView, _pos, value, mProgBarHandler,
										returnInfo.getResult_Content_TX());
								if (b) {//下载成功
									//拼接LineList
									String isMerge=DownLoad.UpdateLineListFile(String.valueOf(AppContext.CommDJLineBuffer.get(i).getLineID()));
									
									if(StringUtils.toBool(isMerge)){
										AppContext.CommDJLineBuffer.get(i)
												.setneedUpdate(false);
		
										needDownLineList
												.remove(AppContext.CommDJLineBuffer
														.get(i).getLineID());
		
										// 插入操作日志
										OperatingConfigHelper
												.getInstance()
												.i(AppConst.CONNECTIONTYPE_WIFI
														+ AppConst.ANDROID_DATA_DOWNLOAD,
														AppConst.LOGSTATUS_NORMAL,
														AppContext.CommDJLineBuffer
																.get(i)
																.getLineName());
										//单条下载完成
										_msg = Message.obtain();
										_msg.what = 4;
										_msg.arg1 = 100;
										_msg.arg2 = _pos;
										mProgBarHandler.sendMessage(_msg);
									}else{//拼接出错视为下载失败
										
										// 插入操作日志
										OperatingConfigHelper
												.getInstance()
												.i(AppConst.CONNECTIONTYPE_WIFI
														+ AppConst.ANDROID_DATA_DOWNLOAD,
														AppConst.LOGSTATUS_ERROR,
														AppContext.CommDJLineBuffer
																.get(i).getLineName()
																+ "\n["+ isMerge+ "]");
										
										//单条下载失败（拼接失败）
										_msg = Message.obtain();
										_msg.what = -1;
										_msg.obj = currView;
										_msg.arg1 = -3;
										_msg.arg2 = _pos;
										mProgBarHandler.sendMessage(_msg);
									}
									
								}
							} else {
								_msg = Message.obtain();
								_msg.what = 1;
								_msg.obj = currView;
								_msg.arg1 = -2;
								_msg.arg2 = _pos;
								mProgBarHandler.sendMessage(_msg);

								// 插入操作日志
								OperatingConfigHelper
										.getInstance()
										.i(AppConst.CONNECTIONTYPE_WIFI
												+ AppConst.ANDROID_DATA_STANDARD_ERROR,
												AppConst.LOGSTATUS_ERROR,
												AppContext.CommDJLineBuffer
														.get(i).getLineName()
														+ "\n["
														+ returnInfo
																.getError_Message_TX()
														+ "]");
							}
							Thread.sleep(500);
						} catch (Exception e) {
							_msg = Message.obtain();
							_msg.what = 1;
							_msg.obj = currView;
							_msg.arg1 = -3;
							_msg.arg2 = _pos;
							mProgBarHandler.sendMessage(_msg);

							// 插入操作日志
							OperatingConfigHelper.getInstance().i(
									AppConst.CONNECTIONTYPE_WIFI
											+ AppConst.ANDROID_DATA_DOWNLOAD,
									AppConst.LOGSTATUS_ERROR,
									AppContext.CommDJLineBuffer.get(i)
											.getLineName()
											+ "\n["
											+ e.getMessage() + "]");
							continue;
						}

						/*
						 * for (DJLine djLine : AppContext.CommDJLineBuffer) {
						 * if
						 * (String.valueOf(djLine.getLineID()).equals(String.valueOf
						 * (value.getLineID()))) djLine.setneedUpdate(false); }
						 */
					}
				}
				
			} else {
				if (AppContext.CommDJLineBuffer.size() <= 0) {
					msg = Message.obtain();
					msg.what = -2;
					msg.obj = getString(R.string.drawleft_msg_noline);
				} else {
					msg = Message.obtain();
					msg.what = -2;
					msg.obj = getString(R.string.cumm_cummdownload_planislast);
				}
				mDwonHandler.sendMessage(msg);
				// if (!stopThreadYN)
				// downLoad.UpdateLineListFile();
			}
		} catch (Exception e) {
			msg = Message.obtain();
			msg.what = -1;
			msg.obj = "[" + e.getMessage() + "]";
			mDwonHandler.sendMessage(msg);
			// TODO: handle exception
		} finally {
			downloading = false;
		}
	}

	@SuppressWarnings("unchecked")
	private void downLoadOneSelectedLineData(DJLine djLine, Integer pos) {
		Message msg = Message.obtain();
		Context context = getBaseContext();
		DownLoad downLoad = new DownLoad(context);

		try {
			Integer key = pos;
			View currView = lvDJLineAdapter.getViewByPosition(pos,
					lvCommLineListView);
			Message _msg = Message.obtain();
			try {
				_msg = Message.obtain();
				_msg.what = 1;
				_msg.obj = currView;
				_msg.arg1 = -1;
				_msg.arg2 = pos;
				mProgBarHandler.sendMessage(_msg);
				
				// 合并newUserList
				if (!isRepairXML) {
					isRepairXML = DataTransHelper
							.RepairUser(AppContext.getPlanType().equals(
									AppConst.PlanType_XDJ) ? AppConst.PlanType_XDJ
									: AppConst.PlanType_DJPC);
				}
				
				// String dbFileNameString = "";
				ReturnResultInfo returnInfo = new ReturnResultInfo();
				if (AppContext.getPlanType().equals(AppConst.PlanType_XDJ)) {// 巡检下载
					returnInfo = downLoad.RequestPlanDownLoad(
							AppContext.GetxjqCD(), djLine);
				} else if (AppContext.getPlanType().equals(
						AppConst.PlanType_DJPC)) {// 点检排程
					String Value = AppContext.DJPCLineMap.get(String.valueOf(djLine.getLineID()));
					if (Value
							.contains(getString(R.string.download_state_encodeErr))) {
						returnInfo.setResult_YN(false);
						returnInfo.setError_Message_TX(Value);
					} else {
						returnInfo.setResult_YN(true);
						returnInfo.setResult_Content_TX(Value);
					}
				}

				if (returnInfo.getResult_YN()) {
					_msg = Message.obtain();
					_msg.what = 1;
					_msg.obj = currView;
					_msg.arg1 = 0;
					_msg.arg2 = pos;
					mProgBarHandler.sendMessage(_msg);

					boolean b = downLoad.DownLoadPlanDB(context, currView, key,
							djLine, mProgBarHandler,
							returnInfo.getResult_Content_TX());
					if (b) {//下载完成
						//拼接LineList
						String isMerge=DownLoad.UpdateLineListFile(String.valueOf(djLine.getLineID()));
						
						if(StringUtils.toBool(isMerge)){//拼接成功则算真正下载成功
							
							for (DJLine mLine : AppContext.CommDJLineBuffer) {
								if (String.valueOf(mLine.getLineID()).equals(
										String.valueOf(djLine.getLineID())))
									mLine.setneedUpdate(false);
							}
							needDownLineList.remove(djLine.getLineID());
							
							// 插入操作日志
							OperatingConfigHelper.getInstance()
									.i(AppConst.CONNECTIONTYPE_WIFI
											+ AppConst.ANDROID_DATA_DOWNLOAD,
											AppConst.LOGSTATUS_NORMAL,
											djLine.getLineName());
							
							//单条下载完成
							_msg = Message.obtain();
							_msg.what = 4;
							_msg.arg1 = 100;
							_msg.arg2 = pos;
							mProgBarHandler.sendMessage(_msg);
							
						}else{//拼接出错视为下载失败
							
							// 插入操作日志
							OperatingConfigHelper
									.getInstance()
									.i(AppConst.CONNECTIONTYPE_WIFI
											+ AppConst.ANDROID_DATA_DOWNLOAD,
											AppConst.LOGSTATUS_ERROR,
											djLine.getLineName()
													+ "\n["+ isMerge+ "]");
							
							//单条下载失败（拼接失败）
							_msg = Message.obtain();
							_msg.what = -1;
							_msg.obj = currView;
							_msg.arg1 = -3;
							_msg.arg2 = pos;
							mProgBarHandler.sendMessage(_msg);
						}
						
						
					}
				} else {
					_msg = Message.obtain();
					_msg.what = 1;
					_msg.obj = currView;
					_msg.arg1 = -2;
					_msg.arg2 = pos;
					mProgBarHandler.sendMessage(_msg);

					// 插入操作日志
					OperatingConfigHelper.getInstance().i(
							AppConst.CONNECTIONTYPE_WIFI
									+ AppConst.ANDROID_DATA_STANDARD_ERROR,
							AppConst.LOGSTATUS_ERROR,
							djLine.getLineName() + "\n["
									+ returnInfo.getError_Message_TX() + "]");

					return;
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				_msg = Message.obtain();
				_msg.what = 1;
				_msg.obj = currView;
				_msg.arg1 = -3;
				_msg.arg2 = pos;
				mProgBarHandler.sendMessage(_msg);

				// 插入操作日志
				OperatingConfigHelper.getInstance().i(
						AppConst.CONNECTIONTYPE_WIFI
								+ AppConst.ANDROID_DATA_DOWNLOAD,
						AppConst.LOGSTATUS_ERROR,
						djLine.getLineName() + "\n[" + e.getMessage() + "]");

				return;
			}
		} catch (Exception e) {
			msg = Message.obtain();
			msg.what = -1;
			msg.obj = "[" + e.getMessage() + "]";
			mDwonHandler.sendMessage(msg);
			// TODO: handle exception
		} finally {
			downloading = false;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		instance = null;
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

	private void goBack() {
		if (uploading || downloading) {// 如果正在上传或下载不能退出
			if (uploadResultPW == null || !uploadResultPW.isShowing()) {
				UIHelper.ToastMessage(CommDownloadNew.this,
						getString(R.string.cumm_usb_communicating));
			}
			return;
		}
		// 如果正在下载
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
									AppContext.CommDJLineBuffer.clear();
									AppManager.getAppManager().finishActivity(
											CommDownloadNew.this);
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
								}
							}).show();
		} else {
			AppContext.CommDJLineBuffer.clear();
			if(isRepairXML){//如果做过通信则清理数据
				DataTransHelper.cleanUserListAndDB(AppContext.getPlanType().equals(
						AppConst.PlanType_XDJ) ? AppConst.PlanType_XDJ
						: AppConst.PlanType_DJPC);
			}
			
			AppManager.getAppManager().finishActivity(CommDownloadNew.this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQCODE_BACKUPERRORRESULT) {
			if (resultCode == Activity.RESULT_OK) {
				upLoadFailingMap = (HashMap<DJLine, String>) (intent
						.getExtras().get("errorResult"));
				if (upLoadFailingMap != null && upLoadFailingMap.size() <= 0)
					ll_upLoad_statistics.setVisibility(View.GONE);
			}
		}
	}
}