package com.moons.xst.track.common;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionWidget;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.OverhaulUser;
import com.moons.xst.track.bean.URLs;
import com.moons.xst.track.bean.Work_Bill;
import com.moons.xst.track.bean.Work_Detail_Bill;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.common.factory.mapfactory.Map;
import com.moons.xst.track.common.factory.mapfactory.MapFactory;
import com.moons.xst.track.common.factory.myworkfactory.MyWork;
import com.moons.xst.track.common.factory.myworkfactory.MyWorkFactory;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.ui.AboutXST;
import com.moons.xst.track.ui.CalcCycle;
import com.moons.xst.track.ui.CaptureActivity;
import com.moons.xst.track.ui.ClauseRecordAty;
import com.moons.xst.track.ui.CommDJPCDownload;
import com.moons.xst.track.ui.CommDJPCDownloadInterfaceAty;
import com.moons.xst.track.ui.CommDownload;
import com.moons.xst.track.ui.CommDownloadNew;
import com.moons.xst.track.ui.CommOperateBillDownLoad;
import com.moons.xst.track.ui.CommUSBDownload;
import com.moons.xst.track.ui.CommUSBDownloadNew;
import com.moons.xst.track.ui.DJQueryData;
import com.moons.xst.track.ui.DJQueryDatapoPlanNew;
import com.moons.xst.track.ui.DianJianConditions;
import com.moons.xst.track.ui.DianJianHisResult;
import com.moons.xst.track.ui.DianjianPlanDetail;
import com.moons.xst.track.ui.DianjianTouchIDPos;
import com.moons.xst.track.ui.GPSPlanListAty;
import com.moons.xst.track.ui.Home;
import com.moons.xst.track.ui.ImageDialog;
import com.moons.xst.track.ui.ImageZoomDialog;
import com.moons.xst.track.ui.InitCheckPoint;
import com.moons.xst.track.ui.LoginDialog;
import com.moons.xst.track.ui.Main_Page;
import com.moons.xst.track.ui.Map_main;
import com.moons.xst.track.ui.MeasureSpeedActivity;
import com.moons.xst.track.ui.MoonsMediaRecorder;
import com.moons.xst.track.ui.NewDianjian_Main;
import com.moons.xst.track.ui.Offline_Map;
import com.moons.xst.track.ui.OperationBillAty;
import com.moons.xst.track.ui.OperationDetailsAty;
import com.moons.xst.track.ui.OtherLogin;
import com.moons.xst.track.ui.OverhaulActivity;
import com.moons.xst.track.ui.OverhaulPlanActivity;
import com.moons.xst.track.ui.OverhaulProjectActivity;
import com.moons.xst.track.ui.QueryData;
import com.moons.xst.track.ui.QueryDataCheckPointHisResult;
import com.moons.xst.track.ui.QueryDataIDPosHisResult;
import com.moons.xst.track.ui.QueryDjLine;
import com.moons.xst.track.ui.QueryHisDataAllAty;
import com.moons.xst.track.ui.QueryHisResultPerPlan;
import com.moons.xst.track.ui.RFIDLogin;
import com.moons.xst.track.ui.SearchMyWorkAty;
import com.moons.xst.track.ui.Setting;
import com.moons.xst.track.ui.StatisticsUncheckAty;
import com.moons.xst.track.ui.StatisticsUncheckDetailsAty;
import com.moons.xst.track.ui.SystemBugInfoAty;
import com.moons.xst.track.ui.SystemFileInfoAty;
import com.moons.xst.track.ui.SystemManager;
import com.moons.xst.track.ui.SystemServiceAty;
import com.moons.xst.track.ui.SystemSettingAuthorityAty;
import com.moons.xst.track.ui.TempTask;
import com.moons.xst.track.ui.TempTaskList;
import com.moons.xst.track.ui.TempTask_InputBugInfo;
import com.moons.xst.track.ui.TemperatureActivity;
import com.moons.xst.track.ui.TemperatureForOuter;
import com.moons.xst.track.ui.Tool_Camera;
import com.moons.xst.track.ui.Tool_Camera_PreviewAty;
import com.moons.xst.track.ui.Tool_GPS;
import com.moons.xst.track.ui.Tool_NFC;
import com.moons.xst.track.ui.Tool_VoiceInput;
import com.moons.xst.track.ui.TwoTicketsAty;
import com.moons.xst.track.ui.UserLogin;
import com.moons.xst.track.ui.VibrationActivity;
import com.moons.xst.track.ui.VibrationForOuterAty;
import com.moons.xst.track.ui.VibrationPlaybackAty;
import com.moons.xst.track.ui.WorkBillActivity;
import com.moons.xst.track.ui.WorkDetailAty;
import com.moons.xst.track.ui.WorkMeasureAty;
import com.moons.xst.track.widget.LoadingDialog;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class UIHelper {
	private final static String TAG = "UIHelper";

	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;

	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;

	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;

	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;

	/** 表情图片匹配 */
	private static Pattern facePattern = Pattern
			.compile("\\[{1}([0-9]\\d*)\\]{1}");

	/** 全局web样式 */
	// 链接样式文件，代码块高亮的处理
	public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
			+ "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>";
	public final static String WEB_STYLE = linkCss
			+ "<style>* {font-size:14px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} "
			+ "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} "
			+ "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;overflow: auto;} "
			+ "a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";

	private static String sdpath;

	private static Intent outerIntent;

	/**
	 * 显示首页
	 * 
	 * @param activity
	 */
	public static void showHome(Activity activity) {
		Intent intent = new Intent(activity, Home.class);
		activity.startActivity(intent);
		activity.finish();
	}

	/**
	 * 显示图片对话框
	 * 
	 * @param context
	 * @param imgUrl
	 */
	public static void showImageDialog(Context context, String imgUrl) {
		Intent intent = new Intent(context, ImageDialog.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}

	public static void showImageZoomDialog(Context context, String imgUrl) {
		Intent intent = new Intent(context, ImageZoomDialog.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}

	public static void showImageZoomDialog(Context context, String[] imgUrl,
			String visible) {
		Intent intent = new Intent(context, Tool_Camera_PreviewAty.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("mDatas", imgUrl);
		intent.putExtra("position", 0);
		intent.putExtra("visible", visible);
		context.startActivity(intent);
	}

	/**
	 * 显示登录页面
	 * 
	 * @param activity
	 */
	public static void showLoginDialog(Context context) {
		if (AppContext.getUserLoginType().equalsIgnoreCase(
				AppConst.AppLoginType.All.toString())
				|| AppContext.getUserLoginType().equalsIgnoreCase(
						AppConst.AppLoginType.Account.toString())) {
			Intent intent = new Intent(context, UserLogin.class);
			if ((context instanceof Main_Page)) {
				intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
			} else if (context instanceof Setting)
				intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
			else
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else if (AppContext.getUserLoginType().equalsIgnoreCase(
				AppConst.AppLoginType.RFID.toString())) {
			Intent rfidintent = new Intent(context, RFIDLogin.class);
			((Activity) context).startActivityForResult(rfidintent, 103);
		} else if (AppContext.getUserLoginType().equalsIgnoreCase(
				AppConst.AppLoginType.Scan.toString())) {
			Intent scanintent = new Intent();
			scanintent.setClass(context, CaptureActivity.class);
			scanintent.putExtra("ScanType", "EDITTEXT");
			((Activity) context).startActivityForResult(scanintent, 104);
		}

		// Intent intent = new Intent(context, UserLogin.class);
		// if ((context instanceof Main_Page)) {
		// intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
		// } else if (context instanceof Setting)
		// intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
		// else
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(intent);
	}

	/**
	 * 显示系统设置界面
	 * 
	 * @param context
	 */
	public static void showSetting(Context context) {
		Intent intent = new Intent(context, Setting.class);
		context.startActivity(intent);
	}

	/**
	 * 转至触碰ID位置页面
	 * 
	 * @param context
	 * @param djLineID
	 */
	public static void showDianjianTouchIDPos(final Context context,
			final int djLineID, final String djLineName, Boolean NoticeYN) {
		if (!NoticeYN) {
			Intent intent = new Intent(context, DianjianTouchIDPos.class);
			intent.putExtra("line_id", String.valueOf(djLineID));
			intent.putExtra("line_name", djLineName);

			context.startActivity(intent);
		} else {
			LayoutInflater factory = LayoutInflater.from(context);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(context)
					.builder()
					.setTitle(
							context.getString(R.string.dj_confirm_gotodianjian_title))
					.setView(view)
					.setMsg(context.getString(
							R.string.dj_confirm_gotodianjian_desc).replace(
							"%s", djLineName))
					.setPositiveButton(context.getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									// 跳转
									Intent intent = new Intent(context,
											DianjianTouchIDPos.class);
									intent.putExtra("line_id",
											String.valueOf(djLineID));
									intent.putExtra("line_name", djLineName);

									context.startActivity(intent);
								}
							})
					.setNegativeButton(context.getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {

								}
							}).setCanceledOnTouchOutside(false).show();
		}
	}

	/**
	 * 转至地图巡视界面
	 * 
	 * @param context
	 * @param djLineID
	 * @param idPosID
	 * @param idPosName
	 */
	public static void showMainMap(final Context context, final DJLine djline) {
		// 跳转
		try {
			Map map = MapFactory.getMap(AppConst.MapType_Baidu);
			if (map != null) {
				map.ChangeUI(context, djline);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * if (AppContext.getCheckInAfterEnterLine()) { // 跳转 try{ Map map =
		 * MapFactory.getMap(AppConst.MapType_Baidu); if (map != null){
		 * map.ChangeUI(context, djline); } } catch (Exception e){
		 * e.printStackTrace(); } } else { Dialog dialog = new
		 * AlertDialog.Builder(context)
		 * .setTitle(R.string.track_confirm_gototrack_title) .setMessage(
		 * context.getString(R.string.track_confirm_gototrack_desc)
		 * .replace("%s", djline.getLineName())) // 相当于点击确认按钮
		 * .setPositiveButton(R.string.sure, new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int which) { // 跳转 try{ Map map =
		 * MapFactory.getMap(AppConst.MapType_Baidu); if (map != null){
		 * map.ChangeUI(context, djline); } } catch (Exception e){
		 * e.printStackTrace(); } } }) // 相当于点击取消按钮
		 * .setNegativeButton(R.string.cancel, new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int which) { // TODO Auto-generated
		 * method stub } }).create(); dialog.show(); }
		 */
	}

	/**
	 * 转至地图巡视界面
	 * 
	 * @param context
	 * @param djLineID
	 * @param idPosID
	 * @param idPosName
	 */
	public static void showMainMapTest(final Context context,
			final int djLineID, final String lineName) {
		Intent intent = new Intent(context, Map_main.class);
		intent.putExtra("line_id", String.valueOf(djLineID));
		intent.putExtra("line_name", lineName);
		context.startActivity(intent);
	}

	/**
	 * 转至考核点初始化界面
	 * 
	 * @author LKZ
	 * @param context
	 * @param djLineID
	 */
	public static void showInitCheckPoint(final Context context) {

		LayoutInflater factory = LayoutInflater.from(context);
		final View view = factory.inflate(R.layout.editbox_layout, null);
		final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
		new com.moons.xst.track.widget.AlertDialog(context)
				.builder()
				.setTitle(context.getString(R.string.mapmain_message_importpwd))
				.setEditView(
						view,
						InputType.TYPE_TEXT_VARIATION_PASSWORD
								| InputType.TYPE_CLASS_TEXT, "", true)
				.setPositiveButton(context.getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {

								final LoadingDialog loading = new LoadingDialog(
										context);
								final Handler scHandler;
								final String p = edit.getText().toString();
								scHandler = new Handler() {
									public void handleMessage(Message msg) {
										if (loading != null)
											loading.dismiss();
										if (msg.what == 1 && msg.obj != null) {
											String psw = msg.obj.toString();
											if (p.equals(psw)) {
												Intent intent = new Intent(
														context,
														InitCheckPoint.class);
												context.startActivity(intent);
											} else {
												ToastMessage(
														context,
														R.string.mapmain_message_pwdmistake);
											}
										} else if (msg.obj != null) {

										}
									}
								};
								loading.show();
								new Thread() {
									public void run() {
										Message msg = Message.obtain();
										String psw = "";
										try {
											if (!WebserviceFactory.checkWS()) {
												String filePathString = AppConst
														.XSTBasePath()
														+ AppConst.InitPSWXmlFile;
												String temppath = AppConst
														.XSTBasePath()
														+ AppConst.tempInitPSWXmlFile;
												FileEncryptAndDecrypt.decrypt(
														filePathString,
														temppath);
												psw = FileUtils.read(temppath);
												File f = new File(temppath);
												f.delete();
											} else {
												psw = WebserviceFactory
														.getGPSInitPsw(context);
											}
											// 重新加载路线
											msg.what = 1;
											msg.obj = psw;
										} catch (Exception e) {
											e.printStackTrace();
											msg.what = -1;
											msg.obj = e;
										}
										scHandler.sendMessage(msg);
									}
								}.start();

							}
						})
				.setNegativeButton(context.getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();

	}

	/**
	 * 显示系统GPS工具界面
	 * 
	 * @author LKZ
	 * @param context
	 */
	public static void showGPSTool(Context context) {
		Intent intent = new Intent(context, Tool_GPS.class);
		context.startActivity(intent);
	}

	/**
	 * 显示离线地图下载界面
	 * 
	 * @author SX
	 * @param context
	 */
	public static void showOffLineMap(final Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View view = factory.inflate(R.layout.editbox_layout, null);
		final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
		new com.moons.xst.track.widget.AlertDialog(context)
				.builder()
				.setTitle(context.getString(R.string.mapmain_message_importpwd))
				.setEditView(
						view,
						InputType.TYPE_TEXT_VARIATION_PASSWORD
								| InputType.TYPE_CLASS_TEXT, "", true)
				.setPositiveButton(context.getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								final String p = edit.getText().toString();
								if (p.equals("12345678")) {
									Intent intent = new Intent(context,
											Offline_Map.class);
									context.startActivity(intent);
								} else {
									ToastMessage(context,
											R.string.mapmain_message_pwdmistake);
								}
							}
						})
				.setNegativeButton(context.getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
	}

	/**
	 * 转至点检页面
	 * 
	 * @param context
	 * @param djLineID
	 * @param idPosID
	 * @param idPosName
	 */
	public static void showDianjian(Context context, final String djLineID,
			final String idPosID, String idPosName) {
		Intent intent = new Intent(context, NewDianjian_Main.class);
		intent.putExtra("line_id", djLineID);
		intent.putExtra("idpos_id", idPosID);
		intent.putExtra("idpos_name", idPosName);
		context.startActivity(intent);

		/*
		 * Intent intent = new Intent(context, Dianjian_Main.class);
		 * intent.putExtra("line_id", djLineID); intent.putExtra("idpos_id",
		 * idPosID); intent.putExtra("idpos_name", idPosName);
		 * context.startActivity(intent);
		 */
	}

	/**
	 * 转至结果查询界面
	 * 
	 * @param context
	 * @param djPlanID
	 * @param lastResult
	 */
	public static void showHisResult(Context context, final String djPlanID,
			final String lastCompleteDT) {
		Intent intent = new Intent(context, DianJianHisResult.class);
		intent.putExtra("djplanid", djPlanID);
		intent.putExtra("lastcompletedt", lastCompleteDT);
		context.startActivity(intent);
	}

	/**
	 * 转至巡检计划下载页面
	 * 
	 * @param context
	 */
	public static void showCommuDownLoad(Context context) {

		/*Intent intent = new Intent(context, CommDownload.class);
		context.startActivity(intent);*/
		Intent intent = new Intent(context, CommDownloadNew.class);
		context.startActivity(intent);
	}

	/**
	 * 转至2641点检排程标准化下载页面
	 * 
	 * @param context
	 */
	public static void showCommDJPCDownload(Context context) {
		Intent intent = new Intent(context, CommDJPCDownload.class);
		context.startActivity(intent);
	}

	/**
	 * 转至点检排程第三方接口标准化下载页面
	 * 
	 * @param context
	 */
	public static void showCommDJPCDownloadInterfaceAty(Context context,
			String type, String conditions) {
		Intent intent = new Intent(context, CommDJPCDownloadInterfaceAty.class);
		intent.putExtra("interfaceType", type);
		intent.putExtra("conditions", conditions);
		context.startActivity(intent);
	}

	static Handler DJHandler;
	//判断计划类型和项目号
	public static void JudgePlanTypeAndCustomType(final Context mContext,
			final LoadingDialog mLoading) {
		if (NetWorkHelper.checkAppRunBaseCase(AppContext.baseContext)) {
			if (AppContext.getPlanType().equals(AppConst.PlanType_XDJ)) {// 巡检下载路线
				UIHelper.showCommuDownLoad(mContext);
			} else if (AppContext.getPlanType().equals(AppConst.PlanType_DJPC)) {// 点检排程下载路线
				if (!mLoading.isShowing()) {
					mLoading.show();
				}
				new Thread() {
					@Override
					public void run() {
						Message msg = new Message();
						if (!WebserviceFactory.checkWS()) {
							msg.what = -1;
							msg.obj = mContext
									.getString(R.string.cumm_twobill_webcheckPromptinfo);
							DJHandler.sendMessage(msg);
						} else {
							// 获取项目号
							String type = WebserviceFactory.GetCustomType();
							// 获取查询条件(2641返回"")
							String conditions = WebserviceFactory
									.getDJPCCaseItemList(mContext);
							msg.what = 1;
							msg.obj = type + "|" + conditions;
							DJHandler.sendMessage(msg);
						}

					}
				}.start();
			}
			DJHandler = new Handler() {
				public void handleMessage(Message msg) {
					if (mLoading != null && mLoading.isShowing()) {
						mLoading.dismiss();
					}
					if (msg.what == 1) {
						String type = "";
						String conditions = "";
						try {
							type = msg.obj.toString().split("\\|", 2)[0].toString();
							conditions = msg.obj.toString().split("\\|", 2)[1]
									.toString();
						} catch (Exception e) {
							type = AppConst.DJPCCommType_common;
							conditions = "";
						}
						if (type.equals(AppConst.DJPCCommType_common)) {// 2641点检排程
							UIHelper.showCommDJPCDownload(mContext);
						} else if (type.equals(AppConst.DJPCCommType_huarun)) {// 华润接口点检排程
							UIHelper.showCommDJPCDownloadInterfaceAty(mContext,
									type, conditions);
						}
					} else if (msg.what == -1) {
						String obj = msg.obj.toString();
						UIHelper.ToastMessage(mContext, obj);
					}
				}
			};
		}
	}

	/**
	 * 显示振动回放页面
	 * 
	 * @param context
	 */
	public static void showVibrationPlayback(Context context,
			XJ_ResultHis entity) {
		Intent intent = new Intent(context, VibrationPlaybackAty.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("XJ_ResultHis", entity);
		intent.putExtra("parms", "");
		intent.putExtra("btAddress",
				AppContext.getBlueToothAddressforVibration());
		intent.putExtra("btPassword", AppContext.getBTConnectPwdforVibration());
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 转至USB下载页面
	 * 
	 * @param context
	 */
	public static void showUSBCommuDownLoad(Context context) {

		/*Intent intent = new Intent(context, CommUSBDownload.class);
		context.startActivity(intent);*/
		Intent intent = new Intent(context, CommUSBDownloadNew.class);
		context.startActivity(intent);
	}

	/**
	 * 转至操作票下载页面
	 * 
	 * @param context
	 */
	public static void showOperateBillDownLoad(Context context) {
		Intent intent = new Intent(context, CommOperateBillDownLoad.class);
		context.startActivity(intent);
	}

	/**
	 * 显示扫一扫界面
	 * 
	 * @param context
	 */
	public static void showCapture(Context context) {
		Intent intent = new Intent(context, CaptureActivity.class);
		intent.putExtra("ScanType", "SHOWCOPY");
		context.startActivity(intent);
	}

	/**
	 * 显示录像界面
	 * 
	 * @param context
	 */
	public static void showMediaRecorder(Context context) {
		Intent intent = new Intent(context, MoonsMediaRecorder.class);
		context.startActivity(intent);
	}

	/**
	 * 显示语音输入
	 * 
	 * @param context
	 */
	public static void showVoice(Context context) {
		Intent intent = new Intent(context, Tool_VoiceInput.class);
		context.startActivity(intent);
	}

	/**
	 * 显示临时任务列表界面
	 * 
	 * @param context
	 */
	public static void showTempTaskList(Context context) {
		Intent intent = new Intent(context, TempTaskList.class);
		context.startActivity(intent);
	}

	/**
	 * 显示临时任务
	 * 
	 * @param context
	 */
	public static void showTempTask(Context context) {
		Intent intent = new Intent(context, TempTask.class);
		context.startActivity(intent);
	}

	/**
	 * 显示检修页面
	 * 
	 * @param context
	 */
	public static void showOverhaulManagement(Context context) {
		Intent intent = new Intent(context, OverhaulActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 显示检修计划页面
	 * 
	 * @param context
	 */
	public static void showOverhaulPlan(Context context,
			OverhaulUser overhaulUser) {
		Intent intent = new Intent(context, OverhaulPlanActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("overhaulUser", overhaulUser);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 显示检修项目页面
	 * 
	 * @param context
	 */
	public static void showOverhaulProject(Context context) {
		Intent intent = new Intent(context, OverhaulProjectActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 显示两票页面
	 * 
	 * @param context
	 */
	public static void showTwoTickets(Context context) {
		Intent intent = new Intent(context, TwoTicketsAty.class);
		context.startActivity(intent);
	}

	/**
	 * 显示操作票详情页面
	 * 
	 * @param context
	 */
	public static void showOperationDetails(Context context, String Code) {
		Intent intent = new Intent(context, OperationDetailsAty.class);
		intent.putExtra("Code", Code);
		context.startActivity(intent);
	}

	/**
	 * 显示操作项详细页面
	 * 
	 * @param context
	 */
	public static void showClauseRecord(Context context, String Code,
			String Order) {
		Intent intent = new Intent(context, ClauseRecordAty.class);
		intent.putExtra("Code", Code);
		intent.putExtra("Order", Order);
		context.startActivity(intent);
	}

	/**
	 * 显示措施隔离页面
	 * 
	 * @param context
	 */
	public static void showMeasureAty(Context context,
			List<Work_Detail_Bill> WorkDetailList, int position) {
		Intent intent = new Intent(context, WorkMeasureAty.class);
		intent.putExtra("WorkDetail", (Serializable) WorkDetailList);
		intent.putExtra("Order", position);
		context.startActivity(intent);
	}

	/**
	 * 显示操作票页面
	 * 
	 * @param context
	 */
	public static void showOperation(Context context, List<BillUsers> user) {
		Intent intent = new Intent(context, OperationBillAty.class);
		intent.putExtra("OperateBillUser", (Serializable) user);
		context.startActivity(intent);
	}

	/**
	 * 显示工作票详情页面
	 * 
	 * @param context
	 */
	public static void showWorkDetail(Context context, Work_Bill entity) {
		Intent intent = new Intent(context, WorkDetailAty.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("WorkBill", entity);
		intent.putExtras(bundle);

		context.startActivity(intent);
	}

	/**
	 * 显示工作票页面
	 * 
	 * @param context
	 */
	public static void showWorkbill(Context context, List<BillUsers> user) {
		Intent intent = new Intent(context, WorkBillActivity.class);
		intent.putExtra("WorkBillUser", (Serializable) user);
		context.startActivity(intent);
	}

	/**
	 * 显示检修管理登录页面
	 * 
	 * @param context
	 */
	public static void showOtherLogin(Context context, String type) {
		Intent intent = new Intent(context, OtherLogin.class);
		intent.putExtra("LoginFrom", type);
		context.startActivity(intent);
	}

	/**
	 * 周期计算
	 * 
	 * @param context
	 */
	public static void CalacCycle(Context context) {
		Intent intent = new Intent(context, CalcCycle.class);
		context.startActivity(intent);
	}

	/**
	 * 显示数据查询界面
	 * 
	 * @param context
	 */
	public static void showQuerydata(Context context, int djLineID,
			String lineName) {
		Intent intent = new Intent(context, QueryData.class);
		intent.putExtra("line_id", String.valueOf(djLineID));
		intent.putExtra("line_name", lineName);
		context.startActivity(intent);
	}

	public static void showQuerydata(Context context, final DJLine djline) {
		Intent intent = new Intent(context, QueryData.class);
		intent.putExtra("djlineinfo", djline);
		context.startActivity(intent);
	}

	/**
	 * 显示巡检数据查询界面
	 * 
	 * @param context
	 */
	public static void showDJQuerydata(Context context, int djLineID,
			String lineName) {
		Intent intent = new Intent(context, DJQueryData.class);
		intent.putExtra("line_id", String.valueOf(djLineID));
		intent.putExtra("line_name", lineName);
		context.startActivity(intent);
	}

	public static void showDJQuerydata(Context context, final DJLine djline) {
		Intent intent = new Intent(context, DJQueryData.class);
		intent.putExtra("djlineinfo", djline);
		context.startActivity(intent);
	}

	/**
	 * 显示每条计划的历史结果
	 * 
	 * @param context
	 * @param djplan
	 */
	public static void showQuerydataHisResult(Context context, String lineid,
			String planid, String plandesc, String plantype) {
		Intent intent = new Intent(context, QueryHisResultPerPlan.class);
		intent.putExtra("lineid", lineid);
		intent.putExtra("planid", planid);
		intent.putExtra("plandesc", plandesc);
		intent.putExtra("plantype", plantype);
		context.startActivity(intent);
	}

	/**
	 * 显示ID钮扣到位历史数据
	 * 
	 * @param context
	 * @param lineid
	 * @param idposid
	 */
	public static void showQueryDataIDPosHisResult(Context context,
			String lineid, String idposid, String idposName) {
		Intent intent = new Intent(context, QueryDataIDPosHisResult.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("lineid", lineid);
		intent.putExtra("idposid", idposid);
		intent.putExtra("idposname", idposName);
		context.startActivity(intent);
	}

	/**
	 * 显示考核点到位历史数据
	 * 
	 * @param context
	 * @param lineid
	 * @param idposid
	 */
	public static void showQueryDataCPHisResult(Context context, String cpid) {
		Intent intent = new Intent(context, QueryDataCheckPointHisResult.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("checkpointid", cpid);
		context.startActivity(intent);
	}

	/**
	 * 显示计划列表
	 * 
	 * @param context
	 * @param lineName
	 * @param pointname
	 * @param posID
	 */
	public static void showGPSXXPlanList(Context context, String lineName,
			String cpname, String cpID) {
		Intent intent = new Intent(context, GPSPlanListAty.class);
		intent.putExtra("line_name", lineName);
		intent.putExtra("cp_name", cpname);
		intent.putExtra("cp_ID", cpID);
		context.startActivity(intent);
	}

	/**
	 * 显示钮扣计划信息查询界面
	 * 
	 * @param context
	 */
	public static void showDJQueryPoint(Context context, DJLine djline,
			String pointname, String posID) {
		Intent intent = new Intent(context, DJQueryDatapoPlanNew.class);
		intent.putExtra("djlineinfo", djline);
		intent.putExtra("point_name", pointname);
		intent.putExtra("position_ID", posID);
		context.startActivity(intent);
	}

	/**
	 * 显示巡线报缺界面
	 * 
	 * @param context
	 */
	public static void showTaskInputbug(Context context, String taskid,
			String tasktype) {

		Intent intent = new Intent(context, TempTask_InputBugInfo.class);
		float a = Float.parseFloat(taskid);
		intent.putExtra("task_id", (int) a);
		intent.putExtra("task_type", tasktype);
		context.startActivity(intent);
		// Intent intent = new Intent(context, TempTask_inputbug.class);
		// float a= Float.parseFloat(taskid);
		// intent.putExtra("task_id", (int)a);
		// intent.putExtra("task_type", tasktype);
		// context.startActivity(intent);
	}

	/**
	 * 显示NFC工具界面
	 * 
	 * @param context
	 */
	public static void showNFCTool(Context context) {
		Intent intent = new Intent(context, Tool_NFC.class);
		intent.putExtra("debugFlag", 0);
		context.startActivity(intent);
	}

	/**
	 * 显示拍照工具界面
	 * 
	 * @param context
	 */
	public static void showCamera(Context context) {
		Intent intent = new Intent(context, Tool_Camera.class);
		context.startActivity(intent);

		// Intent intent = new Intent(context, CameraAty.class);
		// context.startActivity(intent);
	}

	/**
	 * url跳转
	 * 
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url) {
		URLs urls = URLs.parseURL(url);
		if (urls != null) {
			showLinkRedirect(context, urls.getObjType(), urls.getObjId(),
					urls.getObjKey());
		} else {
			openBrowser(context, url);
		}
	}

	public static void showLinkRedirect(Context context, int objType,
			int objId, String objKey) {
		switch (objType) {
		case URLs.URL_OBJ_TYPE_OTHER:
			openBrowser(context, objKey);
			break;
		}
	}

	public static void showDJCondition(Context context, final String lineid,
			final String linename, final String parentCondition) {
		Intent intent = new Intent(context, DianJianConditions.class);
		intent.putExtra("line_id", lineid);
		intent.putExtra("line_name", linename);
		intent.putExtra("parentcondition", parentCondition);
		context.startActivity(intent);
	}

	/**
	 * 打开浏览器
	 * 
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			ToastMessage(context, "无法浏览此网页", 500);
		}
	}

	/**
	 * 获取webviewClient对象
	 * 
	 * @return
	 */
	public static WebViewClient getWebViewClient() {
		return new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}

	/**
	 * 获取TextWatcher对象
	 * 
	 * @param context
	 * @param tmlKey
	 * @return
	 */
	public static TextWatcher getTextWatcher(final Activity context,
			final String temlKey) {
		return new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 保存当前EditText正在编辑的内容
				((AppContext) context.getApplication()).setProperty(temlKey,
						s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		};
	}

	/**
	 * 编辑器显示保存的草稿
	 * 
	 * @param context
	 * @param editer
	 * @param temlKey
	 */
	public static void showTempEditContent(Activity context, EditText editer,
			String temlKey) {
		String tempContent = ((AppContext) context.getApplication())
				.getProperty(temlKey);
		if (!StringUtils.isEmpty(tempContent)) {

			editer.setText(tempContent);
			editer.setSelection(tempContent.length());// 设置光标位置
		}
	}

	/**
	 * 清除文字
	 * 
	 * @param cont
	 * @param editer
	 */
	public static void showClearWordsDialog(final Context cont,
			final EditText editer, final TextView numwords) {
		LayoutInflater factory = LayoutInflater.from(cont);
		new com.moons.xst.track.widget.AlertDialog(cont)
				.builder()
				.setTitle(cont.getString(R.string.clearwords))
				.setPositiveButton(cont.getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 清除文字
								editer.setText("");
								numwords.setText("160");
							}
						})
				.setNegativeButton(cont.getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						}).show();

	}

	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		ToastMessage(cont, msg, false);
		// Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 弹出提示消息
	 * 
	 * @param cont
	 * @param msg
	 *            消息内容
	 * @param errMes
	 *            是否为错误消息
	 */
	public static void ToastMessage(Context cont, String msg, boolean errMes) {
		MoonsToast.getToastIntance().ShowMessage(cont, msg, errMes);
	}
	
	/**
	 * 线程中弹出提示消息
	 * 
	 * @param cont
	 * @param msg
	 *            消息内容
	 */
	public static void ThreadToastMessage(Context cont, String msg,Handler handler) {
		MoonsToast.getToastIntance().ThreadToast(cont, handler, msg);
	}

	/**
	 * 线程toast前先先初始化toast
	 * 
	 */
	public static void ThreadToast(Context cont) {
		MoonsToast.getToastIntance().InitToast(cont);
	}

	/**
	 * 弹出提示消息
	 * 
	 * @param cont
	 * @param msg
	 *            消息内容
	 * @param errMes
	 *            是否为错误消息
	 */
	public static void ToastMessage(Context cont, int msg, boolean errMes) {
		MoonsToast.getToastIntance().ShowMessage(cont, cont.getString(msg),
				errMes);
	}

	public static void ToastMessage(Context cont, int msg) {
		ToastMessage(cont, msg, false);
		// Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
		Toast.makeText(cont, msg, time).show();
	}

	public static void ToastMessageForSaveOK(Context cont, int msg) {
		MoonsToast.getToastIntance().ShowMessageForSaveOK(cont,
				cont.getString(msg));
	}

	/**
	 * 点击返回监听事件
	 * 
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}

	/**
	 * 显示关于我们
	 * 
	 * @param context
	 */
	public static void showAbout(Context context) {
		Intent intent = new Intent(context, AboutXST.class);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @param context
	 */
	public static void showAboutUS(Context context) {
		Intent intent = new Intent(context, AboutXST.class);
		context.startActivity(intent);
	}

	public static void showSystemManager(final Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View view = factory.inflate(R.layout.editbox_layout, null);
		final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
		new com.moons.xst.track.widget.AlertDialog(context)
				.builder()
				.setTitle(
						context.getString(R.string.setting_sys_enterdia_pwd_inputinfo))
				.setEditView(
						view,
						InputType.TYPE_TEXT_VARIATION_PASSWORD
								| InputType.TYPE_CLASS_TEXT, "", true)
				.setPositiveButton(context.getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								final String p = edit.getText().toString();
								if (p.equalsIgnoreCase(HexUtils
										.toOtherBaseString(Long
												.parseLong(DateTimeHelper
														.getDateTimeNow3()), 12))) {
									Intent intent = new Intent(context,
											SystemManager.class);
									context.startActivity(intent);
								} else {
									ToastMessage(
											context,
											R.string.setting_sys_enterdia_pwd_errorinfo);
								}
							}
						})
				.setNegativeButton(context.getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
	}

	public static void showSystemService(Context context) {
		Intent intent = new Intent(context, SystemServiceAty.class);
		context.startActivity(intent);
	}

	public static void showSystemFileInfo(Context context) {
		Intent intent = new Intent(context, SystemFileInfoAty.class);
		context.startActivity(intent);
	}

	public static void showSystemBugInfo(Context context) {
		Intent intent = new Intent(context, SystemBugInfoAty.class);
		context.startActivity(intent);
	}

	public static void showSystemSettingAuth(Context context) {
		Intent intent = new Intent(context, SystemSettingAuthorityAty.class);
		context.startActivity(intent);
	}

	/*
	 * public static void showVibrationInfo(Context context) { Intent intent =
	 * new Intent(context, VibrationActivity3.class);
	 * context.startActivity(intent); }
	 */

	/**
	 * 快捷栏显示登录与登出
	 * 
	 * @param activity
	 * @param qa
	 */
	public static void showSettingLoginOrLogout(Activity activity,
			QuickActionWidget qa) {
		if (((AppContext) activity.getApplication()).isLogin()) {
			qa.getQuickAction(0).setIcon(
					MyQuickAction.buildDrawable(activity,
							R.drawable.main_pop_logout_icon));
			qa.getQuickAction(0).setTitle(
					activity.getString(R.string.main_menu_logout));
		} else {
			qa.getQuickAction(0).setIcon(
					MyQuickAction.buildDrawable(activity,
							R.drawable.main_pop_login_icon));
			qa.getQuickAction(0).setTitle(
					activity.getString(R.string.main_menu_login));
		}
		qa.getQuickAction(1).setTitle(
				activity.getString(R.string.main_menu_about));
		qa.getQuickAction(2).setTitle(
				activity.getString(R.string.main_menu_quit));
	}

	/**
	 * 用户登录或注销
	 * 
	 * @param activity
	 */
	public static void loginOrLogout(Activity activity) {
		AppContext ac = (AppContext) activity.getApplication();
		if (ac.isLogin()) {
			ac.Logout();
			ToastMessage(activity, "已退出登录");
		} else {
			showLoginDialog(activity);
		}
	}

	/**
	 * 文章是否加载图片显示
	 * 
	 * @param activity
	 */
	public static void changeSettingIsLoadImage(Activity activity) {
		AppContext ac = (AppContext) activity.getApplication();
		if (ac.isLoadImage()) {
			ac.setConfigLoadimage(false);
			ToastMessage(activity, "已设置文章不加载图片");
		} else {
			ac.setConfigLoadimage(true);
			ToastMessage(activity, "已设置文章加载图片");
		}
	}

	public static void changeSettingIsLoadImage(Activity activity, boolean b) {
		AppContext ac = (AppContext) activity.getApplication();
		ac.setConfigLoadimage(b);
	}

	/**
	 * 清除app缓存
	 * 
	 * @param activity
	 */
	public static void clearAppCache(final Activity activity) {
		final AppContext ac = (AppContext) activity.getApplication();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					ToastMessage(
							ac,
							activity.getString(R.string.setting_sys_clearappcach_clearsucced_promptinfo));
				} else {
					ToastMessage(
							ac,
							activity.getString(R.string.setting_sys_clearappcach_clearfail_promptinfo));
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					ac.clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 清除app指定删除目录 文件
	 * 
	 */
	public static void deleteFile() {
		ArrayList<File> fileList = getFileList();
		for (File file : fileList) {
			if (file.exists())
				FileUtils.deleteWithFile(file);
		}
		FileUtils.deleteFileWithPath(sdpath + "tempLineList.xml");
		FileUtils.deleteFileWithPath(sdpath + "LineList.xml");
	}

	/**
	 * 获取app指定删除目录 文件大小
	 * 
	 */
	public static long getFileListSize() {
		ArrayList<File> fileList = getFileList();
		long dirSize = 0;
		for (File file : fileList) {
			dirSize += FileUtils.getDirSize(file);
		}
		long tempLineListFileSize = FileUtils.getFileSize(sdpath
				+ "tempLineList.xml");
		long LineListFileSize = FileUtils.getFileSize(sdpath + "LineList.xml");

		return dirSize + tempLineListFileSize + LineListFileSize;
	}

	/**
	 * 获取app指定删除目录 文件
	 * 
	 */
	public static ArrayList<File> getFileList() {
		ArrayList<File> FileList = new ArrayList<File>();
		sdpath = Environment.getExternalStorageDirectory().getAbsolutePath()
				.toString()
				+ File.separator + "XST" + File.separator;
		List<String> listPath = FileUtils.listPath(sdpath);
		for (String string : listPath) {
			if (!(sdpath + "logfile").equals(string)
					&& !(sdpath + "XSTConfig").equals(string)) {
				File file = new File(string);
				FileList.add(file);
			}
		}
		return FileList;
	}

	/**
	 * 发送App异常崩溃报告
	 * 
	 * @param cont
	 * @param crashReport
	 */
	public static void sendAppCrashReport(final Context cont,
			final String crashReport) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(cont);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle(R.string.app_error);
			builder.setMessage(R.string.app_error_message);
			builder.setPositiveButton(R.string.submit_report,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// 发送异常报告
							Intent i = new Intent(Intent.ACTION_SEND);
							// i.setType("text/plain"); //模拟器
							i.setType("message/rfc822"); // 真机
							i.putExtra(Intent.EXTRA_EMAIL,
									new String[] { "kezhou.li@moons.com.cn" });
							i.putExtra(Intent.EXTRA_SUBJECT,
									"小神探Android客户端 - 错误报告");
							i.putExtra(Intent.EXTRA_TEXT, crashReport);
							cont.startActivity(Intent
									.createChooser(i, "发送错误报告"));
							// 退出
							AppManager.getAppManager().AppExit(cont);
						}
					});
			builder.setNegativeButton(R.string.sure,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// 退出
							AppManager.getAppManager().AppExit(cont);
						}
					});
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示测温界面
	 * 
	 * @param context
	 * @param parms
	 *            传入参数，多个以","分隔开。目前只有发射率，临时测量会根据测温配置文件读取（默认传入95），
	 *            计划内测量根据计划所设置的值传入
	 * @param isRtnValue
	 *            临时测量传入false，计划内传入true
	 * @param requestCode
	 *            针对计划内测量有用，这里要传入在onActivityResult方法测温对应的值
	 * @param debugFlag
	 *            debugFlag = 1时不从硬件读取数据（代码构造数据，调试用），从硬件读取数据传入0
	 */
	public static void showCLWDXC(Context context, String parms,
			Boolean isRtnValue, int requestCode, int clModuleType,
			String btAddress, String btPassword, int debugFlag) {
		Intent intent = new Intent(context, TemperatureActivity.class);
		String outerType_MS_101 = context.getResources().getString(
				R.string.outer_measuretypes_moons);

		if (AppContext.getTemperatureUseYN()
				|| AppContext.getMeasureType().equals(
						AppConst.MeasureType_Outer)) {
			if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer)
					&& (!outerType_MS_101.equals(AppContext
							.getOuterMeasureType()))) {
				outerIntent = new Intent(context, TemperatureForOuter.class);
			} else {
				intent.putExtra("debugFlag", debugFlag);
				intent.putExtra("clModuleType", clModuleType);
				intent.putExtra("btAddress", btAddress);
				intent.putExtra("btPassword", btPassword);
				intent.putExtra("parms", parms);
			}
			if (isRtnValue) {
				if (AppContext.getMeasureType().equals(
						AppConst.MeasureType_Outer)
						&& (!outerType_MS_101.equals(AppContext
								.getOuterMeasureType()))) {

					((Activity) context).startActivityForResult(outerIntent,
							requestCode);
				} else {
					((Activity) context).startActivityForResult(intent,
							requestCode);
				}

			} else {
				if (AppContext.getMeasureType().equals(
						AppConst.MeasureType_Outer)
						&& (!outerType_MS_101.equals(AppContext
								.getOuterMeasureType()))) {

					context.startActivity(outerIntent);
				} else {
					context.startActivity(intent);
				}
			}
		} else {
			UIHelper.ToastMessage(context,
					context.getString(R.string.function_not_supported));
			return;
		}

		/*
		 * if (AppContext.getTemperatureUseYN() ||
		 * AppContext.getMeasureType().equals( AppConst.MeasureType_Outer)) {
		 * intent.putExtra("debugFlag", debugFlag);
		 * intent.putExtra("clModuleType", clModuleType);
		 * intent.putExtra("btAddress", btAddress);
		 * intent.putExtra("btPassword", btPassword); intent.putExtra("parms",
		 * parms); // if (isRtnValue) { ((Activity) context)
		 * .startActivityForResult(intent, requestCode);
		 * 
		 * } else { context.startActivity(intent); } } else {
		 * UIHelper.ToastMessage(context, "该机型不支持此功能！"); return; }
		 */

	}

	/**
	 * 显示测振界面
	 * 
	 * @param context
	 * @param parms
	 *            传入参数，多个以","分隔开。目前测试未使用，后期根据计划中的相关测振参数进行传入
	 * @param isRtnValue
	 *            临时测量传入false，计划内传入true
	 * @param requestCode
	 *            针对计划内测量有用，这里要传入在onActivityResult方法测温对应的值
	 * @param debugFlag
	 *            debugFlag = 1时不从硬件读取数据（代码构造数据，调试用），从硬件读取数据传入0
	 */
	public static void showCLZDXC(Context context, String parms,
			Boolean isRtnValue, int requestCode, int clModuleType,
			String btAddress, String btPassword, int debugFlag) {
		Intent intent = new Intent(context, VibrationActivity.class);
		String outerType_MS_101 = context.getResources().getString(
				R.string.outer_measuretypes_moons);
		if (AppContext.getVibrationUseYN()
				|| AppContext.getMeasureType().equals(
						AppConst.MeasureType_Outer)) {
			if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer)
					&& (!outerType_MS_101.equals(AppContext
							.getOuterMeasureType()))) {
				outerIntent = new Intent(context, VibrationForOuterAty.class);
				outerIntent.putExtra("parms", parms);
				outerIntent.putExtra("isRtnValue", isRtnValue);
			} else {
				intent.putExtra("debugFlag", debugFlag);
				intent.putExtra("parms", parms);
				intent.putExtra("clModuleType", clModuleType);
				intent.putExtra("btAddress", btAddress);
				intent.putExtra("btPassword", btPassword);
				intent.putExtra("isRtnValue", isRtnValue);
			}
			if (isRtnValue) {
				if (AppContext.getMeasureType().equals(
						AppConst.MeasureType_Outer)
						&& (!outerType_MS_101.equals(AppContext
								.getOuterMeasureType()))) {

					((Activity) context).startActivityForResult(outerIntent,
							requestCode);
				} else {
					((Activity) context).startActivityForResult(intent,
							requestCode);
				}

			} else {
				if (AppContext.getMeasureType().equals(
						AppConst.MeasureType_Outer)
						&& (!outerType_MS_101.equals(AppContext
								.getOuterMeasureType()))) {

					context.startActivity(outerIntent);
				} else {
					context.startActivity(intent);
				}
			}
		} else {
			UIHelper.ToastMessage(context, 
					context.getString(R.string.function_not_supported));
			return;
		}
	}

	/**
	 * 显示测转速界面
	 * 
	 * @param context
	 * @param parms
	 *            传入参数，多个以","分隔开。目前未使用
	 * @param isRtnValue
	 *            临时测量传入false，计划内传入true
	 * @param requestCode
	 *            针对计划内测量有用，这里要传入在onActivityResult方法测温对应的值
	 * @param debugFlag
	 *            debugFlag = 1时不从硬件读取数据（代码构造数据，调试用），从硬件读取数据传入0
	 */
	public static void showCLZSXC(Context context, String parms,
			Boolean isRtnValue, int requestCode, int clModuleType, int debugFlag) {
		Intent intent = new Intent(context, MeasureSpeedActivity.class);

		if (AppContext.getSpeedUseYN()
				|| AppContext.getMeasureType().equals(
						AppConst.MeasureType_Outer)) {
			intent.putExtra("debugFlag", debugFlag);
			intent.putExtra("clModuleType", clModuleType);
			intent.putExtra("parms", parms);
			intent.putExtra("isRtnValue", isRtnValue);
			//
			if (isRtnValue) {
				((Activity) context)
						.startActivityForResult(intent, requestCode);
			} else {
				context.startActivity(intent);
			}
		} else {
			UIHelper.ToastMessage(context, 
					context.getString(R.string.function_not_supported));
			return;
		}
	}

	/**
	 * 显示搜索我的工作任务界面
	 * 
	 * @param context
	 */
	public static void showSearchMyWork(Context context, int requestCode) {
		Intent intent = new Intent(context, SearchMyWorkAty.class);
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	/**
	 * 根据路线类型进入相应的界面(工厂方式)
	 * 
	 * @param context
	 * @param djline
	 */
	public static void enterMyWork(Context context, final DJLine djline) {
		AppContext.SetCurrLineID(djline.getLineID());
		AppContext.SetCurrLineIDForJIT(djline.getLineID());
		AppContext.setCurrLineType(djline.getlineType());

		try {
			MyWork mywork = MyWorkFactory.getMyWrok(djline.getlineType());
			if (mywork != null) {
				mywork.enterMyWork(context, djline);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载数据查询路线
	 * 
	 * @param context
	 */
	public static void loadQueryDJLine(Context context, String queryType) {
		Intent intent = new Intent(context, QueryDjLine.class);
		intent.putExtra("queryType", queryType);
		context.startActivity(intent);
	}

	/**
	 * 漏检统计
	 * 
	 * @param context
	 * @param line_id
	 * @param line_name
	 */
	public static void uncheckQueryByLine(Context context, final DJLine adjline) {
		Intent intent = new Intent(context, StatisticsUncheckAty.class);
		intent.putExtra("line_id", adjline.getLineID());
		intent.putExtra("line_name", adjline.getLineName());
		intent.putExtra("line_type", adjline.getlineType());
		context.startActivity(intent);
	}

	/**
	 * 漏检统计明细
	 * 
	 * @param context
	 */
	public static void showUncheckDetails(Context context, String[] datas) {
		Intent intent = new Intent(context, StatisticsUncheckDetailsAty.class);
		Bundle bData = new Bundle();
		bData.putStringArray("serialdata", datas);
		intent.putExtras(bData);
		context.startActivity(intent);
	}

	/**
	 * 历史数据查询
	 * 
	 * @param context
	 * @param line_id
	 * @param line_name
	 */
	public static void hisDataQueryByLine(Context context, int line_id) {
		Intent intent = new Intent(context, QueryHisDataAllAty.class);
		intent.putExtra("line_id", line_id);
		context.startActivity(intent);
	}
	
	/**
	 * 计划详细
	 * @param context
	 * @param djplan
	 */
	public static void showPlanDetail(Context context, final DJPlan djplan) {
		Intent intent = new Intent(context, DianjianPlanDetail.class);
		String cycleName = "--";
		if (djplan.GetCycle() != null)
			cycleName = djplan.GetCycle().getDJCycle().getName_TX();
		Bundle bundle = new Bundle();
		bundle.putSerializable("currentDJPlan", djplan.getDJPlan());
		intent.putExtras(bundle);
		intent.putExtra("cycleName", cycleName);
		context.startActivity(intent);
	}
	/**
	 * 添加截屏功能
	 */
	/*
	 * @SuppressLint("NewApi") public static void addScreenShot(Activity
	 * context, OnScreenShotListener mScreenShotListener) { BaseActivity cxt =
	 * null; if (context instanceof BaseActivity) { cxt = (BaseActivity)
	 * context; cxt.setAllowFullScreen(false); ScreenShotView screenShot = new
	 * ScreenShotView(cxt, mScreenShotListener); LayoutParams lp = new
	 * LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	 * context.getWindow().addContentView(screenShot, lp); } }
	 */
}