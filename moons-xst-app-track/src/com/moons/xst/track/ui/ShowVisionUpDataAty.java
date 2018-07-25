package com.moons.xst.track.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.Update;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.widget.LoadingDialog;

public class ShowVisionUpDataAty extends BaseActivity {
	private TextView tv_vision;
	private PackageInfo packageInfo;
	private LinearLayout ll_container;
	private ListView lv_listView;
	private ImageButton btn_back;
	private static int APPVERSIONMESSAGE = 1;// 版本更新信息
	private static int ERRORMESSG = 0;// 服务器获取数据失败
	private File file;
	private LoadingDialog loading;
	private ArrayAdapter<String> adapter;
	private String[] splits = {};
	String filePath = Environment.getExternalStorageDirectory()
			+ "/Version.xml";
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (1 == msg.what && msg.obj != null) {
				file = new File(filePath);
				if(!file.exists()){
					file=new File(filePath);
				}
				BufferedWriter bw;
				try {
					bw = new BufferedWriter(new FileWriter(file, true));
					String strContent = msg.obj.toString() + "\r\n";
					bw.write(strContent);
					bw.flush();
					bw.close();
					setData();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (0 == msg.what) {
				if (loading != null) {
					loading.dismiss();
				}
				adapter.notifyDataSetChanged();
				UIHelper.ToastMessage(ShowVisionUpDataAty.this, R.string.setting_sys_aboutus_logo_entered_data_obtainerror);
			} 

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_updata_vision);
		initView();
		getVerisonNameAndCode();
		getVerisonDataInfo();
		adapter.notifyDataSetChanged();
	}

	private void setData() {

				try {
					FileReader fr = new FileReader(
							Environment.getExternalStorageDirectory()
									+ "/Version.xml");
					BufferedReader br = new BufferedReader(fr);
					String str;
					StringBuffer sb = new StringBuffer();

					while ((str = br.readLine()) != null) {
						sb.append(str);
					}
					br.close();
					fr.close();
					
					String strVerisonInfo = sb.toString();
					splits = strVerisonInfo.split("\\|");
					if (splits != null) {
						if (loading != null) {
							loading.dismiss();
						}
					}
					adapter = new ArrayAdapter<String>(ShowVisionUpDataAty.this,
							R.layout.item_version_info, R.id.tv, splits);
					lv_listView.setAdapter(adapter);
					
					if (file.exists()) {
						file.delete();
					}
					// for(int i=0;i<split.length;i++){
					// TextView tv=new TextView(this);
					// tv.setTextColor(getResources().getColor(R.color.black));
					// tv.setText(split[i].trim().toString());
					// ll_container.addView(tv);
					// ll_container.setPadding(30, 30, 30, 20);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	private void initView() {
		tv_vision = (TextView) findViewById(R.id.tv_vision_info);
		ll_container = (LinearLayout) findViewById(R.id.ll_version_container);
		lv_listView = (ListView) findViewById(R.id.lv_version_info);
		lv_listView.setVerticalScrollBarEnabled(false);
		adapter = new ArrayAdapter<String>(this, R.layout.item_version_info,
				R.id.tv, splits);
		// if(split!=null){
		lv_listView.setAdapter(adapter);
		btn_back = (ImageButton) findViewById(R.id.home_head_Rebutton);
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goBack();
			}
		});
		loading = new LoadingDialog(this);
		loading.show();
	}

	/**
	 * 从服务器获取版本信息
	 */
	private void getVerisonDataInfo() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String appversionxml = WebserviceFactory.getAppVersionInfo(
						ShowVisionUpDataAty.this, AppContext.getModel());
				Update update;
				try {
					update = Update.parse(appversionxml);
					String updateLog = update.getUpdateLog() + "\r\n";
					handler.obtainMessage(APPVERSIONMESSAGE, updateLog)
							.sendToTarget();
				} catch (IOException e) {
					e.printStackTrace();
					handler.obtainMessage(ERRORMESSG).sendToTarget();
				} catch (AppException e) {
					e.printStackTrace();
					handler.obtainMessage(ERRORMESSG).sendToTarget();
				} catch (Exception e) {
					e.printStackTrace();
					handler.obtainMessage(ERRORMESSG).sendToTarget();
				}
			}
		}).start();
	}

	/**
	 * 获取程序的版本信息
	 */
	private void getVerisonNameAndCode() {
		PackageManager packageManager = this.getPackageManager();
		try {
			packageInfo = packageManager.getPackageInfo(this.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageInfo != null) {
			tv_vision.setText(getString(R.string.setting_sys_aboutus_versioninfo
					, packageInfo.versionName
					, String.valueOf(packageInfo.versionCode)));
		}
	}

	private void goBack() {
		// if(file.exists()){
		// file.delete();
		// }
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		goBack();
	}

}
