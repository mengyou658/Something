package com.moons.xst.track.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.OverhaulHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.OverhaulProject;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.UIHelper;

public class OverhaulProjectImplementationAty extends BaseActivity implements
		View.OnClickListener {

	private ImageButton btn_back;
	private Button btn_save;
	private OverhaulProject overhaulProject;
	private TextView projectCode;
	private TextView projectName;
	private RelativeLayout rlProjectContent;
	private EditText projectImplementationMemo;
	private EditText projectImplementationSign;
	private OverhaulHelper overhaulHelper;
	private TextView projectImplementationMemoTitle;
	private static final int RESULTCODE = 200;
	private TextView tv_projectContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitvity_overhaul_project_implementation);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		overhaulProject = (OverhaulProject) bundle
				.getSerializable("overhaulProject");
		overhaulHelper = OverhaulHelper.getInstance();
		init();
	}

	public void init() {
		btn_back = (ImageButton) findViewById(R.id.home_head_Rebutton);
		btn_back.setOnClickListener(this);
		btn_save = (Button) findViewById(R.id.btn_save_data);
		btn_save.setOnClickListener(this);
		projectCode = (TextView) findViewById(R.id.overhaul_project_code);
		projectName = (TextView) findViewById(R.id.overhaul_project_name);
		projectCode.setText(overhaulProject.getWorkOrder_CD());
		projectName.setText(overhaulProject.getWorkOrderName_TX());
		tv_projectContent = (TextView) findViewById(R.id.tv_implementation_content);
		tv_projectContent.setText(overhaulProject.getWorkOrderContent_TX());
		rlProjectContent = (RelativeLayout) findViewById(R.id.rl_project_content);
		rlProjectContent.setOnClickListener(this);
		projectImplementationSign = (EditText) findViewById(R.id.project_implementation_sign);
		projectImplementationSign.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String editable = projectImplementationSign.getText().toString();  
	            String str = com.moons.xst.track.common.StringUtils.stringFilter(editable.toString());
	            if(!editable.equals(str)){
	            	projectImplementationSign.setText(str);
	              //设置新的光标所在位置  
	            	projectImplementationSign.setSelection(str.length());
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
//		projectImplementationSign.setText(overhaulProject.getQC0_TX());
		projectImplementationMemo = (EditText) findViewById(R.id.project_implementation_memo);
//		projectImplementationMemo.setText(overhaulProject.getQC0_MemoTX());
		if (!StringUtils.isEmpty(overhaulProject.getQC0())) {
			projectImplementationSign.setEnabled(false);
			projectImplementationMemo.setEnabled(false);
		}
		projectImplementationMemoTitle = (TextView) findViewById(R.id.project_implementation_memo_title);
		projectImplementationMemo.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String editable = projectImplementationMemo.getText().toString();  
	            String str = com.moons.xst.track.common.StringUtils.stringFilterWithSomeSymbol(editable.toString());
	            if(!editable.equals(str)){
	            	projectImplementationMemo.setText(str);
	              //设置新的光标所在位置  
	            	projectImplementationMemo.setSelection(str.length());
	            }
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				projectImplementationMemoTitle.setText(s.toString().length()
						+ "/250");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_head_Rebutton:
			AppManager.getAppManager().finishActivity(
					OverhaulProjectImplementationAty.this);
			break;
		case R.id.btn_save_data:
			if ("1".equals(overhaulProject.getQC0())) {
				return;
			}
			final String strMemo = projectImplementationMemo.getText()
					.toString();
			final String strSign = projectImplementationSign.getText()
					.toString();
			if (StringUtils.isEmpty(overhaulProject.getQC0())) {
				if (!StringUtils.isEmpty(strSign)) {
					LayoutInflater factory = LayoutInflater
							.from(OverhaulProjectImplementationAty.this);
					final View view = factory.inflate(R.layout.textview_layout,
							null);
					new com.moons.xst.track.widget.AlertDialog(
							OverhaulProjectImplementationAty.this)
							.builder()
							.setTitle(getString(R.string.tips))
							.setView(view)
							.setMsg(getString(R.string.overhaul_btn_saved_diatitle))
							.setPositiveButton(getString(R.string.sure),
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											overhaulProject
													.setCurrentQC(AppConst.CurrentQC.QC0
															.toString());
											overhaulProject.setQC0("1");
											String dateTimeNow = DateTimeHelper
													.getDateTimeNow();
											overhaulProject.setQC0_TX(strSign
													+ "   " + dateTimeNow);
											overhaulProject
													.setQC0_MemoTX(strMemo);
											overhaulHelper
													.upDateProjectImplement(
															OverhaulProjectImplementationAty.this,
															overhaulProject);
											overhaulProject
													.setState(getString(R.string.overhaul_project_examine_one_unFinished));
											Intent intent = new Intent();
											Bundle bundle = new Bundle();
											bundle.putSerializable(
													"detailProject",
													overhaulProject);
											intent.putExtras(bundle);
											setResult(RESULTCODE, intent);
											Toast.makeText(
													OverhaulProjectImplementationAty.this,
													getString(R.string.overhaul_project_save_ok),
													Toast.LENGTH_SHORT).show();
											AppManager
													.getAppManager()
													.finishActivity(
															OverhaulProjectImplementationAty.this);
										}
									})
							.setNegativeButton(getString(R.string.cancel), null)
							.show();
				} else {
					Toast.makeText(
							this,
							getString(R.string.overhaul_project_tips_signname_empt),
							Toast.LENGTH_LONG).show();
					return;
				}
			}
			break;
		case R.id.rl_project_content:
			if (StringUtils.isEmpty(overhaulProject.getWorkOrderContent_TX())) {
				return;
			}
			LayoutInflater factory = LayoutInflater
					.from(OverhaulProjectImplementationAty.this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(
					OverhaulProjectImplementationAty.this)
					.builder()
					.setTitle(
							getString(R.string.overhaul_project_content_diashow))
					.setView(view)
					.setMsg(overhaulProject.getWorkOrderContent_TX().toString())
					.setPositiveButton(getString(R.string.sure), null).show();
			break;
		default:
			break;
		}
	}

}