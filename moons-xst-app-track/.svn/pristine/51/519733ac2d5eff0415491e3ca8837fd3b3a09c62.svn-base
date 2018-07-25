package com.moons.xst.track.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.SystemFileInfoAdapter;
import com.moons.xst.track.bean.XSTFileInfo;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.widget.LoadingDialog;

public class SystemFileInfoAty extends BaseActivity {
	
	private ListView lv_file;
	private ImageView iv_send;
	private ImageButton returnButton;
	
	private static int curIndex = 1; 
	private static String flag = "FORWARD";
	private static List<XSTFileInfo> mData = new ArrayList<XSTFileInfo>();
	
	private static Hashtable<Integer, String> backFileName = new Hashtable<Integer, String>();
	
	private XSTFileInfo fileInfo;
	
	private SystemFileInfoAdapter mAdapter;
	private LoadingDialog loading;
	private Handler mHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_fileinfo);
		File file = new File(AppConst.XSTBasePath());
		getXSTRootFileList(file);
		initView();
	}
	
	private void xstZipThread() {
		loading = new LoadingDialog(this);
		loading.setLoadText(getString(R.string.setting_sys_aboutus_logoentered_filefinddiainfo));
		loading.setCancelable(false);
		loading.show();
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					ZipUtils.zip(AppConst.XSTBasePath(),
							AppConst.XSTZipFilePath() + "XST.zip");
					msg.what = 1;
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					msg.what = -1;
				}
				
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	
	private void initView() {
		iv_send = (ImageView) findViewById(R.id.system_fileinfo_head_sendbutton);
		iv_send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
				File file = new File(AppConst.XSTZipFilePath() + "XST.zip");
				if (file.exists())
					file.delete();
				
				// TODO 自动生成的方法存根
				mHandler = new Handler() {
					public void handleMessage(Message msg) {
						if (loading != null)
							loading.dismiss();
						
						if (msg.what == 1) {
							Intent email = new Intent(android.content.Intent.ACTION_SEND);
							
							File file = new File(AppConst.XSTZipFilePath() + "XST.zip");
							//邮件发送类型：带附件的邮件
							email.setType("application/octet-stream");
							 //邮件接收者（数组，可以是多位接收者）

							String title = getString(R.string.setting_sys_aboutus_logoentered_filefind_emailtitle);
							//设置邮件地址
							email.putExtra(android.content.Intent.EXTRA_EMAIL, "");
							//设置邮件标题
							email.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
							//设置发送的内容
							email.putExtra(android.content.Intent.EXTRA_TEXT, "");
							
							email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
							 //调用系统的邮件系统
							startActivity(Intent.createChooser(email, getString(R.string.setting_sys_aboutus_logoentered_filefind_email_choosetosend)));
						} else if (msg.what == -1) {
							UIHelper.ToastMessage(SystemFileInfoAty.this, R.string.setting_sys_aboutus_logoentered_filefind_email_ziperror);
							return;
						}
					}
				};
				
				xstZipThread();
			}
		});
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (curIndex == 1) {
					mData.clear();
					backFileName.clear();
					AppManager.getAppManager().finishActivity(SystemFileInfoAty.this);
				} else if (curIndex == 2) {
					File file = new File(AppConst.XSTBasePath());
					getXSTRootFileList(file);
					mAdapter.notifyDataSetChanged();
					curIndex--;
					iv_send.setVisibility(View.VISIBLE);
				}
				else {
					flag = "BACK";
					String filename = backFileName.get(curIndex - 2);
					File file = new File(filename);
					getChildFileListByName(file);
					mAdapter.notifyDataSetChanged();
					curIndex--;
				}
			}
		});
		lv_file = (ListView) findViewById(R.id.listview_system_fileinfo);
		mAdapter = new SystemFileInfoAdapter(SystemFileInfoAty.this,
				mData, R.layout.listitem_system_fileinfo);
		lv_file.setAdapter(mAdapter);
		
		lv_file.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final XSTFileInfo info = (XSTFileInfo)(mAdapter.getItem(position));
				if (info == null)
					return;
				if (!info.getDirectoryYN())
					return;
				
				flag = "FORWARD";
				
				String filePath = info.getFilePath();
				File file = new File(filePath);
				getChildFileListByName(file);
				mAdapter.notifyDataSetChanged();
				iv_send.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	private void getXSTRootFileList(File file) {
		mData.clear();
		getRootFileList(file);
	}
	
	private void getRootFileList(File file){
        //如果是文件夹的话
        if(file.isDirectory()){
            //返回文件夹中有的数据
            File[] files = file.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if(null == files)
                return;
            
            int directoryCount = 0, fileCount = 0;
            for(int i = 0; i < files.length; i++){
                if (files[i].isDirectory()) {
                	directoryCount ++;
                } else {
                	fileCount ++;
                }             	
            }
            
            fileInfo = new XSTFileInfo();
            fileInfo.setFileName(file.getName());
            fileInfo.setFilePath(file.getAbsolutePath());
            fileInfo.setDirectoryYN(true);
            fileInfo.setSubDirectoryCount(String.valueOf(directoryCount));
            fileInfo.setSubFileCount(String.valueOf(fileCount));
            
            mData.add(fileInfo);
        }
    }
	
	private void getChildFileListByName(File file) {
		mData.clear();
		getChildFileList(file);
		
		iv_send.setVisibility(View.INVISIBLE);
	}
	
	private void getChildFileList(File file) {
		//返回文件夹中有的数据
        File[] files = file.listFiles();
        //先判断下有没有权限，如果没有权限的话，就不执行了
        if(null == files)
            return;
        
        
        List<File> list1 = new ArrayList<File>();
        List<File> list2 = new ArrayList<File>();
        List<File> list3 = new ArrayList<File>();
        
        for (File f : files) {
    	   if (f.isDirectory()) {
    	    list1.add(f);
    	   }
    	   if (f.isFile()) {
    	    list2.add(f);
    	   }
	    }
        
        Collections.sort(list1);
        Collections.sort(list2);
        list3.addAll(list1);
        list3.addAll(list2);
        
        
        
        for (int i = 0; i < list3.size(); i++) {
        	if (list3.get(i).isDirectory()) {
        		File[] subFiles = list3.get(i).listFiles();
        		
        		int directoryCount = 0, fileCount = 0;
                for(int j = 0; j < subFiles.length; j++){
                    if (subFiles[j].isDirectory()) {
                    	directoryCount ++;
                    } else {
                    	fileCount ++;
                    }             	
                }
                
                fileInfo = new XSTFileInfo();
                fileInfo.setFileName(list3.get(i).getName());
                fileInfo.setFilePath(list3.get(i).getAbsolutePath());
                fileInfo.setDirectoryYN(true);
                fileInfo.setSubDirectoryCount(String.valueOf(directoryCount));
                fileInfo.setSubFileCount(String.valueOf(fileCount));
        	} else {
        		fileInfo = new XSTFileInfo();
        		fileInfo.setFileName(list3.get(i).getName());
        		fileInfo.setFilePath(list3.get(i).getAbsolutePath());
        		fileInfo.setDirectoryYN(false);
        		fileInfo.setFileType(FileUtils.getFileFormat(list3.get(i).getName()));
        		Date date = new Date(list3.get(i).lastModified());
        		fileInfo.setFileTime(DateTimeHelper.DateToString(date));
        		fileInfo.setFileLen(FileUtils.formatFileSize(list3.get(i).length()));
        	}
        	mData.add(fileInfo);
        }
        
        if (flag.equalsIgnoreCase("FORWARD")) {
	        if (backFileName.contains(curIndex))
	        	backFileName.remove(curIndex);
	        backFileName.put(curIndex, file.getAbsolutePath());
	        curIndex++;
        }
	}
}