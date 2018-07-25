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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;

public class VibrationSetting extends PreferenceActivity {

	private int mZDType = 1;
	private String mVibPackage = "";
	private String mBandWidth = "0xc0000"; // 带宽默认为Highpass12KHz
	private int[] mCZPars = new int[10];
	private String mBTAddress = "";
	private String mBTPwd = "";
	
	static String vibpackage = "";
	static String passageway = "";
	static String maxanyfreq = "";
	static String samplepoint = "";
	static String avgnums = "";
	static CharSequence[] strPassageway;
	static CharSequence[] strMaxAnyFreq;
	static CharSequence[] strSamplePoint;
	
	public static final int REQCODE_BLUETOOTHSET = 1;//蓝牙设置
	
	SharedPreferences mPreferences;

	Preference vibrationpackage;
	Preference vibrationpassageway;
	Preference maxfreq;
	Preference samplepoints;
	Preference windowtype;
	Preference avgnum;
	Preference averwrap;
	Preference bandwidth;

	ImageButton returnButton;
	
	Preference bluetoothsearch;
	EditTextPreference pwd;
	EditText pwdEditText;
	
	private com.moons.xst.track.widget.AlertDialog dialog;
	
	private static int vibrationIndex=0;
	private static int vibrationpassageIndex=0;
	private static int frequencyIndex=0;
	private static int samplingIndex=0;
	private static int windowIndex=0;
	private static int averageIndex=0;
	private static int overlapIndex=0;
	private static int bandwidthIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

		// 设置显示Preferences
		addPreferencesFromResource(R.xml.vibrationsetpref);
		// 获得SharedPreferences
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
        try {			
			//mZDType = getIntent().getExtras().getInt("zdType");
        	mVibPackage = getIntent().getExtras().getString("vibPackage");
        	mCZPars = getIntent().getExtras().getIntArray("czPars");
			mBTAddress = getIntent().getStringExtra("btAddress");
			mBTPwd = getIntent().getStringExtra("btPassword");
			mBandWidth = getIntent().getStringExtra("bandWidth");
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
				if (!checkInput()) {
					UIHelper.ToastMessage(VibrationSetting.this, getString(R.string.vibration_impact_notsupport));
					return;
				}
				Save();
				AppManager.getAppManager().finishActivity(VibrationSetting.this);
			}
		});
		
		initData();
		
		// 蓝牙搜索
		bluetoothsearch = (Preference) findPreference("bluetooth_settings");
		bluetoothsearch.setSummary(mBTAddress);
		bluetoothsearch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(VibrationSetting.this,BluetoothDeviceAty.class);
				startActivityForResult(intent, REQCODE_BLUETOOTHSET);
				return true;
			}
		});
		
		// 配对秘钥
		pwd = (EditTextPreference) findPreference("pwd");
		pwd.setSummary(mBTPwd);
		pwdEditText = (EditText) pwd.getEditText();
		pwdEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		pwd.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				pwdEditText.setText(mBTPwd);
				
				pwd.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

							@Override
							public boolean onPreferenceChange(
									Preference preference,
									Object newValue) {
								try{
									mBTPwd = String.valueOf(newValue);
									pwd.setSummary(mBTPwd);
									return true;
								}
								catch (Exception e){
									return false;
								}							
							}
						});
				
				return true;
			}
		});
		
		if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer)){
			bluetoothsearch.setEnabled(true);
			pwd.setEnabled(true);
		} else {
			bluetoothsearch.setEnabled(false);
			pwd.setEnabled(false);
		}
		//冲击可设置带宽
		setBandwidthEnable(changeDesc(mVibPackage, true)
				.equalsIgnoreCase(getString(R.string.vibrationSetting_type_impact)));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == REQCODE_BLUETOOTHSET){
			if (resultCode == Activity.RESULT_OK){
				mBTAddress = data.getExtras().getString(BluetoothDeviceAty.EXTRA_DEVICE_ADDRESS);
				bluetoothsearch.setSummary(mBTAddress);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (!checkInput()) {
				UIHelper.ToastMessage(VibrationSetting.this, getString(R.string.vibration_impact_notsupport));
				return false;
			} else {
				Save();
				AppManager.getAppManager().finishActivity(this);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void initData() {
		/* 测量包 */
		initPackage();
		/* 通道 */
		initPassageway();
		/* 最大频率 */
		initMaxFreq();
		/* 采样点数 */
		initSamplePoints();
		/* 其他：窗类型，平均次数，重叠率 */
		initOthers();
	}
	
	/**
	 * 加载测量包LISTPREFERENCE
	 */
	private void initPackage() {
		/* 测量包 */		
	    vibpackage = changeDesc(mVibPackage, true);
		vibrationpackage = (Preference) findPreference("vibrationpackage");
		vibrationpackage.setSummary(vibpackage);
		
		vibrationpackage
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				final String[] vibration = getResources().getStringArray(R.array.Vibrationpackage_values);
				final List<String> listData = new ArrayList<String>();
				int checkitem=0;
				for(int i=0;i<vibration.length;i++){
					listData.add(vibration[i]);
					if(vibration[i].equals(vibrationpackage.getSummary())){
						checkitem=i;
					}
				}
				
				LayoutInflater factory = LayoutInflater
						.from(VibrationSetting.this);
				final View view = factory.inflate(R.layout.listview_layout, null);
				dialog = new com.moons.xst.track.widget.AlertDialog(VibrationSetting.this).builder()
					.setTitle(getString(R.string.vibrationSetting_vibrationpackage))
					.setView(view)
					.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							vibrationIndex = position;
							dialog.refresh(listData, vibrationIndex);
						}
					})
					.setPositiveButton(getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							String value=vibration[vibrationIndex];
			        		 vibrationpackage.setSummary(value);
			        		 vibpackage = value;
			        		 if (vibpackage.contains("_")) {
			        			 	strPassageway = new CharSequence[vibpackage.split("_").length];
			        				for (int i = 0; i < vibpackage.split("_").length; i++) {
			        					strPassageway[i] = vibpackage.split("_")[i].toString();
			        				}
			        			}
			        			else {
			        				strPassageway = new CharSequence[1];
			        				strPassageway[0] = vibpackage;
			        			}
			        		// vibrationpassageway.setEntries(strPassageway);
			        		 //vibrationpassageway.setEntryValues(strPassageway);
			        		 vibrationpassageway.setSummary(strPassageway[0].toString());
			        		// vibrationpassageway.setValueIndex(0);
			        		 
			        		 /* 联动加载最大频率和采样点数 */
			        		 if (strPassageway[0].toString().equals(getString(R.string.vibrationSetting_type_acceleration))) {
			        			 strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("ACC");
			        			 strSamplePoint = AppConst.getVibrationSamplePoints("ACC");
			        			 maxanyfreq = AppConst.MaxAnaFreq_ACC_DefaultValue;
			        			 samplepoint = AppConst.SamplePoints_ACC_DefaultValue;
			        		 }
			        		 else if (strPassageway[0].toString().equals(getString(R.string.vibrationSetting_type_tempo))) {
			        			 strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("VEL");
			        			 strSamplePoint = AppConst.getVibrationSamplePoints("VEL");
			        			 maxanyfreq = AppConst.MaxAnaFreq_VEL_DefaultValue;
			        			 samplepoint = AppConst.SamplePoints_VEL_DefaultValue;
			        		 }
			        		 else if (strPassageway[0].toString().equals(getString(R.string.vibrationSetting_type_displacement))) {
			        			 strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("DIS");
			        			 strSamplePoint = AppConst.getVibrationSamplePoints("DIS");
			        			 maxanyfreq = AppConst.MaxAnaFreq_DIS_DefaultValue;
			        			 samplepoint = AppConst.SamplePoints_DIS_DefaultValue;
			        		 }
			        		 else {
			        			 strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("ATT");
			        			 strSamplePoint = AppConst.getVibrationSamplePoints("ATT");
			        			 maxanyfreq = AppConst.MaxAnaFreq_ATT_DefaultValue;
			        			 samplepoint = AppConst.SamplePoints_ATT_DefaultValue;
			        			 avgnums = AppConst.SamplePoints_AVGNUM_DefaultValue;
			        		 }
			        		 
			        		 //maxfreq.setEntries(strMaxAnyFreq);
			        		 //maxfreq.setEntryValues(strMaxAnyFreq);
			        		 maxfreq.setSummary(maxanyfreq);
			        		// maxfreq.setValueIndex(getIndex(maxfreq.getEntryValues(), maxanyfreq));
			        		 
			        		 //samplepoints.setEntries(strSamplePoint);
			        		 //samplepoints.setEntryValues(strSamplePoint);
			        		 samplepoints.setSummary(samplepoint);
			        		 //samplepoints.setValueIndex(getIndex(samplepoints.getEntryValues(), samplepoint));
			        	 
			        		 avgnum.setSummary(avgnums);
			        		//冲击可设置带宽
		        			setBandwidthEnable(strPassageway[0].toString()
		        					.equals(getString(R.string.vibrationSetting_type_impact)));
						}
					}).setNegativeButton(getString(R.string.cancel),null);
				dialog.show();
				return true;
			}
		});
		
	}
	
	/**
	 * 加载通道LISTPREFERENCE
	 */
	private void initPassageway() {
		/* 根据测量包加载通道 */		
		if (vibpackage.contains("_")) {
			strPassageway = new CharSequence[vibpackage.split("_").length];
			for (int i = 0; i < vibpackage.split("_").length; i++) {
				strPassageway[i] = vibpackage.split("_")[i].toString();
			}
		}
		else {
			strPassageway = new CharSequence[1];
			strPassageway[0] = vibpackage;
		}
		
		passageway = changeZDType(mCZPars[5], "", true);
		vibrationpassageway = (Preference) findPreference("vibrationpassageway");
		//vibrationpassageway.setEntries(strPassageway);
		//vibrationpassageway.setEntryValues(strPassageway);
		vibrationpassageway.setSummary(passageway);
		//vibrationpassageway
		//.setValueIndex(getIndex(vibrationpassageway.getEntryValues(), passageway));
		vibrationpassageway
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				final String[] vibrationpassage =new String[]{(String) strPassageway[0]};
				final List<String> listData = new ArrayList<String>();
				int checkitem=0;
				for(int i=0;i<vibrationpassage.length;i++){
					listData.add(vibrationpassage[i]);
					if(vibrationpassage[i].equals(vibrationpassageway.getSummary())){
						checkitem=i;
					}
				}
				
				LayoutInflater factory = LayoutInflater
						.from(VibrationSetting.this);
				final View view = factory.inflate(R.layout.listview_layout, null);
				dialog = new com.moons.xst.track.widget.AlertDialog(VibrationSetting.this).builder()
					.setTitle(getString(R.string.vibrationSetting_vibrationpassageway))
					.setView(view)
					.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							vibrationpassageIndex = position;
							dialog.refresh(listData, vibrationpassageIndex);
							
						}
					}).setPositiveButton(getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							String newValue=vibrationpassage[vibrationpassageIndex];
		        		    vibrationpassageway.setSummary(String
									.valueOf(newValue));
		        		    
		        		    /* 联动加载最大频率和采样点数 */
			        		 if (newValue.toString().equals(getString(R.string.vibrationSetting_type_acceleration))) {
			        			 strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("ACC");
			        			 strSamplePoint = AppConst.getVibrationSamplePoints("ACC");
			        			 maxanyfreq = AppConst.MaxAnaFreq_ACC_DefaultValue;
			        			 samplepoint = AppConst.SamplePoints_ACC_DefaultValue;
			        		 }
			        		 else if (newValue.toString().equals(getString(R.string.vibrationSetting_type_tempo))) {
			        			 strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("VEL");
			        			 strSamplePoint = AppConst.getVibrationSamplePoints("VEL");
			        			 maxanyfreq = AppConst.MaxAnaFreq_VEL_DefaultValue;
			        			 samplepoint = AppConst.SamplePoints_VEL_DefaultValue;
			        		 }
			        		 else if (newValue.toString().equals(getString(R.string.vibrationSetting_type_displacement))) {
			        			 strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("DIS");
			        			 strSamplePoint = AppConst.getVibrationSamplePoints("DIS");
			        			 maxanyfreq = AppConst.MaxAnaFreq_DIS_DefaultValue;
			        			 samplepoint = AppConst.SamplePoints_DIS_DefaultValue;
			        		 }
			        		 else {
			        			 strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("ATT");
			        			 strSamplePoint = AppConst.getVibrationSamplePoints("ATT");
			        			 maxanyfreq = AppConst.MaxAnaFreq_ATT_DefaultValue;
			        			 samplepoint = AppConst.SamplePoints_ATT_DefaultValue;
			        			 avgnums = AppConst.SamplePoints_AVGNUM_DefaultValue;
			        		 }
			        		 
			        		 //maxfreq.setEntries(strMaxAnyFreq);
			        		 //maxfreq.setEntryValues(strMaxAnyFreq);
			        		 maxfreq.setSummary(maxanyfreq);
			        		//maxfreq.setValueIndex(getIndex(maxfreq.getEntryValues(), maxanyfreq));
			        		 
			        		 //samplepoints.setEntries(strSamplePoint);
			        		// samplepoints.setEntryValues(strSamplePoint);
			        		 samplepoints.setSummary(samplepoint);
			        		 //samplepoints.setValueIndex(getIndex(samplepoints.getEntryValues(), samplepoint));
			        		 
			        		 avgnum.setSummary(avgnums);
			        		//冲击可设置带宽
		        			setBandwidthEnable(newValue.toString()
		        					.equals(getString(R.string.vibrationSetting_type_impact)));
						}
					}).setNegativeButton(getString(R.string.cancel),null);
				dialog.show();
				return true;
			}
		});
		
	}
	
	/**
	 * 加载最大频率LISTPREFERENCE
	 */
	private void initMaxFreq() {
		if (passageway.equals(getString(R.string.vibrationSetting_type_acceleration)))
			strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("ACC");
		else if (passageway.equals(getString(R.string.vibrationSetting_type_tempo)))
			strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("VEL");
		else if (passageway.equals(getString(R.string.vibrationSetting_type_displacement)))
			strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("DIS");
		else
			strMaxAnyFreq = AppConst.getVibrationMaxAnaFreq("ATT");
		maxanyfreq = String.valueOf(mCZPars[1]) + "Hz";
		
		maxfreq = (Preference) findPreference("maxfreq");
		//maxfreq.setEntries(strMaxAnyFreq);
		//maxfreq.setEntryValues(strMaxAnyFreq);
		maxfreq.setSummary(maxanyfreq);
		//maxfreq.setValueIndex(getIndex(maxfreq.getEntryValues(), maxanyfreq));
		maxfreq
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				 final String[] frequency = (String[]) strMaxAnyFreq;
				final List<String> listData = new ArrayList<String>();
				int checkitem=0;
				frequencyIndex=0;
				for(int i=0;i<frequency.length;i++){
					listData.add(frequency[i]);
					if(frequency[i].equals(maxfreq.getSummary())){
						checkitem=i;
						frequencyIndex=i;
					}
				}
				
				LayoutInflater factory = LayoutInflater
						.from(VibrationSetting.this);
				final View view = factory.inflate(R.layout.listview_layout, null);
				dialog = new com.moons.xst.track.widget.AlertDialog(VibrationSetting.this).builder()
					.setTitle(getString(R.string.vibrationSetting_maxfreq))
					.setView(view)
					.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							frequencyIndex = position;
							dialog.refresh(listData, frequencyIndex);
						}
					})
					.setPositiveButton(getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							 maxfreq.setSummary(frequency[frequencyIndex]);
						}
					}).setNegativeButton(getString(R.string.cancel),null);
				dialog.show();
				return true;
			}
		});
	}
	
	/**
	 * 加载采样点数LISTPREFERENCE
	 */
	private void initSamplePoints() {
		if (passageway.equals(getString(R.string.vibrationSetting_type_acceleration)))
			strSamplePoint = AppConst.getVibrationSamplePoints("ACC");
		else if (passageway.equals(getString(R.string.vibrationSetting_type_tempo)))
			strSamplePoint = AppConst.getVibrationSamplePoints("VEL");
		else if (passageway.equals(getString(R.string.vibrationSetting_type_displacement)))
			strSamplePoint = AppConst.getVibrationSamplePoints("DIS");
		else
			strSamplePoint = AppConst.getVibrationSamplePoints("ATT");
		samplepoint = String.valueOf(mCZPars[0]);
		
		samplepoints = (Preference) findPreference("samplepoints");
		//samplepoints.setEntries(strSamplePoint);
		//samplepoints.setEntryValues(strSamplePoint);
		samplepoints.setSummary(samplepoint);
		//samplepoints.setValueIndex(getIndex(samplepoints.getEntryValues(), samplepoint));
		samplepoints
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				final String[] sampling = (String[]) strSamplePoint;
				final List<String> listData = new ArrayList<String>();
				int checkitem=0;
				samplingIndex=0;
				for(int i=0;i<sampling.length;i++){
					listData.add(sampling[i]);
					if(sampling[i].equals(samplepoints.getSummary())){
						checkitem=i;
						samplingIndex=i;
					}
				}
				
				LayoutInflater factory = LayoutInflater
						.from(VibrationSetting.this);
				final View view = factory.inflate(R.layout.listview_layout, null);
				dialog = new com.moons.xst.track.widget.AlertDialog(VibrationSetting.this).builder()
					.setTitle(getString(R.string.vibrationSetting_samplepoints))
					.setView(view)
					.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							samplingIndex = position;
							dialog.refresh(listData, samplingIndex);
						}
					})
					.setPositiveButton(getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							samplepoints.setSummary(sampling[samplingIndex]);
						}
					}).setNegativeButton(getString(R.string.cancel),null);
				dialog.show();
				return true;
			}
		});
		
	}
	
	private void initOthers() {
		windowtype = (Preference) findPreference("windowtype");
		String wt = mCZPars[3] == 1 ? getString(R.string.vibrationSetting_windowtype_rectangle) : getString(R.string.vibrationSetting_windowtype_defaultvalue); 
		windowtype.setSummary(wt);
		//windowtype.setValueIndex(getIndex(windowtype.getEntryValues(), wt));
		windowtype
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				final String[] window = getResources().getStringArray(R.array.Vibrationwindowtype_values);
				final List<String> listData = new ArrayList<String>();
				int checkitem=0;
				for(int i=0;i<window.length;i++){
					listData.add(window[i]);
					if(window[i].equals(windowtype.getSummary())){
						checkitem=i;
					}
				}
				
				LayoutInflater factory = LayoutInflater
						.from(VibrationSetting.this);
				final View view = factory.inflate(R.layout.listview_layout, null);
				dialog = new com.moons.xst.track.widget.AlertDialog(VibrationSetting.this).builder()
					.setTitle(getString(R.string.vibrationSetting_windowtype))
					.setView(view)
					.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							windowIndex = position;
							dialog.refresh(listData, windowIndex);
						}
					})
					.setPositiveButton(getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							windowtype.setSummary(window[windowIndex]);
						}
					}).setNegativeButton(getString(R.string.cancel),null);
				dialog.show();
				return true;
			}
		});
		
		
		avgnum = (Preference) findPreference("avgnum");
		avgnums = (changeDesc(mVibPackage, true)
				 	.equalsIgnoreCase(getString(R.string.vibrationSetting_type_impact))) == true 
				 	? AppConst.SamplePoints_AVGNUM_DefaultValue 
				 			: String.valueOf(mCZPars[6]);
		//avgnums = String.valueOf(mCZPars[6]);
		avgnum.setSummary(avgnums);
		avgnum
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				final String[] average = getResources().getStringArray(R.array.Vibrationavgnum_values);
				final List<String> listData = new ArrayList<String>();
				int checkitem=0;
				for(int i=0;i<average.length;i++){
					listData.add(average[i]);
					if(average[i].equals(avgnum.getSummary())){
						checkitem=i;
					}
				}
				
				LayoutInflater factory = LayoutInflater
						.from(VibrationSetting.this);
				final View view = factory.inflate(R.layout.listview_layout, null);
				dialog = new com.moons.xst.track.widget.AlertDialog(VibrationSetting.this).builder()
					.setTitle(getString(R.string.vibrationSetting_avgnum))
					.setView(view)
					.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							averageIndex=position;
							dialog.refresh(listData, position);
							
						}
					}).setPositiveButton(getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							avgnum.setSummary(average[averageIndex]);
						}
					}).setNegativeButton(getString(R.string.cancel),null);
				dialog.show();
				return true;
			}
		});
		
		averwrap = (Preference) findPreference("averwrap");
		String aw = changeAverWrap(String.valueOf(mCZPars[7]), true);
		averwrap.setSummary(aw);
		averwrap
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				final String[] overlap = getResources().getStringArray(R.array.Vibrationaverwrap_values);
				final List<String> listData = new ArrayList<String>();
				int checkitem=0;
				for(int i=0;i<overlap.length;i++){
					listData.add(overlap[i]);
					if((overlap[i]).equals(averwrap.getSummary())){
						checkitem=i;
					}
				}
				
				LayoutInflater factory = LayoutInflater
						.from(VibrationSetting.this);
				final View view = factory.inflate(R.layout.listview_layout, null);
				dialog = new com.moons.xst.track.widget.AlertDialog(VibrationSetting.this).builder()
					.setTitle(getString(R.string.vibrationSetting_averwrap))
					.setView(view)
					.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							overlapIndex = position;
							dialog.refresh(listData, overlapIndex);
						}
					})
					.setPositiveButton(getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							averwrap.setSummary(overlap[overlapIndex]);
						}
					}).setNegativeButton(getString(R.string.cancel),null);
				dialog.show();
				return true;
			}
		});
		
		bandwidth = (Preference) findPreference("bandwidth");
		String bw = AppConst.getBandWidth(mBandWidth);
		bandwidth.setSummary(bw);
		bandwidth
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				final String[] bandwidthOptions = getResources().getStringArray(R.array.Vibrationbandwidth_options);
				final String[] bandwidthValues = getResources().getStringArray(R.array.Vibrationbandwidth_values);
				final List<String> listData = new ArrayList<String>();
				int checkitem=0;
				for(int i = 0; i < bandwidthOptions.length; i++){
					listData.add(bandwidthOptions[i]);
					if((bandwidthOptions[i]).equals(bandwidth.getSummary())){
						checkitem=i;
					}
				}
				
				LayoutInflater factory = LayoutInflater
						.from(VibrationSetting.this);
				final View view = factory.inflate(R.layout.listview_layout, null);
				dialog = new com.moons.xst.track.widget.AlertDialog(VibrationSetting.this).builder()
					.setTitle(getString(R.string.vibrationSetting_bandwidth))
					.setView(view)
					.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							bandwidthIndex = position;
							dialog.refresh(listData, bandwidthIndex);
						}
					})
					.setPositiveButton(getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							bandwidth.setSummary(bandwidthOptions[bandwidthIndex]);
							mBandWidth = bandwidthValues[bandwidthIndex];
						}
					}).setNegativeButton(getString(R.string.cancel),null);
				dialog.show();
				return true;
			}
		});
	}
	
	private String changeZDType(int type, String typeCN, boolean flag) {
		String res = "";
		if (flag) {
			if (type == 1) {
				res = getString(R.string.vibrationSetting_type_acceleration);
			} else if (type == 2) {
				res = getString(R.string.vibrationSetting_type_tempo);
			} else if (type == 4) {
				res = getString(R.string.vibrationSetting_type_displacement);
			} else {
				res = getString(R.string.vibrationSetting_type_impact);
			}
			/*switch (type){
			case 1:
				res = getString(R.string.vibrationSetting_type_acceleration);
				break;
			case 2:
				res = getString(R.string.vibrationSetting_type_tempo);
				break;
			case 4:
				res = getString(R.string.vibrationSetting_type_displacement);
				break;
			case 8:
				res = getString(R.string.vibrationSetting_type_impact);
				break;
			default:
				res = getString(R.string.vibrationSetting_type_acceleration);
				break;
			}*/
		}
		else {
			if (typeCN.equals(getString(R.string.vibrationSetting_type_acceleration)))
				res = "A|1";
			else if (typeCN.equals(getString(R.string.vibrationSetting_type_tempo)))
				res = "V|2";
			else if (typeCN.equals(getString(R.string.vibrationSetting_type_displacement)))
				res = "S|4";
			else
				res = "I|8";
		}
		
		return res;
	}
	
	private int getIndex(CharSequence[] cs, String s) {
		int result = 0;
		for (int i = 0; i < cs.length; i++) {
			if (cs[i].equals(s)) {
				result = i;
				break;
			}
		}	
		return result;
	}
	
	private String changeDesc(String cha, boolean flag) {
		String res = cha;
		if (flag) {
			if (res.contains("A")){
				res = res.replaceAll("A", getString(R.string.vibrationSetting_type_acceleration));
			}
			if (res.contains("V")) {
				res = res.replaceAll("V", getString(R.string.vibrationSetting_type_tempo));
			}
			if (res.contains("S")) {
				res = res.replaceAll("S", getString(R.string.vibrationSetting_type_displacement));
			}
			if (res.contains("I")) {
				res = res.replaceAll("I", getString(R.string.vibrationSetting_type_impact));
			}
		}
		else {
			if (res.contains(getString(R.string.vibrationSetting_type_acceleration))){
				res = res.replaceAll(getString(R.string.vibrationSetting_type_acceleration), "A");
			}
			if (res.contains(getString(R.string.vibrationSetting_type_tempo))) {
				res = res.replaceAll(getString(R.string.vibrationSetting_type_tempo), "V");
			}
			if (res.contains(getString(R.string.vibrationSetting_type_displacement))) {
				res = res.replaceAll(getString(R.string.vibrationSetting_type_displacement), "S");
			}
			if (res.contains(getString(R.string.vibrationSetting_type_impact))) {
				res = res.replaceAll(getString(R.string.vibrationSetting_type_impact), "I");
			}
		}
		
		return res;
	}
	
	private String changeAverWrap(String averWrap, boolean flag) {
		String res = "";
		if (flag) {
			if (averWrap.equals("0"))
				res = "0%";
			else if (averWrap.equals("1"))
				res = "25%";
			else if (averWrap.equals("2"))
				res = "50%";
			else
				res = "75%";
		}
		else {
			if (averWrap.equals("0%"))
				res = "0";
			else if (averWrap.equals("25%"))
				res = "1";
			else if (averWrap.equals("50%"))
				res = "2";
			else
				res = "3";
		}
		
		return res;
	}
	
	private void setBandwidthEnable(boolean b) {
		bandwidth.setEnabled(b);
		avgnum.setEnabled(!b);
	}
	
	private void Save(){
		Intent intent=new Intent();
	   
	    SaveConfig();
	    
	    //intent.putExtra("zdType", mZDType);
	    intent.putExtra("vibpackage", mVibPackage);
	    intent.putExtra("czPars", mCZPars);
	    intent.putExtra("bandwidth", mBandWidth);
	    if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer)){
			intent.putExtra("btAddress", mBTAddress);
			intent.putExtra("btPwd", mBTPwd);
	    }
	    setResult(RESULT_OK,intent);
	}
	
	/**
	 * 保存配置信息
	 */
	private void SaveConfig()
	{
		SaveVibrationBaseInfo();
		if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer))
			SaveBluetoothInfo();
	}
	
	private void SaveVibrationBaseInfo(){
		/* 获取更改后的参数 */
		//测量包
		mVibPackage = changeDesc(vibrationpackage.getSummary().toString(),false);
		//采样点数
		mCZPars[0] = StringUtils.toInt((samplepoints.getSummary()));
		//最大频率
		mCZPars[1] = StringUtils.toInt(maxfreq.getSummary()
				.subSequence(0, maxfreq.getSummary().length() - 2));
		//频谱线数
		mCZPars[2] = (int)(StringUtils.toInt(samplepoints.getSummary()) / 2.56);
		//窗类型
		mCZPars[3] = windowtype.getSummary().toString().equals(getString(R.string.vibrationSetting_windowtype_rectangle)) ? 1 : 2;
		//信号类型
		String tempstr = changeZDType(0, vibrationpassageway.getSummary().toString(), false);
		mCZPars[5] = StringUtils.toInt((tempstr.split("\\|",2)[1]));
		//平均次数
		mCZPars[6] = StringUtils.toInt(avgnum.getSummary().toString());
		//重叠率
		mCZPars[7] = StringUtils.toInt(changeAverWrap(averwrap.getSummary().toString(), false));
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.VibrationConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			boolean hasBandWidth = false;
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
                if (element.getAttribute("Name").equals("vibrationPackage")) {
                	element.setAttribute("Using", mVibPackage);
				} else if (element.getAttribute("Name").equals("samplePoints")) {
					element.setAttribute("Using", String.valueOf(mCZPars[0]));
				} else if (element.getAttribute("Name").equals("maxAnaFreq")) {
					element.setAttribute("Using", String.valueOf(mCZPars[1]));
				} else if (element.getAttribute("Name").equals("fftLines")) {
					element.setAttribute("Using", String.valueOf(mCZPars[2]));
				} else if (element.getAttribute("Name").equals("windowType")) {
					element.setAttribute("Using", String.valueOf(mCZPars[3]));
				} else if (element.getAttribute("Name").equals("signalType")) {
					element.setAttribute("Using", String.valueOf(tempstr.split("\\|",2)[0]));
				} else if (element.getAttribute("Name").equals("averNum")) {
					element.setAttribute("Using", String.valueOf(mCZPars[6]));
				} else if (element.getAttribute("Name").equals("averWrap")) {
					element.setAttribute("Using", String.valueOf(mCZPars[7]));
				} else if (element.getAttribute("Name").equals("bandWidth")) {
					element.setAttribute("Using", mBandWidth);
					hasBandWidth = true;
				}
			}
			
			if (!hasBandWidth) {
				Element eltName = doc.createElement("Setting");	             
	            Attr attr = doc.createAttribute("Name");
	            attr.setValue("bandWidth");
	            Attr attr2 = doc.createAttribute("Using");
	            attr2.setValue(mBandWidth);
	            eltName.setAttributeNode(attr);
	            eltName.setAttributeNode(attr2);
				nodes.item(nodes.getLength() - 1).appendChild(eltName);
			}
			
			//保存xml文件
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			javax.xml.transform.Transformer transformer=transformerFactory.newTransformer();
			DOMSource domSource=new DOMSource(doc);
			//设置编码类型
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			FileOutputStream fs = new FileOutputStream(AppConst.VibrationConfigPath());
			StreamResult result=new StreamResult(fs);
			//把DOM树转换为xml文件
			transformer.transform(domSource, result);
			fs.close();
			}	
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void SaveBluetoothInfo(){
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
			
			//保存xml文件
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			javax.xml.transform.Transformer transformer=transformerFactory.newTransformer();
			DOMSource domSource=new DOMSource(doc);
			//设置编码类型
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			StreamResult result=new StreamResult(new FileOutputStream(AppConst.MeasureTypeForOuterConfigPath()));
			//把DOM树转换为xml文件
			transformer.transform(domSource, result);
			
			AppContext.setBlueToothAddressforVibration(mBTAddress);
			AppContext.setBTConnectPwdforTemperature(mBTPwd);
			result.getOutputStream().close();
			}	
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkInput() {
		if (vibpackage.equals(getString(R.string.vibrationSetting_type_impact)) && 
				((maxfreq.getSummary().equals("100Hz") && samplepoints.getSummary().equals("2048"))
				|| (maxfreq.getSummary().equals("200Hz") && samplepoints.getSummary().equals("2048"))
				|| (maxfreq.getSummary().equals("500Hz") && samplepoints.getSummary().equals("256"))
				|| (maxfreq.getSummary().equals("1000Hz") && samplepoints.getSummary().equals("256"))
				|| (maxfreq.getSummary().equals("1000Hz") && samplepoints.getSummary().equals("512"))))
			return false;
		
		return true;
	}
}
