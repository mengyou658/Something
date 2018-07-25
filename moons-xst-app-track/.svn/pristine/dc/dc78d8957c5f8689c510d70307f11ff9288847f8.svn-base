package com.moons.xst.track.ui;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.PowerSaveManager;


/**
 * 双击退出
 * 创建日期 2014-09-12
 * @author gaojun
 * 
 */
public class DoubleClickExitHelper {

	private final Activity mActivity;
	
	private boolean isOnKeyBacking;
	private Handler mHandler;
	private Toast mBackToast;
	
	public DoubleClickExitHelper(Activity activity) {
		mActivity = activity;
		mHandler = new Handler(Looper.getMainLooper());
	}
	
	/**
	 * Activity onKeyDown事件
	 * */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode != KeyEvent.KEYCODE_BACK) {
			return false;
		}
		if(isOnKeyBacking) {
			mHandler.removeCallbacks(onBackTimeRunnable);
			if(mBackToast != null){
				mBackToast.cancel();
			}
			/* 根据节电设置，关闭相应的模块 */
			//PowerSaveManager.getPowerSaveManager(mActivity).close();
			
			return false;
		} else {
			isOnKeyBacking = true;
			if(mBackToast == null) {
				mBackToast = Toast.makeText(mActivity, R.string.back_exit_tips, 2000);
			}
			mBackToast.show();
			mHandler.postDelayed(onBackTimeRunnable, 2000);
			return true;
		}
	}
	
	private Runnable onBackTimeRunnable = new Runnable() {
		
		@Override
		public void run() {
			isOnKeyBacking = false;
			if(mBackToast != null){
				mBackToast.cancel();
			}
		}
	};
}
