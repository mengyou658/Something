package com.moons.xst.track.ui;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.DateTimeHelper;

public class FontSizeSettingAty extends BaseActivity implements OnClickListener {
	private SeekBar seekBar;
	private TextView listitemTitle;
	private TextView listitemBuildTime;
	private TextView txtContent;
	private int newProgress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fontsize_setting);
		initView();
		if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Normal.toString())) {
			seekBar.setProgress(0);
			listitemTitle.setTextAppearance(this, R.style.widget_listview_title2);
			listitemBuildTime.setTextAppearance(this,  R.style.widget_listview_subdesc);
			txtContent.setTextAppearance(this, R.style.widget_textview_22_normal);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Big.toString())) {
			seekBar.setProgress(50);
			listitemTitle.setTextAppearance(this, R.style.widget_listview_title2_big);
			listitemBuildTime.setTextAppearance(this,  R.style.widget_listview_subdesc_big);
			txtContent.setTextAppearance(this, R.style.widget_textview_22_big);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Huge.toString())) {
			seekBar.setProgress(100);
			listitemTitle.setTextAppearance(this, R.style.widget_listview_title2_super);
			listitemBuildTime.setTextAppearance(this,  R.style.widget_listview_subdesc_super);
			txtContent.setTextAppearance(this, R.style.widget_textview_22_huge);
		};
	}

	private void initView() {
		ImageButton returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(this);
		TextView fontsizeNormal = (TextView) findViewById(R.id.tv_textsize_normal);
		fontsizeNormal.setOnClickListener(this);
		TextView fontsizeBig = (TextView) findViewById(R.id.tv_textsize_big);
		fontsizeBig.setOnClickListener(this);
		TextView fontsizeHuge = (TextView) findViewById(R.id.tv_textsize_huge);
		fontsizeHuge.setOnClickListener(this);
		listitemTitle = (TextView) findViewById(R.id.fontsize_listitem_title);
		listitemBuildTime = (TextView) findViewById(R.id.fontsize_listitem_BuildTime);
		listitemBuildTime.setText(DateTimeHelper.getDateTimeNow());
		txtContent = (TextView) findViewById(R.id.fontsize_txt_content);

		seekBar = (SeekBar) findViewById(R.id.textsize_seekBar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (newProgress < 30) {
					newProgress = 0;
					AppContext.setFontSize("normal");
				} else if (newProgress >= 70) {
					newProgress = 100;
					AppContext.setFontSize("huge");
				} else {
					newProgress = 50;
					AppContext.setFontSize("big");
				}
				seekBar.setProgress(newProgress);
				textChange();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				newProgress = progress;
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_textsize_normal:
			seekBar.setProgress(0);
			newProgress = 0;
			break;
		case R.id.tv_textsize_big:
			seekBar.setProgress(50);
			newProgress = 50;
			break;
		case R.id.tv_textsize_huge:
			seekBar.setProgress(100);
			newProgress = 100;
			break;
		case R.id.home_head_Rebutton:
			goBack();
			break;
		default:
			break;
		}
		textChange();
	}

	private void textChange() {
		if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Normal.toString())) {
			listitemTitle.setTextAppearance(this, R.style.widget_listview_title2);
			listitemBuildTime.setTextAppearance(this,  R.style.widget_listview_subdesc);
			txtContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Big.toString())) {
			listitemTitle.setTextAppearance(this, R.style.widget_listview_title2_big);
			listitemBuildTime.setTextAppearance(this,  R.style.widget_listview_subdesc_big);
			txtContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Huge.toString())) {
			listitemTitle.setTextAppearance(this, R.style.widget_listview_title2_super);
			listitemBuildTime.setTextAppearance(this,  R.style.widget_listview_subdesc_super);
			txtContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
		};
		
	}

	private void goBack() {
		((AppContext) this.getApplication()).setConfigFontSize(AppContext.getFontSize());
		AppManager.getAppManager().finishActivity(FontSizeSettingAty.this);		
	}

}
