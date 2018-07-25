package com.moons.xst.track.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJPCInterfaceInfoAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.DJPCInterfaceResultInfo;
import com.moons.xst.track.bean.KeyValueInfo;
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
import com.moons.xst.track.widget.ActionSheetDialog;
import com.moons.xst.track.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.moons.xst.track.widget.ActionSheetDialog.SheetItemColor;
import com.moons.xst.track.widget.LoadingDialog;

public class CommDJPCDownloadInterfaceAty extends BaseActivity {

	LinearLayout ll_condition;
	ImageButton returnButton, query_line;

	private LoadingDialog loading;
	private ProgressDialog mProDialog;// 正在标准化

	// 搜索条件list
	List<String> searchList = new ArrayList<String>();
	// 接口类型
	String interfaceType = "";
	// 查询条件
	String conditions = "";
	DJPCInterfaceInfoAdapter adapter;
	Button btn_standardization;
	ListView lv_result;
	Gson gson;
	List<DJPCInterfaceResultInfo> listInfo = new ArrayList<DJPCInterfaceResultInfo>();
	
	// 进度条
	private ProgressBar mProgress;
	// 显示上传数值
	private TextView mProgressText;
	// 上传对话框
	private com.moons.xst.track.widget.AlertDialog Dialog;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_djpc_download_interface);
		
		if (savedInstanceState != null) {
			// 这个是之前保存的数据
			interfaceType = savedInstanceState.getString("interfaceType");
			conditions = savedInstanceState.getString("conditions");
		} else {
			// 这个是从另外一个界面进入这个时传入的
			interfaceType = getIntent().getStringExtra("interfaceType");
			conditions = getIntent().getStringExtra("conditions");
		}
		
		gson = new GsonBuilder().serializeNulls().disableHtmlEscaping()
				.create();
		String historyString=FileUtils.read(AppConst.HistoryQueryPath());
		if(!StringUtils.isEmpty(historyString)){
			HistoryMap = gson.fromJson(historyString, HistoryMap.getClass());
		}
		// 初始化控件
		init();
		// 加载条件
		loadCondition();
		// 上传数据
		upLoadData();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO
		super.onSaveInstanceState(outState);
		outState.putString("interfaceType", interfaceType);
		outState.putString("conditions", conditions);
	}

	private void init() {

		// 查询条件Layout
		ll_condition = (LinearLayout) findViewById(R.id.ll_condition);
		lv_result = (ListView) findViewById(R.id.lv_result);

		// 查询路线
		query_line = (ImageButton) findViewById(R.id.query_line);
		query_line.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!NetWorkHelper.isNetworkAvailable(CommDJPCDownloadInterfaceAty.this)) {
					UIHelper.ToastMessage(CommDJPCDownloadInterfaceAty.this,
							R.string.network_not_connected);
					return;
				}
				
				if (loading == null || !loading.isShowing()) {
					loading = new LoadingDialog(CommDJPCDownloadInterfaceAty.this);
					loading.setCanceledOnTouchOutside(false);
					loading.setCancelable(false);
					loading.show();
				}
				if (interfaceType.equals(AppConst.DJPCCommType_huarun)) {// 华润接口查询路线
					List<KeyValueInfo> list = new ArrayList<KeyValueInfo>();
					for (int i = 0; i < ll_condition.getChildCount(); i++) {
						KeyValueInfo entity = new KeyValueInfo();
						EditText et_hint = (EditText) ll_condition
								.getChildAt(i).findViewById(R.id.et_search);
						String key=et_hint.getTag().toString();
						String value=et_hint.getText().toString();
						entity.setKey(key);
						if (StringUtils.isEmpty(value)) {
							entity.setValue("");
							entity.setValueEmptyYN("false");
						} else {
							entity.setValue(value);
							entity.setValueEmptyYN("true");
						}
						list.add(entity);
						//保存查询记录到map
						if(!StringUtils.isEmpty(value)){
							if(HistoryMap.containsKey(key)){
								HistoryMap.put(key,HistoryMap.get(key)+"@"+value);
							}else{
								HistoryMap.put(key,value);
							}
						}
					}
					try {
						//保存历史查询记录到本地
						FileUtils.SaveToFile(AppConst.XSTBasePath(), "history_query.txt", gson.toJson(HistoryMap), false);
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					final String json = gson.toJson(list);

					new Thread() {
						@Override
						public void run() {
							try {
								Message msg = new Message();
								if (!WebserviceFactory.checkWS()) {
									msg.what = -1;
									msg.obj = getString(R.string.cumm_twobill_webcheckPromptinfo);
									mHandler.sendMessage(msg);
								} else {
									String result = WebserviceFactory
											.getDJPCItemList(json);
									JSONArray array = new JSONArray(result);
									listInfo.clear();
									for (int i = 0; i < array.length(); i++) {
										DJPCInterfaceResultInfo entity = new DJPCInterfaceResultInfo();
										JSONObject object = array.getJSONObject(i);
										entity.setDJPCItemID(object
												.getString("DJPCItemID"));
										entity.setDJPCItemCD(object
												.getString("DJPCItemCD"));
										entity.setDJPCItemTX(object
												.getString("DJPCItemTX"));
										entity.setDJPCItemTransTX(object
												.getString("DJPCItemTransTX"));
										listInfo.add(entity);
									}								
									msg.what = 2;
									mHandler.sendMessage(msg);
								}
							} catch (Exception e) {
								Message msg = new Message();
								msg.what = -1;
								msg.obj = e.getMessage();
								mHandler.sendMessage(msg);
							}
						}
					}.start();
				}
			}
		});
		// 标准化
		btn_standardization = (Button) findViewById(R.id.btn_standardization);
		btn_standardization.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(adapter == null || adapter.getList().size() <= 0){
					return;
				}
				if (!NetWorkHelper.isNetworkAvailable(CommDJPCDownloadInterfaceAty.this)) {
					UIHelper.ToastMessage(CommDJPCDownloadInterfaceAty.this,
							R.string.network_not_connected);
					return;
				}
				new Thread() {
					@Override
					public void run() {
						Message msg = new Message();
						if (!WebserviceFactory.checkWS()) {
							msg = new Message();
							msg.what = -1;
							msg.obj = getString(R.string.cumm_twobill_webcheckPromptinfo);
							mHandler.sendMessage(msg);
							return;
						}
						List<DJPCInterfaceResultInfo> listInfo = adapter
								.getList();
						AppContext.DJPCLineMap.clear();
						String line = "";
						for (int i = 0; i < listInfo.size(); i++) {
							if (listInfo.get(i).getIsSelected()) {
								msg = new Message();
								msg.what = 3;
								msg.obj = getString(R.string.commu_djpc_standardization_hint)
										+ listInfo.get(i).getDJPCItemTX();
								mHandler.sendMessage(msg);
								List<DJPCInterfaceResultInfo> list = new ArrayList<DJPCInterfaceResultInfo>();
								list.add(listInfo.get(i));
								String json = gson.toJson(list);
								ReturnResultInfo ReturnInfo = WebserviceFactory
										.StartDownDJPCLineForInterface(json);
								if (ReturnInfo.getResult_YN()) {
									try {
										JSONArray jsonArray = new JSONArray(
												ReturnInfo
														.getResult_Content_TX());
										for (int j = 0; j < jsonArray.length(); j++) {
											JSONObject jsonObject = jsonArray
													.getJSONObject(j);
											String lineId = jsonObject
													.getString("LineId");
											line = line + lineId + "@";
											String error = jsonObject
													.getString("errorHint");
											if (StringUtils.isEmpty(error)) {
												String url = jsonObject
														.getString("DownloadUrl");
												AppContext.DJPCLineMap.put(
														lineId, url);
											} else {
												AppContext.DJPCLineMap
														.put(lineId,
																getString(R.string.download_state_encodeErr)
																		+ "："
																		+ error);
											}
										}
										
									} catch (JSONException e) {
										msg = new Message();
										msg.what = -1;
										msg.obj = e.getMessage();
										mHandler.sendMessage(msg);
									}
								} else {
									msg = new Message();
									msg.what = -1;
									msg.obj = ReturnInfo.getError_Message_TX();
									mHandler.sendMessage(msg);
								}
							}
						}
						//没勾选人员
						if(StringUtils.isEmpty(line)){
							return;
						}
						
						ReturnResultInfo ReturnInfo = WebserviceFactory
								.getDJPCLineAndUserXML(line);
						if (ReturnInfo.getResult_YN()) {
							CreationUserXML(ReturnInfo.getResult_Content_TX(),
									AppConst.UserXmlFile, true);
							CreationUserXML(ReturnInfo.getExResult1_TX(),
									AppConst.DJLineTempXmlFile, false);
						} else {
							msg = new Message();
							msg.what = -1;
							msg.obj = ReturnInfo.getError_Message_TX();
							mHandler.sendMessage(msg);
						}
						msg = new Message();
						msg.what = 4;
						mHandler.sendMessage(msg);
					}
				}.start();
			}
		});
		// 退出
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(
						CommDJPCDownloadInterfaceAty.this);
			}
		});
	}

	// 加载搜索条件
	private void loadCondition() {
		try {
			JSONArray JsonArray = new JSONArray(conditions);
			for (int i = 0; i < JsonArray.length(); i++) {
				JSONObject JsonObject = JsonArray.getJSONObject(i);
				searchList.add(JsonObject.get("CaseName")
						.toString());
			}
			
			loadSearchCondition();
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null && loading.isShowing()) {
				loading.dismiss();
			}
			if (msg.what == 2) {// 华润接口显示人员信息
				adapter = new DJPCInterfaceInfoAdapter(
						CommDJPCDownloadInterfaceAty.this, listInfo,
						R.layout.djpc_interface_userinfo_item);
				lv_result.setAdapter(adapter);
			}
			if (msg.what == 3) {// 正在标准化提示
				if (mProDialog == null || !mProDialog.isShowing()) {
					mProDialog = ProgressDialog.show(
							CommDJPCDownloadInterfaceAty.this, null,
							msg.obj.toString(), true, true);
					mProDialog.setCanceledOnTouchOutside(false);
					mProDialog.setCancelable(false);
					mProDialog.show();
				} else {
					mProDialog.setMessage(msg.obj.toString());
				}
			}
			if (msg.what == 4) {// 标准化完成
				if(mProDialog!=null&&mProDialog.isShowing()){
					mProDialog.dismiss();
				}
				UIHelper.showCommuDownLoad(CommDJPCDownloadInterfaceAty.this);
				AppManager.getAppManager().finishActivity(
						CommDJPCDownloadInterfaceAty.this);
			}
			if(msg.what==5){//标准化失败
				if(mProDialog!=null&&mProDialog.isShowing()){
					mProDialog.dismiss();
				}
				if (msg.obj != null) {
					UIHelper.ToastMessage(CommDJPCDownloadInterfaceAty.this,
							msg.obj.toString());
				}
				mProDialog.dismiss();
			}
			if (msg.what == -1) {// 提示信息
				if(mProDialog!=null&&mProDialog.isShowing()){
					mProDialog.dismiss();
				}
				if (msg.obj != null) {
					UIHelper.ToastMessage(CommDJPCDownloadInterfaceAty.this,
							msg.obj.toString());
				}
			}
		};
	};

	// 上传数据
	private void upLoadData() {
		if (AppContext.resultDataFileListBuffer != null
				&& AppContext.resultDataFileListBuffer.size() > 0) {
			showUploadingDialog();
			try {
				upLoadResultListData(
						CommDJPCDownloadInterfaceAty.this);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				Dialog.dismiss();
				UIHelper.ToastMessage(this, e.toString());
			}
		}
	}
	
	//存放历史搜索记录
	Map<String, String> HistoryMap=new HashMap<String, String>();
	List<String> list=new ArrayList<String>();
	// 加载搜索条件Layout
	private void loadSearchCondition() {
		LayoutInflater localLayoutInflater = LayoutInflater.from(this);
		for (int i = 0; i < searchList.size(); i++) {
			View view = localLayoutInflater.inflate(
					R.layout.djpc_search_layout, null);
			final EditText et_hint = (EditText) view.findViewById(R.id.et_search);
			et_hint.setHint(getString(R.string.commu_djpc_condition_hint)
					+ searchList.get(i));
			et_hint.setTag(searchList.get(i));
			//点击显示历史记录
			((RelativeLayout) view.findViewById(R.id.ll_more)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String historyString=FileUtils.read(AppConst.HistoryQueryPath());
					if(!StringUtils.isEmpty(historyString)){
						//获取历史搜索记录String
						HistoryMap = gson.fromJson(historyString, HistoryMap.getClass());
						String value=HistoryMap.get(et_hint.getTag());
						if(StringUtils.isEmpty(value)){
							return;
						}
						String[] historys=value.split("@");
						//去除重复记录
				         list.clear();
				         for(int i=historys.length-1;i>=0;i--){
				        	 if(!list.contains(historys[i])){
				        		 list.add(historys[i]);
				        	 }
				         }
				        Collections.reverse(list);  
						String history=null;
						//弹出选择历史记录框
						ActionSheetDialog dialog=new ActionSheetDialog(CommDJPCDownloadInterfaceAty.this)
						.builder()
						.setCancelable(false)
						.setCanceledOnTouchOutside(true);
						for(int i=0;i<list.size();i++){
							if(list.size()>5){
								list.remove(0);
								i--;
								continue;
							}
							if(history==null){
								history=list.get(i);
							}else{
								history=history+"@"+list.get(i);
							}
							dialog.addSheetItem(list.get(i),
									SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											et_hint.setText(list.get(which-1));
										}
									});
						}
						HistoryMap.put(et_hint.getTag().toString(), history);
						dialog.show();
					}
				}
			});
			ll_condition.addView(view);
		}
	}

	Handler mUploadProgBarHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 接收上传进度条
				if (Dialog != null && !Dialog.isShowing()) {
					Dialog.show();
				}
				mProgress.setProgress(msg.arg1);
				mProgressText.setText(msg.obj.toString());
			}
			if (msg.what == 2) {// 全部上传完成
				Dialog.dismiss();
			}
			if (msg.what == -1) {
				Dialog.dismiss();
				if (msg.obj != null) {
					UIHelper.ToastMessage(CommDJPCDownloadInterfaceAty.this,
							msg.obj.toString());
				}
			}
		};
	};

	// 创建userxml
	public void CreationUserXML(String xmlUserStr, String xmlName,
			boolean isEncryption) {
		File file = new File(AppConst.XSTBasePath(), xmlName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			StringBuffer sb = new StringBuffer();
			sb.append(xmlUserStr);
			fos.write(sb.toString().getBytes());
			fos.close();
			/*if (isEncryption) {
				// 文件加密
				FileEncryptAndDecrypt.encrypt(AppConst.XSTBasePath() + xmlName);
			}*/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示上传进度框
	 */
	public void showUploadingDialog() {
		LayoutInflater factory = LayoutInflater.from(CommDJPCDownloadInterfaceAty.this);
		final View view = factory.inflate(R.layout.progressbar_layout,
				null);
		mProgressText=(TextView) view.findViewById(R.id.update_progress_text);
		mProgress=(ProgressBar) view.findViewById(R.id.update_progress);
		
		mProgressText.setText(getString(R.string.setting_sys_aboutus_checkupdata_diapromptinfo));
		Dialog=new com.moons.xst.track.widget.AlertDialog(CommDJPCDownloadInterfaceAty.this)
		.builder()
		.setTitle(getString(R.string.commu_djpc_uploading_title))
		.setView(view).setCancelable(false).setCanceledOnTouchOutside(false).setButtonGone();
		Dialog.show();
		
	}
	boolean isSucceed=true;
	/**
	 * 上传结果数据
	 */
	public void upLoadResultListData(Context context) {
		isSucceed=true;
		new Thread() {
			@Override
			public void run() {
				Context context = AppContext.baseContext;
				UpLoad uploadRes = new UpLoad(context);
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

							ZipUtils.zipFolder(
									_file.getParent(),
									AppConst.XSTZipResultPath()
											+ _djLine.getLineID());
							ReturnResultInfo returnInfo = uploadRes.UploadResultDBNew(
									AppContext.getAppVersion(context),
									context,
									mUploadProgBarHandler,
									AppConst.XSTZipResultPath()
											+ _djLine.getLineID());
							if(returnInfo.getResult_YN()){
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
								
								// 插入操作日志
								OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI 
										+ AppConst.ANDROID_DATA_UPLOADING,
										AppConst.LOGSTATUS_NORMAL,
										"Line_ID: " + String.valueOf((_djLine.getLineID())));
							}else{
								isSucceed=false;
								Message _msg = Message.obtain();
								_msg.what = -1;
								_msg.obj = context
										.getString(R.string.cumm_cummdownload_uploadresultfilefailed);
								mUploadProgBarHandler.sendMessage(_msg);
								
								// 插入操作日志
								OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI 
										+ AppConst.ANDROID_DATA_UPLOADING,
										AppConst.LOGSTATUS_ERROR,
										"Line_ID: " + String.valueOf((_djLine.getLineID())) + "\n[" + returnInfo.getError_Message_TX() + "]");
							}
						}
					} catch (Exception e) {
						isSucceed=false;
						
						Message _msg = Message.obtain();
						_msg.what = 1;
						_msg.obj = context
								.getString(R.string.cumm_cummdownload_uploadresultfilefailed)
								+ e.getMessage();
						_msg.arg1 = 0;
						mUploadProgBarHandler.sendMessage(_msg);
					}
				}
				if(isSucceed){
					AppContext.resultDataFileListBuffer.clear();
					// 全部上传完成
					Message _msg = Message.obtain();
					_msg.what = 2;
					mUploadProgBarHandler.sendMessage(_msg);
				}				
			}
		}.start();
	}
}
