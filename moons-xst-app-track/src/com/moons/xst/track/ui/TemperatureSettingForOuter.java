package com.moons.xst.track.ui;

import java.io.File;
import java.io.FileOutputStream;

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
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.buss.BluetoothHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.UIHelper;

public class TemperatureSettingForOuter extends PreferenceActivity {

	private static final String Temperature = "Temperature";
	private int mFsl = 95;
	private String mSaveType = "Current";
	private String mBTAddress = "";
	private String mBTPwd = "";

	public static final int REQCODE_BLUETOOTHSET = 1;// 蓝牙设置

	SharedPreferences mPreferences;
	ImageButton home_head_Rebutton;

	Preference fsl;

	CheckBoxPreference current;
	CheckBoxPreference max;
	CheckBoxPreference min;

	Preference bluetoothsearch;
	Preference pwd;

	EditText pwdEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

		// 设置显示Preferences
		addPreferencesFromResource(R.xml.temperaturesetprefforouter);
		// 获得SharedPreferences
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		try {
			mFsl = getIntent().getIntExtra("fsl", 95);
			mSaveType = getIntent().getStringExtra("saveType");
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

		home_head_Rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		home_head_Rebutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Save();
				AppManager.getAppManager().finishActivity(
						TemperatureSettingForOuter.this);
			}
		});

		fsl = (Preference) findPreference("fsl");
		float fFsl = (float) mFsl / 100;
		fsl.setSummary(Float.toString(fFsl));
		fsl.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				LayoutInflater factory = LayoutInflater
						.from(TemperatureSettingForOuter.this);
				final View view = factory
						.inflate(R.layout.editbox_layout, null);
				final EditText edit = (EditText) view
						.findViewById(R.id.editText);// 获得输入框对象
				edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						4) });
				edit.setText(fsl.getSummary());
				new com.moons.xst.track.widget.AlertDialog(
						TemperatureSettingForOuter.this)
						.builder()
						.setTitle(getString(R.string.temperature_fsl_desc))
						.setEditView(
								view,
								InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
										| InputType.TYPE_NUMBER_FLAG_SIGNED, "")
						.setPositiveButton(getString(R.string.sure),
								new android.view.View.OnClickListener() {
									@Override
									public void onClick(View v) {
										try {
											String identifier = edit.getText()
													.toString();
											mFsl = (int) (Float
													.parseFloat(identifier) * 100);
											if (mFsl > 100 || mFsl < 0) {
												UIHelper.ToastMessage(
														getApplication(),
														R.string.temperature_power_scope);
											} else {
												// 设置发射率
												// BluetoothHelper.getIntance(TemperatureSettingForOuter.this,
												// AppContext.getOuterMeasureType(),
												// Temperature).setFSL(Double.valueOf(identifier));
												fsl.setSummary(String
														.valueOf(identifier));
											}
										} catch (Exception e) {
											UIHelper.ToastMessage(
													getApplication(),
													R.string.temperature_power_mistake);
										}

									}
								})
						.setNegativeButton(getString(R.string.cancel), null)
						.show();
				return true;
			}
		});

		if (BluetoothHelper.getIntance(this, AppContext.getOuterMeasureType(),
				Temperature).getFSLEnable()) {
			fsl.setEnabled(true);
			fsl.setSummary(Float.toString(fFsl));
		} else {
			fsl.setEnabled(false);
			fsl.setSummary("");
		}

		// 当前温度
		current = (CheckBoxPreference) findPreference("Current");
		current.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO 自动生成的方法存根
				if (current.isChecked()) {
					max.setChecked(false);
					min.setChecked(false);
				} else
					current.setChecked(true);
				return true;
			}
		});

		// 最大温度
		max = (CheckBoxPreference) findPreference("MAX");
		max.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO 自动生成的方法存根
				if (max.isChecked()) {
					current.setChecked(false);
					min.setChecked(false);
				} else
					max.setChecked(true);
				return true;
			}
		});

		// 最大温度
		min = (CheckBoxPreference) findPreference("MIN");
		min.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO 自动生成的方法存根
				if (min.isChecked()) {
					current.setChecked(false);
					max.setChecked(false);
				} else
					min.setChecked(true);
				return true;
			}
		});

		// 蓝牙搜索
		bluetoothsearch = (Preference) findPreference("bluetooth_settings");
		bluetoothsearch.setSummary(mBTAddress);
		bluetoothsearch
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						// TODO 自动生成的方法存根
						Intent intent = new Intent(
								TemperatureSettingForOuter.this,
								BluetoothDeviceForOuterAty.class);
						intent.putExtra("From", Temperature);
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
						.from(TemperatureSettingForOuter.this);
				final View view = factory
						.inflate(R.layout.editbox_layout, null);
				final EditText edit = (EditText) view
						.findViewById(R.id.editText);// 获得输入框对象
				edit.setText(pwd.getSummary());
				new com.moons.xst.track.widget.AlertDialog(
						TemperatureSettingForOuter.this)
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
			/*
			 * @Override public boolean onPreferenceClick(Preference preference)
			 * {
			 * 
			 * pwdEditText.setText(mBTPwd);
			 * 
			 * pwd.setOnPreferenceChangeListener(new
			 * OnPreferenceChangeListener() {
			 * 
			 * @Override public boolean onPreferenceChange(Preference
			 * preference, Object newValue) { try { mBTPwd =
			 * String.valueOf(newValue); pwd.setSummary(mBTPwd); return true; }
			 * catch (Exception e) { return false; } } });
			 * 
			 * return true; }
			 */
		});

		GetCheckItem();

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

	private void GetCheckItem() {
		if (mSaveType.equals("Current")) {
			current.setChecked(true);
			max.setChecked(false);
			min.setChecked(false);
		} else if (mSaveType.equals("MAX")) {
			max.setChecked(true);
			current.setChecked(false);
			min.setChecked(false);
		} else {
			min.setChecked(true);
			current.setChecked(false);
			max.setChecked(false);
		}
	}

	private void Save() {
		Intent intent = new Intent();

		SaveConfig();
		intent.putExtra("fsl", mFsl);
		intent.putExtra("saveType", mSaveType);
		intent.putExtra("btAddress", mBTAddress);
		intent.putExtra("btPwd", mBTPwd);

		setResult(RESULT_OK, intent);
	}

	/**
	 * 保存配置信息
	 */
	private void SaveConfig() {
		SaveTemperatureBaseInfo();
		SaveBluetoothInfo();
	}

	private void SaveTemperatureBaseInfo() {
		// 保存类型
		// TODO Auto-generated method stub18
		if (current.isChecked())
			mSaveType = "Current";
		else if (max.isChecked())
			mSaveType = "MAX";
		else
			mSaveType = "MIN";

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.OuterTemperatureConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("SaveType")) {
					element.setAttribute("Using", mSaveType);
				} else if (element.getAttribute("Name").equals("FSL")) {
					element.setAttribute("Using", String.valueOf(mFsl));
					// AppContext.setBTFSLforTemputer(String.valueOf(mFsl/100));
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
					AppConst.OuterTemperatureConfigPath());
			StreamResult result = new StreamResult(fs);
			// 把DOM树转换为xml文件
			transformer.transform(domSource, result);
			result.getOutputStream().close();
			fs.close();
		} catch (SAXParseException e) {
			AppConst.OuterTemperatureConfigPathOnError();
			SaveTemperatureBaseInfo();
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
				if (element.getAttribute("Name").equals("Temperature")) {
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

			AppContext.setBlueToothAddressforTemperature(mBTAddress);
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
