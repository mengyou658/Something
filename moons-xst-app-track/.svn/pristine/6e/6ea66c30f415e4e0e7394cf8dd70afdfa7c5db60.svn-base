package com.moons.xst.track.communication;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;

/**
 * 文件下载
 * 
 * @author LKZ
 * @version 1.0
 * @created 2014-11-24
 */
public class DownloadFile {

	// 文件在服务端的位置（包括文件名）
	private static String urlPathDownload;
	// 下载后保存的路径
	private static String filesavePath;
	// 下载后保存的名称
	private static String fileName;

	/**
	 * 从服务器端下载文件
	 * 
	 * @author LKZ
	 * @param fileUrlFullPath
	 *            资源路径
	 * @param savedName
	 *            保存后文件名称
	 * @param SavedPath
	 *            保存后文件路径
	 **/
	public static boolean DownLoadFileFromServer(String fileUrlFullPath,
			String savedName, String SavedPath,Handler mHandler) {
		urlPathDownload = fileUrlFullPath;
		filesavePath = SavedPath;
		fileName = savedName;
		try {
			URL url = new URL(urlPathDownload);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(1000 * 6);
			con.setRequestMethod("GET");
			int bytelength = con.getContentLength();
			int cutsize = 256 * 256;
			InputStream is = con.getInputStream();
			if (bytelength > 0) {
				if(bytelength < 1024){
					cutsize = 128;
				}
				else if (bytelength < 1024 * 1024)
					cutsize = 1024 * 16;
				else if (bytelength < 1024 * 1024 * 10) {
					cutsize = 1024 * 16 * 8;
				} else if (bytelength < 1024 * 1024 * 50) {
					cutsize = 1024 * 16 * 16;
				} else {
					cutsize = 1024 * 512;
				}
			}
			int precent =0,x=0;
			byte[] bs = new byte[cutsize];
			int len;
			OutputStream os = new FileOutputStream(filesavePath + "/"
					+ fileName);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				x+=len;
			}
			os.close();
			is.close();

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
