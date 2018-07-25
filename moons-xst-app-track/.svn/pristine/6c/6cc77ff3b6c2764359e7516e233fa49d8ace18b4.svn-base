package com.moons.xst.track.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.OverhaulDownLoad;

public class OverhaulActivity extends BaseActivity {
	ListView listview;
	private ImageButton returnButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overhaul);

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager()
						.finishActivity(OverhaulActivity.this);
			}
		});

	}
	
	@Override
	protected void onDestroy() {		
		OverhaulDownLoad.getOverhaulDownLoad().destroy();
		super.onDestroy();
	}

	// 检修数据同步
	public void ll_overhaulUpdate(View v) {
		if (AppContext.getCommunicationType().equals(
				AppConst.CommunicationType_Wireless)) {
			if (!NetWorkHelper
					.isNetworkAvailable(OverhaulActivity.this)) {
				UIHelper.ToastMessage(
						OverhaulActivity.this,
						R.string.network_not_connected);
				return;
			}
			if (NetWorkHelper
					.checkAppRunBaseCase(OverhaulActivity.this)) {
				String DataBaseName = AppConst.XSTDBPath() + "Overhaul.sdf";
				if (!FileUtils.checkFileExists(DataBaseName)) {
					OverhaulDownLoad.getOverhaulDownLoad()
							.encodeAndDownloadOverhaul(
									OverhaulActivity.this,
									false);
				} else {
					OverhaulDownLoad.getOverhaulDownLoad().encodeAndDownloadOverhaul(
							OverhaulActivity.this, true);
				}
			}
		} else {
			UIHelper.ToastMessage(
					OverhaulActivity.this,
					R.string.download_message_notsupportusb);
			return;
		}		
	}

	// 检修计划
	public void ll_overhaulplan(View v) {
		String DataBaseName = AppConst.XSTDBPath() + "Overhaul.sdf";
		if (!FileUtils.checkFileExists(DataBaseName)) {
			showNoticeDialog();
		} else {
			UIHelper.showOtherLogin(OverhaulActivity.this,AppConst.LoginFrom.overhaul.toString());
		}
	}

	private void showNoticeDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.system_notice))
				.setView(view)
				.setMsg(getString(R.string.overhaul_operate_noplanfile_notice))
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (AppContext.getCommunicationType().equals(
										AppConst.CommunicationType_Wireless)) {
									if (!NetWorkHelper
											.isNetworkAvailable(OverhaulActivity.this)) {
										UIHelper.ToastMessage(
												OverhaulActivity.this,
												R.string.network_not_connected);
										return;
									}
									if (NetWorkHelper
											.checkAppRunBaseCase(OverhaulActivity.this)) {
										OverhaulDownLoad.getOverhaulDownLoad()
												.encodeAndDownloadOverhaul(
														OverhaulActivity.this,
														false);
									}
								} else {
									UIHelper.ToastMessage(
											OverhaulActivity.this,
											R.string.download_message_notsupportusb);
									return;
								}
							}
						}).setNegativeButton(getString(R.string.cancel), null)
				.show();
	}
}
