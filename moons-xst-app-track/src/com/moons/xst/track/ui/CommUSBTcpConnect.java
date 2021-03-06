package com.moons.xst.track.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.ResponseLogInfoHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.communication.DownLoad;
import com.moons.xst.track.dao.DataTransHelper;

import android.os.Handler;
import android.os.Message;

public class CommUSBTcpConnect implements Runnable {

	private Socket mSocket;
	private Handler mRefreshHandler;
	private Boolean mIsRun = true;
	
	BufferedOutputStream out = null;

	String mCMD = "";
	String mSplitChar = "@";
	String mAppVersion = "";
	int LineID=-1;
	int uploadingId=-1;
	
	public boolean isDJPC=false;
	public boolean judgeDisconnect=false;

	public CommUSBTcpConnect(Socket socket, Handler refershHandle,
			String appVersion,boolean isdisconnect) {
		mAppVersion = appVersion;
		mSocket = socket;
		this.mRefreshHandler = refershHandle;
		judgeDisconnect=isdisconnect;

		try {
			out = new BufferedOutputStream(mSocket.getOutputStream());

		} catch (Exception e) {

			System.out.println("TcpConnect" + e.getMessage());
		}
	}
	

	private String GetSendData(boolean isSucc, String cmd, String data) {
		String result = cmd + mSplitChar + data;
		if (isSucc)
			result = "true" + mSplitChar + result;
		else
			result = "false" + mSplitChar + result;
		return result + mSplitChar;
	}

	private void ReceiveInfo(String info) {
		String[] tempList = info.split(mSplitChar);
		String isSuc = "false";
		String cmd = "";
		String data = "";
		String attach = "";
		if (tempList.length >= 1)
			isSuc = tempList[0];
		if (tempList.length >= 2)
			cmd = tempList[1];
		if (tempList.length >= 3)
			data = tempList[2];
		if (tempList.length >= 4) {
			attach = tempList[3];
		}
		if (isSuc.equals("true")) {
			if (cmd.equals("pc-connect")) {// 连接成功
				ShowInfo(info);
				String pdaGUID = AppContext.GetUniqueID();
				String xjqCD = AppContext.GetxjqCD();
				String hardwareInfo = AppContext.getHardwareInfo();
				String sendData = GetSendData(true, "pda-connect", xjqCD + ";"
						+ pdaGUID + ";" + mAppVersion + ";" + hardwareInfo);
				// 处理发送数据逻辑
				SendData(sendData);
			}
			if (cmd.equals("pc-uploaddataend")) {// 上传成功
				
			} else if (cmd.equals("pc-initdataend")||cmd.equals("pc-underwaydownload")) {// 初始化成功
				isDJPC=data.equals("true");
				
				InitLinesData();

				String sendData = GetSendData(true, "pda-initdataend", "");

				SendData(sendData);

			} else if (cmd.equals("pc-downloadplanend")) {// 下载成功
				String lineID = data.split(";")[0];
				String b=DownLoad.UpdateLineListFile(lineID);
				if(!StringUtils.toBool(b)){//拼接失败也算下载失败
					info="true@pc-downloaderror@"+b+"@"+data;
				}
			}
			RefreshHandle(info);
		}

	}

	public void SendUpLoadEnd() {
		String sendData = GetSendData(true, "pda-uploaddataend", "");
		SendData(sendData);
	}

	public void SendDownLoadEnd() {
		String sendData = GetSendData(true, "pda-downloadplanend", "");
		SendData(sendData);
	}

	public void SendDisConnect() {
		String sendData = GetSendData(true, "pda-disconnect", "");
		SendData(sendData);
	}

	private void InitLinesData() {
		try {
			//FileEncryptAndDecrypt.encrypt(AppConst.XSTBasePath()
				//	+ AppConst.UserXmlFile);
			
			String filePathString = AppConst.XSTBasePath()
					+ AppConst.EventTypeXmlFile;
			String eventTypeResultStr = FileUtils.read(filePathString);
			DownLoad.loadEventTypeList(eventTypeResultStr);

			FileEncryptAndDecrypt.encrypt(AppConst.XSTBasePath()
					+ AppConst.SettingxmlFile);

			FileEncryptAndDecrypt.encrypt(AppConst.XSTBasePath()
					+ AppConst.ModuleSettingxmlFile);
			filePathString = AppConst.XSTBasePath()
					+ AppConst.DJLineTempXmlFile;
			String djlineXmlString = FileUtils.read(filePathString);
			if (!StringUtils.isEmpty(djlineXmlString)) {
				AppContext.CommDJLineBuffer = DataTransHelper
						.ConvertLineFromXML(djlineXmlString);
				List<DJLine> lastDJLineList = DataTransHelper
						.ConvertLineFromXMLFile();
				for(int i=AppContext.CommDJLineBuffer.size()-1;i>=0;i--){
					DJLine djLine=AppContext.CommDJLineBuffer.get(i);
					
					if (lastDJLineList != null) {
						if (lastDJLineList.contains(djLine)) {
							File file=new File(AppConst.XSTDBPath()+"424DB_"+djLine.getLineID()+".sdf");
							if(!file.exists()) {
								djLine.setneedUpdate(true);
							} else {
								djLine.setneedUpdate(false);
							}
						} else {
							djLine.setneedUpdate(djLine.getBuidYN() ? true : false);
						}
						
						/*boolean b = lastDJLineList.contains(djLine) ? false
								: djLine.getBuidYN() ? true : false;
						
						File file=new File(AppConst.XSTDBPath()+"424DB_"+djLine.getLineID()+".sdf");
						
						djLine.setneedUpdate(!(b && file.exists()));*/
						/*if(!file.exists()){
							djLine.setneedUpdate(true);
						}*/
						// djLine.setneedUpdate(!lastDJLineList.contains(djLine));
					} else {
						djLine.setneedUpdate(djLine.getBuidYN() ? true : false);
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void UpLoadLineData(DJLine djLine, String filePath) {
		uploadingId=djLine.getLineID();
		String sendData = GetSendData(true, "pda-uploaddata",
				String.valueOf(djLine.getLineID()) + ";" + djLine.getLineName() + ";" + filePath);
		SendData(sendData);
	}

	// 下载路线
	public void DownloadPlan(DJLine djLine) {
		
		LineID=djLine.getLineID();
		String sendData = GetSendData(true, "pda-downloadplan",
				String.valueOf(djLine.getLineID()) + ";" + djLine.getLineName());
		SendData(sendData);
	}

	private void RefreshHandle(String str) {
		if (mRefreshHandler != null) {
			Message msg = new Message();
			msg.obj = str;
			msg.arg1 = 1;
			mRefreshHandler.sendMessage(msg);
		}
	}

	private void ShowInfo(String str) {
		if (mRefreshHandler != null) {
			Message msg = new Message();
			msg.obj = str;
			msg.arg1 = 10;
			mRefreshHandler.sendMessage(msg);
		}
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

	public void CloseRecSocket() {
		try {
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
		} catch (Exception e) {}
	}

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
				if (len == -1&&judgeDisconnect) {
					Message msg = new Message();
					msg.what = -2;
					mRefreshHandler.sendMessage(msg);
					continue;
				}else if(len==-1){
					continue;
				}
				recmsg = new String(buffer, "UTF-8");
				recmsg = recmsg.trim();
				if (recmsg != null) {
					ReceiveInfo(recmsg);
				}
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

}