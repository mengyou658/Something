package com.moons.xst.track.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moons.xst.track.R;

/**
 * 加载对话框控件
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class LoadingDialog extends Dialog {

	private LayoutParams lp;
	private TextView loadtext;
	private LinearLayout ll_layout;

	public LoadingDialog(Context context) {
		super(context, R.style.Dialog);
		setCustomView();
	}

	public LoadingDialog(Context context, AttributeSet attrs) {
		// TODO 自动生成的构造函数存根
		super(context, R.style.Dialog);
		setCustomView();
	}

	public LoadingDialog(Context context, AttributeSet attrs, int defStyle) {
		super(context, R.style.Dialog);
		setCustomView();
	}
	
	private void setCustomView(){
		/*inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
		View layout = LayoutInflater.from(getContext()).inflate(R.layout.loadingdialog, null);
		loadtext = (TextView) layout.findViewById(R.id.loading_text);
		ll_layout=(LinearLayout) layout.findViewById(R.id.ll_layout);
		setContentView(layout);

		// 设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0; // 去背景遮盖
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);
	}

	public void setLoadText(String content) {
		loadtext.setText(content);
	}
	
	//设置隐藏layout,看起来是透明的
	public void setLoadConceal(){
		ll_layout.setVisibility(View.GONE);
	}
}