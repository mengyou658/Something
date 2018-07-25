/**
 * 
 */
package com.moons.xst.track.pad_ui.activity;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.pad_adapter.Pad_SecondMenuApter;
import com.moons.xst.track.bean.SecondMenuInfo;
import com.moons.xst.track.pad_ui.fragement.Pad_HomeMainFragment;
import com.moons.xst.track.pad_ui.fragement.Pad_HomeSysFragment;
import com.moons.xst.track.pad_ui.fragement.Pad_JXGL_Fragment;
import com.moons.xst.track.pad_ui.fragement.Pad_LPGL_Fragment;
import com.moons.xst.track.pad_ui.fragement.Pad_QXGL_Fragment;
import com.moons.xst.track.pad_ui.fragement.Pad_SBDA_Fragment;
import com.moons.xst.track.pad_ui.fragement.Pad_WDZL_Fragment;
import com.moons.xst.track.pad_ui.fragement.Pad_XJGL_Fragment;
import com.moons.xst.track.pad_ui.fragement.Pad_YXGL_Fragment;
import com.moons.xst.track.pad_ui.fragement.Pad_ZTGL_Fragment;
import com.moons.xst.track.ui.BaseActivity;
import com.moons.xst.track.ui.DoubleClickExitHelper;
import com.moons.xst.track.widget.GestureListener;
import com.moons.xst.track.xstinterface.Pad_MenuChangeListener;

/**
 * PAD主界面
 * 
 * @author LKZ
 * 
 */
public class Pad_Home extends BaseActivity implements OnClickListener {
	private DoubleClickExitHelper mDoubleClickExitHelper;

	private RelativeLayout homeTitleLayout, homeMenuLayout;
	private FrameLayout mainFrameLayout;
	private ImageView appLogo;

	private Pad_HomeMainFragment HomeMainFragment;
	private Pad_WDZL_Fragment wdgl_Fragment;
	private Pad_SBDA_Fragment sbda_Fragment;
	private Pad_XJGL_Fragment xjgl_Fragment;
	private Pad_YXGL_Fragment yxgl_Fragment;
	private Pad_ZTGL_Fragment ztgl_Fragment;
	private Pad_QXGL_Fragment qxgl_Fragment;
	private Pad_JXGL_Fragment jxgl_Fragment;
	private Pad_LPGL_Fragment lpgl_Fragment;
	private Pad_HomeSysFragment xtsz_Fragment;
	private View mainLayout;
	private View xtsz_Layout;
	private View wdgl_Layout;
	private View sbda_Layout;
	private View xjgl_Layout;
	private View yxgl_Layout;
	private View ztgl_Layout;
	private View qxgl_Layout;
	private View jxgl_Layout;
	private View lpgl_Layout;

	private ImageView mainImage;
	private ImageView xtsz_Image;
	private ImageView wdgl_Image;
	private ImageView sbda_Image;
	private ImageView xjgl_Image;
	private ImageView yxgl_Image;
	private ImageView ztgl_Image;
	private ImageView qxgl_Image;
	private ImageView jxgl_Image;
	private ImageView lpgl_Image;

	private TextView mainText;
	private TextView xtsz_Text;
	private TextView wdgl_Text;
	private TextView sbda_Text;
	private TextView xjgl_Text;
	private TextView yxgl_Text;
	private TextView ztgl_Text;
	private TextView qxgl_Text;
	private TextView jxgl_Text;
	private TextView lpgl_Text;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	// 考核点弹出--popupWindow
	private PopupWindow menuPW;
	private LayoutInflater lm_menuInflater;
	private View lm_menuLayout;
	private ListView lm_menuListView;
	private Pad_SecondMenuApter menuApter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		setContentView(R.layout.activity_pad_home);
		// 程序两次点击退出事件
		mDoubleClickExitHelper = new DoubleClickExitHelper(this);
		// 初始化布局元素
		initViews();
		fragmentManager = getFragmentManager();
		// 第一次启动时选中第0个tab
		setTabSelection(0);
		initMenu();
		loadMenuData();
		
	}

	/**
	 * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
	 */
	private void initViews() {
		homeTitleLayout = (RelativeLayout) findViewById(R.id.layout_home_title_part);
		homeTitleLayout.setTag("0");
		homeTitleLayout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				fullscreenExchange(homeTitleLayout.getTag().equals("0"));
				return true;
			}
		});
		homeTitleLayout.setOnTouchListener(new MyGestureListener(this));
		homeMenuLayout = (RelativeLayout) findViewById(R.id.layout_memu_bg);
		homeMenuLayout.setTag("0");
		mainFrameLayout = (FrameLayout) findViewById(R.id.content);
		mainFrameLayout.setLongClickable(true);
		mainFrameLayout.setOnTouchListener(new MyGestureListener(this));
		appLogo = (ImageView) findViewById(R.id.pad_home_logo);
		appLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				homeMenuHideShow();
			}
		});

		mainLayout = findViewById(R.id.layout_home);
		mainLayout.setOnClickListener(this);
		mainLayout.setTag("home");
		wdgl_Layout = findViewById(R.id.layout_wdzl);
		wdgl_Layout.setOnClickListener(this);
		wdgl_Layout.setTag("wdzl");
		sbda_Layout = findViewById(R.id.layout_sbda);
		sbda_Layout.setOnClickListener(this);
		sbda_Layout.setTag("sbda");
		xjgl_Layout = findViewById(R.id.layout_xjgl);
		xjgl_Layout.setOnClickListener(this);
		xjgl_Layout.setTag("xjgl");
		yxgl_Layout = findViewById(R.id.layout_yxgl);
		yxgl_Layout.setOnClickListener(this);
		yxgl_Layout.setTag("yxgl");
		ztgl_Layout = findViewById(R.id.layout_ztjc);
		ztgl_Layout.setOnClickListener(this);
		ztgl_Layout.setTag("ztjc");
		qxgl_Layout = findViewById(R.id.layout_qxgl);
		qxgl_Layout.setOnClickListener(this);
		qxgl_Layout.setTag("qxgl");
		jxgl_Layout = findViewById(R.id.layout_jxcl);
		jxgl_Layout.setOnClickListener(this);
		jxgl_Layout.setTag("jxcl");
		lpgl_Layout = findViewById(R.id.layout_lpgl);
		lpgl_Layout.setOnClickListener(this);
		lpgl_Layout.setTag("lpgl");
		xtsz_Layout = findViewById(R.id.layout_xtsz);
		xtsz_Layout.setOnClickListener(this);
		xtsz_Layout.setTag("xtsz");

		mainImage = (ImageView) findViewById(R.id.pad_home_main_imagebtn);
		wdgl_Image = (ImageView) findViewById(R.id.pad_home_wdzl_imagebtn);
		sbda_Image = (ImageView) findViewById(R.id.pad_home_sbda_imagebtn);
		xjgl_Image = (ImageView) findViewById(R.id.pad_home_xjgl_imagebtn);
		yxgl_Image = (ImageView) findViewById(R.id.pad_home_yxgl_imagebtn);
		ztgl_Image = (ImageView) findViewById(R.id.pad_home_ztjc_imagebtn);
		qxgl_Image = (ImageView) findViewById(R.id.pad_home_qxgl_imagebtn);
		jxgl_Image = (ImageView) findViewById(R.id.pad_home_jxcl_imagebtn);
		lpgl_Image = (ImageView) findViewById(R.id.pad_home_lpgl_imagebtn);
		xtsz_Image = (ImageView) findViewById(R.id.pad_home_xtsz_imagebtn);

		mainText = (TextView) findViewById(R.id.pad_home_main_txt);
		wdgl_Text = (TextView) findViewById(R.id.pad_home_wdzl_txt);
		sbda_Text = (TextView) findViewById(R.id.pad_home_sbda_txt);
		xjgl_Text = (TextView) findViewById(R.id.pad_home_xjgl_txt);
		yxgl_Text = (TextView) findViewById(R.id.pad_home_yxgl_txt);
		ztgl_Text = (TextView) findViewById(R.id.pad_home_ztjc_txt);
		qxgl_Text = (TextView) findViewById(R.id.pad_home_qxgl_txt);
		jxgl_Text = (TextView) findViewById(R.id.pad_home_jxcl_txt);
		lpgl_Text = (TextView) findViewById(R.id.pad_home_lpgl_txt);
		xtsz_Text = (TextView) findViewById(R.id.pad_home_xtsz_txt);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 获取
			// back键
			// 是否退出应用
			boolean bet = mDoubleClickExitHelper.onKeyDown(keyCode, event);
			return bet;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_home:
			setTabSelection(0);
			break;
		case R.id.layout_wdzl:
			setTabSelection(1);
			break;
		case R.id.layout_sbda:
			setTabSelection(2);
			break;
		case R.id.layout_xjgl:
			setTabSelection(3);
			break;
		case R.id.layout_yxgl:
			setTabSelection(4);
			break;
		case R.id.layout_ztjc:
			setTabSelection(5);
			break;
		case R.id.layout_qxgl:
			setTabSelection(6);
			break;
		case R.id.layout_jxcl:
			setTabSelection(7);
			break;
		case R.id.layout_lpgl:
			setTabSelection(8);
			break;
		case R.id.layout_xtsz:
			setTabSelection(9);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 */
	private void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:// 我的主页
			mainLayout.setBackgroundResource(R.drawable.pad_item_press);
			mainImage.setImageResource(R.drawable.pad_tab_home_pressed);
			mainText.setTextColor(Color.WHITE);
			if (HomeMainFragment == null) {
				HomeMainFragment = new Pad_HomeMainFragment();
				transaction.add(R.id.content, HomeMainFragment);
				HomeMainFragment
						.setMenuChangeListener(new Pad_MenuChangeListener() {

							@Override
							public void redirectToSBDA(String caseString) {
								// TODO 自动生成的方法存根
								setTabSelection(2);
								Intent intent = new Intent(Pad_Home.this,
										Pad_SBDA_Activity.class);
								startActivity(intent);
							}
						});
			} else {
				transaction.show(HomeMainFragment);
			}
			break;
		case 1:// 文档资料
			wdgl_Layout.setBackgroundResource(R.drawable.pad_item_press);
			wdgl_Image.setImageResource(R.drawable.pad_tab_wdzl_pressed);
			wdgl_Text.setTextColor(Color.WHITE);
			if (wdgl_Fragment == null) {
				wdgl_Fragment = new Pad_WDZL_Fragment();
				transaction.add(R.id.content, wdgl_Fragment);
			} else {
				transaction.show(wdgl_Fragment);
			}
			break;
		case 2:// 设备档案
			sbda_Layout.setBackgroundResource(R.drawable.pad_item_press);
			sbda_Image.setImageResource(R.drawable.pad_tab_sbda_pressed);
			sbda_Text.setTextColor(Color.WHITE);
			if (sbda_Fragment == null) {
				sbda_Fragment = new Pad_SBDA_Fragment();
				transaction.add(R.id.content, sbda_Fragment);
			} else {
				transaction.show(sbda_Fragment);
			}
			break;
		case 3:// 巡检管理
			xjgl_Layout.setBackgroundResource(R.drawable.pad_item_press);
			xjgl_Image.setImageResource(R.drawable.pad_tab_xjgl_pressed);
			xjgl_Text.setTextColor(Color.WHITE);
			if (xjgl_Fragment == null) {
				xjgl_Fragment = new Pad_XJGL_Fragment();
				transaction.add(R.id.content, xjgl_Fragment);
			} else {
				transaction.show(xjgl_Fragment);
			}
			break;
		case 4:// 运行管理
			yxgl_Layout.setBackgroundResource(R.drawable.pad_item_press);
			yxgl_Image.setImageResource(R.drawable.pad_tab_yxgl_pressed);
			yxgl_Text.setTextColor(Color.WHITE);
			if (yxgl_Fragment == null) {
				yxgl_Fragment = new Pad_YXGL_Fragment();
				transaction.add(R.id.content, yxgl_Fragment);
			} else {
				transaction.show(yxgl_Fragment);
			}
			break;
		case 5:// 状态监测
			ztgl_Layout.setBackgroundResource(R.drawable.pad_item_press);
			ztgl_Image.setImageResource(R.drawable.pad_tab_ztjc_pressed);
			ztgl_Text.setTextColor(Color.WHITE);
			if (ztgl_Fragment == null) {
				ztgl_Fragment = new Pad_ZTGL_Fragment();
				transaction.add(R.id.content, ztgl_Fragment);
			} else {
				transaction.show(ztgl_Fragment);
			}
			break;
		case 6:// 缺陷处理
			qxgl_Layout.setBackgroundResource(R.drawable.pad_item_press);
			qxgl_Image.setImageResource(R.drawable.pad_tab_qxgl_pressed);
			qxgl_Text.setTextColor(Color.WHITE);
			if (qxgl_Fragment == null) {
				qxgl_Fragment = new Pad_QXGL_Fragment();
				transaction.add(R.id.content, qxgl_Fragment);
			} else {
				transaction.show(qxgl_Fragment);
			}
			break;
		case 7:// 检修处理
			jxgl_Layout.setBackgroundResource(R.drawable.pad_item_press);
			jxgl_Image.setImageResource(R.drawable.pad_tab_jxcl_pressed);
			jxgl_Text.setTextColor(Color.WHITE);
			if (jxgl_Fragment == null) {
				jxgl_Fragment = new Pad_JXGL_Fragment();
				transaction.add(R.id.content, jxgl_Fragment);
			} else {
				transaction.show(jxgl_Fragment);
			}
			break;
		case 8:// 两票管理
			lpgl_Layout.setBackgroundResource(R.drawable.pad_item_press);
			lpgl_Image.setImageResource(R.drawable.pad_tab_lpgl_pressed);
			lpgl_Text.setTextColor(Color.WHITE);
			if (lpgl_Fragment == null) {
				lpgl_Fragment = new Pad_LPGL_Fragment();
				transaction.add(R.id.content, lpgl_Fragment);
			} else {
				transaction.show(lpgl_Fragment);
			}
			break;
		case 9:// 系统设置
			xtsz_Layout.setBackgroundResource(R.drawable.pad_item_press);
			xtsz_Image.setImageResource(R.drawable.pad_tab_sys_pressed);
			xtsz_Text.setTextColor(Color.WHITE);
			if (xtsz_Fragment == null) {
				xtsz_Fragment = new Pad_HomeSysFragment();
				transaction.add(R.id.content, xtsz_Fragment);
			} else {
				transaction.show(xtsz_Fragment);
			}
			break;
		default:
			mainLayout.setBackgroundResource(R.drawable.pad_item_press);
			mainImage.setImageResource(R.drawable.pad_tab_home_pressed);
			mainText.setTextColor(Color.WHITE);
			if (HomeMainFragment == null) {
				HomeMainFragment = new Pad_HomeMainFragment();
				transaction.add(R.id.content, HomeMainFragment);
			} else {
				transaction.show(HomeMainFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		mainLayout.setBackgroundResource(R.drawable.pad_item_unpress);
		wdgl_Layout.setBackgroundResource(R.drawable.pad_item_unpress);
		sbda_Layout.setBackgroundResource(R.drawable.pad_item_unpress);
		xjgl_Layout.setBackgroundResource(R.drawable.pad_item_unpress);
		yxgl_Layout.setBackgroundResource(R.drawable.pad_item_unpress);
		ztgl_Layout.setBackgroundResource(R.drawable.pad_item_unpress);
		qxgl_Layout.setBackgroundResource(R.drawable.pad_item_unpress);
		jxgl_Layout.setBackgroundResource(R.drawable.pad_item_unpress);
		lpgl_Layout.setBackgroundResource(R.drawable.pad_item_unpress);
		xtsz_Layout.setBackgroundResource(R.drawable.pad_item_unpress);

		mainImage.setImageResource(R.drawable.pad_tab_home_normal);
		wdgl_Image.setImageResource(R.drawable.pad_tab_wdzl_normal);
		sbda_Image.setImageResource(R.drawable.pad_tab_sbda_normal);
		xjgl_Image.setImageResource(R.drawable.pad_tab_xjgl_normal);
		yxgl_Image.setImageResource(R.drawable.pad_tab_yxgl_normal);
		ztgl_Image.setImageResource(R.drawable.pad_tab_ztjc_normal);
		qxgl_Image.setImageResource(R.drawable.pad_tab_qxgl_normal);
		jxgl_Image.setImageResource(R.drawable.pad_tab_jxcl_normal);
		lpgl_Image.setImageResource(R.drawable.pad_tab_lpgl_normal);
		xtsz_Image.setImageResource(R.drawable.pad_tab_sys_normal);

		mainText.setTextColor(Color.parseColor("#82858b"));
		wdgl_Text.setTextColor(Color.parseColor("#82858b"));
		sbda_Text.setTextColor(Color.parseColor("#82858b"));
		xjgl_Text.setTextColor(Color.parseColor("#82858b"));
		yxgl_Text.setTextColor(Color.parseColor("#82858b"));
		ztgl_Text.setTextColor(Color.parseColor("#82858b"));
		qxgl_Text.setTextColor(Color.parseColor("#82858b"));
		jxgl_Text.setTextColor(Color.parseColor("#82858b"));
		lpgl_Text.setTextColor(Color.parseColor("#82858b"));
		xtsz_Text.setTextColor(Color.parseColor("#82858b"));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (HomeMainFragment != null) {
			transaction.hide(HomeMainFragment);
		}
		if (wdgl_Fragment != null) {
			transaction.hide(wdgl_Fragment);
		}
		if (sbda_Fragment != null) {
			transaction.hide(sbda_Fragment);
		}
		if (xjgl_Fragment != null) {
			transaction.hide(xjgl_Fragment);
		}
		if (yxgl_Fragment != null) {
			transaction.hide(yxgl_Fragment);
		}
		if (ztgl_Fragment != null) {
			transaction.hide(ztgl_Fragment);
		}
		if (qxgl_Fragment != null) {
			transaction.hide(qxgl_Fragment);
		}
		if (jxgl_Fragment != null) {
			transaction.hide(jxgl_Fragment);
		}
		if (lpgl_Fragment != null) {
			transaction.hide(lpgl_Fragment);
		}
		if (xtsz_Fragment != null) {
			transaction.hide(xtsz_Fragment);
		}
	}

	private void homeMenuHideShow() {
		if (homeMenuLayout.getVisibility() == View.GONE)
			homeMenuLayout.setVisibility(View.VISIBLE);
		else {
			homeMenuLayout.setVisibility(View.GONE);
		}
	}

	private void fullscreenExchange(boolean isFull) {
		if (isFull) {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(params);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			homeTitleLayout.setTag("1");
		} else {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(params);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			homeTitleLayout.setTag("0");
		}
	}

	private class MyGestureListener extends GestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO 自动生成的方法存根
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO 自动生成的方法存根
			fullscreenExchange(homeTitleLayout.getTag().equals("0"));
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO 自动生成的方法存根
			return super.onDoubleTapEvent(e);
		}

		public MyGestureListener(Context context) {
			super(context);
		}

		@Override
		public boolean left() {
			homeMenuLayout.setVisibility(View.GONE);
			return super.left();
		}

		@Override
		public boolean right() {
			homeMenuLayout.setVisibility(View.VISIBLE);
			return super.right();
		}

	}

	/**
	 * 获取菜单
	 */
	private void loadMenuData() {
		ArrayList<SecondMenuInfo> _secondMenuList = new ArrayList<SecondMenuInfo>();
		SecondMenuInfo _secondMenuInfo = new SecondMenuInfo();
		_secondMenuInfo.setMenuItemID(0);
		_secondMenuInfo.setMenuItemDesc("操作票");
		_secondMenuInfo.setChildrenCount(0);
		_secondMenuList.add(_secondMenuInfo);
		_secondMenuInfo = new SecondMenuInfo();
		_secondMenuInfo.setMenuItemID(1);
		_secondMenuInfo.setMenuItemDesc("工作票");
		_secondMenuInfo.setChildrenCount(0);
		_secondMenuList.add(_secondMenuInfo);
		_secondMenuInfo = new SecondMenuInfo();
		AppContext.allSecondMenuBuffer.put("lpgl", _secondMenuList);
	}

	private void initMenu() {
		// 获取LayoutInflater实例
		lm_menuInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 获取弹出菜单的布局
		lm_menuLayout = lm_menuInflater.inflate(R.layout.pad_memu, null);
	}

	private void showMenu(View v) {
		ArrayList<SecondMenuInfo> menusInfo = AppContext.allSecondMenuBuffer
				.get(v.getTag());
		if (menusInfo == null || menusInfo.size() <= 0)
			return;
		menuApter = new Pad_SecondMenuApter(this, menusInfo,
				R.layout.pad_listitem_second_menu);
		lm_menuListView.setAdapter(menuApter);
		View parentView = findViewById(R.id.content);
		menuPW.showAsDropDown(parentView, 0, 0); // 设置在屏幕中的显示位置
	}

}
