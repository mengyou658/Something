package com.moons.xst.track.ui.main_fragment;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.custom;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UpdateManager;
import com.moons.xst.track.communication.DataCommUpgrade;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.ui.Main_Page;
import com.moons.xst.track.ui.ShowSettingAty;
import com.moons.xst.track.ui.CommUSBDownload.UsbStatesReceiver;
import com.moons.xst.track.widget.LoadingDialog;

public class CommuFragment extends Fragment implements View.OnClickListener {

	View commuFragment;
	RelativeLayout rl_layout_commu_receive, rl_layout_commu_twobilldownload,
			rl_layout_commu_appUpdate, rl_layout_commu_mapUpdate;
	View layout;
	RelativeLayout rl_commutype_wireless, rl_commutype_usb;
	ImageView iv_commutype_wireless_select, iv_commutype_usb_select;
	private final static String STATE_REFRESHRADIOBTN = "REFRESHRADIOBTN";
	AppContext appContext;
	private ProgressDialog mProDialog;// 正在检测版本信息
	private ProgressDialog downloadLoading;// 真在检测版本信息
	private LoadingDialog loading;// 加载中

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (commuFragment == null) {
			commuFragment = inflater.inflate(R.layout.fragment_commu,
					container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) commuFragment.getParent();
		if (parent != null) {
			parent.removeView(commuFragment);
		}
		return commuFragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		appContext = (AppContext) getActivity().getApplication();
		init();
		registerBoradcastReceiver();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (AppContext.getCommunicationType().equals(
				AppConst.CommunicationType_Wireless)) {
			AppContext
				.setTempCommunicationType(AppConst.CommunicationType_Wireless);
			iv_commutype_wireless_select.setVisibility(View.VISIBLE);
			iv_commutype_usb_select.setVisibility(View.GONE);			
		} else {
			AppContext.setTempCommunicationType(AppConst.CommunicationType_USB);
			iv_commutype_wireless_select.setVisibility(View.GONE);
			iv_commutype_usb_select.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unregisterBoradcastReceiver();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_layout_commu_receive:
			if (AppContext.getTempCommunicationType().equals(
					AppConst.CommunicationType_Wireless)) {
				if (!NetWorkHelper.isNetworkAvailable(getActivity()
						.getBaseContext())) {
					UIHelper.ToastMessage(getActivity(),
							R.string.network_not_connected);
					return;
				}
				if (loading == null) {
					loading = new LoadingDialog(getActivity());
					loading.setCanceledOnTouchOutside(false);
					loading.setCancelable(false);
				}
				UIHelper.JudgePlanTypeAndCustomType(getActivity(), loading);

			} else {
				if (StringUtils.isEmpty(AppContext.GetxjqCD())) {
					UIHelper.ToastMessage(getActivity(),
							R.string.download_message_errorxjqcd);
					return;
				}
				UIHelper.showUSBCommuDownLoad(getActivity());
			}
			break;
		case R.id.rl_layout_commu_twobilldownload:
			if (AppContext.getCommunicationType().equals(
					AppConst.CommunicationType_Wireless)) {
				if (!NetWorkHelper.isNetworkAvailable(getActivity()
						.getBaseContext())) {
					UIHelper.ToastMessage(getActivity(),
							R.string.network_not_connected);
					return;
				}
				if (NetWorkHelper.checkAppRunBaseCase(getActivity()
						.getBaseContext()))
					UIHelper.showOperateBillDownLoad(getActivity());
			} else {
				UIHelper.ToastMessage(getActivity(),
						R.string.download_message_notsupportusb);
				return;
			}
			break;
		case R.id.rl_layout_commu_appUpdate:
			if (AppContext.getTempCommunicationType().equals(
					AppConst.CommunicationType_Wireless)) {// 无线方式更新
				if (NetWorkHelper.checkAppRunBaseCase(getActivity()
						.getBaseContext())) {
					if (!NetWorkHelper.isNetworkAvailable(getActivity()
							.getBaseContext())) {
						UIHelper.ToastMessage(getActivity(),
								R.string.network_not_connected);
						return;
					}
					UpdateManager.getUpdateManager().checkAppUpdate(
							getActivity(), true);
				}
			} else {// usb更新
				if (StringUtils.isEmpty(AppContext.GetxjqCD())) {
					UIHelper.ToastMessage(getActivity(),
							R.string.download_message_errorxjqcd);
					return;
				}
				CommUSBUpgrade();
			}
			break;
		case R.id.rl_layout_commu_mapUpdate:
			break;
		}
	}

	private void init() {
		rl_layout_commu_receive = (RelativeLayout) commuFragment
				.findViewById(R.id.rl_layout_commu_receive);
		rl_layout_commu_twobilldownload = (RelativeLayout) commuFragment
				.findViewById(R.id.rl_layout_commu_twobilldownload);
		rl_layout_commu_appUpdate = (RelativeLayout) commuFragment
				.findViewById(R.id.rl_layout_commu_appUpdate);
		rl_layout_commu_mapUpdate = (RelativeLayout) commuFragment
				.findViewById(R.id.rl_layout_commu_mapUpdate);

		layout = (View) commuFragment
				.findViewById(R.id.layout_download_twobill);

		rl_layout_commu_receive.setOnClickListener(this);
		rl_layout_commu_twobilldownload.setOnClickListener(this);
		rl_layout_commu_appUpdate.setOnClickListener(this);
		rl_layout_commu_mapUpdate.setOnClickListener(this);
		
		rl_commutype_wireless = (RelativeLayout) commuFragment
				.findViewById(R.id.ll_commutype_wireless);
		rl_commutype_wireless.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				iv_commutype_wireless_select.setVisibility(View.VISIBLE);
				iv_commutype_usb_select.setVisibility(View.GONE);
				AppContext
					.setTempCommunicationType(AppConst.CommunicationType_Wireless);
			}
		});
		rl_commutype_usb = (RelativeLayout) commuFragment
				.findViewById(R.id.ll_commutype_usb);
		rl_commutype_usb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				iv_commutype_wireless_select.setVisibility(View.GONE);
				iv_commutype_usb_select.setVisibility(View.VISIBLE);
				AppContext
					.setTempCommunicationType(AppConst.CommunicationType_USB);
			}
		});
		
		iv_commutype_wireless_select = (ImageView) commuFragment
				.findViewById(R.id.iv_wireless_selected);
		iv_commutype_usb_select = (ImageView) commuFragment
				.findViewById(R.id.iv_usb_selected);
	}


	com.moons.xst.track.widget.AlertDialog dialog;
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 关闭socket
				CloseSocket();
			}
			if (msg.what == 2) {// 关闭提示框
				dismissLoading();
			}

			if (msg.what == 3) {// 是否更新
				String obj = (String) msg.obj;
				String updateMsg = "";
				if (obj.split("@").length > 0) {
					updateMsg = obj.split("@")[0];
				}
				if (obj.split("@").length > 1) {
					apkUrl = obj.split("@")[1];
				}
				LayoutInflater factory = LayoutInflater.from(getActivity());
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				dialog = new com.moons.xst.track.widget.AlertDialog(
						getActivity())
						.builder()
						.setTitle(
								getActivity().getString(
										R.string.software_version_update))
						.setView(view)
						.setMsg(updateMsg)
						.setPositiveButton(
								getActivity().getString(R.string.update_now),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if (downloadLoading == null
												|| !downloadLoading.isShowing()) {
											downloadLoading = ProgressDialog
													.show(getActivity(),
															null,
															getActivity()
																	.getString(
																			R.string.update_download),
															true, true);
											downloadLoading
													.setCanceledOnTouchOutside(false);
											downloadLoading
													.setCancelable(false);
											downloadLoading.show();
										}
										// 发送下载最新程序指令
										 new Thread() {
											 @Override
												public void run() {
										    		tcpConnect.SendUpLoadEnd(apkUrl);
										    	}
										 }.start();
									}
								})
						.setNegativeButton(
								getActivity().getString(
										R.string.update_talklater),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										CloseSocket();
									}
								}).setCanceledOnTouchOutside(false)
						.setCancelable(false);
				dialog.show();
			}
			if (msg.what == 4) {// 下载完成
				CloseSocket();
				if (downloadLoading != null) {
					downloadLoading.dismiss();
				}
				String apkPath = AppConst.XSTBasePath()
						+ "moons-xst-app-track.apk";
				installApk(getActivity(), apkPath);
//				AppContext.install(getActivity(), apkPath);
			}
			if (msg.what == 5) {// 提示
				CloseSocket();
				dismissLoading();
				if (dialog != null) {
					dialog.dismiss();
				}
				String Msg = msg.obj.toString() + "";
				LayoutInflater factory = LayoutInflater.from(getActivity());
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(getActivity())
						.builder()
						.setTitle(
								getActivity().getString(R.string.system_notice))
						.setView(view)
						.setMsg(Msg)
						.setPositiveButton(
								getActivity().getString(R.string.sure), null)
						.show();
			}
			if (msg.what == 6) {// 结束通信
				CloseSocket();
				dismissLoading();
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		};
	};

	// 关闭socket
	private void CloseSocket() {
		if (tcpConnect != null) {
			tcpConnect.SendDisConnect();
		}

		Thread th = new Thread(closeSocketTask);
		th.start();

	}

	Runnable closeSocketTask = new Runnable() {

		@Override
		public void run() {
			isUnderwayComm = false;
			Socket closeSocket = new Socket();

			try {
				InetAddress localhost = InetAddress.getLocalHost();
				SocketAddress address = new InetSocketAddress(localhost,
						SERVER_PORT);
				closeSocket.connect(address, 100);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} finally {
				try {
					closeSocket.close();

					if (tcpConnect != null) {
						tcpConnect.CloseRecSocket();
						tcpConnect = null;
					}
					if (mServerSocket != null) {
						mServerSocket.close();
						mServerSocket = null;
					}

				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

		}
	};

	private void dismissLoading() {
		if (mProDialog != null) {
			mProDialog.dismiss();
		}
		if (downloadLoading != null) {
			downloadLoading.dismiss();
		}
	}

	// USB更新
	private final int SERVER_PORT = 10086;
	private ServerSocket mServerSocket = null;
	UsbStatesReceiver mUsbStatesReceiver = null;
	String apkUrl = "";
	DataCommUpgrade tcpConnect = null;
	// 是否真正同步
	private boolean isUnderwayComm = false;

	private void CommUSBUpgrade() {
		if (isUnderwayComm) {
			return;
		}
		isUnderwayComm = true;
		if (mProDialog == null || !mProDialog.isShowing()) {
			mProDialog = ProgressDialog
					.show(getActivity(),
							null,
							getActivity()
									.getString(
											R.string.setting_sys_aboutus_checkupdata_diapromptinfo),
							true, true);
			mProDialog.setCanceledOnTouchOutside(false);
			mProDialog.setCancelable(false);
			mProDialog.show();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InetAddress localhost = InetAddress.getLocalHost();
					String ip = localhost.getHostAddress();
					// String ip = "127.0.0.1";
					System.out.println("TcpConnect ip地址是: " + ip);

					mServerSocket = new ServerSocket(SERVER_PORT);
					Socket client = null;
					mServerSocket.setSoTimeout(30000);
					client = mServerSocket.accept();
					tcpConnect = new DataCommUpgrade(client, mHandler,
							getActivity());
					new Thread(tcpConnect).start();

				} catch (SocketTimeoutException e) {
					CloseSocket();
					Message msg = new Message();
					msg.what = 5;
					msg.obj = getActivity()
							.getString(
									R.string.cumm_cummdownload_usbdownload_sockettimeout);
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					CloseSocket();
					Message msg = new Message();
					msg.what = 5;
					msg.obj = e;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
	}

	private void getList() {
		List<custom> list = new ArrayList<custom>();
		list = ShowSettingAty.getList(AppContext.getShortcutFunction());

		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getQuickState()
					&& list.get(i).getTag().equals("isTwoBill")
					&& AppContext.getTwoBillYN()) {
				flag = true;
			}
		}
		if (flag) {
			rl_layout_commu_twobilldownload.setVisibility(View.GONE);
			layout.setVisibility(View.GONE);
		} else {
			rl_layout_commu_twobilldownload.setVisibility(View.GONE);
			layout.setVisibility(View.GONE);
		}
	}

	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(STATE_REFRESHRADIOBTN);
		myIntentFilter.addAction("android.hardware.usb.action.USB_STATE");
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void unregisterBoradcastReceiver() {
		try {
			if (mBroadcastReceiver != null)
				getActivity().unregisterReceiver(mBroadcastReceiver);
		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("Receiver not registered")) {
				// Ignore this exception
			}
		}
	}

	/**
	 * 利用广播方式更新RADIOBUTTON
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("android.hardware.usb.action.USB_STATE")) {
				if (intent.getExtras().getBoolean("connected")) { // usb 插入
				} else {// usb拔出
					Message msg = new Message();
					msg.what = 6;
					mHandler.sendMessage(msg);
				}
			}
			if (intent.getAction().equals(STATE_REFRESHRADIOBTN)) {
				if (AppContext.getCommunicationType().equals(
						AppConst.CommunicationType_Wireless)) {
					AppContext
						.setTempCommunicationType(AppConst.CommunicationType_Wireless);
					iv_commutype_wireless_select.setVisibility(View.VISIBLE);
					iv_commutype_usb_select.setVisibility(View.GONE);			
				} else {
					AppContext.setTempCommunicationType(AppConst.CommunicationType_USB);
					iv_commutype_wireless_select.setVisibility(View.GONE);
					iv_commutype_usb_select.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	
	private void installApk(Context context, String apkFilePath) {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Editor editor = ((Main_Page) context).getPreferences().edit();
		editor.putBoolean("isFirstStart", true);
		editor.commit();
		
		// 插入操作日志
		OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_USB + 
				AppConst.COMMTYPE_APKUPGRADE,
				AppConst.LOGSTATUS_NORMAL,
				"");
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		((FragmentActivity) context).startActivityForResult(i, 0);
	}
}
