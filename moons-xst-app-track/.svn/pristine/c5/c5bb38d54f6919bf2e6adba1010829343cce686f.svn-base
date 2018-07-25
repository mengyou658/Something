package com.moons.xst.track.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.AppContext.LoginType;
import com.moons.xst.track.api.ApiClient;
import com.moons.xst.track.bean.Result;
import com.moons.xst.track.bean.User;
import com.moons.xst.track.common.AppResourceUtils;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.GestureContentView;
import com.moons.xst.track.widget.GestureDrawline.GestureCallBack;

public class UserLogin extends BaseActivity implements OnClickListener{
	
	private final static String TAG = "UserLogin";
	private final static int REQCODE_RFID = 100;
	private final static int REQCODE_SCAN = 101;
	
	private RelativeLayout rl_gesture,rl_login_userinfo,rl_login_gesture,rl_login_othertype;
	private TextView tv_login_userinfo, tv_gesture;
	private ImageButton btn_close;
	private Button btn_login;
	private AutoCompleteTextView mAccount;
	private EditText mPwd;
	private CheckBox chb_rememberMe;
	private ImageView iv_rfid, iv_scan,logo_pic;
	private InputMethodManager imm;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	AppContext ac = null;

	private int curLoginType;
	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
		
        super.onCreate(savedInstanceState);  
        this.setTheme(R.style.Theme_HalfTranslucent);
        setContentView(R.layout.userlogin);  
        
        ac = (AppContext) getApplication();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        curLoginType = getIntent().getIntExtra("LOGINTYPE", LOGIN_OTHER);
        LoadAppConfigHelper
	       .getAppConfigHelper(AppConst.AppConfigType.Gesture.toString())
	       .LoadConfigByType();
        
        initView();
        initOnClickListener();
        
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQCODE_SCAN) {
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
				
				User userInfo = new User();
				userInfo.setAccount(account);
				userInfo.setPwd(pwd);
				userInfo.setRememberMe(chb_rememberMe.isChecked());
				login(userInfo, LoginType.Scan);
			}
		} else if (requestCode == REQCODE_RFID) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String rfidStr = bundle.getString("RFIDString");
				
				rfidLogin(rfidStr);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case (R.id.rl_login_userinfo) :
			tv_login_userinfo.setTextColor(Color.WHITE);
			tv_login_userinfo.setBackgroundColor(Color.parseColor(getResources().getString(R.color.lightblue)));
			//tv_login_userinfo.setBackgroundResource(R.color.lightblue);
			tv_gesture.setBackgroundColor(Color.WHITE);
			tv_gesture.setTextColor(Color.GRAY);
			rl_login_gesture.setBackgroundResource(R.drawable.input_layout_bg);
			rl_gesture.setVisibility(View.GONE);
		    break;
		case (R.id.rl_login_gesture) :			
			tv_login_userinfo.setTextColor(Color.GRAY);
			tv_login_userinfo.setBackgroundColor(Color.WHITE);
			tv_gesture.setTextColor(Color.WHITE);
			tv_gesture.setBackgroundColor(Color.parseColor(getResources().getString(R.color.lightblue)));
			//tv_gesture.setBackgroundResource(R.color.lightblue);
			rl_login_userinfo.setBackgroundResource(R.drawable.input_layout_bg);
			rl_gesture.setVisibility(View.VISIBLE);
		    break;
		case (R.id.login_close_button) :
			AppManager.getAppManager().finishActivity(UserLogin.this);
		    break;
		case (R.id.login_btn_login) :
			userLogin(v);
		    break;
		case (R.id.login_othertype_rfid_pic) :
			Intent rfidintent = new Intent(UserLogin.this, RFIDLogin.class);
		    startActivityForResult(rfidintent, REQCODE_RFID);
			break;
		case (R.id.login_othertype_scan_pic) :
			Intent intent = new Intent();
			intent.setClass(UserLogin.this, CaptureActivity.class);
			intent.putExtra("ScanType", "EDITTEXT");
			startActivityForResult(intent, REQCODE_SCAN);
			break;
		default :
			break;
		}
	}
	
	private void initView() {
		
		rl_gesture = (RelativeLayout) findViewById(R.id.rl_login_gesture_desc);
        rl_gesture.setVisibility(View.GONE);
        rl_login_userinfo = (RelativeLayout) findViewById(R.id.rl_login_userinfo);
        rl_login_gesture = (RelativeLayout) findViewById(R.id.rl_login_gesture);
        rl_login_othertype = (RelativeLayout) findViewById(R.id.rl_login_othertype);
        if (AppContext.getUserLoginType().equalsIgnoreCase(AppConst.AppLoginType.Account.toString())) {
        	rl_login_othertype.setVisibility(View.GONE);
        } else {
        	rl_login_othertype.setVisibility(View.VISIBLE);
        }
        tv_login_userinfo = (TextView) findViewById(R.id.tv_login_userinfo);
        tv_gesture = (TextView) findViewById(R.id.tv_login_gesture);
        btn_close = (ImageButton) findViewById(R.id.login_close_button);
        btn_login = (Button) findViewById(R.id.login_btn_login);
        mAccount = (AutoCompleteTextView) findViewById(R.id.login_account);
        mPwd = (EditText) findViewById(R.id.login_password);
        chb_rememberMe = (CheckBox) findViewById(R.id.login_checkbox_rememberMe);
        iv_rfid = (ImageView) findViewById(R.id.login_othertype_rfid_pic);
        iv_scan = (ImageView) findViewById(R.id.login_othertype_scan_pic);
        logo_pic=(ImageView) findViewById(R.id.logo_pic);
        String loginlogo = AppResourceUtils.getValue(this, getString(R.string.app_name), "loginlogo");
		if (!StringUtils.isEmpty(loginlogo)){
			int loginlogoID = AppResourceUtils.getDrawableId(this, loginlogo);
			logo_pic.setImageResource(loginlogoID);
		}
        
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, true, AppContext.gestureUserInfo, new GestureCallBack() {
        	
        	@Override
			public void onGestureCodeInput(String inputCode) {

			}
        	
        	@Override
			public void checkedSuccess(String account, String password) {
        		gestureLogin(account, password);
			}

			@Override
			public void checkedFail(String emsg) {
				UIHelper.ToastMessage(UserLogin.this, emsg);
				mGestureContentView.clearDrawlineState(0L);
				return;
			}
			
        });
        mGestureContentView.setParentView(mGestureContainer);
	}
	
	private void initOnClickListener() {
			
		rl_login_userinfo.setOnClickListener(this);
		rl_login_gesture.setOnClickListener(this);
		btn_close.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		iv_rfid.setOnClickListener(this);
		iv_scan.setOnClickListener(this);
	}
	
	private void userLogin(View v) {
		
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
			pwd = "";
		}
		
		User userInfo = new User();
		userInfo.setAccount(account);
		userInfo.setPwd(pwd);
		userInfo.setRememberMe(isRememberMe);
		login(userInfo, LoginType.Account);
	}
	
	private void rfidLogin(String rfidStr) {
		
		User userInfo = new User();
		userInfo.setAccount(rfidStr);
		userInfo.setPwd("");
		userInfo.setUserRFID(rfidStr);
		userInfo.setRememberMe(chb_rememberMe.isChecked());
		login(userInfo, LoginType.RFID);
	}
	
	private void gestureLogin(String account, String password) {
		
		boolean isRememberMe = chb_rememberMe.isChecked();
		User userInfo = new User();
		userInfo.setAccount(account);
		userInfo.setPwd(password);
		userInfo.setRememberMe(isRememberMe);
		login(userInfo, LoginType.Account);
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
						
						if (AppContext.getCheckInAfterEnterLine()) {
							if (AppContext.getFromLoginYN()) {
								// 提示登陆成功
								Toast.makeText(UserLogin.this, getString(R.string.msg_login_success),
										Toast.LENGTH_SHORT).show();
								
								OperatingConfigHelper.getInstance().i(AppConst.COMMTYPE_USERLOGIN,
										AppConst.LOGSTATUS_NORMAL,
										"");
								AppManager.getAppManager().finishActivity(UserLogin.this);
							} else {
								boolean flag = false;
								for (String s : user.getUserLineList()) {
									if (s.equals
											(String.valueOf(AppContext.getCurrLineInfo().getLineID()))) {
										flag = true;
										// 提示登陆成功
										Toast.makeText(UserLogin.this, getString(R.string.msg_login_success),
												Toast.LENGTH_SHORT).show();
										
										OperatingConfigHelper.getInstance().i(AppConst.COMMTYPE_USERLOGIN,
												AppConst.LOGSTATUS_NORMAL,
												"");
										UIHelper.enterMyWork(Main_Page.instance(), AppContext.getCurrLineInfo());
										AppManager.getAppManager().finishActivity(UserLogin.this);
									}
								}
								if (!flag) {
									// 提示登陆失败
									UIHelper.ToastMessage(UserLogin.this,
											R.string.msg_login_notin_fail);
								}
							}
						}
						else {
							// 提示登陆成功
							Toast.makeText(UserLogin.this, getString(R.string.msg_login_success),
									Toast.LENGTH_SHORT).show();
							if (curLoginType == LOGIN_MAIN) {
								// 跳转--加载用户动态
								Intent intent = new Intent(UserLogin.this,
										Main_Page.class);
								intent.putExtra("LOGIN", true);
		
								startActivity(intent);
							} else if (curLoginType == LOGIN_SETTING) {
								// 跳转--用户设置页面
								Intent intent = new Intent(UserLogin.this,
										Setting.class);
								intent.putExtra("LOGIN", true);
								startActivity(intent);
							}
							AppManager.getAppManager().finishActivity(UserLogin.this);
						}
					}
				} else if (msg.what == 0) {
					btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(UserLogin.this,
							getString(R.string.msg_login_fail) + "\n" + msg.obj);
				} else if (msg.what == -1) {
					btn_close.setVisibility(View.VISIBLE);
					((AppException) msg.obj).makeToast(UserLogin.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					User user = ac.loginVerify(userInfo, loginType);
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
}