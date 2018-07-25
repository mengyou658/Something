package com.moons.xst.track;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.moons.xst.buss.DJHisDataHelper;
import com.moons.xst.track.common.AppResourceUtils;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.pad_ui.PADStart;
import com.moons.xst.track.ui.BaseActivity;
import com.moons.xst.track.ui.Main_Page;
import com.moons.xst.track.widget.LoadingDialog;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class AppStart extends BaseActivity {

	public static AppStart instance = null;
	private static final String TAG = "AppStart";
	AppContext ac;
	TextView mVersion, app_name;
	private Handler mHandler;
	private LoadingDialog loading;
	View view = null;
	FrameLayout wellcome;

	SharedPreferences sp;
	boolean isFirstStart = true;
	
	boolean SkipYN=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		view = View.inflate(this, R.layout.start, null);
		setContentView(view);
		
		initView();
			
		if (checkRestartYN())
			return;
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);		
		ac = (AppContext) getApplication();
		changeAppLanguage();
		DisposeDataThread();
		
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		
		instance = this;
		
		/*if (AppContext.AppName == 3) {
		Intent intent = new Intent(this, PADStart.class);
		startActivity(intent);
		return;
	}*/
		//check(wellcome);
	}
	
	private void initView() {
		wellcome = (FrameLayout) view.findViewById(R.id.app_start_view);

		String welcomePic = AppResourceUtils.getValue(this,
				getString(R.string.app_name), "welcomepic");
		if (StringUtils.isEmpty(welcomePic))
			wellcome.setBackgroundResource(R.drawable.welcome);
		else {
			int welcomePicID = AppResourceUtils.getDrawableId(this, welcomePic);
			wellcome.setBackgroundResource(welcomePicID);
		}

		// 获取客户端版本信息
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			mVersion = (TextView) view.findViewById(R.id.about_version);
			mVersion.setText("Version：" + info.versionName + "("
					+ info.versionCode + ")");
			app_name = (TextView) view.findViewById(R.id.start_app_name);
			if (AppContext.AppName == AppConst.AppNameConfig.AppName_Track)
				app_name.setText(R.string.main_about_appnameXX);
			if (AppContext.AppName == AppConst.AppNameConfig.AppName_XDJ)
				app_name.setText(R.string.main_about_appnameXDJ);
			if (AppContext.AppName == AppConst.AppNameConfig.AppName_ALL)
				app_name.setText(R.string.main_about_appnameXST);
			if (AppContext.AppName == AppConst.AppNameConfig.AppName_PAD)
				app_name.setText(R.string.main_about_appnamePAD);

			String appnamevisible = AppResourceUtils.getValue(this,
					getString(R.string.app_name), "welcomenamevisible");
			if (!StringUtils.isEmpty(appnamevisible)
					&& !StringUtils.toBool(appnamevisible))
				app_name.setVisibility(View.GONE);

			String appName = AppResourceUtils.getValue(this,
					getString(R.string.app_name), "welcomename");
			if (!StringUtils.isEmpty(appName)) {
				int appNameID = AppResourceUtils.getStringId(this, appName);
				app_name.setText(appNameID);
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}
	
	private boolean checkRestartYN() {
		sp = this.getSharedPreferences("UpDateConfig", MODE_PRIVATE);
		isFirstStart = sp.getBoolean("isFirstStart", true);
		String deviceModel = AppContext.getModel();
		// MS601
		String[] deviceModelArray = this.getResources().getStringArray(
				R.array.equipment_type);
		if (Arrays.toString(deviceModelArray).contains(deviceModel)&&isFirstStart) {
			// Android 某些型号需要重启
			//SDKInitializer.initialize(getApplicationContext());
			if (reStartOS()) {
				return true;
			}
		}
		
		return false;
	}
	
	private void DisposeDataThread(){
		new Thread(){
			@Override
			public void run() {
				// 读取XST的配置
				ac.readXSTKeyConfig();
				
				// 读取两票模块是否可见
				LoadAppConfigHelper.getAppConfigHelper(
						AppConst.AppConfigType.TwoBill.toString()).LoadConfigByType();
				// 读取检修模块是否可见
				LoadAppConfigHelper.getAppConfigHelper(
						AppConst.AppConfigType.Overhaul.toString()).LoadConfigByType();
				// 为上位机配置，APP中不可见的配置项赋值
				LoadAppConfigHelper.getAppConfigHelper(
						AppConst.AppConfigType.Invisible.toString()).LoadConfigByType();

				shieldOSSettingTime();
				
				// 将用户信息从config中移出到user_config中
				String Userconfig = AppConst.XSTConfigFilePath() + "/user_config";
				File file = new File(Userconfig);
				if (!file.exists()) {
					ac.newUserConfig();
				}

				/**
				 * 兼容之前的版本，UserList.xml改名NewUserList.xml且不加密
				 */
				File userFile = new File(AppConst.XSTBasePath() + AppConst.UserXmlFile);
				File newUserFile = new File(AppConst.XSTBasePath()
						+ AppConst.NewUserXmlFile);
				String tempUserList = AppConst.XSTBasePath() + AppConst.tempUserXmlFile;
				if (userFile.exists() && !newUserFile.exists()) {
					FileUtils.copyFile(userFile.toString(), newUserFile.toString());
				}

				if (newUserFile.exists()
						&& FileEncryptAndDecrypt.readFileLastByte(
								newUserFile.toString(), 5)) {
					try {
						FileEncryptAndDecrypt.decrypt(newUserFile.toString(),
								tempUserList);
						FileUtils.copyFile(tempUserList, newUserFile.toString());
						(new File(tempUserList)).delete();
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}

				int hisDataSavedDays = Integer.parseInt(AppContext
						.getHisDataSavedDays());
				String beforeDate = DateTimeHelper.beforeDate(hisDataSavedDays);

				// 大于设置的天数，将历史数据清除
				if (DJHisDataHelper.GetIntance().checkXJHisDataExist(AppStart.this)) {
					// 删除过期历史数据
					DJHisDataHelper.GetIntance().clearHisData(AppStart.this, beforeDate);
				}
				// 删除过期操作日志
				File operateFile = new File(AppConst.XSTLogFilePath());
				OperatingConfigHelper.getInstance().deleteOverdueLog(operateFile,
						beforeDate);
				
				redirectTo();
			}
		}.start();
	}

	// MS601 7.0版本升级AP第一次需要重启OS
	private boolean reStartOS() {
		LayoutInflater factory = LayoutInflater.from(AppStart.this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(AppStart.this)
				.builder()
				.setTitle(getString(R.string.system_notice))
				.setView(view)
				.setMsg(getString(R.string.restart_dialog_msg))
				.setPositiveButton(getString(R.string.restart_dialog_btn_ok),
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Editor editor = sp.edit();
								editor.putBoolean("isFirstStart", false);
								editor.commit();
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_REBOOT);
								intent.putExtra("nowait", 1);
								intent.putExtra("interval", 1);
								//intent.putExtra("startTime", 1);
								intent.putExtra("window", 0);
								sendBroadcast(intent);
							}
						})
				.setNegativeButton(
						getString(R.string.restart_dialog_btn_cancel),
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								AppManager.getAppManager().AppExit(
										AppStart.this);
							}
						}).setCancelable(false)
				.setCanceledOnTouchOutside(false).show();
		return true;
	}

	// @Override
	// public Object onRetainNonConfigurationInstance() {
	// final AppStart data = instance;
	// return data;
	// }

	/**
	 * 检查是否需要换图片
	 * 
	 * @param view
	 */
	private void check(FrameLayout view) {
		String path = FileUtils.getAppCache(this, "welcomeback");
		List<File> files = FileUtils.listPathFiles(path);
		if (!files.isEmpty()) {
			File f = files.get(0);
			long time[] = getTime(f.getName());
			long today = StringUtils.getToday();
			if (today >= time[0] && today <= time[1]) {
				view.setBackgroundDrawable(Drawable.createFromPath(f
						.getAbsolutePath()));
			}
		}
	}

	/**
	 * 分析显示的时间
	 * 
	 * @param time
	 * @return
	 */
	private long[] getTime(String time) {
		long res[] = new long[2];
		try {
			time = time.substring(0, time.indexOf("."));
			String t[] = time.split("-");
			res[0] = Long.parseLong(t[0]);
			if (t.length >= 2) {
				res[1] = Long.parseLong(t[1]);
			} else {
				res[1] = Long.parseLong(t[0]);
			}
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * 跳转到...
	 */
	private void redirectTo() {

		if(!SkipYN){
			SkipYN=true;
			return;
		}
		final String factorynameString = this
				.getString(R.string.pda_product_factory_info);
		try {
			final Integer currVerCode = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;
			final Integer preVerCode = Integer.valueOf(AppContext
					.getPreVersionCode());
			if (currVerCode > preVerCode && preVerCode != 0) {
				// APP升级后，首次进入需做的操作
				mHandler = new Handler() {
					public void handleMessage(Message msg) {
						if (loading != null)
							loading.dismiss();
						if (msg.what == 1) {
							AppContext.isNowUpdate = true;
							if (preVerCode <= 0)
								AppContext.isNewInstall = true;
							ac.setConfigPreVersionCode(String
									.valueOf(currVerCode));
						}
						doSomeThingFinishedAndContinue(factorynameString);
					}
				};
				this.doSomeThingAfterUpdateAPPThread();
			} else {
				doSomeThingFinishedAndContinue(factorynameString);
			}
		} catch (NameNotFoundException e) {
			doSomeThingFinishedAndContinue(factorynameString);
		}

	}

	/**
	 * 处理事情完成后，继续的操作
	 */
	private void doSomeThingFinishedAndContinue(String factorynameString) {
		if (AppContext.debug) {
			gotoApp();
		} else if (!AppContext.getFactoryName().equals(
				factorynameString.toUpperCase()))
			UIHelper.ToastMessage(this, R.string.pda_checkhardwave_info);
		else {
			gotoApp();
		}
		AppManager.getAppManager().finishActivity(this);
	}

	private void gotoApp() {
		Intent intent = new Intent(AppStart.this, Main_Page.class);
		startActivity(intent);
	}

	/**
	 * 升级APP后初次进入APP时处理的事情
	 */
	private void doSomeThingAfterUpdateAPPThread() {
		loading = new LoadingDialog(this);
		loading.setCancelable(false);
		loading.show();
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					doSomeThingAfterUpdateAPP();
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

	/**
	 * 升级APP后初次进入APP时处理的事情
	 */
	private void doSomeThingAfterUpdateAPP() {
		try {
			// Debug时不清理
			// if (AppContext.debug)
			// return;
			// 升级完成后清理路线数据
			if (FileUtils.checkFilePathExists(AppConst.XSTDBPath()))
				FileUtils.clearFileWithPath(AppConst.XSTDBPath());
			// 删除路线列表
			if (FileUtils.checkFileExists(AppConst.XSTBasePath()
					+ AppConst.DJLineXmlFile))
				FileUtils.deleteFileWithPath(AppConst.XSTBasePath()
						+ AppConst.DJLineXmlFile);
			if (FileUtils.checkFileExists(AppConst.XSTBasePath()
					+ AppConst.DJLineTempXmlFile))
				FileUtils.deleteFileWithPath(AppConst.XSTBasePath()
						+ AppConst.DJLineTempXmlFile);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void changeAppLanguage() {
		String sta = AppContext.getAppLanguage();
		// 本地语言设置
		Locale myLocale = new Locale(sta);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
	}

	private void shieldOSSettingTime() {
		// 屏蔽OS设置时间
		if (AppContext.getShieldOSSetTimeYN() && AppConst.MS600YN()) {
			ContentResolver resolver = AppStart.this.getContentResolver();
			String modifytimeV = Settings.System.getString(resolver,
					"modifytime");
			if (modifytimeV.equalsIgnoreCase("Yes")) {
				try {
					String db_name = Environment.getDownloadCacheDirectory()
							.getPath() + "/androidsqlite1.db";
					String table_name1 = "deviceconfig";
					int ret;
					SQLiteDatabase db;
					db = SQLiteDatabase.openOrCreateDatabase(db_name, null);
					ContentValues cv = new ContentValues();
					cv.put("Name", "modifytime");
					cv.put("Value", "No");
					ret = db.update(table_name1, cv, "Name='modifytime'", null);
					db.close();
					if (ret > 0) {
						Settings.System.putString(resolver, "modifytime", "No");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}