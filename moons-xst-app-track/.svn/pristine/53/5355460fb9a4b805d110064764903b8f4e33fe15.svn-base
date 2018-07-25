/**
 * 
 */
package com.moons.xst.track.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.moons.xst.track.R;

/**
 * @author LKZ
 * 
 */
public class SimpleTextDialog extends MoonsBaseDialog {

	private Context mcontext;
	private TextView titleTextView, textView;
	private Button OkButton, CancelButton;
	private String mtitleMes, mtextMes;

	View.OnClickListener okOnClickListener, cancelClickListener;

	public SimpleTextDialog(Context context) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCancelable(false);
		setContentView(R.layout.simple_text_dialog);
		mcontext = context;
		initView();

	}

	public SimpleTextDialog(Context context, String titleMes, String textMes) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCancelable(false);
		setContentView(R.layout.simple_text_dialog);
		mcontext = context;
		mtitleMes = titleMes;
		mtextMes = textMes;
		initView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {
		titleTextView = (TextView) findViewById(R.id.simple_text_dialog_title);
		titleTextView.setText(mtitleMes);
		textView = (TextView) findViewById(R.id.simple_dialog_txt);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		textView.setText(mtextMes);
		OkButton = (Button) findViewById(R.id.btn_simple_text_dialog_ok);
		OkButton.setVisibility(View.GONE);
		CancelButton = (Button) findViewById(R.id.btn_simple_text_dialog_cancel);
		CancelButton.setVisibility(View.GONE);

	}

	/**
	 * 设置文本颜色
	 * 
	 * @param colorStateList
	 */
	public void setTextColor(ColorStateList colorStateList) {
		textView.setTextColor(colorStateList);
	}

	/**
	 * 设置文本大小
	 * 
	 * @param size
	 */
	public void setTextSize(float size) {
		titleTextView.setTextSize(size);
		textView.setTextSize(size);
	}

	public void setOKButton(int sure, View.OnClickListener onClickListener) {
		// TODO 自动生成的方法存根
		okOnClickListener = onClickListener;
		OkButton.setVisibility(View.VISIBLE);
		OkButton.setOnClickListener(okOnClickListener);
	}

	public void setCancelButton(int cancel, View.OnClickListener onClickListener) {
		// TODO 自动生成的方法存根
		cancelClickListener = onClickListener;
		CancelButton.setVisibility(View.VISIBLE);
		CancelButton.setOnClickListener(cancelClickListener);
	}
}
