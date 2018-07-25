package com.moons.xst.track.ui.queryhisdata_fragment;

import java.util.List;

import com.moons.xst.buss.DJHisDataHelper;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.HisMobjectStateAdapter;
import com.moons.xst.track.bean.XJ_MobjectStateHis;
import com.moons.xst.track.ui.QueryHisDataAllAty;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.PinnedHeaderListView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HisMobjectStateFragment extends Fragment {
	View hisMobjectStateFragment;

	PinnedHeaderListView listview;
	HisMobjectStateAdapter adapter;
	List<XJ_MobjectStateHis> list;
	// 加载动画
	LoadingDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (hisMobjectStateFragment == null) {
			hisMobjectStateFragment = inflater.inflate(
					R.layout.fragment_hismobjectstate, container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) hisMobjectStateFragment.getParent();
		if (parent != null) {
			parent.removeView(hisMobjectStateFragment);
		}
		return hisMobjectStateFragment;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	private void init() {
		listview = (PinnedHeaderListView) hisMobjectStateFragment
				.findViewById(R.id.listview_queryall_his_mobjectstate);
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(getActivity());
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		new Thread() {
			public void run() {
				list = DJHisDataHelper.GetIntance().loadTimeHisState(
						getActivity(),
						((QueryHisDataAllAty) getActivity()).lineID,
						((QueryHisDataAllAty) getActivity()).startTime,
						((QueryHisDataAllAty) getActivity()).endTime);
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
		adapter = new HisMobjectStateAdapter(getActivity(), list);
		// * 创建新的HeaderView，即置顶的HeaderView
		View HeaderView = getActivity().getLayoutInflater().inflate(
				R.layout.listview_item_header, listview, false);
		listview.setPinnedHeader(HeaderView);

		listview.setAdapter(adapter);
		listview.setOnScrollListener(adapter);
	}

	// 筛选后刷新方法
	public void screenRefresh(final String startTime, final String endTime) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(getActivity());
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		new Thread() {
			public void run() {
				list = DJHisDataHelper.GetIntance().loadTimeHisState(
						getActivity(),
						((QueryHisDataAllAty) getActivity()).lineID, startTime,
						endTime);
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}.start();
	}
}