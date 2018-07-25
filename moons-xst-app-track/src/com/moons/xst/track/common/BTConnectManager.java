package com.moons.xst.track.common;

/**
 * 未完成，后续将打开蓝牙和连接蓝牙封装在此...
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;

public class BTConnectManager{
	
	private Context mContext;
	private static BTConnectManager btConnectManager;
	// 连接动画
	private ProgressDialog mProDialog;
	private Dialog latestOrFailDialog;
	
	private BluetoothAdapter mBluetoothAdapter;
	private AlertDialog mAlertDialog;   //确定打开蓝牙 dialog
	
	public static BTConnectManager getBTConnectManager() {
		if (btConnectManager == null) {
			btConnectManager = new BTConnectManager();
		}
		
		return btConnectManager;
	}
	
	/**
     * 连接蓝牙
     */
    private void ConnectBTDevice(Context context, String address, String pwd, 
    		final boolean isShowMsg){
    	this.mContext = context;
    	if (isShowMsg){
    		if (mProDialog == null) {
				mProDialog = ProgressDialog.show(mContext, null, "连接中，请稍等...",
						true, true);
				mProDialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO 自动生成的方法存根
						mProDialog.dismiss();
						mProDialog = null;
					}
				});
			} else if (mProDialog.isShowing()
					|| (latestOrFailDialog != null && latestOrFailDialog
							.isShowing()))
				return;
    	}
    	
    	final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				// 进度条对话框不显示 - 检测结果也不显示
				if (mProDialog != null && !mProDialog.isShowing()) {
					return;
				}
				// 关闭并释放释放进度条对话框
				if (isShowMsg && mProDialog != null) {
					mProDialog.dismiss();
					mProDialog = null;
				}
				// 显示检测结果
				if (msg.what == 1) {
					
				} else if (isShowMsg) {
				}
			}
		};
    }
}