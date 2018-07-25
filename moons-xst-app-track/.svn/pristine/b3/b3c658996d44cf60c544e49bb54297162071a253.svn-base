package com.moons.xst.track.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJPCUserAdapter;
import com.moons.xst.track.bean.CommUser;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.ResponseLogInfo;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.ResponseLogInfoHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.communication.UpLoad;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.widget.LoadingDialog;

import de.greenrobot.event.EventBus;

public class CommDJPCDownload extends BaseActivity {

	public static final int REQCODE_BACKUPERRORRESULT = 1;// 备份错误结果
	
	private Gson gson;
	private GsonBuilder builder;
	private LoadingDialog loading;
	private String appVersion = "";

	ImageButton returnButton, query_djline_serach;
	ListView lv_user;
	DJPCUserAdapter adapter;
	TextView tv_defeated;
	LinearLayout ll_upLoad_statistics;

	// 2641人员list
	List<CommUser> list = new ArrayList<CommUser>();
	// 是否正在上传标记
	private boolean uploading = false;
	// 保存上传失败信息，key为路线value为信息
	private HashMap<DJLine, String> upLoadFailingMap = new HashMap<DJLine, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_djpc_download_layout);
		EventBus.getDefault().register(this);
		appVersion = AppContext.getAppVersion(this);
		// 初始化上传提示框
		initPopupwindow();
		init();
		// 上传数据
		new Thread(){
			public void run(){
				uploading=true;
				upLoadResultListData();
				uploading=false;
			}
		}.start();
	}

	public void init() {
		builder = new GsonBuilder();
		gson = builder.create();
		// 查询
		query_djline_serach = (ImageButton) findViewById(R.id.query_djline_serach);
		query_djline_serach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				// 搜索按钮点击事件
				CommonSearchBean<CommUser> commonSearchBean = new CommonSearchBean<CommUser>();
				commonSearchBean.setList(list);
				commonSearchBean.setSearchType(AppConst.SearchType.CommUser
						.toString());
				commonSearchBean
						.setHint(getString(R.string.commu_djpc_condition_name_number_hint));
				Intent intent = new Intent(CommDJPCDownload.this,
						UnifySearchAty.class);
				intent.putExtra("seachBean", (Serializable) commonSearchBean);
				startActivity(intent);
			}
		});
		lv_user = (ListView) findViewById(R.id.lv_user);
		lv_user.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				CommUser entity = list.get(position);
				SkipActivity(entity);
			}
		});

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager()
						.finishActivity(CommDJPCDownload.this);
			}
		});
		
		ll_upLoad_statistics=(LinearLayout) findViewById(R.id.ll_upLoad_statistics);
		tv_defeated=(TextView) findViewById(R.id.tv_defeated);
		tv_defeated.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(CommDJPCDownload.this, CommDownloadErrorResultBack.class);
				Bundle bundle = new Bundle();
                bundle.putSerializable("errorResult",upLoadFailingMap);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQCODE_BACKUPERRORRESULT);
			}
		});
		
	}

	// 加载人员信息
	private void loadUser() {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		// 获取人员信息
		new Thread() {
			@Override
			public void run() {
				list = WebserviceFactory
						.getQueryUser(CommDJPCDownload.this, "");
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null && loading.isShowing()) {
				loading.dismiss();
			}
			isCommunication=false;
			if (msg.what == 1) {// 2641显示人员
				adapter = new DJPCUserAdapter(CommDJPCDownload.this, list,
						R.layout.djpc_user_item);
				lv_user.setAdapter(adapter);
			}
			if (msg.what == 2) {// 2641标准化完成
				UIHelper.showCommuDownLoad(CommDJPCDownload.this);
				AppManager.getAppManager()
					.finishActivity(CommDJPCDownload.this);
			}
			if (msg.what == -1) {// 提示信息
				if (msg.obj != null) {
					String msgString = String.valueOf(msg.obj);
					LayoutInflater factory = LayoutInflater.from(CommDJPCDownload.this);
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					new com.moons.xst.track.widget.AlertDialog(CommDJPCDownload.this)
							.builder()
							.setTitle(getString(R.string.tips))
							.setView(view)
							.setMsg(msgString)
							.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {								
								@Override
								public void onClick(View v) {
									AppManager.getAppManager().finishActivity(
											CommDJPCDownload.this);
								}
					}).setCanceledOnTouchOutside(false).setCancelable(false).show();
				}
			}
		};
	};

	//上传Handler
	Handler mUploadProgBarHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==4){//开始上传
				//ll_upLoad_statistics.setVisibility(View.GONE);
				uploadResultPW.showAsDropDown(parentView, 0, 0);
				popupload_comm_hint.removeAllViews();
				pop_upload_progressBar.setProgress(0);
				popStartupload
						.setText(getString(R.string.cumm_cummdownload_headtitle_promptinfo));
			}
			if(msg.what==1){
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
			}
			if(msg.what==2){//正在压缩
				String mesString = String.valueOf(msg.obj);
				int progBarDoNum = (int) msg.arg1;
				pop_upload_progressBar.setProgress(progBarDoNum);
				addCommHint(true, mesString, false, "false");
			}
			if(msg.what==3){//上传结束
				if (uploadResultPW != null && uploadResultPW.isShowing())
					uploadResultPW.dismiss();
				
				if (upLoadFailingMap.size() > 0) {//有失败文件
					ll_upLoad_statistics.setVisibility(View.VISIBLE);
					tv_defeated.setText(getString(
							R.string.cumm_cummdownload_downloadfail_count,
							upLoadFailingMap.size()));
				}else{
					loadUser();
				}
			}
			else if (msg.what == -1) {//上传失败
				int progBarDoNum = (int) msg.arg1;
				pop_upload_progressBar.setProgress(progBarDoNum);
				addCommHint(true, String.valueOf(msg.obj), true, "true");
			}
			if (msg.what == -2) {//压缩失败
				String mesString = String.valueOf(msg.obj);
				int progBarDoNum = (int) msg.arg1;
				pop_upload_progressBar.setProgress(progBarDoNum);
				addCommHint(true, mesString, true, "true");
			} 
			if (msg.what == -3) {//上传异常
				String mesString = String.valueOf(msg.obj);
				int progBarDoNum = (int) msg.arg1;
				pop_upload_progressBar.setProgress(progBarDoNum);
				addCommHint(true, mesString, true, "true");
				
				ll_upLoad_statistics.setVisibility(View.VISIBLE);
				tv_defeated.setText(getString(R.string.cumm_cummdownload_downloadfail_hint));
				
				if (uploadResultPW != null && uploadResultPW.isShowing())
					uploadResultPW.dismiss();
			}
			if(msg.what==-4){//Web服务不可用
				UIHelper.ToastMessage(CommDJPCDownload.this, msg.obj.toString());
				AppManager.getAppManager()
					.finishActivity(CommDJPCDownload.this);
		}
		};
	};

	// 创建tempLineList
	private void CreationLineXML(String xmlLineStr) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		File file = new File(AppConst.XSTBasePath(),AppConst.DJLineTempXmlFile);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			StringBuffer sb = new StringBuffer();
			// 添加xml头
			sb.append("<?xml version='1.0'?>");
			// 添加根节点
			sb.append("<NewDataSet>");

			StringReader stringReader = new StringReader(xmlLineStr);
			InputSource inputSource = new InputSource(stringReader);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputSource);
			doc.normalize();
			NodeList tableList = doc.getElementsByTagName("Table");
			for (int i = 0; i < tableList.getLength(); i++) {
				sb.append("<Table>");
				Node table = tableList.item(i);
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeName().toUpperCase().equals("LINE_ID")) {
						sb.append("<LINE_ID>");
						sb.append(node.getTextContent());
						sb.append("</LINE_ID>");
					} else if (node.getNodeName().toUpperCase()
							.equals("NAME_TX")) {
						sb.append("<NAME_TX>");
						sb.append(node.getTextContent());
						sb.append("</NAME_TX>");
					} else if (node.getNodeName().toUpperCase()
							.equals("STANDARD_TM")) {
						sb.append("<BUILD_TM>");
						sb.append(node.getTextContent());
						sb.append("</BUILD_TM>");
						sb.append("<STANDARD_TM>");
						sb.append(node.getTextContent());
						sb.append("</STANDARD_TM>");
					} else if (node.getNodeName().toUpperCase()
							.equals("BUILD_YN")) {
						sb.append("<BUILD_YN>");
						sb.append(node.getTextContent());
						sb.append("</BUILD_YN>");
					}
				}
				sb.append("<IDTYPE_CD>");
				sb.append("R");
				sb.append("</IDTYPE_CD>");
				sb.append("<PCTYPE_CD>");
				sb.append("1");
				sb.append("</PCTYPE_CD>");
				sb.append("<XJQ_ID>");
				sb.append("");
				sb.append("</XJQ_ID>");
				sb.append("<XJQ_CD>");
				sb.append("");
				sb.append("</XJQ_CD>");
				sb.append("<XJQTYPE_CD>");
				sb.append("3TH");
				sb.append("</XJQTYPE_CD>");
				sb.append("</Table>");
			}
			sb.append("</NewDataSet>");
			fos.write(sb.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 创建userxml
	public void CreationUserXML(String xmlUserStr) {
		File file = new File(AppConst.XSTBasePath(), AppConst.UserXmlFile);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			StringBuffer sb = new StringBuffer();
			sb.append(xmlUserStr);
			fos.write(sb.toString().getBytes());
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 搜索回调事件
	public void onEvent(CommUser entity) {
		SkipActivity(entity);
	}

	boolean isCommunication=false;
	private void SkipActivity(CommUser entity) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		final String json = gson.toJson(entity, CommUser.class);
		new Thread() {
			@Override
			public void run() {
				if(isCommunication){
					return;
				}else{
					isCommunication=true;
				}
				
				if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
					Message msg = new Message();
					msg.what = -1;
					msg.obj = getString(R.string.network_not_connected);
					mHandler.sendMessage(msg);
					return;
				}
				if (!WebserviceFactory.checkWS()) {
					Message msg = new Message();
					msg.what = -1;
					msg.obj = getString(R.string.cumm_twobill_webcheckPromptinfo);
					mHandler.sendMessage(msg);
					return;
				} 
				// 点检排程下载路线xml以及标准化路线
				ReturnResultInfo ReturnInfo = WebserviceFactory
						.AndroidInitDJPCLine(json);
				if (ReturnInfo.getResult_YN()) {
					// 转载路线下载地址
					try {
						AppContext.DJPCLineMap.clear();
						JSONArray jsonArray = new JSONArray(
								ReturnInfo.getExResult1_TX());
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String lineId = jsonObject.getString("LineId");
							String error = jsonObject.getString("errorHint");
							if (StringUtils.isEmpty(error)) {
								String url = jsonObject
										.getString("DownloadUrl");
								AppContext.DJPCLineMap.put(lineId, url);
							} else {
								AppContext.DJPCLineMap
										.put(lineId,
												getString(R.string.download_state_encodeErr)
														+ "：" + error);
							}
						}
					} catch (JSONException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					// 创建tempLineList.xml
					CreationLineXML(ReturnInfo.getResult_Content_TX());
					// 创建UserList.xml
					CreationUserXML(ReturnInfo.getExResult2_TX());

					Message msg = new Message();
					msg.what = 2;
					mHandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = -1;
					msg.obj = ReturnInfo.getError_Message_TX();
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	};
	
	
	/**
	 * 上传结果数据
	 * @throws InterruptedException
	 */
	private void upLoadResultListData(){
		try{
			Context context = getBaseContext();
			UpLoad uploadRes = new UpLoad(context);
			upLoadFailingMap.clear();
			if (AppContext.resultDataFileListBuffer != null
					&& AppContext.resultDataFileListBuffer.size() > 0) {
				//Web服务不可用
				if (!WebserviceFactory.checkWS()) {
					Message _msg = Message.obtain();
					_msg.what = -4;
					_msg.obj = getString(R.string.cumm_twobill_webcheckPromptinfo);;
					mUploadProgBarHandler.sendMessage(_msg);
					return;
				}else{//开始上传
					Message msg = Message.obtain();
					msg.what = 4;
					msg.obj = getString(R.string.cumm_cummdownload_startupload);
					mUploadProgBarHandler.sendMessage(msg);
				}
				
				String tempString = uploadRes.GetNeedUploadFileInfo();
				boolean isUpPlan = tempString.toUpperCase().contains("|NEEDPLAN");
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
						return;
					}
				}
				Thread.sleep(800);
			}
			//上传结束
			Message _msg = Message.obtain();
			_msg.what = 3;
			mUploadProgBarHandler.sendMessage(_msg);	
		}catch(Exception e){
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
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(!uploading){
				AppManager.getAppManager()
					.finishActivity(CommDJPCDownload.this);
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
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
