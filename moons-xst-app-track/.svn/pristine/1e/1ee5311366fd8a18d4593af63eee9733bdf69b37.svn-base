package com.moons.xst.track.ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.GestureContentView;
import com.moons.xst.track.widget.GestureDrawline.GestureCallBack;
import com.moons.xst.track.widget.LockIndicator;

public class GestureSettingAty extends BaseActivity implements OnClickListener {
	
	private LockIndicator mLockIndicator;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private ImageButton returnButton;

	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesture_setting);
		setUpViews();
		setUpListeners();
		LoadAppConfigHelper
		       .getAppConfigHelper(AppConst.AppConfigType.Gesture.toString())
		       .LoadConfigByType();
	}
	
	private void setUpViews() {
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		mTextReset = (TextView) findViewById(R.id.text_reset);
		mTextReset.setClickable(false);
		mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		// 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "", new GestureCallBack() {
        	
        	@Override
			public void onGestureCodeInput(String inputCode) {
        		if (!isInputPassValidate(inputCode)) {
					mTextTip.setText(Html.fromHtml("<font color='#FF0000'>" + getString(R.string.login_gesturelogin_min_connectnum)+"</font>"));
					mGestureContentView.clearDrawlineState(0L);
					return;
				}
				if (mIsFirstInput) {
					if (!isEffective(inputCode))
						return;
					mFirstPassword = inputCode;
					updateCodeList(inputCode);
					mGestureContentView.clearDrawlineState(0L);
					mTextReset.setClickable(true);
					mTextReset.setText(getString(R.string.reset_gesture_code));
				} else {
					if (inputCode.equals(mFirstPassword)) {
						if (!saveGesture(inputCode)) {
							UIHelper.ToastMessage(GestureSettingAty.this, R.string.login_gesturelogin_savefailed);
							return;
						}
						UIHelper.ToastMessage(GestureSettingAty.this, R.string.login_gesturelogin_setsucceed);
						mGestureContentView.clearDrawlineState(0L);
						AppManager.getAppManager().finishActivity(GestureSettingAty.this);
					} else {
						mTextTip.setText(Html.fromHtml("<font color='#FF0000'>"+getString(R.string.login_gesturelogin_twotimediff)+"</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureSettingAty.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						// 保持绘制的线，1.5秒后清除
						mGestureContentView.clearDrawlineState(1300L);
					}
				}
				mIsFirstInput = false;
			}
        	
        	@Override
			public void checkedSuccess(String account, String password) {

			}

			@Override
			public void checkedFail(String emsg) {

			}
			
        });

		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");
	}
	
	private void setUpListeners() {
		returnButton.setOnClickListener(this);
		mTextReset.setOnClickListener(this);
	}
	
	private void updateCodeList(String inputCode) {
		// 更新选择的图案
		mLockIndicator.setPath(inputCode);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_reset:
			mIsFirstInput = true;
			updateCodeList("");
			mTextTip.setText(getString(R.string.setup_gesture_pattern));
			break;
		case R.id.home_head_Rebutton:
			AppManager.getAppManager().finishActivity(GestureSettingAty.this);
		default:
			break;
		}
	}
	
	private boolean isInputPassValidate(String inputPassword) {
		if (StringUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}
	
	private boolean isEffective(String inputCode) {
		boolean result = true;
		Set<String> keys = AppContext.gestureUserInfo.keySet();
		for (String key : keys) {
			if (!AppContext.getUserAccount().equals(key) && 
					inputCode.equals(AppContext.gestureUserInfo.get(key)[1].toString())) {
				UIHelper.ToastMessage(GestureSettingAty.this, R.string.login_gesturelogin_pwdisused);
				mGestureContentView.clearDrawlineState(0L);
				result = false;			
				break;
			}			
		}		
		return result;
	}
	
	private boolean saveGesture(String inputCode) {
		try {
			Set<String> keys = AppContext.gestureUserInfo.keySet();
			for (String key : keys) {
				if (AppContext.getUserAccount().equals(key)) {
					AppContext.gestureUserInfo.remove(key);
					break;
				}
			}
			String[] values = new String[2];
			values[0] = AppContext.getUserPassword();
			values[1] = inputCode;
			AppContext.gestureUserInfo.put(AppContext.getUserAccount(), values);
			
			String configStr = ParseObjectListToXml(AppContext.gestureUserInfo);
			
			FileWriter fw = null;  
	        BufferedWriter bw = null;   
	        try {   
	            fw = new FileWriter(AppConst.XSTBasePath() + "GestureConfig.xml", false);
	            bw = new BufferedWriter(fw);               
	            bw.write(configStr);   
	            bw.flush();  
	            bw.close();  
	            fw.close();  
	        } catch (IOException e) {    
	            e.printStackTrace();  
	            try {  
	                bw.close();  
	                fw.close();
	                return false;
	            } catch (IOException e1) { 
	            	return false;
	            }  
	        }
		} catch (Exception e2) {
			e2.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private static String ParseObjectListToXml(Hashtable<String, String[]> enitities)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("\r\n");
        sb.append("<Configuration>");
        sb.append("\r\n");
        for (String key : enitities.keySet())
        {
            sb.append("<Setting Account=\"" + key + "\" ");
            sb.append("Password=\"" + enitities.get(key)[0].toString() + "\" ");
        	sb.append("Gesture=\"" + enitities.get(key)[1].toString() + "\" />");
        }
        sb.append("\r\n");
        sb.append("</Configuration>");
        return sb.toString();
    }
}