package com.moons.xst.track.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.OperateLogBean;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.widget.LoadingDialog;

public class CommDownloadErrorResultBack extends BaseActivity {
	
	ListView mlistview;		
	Button backBtn;
	CommonAdapter mAdapter;
	
	private HashMap<DJLine, String> upLoadFailingMap=new HashMap<DJLine, String>();
	
	List<OperateLogBean> mDatas = new ArrayList<OperateLogBean>();
	
	LoadingDialog backing;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_download_errorresultback);
		
		//获得意图
        Intent intent=getIntent();
        //得到数据集
        Bundle bundle=intent.getExtras();
        upLoadFailingMap = (HashMap<DJLine, String>)(bundle.get("errorResult"));
		
        backBtn = (Button) findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				LayoutInflater factory = LayoutInflater.from(CommDownloadErrorResultBack.this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(CommDownloadErrorResultBack.this)
						.builder()
						.setTitle(getString(R.string.tips))
						.setView(view)
						.setMsg(getString(R.string.cumm_cummdownload_errorback_message))
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										// 备份错误结果文件
										backErrorData();
									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								}).show();
			}
		});
        
		ImageButton returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				goBack();
			}
		});	
		
		loadData();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void loadData() {
		Iterator<Entry<DJLine, String>> it = upLoadFailingMap.entrySet().iterator();
        while (it.hasNext()) {
        	@SuppressWarnings("unchecked")
			Entry<DJLine, String> entry = (Entry<DJLine, String>) it.next();
        	OperateLogBean bean = new OperateLogBean();
        	bean.setOperate_TX(getString(R.string.cumm_cummdownload_uploadfilefailed, entry.getKey().getLineID()));
        	bean.setState_TX(getString(R.string.system_failed));
        	bean.setMessage_TX(entry.getValue());
        	
        	mDatas.add(bean);
        }
        
        mlistview = (ListView) findViewById(R.id.lv_comm_download_errorresultback);
		mlistview.setAdapter(mAdapter = new CommonAdapter<OperateLogBean>(
				CommDownloadErrorResultBack.this, mDatas, R.layout.listitem_operatelog_detail){

			@Override
			public void convert(com.moons.xst.track.adapter.ViewHolder helper,
					OperateLogBean bean) {
				// TODO 自动生成的方法存根
				helper.setImageResource(R.id.operatelog_listitem_icon, R.drawable.commu_fail_50dp);
				helper.setText(R.id.listitem_operatelog_detail_itemdesc, bean.getOperate_TX());
				helper.setText(R.id.tv_error_desc, bean.getMessage_TX());
				helper.setText(R.id.listitem_operatelog_state_itemdesc, bean.getState_TX());
				helper.setColor(R.id.listitem_operatelog_state_itemdesc, getResources().getColor(R.color.red));
			}  
        });
	}
	
	private void backErrorData() {
		backReeoeDataThread();
	}
	
	private void backReeoeDataThread() {
		if (backing == null || !backing.isShowing()) {
			backing = new LoadingDialog(this);
			backing.setLoadText(getString(R.string.backuping_ing));
			backing.setCancelable(false);
			backing.setCanceledOnTouchOutside(false);
			backing.show();
		}
		
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					Iterator<Entry<DJLine, String>> it = upLoadFailingMap.entrySet().iterator();
			        while (it.hasNext()) {
			        	@SuppressWarnings("unchecked")
						Entry<DJLine, String> entry = (Entry<DJLine, String>) it.next();
			        	File file = new File(entry.getKey().getResultDBPath());
			        	if (file.exists()) {
			        		if (ZipUtils.zipFolder(
			        				file.getParent(),
									AppConst.XSTErrorDBBackPath()
											+ entry.getKey().getLineID()
											+ "_"
											+ DateTimeHelper.getDateTimeNow1())) {
			        			// 清除上传文件夹
								String path = AppConst.XSTResultPath() + entry.getKey().getLineID();
								FileUtils.clearFileWithPath(path);
							} else {
								msg.what = 0;
								msg.obj = getString(R.string.cumm_cummdownload_backuploadfilefailed, 
										String.valueOf(entry.getKey().getLineID()));
						        mHandler.sendMessage(msg);
						        break;
							}
			        	}
			        }
			        msg.what = 1;
			        mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = getString(R.string.cumm_cummdownload_backuploadfilefailedall) + e.getMessage();
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (backing != null)
				backing.dismiss();
			if (msg.what == 1) {
				LayoutInflater factory = LayoutInflater.from(CommDownloadErrorResultBack.this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(CommDownloadErrorResultBack.this)
						.builder()
						.setTitle(getString(R.string.tips))
						.setView(view)
						.setCancelable(false)
						.setCanceledOnTouchOutside(false)
						.setMsg(getString(R.string.cumm_cummdownload_errorback_success))
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										upLoadFailingMap.clear();
										goBack();
									}
								}).show();
			} else if ((msg.what == 0 || msg.what == -1) && msg.obj != null) {
				String msgString = String.valueOf(msg.obj);
				UIHelper.ToastMessage(CommDownloadErrorResultBack.this, msgString);
			}
		}
	};
	
	private void goBack() {
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
        bundle.putSerializable("errorResult",upLoadFailingMap);
        intent.putExtras(bundle);
	    setResult(RESULT_OK,intent);
	    
	    AppManager.getAppManager().finishActivity(CommDownloadErrorResultBack.this);
	}
}