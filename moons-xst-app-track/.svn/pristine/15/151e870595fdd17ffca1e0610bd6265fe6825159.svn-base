package com.moons.xst.track.ui;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.moons.xst.buss.TempMeasureDBHelper;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.TempMeasureBaseInfo;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.StringUtils;

public class Tool_Video_PreviewAty extends BaseActivity {
	
	String videoPath = "";
	String thumbnailPath = "";
	
	private ImageButton rebutton;
	private Button delbtn;
	private VideoView videoView;
	
	/* 设置删除按钮是否可见 */
	private boolean setDelBtnVisible = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tool_video_preview);

		videoPath = getIntent().getExtras().getString("videoPath");
		thumbnailPath = getIntent().getExtras().getString("thumbnailPath");
		setDelBtnVisible = StringUtils.toBool(getIntent().getStringExtra("visible"));
		
		rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		rebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(Tool_Video_PreviewAty.class);
			}
		});
		
		delbtn = (Button) findViewById(R.id.video_del);
		delbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				LayoutInflater factory = LayoutInflater
						.from(Tool_Video_PreviewAty.this);
					final View view = factory.inflate(R.layout.textview_layout, null);
					new com.moons.xst.track.widget.AlertDialog(Tool_Video_PreviewAty.this).builder()
					.setTitle(getString(R.string.camera_delete_video))
					.setView(view)
					.setMsg(getString(R.string.camera_confirm_delete))
					.setPositiveButton(getString(R.string.sure), new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
							File file = new File(thumbnailPath);
							file.delete();
							
							File videoFile = new File(videoPath);
							videoFile.delete();
							
							TempMeasureBaseInfo mTempMeasureBaseInfo = new TempMeasureBaseInfo();
							mTempMeasureBaseInfo.setGUID(FileUtils.getFileNameNoFormat(videoPath));
							TempMeasureDBHelper.GetIntance().DeleteTempMeasureBaseInfo(Tool_Video_PreviewAty.this, mTempMeasureBaseInfo);
							
							AppManager.getAppManager().finishActivity(Tool_Video_PreviewAty.this);
						}
					}).setNegativeButton(getString(R.string.cancel), new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}).show();
				
			}
		});
		if (setDelBtnVisible)
			delbtn.setVisibility(View.VISIBLE);
		else
			delbtn.setVisibility(View.INVISIBLE);
		
		videoView = (VideoView) findViewById(R.id.videoView);
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoPath(videoPath);
		videoView.start();
	}

}
