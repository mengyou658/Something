package com.moons.xst.track.adapter;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import com.moons.xst.track.R;

/**
 * 适配器管理器
 * @author 210001001427
 *
 */
public class BluetoothAdapterManager {
	private Context mContext;
	private BluetoothDeviceSearchAdapter mDeviceListAdapter;   //设备列表 adapter
	private List<BluetoothDevice> mDeviceList;   //设备集合
	private Handler mainHandler;   //主线程Handler
	
	public BluetoothAdapterManager(Context context){
		this.mContext = context;
	}
	
	/**
	 * 取得设备列表adapter
	 * @return
	 */
	public BluetoothDeviceSearchAdapter getDeviceListAdapter(){
		if(null == mDeviceListAdapter){
			mDeviceList = new ArrayList<BluetoothDevice>();
			mDeviceListAdapter = new BluetoothDeviceSearchAdapter(mContext, mDeviceList, R.layout.listitem_bluetooth_device);
		}
		
		return mDeviceListAdapter;
	}
	
	/**
	 * 更新设备列表
	 */
	public void updateDeviceAdapter(){
		if(null == mainHandler){
			mainHandler = new Handler(mContext.getMainLooper());
		}
		mainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mDeviceListAdapter.notifyDataSetChanged();
			}
		});
	}
	
	/**
	 * 清空设备列表
	 */
	public void clearDevice(){
		if(null != mDeviceList){
			mDeviceList.clear();
		}
	}
	
	/**
	 * 添加设备
	 * @param bluetoothDevice
	 */
	public void addDevice(BluetoothDevice bluetoothDevice){
		mDeviceList.add(bluetoothDevice);
	}
	
	/**
	 * 更新设备信息
	 * @param listId
	 * @param bluetoothDevice
	 */
	public void changeDevice(int listId, BluetoothDevice bluetoothDevice){
		mDeviceList.remove(listId);
		mDeviceList.add(listId, bluetoothDevice);
	}
	
	/**
	 * 取得设备列表
	 * @return
	 */
	public List<BluetoothDevice> getDeviceList() {
		return mDeviceList;
	}
}
