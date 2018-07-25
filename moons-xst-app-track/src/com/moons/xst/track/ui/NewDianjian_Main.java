package com.moons.xst.track.ui;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Helpers;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.baidu.location.BDLocation;
import com.moons.xst.buss.DJIDPosHelper;
import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.buss.DJResultHelper;
import com.moons.xst.buss.XSTMethodByLineTypeHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.NewDJAdapter;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.bean.Z_DataCodeGroup;
import com.moons.xst.track.bean.Z_DataCodeGroupItem;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.InputTools;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UpdateManager;
import com.moons.xst.track.widget.ActionSheetDialog;
import com.moons.xst.track.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.moons.xst.track.widget.ActionSheetDialog.SheetItemColor;
import com.moons.xst.track.widget.HorizontalListView;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.SimpleMultiListViewDialog;
import com.moons.xst.track.widget.SimpleTextDialog;
import com.moons.xst.track.widget.SuperscriptView;
import com.moons.xst.track.widget.TimerTextView;
import com.moons.xst.track.xstinterface.LKListener;
import com.moons.xst.track.xstinterface.PriorityListener;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;

public class NewDianjian_Main extends BaseActivity implements OnClickListener,
		OnLongClickListener, OnTouchListener {

	private final static String TAG = "NewDianjian_Main";
	public static NewDianjian_Main instance = null;
	private AppContext appContext;// 全局Context

	// ok建是否可点击
	private boolean isClickableOK = true;

	private final long TIME_INTERVAL = 4000L;

	public static int CURRENT_MODE = 0; // 当前的模式，默认可修改模式
	public static final int PopMenu_EDIT = 0;// 可修改模式
	public static final int PopMenu_CHECK = 1;// 检查模式(不可修改)

	public static final int REQCODE_PICTURE = 0;// 拍照
	public static final int REQCODE_SOUND = 1;// 录音
	public static final int REQCODE_VIDIO = 2;// 录像

	public static final int REQCODE_TEMPERATURE = 100;// 测温
	public static final int REQCODE_VIBRATION = 101;// 测振
	public static final int REQCODE_SPEED = 102;// 测转速

	public static final int REQCODE_TEMPTEMPERATURE = 1000;// 临时测温
	public static final int REQCODE_TEMPVIBRATION = 1001;// 临时测振

	private final static int BROWSE_IMAGE = 200;// 查看图片

	private int currIndex = 0;// 当前页卡编号
	private String strLocationInfo = "";
	// 测振相关
	private byte[] timewave = null, fftdata = null;
	private int datalen = 0x4000;
	private String featurevalue = "";
	private int rate = 0;

	// 拍照相关
	private String picname;
	private String imagePath;
	private UUID uuid;
	private String photoTimeString;

	// 录音相关
	private String audioname;
	private String audioPath;

	private String current_DJLine_ID = "";
	private String current_IDPos_ID = "";
	private String current_IDPos_Name = "";
	private String current_cycid = "";

	// 计划
	private int currentPlanIndex;
	/**
	 * 当前计算出的有效计划对象
	 */
	private DJPlan currentDJPlan = new DJPlan();
	/**
	 * 当前游标下的计划对象
	 */
	private DJPlan mDJPlan = new DJPlan();
	private Hashtable<Integer, ArrayList<DJ_PhotoByResult>> planFilesBuffer = new Hashtable<Integer, ArrayList<DJ_PhotoByResult>>();
	private DJ_ResultActive currDJResultActive = new DJ_ResultActive();
	private boolean currDJPlancompletedYN = false;
	private Date startTime;

	// 钮扣下有效计划条数
	private int YXPlanTotalCount = 0;
	// 计划统计
	private String countString = "";

	private String alarmName = "", m_ExceptionYN = "0", emsg = "";
	private Integer m_ExLevelCD = 0;

	GestureDetector mGestureDetector;
	private static final int FLING_MIN_DISTANCE = 50; // 最小距离
	private static final int FLING_MIN_VELOCITY = 0; // 最小速度
	/**
	 * 控件
	 */
	// Title控件
	private ImageButton returnButton, fbMore; // 模式选择图标
	private QuickActionWidget mGrid;// 快捷栏控件
	private TextView head_title, dianjian_main_gpsinfo, dianjian_main_username,
			dianjian_main_cyclename;
	private ViewAnimator viewAnimator;

	// 计划内容控件
	private TextView plan_index, plan_description, textview_plandetail,
			plan_remained;
	private Button btn_goto_next, btn_goto_pre;
	private SuperscriptView plan_neworedit;

	// 操作控件
	private TextView textview_plan_operate_tempcl, textview_plan_operate_srset,
			textview_plan_operate_history, textview_plan_operate_back;

	// 结果区控件
	private RelativeLayout layout_plan_result_tab, layout_plan_memo_tab;
	private RelativeLayout layout_plan_result, layout_plan_memo;
	private TextView text_result_desc, text_memo_desc;
	private View view_result, view_memo;
	private ImageView imageview_plan_addfile;
	private TextView txt_plan_result_others; // 除记录类外其他数据类型的结果框
	private EditText edit_plan_result_jl; // 记录类结果框
	private TextView textview_plan_type, textview_plan_lastresult,
			textview_plan_lasttime, textview_plan_unit;
	private TextView plan_input_mes_almlevel, planmemo_editor_title;
	private EditText planmemo_editor;
	private Button btn_savedata;
	private HorizontalListView gridview;
	private TimerTextView ttv;

	// 加载控件
	private LoadingDialog loading, saving,savePhoto;

	private Handler mHandler;
	Handler handler = new Handler();

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			showNext();
			handler.postDelayed(runnable, TIME_INTERVAL);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.newdianjian_main);
		appContext = (AppContext) getApplication();
		instance = this;
		final NewDianjian_Main data = (NewDianjian_Main) getLastNonConfigurationInstance();
		if (data != null) {
			planFilesBuffer = data.planFilesBuffer;
			currentPlanIndex = data.currentPlanIndex;
			appContext = data.appContext;
			imagePath = data.imagePath;
			picname = data.picname;
			audioPath = data.audioPath;
			audioname = data.audioname;
			photoTimeString = data.photoTimeString;
			uuid = data.uuid;
			currDJResultActive = data.currDJResultActive;
		} else {
			planFilesBuffer = new Hashtable<Integer, ArrayList<DJ_PhotoByResult>>();
			currDJResultActive = new DJ_ResultActive();
		}
		if (savedInstanceState != null) {
			// 这个是之前保存的数据
			current_DJLine_ID = savedInstanceState.getString("djline_id");
			current_IDPos_Name = savedInstanceState.getString("idpos_name");
			current_IDPos_ID = savedInstanceState.getString("idpos_id");
			current_cycid = savedInstanceState.getString("current_cycid");
			currIndex = savedInstanceState.getInt("currIndex");
		} else {
			// 这个是从另外一个界面进入这个时传入的
			current_DJLine_ID = getIntent().getStringExtra("line_id");
			current_IDPos_Name = getIntent().getStringExtra("idpos_name");
			current_IDPos_ID = getIntent().getStringExtra("idpos_id");
			current_cycid = getIntent().getStringExtra("current_cycid");
		}
		initHeadViewAndMoreBar();
		initQuickActionGrid();
		initPlanAreaView();
		initOperateAreaView();
		initResultAreaView();
		changeFontSize();
		showResultTab();
		inputMethodCtrl();
		CURRENT_MODE = PopMenu_EDIT; // 默认为可修改模式
		currentPlanIndex = -1;

		// 根据返回的what值判断，打开什么界面或显示计划
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null || !isFinishing())
					loading.dismiss();
				switch (msg.what) {
				case 0: {
					LayoutInflater factory = LayoutInflater
							.from(NewDianjian_Main.this);
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					TextView dialogFontSize = (TextView) view
							.findViewById(R.id.text);
					changeDialogFontSize(dialogFontSize);
					new com.moons.xst.track.widget.AlertDialog(
							NewDianjian_Main.this)
							.builder()
							.setTitle(getString(R.string.system_notice))
							.setView(view)
							.setMsg(getString(R.string.plan_noplan_notice))
							.setCancelable(false)
							.setPositiveButton(getString(R.string.sure),
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											backClose();
											AppManager
													.getAppManager()
													.finishActivity(
															NewDianjian_Main.this);

										}
									}).setCanceledOnTouchOutside(false).show();
				}
					break;
				case 1: {
					initSettingsByPlanData(currentDJPlan.getDJPlan());
					dispIndexOfDJPlan();
					loadDJResultToUI(currDJPlancompletedYN);
				}
					break;
				case 2: {
					final int cycTotalCount = YXPlanTotalCount;
					int completeNum = 0;

					completeNum = XSTMethodByLineTypeHelper.getInstance()
							.getCompleteNum(NewDianjian_Main.this,
									current_IDPos_ID);

					final int srNotNeedDoNum = DJIDPosHelper
							.GetSKNotNeedDoNum(current_IDPos_ID);
					final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
							- completeNum - srNotNeedDoNum)
							: 0;
					String noticeString = getString(
							R.string.plan_statistic_notice, cycTotalCount,
							notcompleteNum, srNotNeedDoNum);

					countString = XSTMethodByLineTypeHelper.getInstance()
							.getCountStr(NewDianjian_Main.this, completeNum,
									srNotNeedDoNum, YXPlanTotalCount);

					noticeString = XSTMethodByLineTypeHelper.getInstance()
							.getNoticeStr(NewDianjian_Main.this, noticeString,
									cycTotalCount);

					final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
							.valueOf(current_IDPos_ID));

					LayoutInflater factory = LayoutInflater
							.from(NewDianjian_Main.this);
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					TextView dialogFontSize = (TextView) view
							.findViewById(R.id.text);
					changeDialogFontSize(dialogFontSize);
					new com.moons.xst.track.widget.AlertDialog(
							NewDianjian_Main.this)
							.builder()
							.setTitle(getString(R.string.plan_totheend))
							.setView(view)
							.setMsg(noticeString)
							.setCancelable(false)
							.setPositiveButton(getString(R.string.sure),
									new OnClickListener() {
										@Override
										public void onClick(View v) {

											if (notcompleteNum > 0 
													&& AppContext.DJSpecCaseFlag == 0) {
												LayoutInflater factory = LayoutInflater.from(NewDianjian_Main.this);
												final View view = factory.inflate(R.layout.textview_layout,
														null);
												TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
												changeDialogFontSize(dialogFontSize);
												dialogFontSize.setTextColor(Color.RED);
												new com.moons.xst.track.widget.AlertDialog(NewDianjian_Main.this)
														.builder()
														.setTitle(getString(R.string.tips))
														.setView(view)
														.setMsg(getString(R.string.plan_remain_msg, notcompleteNum))
														.setPositiveButton(getString(R.string.sure),
																new OnClickListener() {
																	@Override
																	public void onClick(View v) {
																		_myidpos.setPlanCount(countString);
																		backClose();
																		AppManager.getAppManager().finishActivity(
																				NewDianjian_Main.this);
																	}
																})
														.setNegativeButton(getString(R.string.cancel),
																new OnClickListener() {
																	@Override
																	public void onClick(View v) {
																		LoadFirstDJPLanWithOutCompleted();
																	}
																}).show();
											} else {
												if (notcompleteNum == 0) {
													if (_myidpos != null) {
														_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
													}
												}
												_myidpos.setPlanCount(countString);
	
												backClose();
												AppManager
														.getAppManager()
														.finishActivity(
																NewDianjian_Main.this);
											}
										}
									})
							.setNegativeButton(getString(R.string.cancel),
									new OnClickListener() {
										@Override
										public void onClick(View v) {

											// TODO Auto-generated method stub
											if (notcompleteNum > 0)
												LoadFirstDJPLanWithOutCompleted();
											else {
												if (notcompleteNum <= 0) {
													if (srNotNeedDoNum == cycTotalCount) {
														_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
														_myidpos.setPlanCount(countString);

														backClose();
														AppManager
																.getAppManager()
																.finishActivity(
																		NewDianjian_Main.this);
													} else {
														switch (CURRENT_MODE) {
														case PopMenu_EDIT:
															currentPlanIndex = -1;
															loadNextDJPlan();
															break;
														case PopMenu_CHECK:
															_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
															_myidpos.setPlanCount(countString);

															backClose();
															AppManager
																	.getAppManager()
																	.finishActivity(
																			NewDianjian_Main.this);
															break;
														}
													}
												} else {
													dispIndexOfDJPlan();
												}
											}

										}
									}).setCanceledOnTouchOutside(false).show();

				}
					break;
				case 3: {
					// 周期过期处理
					LayoutInflater factory = LayoutInflater
							.from(NewDianjian_Main.this);
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					TextView dialogFontSize = (TextView) view
							.findViewById(R.id.text);
					changeDialogFontSize(dialogFontSize);
					new com.moons.xst.track.widget.AlertDialog(
							NewDianjian_Main.this)
							.builder()
							.setTitle(getString(R.string.system_notice))
							.setView(view)
							.setMsg(emsg)
							.setCancelable(false)
							.setPositiveButton(getString(R.string.sure),
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											backClose();
											AppManager
													.getAppManager()
													.finishActivity(
															NewDianjian_Main.this);

										}
									}).setCanceledOnTouchOutside(false).show();
				}
					break;
				case 4: {
					showSR(true, no);
					currentDJPlan = mDJPlan;
					no++;

					initSettingsByPlanData(currentDJPlan.getDJPlan());
					dispIndexOfDJPlan();
					loadDJResultToUI(currDJPlancompletedYN);
				}
					break;
				case 5: {
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(NewDianjian_Main.this,
									mDJPlan.getDJPlan().getLKPoint_ID(),
									current_IDPos_ID);
					ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
					if (lkPlanList != null && lkPlanList.size() > 0) {
						for (DJPlan _planInfo : lkPlanList) {
							yxplanList.add(_planInfo);
						}
					}
					if (yxplanList != null && yxplanList.size() > 0)
						selectLK(mDJPlan.getDJPlan().getLKPoint_ID(),
								yxplanList);

					currentDJPlan = mDJPlan;

					initSettingsByPlanData(currentDJPlan.getDJPlan());
					dispIndexOfDJPlan();
					loadDJResultToUI(currDJPlancompletedYN);
				}
				}
			}			
		};

		loadFirstUnDoDJPlanThread();
		registerBoradcastReceiver();
		handler.postDelayed(runnable, TIME_INTERVAL);
		
		
	}

	private void changeDialogFontSize(TextView view) {
		if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Normal.toString())) {
			view.setTextAppearance(this, R.style.widget_listview_title2);
		} else if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Big.toString())) {
			view.setTextAppearance(this, R.style.widget_listview_title2_big);
		} else if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Huge.toString())) {
			view.setTextAppearance(this, R.style.widget_listview_title2_super);
		}
		;
	}

	private void changeFontSize() {
		if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Normal.toString())) {
			plan_description.setTextAppearance(this, R.style.widget_textview_22_normal);
			textview_plan_operate_tempcl.setTextAppearance(this, R.style.widget_textview_15_normal);;
			textview_plan_operate_srset.setTextAppearance(this, R.style.widget_textview_15_normal);
			textview_plan_operate_back.setTextAppearance(this, R.style.widget_textview_15_normal);
			textview_plan_operate_history.setTextAppearance(this, R.style.widget_textview_15_normal);
			text_result_desc.setTextAppearance(this, R.style.widget_textview_15_normal);
			text_memo_desc.setTextAppearance(this, R.style.widget_textview_15_normal);
			textview_plan_lastresult.setTextAppearance(this, R.style.widget_textview_15_normal);
			textview_plan_lasttime.setTextAppearance(this, R.style.widget_textview_15_normal);
			textview_plandetail.setTextAppearance(this, R.style.widget_textview_14_normal);
			plan_remained.setTextAppearance(this, R.style.widget_textview_14_normal);
			planmemo_editor_title.setTextAppearance(this, R.style.widget_textview_14_normal);
			plan_index.setTextAppearance(this, R.style.widget_textview_16_normal);			
			planmemo_editor.setTextAppearance(this, R.style.widget_textview_16_normal);
		} else if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Big.toString())) {
			plan_description.setTextAppearance(this, R.style.widget_textview_22_big);
			textview_plan_operate_tempcl.setTextAppearance(this, R.style.widget_textview_15_big);;
			textview_plan_operate_srset.setTextAppearance(this, R.style.widget_textview_15_big);
			textview_plan_operate_back.setTextAppearance(this, R.style.widget_textview_15_big);
			textview_plan_operate_history.setTextAppearance(this, R.style.widget_textview_15_big);
			text_result_desc.setTextAppearance(this, R.style.widget_textview_15_big);
			text_memo_desc.setTextAppearance(this, R.style.widget_textview_15_big);
			textview_plan_lastresult.setTextAppearance(this, R.style.widget_textview_15_big);
			textview_plan_lasttime.setTextAppearance(this, R.style.widget_textview_15_big);
			textview_plandetail.setTextAppearance(this, R.style.widget_textview_14_big);
			plan_remained.setTextAppearance(this, R.style.widget_textview_14_big);
			planmemo_editor_title.setTextAppearance(this, R.style.widget_textview_14_big);
			plan_index.setTextAppearance(this, R.style.widget_textview_16_big);			
			planmemo_editor.setTextAppearance(this, R.style.widget_textview_16_big);
		} else if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Huge.toString())) {
			plan_description.setTextAppearance(this, R.style.widget_textview_22_huge);
			textview_plan_operate_tempcl.setTextAppearance(this, R.style.widget_textview_15_huge);;
			textview_plan_operate_srset.setTextAppearance(this, R.style.widget_textview_15_huge);
			textview_plan_operate_back.setTextAppearance(this, R.style.widget_textview_15_huge);
			textview_plan_operate_history.setTextAppearance(this, R.style.widget_textview_15_huge);
			text_result_desc.setTextAppearance(this, R.style.widget_textview_15_huge);
			text_memo_desc.setTextAppearance(this, R.style.widget_textview_15_huge);
			textview_plan_lastresult.setTextAppearance(this, R.style.widget_textview_15_huge);
			textview_plan_lasttime.setTextAppearance(this, R.style.widget_textview_15_huge);
			textview_plandetail.setTextAppearance(this, R.style.widget_textview_14_huge);
			plan_remained.setTextAppearance(this, R.style.widget_textview_14_huge);
			planmemo_editor_title.setTextAppearance(this, R.style.widget_textview_14_huge);
			plan_index.setTextAppearance(this, R.style.widget_textview_16_huge);			
			planmemo_editor.setTextAppearance(this, R.style.widget_textview_16_huge);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO
		super.onSaveInstanceState(outState);
		outState.putString("djline_id", current_DJLine_ID);
		outState.putString("idpos_name", current_IDPos_Name);
		outState.putString("idpos_id", current_IDPos_ID);
		outState.putString("current_cycid", current_cycid);
		outState.putInt("currIndex", currIndex);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	GestureDetector.SimpleOnGestureListener myGestureListener = new GestureDetector.SimpleOnGestureListener() {
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			float x = e1.getX() - e2.getX();
			float x2 = e2.getX() - e1.getX();
			if (x > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				btn_next_djplan(null);
			} else if (x2 > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				btn_pre_djplan(null);
			}

			return false;
		};
	};

	@Override
	public boolean onLongClick(View v) {
		if (v.getId() == R.id.textview_plan_operate_back) {
			LoadFirstDJPLanWithOutCompleted();
			return true;
		} else if (v.getId() == R.id.plan_description) {
			AppContext.voiceConvertService.Speaking(((TextView) v).getText()
					.toString());
			return true;
		} else if (v.getId() == R.id.textview_plan_lastresult) {
			String result = ((TextView) v).getText().toString();
			if (!StringUtils.isEmpty(result)) {
				String titleMes = getString(R.string.plan_lastresult_detail);
				final SimpleTextDialog _dialog = new SimpleTextDialog(
						NewDianjian_Main.this, titleMes, result);
				_dialog.setTextSize(20);
				_dialog.setOKButton(R.string.simple_text_dialog_btnok,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO 自动生成的方法存根
								_dialog.dismiss();
							}
						});
				_dialog.show();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.layout_plan_result_tab) {
			showResultTab();
		} else if (v.getId() == R.id.layout_plan_memo_tab) {
			showMemoTab();
		} else if (v.getId() == R.id.imageview_plan_addfile) {
			new ActionSheetDialog(NewDianjian_Main.this)
					.builder()
					.setCancelable(false)
					.setCanceledOnTouchOutside(true)
					.addSheetItem(getString(R.string.plan_photo),
							SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									if (!checkMaxFileNum(REQCODE_PICTURE))
										return;

									Intent getImageByCamera = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									uuid = UUID.randomUUID();
									photoTimeString = DateTimeHelper
											.getDateTimeNow();
									picname = uuid.toString()
											+ "_"
											+ DateTimeHelper.TransDateTime(
													photoTimeString,
													"yyyyMMddHHmmss") + ".jpg";
									imagePath = AppConst
											.CurrentResultPath_Pic(AppContext
													.GetCurrLineID())
											+ picname;
									File f = new File(imagePath);
									File destDir = new File(
											AppConst.CurrentResultPath_Record(AppContext
													.GetCurrLineID()));
									if (!destDir.exists()) {
										destDir.mkdirs();
									}
									try {
										f.createNewFile();
										// 这行代码很重要，没有的话会因为写入权限不够出一些问题
										f.setWritable(true, false);
									} catch (IOException e) {
									}
									getImageByCamera.putExtra("return-data",
											true);
									getImageByCamera.putExtra(
											MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(f));
									getImageByCamera.putExtra("outputFormat",
											Bitmap.CompressFormat.JPEG
													.toString());
									getImageByCamera.putExtra(
											"noFaceDetection", true);
									startActivityForResult(getImageByCamera,
											REQCODE_PICTURE);
								}
							})
					.addSheetItem(getString(R.string.plan_audio),
							SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									if (!checkMaxFileNum(REQCODE_SOUND))
										return;

									uuid = UUID.randomUUID();
									audioname = uuid.toString() + ".arm";
									audioPath = AppConst
											.CurrentResultPath_Record(AppContext
													.GetCurrLineID())
											+ audioname;
									File f = new File(audioPath);
									File destDir = new File(
											AppConst.CurrentResultPath_Record(AppContext
													.GetCurrLineID()));
									try {
										if (!destDir.exists()) {
											destDir.mkdirs();
										}
										f.createNewFile();
										// 这行代码很重要，没有的话会因为写入权限不够出一些问题
										f.setWritable(true, false);
									} catch (IOException e) {
									}
									// Intent intentFromRecord = new Intent();
									// intentFromRecord.setType("audio/*");
									// intentFromRecord.setAction(Intent.ACTION_GET_CONTENT);
									Intent intentFromRecord = new Intent(
											MediaStore.Audio.Media.RECORD_SOUND_ACTION);
									intentFromRecord.putExtra("return-data",
											true);
									//设置录音文件最大值（大约1000KB）
									long bytes = (long) (1024 * 1000L);
									intentFromRecord.putExtra(MediaStore.Audio.Media.EXTRA_MAX_BYTES,bytes);
									startActivityForResult(intentFromRecord,
											REQCODE_SOUND);
								}
							}).show();
		} else if (v.getId() == R.id.textview_plandetail) {
			UIHelper.showPlanDetail(this, currentDJPlan);
		} else if (v.getId() == R.id.textview_plan_operate_tempcl) {
			new ActionSheetDialog(NewDianjian_Main.this)
					.builder()
					.setCancelable(false)
					.setCanceledOnTouchOutside(true)
					.addSheetItem(getString(R.string.plan_tempcl_temperature),
							SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									int clModuleType = AppContext
											.getMeasureType().equals(
													AppConst.MeasureType_Inner) ? 0
											: 1;
									UIHelper.showCLWDXC(
											NewDianjian_Main.this,
											"",
											false,
											REQCODE_TEMPTEMPERATURE,
											clModuleType,
											AppContext
													.getBlueToothAddressforTemperature(),
											AppContext
													.getBTConnectPwdforTemperature(),
											0);
								}
							})
					.addSheetItem(getString(R.string.plan_tempcl_vibration),
							SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									int clModuleType = AppContext
											.getMeasureType().equals(
													AppConst.MeasureType_Inner) ? 0
											: 1;
									UIHelper.showCLZDXC(
											NewDianjian_Main.this,
											"",
											false,
											REQCODE_TEMPVIBRATION,
											clModuleType,
											AppContext
													.getBlueToothAddressforVibration(),
											AppContext
													.getBTConnectPwdforVibration(),
											0);
								}
							}).show();
		} else if (v.getId() == R.id.textview_plan_operate_back) {
			UIHelper.ToastMessage(getApplication(),
					R.string.plan_backtofirstundoplan);
		} else if (v.getId() == R.id.textview_plan_operate_srset) {
			showSR(false, 0);
		} else if (v.getId() == R.id.textview_plan_operate_history) {
			UIHelper.showQuerydataHisResult(NewDianjian_Main.this,
					current_DJLine_ID, currentDJPlan.getDJPlan()
							.getDJ_Plan_ID(), currentDJPlan.getDJPlan()
							.getPlanDesc_TX(), currentDJPlan.getDJPlan()
							.getDataType_CD());
		} else if (v.getId() == R.id.btn_saveResult_data) {
			saveData();
		}
	};

	/**
	 * 初始化头部视图
	 */
	private void initHeadViewAndMoreBar() {
		head_title = (TextView) findViewById(R.id.dianjian_head_title);
		head_title.setText(getString(R.string.plan_idpos) + current_IDPos_Name);
		viewAnimator = (ViewAnimator) this.findViewById(R.id.animator);
		dianjian_main_username = (TextView) findViewById(R.id.dianjian_main_username);
		dianjian_main_username.setText(AppContext.getUserName());
		dianjian_main_gpsinfo = (TextView) findViewById(R.id.dianjian_main_gpsinfo);
		dianjian_main_cyclename = (TextView) findViewById(R.id.dianjian_main_cyclename);
		if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ
				.getLineType()) {
			dianjian_main_gpsinfo.setText(AppContext.getCondition());
			viewAnimator.removeView(dianjian_main_cyclename);
		} else if (AppContext.getCurrLineType() == AppConst.LineType.DJPC
				.getLineType()) {
			viewAnimator.removeView(dianjian_main_cyclename);
		}

		fbMore = (ImageButton) findViewById(R.id.dianjian_head_morebutton);
		fbMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mGrid.show(v);
			}
		});
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Goback();
				// unregisterBoradcastReceiver();
			}
		});
	}

	/**
	 * 初始化计划区控件
	 */
	private void initPlanAreaView() {
		btn_goto_next = (Button) findViewById(R.id.btn_goto_next);
		btn_goto_pre = (Button) findViewById(R.id.btn_goto_pre);
		plan_index = (TextView) findViewById(R.id.plan_index);
		plan_description = (TextView) findViewById(R.id.plan_description);
		plan_description.setLongClickable(true);
		textview_plandetail = (TextView) findViewById(R.id.textview_plandetail);
		textview_plandetail.setOnClickListener(this);
		plan_remained = (TextView) findViewById(R.id.textview_remain);
		plan_neworedit = (SuperscriptView) findViewById(R.id.plan_neworedit);
		mGestureDetector = new GestureDetector(this, myGestureListener);
		plan_description.setOnTouchListener(this);
		plan_description.setLongClickable(true);

	}

	/**
	 * 初始化操作区控件
	 */
	private void initOperateAreaView() {
		textview_plan_operate_tempcl = (TextView) findViewById(R.id.textview_plan_operate_tempcl);
		textview_plan_operate_tempcl.setOnClickListener(this);
		textview_plan_operate_srset = (TextView) findViewById(R.id.textview_plan_operate_srset);
		textview_plan_operate_srset.setOnClickListener(this);
		textview_plan_operate_back = (TextView) findViewById(R.id.textview_plan_operate_back);
		textview_plan_operate_back.setLongClickable(true);
		textview_plan_operate_back.setOnClickListener(this);
		textview_plan_operate_back.setOnLongClickListener(this);
		textview_plan_operate_history = (TextView) findViewById(R.id.textview_plan_operate_history);
		textview_plan_operate_history.setOnClickListener(this);
	}

	/**
	 * 初始化结果区控件
	 */
	private void initResultAreaView() {
		ttv = (TimerTextView) findViewById(R.id.idpos_mincost);
		if (AppContext.getCurIDPos() != null 
				&& AppContext.getCurIDPos().getCostDateLimit_NR() > 0) {
			ttv.setVisibility(View.VISIBLE);
			ttv.setSecondCount(AppContext.getCurIDPos().getCostDateLimit_NR() * 60);
			ttv.beginRun();
		} else {
			ttv.setVisibility(View.GONE);
			ttv.stopRun();
		}
		layout_plan_result_tab = (RelativeLayout) findViewById(R.id.layout_plan_result_tab);
		layout_plan_result_tab.setOnClickListener(this);
		layout_plan_memo_tab = (RelativeLayout) findViewById(R.id.layout_plan_memo_tab);
		layout_plan_memo_tab.setOnClickListener(this);
		layout_plan_result = (RelativeLayout) findViewById(R.id.layout_plan_result);
		layout_plan_memo = (RelativeLayout) findViewById(R.id.layout_plan_memo);
		text_result_desc = (TextView) findViewById(R.id.text_result_desc);
		text_memo_desc = (TextView) findViewById(R.id.text_memo_desc);
		view_result = (View) findViewById(R.id.view_result);
		view_memo = (View) findViewById(R.id.view_memo);

		textview_plan_type = (TextView) findViewById(R.id.textview_plan_type);
		textview_plan_lastresult = (TextView) findViewById(R.id.textview_plan_lastresult);
		textview_plan_lastresult.setLongClickable(true);
		textview_plan_lasttime = (TextView) findViewById(R.id.textview_plan_lasttime);
		textview_plan_unit = (TextView) findViewById(R.id.plan_result_unit);

		plan_input_mes_almlevel = (TextView) findViewById(R.id.plan_input_mes_almlevel);
		txt_plan_result_others = (TextView) findViewById(R.id.plan_result_others);
		edit_plan_result_jl = (EditText) findViewById(R.id.plan_result_jl);
		btn_savedata = (Button) findViewById(R.id.btn_saveResult_data);
		btn_savedata.setOnClickListener(this);

		planmemo_editor = (EditText) findViewById(R.id.editview_plan_memo);
		planmemo_editor_title = (TextView) findViewById(R.id.planmemo_editor_title);
		planmemo_editor.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				planmemo_editor_title.setText(s.toString().length() + "/80");
			}
		});
		imageview_plan_addfile = (ImageView) findViewById(R.id.imageview_plan_addfile);
		imageview_plan_addfile.setOnClickListener(this);
		// 横向listview，显示照片和录音
		gridview = (HorizontalListView) findViewById(R.id.grid);
		// 点击播放
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO 自动生成的方法存根
				if (list.get(position).getLCType().equals("ZP")) {// 查看照片

					Intent intent = new Intent(NewDianjian_Main.this,
							Tool_Camera_PreviewAty.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("mDatas", getImagePath());
					intent.putExtra("visible", "false");
					intent.putExtra("position", position);
					startActivityForResult(intent, BROWSE_IMAGE);
				} else if (list.get(position).getLCType().equals("LY")) {// 查看录音
					final String actionFileName;
					ImageView aImage = (ImageView) v
							.findViewById(R.id.planimage_listitem_icon);
					if (aImage == null) {
						return;
					}
					/*final DJ_PhotoByResult _photoByResult = (DJ_PhotoByResult) aImage
							.getTag();*/
					actionFileName = (String) aImage.getTag();

					List<String> pathlist = new ArrayList<String>();
					if (planFilesBuffer.get(REQCODE_SOUND) != null) {
						for (int i = 0; i < planFilesBuffer.get(REQCODE_SOUND)
								.size(); i++) {
							if (planFilesBuffer.get(REQCODE_SOUND).get(i)
									.getLCType().equals("LY")) {
								String pa = planFilesBuffer.get(REQCODE_SOUND)
										.get(i).getFilePath();
								pathlist.add(pa);
							}
						}
						AudioPlayerDialog Audialog = new AudioPlayerDialog(
								NewDianjian_Main.this, actionFileName, pathlist);
						Audialog.show();
					} else {
						UIHelper.ToastMessage(NewDianjian_Main.this,
								R.string.plan_audiodeleted);
					}

				}
			}
		});
		// 长按删除
		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				deleteHint(position);
				return false;
			}
		});
	}

	private void deleteHint(final int position) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
		changeDialogFontSize(dialogFontSize);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.system_notice))
				.setView(view)
				.setMsg(getString(R.string.plan_deletefile))
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {

								// 删除图片文件
								// File file = new
								// File(list.get(position).getFilePath());
								// file.delete();
								UIHelper.ToastMessage(getApplication(),
										R.string.plan_del_ok);
								// 删除一条附件关联信息
								// DJResultHelper.GetIntance().deleteOneFile(
								// getApplication(), list.get(position));
								if (planFilesBuffer.get(0) != null) {
									planFilesBuffer.get(0).remove(
											list.get(position));
								}
								if (planFilesBuffer.get(1) != null) {
									planFilesBuffer.get(1).remove(
											list.get(position));
								}
								// 刷新listview
								reflashFilesList();

							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).setCanceledOnTouchOutside(false).show();
	}

	/**
	 * 隐藏输入法
	 */
	private void inputMethodCtrl() {
		if (edit_plan_result_jl != null)
			InputTools.HideKeyboard(edit_plan_result_jl);
	}

	/**
	 * 初始化快捷栏
	 */
	private void initQuickActionGrid() {
		mGrid = new QuickActionGrid(this);
		// 可修改模式
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.widget_bar_option_edit,
				R.string.dianjian_optionmode_edit));
		// 检查模式(不可修改)
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.widget_bar_option_check,
				R.string.dianjian_optionmode_check));

		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	/**
	 * 快捷栏item点击事件
	 */
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			switch (position) {
			case PopMenu_EDIT:// 可修改模式
				CURRENT_MODE = PopMenu_EDIT;
				fbMore.setImageResource(R.drawable.widget_bar_option_edit);
				break;
			case PopMenu_CHECK:// 点检模式(不可修改)
				CURRENT_MODE = PopMenu_CHECK;
				fbMore.setImageResource(R.drawable.widget_bar_option_check);
				// 切换到不可修改模式时，如果当前计划正处在修改状态，则跳到下一条未完成计划上
				if (currDJPlancompletedYN) {
					new Thread() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									loadNextUnDoDJPlan();
								}
							});
						}
					}.start();
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 显示结果tab页
	 */
	private void showResultTab() {
		text_result_desc.setTextColor(getResources().getColor(
				R.color.buttombuttonColor));
		text_memo_desc.setTextColor(Color.BLACK);
		view_result.setVisibility(View.VISIBLE);
		view_memo.setVisibility(View.INVISIBLE);
		layout_plan_result.setVisibility(View.VISIBLE);
		layout_plan_memo.setVisibility(View.INVISIBLE);
	}

	/**
	 * 显示备注tab页
	 */
	private void showMemoTab() {
		text_result_desc.setTextColor(Color.BLACK);
		text_memo_desc.setTextColor(getResources().getColor(
				R.color.buttombuttonColor));
		view_result.setVisibility(View.INVISIBLE);
		view_memo.setVisibility(View.VISIBLE);
		layout_plan_result.setVisibility(View.INVISIBLE);
		layout_plan_memo.setVisibility(View.VISIBLE);
	}

	/**
	 * 切换到前一条计划
	 */
	public void btn_next_djplan(View v) {
		btn_goto_next.setEnabled(false);
		btn_savedata.setEnabled(false);
		isClickableOK = false;
		switch (CURRENT_MODE) {
		case PopMenu_EDIT:
			this.loadNextDJPlan();
			break;
		case PopMenu_CHECK:
			this.loadNextUnDoDJPlan();
			break;
		}
		// btn_goto_next.setEnabled(true);
	}

	/**
	 * 加载下一条计划
	 */
	private void loadNextDJPlan() {

		if (loading != null) {
			loading.setLoadConceal();
			loading.show();
		}
		new Thread() {
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Message msg = Message.obtain();
						String result = LoadNextOrPreDJPlanForLoop(true, false);
						for (int i = 0; i <= YXPlanTotalCount; i++) {
							if (result.split("\\|").length == 1) {
								if (!StringUtils.toBool(result)) {
									if (loading != null)
										loading.dismiss();
									msg.what = -1;
									BtnNextHandler.sendMessage(msg);
									return;
								} else
									break;
							} else {
								boolean flag = StringUtils.toBool(result
										.split("\\|")[1].toString());
								boolean isComplete = StringUtils.toBool(result
										.split("\\|")[2].toString());
								result = LoadNextOrPreDJPlanForLoop(flag,
										isComplete);
							}
						}

						inputMethodCtrl();

						msg.what = 0;
						BtnNextHandler.sendMessage(msg);
					}
				});
			}
		}.start();

	}

	// 加载上下一条计划Handler
	private Handler BtnNextHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 0 || msg.what == 1
					|| msg.what == 2 || msg.what == 3) {
				initSettingsByPlanData(currentDJPlan.getDJPlan());
				dispIndexOfDJPlan();
				loadDJResultToUI(currDJPlancompletedYN);
			}
			
			btn_goto_next.setEnabled(true);
			btn_goto_pre.setEnabled(true);
			btn_savedata.setEnabled(true);
			isClickableOK = true;
			/*btn_goto_next.setEnabled(true);
			btn_goto_pre.setEnabled(true);
			btn_savedata.setEnabled(true);
			isClickableOK = true;
			if (msg.what == 0) {// 加载下一条计划
				if (loading != null)
					loading.dismiss();
				initSettingsByPlanData(currentDJPlan.getDJPlan());
				dispIndexOfDJPlan();
				loadDJResultToUI(currDJPlancompletedYN);
			}
			if (msg.what == 1) {// 加载下一条未做计划
				if (loading != null)
					loading.dismiss();
				initSettingsByPlanData(currentDJPlan.getDJPlan());
				dispIndexOfDJPlan();
				loadDJResultToUI(currDJPlancompletedYN);
			}
			if (msg.what == 2) {// 加载上一条计划
				if (loading != null)
					loading.dismiss();
				initSettingsByPlanData(currentDJPlan.getDJPlan());
				dispIndexOfDJPlan();
				loadDJResultToUI(currDJPlancompletedYN);
			}
			if (msg.what == 3) {// 加载上一条未做计划
				if (loading != null)
					loading.dismiss();
				initSettingsByPlanData(currentDJPlan.getDJPlan());
				dispIndexOfDJPlan();
				loadDJResultToUI(currDJPlancompletedYN);
			}*/
		}		
	};

	/**
	 * 加载上一条计划
	 */
	private void loadPreDJPlan() {
		if (loading != null){
			loading.setLoadConceal();
			loading.show();
		}
		new Thread() {
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Message msg = Message.obtain();
						String result = LoadNextOrPreDJPlanForLoop(false, false);
						for (int i = 0; i <= YXPlanTotalCount; i++) {
							if (result.split("\\|").length == 1) {
								if (!StringUtils.toBool(result)) {
									if (loading != null)
										loading.dismiss();
									msg.what = -1;
									BtnNextHandler.sendMessage(msg);
									return;
								} else
									break;
							} else {
								boolean flag = StringUtils.toBool(result
										.split("\\|")[1].toString());
								boolean isComplete = StringUtils.toBool(result
										.split("\\|")[2].toString());
								result = LoadNextOrPreDJPlanForLoop(flag,
										isComplete);
							}
						}

						inputMethodCtrl();

						msg.what = 2;
						BtnNextHandler.sendMessage(msg);
					}
				});
			}
		}.start();

	}

	/***
	 * 跳转到首条未做计划
	 */
	private void LoadFirstDJPLanWithOutCompleted() {
		currentPlanIndex = -1;
		if (loading != null) {
			loading.setLoadConceal();
			loading.show();
		}

		String result = LoadNextOrPreDJPlanForLoop(true, true);
		for (int i = 0; i <= YXPlanTotalCount; i++) {
			if (result.split("\\|").length == 1) {
				if (!StringUtils.toBool(result)) {
					if (loading != null)
						loading.dismiss();
					return;
				} else
					break;
			} else {
				boolean flag = StringUtils.toBool(result.split("\\|")[1]
						.toString());
				boolean isComplete = StringUtils.toBool(result.split("\\|")[2]
						.toString());
				result = LoadNextOrPreDJPlanForLoop(flag, isComplete);
			}
		}

		if (loading != null)
			loading.dismiss();
		initSettingsByPlanData(currentDJPlan.getDJPlan());
		dispIndexOfDJPlan();
		loadDJResultToUI(currDJPlancompletedYN);
	}

	private void loadFirstUnDoDJPlanThread() {
		loading = new LoadingDialog(this);
		loading.setLoadText(getString(R.string.load_ing));
		loading.setCancelable(false);
		loading.show();
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					YXPlanTotalCount = DJIDPosHelper
							.LoadCanDoPlan(current_IDPos_ID);
					if (YXPlanTotalCount <= 0) {
						msg.what = 0;
					} else {
						String result = LoadFirstUnDoDJPlan(true, true);
						for (int i = 0; i <= YXPlanTotalCount; i++) {
							if (result.split("\\|").length == 2) {
								break;
							} else {
								boolean flag = StringUtils.toBool(result
										.split("\\|")[1].toString());
								boolean isComplete = StringUtils.toBool(result
										.split("\\|")[2].toString());
								result = LoadFirstUnDoDJPlan(flag, isComplete);
							}
						}
						msg.what = Integer.parseInt(result.split("\\|")[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e.getMessage();
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 加载ID位置状态数据的线程
	 */
	private void loadDJResultThread(final boolean isRefresh) {
		/*
		 * loading = new LoadingDialog(this); loading.setCancelable(false);
		 * loading.show();
		 */

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				loadDJResultData();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 当前计划下结果数据到内存
	 */
	private void loadDJResultData() {
		// 加载数据
		currDJResultActive = XSTMethodByLineTypeHelper.getInstance()
				.getCurResultActive(NewDianjian_Main.this,
						AppContext.GetCurrLineID(), currentDJPlan);

		/*
		 * if (AppContext.getCurrLineType() ==
		 * AppConst.LineType.XDJ.getLineType()) { currDJResultActive =
		 * DJResultHelper.GetIntance().getOneDJResult( NewDianjian_Main.this,
		 * AppContext.GetCurrLineID(),
		 * currentDJPlan.getDJPlan().getDJ_Plan_ID(),
		 * DateTimeHelper.DateToString(currentDJPlan.GetCycle()
		 * .getTaskBegin()),
		 * DateTimeHelper.DateToString(currentDJPlan.GetCycle() .getTaskEnd()));
		 * } else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ
		 * .getLineType()) { currDJResultActive =
		 * DJResultHelper.GetIntance().getOneDJResult( NewDianjian_Main.this,
		 * AppContext.GetCurrLineID(),
		 * currentDJPlan.getDJPlan().getDJ_Plan_ID(), AppConst.PlanTimeIDStr); }
		 */
		if (currDJResultActive != null)
			planFilesBuffer = DJResultHelper.GetIntance()
					.getResultFileListByDJRes(NewDianjian_Main.this,
							AppContext.GetCurrLineID(),
							currentDJPlan.getDJPlan().getDJ_Plan_ID(),
							currDJResultActive);
	}

	/**
	 * 加载结果
	 */
	private void bindingDJResultData() {
		// 绑定结果数据
		if (currDJResultActive != null) {
			if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {// 记录类
				edit_plan_result_jl.setText(currDJResultActive.getResult_TX());
				edit_plan_result_jl
						.setTag(currDJResultActive.getException_YN());
				plan_input_mes_almlevel.setText(AppContext.almLevelBuffer.get(
						currDJResultActive.getAlarmLevel_ID()).getName_TX());
				plan_input_mes_almlevel.setTag(currDJResultActive
						.getAlarmLevel_ID());
				if (currDJResultActive.getException_YN().equals("1"))
					plan_input_mes_almlevel.setTextColor(Color.RED);
				else
					plan_input_mes_almlevel.setTextColor(Color.BLACK);
			} else if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)
					|| currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
					|| currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CS)) {// 测温,测振,测速类
				txt_plan_result_others.setText(currDJResultActive
						.getResult_TX());
				txt_plan_result_others.setTag(currDJResultActive
						.getException_YN());
				plan_input_mes_almlevel.setText(AppContext.almLevelBuffer.get(
						currDJResultActive.getAlarmLevel_ID()).getName_TX());
				plan_input_mes_almlevel.setTag(currDJResultActive
						.getAlarmLevel_ID());
				if (currDJResultActive.getException_YN().equals("1"))
					plan_input_mes_almlevel.setTextColor(Color.RED);
				else
					plan_input_mes_almlevel.setTextColor(Color.BLACK);
			} else { // 观察类

				txt_plan_result_others.setText(currDJResultActive
						.getResult_TX());
				txt_plan_result_others.setTag(currDJResultActive
						.getException_YN());
				plan_input_mes_almlevel.setText(AppContext.almLevelBuffer.get(
						currDJResultActive.getAlarmLevel_ID()).getName_TX());
				plan_input_mes_almlevel.setTag(currDJResultActive
						.getAlarmLevel_ID());
				if (currDJResultActive.getException_YN().equals("1"))
					plan_input_mes_almlevel.setTextColor(Color.RED);
				else
					plan_input_mes_almlevel.setTextColor(Color.BLACK);
			}
			if (!StringUtils.isEmpty(currDJResultActive.getMemo_TX()))
				planmemo_editor.setText(currDJResultActive.getMemo_TX());
			else {
				planmemo_editor.setText("");
			}
		} else {
			edit_plan_result_jl.setText("");
			txt_plan_result_others.setText("");
			planmemo_editor.setText("");
			plan_input_mes_almlevel.setText("");
		}
		// 绑定结果附件
		reflashFilesList();
	}

	/**
	 * 刷新文件预览栏（照片、录音）
	 */
	private void reflashFilesList() {
		if (planFilesBuffer != null && planFilesBuffer.size() > 0) {
			NewDJAdapter imageAdapter = new NewDJAdapter(this, getList(),
					R.layout.listitem_djaccessory);
			gridview.setAdapter(imageAdapter);

		} else {
			NewDJAdapter imageAdapter = new NewDJAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djaccessory);
			gridview.setAdapter(imageAdapter);
		}
	}

	/**
	 * 加载第一条未做计划，只是onCreate()的时候调用一次
	 * 
	 * @param nextFlag
	 * @param IsJudgeComplete
	 * @return
	 */
	private String LoadFirstUnDoDJPlan(Boolean nextFlag, Boolean IsJudgeComplete) {
		String result = "true|1";
		if (nextFlag) {
			if (currentPlanIndex >= YXPlanTotalCount - 1) {
				return result = "false|2";
			}
			currentPlanIndex = currentPlanIndex + 1;
		}

		mDJPlan = AppContext.YXDJPlanByIDPosBuffer.get(current_IDPos_ID).get(
				currentPlanIndex);

		// 如果是特巡，则不做任何过滤
		if (AppContext.DJSpecCaseFlag == 1) {
			currentDJPlan = mDJPlan;
			return result = "true|1";
		}

		// [start] 巡点检
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			// 不符合周期
			emsg = mDJPlan.JudgePlanTemp(NewDianjian_Main.this,
					DateTimeHelper.GetDateTimeNow());
			if (emsg.length() > 0) {
				return result = "false|3";
			}

			// 计划已完成跳过或者计划无需做跳过
			if (IsJudgeComplete) {
				if (mDJPlan.JudgePlanIsCompleted(DateTimeHelper
						.GetDateTimeNow())) {
					return result = "false|" + String.valueOf(nextFlag) + "|"
							+ String.valueOf(IsJudgeComplete);
				}
				if (mDJPlan.JudgePlanIsSrControl()) {
					// 该周期下已经设置过启停点，并且启停点下计划无需做，则跳过
					if (mDJPlan.SrIsSetting()
							&& (!mDJPlan.JudgePlanBySrPoint())) {
						return result = "false|" + String.valueOf(nextFlag)
								+ "|" + String.valueOf(IsJudgeComplete);
					}
				}
			}
			if (mDJPlan.JudgePlanIsSrControl()) {
				// 当前周期没有设置过启停点
				if (!mDJPlan.SrIsSetting()) {
					return result = "true|4";
				}
				if (!mDJPlan.JudgePlanBySrPoint()) {
					return result = "false|" + String.valueOf(nextFlag)
							+ "|false";
				}
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					return result = "true|5";
				}
				currentDJPlan = mDJPlan;
			} else {
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					return result = "true|5";
				}
				currentDJPlan = mDJPlan;
			}
		}
		// [end]
		// [start] 点检排程
		else if (AppContext.getCurrLineType() == AppConst.LineType.DJPC
				.getLineType()) {
			// 不符合时间段
			emsg = mDJPlan.JudgePlanTemp(NewDianjian_Main.this,
					DateTimeHelper.GetDateTimeNow());
			if (emsg.length() > 0) {
				return result = "false|3";
			}

			// 计划已完成跳过或者计划无需做跳过
			if (IsJudgeComplete) {
				if (mDJPlan.JudgePlanIsCompletedForDJPC()) {
					return result = "false|" + String.valueOf(nextFlag) + "|"
							+ String.valueOf(IsJudgeComplete);
				}
				if (mDJPlan.JudgePlanIsSrControl()) {
					// 该周期下已经设置过启停点，并且启停点下计划无需做，则跳过
					if (mDJPlan.SrIsSetting()
							&& (!mDJPlan.JudgePlanBySrPoint())) {
						return result = "false|" + String.valueOf(nextFlag)
								+ "|" + String.valueOf(IsJudgeComplete);
					}
				}
			}
			if (mDJPlan.JudgePlanIsSrControl()) {
				// 当前周期没有设置过启停点
				if (!mDJPlan.SrIsSetting()) {
					return result = "true|4";
				}
				if (!mDJPlan.JudgePlanBySrPoint()) {
					return result = "false|" + String.valueOf(nextFlag)
							+ "|false";
				}
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					return result = "true|5";
				}
				currentDJPlan = mDJPlan;
			} else {
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					return result = "true|5";
				}
				currentDJPlan = mDJPlan;
			}
		}
		// [end]
		// [start] 条件巡检
		else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ
				.getLineType()) {
			if (IsJudgeComplete) {
				if (mDJPlan
						.JudgePlanIsCompletedForCASEXJ(NewDianjian_Main.this)) {
					return result = "false|" + String.valueOf(nextFlag) + "|"
							+ String.valueOf(IsJudgeComplete);
				}
				if (mDJPlan.JudgePlanIsSrControl()) {
					// 已经设置过启停点，并且启停点下计划无需做，则跳过
					if (mDJPlan.SrIsSetting()
							&& (!mDJPlan.JudgePlanBySrPoint())) {
						return result = "false|" + String.valueOf(nextFlag)
								+ "|" + String.valueOf(IsJudgeComplete);
					}
				}
			}
			if (mDJPlan.JudgePlanIsSrControl()) {
				// 没有设置过启停点
				if (!mDJPlan.SrIsSetting()) {
					return result = "true|4";
				}
				if (!mDJPlan.JudgePlanBySrPoint()) {
					return result = "false|" + String.valueOf(nextFlag)
							+ "|false";
				}
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					return result = "true|5";
				}
				currentDJPlan = mDJPlan;
			} else {
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					return result = "true|5";
				}
				currentDJPlan = mDJPlan;
			}
		}
		// [end]

		return result;
	}

	/**
	 * 根据点检计划初始化设置
	 */
	private void initSettingsByPlanData(Z_DJ_Plan myDJPlan) {
		if (myDJPlan == null) {
			return;
		}
		showResultTab();
		if (planFilesBuffer != null && planFilesBuffer.size() > 0) {
			planFilesBuffer.clear();
		}
		planFilesBuffer = new Hashtable<Integer, ArrayList<DJ_PhotoByResult>>();
		textview_plan_operate_srset.setVisibility(View.GONE);
		// 修改启停
		if (!(StringUtils.isEmpty(currentDJPlan.getDJPlan().getSRPoint_ID()))
				&& Integer.parseInt(currentDJPlan.getDJPlan().getSRPoint_ID()) > 0) {
			if (AppContext.DJSpecCaseFlag == 0)
				textview_plan_operate_srset.setVisibility(View.VISIBLE);
		}
		/*
		 * if ((AppContext.getCurrLineType() == AppConst.LineType.XDJ
		 * .getLineType() || AppContext.getCurrLineType() ==
		 * AppConst.LineType.CaseXJ .getLineType()) &&
		 * !(StringUtils.isEmpty(currentDJPlan.getDJPlan() .getSRPoint_ID())) &&
		 * Integer.parseInt(currentDJPlan.getDJPlan().getSRPoint_ID()) > 0) { if
		 * (AppContext.getCurrLineType() == AppConst.LineType.XDJ
		 * .getLineType()) { if (AppContext.DJSpecCaseFlag == 0)
		 * textview_plan_operate_srset.setVisibility(View.VISIBLE); } else {
		 * textview_plan_operate_srset.setVisibility(View.VISIBLE); } }
		 */

		String cycleName = "";
		if (currentDJPlan.GetCycle() != null)
			cycleName = getString(R.string.plan_detail_cycle)
					+ currentDJPlan.GetCycle().getDJCycle().getName_TX();
		dianjian_main_cyclename.setText(cycleName);

		// 计划
		String planDescString = getString(R.string.plan_plandesc,
				myDJPlan.getPlanDesc_TX(), myDJPlan.getESTStandard_TX());
		plan_description.setText(planDescString);

		// 测量类输入框
		txt_plan_result_others.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO 自动生成的方法存根
				int clModuleType = AppContext.getMeasureType().equals(
						AppConst.MeasureType_Inner) ? 0 : 1;
				// 观察类
				if (currentDJPlan.getDJPlan().getDataType_CD()
						.equals(AppConst.DJPLAN_DATACODE_GC)) {
					selectResItem();
				}
				// 测温类
				else if (currentDJPlan.getDJPlan().getDataType_CD()
						.equals(AppConst.DJPLAN_DATACODE_CW)) {
					String fsl = "95";
					try {
						fsl = Float.toString(
								currentDJPlan.getDJPlan().getWenDu_FSL())
								.split("\\.", 2)[1];
					} catch (Exception e) {

					}
					UIHelper.showCLWDXC(NewDianjian_Main.this, fsl, true,
							REQCODE_TEMPERATURE, clModuleType,
							AppContext.getBlueToothAddressforTemperature(),
							AppContext.getBTConnectPwdforTemperature(), 0);
				}
				// 测振类
				else if (currentDJPlan.getDJPlan().getDataType_CD()
						.equals(AppConst.DJPLAN_DATACODE_CZ)) {
					String zdType = "1";
					StringBuilder parms = new StringBuilder();
					parms.append(currentDJPlan.getDJPlan().getZhenDong_Type());
					parms.append(",");
					double d = 0.0;
					try {
						d = currentDJPlan.getDJPlan().getFFTLine_NR() * 2.56;
					} catch (Exception e) {
						d = 400 * 2.56;
					}
					parms.append((int) d);
					parms.append(",");
					parms.append(currentDJPlan.getDJPlan().getMaxFreq_NR());
					parms.append(",");
					parms.append(currentDJPlan.getDJPlan().getFFTLine_NR());
					parms.append(",");
					parms.append(currentDJPlan.getDJPlan().getWindowType_NR());
					parms.append(",");
					parms.append("0");
					parms.append(",");
					if (currentDJPlan.getDJPlan().getZhenDong_Type()
							.equals("A"))
						zdType = "1";
					else if (currentDJPlan.getDJPlan().getZhenDong_Type()
							.equals("V"))
						zdType = "2";
					else if (currentDJPlan.getDJPlan().getZhenDong_Type()
							.equals("S"))
						zdType = "4";
					else
						zdType = "8";
					parms.append(zdType);
					parms.append(",");
					parms.append(currentDJPlan.getDJPlan().getAverNum_NR());// 暂时没用平均次数，会报错。currentDJPlan.getDJPlan().getAverNum_NR());
					parms.append(",");
					parms.append(currentDJPlan.getDJPlan().getAverageType_NR());
					parms.append(",");
					parms.append("0");
					parms.append(",");
					parms.append("-1");
					UIHelper.showCLZDXC(NewDianjian_Main.this,
							parms.toString(), true, REQCODE_VIBRATION,
							clModuleType,
							AppContext.getBlueToothAddressforVibration(),
							AppContext.getBTConnectPwdforVibration(), 0);
				}
				// 测速类
				else if (currentDJPlan.getDJPlan().getDataType_CD()
						.equals(AppConst.DJPLAN_DATACODE_CS)) {
					UIHelper.showCLZSXC(NewDianjian_Main.this, "", true,
							REQCODE_SPEED, clModuleType, 0);
				}
			}
		});
		edit_plan_result_jl.setFocusable(true);
		edit_plan_result_jl.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL
				| InputType.TYPE_NUMBER_FLAG_SIGNED);
		edit_plan_result_jl.addTextChangedListener(new TextWatcher() {
			private boolean isChanged = false;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根

				if (StringUtils.isEmpty(edit_plan_result_jl.getText()
						.toString())
						|| edit_plan_result_jl.getText().toString().equals(".")
						|| edit_plan_result_jl.getText().toString().equals("-")
						|| edit_plan_result_jl.getText().toString().equals("+")) {
					plan_input_mes_almlevel.setText("");
					plan_input_mes_almlevel.setTag("0");
					plan_input_mes_almlevel.setTextColor(Color.BLACK);
					return;
				}
				Double result = 0.0;
				if (!StringUtils.isEmpty(edit_plan_result_jl.getText()
						.toString())) {
					result = Double.valueOf(edit_plan_result_jl.getText()
							.toString());
				}

				checkResult(result);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if (isChanged) {// ----->如果字符未改变则返回
					return;
				}
				String str = s.toString();
				isChanged = true;
				String cuttedStr = str;
				boolean flag = false;
				/* 删除字符串中的dot */
				for (int i = str.length() - 1; i >= 0; i--) {
					char c = str.charAt(i);
					if ('.' == c && i == str.length() - 8) {
						cuttedStr = str.substring(0, i + 7);
						if (cuttedStr.endsWith(".")) {
							cuttedStr = cuttedStr.substring(0, i + 6);
						}
						flag = true;
						break;
					}
				}
				if (flag) {
					edit_plan_result_jl.setText(cuttedStr);
					edit_plan_result_jl.setSelection(edit_plan_result_jl
							.length());
				}
				isChanged = false;
			}
		});

		// 最近结果
		String lastRes = StringUtils.isEmpty(myDJPlan.getLastResult_TX()) ? ""
				: myDJPlan.getLastResult_TX();
		String unitString = StringUtils.isEmpty(myDJPlan.getMetricUnit_TX()) ? ""
				: myDJPlan.getMetricUnit_TX();
		if (StringUtils.isEmpty(lastRes))
			textview_plan_lastresult.setText("");
		else
			textview_plan_lastresult.setText(lastRes + " " + unitString);
		// 最近日期时间
		String dateString = StringUtils.isEmpty(myDJPlan.getLastComplete_DT()) ? ""
				: DateTimeHelper.TransDateTime(myDJPlan.getLastComplete_DT(),
						"yyyy-MM-dd HH:mm:ss");
		textview_plan_lasttime.setText(dateString);

		textview_plan_unit.setText(myDJPlan.getMetricUnit_TX());

		if (myDJPlan.getDataType_CD().equalsIgnoreCase(
				AppConst.DJPLAN_DATACODE_JL)) {// 记录类
			// textview_plan_type.setText(AppConst.DJPLAN_DATACODE_JL_DESC);
			txt_plan_result_others.setVisibility(View.INVISIBLE);
			plan_input_mes_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.VISIBLE);
			edit_plan_result_jl.requestFocus();
			SpannableString ss = new SpannableString(
					getString(R.string.plan_checkinput));// 定义hint的值
			AbsoluteSizeSpan ass = new AbsoluteSizeSpan(18, true);// 设置字体大小
																	// true表示单位是sp
			ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			edit_plan_result_jl.setHint(new SpannedString(ss));
		} else if (myDJPlan.getDataType_CD().equalsIgnoreCase(
				AppConst.DJPLAN_DATACODE_CW)) {// 测温类
			// textview_plan_type.setText(AppConst.DJPLAN_DATACODE_CW_DESC);
			txt_plan_result_others.setVisibility(View.VISIBLE);
			SpannableString ss = new SpannableString(
					getString(R.string.plan_checkmeasure));// 定义hint的值
			AbsoluteSizeSpan ass = new AbsoluteSizeSpan(18, true);// 设置字体大小
																	// true表示单位是sp
			ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			txt_plan_result_others.setHint(new SpannedString(ss));
			plan_input_mes_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.INVISIBLE);
		} else if (myDJPlan.getDataType_CD().equalsIgnoreCase(
				AppConst.DJPLAN_DATACODE_CZ)) {// 测振类
			// textview_plan_type.setText(AppConst.DJPLAN_DATACODE_CZ_DESC);
			txt_plan_result_others.setVisibility(View.VISIBLE);
			SpannableString ss = new SpannableString(
					getString(R.string.plan_checkmeasure));// 定义hint的值
			AbsoluteSizeSpan ass = new AbsoluteSizeSpan(18, true);// 设置字体大小
																	// true表示单位是sp
			ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			txt_plan_result_others.setHint(new SpannedString(ss));
			plan_input_mes_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.INVISIBLE);
		} else if (myDJPlan.getDataType_CD().equalsIgnoreCase(
				AppConst.DJPLAN_DATACODE_CS)) { // 测速类
			// textview_plan_type.setText(AppConst.DJPLAN_DATACODE_CS_DESC);
			txt_plan_result_others.setVisibility(View.VISIBLE);
			SpannableString ss = new SpannableString(
					getString(R.string.plan_checkmeasure));// 定义hint的值
			AbsoluteSizeSpan ass = new AbsoluteSizeSpan(18, true);// 设置字体大小
																	// true表示单位是sp
			ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			txt_plan_result_others.setHint(new SpannedString(ss));
			plan_input_mes_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.INVISIBLE);
		} else { // 观察类
			// textview_plan_type.setText(AppConst.DJPLAN_DATACODE_GC_DESC);
			txt_plan_result_others.setVisibility(View.VISIBLE);
			SpannableString ss = new SpannableString(
					getString(R.string.plan_checkchoose));// 定义hint的值
			AbsoluteSizeSpan ass = new AbsoluteSizeSpan(18, true);// 设置字体大小
																	// true表示单位是sp
			ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			txt_plan_result_others.setHint(new SpannedString(ss));
			plan_input_mes_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 显示计划序号、数量、剩余和录入状态
	 */
	private void dispIndexOfDJPlan() {
		// 计划的序号和总数
		if (currentDJPlan == null) {
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
			changeDialogFontSize(dialogFontSize);
			new com.moons.xst.track.widget.AlertDialog(this)
					.builder()
					.setTitle(getString(R.string.plan_backdoplan))
					.setView(view)
					.setMsg(getString(R.string.plan_noplan_notice))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									backClose();
									AppManager.getAppManager().finishActivity(
											NewDianjian_Main.this);
								}
							}).setCanceledOnTouchOutside(false).show();

		} else {
			startTime = DateTimeHelper.GetDateTimeNow();
			String indexCaption = String.valueOf(currentPlanIndex + 1) + "/"
					+ String.valueOf(YXPlanTotalCount);
			plan_index.setText(indexCaption);
			// 计划是新增还是修改
			currDJPlancompletedYN = XSTMethodByLineTypeHelper.getInstance()
					.judgePlanIsCompleted(NewDianjian_Main.this, currentDJPlan);

			/*
			 * if (AppContext.getCurrLineType() == AppConst.LineType.XDJ
			 * .getLineType()) { currDJPlancompletedYN = currentDJPlan
			 * .JudgePlanIsCompleted(DateTimeHelper.GetDateTimeNow()); } else if
			 * (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ
			 * .getLineType()) { currDJPlancompletedYN = currentDJPlan
			 * .JudgePlanIsCompletedForCASEXJ(NewDianjian_Main.this); }
			 */
			if (currDJPlancompletedYN) {
				plan_neworedit.setVisibility(View.VISIBLE);
				plan_neworedit.setTag(1);
			} else {
				plan_neworedit.setVisibility(View.GONE);
				plan_neworedit.setTag(0);
			}
			if (AppContext.DJSpecCaseFlag == 1) {
				plan_neworedit.setText(R.string.plan_speccase);
				plan_neworedit.setVisibility(View.VISIBLE);
				plan_neworedit.setTag(0);
			}
			// 计划剩余数量
			int completeNum = DJIDPosHelper.GetCompleteNum(current_IDPos_ID);

			int srNotNeedDoNum = DJIDPosHelper
					.GetSKNotNeedDoNum(current_IDPos_ID);

			String remainText = XSTMethodByLineTypeHelper.getInstance()
					.setRemainText(NewDianjian_Main.this, YXPlanTotalCount,
							completeNum, srNotNeedDoNum);
			plan_remained.setText(remainText);

			/*
			 * if (AppContext.getCurrLineType() == AppConst.LineType.XDJ
			 * .getLineType()) { if (AppContext.DJSpecCaseFlag == 1) {
			 * plan_remained.setText(""); } else { plan_remained
			 * .setText(getString(R.string.plan_undo) +
			 * String.valueOf((YXPlanTotalCount - completeNum -
			 * srNotNeedDoNum))); } } else if (AppContext.getCurrLineType() ==
			 * AppConst.LineType.CaseXJ .getLineType()) {
			 * plan_remained.setText(""); }
			 */
		}
	}

	/**
	 * 加载结果到UI界面
	 */
	private void loadDJResultToUI(boolean editYN) {
		if (editYN) {
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					/*
					 * if (loading != null) loading.dismiss();
					 */
					if (msg.what == 1) {
						bindingDJResultData();
					}
				}
			};

			this.loadDJResultThread(false);
		} else {
			NewDJAdapter adapter = new NewDJAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djaccessory);
			gridview.setAdapter(adapter);
			edit_plan_result_jl.setText("");
			txt_plan_result_others.setText("");
			planmemo_editor.setText("");
			if (currentDJPlan.getDJPlan().getDataType_CD()
					.equals(AppConst.DJPLAN_DATACODE_GC)) {
				String Result_TX = getString(R.string.plan_gc_defaultresult);
				String expYN = "0";
				int alarmlevelid = 0;
				String alarmName = getString(R.string.plan_gc_defaultresult);
				if (AppContext.dataCodeBuffer == null
						|| AppContext.dataCodeBuffer.size() <= 0) {
					Result_TX = getString(R.string.plan_gc_defaultresult);
				} else {
					Integer datagroupID = (currentDJPlan.getDJPlan()
							.getDataCodeGroup_ID() != null && currentDJPlan
							.getDJPlan().getDataCodeGroup_ID() >= 0) ? currentDJPlan
							.getDJPlan().getDataCodeGroup_ID() : 0;
					Z_DataCodeGroup _temCodeGroup = getDataCodeGroupByID(String
							.valueOf(datagroupID));

					if (_temCodeGroup != null)
						if (_temCodeGroup.getDataCodeGroupItems() != null
								&& _temCodeGroup.getDataCodeGroupItems().size() > 0) {
							Result_TX = _temCodeGroup.getDataCodeGroupItems()
									.get(0).getName_TX();
							expYN = _temCodeGroup.getDataCodeGroupItems()
									.get(0).getStatus_CD().equals("1") ? "1"
									: "0";
							alarmlevelid = _temCodeGroup
									.getDataCodeGroupItems().get(0)
									.getAlarmLevel_ID();
							if (AppContext.almLevelBuffer != null
									&& AppContext.almLevelBuffer.size() > 0) {
								alarmName = AppContext.almLevelBuffer.get(
										alarmlevelid).getName_TX();// 报警名称
							}
						} else {
							Result_TX = getString(R.string.plan_gc_defaultresult);
						}
					else {
						Result_TX = getString(R.string.plan_gc_defaultresult);
					}
				}

				plan_input_mes_almlevel.setText(alarmName);
				plan_input_mes_almlevel.setTag(alarmlevelid);
				txt_plan_result_others.setText(Result_TX);
				txt_plan_result_others.setTag(expYN);
				if (expYN.equals("0"))
					plan_input_mes_almlevel.setTextColor(Color.BLACK);
				else
					plan_input_mes_almlevel.setTextColor(Color.RED);
			}

			/* 如果设置为默认填入上次结果，并且存在上次结果，则填入到输入框内 */
			if (AppContext.getLastResult()) {
				if (!StringUtils.isEmpty(currentDJPlan.getDJPlan()
						.getLastResult_TX())) {
					/* 记录类 */
					if (currentDJPlan.getDJPlan().getDataType_CD()
							.equals(AppConst.DJPLAN_DATACODE_JL))
						try {
							edit_plan_result_jl.setText(currentDJPlan.getDJPlan()
									.getLastResult_TX());
						} catch (NumberFormatException nfe) {
							UIHelper.ToastMessage(this,
									getString(R.string.plan_wrongFormat_result));
							edit_plan_result_jl.setText("");
						}
					/* 观察类 */
					else if (currentDJPlan.getDJPlan().getDataType_CD()
							.equals(AppConst.DJPLAN_DATACODE_GC)) {
						txt_plan_result_others.setText(currentDJPlan
								.getDJPlan().getLastResult_TX());
						getLastAlarmLevelByLastResult(txt_plan_result_others
								.getText().toString());
					}
					/* 测温,测振,测速类不能输入上次结果，必须测量 */
					else {
						/*
						 * txt_plan_result_others.setText(currentDJPlan
						 * .getDJPlan().getLastResult_TX()); try { double result
						 * = Double .parseDouble(txt_plan_result_others
						 * .getText().toString()); checkResult(result); } catch
						 * (NumberFormatException nfx) {
						 * UIHelper.ToastMessage(this,
						 * getString(R.string.plan_wrongFormat_result)); return;
						 * 
						 * }
						 */

					}
				}
			}
		}
	}

	/**
	 * 加载上一条未做计划
	 * 
	 * @param IsJudgeComplete
	 */
	private void loadPreUnDoDJPlan() {
		if (loading != null) {
			loading.setLoadConceal();
			loading.show();
		}
		new Thread() {
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Message msg = Message.obtain();
						String result = LoadNextOrPreDJPlanForLoop(false, true);
						for (int i = 0; i <= YXPlanTotalCount; i++) {
							if (result.split("\\|").length == 1) {
								if (!StringUtils.toBool(result)) {
									if (loading != null)
										loading.dismiss();
									msg.what = -1;
									BtnNextHandler.sendMessage(msg);
									return;
								} else
									break;
							} else {
								boolean flag = StringUtils.toBool(result
										.split("\\|")[1].toString());
								boolean isComplete = StringUtils.toBool(result
										.split("\\|")[2].toString());
								result = LoadNextOrPreDJPlanForLoop(flag,
										isComplete);
							}
						}

						inputMethodCtrl();

						msg.what = 3;
						BtnNextHandler.sendMessage(msg);
					}
				});
			}
		}.start();

	}

	/**
	 * 加载下一条未做计划
	 * 
	 * @param IsJudgeComplete
	 */
	private void loadNextUnDoDJPlan() {
		if (loading != null) {
			loading.setLoadConceal();
			loading.show();
		}
		new Thread() {
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Message msg = Message.obtain();
						String result = LoadNextOrPreDJPlanForLoop(true, true);
						for (int i = 0; i <= YXPlanTotalCount; i++) {
							if (result.split("\\|").length == 1) {
								if (!StringUtils.toBool(result)) {
									if (loading != null)
										loading.dismiss();
									msg.what = -1;
									BtnNextHandler.sendMessage(msg);
									return;
								} else
									break;
							} else {
								boolean flag = StringUtils.toBool(result
										.split("\\|")[1].toString());
								boolean isComplete = StringUtils.toBool(result
										.split("\\|")[2].toString());
								result = LoadNextOrPreDJPlanForLoop(flag,
										isComplete);
							}
						}

						inputMethodCtrl();

						msg.what = 1;
						BtnNextHandler.sendMessage(msg);
					}
				});
			}
		}.start();

	}

	/**
	 * 切换到前一条计划
	 */
	public void btn_pre_djplan(View v) {
		btn_goto_pre.setEnabled(false);
		btn_savedata.setEnabled(false);
		isClickableOK = false;
		switch (CURRENT_MODE) {
		case PopMenu_EDIT:
			this.loadPreDJPlan();
			break;
		case PopMenu_CHECK:
			this.loadPreUnDoDJPlan();
			break;
		}
		// btn_goto_pre.setEnabled(true);
	}

	int no = 0;

	/**
	 * 向前或者向后移动游标
	 * 
	 * @param nextFlag
	 * @param IsJudgeComplete
	 *            是否判断已完成
	 * @return
	 */
	private String LoadNextOrPreDJPlanForLoop(Boolean nextFlag,
			Boolean IsJudgeComplete) {
		String result = "true";
		if (nextFlag) {
			if (currentPlanIndex >= YXPlanTotalCount - 1) {
				final int cycTotalCount = YXPlanTotalCount;
				int completeNum = 0;
				completeNum = XSTMethodByLineTypeHelper
						.getInstance()
						.getCompleteNum(NewDianjian_Main.this, current_IDPos_ID);
				final int srNotNeedDoNum = DJIDPosHelper
						.GetSKNotNeedDoNum(current_IDPos_ID);
				final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
						- completeNum - srNotNeedDoNum)
						: 0;
				String noticeString = getString(R.string.plan_statistic_notice,
						cycTotalCount, notcompleteNum, srNotNeedDoNum);

				countString = XSTMethodByLineTypeHelper.getInstance()
						.getCountStr(NewDianjian_Main.this, completeNum,
								srNotNeedDoNum, YXPlanTotalCount);

				noticeString = XSTMethodByLineTypeHelper.getInstance()
						.getNoticeStr(NewDianjian_Main.this, noticeString,
								cycTotalCount);

				final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
						.valueOf(current_IDPos_ID));
				LayoutInflater factory = LayoutInflater
						.from(NewDianjian_Main.this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				TextView dialogFontSize = (TextView) view
						.findViewById(R.id.text);
				changeDialogFontSize(dialogFontSize);
				new com.moons.xst.track.widget.AlertDialog(
						NewDianjian_Main.this)
						.builder()
						.setTitle(getString(R.string.plan_totheend))
						.setView(view)
						.setMsg(noticeString)
						.setCancelable(false)
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if (ttv.isRun()) {
											if (notcompleteNum > 0) {
												UIHelper.ToastMessage(
														NewDianjian_Main.this,
														getString(R.string.plan_mincoast_notice3));
												LoadFirstDJPLanWithOutCompleted();
											} else {
												if (srNotNeedDoNum == cycTotalCount) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
													_myidpos.setPlanCount(countString);

													backClose();
													AppManager
															.getAppManager()
															.finishActivity(
																	NewDianjian_Main.this);
												} else {
													UIHelper.ToastMessage(
															NewDianjian_Main.this,
															getString(R.string.plan_mincoast_notice3));
													currentPlanIndex = -1;
													loadNextDJPlan();
												}
											}
											return;
										}
										
										if (notcompleteNum > 0 
												&& AppContext.DJSpecCaseFlag == 0) {
											LayoutInflater factory = LayoutInflater.from(NewDianjian_Main.this);
											final View view = factory.inflate(R.layout.textview_layout,
													null);
											TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
											changeDialogFontSize(dialogFontSize);
											dialogFontSize.setTextColor(Color.RED);
											new com.moons.xst.track.widget.AlertDialog(NewDianjian_Main.this)
													.builder()
													.setTitle(getString(R.string.tips))
													.setView(view)
													.setMsg(getString(R.string.plan_remain_msg, notcompleteNum))
													.setPositiveButton(getString(R.string.sure),
															new OnClickListener() {
																@Override
																public void onClick(View v) {
																	_myidpos.setPlanCount(countString);
																	backClose();
																	AppManager.getAppManager().finishActivity(
																			NewDianjian_Main.this);
																}
															})
													.setNegativeButton(getString(R.string.cancel),
															new OnClickListener() {
																@Override
																public void onClick(View v) {
																	LoadFirstDJPLanWithOutCompleted();
																}
															}).show();
										} else {
											if (notcompleteNum == 0) {
												if (_myidpos != null) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
												}
											}
											_myidpos.setPlanCount(countString);

											backClose();
											AppManager
													.getAppManager()
													.finishActivity(
															NewDianjian_Main.this);
										}
									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {

										// TODO Auto-generated method stub
										if (notcompleteNum > 0) {
											LoadFirstDJPLanWithOutCompleted();
										} else {
											// String msg = checkIDPosMinCost();
											if (!ttv.isRun()) {
												if (notcompleteNum <= 0) {
													if (srNotNeedDoNum == cycTotalCount) {
														_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
														_myidpos.setPlanCount(countString);

														backClose();
														AppManager
																.getAppManager()
																.finishActivity(
																		NewDianjian_Main.this);
													} else {
														switch (CURRENT_MODE) {
														case PopMenu_EDIT:
															currentPlanIndex = -1;
															loadNextDJPlan();
															break;
														case PopMenu_CHECK:
															_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
															_myidpos.setPlanCount(countString);

															backClose();
															AppManager
																	.getAppManager()
																	.finishActivity(
																			NewDianjian_Main.this);
															break;
														}
													}
												} else {
													dispIndexOfDJPlan();
												}
											} else {
												UIHelper.ToastMessage(
														NewDianjian_Main.this,
														getString(R.string.plan_mincoast_notice3));
											}
										}

									}
								}).setCanceledOnTouchOutside(false).show();

				return result = "false";
			}
			currentPlanIndex = currentPlanIndex + 1;
		} else {
			if (currentPlanIndex <= 0) {
				final int cycTotalCount = YXPlanTotalCount;
				int completeNum = 0;
				completeNum = XSTMethodByLineTypeHelper
						.getInstance()
						.getCompleteNum(NewDianjian_Main.this, current_IDPos_ID);
				final int srNotNeedDoNum = DJIDPosHelper
						.GetSKNotNeedDoNum(current_IDPos_ID);
				final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
						- completeNum - srNotNeedDoNum)
						: 0;
				String noticeString = getString(R.string.plan_statistic_notice,
						cycTotalCount, notcompleteNum, srNotNeedDoNum);

				countString = XSTMethodByLineTypeHelper.getInstance()
						.getCountStr(NewDianjian_Main.this, completeNum,
								srNotNeedDoNum, YXPlanTotalCount);

				noticeString = XSTMethodByLineTypeHelper.getInstance()
						.getNoticeStr(NewDianjian_Main.this, noticeString,
								cycTotalCount);

				final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
						.valueOf(current_IDPos_ID));

				LayoutInflater factory = LayoutInflater
						.from(NewDianjian_Main.this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				TextView dialogFontSize = (TextView) view
						.findViewById(R.id.text);
				changeDialogFontSize(dialogFontSize);
				new com.moons.xst.track.widget.AlertDialog(
						NewDianjian_Main.this)
						.builder()
						.setTitle(getString(R.string.plan_tothefirst))
						.setView(view)
						.setMsg(noticeString)
						.setCancelable(false)
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if (ttv.isRun()) {
											if (notcompleteNum > 0) {
												UIHelper.ToastMessage(
														NewDianjian_Main.this,
														getString(R.string.plan_mincoast_notice3));
												LoadFirstDJPLanWithOutCompleted();
											} else {
												if (srNotNeedDoNum == cycTotalCount) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
													_myidpos.setPlanCount(countString);

													backClose();
													AppManager
															.getAppManager()
															.finishActivity(
																	NewDianjian_Main.this);
												} else {
													UIHelper.ToastMessage(
															NewDianjian_Main.this,
															getString(R.string.plan_mincoast_notice3));
													currentPlanIndex = -1;
													loadNextDJPlan();
												}
											}
											return;
										}
										
										if (notcompleteNum > 0 
												&& AppContext.DJSpecCaseFlag == 0) {
											LayoutInflater factory = LayoutInflater.from(NewDianjian_Main.this);
											final View view = factory.inflate(R.layout.textview_layout,
													null);
											TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
											changeDialogFontSize(dialogFontSize);
											dialogFontSize.setTextColor(Color.RED);
											new com.moons.xst.track.widget.AlertDialog(NewDianjian_Main.this)
													.builder()
													.setTitle(getString(R.string.tips))
													.setView(view)
													.setMsg(getString(R.string.plan_remain_msg, notcompleteNum))
													.setPositiveButton(getString(R.string.sure),
															new OnClickListener() {
																@Override
																public void onClick(View v) {
																	_myidpos.setPlanCount(countString);
																	backClose();
																	AppManager.getAppManager().finishActivity(
																			NewDianjian_Main.this);
																}
															})
													.setNegativeButton(getString(R.string.cancel),
															new OnClickListener() {
																@Override
																public void onClick(View v) {
																	LoadFirstDJPLanWithOutCompleted();
																}
															}).show();
										} else {
											if (notcompleteNum == 0) {
												if (_myidpos != null) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
												}
											}
											_myidpos.setPlanCount(countString);
			
											backClose();
											AppManager.getAppManager().finishActivity(
													NewDianjian_Main.this);
										}
									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {

										if (notcompleteNum > 0)
											LoadFirstDJPLanWithOutCompleted();
										else {
											if (!ttv.isRun()) {
												if (notcompleteNum <= 0) {
													if (srNotNeedDoNum == cycTotalCount) {
														_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
														_myidpos.setPlanCount(countString);

														backClose();
														AppManager
																.getAppManager()
																.finishActivity(
																		NewDianjian_Main.this);
													} else {
														switch (CURRENT_MODE) {
														case PopMenu_EDIT:
															currentPlanIndex = -1;
															loadNextDJPlan();
															break;
														case PopMenu_CHECK:
															_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
															_myidpos.setPlanCount(countString);

															backClose();
															AppManager
																	.getAppManager()
																	.finishActivity(
																			NewDianjian_Main.this);
															break;
														}
													}
												} else {
													dispIndexOfDJPlan();
												}
											} else {
												UIHelper.ToastMessage(
														NewDianjian_Main.this,
														getString(R.string.plan_mincoast_notice3));
											}
										}
									}
								}).setCanceledOnTouchOutside(false).show();

				return result = "false";
			}
			currentPlanIndex = currentPlanIndex - 1;
		}

		mDJPlan = AppContext.YXDJPlanByIDPosBuffer.get(current_IDPos_ID).get(
				currentPlanIndex);

		// 如果是特巡，则不做任何过滤
		if (AppContext.DJSpecCaseFlag == 1) {
			currentDJPlan = mDJPlan;
			return result = "true";
		}

		String emsg = "";
		// [start] 巡点检
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			result = caculateCurPlan_XDJ(nextFlag, IsJudgeComplete);
		}
		// [end]
		// 点检排程
		else if (AppContext.getCurrLineType() == AppConst.LineType.DJPC
				.getLineType()) {
			result = caculateCurPlan_DJPC(nextFlag, IsJudgeComplete);
		}
		// [start] 条件巡检
		else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ
				.getLineType()) {
			result = caculateCurPlan_CaseXJ(nextFlag, IsJudgeComplete);
		}
		// [end]
		return result;
	}

	private DJ_IDPos getIDPosInfoByIDPosID(int ID) {
		int index = -1;
		if (AppContext.DJIDPosBuffer == null
				|| AppContext.DJIDPosBuffer.isEmpty()) {
			return null;
		}
		Iterator<DJ_IDPos> iter = AppContext.DJIDPosBuffer.iterator();
		while (iter.hasNext()) {
			index++;
			if (Integer.parseInt(iter.next().getIDPos_ID()) == ID) {
				return AppContext.DJIDPosBuffer.get(index);
			}
		}
		return null;
	}

	/**
	 * 判断结果是否超出范围及是否为异常数据
	 * 
	 * @param result
	 */
	private void checkResult(double result) {
		alarmName = "";
		m_ExceptionYN = "0";
		emsg = "";
		m_ExLevelCD = 0;
		if (!IsOverRange(result)) {
			plan_input_mes_almlevel
					.setText(R.string.plan_result_outofrange_notice);
			plan_input_mes_almlevel.setTextColor(Color.RED);
			return;
		}

		if (IsException(result)) {
			// 记录类
			if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {
				edit_plan_result_jl.setTag(m_ExceptionYN);
			}
			// 测温，测振，测速
			else if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)
					|| currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
					|| currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CS)) {
				txt_plan_result_others.setTag(m_ExceptionYN);
			}

			plan_input_mes_almlevel.setText(emsg);
			plan_input_mes_almlevel.setTag(m_ExLevelCD.toString());
			plan_input_mes_almlevel.setTextColor(Color.RED);
			AppContext.voiceConvertService.Speaking(emsg);
		} else {
			// 记录类
			if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {
				edit_plan_result_jl.setTag(null);
			}
			// 测温，测振，测速
			else if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)
					|| currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
					|| currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CS)) {
				txt_plan_result_others.setTag(m_ExceptionYN);
			}
			plan_input_mes_almlevel.setText(R.string.plan_gc_defaultresult);
			plan_input_mes_almlevel.setTag("0");
			plan_input_mes_almlevel.setTextColor(Color.BLACK);
		}
	}

	@Override
	protected void onDestroy() {
		try {
			if (loading != null) {
				loading.dismiss();
			}
			if (saving != null) {
				saving.dismiss();
			}
		} catch (Exception e) {
			// System.out.println("Dialog取消，失败！");
		}
		AppContext.voiceConvertService.StopSpeaking();

		super.onDestroy();
	}

	/**
	 * 量程判断
	 * 
	 * @param reult
	 * @return
	 */
	private boolean IsOverRange(double result) {
		try {
			Double uper = null;
			Double down = null;
			if (!StringUtils.isEmpty(currentDJPlan.getDJPlan()
					.getParmLowerLimit_TX())) {
				down = Double.parseDouble(currentDJPlan.getDJPlan()
						.getParmLowerLimit_TX());
			}
			if (!StringUtils.isEmpty(currentDJPlan.getDJPlan()
					.getParmUpperLimit_TX())) {
				uper = Double.parseDouble(currentDJPlan.getDJPlan()
						.getParmUpperLimit_TX());
			}
			if (uper != null && down != null) {
				return ((uper >= result) && (down <= result));
			} else if (down != null) {
				return down <= result;
			} else if (uper != null) {
				return uper >= result;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return true;
		}
	}

	// 超上限判断
	private boolean CheckResultForUpper(double result, String uppervalue,
			int warninglevel) {

		if (result > Double.parseDouble(uppervalue)) {
			GetAlarmName(warninglevel);
			return true;
		}
		return false;
	}

	// 超下限判断
	private boolean CheckResultForLower(double result, String lowervalue,
			int warninglevel) {
		if (result < Double.parseDouble(lowervalue)) {
			GetAlarmName(warninglevel);
			return true;
		}
		return false;
	}

	// 窗内报警判断
	private boolean CheckResultForInWindow(double result, String uppervalue,
			String lowervalue, int warninglevel) {
		if (result < Double.parseDouble(uppervalue)
				&& result > Double.parseDouble(lowervalue)) {
			GetAlarmName(warninglevel);
			return true;
		}
		return false;
	}

	// 窗外报警判断
	private boolean CheckResultForOutWindow(double result, String uppervalue,
			String lowervalue, int warninglevel) {
		if (result > Double.parseDouble(uppervalue)
				|| result < Double.parseDouble(lowervalue)) {
			GetAlarmName(warninglevel);
			return true;
		}
		return false;
	}

	// 差值报警判断
	private boolean CheckResultForDifferent(double result, int warninglevel) {
		double WARNINGVALUE_NR = currentDJPlan.getDJPlan().getWARNINGVALUE_NR();

		double LastResult_TX = Double.parseDouble(currentDJPlan.getDJPlan()
				.getLastResultForDifferent_TX());
		// 测振位移需要将单位mm转化为um
		if (currentDJPlan.getDJPlan().getDataType_CD().equals("CZ")
				&& currentDJPlan.getDJPlan().getZhenDong_Type().equals("S")) {
			String s = currentDJPlan.getDJPlan().getLastResultForDifferent_TX();
			LastResult_TX = Double.valueOf(s) * 1000;
		}
		double currResult = result;
		double aa = Math.abs(currResult - LastResult_TX);
		if (aa > WARNINGVALUE_NR) {
			alarmName = StringUtils.isEmpty(AppContext.almLevelBuffer.get(
					warninglevel).getName_TX()) ? AppConst.AlarmName_Alert
					: AppContext.almLevelBuffer.get(warninglevel).getName_TX();
			m_ExceptionYN = "1";
			m_ExLevelCD = warninglevel;
			emsg = getString(R.string.plan_errordata_notice1, alarmName);
			return true;
		}
		return false;
	}

	private void GetAlarmName(int warninglevel) {
		String tempName = "";
		switch (warninglevel) {
		case 4:
			tempName = getString(R.string.plan_alarmlevel_Pre_Dangerous);
			break;
		case 3:
			tempName = getString(R.string.plan_alarmlevel_Pre_Alert);
			break;
		case 2:
			tempName = getString(R.string.plan_alarmlevel_Warning);
			break;
		case 1:
			tempName = getString(R.string.plan_alarmlevel_Pre_Alarm);
			break;
		}
		if (AppContext.almLevelBuffer != null
				&& AppContext.almLevelBuffer.size() > 0) {
			alarmName = StringUtils.isEmpty(AppContext.almLevelBuffer.get(
					warninglevel).getName_TX()) ? tempName
					: AppContext.almLevelBuffer.get(warninglevel).getName_TX();
		} else
			alarmName = tempName;
		m_ExceptionYN = "1";
		m_ExLevelCD = warninglevel;
		emsg = getString(R.string.plan_errordata_notice2, alarmName);
	}

	/**
	 * 更加报警窗口访问判断报警等级
	 * 
	 * @param result
	 * @param alarmName
	 * @param m_ExceptionYN
	 * @param m_ExLevelCD
	 * @param emsg
	 * @return
	 */
	private boolean IsException(double result) {
		m_ExceptionYN = "0";
		m_ExLevelCD = 0;
		alarmName = " ";
		if (currentDJPlan.getDJPlan().getAlarmType_ID() == null)
			return false;
		switch (currentDJPlan.getDJPlan().getAlarmType_ID()) {
		case 0: // 超上限
			if (currentDJPlan.getDJPlan().getUpperLimit4_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit4_TX().length() > 0) {
				if (CheckResultForUpper(result, currentDJPlan.getDJPlan()
						.getUpperLimit4_TX(), 4))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit3_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit3_TX().length() > 0) {
				if (CheckResultForUpper(result, currentDJPlan.getDJPlan()
						.getUpperLimit3_TX(), 3))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit2_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit2_TX().length() > 0) {
				if (CheckResultForUpper(result, currentDJPlan.getDJPlan()
						.getUpperLimit2_TX(), 2))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit1_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit1_TX().length() > 0) {
				if (CheckResultForUpper(result, currentDJPlan.getDJPlan()
						.getUpperLimit1_TX(), 1))
					return true;
			}
			break;
		case 1: // 超下限
			if (currentDJPlan.getDJPlan().getLowerLimit4_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit4_TX().length() > 0) {
				if (CheckResultForLower(result, currentDJPlan.getDJPlan()
						.getLowerLimit4_TX(), 4))
					return true;
			}
			if (currentDJPlan.getDJPlan().getLowerLimit3_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit3_TX().length() > 0) {
				if (CheckResultForLower(result, currentDJPlan.getDJPlan()
						.getLowerLimit3_TX(), 3))
					return true;
			}
			if (currentDJPlan.getDJPlan().getLowerLimit2_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit2_TX().length() > 0) {
				if (CheckResultForLower(result, currentDJPlan.getDJPlan()
						.getLowerLimit2_TX(), 2))
					return true;
			}
			if (currentDJPlan.getDJPlan().getLowerLimit1_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit1_TX().length() > 0) {
				if (CheckResultForLower(result, currentDJPlan.getDJPlan()
						.getLowerLimit1_TX(), 1))
					return true;
			}
			break;
		case 2: // 窗内报警
			if (currentDJPlan.getDJPlan().getUpperLimit4_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit4_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit4_TX().length() > 0
					&& currentDJPlan.getDJPlan().getLowerLimit4_TX().length() > 0) {
				if (CheckResultForInWindow(result, currentDJPlan.getDJPlan()
						.getUpperLimit4_TX(), currentDJPlan.getDJPlan()
						.getLowerLimit4_TX(), 4))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit3_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit3_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit3_TX().length() > 0
					&& currentDJPlan.getDJPlan().getLowerLimit3_TX().length() > 0) {
				if (CheckResultForInWindow(result, currentDJPlan.getDJPlan()
						.getUpperLimit3_TX(), currentDJPlan.getDJPlan()
						.getLowerLimit3_TX(), 3))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit2_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit2_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit2_TX().length() > 0
					&& currentDJPlan.getDJPlan().getLowerLimit2_TX().length() > 0) {
				if (CheckResultForInWindow(result, currentDJPlan.getDJPlan()
						.getUpperLimit2_TX(), currentDJPlan.getDJPlan()
						.getLowerLimit2_TX(), 2))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit1_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit1_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit1_TX().length() > 0
					&& currentDJPlan.getDJPlan().getLowerLimit1_TX().length() > 0) {
				if (CheckResultForInWindow(result, currentDJPlan.getDJPlan()
						.getUpperLimit1_TX(), currentDJPlan.getDJPlan()
						.getLowerLimit1_TX(), 1))
					return true;
			}
			break;
		case 3: // 窗外报警
			if (currentDJPlan.getDJPlan().getUpperLimit4_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit4_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit4_TX().length() > 0
					&& currentDJPlan.getDJPlan().getLowerLimit4_TX().length() > 0) {
				if (CheckResultForOutWindow(result, currentDJPlan.getDJPlan()
						.getUpperLimit4_TX(), currentDJPlan.getDJPlan()
						.getLowerLimit4_TX(), 4))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit3_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit3_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit3_TX().length() > 0
					&& currentDJPlan.getDJPlan().getLowerLimit3_TX().length() > 0) {
				if (CheckResultForOutWindow(result, currentDJPlan.getDJPlan()
						.getUpperLimit3_TX(), currentDJPlan.getDJPlan()
						.getLowerLimit3_TX(), 3))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit2_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit2_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit2_TX().length() > 0
					&& currentDJPlan.getDJPlan().getLowerLimit2_TX().length() > 0) {
				if (CheckResultForOutWindow(result, currentDJPlan.getDJPlan()
						.getUpperLimit2_TX(), currentDJPlan.getDJPlan()
						.getLowerLimit2_TX(), 2))
					return true;
			}
			if (currentDJPlan.getDJPlan().getUpperLimit1_TX() != null
					&& currentDJPlan.getDJPlan().getLowerLimit1_TX() != null
					&& currentDJPlan.getDJPlan().getUpperLimit1_TX().length() > 0
					&& currentDJPlan.getDJPlan().getLowerLimit1_TX().length() > 0) {
				if (CheckResultForOutWindow(result, currentDJPlan.getDJPlan()
						.getUpperLimit1_TX(), currentDJPlan.getDJPlan()
						.getLowerLimit1_TX(), 1))
					return true;
			}
			break;
		case 4: // 差值报警
			if (currentDJPlan.getDJPlan().getLastResultForDifferent_TX() != null
					&& !StringUtils.isEmpty(currentDJPlan.getDJPlan()
							.getLastResultForDifferent_TX())
					&& currentDJPlan.getDJPlan().getWARNINGVALUE_NR() != null) {
				if (CheckResultForDifferent(result, 4))
					return true;
			}
			break;
		}

		return false;
	}

	/**
	 * 启停点
	 * 
	 * @param adjline
	 */
	DianJianSRDialog srDialog;

	private void showSR(boolean autoYN, int srno) {
		if (srDialog != null && srDialog.isShowing())
			srDialog.dismiss();
		srDialog = new DianJianSRDialog(NewDianjian_Main.this,
				new PriorityListener() {

					@Override
					public void refreshPriorityUI(boolean autoYn) {
						// 如果启停点是手动进入修改的，当启停点修改后，重新计算计划并跳至首条未做计划
						/*
						 * if (!autoYn) { LoadFirstDJPLanWithOutCompleted(); }
						 * // 如果启停点下该计划不做，则加载下一条 else if
						 * (!currentDJPlan.JudgePlanBySrPoint()) { switch
						 * (CURRENT_MODE) { case PopMenu_EDIT: loadNextDJPlan();
						 * break; case PopMenu_CHECK: loadNextUnDoDJPlan();
						 * break; } }
						 */
					}

					@Override
					public void comeBackToDianjianMain(boolean autoYn) {
						if (!autoYn) {
							LoadFirstDJPLanWithOutCompleted();
						} else {
							if (!currentDJPlan.JudgePlanBySrPoint()) {
								switch (CURRENT_MODE) {
								case PopMenu_EDIT:
									loadNextDJPlan();
									break;
								case PopMenu_CHECK:
									loadNextUnDoDJPlan();
									break;
								}
							} else {
								if (currentDJPlan.JudgePlanIsLKControl()
										&& (!currentDJPlan.getLKDoYn())
										&& (!StringUtils.isEmpty(currentDJPlan
												.getDJPlan().getMustCheck_YN()))
										&& (currentDJPlan.getDJPlan()
												.getMustCheck_YN().equals("0"))) {
									List<DJPlan> lkPlanList = DJPlanHelper
											.GetIntance()
											.GetPlanListByControlPoint(
													NewDianjian_Main.this,
													currentDJPlan.getDJPlan()
															.getLKPoint_ID(),
													current_IDPos_ID);
									// 有效主控点下计划列表，经过启停点处理
									ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
									for (DJPlan _planInfo : lkPlanList) {
										yxplanList.add(_planInfo);
									}
									if (yxplanList != null
											&& yxplanList.size() > 0)
										selectLK(currentDJPlan.getDJPlan()
												.getLKPoint_ID(), yxplanList);
								}
							}
						}
						// 受主控控制并且还未设置过主控
						/*
						 * if (currentDJPlan.JudgePlanIsLKControl() &&
						 * (!currentDJPlan.getLKDoYn()) &&
						 * (!StringUtils.isEmpty(currentDJPlan
						 * .getDJPlan().getMustCheck_YN())) &&
						 * (currentDJPlan.getDJPlan().getMustCheck_YN()
						 * .equals("0"))) { List<DJPlan> lkPlanList =
						 * DJPlanHelper.GetIntance() .GetPlanListByControlPoint(
						 * currentDJPlan.getDJPlan() .getLKPoint_ID(),
						 * current_IDPos_ID); // 有效主控点下计划列表，经过启停点处理
						 * ArrayList<DJPlan> yxplanList = new
						 * ArrayList<DJPlan>(); for (DJPlan _planInfo :
						 * lkPlanList) { yxplanList.add(_planInfo); } if
						 * (yxplanList != null && yxplanList.size() > 0)
						 * selectLK(currentDJPlan.getDJPlan() .getLKPoint_ID(),
						 * yxplanList); }
						 */
					}
				}, current_IDPos_ID, mDJPlan, autoYN, srno);
		srDialog.show();
	}

	/**
	 * 钮扣最小用时检查
	 * 
	 * @return
	 */
	private String checkIDPosMinCost() {
		try {
			String msg = "";
			int days = (int) (DateTimeHelper.getDistanceTimes(AppContext
					.getCurIDPos().getLastArrivedTime_DT(), DateTimeHelper
					.getDateTimeNow())[0]);
			int hours = (int) (DateTimeHelper.getDistanceTimes(AppContext
					.getCurIDPos().getLastArrivedTime_DT(), DateTimeHelper
					.getDateTimeNow())[1]);
			int mins = (int) (DateTimeHelper.getDistanceTimes(AppContext
					.getCurIDPos().getLastArrivedTime_DT(), DateTimeHelper
					.getDateTimeNow())[2]);
			int seconds = (int) (DateTimeHelper.getDistanceTimes(AppContext
					.getCurIDPos().getLastArrivedTime_DT(), DateTimeHelper
					.getDateTimeNow())[3]);
			int getCostSeconds = days * 24 * 60 * 60 + hours * 60 * 60 + mins
					* 60 + seconds;

			if (getCostSeconds < AppContext.getCurIDPos().getCostDateLimit_NR() * 60) {
				int remain = AppContext.getCurIDPos().getCostDateLimit_NR()
						* 60 - getCostSeconds;
				int remainSeconds = remain % 60;
				int remainMins = remain / 60;
				if (remainMins <= 0) {
					msg = getString(R.string.plan_mincoast_notice1,
							String.valueOf(AppContext.getCurIDPos()
									.getCostDateLimit_NR()),
							String.valueOf(remainSeconds));
				} else {
					msg = getString(R.string.plan_mincoast_notice2,
							String.valueOf(AppContext.getCurIDPos()
									.getCostDateLimit_NR()),
							String.valueOf(remainMins),
							String.valueOf(remainSeconds));
				}
				return msg;
			} else
				return "";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * 根据ID获取结果选项对象
	 * 
	 * @param gID
	 * @return
	 */
	private Z_DataCodeGroup getDataCodeGroupByID(String gID) {
		if (AppContext.dataCodeBuffer != null
				&& AppContext.dataCodeBuffer.size() > 0)
			for (Z_DataCodeGroup _data : AppContext.dataCodeBuffer) {
				if (_data.getDataCodeGroup_ID().equals(gID)) {
					return _data;
				}
			}
		return null;
	}

	/**
	 * 结果选项
	 */
	private void selectResItem() {
		if (currentDJPlan.getDJPlan().getDataCodeGroup_ID() <= 0)
			return;
		String selfTitleContent = getString(R.string.plan_plandesc1,
				currentDJPlan.getDJPlan().getPlanDesc_TX());
		List<List<String>> data = new ArrayList<List<String>>();
		for (Z_DataCodeGroupItem _item : currentDJPlan.getDataCodeGroup()
				.getDataCodeGroupItems()) {
			List<String> _temList = new ArrayList<String>();
			_temList.add(_item.getItem_ID());// ID
			_temList.add(_item.getName_TX());// Name
			_temList.add("0");// 选中标识
			_temList.add(_item.getStatus_CD());
			_temList.add(String.valueOf(_item.getAlarmLevel_ID()));
			data.add(_temList);
		}
		SimpleMultiListViewDialog _dialog = new SimpleMultiListViewDialog(
				NewDianjian_Main.this, new SimpleMultiListViewDialogListener() {

					@Override
					public void refreshParentUI() {
						// TODO 自动生成的方法存根

					}

					@Override
					public void goBackToParentUI() {
						// TODO 自动生成的方法存根

					}

					@Override
					public void btnOK(DialogInterface dialog,
							List<List<String>> mData) {
						// 0-ID 1-结果 2-选中标识 3-异常标识 4-报警等级ID
						String _result = "";
						String _expTag = "0";
						String _almID = "0";
						if (mData != null && mData.size() > 0) {
							for (List<String> list : mData) {
								if (list.get(2).equals("1")) {
									_result += "/" + list.get(1);
									_expTag = list.get(3);
									_almID = Integer.parseInt(_almID) > Integer
											.parseInt(list.get(4)) ? _almID
											: list.get(4);
								}
							}
							if (StringUtils.isEmpty(_result)) {
								UIHelper.ToastMessage(getApplication(),
										R.string.plan_choosedataitems);
								return;
							}
							String resString = _result.substring(1);
							if (_expTag.equals("1"))
								plan_input_mes_almlevel.setTextColor(Color.RED);
							else {
								plan_input_mes_almlevel
										.setTextColor(Color.BLACK);
							}
							if (AppContext.almLevelBuffer != null
									&& AppContext.almLevelBuffer.size() > 0) {
								plan_input_mes_almlevel
										.setText(AppContext.almLevelBuffer.get(
												Integer.parseInt(_almID))
												.getName_TX());// 报警名称
								plan_input_mes_almlevel.setTag(Integer
										.parseInt(_almID));
							} else {
								plan_input_mes_almlevel
										.setText(R.string.plan_gc_defaultresult);// 报警名称
								plan_input_mes_almlevel.setTag(0);
							}
							txt_plan_result_others.setText(resString);// 结果
							txt_plan_result_others.setTag(_expTag);
							dialog.dismiss();
						}
					}
				}, data, true, selfTitleContent);
		_dialog.setCancelButton(true);
		_dialog.setTitle(R.string.dianjian_resitem_dialog_title);
		_dialog.show();
	}

	/**
	 * 根据上次结果获取上次报警等级(观察类计划)
	 * 
	 * @param lastResult
	 */
	private void getLastAlarmLevelByLastResult(String lastResult) {
		int alarmlevelid = 0;
		String expYN = "0";
		if (currentDJPlan.getDataCodeGroup().getDataCodeGroupItems() != null
				&& currentDJPlan.getDataCodeGroup().getDataCodeGroupItems()
						.size() > 0) {
			String[] result = lastResult.split("/");
			for (String s : result) {
				int index = 0;
				for (int i = 0; i < currentDJPlan.getDataCodeGroup()
						.getDataCodeGroupItems().size(); i++) {
					if (s.equals(currentDJPlan.getDataCodeGroup()
							.getDataCodeGroupItems().get(i).getName_TX())) {
						index = i;
						break;
					}
				}
				alarmlevelid = alarmlevelid > currentDJPlan.getDataCodeGroup()
						.getDataCodeGroupItems().get(index).getAlarmLevel_ID() ? alarmlevelid
						: currentDJPlan.getDataCodeGroup()
								.getDataCodeGroupItems().get(index)
								.getAlarmLevel_ID();
				expYN = currentDJPlan.getDataCodeGroup()
						.getDataCodeGroupItems().get(index).getStatus_CD();
			}

			txt_plan_result_others.setTag(expYN);
			if (AppContext.almLevelBuffer != null
					&& AppContext.almLevelBuffer.size() > 0) {
				plan_input_mes_almlevel.setText(AppContext.almLevelBuffer.get(
						alarmlevelid).getName_TX());// 报警名称
				plan_input_mes_almlevel.setTag(alarmlevelid);
				if (expYN.equals("0"))
					plan_input_mes_almlevel.setTextColor(Color.BLACK);
				else
					plan_input_mes_almlevel.setTextColor(Color.RED);
			} else {
				plan_input_mes_almlevel.setText(R.string.plan_gc_defaultresult);
				plan_input_mes_almlevel.setTag(0);
			}
		} else {
			txt_plan_result_others.setTag("0");
			plan_input_mes_almlevel.setText(R.string.plan_gc_defaultresult);
			plan_input_mes_almlevel.setTag(0);
		}
	}

	static boolean savingLKYN = false;
	/**
	 * 主控点
	 * 
	 * @param adjline
	 */
	private void selectLK(final String lkID, final ArrayList<DJPlan> yxplanList) {
		DJ_ControlPoint controlPoint = DJPlanHelper.GetIntance().getLKbyID(
				NewDianjian_Main.this, lkID);
		if (controlPoint != null && yxplanList != null && yxplanList.size() > 0) {
			final DianJianLKDialog lkDialog = new DianJianLKDialog(
					NewDianjian_Main.this, new LKListener() {

						@Override
						public void refreshParentUI() {
							// TODO 自动生成的方法存根

						}

						@Override
						public void goBackToParentUI() {
							// TODO 自动生成的方法存根
						}

						@Override
						public void btnOK(final DialogInterface dialog,
								final List<DJPlan> mDJPlan) {
							// 首先完成被选中的计划的批量处理，然后关掉对话框
							if (!savingLKYN) {
							if (mDJPlan != null && mDJPlan.size() > 0) {
								savingLKYN = true;
								mHandler = new Handler() {
									public void handleMessage(Message msg) {
										if (saving != null)
											saving.dismiss();
										if (msg.what == 1) {
											/* 循环保存完主控点计划后，更新钮扣到位结束时间 */
											DJ_IDPos _curridpos = getIDPosInfoByIDPosID(Integer
													.valueOf(current_IDPos_ID));
											DJIDPosHelper.GetIntance()
													.updateDJTaskIDPos(
															getApplication(),
															_curridpos,
															current_DJLine_ID);

											// 如果主控点下未批处理所有计划，那么返回时，定位到主控点下第一条未做计划
											if (yxplanList != null
													&& mDJPlan != null) {
												for (DJPlan _item : yxplanList) {
													if (!mDJPlan
															.contains(_item)) {
														currentPlanIndex = AppContext.YXDJPlanByIDPosBuffer
																.get(current_IDPos_ID)
																.indexOf(_item) - 1;
														break;
													}
												}
											}
											LoadFirstDJPLanWithOutCompleted();
											// loadNextUnDoDJPlan();

										} else {
											UIHelper.ToastMessage(
													getApplication(),
													((Exception) (msg.obj))
															.getMessage());
										}
										dialog.dismiss();
										savingLKYN = false;
									}
								};

								saveDJResultForLKThread(mDJPlan);
							}
						}
						}

						@Override
						public void btnCancel(DialogInterface dialog,
								List<DJPlan> mDJPlan) {
							// TODO Auto-generated method stub

						}
					}, yxplanList);
			lkDialog.setTitle(getString(R.string.plan_lk,
					controlPoint.getName_TX()));
			lkDialog.show();
		}
	}

	private void saveDJResultForLKThread(final List<DJPlan> mDJPlan) {
		saving = new LoadingDialog(this);
		saving.setLoadText(getString(R.string.save_ing));
		saving.setCancelable(false);
		saving.show();
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 批量保存主控数据
					saveDJResultForLK(mDJPlan);
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	// 保存数据时接收的handler
	Handler saveHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 保存成功
				loadNextUnDoDJPlan();
			} else {
				btn_savedata.setEnabled(true);
				isClickableOK = true;
				if (msg.what == -1) {// 保存失败
				} else if (msg.what == 2) {// 修改成功
					UIHelper.ToastMessageForSaveOK(getApplication(),
							R.string.plan_edit_ok);
				}
			}
		}
	};


	/**
	 * 点击确认按钮和OK键保存数据
	 */
	private synchronized void saveData() {
		btn_savedata.setEnabled(false);
		isClickableOK = false;
		UIHelper.ThreadToast(NewDianjian_Main.this);
		
		final String completeTime = DateTimeHelper.getDateTimeNow();
		new Thread(){
			public void run() {
				Message message = new Message();
				try {
					// 这里直接判断当前计划是否为空即可，不用再取一次
					// 而且修改计划的最后完成时间也在保存结果后修改，无需再修改一次
					if (currentDJPlan.getDJPlan() == null) {
						DJ_IDPos _curridpos = getIDPosInfoByIDPosID(Integer
								.valueOf(current_IDPos_ID));
	
						// 如果本条是修改的，则不自动加载下一条
						if (currDJPlancompletedYN) {
							message.what = 2;
							saveHandler.sendMessage(message);
						} else {
							// 点检时，更新钮扣到位结束时间，修改时不更新
							DJIDPosHelper.GetIntance().updateDJTaskIDPos(
									NewDianjian_Main.this, _curridpos,
									current_DJLine_ID);
							message.what = 1;
							saveHandler.sendMessage(message);
						}
						return;
					}
				
				
					if (currDJPlancompletedYN && currDJResultActive == null) {
						UIHelper.ToastMessage(getApplication(),
								R.string.plan_save_notice1);
						message.what = -1;
						saveHandler.sendMessage(message);
						return;
					}
					int costTime = (int) ((DateTimeHelper.GetDateTimeNow().getTime() - startTime
							.getTime()) / 1000);
					String Result_TX = "";
					if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {// 记录类
						Result_TX = String.valueOf(edit_plan_result_jl.getText());
					} else if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)
							|| currentDJPlan.getDJPlan().getDataType_CD()
									.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
							|| currentDJPlan.getDJPlan().getDataType_CD()
									.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CS)) {// 测温类,
																						// 测振类，测速类
						Result_TX = String.valueOf(txt_plan_result_others.getText());
					} else if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_GC)) { // 观察类
						Result_TX = String.valueOf(txt_plan_result_others.getText());
					}
					String Memo_TX = String.valueOf(planmemo_editor.getText());
					if (StringUtils.isEmpty(Result_TX)) {
						UIHelper.ToastMessage(getApplication(),
								R.string.plan_stringEmpty_result);
						message.what = -1;
						saveHandler.sendMessage(message);
						return;
					} else {
						if (currentDJPlan.getDJPlan().getDataType_CD()
								.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)
								|| currentDJPlan.getDJPlan().getDataType_CD()
										.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)
								|| currentDJPlan.getDJPlan().getDataType_CD()
										.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
								|| currentDJPlan.getDJPlan().getDataType_CD()
										.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CS)) {
							if (Result_TX.equals(".") || Result_TX.equals("-")
									|| Result_TX.equals("+")) {
								UIHelper.ToastMessage(getApplication(),
										R.string.plan_wrongFormat_result);
								message.what = -1;
								saveHandler.sendMessage(message);
								return;
							}
							try {
								if (!StringUtils.isEmpty(Result_TX)
										&& !IsOverRange(Double.valueOf(Result_TX))) {
									UIHelper.ToastMessage(getApplication(),
											R.string.plan_result_notinrange_notice);
									message.what = -1;
									saveHandler.sendMessage(message);
									return;
								}
							} catch (NumberFormatException nfe) {
								UIHelper.ToastMessage(NewDianjian_Main.this,
										getString(R.string.plan_wrongFormat_result));
								message.what = -1;
								saveHandler.sendMessage(message);
								return;
							}
						}
					}
					if (Memo_TX.length() > 80) {
						UIHelper.ToastMessage(getApplication(),
								R.string.plan_most_memo_length);
						message.what = -1;
						saveHandler.sendMessage(message);
						return;
					}
					DJ_ResultActive djResult = new DJ_ResultActive();
					if (plan_input_mes_almlevel.getTag() != null) {
						Integer almID = Integer.parseInt((String
								.valueOf(plan_input_mes_almlevel.getTag())));
						djResult.setAlarmLevel_ID(almID);
					} else {
						djResult.setAlarmLevel_ID(0);
					}
					djResult.setAppUser_CD(AppContext.getUserCD());
					djResult.setAppUserName_TX(AppContext.getUserName());

					djResult.setCompleteTime_DT(completeTime);
					// currentDJPlan.getDJPlan().setLastComplete_DT(completeTime);
					/*
					 * if (AppContext.DJSpecCaseFlag == 0)
					 * currentDJPlan.getDJPlan().setLastComplete_DT(completeTime); else
					 * { currentDJPlan.getDJPlan().setLastComplete_DT(null); }
					 */

					djResult.setDataFlag_CD("M");

					djResult.setDJ_Plan_ID(currentDJPlan.getDJPlan().getDJ_Plan_ID());
					if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {
						if (edit_plan_result_jl.getTag() != null) {
							String expYN = String.valueOf(edit_plan_result_jl.getTag());
							expYN = expYN.equals("0") ? "0" : "1";
							djResult.setException_YN(expYN);
						} else {
							djResult.setException_YN("0");
						}
					} else if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_GC)) {
						if (txt_plan_result_others.getTag() != null) {
							String expYN = String.valueOf(txt_plan_result_others
									.getTag());
							expYN = expYN.equals("0") ? "0" : "1";
							djResult.setException_YN(expYN);
						} else {
							djResult.setException_YN("0");
						}
					} else if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)) {
						// 测温类
						djResult.setException_YN(m_ExceptionYN);
					} else if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)) {
						// 测振类
						djResult.setException_YN(m_ExceptionYN);
					} else {
						djResult.setException_YN("0");
					}

					if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)
							|| currentDJPlan.getDJPlan().getDataType_CD()
									.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)
							|| currentDJPlan.getDJPlan().getDataType_CD()
									.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
							|| currentDJPlan.getDJPlan().getDataType_CD()
									.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CS)) {
						try {
							if (!StringUtils.isEmpty(currentDJPlan.getDJPlan()
								.getLastResult_TX())) {
								Double.valueOf(currentDJPlan.getDJPlan()
									.getLastResult_TX());
							}
							djResult.setLastResult_TX(currentDJPlan.getDJPlan()
									.getLastResult_TX());
						} catch (NumberFormatException nfe) {
							// 如果上次结果与计划的格式不一样，则不赋值
							djResult.setLastResult_TX("");
							/*UIHelper.ToastMessage(this,
									getString(R.string.plan_wrongFormat_hisresult));
							return false;*/
						}
						
					} else {
						djResult.setLastResult_TX(currentDJPlan.getDJPlan()
								.getLastResult_TX());
					}
					djResult.setMemo_TX(Memo_TX.replaceAll("'", ""));
					djResult.setMObjectStateName_TX("");

					// 巡点检根据跨天计算查询日期
					String queryDT = XSTMethodByLineTypeHelper.getInstance()
							.getQueryDT(currentDJPlan);
					djResult.setQuery_DT(queryDT);

					/*
					 * if (AppContext.getCurrLineType() == AppConst.LineType.XDJ
					 * .getLineType()) { if (AppContext.DJSpecCaseFlag == 1) {
					 * djResult.setQuery_DT(DateTimeHelper.getDateTimeNow()); } else {
					 * djResult.setQuery_DT(DateTimeHelper .DateToString(currentDJPlan
					 * .GetQueryDate(DateTimeHelper .GetDateTimeNow()))); } } else
					 * djResult.setQuery_DT(DateTimeHelper.getDateTimeNow());
					 */

					djResult.setResult_TX(Result_TX);
					// currentDJPlan.getDJPlan().setLastResult_TX(Result_TX);
					djResult.setSpecCase_TX(currentDJPlan.getSpecCaseNames());
					djResult.setSpecCase_YN(AppContext.DJSpecCaseFlag == 1 ? "1" : "0");
					djResult.setTime_NR(costTime);

					String transinfo = XSTMethodByLineTypeHelper.getInstance()
							.setResultTransInfo(
									currentDJPlan.getDJPlan().getTransInfo_TX());
					djResult.setTransInfo_TX(transinfo);

					// 条件巡检
					/*
					 * if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ
					 * .getLineType()) { if
					 * (!StringUtils.isEmpty(AppConst.PlanTimeIDStr)) {
					 * djResult.setTransInfo_TX(currentDJPlan.getDJPlan()
					 * .getTransInfo_TX() + "|" + AppConst.PlanTimeIDStr); } } else {
					 * djResult.setTransInfo_TX(currentDJPlan.getDJPlan()
					 * .getTransInfo_TX()); }
					 */
					String shiftname = "";
					String shiftGroupName = "";
					// 巡点检计算班次，职别
					shiftname = XSTMethodByLineTypeHelper.getInstance().getShiftName(
							NewDianjian_Main.this, currentDJPlan);
					shiftGroupName = XSTMethodByLineTypeHelper.getInstance()
							.getShiftGroupName(NewDianjian_Main.this, currentDJPlan);

					/*
					 * if (AppContext.getCurrLineType() == AppConst.LineType.XDJ
					 * .getLineType() || AppContext.getCurrLineType() ==
					 * AppConst.LineType.DJPC .getLineType()) { shiftname =
					 * DJShiftHelper.GetIntance().getShiftNameByCD(this,
					 * currentDJPlan.GetCycle().getCurShiftNo()); shiftGroupName =
					 * DJShiftHelper.GetIntance() .computeNowShiftGroupName(this,
					 * DateTimeHelper.GetDateTimeNow(),
					 * currentDJPlan.GetCycle().getCurShiftNo()); }
					 */
					djResult.setShiftName_TX(shiftname);
					djResult.setShiftGroupName_TX(shiftGroupName);

					/* 测振相关赋值 */
					if (currentDJPlan.getDJPlan().getDataType_CD()
							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)) {
						djResult.setData8K_TX(timewave);
						djResult.setDataLen(datalen);
						if (currentDJPlan.getDJPlan().getMaxFreq_NR() == null) {
							djResult.setRate((int) (1000 * 2.56));
						} else {
							// djResult.setRate((int) (currentDJPlan.getDJPlan()
							// .getMaxFreq_NR() * 2.56));
							djResult.setRate(rate);
						}
						djResult.setRatio1_NR(true);
						djResult.setRatio_NR(true);
						if (currentDJPlan.getDJPlan().getWindowType_NR() == null) {
							djResult.setVibParam_TX(currentDJPlan.getDJPlan()
									.getZhenDong_Type() + ";2;0");
						} else {
							djResult.setVibParam_TX(currentDJPlan.getDJPlan()
									.getZhenDong_Type()
									+ ";"
									+ currentDJPlan.getDJPlan().getWindowType_NR()
									+ ";0");
						}
						djResult.setFeatureValue_TX(featurevalue);
						djResult.setFFTData_TX(fftdata);
					}
					XJ_ResultHis xj_ResultHis = new XJ_ResultHis();
					xj_ResultHis.setLine_ID(current_DJLine_ID);
					xj_ResultHis.setDJ_Plan_ID(djResult.getDJ_Plan_ID());
					xj_ResultHis.setCompleteTime_DT(djResult.getCompleteTime_DT());
					xj_ResultHis.setPlanDesc_TX(currentDJPlan.getDJPlan()
							.getPlanDesc_TX());
					xj_ResultHis.setIDPosName_TX(current_IDPos_Name);
					xj_ResultHis.setAppUser_CD(djResult.getAppUser_CD());
					xj_ResultHis.setAppUserName_TX(djResult.getAppUserName_TX());
					xj_ResultHis.setResult_TX(djResult.getResult_TX());
					xj_ResultHis.setData8K_TX(djResult.getData8K_TX());
					xj_ResultHis.setQuery_DT(djResult.getQuery_DT());
					xj_ResultHis.setMObjectStateName_TX(djResult
							.getMObjectStateName_TX());
					xj_ResultHis.setException_YN(djResult.getException_YN());
					xj_ResultHis.setAlarmLevel_ID(djResult.getAlarmLevel_ID());
					xj_ResultHis.setSpecCase_YN(djResult.getSpecCase_YN());
					xj_ResultHis.setSpecCase_TX(djResult.getSpecCase_TX());
					xj_ResultHis.setMemo_TX(djResult.getMemo_TX());
					xj_ResultHis.setTime_NR(djResult.getTime_NR());
					xj_ResultHis.setDataFlag_CD(djResult.getDataFlag_CD());
					xj_ResultHis.setShiftGroupName_TX(djResult.getShiftGroupName_TX());
					xj_ResultHis.setShiftName_TX(djResult.getShiftName_TX());
					xj_ResultHis.setRatio_NR(djResult.getRatio_NR());
					xj_ResultHis.setRatio1_NR("");
					xj_ResultHis.setRate(djResult.getRate());
					xj_ResultHis.setDataLen(djResult.getDataLen());
					xj_ResultHis.setLastResult_TX(currentDJPlan.getDJPlan()
							.getLastResult_TX());
					xj_ResultHis.setTransInfo_TX(djResult.getTransInfo_TX());
					xj_ResultHis.setVibParam_TX(djResult.getVibParam_TX());
					xj_ResultHis.setFeatureValue_TX(djResult.getFeatureValue_TX());
					xj_ResultHis.setGPSInfo_TX("");
					xj_ResultHis.setDataType_CD(currentDJPlan.getDJPlan()
							.getDataType_CD());
					xj_ResultHis.setZhenDong_Type(currentDJPlan.getDJPlan()
							.getZhenDong_Type());
					xj_ResultHis.setFFTData_TX(djResult.getFFTData_TX());

					//异常数据是否转出缺陷单
					if(AppContext.getTransException()&&djResult.getException_YN().equals("1")){
						message.what = 1;
						Bundle bundle = new Bundle(); 
						bundle.putSerializable("djResult", djResult);
						bundle.putSerializable("xj_ResultHis", xj_ResultHis);
						message.setData(bundle);
						SaveHandler.sendMessage(message);
						return;
					}
					saveDJResult(djResult,xj_ResultHis);
					
				} catch (Exception ex) {
					UIHelper.ToastMessage(getApplication(), ex.toString());
					message.what = -1;
					saveHandler.sendMessage(message);
					return;
				}
			};
		}.start();
	}
	
	com.moons.xst.track.widget.AlertDialog sureDialog;
	DJ_ResultActive djResult;
	XJ_ResultHis xj_ResultHis;
	Handler SaveHandler=new Handler(){
		public void handleMessage(final Message msg) {
			if(msg.what==1){//异常提示
				djResult=(DJ_ResultActive) (msg.getData().getSerializable("djResult"));
				xj_ResultHis=(XJ_ResultHis) (msg.getData().getSerializable("xj_ResultHis"));
				LayoutInflater factory = LayoutInflater.from(NewDianjian_Main.this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				sureDialog=new com.moons.xst.track.widget.AlertDialog(NewDianjian_Main.this)
						.builder()
						.setTitle(getString(R.string.tips))
						.setView(view)
						.setMsg(getString(R.string.dianjian_data_abnormal_hint))
						.setCustomButton(getString(R.string.setting_powersave_setting_on),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										showDialog();
									}
								})
						.setNegativeButton(
								getString(R.string.setting_powersave_setting_off),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										djResult.setTransException_YN("0");
										xj_ResultHis.setTransException_YN("0");
										saveDJResult(djResult,xj_ResultHis);
									}
								}).setCanceledOnTouchOutside(false).setCancelable(false);
				sureDialog.show();
			}
		}
	};
	
	com.moons.xst.track.widget.AlertDialog AlarmDialog;
	EditText et_AlarmDesc;
	TextView tv_type;
	String[] type;
	private void showDialog(){
		LayoutInflater factory = LayoutInflater.from(NewDianjian_Main.this);
		final View view = factory.inflate(R.layout.alarm_particulars_layout,
				null);
		String NoticeType=AppContext.getNoticeType();
		type=NoticeType.split(";");
		
		tv_type=(TextView) view.findViewById(R.id.tv_type);
		tv_type.setText(type[0]);
		tv_type.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showSelectFilter();
			}
		});

		et_AlarmDesc=(EditText) view.findViewById(R.id.et_AlarmDesc);
		et_AlarmDesc.setText(xj_ResultHis.getPlanDesc_TX()+"/"+xj_ResultHis.getResult_TX());
		
		AlarmDialog=new com.moons.xst.track.widget.AlertDialog(NewDianjian_Main.this)
				.builder()
				.setTitle(getString(R.string.plan_alarm_details))
				.setView(view)
				.setCustomButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								String AlarmDesc_TX=et_AlarmDesc.getText().toString();
								try {
									if(StringUtils.isEmpty(AlarmDesc_TX)){
										UIHelper.ToastMessage(NewDianjian_Main.this, getString(R.string.plan_alarm_details_input_hint));
										return;
									}
									if(AlarmDesc_TX.getBytes("GBK").length>40){
										UIHelper.ToastMessage(NewDianjian_Main.this, getString(R.string.plan_alarm_details_input_hint2));
									}else{
										djResult.setTransException_YN("9");
										djResult.setNoticeType_TX(tv_type.getText().toString());
										djResult.setAlarmDesc_TX(AlarmDesc_TX);
										
										xj_ResultHis.setTransException_YN("9");
										xj_ResultHis.setNoticeType_TX(tv_type.getText().toString());
										xj_ResultHis.setAlarmDesc_TX(AlarmDesc_TX);
										saveDJResult(djResult,xj_ResultHis);
										
										if(AlarmDialog!=null){
											AlarmDialog.dismiss();
										}
										if(sureDialog!=null){
											sureDialog.dismiss();
										}	
									}
								} catch (UnsupportedEncodingException e) {
									// TODO 自动生成的 catch 块
									UIHelper.ToastMessage(NewDianjian_Main.this, e.getMessage());
								}
							}
						})
				.setNegativeButton(
						getString(R.string.cancel),null).setCanceledOnTouchOutside(false).setCancelable(false);
		AlarmDialog.show();
	}
	com.moons.xst.track.widget.AlertDialog dialog;
	private void showSelectFilter(){
		LayoutInflater factory = LayoutInflater
				.from(this);
		View view = factory.inflate(R.layout.listview_layout, null);
		ListView listView = (ListView) view.findViewById(R.id.listView);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.notice_type_item,type);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				tv_type.setText(type[position]);
				if(dialog!=null){
					dialog.dismiss();
				}
			}
		});
		
		dialog = new com.moons.xst.track.widget.AlertDialog(
				NewDianjian_Main.this)
				.builder()
				.setTitle(getString(R.string.plan_notice_type_title))
				.setView(view).setButtonGone().setCanceledOnTouchOutside(false).setCancelable(false);
		dialog.show();
	}
	
	private void saveDJResult(final DJ_ResultActive djResult,final XJ_ResultHis xj_ResultHis){
		new Thread() {
			public void run() {
				Message message = new Message();
				try{
					long row = DJResultHelper.GetIntance().InsertDJPlanResult(
							NewDianjian_Main.this, djResult, xj_ResultHis,
							planFilesBuffer, currentDJPlan);
					if (row > 0) {
						timewave = null;
						if (AppContext.DJSpecCaseFlag == 0) {
							currentDJPlan.getDJPlan().setLastComplete_DT(
									djResult.getCompleteTime_DT());
							currentDJPlan.getDJPlan().setLastResult_TX(
									djResult.getResult_TX());
							DJPlanHelper.GetIntance().updateDJPlan(
									NewDianjian_Main.this, currentDJPlan.getDJPlan());
						} 
						DJ_IDPos _curridpos = getIDPosInfoByIDPosID(Integer
								.valueOf(current_IDPos_ID));

						
						// 如果本条是修改的，则不自动加载下一条
						if (currDJPlancompletedYN) {
							message.what = 2;
							saveHandler.sendMessage(message);
						} else {
							// 点检时，更新钮扣到位结束时间，修改时不更新
							DJIDPosHelper.GetIntance().updateDJTaskIDPos(
									NewDianjian_Main.this, _curridpos,
									current_DJLine_ID);
							message.what = 1;
							saveHandler.sendMessage(message);
						}
					} else {
						message.what = -1;
						saveHandler.sendMessage(message);
						return;
					}
				}catch(Exception e){
					message.what = -1;
					saveHandler.sendMessage(message);
					return;
				}
			}
		}.start();
	}

	/**
	 * 批量保存主控点下结果
	 */
	private void saveDJResultForLK(List<DJPlan> lkPlanS) {
		try {
			for (DJPlan _djPlan : lkPlanS) {
				if (!StringUtils.isEmpty(_djPlan.getDJPlan().getMustCheck_YN())
						&& _djPlan.getDJPlan().getMustCheck_YN().equals("1")) {
					continue;
				}
				// 主控批处理的计划结果耗时统一为2S
				int costTime = 2;
				String expYN = "0";
				String Result_TX = _djPlan.getDJPlan().getDefaultResult_TX();
				if (_djPlan.getDJPlan().getDataType_CD()
						.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {
					Result_TX = _djPlan.getDJPlan().getStandardValue_TX();
				} else if (_djPlan.getDJPlan().getDataType_CD()
						.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_GC)) {
					if (AppContext.dataCodeBuffer == null
							|| AppContext.dataCodeBuffer.size() <= 0) {
						Result_TX = getString(R.string.plan_gc_defaultresult);
					} else {
						Integer datagroupID = (_djPlan.getDJPlan()
								.getDataCodeGroup_ID() != null && _djPlan
								.getDJPlan().getDataCodeGroup_ID() >= 0) ? _djPlan
								.getDJPlan().getDataCodeGroup_ID() : 0;
						Z_DataCodeGroup _temCodeGroup = getDataCodeGroupByID(String
								.valueOf(datagroupID));
						if (_temCodeGroup != null)
							if (_temCodeGroup.getDataCodeGroupItems() != null) {
								Result_TX = _temCodeGroup
										.getDataCodeGroupItems().get(0)
										.getName_TX();
								expYN = _temCodeGroup.getDataCodeGroupItems()
										.get(0).getStatus_CD().equals("1") ? "1"
										: "0";
							} else {
								Result_TX = getString(R.string.plan_gc_defaultresult);
							}
						else {
							Result_TX = getString(R.string.plan_gc_defaultresult);
						}
					}

				}
				DJ_ResultActive djResult = new DJ_ResultActive();
				djResult.setAlarmLevel_ID(0);
				djResult.setAppUser_CD(AppContext.getUserCD());
				djResult.setAppUserName_TX(AppContext.getUserName());
				String completeTime = DateTimeHelper.getDateTimeNow();
				djResult.setCompleteTime_DT(completeTime);
				// _djPlan.getDJPlan().setLastComplete_DT(completeTime);
				djResult.setData8K_TX(null);
				djResult.setDataFlag_CD("A");
				djResult.setDataLen(0);
				djResult.setDJ_Plan_ID(_djPlan.getDJPlan().getDJ_Plan_ID());
				djResult.setException_YN(expYN);
				djResult.setFeatureValue_TX("");
				djResult.setFFTData_TX(null);
				djResult.setLastResult_TX(_djPlan.getDJPlan()
						.getLastResult_TX());
				djResult.setMemo_TX("");
				djResult.setMObjectStateName_TX("");
				djResult.setQuery_DT(DateTimeHelper.DateToString(_djPlan
						.GetQueryDate(DateTimeHelper.GetDateTimeNow())));
				djResult.setRate(0);
				djResult.setRatio1_NR(false);
				djResult.setRatio_NR(false);
				djResult.setResult_TX(Result_TX);
				// _djPlan.getDJPlan().setLastResult_TX(Result_TX);

				String shiftname = "";
				String shiftGroupName = "";
				// 巡点检计算班次，职别
				shiftname = XSTMethodByLineTypeHelper.getInstance()
						.getShiftName(NewDianjian_Main.this, _djPlan);
				shiftGroupName = XSTMethodByLineTypeHelper.getInstance()
						.getShiftGroupName(NewDianjian_Main.this, _djPlan);
				djResult.setShiftName_TX(shiftname);
				djResult.setShiftGroupName_TX(shiftGroupName);
				djResult.setSpecCase_TX(_djPlan.getSpecCaseNames());
				djResult.setSpecCase_YN(AppContext.DJSpecCaseFlag == 1 ? "1"
						: "0");
				djResult.setTime_NR(costTime);
				djResult.setTransInfo_TX(_djPlan.getDJPlan().getTransInfo_TX());
				djResult.setVibParam_TX("");

				XJ_ResultHis xj_ResultHis = new XJ_ResultHis();
				xj_ResultHis.setLine_ID(current_DJLine_ID);
				xj_ResultHis.setDJ_Plan_ID(djResult.getDJ_Plan_ID());
				xj_ResultHis.setCompleteTime_DT(djResult.getCompleteTime_DT());
				xj_ResultHis.setPlanDesc_TX(_djPlan.getDJPlan()
						.getPlanDesc_TX());
				xj_ResultHis.setIDPosName_TX(current_IDPos_Name);
				xj_ResultHis.setAppUser_CD(djResult.getAppUser_CD());
				xj_ResultHis.setAppUserName_TX(djResult.getAppUserName_TX());
				xj_ResultHis.setResult_TX(djResult.getResult_TX());
				xj_ResultHis.setData8K_TX(djResult.getData8K_TX());
				xj_ResultHis.setQuery_DT(djResult.getQuery_DT());
				xj_ResultHis.setMObjectStateName_TX(djResult
						.getMObjectStateName_TX());
				xj_ResultHis.setException_YN(djResult.getException_YN());
				xj_ResultHis.setAlarmLevel_ID(djResult.getAlarmLevel_ID());
				xj_ResultHis.setSpecCase_YN(djResult.getSpecCase_YN());
				xj_ResultHis.setSpecCase_TX(djResult.getSpecCase_TX());
				xj_ResultHis.setMemo_TX(djResult.getMemo_TX());
				xj_ResultHis.setTime_NR(djResult.getTime_NR());
				xj_ResultHis.setDataFlag_CD(djResult.getDataFlag_CD());
				xj_ResultHis.setShiftGroupName_TX(djResult
						.getShiftGroupName_TX());
				xj_ResultHis.setShiftName_TX(djResult.getShiftName_TX());
				xj_ResultHis.setRatio_NR(djResult.getRatio_NR());
				xj_ResultHis.setRatio1_NR("");
				xj_ResultHis.setRate(djResult.getRate());
				xj_ResultHis.setDataLen(djResult.getDataLen());
				xj_ResultHis.setLastResult_TX(djResult.getLastResult_TX());
				xj_ResultHis.setTransInfo_TX(djResult.getTransInfo_TX());
				xj_ResultHis.setVibParam_TX(djResult.getVibParam_TX());
				xj_ResultHis.setFeatureValue_TX(djResult.getFeatureValue_TX());
				xj_ResultHis.setGPSInfo_TX(strLocationInfo);
				xj_ResultHis.setDataType_CD(_djPlan.getDJPlan()
						.getDataType_CD());
				xj_ResultHis.setZhenDong_Type(_djPlan.getDJPlan()
						.getZhenDong_Type());
				xj_ResultHis.setFFTData_TX(djResult.getFFTData_TX());
				long row = DJResultHelper.GetIntance().InsertDJPlanResult(
						NewDianjian_Main.this, djResult, xj_ResultHis, null,
						null);
				if (row > 0) {
					_djPlan.getDJPlan().setLastComplete_DT(
							djResult.getCompleteTime_DT());
					_djPlan.getDJPlan().setLastResult_TX(
							djResult.getResult_TX());
					DJPlanHelper.GetIntance().updateDJPlan(
							NewDianjian_Main.this, _djPlan.getDJPlan());
				}

				/*
				 * _djPlan.getDJPlan().setLastComplete_DT(
				 * DateTimeHelper.getDateTimeNow());
				 */

			}
			DJ_IDPos _curridpos = getIDPosInfoByIDPosID(Integer
					.valueOf(current_IDPos_ID));
			if (_curridpos != null) {
				int completeNum = DJIDPosHelper
						.GetCompleteNum(current_IDPos_ID);
				String countString = "";
				countString = XSTMethodByLineTypeHelper.getInstance()
						.getCountStrForLK(NewDianjian_Main.this, completeNum,
								YXPlanTotalCount);

				/*
				 * if (AppContext.getCurrLineType() == AppConst.LineType.XDJ
				 * .getLineType()) { countString = AppContext.DJSpecCaseFlag ==
				 * 1 ? getString( R.string.plan_speccasecount,
				 * String.valueOf(YXPlanTotalCount)) : completeNum + "/" +
				 * YXPlanTotalCount; } else if (AppContext.getCurrLineType() ==
				 * AppConst.LineType.CaseXJ .getLineType()) { countString =
				 * getString(R.string.plan_casexjcount, YXPlanTotalCount); }
				 */
				_curridpos.setPlanCount(countString);
			}
		} catch (Exception ex) {
			UIHelper.ToastMessage(getApplication(), ex.getMessage());
		}
	}

	private void Goback() {
		if (ttv.isRun()) {
			UIHelper.ToastMessage(NewDianjian_Main.this,
					getString(R.string.plan_mincoast_notice3));
			return;
		}

		int cycTotalCount = YXPlanTotalCount;
		int completeNum = 0;
		completeNum = XSTMethodByLineTypeHelper.getInstance().getCompleteNum(
				NewDianjian_Main.this, current_IDPos_ID);
		int srNotNeedDoNum = DJIDPosHelper.GetSKNotNeedDoNum(current_IDPos_ID);
		final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
				- completeNum - srNotNeedDoNum)
				: 0;
		String noticeString = getString(R.string.plan_statistic_notice1,
				current_IDPos_Name, String.valueOf(cycTotalCount),
				String.valueOf(notcompleteNum), String.valueOf(srNotNeedDoNum));

		countString = XSTMethodByLineTypeHelper.getInstance().getCountStr(
				NewDianjian_Main.this, completeNum, srNotNeedDoNum,
				YXPlanTotalCount);

		noticeString = XSTMethodByLineTypeHelper.getInstance().getNoticeStr(
				NewDianjian_Main.this, noticeString, cycTotalCount);

		final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
				.valueOf(current_IDPos_ID));
		AppContext.voiceConvertService.Speaking(noticeString);

		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
		changeDialogFontSize(dialogFontSize);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.plan_backdoplan))
				.setView(view)
				.setMsg(noticeString)
				.setCancelable(false)
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {

								if (notcompleteNum > 0
										&& AppContext.DJSpecCaseFlag == 0) {
									LayoutInflater factory = LayoutInflater.from(NewDianjian_Main.this);
									final View view = factory.inflate(R.layout.textview_layout,
											null);
									TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
									changeDialogFontSize(dialogFontSize);
									dialogFontSize.setTextColor(Color.RED);
									new com.moons.xst.track.widget.AlertDialog(NewDianjian_Main.this)
											.builder()
											.setTitle(getString(R.string.tips))
											.setView(view)
											.setMsg(getString(R.string.plan_remain_msg, notcompleteNum))
											.setPositiveButton(getString(R.string.sure),
													new OnClickListener() {
														@Override
														public void onClick(View v) {
															_myidpos.setPlanCount(countString);
															backClose();
															AppManager.getAppManager().finishActivity(
																	NewDianjian_Main.this);
														}
													})
											.setNegativeButton(getString(R.string.cancel),
													new OnClickListener() {
														@Override
														public void onClick(View v) {
															LoadFirstDJPLanWithOutCompleted();
														}
													}).show();
								} else {
									if (notcompleteNum ==0) {
										if (_myidpos != null) {
											_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
										}
									}
									_myidpos.setPlanCount(countString);
	
									backClose();
									AppManager.getAppManager().finishActivity(
											NewDianjian_Main.this);
								}
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
							}
						}).setCanceledOnTouchOutside(false).show();

	}

	// 退出时的关闭操作（关闭广播接收器，handler等）
	private void backClose() {
		handler.removeCallbacks(runnable);
		if (AppContext.getCurIDPos() != null) {
			List<DJ_ControlPoint> idPosSRBuffer = new ArrayList<DJ_ControlPoint>();
			idPosSRBuffer = DJPlanHelper.GetIntance().getSRbyidPosID(
					NewDianjian_Main.this,
					AppContext.getCurIDPos().getIDPos_ID());
		}
		AppContext.setCurIDPos(null);
		unregisterBoradcastReceiver();
	}

	/**
	 * 人员与GPS信息切换
	 */
	public void showNext() {
		viewAnimator.setOutAnimation(this, R.anim.slide_out_up);
		viewAnimator.setInAnimation(this, R.anim.slide_in_down);
		viewAnimator.showNext();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			//mHandler2.removeCallbacks(mRunnable);
			Goback();
			// unregisterBoradcastReceiver();
			return true;
		case KeyEvent.KEYCODE_9:
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void saverevitionImageSize(final String path, int size)
			throws IOException {
		final BitmapFactory.Options opts = new Options();
		//缩放的比例
		opts.inSampleSize = 10;
		//内存不足时可被回收
		opts.inPurgeable = true;
		
		try{
			Bitmap btp=BitmapFactory.decodeFile(path,opts);
			btp.recycle();
		}catch(Exception e){
			e.toString();
		}
		if (AppContext.getRotateCameraYN()) {//旋转图片
			savePhoto = new LoadingDialog(this);
			savePhoto.setLoadConceal();
			savePhoto.setCancelable(false);
			savePhoto.show();
			try {
				Bitmap bitmap=null;
				while(bitmap==null){
					try{
						bitmap = BitmapFactory.decodeFile(path,opts);
					}catch(Exception e){
						e.toString();
					}
				}
				FileOutputStream out = new FileOutputStream(path);
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				Bitmap returnbm = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				returnbm.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.close();
				bitmap.recycle();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}		
		}
		/*
		// 取得图片
		File file = new File(path);
		FileInputStream temp = new FileInputStream(file);
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
		BitmapFactory.decodeStream(temp, null, options);
		// 关闭流
		temp.close();
		// 生成压缩的图片
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= size)
					&& (options.outHeight >> i <= size)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				temp = new FileInputStream(file);
				// 这个参数表示 新生成的图片为原始图片的几分之一。
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;

				bitmap = BitmapFactory.decodeStream(temp, null, options);
				break;
			}
			i += 1;
		}
		temp.close();

		FileOutputStream out = new FileOutputStream(path);
		if (bitmap != null) {
			if (AppContext.getRotateCameraYN()) {
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				Bitmap returnbm = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);

				returnbm.compress(Bitmap.CompressFormat.PNG, 90, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			}
		}
		out.close();
		// FileOutputStream out = new FileOutputStream(path);
		// bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		// bitmap.recycle();
*/	}
	

	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			// System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	public String UriToPath(Uri uri) {
		String path = null;
		ContentResolver cr = this.getContentResolver();
		Cursor cursor = cr.query(uri, null, null, null, null);
		cursor.moveToFirst();
		if (cursor != null) {
			path = cursor.getString(1);
			cursor.close();
		}
		return path;
	}

	// 获取所有图片路径
	private String[] getImagePath() {
		String[] datas = null;
		List<DJ_PhotoByResult> imageList = new ArrayList<DJ_PhotoByResult>();
		imageList = planFilesBuffer.get(REQCODE_PICTURE);
		if (imageList != null) {
			datas = new String[imageList.size()];
			for (int i = 0; i < imageList.size(); i++) {
				if (imageList.get(i).getLCType().equals("ZP")) {
					datas[i] = imageList.get(i).getFilePath();
				}
			}
		}
		return datas;
	}

	List<DJ_PhotoByResult> list = new ArrayList<DJ_PhotoByResult>();

	private List<DJ_PhotoByResult> getList() {
		// new一个list用来装载录音和图片
		list.clear();
		List<DJ_PhotoByResult> imageList = planFilesBuffer.get(REQCODE_PICTURE);
		// 先装载图片
		if (imageList != null) {
			for (int i = 0; i < imageList.size(); i++) {
				if (imageList.get(i).getLCType().equals("ZP")) {
					list.add(imageList.get(i));
				}
			}
		}
		imageList = planFilesBuffer.get(REQCODE_SOUND);
		// 再装载录音
		if (imageList != null) {
			for (int i = 0; i < imageList.size(); i++) {
				if (imageList.get(i).getLCType().equals("LY")) {
					list.add(imageList.get(i));
				}
			}
		}
		return list;
	}

	private boolean checkMaxFileNum(int type) {
		boolean result = true;
		if (planFilesBuffer != null && planFilesBuffer.size() > 0) {
			switch (type) {
			case REQCODE_PICTURE:
				if (planFilesBuffer.containsKey(type)
						&& planFilesBuffer.get(type).size() >= 10) {
					UIHelper.ToastMessage(NewDianjian_Main.this,
							R.string.plan_maxphotocount);
					result = false;
				}
				break;
			case REQCODE_SOUND:
				if (planFilesBuffer.containsKey(type)
						&& planFilesBuffer.get(type).size() >= 5) {
					UIHelper.ToastMessage(NewDianjian_Main.this,
							R.string.plan_maxaudiocount);
					result = false;
				}
				break;
			default:
				result = false;
				break;
			}
		}

		return result;
	}

	private void saveAndRefreshPhoto(){
		DJ_PhotoByResult _temPhotoByResult = new DJ_PhotoByResult();
		_temPhotoByResult.setGUID(uuid.toString());
		_temPhotoByResult.setDJ_Plan_ID(currentDJPlan.getDJPlan()
				.getDJ_Plan_ID());
		_temPhotoByResult.setLCType("ZP");
		_temPhotoByResult.setPartition_ID("");
		_temPhotoByResult.setPhoto_DT(photoTimeString);
		_temPhotoByResult.setFilePath(imagePath);
		_temPhotoByResult.setFileTitle(uuid.toString());
		if (planFilesBuffer == null)
			planFilesBuffer = new Hashtable<Integer, ArrayList<DJ_PhotoByResult>>();
		if (!planFilesBuffer.containsKey(REQCODE_PICTURE)) {
			planFilesBuffer.put(REQCODE_PICTURE,
					new ArrayList<DJ_PhotoByResult>());
			planFilesBuffer.get(REQCODE_PICTURE).add(_temPhotoByResult);
		} else {
			planFilesBuffer.get(REQCODE_PICTURE).add(_temPhotoByResult);
		}

		NewDJAdapter imageAdapter = new NewDJAdapter(this, getList(),
				R.layout.listitem_djaccessory);
		gridview.setAdapter(imageAdapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 拍照返回
		if (requestCode == REQCODE_PICTURE) {
			if (resultCode == Activity.RESULT_OK) {
				try {
					saverevitionImageSize(imagePath, 800);
					saveAndRefreshPhoto();
					if(savePhoto!=null){
						savePhoto.dismiss();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				File file = new File(imagePath);
				file.delete();
			}
		}
		// 录音返回
		if (requestCode == REQCODE_SOUND) {
			if (resultCode == Activity.RESULT_OK) {
				DJ_PhotoByResult _temSoundByResult = new DJ_PhotoByResult();
				_temSoundByResult.setGUID(uuid.toString());
				_temSoundByResult.setDJ_Plan_ID(currentDJPlan.getDJPlan()
						.getDJ_Plan_ID());
				_temSoundByResult.setLCType("LY");
				_temSoundByResult.setPartition_ID("");
				_temSoundByResult.setPhoto_DT(DateTimeHelper.getDateTimeNow());
				_temSoundByResult.setFilePath(audioPath);
				_temSoundByResult.setFileTitle(uuid.toString());
				if (planFilesBuffer == null)
					planFilesBuffer = new Hashtable<Integer, ArrayList<DJ_PhotoByResult>>();
				if (!planFilesBuffer.containsKey(REQCODE_SOUND)) {
					planFilesBuffer.put(REQCODE_SOUND,
							new ArrayList<DJ_PhotoByResult>());
					planFilesBuffer.get(REQCODE_SOUND).add(_temSoundByResult);
				} else {
					planFilesBuffer.get(REQCODE_SOUND).add(_temSoundByResult);
				}
				// 获取录音文件
				Uri uri = data.getData();
				String path = UriToPath(uri);
				copyFile(path, audioPath);
				File file = new File(path);
				file.delete();

				NewDJAdapter lyadapter = new NewDJAdapter(this, getList(),
						R.layout.listitem_djaccessory);
				gridview.setAdapter(lyadapter);
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				File file = new File(audioPath);
				file.delete();
			}
		}
		// 测温返回
		if (requestCode == REQCODE_TEMPERATURE) {
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Bundle bundle = data.getExtras();
					String res = bundle.getString("rsts");

					txt_plan_result_others.setText(res);
					checkResult(Double.parseDouble(res));
				}
			}
		}
		// 测振返回
		if (requestCode == REQCODE_VIBRATION) {
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Bundle bundle = data.getExtras();
					String res = bundle.getString("rsts");
					timewave = StringUtils.unGZip(bundle.getByteArray("sydata"));
					fftdata = bundle.getByteArray("pydata");
					datalen = bundle.getInt("datalen");
					featurevalue = bundle.getString("featurevalue");
					rate = bundle.getInt("rate");
					txt_plan_result_others.setText(res);
					checkResult(Double.parseDouble(res));
				}
			}
		}
		// 测转速返回
		if (requestCode == REQCODE_SPEED) {
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Bundle bundle = data.getExtras();
					String res = bundle.getString("rsts");

					txt_plan_result_others.setText(res);
					checkResult(Double.parseDouble(res));
				}
			}
		}
		// 浏览图片返回
		if (requestCode == BROWSE_IMAGE) {
			//巡检浏览里面不能删除照片，回来从新加载会慢，所以浏览回来不做处理
			/*List<String> path = AppContext.DJImagePath;
			for (int i = 0; i < planFilesBuffer.get(REQCODE_PICTURE).size(); i++) {
				boolean isDelete = true;
				// 判断返回的图片路径，buffer中存在的返回路径就保留，不存在的就删除
				for (int j = 0; j < path.size(); j++) {
					if (planFilesBuffer.get(REQCODE_PICTURE).get(i)
							.getFilePath().equals(path.get(j))) {
						isDelete = false;
						break;
					}
				}
				if (isDelete) {
					// 删除一条附件关联信息
					DJResultHelper.GetIntance().deleteOneFile(getApplication(),
							planFilesBuffer.get(REQCODE_PICTURE).get(i));
					// 删除Buffer用来刷新listview
					planFilesBuffer.get(REQCODE_PICTURE).remove(
							planFilesBuffer.get(REQCODE_PICTURE).get(i));
					i--;
				}
			}
			// 刷新listview
			reflashFilesList();*/
		}
	}

	/**
	 * OK键点击确认
	 * 
	 * @param event
	 * @return
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_UP) {
			if (isClickableOK) {
				saveData();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		// 注册广播
		myIntentFilter.addAction("com.xst.track.service.updataCurrentLoction");
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unregisterBoradcastReceiver() {
		try {
			if (mBroadcastReceiver != null)
				this.unregisterReceiver(mBroadcastReceiver);
		} catch (IllegalArgumentException e) {
		}
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.xst.track.service.updataCurrentLoction")) {
				updateGPSInfo(AppContext.getCurrLocation());
			}
		}
	};
	

	private void updateGPSInfo(BDLocation location) {
		try {

			if (location != null) {
				int locType = location.getLocType();
				if (locType == BDLocation.TypeGpsLocation) {
					strLocationInfo = getString(R.string.plan_gpsinfo,
							String.valueOf(location.getLongitude()),
							String.valueOf(location.getLatitude()));
					Drawable drawable = getResources().getDrawable(
							R.drawable.icon_from_gps);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					showAnimatorGPSInfo(drawable, strLocationInfo);
				} else if (locType == BDLocation.TypeNetWorkLocation) {
					strLocationInfo = getString(R.string.plan_gpsinfo,
							String.valueOf(location.getLongitude()),
							String.valueOf(location.getLatitude()));

					Drawable drawable = getResources().getDrawable(
							R.drawable.icon_from_network);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					showAnimatorGPSInfo(drawable, strLocationInfo);
				} else {
					showAnimatorGPSInfo(null,
							getString(R.string.plan_positioning));
				}
			} else {
				showAnimatorGPSInfo(null, getString(R.string.plan_positioning));
			}
		} catch (Exception e) {
			showAnimatorGPSInfo(null, getString(R.string.plan_positioning));
		}
	}

	private void showAnimatorGPSInfo(Drawable drawable, String gpsInfo) {
		if (AppContext.getCurrLineType() != AppConst.LineType.CaseXJ
				.getLineType()) {
			dianjian_main_gpsinfo.setCompoundDrawables(drawable, null, null,
					null);
			dianjian_main_gpsinfo.setText(gpsInfo);
		}
	}

	private String caculateCurPlan_XDJ(boolean nextFlag, boolean IsJudgeComplete) {
		String result = "true";
		String emsg = "";

		emsg = mDJPlan.JudgePlanTemp(NewDianjian_Main.this,
				DateTimeHelper.GetDateTimeNow());
		if (emsg.length() > 0) {
			// 周期过期处理
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
			changeDialogFontSize(dialogFontSize);
			new com.moons.xst.track.widget.AlertDialog(this)
					.builder()
					.setTitle(getString(R.string.system_notice))
					.setView(view)
					.setMsg(emsg)
					.setCancelable(false)
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {

									backClose();
									AppManager.getAppManager().finishActivity(
											NewDianjian_Main.this);

								}
							}).setCanceledOnTouchOutside(false).show();

			return result = "false";
			// return LoadNextOrPreDJPlan(nextFlag, IsJudgeComplete);
		}

		// 计划已完成跳过或者计划无需做跳过
		if (IsJudgeComplete) {
			if (mDJPlan.JudgePlanIsCompleted(DateTimeHelper.GetDateTimeNow())) {
				return result = "false|" + String.valueOf(nextFlag) + "|"
						+ String.valueOf(IsJudgeComplete);
			}
			if (mDJPlan.JudgePlanIsSrControl()) {
				// 该周期下已经设置过启停点，并且启停点下计划无需做，则跳过
				if (mDJPlan.SrIsSetting() && (!mDJPlan.JudgePlanBySrPoint())) {
					return result = "false|" + String.valueOf(nextFlag) + "|"
							+ String.valueOf(IsJudgeComplete);
				}
			}

		}
		if (mDJPlan.JudgePlanIsSrControl()) {
			// 当前周期没有设置过启停点
			if (!mDJPlan.SrIsSetting()) {
				showSR(true, no);
				currentDJPlan = mDJPlan;
				no++;
				return result = "true";
			}
			if (!mDJPlan.JudgePlanBySrPoint()) {
				return result = "false|" + String.valueOf(nextFlag) + "|false";
			}
			if (mDJPlan.JudgePlanIsLKControl()
					&& (!mDJPlan.getLKDoYn())
					&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
							.getMustCheck_YN()))
					&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
				List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
						.GetPlanListByControlPoint(NewDianjian_Main.this,
								mDJPlan.getDJPlan().getLKPoint_ID(),
								current_IDPos_ID);
				ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
				if (lkPlanList != null && lkPlanList.size() > 0) {
					for (DJPlan _planInfo : lkPlanList) {
						yxplanList.add(_planInfo);
					}
				}
				if (yxplanList != null && yxplanList.size() > 0)
					selectLK(mDJPlan.getDJPlan().getLKPoint_ID(), yxplanList);
			}
			currentDJPlan = mDJPlan;
		} else {
			if (mDJPlan.JudgePlanIsLKControl()
					&& (!mDJPlan.getLKDoYn())
					&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
							.getMustCheck_YN()))
					&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
				List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
						.GetPlanListByControlPoint(NewDianjian_Main.this,
								mDJPlan.getDJPlan().getLKPoint_ID(),
								current_IDPos_ID);

				ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
				if (lkPlanList != null && lkPlanList.size() > 0) {
					for (DJPlan _planInfo : lkPlanList) {
						yxplanList.add(_planInfo);
					}
				}
				if (yxplanList != null && yxplanList.size() > 0)
					selectLK(mDJPlan.getDJPlan().getLKPoint_ID(), yxplanList);
			}
			currentDJPlan = mDJPlan;
		}
		return result;
	}

	private String caculateCurPlan_DJPC(boolean nextFlag,
			boolean IsJudgeComplete) {
		String result = "true";
		String emsg = "";

		emsg = mDJPlan.JudgePlanTemp(NewDianjian_Main.this,
				DateTimeHelper.GetDateTimeNow());
		if (emsg.length() > 0) {
			// 周期过期处理
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			TextView dialogFontSize = (TextView) view.findViewById(R.id.text);
			changeDialogFontSize(dialogFontSize);
			new com.moons.xst.track.widget.AlertDialog(this)
					.builder()
					.setTitle(getString(R.string.system_notice))
					.setView(view)
					.setMsg(emsg)
					.setCancelable(false)
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {

									backClose();
									AppManager.getAppManager().finishActivity(
											NewDianjian_Main.this);

								}
							}).setCanceledOnTouchOutside(false).show();

			return result = "false";
			// return LoadNextOrPreDJPlan(nextFlag, IsJudgeComplete);
		}

		// 计划已完成跳过或者计划无需做跳过
		if (IsJudgeComplete) {
			if (mDJPlan.JudgePlanIsCompletedForDJPC()) {
				return result = "false|" + String.valueOf(nextFlag) + "|"
						+ String.valueOf(IsJudgeComplete);
			}
			if (mDJPlan.JudgePlanIsSrControl()) {
				// 该周期下已经设置过启停点，并且启停点下计划无需做，则跳过
				if (mDJPlan.SrIsSetting() && (!mDJPlan.JudgePlanBySrPoint())) {
					return result = "false|" + String.valueOf(nextFlag) + "|"
							+ String.valueOf(IsJudgeComplete);
				}
			}

		}
		if (mDJPlan.JudgePlanIsSrControl()) {
			// 当前周期没有设置过启停点
			if (!mDJPlan.SrIsSetting()) {
				showSR(true, no);
				currentDJPlan = mDJPlan;
				no++;
				return result = "true";
			}
			if (!mDJPlan.JudgePlanBySrPoint()) {
				return result = "false|" + String.valueOf(nextFlag) + "|false";
			}
			if (mDJPlan.JudgePlanIsLKControl()
					&& (!mDJPlan.getLKDoYn())
					&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
							.getMustCheck_YN()))
					&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
				List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
						.GetPlanListByControlPoint(NewDianjian_Main.this,
								mDJPlan.getDJPlan().getLKPoint_ID(),
								current_IDPos_ID);
				ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
				if (lkPlanList != null && lkPlanList.size() > 0) {
					for (DJPlan _planInfo : lkPlanList) {
						yxplanList.add(_planInfo);
					}
				}
				if (yxplanList != null && yxplanList.size() > 0)
					selectLK(mDJPlan.getDJPlan().getLKPoint_ID(), yxplanList);
			}
			currentDJPlan = mDJPlan;
		} else {
			if (mDJPlan.JudgePlanIsLKControl()
					&& (!mDJPlan.getLKDoYn())
					&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
							.getMustCheck_YN()))
					&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
				List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
						.GetPlanListByControlPoint(NewDianjian_Main.this,
								mDJPlan.getDJPlan().getLKPoint_ID(),
								current_IDPos_ID);

				ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
				if (lkPlanList != null && lkPlanList.size() > 0) {
					for (DJPlan _planInfo : lkPlanList) {
						yxplanList.add(_planInfo);
					}
				}
				if (yxplanList != null && yxplanList.size() > 0)
					selectLK(mDJPlan.getDJPlan().getLKPoint_ID(), yxplanList);
			}
			currentDJPlan = mDJPlan;
		}
		return result;
	}

	private String caculateCurPlan_CaseXJ(boolean nextFlag,
			boolean IsJudgeComplete) {
		String result = "true";
		if (IsJudgeComplete) {
			if (mDJPlan.JudgePlanIsCompletedForCASEXJ(NewDianjian_Main.this)) {
				return result = "false|" + String.valueOf(nextFlag) + "|"
						+ String.valueOf(IsJudgeComplete);
			}
			if (mDJPlan.JudgePlanIsSrControl()) {
				// 已经设置过启停点，并且启停点下计划无需做，则跳过
				if (mDJPlan.SrIsSetting() && (!mDJPlan.JudgePlanBySrPoint())) {
					return result = "false|" + String.valueOf(nextFlag) + "|"
							+ String.valueOf(IsJudgeComplete);
				}
			}
		}
		if (mDJPlan.JudgePlanIsSrControl()) {
			// 没有设置过启停点
			if (!mDJPlan.SrIsSetting()) {
				showSR(true, no);
				currentDJPlan = mDJPlan;
				no++;
				return result = "true";
			}
			if (!mDJPlan.JudgePlanBySrPoint()) {
				return result = "false|" + String.valueOf(nextFlag) + "|false";
			}
			if (mDJPlan.JudgePlanIsLKControl()
					&& (!mDJPlan.getLKDoYn())
					&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
							.getMustCheck_YN()))
					&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
				List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
						.GetPlanListByControlPoint(NewDianjian_Main.this,
								mDJPlan.getDJPlan().getLKPoint_ID(),
								current_IDPos_ID);
				ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
				if (lkPlanList != null && lkPlanList.size() > 0) {
					for (DJPlan _planInfo : lkPlanList) {
						yxplanList.add(_planInfo);
					}
				}
				if (yxplanList != null && yxplanList.size() > 0)
					selectLK(mDJPlan.getDJPlan().getLKPoint_ID(), yxplanList);
			}
			currentDJPlan = mDJPlan;
		} else {
			if (mDJPlan.JudgePlanIsLKControl()
					&& (!mDJPlan.getLKDoYn())
					&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
							.getMustCheck_YN()))
					&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
				List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
						.GetPlanListByControlPoint(NewDianjian_Main.this,
								mDJPlan.getDJPlan().getLKPoint_ID(),
								current_IDPos_ID);

				ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
				if (lkPlanList != null && lkPlanList.size() > 0) {
					for (DJPlan _planInfo : lkPlanList) {
						yxplanList.add(_planInfo);
					}
				}
				if (yxplanList != null && yxplanList.size() > 0)
					selectLK(mDJPlan.getDJPlan().getLKPoint_ID(), yxplanList);
			}
			currentDJPlan = mDJPlan;
		}
		return result;
	}
}
