package com.moons.xst.track.ui.operatebill_fragment;

import java.util.List;

import com.moons.xst.buss.OperateHelper;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.OperateClauseAdapter;
import com.moons.xst.track.bean.Operate_Detail_Bill;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.ui.OperationDetailsAty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 操作项fragment
 * **/
public class OperateClauseFragment extends Fragment {

	View operateClause;
	ListView data_listview;
	OperateClauseAdapter adapter;
	private OperateHelper operateHelper;
	private List<Operate_Detail_Bill> list;
	private String Code;
	private Boolean state = true;// 当前操作项是否可进行

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (operateClause == null) {
			operateClause = inflater.inflate(R.layout.operate_clause,
					container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) operateClause.getParent();
		if (parent != null) {
			parent.removeView(operateClause);
		}
		return operateClause;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		operateHelper = new OperateHelper();
		OperationDetailsAty activity = (OperationDetailsAty) getActivity();
		Code = activity.Code;
		list = operateHelper.clauseQuery(getActivity(), Code);
		initview();
	}

	private void initview() {

		adapter = new OperateClauseAdapter(getActivity(), list,
				R.layout.operate_clause_item);
		data_listview = (ListView) operateClause
				.findViewById(R.id.data_listview);
		data_listview.setAdapter(adapter);
		data_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				for (int i = 0; i < arg2; i++) {
					String State = list.get(i).getOperate_Detail_Item_State()
							+ "";
					if (State.length() != 1) {// 如果状态不是0或1值
						UIHelper.ToastMessage(getActivity(), R.string.operationDetails_skip_hint);
						state = false;
						break;
					}
				}
				if (state) {
					/*
					 * Intent intent = new Intent(OperateClauseAty.this,
					 * ClauseRecordAty.class); intent.putExtra("Code",
					 * list.get(arg2) .getOperate_Bill_Code());
					 * intent.putExtra("Order", list.get(arg2)
					 * .getOperate_Detail_Item_Order());
					 * startActivityForResult(intent, REQCODE_OPERATE);
					 */

					UIHelper.showClauseRecord(getActivity(), list.get(arg2)
							.getOperate_Bill_Code(), list.get(arg2)
							.getOperate_Detail_Item_Order());
				}
				state = true;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		operateHelper = new OperateHelper();
		list = operateHelper.clauseQuery(getActivity(), Code);
		adapter.refresh(list);

	}
}
