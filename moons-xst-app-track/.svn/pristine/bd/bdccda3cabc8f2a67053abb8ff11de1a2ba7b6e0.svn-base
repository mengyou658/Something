package com.moons.xst.track.ui.queryhisdata_fragment;

import java.util.ArrayList;
import java.util.List;

import com.moons.xst.buss.DJHisDataHelper;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.HisAbsenceAdapter;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;
import com.moons.xst.track.ui.QueryHisDataAllAty;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.PinnedHeaderListView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HisAbsenceFragment extends Fragment {
	View hisAbsenceFragment;

	PinnedHeaderListView listview;
	HisAbsenceAdapter adapter;
	List<XJ_TaskIDPosHis> list;
	// 加载动画
	LoadingDialog loading;

	public List<String[]> AbsenceArray;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (hisAbsenceFragment == null) {
			hisAbsenceFragment = inflater.inflate(R.layout.fragment_hisabsence,
					container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) hisAbsenceFragment.getParent();
		if (parent != null) {
			parent.removeView(hisAbsenceFragment);
		}
		return hisAbsenceFragment;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	private void init() {
		listview = (PinnedHeaderListView) hisAbsenceFragment
				.findViewById(R.id.listview_queryall_his_absence);
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(getActivity());
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		new Thread() {
			public void run() {
				list = DJHisDataHelper.GetIntance().loadTimeTaskHisDate(
						getActivity(),
						((QueryHisDataAllAty) getActivity()).lineID,
						((QueryHisDataAllAty) getActivity()).startTime,
						((QueryHisDataAllAty) getActivity()).endTime);
				List<String> list = DJHisDataHelper.GetIntance().loadTaskHisNK(
						getActivity(),
						((QueryHisDataAllAty) getActivity()).lineID);
				AbsenceArray = new ArrayList<String[]>();
				String[] str = null;
				for (int i = 0; i < list.size(); i++) {
					str = new String[2];
					str[0] = list.get(i);
					str[1] = "false";
					AbsenceArray.add(str);
				}
				// 全部
				str = new String[2];
				str[0] = getString(R.string.query_select_conditions_all);
				str[1] = "false";
				AbsenceArray.add(0, str);
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}.start();
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			initDate();
		}
	};

	private void initDate() {
		if (list == null) {
			return;
		}
		adapter = new HisAbsenceAdapter(getActivity(), list);
		// * 创建新的HeaderView，即置顶的HeaderView
		View HeaderView = getActivity().getLayoutInflater().inflate(
				R.layout.listview_item_header, listview, false);
		listview.setPinnedHeader(HeaderView);

		listview.setAdapter(adapter);
		listview.setOnScrollListener(adapter);
	}

	// 筛选后刷新方法
	public void screenRefresh(final String startTime, final String endTime,
			final List<String> screenList) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(getActivity());
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		new Thread() {
			public void run() {
				// 查询规定时间内的所有数据
				list = DJHisDataHelper.GetIntance().loadTimeTaskHisDate(
						getActivity(),
						((QueryHisDataAllAty) getActivity()).lineID, startTime,
						endTime);
				// 根据选择list的筛选数据
				for (int i = 0; i < list.size(); i++) {
					if (!screenList.contains(list.get(i).getIDPosDesc_TX())
							&& screenList.size() > 0) {
						list.remove(i);
						i--;
					}
				}
				for (int i = 0; i < AbsenceArray.size(); i++) {
					AbsenceArray.get(i)[1] = "false";
				}
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}.start();
	}
}