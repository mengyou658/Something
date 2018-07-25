package com.moons.xst.track.ui.main_fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CustomFunctionAdapter;
import com.moons.xst.track.adapter.DJLine_ListViewAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.custom;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.SystemControlHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UpdateManager;
import com.moons.xst.track.dao.DJLineDAO;
import com.moons.xst.track.dao.DataTransHelper;
import com.moons.xst.track.ui.Main_Page;
import com.moons.xst.track.ui.ShowSettingAty;
import com.moons.xst.track.widget.ActionSheetDialog;
import com.moons.xst.track.widget.HorizontalListView;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.moons.xst.track.widget.ActionSheetDialog.SheetItemColor;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenu;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuListView;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuListView.OnMenuItemClickListener;

import de.greenrobot.event.EventBus;

public class HomeFragment extends Fragment {

	View homeFragment;

	private final static String CLEAN_LOADUSERDJLINE = "CLEANLOADUSERDJLINE";
	private final static String STATE_LOADUSERDJLINE = "LOADUSERDJLINE";
	private final static String STATE_LOADALLDJLINE = "LOADALLDJLINE";
	// 路线列表长按菜单三项操作
	private static final int MENUACTION_DODJ = 0;// 执行点检
	private static final int MENUACTION_QUERY = 1;// 查询数据
	private static final int MENUACTION_RETURN = 2;// 返回
	private static final int MENUACTION_TEMPTASK = 3;// 临时任务

	HorizontalListView lv_horizontal;

	DJLine_ListViewAdapter lvDJLineAdapter;
	SwipeMenuListView lvDJLine;
	EditText etSearch;

	LinearLayout ll_custom;
	AppContext appContext;

	CustomFunctionAdapter adapter;
	LoadingDialog loading;
	LoadingDialog mLoading;
	Handler mHandler;

	List<custom> mList = new ArrayList<custom>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (homeFragment == null) {
			homeFragment = inflater.inflate(R.layout.fragment_home, container,
					false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) homeFragment.getParent();
		if (parent != null) {
			parent.removeView(homeFragment);
		}
		return homeFragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		appContext = (AppContext) getActivity().getApplication();
		init();
		if (AppContext.getCheckInAfterEnterLine()) {
			loadAllDJLine();
		} else {
			loadUserDJLine();
		}
		registerBoradcastReceiver();
	}

	@Override
	public void onResume() {
		super.onResume();		
		/*// 刷新快捷操作栏
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.TwoBill.toString()).LoadConfigByType();
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.Overhaul.toString()).LoadConfigByType();*/
		/*getList();
		if (mList == null || mList.size() <= 0) {
			ll_custom.setVisibility(View.GONE);
		} else {
			ll_custom.setVisibility(View.VISIBLE);
			if (adapter != null) {
				adapter.refresh(mList);
			} else {
				adapter = new CustomFunctionAdapter(getActivity(), mList);
				lv_horizontal.setAdapter(adapter);
			}

		}*/
		
		refreshCustomShortcut();
		etSearch.clearFocus();
		if (AppContext.GetRefreshLineYN()) {
			if (AppContext.isLogin()) {
				// 刷新Listview
				AppContext.SetRefreshLineYN(false);
				File file = new File(AppConst.XSTBasePath()
						+ AppConst.DJLineXmlFile);
				if (file.exists()) {
					loadUserDJLine();
				}
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unregisterBoradcastReceiver();
	}

	private void init() {

		ll_custom = (LinearLayout) (homeFragment).findViewById(R.id.ll_custom);
		lv_horizontal = (HorizontalListView) (homeFragment)
				.findViewById(R.id.lv_horizontal);
		lv_horizontal.setViewPager(Main_Page.instance.viewPager);
		lv_horizontal.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				String mCustom = mList.get(position).getTag();
				if (mCustom.equals("isCewen")) {// 测温
					EventBus.getDefault().post("CW");
				} else if (mCustom.equals("isCezhen")) {// 测振
					EventBus.getDefault().post("CZ");
				} else if (mCustom.equals("isTwoBill")) {// 两票
					UIHelper.showTwoTickets(getActivity());
				} else if (mCustom.equals("isCZS")) {// 测转速
					EventBus.getDefault().post("CS");
				} else if (mCustom.equals("isCamera")) {// 相机
					UIHelper.showCamera(getActivity());
				} else if (mCustom.equals("isPlanDownLoad")) {// 数据同步
					if (AppContext.getCommunicationType().equals(
							AppConst.CommunicationType_Wireless)) {// Wifi
						if (!NetWorkHelper.isNetworkAvailable(getActivity()
								.getBaseContext())) {
							UIHelper.ToastMessage(getActivity(),
									R.string.network_not_connected);
							return;
						}
						if (mLoading == null) {
							mLoading = new LoadingDialog(getActivity());
							mLoading.setCanceledOnTouchOutside(false);
							mLoading.setCancelable(false);
						}
						UIHelper.JudgePlanTypeAndCustomType(getActivity(),
								mLoading);
					} else {// usb
						if (StringUtils.isEmpty(AppContext.GetxjqCD())) {
							UIHelper.ToastMessage(getActivity(),
									R.string.download_message_errorxjqcd);
							return;
						}
						UIHelper.showUSBCommuDownLoad(getActivity());
					}
				} else if (mCustom.equals("isOverhaul")) {// 检修管理
					UIHelper.showOverhaulManagement(getActivity());
				}

			}
		});
		etSearch = (EditText) (homeFragment).findViewById(R.id.search_content);
		etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO 自动生成的方法存根
				boolean b = false;
				if (hasFocus) {
					EventBus.getDefault().post("Search");
				}
			}
		});
	}
	
	//刷新适配器
	public void refreshAdapter(){
		if(lvDJLineAdapter!=null){
			lvDJLineAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 根据登录的人员信息刷新相关数据
	 */
	private void loadUserDJLine() {

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					// disp data loaded

					bindingUserDJLine();// 获取的数据绑定到列表中

				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(getActivity());
				}
			}
		};
		this.loadUserDJLineThread(false);
	}

	private void loadAllDJLine() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					bindingUserDJLine();// 获取的数据绑定到列表中

				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(getActivity());
				}
			}
		};
		this.loadAllDJLineThread(false);
	}

	private void loadAllDJLineThread(final boolean isRefresh) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(getActivity());
			loading.setLoadConceal();
			loading.show();
		}

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					//如果配置为自动清理则清理过期路线
					if(AppContext.getClearDJPCLineMode()
							.equalsIgnoreCase(AppConst.ClearDJPCLineMode.Auto.toString())){
						DataTransHelper.ClearPastDueLine();
					}
					// 重新加载路线
					loadDJLine();
					Thread.sleep(1000);// debug

					msg.what = 1;
					msg.obj = AppContext.DJLineBuffer;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
					if (loading != null)
						loading.dismiss();
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	private void loadUserDJLineThread(final boolean isRefresh) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(getActivity());
			loading.setLoadConceal();
			loading.show();
		}

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					//如果配置为自动清理则清理过期路线
					if(AppContext.getClearDJPCLineMode()
							.equalsIgnoreCase(AppConst.ClearDJPCLineMode.Auto.toString())){
						DataTransHelper.ClearPastDueLine();
					}
					// 重新加载路线
					loadMyDJLine();
					Thread.sleep(1000);// debug

					msg.what = 1;
					msg.obj = AppContext.DJLineBuffer;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
					if (loading != null)
						loading.dismiss();
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 初始化路线列表
	 */
	private void loadMyDJLine() {
		AppContext.DJLineBuffer = DJLineDAO.loadDJLineByUser(appContext
				.getLoginUid());

		/*
		 * if (AppContext.getStartXDJYN())
		 * DJPlanHelper.GetIntance().loadDJSpecCase(appContext,
		 * AppContext.DJLineBuffer);
		 */
	}

	private void loadDJLine() {
		AppContext.DJLineBuffer = DJLineDAO.loadAllDJLine();
	}

	private void bindingUserDJLine() {
		lvDJLine = (SwipeMenuListView) homeFragment
				.findViewById(R.id.home_listview_djline);
		lvDJLineAdapter = new DJLine_ListViewAdapter(getActivity(),
				AppContext.DJLineBuffer, R.layout.listitem_djline, lvDJLine);
		if (lvDJLine == null) {
			return;
		}

		lvDJLine.setAdapter(lvDJLineAdapter);

		// 点击事件
		lvDJLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (SystemControlHelper.isFastDoubleClick())
					return;

				TextView aTitle = (TextView) view
						.findViewById(R.id.djline_listitem_title);
				if (aTitle == null) {
					return;
				}
				final DJLine adjline = (DJLine) aTitle.getTag();

				if (adjline == null)
					return;
				enterSwictherModule(view.getContext(), adjline);
			}
		});
		// 长按事件
		lvDJLine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TextView aTitle = (TextView) arg1
						.findViewById(R.id.djline_listitem_title);
				if (aTitle == null) {
					return false;
				}
				final DJLine adjline = (DJLine) aTitle.getTag();

				if (adjline == null) {
					return false;
				}

				if (adjline.getlineType() == AppConst.LineType.GPSXX
						.getLineType()
						|| adjline.getlineType() == AppConst.LineType.GPSXXNew
								.getLineType()) {
					new ActionSheetDialog(getActivity())
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(true)
							.addSheetItem(
									getString(R.string.main_menu_home_lvMemu_TempTask),
									SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											UIHelper.showTempTaskList(getActivity());
										}
									}).show();
				} else if (adjline.getlineType() == AppConst.LineType.DJPC.getLineType()){
					//  手动清理
					if(AppContext.getClearDJPCLineMode()
							.equalsIgnoreCase(AppConst.ClearDJPCLineMode.Manual.toString())){
						lvDJLine.smoothOpenMenu(arg2);
					}
				}
				return true;
			}
		});
		// 菜单点击事件
		lvDJLine.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu,
					int index) {
				// TODO 自动生成的方法存根
				switch (index) {
				case 0:// 删除
					LayoutInflater factory = LayoutInflater.from(getActivity());
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					new com.moons.xst.track.widget.AlertDialog(getActivity())
							.builder()
							.setTitle(getString(R.string.tips))
							.setView(view)
							.setMsg(getString(R.string.delete_line_surelogout))
							.setPositiveButton(getString(R.string.sure),
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											DJLine djLine = lvDJLineAdapter
													.getData().get(position);
											// 删除下载库
											File downloadPath = new File(
													AppConst.XSTDBPath()
															+ "424DB_"
															+ djLine.getLineID()
															+ ".sdf");
											downloadPath.delete();
											// 删除结果库
											String ResultPath = AppConst
													.XSTResultPath()
													+ djLine.getLineID();
											FileUtils
													.deleteDirectory(ResultPath);
											// 删除LineList中的路线
											String LineListPath = AppConst
													.XSTBasePath()
													+ "LineList.xml";
											boolean isSucceed = DataTransHelper
													.DeleteLine(LineListPath,
															djLine.getLineID()
																	+ "");
											if (isSucceed) {
												((Main_Page) getActivity())
														.sendBroadcast();
											} else {
												UIHelper.ToastMessage(
														getActivity(),
														getString(R.string.delete_line_defeated));
											}
										}
									})
							.setNegativeButton(getString(R.string.cancel), null)
							.setCanceledOnTouchOutside(false).show();
					break;
				}
				return false;
			}
		});

		/*
		 * lvDJLine.setOnCreateContextMenuListener(new
		 * OnCreateContextMenuListener() {
		 * 
		 * @Override public void onCreateContextMenu(ContextMenu menu, View v,
		 * ContextMenuInfo menuInfo) { //
		 * menu.setHeaderTitle(R.string.main_menu_home_lvMemu); menu.add(0,
		 * MENUACTION_DODJ, 0, R.string.main_menu_home_lvMemu_toXX); menu.add(0,
		 * MENUACTION_QUERY, 0, R.string.main_menu_home_lvMemu_Query);
		 * menu.add(0, MENUACTION_TEMPTASK, 0,
		 * R.string.main_menu_home_lvMemu_TempTask); menu.add(0,
		 * MENUACTION_RETURN, 0, R.string.main_menu_home_Return); } });
		 */
	}
	
	public void refreshCustomShortcut() {
		getList();
		if (mList == null || mList.size() <= 0) {
			ll_custom.setVisibility(View.GONE);
		} else {
			ll_custom.setVisibility(View.VISIBLE);
			if (adapter != null) {
				adapter.refresh(mList);
			} else {
				adapter = new CustomFunctionAdapter(getActivity(), mList);
				lv_horizontal.setAdapter(adapter);
			}

		}
	}

	private void getList() {
		List<custom> list = new ArrayList<custom>();
		list = ShowSettingAty.getList(AppContext.getShortcutFunction());
		mList.clear();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getQuickState()) {
				if (list.get(i).getTag().equals("isTwoBill")) {
					if (AppContext.getTwoBillYN()) {
						mList.add(list.get(i));
					}
				} else if (list.get(i).getTag().equals("isCewen")) {
					if (AppContext.getTemperatureUseYN()) {
						mList.add(list.get(i));
					}
				} else if (list.get(i).getTag().equals("isCezhen")) {
					if (AppContext.getVibrationUseYN()) {
						mList.add(list.get(i));
					}
				} else if (list.get(i).getTag().equals("isCZS")) {
					if (AppContext.getSpeedUseYN()) {
						mList.add(list.get(i));
					}
				} else if (list.get(i).getTag().equals("isOverhaul")) {
					if (AppContext.getOverhaulYN()) {
						mList.add(list.get(i));
					}
				} else {
					mList.add(list.get(i));
				}
			}
		}
	}

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		int po = lvDJLine.getFirstVisiblePosition();

		View v = (View) lvDJLine.getChildAt(info.position - po);
		DJLine adjline = null;
		if (v == null) {
			return false;
		}
		TextView aTitle = (TextView) v.findViewById(R.id.djline_listitem_title);
		if (aTitle == null) {
			return false;
		}
		adjline = (DJLine) aTitle.getTag();

		if (adjline == null) {
			return false;
		}
		switch (item.getItemId()) {
		case MENUACTION_DODJ:
			enterSwictherModule(getActivity(), adjline);
			break;
		case MENUACTION_QUERY:
			if (adjline.getlineType() == 0) {
				UIHelper.showDJQuerydata(getActivity(), adjline);
			} else if (adjline.getlineType() == 6 || adjline.getlineType() == 7) {
				UIHelper.showQuerydata(getActivity(), adjline);
			} else if (adjline.getlineType() == 8) {
				UIHelper.showDJQuerydata(getActivity(), adjline);
			}
			break;

		case MENUACTION_TEMPTASK:
			UIHelper.showTempTaskList(getActivity());
			break;

		case MENUACTION_RETURN:
			return false;
		}

		return super.onContextItemSelected(item);

	}

	private void enterSwictherModule(Context context, final DJLine adjline) {
		if (!FileUtils.checkFileExists(AppConst.XSTDBPath()
				+ AppConst.PlanDBName(adjline.getLineID()))) {
			UIHelper.ToastMessage(getActivity(),
					R.string.home_line_noplanfile_notice);
			return;
		}

		if (AppContext.getCheckInAfterEnterLine()) {
			AppContext.setCurrLineInfo(adjline);
			AppContext.setFromLoginYN(false);
			// UIHelper.showLoginDialog(getActivity());
			EventBus.getDefault().post("Login");
		} else {
			if (AppContext.isLogin()
					&& !StringUtils.isEmpty(AppContext.getUserName()))
				UIHelper.enterMyWork(context, adjline);
			else {
				UIHelper.ToastMessage(getActivity(),
						R.string.mainpage_message_notlogin);
				return;
			}
		}
	}

	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(STATE_LOADUSERDJLINE);
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);

		IntentFilter myIntentFilter1 = new IntentFilter();
		myIntentFilter1.addAction(CLEAN_LOADUSERDJLINE);
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter1);

		IntentFilter loadAllDJLineIntentFilter = new IntentFilter();
		loadAllDJLineIntentFilter.addAction(STATE_LOADALLDJLINE);
		getActivity().registerReceiver(mBroadcastReceiver,
				loadAllDJLineIntentFilter);
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
	 * 利用广播方式刷新路线LISTVIEW
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(STATE_LOADUSERDJLINE)) {
				File file = new File(AppConst.XSTBasePath()
						+ AppConst.DJLineXmlFile);
				if (file.exists()) {
					loadUserDJLine();
				}
			} else if (intent.getAction().equals(CLEAN_LOADUSERDJLINE)) {
				if (lvDJLineAdapter != null && AppContext.DJLineBuffer != null) {
					AppContext.DJLineBuffer.clear();
					lvDJLineAdapter.notifyDataSetChanged();
				}
			} else if (intent.getAction().equals(STATE_LOADALLDJLINE)) {
				File file = new File(AppConst.XSTBasePath()
						+ AppConst.DJLineXmlFile);
				if (file.exists()) {
					loadAllDJLine();
				}
			}
		}
	};
}
