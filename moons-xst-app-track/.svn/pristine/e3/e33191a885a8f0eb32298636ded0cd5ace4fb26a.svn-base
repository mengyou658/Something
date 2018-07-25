package com.moons.xst.track.ui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.RFIDManager;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.ShineTextView;

public class RFIDLogin extends BaseActivity {
	
	private final static String TOUCHIDPOS_RFIDLOGIN = "RFIDLogin";
	
	RelativeLayout app_start_view;
	
	NfcAdapter nfcAdapter;
	PendingIntent mPendingIntent;
	MyReceiver receiver;
	String RFIDString = "";
	private Boolean isRFID=false;
	
	Vibrator vibrator;// = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	MediaPlayer player;// = MediaPlayer.create(RFIDLogin.this, R.raw.notificationsound);
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);  
		this.setTheme(R.style.Theme_HalfTranslucent);
        setContentView(R.layout.rfid_login);     
               
        app_start_view = (RelativeLayout) findViewById(R.id.app_start_view);
        app_start_view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				goBack();
			}
		});
        vibrator = (Vibrator)getSystemService(RFIDLogin.VIBRATOR_SERVICE);
        player = MediaPlayer.create(RFIDLogin.this, R.raw.notificationsound);
        
        /* 不是600机型，获取NFC控制器 */
		if (!AppContext.getRFIDUseYN()) {
			// 获取默认的NFC控制器
			isNFCUse();		
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		/* 600机型，绑定Service */
		if (AppContext.getRFIDUseYN()){
			regReceiver();
			RFIDManager.getRFIDManager(RFIDLogin.this, "RFIDLogin").bindService();
		} else {			
			if (this.nfcAdapter != null && this.nfcAdapter.isEnabled())
				this.nfcAdapter.enableForegroundDispatch(this, this.mPendingIntent,
						null, null);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}
	
	@Override
	protected void onNewIntent(Intent paramIntent){
		RFIDString = "";
		setIntent(paramIntent);
		processIntent(paramIntent);
	}
	
	private void isNFCUse() {
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (this.nfcAdapter == null) {
			UIHelper.ToastMessage(
					getBaseContext(),
					R.string.djidpos_message_notsupportnfc);
			AppManager.getAppManager().finishActivity(RFIDLogin.this);
			return;
		}
		if (!this.nfcAdapter.isEnabled()) {
			UIHelper.ToastMessage(getBaseContext(), R.string.djidpos_message_opennfc);
			AppManager.getAppManager().finishActivity(RFIDLogin.this);
			return;
		}
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}
	
	/**
	 * 读取NFC标签信息
	 */
	protected void processIntent(Intent intent) {
		// 取出封装在intent中的TAG
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			byte[] bytesId = tagFromIntent.getId();
			try {
				// 调用振动
	    		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	    	    vibrator.vibrate(500);
	    	    
				RFIDString = bytesToHexString(bytesId);			
				intent.putExtra("RFIDString", RFIDString);				
				setResult(RESULT_OK, intent);
				
				goBack();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}
	
	/**
	 * 字符序列转换为16进制字符串
	 * 
	 * @param src
	 * @return
	 */
	private String bytesToHexString(byte[] src) {
		StringBuffer sb = new StringBuffer(src.length);
		String sTemp;
		for (int i = src.length - 1; i >= 0; i--) {
			sTemp = Integer.toHexString(0xFF & src[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
	private void goBack(){
		if (AppContext.getRFIDUseYN()) {
			unRegReceiver();
			RFIDManager.getRFIDManager(null, "").unbindService();
		}
		AppManager.getAppManager().finishActivity(RFIDLogin.this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void get600RFIDStr(String rfidStr) {	
		Intent intent = new Intent();
		intent.putExtra("RFIDString", rfidStr);		
		setResult(RESULT_OK, intent);	
		goBack();
	}
	
	/**
	 * 注册接收器
	 */
    private void regReceiver() {
    	//注册接收器
    	receiver = new MyReceiver();
    	IntentFilter filter=new IntentFilter();
    	filter.addAction("android.intent.action.RfidActivity");
    	RFIDLogin.this.registerReceiver(receiver,filter);
    }
    /**
     * 解除注册接收器
     */
    private void unRegReceiver(){    	
    	RFIDLogin.this.unregisterReceiver(receiver);
    }
	
	public class MyReceiver extends BroadcastReceiver {

    	//自定义一个广播接收器
    	@Override
    	public void onReceive(Context context, Intent intent) {
	    	Bundle bundle = intent.getExtras();	
	    	String curRfidNo = bundle.getString("RfidNo");
	    	
	    	//处理接收到的内容
	    	if (!StringUtils.isEmpty(curRfidNo)) {
	    		if(!isFinishing()){
	    			
	    		// 调用振动
//	    		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	    	    vibrator.vibrate(500);
	    	    //调用声音
//	    	    MediaPlayer player = MediaPlayer.create(RFIDLogin.this, R.raw.notificationsound);
	    	    player.start();
	    	    
	    		get600RFIDStr(curRfidNo);
	    		}
	    	}
	    	}
    
	    	public MyReceiver(){
	    	//构造函数，做一些初始化工作，本例中无任何作用
	    	}
    }
}