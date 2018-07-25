package com.moons.xst.track.ui.main_fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UpdateManager;
import com.moons.xst.track.communication.DataCommDownload;
import com.moons.xst.track.communication.USBDataCommDownload;
import com.moons.xst.track.ui.Main_Page;
import com.moons.xst.track.widget.ProgressButton;
import com.moons.xst.track.widget.ProgressButton.OnProgressButtonClickListener;

public class DrawerLeftFragment extends Fragment implements OnClickListener {

	View drawerLeftFragment;
	private final static String STATE_LOADUSERDJLINE = "LOADUSERDJLINE";
	private final static String STATE_LOADALLDJLINE = "LOADALLDJLINE";

	private com.moons.xst.track.widget.AlertDialog dialog;
	private RelativeLayout communicationType;
	private RelativeLayout rl_xjqcd;
	private RelativeLayout rl_wsaddress;
	private RelativeLayout rl_wlan_wsaddress;
	
	private EditText webserviceEditTextForWlan;
	private EditText edit;
	private TextView mUserName;
	private TextView communicationTypeInfo;
	private TextView xjqcdInfo;
	private TextView wlanWsaddressInfo;
	private TextView wsaddressInfo;
	private RelativeLayout login;
	// private TextView tv_login;
	private LinearLayout ll_comm_hint;// 通信消息提示
	private ProgressButton progress_button;// 进度条按钮

	private TextView tv_commuSetting, tv_commuType_desc, tv_xjdcd_desc,
			tv_webservice_desc, tv_webservice_wlan_desc;
	private ScrollView scroll_view;

	private AppContext appContext;
	// 当前是否连接usb
	private boolean isConnectUSB = false;

	private int communicationIndex;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (drawerLeftFragment == null) {
			drawerLeftFragment = inflater.inflate(
					R.layout.fragment_drawer_left, container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) drawerLeftFragment.getParent();
		if (parent != null) {
			parent.removeView(drawerLeftFragment);
		}
		return drawerLeftFragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		appContext = (AppContext) getActivity().getApplication();
		initView(drawerLeftFragment);
		registerBoradcastReceiver();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (AppContext.isLogin()) {
			mUserName.setText(((AppContext) getActivity()
					.getApplicationContext()).getLoginInfo().getUserName());
		} else {
			mUserName.setText(R.string.mainpage_message_notlogin);
		}
		initData();
		refreshView();
	}

	private void initData() {
		if (AppContext.getCommunicationType().equalsIgnoreCase(
				AppConst.CommunicationType_Wireless)) {
			communicationTypeInfo
					.setText(getString(R.string.commu_type_wireless));
		} else if (AppContext.getCommunicationType().equalsIgnoreCase(
				AppConst.CommunicationType_USB)) {
			communicationTypeInfo.setText(getString(R.string.commu_type_usb));
		}

		xjqcdInfo.setText(AppContext.getxjqCD());
		wlanWsaddressInfo.setText(AppContext.getWebServiceAddressForWlan());
		wsaddressInfo.setText(AppContext.getWebServiceAddress());

	}

	private void initView(View view) {
		ll_comm_hint = (LinearLayout) view.findViewById(R.id.ll_comm_hint);
		scroll_view = (ScrollView) view.findViewById(R.id.scroll_view);

		progress_button = (ProgressButton) view
				.findViewById(R.id.progress_button);
		progress_button
				.setOnProgressButtonClickListener(new OnProgressButtonClickListener() {
					@Override
					public void onClickListener() {
						// TODO 自动生成的方法存根
						// wifi通信
						if (AppContext.getCommunicationType().equals(
								AppConst.CommunicationType_Wireless)) {
								if (!NetWorkHelper
										.isNetworkAvailable(getActivity()
												.getBaseContext())) {
									UIHelper.ToastMessage(getActivity(),
											R.string.network_not_connected);
									return;
								}
								if (NetWorkHelper
										.checkAppRunBaseCase(getActivity()
												.getBaseContext())) {
									// 跳到Scrollview第一条
									// scroll_view.fullScroll(ScrollView.FOCUS_UP);
									DataCommDownload.getDataCommDownload()
											.dataCommEntrance(getActivity(),
													ll_comm_hint,
													progress_button);
								}
						}
						// USB通信
						else {
							if (isConnectUSB) {
								if (NetWorkHelper
										.checkAppRunBaseCase(getActivity()
												.getBaseContext())) {
									USBDataCommDownload
											.getUSBDataCommDownload()
											.dataCommEntrance(getActivity(),
													ll_comm_hint,
													progress_button);
								}
							} else {
								UIHelper.ToastMessage(
										getActivity(),
										getString(R.string.cumm_cummdownload_usbdisconnect_hint));
							}
						}
					}
				});
		mUserName = (TextView) view.findViewById(R.id.drawer_left_user_info);
		communicationTypeInfo = (TextView) view
				.findViewById(R.id.textview_drawerleft_communicationtype_value);
		xjqcdInfo = (TextView) view
				.findViewById(R.id.textview_drawerleft_xjqcd_value);
		wlanWsaddressInfo = (TextView) view
				.findViewById(R.id.textview_drawerleft_wlan_wsaddress_value);
		wsaddressInfo = (TextView) view
				.findViewById(R.id.textview_drawerleft_wsaddress_value);
		communicationType = (RelativeLayout) view
				.findViewById(R.id.rl_drawerleft_communicationtype);
		rl_xjqcd = (RelativeLayout) view.findViewById(R.id.rl_drawerleft_xjqcd);
		rl_wsaddress = (RelativeLayout) view
				.findViewById(R.id.rl_drawerleft_wsaddress);
		rl_wlan_wsaddress = (RelativeLayout) view
				.findViewById(R.id.rl_drawerleft_wlan_wsaddress);
		login = (RelativeLayout) view.findViewById(R.id.rl_drawerleft_login);

		tv_commuSetting = (TextView) view
				.findViewById(R.id.tv_drawerleft_commusetting);
		tv_commuType_desc = (TextView) view
				.findViewById(R.id.textview_drawerleft_communicationtype);
		tv_xjdcd_desc = (TextView) view
				.findViewById(R.id.textview_drawerleft_xjqcd);
		tv_webservice_desc = (TextView) view
				.findViewById(R.id.textview_drawerleft_wsaddress);
		tv_webservice_wlan_desc = (TextView) view
				.findViewById(R.id.textview_drawerleft_wlan_wsaddress);
		registerListener();
	}

	private void refreshView() {
		tv_commuSetting.setText(R.string.setting_head_title_comm);
		tv_commuType_desc
				.setText(R.string.setting_head_title_sys_communicationtype);
		tv_xjdcd_desc.setText(R.string.setting_head_title_comm_xjqcd);
		tv_webservice_desc.setText(R.string.setting_head_title_comm_webaddr);
		tv_webservice_wlan_desc
				.setText(R.string.setting_head_title_comm_wlan_webaddr);
		if (AppContext.getCommunicationType().equalsIgnoreCase(
				AppConst.CommunicationType_Wireless)) {
			communicationTypeInfo
					.setText(getString(R.string.commu_type_wireless));
		} else if (AppContext.getCommunicationType().equalsIgnoreCase(
				AppConst.CommunicationType_USB)) {
			communicationTypeInfo.setText(getString(R.string.commu_type_usb));
		}
		progress_button.setText(getString(R.string.drawleft_btn_syncdata));
	}

	private void registerListener() {
		mUserName.setOnClickListener(this);
		communicationType.setOnClickListener(this);
		rl_xjqcd.setOnClickListener(this);
		rl_wsaddress.setOnClickListener(this);
		rl_wlan_wsaddress.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(STATE_LOADUSERDJLINE);
		myIntentFilter.addAction(STATE_LOADALLDJLINE);
		myIntentFilter.addAction("android.hardware.usb.action.USB_STATE");
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("android.hardware.usb.action.USB_STATE")) {
				if (intent.getExtras().getBoolean("connected")) { // usb 插入
					isConnectUSB = true;
				} else {// usb拔出
					isConnectUSB = false;
				}
			}
			if (intent.getAction().equals(STATE_LOADUSERDJLINE)
					|| intent.getAction().equals(STATE_LOADALLDJLINE)) {
				if (!AppContext.isLogin()) {
					mUserName.setText(R.string.mainpage_message_notlogin);
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_drawerleft_communicationtype:
			chooseCommunicationType();
			break;
		case R.id.rl_drawerleft_xjqcd:
			xjqCD();
			break;
		case R.id.rl_drawerleft_wlan_wsaddress:
			webServiceAddressForWlan();
			break;
		case R.id.rl_drawerleft_wsaddress:
			webServiceAddress();
			break;
		case R.id.drawer_left_user_info:
			Main_Page.instance.callbackLoginOrOut();
			break;
		default:
			break;
		}
	}

	private void chooseCommunicationType() {
		if (AppContext.getPwdYN()) {
			UIHelper.ToastMessage(getActivity(), this
					.getString(R.string.setting_sys_settingconfig_needpwd_msg));
			return;
		}
		final String[] communication = getResources().getStringArray(
				R.array.communicationtype_values);
		final List<String> listData = new ArrayList<String>();
		int checkitem = 0;
		for (int i = 0; i < communication.length; i++) {
			listData.add(communication[i]);
			if (communication[i].equals(communicationTypeInfo.getText()
					.toString())) {
				checkitem = i;
			}
		}
		LayoutInflater factory = LayoutInflater
				.from(DrawerLeftFragment.this.getActivity());
		final View view = factory.inflate(R.layout.listview_layout, null);
		dialog = new com.moons.xst.track.widget.AlertDialog(
				DrawerLeftFragment.this.getActivity())
				.builder()
				.setTitle(
						getString(R.string.setting_head_title_sys_communicationtype))
				.setView(view)
				.setListViewItems(listData, checkitem,
						new AdapterView.OnItemClickListener() {

							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								communicationIndex = position;
								dialog.refresh(listData, communicationIndex);
							}
						})
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								communicationTypeInfo
										.setText(communication[communicationIndex]);
								appContext.setConfigCommunicationType(String
										.valueOf(communicationIndex));
								Main_Page.instance
										.sendRefreshRadioBtnBroadcast();
							}
						})
				.setNegativeButton(getString(R.string.cancel), null);
		dialog.show();
	}

	private void xjqCD() {
		if (AppContext.getPwdYN()) {
			UIHelper.ToastMessage(getActivity(), this
					.getString(R.string.setting_sys_settingconfig_needpwd_msg));
			return;
		}

		LayoutInflater factory = LayoutInflater.from(DrawerLeftFragment.this
				.getActivity());
		final View view = factory.inflate(R.layout.editbox_layout, null);
		final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
		edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
		edit.setText(xjqcdInfo.getText());
		edit.setSelection(xjqcdInfo.getText().toString().length());
		new com.moons.xst.track.widget.AlertDialog(
				DrawerLeftFragment.this.getActivity())
				.builder()
				.setTitle(getString(R.string.setting_head_title_comm_xjqcd))
				.setEditView(view, InputType.TYPE_CLASS_TEXT, "")
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// 如果巡检器标识符发生改变则刷新路线信息
								/*if (!AppContext.getxjqCD().equals(
										String.valueOf(edit.getText()))) {
									AppContext.setxjqCDIsAlter(true);
									Main_Page.instance().sendBroadcast();
								}*/
								xjqcdInfo.setText(String.valueOf(edit.getText()));
								appContext.setConfigXJQCD(String.valueOf(edit
										.getText()));
								AppContext.SetxjqCD(String.valueOf(edit
										.getText()));
							}
						}).setNegativeButton(getString(R.string.cancel), null)
				.show();
	}

	private void webServiceAddressForWlan() {
		if (AppContext.getPwdYN()) {
			UIHelper.ToastMessage(getActivity(), this
					.getString(R.string.setting_sys_settingconfig_needpwd_msg));
			return;
		}

		LayoutInflater factory = LayoutInflater.from(DrawerLeftFragment.this
				.getActivity());
		final View view = factory.inflate(R.layout.editbox_layout, null);
		webserviceEditTextForWlan = (EditText) view.findViewById(R.id.editText);
		webserviceEditTextForWlan.setText(wlanWsaddressInfo.getText());
		webserviceEditTextForWlan.setSelection(wlanWsaddressInfo.getText()
				.toString().length());
		new com.moons.xst.track.widget.AlertDialog(
				DrawerLeftFragment.this.getActivity())
				.builder()
				.setTitle(
						getString(R.string.setting_head_title_comm_wlan_webaddr))
				.setEditView(view, InputType.TYPE_CLASS_TEXT, "")
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								String identifier = String
										.valueOf(webserviceEditTextForWlan
												.getText());
								wlanWsaddressInfo.setText(identifier);
								appContext
										.setConfigWSAddressForWlan(identifier);
								String lanString = AppContext
										.getWebServiceAddress();
								AppContext.SetWSAddress(lanString, identifier);
							}
						}).setNegativeButton(getString(R.string.cancel), null)
				.show();

		/*
		 * webserviceEditTextForWlan .setOnLongClickListener(new
		 * OnLongClickListener() {
		 * 
		 * @Override public boolean onLongClick(View v) { Intent intent = new
		 * Intent(); intent.setClass(DrawerLeftFragment.this.getActivity(),
		 * CaptureActivity.class); intent.putExtra("ScanType", "EDITTEXT");
		 * startActivityForResult(intent, REQUEST_CODE_Wlan); return true; } });
		 */
	}

	private void webServiceAddress() {
		if (AppContext.getPwdYN()) {
			UIHelper.ToastMessage(getActivity(), this
					.getString(R.string.setting_sys_settingconfig_needpwd_msg));
			return;
		}

		LayoutInflater factory = LayoutInflater.from(DrawerLeftFragment.this
				.getActivity());
		final View view = factory.inflate(R.layout.editbox_layout, null);
		edit = (EditText) view.findViewById(R.id.editText);
		edit.setText(wsaddressInfo.getText());
		edit.setSelection(wsaddressInfo.getText().toString().length());
		new com.moons.xst.track.widget.AlertDialog(
				DrawerLeftFragment.this.getActivity())
				.builder()
				.setTitle(getString(R.string.setting_head_title_comm_webaddr))
				.setEditView(view, InputType.TYPE_CLASS_TEXT, "")
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								String WEBAddress = String.valueOf(edit
										.getText());
								wsaddressInfo.setText(WEBAddress);
								appContext.setConfigWSAddress(WEBAddress);
								String wlanString = AppContext
										.getWebServiceAddressForWlan();
								AppContext.SetWSAddress(
										String.valueOf(WEBAddress), wlanString);
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();

	}

	public void AppplicationUpdate() {
		if (NetWorkHelper.checkAppRunBaseCase(getActivity().getBaseContext())) {
			if (!NetWorkHelper.isNetworkAvailable(getActivity()
					.getBaseContext())) {
				UIHelper.ToastMessage(getActivity(),
						R.string.network_not_connected);
				return;
			}
			UpdateManager.getUpdateManager()
					.checkAppUpdate(getActivity(), true);
		}
	}
}
