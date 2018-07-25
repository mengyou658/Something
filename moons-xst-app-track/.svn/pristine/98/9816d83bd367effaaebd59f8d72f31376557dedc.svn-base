package com.moons.xst.track.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.UIHelper;

public class SystemManager extends BaseActivity implements View.OnClickListener{

	ImageButton returnButton;
	RelativeLayout rl_Service;
	RelativeLayout rl_FileInfo;
	RelativeLayout rl_BugInfo;
	RelativeLayout rl_Reduction;
	RelativeLayout rl_settingjurisdiction;

	AppContext ac;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_manager);

		ac = (AppContext) (this.getApplication());
		
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(this);
		rl_Service = (RelativeLayout) findViewById(R.id.ll_system_manager_service);
		rl_Service.setOnClickListener(this);
		rl_FileInfo = (RelativeLayout) findViewById(R.id.ll_system_manager_fileinfo);
		rl_FileInfo.setOnClickListener(this);
		rl_BugInfo = (RelativeLayout) findViewById(R.id.ll_system_manager_buginfo);
		rl_BugInfo.setOnClickListener(this);
		rl_Reduction = (RelativeLayout) findViewById(R.id.ll_system_manager_reduction);
		rl_Reduction.setOnClickListener(this);
		rl_settingjurisdiction = (RelativeLayout) findViewById(R.id.ll_system_manager_settingjurisdiction);
		rl_settingjurisdiction.setOnClickListener(this);
	}
	
	public void returnBtnOnClick() {
		AppManager.getAppManager().finishActivity(SystemManager.this);
	}

	public void serviceOnClick() {
		UIHelper.showSystemService(SystemManager.this);
	}

	public void fileInfoOnClick() {
		UIHelper.showSystemFileInfo(SystemManager.this);
	}

	public void buginfoOnClick() {
		UIHelper.showSystemBugInfo(SystemManager.this);
	}

	public void reductionOnClick() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(
						getString(R.string.setting_sys_aboutus_logo_enterdia_pwd_inputinfo))
				.setView(view)
				.setMsg(getString(R.string.setting_sys_aboutus_logo_enterdia_msginfo))
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {

								try {
									File file = new File(AppConst.XSTBasePath());
									if (file.exists()) {
										FileUtils.delete(file);
									}
									ac.reductionAllConfig();

								} catch (Exception e) {
									e.printStackTrace();
									return;
								}

								AppManager.getAppManager().finishAllActivity();
								Intent i = getBaseContext().getPackageManager()
										.getLaunchIntentForPackage(
												getBaseContext()
														.getPackageName());
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);

							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
	}
	
	public void showSettingAuth() {
		if (AppContext.getCommunicationType().equals(
				AppConst.CommunicationType_Wireless)) {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
				UIHelper.ToastMessage(SystemManager.this,
						R.string.network_not_connected);
				return;
			}
			if (NetWorkHelper.checkAppRunBaseCase(getBaseContext()))
				UIHelper.showSystemSettingAuth(SystemManager.this);
		} else {
			UIHelper.ToastMessage(SystemManager.this, R.string.download_message_notsupportusb);
			return;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (v.getId() == R.id.home_head_Rebutton) {
			returnBtnOnClick();
		} else if (v.getId() == R.id.ll_system_manager_service) {
			serviceOnClick();
		} else if (v.getId() == R.id.ll_system_manager_fileinfo) {
			fileInfoOnClick();
		} else if (v.getId() == R.id.ll_system_manager_buginfo) {
			buginfoOnClick();
		} else if (v.getId() == R.id.ll_system_manager_reduction) {
			reductionOnClick();
		} else if (v.getId() == R.id.ll_system_manager_settingjurisdiction) {
			showSettingAuth();
		}
	}
}