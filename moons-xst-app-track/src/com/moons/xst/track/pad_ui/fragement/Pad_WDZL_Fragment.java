/**
 * 
 */
package com.moons.xst.track.pad_ui.fragement;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.pad_adapter.Pad_FileListAdapter;
import com.moons.xst.track.adapter.pad_adapter.Pad_SecondMenuApter;
import com.moons.xst.track.bean.FileInfo;
import com.moons.xst.track.bean.SecondMenuInfo;
import com.moons.xst.track.common.OfficeHelper;
import com.moons.xst.track.widget.GestureListener;
import com.moons.xst.track.widget.LoadingDialog;

/**
 * @author LKZ
 * 
 */
public class Pad_WDZL_Fragment extends Fragment {

	View myLayout;
	private LoadingDialog loading;
	private Handler mHandler, mWDZLHandler;
	private AppContext appContext;// 全局Context
	private Pad_SecondMenuApter secondMenuApter;
	private Pad_FileListAdapter fileListAdapter;
	private ListView secondMenuListView, wdzListView;
	private RelativeLayout menuLayout, contentLayout, contentTitleLayout;

	private List<FileInfo> WDZLFileInfos = new ArrayList<FileInfo>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myLayout = inflater.inflate(R.layout.fragment_home_wdzl, container,
				false);
		appContext = (AppContext) getActivity().getApplication();
		initView();
		loadMenu();
		return myLayout;
	}

	private void initView() {
		menuLayout = (RelativeLayout) myLayout
				.findViewById(R.id.fl_home_wdzl_content_left);
		menuLayout.setTag(0);
		menuLayout.setVisibility(View.VISIBLE);
		contentLayout = (RelativeLayout) myLayout
				.findViewById(R.id.fl_home_wdzl_content_right);
		contentLayout.setLongClickable(true);
		contentLayout.setOnTouchListener(new MyGestureListener(getActivity()));
		secondMenuListView = (ListView) myLayout
				.findViewById(R.id.home_wdzl_content_left_view1_listview);
		wdzListView = (ListView) myLayout
				.findViewById(R.id.home_wdzl_content_right_view1_listview);
		contentTitleLayout = (RelativeLayout) myLayout
				.findViewById(R.id.fl_home_wdzl_content_right_view1_title);
		contentTitleLayout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				if (menuLayout.getTag().equals(0)) {
					menuLayout.setVisibility(View.GONE);
					menuLayout.setTag(1);
				} else {
					menuLayout.setVisibility(View.VISIBLE);
					menuLayout.setTag(0);
				}
				return true;
			}
		});
	}

	/**
	 * 初始化
	 */
	private void loadMenuData() {
		SecondMenuInfo menuInfo = new SecondMenuInfo();
		menuInfo.setMenuItemID(1);
		menuInfo.setMenuItemDesc("标准规范");
		menuInfo.setMenuItemLevel(0);
		menuInfo.setChildrenCount(4);
		AppContext.secondMenuBuffer.add(menuInfo);
		//
		// menuInfo.setMenuItemID(14);
		// menuInfo.setMenuItemDesc("二级分类1");
		// menuInfo.setMenuItemLevel(1);
		// menuInfo.setChildrenCount(0);
		// AppContext.secondMenuBuffer.add(menuInfo);
		//
		// menuInfo.setMenuItemID(15);
		// menuInfo.setMenuItemDesc("二级分类2");
		// menuInfo.setMenuItemLevel(1);
		// menuInfo.setChildrenCount(0);
		// AppContext.secondMenuBuffer.add(menuInfo);
		//
		// menuInfo.setMenuItemID(16);
		// menuInfo.setMenuItemDesc("二级分类3");
		// menuInfo.setMenuItemLevel(1);
		// menuInfo.setChildrenCount(0);
		// AppContext.secondMenuBuffer.add(menuInfo);
		//
		// menuInfo.setMenuItemID(17);
		// menuInfo.setMenuItemDesc("二级分类4");
		// menuInfo.setMenuItemLevel(1);
		// menuInfo.setChildrenCount(0);
		menuInfo = new SecondMenuInfo();
		menuInfo.setMenuItemID(2);
		menuInfo.setMenuItemDesc("大小修资料");
		menuInfo.setMenuItemLevel(0);
		menuInfo.setChildrenCount(0);
		AppContext.secondMenuBuffer.add(menuInfo);
		menuInfo = new SecondMenuInfo();
		menuInfo.setMenuItemID(3);
		menuInfo.setMenuItemDesc("图纸资料");
		menuInfo.setMenuItemLevel(0);
		menuInfo.setChildrenCount(0);
		AppContext.secondMenuBuffer.add(menuInfo);
	}

	private void bindingMenuData() {

		//
		secondMenuApter = new Pad_SecondMenuApter(getActivity(),
				AppContext.secondMenuBuffer, R.layout.pad_listitem_second_menu);

		secondMenuListView = (ListView) myLayout
				.findViewById(R.id.home_wdzl_content_left_view1_listview);
		if (secondMenuListView == null) {
			return;
		}

		secondMenuListView.setAdapter(secondMenuApter);

		secondMenuListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						loadFileList();
					}
				});
	}

	/**
	 * 加载目录
	 */
	private void loadMenu() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					// disp data loaded
					bindingMenuData();// 获取的数据绑定到列表中

				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(getActivity());
				}
			}
		};
		this.loadMenuDataThread(false);
	}

	private void loadMenuDataThread(final boolean isRefresh) {
		loading = new LoadingDialog(getActivity());
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					loadMenuData();
					msg.what = 1;
					msg.obj = AppContext.DJLineBuffer;
				} catch (Exception e) {
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

	private void loadFileListData() {
		WDZLFileInfos.clear();
		FileInfo _fileFileInfo = new FileInfo();
		_fileFileInfo.setFileID(1);
		_fileFileInfo.setFileName("“小神探”巡检客户端用户使用手册（Android版）讨论版.doc");
		_fileFileInfo.setFilePath(AppConst.XSTBasePath()
				+ "/“小神探”巡检客户端用户使用手册（Android版）讨论版.doc");
		_fileFileInfo.setFileTime("2015-07-03");
		_fileFileInfo.setFileType("DOC");
		WDZLFileInfos.add(_fileFileInfo);
		_fileFileInfo = new FileInfo();
		_fileFileInfo.setFileID(1);
		_fileFileInfo.setFileName("东风日产工厂备件系统工时估计.xls");
		_fileFileInfo.setFilePath(AppConst.XSTBasePath()
				+ "/东风日产工厂备件系统工时估计.xls");
		_fileFileInfo.setFileTime("2015-07-08");
		_fileFileInfo.setFileType("XLS");
		WDZLFileInfos.add(_fileFileInfo);
		_fileFileInfo = new FileInfo();
		_fileFileInfo.setFileID(1);
		_fileFileInfo.setFileName("人月神话.pdf");
		_fileFileInfo.setFilePath(AppConst.XSTBasePath() + "/人月神话.pdf");
		_fileFileInfo.setFileTime("2015-07-05");
		_fileFileInfo.setFileType("PDF");
		WDZLFileInfos.add(_fileFileInfo);
	}

	private void bindingFileListData() {

		//
		fileListAdapter = new Pad_FileListAdapter(getActivity(), WDZLFileInfos,
				R.layout.pad_listitem_fileinfo);

		wdzListView = (ListView) myLayout
				.findViewById(R.id.home_wdzl_content_right_view1_listview);
		if (wdzListView == null) {
			return;
		}

		wdzListView.setAdapter(fileListAdapter);

		wdzListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView aTitle = (TextView) view
								.findViewById(R.id.djline_listitem_title);
						if (aTitle == null) {
							return;
						}
						final FileInfo _info = (FileInfo) aTitle.getTag();

						if (_info == null)
							return;
						OfficeHelper.openWPSFile(getActivity(),
								_info.getFilePath());
					}
				});
	}

	/**
	 * 加载文档列表
	 */
	private void loadFileList() {
		mWDZLHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					// disp data loaded
					bindingFileListData();// 获取的数据绑定到列表中

				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(getActivity());
				}
			}
		};
		this.loadFileListDataThread(false);
	}

	private void loadFileListDataThread(final boolean isRefresh) {
		loading = new LoadingDialog(getActivity());
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					loadFileListData();
					msg.what = 1;
					msg.obj = WDZLFileInfos;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
					if (loading != null)
						loading.dismiss();
				}
				mWDZLHandler.sendMessage(msg);
			}
		}.start();
	}

	private class MyGestureListener extends GestureListener {
		@Override
		public boolean left() {
			menuLayout.setVisibility(View.GONE);
			return true;
		}

		@Override
		public boolean right() {
			menuLayout.setVisibility(View.VISIBLE);
			return true;
		}

		public MyGestureListener(Context context) {
			super(context);
		}
	}
}
