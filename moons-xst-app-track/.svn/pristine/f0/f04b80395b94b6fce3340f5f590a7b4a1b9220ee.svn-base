package com.moons.xst.track.ui;

import java.io.Serializable;
import java.util.List;

import com.moons.xst.buss.WorkBillHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.WorkBillAdapter;
import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.bean.Work_Bill;
import com.moons.xst.track.common.UIHelper;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WorkBillActivity extends BaseActivity {

	List<BillUsers> user;

	TextView tv_title;// 标题
	TextView search_content;
	ListView listview;
	WorkBillAdapter adapter;
	List<Work_Bill> mList;
	private ImageButton returnButton;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operation_bill);
		EventBus.getDefault().register(this);
		user = (List<BillUsers>) (getIntent()
				.getSerializableExtra("WorkBillUser"));
		initView();
	}

	private void initView() {
		// 标题
		tv_title = (TextView) findViewById(R.id.oeration_bill_head_title);
		tv_title.setText(getString(R.string.twobill_work_bill));

		listview = (ListView) findViewById(R.id.data_listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				UIHelper.showWorkDetail(WorkBillActivity.this,
						mList.get(position));
			}
		});

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager()
						.finishActivity(WorkBillActivity.this);
			}
		});

		// 搜索栏
		search_content = (TextView) findViewById(R.id.search_content);
		search_content.setHint(getString(R.string.workbill_search_hint));
		search_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				CommonSearchBean<Work_Bill> commonSearchBean = new CommonSearchBean<Work_Bill>();
				commonSearchBean.setList(mList);
				commonSearchBean.setSearchType(AppConst.SearchType.WorkBill
						.toString());
				commonSearchBean
						.setHint(getString(R.string.workbill_search_hint));
				Intent intent = new Intent(WorkBillActivity.this,
						UnifySearchAty.class);
				intent.putExtra("seachBean", (Serializable) commonSearchBean);
				startActivity(intent);
			}
		});
	}

	// 搜索回调事件
	public void onEvent(Work_Bill entity) {
		UIHelper.showWorkDetail(this, entity);
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		mList = WorkBillHelper.GetIntance().getWorkBill(this, user);
		adapter = new WorkBillAdapter(this, mList, R.layout.operate_item);
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
