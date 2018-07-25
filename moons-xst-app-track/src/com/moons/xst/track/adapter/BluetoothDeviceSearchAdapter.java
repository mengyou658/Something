package com.moons.xst.track.adapter;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;


public class BluetoothDeviceSearchAdapter extends BaseAdapter {
	private List<BluetoothDevice> mDeviceList;
	private LayoutInflater mLayoutInflater;
	private int mLayoutId;
	
	private String pairInfo;
	private Context mcontext;
	
	public BluetoothDeviceSearchAdapter(Context context, List<BluetoothDevice> deviceList, int layoutId){
		this.mDeviceList = deviceList;
		mcontext = context;
		this.mLayoutInflater = LayoutInflater.from(context);
		this.mLayoutId = layoutId;
	}
	
	static class ListItemView { // 自定义控件集合
		public TextView mDeviceNameTV;  //设备名
		public TextView mIsPairTV;   //配对信息
		public TextView mMacAddressTV;     //设备地址
	}

	@Override
	public int getCount() {
		return mDeviceList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDeviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mDeviceList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BluetoothDevice bluetoothDevice = mDeviceList.get(position);
		ListItemView device = null;
		String outerType = AppContext.getOuterMeasureType();
		if(convertView == null){
			device = new ListItemView();
			convertView = mLayoutInflater.inflate(mLayoutId, null);
			device.mDeviceNameTV = (TextView) convertView.findViewById(R.id.bluetooth_device_listitem_tv_devicename);
			device.mIsPairTV = (TextView) convertView.findViewById(R.id.bluetooth_device_listitem_tv_ispair);
			device.mMacAddressTV = (TextView) convertView.findViewById(R.id.bluetooth_device_listitem_tv_marAddress);
			
			convertView.setTag(device);
		}else {
			device = (ListItemView) convertView.getTag();
		}
		try {
			device.mDeviceNameTV.setText(bluetoothDevice.getName());
		} catch (Exception e) {
			device.mDeviceNameTV.setText("null");
		}
		if((mcontext.getString(R.string.outer_measuretypes_soeasytest).equals(
					outerType)) ){
			device.mIsPairTV.setText("");
		}else{
			if(bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED){
				pairInfo = mcontext.getString(R.string.bluetooth_paried);
			}else {
				pairInfo = mcontext.getString(R.string.bluetooth_unparied);
			}
			device.mIsPairTV.setText(pairInfo);
		}
		device.mMacAddressTV.setText(bluetoothDevice.getAddress());
		
		return convertView;
	}

}