package com.moons.xst.track.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;
import com.moons.xst.track.bean.Z_Condition;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.SimpleTextDialog;

public class OperateLogQuery extends BaseActivity {
	
	ListView mlistview;
	
	CommonAdapter mAdapter;
	private List<String> mData = new ArrayList<String>();
	
	LoadingDialog loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operatelog_query);
		
		ImageButton returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(OperateLogQuery.this);
			}
		});
		loadOperateLogFile();
	}
	
	private void loadOperateLogFile() {
		loadOperateLogFileThread();
	}
	
	private void getOperateLogFiles() {
		File file = new File(AppConst.XSTLogFilePath());
		getFileList(file);
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 1) {
				bindingData();// 获取的数据绑定到列表中
			} else if (msg.what == -1 && msg.obj != null) {
				String msgString = String.valueOf(msg.obj);
				UIHelper.ToastMessage(getBaseContext(), msgString);
			}
		}
	};
	
	private void bindingData() {
		mlistview = (ListView) findViewById(R.id.lv_operatelog);
		mlistview.setAdapter(mAdapter = new CommonAdapter<String>(
        		OperateLogQuery.this, mData, R.layout.listitem_operatelog){

			@Override
			public void convert(com.moons.xst.track.adapter.ViewHolder helper,
					String item) {
				// TODO 自动生成的方法存根
				helper.setText(R.id.listitem_operatelog_itemdesc, item.split("\\|", 4)[1].toString());
				helper.setText(R.id.listitem_operatelog_itemdesc_subdesc, item.split("\\|", 4)[2].toString() 
						+ "  " + item.split("\\|", 4)[3].toString());
			}  
        });
		mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String filePath = mData.get(position).split("\\|", 4)[0].toString();
				File file = new File(filePath);
				if (!file.exists()) {
					UIHelper.ToastMessage(OperateLogQuery.this, getString(R.string.operatelog_file_notexist));
					return;
				}
				Intent intent = new Intent(OperateLogQuery.this, OperateLogDetail.class);
				intent.putExtra("filePath", filePath);
				startActivity(intent);
			}
		});
	}
	
	/**
     * @param path
     * @param fileList
     * 注意的是并不是所有的文件夹都可以进行读取的，权限问题
     */
    private void getFileList(File path){
    	Message msg = Message.obtain();
    	try {
        //如果是文件夹的话
        if(path.isDirectory()){
            //返回文件夹中有的数据
            File[] files = path.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if(null == files) {
            	msg.what = -1;
            	msg.obj = getString(R.string.album_message);
            	mHandler.sendMessage(msg);
                return;
            }
            for(int i = 0; i < files.length; i++){
            	if (FileUtils.getFileFormat(files[i].getName()).equalsIgnoreCase("xml"))
            		getFileList(files[i]);
            }
        }
        //如果是文件的话直接加入
        else{
            //进行文件的处理
            String filePath = path.getAbsolutePath();
            //文件名
            String fileName = FileUtils.getFileNameNoFormat(filePath);
            //文件日期
            Date date = new Date(path.lastModified());
            String fileDate = DateTimeHelper.DateToString(date);
            //文件大小
            String fileLen = FileUtils.formatFileSize(path.length());
            
            String file = filePath + "|" + 
            		fileName + "|" + fileDate + "|" + fileLen;
            
            mData.add(file);
        }
        
        msg.what = 1;
    	mHandler.sendMessage(msg);
    	
    	} catch (Exception e) {
    		msg.what = -1;
    		msg.obj = e.getMessage();
        	mHandler.sendMessage(msg);
    		e.printStackTrace();
    	}
    }
	
	private void loadOperateLogFileThread() {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.setCancelable(false);
			loading.setCanceledOnTouchOutside(false);
			loading.setLoadConceal();
			loading.show();
		}
		
		new Thread() {
			@Override
			public void run() { 
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							getOperateLogFiles();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});				
			}
		}.start();
	}
}