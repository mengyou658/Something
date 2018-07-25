package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.service.CommuJITService;
import com.moons.xst.track.widget.SlipButton;
import com.moons.xst.track.widget.SlipButton.OnChangedListener;

public class JITUploadAty extends BaseActivity {
	private String TAG = "JITUploadAty";
	
	private ImageButton returnbutton;
	private SlipButton sbjitupload, sbjitxdjresult, sbjitxxresult;
	private TextView jituploadstate;
	private AppContext appcontext;
	private static boolean isChecked, isXDJChecked, isXXChecked;
	private EditText etOnceNumber, et_xxOnceNumber;
	private AppConfig appConfig;
	private EditText etOnceUpLoadTime, et_xxUpTime;
	private LinearLayout llOnceUpLoad, ll_xxUpLoad;
	private int DJPLAN_ONCE_MAX_UNMBER;// 巡点检最大上传条数
	private RelativeLayout reOnceUpLoadNumber;
	private Intent uploadDataIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_jitupload);
		appcontext = (AppContext) getApplication();
		appConfig = AppConfig.getAppConfig(this);
		init();
		setListener();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void init() {
		// 初始化巡线LinearLayout，巡线上传数量和时间
		ll_xxUpLoad = (LinearLayout) findViewById(R.id.ll_setting_jitupload_xxonce_upload);
		et_xxOnceNumber = (EditText) findViewById(R.id.et_setting_jitupload_xxonce_upload);
		et_xxUpTime = (EditText) findViewById(R.id.et_setting_jitupload_xxonce_upload_time);

		sbjitupload = (SlipButton) findViewById(R.id.setting_jitupload_checked);
		sbjitxdjresult = (SlipButton) findViewById(R.id.setting_jitupload_xdjresult_checked);
		sbjitxxresult = (SlipButton) findViewById(R.id.setting_jitupload_xxresult_checked);
		llOnceUpLoad = (LinearLayout) findViewById(R.id.ll_setting_jitupload_once_number);
		reOnceUpLoadNumber = (RelativeLayout) findViewById(R.id.re_setting_jitupload_xxonce_number);
		etOnceNumber = (EditText) findViewById(R.id.setting_jitupload_once_number);
		etOnceUpLoadTime = (EditText) findViewById(R.id.setting_jitupload_once_upload_time);
		if(TextUtils.isEmpty(etOnceNumber.getText().toString())){
			etOnceNumber.setText("10");
		}
		if(TextUtils.isEmpty(etOnceUpLoadTime.getText().toString())){
			etOnceUpLoadTime.setText("60");
		}
		et_xxOnceNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				int MaxNumber = checkMaxNumber(String.valueOf(AppContext.getJITUploadXDJOnceNum()));
				if (!TextUtils.isEmpty(s.toString())) {
					if (Integer.parseInt(s.toString()) > MaxNumber
							|| Integer.parseInt(s.toString()) <= 0) {
						et_xxOnceNumber.setTextColor(JITUploadAty.this
								.getResources().getColor(R.color.red));
						showToast(MaxNumber);
					} else {
						et_xxOnceNumber.setTextColor(JITUploadAty.this
								.getResources().getColor(R.color.black));
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根

			}
		});
		et_xxUpTime.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 自动生成的方法存根
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showXXTimePickDlg(AppContext.getJITUploadXDJOnceTime());
					return true;
				}
				return false;
			}
		});
		etOnceNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String strOnceUpLoadTime = etOnceUpLoadTime.getText()
						.toString();
				int MaxNumber = checkMaxNumber(strOnceUpLoadTime);
				if (!TextUtils.isEmpty(s.toString())) {
					if (Integer.parseInt(s.toString()) > MaxNumber) {
						etOnceNumber.setTextColor(JITUploadAty.this
								.getResources().getColor(R.color.red));
						// return;
						showToast(MaxNumber);
					} else {
						etOnceNumber.setTextColor(JITUploadAty.this
								.getResources().getColor(R.color.black));
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		etOnceUpLoadTime.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showTimePickDlg(Integer.valueOf(etOnceUpLoadTime.getText()
							.toString()));
					return true;
				}
				return false;
			}
		});
		// 获取巡点检单次上传条数和时间
		String savedOnceNumber = String.valueOf(AppContext.getJITUploadXDJOnceNum());
		String savedOnceUpLoadTime = String.valueOf(AppContext.getJITUploadXDJOnceTime());
		// 获取巡线单次上传条数和时间
		String XXOnceLoad = String.valueOf(AppContext.getJITUploadXDJOnceNum());
		String XXOnceLoadTime = String.valueOf(AppContext.getJITUploadXDJOnceTime());
		
		etOnceNumber.setText(savedOnceNumber);
		etOnceUpLoadTime.setText(savedOnceUpLoadTime);
		/*if (TextUtils.isEmpty(savedOnceNumber)) {
			appcontext.setOnceLoad(String
					.valueOf(AppConst.DJPLAN_DEFAULT_ONCE_UNMBER));
		}else{
			etOnceNumber.setText(savedOnceNumber);
		}
		if (TextUtils.isEmpty(savedOnceUpLoadTime)) {
			appcontext.setOnceLoadTime(String
					.valueOf(AppConst.DJPLAN_DEFAULT_ONCE_UPLOAD_TIME));
		}else{
			etOnceUpLoadTime.setText(savedOnceUpLoadTime);
		}*/
		isChecked = AppContext.getJITUpload();
		isXDJChecked = AppContext.getJITUploadXDJ();
		isXXChecked = AppContext.getJITUploadXX();
		if (isXXChecked == true) {
			// ll_xxUpLoad.setVisibility(View.VISIBLE);
			et_xxOnceNumber.setText(XXOnceLoad);
			et_xxUpTime.setText(XXOnceLoadTime);
		} else {
			ll_xxUpLoad.setVisibility(View.GONE);
		}

		if (isXDJChecked == true) {
			llOnceUpLoad.setVisibility(View.VISIBLE);
		} else {
			llOnceUpLoad.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(savedOnceNumber)
				&& (llOnceUpLoad.getVisibility() == 0)
				&& !TextUtils.isEmpty(savedOnceUpLoadTime)) {
			etOnceNumber.setText(savedOnceNumber);
			etOnceUpLoadTime.setText(savedOnceUpLoadTime);
		}
		sbjitupload.setCheck(isChecked);
		sbjitxdjresult.setCheck(isXDJChecked);
		sbjitxxresult.setCheck(isXXChecked);
		jituploadstate = (TextView) findViewById(R.id.setting_jitupload_state);
		if (isChecked)
			jituploadstate.setText(R.string.setting_jitupload_state_on);
		else
			jituploadstate.setText(R.string.setting_jitupload_state_off);
		returnbutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goBack();
			}
		});
	}
	//巡点检
	private static int index = 0;
	private com.moons.xst.track.widget.AlertDialog dialog;
	//巡线
	private static int index2 = 0;
	private com.moons.xst.track.widget.AlertDialog dialog2;
	private void showXXTimePickDlg(int position) {
		int checkitem = -1;
		if (position == 30) {
			checkitem = 0;
		} else if (position == 60) {
			checkitem = 1;
		}/*
		 * else if(position==30){ defaultChoose=2; }
		 */
		final String[] language = getResources().getStringArray(R.array.XXOnceUpLoadTime_options);
		final List<String> listData = new ArrayList<String>();
		for (String str : language) {
			listData.add(str);
		}
		LayoutInflater factory = LayoutInflater
				.from(this);
		final View view = factory.inflate(R.layout.listview_layout, null);
		dialog2 = new com.moons.xst.track.widget.AlertDialog(this).builder()
			.setTitle(getString(R.string.setting_jitupload_once_upload_time_title))
			.setView(view)
			.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					index2 = position;
					dialog2.refresh(listData, index2);
				}
			})
			.setPositiveButton(getString(R.string.sure), new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (index2) {
					case 0:
						
						break;
					case 1:
						
						break;
					}
					String timeChose = getResources().getStringArray(
							R.array.XXOnceUpLoadTime_options)[index2];
					et_xxUpTime.setTextSize(15);
					et_xxUpTime.setText(timeChose);
				}
			}).setNegativeButton(getString(R.string.cancel), new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
		dialog2.show();
		
		
		
		/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
		Builder setTitle = builder.setTitle(R.string.setting_jitupload_once_upload_time_title);
		builder.setSingleChoiceItems(R.array.XXOnceUpLoadTime_options,
				defaultChoose, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String timeChose = getResources().getStringArray(
								R.array.XXOnceUpLoadTime_options)[which];
						et_xxUpTime.setTextSize(15);
						et_xxUpTime.setText(timeChose);
					}
				}).setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();*/
	}

	private void showTimePickDlg(int position) {
		int checkitem = -1;
		if (position == 30) {
			checkitem = 0;
		} else if (position == 60) {
			checkitem = 1;
		}
		
		final String[] language = getResources().getStringArray(R.array.XDJOnceUpLoadTime_options);
		final List<String> listData = new ArrayList<String>();
		for (String str : language) {
			listData.add(str);
		}
		LayoutInflater factory = LayoutInflater
				.from(this);
		final View view = factory.inflate(R.layout.listview_layout, null);
		dialog = new com.moons.xst.track.widget.AlertDialog(this).builder()
			.setTitle(getString(R.string.setting_jitupload_once_upload_time_title))
			.setView(view)
			.setListViewItems(listData, checkitem, new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					index = position;
					dialog.refresh(listData, index);
				}
			})
			.setPositiveButton(getString(R.string.sure), new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String timeChose = getResources().getStringArray(
							R.array.XDJOnceUpLoadTime_options)[index];
					etOnceUpLoadTime.setTextSize(15);
					etOnceUpLoadTime.setText(timeChose);
				}
			}).setNegativeButton(getString(R.string.cancel), new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			}).setCanceledOnTouchOutside(false);
		dialog.show();
		
		
		
		/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
		Builder setTitle = builder.setTitle(R.string.setting_jitupload_once_upload_time_title);
		builder.setSingleChoiceItems(R.array.XDJOnceUpLoadTime_options,
				defaultChoose, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String timeChose = getResources().getStringArray(
								R.array.XDJOnceUpLoadTime_options)[which];
						etOnceUpLoadTime.setTextSize(15);
						etOnceUpLoadTime.setText(timeChose);
					}
				}).setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();*/
	}
	
	Runnable goBackRun = new Runnable() {
		
	    @Override
	    public void run() {
	    	
	    	uploadDataIntent = new Intent(JITUploadAty.this, CommuJITService.class);
			if (isXDJChecked != AppContext.getJITUploadXDJ()
					|| isXXChecked != AppContext.getJITUploadXX()) {
				appcontext.setConfigJITUpload(isChecked);
				appcontext.setConfigJITUploadXDJ(isXDJChecked);
				appcontext.setConfigJITUploadXX(isXXChecked);

				Bundle bundle = new Bundle();
				if (isChecked) {
					int xdjJITStartYN = isXDJChecked ? 0 : 2;
					int xxJITStartYN = isXXChecked ? 0 : 2;
					// 启动实时上传服务，赋值0
					bundle.putInt("Runstate", 0);
					bundle.putInt("XDJRunstate", xdjJITStartYN);
					bundle.putInt("XXRunstate", xxJITStartYN);

				} else {
					// 关闭实时上传，赋值2
					bundle.putInt("Runstate", 2);
				}
				uploadDataIntent.putExtras(bundle);
//				startService(uploadDataIntent);
				if (!etOnceUpLoadTime.getText().toString()
						.equals(AppContext.getJITUploadXDJOnceTime())||!etOnceNumber.getText().toString().equals(AppContext.getJITUploadXDJOnceNum())) {
					String strOnceUnmber = etOnceNumber.getText().toString();
					String strOnceUpLoadTime = etOnceUpLoadTime.getText().toString();
					
					if (!TextUtils.isEmpty(strOnceUnmber)) {
						errerInput(strOnceUnmber, strOnceUpLoadTime);
					}		
				}
				startService(uploadDataIntent);
				
				if (isChecked) {
					OperatingConfigHelper
					.getInstance()
					.i(AppConst.ANDROID_JITSETTING_ON, AppConst.LOGSTATUS_NORMAL,"");
				} else {
					OperatingConfigHelper
					.getInstance()
					.i(AppConst.ANDROID_JITSETTING_OFF, AppConst.LOGSTATUS_NORMAL,"");
				}
			} else {
				if (!etOnceUpLoadTime.getText().toString()
						.equals(AppContext.getJITUploadXDJOnceTime())||!etOnceNumber.getText().toString().equals(AppContext.getJITUploadXDJOnceNum())) {
					String strOnceUnmber = etOnceNumber.getText().toString();
					String strOnceUpLoadTime = etOnceUpLoadTime.getText().toString();
					
					if (!TextUtils.isEmpty(strOnceUnmber)) {
						errerInput(strOnceUnmber, strOnceUpLoadTime);
					}
					Bundle bundle = new Bundle();
					if (isChecked) {
						int xdjJITStartYN = isXDJChecked ? 0 : 2;
						int xxJITStartYN = isXXChecked ? 0 : 2;
						// 启动实时上传服务，赋值0
						bundle.putInt("Runstate", 0);
						bundle.putInt("XDJRunstate", xdjJITStartYN);
						bundle.putInt("XXRunstate", xxJITStartYN);

					} else {
						// 关闭实时上传，赋值2
						bundle.putInt("Runstate", 2);
					}
					uploadDataIntent.putExtras(bundle);
					startService(uploadDataIntent);
				}
			}

//			if (isChecked) {
//				OperatingConfigHelper
//				.getInstance()
//				.i(AppConst.ANDROID_JITSETTING_ON, AppConst.LOGSTATUS_NORMAL,"");
//			} else {
//				OperatingConfigHelper
//				.getInstance()
//				.i(AppConst.ANDROID_JITSETTING_OFF, AppConst.LOGSTATUS_NORMAL,"");
//			}
			AppManager.getAppManager().finishActivity(JITUploadAty.this);
				
	    }
	};
	
	Handler handler = new Handler();

	private void goBack() {
		handler.post(goBackRun);
	}

	/**
	 * 将单次上传条数存入本地
	 */
	private void saveOnceNumber(String strOnceUnmber) {
		appcontext.setConfigOnceUploadNum(strOnceUnmber);
		//appcontext.setOnceLoad(strOnceUnmber);
	}

	/**
	 * 将单次上传条数间隔时间存入本地
	 */
	private void saveOnceTime(String strOnceUpLoadTime) {
		appcontext.setConfigOnceLoadTime(strOnceUpLoadTime);
//		appcontext.setOnceLoadTime(strOnceUpLoadTime);
	}

	/**
	 * 巡线单次上传条数存入本地
	 */
	private void XXOnceNumber(String strOnceUnmber) {
		//appcontext.setXXOnceLoad(strOnceUnmber);
	}

	/**
	 * 巡线单次上传时间存入本地
	 */
	private void XXOnceTime(String strOnceUpLoadTime) {
		//appcontext.setXXOnceLoadTime(strOnceUpLoadTime);
	}

	/**
	 * 检查单次最大上传条数
	 * 
	 */
	private int checkMaxNumber(String UpLoadTime) {
		if (Integer.parseInt(UpLoadTime) == 30) {
			DJPLAN_ONCE_MAX_UNMBER = AppConst.DJPLAN_ONCE_MAX_UNMBER / 2;
		} else if (Integer.parseInt(UpLoadTime) == 60) {
			DJPLAN_ONCE_MAX_UNMBER = AppConst.DJPLAN_ONCE_MAX_UNMBER;
		}
		return DJPLAN_ONCE_MAX_UNMBER;
	}

	/**
	 * 巡线单次最大上传条数
	 * 
	 */
	public static int xxMaxNumber(String UpLoadTime) {
		int count = AppConst.XXPLAN_ONCE_MAX_UNMBER;
		if (Integer.parseInt(UpLoadTime) == 30) {
			return count / 2;
		} else if (Integer.parseInt(UpLoadTime) == 60) {
			return count;
		}
		return count;
	}

	/**
	 * 
	 * 单次上传错误输入类型处理设置
	 * 
	 * @param strOnceUnmber
	 *            条数
	 * @param strOnceUpLoadTime
	 *            上传时间间隔
	 */
	private void errerInput(String strOnceUnmber, String strOnceUpLoadTime) {
		int MaxNumber = checkMaxNumber(strOnceUpLoadTime);
		if (!TextUtils.isEmpty(strOnceUnmber)) {
			if (Integer.parseInt(strOnceUnmber) > MaxNumber) {
				saveOnceNumber(String
						.valueOf(AppContext.getJITUploadXDJOnceNum()));
			} else {
				saveOnceNumber(strOnceUnmber);
			}
			saveOnceTime(strOnceUpLoadTime);
		}
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		sbjitupload.SetOnChangedListener(new OnChangedListener() {

			public void OnChanged(boolean CheckState) {
				sbjitxdjresult.setCheck(CheckState);
				sbjitxdjresult.invalidate();
				sbjitxxresult.setCheck(CheckState);
				sbjitxxresult.invalidate();

				isChecked = CheckState;
				isXDJChecked = CheckState;
				isXXChecked = CheckState;

				String strOnceUnmber = etOnceNumber.getText().toString();
				String strOnceUpLoadTime = etOnceUpLoadTime.getText()
						.toString();
				if (isChecked) {
					// ll_xxUpLoad.setVisibility(View.VISIBLE);
					et_xxOnceNumber.setText(String.valueOf(AppContext.getJITUploadXDJOnceNum()));
					et_xxUpTime.setText(String.valueOf(AppContext.getJITUploadXDJOnceTime()));

					llOnceUpLoad.setVisibility(View.VISIBLE);
					etOnceNumber.setText(String.valueOf(AppContext.getJITUploadXDJOnceNum()));
					etOnceUpLoadTime.setText(String.valueOf(AppContext.getJITUploadXDJOnceTime()));

				} else {
					XXOnceNumber(et_xxOnceNumber.getText().toString());
					XXOnceTime(et_xxUpTime.getText().toString());
					ll_xxUpLoad.setVisibility(View.GONE);

					errerInput(strOnceUnmber, strOnceUpLoadTime);
					llOnceUpLoad.setVisibility(View.GONE);
				}
				if (CheckState) {
					jituploadstate.setText(R.string.setting_jitupload_state_on);
				} else {
					jituploadstate
							.setText(R.string.setting_jitupload_state_off);
				}
			}
		});

		sbjitxdjresult.SetOnChangedListener(new OnChangedListener() {

			public void OnChanged(boolean CheckState) {
				isXDJChecked = CheckState;
				if (isXDJChecked) {
					llOnceUpLoad.setVisibility(View.VISIBLE);
				} else {
					llOnceUpLoad.setVisibility(View.GONE);
				}

				if (isXDJChecked && !isChecked) {
					sbjitupload.setCheck(true);
					sbjitupload.invalidate();
					isChecked = true;
					jituploadstate.setText(R.string.setting_jitupload_state_on);
				} else if (!isXDJChecked && !isXXChecked) {
					sbjitupload.setCheck(false);
					sbjitupload.invalidate();
					isChecked = false;
					jituploadstate
							.setText(R.string.setting_jitupload_state_off);
				}
			}
		});

		sbjitxxresult.SetOnChangedListener(new OnChangedListener() {

			public void OnChanged(boolean CheckState) {
				isXXChecked = CheckState;
				if (isXXChecked) {
					// ll_xxUpLoad.setVisibility(View.VISIBLE);
					et_xxOnceNumber.setText(String.valueOf(AppContext.getJITUploadXDJOnceNum()));
					et_xxUpTime.setText(String.valueOf(AppContext.getJITUploadXDJOnceTime()));
				} else {
					XXOnceNumber(et_xxOnceNumber.getText().toString());
					XXOnceTime(et_xxUpTime.getText().toString());
					ll_xxUpLoad.setVisibility(View.GONE);
				}

				if (isXXChecked && !isChecked) {
					sbjitupload.setCheck(true);
					sbjitupload.invalidate();
					isChecked = true;
					jituploadstate.setText(R.string.setting_jitupload_state_on);
				} else if (!isXDJChecked && !isXXChecked) {
					sbjitupload.setCheck(false);
					sbjitupload.invalidate();
					isChecked = false;
					jituploadstate
							.setText(R.string.setting_jitupload_state_off);
				}
			}
		});
	}

	private void showToast(int number) {
		UIHelper.ToastMessage(JITUploadAty.this,
				JITUploadAty.this.getString(R.string.setting_sys_jitupload_onceupload_maxuploadpromptinfo,String.valueOf(number)));
	}
}