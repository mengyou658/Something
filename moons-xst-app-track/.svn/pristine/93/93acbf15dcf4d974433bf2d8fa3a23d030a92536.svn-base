package com.moons.xst.track.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppContext.LoginType;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.api.ApiClient;
import com.moons.xst.track.bean.Result;
import com.moons.xst.track.bean.User;
import com.moons.xst.track.common.RFIDManager;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;

/**
 * 用户登录对话框
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class LoginDialog extends BaseActivity {

	private final static String TOUCHIDPOS_LOGIN = "Login";
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_login;
	private AutoCompleteTextView mAccount;
	private EditText mPwd;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
	private CheckBox chb_rememberMe;
	private int curLoginType;
	private InputMethodManager imm;
	AppContext ac = null;

	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	
	private final static int REQUEST_CODE = 1;
	private String RFIDString = "";
	NfcAdapter nfcAdapter;
	PendingIntent mPendingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.setTheme(R.style.Theme_HalfTranslucent);
		setContentView(R.layout.login_dialog);

		ac = (AppContext) getApplication();
		
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		curLoginType = getIntent().getIntExtra("LOGINTYPE", LOGIN_OTHER);

		mViewSwitcher = (ViewSwitcher) findViewById(R.id.logindialog_view_switcher);
		loginLoading = (View) findViewById(R.id.login_loading);
		mAccount = (AutoCompleteTextView) findViewById(R.id.login_account);
		mAccount.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (ac.getLoginType().contains(LoginType.Scan.toString())) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent();
					intent.setClass(LoginDialog.this, CaptureActivity.class);
					intent.putExtra("ScanType", "EDITTEXT");
					startActivityForResult(intent, REQUEST_CODE);
				}
				return true;
			}
		});
		
		mPwd = (EditText) findViewById(R.id.login_password);
		chb_rememberMe = (CheckBox) findViewById(R.id.login_checkbox_rememberMe);

		btn_close = (ImageButton) findViewById(R.id.login_close_button);
		btn_close.setOnClickListener(UIHelper.finish(this));

		btn_login = (Button) findViewById(R.id.login_btn_login);
		btn_login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				String account = mAccount.getText().toString();
				String pwd = mPwd.getText().toString();
				boolean isRememberMe = chb_rememberMe.isChecked();
				// 判断输入
				if (StringUtils.isEmpty(account)) {
					UIHelper.ToastMessage(v.getContext(),
							getString(R.string.msg_login_email_null));
					return;
				}
				if (StringUtils.isEmpty(pwd)) {
					// UIHelper.ToastMessage(v.getContext(),
					// getString(R.string.msg_login_pwd_null));
					// return;
					pwd = "";
				}

				btn_close.setVisibility(View.GONE);
				loadingAnimation = (AnimationDrawable) loginLoading
						.getBackground();
				loadingAnimation.start();
				mViewSwitcher.showNext();

				User userInfo = new User();
				userInfo.setAccount(account);
				userInfo.setPwd(pwd);
				userInfo.setRememberMe(isRememberMe);
				login(userInfo, LoginType.Account);
			}
		});
		
		/* 根据登录验证配置项来加载具备哪些登录方式 */
		if (!ac.getLoginType().contains(LoginType.Account.toString()))
			btn_login.setEnabled(false);
		if (ac.getLoginType().contains(LoginType.RFID.toString())) {
			if (AppContext.getRFIDUseYN()){
				RFIDManager.getRFIDManager(LoginDialog.this, TOUCHIDPOS_LOGIN).regReceiver_and_bindService();
			}
			else{
				// 获取默认的NFC控制器
			    nfcAdapter = NfcAdapter.getDefaultAdapter(this);
				mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
						getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			}
		}
		
		// 是否显示登录信息		
		User user = ac.getLoginInfo();
		if (user == null || !user.isRememberMe())
			return;
		if (!StringUtils.isEmpty(user.getUserAccount())) {
			mAccount.setText(user.getUserAccount());
			mAccount.selectAll();
			chb_rememberMe.setChecked(user.isRememberMe());
		}
		if (!StringUtils.isEmpty(user.getUserPwd())) {
			mPwd.setText(user.getUserPwd());
		}
	}
	
	@Override
	protected void onDestroy() {
		if (ac.getLoginType().contains(LoginType.RFID.toString())) {
			if (AppContext.getRFIDUseYN())
				RFIDManager.getRFIDManager(LoginDialog.this, TOUCHIDPOS_LOGIN).unRegReceiver_and_unbindRemoteService();
		}
		super.onDestroy();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if (ac.getLoginType().contains(LoginType.RFID.toString())) {
			/* 此处是为了代码能执行onNewIntent()方法，不加则不进入此方法 */
				if (this.nfcAdapter == null)
					return;
				if (!this.nfcAdapter.isEnabled()) {
					if (!AppContext.debug) {
						UIHelper.ToastMessage(getBaseContext(), "请在系统设置中先启用NFC功能！");
					}
					return;
				}
				this.nfcAdapter.enableForegroundDispatch(this, this.mPendingIntent,
						null, null);
		}
	}

	// 登录验证
	private void login(final User userInfo, final AppContext.LoginType loginType) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					User user = (User) msg.obj;
					if (user != null) {
						// 清空原先cookie
						ApiClient.cleanCookie();
						// 发送通知广播
						// debug UIHelper.sendBroadCast(LoginDialog.this,
						// user.getNotice());
						// 提示登陆成功
						UIHelper.ToastMessage(LoginDialog.this,
								R.string.msg_login_success);
						if (curLoginType == LOGIN_MAIN) {
							// 跳转--加载用户动态
							Intent intent = new Intent(LoginDialog.this,
									Main_Page.class);
							intent.putExtra("LOGIN", true);

							startActivity(intent);
						} else if (curLoginType == LOGIN_SETTING) {
							// 跳转--用户设置页面
							Intent intent = new Intent(LoginDialog.this,
									Setting.class);
							intent.putExtra("LOGIN", true);
							startActivity(intent);
						}
						finish();
					}
				} else if (msg.what == 0) {
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(LoginDialog.this,
							getString(R.string.msg_login_fail) + msg.obj);
				} else if (msg.what == -1) {
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					((AppException) msg.obj).makeToast(LoginDialog.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					//AppContext ac = (AppContext) getApplication();
					User user = ac.loginVerify(userInfo, loginType);
//					user.setAccount(userInfo.getUserAccount());
//					user.setPwd(userInfo.getUserPwd());
//					user.setRememberMe(userInfo.isRememberMe());
					Result res = user.getValidate();
					if (res.OK()) {
						ac.saveLoginInfo(user);// 保存登录信息
						msg.what = 1;// 成功
						msg.obj = user;
					} else {
						// ac.cleanLoginInfo();//清除登录信息
						msg.what = 0;// 失败
						msg.obj = res.getErrorMessage();
					}
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AppManager.getAppManager().finishActivity(LoginDialog.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onNewIntent(Intent paramIntent){
		RFIDString = "";
		setIntent(paramIntent);
		processIntent(paramIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String codeResult = bundle.getString("codeResult");
				String account = "";
				String pwd = "";
				try{
					account = codeResult.split("\\|", 2)[0].toString();
					pwd = codeResult.split("\\|", 2)[1].toString();
				}
				catch (Exception e){
					account = "";
					pwd = "";
				}
				
				loadingAnimation = (AnimationDrawable) loginLoading
						.getBackground();
				loadingAnimation.start();
				mViewSwitcher.showNext();
				
				User userInfo = new User();
				userInfo.setAccount(account);
				userInfo.setPwd(pwd);
				userInfo.setRememberMe(chb_rememberMe.isChecked());
				login(userInfo, LoginType.Scan);
			}
		}
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
				
				loadingAnimation = (AnimationDrawable) loginLoading
						.getBackground();
				loadingAnimation.start();
				mViewSwitcher.showNext();
				
				User userInfo = new User();
				userInfo.setAccount(RFIDString);
				userInfo.setPwd("");
				userInfo.setUserRFID(RFIDString);
				userInfo.setRememberMe(chb_rememberMe.isChecked());
				login(userInfo, LoginType.RFID);
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
	
	/**
	 * 验证登录(600型号刷工卡)
	 * @param userRFID
	 */
	public void checkLogin(String userRFID) {
		try {
			loadingAnimation = (AnimationDrawable) loginLoading
					.getBackground();
			loadingAnimation.start();
			mViewSwitcher.showNext();
			
			User userInfo = new User();
			userInfo.setAccount(userRFID);
			userInfo.setPwd("");
			userInfo.setUserRFID(userRFID);
			userInfo.setRememberMe(chb_rememberMe.isChecked());
			login(userInfo, LoginType.RFID);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
