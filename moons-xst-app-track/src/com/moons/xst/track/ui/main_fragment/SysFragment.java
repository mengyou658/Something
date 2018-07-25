package com.moons.xst.track.ui.main_fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.ui.VibrationActivity;

public class SysFragment extends Fragment implements View.OnClickListener {

	View sysFragment;

	RelativeLayout rl_layout_sys_setting, rl_layout_sys_aboutus;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (sysFragment == null) {
			sysFragment = inflater.inflate(R.layout.fragment_sys, container,
					false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) sysFragment.getParent();
		if (parent != null) {
			parent.removeView(sysFragment);
		}
		return sysFragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void init() {
		rl_layout_sys_setting = (RelativeLayout) sysFragment
				.findViewById(R.id.rl_layout_sys_setting);
		rl_layout_sys_setting.setOnClickListener(this);
		rl_layout_sys_aboutus = (RelativeLayout) sysFragment
				.findViewById(R.id.rl_layout_sys_aboutus);
		rl_layout_sys_aboutus.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 参数设置
		case R.id.rl_layout_sys_setting:
			//LoadAppConfigHelper.LoadPWDSettingConfig();
			final String pwd = AppContext.getPwd();
			if (AppContext.getPwdYN()) {
				LayoutInflater factory = LayoutInflater
						.from(getActivity());
					final View view = factory.inflate(R.layout.editbox_layout, null);
					final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象
					new com.moons.xst.track.widget.AlertDialog(getActivity()).builder()
					.setTitle(getString(R.string.setting_sys_enterdia_pwd_inputinfo))
					.setEditView(view, InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT, "", true)
					.setPositiveButton(getString(R.string.sure), new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (edit.getText().toString()
									.equals(pwd)) {
								UIHelper.showSetting(getActivity());
							} else {
								UIHelper.ToastMessage(
										getActivity(), R.string.setting_sys_enterdia_pwd_errorinfo);
							}
						}
					}).setNegativeButton(getString(R.string.cancel), new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}).show();
				
				//0.5秒后弹出软键盘
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						InputMethodManager inputManager = (InputMethodManager) edit
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						inputManager.showSoftInput(edit, 0);
					}
				}, 500);
			} else {
				UIHelper.showSetting(getActivity());
			}
			break;
		// 关于
		case R.id.rl_layout_sys_aboutus:
			UIHelper.showAboutUS(getActivity());
			break;
		}
	}
}
