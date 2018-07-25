package com.moons.xst.track.ui;

import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.bean.OverhaulUser;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.xstinterface.OperateBillLogin;
import com.moons.xst.track.xstinterface.OtherLoginManager;
import com.moons.xst.track.xstinterface.OverhaulLogin;
import com.moons.xst.track.xstinterface.WorkBillLogin;

public class OtherLogin extends BaseActivity implements OnClickListener {

	ImageButton reButton;
	TextView headTitle;
	AutoCompleteTextView account;
	EditText pwd;
	RelativeLayout rl_accDel;
	RelativeLayout rl_pwdDel;
	ImageView accountDel;
	ImageView pwdDel;
	Button login;

	private String loginFrom;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_login);
		((AppContext) getApplicationContext()).getOverhaulDaoSession();
		loginFrom = getIntent().getStringExtra("LoginFrom");
		init();
	}

	private void init() {
		reButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		reButton.setOnClickListener(this);
		headTitle = (TextView) findViewById(R.id.ll_other_login_head_title);
		account = (AutoCompleteTextView) findViewById(R.id.other_login_account);
		pwd = (EditText) findViewById(R.id.other_login_password);
		rl_accDel = (RelativeLayout) findViewById(R.id.rl_other_login_account_delete);
		rl_accDel.setOnClickListener(this);
		rl_pwdDel = (RelativeLayout) findViewById(R.id.rl_other_login_password_delete);
		rl_pwdDel.setOnClickListener(this);
		accountDel = (ImageView) findViewById(R.id.other_login_account_delete);
		pwdDel = (ImageView) findViewById(R.id.other_login_password_delete);
		login = (Button) findViewById(R.id.other_login_btn_login);
		login.setOnClickListener(this);

		// 检修管理
		if (loginFrom.equals(AppConst.LoginFrom.overhaul.toString())) {
			headTitle.setText(getString(R.string.overhaul_managerment_login));
		} else if (loginFrom.equals(AppConst.LoginFrom.operatebill.toString())) {// 操作票
			headTitle.setText(getString(R.string.twobill_operate_login));
		} else if (loginFrom.equals(AppConst.LoginFrom.workbill.toString())) {// 工作票
			headTitle.setText(getString(R.string.twobill_work_login));
		}
		account.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					accountDel.setVisibility(View.GONE);
				} else {
					accountDel.setVisibility(View.VISIBLE);
				}
			}
		});
		pwd.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					pwdDel.setVisibility(View.GONE);
				} else {
					pwdDel.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.home_head_Rebutton:
			AppManager.getAppManager().finishActivity(OtherLogin.this);
			break;

		case R.id.rl_other_login_account_delete:
			account.setText("");
			break;
		case R.id.rl_other_login_password_delete:
			pwd.setText("");
			break;
		case R.id.other_login_btn_login:

			String acc = account.getText().toString();
			String password = pwd.getText().toString();
			// 检修管理
			if (loginFrom.equals(AppConst.LoginFrom.overhaul.toString())) {
				//OverhaulLogin overhaul = new OverhaulLogin();
				OtherLoginManager<OverhaulUser> manager = 
						new OtherLoginManager<OverhaulUser>(new OverhaulLogin());
				List<OverhaulUser> user = manager.checkLogin(this, acc,
						password);
				if (user == null || user.size() <= 0) {
					Toast.makeText(OtherLogin.this,
							getString(R.string.msg_login_fail),
							Toast.LENGTH_SHORT).show();
				} else {
					UIHelper.showOverhaulPlan(OtherLogin.this, user.get(0));
					Toast.makeText(OtherLogin.this,
							getString(R.string.msg_login_success),
							Toast.LENGTH_LONG).show();
					AppManager.getAppManager().finishActivity(this);
				}
				// 操作票
			} else if (loginFrom.equals(AppConst.LoginFrom.operatebill
					.toString())) {
//				OperateBillLogin opbill = new OperateBillLogin();
				OtherLoginManager<BillUsers> manager = 
						new OtherLoginManager<BillUsers>(new OperateBillLogin());
				List<BillUsers> user = manager.checkLogin(this, acc, password);
				if (user == null || user.size() <= 0) {
					Toast.makeText(OtherLogin.this,
							getString(R.string.msg_login_fail),
							Toast.LENGTH_SHORT).show();
				} else {
					UIHelper.showOperation(OtherLogin.this, user);
					Toast.makeText(OtherLogin.this,
							getString(R.string.msg_login_success),
							Toast.LENGTH_LONG).show();
					AppManager.getAppManager().finishActivity(this);
				}
				// 工作票
			} else if (loginFrom.equals(AppConst.LoginFrom.workbill.toString())) {
//				WorkBillLogin workbill = new WorkBillLogin();
				OtherLoginManager<BillUsers> manager = 
						new OtherLoginManager<BillUsers>(new WorkBillLogin());
				List<BillUsers> user = manager.checkLogin(this, acc, password);
				if (user == null || user.size() <= 0) {
					Toast.makeText(OtherLogin.this,
							getString(R.string.msg_login_fail),
							Toast.LENGTH_SHORT).show();
				} else {
					UIHelper.showWorkbill(OtherLogin.this, user);
					Toast.makeText(OtherLogin.this,
							getString(R.string.msg_login_success),
							Toast.LENGTH_LONG).show();
					AppManager.getAppManager().finishActivity(this);
				}
			}

			break;
		}
	}
}
