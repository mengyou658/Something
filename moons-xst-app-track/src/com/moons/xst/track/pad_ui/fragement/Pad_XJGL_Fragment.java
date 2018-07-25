/**
 * 
 */
package com.moons.xst.track.pad_ui.fragement;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJLine_ListViewAdapter;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.dao.DJLineDAO;
import com.moons.xst.track.widget.LoadingDialog;

/**
 * @author LKZ
 * 
 */
public class Pad_XJGL_Fragment extends Fragment {

	View myLayout;
	private LoadingDialog loading;
	private Handler mHandler;
	private AppContext appContext;// 全局Context
	private DJLine_ListViewAdapter lvDJLineAdapter;
	private ListView djlineListView;

	private Pad_XJIDPos_Fragment xjidpos_Fragment;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myLayout = inflater.inflate(R.layout.fragment_home_xjgl, container,
				false);
		appContext = (AppContext) getActivity().getApplication();
		initView();
		loadUserDJLine();
		// 开启一个Fragment事务
		transaction = fragmentManager.beginTransaction();
		return myLayout;
	}

	private void initView() {
		djlineListView = (ListView) myLayout
				.findViewById(R.id.home_xjgl_content_left_view1_linelist);
		djlineListView.setOnItemClickListener(new Listener());
	}

	/**
	 * 点击Item
	 * 
	 */
	private class Listener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long ID) {
			// TODO Auto-generated method stub
			TextView viewTitile = (TextView) view
					.findViewById(R.id.djidpos_listitem_title);
			DJ_IDPos _idPos = (DJ_IDPos) viewTitile.getTag();
			if (AppContext.debug)
			 xjidpos_Fragment = new Pad_XJIDPos_Fragment();
			transaction.show(xjidpos_Fragment);
		}
	}
	
	/**
	 * 初始化路线列表
	 */
	private void loadMyDJLine() {
		DJLineDAO djlineDAO = new DJLineDAO();
		AppContext.DJLineBuffer = DJLineDAO.getDJLineInfoFromWS();
	}

	private void bindingUserDJLine() {

		//
		lvDJLineAdapter = new DJLine_ListViewAdapter(getActivity(),
				AppContext.DJLineBuffer, R.layout.pad_listitem_djline);

		djlineListView = (ListView) myLayout
				.findViewById(R.id.home_xjgl_content_left_view1_linelist);
		if (djlineListView == null) {
			return;
		}

		djlineListView.setAdapter(lvDJLineAdapter);

		djlineListView
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
}
