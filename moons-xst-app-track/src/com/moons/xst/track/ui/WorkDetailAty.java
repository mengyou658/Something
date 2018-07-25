package com.moons.xst.track.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.moons.xst.buss.WorkBillHelper;
import com.moons.xst.buss.WorkResultHelper;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.WorkClaustAdapter;
import com.moons.xst.track.bean.Work_Bill;
import com.moons.xst.track.bean.Work_Detail_Bill;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.MyViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class WorkDetailAty extends BaseActivity {

	Work_Bill WorkBill;
	int workID;// 工作票id

	// 加载动画
	LoadingDialog loading;

	View workView, measureView;
	ImageButton home_head_Rebutton;
	MyViewPager viewpager;
	RadioGroup rg_tabPage_state;
	RadioButton rb_tabPage1, rb_tabPage2;
	TextView tv_tab1;

	// 上次移动的x坐标
	private int fromX = 0;

	// 工作票viewpager控件
	TextView tv_contents, tv_starttime, tv_endtime, tv_user, tv_jh_user,
			tv_worktype, tv_workcode;
	ListView data_listview;
	ImageView btn_signature, workdetail_iv_workbilltype;
	List<Work_Detail_Bill> WorkDetailList;// 隔离措施list
	LinearLayout rl_layout_others;
	WorkClaustAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_bill_detail_layout);
		WorkBill = (Work_Bill) getIntent().getSerializableExtra("WorkBill");
		workID = WorkBill.getWork_Bill_ID();
		initView();
		initViewPage();
	}

	private void initView() {
		rg_tabPage_state = (RadioGroup) findViewById(R.id.rg_tabPage_state);
		tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
		viewpager = (MyViewPager) findViewById(R.id.viewpager);

		// tab点击事件
		rb_tabPage1 = (RadioButton) findViewById(R.id.rb_tabPage1);
		rb_tabPage2 = (RadioButton) findViewById(R.id.rb_tabPage2);
		rb_tabPage1.setOnClickListener(new MyOnClickListener(0));
		rb_tabPage2.setOnClickListener(new MyOnClickListener(1));

		LayoutInflater mLi = LayoutInflater.from(this);
		workView = mLi.inflate(R.layout.work_details_viewpager, null);
		measureView = mLi.inflate(R.layout.work_measure_clause, null);
		final ArrayList<View> views = new ArrayList<View>();
		views.add(workView);
		views.add(measureView);

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((MyViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((MyViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		// ViewPager页面切换动画
		viewpager.setPageTransformer(false, new MyViewPager.PageTransformer() {
			@Override
			public void transformPage(View page, float position) {
				final float normalizedposition = Math.abs(Math.abs(position) - 1);
				page.setAlpha(normalizedposition);
			}
		});
		viewpager.setAdapter(mPagerAdapter);
		viewpager.setCurrentItem(0);

		// 返回
		home_head_Rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		home_head_Rebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(WorkDetailAty.this);
			}
		});
	}

	private void initViewPage() {
		// 操作任务
		tv_contents = (TextView) workView
				.findViewById(R.id.text_operation_contents);
		tv_contents.setText(WorkBill.getWork_Bill_TaskContent_TX());
		tv_contents.setMovementMethod(ScrollingMovementMethod.getInstance());
		// 开始时间
		tv_starttime = (TextView) workView
				.findViewById(R.id.textview_starttime);
		tv_starttime.setText(WorkBill.getWork_Begin_Time());
		// 结束时间
		tv_endtime = (TextView) workView.findViewById(R.id.textview_endtime);
		tv_endtime.setText(WorkBill.getWork_End_Time());
		// 工作人LinearLayout
		rl_layout_others = (LinearLayout) workView
				.findViewById(R.id.rl_layout_others);
		if (WorkBill.getJD_ID() == 2) {// 如果负责人已签名完则显示操作人
			rl_layout_others.setVisibility(View.VISIBLE);
		} else {
			rl_layout_others.setVisibility(View.GONE);
		}
		// 许可人
		tv_user = (TextView) workView
				.findViewById(R.id.textview_operation_user);
		tv_user.setText(getString(R.string.workbill_licensor)
				+ isEmpty(WorkBill.getWork_Bill_Operator()));
		// 负责人
		tv_jh_user = (TextView) workView.findViewById(R.id.textview_jh_user);
		tv_jh_user.setText(getString(R.string.workbill_principal)
				+ isEmpty(WorkBill.getWork_Bill_Guardian()));

		// 票类型图标
		workdetail_iv_workbilltype = (ImageView) workView
				.findViewById(R.id.workdetail_iv_workbilltype);
		// 工作票类型
		tv_worktype = (TextView) workView.findViewById(R.id.tv_worktype);
		String Type = WorkBill.getWork_Bill_Type_TX();
		if (Type.equals("DQYZ")) {// 电气一种
			tv_worktype.setText(getString(R.string.workbill_type_dqyz));
			workdetail_iv_workbilltype
					.setImageResource(R.drawable.ic_workbill_dq_xh);
		} else if (Type.equals("DQEZ")) {// 电气二种
			tv_worktype.setText(getString(R.string.workbill_type_dqez));
			workdetail_iv_workbilltype
					.setImageResource(R.drawable.ic_workbill_dq_xh);
		} else if (Type.equals("RLJX")) {// 热力机械
			tv_worktype.setText(getString(R.string.workbill_type_rljx));
			workdetail_iv_workbilltype
					.setImageResource(R.drawable.ic_workbill_jx_xh);
		}

		// 工作票code
		tv_workcode = (TextView) workView.findViewById(R.id.tv_workcode);
		tv_workcode.setText(WorkBill.getWork_Bill_Code());
		// 签名
		btn_signature = (ImageView) workView
				.findViewById(R.id.workdetail_ib_sign);
		// 状态为1则显示许可人签名
		if (WorkBill.getJD_ID() == 1) {
			btn_signature.setVisibility(View.VISIBLE);
		} else {
			btn_signature.setVisibility(View.GONE);
		}
		btn_signature.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btn_signature.getTag().equals("1")) {
					showSignatureDialog(getString(R.string.workbill_licensor_signature));
				} else {
					showSignatureDialog(getString(R.string.workbill_principal_signature));
				}
			}
		});

		// 加载隔离措施viewpager
		data_listview = (ListView) measureView.findViewById(R.id.data_listview);
		data_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				UIHelper.showMeasureAty(WorkDetailAty.this, WorkDetailList,
						position);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		WorkDetailList = WorkBillHelper.GetIntance().getMeasureClause(this,
				workID);
		adapter = new WorkClaustAdapter(this, WorkDetailList,
				R.layout.work_measure_clause_item);
		data_listview.setAdapter(adapter);
	}

	// 提示错误框
	private void showErrorDialog(String error) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this).builder()
				.setTitle(getString(R.string.tips)).setView(view).setMsg(error)
				.setPositiveButton(getString(R.string.sure), null)
				.setCancelable(false).setCanceledOnTouchOutside(false).show();
	}

	// 弹出确定框
	private void showconfirmDialog(final String name) {
		WorkBill = WorkBillHelper.GetIntance().getSingleWork(this, workID);
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.tips))
				.setView(view)
				.setMsg(getString(R.string.workbill_licensor)
						+ OperatorName + "\n\n"
						+ getString(R.string.workbill_principal) + name)
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (loading == null || !loading.isShowing()) {
									loading = new LoadingDialog(
											WorkDetailAty.this);
									loading.setLoadText(getString(R.string.save_ing));
									loading.setCanceledOnTouchOutside(false);
									loading.setCancelable(false);
									loading.show();
								}
								new Thread() {
									public void run() {
										String src;
										// 保存签名人信息
										src = WorkBillHelper.GetIntance()
												.savePrincipal(
														WorkDetailAty.this,
														workID, name,
														OperatorName);
										if (src.equals("true")) {
											// 保存数据到结果库
											src = WorkResultHelper.GetIntance()
													.saveWorkResult(
															WorkDetailAty.this,
															workID);
										}
										Message msg = new Message();
										if (src.equals("true")) {
											msg.what = 0;
											handler.sendMessage(msg);
										} else {
											msg.what = 1;
											msg.obj = src;
											handler.sendMessage(msg);
										}
									}
								}.start();
							}
						}).setCancelable(false)
				.setCanceledOnTouchOutside(false).show();

	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 0) {
				Toast.makeText(WorkDetailAty.this,
						getString(R.string.plan_save_ok), Toast.LENGTH_SHORT).show();
				AppManager.getAppManager().finishActivity(WorkDetailAty.this);
			} else if (msg.what == 1) {
				showErrorDialog((msg.obj).toString());
			}
		}
	};

	String OperatorName = "";

	// 弹出签字框
	private void showSignatureDialog(String hint) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.work_signature_layout, null);
		final EditText editText = (EditText) view.findViewById(R.id.editText);
		editText.setHint(hint);
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) });
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String editable = editText.getText().toString();
				String str = StringUtils.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					editText.setText(str);
					// 设置新的光标所在位置
					editText.setSelection(str.length());
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		final RadioButton radiopass = (RadioButton) view
				.findViewById(R.id.radiopass);// 通过
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.tips))
				.setView(view)
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								String name = editText.getText().toString();
								if (name.length() <= 0) {
									UIHelper.ToastMessage(
											WorkDetailAty.this,
											getString(R.string.workbill_precedence_signature));
									return;
								}
								if (radiopass.isChecked()) {// 通过
									if (btn_signature.getTag().equals("1")) {
										btn_signature.setTag("2");
										btn_signature
												.setImageResource(R.drawable.widget_bar_workbill_sign_fzr_xh);
										OperatorName = editText.getText()
												.toString();
									} else if (btn_signature.getTag().equals(
											"2")) {
										showconfirmDialog(name);
									}
								} else {// 不通过
									WorkBillHelper.GetIntance()
											.updateWorkRestore(
													WorkDetailAty.this, workID);
									AppManager.getAppManager().finishActivity(
											WorkDetailAty.this);
								}
							}
						}).setNegativeButton(getString(R.string.cancel), null)
				.setCanceledOnTouchOutside(false).show();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) editText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}
		}, 500);
	}

	/**
	 * ViewPager图标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewpager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {

			switch (arg0) {
			case 0:
				rb_tabPage1.setEnabled(false);
				rb_tabPage2.setEnabled(true);
				break;
			case 1:
				rb_tabPage1.setEnabled(true);
				rb_tabPage2.setEnabled(false);
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (arg2 == 0) {
				return;
			}
			tvMoveTo(arg0, arg1);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	// tab下面那条线的移动动画
	private void tvMoveTo(int index, float f) {
		RadioButton button = (RadioButton) rg_tabPage_state.getChildAt(index);
		int location[] = new int[2];
		button.getLocationInWindow(location);
		TranslateAnimation animation = new TranslateAnimation(fromX,
				location[0] + f * button.getWidth(), 0, 0);
		animation.setDuration(50);
		animation.setFillAfter(true);
		fromX = (int) (location[0] + f * button.getWidth());
		// 开启动画
		tv_tab1.startAnimation(animation);

	}

	private String isEmpty(String value) {
		return StringUtils.isEmpty(value) ? "" : value;
	}
}
