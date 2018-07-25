package com.moons.xst.track.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.OverhaulHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.FragmentViewPagerAdapter;
import com.moons.xst.track.bean.OverhaulProject;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.ui.overhaul_fragment.FragmentQC;
import com.moons.xst.track.ui.overhaul_fragment.FragmentQC.CallBack;

public class OverhaulProjectDetailAty extends FragmentActivity implements
		OnClickListener {
	private TextView tv_projectCoding;
	private TextView tv_projectName;
	private TextView tv_projectContent;
	private TextView tv_tab1, tv_tab2, tv_tab3;
	private OverhaulProject overhaulProject;
	private ImageButton returnButton;
	private int jxType_ID;
	private Button btn_overhaulSave;
	private OverhaulHelper overhaulHelper;
	private ViewPager vp_overhaulExamine;
	RadioGroup rg_tabPage_state;
	RadioButton rb_tabPage1, rb_tabPage2, rb_tabPage3;
	// 上次移动的x坐标
	private int fromX = 0;
	private ArrayList<Fragment> views;
	private FragmentViewPagerAdapter adapter;
	private FragmentQC fragment1;
	private FragmentQC fragment2;
	private FragmentQC fragment3;
	private RelativeLayout rl_projectContent;
	private static final int RESULTCODE = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overhaul_project_detail);
		Bundle bundle = this.getIntent().getExtras();
		overhaulProject = (OverhaulProject) bundle.get("overhaulProject");
		jxType_ID = overhaulProject.getJXType_ID();
		overhaulHelper = OverhaulHelper.getInstance();
		views = new ArrayList<Fragment>();
		init();
		showData();
	}

	private void init() {
		tv_projectCoding = (TextView) findViewById(R.id.tv_project_coding);
		tv_projectName = (TextView) findViewById(R.id.tv_project_name);
		tv_projectContent = (TextView) findViewById(R.id.tv_project_content);
		tv_projectContent.setText(overhaulProject.getWorkOrderContent_TX());
		rl_projectContent = (RelativeLayout) findViewById(R.id.rl_project_content);
		rl_projectContent.setOnClickListener(this);
		returnButton = (ImageButton) findViewById(R.id.overhaul_project_head_Rebutton);
		returnButton.setOnClickListener(this);
		vp_overhaulExamine = (ViewPager) findViewById(R.id.vp_overhaul_examine);
		tv_projectCoding.setText(overhaulProject.getWorkOrder_CD());
		tv_projectName.setText(overhaulProject.getWorkOrderName_TX());
		btn_overhaulSave = (Button) findViewById(R.id.btn_overhaul_save);
		btn_overhaulSave.setOnClickListener(this);
		if ("1".equals(overhaulProject.getQC1())) {
			btn_overhaulSave.setClickable(false);
		} else {
			btn_overhaulSave.setClickable(true);
		}

		rg_tabPage_state = (RadioGroup) findViewById(R.id.rg_examine_tabPage_state);
		rb_tabPage1 = (RadioButton) findViewById(R.id.rb_examine_tabPage1);
		rb_tabPage2 = (RadioButton) findViewById(R.id.rb_examine_tabPage2);
		rb_tabPage3 = (RadioButton) findViewById(R.id.rb_examine_tabPage3);
		rb_tabPage1.setOnClickListener(new MyOnClickListener(0));
		rb_tabPage2.setOnClickListener(new MyOnClickListener(1));
		rb_tabPage3.setOnClickListener(new MyOnClickListener(2));
		tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
		tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
		tv_tab3 = (TextView) findViewById(R.id.tv_tab3);
	}
	//动态加载fragment
	private void showData() {
		switch (jxType_ID) {
		case 1:
			rb_tabPage1.setVisibility(View.VISIBLE);
			rb_tabPage2.setVisibility(View.INVISIBLE);
			rb_tabPage3.setVisibility(View.INVISIBLE);
			tv_tab1.setVisibility(View.VISIBLE);
			tv_tab2.setVisibility(View.INVISIBLE);
			tv_tab3.setVisibility(View.INVISIBLE);
			fragment1 = new FragmentQC(overhaulProject, 1);
			views.add(fragment1);
			if ("1".equals(overhaulProject.getFinish_YN())) {
				btn_overhaulSave.setClickable(false);
			}
			break;
		case 2:
			if (StringUtils.isEmpty(overhaulProject.getQC1())) {
				rb_tabPage1.setVisibility(View.VISIBLE);
				rb_tabPage2.setVisibility(View.INVISIBLE);
				rb_tabPage3.setVisibility(View.INVISIBLE);
				tv_tab1.setVisibility(View.VISIBLE);
				tv_tab2.setVisibility(View.INVISIBLE);
				tv_tab3.setVisibility(View.INVISIBLE);
				fragment1 = new FragmentQC(overhaulProject, 1);
				views.add(fragment1);
			} else {
				rb_tabPage1.setVisibility(View.VISIBLE);
				rb_tabPage2.setVisibility(View.VISIBLE);
				rb_tabPage3.setVisibility(View.GONE);
				tv_tab1.setVisibility(View.VISIBLE);
				tv_tab2.setVisibility(View.INVISIBLE);
				tv_tab3.setVisibility(View.GONE);
				fragment1 = new FragmentQC(overhaulProject, 1);
				fragment2 = new FragmentQC(overhaulProject, 2);
				views.add(fragment1);
				views.add(fragment2);
			}
			break;
		case 3:
			if (StringUtils.isEmpty(overhaulProject.getQC1())) {
				rb_tabPage1.setVisibility(View.VISIBLE);
				rb_tabPage2.setVisibility(View.INVISIBLE);
				rb_tabPage3.setVisibility(View.INVISIBLE);
				tv_tab1.setVisibility(View.VISIBLE);
				tv_tab2.setVisibility(View.INVISIBLE);
				tv_tab3.setVisibility(View.INVISIBLE);
				fragment1 = new FragmentQC(overhaulProject, 1);
				views.add(fragment1);
			} else {
				if (StringUtils.isEmpty(overhaulProject.getQC2())) {
					rb_tabPage1.setVisibility(View.VISIBLE);
					rb_tabPage2.setVisibility(View.VISIBLE);
					rb_tabPage3.setVisibility(View.GONE);
					tv_tab1.setVisibility(View.VISIBLE);
					tv_tab2.setVisibility(View.INVISIBLE);
					tv_tab3.setVisibility(View.GONE);
					fragment1 = new FragmentQC(overhaulProject, 1);
					fragment2 = new FragmentQC(overhaulProject, 2);
					views.add(fragment1);
					views.add(fragment2);
				} else {
					rb_tabPage1.setVisibility(View.VISIBLE);
					rb_tabPage2.setVisibility(View.VISIBLE);
					rb_tabPage3.setVisibility(View.VISIBLE);
					tv_tab1.setVisibility(View.VISIBLE);
					tv_tab2.setVisibility(View.INVISIBLE);
					tv_tab3.setVisibility(View.INVISIBLE);
					fragment1 = new FragmentQC(overhaulProject, 1);
					fragment2 = new FragmentQC(overhaulProject, 2);
					fragment3 = new FragmentQC(overhaulProject, 3);
					views.add(fragment1);
					views.add(fragment2);
					views.add(fragment3);
				}
			}
			break;
		default:
			break;
		}
		adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),
				vp_overhaulExamine, views);
		adapter.notifyDataSetChanged();
		vp_overhaulExamine
				.setOnPageChangeListener(new MyOnPageChangeListener());
		// ViewPager页面切换动画
		vp_overhaulExamine.setPageTransformer(false,
				new ViewPager.PageTransformer() {
					@Override
					public void transformPage(View page, float position) {
						page.setRotationY(position * -30);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.overhaul_project_head_Rebutton:
			AppManager.getAppManager().finishActivity(
					OverhaulProjectDetailAty.this);
			break;
		case R.id.rl_project_content:
			if (StringUtils.isEmpty(overhaulProject.getWorkOrderContent_TX())) {
				return;
			}
			LayoutInflater inflater = LayoutInflater
					.from(OverhaulProjectDetailAty.this);
			final View view = inflater.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(
					OverhaulProjectDetailAty.this)
					.builder()
					.setTitle(
							getString(R.string.overhaul_project_content_diashow))
					.setView(view)
					.setMsg(overhaulProject.getWorkOrderContent_TX().toString())
					.setPositiveButton(getString(R.string.sure), null).show();
			break;

		case R.id.btn_overhaul_save:// 保存按钮
			switch (overhaulProject.getJXType_ID()) {
			case 1:// 一审完成，保存名称 ，备注信息,是否完成，编号信息
				if ("0".equals(overhaulProject.getFinish_YN())) {
					getInfoFromFragment(fragment1, 1);
					if (!StringUtils.isEmpty(overhaulProject.getQC1_TX())) {
						showMyDialog(
								String.valueOf(1),
								String.valueOf(1),
								AppConst.CurrentQC.QC1.toString(),
								overhaulProject,
								getString(R.string.overhaul_project_examine_two_unFinished));
					} else {
						isEmptShow();
						return;
					}
				} else {
					return;
				}
				break;
			case 2:
				// 二审完成保存
				if ("0".equals(overhaulProject.getFinish_YN())) {
					// 二级审核的一审保存
					if (StringUtils.isEmpty(overhaulProject.getQC1())) {
						getInfoFromFragment(fragment1, 1);
						if (!StringUtils.isEmpty(overhaulProject.getQC1_TX())) {
							showMyDialog(
									String.valueOf(1),
									String.valueOf(0),
									AppConst.CurrentQC.QC1.toString(),
									overhaulProject,
									getString(R.string.overhaul_project_examine_two_unFinished));
						} else {
							isEmptShow();
							return;
						}
					} else {
						// 二级审核的一审完成，二审的保存
						if (StringUtils.isEmpty(overhaulProject.getQC2())) {
							getInfoFromFragment(fragment2, 2);
							if (!StringUtils.isEmpty(overhaulProject
									.getQC2_TX())) {
								showMyDialog(
										String.valueOf(1),
										String.valueOf(1),
										AppConst.CurrentQC.QC2.toString(),
										overhaulProject,
										getString(R.string.overhaul_project_examine_isFinished));
							} else {
								isEmptShow();
								return;
							}
						} else {
							return;
						}
					}
				}
				break;
			case 3:
				// 三级审完成保存
				if ("0".equals(overhaulProject.getFinish_YN())) {
					// 三级审核的一审保存
					if (StringUtils.isEmpty(overhaulProject.getQC1())) {
						getInfoFromFragment(fragment1, 1);
						if (!StringUtils.isEmpty(overhaulProject.getQC1_TX())) {
							showMyDialog(
									String.valueOf(1),
									String.valueOf(0),
									AppConst.CurrentQC.QC1.toString(),
									overhaulProject,
									getString(R.string.overhaul_project_examine_two_unFinished));
						} else {
							isEmptShow();
							return;
						}
					} else if (StringUtils.isEmpty(overhaulProject.getQC2())) {
						getInfoFromFragment(fragment2, 2);
						// 三级审核的一审完成，二审的保存
						if (!StringUtils.isEmpty(overhaulProject.getQC2_TX())) {
							showMyDialog(
									String.valueOf(1),
									String.valueOf(0),
									AppConst.CurrentQC.QC2.toString(),
									overhaulProject,
									getString(R.string.overhaul_project_examine_three_unFinished));
						} else {
							isEmptShow();
							return;
						}
					} else {
						// 三级审核的一审完成，二审完成，三审的保存
						if (StringUtils.isEmpty(overhaulProject.getQC3())) {
							getInfoFromFragment(fragment3, 3);
							if (!StringUtils.isEmpty(overhaulProject
									.getQC3_TX())) {
								showMyDialog(
										String.valueOf(1),
										String.valueOf(1),
										AppConst.CurrentQC.QC3.toString(),
										overhaulProject,
										getString(R.string.overhaul_project_examine_isFinished));
							} else {
								isEmptShow();
								return;
							}
						} else {
							return;
						}
					}
				}
				break;
			default:
				break;
			}
		default:
			break;
		}

	}

	private void isEmptShow() {
		Toast.makeText(this,
				getString(R.string.overhaul_project_tips_signname_empt),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * 从fragment上获取信息
	 * 
	 * @param fragmentQC
	 * @param num
	 */
	public void getInfoFromFragment(Fragment fragmentQC, final Integer num) {
		((FragmentQC) fragmentQC).getSignName(new CallBack() {

			@Override
			public void getResult(String signName) {
				if (!StringUtils.isEmpty(signName)) {
					String dateTimeNow = DateTimeHelper.getDateTimeNow();
					if (num == 1)
						overhaulProject.setQC1_TX(signName + "   "
								+ dateTimeNow);
					else if (num == 2)
						overhaulProject.setQC2_TX(signName + "   "
								+ dateTimeNow);
					else if (num == 3)
						overhaulProject.setQC3_TX(signName + "   "
								+ dateTimeNow);
				}
			}

		});
		((FragmentQC) fragmentQC).getremarkTX(new CallBack() {

			@Override
			public void getResult(String remarkTX) {
				if (!StringUtils.isEmpty(remarkTX)) {
					if (num == 1)
						overhaulProject.setQC1_MemoTX(remarkTX);
					else if (num == 2)
						overhaulProject.setQC2_MemoTX(remarkTX);
					else if (num == 3)
						overhaulProject.setQC3_MemoTX(remarkTX);
				}
			}
		});
	}

	/**
	 * 按钮保存相关逻辑
	 * 
	 * @param qcNO
	 *            当前QC的值
	 * @param finishYN
	 *            项目是否完成
	 * @param currentQC
	 *            当前的QC的状态
	 * @param overhaulProject
	 * @param tips
	 *            完成提示
	 */
	public void showMyDialog(final String qcNO, final String finishYN,
			final String currentQC, final OverhaulProject overhaulProject,
			final String tips) {
		LayoutInflater factory = LayoutInflater
				.from(OverhaulProjectDetailAty.this);
		final View view1 = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(
				OverhaulProjectDetailAty.this)
				.builder()
				.setTitle(getString(R.string.tips))
				.setView(view1)
				.setMsg(getString(R.string.overhaul_btn_saved_diatitle))
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								if ("QC1".equals(currentQC))
									overhaulProject.setQC1(qcNO);
								else if ("QC2".equals(currentQC))
									overhaulProject.setQC2(qcNO);
								else if ("QC3".equals(currentQC))
									overhaulProject.setQC3(qcNO);
								overhaulProject.setFinish_YN(finishYN);
								overhaulProject.setCurrentQC(currentQC);
								overhaulHelper.upDateExamine(
										OverhaulProjectDetailAty.this,
										overhaulProject);
								Toast.makeText(
										OverhaulProjectDetailAty.this,
										getString(R.string.overhaul_project_save_ok),
										Toast.LENGTH_SHORT).show();
								overhaulProject.setState(tips);
								goProjectAty();
								AppManager.getAppManager().finishActivity(
										OverhaulProjectDetailAty.this);
							}
						}).setNegativeButton(getString(R.string.cancel), null)
				.show();
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
			vp_overhaulExamine.setCurrentItem(index);
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
				rb_tabPage3.setEnabled(true);
				setClickEnable(overhaulProject.getQC1());
				break;
			case 1:
				rb_tabPage1.setEnabled(true);
				rb_tabPage2.setEnabled(false);
				rb_tabPage3.setEnabled(true);
				setClickEnable(overhaulProject.getQC2());
				break;
			case 2:
				rb_tabPage1.setEnabled(true);
				rb_tabPage2.setEnabled(true);
				rb_tabPage3.setEnabled(false);
				setClickEnable(overhaulProject.getQC3());
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

	private void setClickEnable(String currentPapge) {
		if ("1".equals(String.valueOf(currentPapge))) {
			btn_overhaulSave.setClickable(false);
		} else {
			btn_overhaulSave.setClickable(true);
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

	public void goProjectAty() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("detailProject", overhaulProject);
		intent.putExtras(bundle);
		OverhaulProjectDetailAty.this.setResult(RESULTCODE, intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppManager.getAppManager().finishActivity(
					OverhaulProjectDetailAty.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
