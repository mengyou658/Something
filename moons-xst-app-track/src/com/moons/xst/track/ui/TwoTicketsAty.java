package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.buss.WorkBillHelper;
import com.moons.xst.buss.WorkResultHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.OperateBillDownLoad;
import com.moons.xst.track.communication.OverhaulDownLoad;
import com.moons.xst.track.communication.WorkBillDownLoad;
import com.moons.xst.track.widget.ActionSheetDialog;
import com.moons.xst.track.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.moons.xst.track.widget.ActionSheetDialog.SheetItemColor;

public class TwoTicketsAty extends BaseActivity {

	ListView listview;
	private ImageButton returnButton;
	private int updateCheckItem;

	private com.moons.xst.track.widget.AlertDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.two_tickets);
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(TwoTicketsAty.this);
			}
		});

	}
	
	@Override
	protected void onDestroy() {		
		OperateBillDownLoad.getOperateBillDownLoad().destroy();
		WorkBillDownLoad.getWorkBillDownLoad(this,0).destroy();
		super.onDestroy();
	}

	// 操作票点击事件
	public void ll_operationTickets(View v) {
		String DataBaseName = AppConst.XSTDBPath() + "OperateBill.sdf";
		if (!FileUtils.checkFileExists(DataBaseName)) {
			showNoticeDialog();
		} else {
			UIHelper.showOtherLogin(TwoTicketsAty.this,
					AppConst.LoginFrom.operatebill.toString());
		}
	}

	// 工作票点击操作
	public void WorkBill(View v) {
		String DataBaseName = AppConst.XSTDBPath() + "WorkBill.sdf";
		if (!FileUtils.checkFileExists(DataBaseName)) {
			showNotWorkBilliceDialog();
		} else {
			UIHelper.showOtherLogin(TwoTicketsAty.this,
					AppConst.LoginFrom.workbill.toString());
		}
	}

	private void showNotWorkBilliceDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.system_notice))
				.setView(view)
				.setMsg(getString(R.string.twobill_work_noplanfile_notice))
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (isAllowCommunication()) {
									showSheetdialog(TwoTicketsAty.this);
								}
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
	}

	// 数据库不存在提示框
	private void showNoticeDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.system_notice))
				.setView(view)
				.setMsg(getString(R.string.twobill_operate_noplanfile_notice))
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (isAllowCommunication()) {
									try {
										OperateBillDownLoad
												.getOperateBillDownLoad()
												.uploadingData(
														TwoTicketsAty.this);
									} catch (InterruptedException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
								}
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
	}

	// 两票数据同步点击事件
	public void dataUpdate(View v) {
		if (!isAllowCommunication()) {
			return;
		}
		updateCheckItem = 0;
		final List<String> listData = new ArrayList<String>();
		listData.add(getString(R.string.twobill_operate_ticket));
		listData.add(getString(R.string.twobill_work_bill));
		LayoutInflater factory = LayoutInflater.from(TwoTicketsAty.this);
		final View view = factory.inflate(R.layout.listview_layout, null);
		dialog = new com.moons.xst.track.widget.AlertDialog(TwoTicketsAty.this)
				.builder()
				.setTitle(getString(R.string.twobill_update_hint))
				.setView(view)
				.setListViewItems(listData, 0,
						new AdapterView.OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								updateCheckItem = position;
								dialog.refresh(listData, updateCheckItem);
							}
						})
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (updateCheckItem == 0) {// 操作票数据同步
									try {
										OperateBillDownLoad
												.getOperateBillDownLoad()
												.uploadingData(
														TwoTicketsAty.this);
									} catch (InterruptedException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
								} else if (updateCheckItem == 1) {// 作业票数据同步
									showSheetdialog(TwoTicketsAty.this);
								}
							}

						}).setNegativeButton(getString(R.string.cancel), null);
		dialog.show();
	}

	// 弹出底部对话框
	private void showSheetdialog(final Context context) {
		final List<String> items = Arrays.asList(getResources().getStringArray(
				R.array.workbill_types));
		new ActionSheetDialog(context)
				.builder()
				.setTitle(getString(R.string.choice))
				.setCancelable(true)
				.setCanceledOnTouchOutside(false)
				.addSheetItems(items, SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								try {
									WorkBillDownLoad.getWorkBillDownLoad(
											context, which).uploadingData();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).show();
	}

	// 判断是否可以通信
	public boolean isAllowCommunication() {
		if (AppContext.getCommunicationType().equals(
				AppConst.CommunicationType_Wireless)) {
			if (!NetWorkHelper.isNetworkAvailable(TwoTicketsAty.this)) {
				UIHelper.ToastMessage(TwoTicketsAty.this,
						R.string.network_not_connected);
				return false;
			}
			if (NetWorkHelper.checkAppRunBaseCase(TwoTicketsAty.this)) {
				return true;
			} else {
				return false;
			}
		} else {
			UIHelper.ToastMessage(TwoTicketsAty.this,
					R.string.download_message_notsupportusb);
			return false;
		}
	}
}