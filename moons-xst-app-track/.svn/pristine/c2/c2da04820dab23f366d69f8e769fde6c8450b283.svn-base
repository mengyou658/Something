package com.moons.xst.track.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.User;
import com.moons.xst.track.common.AppResourceUtils;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.MethodsCompat;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.ActionSheetDialog;
import com.moons.xst.track.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.moons.xst.track.widget.ActionSheetDialog.SheetItemColor;

public class Setting extends PreferenceActivity {

	SharedPreferences mPreferences;
	Preference account;
	Preference cache;
	// Preference feedback;
	// Preference update;
	// Preference about;
	Preference changeDate;
	Preference changeTime;
	// Preference saveImagePath;

	Preference jituploadsetting;
	Preference gesturesetting;
	Preference fontsizesettings;
	Preference showsetting;

	Preference videoqualitysetting;
	Preference gps_settings;
	// Preference gps_settings1;
	Preference webServiceAddressForOther;
	Preference webServiceAddress;
	Preference webServiceAddressForWlan;
	Preference xjqCD;
	Preference communicationType;
	Preference planType;
	Preference updateSysDate;
	Preference measureType;
	Preference hisDataSettings;

	CheckBoxPreference httpslogin;
	CheckBoxPreference loadimage;
	CheckBoxPreference scroll;
	CheckBoxPreference voice;
	CheckBoxPreference lastresult;
	CheckBoxPreference checkup;
	CheckBoxPreference uploadFile;
	CheckBoxPreference openWakeLock;

	CheckBoxPreference ctrlgps;

	Button scanButton;
	ImageButton returnButton;
	// private Boolean TwoBill = false;
	private final static String CLEAN_LOADUSERDJLINE = "CLEANLOADUSERDJLINE";
	private final static int REQUEST_CODE = 1;
	private final static int REQUEST_CODE_Wlan = 1;
	private final static int REQUEST_CODE_Other = 1;
	private String checkedSaveDays;
	private String hisDataSavedDays;
	EditText edit;
	EditText webserviceEditTextForWlan;
	EditText webserviceEditTextForOther;

	private static int communicationIndex;
	private static int plantypeIndex;
	private static int measureIndex;
	private static int updateSysIndex;

	private com.moons.xst.track.widget.AlertDialog dialog;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

		// 设置显示Preferences
		addPreferencesFromResource(R.xml.preferences);
		// 获得SharedPreferences
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		ListView localListView = getListView();
		localListView.setBackgroundColor(0);
		localListView.setCacheColorHint(0);
		((ViewGroup) localListView.getParent()).removeView(localListView);
		ViewGroup localViewGroup = (ViewGroup) getLayoutInflater().inflate(
				R.layout.setting, null);
		((ViewGroup) localViewGroup.findViewById(R.id.setting_content))
				.addView(localListView, -1, -1);
		setContentView(localViewGroup);
		final AppContext ac = (AppContext) getApplication();
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(Setting.this);
			}
		});
		registerBoradcastReceiver();
		webServiceAddress = (Preference) findPreference("webServiceAddress");
		webServiceAddress.setSummary(AppContext.getWebServiceAddress());
		webServiceAddress
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						LayoutInflater factory = LayoutInflater
								.from(Setting.this);
						final View view = factory.inflate(
								R.layout.editbox_layout, null);
						edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
						edit.setText(webServiceAddress.getSummary());
						new com.moons.xst.track.widget.AlertDialog(Setting.this)
								.builder()
								.setTitle(
										getString(R.string.setting_head_title_comm_webaddr))
								.setEditView(view, InputType.TYPE_CLASS_TEXT,
										"")
								.setPositiveButton(
										getString(R.string.sure),
										new android.view.View.OnClickListener() {
											@Override
											public void onClick(View v) {
												String WEBAddress = String
														.valueOf(edit.getText());
												webServiceAddress.setSummary(String
														.valueOf(WEBAddress));
												ac.setConfigWSAddress(WEBAddress);
												String wlanString = AppContext
														.getWebServiceAddressForWlan();
												AppContext.SetWSAddress(String
														.valueOf(WEBAddress),
														wlanString);
											}
										})
								.setNegativeButton(
										getString(R.string.cancel),
										new android.view.View.OnClickListener() {
											@Override
											public void onClick(View v) {
											}
										}).show();

						edit.setOnLongClickListener(new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								Intent intent = new Intent();
								intent.setClass(Setting.this,
										CaptureActivity.class);
								intent.putExtra("ScanType", "EDITTEXT");
								startActivityForResult(intent, REQUEST_CODE);
								return true;
							}
						});
						return true;
					}
				});

		webServiceAddressForWlan = (Preference) findPreference("webServiceAddressForWlan");
		webServiceAddressForWlan.setSummary(AppContext
				.getWebServiceAddressForWlan());
		webServiceAddressForWlan
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						LayoutInflater factory = LayoutInflater
								.from(Setting.this);
						final View view = factory.inflate(
								R.layout.editbox_layout, null);
						webserviceEditTextForWlan = (EditText) view
								.findViewById(R.id.editText);// 获得输入框对象
						webserviceEditTextForWlan
								.setText(webServiceAddressForWlan.getSummary());
						new com.moons.xst.track.widget.AlertDialog(Setting.this)
								.builder()
								.setTitle(
										getString(R.string.setting_head_title_comm_wlan_webaddr))
								.setEditView(view, InputType.TYPE_CLASS_TEXT,
										"")
								.setPositiveButton(
										getString(R.string.sure),
										new android.view.View.OnClickListener() {
											@Override
											public void onClick(View v) {
												String identifier = String
														.valueOf(webserviceEditTextForWlan
																.getText());
												webServiceAddressForWlan
														.setSummary(identifier);
												ac.setConfigWSAddressForWlan(identifier);
												String lanString = AppContext
														.getWebServiceAddress();
												AppContext.SetWSAddress(
														lanString, identifier);
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null).show();

						webserviceEditTextForWlan
								.setOnLongClickListener(new OnLongClickListener() {

									@Override
									public boolean onLongClick(View v) {
										Intent intent = new Intent();
										intent.setClass(Setting.this,
												CaptureActivity.class);
										intent.putExtra("ScanType", "EDITTEXT");
										startActivityForResult(intent,
												REQUEST_CODE_Wlan);
										return true;
									}
								});
						return true;
					}
				});

		webServiceAddressForOther = (Preference) findPreference("webServiceAddressForOther");
		webServiceAddressForOther.setSummary(ac.getWebServiceAddressForOther());
		webServiceAddressForOther
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						LayoutInflater factory = LayoutInflater
								.from(Setting.this);
						final View view = factory.inflate(
								R.layout.editbox_layout, null);
						webserviceEditTextForOther = (EditText) view
								.findViewById(R.id.editText);// 获得输入框对象
						webserviceEditTextForOther
								.setText(webServiceAddressForOther.getSummary());
						new com.moons.xst.track.widget.AlertDialog(Setting.this)
								.builder()
								.setTitle(
										getString(R.string.setting_head_title_comm_other_webaddr))
								.setEditView(view, InputType.TYPE_CLASS_TEXT,
										"")
								.setPositiveButton(
										getString(R.string.sure),
										new android.view.View.OnClickListener() {
											@Override
											public void onClick(View v) {
												String identifier = String
														.valueOf(webserviceEditTextForOther
																.getText());
												webServiceAddressForOther
														.setSummary(identifier);
												ac.setConfigWSAddressForOther(identifier);
												String lanString = AppContext
														.getWebServiceAddress();
												AppContext.SetWSAddress(
														lanString, identifier);
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null).show();

						webserviceEditTextForOther
								.setOnLongClickListener(new OnLongClickListener() {
									@Override
									public boolean onLongClick(View v) {
										Intent intent = new Intent();
										intent.setClass(Setting.this,
												CaptureActivity.class);
										intent.putExtra("ScanType", "EDITTEXT");
										startActivityForResult(intent,
												REQUEST_CODE_Other);
										return true;
									}
								});
						return true;
					}
				});

		xjqCD = (Preference) findPreference("xjqCD");
		xjqCD.setSummary(AppContext.getxjqCD());
		xjqCD.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				LayoutInflater factory = LayoutInflater.from(Setting.this);
				final View view = factory
						.inflate(R.layout.editbox_layout, null);
				final EditText edit = (EditText) view
						.findViewById(R.id.editText);// 获得输入框对象
				
				String xjqcdLen = AppResourceUtils.getValue(Setting.this,
						getString(R.string.app_name), "xjqcdlength");
				if (StringUtils.isEmpty(xjqcdLen))
					edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
				else {
					int len = Integer.parseInt(xjqcdLen);
					edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(len)});
				}
				
				//edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
				edit.setText(xjqCD.getSummary());
				new com.moons.xst.track.widget.AlertDialog(Setting.this)
						.builder()
						.setTitle(
								getString(R.string.setting_head_title_comm_xjqcd))
						.setEditView(view, InputType.TYPE_CLASS_TEXT, "")
						.setPositiveButton(getString(R.string.sure),
								new android.view.View.OnClickListener() {
									@Override
									public void onClick(View v) {
										String identifier = String.valueOf(edit
												.getText());
										/*if (!AppContext.getxjqCD().equals(
												identifier)) {
											AppContext.setxjqCDIsAlter(true);
											Main_Page.instance()
													.sendBroadcast();
										}*/
										xjqCD.setSummary(identifier);
										ac.setConfigXJQCD(identifier);
										AppContext.SetxjqCD(identifier);
									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new android.view.View.OnClickListener() {
									@Override
									public void onClick(View v) {
									}
								}).show();
				return true;
			}
		});

		
		communicationType = (Preference) findPreference("communicationtype");
		String commuType = "";

		if (AppContext.getCommunicationType().equalsIgnoreCase(
				AppConst.CommunicationType_Wireless)) {
			commuType = getString(R.string.commu_type_wireless);
		} else if (AppContext.getCommunicationType().equalsIgnoreCase(
				AppConst.CommunicationType_USB)) {
			commuType = getString(R.string.commu_type_usb);
		}
		communicationType.setSummary(commuType);
		communicationType
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						final String[] communication = getResources()
								.getStringArray(
										R.array.communicationtype_values);
						final List<String> listData = new ArrayList<String>();
						int checkitem = 0;
						for (int i = 0; i < communication.length; i++) {
							listData.add(communication[i]);
							if (communication[i].equals(communicationType
									.getSummary())) {
								checkitem = i;
							}
						}

						LayoutInflater factory = LayoutInflater
								.from(Setting.this);
						final View view = factory.inflate(
								R.layout.listview_layout, null);
						dialog = new com.moons.xst.track.widget.AlertDialog(
								Setting.this)
								.builder()
								.setTitle(
										getString(R.string.setting_head_title_sys_communicationtype))
								.setView(view)
								.setListViewItems(listData, checkitem,
										new AdapterView.OnItemClickListener() {
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												communicationIndex = position;
												dialog.refresh(listData,
														communicationIndex);
											}
										})
								.setPositiveButton(getString(R.string.sure),
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												communicationType
														.setSummary(communication[communicationIndex]);
												ac.setConfigCommunicationType(String
														.valueOf(communicationIndex));
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null);
						dialog.show();
						return true;
					}
				});
		
		planType = (Preference) findPreference("plantype");
		String plantype = "";

		if (AppContext.getPlanType().equalsIgnoreCase(
				AppConst.PlanType_XDJ)) {
			plantype = getString(R.string.plantype_xdj);
		} else if (AppContext.getPlanType().equalsIgnoreCase(
				AppConst.PlanType_DJPC)) {
			plantype = getString(R.string.plantype_djpc);
		}
		planType.setSummary(plantype);
		planType
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						final String[] plantypes = getResources()
								.getStringArray(
										R.array.plan_type);
						final List<String> listData = new ArrayList<String>();
						int checkitem = 0;
						for (int i = 0; i < plantypes.length; i++) {
							listData.add(plantypes[i]);
							if (plantypes[i].equals(planType
									.getSummary())) {
								checkitem = i;
							}
						}

						LayoutInflater factory = LayoutInflater
								.from(Setting.this);
						final View view = factory.inflate(
								R.layout.listview_layout, null);
						dialog = new com.moons.xst.track.widget.AlertDialog(
								Setting.this)
								.builder()
								.setTitle(
										getString(R.string.setting_head_title_sys_plantype))
								.setView(view)
								.setListViewItems(listData, checkitem,
										new AdapterView.OnItemClickListener() {
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												plantypeIndex = position;
												dialog.refresh(listData,
														plantypeIndex);
											}
										})
								.setPositiveButton(getString(R.string.sure),
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												planType
														.setSummary(plantypes[plantypeIndex]);
												ac.setConfigPlanType(String
														.valueOf(plantypeIndex));
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null);
						dialog.show();
						return true;
					}
				});

		// 左右滑动
		/*
		 * scroll = (CheckBoxPreference) findPreference("scroll");
		 * scroll.setChecked(ac.isScroll()); if (ac.isScroll()) {
		 * scroll.setSummary(this
		 * .getString(R.string.setting_function_scroll_on)); } else {
		 * scroll.setSummary(this
		 * .getString(R.string.setting_function_scroll_off)); }
		 * scroll.setOnPreferenceClickListener(new
		 * Preference.OnPreferenceClickListener() { public boolean
		 * onPreferenceClick(Preference preference) {
		 * ac.setConfigScroll(scroll.isChecked()); if (scroll.isChecked()) {
		 * scroll.setSummary(getString(R.string.setting_function_scroll_on)); }
		 * else {
		 * scroll.setSummary(getString(R.string.setting_function_scroll_off)); }
		 * return true; } });
		 */

		// 提示声音
		voice = (CheckBoxPreference) findPreference("voice");
		voice.setChecked(AppContext.getVoice());
		if (AppContext.getVoice()) {
			voice.setSummary(this.getString(R.string.setting_function_voice_on));
		} else {
			voice.setSummary(this
					.getString(R.string.setting_function_voice_off));
		}
		voice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigVoice(voice.isChecked());
				if (voice.isChecked()) {
					voice.setSummary(getString(R.string.setting_function_voice_on));
				} else {
					voice.setSummary(getString(R.string.setting_function_voice_off));
				}
				return true;
			}
		});

		lastresult = (CheckBoxPreference) findPreference("lastresult");
		// 上次结果
		lastresult.setChecked(AppContext.getLastResult());
		lastresult
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						ac.setConfigLastResult(lastresult.isChecked());
						return true;
					}
				});

		// 启动检查更新
		checkup = (CheckBoxPreference) findPreference("checkup");
		checkup.setChecked(AppContext.getCheckUp());
		checkup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigCheckUp(checkup.isChecked());
				return true;
			}
		});
		// 启动文件后台上传
		uploadFile = (CheckBoxPreference) findPreference("uploadfile");
		uploadFile.setChecked(AppContext.getUploadFile());
		uploadFile
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						ac.setConfigUploadFile(uploadFile.isChecked());
						return true;
					}
				});

		// 启动休眠锁
		openWakeLock = (CheckBoxPreference) findPreference("openWakeLock");
		openWakeLock.setChecked(AppContext.getOpenWakeLock());
		if (AppContext.getOpenWakeLock())
			openWakeLock
					.setSummary(this
							.getString(R.string.setting_head_title_function_openwakelock));
		else
			openWakeLock
					.setSummary(this
							.getString(R.string.setting_head_title_function_closewakelock));
		openWakeLock
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						// 设置为true后返回主页面时刷新休眠锁
						AppContext.setRefreshWakeLock(true);
						ac.setConfigWakeLock(openWakeLock.isChecked());
						if (AppContext.getOpenWakeLock())
							openWakeLock
									.setSummary(getString(R.string.setting_head_title_function_openwakelock));
						else
							openWakeLock
									.setSummary(getString(R.string.setting_head_title_function_closewakelock));
						return true;
					}
				});

		// 实时上传设置
		jituploadsetting = (Preference) findPreference("jitupload_settings");
		jituploadsetting
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(Setting.this,
								JITUploadAty.class);
						startActivity(intent);
						return true;
					}
				});

		// 节电设置

		// powersave = (Preference) findPreference("powersave_settings");
		// powersave.setOnPreferenceClickListener(new
		// Preference.OnPreferenceClickListener() {
		//
		// @Override
		// public boolean onPreferenceClick(Preference preference) {
		// // TODO 自动生成的方法存根
		// Intent intent = new Intent(Setting.this,PowerSaveSettingAty.class);
		// startActivity(intent);
		// return true;
		// }
		// });

		// 手势密码设置
		gesturesetting = (Preference) findPreference("gesture_settings");
		gesturesetting
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(Setting.this,
								GestureSettingAty.class);
						startActivity(intent);
						return true;
					}
				});
		// 字体大小设置
		fontsizesettings = (Preference) findPreference("fontsize_settings");
		fontsizesettings
		.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(Setting.this,
						FontSizeSettingAty.class);
				startActivity(intent);
				return true;
			}
		});

		// 显示操作设置

		/*
		 * showsetting = findPreference("show_settings"); showsetting
		 * .setOnPreferenceClickListener(new
		 * Preference.OnPreferenceClickListener() {
		 * 
		 * @Override public boolean onPreferenceClick(Preference preference) {
		 * // TODO 自动生成的方法存根 Intent intent = new Intent(Setting.this,
		 * ShowSettingAty.class); intent.putExtra("TwoBil", TwoBill);
		 * startActivity(intent); return true; } });
		 */

		showsetting = findPreference("show_settings");
		showsetting
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(Setting.this,
								ShowSettingAty.class);
						intent.putExtra("TwoBil", AppContext.getTwoBillYN());
						intent.putExtra("Overhaul", AppContext.getOverhaulYN());
						startActivity(intent);
						return true;
					}
				});

		// 视屏录制设置
		videoqualitysetting = (Preference) findPreference("video_settings");
		videoqualitysetting
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(Setting.this,
								VideoQualitySettingAty.class);
						startActivity(intent);
						return true;
					}
				});

		// 测量模块接入的方式(内置/外接)
		measureType = (Preference) findPreference("measuretype");

		String measureTypeStr = "";
		String mType = "";
		if (AppContext.getMeasureType().equalsIgnoreCase(
				AppConst.MeasureType_Inner)) {
			mType = getString(R.string.seting_sys_measuretype_defaultvalue);
			measureTypeStr = getString(R.string.seting_sys_measuretype_defaultvalue);
			measureIndex = Integer.parseInt(AppConst.MeasureType_Inner);
		} else if (AppContext.getMeasureType().equalsIgnoreCase(
				AppConst.MeasureType_Outer)) {
			mType = getString(R.string.seting_sys_measuretype_circumscribedvalue);
			measureTypeStr = getString(R.string.seting_sys_measuretype_circumscribedvalue)
					+ "(" + AppContext.getOuterMeasureType() + ")";
			measureIndex = Integer.parseInt(AppConst.MeasureType_Outer);
		}
		measureType.setSummary(measureTypeStr);
		measureType.setKey(mType);
		measureType
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						final String[] measure = getResources().getStringArray(
								R.array.measuretype_values);
						final List<String> listData = new ArrayList<String>();
						int checkitem = 0;
						for (int i = 0; i < measure.length; i++) {
							listData.add(measure[i]);
							if (measure[i].equals(measureType.getKey())) {
								checkitem = i;
							}
						}

						LayoutInflater factory = LayoutInflater
								.from(Setting.this);
						final View view = factory.inflate(
								R.layout.listview_layout, null);
						dialog = new com.moons.xst.track.widget.AlertDialog(
								Setting.this)
								.builder()
								.setTitle(
										getString(R.string.setting_head_title_sys_measuretype))
								.setView(view)
								.setListViewItems(listData, checkitem,
										new AdapterView.OnItemClickListener() {
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												measureIndex = position;
												dialog.refresh(listData,
														measureIndex);
											}
										})
								.setPositiveButton(getString(R.string.sure),
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												// 内置
												if (measure[measureIndex]
														.equalsIgnoreCase(measure[Integer
																.parseInt(AppConst.MeasureType_Inner)])) {
													measureType
															.setSummary(measure[measureIndex]);
													measureType
															.setKey(measure[measureIndex]);
													ac.setConfigMeasureType(String
															.valueOf(measureIndex));
												} else {
													final List<String> outerMeasureTypes = new ArrayList<String>();
													for (int i = 0; i < getResources()
															.getStringArray(
																	R.array.outer_measuretypes).length; i++) {
														outerMeasureTypes
																.add(getResources()
																		.getStringArray(
																				R.array.outer_measuretypes)[i]
																		.toString());
													}
													new ActionSheetDialog(
															Setting.this)
															.builder()
															.setCancelable(
																	false)
															.setCanceledOnTouchOutside(
																	true)
															.addSheetItems(
																	outerMeasureTypes,
																	SheetItemColor.Blue,
																	new OnSheetItemClickListener() {
																		@Override
																		public void onClick(
																				int which) {
																			measureType
																					.setSummary(measure[measureIndex]
																							+ "("
																							+ outerMeasureTypes
																									.get(which - 1)
																							+ ")");
																			measureType
																					.setKey(measure[measureIndex]);
																			ac.setConfigMeasureType(String
																					.valueOf(measureIndex));
																			ac.setConfigOuterMeasureType(outerMeasureTypes
																					.get(which - 1));
																		}
																	}).show();
												}
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null);
						dialog.show();
						return true;
					}
				});

		// 自动校准时间的类型（不校准/GPS方式/WEB方式）
		updateSysDate = (Preference) findPreference("updatesysdatetimetype");
		String updateType = "";
		if (AppContext.getUpdateSysDateType().equalsIgnoreCase(
				AppConst.UpdateSysDate_NONE))
			updateType = getString(R.string.setting_function_nocorrect);
		else if (AppContext.getUpdateSysDateType().equalsIgnoreCase(
				AppConst.UpdateSysDate_Commu))
			updateType = getString(R.string.setting_function_commucorrect);
		/*else if (AppContext.getUpdateSysDateType().equalsIgnoreCase(
				AppConst.UpdateSysDate_WEB))
			updateType = getString(R.string.setting_function_webcorrect);*/
		updateSysDate.setSummary(updateType);
		updateSysDate
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						final String[] updateSysType = getResources()
								.getStringArray(
										R.array.updatesysdatetime_values);
						final List<String> listData = new ArrayList<String>();
						int checkitem = 0;
						for (int i = 0; i < updateSysType.length; i++) {
							listData.add(updateSysType[i]);
							if (updateSysType[i].equals(updateSysDate
									.getSummary())) {
								checkitem = i;
								updateSysIndex = i;
							}
						}

						LayoutInflater factory = LayoutInflater
								.from(Setting.this);
						final View view = factory.inflate(
								R.layout.listview_layout, null);
						dialog = new com.moons.xst.track.widget.AlertDialog(
								Setting.this)
								.builder()
								.setTitle(
										getString(R.string.setting_head_title_sys_updatetimetype))
								.setView(view)
								.setListViewItems(listData, checkitem,
										new AdapterView.OnItemClickListener() {
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												updateSysIndex = position;
												dialog.refresh(listData,
														updateSysIndex);
											}
										})
								.setPositiveButton(getString(R.string.sure),
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												updateSysDate
														.setSummary(updateSysType[updateSysIndex]);
												ac.setConfigUpdateSysDate(String
														.valueOf(updateSysIndex));
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null);
						dialog.show();
						return true;
					}
				});

		// 修改系统日期
		changeDate = findPreference("changedate");
		changeDate.setSummary(DateTimeHelper.getDateNow());
		changeDate
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						// todo
						Calendar calendar = Calendar.getInstance(Locale.CHINA);
						new DatePickerDialog(Setting.this,
								new OnDateSetListener() {
									String mYear, mMonth, mDay;

									@Override
									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {
										// TODO 自动生成的方法存根
										mYear = String.valueOf(year);
										mMonth = monthOfYear + 1 < 10 ? "0"
												+ String.valueOf((monthOfYear + 1))
												: String.valueOf((monthOfYear + 1));
										mDay = dayOfMonth < 10 ? "0"
												+ String.valueOf(dayOfMonth)
												: String.valueOf(dayOfMonth);
										String currDateString = mYear + "-"
												+ mMonth + "-" + mDay;
										changeDate.setSummary(currDateString);
										String currDateTimeString = currDateString
												+ " "
												+ DateTimeHelper.getTimeNow1();
										sendUpdateSysTime(DateTimeHelper
												.StringToDate(
														currDateTimeString)
												.getTime());
									}
								}, calendar.get(Calendar.YEAR), calendar
										.get(Calendar.MONTH), calendar
										.get(Calendar.DAY_OF_MONTH)).show();
						return true;
					}
				});

		hisDataSettings = findPreference("hisdatasettings");
		hisDataSavedDays = AppContext.getHisDataSavedDays();
		hisDataSettings.setSummary(getString(
				R.string.main_menu_system_hisdatasaved_unit, hisDataSavedDays));
		hisDataSettings
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						LayoutInflater inflater = (LayoutInflater) Setting.this
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View his_data_saved_setting = inflater.inflate(
								R.layout.his_data_saved_setting, null);
						com.moons.xst.track.widget.WheelView.WheelView wheelView = (com.moons.xst.track.widget.WheelView.WheelView) his_data_saved_setting
								.findViewById(R.id.sys_hisdatasaved_setting);
						wheelView
								.setAdapter(new com.moons.xst.track.widget.WheelView.NumericWheelAdapter(
										1, 100));
						hisDataSavedDays = AppContext.getHisDataSavedDays();
						wheelView.setCurrentItem(Integer
								.parseInt(hisDataSavedDays));

						wheelView.addChangingListener(changedListener);
						wheelView.addScrollingListener(scrolledListener);
						wheelView.setCyclic(true);
						final com.moons.xst.track.widget.AlertDialog builder = new com.moons.xst.track.widget.AlertDialog(
								Setting.this).builder();
						builder.setTitle(
								getString(R.string.setting_sys_hisdatasaveddays_diatitle))
								.setView(his_data_saved_setting)
								.setCancelable(false)
								.setPositiveButton(getString(R.string.sure),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												builder.setCancelable(true);
												if (!wheelScrolled
														&& (checkedSaveDays == null || Integer
																.parseInt(checkedSaveDays) == 0)) {
													ac.setConfigHisDataSavedDays(hisDataSavedDays);
													hisDataSettings
															.setSummary(getString(
																	R.string.main_menu_system_hisdatasaved_unit,
																	hisDataSavedDays));
												} else {
													ac.setConfigHisDataSavedDays(checkedSaveDays);
													hisDataSettings
															.setSummary(getString(
																	R.string.main_menu_system_hisdatasaved_unit,
																	checkedSaveDays));
												}
											}
										})
								.setNegativeButton(getString(R.string.cancel),
										null).show();
						return true;
					}

				});
		//

		// 修改系统时间
		changeTime = findPreference("changetime");
		changeTime.setSummary(DateTimeHelper.getTimeNow());
		changeTime
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {

						Calendar calendar = Calendar.getInstance(Locale.CHINA);
						new TimePickerDialog(Setting.this,
								new OnTimeSetListener() {
									String mHour, mMinute;

									@Override
									public void onTimeSet(TimePicker view,
											int hourOfDay, int minute) {
										mHour = hourOfDay < 10 ? "0"
												+ String.valueOf(hourOfDay)
												: String.valueOf(hourOfDay);
										mMinute = minute < 10 ? "0"
												+ String.valueOf(minute)
												: String.valueOf(minute);
										String currTimeString = mHour + ":"
												+ mMinute;
										changeTime.setSummary(currTimeString);
										String currDateTimeString = DateTimeHelper
												.getDateNow()
												+ " "
												+ currTimeString + ":01";
										sendUpdateSysTime(DateTimeHelper
												.StringToDate(
														currDateTimeString)
												.getTime());
									}
								}, calendar.get(Calendar.HOUR_OF_DAY), calendar
										.get(Calendar.MINUTE), true).show();

						return true;
					}
				});

		// 检查GPS是否开启
		ctrlgps = (CheckBoxPreference) findPreference("ctrlgps");
		ctrlgps.setChecked(AppContext.getGPSOpen());
		if (AppContext.getGPSOpen()) {
			ctrlgps.setSummary(this.getString(R.string.setting_sys_gps_on));
		} else {
			ctrlgps.setSummary(this.getString(R.string.setting_sys_gps_off));
		}
		ctrlgps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigGps(ctrlgps.isChecked());
				if (ctrlgps.isChecked()) {
					ctrlgps.setSummary(getString(R.string.setting_sys_gps_on));
					UIHelper.ToastMessage(Setting.this,
							R.string.setting_sys_gpsisopen_isopenpromptinfo);
				} else {
					ctrlgps.setSummary(getString(R.string.setting_sys_gps_off));
					UIHelper.ToastMessage(Setting.this,
							R.string.setting_sys_gpsisopen_isclosedpromptinfo);
				}
				return true;
			}
		});
		gps_settings = findPreference("gpssettings");
		gps_settings
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(Setting.this,
								BaiduLocationAty.class);
						startActivity(intent);
						return true;
					}
				});
		// 计算缓存大小
		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = getFilesDir();
		File cacheDir = getCacheDir();

		long fileListSize = UIHelper.getFileListSize();

		fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);
		// 2.2版本才有将应用缓存转移到sd卡的功能
		if (AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			File externalCacheDir = MethodsCompat.getExternalCacheDir(this);
			fileSize += FileUtils.getDirSize(externalCacheDir);
		}
		if (fileSize + fileListSize == 0) {
			cacheSize = "0KB";
		} else {
			cacheSize = FileUtils.formatFileSize(fileSize + fileListSize);
		}
		// 清除缓存 删除sd卡中的相关文件夹
		cache = (Preference) findPreference("cache");
		cache.setSummary(cacheSize);
		cache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				CharSequence summary = cache.getSummary();
				if (!"0KB".equals(summary)) {
					LayoutInflater factory = LayoutInflater.from(Setting.this);
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					new com.moons.xst.track.widget.AlertDialog(Setting.this)
							.builder()
							.setTitle(
									getString(R.string.setting_function_cleardatatitle))
							.setView(view)
							.setMsg(getString(R.string.setting_function_surecleardata))
							.setPositiveButton(getString(R.string.sure),
									new android.view.View.OnClickListener() {
										@Override
										public void onClick(View v) {
											UIHelper.deleteFile();
											Intent loadUserDJLineIntent = new Intent();
											loadUserDJLineIntent
													.setAction(CLEAN_LOADUSERDJLINE);
											Setting.this
													.sendBroadcast(loadUserDJLineIntent);
											UIHelper.clearAppCache(Setting.this);
											cache.setSummary("0KB");

										}
									})
							.setNegativeButton(getString(R.string.cancel),
									new android.view.View.OnClickListener() {
										@Override
										public void onClick(View v) {
										}
									}).show();

				}
				return true;
			}
		});
		setEnable();
	}

	private boolean wheelScrolled = false;

	com.moons.xst.track.widget.WheelView.OnWheelScrollListener scrolledListener = new com.moons.xst.track.widget.WheelView.OnWheelScrollListener() {
		public void onScrollingStarted(
				com.moons.xst.track.widget.WheelView.WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(
				com.moons.xst.track.widget.WheelView.WheelView wheel) {
			wheelScrolled = false;
			int currentItem = wheel.getCurrentItem();
			checkedSaveDays = String.valueOf(currentItem);
			// Toast.makeText(Setting.this, ""+currentItem,
			// Toast.LENGTH_SHORT).show();
		}
	};

	private com.moons.xst.track.widget.WheelView.OnWheelChangedListener changedListener = new com.moons.xst.track.widget.WheelView.OnWheelChangedListener() {
		public void onChanged(
				com.moons.xst.track.widget.WheelView.WheelView wheel,
				int oldValue, int newValue) {
			// if (!wheelScrolled) {
			//
			// }
		}
	};

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (intent.getBooleanExtra("LOGIN", false)) {
			account.setTitle(R.string.main_menu_logout);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterBoradcastReceiver();
		// 结束Activity&从堆栈中移除
		//AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AppManager.getAppManager().finishActivity(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String codeReust = bundle.getString("codeResult");
				if (edit != null)
					edit.setText(codeReust);
			}
		}
		if (requestCode == REQUEST_CODE_Wlan) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String codeReust = bundle.getString("codeResult");
				if (webserviceEditTextForWlan != null)
					webserviceEditTextForWlan.setText(codeReust);
			}
		}
		if (requestCode == REQUEST_CODE_Other) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String codeReust = bundle.getString("codeResult");
				if (webserviceEditTextForOther != null)
					webserviceEditTextForOther.setText(codeReust);
			}
		}

	}

	private void setEnable() {
		AppContext appcontext = (AppContext) getApplication();
		if (AppContext.isLogin()) {
			User loginUser = appcontext.getLoginInfo();
			if (loginUser.getUserAccess().equals("")) {
				loadSettingXml();
			}
		} else {
			voice.setEnabled(false);
			lastresult.setEnabled(false);
			checkup.setEnabled(false);
			uploadFile.setEnabled(false);
			openWakeLock.setEnabled(false);
			jituploadsetting.setEnabled(false);
			gesturesetting.setEnabled(false);
			videoqualitysetting.setEnabled(false);
			cache.setEnabled(false);
			updateSysDate.setEnabled(false);
			changeDate.setEnabled(false);
			changeTime.setEnabled(false);
			ctrlgps.setEnabled(false);
			gps_settings.setEnabled(false);
			hisDataSettings.setEnabled(false);
			fontsizesettings.setEnabled(false);
		}
		/*// 读取两票模块是否可见
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.TwoBill.toString()).LoadConfigByType();
		// 读取检修模块是否可见
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.Overhaul.toString()).LoadConfigByType();*/
	}

	private void loadSettingXml() {

		/*
		 * 对于后加的配置项，以防由于WEBSERVICE没有更新最新的，导致新加的配置项 不受配置文件控制，默认先不可用
		 */
		lastresult.setEnabled(false);
		gesturesetting.setEnabled(false);
		gps_settings.setEnabled(false);
		openWakeLock.setEnabled(false);
		hisDataSettings.setEnabled(false);
		fontsizesettings.setEnabled(false);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.XSTBasePath() + AppConst.SettingxmlFile;
			String temppath = AppConst.XSTBasePath()
					+ AppConst.tempSettingxmlFile;
			FileEncryptAndDecrypt.decrypt(path, temppath);
			File f = new File(temppath);
			if (!f.exists()) {
				return;
			}
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				// 获取river中name属性值
				if (element.getAttribute("Name").equals("Voice")) {
					if (element.getAttribute("Using").equals("No")) {
						voice.setChecked(false);
						voice.setEnabled(false);
					}
				} else if (element.getAttribute("Name").equals(
						"InputLastResult")) {
					if (element.getAttribute("Using").equals("No")) {
						lastresult.setChecked(false);
						lastresult.setEnabled(false);
					} else {
						lastresult.setEnabled(true);
					}

				} else if (element.getAttribute("Name")
						.equals("GestureSetting")) {
					if (element.getAttribute("Using").equals("No")) {
						gesturesetting.setEnabled(false);
					} else {
						gesturesetting.setEnabled(true);
					}
				} else if (element.getAttribute("Name").equals("VideoSetting")) {
					if (element.getAttribute("Using").equals("No")) {
						videoqualitysetting.setEnabled(false);
					} else {
						videoqualitysetting.setEnabled(true);
					}
				} else if (element.getAttribute("Name").equals("CheckUpdate")) {
					if (element.getAttribute("Using").equals("No")) {
						checkup.setChecked(false);
						checkup.setEnabled(false);
					}
				} else if (element.getAttribute("Name").equals("UploadFile")) {
					if (element.getAttribute("Using").equals("No")) {
						uploadFile.setChecked(false);
						uploadFile.setEnabled(false);
					} else {
						uploadFile.setChecked(true);
						uploadFile.setEnabled(true);
					}
				} else if (element.getAttribute("Name").equals("ClearCache")) {
					if (element.getAttribute("Using").equals("No")) {
						cache.setEnabled(false);
					}
				} else if (element.getAttribute("Name").equals(
						"JITUploadSetting")) {
					if (element.getAttribute("Using").equals("No")) {
						jituploadsetting.setEnabled(false);
					}
				} else if (element.getAttribute("Name").equals(
						"UpdateSysDatetimeType")) {
					if (element.getAttribute("Using").equals("No")) {
						updateSysDate.setEnabled(false);
					}
				} else if (element.getAttribute("Name").equals("ChangeDate")) {
					if (element.getAttribute("Using").equals("No")) {
						changeDate.setEnabled(false);
					}
				} else if (element.getAttribute("Name").equals("ChangeTime")) {
					if (element.getAttribute("Using").equals("No")) {
						changeTime.setEnabled(false);
					}
				} else if (element.getAttribute("Name").equals("GpsCtrl")) {
					if (element.getAttribute("Using").equals("No")) {
						ctrlgps.setChecked(false);
						ctrlgps.setEnabled(false);
					}
				} else if (element.getAttribute("Name").equals("GpsSetting")) {
					if (element.getAttribute("Using").equals("No")) {
						gps_settings.setEnabled(false);
					} else {
						gps_settings.setEnabled(true);
					}
				} else if (element.getAttribute("Name").equals(
						"WeakLockSetting")) {
					if (element.getAttribute("Using").equals("No")) {
						openWakeLock.setEnabled(false);
					} else {
						openWakeLock.setEnabled(true);
					}
				} else if (element.getAttribute("Name").equals(
						"HisDataRetentionDays")) {
					if (element.getAttribute("Using").equals("No")) {
						hisDataSettings.setEnabled(false);
					} else {
						hisDataSettings.setEnabled(true);
					}
				} else if (element.getAttribute("Name").equals(
						"FontSizeSetting")) {
					if (element.getAttribute("Using").equals("No")) {
						fontsizesettings.setEnabled(false);
					} else {
						fontsizesettings.setEnabled(true);
					}
				}
			}
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendUpdateSysTime(long sysDateTime) {
		if (sysDateTime > 0) {
			long timeSpan = sysDateTime - System.currentTimeMillis();
			if (Math.abs(timeSpan) > 1000 * 60) {

				Intent updateSysDatetime = new Intent(
						"com.xst.track.service.updateSysDateTime");
				updateSysDatetime.putExtra("sysDateTime", sysDateTime);
				sendBroadcast(updateSysDatetime);
			}
		}
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

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter restartAppIntentFilter = new IntentFilter();
		restartAppIntentFilter
				.addAction("com.xst.track.service.updateSysDateTime");
		// 注册广播
		registerReceiver(mBroadcastReceiver, restartAppIntentFilter);
	}
	
	private void unregisterBoradcastReceiver() {
		try {
			if (mBroadcastReceiver != null)
				unregisterReceiver(mBroadcastReceiver);
		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("Receiver not registered")) {
				// Ignore this exception
			}
		}
	}
}
