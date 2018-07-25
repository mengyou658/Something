package com.moons.xst.track.ui;

import java.util.List;

import com.moons.xst.buss.WorkBillHelper;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.Work_Detail_Bill;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class WorkMeasureAty extends BaseActivity {
	List<Work_Detail_Bill> DetailList;
	int Order;// 当前第几条
	int Work_Bill_ID;// 作业票id

	TextView tv_code, plan_index, btn_goto_next, btn_goto_pre, tv_typea,
			tv_typeb, tv_contenta, tv_contentb;
	RadioButton radiowireless, radiousb;
	Button btn_save;
	ImageButton home_head_Rebutton;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_measure_particulars);
		DetailList = (List<Work_Detail_Bill>) (getIntent()
				.getSerializableExtra("WorkDetail"));
		Order = getIntent().getIntExtra("Order", 0);
		Work_Bill_ID = DetailList.get(0).getWork_Bill_ID();
		initView();
		initData();
	}

	// 初始化控件
	private void initView() {
		// 类型a
		tv_typea = (TextView) findViewById(R.id.tv_typea);
		// 类型b
		tv_typeb = (TextView) findViewById(R.id.tv_typeb);
		// 说明内容a
		tv_contenta = (TextView) findViewById(R.id.tv_contenta);
		// 说明内容b
		tv_contentb = (TextView) findViewById(R.id.tv_contentb);
		// 当前第几页
		plan_index = (TextView) findViewById(R.id.plan_index);
		// 下一页
		btn_goto_next = (TextView) findViewById(R.id.btn_goto_next);
		btn_goto_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (Order + 1 < DetailList.size()) {
					Order += 1;
					initData();
				}
			}
		});
		// 上一页
		btn_goto_pre = (TextView) findViewById(R.id.btn_goto_pre);
		btn_goto_pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Order + 1 > 1) {
					Order -= 1;
					initData();
				}
			}
		});
		// 不执行
		radiowireless = (RadioButton) findViewById(R.id.radiowireless);
		// 已执行
		radiousb = (RadioButton) findViewById(R.id.radiousb);
		// 保存
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取当前时间
				String time = DateTimeHelper.DateToString(DateTimeHelper
						.GetDateTimeNow());
				String state = "1";
				if (radiowireless.isChecked()) {// 若果不执行被选中则state保存0
					state = "0";
				}
				boolean isSucceed = WorkBillHelper.GetIntance()
						.saveMeasureDetail(WorkMeasureAty.this,
								DetailList.get(Order).getWork_Bill_Item_ID(),
								time, state);
				if (isSucceed) {
					DetailList = WorkBillHelper.GetIntance().getMeasureClause(
							WorkMeasureAty.this, Work_Bill_ID);
					// 判断措施是否全部做完
					if (isAccomplish()) {
						WorkBillHelper.GetIntance().updateWorkState(
								WorkMeasureAty.this, Work_Bill_ID);
						showAccomplishDialog();
					}
					if (Order + 1 < DetailList.size()) {
						Order += 1;
					}
					initData();

				} else {
					UIHelper.ToastMessage(WorkMeasureAty.this,
							getString(R.string.initcp_message_savedefeated));
				}
			}
		});
		// 返回
		home_head_Rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		home_head_Rebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(WorkMeasureAty.this);
			}
		});
	}

	// 加载数据
	private void initData() {
		// 如果
		Work_Detail_Bill entity = DetailList.get(Order);
		plan_index.setText(Order + 1 + "/" + DetailList.size());
		String state = entity.getWork_Bill_Item_State() + "";
		tv_typea.setText(entity.getWork_Bill_Item_ItemTitleA());
		tv_contenta.setText(entity.getWork_Bill_Item_AQCS1_TX());
		tv_typeb.setText(entity.getWork_Bill_Item_ItemTitleB());
		tv_contentb.setText(entity.getWork_Bill_Item_AQCS2_TX());
		if (StringUtils.isEmpty(entity.getWork_Bill_Item_ItemTitleB())) {
			tv_typeb.setVisibility(View.GONE);
			tv_contentb.setVisibility(View.GONE);
		} else {
			tv_typeb.setVisibility(View.VISIBLE);
			tv_contentb.setVisibility(View.VISIBLE);
		}
		if (state.equals("0")) {
			radiowireless.setChecked(true);
			clickSield();
		} else if (state.equals("1")) {
			radiousb.setChecked(true);
			clickSield();
		} else {
			radiousb.setChecked(true);
			clickUnfinished();
		}
	}

	private void clickSield() {
		btn_save.setEnabled(false);
		radiowireless.setEnabled(false);
		radiousb.setEnabled(false);
	}

	private void clickUnfinished() {
		btn_save.setEnabled(true);
		radiowireless.setEnabled(true);
		radiousb.setEnabled(true);
	}

	// 显示措施全部完成提示框
	private void showAccomplishDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.system_notice))
				.setView(view)
				.setMsg(getString(R.string.workbill_accomplish_hint))
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								AppManager.getAppManager().finishActivity(
										WorkMeasureAty.this);
								AppManager.getAppManager().finishActivity(
										WorkDetailAty.class);
							}
						}).setCanceledOnTouchOutside(false)
				.setCancelable(false).show();
	}

	// 判断措施是否做完
	private boolean isAccomplish() {
		for (int i = 0; i < DetailList.size(); i++) {
			if (StringUtils
					.isEmpty(DetailList.get(i).getWork_Bill_Item_State())) {
				return false;
			}
		}
		return true;
	}
}
