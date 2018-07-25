package com.moons.xst.track.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;

public class VibrationSettingForOuter extends PreferenceActivity {

	private String mZDType = "";
	private String mBTAddress = "";
	private String mBTPwd = "";

	static String vibpackage = "";
	static CharSequence[] strPassageway;

	public static final int REQCODE_BLUETOOTHSET = 1;// 蓝牙设置

	SharedPreferences mPreferences;

	ImageButton home_head_Rebutton;

	Preference vibrationpackage;

	ImageButton returnButton;

	Preference bluetoothsearch;
	Preference pwd;
	EditText pwdEditText;

	private com.moons.xst.track.widget.AlertDialog dialog;

	private static int vibrationIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

		// 设置显示Preferences
		addPreferencesFromResource(R.xml.vibrationsetprefforouter);
		// 获得SharedPreferences
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		try {
			// mZDType = getIntent().getExtras().getInt("zdType");
			mZDType = getIntent().getExtras().getString("vibPackage");
			mBTAddress = getIntent().getStringExtra("btAddress");
			mBTPwd = getIntent().getStringExtra("btPassword");
		} catch (Exception e) {

		}

		ListView localListView = getListView();
		localListView.setBackgroundColor(0);
		localListView.setCacheColorHint(0);
		((ViewGroup) localListView.getParent()).removeView(localListView);
		ViewGroup localViewGroup = (ViewGroup) getLayoutInflater().inflate(
				R.layout.temperaturesetting, null);
		((ViewGroup) localViewGroup.findViewById(R.id.setting_content))
				.addView(localListView, -1, -1);
		setContentView(localViewGroup);
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Save();
				AppManager.getAppManager().finishActivity(
						VibrationSettingForOuter.this);
			}
		});
		initData();

		// 蓝牙搜索
		bluetoothsearch = (Preference) findPreference("bluetooth_settings");
		bluetoothsearch.setSummary(mBTAddress);
		bluetoothsearch
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(
								VibrationSettingForOuter.this,
								BluetoothDeviceForOuterAty.class);
						intent.putExtra("From", "Vibration");
						startActivityForResult(intent, REQCODE_BLUETOOTHSET);
						return true;
					}
				});

		// 配对秘钥
		pwd = (Preference) findPreference("pwd");
		pwd.setSummary(mBTPwd);
		// pwdEditText = (EditText) pwd.getEditText();
		// pwdEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		pwd.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				LayoutInflater factory = LayoutInflater
						.from(VibrationSettingForOuter.this);
				final View view = factory
						.inflate(R.layout.editbox_layout, null);
				final EditText edit = (EditText) view
						.findViewById(R.id.editText);// 获得输入框对象
				edit.setText(pwd.getSummary());
				new com.moons.xst.track.widget.AlertDialog(
						VibrationSettingForOuter.this)
						.builder()
						.setTitle(getString(R.string.bluetooth_password))
						.setEditView(view, InputType.TYPE_CLASS_NUMBER, "")
						.setPositiveButton(getString(R.string.sure),
								new android.view.View.OnClickListener() {
									@Override
									public void onClick(View v) {
										try {
											mBTPwd = edit.getText().toString();
											pwd.setSummary(mBTPwd);
										} catch (Exception e) {
										}
									}
								})
						.setNegativeButton(getString(R.string.cancel), null)
						.show();
				return true;
			}
		});

		if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer)) {
			bluetoothsearch.setEnabled(true);
			pwd.setEnabled(true);
		} else {
			bluetoothsearch.setEnabled(false);
			pwd.setEnabled(false);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQCODE_BLUETOOTHSET) {
			if (resultCode == Activity.RESULT_OK) {
				mBTAddress = data.getExtras().getString(
						BluetoothDeviceForOuterAty.EXTRA_DEVICE_ADDRESS);
				bluetoothsearch.setSummary(mBTAddress);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Save();
			AppManager.getAppManager().finishActivity(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initData() {
		/* 测量类型 */
		initPackage();
	}

	/**
	 * 加载测量类型LISTPREFERENCE
	 */
	private void initPackage() {
		/* 测量类型 */
		vibpackage = changeDesc(mZDType, true);
		vibrationpackage = (Preference) findPreference("vibrationpackage");
		vibrationpackage.setSummary(vibpackage);

		vibrationpackage
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						final String[] vibration = getResources()
								.getStringArray(
										R.array.Vibrationpackage_values_forOuter);
						final List<String> listData = new ArrayList<String>();
						int checkitem = 0;
						for (int i = 0; i < vibration.length; i++) {
							listData.add(vibration[i]);
							if (vibration[i].equals(vibrationpackage
									.getSummary())) {
								checkitem = i;
							}
						}

						LayoutInflater factory = LayoutInflater
								.from(VibrationSettingForOuter.this);
						final View view = factory.inflate(
								R.layout.listview_layout, null);
						dialog = new com.moons.xst.track.widget.AlertDialog(
								VibrationSettingForOuter.this)
								.builder()
								.setTitle(
										getString(R.string.vibrationSetting_vibrationtype))
								.setView(view)
								.setListViewItems(listData, checkitem,
										new AdapterView.OnItemClickListener() {
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												vibrationIndex = position;
												dialog.refresh(listData,
														vibrationIndex);
											}
										})
								.setPositiveButton(getString(R.string.sure),
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												String value = vibration[vibrationIndex];
												vibrationpackage
														.setSummary(value);
												vibpackage = value;
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null);
						dialog.show();
						return true;
					}
				});

	}

	private String changeDesc(String cha, boolean flag) {
		String res = cha;
		if (flag) {
			if (res.contains("A")) {
				res = res.replaceAll("A",
						getString(R.string.vibrationSetting_type_acceleration));
			}
			if (res.contains("V")) {
				res = res.replaceAll("V",
						getString(R.string.vibrationSetting_type_tempo));
			}
			if (res.contains("S")) {
				res = res.replaceAll("S",
						getString(R.string.vibrationSetting_type_displacement));
			}
		} else {
			if (res.contains(getString(R.string.vibrationSetting_type_acceleration))) {
				res = res.replaceAll(
						getString(R.string.vibrationSetting_type_acceleration),
						"A");
			}
			if (res.contains(getString(R.string.vibrationSetting_type_tempo))) {
				res = res.replaceAll(
						getString(R.string.vibrationSetting_type_tempo), "V");
			}
			if (res.contains(getString(R.string.vibrationSetting_type_displacement))) {
				res = res.replaceAll(
						getString(R.string.vibrationSetting_type_displacement),
						"S");
			}
		}

		return res;
	}

	private void Save() {
		Intent intent = new Intent();

		SaveConfig();

		// intent.putExtra("zdType", mZDType);
		intent.putExtra("vibpackage", mZDType);
		if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer)) {
			intent.putExtra("btAddress", mBTAddress);
			intent.putExtra("btPwd", mBTPwd);
		}
		setResult(RESULT_OK, intent);
	}

	/**
	 * 保存配置信息
	 */
	private void SaveConfig() {
		SaveVibrationBaseInfo();
		if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer))
			SaveBluetoothInfo();
	}

	private void SaveVibrationBaseInfo() {
		/* 获取更改后的参数 */
		// 测量包
		mZDType = changeDesc(vibrationpackage.getSummary().toString(), false);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.OuterVibrationConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("vibrationType")) {
					element.setAttribute("Using", mZDType);
				}
			}
			// 保存xml文件
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			javax.xml.transform.Transformer transformer = transformerFactory
					.newTransformer();
			DOMSource domSource = new DOMSource(doc);
			// 设置编码类型
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			FileOutputStream fs = new FileOutputStream(
					AppConst.OuterVibrationConfigPath());
			StreamResult result = new StreamResult(fs);
			// 把DOM树转换为xml文件
			transformer.transform(domSource, result);
			fs.close();
		} catch (SAXParseException e) {
			AppConst.OuterVibrationConfigPathOnError();
			SaveVibrationBaseInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SaveBluetoothInfo() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.MeasureTypeForOuterConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("Vibration")) {
					element.setAttribute("BTAddress", mBTAddress);
					element.setAttribute("Password", mBTPwd);
				}
			}

			// 保存xml文件
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			javax.xml.transform.Transformer transformer = transformerFactory
					.newTransformer();
			DOMSource domSource = new DOMSource(doc);
			// 设置编码类型
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			StreamResult result = new StreamResult(new FileOutputStream(
					AppConst.MeasureTypeForOuterConfigPath()));
			// 把DOM树转换为xml文件
			transformer.transform(domSource, result);

			AppContext.setBlueToothAddressforVibration(mBTAddress);
			AppContext.setBTConnectPwdforTemperature(mBTPwd);
			result.getOutputStream().close();
		} catch (SAXParseException e) {
			AppConst.MeasureTypeForOuterConfigPathOnError();
			SaveBluetoothInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
