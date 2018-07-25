package com.moons.xst.track.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MainBottomBar extends LinearLayout implements OnClickListener {

	private CallBack mCallBack;
	public int prevIndex;

	public MainBottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		initAction();
	}

	/**
	 * 对每个子控件进行点击事件设置
	 */
	private void initAction() {
		for (int i = 0, len = getChildCount(); i < len; i++) {
			getChildAt(i).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		View childView = null;
		if (!v.isSelected()) {
			for (int i = 0, len = getChildCount(); i < len; i++) {
				childView = getChildAt(i);
				if (v == childView) { // 当前view和点击的view相同时设置为选中，并回调mCallBack。
					childView.setSelected(true);
					if (mCallBack != null) {
						mCallBack.call(prevIndex, i);
					}
					prevIndex = i;
				} else {
					childView.setSelected(false);
				}
			}
		}
	}

	/**
	 * 给外部调用，设置点击哪个button
	 * 
	 * @param index
	 */
	public void setSelected(int index) {
		if (index < getChildCount()) {
			getChildAt(index).performClick(); // 主动出发点击事件
			prevIndex = index;
		}
	}

	public void setCallBack(CallBack mCallBack) {
		this.mCallBack = mCallBack;
	}

	/**
	 * prevIndex：当前fragment下标 currentIndex：要切换到的fragment的下标
	 * 
	 * 
	 */
	public interface CallBack {
		public void call(int prevIndex, int currentIndex);
	}

}
