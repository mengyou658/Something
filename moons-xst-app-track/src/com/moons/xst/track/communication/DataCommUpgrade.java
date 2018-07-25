package com.moons.xst.track.communication;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.Update;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.ResponseLogInfoHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.SystemControlHelper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class DataCommUpgrade implements Runnable {
	private String TAG = "DataCommUpgrade";
	private Socket mSocket;
	private Handler mHandle;
	String mAppVersion = "";
	private Boolean mIsRun = true;
	private Context mContext;

	String mSplitChar = "@";

	BufferedOutputStream out = null;

	public DataCommUpgrade(Socket socket, Handler handle, Context context) {
		mSocket = socket;
		mHandle = handle;
		mAppVersion = AppContext.getAppVersion(context);
		mContext = context;
		try {
			out = new BufferedOutputStream(mSocket.getOutputStream());
		} catch (Exception e) {
			System.out.println(TAG + e.getMessage());
		}
	}

	// 接收信息
	private void ReceiveInfo(String info) {
		String[] tempList = info.split(mSplitChar);
		String isSuc = "false";
		String cmd = "";
		String data = "";
		String time = "";
		if (tempList.length >= 1)
			isSuc = tempList[0];
		if (tempList.length >= 2)
			cmd = tempList[1];
		if (tempList.length >= 3)
			data = tempList[2];
		if (tempList.length >= 4) {
			time = tempList[3];
		}
		if (isSuc.equals("true")) {
			if (cmd.equals("pc-connect")) {// 连接成功
				String xjqCD = AppContext.GetxjqCD();
				String sendData = GetSendData(true, "pda-updatetime", xjqCD);
				// 发送获取服务器时间指令
				SendData(sendData);

			}
			if (cmd.equals("pc-uploaddataend")) {// 获取时间成功
				// 更新时间
				/*if (!StringUtils.isEmpty(time)) {
					SystemControlHelper.setSystemDatatime(DateTimeHelper
							.StringToDate(time).getTime());
				}*/
				String pdaGUID = AppContext.GetUniqueID();
				String sendData = GetSendData(true, "pda-upgrade", mAppVersion
						+ ";" + pdaGUID + ";" + "moons-xst-app-track" + ";"
						+ AppContext.getModel());
				// 发送获取版本信息指令
				SendData(sendData);
			}
			if (cmd.equals("pc-upgrade")) {// 接收版本信息指令
				RefreshHandle(2, "");
				int oldVsersionCode = 0;// 当前版本号
				if (AppContext.xstTrackPackageInfo != null) {
					oldVsersionCode = AppContext.xstTrackPackageInfo.versionCode;
				}

				try {
					Update update = Update.parse(data);
					String updateMsgBeforeReplace = update.getUpdateLog();
					String updateMsg = updateMsgBeforeReplace.replace("|", "");
					final String apkUrl = update.getDownloadUrl();
					int versionCode = update.getVersionCode();// 服务器版本号
					String versionName = update.getVersionName();
					if (oldVsersionCode < versionCode && versionCode != 0
							&& oldVsersionCode != 0) {// 有新程序需要跟新
						Message msg = new Message();
						msg.what = 3;
						msg.obj = updateMsg + "@" + apkUrl;
						mHandle.sendMessage(msg);
					} else {
						RefreshHandle(
								5,
								mContext.getString(R.string.setting_sys_aboutus_checkupdata_diamsg_islast));
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			if (cmd.equals("pc-upgradeapkend")) {// 版本更新成功指令
				RefreshHandle(4, "");
			}
			if (cmd.equals("pc-defeated")) {
				RefreshHandle(
						5,
						mContext.getString(R.string.cumm_cummdownload_usbdownload_hint));
			}
		}
	}

	// 发送handle
	private void RefreshHandle(int what, String hint) {
		if (mHandle != null) {
			Message msg = new Message();
			msg.what = what;
			if (hint != null) {
				msg.obj = hint;
			}
			mHandle.sendMessage(msg);
		}
	}

	// 发送下载最新程序指令
	public void SendUpLoadEnd(String apkUrl) {
		String sendData = GetSendData(true, "pda-upgradedownload", apkUrl);
		SendData(sendData);
	}

	private String GetSendData(boolean isSucc, String cmd, String data) {
		String result = cmd + mSplitChar + data;
		if (isSucc)
			result = "true" + mSplitChar + result;
		else
			result = "false" + mSplitChar + result;
		return result + mSplitChar;
	}

	public void SendData(String info) {
		if (mSocket == null)
			return;
		try {
			String recordStr = info;
			out.write(recordStr.getBytes("utf-8"));
			out.flush();

		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {

		}
	}

	// 关闭连接
	public void SendDisConnect() {
		String sendData = GetSendData(true, "pda-disconnect", "");
		SendData(sendData);
	}

	byte[] temporary = new byte[0];

	@Override
	public void run() {
		InputStream in = null;
		try {
			mIsRun = true;
			String recmsg = "";
			while (mIsRun) {
				in = mSocket.getInputStream();

				byte[] buffer = new byte[1024];
				int len = in.read(buffer, 0, 1024);
				if (len == -1) {
					RefreshHandle(6, "");
					continue;
				}

				byte[] temp = new byte[temporary.length + buffer.length];
				System.arraycopy(temporary, 0, temp, 0, temporary.length);
				System.arraycopy(buffer, 0, temp, temporary.length,
						buffer.length);
				temporary = temp;

				recmsg = new String(temporary, "UTF-8");
				recmsg = recmsg.trim();
				if (len == 1024
						&& !recmsg.substring(recmsg.length() - 1).equals("@")) {
					continue;
				}
				if (recmsg != null) {
					ReceiveInfo(recmsg);
				}
				temporary = new byte[0];
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			CloseRecSocket();
		}
	}

	public void CloseRecSocket() {
		mIsRun = false;

		if (out != null)
			try {
				out.close();
				out = null;
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		try {
			if (mSocket != null) {
				System.out.println("Close mSocket");
				mSocket.close();
				mSocket = null;
			}
		} catch (Exception e) {

		}
	}

}
