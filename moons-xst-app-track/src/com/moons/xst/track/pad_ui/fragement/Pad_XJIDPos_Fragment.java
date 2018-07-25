package com.moons.xst.track.pad_ui.fragement;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJIDPos_ListViewAdapter;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.widget.LoadingDialog;

public class Pad_XJIDPos_Fragment extends Fragment{

	View myLayout;
	private LoadingDialog loading;
	private Handler mHandler;
	private AppContext appContext;// 全局Context
	private DJIDPos_ListViewAdapter lvDJIDPosAdapter;
	private ListView djIDPosListView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myLayout = inflater.inflate(R.layout.fragment_home_xjidpos, container,
				false);
		appContext = (AppContext) getActivity().getApplication();
		initView();
		loadIDPos();
		return myLayout;
	}
	
	private void initView() {
		djIDPosListView = (ListView) myLayout
				.findViewById(R.id.home_xjidpos_content_left_view1_linelist);
	}

	/**
	 * 初始化路线列表
	 */
	private void loadIDPos() {
		for (int i = 0; i < 20; i++) {
			DJ_IDPos idpos = new DJ_IDPos();
			idpos.setPlace_TX( i+"号纽扣");
			idpos.setPlanCount("10");
			AppContext.DJIDPosBuffer.add(idpos);
		}

	}

	private void bindingUserDJLine() {

		//
		lvDJIDPosAdapter = new DJIDPos_ListViewAdapter(getActivity(),
				AppContext.DJIDPosBuffer, R.layout.pad_listitem_djline);

		djIDPosListView = (ListView) myLayout
				.findViewById(R.id.home_xjidpos_content_left_view1_linelist);
		if (djIDPosListView == null) {
			return;
		}

		djIDPosListView.setAdapter(lvDJIDPosAdapter);

		djIDPosListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					}
				});
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

	private void loadUserDJLineThread(final boolean isRefresh) {
		loading = new LoadingDialog(getActivity());
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 重新加载路线
					loadIDPos();

					Thread.sleep(1000);// debug

					msg.what = 1;
					msg.obj = AppContext.DJIDPosBuffer;
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
	
}
