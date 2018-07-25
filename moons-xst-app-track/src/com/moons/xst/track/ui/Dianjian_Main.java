package com.moons.xst.track.ui;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.baidu.location.BDLocation;
import com.moons.xst.buss.DJIDPosHelper;
import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.buss.DJResultHelper;
import com.moons.xst.buss.DJShiftHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJPlanAudioAdapter;
import com.moons.xst.track.adapter.DJPlanImageAdapter;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.bean.Z_DataCodeGroup;
import com.moons.xst.track.bean.Z_DataCodeGroupItem;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.InputTools;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UnitHelper;
import com.moons.xst.track.widget.GestureListener;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.SimpleMultiListViewDialog;
import com.moons.xst.track.widget.SimpleTextDialog;
import com.moons.xst.track.xstinterface.LKListener;
import com.moons.xst.track.xstinterface.PriorityListener;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;

public class Dianjian_Main extends BaseActivity {

	/*public static Dianjian_Main instance = null;
	private AppContext appContext;// 全局Context
	
	private final long TIME_INTERVAL = 4000L;  

	// [start] 自定义变量
	public static int CURRENT_MODE = 0; // 当前的模式，默认可修改模式
	public static final int PopMenu_EDIT = 0;// 可修改模式
	public static final int PopMenu_CHECK = 1;// 检查模式(不可修改)

	// 图片/录音长按菜单三项操作
	private static final int MENUACTION_DELETE = 1;// 删除
	private static final int MENUACTION_RETURN = 2;// 返回

	public static final int REQCODE_PICTURE = 0;// 拍照
	public static final int REQCODE_SOUND = 1;// 录音
	public static final int REQCODE_VIDIO = 2;// 录像

	public static final int REQCODE_TEMPERATURE = 100;// 测温
	public static final int REQCODE_VIBRATION = 101;// 测振
	public static final int REQCODE_SPEED = 102;// 测转速

	private int currIndex = 0;// 当前页卡编号

	private byte[] timewave = null, fftdata = null;
	private int datalen = 0x4000;
	private String featurevalue = "";

	private float zoomMaxValue = 42;
	private float zoomMinValue = 16;
	private String current_DJLine_ID = "";
	private String current_IDPos_ID = "";
	private String current_IDPos_Name = "";
	private String current_cycid = "";

	// 计划
	private int currentPlanIndex;
	*//**
	 * 当前计算出的有效计划对象
	 *//*
	private DJPlan currentDJPlan = new DJPlan();
	*//**
	 * 当前游标下的计划对象
	 *//*
	private DJPlan mDJPlan = new DJPlan();
	private Hashtable<Integer, ArrayList<DJ_PhotoByResult>> planFilesBuffer = new Hashtable<Integer, ArrayList<DJ_PhotoByResult>>();
	private DJ_ResultActive currDJResultActive = new DJ_ResultActive();
	private boolean currDJPlancompletedYN = false;
	private Date startTime;

	// 钮扣下有效计划条数
	private int YXPlanTotalCount = 0;
	// 计划统计
	private String countString = "";
	// [end]

	// [start] 控件
	private ImageButton fbMore; // 模式选择图标
	private QuickActionWidget mGrid;// 快捷栏控件

	private ViewPager mTabPager;
	private View viewResult, viewMemo, viewPicture, viewAudio;
	private TextView txtTab1, txtTab2, txtTab3, txtTab4;
	private LinearLayout ll_Tab1, ll_Tab2, ll_Tab3, ll_Tab4;

	private TextView head_title;
	private ImageButton returnButton, gotoUnDoButton, plandetailButton;
	private ImageView resHisImageView; // 历史结果

	private TextView plan_result_almlevel, plan_result_jl_notice; // 报警等级及记录类超量程提示
	private TextView plan_description, zoomlargeTV, zoomsmallTV;

	private TextView txt_plan_result_others; // 除记录类外其他数据类型的结果框
	private EditText edit_plan_result_jl; // 记录类结果框

	private Button btn_goto_next, btn_goto_pre;
	
	private ViewAnimator viewAnimator;  
	private TextView dianjian_main_gpsinfo;

	// 数据栏变量
	private Button btn_savedata, btn_start_others;
	TextView txt_plandata_lastresult, txt_plandata_lastdate,
			txt_plandata_lasttime;
	private ImageView img_plandatatype;
	// 备注栏变量
	private EditText planmemo_editor;
	private TextView planmemo_editor_title;
	// 拍照栏变量
	private ImageButton btn_photo;
	private Gallery plan_picture_gallery;
	private TextView picNumTextView;
	private String picname;
	private String imagePath;
	private UUID uuid;
	private String photoTimeString;

	// 录音栏变量
	private ImageButton btn_record;
	private Gallery plan_audio_gallery;
	private TextView soundNumTextView;
	private String audioname;
	private String audioPath;

	// 主控、启停
	private ImageButton lkImageButton, srImageButton;

	private LoadingDialog loading, saving;
	private Handler mHandler;
	
	Handler handler = new Handler(){  
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
        	showNext();  
            handler.sendMessageDelayed(new Message(),TIME_INTERVAL);  
        }  
    };

	// [end]

	// [start] 自定义函数
	*//**
	 * 初始化头部视图
	 *//*
	private void initHeadViewAndMoreBar() {
		head_title = (TextView) findViewById(R.id.dianjian_head_title);
		head_title.setText("ID位置：" + current_IDPos_Name);
		viewAnimator = (ViewAnimator) this.findViewById(R.id.animator);  
		TextView dianjian_main_username = (TextView) findViewById(R.id.dianjian_main_username);
		dianjian_main_username.setText(AppContext.getUserName());
		dianjian_main_gpsinfo = (TextView) findViewById(R.id.dianjian_main_gpsinfo);
		fbMore = (ImageButton) findViewById(R.id.dianjian_head_morebutton);
		fbMore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mGrid.show(v);
			}
		});
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Goback();
				unregisterBoradcastReceiver();
			}
		});
		gotoUnDoButton = (ImageButton) findViewById(R.id.btn_goto_firstundo);
		gotoUnDoButton.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				LoadFirstDJPLanWithOutCompleted();
				return true;
			}
		});
		gotoUnDoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				UIHelper.ToastMessage(getApplication(), "长按此按钮快速到达第一条未做计划");
			}
		});
		zoomlargeTV = (TextView) findViewById(R.id.map_zoom_large);
		zoomlargeTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				float dpSize = UnitHelper.px2dip(Dianjian_Main.this,
						plan_description.getTextSize());
				float currZoomValue = dpSize + 1 > zoomMaxValue ? zoomMaxValue
						: dpSize + 1;
				if (dpSize > zoomMaxValue) {
					UIHelper.ToastMessage(getApplication(),
							R.string.djplan_zoomMaxMes);
					return;
				}
				plan_description.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
						currZoomValue);
			}
		});
		zoomsmallTV = (TextView) findViewById(R.id.map_zoom_small);
		zoomsmallTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				float dpSize = UnitHelper.px2dip(Dianjian_Main.this,
						plan_description.getTextSize());
				float currZoomValue = dpSize - 1 < zoomMinValue ? zoomMinValue
						: dpSize - 1;
				if (dpSize - 1 < zoomMinValue) {
					UIHelper.ToastMessage(getApplication(),
							R.string.djplan_zoomMinMes);
					return;
				}
				plan_description.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
						currZoomValue);
			}
		});
		lkImageButton = (ImageButton) findViewById(R.id.btn_dj_lk);
		lkImageButton.setVisibility(View.GONE);
		lkImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
			}
		});
		srImageButton = (ImageButton) findViewById(R.id.btn_dj_sr);
		srImageButton.setVisibility(View.GONE);
		srImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showSR(false, 0);
			}
		});
		plandetailButton = (ImageButton) findViewById(R.id.btn_plan_detail_show);
		plandetailButton.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				return true;
			}
		});
		plandetailButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showPlanDetail();
			}
		});

		btn_goto_next = (Button) findViewById(R.id.btn_goto_next);
		btn_goto_pre = (Button) findViewById(R.id.btn_goto_pre);
	}

	*//**
	 * 初始化ViewPager
	 *//*
	private void initViewPager() {
		mTabPager = (ViewPager) findViewById(R.id.DianjianTabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

		txtTab1 = (TextView) findViewById(R.id.dianjian_tab_result);
		txtTab2 = (TextView) findViewById(R.id.dianjian_tab_memo);
		txtTab3 = (TextView) findViewById(R.id.dianjian_tab_picture);
		txtTab4 = (TextView) findViewById(R.id.dianjian_tab_audio);

		txtTab1.setOnClickListener(new MyOnClickListener(0));
		txtTab2.setOnClickListener(new MyOnClickListener(1));
		txtTab3.setOnClickListener(new MyOnClickListener(2));
		txtTab4.setOnClickListener(new MyOnClickListener(3));

		ll_Tab1 = (LinearLayout) findViewById(R.id.ll_dianjian_tab_result);
		ll_Tab2 = (LinearLayout) findViewById(R.id.ll_dianjian_tab_memo);
		ll_Tab3 = (LinearLayout) findViewById(R.id.ll_dianjian_tab_picture);
		ll_Tab4 = (LinearLayout) findViewById(R.id.ll_dianjian_tab_audio);

		ll_Tab1.setOnClickListener(new MyOnClickListener(0));
		ll_Tab2.setOnClickListener(new MyOnClickListener(1));
		ll_Tab3.setOnClickListener(new MyOnClickListener(2));
		ll_Tab4.setOnClickListener(new MyOnClickListener(3));

		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();

		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		viewResult = mLi.inflate(R.layout.dianjian_tab_result, null);
	
		viewMemo = mLi.inflate(R.layout.dianjian_tab_memo, null);
		viewPicture = mLi.inflate(R.layout.dianjian_tab_picture, null);
		viewAudio = mLi.inflate(R.layout.dianjian_tab_audio, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(viewResult);
		views.add(viewMemo);
		views.add(viewPicture);
		views.add(viewAudio);
		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		mTabPager.setCurrentItem(0);//
	}

	*//**
	 * 初始化各栏目数据项
	 *//*
	private void initAllPagerData() {

		// 报警等级
		plan_result_almlevel = (TextView) (viewResult)
				.findViewById(R.id.plan_input_mes_almlevel);
		// 记录类超量程提示
		plan_result_jl_notice = (TextView) (viewResult)
				.findViewById(R.id.plan_input_mes_desc);
		// 数据类型
		img_plandatatype = (ImageView) (viewResult)
				.findViewById(R.id.img_plandatatype);
		// 结果框
		txt_plan_result_others = (TextView) (viewResult)
				.findViewById(R.id.plan_result_others);
		// 计划描述
		plan_description = (TextView) findViewById(R.id.plan_description);
		plan_description.setOnTouchListener(new TextZoomListenter(16, 36));
		btn_savedata = (Button) findViewById(R.id.btn_saveResult_data);
		btn_savedata.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveData();
			}

		});
		// 备注栏
		planmemo_editor_title = (TextView) viewMemo
				.findViewById(R.id.planmemo_title_txt);
		planmemo_editor = (EditText) viewMemo.findViewById(R.id.edit_planmemo);
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
				if (s.toString().length() >= 80)
					planmemo_editor_title.setText("备注不能超过80个字符");
				else
					planmemo_editor_title.setText("已输入" + s.toString().length()
							+ "字，还可输入"
							+ String.valueOf((80 - s.toString().length()))
							+ "字");
			}
		});
		// 拍照
		picNumTextView = (TextView) viewPicture
				.findViewById(R.id.planpicture_num);
		btn_photo = (ImageButton) viewPicture.findViewById(R.id.btn_dj_photo);
		btn_photo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (!checkMaxFileNum(REQCODE_PICTURE))
					return;

				Intent getImageByCamera = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				uuid = UUID.randomUUID();
				photoTimeString = DateTimeHelper.getDateTimeNow();
				picname = uuid.toString()
						+ "_"
						+ DateTimeHelper.TransDateTime(photoTimeString,
								"yyyyMMddHHmmss") + ".jpg";
				imagePath = AppConst.CurrentResultPath_Pic(AppContext
						.GetCurrLineID()) + picname;
				File f = new File(imagePath);
				File destDir = new File(AppConst
						.CurrentResultPath_Record(AppContext.GetCurrLineID()));
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				try {
					f.createNewFile();
					// 这行代码很重要，没有的话会因为写入权限不够出一些问题
					f.setWritable(true, false);
				} catch (IOException e) {
				}
				getImageByCamera.putExtra("return-data", true);
				getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(f));
				getImageByCamera.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());
				getImageByCamera.putExtra("noFaceDetection", true);
				startActivityForResult(getImageByCamera, REQCODE_PICTURE);
			}
		});
		plan_picture_gallery = (Gallery) viewPicture
				.findViewById(R.id.plan_picture_galley);
		DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
				planFilesBuffer.get(REQCODE_PICTURE),
				R.layout.listitem_djplanimage);
		plan_picture_gallery.setAdapter(imageAdapter);
		plan_picture_gallery
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		// 长按图片后的弹出菜单
		plan_picture_gallery
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {

						menu.setHeaderTitle("图片操作");
						menu.add(0, MENUACTION_DELETE, 0, "删除");
						menu.add(0, MENUACTION_RETURN, 0, "返回");

						AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
						plan_picture_gallery.setSelection(info.position);
					}
				});
		// 单击图片后浏览图片
		plan_picture_gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					final int arg2, long arg3) {
				final String[] actionFileName = new String[1];
				ImageView aImage = (ImageView) v
						.findViewById(R.id.planimage_listitem_icon);
				if (aImage == null) {
					return;
				}
				final DJ_PhotoByResult _photoByResult = (DJ_PhotoByResult) aImage
						.getTag();
				actionFileName[0] = _photoByResult.getFilePath();

				UIHelper.showImageZoomDialog(Dianjian_Main.this,
						actionFileName, "false");
			}
		});

		// 录音
		btn_record = (ImageButton) viewAudio.findViewById(R.id.btn_dj_record);
		btn_record.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (!checkMaxFileNum(REQCODE_SOUND))
					return;

				uuid = UUID.randomUUID();
				audioname = uuid.toString() + ".arm";
				audioPath = AppConst.CurrentResultPath_Record(AppContext
						.GetCurrLineID()) + audioname;
				File f = new File(audioPath);
				File destDir = new File(AppConst
						.CurrentResultPath_Record(AppContext.GetCurrLineID()));
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
				intentFromRecord.putExtra("return-data", true);
				startActivityForResult(intentFromRecord, REQCODE_SOUND);
			}
		});
		soundNumTextView = (TextView) viewAudio
				.findViewById(R.id.planaudio_num);

		plan_audio_gallery = (Gallery) viewAudio
				.findViewById(R.id.plan_audio_gallery);
		DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
				planFilesBuffer.get(REQCODE_SOUND),
				R.layout.listitem_djplanimage);
		plan_audio_gallery.setAdapter(audioAdapter);

		// 长按声音
		plan_audio_gallery
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						// TODO Auto-generated method stub
						return false;// false 才能触发弹出菜单
					}
				});

		// 长按声音后的弹出菜单
		plan_audio_gallery
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("录音文件操作");
						menu.add(1, MENUACTION_DELETE, 0, "删除");
						menu.add(1, MENUACTION_RETURN, 0, "返回");

						AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
						plan_audio_gallery.setSelection(info.position);

					}
				});

		// 单击录音文件后播放录音
		plan_audio_gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					final int arg2, long arg3) {
				final String actionFileName;
				ImageView aImage = (ImageView) v
						.findViewById(R.id.planimage_listitem_icon);
				if (aImage == null) {
					return;
				}
				final DJ_PhotoByResult _photoByResult = (DJ_PhotoByResult) aImage
						.getTag();
				actionFileName = _photoByResult.getFilePath();

				List<String> pathlist = new ArrayList<String>();
				for (int i = 0; i < planFilesBuffer.get(REQCODE_SOUND).size(); i++) {
					String pa = planFilesBuffer.get(REQCODE_SOUND).get(i)
							.getFilePath();
					pathlist.add(pa);
				}
				AudioPlayerDialog Audialog = new AudioPlayerDialog(
						Dianjian_Main.this, actionFileName, pathlist);
				Audialog.show();
			}
		});
	}

	*//**
	 * ViewPager图标点击监听
	 *//*
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};

	*//**
	 * 页卡切换监听(原作者:D.Winter)
	 *//*
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			changeViewPager(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	*//**
	 * 切换Page
	 *//*
	private void changeViewPager(int arg0) {

		switch (arg0) {
		case 0:// 结果栏
			ll_Tab1.setEnabled(true);
			ll_Tab2.setEnabled(false);
			ll_Tab3.setEnabled(false);
			ll_Tab4.setEnabled(false);

			txtTab1.setTextColor(0xff228B22);
			txtTab2.setTextColor(Color.BLACK);
			txtTab3.setTextColor(Color.BLACK);
			txtTab4.setTextColor(Color.BLACK);
			break;
		case 1:// 备注栏
			ll_Tab1.setEnabled(false);
			ll_Tab2.setEnabled(true);
			ll_Tab3.setEnabled(false);
			ll_Tab4.setEnabled(false);

			txtTab2.setTextColor(0xff228B22);
			txtTab1.setTextColor(Color.BLACK);
			txtTab3.setTextColor(Color.BLACK);
			txtTab4.setTextColor(Color.BLACK);
			break;
		case 2:// 拍照
			ll_Tab1.setEnabled(false);
			ll_Tab2.setEnabled(false);
			ll_Tab3.setEnabled(true);
			ll_Tab4.setEnabled(false);

			txtTab3.setTextColor(0xff228B22);
			txtTab1.setTextColor(Color.BLACK);
			txtTab2.setTextColor(Color.BLACK);
			txtTab4.setTextColor(Color.BLACK);

			break;
		case 3:// 录音
			ll_Tab1.setEnabled(false);
			ll_Tab2.setEnabled(false);
			ll_Tab3.setEnabled(false);
			ll_Tab4.setEnabled(true);

			txtTab4.setTextColor(0xff228B22);
			txtTab1.setTextColor(Color.BLACK);
			txtTab2.setTextColor(Color.BLACK);
			txtTab3.setTextColor(Color.BLACK);
			break;
		}
		currIndex = arg0;

		// 关闭输入栏
		inputMethodCtrl();
	}

	*//**
	 * 隐藏输入法
	 *//*
	private void inputMethodCtrl() {
		if (edit_plan_result_jl != null)
			InputTools.HideKeyboard(edit_plan_result_jl);
	}

	*//**
	 * 初始化快捷栏
	 *//*
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

	*//**
	 * 快捷栏item点击事件
	 *//*
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
					loadNextUnDoDJPlan();
				}
				break;
			default:
				break;
			}
		}
	};

	*//***
	 * 跳转到首条未做计划
	 *//*
	private void LoadFirstDJPLanWithOutCompleted() {
		currentPlanIndex = -1;
		if (loading != null)
			loading.show();

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

		// if (!LoadNextOrPreDJPlan(true, true)) {
		// if (loading != null)
		// loading.dismiss();
		// return;
		// }
		if (loading != null)
			loading.dismiss();
		initSettingsByPlanData(currentDJPlan.getDJPlan());
		dispIndexOfDJPlan();
		loadDJResultToUI(currDJPlancompletedYN);
	}

	private String LoadNextOrPreDJPlanForLoop(Boolean nextFlag,
			Boolean IsJudgeComplete) {
		String result = "true";
		if (nextFlag) {
			if (currentPlanIndex >= YXPlanTotalCount - 1) {
				final int cycTotalCount = YXPlanTotalCount;
				int completeNum = 0;
				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					completeNum = DJIDPosHelper
							.GetCompleteNum(current_IDPos_ID);
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					completeNum = DJIDPosHelper.GetCompleteNum(
							Dianjian_Main.this, current_IDPos_ID);
				}
				final int srNotNeedDoNum = DJIDPosHelper
						.GetSKNotNeedDoNum(current_IDPos_ID);
				final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
						- completeNum - srNotNeedDoNum)
						: 0;
				String noticeString = getString(R.string.plan_statistic_notice,
						cycTotalCount, notcompleteNum, srNotNeedDoNum);

				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					countString = AppContext.DJSpecCaseFlag == 1 ? "特巡条目："
							+ YXPlanTotalCount
							: ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);

					noticeString = AppContext.DJSpecCaseFlag == 1 ? "计划完成情况\n"
							+ "特巡计划合计：" + cycTotalCount + "条\n"
							+ "‘确定’退出，‘取消’继续。" : noticeString;
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					countString = ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);
				}

				final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
						.valueOf(current_IDPos_ID));

				Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
						.setTitle("条目到底")
						.setMessage(noticeString)
						// 相当于点击确认按钮
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										String msg = checkIDPosMinCost();
										if (!StringUtils.isEmpty(msg)) {
											if (notcompleteNum > 0) {
												UIHelper.ToastMessage(Dianjian_Main.this, msg);
												LoadFirstDJPLanWithOutCompleted();
											}
											else {
												if (srNotNeedDoNum == cycTotalCount) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
													_myidpos.setPlanCount(countString);

													AppContext.setCurIDPos(null);
													AppManager
															.getAppManager()
															.finishActivity(
																	Dianjian_Main.this);
												} else {
													UIHelper.ToastMessage(Dianjian_Main.this, msg);
													currentPlanIndex = -1;
													loadNextDJPlan();
												}
											}
											return;
										}
										
										if (notcompleteNum == 0) {
											if (_myidpos != null) {
												_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);

											}
										}
										_myidpos.setPlanCount(countString);

										AppContext.setCurIDPos(null);
										AppManager.getAppManager()
												.finishActivity(
														Dianjian_Main.this);
									}
								})
						// 相当于点击取消按钮
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										if (notcompleteNum > 0)
											LoadFirstDJPLanWithOutCompleted();
										else {
											if (notcompleteNum <= 0) {
												if (srNotNeedDoNum == cycTotalCount) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
													_myidpos.setPlanCount(countString);

													AppContext.setCurIDPos(null);
													AppManager
															.getAppManager()
															.finishActivity(
																	Dianjian_Main.this);
												} else {
													switch (CURRENT_MODE) {
													case PopMenu_EDIT:
														currentPlanIndex = -1;
														loadNextDJPlan();
														break;
													case PopMenu_CHECK:
														_myidpos.setPlanCount(countString);
														
														AppContext.setCurIDPos(null);
														AppManager
																.getAppManager()
																.finishActivity(
																		Dianjian_Main.this);
														break;
													}
												}
											} else {
												dispIndexOfDJPlan();
											}
										}
									}
								}).setCancelable(false).create();
				dialog.show();
				return result = "false";
			}
			currentPlanIndex = currentPlanIndex + 1;
		} else {
			if (currentPlanIndex <= 0) {
				final int cycTotalCount = YXPlanTotalCount;
				int completeNum = 0;
				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					completeNum = DJIDPosHelper
							.GetCompleteNum(current_IDPos_ID);
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					completeNum = DJIDPosHelper.GetCompleteNum(
							Dianjian_Main.this, current_IDPos_ID);
				}
				final int srNotNeedDoNum = DJIDPosHelper
						.GetSKNotNeedDoNum(current_IDPos_ID);
				final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
						- completeNum - srNotNeedDoNum)
						: 0;
				String noticeString = getString(R.string.plan_statistic_notice,
						cycTotalCount, notcompleteNum, srNotNeedDoNum);

				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					countString = AppContext.DJSpecCaseFlag == 1 ? "特巡条目："
							+ YXPlanTotalCount
							: ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);

					noticeString = AppContext.DJSpecCaseFlag == 1 ? "计划完成情况\n"
							+ "特巡计划合计：" + cycTotalCount + "条\n"
							+ "‘确定’退出，‘取消’继续。" : noticeString;
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					countString = ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);
				}

				final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
						.valueOf(current_IDPos_ID));
				Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
						.setTitle("条目到顶")
						.setMessage(noticeString)
						// 相当于点击确认按钮
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										
										String msg = checkIDPosMinCost();
										if (!StringUtils.isEmpty(msg)) {
											if (notcompleteNum > 0) {
												UIHelper.ToastMessage(Dianjian_Main.this, msg);
												LoadFirstDJPLanWithOutCompleted();
											}
											else {
												if (srNotNeedDoNum == cycTotalCount) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
													_myidpos.setPlanCount(countString);

													AppContext.setCurIDPos(null);
													AppManager
															.getAppManager()
															.finishActivity(
																	Dianjian_Main.this);
												} else {
													UIHelper.ToastMessage(Dianjian_Main.this, msg);
													currentPlanIndex = -1;
													loadNextDJPlan();
												}
											}
											return;
										}
										
										if (notcompleteNum == 0) {
											if (_myidpos != null) {
												_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);

											}
										}

										_myidpos.setPlanCount(countString);

										AppContext.setCurIDPos(null);
										AppManager.getAppManager()
												.finishActivity(
														Dianjian_Main.this);
									}
								})
						// 相当于点击取消按钮
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (notcompleteNum > 0)
											LoadFirstDJPLanWithOutCompleted();
										else {
											if (notcompleteNum <= 0) {
												if (srNotNeedDoNum == cycTotalCount) {
													_myidpos.setPlanCount(countString);
//													_myidpos.setPlanCount(countString);

													AppContext.setCurIDPos(null);
													AppManager
															.getAppManager()
															.finishActivity(
																	Dianjian_Main.this);
												} else {
													switch (CURRENT_MODE) {
													case PopMenu_EDIT:
														currentPlanIndex = -1;
														loadNextDJPlan();
														break;
													case PopMenu_CHECK:
														_myidpos.setPlanCount(countString);
														
														AppContext.setCurIDPos(null);
														AppManager
																.getAppManager()
																.finishActivity(
																		Dianjian_Main.this);
														break;
													}
												}
											} else {
												dispIndexOfDJPlan();
											}
										}
									}
								}).setCancelable(false).create();
				dialog.show();
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
			// 不符合周期
			emsg = mDJPlan.JudgePlanTemp(Dianjian_Main.this, DateTimeHelper.GetDateTimeNow());
			if (emsg.length() > 0) {
				// 周期过期处理
				Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
						.setTitle("系统提示")
						.setMessage(emsg)
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										AppContext.setCurIDPos(null);
										AppManager.getAppManager()
												.finishActivity(
														Dianjian_Main.this);
									}
								}).setCancelable(false).create();
				dialog.show();
				return result = "false";
				// return LoadNextOrPreDJPlan(nextFlag, IsJudgeComplete);
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
					showSR(true, no);
					currentDJPlan = mDJPlan;
					no++;
					return result = "true";
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
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(
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
				}
				currentDJPlan = mDJPlan;
			} else {
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(
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
				}
				currentDJPlan = mDJPlan;
			}
		}
		// [end]
		// [start] 条件巡检
		else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
			if (IsJudgeComplete) {
				if (mDJPlan.JudgePlanIsCompletedForCASEXJ(Dianjian_Main.this)) {
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
					showSR(true, no);
					currentDJPlan = mDJPlan;
					no++;
					return result = "true";
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
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(
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
				}
				currentDJPlan = mDJPlan;
			} else {
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(
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
				}
				currentDJPlan = mDJPlan;
			}
		}
		// [end]

		return result;
	}

	*//**
	 * 向前或者向后移动游标
	 * 
	 * @param nextFlag
	 * @param IsJudgeComplete
	 *            是否判断已完成
	 * @return
	 *//*
	int no = 0;

	private Boolean LoadNextOrPreDJPlan(Boolean nextFlag,
			Boolean IsJudgeComplete) {
		if (nextFlag) {
			if (currentPlanIndex >= YXPlanTotalCount - 1) {
				final int cycTotalCount = YXPlanTotalCount;
				int completeNum = 0;
				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					completeNum = DJIDPosHelper
							.GetCompleteNum(current_IDPos_ID);
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					completeNum = DJIDPosHelper.GetCompleteNum(
							Dianjian_Main.this, current_IDPos_ID);
				}
				final int srNotNeedDoNum = DJIDPosHelper
						.GetSKNotNeedDoNum(current_IDPos_ID);
				final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
						- completeNum - srNotNeedDoNum)
						: 0;
				String noticeString = getString(R.string.plan_statistic_notice,
						cycTotalCount, notcompleteNum, srNotNeedDoNum);

				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					countString = AppContext.DJSpecCaseFlag == 1 ? "特巡条目："
							+ YXPlanTotalCount
							: ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);

					noticeString = AppContext.DJSpecCaseFlag == 1 ? "计划完成情况\n"
							+ "特巡计划合计：" + cycTotalCount + "条\n"
							+ "‘确定’退出，‘取消’继续。" : noticeString;
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					countString = ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);
				}

				final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
						.valueOf(current_IDPos_ID));

				Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
						.setTitle("条目到底")
						.setMessage(noticeString)
						// 相当于点击确认按钮
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {										
										if (notcompleteNum == 0) {
											if (_myidpos != null) {
												_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);

											}
										}
										_myidpos.setPlanCount(countString);

										AppContext.setCurIDPos(null);
										AppManager.getAppManager()
												.finishActivity(
														Dianjian_Main.this);
									}
								})
						// 相当于点击取消按钮
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										if (notcompleteNum > 0)
											LoadFirstDJPLanWithOutCompleted();
										else {
											if (notcompleteNum <= 0) {
												if (srNotNeedDoNum == cycTotalCount) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
													_myidpos.setPlanCount(countString);

													AppContext.setCurIDPos(null);
													AppManager
															.getAppManager()
															.finishActivity(
																	Dianjian_Main.this);
												} else {
													switch (CURRENT_MODE) {
													case PopMenu_EDIT:
														currentPlanIndex = -1;
														loadNextDJPlan();
														break;
													case PopMenu_CHECK:
														_myidpos.setPlanCount(countString);
														
														AppContext.setCurIDPos(null);
														AppManager
																.getAppManager()
																.finishActivity(
																		Dianjian_Main.this);
														break;
													}
												}
											} else {
												dispIndexOfDJPlan();
											}
										}
									}
								}).setCancelable(false).create();
				dialog.show();
				return false;
			}
			currentPlanIndex = currentPlanIndex + 1;
		} else {
			if (currentPlanIndex <= 0) {
				final int cycTotalCount = YXPlanTotalCount;
				int completeNum = 0;
				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					completeNum = DJIDPosHelper
							.GetCompleteNum(current_IDPos_ID);
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					completeNum = DJIDPosHelper.GetCompleteNum(
							Dianjian_Main.this, current_IDPos_ID);
				}
				final int srNotNeedDoNum = DJIDPosHelper
						.GetSKNotNeedDoNum(current_IDPos_ID);
				final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
						- completeNum - srNotNeedDoNum)
						: 0;
				// String noticeString = "计划完成情况\n" + "计划合计：" + cycTotalCount
				// + "条\n" + "未完成数：" + notcompleteNum + "条\n" + "无需做计划数："
				// + srNotNeedDoNum + "条\n‘确定’退出，‘取消’继续。";
				String noticeString = getString(R.string.plan_statistic_notice,
						cycTotalCount, notcompleteNum, srNotNeedDoNum);

				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					countString = AppContext.DJSpecCaseFlag == 1 ? "特巡条目："
							+ YXPlanTotalCount
							: ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);

					noticeString = AppContext.DJSpecCaseFlag == 1 ? "计划完成情况\n"
							+ "特巡计划合计：" + cycTotalCount + "条\n"
							+ "‘确定’退出，‘取消’继续。" : noticeString;
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					countString = ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);
				}

				final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
						.valueOf(current_IDPos_ID));
				Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
						.setTitle("条目到顶")
						.setMessage(noticeString)
						// 相当于点击确认按钮
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (notcompleteNum == 0) {
											if (_myidpos != null) {
												_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);

											}
										}

										_myidpos.setPlanCount(countString);

										AppContext.setCurIDPos(null);
										AppManager.getAppManager()
												.finishActivity(
														Dianjian_Main.this);
									}
								})
						// 相当于点击取消按钮
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (notcompleteNum > 0)
											LoadFirstDJPLanWithOutCompleted();
										else {
											if (notcompleteNum <= 0) {
												if (srNotNeedDoNum == cycTotalCount) {
													_myidpos.setPlanCount(countString);
													_myidpos.setPlanCount(countString);

													AppContext.setCurIDPos(null);
													AppManager
															.getAppManager()
															.finishActivity(
																	Dianjian_Main.this);
												} else {
													switch (CURRENT_MODE) {
													case PopMenu_EDIT:
														currentPlanIndex = -1;
														loadNextDJPlan();
														break;
													case PopMenu_CHECK:
														_myidpos.setPlanCount(countString);
														
														AppContext.setCurIDPos(null);
														AppManager
																.getAppManager()
																.finishActivity(
																		Dianjian_Main.this);
														break;
													}
												}
											} else {
												dispIndexOfDJPlan();
											}
										}
									}
								}).setCancelable(false).create();
				dialog.show();
				return false;
			}
			currentPlanIndex = currentPlanIndex - 1;
		}

		mDJPlan = AppContext.YXDJPlanByIDPosBuffer.get(current_IDPos_ID).get(
				currentPlanIndex);

		// 如果是特巡，则不做任何过滤
		if (AppContext.DJSpecCaseFlag == 1) {
			currentDJPlan = mDJPlan;
			return true;
		}

		String emsg = "";

		// [start] 巡点检
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			// 不符合周期
			emsg = mDJPlan.JudgePlanTemp(Dianjian_Main.this, DateTimeHelper.GetDateTimeNow());
			if (emsg.length() > 0) {
				// 周期过期处理
				Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
						.setTitle("系统提示")
						.setMessage(emsg)
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										AppContext.setCurIDPos(null);
										AppManager.getAppManager()
												.finishActivity(
														Dianjian_Main.this);
									}
								}).setCancelable(false).create();
				dialog.show();
				return false;
				// return LoadNextOrPreDJPlan(nextFlag, IsJudgeComplete);
			}

			// 计划已完成跳过或者计划无需做跳过
			if (IsJudgeComplete) {
				if (mDJPlan.JudgePlanIsCompleted(DateTimeHelper
						.GetDateTimeNow())) {
					return LoadNextOrPreDJPlan(nextFlag, IsJudgeComplete);
				}
				if (mDJPlan.JudgePlanIsSrControl()) {
					// 该周期下已经设置过启停点，并且启停点下计划无需做，则跳过
					if (mDJPlan.SrIsSetting()
							&& (!mDJPlan.JudgePlanBySrPoint())) {
						return LoadNextOrPreDJPlan(nextFlag, IsJudgeComplete);
					}
				}

			}
			if (mDJPlan.JudgePlanIsSrControl()) {
				// 当前周期没有设置过启停点
				if (!mDJPlan.SrIsSetting()) {
					showSR(true, no);
					currentDJPlan = mDJPlan;
					no++;
					return true;
				}
				if (!mDJPlan.JudgePlanBySrPoint())
					return LoadNextOrPreDJPlan(nextFlag, false);
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(
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
				}
				currentDJPlan = mDJPlan;
			} else {
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(
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
				}
				currentDJPlan = mDJPlan;
			}
		}
		// [end]
		// [start] 条件巡检
		else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
			if (IsJudgeComplete) {
				if (mDJPlan.JudgePlanIsCompletedForCASEXJ(Dianjian_Main.this))
					return LoadNextOrPreDJPlan(nextFlag, IsJudgeComplete);
				if (mDJPlan.JudgePlanIsSrControl()) {
					// 已经设置过启停点，并且启停点下计划无需做，则跳过
					if (mDJPlan.SrIsSetting()
							&& (!mDJPlan.JudgePlanBySrPoint())) {
						return LoadNextOrPreDJPlan(nextFlag, IsJudgeComplete);
					}
				}
			}
			if (mDJPlan.JudgePlanIsSrControl()) {
				// 没有设置过启停点
				if (!mDJPlan.SrIsSetting()) {
					showSR(true, no);
					currentDJPlan = mDJPlan;
					no++;
					return true;
				}
				if (!mDJPlan.JudgePlanBySrPoint())
					return LoadNextOrPreDJPlan(nextFlag, false);
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(
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
				}
				currentDJPlan = mDJPlan;
			} else {
				if (mDJPlan.JudgePlanIsLKControl()
						&& (!mDJPlan.getLKDoYn())
						&& (!StringUtils.isEmpty(mDJPlan.getDJPlan()
								.getMustCheck_YN()))
						&& (mDJPlan.getDJPlan().getMustCheck_YN().equals("0"))) {
					List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
							.GetPlanListByControlPoint(
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
				}
				currentDJPlan = mDJPlan;
			}
		}
		// [end]

		return true;

	}

	*//**
	 * 切换到前一条计划
	 *//*
	public void btn_pre_djplan(View v) {
		btn_goto_pre.setEnabled(false);
		switch (CURRENT_MODE) {
		case PopMenu_EDIT:
			this.loadPreDJPlan();
			break;
		case PopMenu_CHECK:
			this.loadPreUnDoDJPlan();
			break;
		}
		btn_goto_pre.setEnabled(true);
	}

	*//**
	 * 点击确认按钮和OK键保存数据
	 *//*
	private void saveData() {
		btn_savedata.setEnabled(false);

		Z_DJ_Plan _plan = AppContext.DJPlanBuffer.get(current_IDPos_ID)
				.get(currentPlanIndex).getDJPlan();
		if (_plan != null) {
			String completeTime = DateTimeHelper.getDateTimeNow();
			if (!saveDJResult(completeTime)) {
				btn_savedata.setEnabled(true);
				return;
			}
			_plan.setLastComplete_DT(completeTime);
			// UIHelper.ToastMessageForSaveOK(getApplication(),
			// R.string.plan_save_ok);
		}
		DJ_IDPos _curridpos = getIDPosInfoByIDPosID(Integer
				.valueOf(current_IDPos_ID));

		// 如果本条是修改的，则不自动加载下一条
		if (currDJPlancompletedYN) {
			UIHelper.ToastMessageForSaveOK(getApplication(),
					R.string.plan_edit_ok);
		} else {
			// 点检时，更新钮扣到位结束时间，修改时不更新
			DJIDPosHelper.GetIntance().updateDJTaskIDPos(Dianjian_Main.this,
					_curridpos, current_DJLine_ID);
			loadNextUnDoDJPlan();
		}
		btn_savedata.setEnabled(true);
	}

	*//**
	 * 切换到前一条计划
	 *//*
	public void btn_next_djplan(View v) {
		btn_goto_next.setEnabled(false);
		switch (CURRENT_MODE) {
		case PopMenu_EDIT:
			this.loadNextDJPlan();
			break;
		case PopMenu_CHECK:
			this.loadNextUnDoDJPlan();
			break;
		}
		btn_goto_next.setEnabled(true);
	}

	// [start] 加载数据
	*//**
	 * 加载下一条计划
	 *//*
	private void loadNextDJPlan() {

		if (loading != null)
			loading.show();

		String result = LoadNextOrPreDJPlanForLoop(true, false);
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

		// if (!LoadNextOrPreDJPlan(true, false)) {
		// if (loading != null)
		// loading.dismiss();
		// return;
		// }
		inputMethodCtrl();
		if (loading != null)
			loading.dismiss();
		initSettingsByPlanData(currentDJPlan.getDJPlan());
		dispIndexOfDJPlan();
		loadDJResultToUI(currDJPlancompletedYN);
	}

	*//**
	 * 加载下一条未做计划
	 * 
	 * @param IsJudgeComplete
	 *//*
	private void loadNextUnDoDJPlan() {

		if (loading != null)
			loading.show();

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

		// if (!LoadNextOrPreDJPlan(true, true)) {
		// if (loading != null)
		// loading.dismiss();
		// return;
		// }
		inputMethodCtrl();
		if (loading != null)
			loading.dismiss();
		initSettingsByPlanData(currentDJPlan.getDJPlan());
		dispIndexOfDJPlan();
		loadDJResultToUI(currDJPlancompletedYN);
	}

	*//**
	 * 加载上一条计划
	 *//*
	private void loadPreDJPlan() {
		if (loading != null)
			loading.show();

		String result = LoadNextOrPreDJPlanForLoop(false, false);
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

		// if (!LoadNextOrPreDJPlan(false, false)) {
		// if (loading != null)
		// loading.dismiss();
		// return;
		// }
		inputMethodCtrl();
		if (loading != null)
			loading.dismiss();
		initSettingsByPlanData(currentDJPlan.getDJPlan());
		dispIndexOfDJPlan();
		loadDJResultToUI(currDJPlancompletedYN);
	}

	*//**
	 * 加载上一条未做计划
	 * 
	 * @param IsJudgeComplete
	 *//*
	private void loadPreUnDoDJPlan() {

		if (loading != null)
			loading.show();

		String result = LoadNextOrPreDJPlanForLoop(false, true);
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

		// if (!LoadNextOrPreDJPlan(false, true)) {
		// if (loading != null)
		// loading.dismiss();
		// return;
		// }
		inputMethodCtrl();
		if (loading != null)
			loading.dismiss();
		initSettingsByPlanData(currentDJPlan.getDJPlan());
		dispIndexOfDJPlan();
		loadDJResultToUI(currDJPlancompletedYN);
	}

	// [end]

	*//**
	 * 根据点检计划初始化设置
	 *//*
	private void initSettingsByPlanData(Z_DJ_Plan myDJPlan) {
		if (myDJPlan == null) {
			return;
		}
		if (mTabPager != null)
			mTabPager.setCurrentItem(0);
		if (planFilesBuffer != null && planFilesBuffer.size() > 0) {
			planFilesBuffer.clear();
		}
		planFilesBuffer = new Hashtable<Integer, ArrayList<DJ_PhotoByResult>>();
		// 启停、主控
		lkImageButton.setVisibility(View.GONE);
		srImageButton.setVisibility(View.GONE);
		// 巡点检时才可以修改启停
		if ((AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType() ||
				AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType())
				&& !(StringUtils.isEmpty(currentDJPlan.getDJPlan()
						.getSRPoint_ID()))
				&& Integer.parseInt(currentDJPlan.getDJPlan().getSRPoint_ID()) > 0) {
			if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
				if (AppContext.DJSpecCaseFlag == 0)
					srImageButton.setVisibility(View.VISIBLE);
			} else {
				srImageButton.setVisibility(View.VISIBLE);
			}
		}

		// 计划
		TextView plan_description = (TextView) findViewById(R.id.plan_description);
		String planDescString = "计划内容：" + myDJPlan.getPlanDesc_TX() + "\n判断标准："
				+ myDJPlan.getESTStandard_TX();
		plan_description.setText(planDescString);
		plan_description.setLongClickable(true);
		plan_description.setOnTouchListener(new MyGestureListener(
				Dianjian_Main.this));
		plan_description.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				AppContext.voiceConvertService.Speaking(((TextView) v)
						.getText().toString());
				return false;
			}
		});
		// 备注
		ImageView img_memo_flag = (ImageView) findViewById(R.id.dianjian_tab_memo_flag);
		EditText edit_planmemo = (EditText) findViewById(R.id.edit_planmemo);
		img_memo_flag.setVisibility(View.GONE);
		// 照片
		ImageView img_picture_flag = (ImageView) findViewById(R.id.dianjian_tab_picture_flag);
		img_picture_flag.setVisibility(View.GONE);
		if ((planFilesBuffer != null && planFilesBuffer.get(REQCODE_PICTURE) != null)
				&& (planFilesBuffer.get(REQCODE_PICTURE).size() > 0)) {
			img_picture_flag.setVisibility(View.VISIBLE);
		}
		// 录音
		ImageView img_audio_flag = (ImageView) findViewById(R.id.dianjian_tab_audio_flag);
		img_audio_flag.setVisibility(View.GONE);
		if (planFilesBuffer != null
				&& (planFilesBuffer.get(REQCODE_SOUND) != null)
				&& (planFilesBuffer.get(REQCODE_SOUND).size() > 0)) {
			img_audio_flag.setVisibility(View.VISIBLE);
		}

		resHisImageView = (ImageView) (viewResult)
				.findViewById(R.id.img_plan_his);
		resHisImageView.setTag("0");
		resHisImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				UIHelper.showQuerydataHisResult(Dianjian_Main.this,
						current_DJLine_ID, currentDJPlan.getDJPlan()
								.getDJ_Plan_ID(), currentDJPlan.getDJPlan()
								.getPlanDesc_TX(), currentDJPlan.getDJPlan()
								.getDataType_CD());
			}
		});

		// 测量类按钮
		btn_start_others = (Button) (viewResult)
				.findViewById(R.id.btn_start_others);
		btn_start_others.setTag(true);
		btn_start_others.setOnClickListener(new View.OnClickListener() {

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
					UIHelper.showCLWDXC(Dianjian_Main.this, fsl, true,
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
					UIHelper.showCLZDXC(Dianjian_Main.this, parms.toString(),
							true, REQCODE_VIBRATION, clModuleType,
							AppContext.getBlueToothAddressforVibration(),
							AppContext.getBTConnectPwdforVibration(), 0);
				}
				// 测速类
				else if (currentDJPlan.getDJPlan().getDataType_CD()
						.equals(AppConst.DJPLAN_DATACODE_CS)) {
					UIHelper.showCLZSXC(Dianjian_Main.this, "", true,
							REQCODE_SPEED, clModuleType, 0);
				}
			}
		});

		edit_plan_result_jl = (EditText) (viewResult)
				.findViewById(R.id.plan_result_jl);
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
					plan_result_almlevel.setText("");
					plan_result_almlevel.setTag("0");
					plan_result_almlevel.setTextColor(Color.BLACK);
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
				 删除字符串中的dot 
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
		txt_plandata_lastresult = (TextView) (viewResult)
				.findViewById(R.id.plandata_lastresult);
		txt_plandata_lastresult
				.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO 自动生成的方法存根
						String result = ((TextView) v).getText().toString();
						if (!StringUtils.isEmpty(result)) {
							String titleMes = "上次结果详情";
							final SimpleTextDialog _dialog = new SimpleTextDialog(
									Dianjian_Main.this, titleMes, result);
							_dialog.setTextSize(20);
							_dialog.setOKButton(
									R.string.simple_text_dialog_btnok,
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
						return false;
					}
				});
		String lastRes = StringUtils.isEmpty(myDJPlan.getLastResult_TX()) ? ""
				: myDJPlan.getLastResult_TX();
		String unitString = StringUtils.isEmpty(myDJPlan.getMetricUnit_TX()) ? ""
				: myDJPlan.getMetricUnit_TX();
		if (StringUtils.isEmpty(lastRes))
			txt_plandata_lastresult.setText("");
		else
			txt_plandata_lastresult.setText(lastRes + " " + unitString);
		// 最近日期时间
		String dateString = StringUtils.isEmpty(myDJPlan.getLastComplete_DT()) ? ""
				: DateTimeHelper.TransDateTime(myDJPlan.getLastComplete_DT(),
						"yyyy-MM-dd");
		String timeString = StringUtils.isEmpty(myDJPlan.getLastComplete_DT()) ? ""
				: DateTimeHelper.TransDateTime(myDJPlan.getLastComplete_DT(),
						"HH:mm:ss");
		txt_plandata_lastdate = (TextView) (viewResult)
				.findViewById(R.id.plandata_lastdate);
		txt_plandata_lastdate.setText(dateString);
		txt_plandata_lasttime = (TextView) (viewResult)
				.findViewById(R.id.plandata_lasttime);
		txt_plandata_lasttime.setText(timeString);
		if (myDJPlan.getDataType_CD().equalsIgnoreCase(
				AppConst.DJPLAN_DATACODE_JL)) {// 记录类
			img_plandatatype.setImageResource(R.drawable.ic_plantype_jl);

			btn_start_others.setVisibility(View.INVISIBLE);
			txt_plan_result_others.setVisibility(View.INVISIBLE);
			edit_plan_result_jl.setVisibility(View.VISIBLE);
			edit_plan_result_jl.requestFocus();
			plan_result_almlevel.setVisibility(View.VISIBLE);
		} else if (myDJPlan.getDataType_CD().equalsIgnoreCase(
				AppConst.DJPLAN_DATACODE_CW)) {// 测温类
			img_plandatatype.setImageResource(R.drawable.ic_plantype_cw);

			btn_start_others.setVisibility(View.VISIBLE);
			txt_plan_result_others.setVisibility(View.VISIBLE);
			plan_result_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.INVISIBLE);
			plan_result_jl_notice.setVisibility(View.INVISIBLE);
			btn_start_others.setBackgroundResource(R.drawable.ic_start_cw);
		} else if (myDJPlan.getDataType_CD().equalsIgnoreCase(
				AppConst.DJPLAN_DATACODE_CZ)) {// 测振类
			img_plandatatype.setImageResource(R.drawable.ic_plantype_cz);

			btn_start_others.setVisibility(View.VISIBLE);
			txt_plan_result_others.setVisibility(View.VISIBLE);
			plan_result_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.INVISIBLE);
			plan_result_jl_notice.setVisibility(View.INVISIBLE);
			btn_start_others.setBackgroundResource(R.drawable.ic_start_cz);
		} else if (myDJPlan.getDataType_CD().equalsIgnoreCase(
				AppConst.DJPLAN_DATACODE_CS)) { // 测速类
			img_plandatatype.setImageResource(R.drawable.ic_plantype_cs);

			btn_start_others.setVisibility(View.VISIBLE);
			txt_plan_result_others.setVisibility(View.VISIBLE);
			plan_result_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.INVISIBLE);
			plan_result_jl_notice.setVisibility(View.INVISIBLE);
			btn_start_others.setBackgroundResource(R.drawable.ic_start_cs);
		} else { // 观察类
			img_plandatatype.setImageResource(R.drawable.ic_plantype_gc);

			btn_start_others.setVisibility(View.VISIBLE);
			txt_plan_result_others.setVisibility(View.VISIBLE);
			plan_result_almlevel.setVisibility(View.VISIBLE);
			edit_plan_result_jl.setVisibility(View.INVISIBLE);
			plan_result_jl_notice.setVisibility(View.INVISIBLE);
			btn_start_others.setBackgroundResource(R.drawable.ic_datagroup);
		}
	}

	*//**
	 * 显示计划序号、数量、剩余和录入状态
	 *//*
	private void dispIndexOfDJPlan() {
		// 计划的序号和总数
		if (currentDJPlan == null) {
			Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
					.setTitle("退出计划执行")
					.setMessage("钮扣下无可做计划！")
					// 相当于点击确认按钮
					.setPositiveButton(R.string.sure,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									
									AppContext.setCurIDPos(null);
									AppManager.getAppManager().finishActivity(
											Dianjian_Main.this);
								}
							}).setCancelable(false).create();
			dialog.show();
		} else {
			startTime = DateTimeHelper.GetDateTimeNow();
			TextView plan_index = (TextView) findViewById(R.id.plan_index);
			String indexCaption = String.valueOf(currentPlanIndex + 1) + "/"
					+ String.valueOf(YXPlanTotalCount);
			plan_index.setText(indexCaption);
			// 计划是新增还是修改
			TextView plan_neworedit = (TextView) findViewById(R.id.plan_neworedit);

			if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
				currDJPlancompletedYN = currentDJPlan
						.JudgePlanIsCompleted(DateTimeHelper.GetDateTimeNow());
			} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
				currDJPlancompletedYN = currentDJPlan
						.JudgePlanIsCompletedForCASEXJ(Dianjian_Main.this);
			}
			if (currDJPlancompletedYN) {
				plan_neworedit.setText("改");
				plan_neworedit.setTag(1);
			} else {
				plan_neworedit.setText("");
				plan_neworedit.setTag(0);
			}
			if (AppContext.DJSpecCaseFlag == 1) {
				plan_neworedit.setText("特巡");
				plan_neworedit.setTag(0);
			}
			// 计划剩余数量
			TextView plan_remained = (TextView) findViewById(R.id.plan_remained);
			int completeNum = DJIDPosHelper.GetCompleteNum(current_IDPos_ID);

			int srNotNeedDoNum = DJIDPosHelper
					.GetSKNotNeedDoNum(current_IDPos_ID);

			if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
				if (AppContext.DJSpecCaseFlag == 1) {
					plan_remained.setText("");
				} else {
					plan_remained.setText(String.valueOf((YXPlanTotalCount
							- completeNum - srNotNeedDoNum))
							+ "(未检)");
				}
			} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
				plan_remained.setText("");
			}
		}
	}

	*//**
	 * 加载结果到UI界面
	 *//*
	private void loadDJResultToUI(boolean editYN) {
		if (editYN) {
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					if (loading != null)
						loading.dismiss();
					if (msg.what == 1) {
						bindingDJResultData();
					}
				}
			};

			this.loadDJResultThread(false);
		} else {
			// 清理图片列表
			DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djplanimage);
			plan_picture_gallery.setAdapter(imageAdapter);
			picNumTextView.setText("未拍照");
			// 清理录音列表
			DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djplanimage);
			plan_audio_gallery.setAdapter(audioAdapter);
			soundNumTextView.setText("未录音");
			edit_plan_result_jl.setText("");
			txt_plan_result_others.setText("");
			planmemo_editor.setText("");
			if (currentDJPlan.getDJPlan().getDataType_CD()
					.equals(AppConst.DJPLAN_DATACODE_GC)) {
				String Result_TX = "正常";
				String expYN = "0";
				int alarmlevelid = 0;
				String alarmName = "正常";
				if (AppContext.dataCodeBuffer == null
						|| AppContext.dataCodeBuffer.size() <= 0) {
					Result_TX = "正常";
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
							Result_TX = "正常";
						}
					else {
						Result_TX = "正常";
					}
				}

				plan_result_almlevel.setText(alarmName);
				plan_result_almlevel.setTag(alarmlevelid);
				txt_plan_result_others.setText(Result_TX);
				txt_plan_result_others.setTag(expYN);
				if (expYN.equals("0"))
					plan_result_almlevel.setTextColor(Color.BLACK);
				else
					plan_result_almlevel.setTextColor(Color.RED);
			}

			 如果设置为默认填入上次结果，并且存在上次结果，则填入到输入框内 
			if (appContext.isLastResult()) {
				if (!StringUtils.isEmpty(currentDJPlan.getDJPlan()
						.getLastResult_TX())) {
					 记录类 
					if (currentDJPlan.getDJPlan().getDataType_CD()
							.equals(AppConst.DJPLAN_DATACODE_JL))
						edit_plan_result_jl.setText(currentDJPlan.getDJPlan()
								.getLastResult_TX());
					 观察类 
					else if (currentDJPlan.getDJPlan().getDataType_CD()
							.equals(AppConst.DJPLAN_DATACODE_GC)) {
						txt_plan_result_others.setText(currentDJPlan
								.getDJPlan().getLastResult_TX());
						getLastAlarmLevelByLastResult(txt_plan_result_others
								.getText().toString());
					}
					 测温,测振,测速 类 
					else {
						txt_plan_result_others.setText(currentDJPlan
								.getDJPlan().getLastResult_TX());
						checkResult(Double.parseDouble(txt_plan_result_others
								.getText().toString()));
					}
				}
			}
		}
	}

	*//**
	 * 加载ID位置状态数据的线程
	 *//*
	private void loadDJResultThread(final boolean isRefresh) {
		loading = new LoadingDialog(this);
		loading.setCancelable(false);
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				loadDJResultData();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	*//**
	 * 当前计划下结果数据到内存
	 *//*
	private void loadDJResultData() {
		// 加载数据
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			currDJResultActive = DJResultHelper.GetIntance().getOneDJResult(
					Dianjian_Main.this,
					AppContext.GetCurrLineID(),
					currentDJPlan.getDJPlan().getDJ_Plan_ID(),
					DateTimeHelper.DateToString(currentDJPlan.GetCycle()
							.getTaskBegin()),
					DateTimeHelper.DateToString(currentDJPlan.GetCycle()
							.getTaskEnd()));
		} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
			currDJResultActive = DJResultHelper.GetIntance().getOneDJResult(
					Dianjian_Main.this, AppContext.GetCurrLineID(),
					currentDJPlan.getDJPlan().getDJ_Plan_ID(),
					AppConst.PlanTimeIDStr);
		}
		if (currDJResultActive != null)
			planFilesBuffer = DJResultHelper.GetIntance()
					.getResultFileListByDJRes(Dianjian_Main.this,
							AppContext.GetCurrLineID(),
							currentDJPlan.getDJPlan().getDJ_Plan_ID(),
							currDJResultActive);
	}

	*//**
	 * 加载结果
	 *//*
	private void bindingDJResultData() {
		// 绑定结果数据
		if (currDJResultActive != null) {
			if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {// 记录类
				// plan_result_jl_notice.setText("");
				edit_plan_result_jl.setText(currDJResultActive.getResult_TX());
				edit_plan_result_jl
						.setTag(currDJResultActive.getException_YN());
				plan_result_almlevel.setText(AppContext.almLevelBuffer.get(
						currDJResultActive.getAlarmLevel_ID()).getName_TX());
				plan_result_almlevel.setTag(currDJResultActive
						.getAlarmLevel_ID());
				if (currDJResultActive.getException_YN().equals("1"))
					plan_result_almlevel.setTextColor(Color.RED);
				else
					plan_result_almlevel.setTextColor(Color.BLACK);
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
				plan_result_almlevel.setText(AppContext.almLevelBuffer.get(
						currDJResultActive.getAlarmLevel_ID()).getName_TX());
				plan_result_almlevel.setTag(currDJResultActive
						.getAlarmLevel_ID());
				if (currDJResultActive.getException_YN().equals("1"))
					plan_result_almlevel.setTextColor(Color.RED);
				else
					plan_result_almlevel.setTextColor(Color.BLACK);
			} else { // 观察类

				txt_plan_result_others.setText(currDJResultActive
						.getResult_TX());
				txt_plan_result_others.setTag(currDJResultActive
						.getException_YN());
				plan_result_almlevel.setText(AppContext.almLevelBuffer.get(
						currDJResultActive.getAlarmLevel_ID()).getName_TX());
				plan_result_almlevel.setTag(currDJResultActive
						.getAlarmLevel_ID());
				if (currDJResultActive.getException_YN().equals("1"))
					plan_result_almlevel.setTextColor(Color.RED);
				else
					plan_result_almlevel.setTextColor(Color.BLACK);
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
			plan_result_almlevel.setText("");
		}
		// 绑定结果附件
		reflashFilesList();
	}

	*//**
	 * 刷新文件预览栏（照片、录音）
	 *//*
	private void reflashFilesList() {
		if (planFilesBuffer != null && planFilesBuffer.size() > 0) {
			if (planFilesBuffer.containsKey(0)
					&& planFilesBuffer.get(0).size() > 0) {
				DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
						planFilesBuffer.get(REQCODE_PICTURE),
						R.layout.listitem_djplanimage);
				plan_picture_gallery.setAdapter(imageAdapter);
				picNumTextView.setText("共有"
						+ planFilesBuffer.get(REQCODE_PICTURE).size() + "张照片");
			} else {
				DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
						new ArrayList<DJ_PhotoByResult>(),
						R.layout.listitem_djplanimage);
				plan_picture_gallery.setAdapter(imageAdapter);
				picNumTextView.setText("未拍照");
			}
			if (planFilesBuffer.containsKey(REQCODE_SOUND)
					&& planFilesBuffer.get(REQCODE_SOUND).size() > 0) {
				DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
						planFilesBuffer.get(REQCODE_SOUND),
						R.layout.listitem_djplanimage);
				plan_audio_gallery.setAdapter(audioAdapter);
				soundNumTextView.setText("共有"
						+ planFilesBuffer.get(REQCODE_SOUND).size() + "段录音");
			} else {
				DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
						new ArrayList<DJ_PhotoByResult>(),
						R.layout.listitem_djplanimage);
				plan_audio_gallery.setAdapter(audioAdapter);
				soundNumTextView.setText("未录音");
			}
		} else {
			// 清理图片列表
			DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djplanimage);
			plan_picture_gallery.setAdapter(imageAdapter);
			picNumTextView.setText("未拍照");
			// 清理录音列表
			DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djplanimage);
			plan_audio_gallery.setAdapter(audioAdapter);
			soundNumTextView.setText("未录音");
		}
	}

	*//**
	 * 显示计划明细
	 *//*
	private void showPlanDetail() {
		String titleMes = "计划详情";
		String splitStr = "-------------------------";
		// 计划描述
		String planContentString = "计划描述:\n"
				+ currentDJPlan.getDJPlan().getPlanDesc_TX() + "\n" + splitStr
				+ "\n";
		String planStandardString = "判断标准:\n"
				+ currentDJPlan.getDJPlan().getESTStandard_TX() + "\n"
				+ splitStr + "\n";
		// 周期
		String cycleString = "周期:\n";
		if (currentDJPlan.GetCycle() != null)
			cycleString += currentDJPlan.GetCycle().getDJCycle().getName_TX()
					+ "\n" + splitStr + "\n";
		else
			cycleString += splitStr + "\n";
		// 检查方法
		String checkMethodString = "检查方法:\n" + getCheckMethod() + "\n"
				+ splitStr + "\n";
		// 典型值
		String standardValueString = "典型值:\n"
				+ (StringUtils.isEmpty(currentDJPlan.getDJPlan()
						.getStandardValue_TX()) ? "--" : currentDJPlan
						.getDJPlan().getStandardValue_TX()) + "\n" + splitStr
				+ "\n";
		// 单位
		String unitString = "单位:\n"
				+ (StringUtils.isEmpty(currentDJPlan.getDJPlan()
						.getMetricUnit_TX()) ? "--" : currentDJPlan.getDJPlan()
						.getMetricUnit_TX()) + "\n" + splitStr + "\n";

		// 量程
		String lcString = "";
		String pLower = StringUtils.isEmpty(currentDJPlan.getDJPlan()
				.getParmLowerLimit_TX()) ? "--" : currentDJPlan.getDJPlan()
				.getParmLowerLimit_TX();
		String pUpper = StringUtils.isEmpty(currentDJPlan.getDJPlan()
				.getParmUpperLimit_TX()) ? "--" : currentDJPlan.getDJPlan()
				.getParmUpperLimit_TX();
		if (!StringUtils.isEmpty(pLower) || !StringUtils.isEmpty(pUpper))
			lcString = "量程:\n" + pLower + "~" + pUpper + "\n" + splitStr + "\n";
		// 报警类型
		String almType = "";
		if (currentDJPlan.getDJPlan().getAlarmType_ID() != null) {
			String overlapNR = "";
			if (currentDJPlan.getDJPlan().getAlarmType_ID() == 0)
				almType = "超上限报警";
			else if (currentDJPlan.getDJPlan().getAlarmType_ID() == 1)
				almType = "超下限报警";
			else if (currentDJPlan.getDJPlan().getAlarmType_ID() == 2)
				almType = "窗内报警";
			else if (currentDJPlan.getDJPlan().getAlarmType_ID() == 3)
				almType = "窗外报警";
			else {
				if (currentDJPlan.getDJPlan().getOverlap_NR() != null)
					overlapNR = String.valueOf(currentDJPlan.getDJPlan()
							.getOverlap_NR());
				almType = "差值报警(差值:" + overlapNR + ")";
			}
			almType = "报警类型:\n" + almType + "\n" + splitStr + "\n";
		}
		// 报警上下限
		String almLimitString = "报警上下限:\n";
		String upperString1 = "--", downString1 = "--";
		if (!StringUtils.isEmpty(currentDJPlan.getDJPlan().getUpperLimit1_TX())) {
			upperString1 = currentDJPlan.getDJPlan().getUpperLimit1_TX();
		}
		if (!StringUtils.isEmpty(currentDJPlan.getDJPlan().getLowerLimit1_TX())) {
			downString1 = currentDJPlan.getDJPlan().getLowerLimit1_TX();
		}
		almLimitString += " 上限:" + upperString1 + "        下限:" + downString1
				+ "\n";

		String upperString2 = "--", downString2 = "--";
		if (!StringUtils.isEmpty(currentDJPlan.getDJPlan().getUpperLimit2_TX())) {
			upperString2 = currentDJPlan.getDJPlan().getUpperLimit2_TX();
		}
		if (!StringUtils.isEmpty(currentDJPlan.getDJPlan().getLowerLimit2_TX())) {
			downString2 = currentDJPlan.getDJPlan().getLowerLimit2_TX();
		}
		almLimitString += " 上限:" + upperString2 + "        下限:" + downString2
				+ "\n";

		String upperString3 = "--", downString3 = "--";
		if (!StringUtils.isEmpty(currentDJPlan.getDJPlan().getUpperLimit3_TX())) {
			upperString3 = currentDJPlan.getDJPlan().getUpperLimit3_TX();
		}
		if (!StringUtils.isEmpty(currentDJPlan.getDJPlan().getLowerLimit3_TX())) {
			downString3 = currentDJPlan.getDJPlan().getLowerLimit3_TX();
		}
		almLimitString += " 上限:" + upperString3 + "        下限:" + downString3
				+ "\n";

		String upperString4 = "--", downString4 = "--";
		if (!StringUtils.isEmpty(currentDJPlan.getDJPlan().getUpperLimit4_TX())) {
			upperString4 = currentDJPlan.getDJPlan().getUpperLimit4_TX();
		}
		if (!StringUtils.isEmpty(currentDJPlan.getDJPlan().getLowerLimit4_TX())) {
			downString4 = currentDJPlan.getDJPlan().getLowerLimit4_TX();
		}
		almLimitString += " 上限:" + upperString4 + "        下限:" + downString4
				+ "\n";
		String textMes = planContentString + planStandardString + cycleString
				+ checkMethodString + standardValueString + unitString
				+ lcString + almType + almLimitString;
		final SimpleTextDialog _dialog = new SimpleTextDialog(
				Dianjian_Main.this, titleMes, textMes);
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
	}

	*//**
	 * 解析检查方法
	 * 
	 * @return
	 *//*
	private String getCheckMethod() {
		String res = "";
		int checkmethod = currentDJPlan.getDJPlan().getCheckMethod();
		String temp = String.valueOf(checkmethod);
		int length = temp.length();
		if (length < 8) {
			for (int i = 0; i < 8 - length; i++) {
				temp = "0" + temp;
			}
		}// { 目视、手摸、解体、其它、精密、嗅觉、敲打、听音 };
		if (temp.substring(0, 1).equals("1"))
			res += "目视 ";
		if (temp.substring(1, 2).equals("1"))
			res += "手摸 ";
		if (temp.substring(2, 3).equals("1"))
			res += "解体 ";
		if (temp.substring(3, 4).equals("1"))
			res += "其它 ";
		if (temp.substring(4, 5).equals("1"))
			res += "精密 ";
		if (temp.substring(5, 6).equals("1"))
			res += "嗅觉 ";
		if (temp.substring(6, 7).equals("1"))
			res += "敲打 ";
		if (temp.substring(7, 8).equals("1"))
			res += "听音 ";
		return res;
	}

	private String alarmName = "", m_ExceptionYN = "0", emsg = "";
	private Integer m_ExLevelCD = 0;

	*//**
	 * 判断结果是否超出范围及是否为异常数据
	 * 
	 * @param result
	 *//*
	private void checkResult(double result) {
		alarmName = "";
		m_ExceptionYN = "0";
		emsg = "";
		m_ExLevelCD = 0;
		if (!IsOverRange(result)) {
			plan_result_almlevel.setText("超出量程范围,请重新输入");
			plan_result_almlevel.setTextColor(Color.RED);
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
			
			plan_result_almlevel.setText(emsg);
			plan_result_almlevel.setTag(m_ExLevelCD.toString());
			plan_result_almlevel.setTextColor(Color.RED);
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
			plan_result_almlevel.setText("正常");
			plan_result_almlevel.setTag("0");
			plan_result_almlevel.setTextColor(Color.BLACK);
		}
		
		
//		else if (IsException(result)) {
//			// 记录类
//			if (currentDJPlan.getDJPlan().getDataType_CD()
//					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL))
//				edit_plan_result_jl.setTag(m_ExceptionYN);
//			// 测温，测振，测速
//			else if (currentDJPlan.getDJPlan().getDataType_CD()
//					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)
//					|| currentDJPlan.getDJPlan().getDataType_CD()
//							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
//					|| currentDJPlan.getDJPlan().getDataType_CD()
//							.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CS))
//				txt_plan_result_others.setTag(m_ExceptionYN);
//
//			if (m_ExceptionYN.equals("1")) {
//				plan_result_almlevel.setText(emsg);
//				plan_result_almlevel.setTag(m_ExLevelCD.toString());
//				plan_result_almlevel.setTextColor(Color.RED);
//				AppContext.voiceConvertService.Speaking(emsg);
//			} else {
//				plan_result_almlevel.setText("正常");
//				plan_result_almlevel.setTag("0");
//				plan_result_almlevel.setTextColor(Color.BLACK);
//			}
//		} else {
//			plan_result_almlevel.setText("正常");
//			plan_result_almlevel.setTag("0");
//			plan_result_almlevel.setTextColor(Color.BLACK);
//		}
	}

	private void GetAlarmName(int warninglevel) {
		String tempName = "";
		switch (warninglevel) {
		case 4:
			tempName = AppConst.AlarmName_Dangerous;
			break;
		case 3:
			tempName = AppConst.AlarmName_Alert;
			break;
		case 2:
			tempName = AppConst.AlarmName_Warning;
			break;
		case 1:
			tempName = AppConst.AlarmName_Pre_Alarm;
			break;
		}
		alarmName = StringUtils.isEmpty(AppContext.almLevelBuffer.get(
				warninglevel).getName_TX()) ? tempName
				: AppContext.almLevelBuffer.get(warninglevel).getName_TX();

		m_ExceptionYN = "1";
		m_ExLevelCD = warninglevel;
		emsg = "异常数据（级别:" + alarmName + "）";
	}

	private class MyGestureListener extends GestureListener {
		public MyGestureListener(Context context) {
			super(context);
		}

		@Override
		public boolean left() {
			if (appContext.isScroll()) {
				switch (CURRENT_MODE) {
				case PopMenu_EDIT:
					loadNextDJPlan();
					break;
				case PopMenu_CHECK:
					loadNextUnDoDJPlan();
					break;
				}

			}
			return super.left();
		}

		@Override
		public boolean right() {
			if (appContext.isScroll()) {
				switch (CURRENT_MODE) {
				case PopMenu_EDIT:
					loadPreDJPlan();
					break;
				case PopMenu_CHECK:
					loadPreUnDoDJPlan();
					break;
				}
			}
			return super.right();
		}
	}

	*//**
	 * 量程判断
	 * 
	 * @param reult
	 * @return
	 *//*
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
			emsg = "异常数据（与上次的差值过大,级别:" + alarmName + "）";
			return true;
		}
		return false;
	}

	*//**
	 * 更加报警窗口访问判断报警等级
	 * 
	 * @param result
	 * @param alarmName
	 * @param m_ExceptionYN
	 * @param m_ExLevelCD
	 * @param emsg
	 * @return
	 *//*
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

	*//**
	 * 根据ID获取结果选项对象
	 * 
	 * @param gID
	 * @return
	 *//*
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

	*//**
	 * 结果选项
	 *//*
	private void selectResItem() {
		if (currentDJPlan.getDJPlan().getDataCodeGroup_ID() <= 0)
			return;
		String selfTitleContent = "计划内容："
				+ currentDJPlan.getDJPlan().getPlanDesc_TX();
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
				Dianjian_Main.this, new SimpleMultiListViewDialogListener() {

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
										"请选择结果选项");
								return;
							}
							String resString = _result.substring(1);
							if (_expTag.equals("1"))
								plan_result_almlevel.setTextColor(Color.RED);
							else {
								plan_result_almlevel.setTextColor(Color.BLACK);
							}
							if (AppContext.almLevelBuffer != null
									&& AppContext.almLevelBuffer.size() > 0) {
								plan_result_almlevel
										.setText(AppContext.almLevelBuffer.get(
												Integer.parseInt(_almID))
												.getName_TX());// 报警名称
								plan_result_almlevel.setTag(Integer
										.parseInt(_almID));
							} else {
								plan_result_almlevel.setText("正常");// 报警名称
								plan_result_almlevel.setTag(0);
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

	*//**
	 * 根据上次结果获取上次报警等级(观察类计划)
	 * 
	 * @param lastResult
	 *//*
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
				plan_result_almlevel.setText(AppContext.almLevelBuffer.get(
						alarmlevelid).getName_TX());// 报警名称
				plan_result_almlevel.setTag(alarmlevelid);
				if (expYN.equals("0"))
					plan_result_almlevel.setTextColor(Color.BLACK);
				else
					plan_result_almlevel.setTextColor(Color.RED);
			} else {
				plan_result_almlevel.setText("正常");
				plan_result_almlevel.setTag(0);
			}
		} else {
			txt_plan_result_others.setTag("0");
			plan_result_almlevel.setText("正常");
			plan_result_almlevel.setTag(0);
		}
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

	DianJianSRDialog srDialog;

	*//**
	 * 启停点
	 * 
	 * @param adjline
	 *//*
	private void showSR(boolean autoYN, int srno) {
		if (srDialog != null && srDialog.isShowing())
			srDialog.dismiss();
		srDialog = new DianJianSRDialog(Dianjian_Main.this,
				new PriorityListener() {

					@Override
					public void refreshPriorityUI(boolean autoYn) {
						// 如果启停点是手动进入修改的，当启停点修改后，重新计算计划并跳至首条未做计划
						if (!autoYn) {
							LoadFirstDJPLanWithOutCompleted();
						}
						// 如果启停点下该计划不做，则加载下一条
						else if (!currentDJPlan.JudgePlanBySrPoint()) {
							switch (CURRENT_MODE) {
							case PopMenu_EDIT:
								loadNextDJPlan();
								break;
							case PopMenu_CHECK:
								loadNextUnDoDJPlan();
								break;
							}
						}
					}

					@Override
					public void comeBackToDianjianMain(boolean autoYn) {
						// 受主控控制并且还未设置过主控
						if (currentDJPlan.JudgePlanIsLKControl()
								&& (!currentDJPlan.getLKDoYn())
								&& (!StringUtils.isEmpty(currentDJPlan
										.getDJPlan().getMustCheck_YN()))
								&& (currentDJPlan.getDJPlan().getMustCheck_YN()
										.equals("0"))) {
							List<DJPlan> lkPlanList = DJPlanHelper.GetIntance()
									.GetPlanListByControlPoint(
											currentDJPlan.getDJPlan()
													.getLKPoint_ID(),
											current_IDPos_ID);
							// 有效主控点下计划列表，经过启停点处理
							ArrayList<DJPlan> yxplanList = new ArrayList<DJPlan>();
							for (DJPlan _planInfo : lkPlanList) {
								yxplanList.add(_planInfo);
							}
							if (yxplanList != null && yxplanList.size() > 0)
								selectLK(currentDJPlan.getDJPlan()
										.getLKPoint_ID(), yxplanList);
						}
					}
				}, current_IDPos_ID, mDJPlan, autoYN, srno);
		srDialog.show();
	}

	*//**
	 * 主控点
	 * 
	 * @param adjline
	 *//*
	private void selectLK(final String lkID, final ArrayList<DJPlan> yxplanList) {
		DJ_ControlPoint controlPoint = DJPlanHelper.GetIntance().getLKbyID(
				Dianjian_Main.this, lkID);
		if (controlPoint != null && yxplanList != null && yxplanList.size() > 0) {
			final DianJianLKDialog lkDialog = new DianJianLKDialog(
					Dianjian_Main.this, new LKListener() {

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
							if (mDJPlan != null && mDJPlan.size() > 0) {
								mHandler = new Handler() {
									public void handleMessage(Message msg) {
										if (saving != null)
											saving.dismiss();
										if (msg.what == 1) {
											 循环保存完主控点计划后，更新钮扣到位结束时间 
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
											loadNextUnDoDJPlan();

										} else {
											UIHelper.ToastMessage(
													getApplication(),
													((Exception) (msg.obj))
															.getMessage());
										}
										dialog.dismiss();
									}
								};

								saveDJResultForLKThread(mDJPlan);
							}
						}

						@Override
						public void btnCancel(DialogInterface dialog,
								List<DJPlan> mDJPlan) {
							// TODO Auto-generated method stub

						}
					}, yxplanList);
			lkDialog.setTitle("主控点-" + controlPoint.getName_TX());
			lkDialog.show();
		}
	}

	private void saveDJResultForLKThread(final List<DJPlan> mDJPlan) {
		saving = new LoadingDialog(this);
		saving.setLoadText("保存中...");
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

	// [start] 保存数据
	*//**
	 * 保存结果(保存点检结果、更新点检计划)
	 * 
	 * @param costTime
	 *//*
	private boolean saveDJResult(String completeTime) {
		try {
			if (currDJPlancompletedYN && currDJResultActive == null) {
				UIHelper.ToastMessage(getApplication(),
						R.string.plan_save_notice1);
				return false;
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
				if (!((Boolean) btn_start_others.getTag())) {
					UIHelper.ToastMessage(getApplication(),
							R.string.plan_cl_notice1);
					return false;
				}
			} else { // 观察类
				Result_TX = String.valueOf(txt_plan_result_others.getText());
			}
			String Memo_TX = String.valueOf(planmemo_editor.getText());
			if (StringUtils.isEmpty(Result_TX)) {
				UIHelper.ToastMessage(getApplication(),
						R.string.plan_stringEmpty_result);
				return false;
			} else {
				if (currentDJPlan.getDJPlan().getDataType_CD()
						.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {
					if (Result_TX.equals(".") || Result_TX.equals("-")
							|| Result_TX.equals("+")) {
						UIHelper.ToastMessage(getApplication(),
								R.string.plan_wrongFormat_result);
						return false;
					}
					if (!IsOverRange(Double.valueOf(Result_TX))) {
						UIHelper.ToastMessage(getApplication(), "输入值不在量程范围");
						return false;
					}
				}
			}
			if (Memo_TX.length() > 80) {
				UIHelper.ToastMessage(getApplication(),
						R.string.plan_most_memo_length);
				return false;
			}
			DJ_ResultActive djResult = new DJ_ResultActive();
			if (plan_result_almlevel.getTag() != null) {
				Integer almID = Integer.parseInt((String
						.valueOf(plan_result_almlevel.getTag())));
				djResult.setAlarmLevel_ID(almID);
			} else {
				djResult.setAlarmLevel_ID(0);
			}
			djResult.setAppUser_CD(AppContext.getUserCD());
			djResult.setAppUserName_TX(AppContext.getUserName());

			djResult.setCompleteTime_DT(completeTime);
			if (AppContext.DJSpecCaseFlag == 0)
				currentDJPlan.getDJPlan().setLastComplete_DT(completeTime);
			else {
				currentDJPlan.getDJPlan().setLastComplete_DT(null);
			}

			djResult.setDataFlag_CD("M");

			djResult.setDJ_Plan_ID(currentDJPlan.getDJPlan().getDJ_Plan_ID());
			if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {
				if (edit_plan_result_jl.getTag() != null) {
					String expYN = String.valueOf(edit_plan_result_jl.getTag());					
//					almYN = StringUtils.isEmpty(almYN) ? "0" : "1";
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
//					almYN = StringUtils.isEmpty(almYN) ? "0" : almYN;
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

			djResult.setLastResult_TX(currentDJPlan.getDJPlan()
					.getLastResult_TX());
			djResult.setMemo_TX(Memo_TX);
			djResult.setMObjectStateName_TX("");
			// 巡点检根据跨天计算查询日期
			if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
				if (AppContext.DJSpecCaseFlag == 1) {
					djResult.setQuery_DT(DateTimeHelper.getDateTimeNow());
				} else {
					djResult.setQuery_DT(DateTimeHelper
							.DateToString(currentDJPlan
									.GetQueryDate(DateTimeHelper
											.GetDateTimeNow())));
				}
			} else
				djResult.setQuery_DT(DateTimeHelper.getDateTimeNow());

			djResult.setResult_TX(Result_TX);
			currentDJPlan.getDJPlan().setLastResult_TX(Result_TX);
			djResult.setSpecCase_TX(currentDJPlan.getSpecCaseNames());
			djResult.setSpecCase_YN(AppContext.DJSpecCaseFlag == 1 ? "1" : "0");
			djResult.setTime_NR(costTime);
			// 条件巡检
			if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
				if (!StringUtils.isEmpty(AppConst.PlanTimeIDStr)) {
					djResult.setTransInfo_TX(currentDJPlan.getDJPlan()
							.getTransInfo_TX() + "|" + AppConst.PlanTimeIDStr);
				}
			} else {
				djResult.setTransInfo_TX(currentDJPlan.getDJPlan()
						.getTransInfo_TX());
			}
			String shiftname = "";
			String shiftGroupName = "";
			// 巡点检计算班次，职别
			if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()
					|| AppContext.getCurrLineType() == AppConst.LineType.DJPC.getLineType()) {
				shiftname = DJShiftHelper.GetIntance().getShiftNameByCD(this,
						currentDJPlan.GetCycle().getCurShiftNo());
				shiftGroupName = DJShiftHelper.GetIntance()
						.computeNowShiftGroupName(this,
								DateTimeHelper.GetDateTimeNow(),
								currentDJPlan.GetCycle().getCurShiftNo());
			}
			djResult.setShiftName_TX(shiftname);
			djResult.setShiftGroupName_TX(shiftGroupName);

			 测振相关赋值 
			if (currentDJPlan.getDJPlan().getDataType_CD()
					.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)) {
				djResult.setData8K_TX(timewave);
				djResult.setDataLen(datalen);
				if (currentDJPlan.getDJPlan()
						.getMaxFreq_NR() == null) {
					djResult.setRate((int)(1000 * 2.56));
				} else {
					djResult.setRate((int) (currentDJPlan.getDJPlan()
							.getMaxFreq_NR() * 2.56));
				}
				djResult.setRatio1_NR(true);
				djResult.setRatio_NR(true);
				if (currentDJPlan.getDJPlan().getWindowType_NR() == null) {
					djResult.setVibParam_TX(currentDJPlan.getDJPlan()
							.getZhenDong_Type()
							+ ";2;0");
				} else {
					djResult.setVibParam_TX(currentDJPlan.getDJPlan()
							.getZhenDong_Type()
							+ ";"
							+ currentDJPlan.getDJPlan().getWindowType_NR() + ";0");
				}
				djResult.setFeatureValue_TX(featurevalue);
				djResult.setFFTData_TX(fftdata);
			}

			long row = DJResultHelper.GetIntance().InsertDJPlanResult(
					Dianjian_Main.this, djResult, null,planFilesBuffer,
					currentDJPlan);
			if (row > 0) {
				DJPlanHelper.GetIntance().updateDJPlan(Dianjian_Main.this,
						currentDJPlan.getDJPlan());
				return true;
			}
			UIHelper.ToastMessage(Dianjian_Main.this, "保存失败");
			return false;
		} catch (Exception ex) {
			UIHelper.ToastMessage(getApplication(), ex.getMessage());
			return false;
		}
	}

	*//**
	 * 批量保存主控点下结果
	 *//*
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
						Result_TX = "正常";
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
								Result_TX = "正常";
							}
						else {
							Result_TX = "正常";
						}
					}

				}
				DJ_ResultActive djResult = new DJ_ResultActive();
				djResult.setAlarmLevel_ID(0);
				djResult.setAppUser_CD(AppContext.getUserCD());
				djResult.setAppUserName_TX(AppContext.getUserName());
				String completeTime = DateTimeHelper.getDateTimeNow();
				djResult.setCompleteTime_DT(completeTime);
				_djPlan.getDJPlan().setLastComplete_DT(completeTime);
				djResult.setData8K_TX(null);
				djResult.setDataFlag_CD("A");
				djResult.setDataLen(0);
				djResult.setDJ_Plan_ID(_djPlan.getDJPlan().getDJ_Plan_ID());
				djResult.setException_YN(expYN);
				djResult.setFeatureValue_TX("");
				djResult.setFFTData_TX(null);
				djResult.setLastResult_TX(Result_TX);
				djResult.setMemo_TX("");
				djResult.setMObjectStateName_TX("");
				djResult.setQuery_DT(DateTimeHelper.DateToString(_djPlan
						.GetQueryDate(DateTimeHelper.GetDateTimeNow())));
				djResult.setRate(0);
				djResult.setRatio1_NR(false);
				djResult.setRatio_NR(false);
				djResult.setResult_TX(Result_TX);
				_djPlan.getDJPlan().setLastResult_TX(Result_TX);
				String shiftname = DJShiftHelper.GetIntance().getShiftNameByCD(
						this, currentDJPlan.GetCycle().getCurShiftNo());
				String shiftGroupName = DJShiftHelper.GetIntance()
						.computeNowShiftGroupName(this,
								DateTimeHelper.GetDateTimeNow(),
								currentDJPlan.GetCycle().getCurShiftNo());
				djResult.setShiftName_TX(shiftname);
				djResult.setShiftGroupName_TX(shiftGroupName);
				djResult.setSpecCase_TX(_djPlan.getSpecCaseNames());
				djResult.setSpecCase_YN(AppContext.DJSpecCaseFlag == 1 ? "1"
						: "0");
				djResult.setTime_NR(costTime);
				djResult.setTransInfo_TX(_djPlan.getDJPlan().getTransInfo_TX());
				djResult.setVibParam_TX("");
				long row = DJResultHelper.GetIntance().InsertDJPlanResult(
						Dianjian_Main.this, djResult, null,null, null);
				if (row > 0) {
					DJPlanHelper.GetIntance().updateDJPlan(Dianjian_Main.this,
							_djPlan.getDJPlan());
				}

				_djPlan.getDJPlan().setLastComplete_DT(
						DateTimeHelper.getDateTimeNow());

			}
			DJ_IDPos _curridpos = getIDPosInfoByIDPosID(Integer
					.valueOf(current_IDPos_ID));
			if (_curridpos != null) {
				int completeNum = DJIDPosHelper
						.GetCompleteNum(current_IDPos_ID);
				String countString = "";
				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					countString = AppContext.DJSpecCaseFlag == 1 ? "特巡条目："
							+ YXPlanTotalCount : completeNum + "/"
							+ YXPlanTotalCount;
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					countString = "条目：" + YXPlanTotalCount;
				}
				_curridpos.setPlanCount(countString);
			}
		} catch (Exception ex) {
			UIHelper.ToastMessage(getApplication(), ex.getMessage());
		}
	}

	private boolean checkMaxFileNum(int type) {
		boolean result = true;
		if (planFilesBuffer != null && planFilesBuffer.size() > 0) {
			switch (type) {
			case REQCODE_PICTURE:
				if (planFilesBuffer.containsKey(type)
						&& planFilesBuffer.get(type).size() >= 10) {
					UIHelper.ToastMessage(Dianjian_Main.this, "最多只能拍摄10张照片");
					result = false;
				}
				break;
			case REQCODE_SOUND:
				if (planFilesBuffer.containsKey(type)
						&& planFilesBuffer.get(type).size() >= 5) {
					UIHelper.ToastMessage(Dianjian_Main.this, "最多只能录制5个录音");
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
	
	*//**
	 * 钮扣最小用时检查
	 * @return
	 *//*
	private String checkIDPosMinCost() {
		try {
			String msg = "";
			int days = (int)(DateTimeHelper.getDistanceTimes(AppContext.getCurIDPos().getLastArrivedTime_DT()
					, DateTimeHelper.getDateTimeNow())[0]);
			int hours = (int)(DateTimeHelper.getDistanceTimes(AppContext.getCurIDPos().getLastArrivedTime_DT()
					, DateTimeHelper.getDateTimeNow())[1]);
			int mins = (int)(DateTimeHelper.getDistanceTimes(AppContext.getCurIDPos().getLastArrivedTime_DT()
					, DateTimeHelper.getDateTimeNow())[2]);
			int seconds = (int)(DateTimeHelper.getDistanceTimes(AppContext.getCurIDPos().getLastArrivedTime_DT()
					, DateTimeHelper.getDateTimeNow())[3]);
			int getCostSeconds = days * 24 * 60 * 60 + hours * 60 * 60 + mins * 60 + seconds;
			
			if (getCostSeconds < AppContext.getCurIDPos().getCostDateLimit_NR() * 60) {
				int remain = AppContext.getCurIDPos().getCostDateLimit_NR() * 60 - getCostSeconds;
				int remainSeconds = remain % 60;
				int remainMins = remain / 60;				
				if (remainMins <= 0) {
					msg = "该位置区域的检查时间必须满" + AppContext.getCurIDPos().getCostDateLimit_NR() + "分钟才能返回,剩余"
						    + remainSeconds + "秒,请继续检查";
				} else {
					msg = "该位置区域的检查时间必须满" + AppContext.getCurIDPos().getCostDateLimit_NR() + "分钟才能返回,剩余"
						    + remainMins + "分" + remainSeconds + "秒,请继续检查";
				}
				//UIHelper.ToastMessage(Dianjian_Main.this, msg);
				return msg;
			} else
				return "";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// [end]

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目");
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final String actionFileName;// 要操作的文件名
		View v = null;
		if (item.getGroupId() == 0) {
			v = (View) plan_picture_gallery.getSelectedView();// .getChildAt(info.position);
		} else {
			v = (View) plan_audio_gallery.getSelectedView();// .getChildAt(info.position);
		}
		if (v == null) {
			// UIHelper.ToastMessage(DianjianMain.this,
			// String.valueOf(info.position));
			return false;
		}
		ImageView aImage = (ImageView) v
				.findViewById(R.id.planimage_listitem_icon);
		if (aImage == null) {
			// UIHelper.ToastMessage(DianjianMain.this,
			// "ImageView is not find ");
			return false;
		}
		final DJ_PhotoByResult _photoByResult = (DJ_PhotoByResult) aImage
				.getTag();
		actionFileName = _photoByResult.getFilePath();

		if (actionFileName == null) {
			return false;
		}
		switch (item.getGroupId()) {
		case 0:// 照片相关
			switch (item.getItemId()) {
			// 变成了单击照片浏览，去掉了长按图片浏览的方式
			case MENUACTION_DELETE:// 删除

				Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
						.setTitle(R.string.plan_del_notice)
						.setMessage(R.string.plan_photo_del)
						// 相当于点击确认按钮
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// 删除关系条目
										if (planFilesBuffer != null
												&& planFilesBuffer
														.containsKey(0)) {
											if (planFilesBuffer.get(0)
													.contains(_photoByResult)) {
												DJResultHelper
														.GetIntance()
														.deleteOneFile(
																getApplication(),
																_photoByResult);
												planFilesBuffer.get(0).remove(
														_photoByResult);
												reflashFilesList();
											}
										}
										// 删除图片文件
										File file = new File(actionFileName);
										file.delete();
										UIHelper.ToastMessage(getApplication(),
												R.string.plan_del_ok);
									}
								})
						// 相当于点击取消按钮
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).create();
				dialog.show();

				break;
			case MENUACTION_RETURN:// 返回
				return false;
			}
			break;
		case 1:// 录音相关
			switch (item.getItemId()) {
			// 变成了单击录音文件回放，去掉了长按的方式
			case MENUACTION_DELETE:// 删除

				Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
						.setTitle(R.string.plan_del_notice)
						.setMessage(R.string.plan_recorder_del)
						// 相当于点击确认按钮
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (planFilesBuffer != null
												&& planFilesBuffer
														.containsKey(REQCODE_SOUND)) {
											if (planFilesBuffer.get(
													REQCODE_SOUND).contains(
													_photoByResult)) {
												DJResultHelper
														.GetIntance()
														.deleteOneFile(
																getApplication(),
																_photoByResult);
												planFilesBuffer.get(
														REQCODE_SOUND).remove(
														_photoByResult);
												reflashFilesList();
											}
										}
										File file = new File(actionFileName);
										file.delete();
										UIHelper.ToastMessage(getApplication(),
												R.string.plan_del_ok);
									}
								})
						// 相当于点击取消按钮
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).create();
				dialog.show();
				break;
			case MENUACTION_RETURN:// 返回
				return false;
			}
			break;
		}

		return super.onContextItemSelected(item);
	}

	private void saverevitionImageSize(String path, int size)
			throws IOException {
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
	}

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
			System.out.println("复制单个文件操作出错");
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

	private void Goback() {
		String msg = checkIDPosMinCost();
		if (!StringUtils.isEmpty(msg)) {
			UIHelper.ToastMessage(Dianjian_Main.this, msg);
			return;
		}
		int cycTotalCount = YXPlanTotalCount;
		int completeNum = 0;
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			completeNum = DJIDPosHelper.GetCompleteNum(current_IDPos_ID);
		} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
			completeNum = DJIDPosHelper.GetCompleteNum(Dianjian_Main.this,
					current_IDPos_ID);
		}
		int srNotNeedDoNum = DJIDPosHelper.GetSKNotNeedDoNum(current_IDPos_ID);
		final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
				- completeNum - srNotNeedDoNum)
				: 0;
		String noticeString = "钮扣：“" + current_IDPos_Name + "”\n" + "计划合计："
				+ cycTotalCount + "条 \n" + "未完成数：" + notcompleteNum + "条 \n"
				+ "无需做计划数：" + srNotNeedDoNum + "条 \n确定要退出吗？";
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			countString = AppContext.DJSpecCaseFlag == 1 ? "特巡条目："
					+ YXPlanTotalCount
					: ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);

			noticeString = AppContext.DJSpecCaseFlag == 1 ? "计划完成情况\n"
					+ "特巡计划合计：" + cycTotalCount + "条\n" + "‘确定’退出，‘取消’继续。"
					: noticeString;
		} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
			countString = ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);

			// noticeString = "计划完成情况\n" + "计划合计：" + cycTotalCount
			// + "条\n" + "无需做计划数："
			// + srNotNeedDoNum + "条\n‘确定’退出，‘取消’继续。";
		}

		final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
				.valueOf(current_IDPos_ID));
		AppContext.voiceConvertService.Speaking(noticeString);
		Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
				.setTitle("退出执行计划")
				.setMessage(noticeString)
				// 相当于点击确认按钮
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (notcompleteNum == 0) {
									if (_myidpos != null) {
										_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
									}
								}
								_myidpos.setPlanCount(countString);

								AppContext.setCurIDPos(null);
								AppManager.getAppManager().finishActivity(
										Dianjian_Main.this);
								AppContext.voiceConvertService.StopSpeaking();
							}
						})
				// 相当于点击取消按钮
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								AppContext.voiceConvertService.StopSpeaking();
								// TODO Auto-generated method stub
							}
						}).setCancelable(false).create();
		dialog.show();
	}

	// [end]

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dianjian);
		appContext = (AppContext) getApplication();
		instance = this;
		@SuppressWarnings("deprecation")
		final Dianjian_Main data = (Dianjian_Main) getLastNonConfigurationInstance();
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

		// 初始化Head
		this.initHeadViewAndMoreBar();
		// 初始化弹出菜单
		this.initQuickActionGrid();
		// 初始化ViewPager
		this.initViewPager();
		// 定位到结果TAB
		changeViewPager(currIndex);
		// 初始化各栏数据
		this.initAllPagerData();

		CURRENT_MODE = PopMenu_EDIT; // 默认为可修改模式
		currentPlanIndex = -1;
		inputMethodCtrl();

		// 根据返回的what值判断，打开什么界面或显示计划
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				switch (msg.what) {
				case 0: {
					UIHelper.ToastMessage(getApplication(), "该钮扣下无可做计划");
					AppContext.setCurIDPos(null);
					AppManager.getAppManager().finishActivity(
							Dianjian_Main.this);
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
					if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
						completeNum = DJIDPosHelper
								.GetCompleteNum(current_IDPos_ID);
					} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
						completeNum = DJIDPosHelper.GetCompleteNum(
								Dianjian_Main.this, current_IDPos_ID);
					}
					final int srNotNeedDoNum = DJIDPosHelper
							.GetSKNotNeedDoNum(current_IDPos_ID);
					final int notcompleteNum = (cycTotalCount - completeNum - srNotNeedDoNum) >= 0 ? (cycTotalCount
							- completeNum - srNotNeedDoNum)
							: 0;
					String noticeString = getString(
							R.string.plan_statistic_notice, cycTotalCount,
							notcompleteNum, srNotNeedDoNum);

					if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
						countString = AppContext.DJSpecCaseFlag == 1 ? "特巡条目："
								+ YXPlanTotalCount
								: ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);

						noticeString = AppContext.DJSpecCaseFlag == 1 ? "计划完成情况\n"
								+ "特巡计划合计："
								+ cycTotalCount
								+ "条\n"
								+ "‘确定’退出，‘取消’继续。"
								: noticeString;
					} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
						countString = ((completeNum + srNotNeedDoNum) + "/" + YXPlanTotalCount);
					}

					final DJ_IDPos _myidpos = getIDPosInfoByIDPosID(Integer
							.valueOf(current_IDPos_ID));

					Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
							.setTitle("条目到底")
							.setMessage(noticeString)
							// 相当于点击确认按钮
							.setPositiveButton(R.string.sure,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (notcompleteNum == 0) {
												if (_myidpos != null) {
													_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);

												}
											}
											_myidpos.setPlanCount(countString);

											AppContext.setCurIDPos(null);
											AppManager.getAppManager()
													.finishActivity(
															Dianjian_Main.this);
										}
									})
							// 相当于点击取消按钮
							.setNegativeButton(R.string.cancel,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											if (notcompleteNum > 0)
												LoadFirstDJPLanWithOutCompleted();
											else {
												if (notcompleteNum <= 0) {
													if (srNotNeedDoNum == cycTotalCount) {
														_myidpos.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
														_myidpos.setPlanCount(countString);

														AppContext.setCurIDPos(null);
														AppManager
																.getAppManager()
																.finishActivity(
																		Dianjian_Main.this);
													} else {
														switch (CURRENT_MODE) {
														case PopMenu_EDIT:
															currentPlanIndex = -1;
															loadNextDJPlan();
															break;
														case PopMenu_CHECK:
															_myidpos.setPlanCount(countString);
															
															AppContext.setCurIDPos(null);
															AppManager
																	.getAppManager()
																	.finishActivity(
																			Dianjian_Main.this);
															break;
														}
													}
												} else {
													dispIndexOfDJPlan();
												}
											}
										}
									}).setCancelable(false).create();
					dialog.show();
				}
					break;
				case 3: {
					// 周期过期处理
					Dialog dialog = new AlertDialog.Builder(Dianjian_Main.this)
							.setTitle("系统提示")
							.setMessage(emsg)
							.setPositiveButton(R.string.sure,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

											AppContext.setCurIDPos(null);
											AppManager.getAppManager()
													.finishActivity(
															Dianjian_Main.this);
										}
									}).setCancelable(false).create();
					dialog.show();
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
							.GetPlanListByControlPoint(
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
		handler.sendMessageDelayed(new Message(),TIME_INTERVAL);
	}

	private void loadFirstUnDoDJPlanThread() {
		loading = new LoadingDialog(this);
		loading.setLoadText("加载中...");
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

	*//**
	 * 加载第一条未做计划，只是onCreate()的时候调用一次
	 * 
	 * @param nextFlag
	 * @param IsJudgeComplete
	 * @return
	 *//*
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

		// String emsg = "";
		// [start] 巡点检
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			// 不符合周期
			emsg = mDJPlan.JudgePlanTemp(Dianjian_Main.this, DateTimeHelper.GetDateTimeNow());
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
		// [start] 条件巡检
		else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
			if (IsJudgeComplete) {
				if (mDJPlan.JudgePlanIsCompletedForCASEXJ(Dianjian_Main.this)) {
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

	// @Override
	// public Object onRetainNonConfigurationInstance() {
	// final Dianjian_Main data = instance;
	// return data;
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 拍照返回
		if (requestCode == REQCODE_PICTURE) {
			if (resultCode == Activity.RESULT_OK) {
				try {
					saverevitionImageSize(imagePath, 800);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
						planFilesBuffer.get(REQCODE_PICTURE),
						R.layout.listitem_djplanimage);
				plan_picture_gallery.setAdapter(imageAdapter);
				picNumTextView.setText("共有"
						+ planFilesBuffer.get(REQCODE_PICTURE).size() + "张照片");
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
				
				 * File oldfile = new File(path); long size = 0; if
				 * (oldfile.exists()) { try { FileInputStream fis = null; fis =
				 * new FileInputStream(oldfile); size = fis.available();
				 * 
				 * if (size < 532*1024) {
				 * UIHelper.ToastMessage(Dianjian_Main.this, size+"");
				 * copyFile(path, audioPath); }else {
				 * UIHelper.ToastMessage(Dianjian_Main.this, "录音文件过大， "); } }
				 * catch (IOException e) { // TODO 自动生成的 catch 块
				 * e.printStackTrace(); } }
				 
				copyFile(path, audioPath);
				File file = new File(path);
				file.delete();

				DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
						planFilesBuffer.get(REQCODE_SOUND),
						R.layout.listitem_djplanimage);
				plan_audio_gallery.setAdapter(audioAdapter);
				soundNumTextView.setText("共有"
						+ planFilesBuffer.get(REQCODE_SOUND).size() + "段录音");

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
					timewave = bundle.getByteArray("sydata");
					fftdata = bundle.getByteArray("pydata");
					datalen = bundle.getInt("datalen");
					featurevalue = bundle.getString("featurevalue");
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
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Goback();
			unregisterBoradcastReceiver();
			return true;
		case KeyEvent.KEYCODE_9:
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	 *//**
	* OK键点击确认
	* @param event
	* @return
	*//*
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
			saveData();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	
	public void showNext(){  
        viewAnimator.setOutAnimation(this,R.anim.slide_out_up);  
        viewAnimator.setInAnimation(this,R.anim.slide_in_down);  
        viewAnimator.showNext();  
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
			//String action = intent.getAction();
			if (intent.getAction().equals("com.xst.track.service.updataCurrentLoction")) {
					updateGPSInfo(AppContext.getCurrLocation());
			}
		}
	};
	
	private void updateGPSInfo(BDLocation location) {
		try {
			String strLocationInfo = "";
			if (location != null) {
				int locType = location.getLocType();
				if (locType == BDLocation.TypeGpsLocation) {
					strLocationInfo = "经：" + location.getLongitude()
							+ "   " + "纬：" + location.getLatitude();
					dianjian_main_gpsinfo.setText(strLocationInfo);
					Drawable drawable = getResources().getDrawable(
							R.drawable.icon_from_gps);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					dianjian_main_gpsinfo.setCompoundDrawables(drawable, null, null,
							null);
					dianjian_main_gpsinfo.setText(strLocationInfo);
				} else if (locType == BDLocation.TypeNetWorkLocation) {
					strLocationInfo = "经：" + location.getLongitude()
							+ "   " + "纬：" + location.getLatitude();

					Drawable drawable = getResources().getDrawable(
							R.drawable.icon_from_network);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					dianjian_main_gpsinfo.setCompoundDrawables(drawable, null, null,
							null);
					dianjian_main_gpsinfo.setText(strLocationInfo);
				} else {
					dianjian_main_gpsinfo.setCompoundDrawables(null, null, null,
							null);
					dianjian_main_gpsinfo.setText("定位中");
				}
			} else {
				dianjian_main_gpsinfo.setCompoundDrawables(null, null, null, null);
				dianjian_main_gpsinfo.setText("定位中");
			}
		} catch (Exception e) {
			dianjian_main_gpsinfo.setCompoundDrawables(null, null, null, null);
			dianjian_main_gpsinfo.setText("定位中");
		}
	}*/
}