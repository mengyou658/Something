package com.moons.xst.track.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.buss.OperateHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.OperateAdapter;
import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.Operate_Bill;
import com.moons.xst.track.bean.Operate_Detail_Bill;
import com.moons.xst.track.common.UIHelper;

import de.greenrobot.event.EventBus;

public class OperationBillAty extends BaseActivity {
	private final static int REQCODE_RFID = 100;
	ListView data_listview;
	TextView search_content;
	PendingIntent mPendingIntent;
	private OperateAdapter operateAdapter;
	private ImageButton returnButton;
	private OperateHelper operateHelper;
	List<BillUsers> user;
	private List<Operate_Bill> listOperate = new ArrayList<Operate_Bill>();
	List<Operate_Detail_Bill> listDetail = new ArrayList<Operate_Detail_Bill>();
	private Operate_Bill Operate_entity;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operation_bill);
		EventBus.getDefault().register(this); 
		
		if (savedInstanceState != null) {
			// 这个是之前保存的数据
			user = (List<BillUsers>)(savedInstanceState
					.getSerializable("OperateBillUser"));
		} else {
			// 这个是从另外一个界面进入这个时传入的
			user = (List<BillUsers>) (getIntent().getSerializableExtra("OperateBillUser"));
		}
		
		operateHelper = new OperateHelper();
		listOperate = operateHelper.operateQuery(this,user);
		listDetail = operateHelper.DetailQuery(this);
		data_listview = (ListView) findViewById(R.id.data_listview);
		if (listOperate != null) {
			operateAdapter = new OperateAdapter(OperationBillAty.this,
					listOperate, R.layout.operate_item, listDetail);
			data_listview.setAdapter(operateAdapter);
		}
		data_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				commonalitySkip(listOperate.get(arg2));
			}
		});

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager()
						.finishActivity(OperationBillAty.this);
			}
		});
		// 搜索栏
		search_content = (TextView) findViewById(R.id.search_content);
		search_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				// 封装好的搜索bean
				CommonSearchBean<Operate_Bill> commonSearchBean = 
						new CommonSearchBean<Operate_Bill>();
				commonSearchBean.setList(listOperate);
				commonSearchBean.setStandbyList(listDetail);
				commonSearchBean.setSearchType(AppConst.SearchType.Operation.toString());
				commonSearchBean
						.setHint(getString(R.string.operationbill_search));
				Intent intent = new Intent(OperationBillAty.this,
						UnifySearchAty.class);
				intent.putExtra("seachBean", (Serializable) commonSearchBean);
				startActivity(intent);
			}
		});
	}

	// 搜索回调事件
	public void onEvent(Operate_Bill entity) {
		commonalitySkip(entity);
	}

	private void commonalitySkip(Operate_Bill entity) {
		String id_cd = entity.getID_CD() + "";
		if (id_cd.equals("null")) {
			UIHelper.showOperationDetails(OperationBillAty.this,
					entity.getOperate_Bill_Code());
		} else {
			Operate_entity = entity;
			Intent rfidintent = new Intent(OperationBillAty.this,
					RFIDLogin.class);
			startActivityForResult(rfidintent, REQCODE_RFID);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("OperateBillUser", (Serializable)user);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		operateHelper = new OperateHelper();
		listDetail = operateHelper.DetailQuery(this);
		operateAdapter.refreshOperation(listDetail);
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQCODE_RFID) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String rfidStr = bundle.getString("RFIDString");
				if (Operate_entity.getID_CD().equals(rfidStr)) {
					UIHelper.showOperationDetails(OperationBillAty.this,
							Operate_entity.getOperate_Bill_Code());
				} else {
					UIHelper.ToastMessage(this,
							getString(R.string.operate_none_msg));
				}
			}
		}
	}
}