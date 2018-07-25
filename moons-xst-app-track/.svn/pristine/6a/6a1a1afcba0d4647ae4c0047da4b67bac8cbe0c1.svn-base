package com.moons.xst.track.ui.operatebill_fragment;

import com.moons.xst.buss.OperateHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.Operate_Bill;
import com.moons.xst.track.ui.OperationDetailsAty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 操作详情fragment
 * **/
public class OperationDetailsFragment extends Fragment {
	View operationDetails;

	private String Code;
	private OperateHelper operateHelper;
	private Operate_Bill Operate;
	TextView tv_operation_contents, tv_starttime, tv_endtime,
			tv_operation_user, tv_jh_user, tv_zhuzhi_user, tv_zhizhang_user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (operationDetails == null) {
			operationDetails = inflater.inflate(
					R.layout.operation_details_viewpager, container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) operationDetails.getParent();
		if (parent != null) {
			parent.removeView(operationDetails);
		}
		return operationDetails;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		operateHelper = new OperateHelper();
		OperationDetailsAty activity = (OperationDetailsAty) getActivity();
		Code = activity.Code;
		Operate = operateHelper.aOperateQuery(getActivity(), Code);
		initView();
	}

	private void initView() {
		// 操作任务
		tv_operation_contents = (TextView) operationDetails
				.findViewById(R.id.text_operation_contents);
		tv_operation_contents.setText(Operate.getOperate_Task_Content());
		tv_operation_contents.setMovementMethod(ScrollingMovementMethod.getInstance());
		// 开始时间
		tv_starttime = (TextView) operationDetails
				.findViewById(R.id.textview_starttime);
		tv_starttime.setText(Operate.getOperate_Begin_Time());
		// 结束时间
		tv_endtime = (TextView) operationDetails
				.findViewById(R.id.textview_endtime);
		tv_endtime.setText(Operate.getOperate_End_Time());
		// 操作人
		tv_operation_user = (TextView) operationDetails
				.findViewById(R.id.textview_operation_user);
		tv_operation_user.setText(getString(R.string.operationDetails_operation_person) + Operate.getOperate_Bill_Operator());
		// 监护人
		tv_jh_user = (TextView) operationDetails
				.findViewById(R.id.textview_jh_user);
		tv_jh_user.setText(getString(R.string.operationDetails_tutelage_person) + Operate.getOperate_Bill_Guardian());
		// 主值
		tv_zhuzhi_user = (TextView) operationDetails
				.findViewById(R.id.textview_zhuzhi_user);
		tv_zhuzhi_user.setText(getString(R.string.operationDetails_main_person) + Operate.getOperate_Bill_Watch());
		// 值长
		tv_zhizhang_user = (TextView) operationDetails
				.findViewById(R.id.textview_zhizhang_user);
		tv_zhizhang_user.setText(getString(R.string.operationDetails_person) + Operate.getOperate_Bill_Duty());
	}
}
