package com.moons.xst.track;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.HexUtils;
import com.moons.xst.track.common.StringUtils;

/**
 * 常量定义类
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-23
 */
@SuppressLint("NewApi")
public class AppConst {
	/**
	 * APP功能配置
	 * 
	 * @author LKZ
	 * 
	 */
	public static class AppNameConfig {
		/**
		 * 巡线
		 */
		public final static Integer AppName_Track = 0;
		/**
		 * 巡点检
		 */
		public final static Integer AppName_XDJ = 1;
		/**
		 * 全功能
		 */
		public final static Integer AppName_ALL = 2;
		/**
		 * 平板系统
		 */
		public final static Integer AppName_PAD = 3;

	}
	
	public static final String CONST_OS_VERSION = "2018-02-01";

	// 程序版本
	public final String CONST_APP_VERSION = "1.0";
	// 数据库版本
	public final String CONST_DATABASE_VERSION = "1.0";
	// 小神探内部密码
	public static String CONST_XST_PASSWORD = HexUtils.toOtherBaseString(Long
			.parseLong(DateTimeHelper.getDateTimeNow3()), 12);
	// 数据库文件目录

	public final static String CONST_DATABASE_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "XSTDB"
			+ File.separator;
	// 数据库名称
	public final String CONST_DATABASE_NAME_COMMON = "XSTCOMMON";
	public final String CONST_DATABASE_NAME_DJLINE_PREFIX = "XSTDJLINE-";

	// ID位置到位状态
	public final static char IDPOS_STATUS_FINISHED = 'C';// 到位并无漏检
	public final static char IDPOS_STATUS_NOTFINISHED = 'H';// 到位但有漏检
	public final static char IDPOS_STATUS_NEEDED = 'O';// 未到位
	public final static char IDPOS_STATUS_NOTNEEDED = 'X';// 无需到位
	public final static char IDPOS_STATUS_NULL = 'N';// 未标注

	// 点检计划数据类型
	public final static String DJPLAN_DATACODE_GC = "GC";// 观察类
	public final static String DJPLAN_DATACODE_JL = "JL";// 记录类
	public final static String DJPLAN_DATACODE_CW = "CW";// 测温类
	public final static String DJPLAN_DATACODE_CZ = "CZ";// 测振类
	public final static String DJPLAN_DATACODE_CS = "CS";// 测速类

	public final static String DJPLAN_DATACODE_GC_DESC = "观察类";
	public final static String DJPLAN_DATACODE_JL_DESC = "记录类";
	public final static String DJPLAN_DATACODE_CW_DESC = "测温类";
	public final static String DJPLAN_DATACODE_CZ_DESC = "测振类";
	public final static String DJPLAN_DATACODE_CS_DESC = "测速类";
	public final static int DJPLAN_ONCE_MAX_UNMBER = 30;// 点检单次上传最大条数
	public final static int XXPLAN_ONCE_MAX_UNMBER = 50;// 巡线单次上传最大条数
	public final static int DJPLAN_DEFAULT_ONCE_UNMBER = 10;// 点检单次默认上传条数
	public final static int XXPLAN_DEFAULT_ONCE_UNMBER = 10;// 巡线单次默认上传条数
	public final static int DJPLAN_DEFAULT_ONCE_UPLOAD_TIME = 60;// 点检单次默认上传时间
	public final static int XXPLAN_DEFAULT_ONCE_UPLOAD_TIME = 60;// 巡线单次默认上传时间
	// 报警等级
	public final static String AlarmName_Dangerous = "危险";
	public final static String AlarmName_Alert = "报警";
	public final static String AlarmName_Warning = "警告";
	public final static String AlarmName_Pre_Alarm = "预警";

	// 校准时间方式
	public final static String UpdateSysDate_NONE = "0";
	public final static String UpdateSysDate_Commu = "1";
	/*public final static String UpdateSysDate_GPS = "1";
	public final static String UpdateSysDate_WEB = "2";*/
	

	// 测量设备类型
	public final static String MeasureType_Inner = "0";
	public final static String MeasureType_Outer = "1";

	public final static String CommunicationType_Wireless = "0";
	public final static String CommunicationType_USB = "1";
	
	public final static String PlanType_XDJ = "0";
	public final static String PlanType_DJPC = "1";
	
	public final static String DJPCCommType_common="2641";
	public final static String DJPCCommType_huarun="华润接口";

	// 巡线使用地图类型
	public final static String MapType_Baidu = "百度地图";

	// 启停点串长度
	public final static int SR_STRINGLENGTH = 8;
	
	//操作日志提示信息
	public final static String CONNECTIONTYPE_WIFI="WiFi通信";
	public final static String CONNECTIONTYPE_USB="USB通信";
	
	public final static String LOGSTATUS_NORMAL = "0";
	public final static String LOGSTATUS_ERROR = "1";
	
	public final static String COMMTYPE_USERLOGIN = "用户登录";
	
	public final static String COMMTYPE_OPERATEBILL_DOWNLOAD="操作票下载";
	public final static String COMMTYPE_OPERATEBILL_UPLOADING="操作票上传";
	
	public final static String COMMTYPE_OVERHAUL_DOWNLOAD = "检修管理下载";	
	public final static String COMMTYPE_OVERHAUL_UPLOADING = "检修计划上传";
	
	public final static String COMMTYPE_WORKBILL_DOWNLOAD = "工作票下载";
	public final static String COMMTYPE_WORKBILL_UPLOADING = "工作票上传";	
	
	public final static String ANDROID_DATA_STANDARD_ERROR = "标准化失败";
	
	public final static String ANDROID_DATA_UPLOADING="数据上传";	
	public final static String ANDROID_DATA_DOWNLOAD="数据下载";
	
	public final static String ANDROID_DATA_JITUPLOAD = "实时上传";
	public final static String ANDROID_JITSETTING_ON = "实时上传开启";
	public final static String ANDROID_JITSETTING_OFF = "实时上传关闭";
	
	public final static String COMMTYPE_APKUPGRADE="安卓APK升级";

	// 硬件配置文件默认内容
	public final static String HardwareConfigContents = "<?xml version=\"1.0\"?>"
			+ "\r\n"
			+ "<Configuration>"
			+ "\r\n"
			+ "<Setting Name=\"temperature\" Values=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"vibration\" Values=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"speed\" Values=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"walkietalkie\" Values=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"rfid\" Values=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"rotateCamera\" Values=\"false\" />"
			+ "\r\n"
			+ "</Configuration>";

	// 测温配置文件默认内容
	public final static String TemperatureConfigContents = "<?xml version=\"1.0\"?>"
			+ "\r\n"
			+ "<Configuration>"
			+ "\r\n"
			+ "<Setting Name=\"FSL\" Using=\"95\" />"
			+ "\r\n"
			+ "<Setting Name=\"SaveType\" Using=\"Current\" />"
			+ "\r\n"
			+ "</Configuration>";

	// 测振配置文件默认内容
	public final static String VibrationConfigContents = "<?xml version=\"1.0\"?>"
			+ "\r\n"
			+ "<Configuration>"
			+ "\r\n"
			+ "<Setting Name=\"vibrationPackage\" Using=\"A\" />"
			+ "\r\n"
			+ "<Setting Name=\"samplePoints\" Using=\"4096\" />"
			+ "\r\n"
			+ "<Setting Name=\"maxAnaFreq\" Using=\"5000\" />"
			+ "\r\n"
			+ "<Setting Name=\"fftLines\" Using=\"1600\" />"
			+ "\r\n"
			+ "<Setting Name=\"windowType\" Using=\"2\" />"
			+ "\r\n"
			+ "<Setting Name=\"trigerType\" Using=\"0\" />"
			+ "\r\n"
			+ "<Setting Name=\"signalType\" Using=\"A\" />"
			+ "\r\n"
			+ "<Setting Name=\"averNum\" Using=\"1\" />"
			+ "\r\n"
			+ "<Setting Name=\"averWrap\" Using=\"2\" />"
			+ "\r\n"
			+ "<Setting Name=\"mode\" Using=\"0\" />"
			+ "\r\n"
			+ "<Setting Name=\"coeff\" Using=\"-1\" />"
			+ "\r\n"
			+ "<Setting Name=\"bandWidth\" Using=\"0xc0000\" />"
			+ "\r\n"
			+ "</Configuration>";

	// 外接测振配置文件默认内容
	public final static String VibrationConfigContentsForOuter = "<?xml version=\"1.0\"?>"
			+ "\r\n"
			+ "<Configuration>"
			+ "\r\n"
			+ "<Setting Name=\"vibrationType\" Using=\"A\" />"
			+ "\r\n"
			+ "</Configuration>";

	// 对讲机配置文件默认内容
	public final static String WalkieTalkieConfigContents = "<?xml version=\"1.0\"?>"
			+ "\r\n"
			+ "<Configuration>"
			+ "\r\n"
			+ "<Setting Name=\"1\" Frequency=\"460.5\" Checked=\"true\" />"
			+ "\r\n"
			+ "<Setting Name=\"2\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"3\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"4\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"5\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"6\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"7\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"8\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"9\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n"
			+ "<Setting Name=\"10\" Frequency=\"460.5\" Checked=\"false\" />"
			+ "\r\n" + "</Configuration>";

	// 外接测量配置文件默认内容
	public final static String MeasureTypeForOuterConfigContents = "<?xml version=\"1.0\"?>"
			+ "\r\n"
			+ "<Configuration>"
			+ "\r\n"
			+ "<Setting Name=\"Temperature\" BTAddress=\"\" Password=\"\" />"
			+ "\r\n"
			+ "<Setting Name=\"Vibration\" BTAddress=\"\" Password=\"\" />"
			+ "\r\n" + "</Configuration>";

	// 手势密码配置文件内容
	public final static String GestureConfigContents = "<?xml version=\"1.0\"?>"
			+ "\r\n" + "<Configuration>" + "\r\n" + "</Configuration>";

	/**
	 * 获取外置SD卡跟目录
	 * 
	 * @return
	 */
	public static String ExSDCardBasePath() {
		return FileUtils.getExternalSDRoot();
	}

	/**
	 * SD卡路径
	 */
	public static String SDCardBasePath() {
		return FileUtils.getSDRoot();
	}

	/**
	 * 小神探数据根目录
	 */
	public static String XSTBasePath() {
		String tempPathString = SDCardBasePath() + "/XST/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 小神探文件压缩后的目录
	 */
	public static String XSTZipFilePath() {
		String tempPathString = SDCardBasePath() + "/XSTZip/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	public static String XSTConfigFilePath() {
		String tempPathString = XSTBasePath() + "/XSTConfig/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 临时任务目录
	 */
	public static String TempTaskPath() {
		String tempPathString = XSTBasePath() + "/TempTask/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}
	
	/**
	 * 历史查询记录存放文件目录
	 */
	public static String HistoryQueryPath(){
		String tempPathString = XSTBasePath() + "history_query.txt";
		File file = new File(tempPathString);
		if(!file.exists()){
			try {
				file.createNewFile();  
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 下载库存放目录
	 */
	public static String XSTDBPath() {
		String tempPathString = XSTBasePath() + "/XSTDB/";
		File file = new File(tempPathString);
		// 如果是文件就删除
		if (file.isFile()) {
			file.delete();
		}
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 结果存放目录
	 */
	public static String XSTResultPath() {
		String tempPathString = XSTBasePath() + "/PDAResultFile/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 实时上传结果存放目录
	 * 
	 * @return
	 */
	public static String XSTResultPathJIT() {
		String tempPathString = XSTBasePath() + "/PDAResultFileForJIT/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 压缩结果文件存放路径
	 * 
	 * @return
	 */
	public static String XSTZipResultPath() {
		String tempPathString = XSTBasePath() + "/PDAZipResultFile/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}
	
	/**
	 * 错误的结果文件备份存放路径
	 * @return
	 */
	public static String XSTErrorDBBackPath() {
		String tempPathString = XSTBasePath() + "/PDAErrorResultFileBack/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 临时照片文件存放路径
	 * 
	 * @return
	 */
	public static String XSTTempPicPath() {
		String tempPathString = XSTBasePath() + "/PicPath/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	public static String XSTTempImagePath() {
		String tempPathString = XSTResultPath() + "/0/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	public static String XSTTempVideosPath() {
		String tempPathString = XSTResultPath() + "/0/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	public static String XSTTempThumbnailImagePath() {
		String tempPathString = XSTTempPicPath() + "/Thumbnail/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	public static String XSTTempRecordPath() {
		String tempPathString = XSTBasePath() + "/RecordPath/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 临时录像文件存放路径
	 * 
	 * @return
	 */
	public static String XSTTempVideoPath() {
		String tempPathString = XSTBasePath() + "/VideoFiles/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 临时任务照片文件存放路径
	 * 
	 * @return
	 */
	public static String TempTaskPhotoPath() {
		String tempPathString = TempTaskPath() + "/photo/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 临时任务录音文件存放路径
	 * 
	 * @return
	 */
	public static String TempTaskAudioPath() {
		String tempPathString = TempTaskPath() + "/audio/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 临时任录像文件存放路径
	 * 
	 * @return
	 */
	public static String TempTaskVedioPath() {
		String tempPathString = TempTaskPath() + "/vedio/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 硬件配置文件路径
	 * 
	 * @return
	 */
	public static String HardwareConfigPath() {
		String tempPathString = XSTBasePath() + "HardwareConfig.xml";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.createNewFile();
			} catch (Exception e) {
			}
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(HardwareConfigContents);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempPathString;
	}

	/**
	 * 测温配置文件路径，如果不存在，则创建，填入默认值
	 * 
	 * @return
	 */
	public static String TemperatureConfigPath() {
		String tempPathString = XSTBasePath() + "TemperatureConfig.xml";
		File dir = new File(tempPathString);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(TemperatureConfigContents);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempPathString;
	}

	/**
	 * 外接测温配置文件路径，如果不存在，则创建，填入默认值
	 * 
	 * @return
	 */
	public static String OuterTemperatureConfigPath() {
		String tempPathString = XSTBasePath() + "OuterTemperatureConfig.xml";
		File dir = new File(tempPathString);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(TemperatureConfigContents);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempPathString;
	}
	public static void OuterTemperatureConfigPathOnError() {
		String tempPathString = XSTBasePath() + "OuterTemperatureConfig.xml";
			
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(TemperatureConfigContents);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
	}

	/**
	 * 测振配置文件路径，如果不存在，则创建，填入默认值
	 * 
	 * @return
	 */
	public static String VibrationConfigPath() {
		String tempPathString = XSTBasePath() + "/VibrationConfig.xml";
		File dir = new File(tempPathString);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(VibrationConfigContents);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempPathString;
	}

	/**
	 * 测振配置文件路径，如果不存在，则创建，填入默认值
	 * 
	 * @return
	 */
	public static String OuterVibrationConfigPath() {
		String tempPathString = XSTBasePath() + "/OuterVibrationConfig.xml";
		File dir = new File(tempPathString);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(VibrationConfigContentsForOuter);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempPathString;
	}
	public static void OuterVibrationConfigPathOnError() {
		String tempPathString = XSTBasePath() + "/OuterVibrationConfig.xml";

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(VibrationConfigContentsForOuter);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
	}
	/**
	 * 对讲机配置文件路径，如果不存在，则创建，填入默认值
	 * 
	 * @return
	 */
	public static String WalkieTalkieConfigPath() {
		String tempPathString = XSTBasePath() + "WalkieTalkieConfig.xml";
		File dir = new File(tempPathString);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(WalkieTalkieConfigContents);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempPathString;
	}

	/**
	 * 外接测量配置文件路径，如果不存在，则创建，填入默认值
	 * 
	 * @return
	 */
	public static String MeasureTypeForOuterConfigPath() {
		String tempPathString = XSTBasePath() + "MeasureTypeForOuter.xml";
		File dir = new File(tempPathString);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(MeasureTypeForOuterConfigContents);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempPathString;
	}

	public static void MeasureTypeForOuterConfigPathOnError() {
		String tempPathString = XSTBasePath() + "MeasureTypeForOuter.xml";
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(tempPathString, false);
			bw = new BufferedWriter(fw);
			bw.write(MeasureTypeForOuterConfigContents);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				bw.close();
				fw.close();
			} catch (IOException e1) {
			}
		}
	}

	/**
	 * 手势密码配置文件路径，如果不存在，则创建，填入默认值
	 * 
	 * @return
	 */
	public static String GestureConfigPath() {
		String tempPathString = XSTBasePath() + "GestureConfig.xml";
		File dir = new File(tempPathString);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(tempPathString, false);
				bw = new BufferedWriter(fw);
				bw.write(GestureConfigContents);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempPathString;
	}

	/**
	 * 当前路线结果存放目录
	 */
	public static String CurrentResultPath(int lineID) {
		String tempPathString = XSTResultPath() + resultFileName(lineID);
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 当前路线结果存放目录
	 */
	public static String CurrentResultPath(String filename) {
		String tempPathString = XSTResultPath() + resultFileName(filename);
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 两票文件存放目录
	 */
	public static String TwoTicketRecordPath() {
		String tempPathString = XSTResultPath() + "OperateBill/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				File oldFile = new File(XSTResultPath() + "-5");
				// 版本兼容（判断是否存在-5文件夹，有则直接改名成OperateBill）
				if (oldFile.exists()) {
					FileUtils
							.reNamePath(XSTResultPath() + "-5", tempPathString);
				} else {
					// 没有则按照指定的路径创建文件夹
					file.mkdirs();
				}
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 工作票文件存放目录
	 */
	public static String WorkBillRecordPath() {
		String tempPathString = XSTResultPath() + "WorkBill/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			// 按照指定的路径创建文件夹
			file.mkdirs();
		}
		return tempPathString;
	}

	/**
	 * 数据库损坏文件存放目录
	 * 
	 * @return
	 */
	public static String XSTDamageDB() {
		String tempPathString = XSTBasePath() + "/DamageDBFile/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			// 按照指定的路径创建文件夹
			file.mkdirs();
		}
		return tempPathString;
	}

	/**
	 * 当前路线结果存放目录(实时上传)
	 * 
	 * @param lineID
	 * @return
	 */
	public static String CurrentJITResultPath(int lineID) {
		String tempPathString = XSTResultPathJIT() + resultFileName(lineID);
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 当前路线照片结果存放目录
	 */
	public static String CurrentResultPath_Pic(int lineID) {
		String tempPathString = CurrentResultPath(lineID);
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 当前路线录音结果存放目录
	 */
	public static String CurrentResultPath_Record(int lineID) {
		String tempPathString = CurrentResultPath(lineID);
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 当前路线录像结果存放目录
	 */
	public static String CurrentResultPath_Vedio(int lineID) {
		String tempPathString = CurrentResultPath(lineID);
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 小神探日志存放目录
	 * 
	 * @return
	 */
	public static String XSTLogFilePath() {
		String tempPathString = XSTBasePath() + "/logfile/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	public static String resultFileName(int lineID) {
		return "/" + String.valueOf(lineID) + "/";
	}

	public static String resultFileName(String filename) {
		return "/" + filename + "/";
	}

	/**
	 * 获取根据路线ID生成的计划库名称（格式：424DB_110.sdf）
	 * 
	 * @param lineID
	 * @return
	 */
	public static String PlanDBName(int lineID) {
		return "424DB_" + String.valueOf(lineID) + ".sdf";
	}

	/**
	 * 根据路线ID生成的结果库名称（格式：D110.sdf）
	 * 
	 * @param lineID
	 * @return
	 */
	public static String ResultDBName(int lineID) {
		return "D" + String.valueOf(lineID) + ".sdf";
	}

	public static String ResultDBName(String filename) {
		return filename + ".sdf";
	}

	/**
	 * 离线地图存放路径 只需要把离线地图包放入此路径即可（此路径不能修改，否则离线地图将无法使用） By LKZ
	 * 
	 * @return
	 */
	public static String BaiduMapOffLineDatPath() {
		String tempPathString = SDCardBasePath() + "/BaiduMapSDK/vmp/h/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 离线地图存放路径 只需要把离线地图包放入此路径即可（此路径不能修改，否则离线地图将无法使用） By LKZ
	 * 
	 * @return
	 */
	public static String BaiduMapOffLineDatPath_Ex() {
		String tempPathString = ExSDCardBasePath() + "/BaiduMapSDK/vmp/h/";
		File file = new File(tempPathString);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		return tempPathString;
	}

	/**
	 * 路线列表文件名
	 */
	public static String DJLineXmlFile = "LineList.xml";
	public static String DJLineTempXmlFile = "tempLineList.xml";
	/**
	 * 人员列表文件名
	 */
	public static String UserXmlFile = "UserList.xml";
	public static String NewUserXmlFile = "NewUserList.xml";
	// 解密后临时人员列表文件名
	public static String tempUserXmlFile = "tempUserList.xml";
	// 事件类型列表文件名
	public static String EventTypeXmlFile = "EventTypeList.xml";
	/**
	 * 初始化密码文件名
	 */
	public static String InitPSWXmlFile = "InitPSW.xml";
	// 解密后临时初始化密码文件名
	public static String tempInitPSWXmlFile = "tempInitPSW.xml";
	/**
	 * 节假日文件名
	 */
	public static String WorkDateXmlFile = "WorkDate.xml";
	/**
	 * setting文件名
	 */
	public static String SettingxmlFile = "SettingConfig.xml";
	// 解密后临时setting文件名
	public static String tempSettingxmlFile = "tempSettingConfig.xml";
	/**
	 * 地图版本文件名
	 */
	public static String BaiduMapVersionXML = "BaiduMapVersion.xml";
	/**
	 * GPS工具保存的GPS坐标信息文件
	 */
	public static String GpsInfoFile = "GpsList.xml";

	/**
	 * NFC工具保存的扫描结果信息文件
	 */
	public static String NfcInfoFile = "NfcList.xml";

	/**
	 * 模块授权文件名
	 */
	public static String ModuleSettingxmlFile = "ModuleConfig.xml";
	// 解密后临时模块授权文件名
	public static String tempModuleSettingxmlFile = "tempModuleConfig.xml";

	/**
	 * 版本号
	 * 
	 * @return
	 */
	public static String GetPhoneBuildVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 型号
	 * 
	 * @return
	 */
	public static String GetPhoneBuildModel() {
		return android.os.Build.MODEL;
	}

	private static String bdGetLocationUrl = "http://api.map.baidu.com/geocoder/v2/?ak=B0EODw2uQHztkfgBr8s86exD&output=xml&pois=1&mcode=9E:89:A8:CB:D7:E2:DB:8A:E1:C6:18:44:40:A7:8C:01:4A:A2:82:DD;com.moons.xst.track ";

	public static String getLocationUrl() {
		return bdGetLocationUrl;
	}

	// 判断是否为MS600的机型
	private final static String MS600 = "600";

	public static boolean MS600YN() {
		if (GetPhoneBuildModel().contains(MS600))
			return true;
		return false;
	}

	public static final String MaxAnaFreq_ACC_DefaultValue = "5000Hz";
	public static final String MaxAnaFreq_VEL_DefaultValue = "1000Hz";
	public static final String MaxAnaFreq_DIS_DefaultValue = "100Hz";
	public static final String MaxAnaFreq_ATT_DefaultValue = "200Hz";

	public static final String SamplePoints_ACC_DefaultValue = "4096";
	public static final String SamplePoints_VEL_DefaultValue = "1024";
	public static final String SamplePoints_DIS_DefaultValue = "256";
	public static final String SamplePoints_ATT_DefaultValue = "2048";
	
	public static final String SamplePoints_AVGNUM_DefaultValue = "1";

	public static final String Sensitivity_DefaultValue = "5.102";

	/**
	 * 最大频率
	 */
	private static Map<String, String[]> maxAnaFreqMap = new HashMap();

	public static String[] getVibrationMaxAnaFreq(String key) {
		if (maxAnaFreqMap.size() <= 0) {
			String[] ACC = new String[] { "1000Hz", "2000Hz", "5000Hz", "10000Hz" };
			String[] VEL = new String[] { "200Hz", "500Hz", "1000Hz" };
			String[] DIS = new String[] { "100Hz", "200Hz", "500Hz" };
			String[] ATT = new String[] { "100Hz", "200Hz", "500Hz", "1000Hz" };
			maxAnaFreqMap.put("ACC", ACC);
			maxAnaFreqMap.put("VEL", VEL);
			maxAnaFreqMap.put("DIS", DIS);
			maxAnaFreqMap.put("ATT", ATT);
		}

		return maxAnaFreqMap.get(key);
	}

	/**
	 * 采样点数
	 */
	private static Map<String, String[]> samplePointsMap = new HashMap();

	public static String[] getVibrationSamplePoints(String key) {
		if (samplePointsMap.size() <= 0) {
			String[] ACC = new String[] { "4096", "8192", "16384" };
			String[] VEL = new String[] { "1024", "2048" };
			String[] DIS = new String[] { "256", "512", "1024" };
			String[] ATT = new String[] { "256", "512", "1024", "2048" };
			samplePointsMap.put("ACC", ACC);
			samplePointsMap.put("VEL", VEL);
			samplePointsMap.put("DIS", DIS);
			samplePointsMap.put("ATT", ATT);
		}

		return samplePointsMap.get(key);
	}
	
	/**
	 * 冲击分析带宽
	 */
	private static Map<String, String> bandWidthMap = new HashMap();
	public static String getBandWidth(String key) {
		if (bandWidthMap.size() <= 0) {
			bandWidthMap.put("0x40000", "2KHz~4KHz");
			bandWidthMap.put("0xc0000", "Highpass12KHz");
		}
		
		return bandWidthMap.get(key);
	}
	
	/**
	 * 筛选条件
	 */
	public static String ConditionStr = "";
	/**
	 * 时间段
	 */
	public static String PlanTimeStr = "";
	/**
	 * 时间段元ID
	 */
	public static String PlanTimeIDStr = "";

	/**
	 * 自定义APP配置文件类型
	 */
	public enum AppConfigType {
		Gesture, MeasureTypeForOuter, TwoBill, Invisible, Overhaul
	}
	/**
	 * 自定义字体大小
	 */
	public enum FontSizeType {
		Normal, Big, Huge
	}

	/**
	 * 登錄App的方式
	 * 
	 * @author 吴俊宜
	 * 
	 */
	public enum AppLoginType {
		All, Account, RFID, Scan
	}

	/**
	 * 0-巡检;1-点检排程;2-巡更;3-精密点检;4-巡视路线;5-SIS路线;6-GPS巡线;7-新GPS巡线;8-条件巡检
	 * 
	 * @author 吴俊宜
	 * 
	 */
	public static enum LineType {
		XDJ("巡点检", 0), DJPC("点检排程", 1), GPSXX("GPS巡线", 6), GPSXXNew("新GPS巡线", 7), CaseXJ(
				"条件巡检", 8);

		private String m_LineType;
		private int m_Index;

		private LineType(String lineType, int index) {
			this.m_LineType = lineType;
			this.m_Index = index;
		}

		public String getName() {
			return m_LineType;
		}

		public int getLineType() {
			return m_Index;
		}
		
		public String getLineTypeString(){
			return String.valueOf(m_Index);
		}
	}

	/**
	 * 实时上传附件最大值2M
	 */
	public final static int JITUploadMaxFileSize = 2 * 1024;
	/**
	 * 单次实时上传条数
	 */
	public static int OnceUploadNum = 10;
	/**
	 * 大附件结果是否已经通过数据同步上传标志
	 */
	public static boolean hasUploadByOverstepMaxSize = true;

	public static enum AppLanguage {
		zh, // 中文
		en, // 英文
		vi  // 越南语
	}

	public static enum LoginFrom {
		overhaul, // 检修管理
		operatebill, // 操作票
		workbill // 工作票
	}

	public static enum SearchType {
		Operation, // 操作票搜索
		OverhaulPlan, // 检修计划搜索
		WorkBill, // 工作票搜索
		OverhaulProject, // 检修项目搜索
		QueryDJLine, // 数据查询路线搜索
		CommUser //点检排程人员信息搜索
	}

	public static enum CurrentQC {
		QC0, // 实施
		QC1, // 一审
		QC2, // 二审
		QC3 // 三审
	}

	public static String WorkBillType(int index) {
		String res = "";
		switch (index) {
		case 1:
			res = "DQYZ";
			break;
		/*case 2:
			res = "DQEZ";
			break;*/
		case 2:
			res = "RLJX";
			break;
		default:
			res = "DQYZ";
			break;
		}
		return res;
	}

	public static enum QueryType {
		TaskResponse, // 任务完成情况
		Uncheck, // 漏检统计
		QueryHisData // 历史数据查询
	}

	public static enum FromTypeForOuter {
		Temperature, Vibration
	}
	
	// 清理点检排程路线方式
	public static enum ClearDJPCLineMode {
		Other,     // 下载后清理
		Auto,      // 自动清理
		Manual     // 手动清理
	}

	/**
	 * 结果库类型
	 * 
	 * @author 吴俊宜
	 * 
	 */
	private enum DBType {
		XJHisDataBase, // 历史数据库
		OperateBill, // 操作票结果库
		WorkBill // 工作票结果库
	}

	/**
	 * 判断结果文件夹中的数据库是否为巡点检数据库， 新加结果库如果不是数值命名的，必须在这里过滤
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isLineDBYn(String fileName) {
		if (fileName.equalsIgnoreCase(DBType.XJHisDataBase.toString()))
			return false;
		else if (fileName.equalsIgnoreCase(DBType.OperateBill.toString()))
			return false;
		else if (fileName.equalsIgnoreCase(DBType.WorkBill.toString()))
			return false;

		return true;
	}

	public static boolean isEffectDevice(Context context, String deviceName) {
		if (!StringUtils.isEmpty(deviceName)&&deviceName.equalsIgnoreCase(context
				.getString(R.string.outer_measuretypes_soeasytest)))
			return false;
		return true;
	}
	
	public static String XSTKeyConfigPath = XSTBasePath() + "XSTKey.xml";
	public static String getXSTKeyStr(String imei, String macaddrs, String guid) {
		String str = "<?xml version=\"1.0\"?>"
				+ "\r\n"
				+ "<Configuration>"
				+ "\r\n"
				+ "<Setting Name=\"IMEI\" Value=\"" + imei +  "\" />"
				+ "\r\n"
				+ "<Setting Name=\"MAC\" Value=\"" + macaddrs +  "\" />"
				+ "\r\n"
				+ "<Setting Name=\"GUID\" Value=\"" + guid +  "\" />"
				+ "\r\n" + "</Configuration>"; 
		
		return str;
	}
}
