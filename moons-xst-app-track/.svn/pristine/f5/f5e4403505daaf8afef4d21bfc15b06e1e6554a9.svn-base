package com.moons.xst.track.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.RFIDManager;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.receiver.TouchIDPosReceiver;

/**
 * 读取射频卡
 * 
 * @author LKZ
 * 
 */
public class Tool_NFC extends BaseActivity implements OnClickListener {

	static String strFilePath = "";
	static String lastRFIDNoString = "";
	// 当前编码
	private String mCurRfidNo = "";
	private final static String TOUCHIDPOS_TOOL_NFC = "Tool_NFC";
	private static final int REQUEST_CODE = 250;
	private static final int REQUEST_CODE1 = 520;
	private int mDebugFlag = 0;
	MyReceiver receiver;
	NfcAdapter nfcAdapter;
	static TextView tagContenTextView;
	Button btnClearFile;
	PendingIntent mPendingIntent;
	EditText nfcDescEditText;
	private ImageButton returnButton;
	private TouchIDPosReceiver mTouchIDPosReceiver;
	static Tool_NFC instance;
	MediaPlayer player;
	private CheckBox chkSaveBox = null;
	private CheckBox scaninputCheckBox;
	private CheckBox inputCheckBox;
	private CheckBox nfcSaveCheckBox;
	private static String equipmentName;
	private static com.moons.xst.track.widget.AlertDialog dialog;
	private static TextView tvSaveFile;
	private static File file;
	private String mobileNfcInfo;
	private static String line;
	private static String nfcContent;
	private Boolean isRFID = false;
//	private AppConfig appConfig;
	private AppContext ac;

	public static Tool_NFC getInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tool_nfc);
		try {
			mDebugFlag = getIntent().getExtras().getInt("debugFlag");
		} catch (Exception e) {

		}
		ac = (AppContext) getApplication();

		btnClearFile = (Button) findViewById(R.id.btn_clearNFC);
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			strFilePath = Environment.getExternalStorageDirectory()
					+ "/IDTemp.txt";
		} else {
			strFilePath = getFilesDir() + "/IDTemp.txt";
		}
		tvSaveFile = (TextView) findViewById(R.id.tv_nfc_save_file);
		file = new File(strFilePath);
		if (file.exists()) {
			tvSaveFile.setText(R.string.nfc_file_exist);
		} else {
			tvSaveFile.setText(R.string.nfc_file_inexistence);
			file = new File(strFilePath);
		}

		chkSaveBox = (CheckBox) findViewById(R.id.saveCheckBox);
		chkSaveBox.setOnClickListener(this);

		tagContenTextView = (TextView) findViewById(R.id.tool_nfc_tagContent);

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);

		scaninputCheckBox = (CheckBox) findViewById(R.id.scaninputCheckBox);
		scaninputCheckBox.setOnClickListener((OnClickListener) this);
		inputCheckBox = (CheckBox) findViewById(R.id.inputCheckBox);
		inputCheckBox.setOnClickListener(this);

		nfcSaveCheckBox = (CheckBox) findViewById(R.id.nfcCheckBox);
		nfcSaveCheckBox.setOnClickListener(this);

		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goBack();
			}
		});
//		清空文件内容
		btnClearFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (file.exists()) {
					LayoutInflater factory = LayoutInflater
							.from(Tool_NFC.this);
						final View view = factory.inflate(R.layout.textview_layout, null);
						new com.moons.xst.track.widget.AlertDialog(Tool_NFC.this).builder()
						.setTitle(getString(R.string.system_notice))
						.setView(view)
						.setMsg(getString(R.string.confirm_clear_file))
						.setPositiveButton(getString(R.string.sure), new OnClickListener() {
							@Override
							public void onClick(View v) {
								file.delete();
								String nfcText = tagContenTextView
										.getText().toString();
								tagContenTextView.setText(null);
								if (nfcText == lastRFIDNoString) {
									lastRFIDNoString = null;
									nfcText = null;

								}

								tvSaveFile
										.setText(R.string.nfc_file_inexistence);

							
							}
						}).setNegativeButton(getString(R.string.cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
				} else {
					return;
				}
			}
		});

		instance = this;
		//checkBox回显
		setCheckBoolean(String.valueOf(AppContext.getChSave()), chkSaveBox);
		setCheckBoolean(String.valueOf(AppContext.getChInput()), inputCheckBox);
		setCheckBoolean(String.valueOf(AppContext.getChScan()), scaninputCheckBox);
		setCheckBoolean(String.valueOf(AppContext.getChNFC()), nfcSaveCheckBox);
		
		/*appConfig = AppConfig.getAppConfig(this);
		String chSaveKeyValue = appConfig.get("chSave");
		setCheckBoolean(chSaveKeyValue, chkSaveBox);
		String chInputKeyValue = appConfig.get("chInput");
		setCheckBoolean(chInputKeyValue, inputCheckBox);
		String chScanKeyValue = appConfig.get("chScan");
		setCheckBoolean(chScanKeyValue,
				scaninputCheckBox);
		String chNfcKeyValue = appConfig.get("chNfc");
		setCheckBoolean(chNfcKeyValue, nfcSaveCheckBox);*/
	}
	
	/**
	 * 使用string转换成Boolean值
	 * 参数1：键值对的Value值
	 * 参数2：要设置的checkbox
	 */
	private void setCheckBoolean(String value,
			CheckBox checkBox) {
		if ("true".equals(value)) {
			checkBox.setChecked(true);
		}
		if ("false".equals(value)) {
			checkBox.setChecked(false);
		}
	}

	/**
	 * Checkbox的点击事件的开关及回显设置
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveCheckBox:
			if (chkSaveBox.isChecked()) {
				inputCheckBox.setChecked(true);
			} else {
				inputCheckBox.setChecked(false);
				scaninputCheckBox.setChecked(false);
				nfcSaveCheckBox.setChecked(false);
			}
			break;
		case R.id.inputCheckBox:
			if (inputCheckBox.isChecked()) {
				chkSaveBox.setChecked(true);
			} else {
				chkSaveBox.setChecked(false);
			}
			scaninputCheckBox.setChecked(false);
			nfcSaveCheckBox.setChecked(false);
			break;
		case R.id.scaninputCheckBox:
			if (scaninputCheckBox.isChecked()) {
				chkSaveBox.setChecked(true);
			} else {
				chkSaveBox.setChecked(false);
			}
			inputCheckBox.setChecked(false);
			nfcSaveCheckBox.setChecked(false);
			break;
		case R.id.nfcCheckBox:
			if (nfcSaveCheckBox.isChecked()) {
				chkSaveBox.setChecked(true);
			} else {
				chkSaveBox.setChecked(false);
			}
			inputCheckBox.setChecked(false);
			scaninputCheckBox.setChecked(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		setIntent(paramIntent);
		processIntent(paramIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 获取默认的NFC控制器 (如果是MS600的机器，则注册广播)
		if (AppContext.getRFIDUseYN()) {
			AppContext.mStartRFID = AppContext.StartRFID.Tool_NFC.toString();
//			RFIDManager.getRFIDManager(Tool_NFC.this, TOUCHIDPOS_TOOL_NFC)
//					.bindService();
			RFIDManager.getRFIDManager(Tool_NFC.this, TOUCHIDPOS_TOOL_NFC)
					.regReceiver_and_bindService();
			player = MediaPlayer.create(this, R.raw.notificationsound);
		} else {
			nfcAdapter = NfcAdapter.getDefaultAdapter(this);
			if (nfcAdapter == null) {
				UIHelper.ToastMessage(getApplication(), R.string.nfc_touchdesc_notsupport);
				finish();
				return;
			}
			if (!nfcAdapter.isEnabled()) {
				UIHelper.ToastMessage(getApplication(), R.string.nfc_touchdesc_starthint);
				finish();
				return;
			}
			mPendingIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, getClass())
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		}
		
		if (this.nfcAdapter == null)
			return;
		if (!this.nfcAdapter.isEnabled()) {
			tagContenTextView.setText(R.string.nfc_touchdesc_starthint);
		}
		this.nfcAdapter.enableForegroundDispatch(this, this.mPendingIntent,
				null, null);

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			goBack();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	// @Override
	// protected void onDestroy(){
	// if (AppConst.MS600YN())
	// Stop();
	// super.onDestroy();
	// }

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

	/**
	 * 读取NFC标签信息并显示在TextView
	 */
	protected void processIntent(Intent intent) {
		// 取出封装在intent中的TAG
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			tagContenTextView.setText("");
			// nfcDescEditText.setText("");
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			byte[] bytesId = tagFromIntent.getId();
			try {
				// 调用振动
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(500);

				tagContenTextView.setText(bytesToHexString(bytesId));
				mobileNfcInfo = bytesToHexString(bytesId);
				// 添加手机的NFC功能
				if (chkSaveBox.isChecked() && inputCheckBox.isChecked()) {

					showInputAlerDialog(mobileNfcInfo);
				}
				if (chkSaveBox.isChecked() && scaninputCheckBox.isChecked()) {
					Intent intent1 = new Intent(Tool_NFC.this,
							CaptureActivity.class);
					intent1.putExtra("ScanType", "EDITTEXT");
					startActivityForResult(intent1, REQUEST_CODE);
				}
				if (chkSaveBox.isChecked() && nfcSaveCheckBox.isChecked()) {
					if (ReadTxtFile(mobileNfcInfo + "") == true) {
						LayoutInflater factory = LayoutInflater
								.from(this);
							final View view = factory.inflate(R.layout.textview_layout, null);
							new com.moons.xst.track.widget.AlertDialog(this).builder()
							.setTitle(getString(R.string.system_notice))
							.setView(view)
							.setMsg(getString(R.string.cancel_file_hint))
							.setPositiveButton(getString(R.string.sure), new OnClickListener() {
								@Override
								public void onClick(View v) {
								}
							}).show();
					} else {
						WriteTxtFile(mobileNfcInfo, "");
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存钮扣信息
	 */
	private void saveNFCInfo() {
		if (nfcDescEditText.getText().length() > 250) {
			UIHelper.ToastMessage(Tool_NFC.this, "字符数超过250，请重新填写");
			return;
		}
		try {
			String tagValueString = String.valueOf(tagContenTextView.getText());
			String tagNFCDescString = String.valueOf(nfcDescEditText.getText());
			String gpsInfoString = "<nfcInfo>";
			if (StringUtils.isEmpty(tagValueString)) {
				UIHelper.ToastMessage(Tool_NFC.this, "请先扫描标牌");
				return;
			}
			gpsInfoString += "<nfcTagValue>" + tagValueString
					+ "</nfcTagValue>";
			gpsInfoString += "<nfcDes>" + tagNFCDescString + "</nfcDes>";
			gpsInfoString += "</nfcInfo>";
			com.moons.xst.track.common.FileUtils.SaveToFile(
					AppConst.XSTBasePath(), AppConst.NfcInfoFile,
					gpsInfoString, true);
			UIHelper.ToastMessage(Tool_NFC.this, R.string.nfc_save_success);
			tagContenTextView.setText("");
			nfcDescEditText.setText("");
		} catch (IOException e) {
			e.printStackTrace();
			UIHelper.ToastMessage(Tool_NFC.this, R.string.nfc_defeated_hint + e.getMessage());
		}
	}

	private void goBack() {
		if (AppContext.getRFIDUseYN()) {
//			RFIDManager.getRFIDManager(Tool_NFC.this, TOUCHIDPOS_TOOL_NFC)
//					.unbindService();
			RFIDManager.getRFIDManager(Tool_NFC.this,
					TOUCHIDPOS_TOOL_NFC).unRegReceiver_and_unbindRemoteService();
			
			if (player != null)
				player.release();
		}
		ac.setConfigChSave(chkSaveBox.isChecked());
		ac.setConfigChInput(inputCheckBox.isChecked());
		ac.setConfigChScan(scaninputCheckBox.isChecked());
		ac.setConfigChNFC(nfcSaveCheckBox.isChecked());
		AppManager.getAppManager().finishActivity(Tool_NFC.this);
	}

	/**
	 * 封装的nfc接受广播
	 */
	public void showIDPosInfo(final String info) {
		//接送到广播后判断当前页面是否退出
		if(!isFinishing()){
			
		if (!StringUtils.isEmpty(info))
			player.start();
		tagContenTextView.setText(info);
		if (dialog != null)
			dialog.dismiss();

		if (chkSaveBox.isChecked() && inputCheckBox.isChecked()) {
			showInputAlerDialog(info);
		}

		if (chkSaveBox.isChecked() && scaninputCheckBox.isChecked()) {
			//跳转扫码页面前关闭nfc
			if (AppContext.getRFIDUseYN()) {
				RFIDManager.getRFIDManager(Tool_NFC.this,
						TOUCHIDPOS_TOOL_NFC).unRegReceiver_and_unbindRemoteService();
				
				/*if (player != null)
					player.release();*/
			}
			Intent intent = new Intent(Tool_NFC.this, CaptureActivity.class);
			intent.putExtra("ScanType", "EDITTEXT");
			startActivityForResult(intent, REQUEST_CODE1);
		}
		if (chkSaveBox.isChecked() && nfcSaveCheckBox.isChecked()) {
			if (ReadTxtFile(info + "") == true) {
				if(dialog1==null||!dialog1.isShowing()){
					LayoutInflater factory = LayoutInflater
							.from(this);
						final View view = factory.inflate(R.layout.textview_layout, null);
						dialog1=new com.moons.xst.track.widget.AlertDialog(this).builder()
						.setTitle(getString(R.string.system_notice))
						.setView(view)
						.setMsg(getString(R.string.cancel_file_hint))
						.setPositiveButton(getString(R.string.sure), new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						});
						dialog1.show();
				}
			} else {
				WriteTxtFile(info, "");
			}

		}
	}
	}
	com.moons.xst.track.widget.AlertDialog dialog1;
	/**
	 * 接收到广播从SD文件中读取
	 */

	private static Boolean ReadTxtFile(final String info) {
		BufferedReader br;
		try {
			StringBuffer sb = new StringBuffer();
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			if (sb.toString().contains(info)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 显示手动输入对话框
	 * 
	 */
	private static void showInputAlerDialog(final String info) {
		LayoutInflater factory = LayoutInflater
				.from(instance);
			final View view = factory.inflate(R.layout.editbox_layout, null);
			final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象
			dialog=new com.moons.xst.track.widget.AlertDialog(instance).builder()
			.setTitle(instance.getString(R.string.import_scutcheon_name))
			.setEditView(view, InputType.TYPE_CLASS_TEXT, "")
			.setPositiveButton(instance.getString(R.string.sure), new OnClickListener() {
				@Override
				public void onClick(View v) {

					equipmentName = edit.getText().toString().trim();
					if (TextUtils.isEmpty(equipmentName)) {
						UIHelper.ToastMessage(instance, R.string.import_scutcheon_name);
					} else {
						if (ReadTxtFile(info) == true) {
							// 返回true表示文件中存在该内容,弹出对话框提示用户更改操作
							LayoutInflater factory = LayoutInflater
									.from(instance);
								final View view = factory.inflate(R.layout.textview_layout, null);
								new com.moons.xst.track.widget.AlertDialog(instance).builder()
								.setTitle(instance.getString(R.string.system_notice))
								.setView(view)
								.setMsg(instance.getString(R.string.file_exist_fint))
								.setPositiveButton(instance.getString(R.string.sure), new OnClickListener() {
									@Override
									public void onClick(View v) {
										// 点击“是”，读取到相应文件内容并删除
										deleteSameContent(info,
												equipmentName);
									}
								}).setNegativeButton(instance.getString(R.string.cancel), new OnClickListener() {
									@Override
									public void onClick(View v) {
										}
								}).show();
							
						} else {
							nfcContent = tagContenTextView.getText()
									.toString();
							WriteTxtFile(nfcContent, equipmentName);
						}
					}
				}
			}).setNegativeButton(instance.getString(R.string.cancel), new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			}).setCanceledOnTouchOutside(false);
			dialog.show();
		}

	// 将文件中重复的内容删除
	public static void deleteSameContent(String info, String edInput) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(strFilePath)));
			StringBuffer sb = new StringBuffer();
			line = "";
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.indexOf(info) == -1) {
					sb.append(line).append("\r\n");
				}
			}
			br.close();
			file.delete();
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					strFilePath)));

			bw.write(sb.toString() + edInput + "  " + info + "\r\n");
			bw.flush();
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 扫描的结果写入到IDTemp.txt
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		Bundle bundle = data.getExtras();
		final String scancleString = bundle.getString("codeResult");
		final String nfcContent = tagContenTextView.getText().toString();
		if (requestCode == REQUEST_CODE) {
			if (data != null) {

				if (ReadTxtFile(nfcContent) == true) {
					LayoutInflater factory = LayoutInflater
							.from(this);
						final View view = factory.inflate(R.layout.textview_layout, null);
						new com.moons.xst.track.widget.AlertDialog(this).builder()
						.setTitle(getString(R.string.system_notice))
						.setView(view)
						.setMsg(scancleString +getString(R.string.scutcheon_exist_fint))
						.setPositiveButton(getString(R.string.sure), new OnClickListener() {
							@Override
							public void onClick(View v) {
								deleteSameContent(mobileNfcInfo,
										scancleString);
							}
						}).setNegativeButton(getString(R.string.cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								}
						}).show();
					
					return;
				} else {
					WriteTxtFile(mobileNfcInfo, scancleString);

				}

			}
		} else if (requestCode == REQUEST_CODE1) {
			if (data != null) {

				if (ReadTxtFile(nfcContent) == true) {
					LayoutInflater factory = LayoutInflater
							.from(this);
						final View view = factory.inflate(R.layout.textview_layout, null);
						new com.moons.xst.track.widget.AlertDialog(this).builder()
						.setTitle(getString(R.string.system_notice))
						.setView(view)
						.setMsg(getString(R.string.cancel_file_hint))
						.setPositiveButton(getString(R.string.sure), new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
					return;
				} else {
					WriteTxtFile(nfcContent, scancleString);
				}
			}
		}
	}

	/**
	 * 注册接收器
	 */
	private void regReceiver() {
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.RfidActivity");
		Tool_NFC.this.registerReceiver(receiver, filter);

	}

	/**
	 * 解除注册接收器
	 */
	private void unRegReceiver() {
		Tool_NFC.this.unregisterReceiver(receiver);
	}

	/**
	 * 广播已经注释掉
	 *
	 */
	public class MyReceiver extends BroadcastReceiver {

		// 自定义一个广播接收器

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();

			mCurRfidNo = bundle.getString("RfidNo");
			if (!StringUtils.isEmpty(mCurRfidNo)) {
				tagContenTextView.setText(mCurRfidNo);
			}

		}

		public MyReceiver() {
			// 构造函数，做一些初始化工作，本例中无任何作用
		}
	}
	//   将标签内容及一维码，手输入写入到文件中
	private static void WriteTxtFile(String strcontent, String inPutOrScan) {
		if (strcontent.contains("错误"))
			return;
		/*
		 * if (strcontent.equals(lastRFIDNoString)) return; lastRFIDNoString =
		 * strcontent;
		 */
		BufferedWriter bw;
		try {
			if (!file.exists()) {
				file = new File(strFilePath);
				tvSaveFile.setText(R.string.nfc_file_exist);
			}
			bw = new BufferedWriter(new FileWriter(file, true));
			// 每次写入时，都换行写
			String strContent = inPutOrScan + "  " + strcontent + "\r\n";
			bw.write(strContent);
			bw.flush();
			bw.close();
			AlertDialog.Builder builder = new AlertDialog.Builder(instance);
			builder.setTitle(R.string.nfc_hint_save_succeed)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage(inPutOrScan + " " + strcontent)
					.setPositiveButton(R.string.sure,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
								}
							}).show();

			tvSaveFile.setText(R.string.nfc_file_exist);
		} catch (Exception e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(instance);
			builder.setTitle(R.string.system_notice)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage(strcontent + R.string.nfc_defeated_hint)
					.setPositiveButton(R.string.sure,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
								}
							}).show();
			e.printStackTrace();
		}

	}
}
