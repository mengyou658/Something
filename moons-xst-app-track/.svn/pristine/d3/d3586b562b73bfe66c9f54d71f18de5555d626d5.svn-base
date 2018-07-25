package com.moons.xst.track.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moons.xst.track.R;

public class WalkieTalkieXDInfo extends BaseActivity{

	private ImageButton returnbtn;
	private Button save;
	private EditText xdname, frequency;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walkie_talkie_xdinfo);
		
		title = (TextView)findViewById(R.id.ll_walkie_talkie_xdinfo_head_title);
		xdname = (EditText)findViewById(R.id.walkie_talkie_xdinfo_name_Value);
		frequency = (EditText)findViewById(R.id.walkie_talkie_xdinfo_frequency_Value);
		
		if (getIntent().getExtras().getString("opertype").equals("add")){
			title.setText(R.string.walkie_talkie_xdnfo_add);
		}
		else {
			title.setText(R.string.walkie_talkie_xdnfo_modify);
			xdname.setText(getIntent().getExtras().getString("xdname").toString());
			frequency.setText(getIntent().getExtras().getString("frequency").toString());
		}
		
		returnbtn = (ImageButton)findViewById(R.id.home_head_Rebutton);
		returnbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		
		save = (Button)findViewById(R.id.btn_saveXDInfo);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent();	    
			    intent.putExtra("xdname", xdname.getText().toString());
				intent.putExtra("frequency", frequency.getText().toString());
			    setResult(RESULT_OK,intent);
			    
			    finish();
			}
		});
	}
	
	
}