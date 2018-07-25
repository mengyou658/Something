package com.moons.xst.track.common;

import com.moons.xst.track.AppConst;

public class LogUtils {

	static String filepath = AppConst.XSTBasePath() + "/Log.txt";

	public static void WriteLog(String TAG, int lineid) {
		/*
		 * String msg = TAG + "    "+
		 * DateFormat.format("yyyy-MM-dd hh:mm:ss",Calendar
		 * .getInstance(Locale.CHINA)) + "    " + lineid + "\n";
		 * contentToTxt(filepath, msg);
		 */
	}

	/**
	 * 
	 * @param fileName
	 * @param message
	 */
	// 写在/mnt/sdcard/目录下面的文件
	public static void contentToTxt(String filePath, String content) {
		// String str = new String(); // 原有txt内容
		// String s1 = new String();// 内容更新
		// try {
		// File f = new File(filePath);
		// if (f.exists()) {
		// System.out.print("文件存在");
		// } else {
		// System.out.print("文件不存在");
		// f.createNewFile();// 不存在则创建
		// }
		// BufferedReader input = new BufferedReader(new FileReader(f));
		//
		// while ((str = input.readLine()) != null) {
		// s1 += str + System.getProperty("line.separator");
		// }
		// System.out.println(s1);
		// input.close();
		// s1 += content;
		//
		// BufferedWriter output = new BufferedWriter(new FileWriter(f));
		// output.write(s1);
		// output.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		//
		// }
	}

	/**
	 * 保存跟踪信息--调试时使用
	 * 
	 * @param excp
	 */
	public static void saveLog(String excp, String logtype) {

		// String errorlog = "print_log_" + logtype + ".txt";
		// String savePath = "";
		// String logFilePath = "";
		// FileWriter fw = null;
		// PrintWriter pw = null;
		// try { // 判断是否挂载了SD卡
		// String storageState = Environment.getExternalStorageState();
		// if (storageState.equals(Environment.MEDIA_MOUNTED)) {
		// savePath = AppConst.XSTLogFilePath();
		// File file = new File(savePath);
		// if (!file.exists()) {
		// file.mkdirs();
		// }
		// logFilePath = savePath + errorlog;
		// } // 没有挂载SD卡，无法写文件
		// if (logFilePath == "") {
		// return;
		// }
		// File logFile = new File(logFilePath);
		// if (!logFile.exists()) {
		// logFile.createNewFile();
		// }
		// fw = new FileWriter(logFile, true);
		// pw = new PrintWriter(fw);
		// pw.println("\n--------------------" + (new Date().toLocaleString())
		// + "---------------------");
		// fw.write(excp);
		// pw.close();
		// fw.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// if (pw != null) {
		// pw.close();
		// }
		// if (fw != null) {
		// try {
		// fw.close();
		// } catch (IOException e) {
		// }
		// }
		// }

	}
}
