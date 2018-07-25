package com.moons.xst.track.ui.main_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.R;
import com.moons.xst.track.common.UIHelper;

public class QueryFragment extends Fragment implements View.OnClickListener {
	
	View queryFragment;

	RelativeLayout rl_layout_task_response, rl_layout_query_uncheck, rl_layout_query_hisdata;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (queryFragment == null) {
			queryFragment = inflater.inflate(R.layout.fragment_query, container,
					false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) queryFragment.getParent();
		if (parent != null) {
			parent.removeView(queryFragment);
		}
		return queryFragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	private void init() {
		rl_layout_task_response = (RelativeLayout) queryFragment
				.findViewById(R.id.rl_layout_task_response);
		rl_layout_task_response.setOnClickListener(this);
		rl_layout_query_uncheck = (RelativeLayout) queryFragment
				.findViewById(R.id.rl_layout_query_uncheck);
		rl_layout_query_uncheck.setOnClickListener(this);
		rl_layout_query_hisdata = (RelativeLayout) queryFragment
				.findViewById(R.id.rl_layout_query_hisdata);
		rl_layout_query_hisdata.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 任务完成情况
		case R.id.rl_layout_task_response:
			UIHelper.loadQueryDJLine(getActivity(), AppConst.QueryType.TaskResponse.toString());
			break;
		// 漏检统计
		case R.id.rl_layout_query_uncheck:
			UIHelper.loadQueryDJLine(getActivity(), AppConst.QueryType.Uncheck.toString());
			break;
		// 历史数据查询
		case R.id.rl_layout_query_hisdata:
			UIHelper.loadQueryDJLine(getActivity(), AppConst.QueryType.QueryHisData.toString());
			break;
		}
	}
}