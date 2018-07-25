/**
 * 
 */
package com.moons.xst.track.pad_ui;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.pad_ui.activity.Pad_Home;
import com.moons.xst.track.ui.BaseActivity;
import com.moons.xst.track.widget.LoadingDialog;

/**
 * @author Administrator
 * 
 */
public class PADStart extends BaseActivity {

	public static PADStart instance = null;
	private static final String TAG = "AppStart";
	AppContext ac = (AppContext) getApplication();
	TextView mVersion, app_name;
	private Handler mHandler;
	private LoadingDialog loading;
	View view = null;
	FrameLayout wellcome;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		final PADStart data = (PADStart) getLastNonConfigurationInstance();
		view = View.inflate(this, R.layout.pad_start, null);
		wellcome = (FrameLayout) view.findViewById(R.id.app_start_view);
		ac = (AppContext) getApplication();
		// 获取客户端版本信息
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			mVersion = (TextView) view.findViewById(R.id.about_version);
			mVersion.setText("Version：" + info.versionName);
			app_name = (TextView) view.findViewById(R.id.start_app_name);
			app_name.setText(R.string.main_about_appnamePAD);

		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		check(wellcome);
		setContentView(view);

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

		// 兼容低版本cookie（1.5版本以下，包括1.5.0,1.5.1）
		AppContext appContext = (AppContext) getApplication();
		String cookie = appContext.getProperty("cookie");
		if (StringUtils.isEmpty(cookie)) {
			String cookie_name = appContext.getProperty("cookie_name");
			String cookie_value = appContext.getProperty("cookie_value");
			if (!StringUtils.isEmpty(cookie_name)
					&& !StringUtils.isEmpty(cookie_value)) {
				cookie = cookie_name + "=" + cookie_value;
				appContext.setProperty("cookie", cookie);
				appContext.removeProperty("cookie_domain", "cookie_name",
						"cookie_value", "cookie_version", "cookie_path");
			}
		}
		instance = this;
	}

//	@Override
//	public Object onRetainNonConfigurationInstance() {
//		final PADStart data = instance;
//		return data;
//	}

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
		final String factorynameString = this
				.getString(R.string.pda_product_factory_info);
		try {
			final Integer currVerCode = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;
			final Integer preVerCode = Integer.valueOf(ac.getPreVersionCode());
			if (currVerCode > preVerCode) {
				// APP升级后，首次进入需做的操作
				mHandler = new Handler() {
					public void handleMessage(Message msg) {
						if (loading != null)
							loading.dismiss();
						if (msg.what == 1) {
							AppContext.isNowUpdate = true;
							if (preVerCode <= 0)
								AppContext.isNewInstall = true;
							ac.setPreVersionCode(String.valueOf(currVerCode));
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
			Intent intent = new Intent(this, Pad_Home.class);
			startActivity(intent);
		} else if (!AppContext.getFactoryName().equals(
				factorynameString.toUpperCase()))
			UIHelper.ToastMessage(this, R.string.pda_checkhardwave_info);
		else {
			Intent intent = new Intent(this, Pad_Home.class);
			startActivity(intent);
		}
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
}
