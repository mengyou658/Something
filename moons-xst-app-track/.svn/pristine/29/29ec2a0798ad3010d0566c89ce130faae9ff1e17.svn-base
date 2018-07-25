package com.moons.xst.track.ui;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.moons.xst.buss.BluetoothHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.BluetoothAdapterManager;
import com.moons.xst.track.adapter.BluetoothDeviceSearchAdapter;

import de.greenrobot.event.EventBus;

public class BluetoothDeviceForOuterAty extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	/*
	 * */
	public static final int REQUEST_ENABLE = 10000; // 打开蓝牙 请求码
	public static String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";
	private Button mSearchDeviceBtn;
	private ListView mDeviceListView;
	private BluetoothAdapterManager mAdapterManager;
	private String fromOuter;
	private BluetoothDeviceSearchAdapter mAdapter;
	private AlertDialog mAlertDialog;
	private ImageButton returnbtn;
	private StringBuilder sb = new StringBuilder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_device_search);
		EventBus.getDefault().register(this);
		AppManager.getAppManager().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		fromOuter = (String) bundle.get("From");
		mDeviceListView = (ListView) findViewById(R.id.bluetooth_device_search_listview_device);
		mAdapterManager = new BluetoothAdapterManager(this);
		mAdapter = mAdapterManager.getDeviceListAdapter();
		mDeviceListView.setAdapter(mAdapter);
		mDeviceListView.setOnItemClickListener(this);
		mSearchDeviceBtn = (Button) findViewById(R.id.btn_search);
		mSearchDeviceBtn.setOnClickListener(this);
		returnbtn = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(
						BluetoothDeviceForOuterAty.this);
			}
		});
	}

	@Override
	public void onClick(View v) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			Toast.makeText(this,
					getString(R.string.temperature_start_bluetooth_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!bluetoothAdapter.isEnabled()) {
			// 蓝牙未打开, 打开蓝牙
			LayoutInflater factory = LayoutInflater.from(BluetoothDeviceForOuterAty.this);
			final View view = factory.inflate(R.layout.textview_layout,
					null);
			new com.moons.xst.track.widget.AlertDialog(BluetoothDeviceForOuterAty.this)
					.builder()
					.setTitle(getString(R.string.system_notice))
					.setView(view)
					.setMsg(getString(R.string.msg_bluetooth_open_message))
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BluetoothHelper.getIntance(BluetoothDeviceForOuterAty.this,
											AppContext.getOuterMeasureType(), fromOuter).enableBluetooth();
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									return;
								}
							}).show();
		} else {		
			if(sb.length()>0){
				sb.setLength(0);
				mAdapterManager.clearDevice();
				mAdapterManager.updateDeviceAdapter();
			}
			BluetoothHelper.getIntance(this, AppContext.getOuterMeasureType(), fromOuter)
					.scanBluetooth();
		}
	}	

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				BluetoothDevice bd = (BluetoothDevice) msg.obj;
				if (!sb.toString().contains(bd.getAddress())) {
					sb.append(bd.getAddress());
					mAdapterManager.addDevice(bd);
					mAdapterManager.updateDeviceAdapter();
				}
			}
		};
	};
	public void onEvent(BluetoothDevice device) {
		handler.obtainMessage(1, device).sendToTarget();
	}  
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.ACTION_DOWN) {
			AppManager.getAppManager().finishActivity(
					BluetoothDeviceForOuterAty.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopScanBluetoothDevice();
		EventBus.getDefault().unregister(this);
	}
	public void stopScanBluetoothDevice(){
		BluetoothHelper.getIntance(this, AppContext.getOuterMeasureType(), fromOuter).stopScanBluetooth();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		final BluetoothDevice info = (BluetoothDevice) mAdapterManager
				.getDeviceListAdapter().getItem(position);
		if (info == null)
			return;

		// 设置返回数据
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DEVICE_ADDRESS, info.getAddress());

		// 设置返回值并结束程序
		setResult(RESULT_OK, intent);
		AppManager.getAppManager().finishActivity(BluetoothDeviceForOuterAty.this);
	}
}
