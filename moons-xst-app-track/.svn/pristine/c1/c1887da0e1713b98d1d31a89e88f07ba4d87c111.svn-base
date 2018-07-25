package com.moons.xst.track.ui;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;

/**
 * 应用程序Activity的基类
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-18
 */
public class BaseActivity extends FragmentActivity {

	// 是否允许全屏
	private boolean allowFullScreen = true;

	// 是否允许销毁
	private boolean allowDestroy = true;

	private View view;

	// 手指上下滑动时的最小速度
	protected static final int YSPEED_MIN = 1000;

	// 手指向右滑动时的最小距离
	protected static final int XDISTANCE_MIN = 150;

	// 手指向上滑或下滑时的最小距离
	protected static final int YDISTANCE_MIN = 100;

	// 记录手指按下时的横坐标。
	protected float xDown;

	// 记录手指按下时的纵坐标。
	protected float yDown;

	// 记录手指移动时的横坐标。
	protected float xMove;

	// 记录手指移动时的纵坐标。
	protected float yMove;

	// 用于计算手指滑动的速度。
	protected VelocityTracker mVelocityTracker;
	private int textSizeMode = 1;
	private static final int THEME_TEXTSIZE_SMALL = 0;
	private static final int THEME_TEXTSIZE_NORM = 1;
	private static final int THEME_TEXTSIZE_LARGE = 2;
	private static final int THEME_TEXTSIZE_HUGE = 3;

	// 检查应用是否有非法操作，true为打开false为关闭
	private static final boolean DEVELOPER_MODE = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allowFullScreen = true;
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

		if (DEVELOPER_MODE) {
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}
	}

	private void chooseTextSizeTheme() {
		switch (textSizeMode) {
		case THEME_TEXTSIZE_SMALL:
			this.setTheme(R.style.theme_textSize_small);
			break;
		case THEME_TEXTSIZE_NORM:
			this.setTheme(R.style.theme_textSize_norm);
			break;
		case THEME_TEXTSIZE_LARGE:
			this.setTheme(R.style.theme_textSize_large);
			break;
		case THEME_TEXTSIZE_HUGE:
			this.setTheme(R.style.theme_textSize_huge);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	public boolean isAllowFullScreen() {
		return allowFullScreen;
	}

	/**
	 * 设置是否可以全屏
	 * 
	 * @param allowFullScreen
	 */
	public void setAllowFullScreen(boolean allowFullScreen) {
		this.allowFullScreen = allowFullScreen;
	}

	public void setAllowDestroy(boolean allowDestroy) {
		this.allowDestroy = allowDestroy;
	}

	public void setAllowDestroy(boolean allowDestroy, View view) {
		this.allowDestroy = allowDestroy;
		this.view = view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
			view.onKeyDown(keyCode, event);
			if (!allowDestroy) {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		return super.dispatchTouchEvent(event);
	}

	/**
	 * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
	 * 
	 * @param event
	 * 
	 */
	protected void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	protected void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	protected int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getYVelocity();
		return Math.abs(velocity);
	}
}
