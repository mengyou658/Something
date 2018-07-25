/**
 * 
 */
package com.moons.xst.track.common;

/**
 * 输入法
 * 
 * @author LKZ
 *
 */
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputTools {

	/**
	 * 隐藏虚拟键盘
	 * 
	 * @param v
	 */
	public static void HideKeyboard(EditText et) {
		InputMethodManager imm = (InputMethodManager) et.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(et.getApplicationWindowToken(), 0);

		}
	}

	/**
	 * 显示虚拟键盘
	 * 
	 * @param v
	 */
	public static void ShowKeyboard(EditText et) {
		InputMethodManager imm = (InputMethodManager) et.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

	}

	/**
	 * 强制显示或者关闭系统键盘
	 * 
	 * @param txtSearchKey
	 * @param status
	 */
	public static void KeyBoard(final EditText txtSearchKey, final String status) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager m = (InputMethodManager) txtSearchKey
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				if (status.equals("open")) {
					m.showSoftInput(txtSearchKey,
							InputMethodManager.SHOW_FORCED);
				} else {
					m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
				}
			}
		}, 300);
	}

	/**
	 * 通过定时器强制隐藏虚拟键盘
	 * 
	 * @param v
	 */
	public static void TimerHideKeyboard(final View v) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
				}
			}
		}, 10);
	}

	/**
	 * 输入法是否显示着
	 * 
	 * @param edittext
	 * @return
	 */
	public static boolean isKeyBoardShow(EditText edittext) {
		boolean bool = false;
		InputMethodManager imm = (InputMethodManager) edittext.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			bool = true;
		}
		return bool;

	}
}
