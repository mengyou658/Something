package com.moons.xst.buss;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.moons.xst.sqlite.ComDaoMaster;
import com.moons.xst.sqlite.ComDaoSession;
import com.moons.xst.sqlite.DJDAOMaster;
import com.moons.xst.sqlite.DJDAOMaster.DevOpenHelper;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOMaster;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.sqlite.TempMeasureDaoMaster;
import com.moons.xst.sqlite.TempMeasureDaoSession;
import com.moons.xst.sqlite.TwoBillMaster;
import com.moons.xst.sqlite.TwoBillResultDaoMaster;
import com.moons.xst.sqlite.TwoBillResultDaoSession;
import com.moons.xst.sqlite.TwoBillSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.LogUtils;
import com.moons.xst.track.common.UIHelper;

/**
 * 初始化操作数据库所用的DAOSession，每次只需初始化一次就可以，不必重复初始化
 * 
 */
public class InitDJsqlite {

	private static final String TAG = "InitDJsqlite";

	/*
	 * private SQLiteDatabase DJdb; public SQLiteDatabase DJdb; private
	 * DJDAOMaster djdaoMaster; private DJDAOSession djdaoSession;
	 */

	/*
	 * private SQLiteDatabase DJResultdb; public SQLiteDatabase DJResultdb;
	 * private DJResultDAOMaster djredaoMaster; private DJResultDAOSession
	 * djredaoSession;
	 */

	/*
	 * private ComDaoMaster comdaoMaster; private ComDaoSession comdaoSession;
	 */

	/*
	 * private SQLiteDatabase TempMeasuredb; private TempMeasureDaoMaster
	 * tempmeasuredaoMaster; private TempMeasureDaoSession
	 * tempmeasuredaoSession;
	 */

	/*
	 * private SQLiteDatabase TowBill; private TwoBillMaster twoBillMaster;
	 * private TwoBillSession twoBillSession;
	 */

	/*
	 * private SQLiteDatabase TwoBillResultdb; private TwoBillResultDaoMaster
	 * twoBillResultDaoMaster; private TwoBillResultDaoSession
	 * twoBillResultDaoSession;
	 */

	/*
	 * public synchronized ComDaoSession InitComDAOSession(Context context) { if
	 * (comdaoSession != null) return comdaoSession; else { try { String
	 * DataBaseName = AppConst.XSTDBPath() + "Common.sdf";
	 * com.moons.xst.sqlite.ComDaoMaster.DevOpenHelper helper = new
	 * ComDaoMaster.DevOpenHelper( context, DataBaseName, null); DJdb =
	 * helper.getWritableDatabase(); comdaoMaster = new ComDaoMaster(DJdb);
	 * comdaoSession = comdaoMaster.newSession(); return comdaoSession; } catch
	 * (Exception e) { Log.e(TAG, e.toString()); e.printStackTrace(); return
	 * null; } } }
	 */

	/*
	 * public synchronized TwoBillResultDaoSession InitTwoBillResultDaoMaster(
	 * Context context) { String DataBaseName = AppConst.TwoTicketRecordPath() +
	 * "D-5.sdf"; if (twoBillResultDaoSession != null) { return
	 * twoBillResultDaoSession; } else { try {
	 * com.moons.xst.sqlite.TwoBillResultDaoMaster.DevOpenHelper helper = new
	 * TwoBillResultDaoMaster.DevOpenHelper( context, DataBaseName, null);
	 * addFileList(-5, DataBaseName); TwoBillResultdb =
	 * helper.getWritableDatabase(); twoBillResultDaoMaster = new
	 * TwoBillResultDaoMaster( TwoBillResultdb); twoBillResultDaoSession =
	 * twoBillResultDaoMaster.newSession(); return twoBillResultDaoSession; }
	 * catch (Exception e) { Log.e(TAG, e.toString()); e.printStackTrace();
	 * return null; } } }
	 */

	/*
	 * public synchronized TempMeasureDaoSession InitTempMeasureDaoSession(
	 * Context context, int lineid) { String DataBaseName =
	 * AppConst.CurrentResultPath(lineid) + "D0.sdf";
	 * 
	 * if (tempmeasuredaoSession != null && lineid ==
	 * tempmeasuredaoSession.getLineID() &&
	 * FileUtils.checkFileExists(DataBaseName)) { return tempmeasuredaoSession;
	 * } else { try { com.moons.xst.sqlite.TempMeasureDaoMaster.DevOpenHelper
	 * helper = new TempMeasureDaoMaster.DevOpenHelper( context, DataBaseName,
	 * null); addFileList(lineid, DataBaseName); TempMeasuredb =
	 * helper.getWritableDatabase(); tempmeasuredaoMaster = new
	 * TempMeasureDaoMaster(TempMeasuredb); tempmeasuredaoSession =
	 * tempmeasuredaoMaster.newSession(); return tempmeasuredaoSession; } catch
	 * (Exception e) { Log.e(TAG, e.toString()); e.printStackTrace(); return
	 * null; } } }
	 */

	public void refreshDao() {
		// djdaoSession = null;
		// djredaoSession = null;
		// comdaoSession = null;
		// tempmeasuredaoSession = null;
		// twoBillSession = null;
		// twoBillResultDaoSession = null;
	}

	// 清空两票session
	public void refreshTwoBil() {
		// twoBillSession = null;
	}

	/**
	 * 初始化上传库Session
	 * 
	 * @param context
	 * @param lineid
	 * @return
	 */
	/*
	 * public synchronized DJResultDAOSession InitDJResultDAOSession( Context
	 * context, final int lineid) { String DataBaseName =
	 * AppConst.CurrentResultPath(lineid) + AppConst.ResultDBName(lineid); if
	 * (djredaoSession != null && lineid == djredaoSession.getLineID() &&
	 * FileUtils.checkFileExists(DataBaseName)) { return djredaoSession; } else
	 * { // 备份数据库路径 String newPath = AppConst.XSTDamageDB() + "D" + lineid + "_"
	 * + DateTimeHelper.getDateTimeNow1() + ".sdf";
	 * FileUtils.copyFile(DataBaseName, newPath); //判断数据库是否损坏 if
	 * (checkDB(DataBaseName)) { UIHelper.ToastMessage(context, "数据库损坏"); } else
	 * { File file = new File(newPath); file.delete(); } try {
	 * com.moons.xst.sqlite.DJResultDAOMaster.DevOpenHelper helper = new
	 * DJResultDAOMaster.DevOpenHelper( context, DataBaseName, null);
	 * addFileList(lineid, DataBaseName); DJResultdb =
	 * helper.getWritableDatabase(); djredaoMaster = new
	 * DJResultDAOMaster(DJResultdb); djredaoSession =
	 * djredaoMaster.newSession(); djredaoSession.setLineID(lineid); return
	 * djredaoSession; } catch (Exception e) { Log.e(TAG, e.toString());
	 * e.printStackTrace(); return null; } } }
	 */

	/*
	 * public synchronized TwoBillSession InitTwoBillSession(Context context) {
	 * if (twoBillSession != null) { return twoBillSession; } else { try {
	 * String DataBaseName = AppConst.XSTDBPath() + "TwoBill.sdf";
	 * com.moons.xst.sqlite.TwoBillMaster.DevOpenHelper helper = new
	 * com.moons.xst.sqlite.TwoBillMaster.DevOpenHelper( context, DataBaseName,
	 * null); TowBill = helper.getWritableDatabase(); twoBillMaster = new
	 * TwoBillMaster(TowBill); twoBillSession = twoBillMaster.newSession();
	 * return twoBillSession; } catch (Exception e) { Log.e(TAG, e.toString());
	 * e.printStackTrace(); return null; } } }
	 */

	/**
	 * 初始化下载库DAOSession
	 * 
	 * @param context
	 * @param lineid
	 * @return
	 * @author LKZ
	 */
	/*
	 * public DJDAOSession InitDJDAOSession(Context context, int lineid) { // if
	 * (djdaoSession != null && lineid == djdaoSession.getLineID()) // return
	 * djdaoSession; // else { try {
	 * 
	 * String DataBaseName = AppConst.XSTDBPath() + AppConst.PlanDBName(lineid);
	 * if (!FileUtils.checkFileExists(DataBaseName)) {
	 * UIHelper.ToastMessage(context, R.string.home_line_noplanfile_notice);
	 * return null; } DevOpenHelper helper = new
	 * DJDAOMaster.DevOpenHelper(context, DataBaseName, null); DJdb =
	 * helper.getWritableDatabase(); djdaoMaster = new DJDAOMaster(DJdb);
	 * djdaoSession = djdaoMaster.newSession(); djdaoSession.setLineID(lineid);
	 * return djdaoSession; } catch (Exception e) { Log.e(TAG, e.toString());
	 * e.printStackTrace(); return null; } } }
	 */

	private synchronized void addFileList(int lineID, String filepath) {
		DJLine djLine = new DJLine();
		djLine.setLineID(lineID);
		djLine.setResultDBPath(filepath);
		djLine.setPlanDBPath(AppConst.XSTDBPath() + File.separator
				+ AppConst.PlanDBName(lineID));
		boolean exit = false;
		if (AppContext.resultDataFileListBuffer != null
				&& AppContext.resultDataFileListBuffer.size() > 0) {
			for (int i = 0; i < AppContext.resultDataFileListBuffer.size(); i++) {
				if (AppContext.resultDataFileListBuffer.get(i).getLineID() == lineID) {
					exit = true;
					break;
				}
			}
		}
		if (!exit) {
			AppContext.resultDataFileListBuffer.add(djLine);
			LogUtils.saveLog(
					"File Create:-" + AppConst.ResultDBName(djLine.getLineID()),
					"JIT");
		}
	}

	/**
	 * 判断数据库是否损坏 损坏返回true
	 */
	public static Boolean checkDB(String DataBaseName) {
		SQLiteDatabase checkDB = null;
		try {
			// File file = new File(DataBaseName);
			// 路径下有数据库才判断数据库是否损坏
			// if (file.exists()) {
			checkDB = SQLiteDatabase.openDatabase(DataBaseName, null,
					SQLiteDatabase.OPEN_READWRITE);
			// } else {
			// return false;
			// }
		} catch (SQLiteException e) {
			// TODO: handle exception
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? false : true;
	}

	// 数据库损坏保存日志
	public static void DBLog(String DataBaseName) {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(DataBaseName, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			// TODO: handle exception
			e.toString();
		}
		if (checkDB != null) {
			checkDB.close();
		}
		if (checkDB == null) {
			try {
				FileUtils.SaveToFile(
						AppConst.XSTDamageDB(),
						"DBLog.txt",
						"\n------"
								+ DateTimeHelper.DateToString(DateTimeHelper
										.GetDateTimeNow()) + "--------"
								+ DataBaseName + "\n", true);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}
