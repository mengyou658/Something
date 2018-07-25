package com.moons.xst.track.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.moons.xst.buss.ComDBHelper;
import com.moons.xst.buss.GPSPositionHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.BugInfoPhotoAdapter;
import com.moons.xst.track.adapter.DJPlanAudioAdapter;
import com.moons.xst.track.adapter.DJPlanImageAdapter;
import com.moons.xst.track.adapter.DJPlanVedioAdapter;
import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.GPSMobjectBugResult;
import com.moons.xst.track.bean.GPSMobjectBugResultForFiles;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.GPSHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.widget.ActionSheetDialog;
import com.moons.xst.track.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.moons.xst.track.widget.ActionSheetDialog.SheetItemColor;
import com.moons.xst.track.widget.LoadingDialog;

public class TempTask_InputBugInfo extends BaseActivity implements
		OnClickListener {

	public static TempTask_InputBugInfo instance = null;

	// 图片/录音长按菜单两项操作
	private static final int MENUACTION_DELETE = 1;// 删除
	private static final int MENUACTION_RETURN = 2;// 返回

	public static final int REQCODE_PICTURE = 0;// 拍照
	public static final int REQCODE_VEDIO = 1;// 录像
	public static final int REQCODE_SOUND = 2;// 录音

	public static final String LineTask = "LINE";// 路线相关
	public static final String TempTask = "TEMP";// 临时任务
	public static final String PointTask = "POINT";// 考核点相关

	private int _GPSPoint_ID = 0;
	private String TaskType;
	private String resultid;
	private List<String> mSpinnerList;
	private String selectString;
	private String TimeString;

	private GPSPositionHelper gpspohelper;
	private ComDBHelper comDBHelper;
	private List<GPSMobjectBugResultForFiles> filelist = new ArrayList<GPSMobjectBugResultForFiles>();// 结果文件信息实体列表

	private String[] gxnames = new String[] { "管线1", "管线2", "管线3", "管线4",
			"管线5", "管线6", "管线7", "管线8" };
	private boolean[] gxbool = new boolean[] { false, false, false, false,
			false, false, false, false };
	private ListView lv;
	private Hashtable<String, String> GXInfoButter = new Hashtable<String, String>();
	private String gxString = "";
	private String gxCD = "";
	// 数据变量
	private Hashtable<Integer, ArrayList<GPSMobjectBugResultForFiles>> taskFilesBuffer = new Hashtable<Integer, ArrayList<GPSMobjectBugResultForFiles>>();

	private AppContext appContext;// 全局Context

	private EditText et_eventname, et_locationdesc, et_eventdesc;
	private TextView tv_eventtype, tv_gxname, tv_more;
	private RelativeLayout rl_eventtype, rl_more, rl_eventdesc;
	private ImageView returnButton, iv_gxname;
	private Button saveButton;

	private ViewPager mTabPager;
	private View viewAudio, viewPicture, viewVideo;
	private RadioButton txtTab1, txtTab2, txtTab3;
	private TextView tv_tab1;
	private RadioGroup rg_state;
	private int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;

	private LoadingDialog loading;
	private Handler mHandler;

	// 拍照栏变量
	private ImageButton btn_SavePicture;
	private Gallery plan_picture_gallery;
	private String picname;
	private String imagePath;
	private TextView picno;
	private UUID uuid;

	// 录像栏变量
	private ImageButton btn_SaveVedio;
	private Gallery plan_Vedio_gallery;
	private String vedname;
	private String vedioPath;
	private TextView vedno;

	// 录音栏变量
	private ImageButton btn_SaveAudio;
	private MediaRecorder mRecorder = null;
	private Gallery plan_audio_gallery;
	private String audioname;
	private String audioPath;
	private TextView audno;

	private GPSMobjectBugResult result;

	private PagerAdapter mPagerAdapter;
	
	// 加载控件
	private LoadingDialog savePhoto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.track_inputbuginfo);

		appContext = (AppContext) getApplication();
		if (savedInstanceState != null) {
			// 这个是之前保存的数据
			_GPSPoint_ID = savedInstanceState.getInt("task_id");
			currIndex = savedInstanceState.getInt("currIndex");
		} else {
			// 这个是从另外一个界面进入这个时传入的
			_GPSPoint_ID = getIntent().getIntExtra("task_id", 0);
			TaskType = getIntent().getStringExtra("task_type");
		}

		// 初始化控件
		initView();
		// 初始化ViewPager
		this.initViewPager();
		changeViewPager(currIndex);
		// 绑定事件
		initOnClickListener();
		initAllPagerData();
		reflashFilesList();

		gpspohelper = new GPSPositionHelper();
		comDBHelper = new ComDBHelper();
		instance = this;

		resultid = (String) DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA));

		mSpinnerList = new ArrayList<String>();
		Iterator<Entry<String, String>> it2 = AppContext.eventTypeBuffer
				.entrySet().iterator();
		while (it2.hasNext()) {
			mSpinnerList.add(it2.next().getValue().toString());
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// if (savedInstanceState != null) {
		// // 这个是之前保存的数据
		// _GPSPoint_ID = savedInstanceState.getInt("task_id");
		// currIndex = savedInstanceState.getInt("currIndex");
		// } else {
		// // 这个是从另外一个界面进入这个时传入的
		// _GPSPoint_ID = getIntent().getIntExtra("task_id", 0);
		// TaskType = getIntent().getStringExtra("task_type");
		// }

		outState.putInt("task_id", _GPSPoint_ID);
		outState.putInt("currIndex", currIndex);
		outState.putString("task_type", TempTask);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.home_head_Rebutton:
			AppManager.getAppManager().finishActivity(
					TempTask_InputBugInfo.this);
			break;
		// 更多
		case R.id.tv_buginfo_more:
			if (tv_more.getText().equals(getString(R.string.temptask_more))) {
				rl_eventdesc.setVisibility(View.GONE);
				rl_more.setVisibility(View.VISIBLE);
				tv_more.setText(getString(R.string.temptask_return));

				// 关闭输入栏
				View view = getWindow().peekDecorView();
				if (view != null) {
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
							0);
				}
			} else {
				rl_more.setVisibility(View.GONE);
				rl_eventdesc.setVisibility(View.VISIBLE);
				tv_more.setText(getString(R.string.temptask_more));
			}
			break;
		// 事件类型
		case R.id.ll_bugbaseinfo_eventtype:
			new ActionSheetDialog(TempTask_InputBugInfo.this)
					.builder()
					.setTitle(getString(R.string.temptask_message_selecttype))
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItems(mSpinnerList, SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									String res = mSpinnerList.get(which - 1)
											.toString();
									tv_eventtype.setText(res);
									selectString = res;
								}
							}).show();

			/*
			 * new AlertDialog.Builder(TempTask_InputBugInfo.this) // build
			 * AlertDialog .setTitle(R.string.temptask_message_selecttype) //
			 * title .setItems( mSpinnerList.toArray(new
			 * CharSequence[mSpinnerList .size()]), new
			 * DialogInterface.OnClickListener() { // content
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { final CharSequence[] list = mSpinnerList .toArray(new
			 * CharSequence[mSpinnerList .size()]); String res =
			 * list[which].toString(); tv_eventtype.setText(res); selectString =
			 * res; } }) .setNegativeButton(R.string.cancel, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { dialog.dismiss(); // 关闭alertDialog } }).show();
			 */
			break;
		// 管线选择
		case R.id.btn_gx:
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.editbox_layout, null);
			final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
			new com.moons.xst.track.widget.AlertDialog(this)
					.builder()
					.setTitle(getString(R.string.temptask_message_importhitn))
					.setEditView(view, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, "")
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									gxString = " ";
									gxCD = " ";
									String radius = edit.getText().toString();
									loadUserDJLine(radius);
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
								}
							}).show();
			break;
		// 保存
		case R.id.btn_saveBugInfo:
			insertGPSMobjectBugResult();
			break;
		default:
			break;
		}
	}

	private void saveAndRefreshPhoto(){
		GPSMobjectBugResultForFiles ResultFile = genFileResult("JPG",
				picname, imagePath);
		if (taskFilesBuffer == null)
			taskFilesBuffer = new Hashtable<Integer, ArrayList<GPSMobjectBugResultForFiles>>();
		if (!taskFilesBuffer.containsKey(REQCODE_PICTURE)) {
			taskFilesBuffer.put(REQCODE_PICTURE,
					new ArrayList<GPSMobjectBugResultForFiles>());
			taskFilesBuffer.get(REQCODE_PICTURE).add(ResultFile);
		} else {
			taskFilesBuffer.get(REQCODE_PICTURE).add(ResultFile);
		}
		BugInfoPhotoAdapter imageAdapter = new BugInfoPhotoAdapter(this,
				taskFilesBuffer.get(REQCODE_PICTURE),
				R.layout.listitem_djplanimage);
		
		/*DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
				taskFilesBuffer.get(REQCODE_PICTURE), 0,
				R.layout.listitem_djplanimage);*/
		plan_picture_gallery.setAdapter(imageAdapter);
		filelist.add(ResultFile);
		picno.setText(getString(R.string.temptask_message_picturesum,
				taskFilesBuffer.get(REQCODE_PICTURE).size()));
	}
	
	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQCODE_PICTURE:
			if (resultCode == Activity.RESULT_OK) {
				try {
					saverevitionImageSize(imagePath, 1000);
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
			break;

		case REQCODE_SOUND:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String path = UriToPath(uri);
				copyFile(path, audioPath);
				File file = new File(path);
				file.delete();
				GPSMobjectBugResultForFiles ResultFile = genFileResult("ARM",
						audioname, audioPath);
				if (taskFilesBuffer == null)
					taskFilesBuffer = new Hashtable<Integer, ArrayList<GPSMobjectBugResultForFiles>>();
				if (!taskFilesBuffer.containsKey(REQCODE_SOUND)) {
					taskFilesBuffer.put(REQCODE_SOUND,
							new ArrayList<GPSMobjectBugResultForFiles>());
					taskFilesBuffer.get(REQCODE_SOUND).add(ResultFile);
				} else {
					taskFilesBuffer.get(REQCODE_SOUND).add(ResultFile);
				}
				filelist.add(ResultFile);
				DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
						taskFilesBuffer.get(REQCODE_SOUND), 0,
						R.layout.listitem_djplanimage);
				plan_audio_gallery.setAdapter(audioAdapter);
				audno.setText(getString(R.string.temptask_message_recordsum,
						taskFilesBuffer.get(REQCODE_SOUND).size()));
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				File file = new File(audioPath);
				file.delete();
			}
			break;
		case REQCODE_VEDIO:
			if (resultCode == Activity.RESULT_OK) {
				GPSMobjectBugResultForFiles ResultFile = genFileResult("WAV",
						vedname, vedioPath);
				if (taskFilesBuffer == null)
					taskFilesBuffer = new Hashtable<Integer, ArrayList<GPSMobjectBugResultForFiles>>();
				if (!taskFilesBuffer.containsKey(REQCODE_VEDIO)) {
					taskFilesBuffer.put(REQCODE_VEDIO,
							new ArrayList<GPSMobjectBugResultForFiles>());
					taskFilesBuffer.get(REQCODE_VEDIO).add(ResultFile);
				} else {
					taskFilesBuffer.get(REQCODE_VEDIO).add(ResultFile);
				}
				filelist.add(ResultFile);
				DJPlanVedioAdapter audioAdapter = new DJPlanVedioAdapter(this,
						taskFilesBuffer.get(REQCODE_VEDIO), 0,
						R.layout.listitem_djplanimage);
				plan_Vedio_gallery.setAdapter(audioAdapter);
				vedno.setText(getString(R.string.temptask_message_videosum,
						taskFilesBuffer.get(REQCODE_VEDIO).size()));
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				File file = new File(vedioPath);
				file.delete();
			}
			break;

		default:
			break;
		}
	}

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		String s = getString(R.string.temptask_message_presshitn,
				item.getItemId());
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final String actionFileName;// 要操作的文件名
		View v = null;
		if (item.getGroupId() == 0) {
			v = (View) plan_picture_gallery.getSelectedView();// .getChildAt(info.position);
		} else if (item.getGroupId() == 1) {
			v = (View) plan_audio_gallery.getSelectedView();// .getChildAt(info.position);
		} else {
			v = (View) plan_Vedio_gallery.getSelectedView();
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
		final GPSMobjectBugResultForFiles fiResult = (GPSMobjectBugResultForFiles) aImage
				.getTag();
		actionFileName = (String) fiResult.getFilePath();

		if (actionFileName == null) {
			// UIHelper.ToastMessage(DianjianMain.this, "Tag is not find ");
			return false;
		}
		switch (item.getGroupId()) {
		case 0:// 照片相关
			switch (item.getItemId()) {
			case MENUACTION_DELETE:// 删除
				LayoutInflater factory = LayoutInflater.from(this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(this)
						.builder()
						.setTitle(getString(R.string.plan_del_notice))
						.setView(view)
						.setMsg(getString(R.string.plan_photo_del))
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {

										// 删除关系条目
										if (taskFilesBuffer != null
												&& taskFilesBuffer
														.containsKey(0)) {
											if (taskFilesBuffer.get(0)
													.contains(fiResult)) {
												// DJResultHelper.GetIntance().deleteOneFile(
												// getApplication(),
												// _photoByResult);
												taskFilesBuffer.get(0).remove(
														fiResult);
												reflashFilesList();
											}
										}
										// 删除图片文件
										File file = new File(actionFileName);
										file.delete();
										UIHelper.ToastMessage(
												TempTask_InputBugInfo.this,
												R.string.plan_del_ok);

									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
									}
								}).show();

				break;
			case MENUACTION_RETURN:// 返回
				return false;
			}
			break;
		case 1:// 录音相关
			switch (item.getItemId()) {
			case MENUACTION_DELETE:// 删除
				LayoutInflater factory = LayoutInflater.from(this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(this)
						.builder()
						.setTitle(getString(R.string.plan_del_notice))
						.setView(view)
						.setMsg(getString(R.string.plan_recorder_del))
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {

										if (taskFilesBuffer != null
												&& taskFilesBuffer
														.containsKey(REQCODE_SOUND)) {
											if (taskFilesBuffer.get(
													REQCODE_SOUND).contains(
													fiResult)) {
												// DJResultHelper.GetIntance().deleteOneFile(
												// getApplication(),
												// _photoByResult);
												taskFilesBuffer.get(
														REQCODE_SOUND).remove(
														fiResult);
												reflashFilesList();
											}
										}
										File file = new File(actionFileName);
										file.delete();
										UIHelper.ToastMessage(
												TempTask_InputBugInfo.this,
												R.string.plan_del_ok);

									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
									}
								}).show();
				break;
			case MENUACTION_RETURN:// 返回
				return false;
			}
			break;
		case 2:// 录像相关
			switch (item.getItemId()) {
			case MENUACTION_DELETE:// 删除
				LayoutInflater factory = LayoutInflater.from(this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(this)
						.builder()
						.setTitle(getString(R.string.plan_del_notice))
						.setView(view)
						.setMsg(getString(R.string.plan_vedio_del))
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if (taskFilesBuffer != null
												&& taskFilesBuffer
														.containsKey(REQCODE_VEDIO)) {
											if (taskFilesBuffer.get(
													REQCODE_VEDIO).contains(
													fiResult)) {
												// DJResultHelper.GetIntance().deleteOneFile(
												// getApplication(),
												// _photoByResult);
												taskFilesBuffer.get(
														REQCODE_VEDIO).remove(
														fiResult);
												reflashFilesList();
											}
										}
										File file = new File(actionFileName);
										file.delete();
										UIHelper.ToastMessage(
												TempTask_InputBugInfo.this,
												R.string.plan_del_ok);

									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
									}
								}).show();

				break;
			case MENUACTION_RETURN:// 返回
				return false;
			}
			break;
		}

		return super.onContextItemSelected(item);
	}

	private void initView() {
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		et_eventname = (EditText) findViewById(R.id.temptask_inputbug_namedes);
		et_locationdesc = (EditText) findViewById(R.id.temptask_inputbug_placedes);
		et_eventdesc = (EditText) findViewById(R.id.edit_planmemo);
		tv_eventtype = (TextView) findViewById(R.id.tv_eventtype_value);
		tv_gxname = (TextView) findViewById(R.id.gx_name);
		tv_more = (TextView) findViewById(R.id.tv_buginfo_more);
		rl_eventtype = (RelativeLayout) findViewById(R.id.ll_bugbaseinfo_eventtype);
		iv_gxname = (ImageView) findViewById(R.id.btn_gx);
		rl_eventdesc = (RelativeLayout) findViewById(R.id.ll_buginfo_more);
		rl_more = (RelativeLayout) findViewById(R.id.layout_buginfo_more_sheet);
		saveButton = (Button) findViewById(R.id.btn_saveBugInfo);
	}

	private void initAllPagerData() {
		/*if (!taskFilesBuffer.containsKey(REQCODE_PICTURE)) {
			taskFilesBuffer.put(REQCODE_PICTURE,
					new ArrayList<GPSMobjectBugResultForFiles>());
		}
		if (!taskFilesBuffer.containsKey(REQCODE_SOUND)) {
			taskFilesBuffer.put(REQCODE_SOUND,
					new ArrayList<GPSMobjectBugResultForFiles>());
		}
		if (!taskFilesBuffer.containsKey(REQCODE_VEDIO)) {
			taskFilesBuffer.put(REQCODE_VEDIO,
					new ArrayList<GPSMobjectBugResultForFiles>());
		}*/
		
		// 录音
		audno = (TextView) viewAudio.findViewById(R.id.planaudio_num);
		btn_SaveAudio = (ImageButton) viewAudio
				.findViewById(R.id.btn_dj_record);
		btn_SaveAudio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (!checkMaxFileNum(REQCODE_SOUND))
					return;

				uuid = UUID.randomUUID();
				audioname = uuid.toString() + ".amr";
				if (TaskType.equals(TempTask)) {
					audioPath = AppConst.TempTaskAudioPath() + audioname;
				} else {
					audioPath = AppConst.CurrentResultPath_Pic(appContext
							.GetCurrLineID()) + audioname;
				}
				File f = new File(audioPath);
				File destDir = new File(AppConst.TempTaskAudioPath());
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
				//设置录音文件最大值（大约1000KB）
				long bytes = (long) (1024 * 1000L);
				intentFromRecord.putExtra(MediaStore.Audio.Media.EXTRA_MAX_BYTES,bytes);
				intentFromRecord.putExtra("return-data", true);
				startActivityForResult(intentFromRecord, REQCODE_SOUND);
			}
		});
		plan_audio_gallery = (Gallery) viewAudio
				.findViewById(R.id.plan_audio_gallery);
		plan_audio_gallery
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView parent, View v,
							int position, long id) {
						final String actionFileName = taskFilesBuffer
								.get(REQCODE_SOUND).get(position).getFilePath();
						List<String> pathlist = new ArrayList<String>();
						for (int i = 0; i < taskFilesBuffer.get(REQCODE_SOUND)
								.size(); i++) {
							String pa = taskFilesBuffer.get(REQCODE_SOUND)
									.get(i).getFilePath();
							pathlist.add(pa);
						}
						AudioPlayerDialog Audialog = new AudioPlayerDialog(
								TempTask_InputBugInfo.this, actionFileName,
								pathlist);
						Audialog.show();
					}
				});
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
						menu.setHeaderTitle(R.string.temptask_message_recordoperation);
						menu.add(1, MENUACTION_DELETE, 0,
								R.string.common_delete);
						menu.add(1, MENUACTION_RETURN, 0,
								R.string.temptask_return);

						AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
						plan_audio_gallery.setSelection(info.position);

					}
				});

		// 拍照
		picno = (TextView) viewPicture.findViewById(R.id.planpicture_num);
		btn_SavePicture = (ImageButton) viewPicture
				.findViewById(R.id.btn_dj_photo);
		btn_SavePicture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!checkMaxFileNum(REQCODE_PICTURE))
					return;

				Intent getImageByCamera = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				TimeString = DateTimeHelper.getDateTimeNow();
				uuid = UUID.randomUUID();
				picname = uuid.toString() + ".jpg";
				imagePath = AppConst.TempTaskPhotoPath() + picname;
				if (TaskType.equals(TempTask)) {
					imagePath = AppConst.TempTaskPhotoPath() + picname;
				} else {
					imagePath = AppConst.CurrentResultPath_Pic(appContext
							.GetCurrLineID()) + picname;
				}
				File f = new File(imagePath);
				File destDir = new File(AppConst.TempTaskPhotoPath());
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
		// 照片浏览器
		plan_picture_gallery = (Gallery) viewPicture
				.findViewById(R.id.plan_picture_galley);
		/*BugInfoPhotoAdapter imageAdapter = new BugInfoPhotoAdapter(this,
				taskFilesBuffer.get(REQCODE_PICTURE),
				R.layout.listitem_djplanimage);

		DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
				taskFilesBuffer.get(REQCODE_PICTURE), 0,
				R.layout.listitem_djplanimage);
		plan_picture_gallery.setAdapter(imageAdapter);*/
		plan_picture_gallery
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView parent, View v,
							int position, long id) {

						final String[] actionFileName = new String[1];
						actionFileName[0] = taskFilesBuffer
								.get(REQCODE_PICTURE).get(position)
								.getFilePath();
						UIHelper.showImageZoomDialog(
								TempTask_InputBugInfo.this, actionFileName,
								"false");
					}
				});
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

						menu.setHeaderTitle(R.string.temptask_message_pictureoperation);
						menu.add(0, MENUACTION_DELETE, 0,
								R.string.common_delete);
						menu.add(0, MENUACTION_RETURN, 0,
								R.string.temptask_return);

						AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
						plan_picture_gallery.setSelection(info.position);
					}
				});

		// 录像
		vedno = (TextView) viewVideo.findViewById(R.id.planvedio_num);
		btn_SaveVedio = (ImageButton) viewVideo
				.findViewById(R.id.btn_savevedio);
		btn_SaveVedio.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub Intent
				// 限制最多3个视频，每个视频限制5秒
				if (!checkMaxFileNum(REQCODE_VEDIO))
					return;
				Intent vedioIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				uuid = UUID.randomUUID();
				vedname = uuid.toString() + ".3gp";
				if (TaskType.equals(TempTask)) {
					vedioPath = AppConst.TempTaskVedioPath() + vedname;
				} else {
					vedioPath = AppConst.CurrentResultPath_Pic(appContext
							.GetCurrLineID()) + vedname;
				}
				File f = new File(vedioPath);
				File destDir = new File(AppConst.TempTaskVedioPath());
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				try {
					f.createNewFile();
					// 这行代码很重要，没有的话会因为写入权限不够出一些问题
					f.setWritable(true, false);
				} catch (IOException e) {
				}
				vedioIntent.putExtra("return-data", true);
				vedioIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				vedioIntent.putExtra("outputFormat", ".3gp");
				vedioIntent.putExtra("noFaceDetection", true);

				// 低清晰度
				if (AppContext.getVideoQuality().equalsIgnoreCase("LOW")) {
					vedioIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
					vedioIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
				} else {
					vedioIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
					vedioIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
				}
				startActivityForResult(vedioIntent, REQCODE_VEDIO);
			}
		});
		plan_Vedio_gallery = (Gallery) viewVideo
				.findViewById(R.id.track_vedio_galley);
		plan_Vedio_gallery
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView parent, View v,
							int position, long id) {

						final String actionFileName = taskFilesBuffer
								.get(REQCODE_VEDIO).get(position).getFilePath();
						Intent intent = new Intent(TempTask_InputBugInfo.this,
								Tool_Video_PreviewAty.class);
						intent.putExtra("videoPath", actionFileName);
						intent.putExtra("thumbnailPath", "");
						intent.putExtra("visible", "false");
						startActivity(intent);
					}
				});
		plan_Vedio_gallery
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		// 长按录像后的弹出菜单
		plan_Vedio_gallery
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {

						menu.setHeaderTitle(R.string.temptask_message_videooperation);
						menu.add(2, MENUACTION_DELETE, 0,
								R.string.common_delete);
						menu.add(2, MENUACTION_RETURN, 0,
								R.string.temptask_return);

						AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
						plan_picture_gallery.setSelection(info.position);
					}
				});
	}

	private void initOnClickListener() {
		returnButton.setOnClickListener(this);
		tv_more.setOnClickListener(this);
		rl_eventtype.setOnClickListener(this);
		iv_gxname.setOnClickListener(this);
		saveButton.setOnClickListener(this);
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mTabPager = (ViewPager) findViewById(R.id.temptaskInputBugTabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mTabPager.setPageTransformer(false, new ViewPager.PageTransformer() {
		    @Override
		    public void transformPage(View page, float position) {
		    	final float normalizedposition = Math.abs(Math.abs(position) - 1);
		        page.setAlpha(normalizedposition);
	        }
		});

		txtTab1 = (RadioButton) findViewById(R.id.txtTab1);
		txtTab2 = (RadioButton) findViewById(R.id.txtTab2);
		txtTab3 = (RadioButton) findViewById(R.id.txtTab3);

		txtTab1.setOnClickListener(new MyOnClickListener(0));
		txtTab2.setOnClickListener(new MyOnClickListener(1));
		txtTab3.setOnClickListener(new MyOnClickListener(2));

		tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
		rg_state = (RadioGroup) findViewById(R.id.rg_state);

		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();
		one = displayWidth / 4; // 设置水平动画平移大小
		two = one * 2;
		three = one * 3;

		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		viewAudio = mLi.inflate(R.layout.dianjian_tab_audio, null);
		viewPicture = mLi.inflate(R.layout.dianjian_tab_picture, null);
		viewVideo = mLi.inflate(R.layout.track_tab_vedio, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(viewAudio);
		views.add(viewPicture);
		views.add(viewVideo);
		mPagerAdapter = new PagerAdapter() {

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

			// @Override
			// public CharSequence getPageTitle(int position) {
			// return titles.get(position);
			// }

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		mTabPager.setCurrentItem(0);//
	}

	/*
	 * 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			changeViewPager(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (arg2 == 0) {
				return;
			}
			tvMoveTo(arg0, arg1);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 切换Page
	 */
	private void changeViewPager(int arg0) {

		switch (arg0) {
		case 0:// 描述
			txtTab1.setEnabled(false);
			txtTab2.setEnabled(true);
			txtTab3.setEnabled(true);
			break;
		case 1:// 拍照
			txtTab1.setEnabled(true);
			txtTab2.setEnabled(false);
			txtTab3.setEnabled(true);
			break;

		case 2:// 历史
			txtTab1.setEnabled(true);
			txtTab2.setEnabled(true);
			txtTab3.setEnabled(false);
			break;
		}
		currIndex = arg0;

		// 关闭输入栏
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	// 上次移动的x坐标
	private int fromX = 0;

	// tab下面那条线的移动动画
	private void tvMoveTo(int index, float f) {
		RadioButton button = (RadioButton) rg_state.getChildAt(index);
		int location[] = new int[2];
		button.getLocationInWindow(location);
		TranslateAnimation animation = new TranslateAnimation(fromX,
				location[0] + f * button.getWidth(), 0, 0);
		animation.setDuration(50);
		animation.setFillAfter(true);
		fromX = (int) (location[0] + f * button.getWidth());
		// 开启动画
		tv_tab1.startAnimation(animation);

	}

	/**
	 * ViewPager图标点击监听
	 */
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

	/**
	 * 根据登录的人员信息刷新相关数据
	 */
	private void loadUserDJLine(String radius) {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					// disp data loaded
					String result = msg.obj.toString();
					boolean re = spliteGXInfo(result);
					if (re) {
						showMultiChoiceItems();
					} else {
						UIHelper.ToastMessage(appContext,
								R.string.temptask_message_anewgainhitn);
					}
				} else if (msg.obj != null) {
					UIHelper.ToastMessage(appContext,
							R.string.temptask_message_GPSgainhint);
				}
			}
		};
		this.loadGCInfoThread(radius);
	}

	private void loadGCInfoThread(final String radius) {
		loading = new LoadingDialog(this);
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 加载管线信息
					String info = null;
					if (WebserviceFactory.checkWSOther()) {
						if (AppContext.getCurrLocation() == null) {
							msg.what = 2;
							info = "";
						} else {
							msg.what = 1;
							BDLocation loc = AppContext.getCurrLocation();
							int locType = loc.getLocType();
							if (locType == BDLocation.TypeGpsLocation
									|| locType == BDLocation.TypeNetWorkLocation) {
								info = WebserviceFactory.getGXInfo(
										TempTask_InputBugInfo.this,
										String.valueOf(loc.getLongitude()),
										String.valueOf(loc.getLatitude()),
										radius);
							}
						}
						// msg.what = 1;
						// info =
						// WebserviceFactory.getGXInfo(TempTask_inputbug.this,"115.957765","39.707085",radius);
					} else {
						msg.what = 1;
						info = "";
					}
					msg.obj = info;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
					if (loading != null)
						loading.dismiss();
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	private boolean spliteGXInfo(String result) {
		GXInfoButter.clear();
		String[] info = result.split("\\;");
		gxnames = new String[info.length];
		gxbool = new boolean[info.length];
		if (result.length() < 5) {
			UIHelper.ToastMessage(appContext,
					R.string.temptask_message_anewgainhitn);
			return false;
		} else {
			for (int i = 0; i < info.length; i++) {
				String[] linfo = info[i].split("\\,");
				GXInfoButter.put(linfo[0], linfo[1]);
				gxnames[i] = linfo[1];
				gxbool[i] = false;
			}
			return true;
		}
	}

	/**
	 * 管线多选
	 */
	private void showMultiChoiceItems() {
		AlertDialog builder = new AlertDialog.Builder(this)
				.setTitle(R.string.temptask_message_selectpipeline)
				.setMultiChoiceItems(gxnames, gxbool,
						new OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								// TODO Auto-generated method stub

							}
						})
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								String s = " ";
								String h = " ";
								// 扫描所有的列表项，如果当前列表项被选中，将列表项的文本追加到s变量中。
								for (int i = 0; i < gxnames.length; i++) {
									if (lv.getCheckedItemPositions().get(i)) {
										s += lv.getAdapter().getItem(i) + "|";
										Iterator<String> itr = GXInfoButter
												.keySet().iterator();
										while (itr.hasNext()) {
											String keyString = (String) itr
													.next();
											if (GXInfoButter.get(keyString)
													.equals(lv.getAdapter()
															.getItem(i))) {
												h += keyString + "|";
											}
										}
									}
									gxString = s;
									gxCD = h;
								}

								// 用户至少选择了一个列表项
								if (lv.getCheckedItemPositions().size() > 0) {
									tv_gxname.setText(gxString);
								}

								// 用户未选择任何列表项
								else if (lv.getCheckedItemPositions().size() <= 0) {
									UIHelper.ToastMessage(
											TempTask_InputBugInfo.this,
											R.string.temptask_message_selectpipelinehint);
								}
							}
						}).setNegativeButton(R.string.cancel, null).create();
		//
		lv = builder.getListView();
		builder.show();
	}

	/**
	 * 插入关联文件结果
	 */
	private GPSMobjectBugResultForFiles genFileResult(String type, String name,
			String path) {
		GPSMobjectBugResultForFiles gpsBugResultFile = new GPSMobjectBugResultForFiles();
		gpsBugResultFile.setResult_ID(resultid);
		gpsBugResultFile.setGUID(uuid.toString());
		String curDate = DateTimeHelper.getDateTimeNow();
		gpsBugResultFile.setFile_DT(curDate);
		gpsBugResultFile.setFile_Type(type);
		gpsBugResultFile.setFilePath(path);
		gpsBugResultFile.setFileTitle(name);
		return gpsBugResultFile;
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
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	/*private void saverevitionImageSize(String path, int size)
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
		if (AppContext.getRotateCameraYN()) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap returnbm = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			returnbm.compress(Bitmap.CompressFormat.PNG, 90, out);
		} else {
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		}
		out.close();
		// FileOutputStream out = new FileOutputStream(path);
		// bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
	}*/

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
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
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
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

	/**
	 * 刷新文件预览栏（照片、录音、录像）
	 */
	private void reflashFilesList() {
		if (taskFilesBuffer != null && taskFilesBuffer.size() > 0) {
			if (taskFilesBuffer.containsKey(0)
					&& taskFilesBuffer.get(0).size() > 0) {
				BugInfoPhotoAdapter imageAdapter = new BugInfoPhotoAdapter(this,
						taskFilesBuffer.get(REQCODE_PICTURE),
						R.layout.listitem_djplanimage);
				
				/*DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
						taskFilesBuffer.get(REQCODE_PICTURE), 0,
						R.layout.listitem_djplanimage);*/
				plan_picture_gallery.setAdapter(imageAdapter);
				picno.setText(getString(R.string.temptask_message_picturesum,
						taskFilesBuffer.get(REQCODE_PICTURE).size()));
			} else {
				DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
						new ArrayList<DJ_PhotoByResult>(),
						R.layout.listitem_djplanimage);
				plan_picture_gallery.setAdapter(imageAdapter);
				picno.setText(R.string.temptask_message_nophotograph);
			}
			if (taskFilesBuffer.containsKey(REQCODE_SOUND)
					&& taskFilesBuffer.get(REQCODE_SOUND).size() > 0) {
				DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
						taskFilesBuffer.get(REQCODE_SOUND), 0,
						R.layout.listitem_djplanimage);
				plan_audio_gallery.setAdapter(audioAdapter);
				audno.setText(getString(R.string.temptask_message_recordsum,
						taskFilesBuffer.get(REQCODE_SOUND).size()));
			} else {
				DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
						new ArrayList<DJ_PhotoByResult>(),
						R.layout.listitem_djplanimage);
				plan_audio_gallery.setAdapter(audioAdapter);
				audno.setText(R.string.temptask_message_norecord);
			}
			if (taskFilesBuffer.containsKey(REQCODE_VEDIO)
					&& taskFilesBuffer.get(REQCODE_VEDIO).size() > 0) {
				DJPlanVedioAdapter vedioAdapter = new DJPlanVedioAdapter(this,
						taskFilesBuffer.get(REQCODE_VEDIO), 0,
						R.layout.listitem_djplanimage);
				plan_Vedio_gallery.setAdapter(vedioAdapter);
				vedno.setText(getString(R.string.temptask_message_videosum,
						taskFilesBuffer.get(REQCODE_VEDIO).size()));
			} else {
				DJPlanVedioAdapter vedioAdapter = new DJPlanVedioAdapter(this,
						new ArrayList<DJ_PhotoByResult>(),
						R.layout.listitem_djplanimage);
				plan_Vedio_gallery.setAdapter(vedioAdapter);
				vedno.setText(R.string.temptask_message_novideo);
			}
		} else {
			// 清理图片列表
			DJPlanImageAdapter imageAdapter = new DJPlanImageAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djplanimage);
			plan_picture_gallery.setAdapter(imageAdapter);
			picno.setText(R.string.temptask_message_nophotograph);
			// 清理录音列表
			DJPlanAudioAdapter audioAdapter = new DJPlanAudioAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djplanimage);
			plan_audio_gallery.setAdapter(audioAdapter);
			audno.setText(R.string.temptask_message_norecord);
			// 清理录像列表
			DJPlanVedioAdapter vedioAdapter = new DJPlanVedioAdapter(this,
					new ArrayList<DJ_PhotoByResult>(),
					R.layout.listitem_djplanimage);
			plan_Vedio_gallery.setAdapter(vedioAdapter);
			vedno.setText(R.string.temptask_message_novideo);
		}
	}

	private boolean checkMaxFileNum(int type) {
		boolean result = true;
		if (taskFilesBuffer != null && taskFilesBuffer.size() > 0) {
			switch (type) {
			case REQCODE_PICTURE:
				if (taskFilesBuffer.containsKey(type)
						&& taskFilesBuffer.get(type).size() >= 10) {
					UIHelper.ToastMessage(TempTask_InputBugInfo.this,
							R.string.temptask_message_photographmaxsum);
					result = false;
				}
				break;
			case REQCODE_VEDIO:
				if (taskFilesBuffer.containsKey(type)
						&& taskFilesBuffer.get(type).size() >= 3) {
					UIHelper.ToastMessage(TempTask_InputBugInfo.this,
							R.string.temptask_message_videomaxsum);
					result = false;
				}
				break;
			case REQCODE_SOUND:
				if (taskFilesBuffer.containsKey(type)
						&& taskFilesBuffer.get(type).size() >= 5) {
					UIHelper.ToastMessage(TempTask_InputBugInfo.this,
							R.string.temptask_message_recordmaxsum);
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

	/**
	 * 插入报缺结果
	 */
	private void insertGPSMobjectBugResult() {
		int Type_ID = 0;
		int GZDM_ID = 0;
		int ZZDM_ID = 0;
		int YYDM_ID = 0;
		int CSDM_ID = 0;
		if (et_eventdesc.getText().toString().length() > 500) {
			UIHelper.ToastMessage(this, R.string.Inputbug_desmessMax);
			return;
		} else if (AppContext.getCurrLocation() == null) {
			UIHelper.ToastMessage(this,
					R.string.mapmain_message_noacquirelocation);
			return;
		} else if (et_eventdesc.getText().toString().length() == 0) {
			UIHelper.ToastMessage(this, R.string.Inputbug_desmessMin);
			return;
		} else if (tv_eventtype.getText().equals(
				getString(R.string.temptask_choose))) {
			UIHelper.ToastMessage(this, R.string.Inputbug_desmessEventType);
			return;
		}
		result = new GPSMobjectBugResult();
		result.setResult_ID(resultid);
		result.setType_ID(Type_ID);
		result.setGZDM_ID(GZDM_ID);
		result.setZZDM_ID(ZZDM_ID);
		result.setYYDM_ID(YYDM_ID);
		result.setCSDM_ID(CSDM_ID);
		result.setGPSPoint_ID(_GPSPoint_ID);// 待添加
		result.setBugMemo_TX(et_eventdesc.getText().toString());
		String curDate = DateTimeHelper.DateToString(
				new java.util.Date(System.currentTimeMillis()),
				"yyyy-MM-dd HH:mm:ss");
		result.setFind_TM(curDate);
		result.setPost_ID(AppContext.getUserID());
		result.setFindUser_TX(AppContext.getUserName());
		BDLocation loc = AppContext.getCurrLocation();
		int locType = loc.getLocType();

		if (locType == BDLocation.TypeGpsLocation
				|| locType == BDLocation.TypeNetWorkLocation) {
			LatLng desLatLng = GPSHelper.convertBaiduToGPS(new LatLng(loc
					.getLatitude(), loc.getLongitude()));
			result.setLatitude(String.valueOf(desLatLng.latitude));
			result.setLongitude(String.valueOf(desLatLng.longitude));
		}
		// result.setLatitude("111");
		// result.setLongitude("111");
		String eventtypeid = null;
		Iterator<String> itr = AppContext.eventTypeBuffer.keySet().iterator();
		while (itr.hasNext()) {
			String keyString = (String) itr.next();
			if (AppContext.eventTypeBuffer.get(keyString).equals(selectString)) {
				eventtypeid = keyString;
			}
		}
		result.setEventType_ID(eventtypeid);
		result.setEventName_TX(selectString);
		result.setSBPost_ID("1");
		result.setGX_ID(gxCD);
		result.setGXName_TX(gxString);
		result.setEventName_TX(et_eventname.getText().toString());
		result.setEventPlace_TX(et_locationdesc.getText().toString());
		result.setEventStatus_ID("0");

		if (TaskType.equals(LineTask)) {
			result.setSBType_ID("1");
			result.setGPSPoint_ID(appContext.GetCurrLineID());
			gpspohelper.InsertMobjectBugResult(this, result);
		} else if (TaskType.equals(TempTask)) {
			result.setSBType_ID("2");
			comDBHelper.InsertMobjectBugResult(this, result);
		} else {
			result.setSBType_ID("3");
			result.setGPSPoint_ID(appContext.GetCurrLineID());
			gpspohelper.InsertMobjectBugResult(this, result);
		}

		if (taskFilesBuffer != null && taskFilesBuffer.size() > 0) {
			if (taskFilesBuffer.containsKey(0)) {
				ArrayList<GPSMobjectBugResultForFiles> _tempList = taskFilesBuffer
						.get(0);
				for (GPSMobjectBugResultForFiles refile : _tempList) {
					if (TaskType.equals("LINE") || TaskType.equals("POINT")) {
						gpspohelper.InsertMobjectBugResultFiles(this, refile);
					} else {
						comDBHelper.InsertMobjectBugResultFiles(this, refile);
					}
				}
			}
			if (taskFilesBuffer.containsKey(1)) {
				ArrayList<GPSMobjectBugResultForFiles> _tempList = taskFilesBuffer
						.get(1);
				for (GPSMobjectBugResultForFiles refile : _tempList) {
					if (TaskType.equals("LINE") || TaskType.equals("POINT")) {
						gpspohelper.InsertMobjectBugResultFiles(this, refile);
					} else {
						comDBHelper.InsertMobjectBugResultFiles(this, refile);
					}
				}
			}
			if (taskFilesBuffer.containsKey(2)) {
				ArrayList<GPSMobjectBugResultForFiles> _tempList = taskFilesBuffer
						.get(2);
				for (GPSMobjectBugResultForFiles refile : _tempList) {
					if (TaskType.equals("LINE") || TaskType.equals("POINT")) {
						gpspohelper.InsertMobjectBugResultFiles(this, refile);
					} else {
						comDBHelper.InsertMobjectBugResultFiles(this, refile);
					}
				}
			}
		}
		taskFilesBuffer.clear();
		UIHelper.ToastMessage(this, R.string.temptask_message_savesucceed);
		this.finish();
	}
}