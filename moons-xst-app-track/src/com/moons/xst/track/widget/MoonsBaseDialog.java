/**
 * 
 */
package com.moons.xst.track.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Moons风格的Dialog基类
 * 
 * @author lkz
 * 
 */
public class MoonsBaseDialog extends Dialog {

	private LayoutParams lp;
	public MoonsBaseDialog(Context context) {
		super(context);		
		// 设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0; // 去背景遮盖
		lp.alpha = 1.0f;	
		lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(lp);
	}
}
