package com.moons.xst.track;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.moons.xst.track.bean.AccessInfo;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.StringUtils;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
@SuppressLint("NewApi")
public class AppConfig {

	private final static String APP_CONFIG_TAG = "App_Config";
	private final static String USER_CONFIG_TAG="User_Config";
	
	private final static String APP_CONFIG = "config";
	private final static String USER_CONFIG="user_config";

	public final static String TEMP_MESSAGE = "temp_message";
	public final static String TEMP_COMMENT = "temp_comment";
	public final static String TEMP_POST_TITLE = "temp_post_title";
	public final static String TEMP_POST_CATALOG = "temp_post_catalog";
	public final static String TEMP_POST_CONTENT = "temp_post_content";

	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	public final static String CONF_COOKIE = "cookie";
	public final static String CONF_ACCESSTOKEN = "accessToken";
	public final static String CONF_ACCESSSECRET = "accessSecret";
	public final static String CONF_EXPIRESIN = "expiresIn";
	public final static String CONF_LOAD_IMAGE = "perf_loadimage";
	public final static String CONF_SCROLL = "perf_scroll";
	public final static String CONF_HTTPS_LOGIN = "perf_httpslogin";
	public final static String CONF_VOICE = "perf_voice";
	public final static String CONF_LASTRESULT = "pref_lastresult";
	public final static String CONF_CHECKUP = "perf_checkup";
	public final static String CONF_UPLOADFILE = "perf_uploadfile";
	public final static String CONF_GPS = "perf_gps";
	public final static String CONF_DOPLANBYIDPOS = "perf_doplanbyidpos";
	public final static String CONF_LOGINTYPE = "perf_logintype";
	public final static String CONF_PreVersionCode = "perf_preversioncode";
	public final static String CONF_WAKELOCK="perf_wakelock";

	public final static String SAVE_IMAGE_PATH = "save_image_path";
	public final static String CONF_WSADDRESS = "Conf_WSAddress";
	public final static String CONF_WSADDRESSFORWLAN = "Conf_WSAddressForWlan";
	public final static String CONF_WSADDRESSFOROTHER = "Conf_WSAddressForOther";
	public final static String CONF_XJQCD = "Conf_xjqCD";
	public final static String CONF_UPDATE_SYS_DATE_TYPE = "Conf_updateDateType";
	public final static String CONF_MEASURE_TYPE = "Conf_measureType";
	public final static String CONF_LOCATIONINFO = "Conf_LocationInfo";
	public final static String CONF_COMMUNICATION_TYPE = "Conf_communicationType";

	public final static String CONF_WIFICLOSEYN = "pref_wificloseyn";
	public final static String CONF_GPSCLOSEYN = "pref_gpscloseyn";

	public final static String CONF_VIDEOQUALITY = "pref_videoquality";
	public final static String CONF_JITUPLOAD = "pref_jitupload";
	public final static String CONF_FONTSIZE = "pref_fontSize";

	public final static String CONF_JITUPLOADXDJ = "pref_jituploadxdj";
	public final static String CONF_JITUPLOADXX = "pref_jituploadxx";
	
	public final static String CONF_JITUPLOADXDJ_ONCE = "conf_jituploadxdjoncenumber";
	public final static String CONF_JITUPLOADXDJ_ONCE_TIME = "conf_jituploadxdjonceuploadtime";
	
	public final static String CONF_JITUPLOADXX_ONCE_NUMBER = "conf_jituploadxxoncenumber";
	public final static String CONF_JITUPLOADXX_ONCE_TIME = "conf_jituploadxxonceuploadtime";
	public final static String CONF_LOCATION_TIME_SPASE = "location_timespase";
	public final static String CONF_LOCATION_GPS_MODE = "gpsmode_checkedRadioButtonId";
	public final static String CONF_LOCATION_CHECKED_TIME_SPASE = "conf_locationcheckedtimespase";
	public final static String CONF_LOCATION_CHECKED_DISTANCE_SPASE = "conf_locationcheckeddistancespase";
	public final static String CONF_SHORTCUT_FUNCTION = "Custom";
	public final static String CONF_XDJRECORDJW = "conf_xdjrecordjw";
	
	public final static String CONF_SYS_HISDATASAVEDDAYS = "conf_sys_hisdatasaveddays";
	public final static String CONFIG_LANGUAGE = "Conf_language";
	public final static String CONFIG_OUTER_MEASURETYPE = "Conf_outermeasuretype";
	public final static String CONF_PLAN_TYPE = "Conf_PlanType";
	
	@SuppressLint("NewApi")
	public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "XST"
			+ File.separator;
	
	public final static String DEFAULT_CONF_PREVERSIONCODE = "0";
	public final static String DEFAULT_CONF_APPLANGUAGE = "zh";
	public final static String DEFAULT_CONF_COMMUNICATIONTYPE = "0";
	public final static String DEFAULT_CONF_XJQCD = "";
	public final static boolean DEFAULT_CONF_VOICE = false;
	public final static boolean DEFAULT_CONF_LASTRESULT = false;
	public final static boolean DEFAULT_CONF_CHECKUP = true;
	public final static boolean DEFAULT_CONF_UPLOADFILE = false;
	public final static boolean DEFAULT_CONF_OPENWAKELOCK = false;
	public final static boolean DEFAULT_CONF_JITUPLOAD = false;
	public final static boolean DEFAULT_CONF_JITUPLOADXDJ = false;
	public final static int DEFAULT_CONF_JITUPLOADXDJONCENUM = 10;
	public final static int DEFAULT_CONF_JITUPLOADXDJONCETIME = 60;
	public final static boolean DEFAULT_CONF_JITUPLOADXX = false;
	public final static String DEFAULT_CONF_VIDEOQUALITY = "LOW";
	public final static String DEFAULT_CONF_FONTSIZE = "normal";
	public final static String DEFAULT_CONF_MEASURETYPE = "0";
	public final static String DEFAULT_CONF_UPDATEDATETYPE = "1";
	public final static boolean DEFAULT_CONF_GPSOPEN = true;
	public final static int DEFAULT_CONF_GPSSAVEDMODE = 0;
	public final static int DEFAULT_CONF_GPSSAVEDTIMESPACE = 5;
	public final static String DEFAULT_CONF_WSADDRESS = "";
	public final static String DEFAULT_CONF_HISDATASAVEDDAYS = "31";
	public final static int DEFAULT_CONF_ONLOCATIONTIMESPACE = 1;
	public final static int DEFAULT_CONF_ONLOCATIONDISTANCESPACE = 3000;
	public final static String DEFAULT_CONF_OUTER_MEASURETYPE = "MS-101";
	public final static String DEFAULT_CONF_PLANTYPE = "0";// 0巡点检，1点检排程
	public final static boolean DEFAULT_CONF_XDJRECORDJW = true;
	public final static String SIGN_SWIPEMENU_DOWNLOAD="COMMDOWNLOAD";
	public final static String USB_SIGN_SWIPEMENU_DOWNLOAD="USBCOMMDOWNLOAD";
	
	private Context mContext;
	private AccessInfo accessInfo = null;
	private static AppConfig appConfig;

	public static AppConfig getAppConfig(Context context) {
		if (appConfig == null) {
			appConfig = new AppConfig();
			appConfig.mContext = context;
		}
		return appConfig;
	}

	/**
	 * 获取Preference设置
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 是否加载显示文章图片
	 */
	public static boolean isLoadImage(Context context) {
		return getSharedPreferences(context).getBoolean(CONF_LOAD_IMAGE, true);
	}

	public String getCookie() {
		return get(CONF_COOKIE);
	}

	public void setAccessToken(String accessToken) {
		set(CONF_ACCESSTOKEN, accessToken);
	}

	public String getAccessToken() {
		return get(CONF_ACCESSTOKEN);
	}

	public void setAccessSecret(String accessSecret) {
		set(CONF_ACCESSSECRET, accessSecret);
	}

	public String getAccessSecret() {
		return get(CONF_ACCESSSECRET);
	}

	public void setExpiresIn(long expiresIn) {
		set(CONF_EXPIRESIN, String.valueOf(expiresIn));
	}

	public long getExpiresIn() {
		return StringUtils.toLong(get(CONF_EXPIRESIN));
	}

	public void setAccessInfo(String accessToken, String accessSecret,
			long expiresIn) {
		if (accessInfo == null)
			accessInfo = new AccessInfo();
		accessInfo.setAccessToken(accessToken);
		accessInfo.setAccessSecret(accessSecret);
		accessInfo.setExpiresIn(expiresIn);
		// 保存到配置
		this.setAccessToken(accessToken);
		this.setAccessSecret(accessSecret);
		this.setExpiresIn(expiresIn);
	}

	public AccessInfo getAccessInfo() {
		if (accessInfo == null && !StringUtils.isEmpty(getAccessToken())
				&& !StringUtils.isEmpty(getAccessSecret())) {
			accessInfo = new AccessInfo();
			accessInfo.setAccessToken(getAccessToken());
			accessInfo.setAccessSecret(getAccessSecret());
			accessInfo.setExpiresIn(getExpiresIn());
		}
		return accessInfo;
	}
	
	public Properties getAll() {
		return get();
	}

	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	public Properties get() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 读取files目录下的config
			// fis = activity.openFileInput(APP_CONFIG);

			// 读取app_config目录下的config
			// File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File dirConf = new File(AppConst.XSTConfigFilePath());
			String configPath = dirConf.getPath() + File.separator
					+ APP_CONFIG;
			if (new File(configPath).exists()) {
				fis = new FileInputStream(configPath);
				props.load(fis);
			}
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}
	
	public String getUser(String key) {
		Properties props = getUser();
		return (props != null) ? props.getProperty(key) : null;
	}
	
	public Properties getUser() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 读取files目录下的config
			// fis = activity.openFileInput(APP_CONFIG);

			// 读取app_config目录下的config
			// File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File dirConf = new File(AppConst.XSTConfigFilePath());
			String configUserPath = dirConf.getPath() + File.separator
					+ USER_CONFIG;
			if (new File(configUserPath).exists()) {
				fis = new FileInputStream(configUserPath);
				props.load(fis);
			}
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}
	//user
	public void setUserProps(Properties p){
		FileOutputStream fos = null;
		try {
			File dirConf = new File(AppConst.XSTConfigFilePath());
			File conf = new File(dirConf, USER_CONFIG);
			fos = new FileOutputStream(conf);

			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	private  void setProps(Properties p) {
		FileOutputStream fos = null;
		try {
			// 把config建在files目录下
			// fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

			// 把config建在(自定义)app_config的目录下
			// File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File dirConf = new File(AppConst.XSTConfigFilePath());
			File conf = new File(dirConf, APP_CONFIG);
			fos = new FileOutputStream(conf);

			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public void set(Properties ps) {
		Properties props = get();
		props.putAll(ps);
		setProps(props);
	}

	public  void set(String key, String value) {
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}
	
	public void setAll(Properties ps) {
		setProps(ps);
	}

	public void remove(String... key) {
		Properties props = get();
		for (String k : key)
			props.remove(k);
		setProps(props);
	}
	//user
	public void setUser(Properties ps) {
		Properties props = getUser();
		props.putAll(ps);
		setUserProps(props);
	}
	//user
	public  void setUser(String key, String value) {
		Properties props = getUser();
		props.setProperty(key, value);
		setUserProps(props);
	}
	//user
	public void removeUser(String... key){
		Properties props = getUser();
		for (String k : key)
			props.remove(k);
		setUserProps(props);
	}
}
