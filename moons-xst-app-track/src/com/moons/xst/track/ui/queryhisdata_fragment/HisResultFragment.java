package com.moons.xst.track.ui.queryhisdata_fragment;

import java.util.ArrayList;
import java.util.List;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.DJHisDataHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.HisDJResultAdapter;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.ui.QueryHisDataAllAty;
import com.moons.xst.track.ui.VibrationActivity;
import com.moons.xst.track.ui.VibrationPlaybackAty;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.PinnedHeaderListView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class HisResultFragment extends Fragment {
	View hisResultFragment;
	PinnedHeaderListView listview;
	TextView tv_total, tv_normal, tv_error;
	HisDJResultAdapter adapter;
	// 加载动画
	LoadingDialog loading;

	private List<XJ_ResultHis> list;
	public List<String[]> ResultArray;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (hisResultFragment == null) {
			hisResultFragment = inflater.inflate(R.layout.fragment_hisresult,
					container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) hisResultFragment.getParent();
		if (parent != null) {
			parent.removeView(hisResultFragment);
		}
		return hisResultFragment;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			initDate();
		}
	};

	private void init() {
		tv_total = (TextView) hisResultFragment
				.findViewById(R.id.tv_hisresult_total);
		tv_normal = (TextView) hisResultFragment
				.findViewById(R.id.tv_hisresult_normal);
		tv_error = (TextView) hisResultFragment
				.findViewById(R.id.tv_hisresult_error);
		listview = (PinnedHeaderListView) hisResultFragment
				.findViewById(R.id.listview_queryall_his_result);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				if (!StringUtils.isEmpty(list.get(position).getDataType_CD()) 
						&& list.get(position).getDataType_CD().equals(AppConst.DJPLAN_DATACODE_CZ)) {
					UIHelper.showVibrationPlayback(getActivity(),
							list.get(position));
				}
			}
		});
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(getActivity());
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		new Thread() {
			public void run() {
				list = DJHisDataHelper.GetIntance().loadTimeDJHisDate(
						getActivity(),
						((QueryHisDataAllAty) getActivity()).lineID,
						((QueryHisDataAllAty) getActivity()).startTime,
						((QueryHisDataAllAty) getActivity()).endTime, null);
				List<String> list = DJHisDataHelper.GetIntance().loadDJHisNK(
						getActivity(),
						((QueryHisDataAllAty) getActivity()).lineID);
				ResultArray = new ArrayList<String[]>();
				String[] str = null;
				for (int i = 0; i < list.size(); i++) {
					str = new String[2];
					str[0] = list.get(i);
					str[1] = "false";
					ResultArray.add(str);
				}
					// 全部
					str = new String[2];
					str[0] = getString(R.string.query_select_conditions_all);
					str[1] = "false";
					ResultArray.add(0, str);
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}.start();
	}

	private void initDate() {
		if (list == null) {
			tv_total.setText(getString(R.string.query_result_msg_total, 0));
			tv_normal.setText(getString(R.string.query_result_msg_normal, 0));
			tv_error.setText(getString(R.string.query_result_msg_error, 0));
			return;
		}
		adapter = new HisDJResultAdapter(getActivity(), list);
		// * 创建新的HeaderView，即置顶的HeaderView
		View HeaderView = getActivity().getLayoutInflater().inflate(
				R.layout.listview_item_header, listview, false);
		listview.setPinnedHeader(HeaderView);

		listview.setAdapter(adapter);
		listview.setOnScrollListener(adapter);
		
		tv_total.setText(getString(R.string.query_result_msg_total, adapter.getCount()));
		tv_error.setText(getString(R.string.query_result_msg_error, adapter.getErrorCount()));
		tv_normal.setText(getString(R.string.query_result_msg_normal, adapter.getCount() - adapter.getErrorCount()));		
	}

	// 筛选后刷新方法
	public void screenRefresh(final String content, final String startTime,
			final String endTime, final List<String> screenList,
			final String type) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(getActivity());
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		new Thread() {
			public void run() {
				// 查询规定时间内的所有数据
				list = DJHisDataHelper.GetIntance().loadTimeDJHisDate(
						getActivity(),
						((QueryHisDataAllAty) getActivity()).lineID, startTime,
						endTime, type);
				// 根据搜索内容和选择list筛选数据
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).getPlanDesc_TX().contains(content)) {
						list.remove(i);
						i--;
						continue;
					}
					if (!screenList.contains(list.get(i).getIDPosName_TX())
							&& screenList.size() > 0) {
						list.remove(i);
						i--;
					}
				}
				for (int i = 0; i < ResultArray.size(); i++) {
					ResultArray.get(i)[1] = "false";
				}
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}.start();
	}
}