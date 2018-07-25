package com.moons.xst.track.widget;

import com.moons.xst.track.R;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;

/**
 * 弹出全透明的框 （主要用于屏蔽屏幕的所有点击事件和返回事件）
 * 
 */
public class LucencyDialog extends Dialog {
	private LayoutParams lp;

	public LucencyDialog(Context context) {
		super(context, R.style.Dialog);
		setCustomView();
	}

	public LucencyDialog(Context context, AttributeSet attrs) {
		// TODO 自动生成的构造函数存根
		super(context, R.style.Dialog);
		setCustomView();
	}

	public LucencyDialog(Context context, AttributeSet attrs, int defStyle) {
		super(context, R.style.Dialog);
		setCustomView();
	}

	private void setCustomView() {
		View layout = LayoutInflater.from(getContext()).inflate(
				R.layout.lucency_dialog_layout, null);
		setContentView(layout);

		// 设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0; // 去背景遮盖
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);
	}

}
