package com.moons.xst.track.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;
import com.moons.xst.track.adapter.SystemBugInfoAdapter;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.SimpleTextDialog;

public class SystemBugInfoAty extends BaseActivity{
	
	private RelativeLayout rl_operation, rl_send, rl_delete;
	private ImageView moreButton;
	private ImageButton returnButton;
	private ListView lv_buginfo;
	private SystemBugInfoAdapter mAdapter;
	HashMap<String, String> fileList;
	
	private List<String> mData = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_buginfo);
		File file = new File(AppConst.XSTLogFilePath());
		getBugInfoFileList(file);	
		initData();
	}
	
	private void getBugInfoFileList(File file) {
		fileList = new HashMap<String, String>();
		mData.clear();
		getFileList(file, fileList);
	}
	
	/**
     * @param path
     * @param fileList
     * 注意的是并不是所有的文件夹都可以进行读取的，权限问题
     */
    private void getFileList(File path, HashMap<String, String> fileList){
        //如果是文件夹的话
        if(path.isDirectory()){
            //返回文件夹中有的数据
            File[] files = path.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if(null == files)
                return;
            for(int i = 0; i < files.length; i++){
                getFileList(files[i], fileList);
            }
        }
        //如果是文件的话直接加入
        else{
            //进行文件的处理
            String filePath = path.getAbsolutePath();
            //文件名
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
            //文件日期
            Date date = new Date(path.lastModified());
            String fileDate = DateTimeHelper.DateToString(date);
            //文件大小
            String fileLen = FileUtils.formatFileSize(path.length());
            
            String file = fileName + "|" + fileDate + "|" + fileLen;
            //添加
            mData.add(file);
            fileList.put(fileName, filePath);
        }
    }

	
	private void initData() {
		rl_operation = (RelativeLayout) findViewById(R.id.rl_operation);
		rl_operation.setVisibility(View.GONE);
		rl_send = (RelativeLayout) findViewById(R.id.rl_send);
		rl_send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// TODO 自动生成的方法存根
				if (AppContext.selectedBugInfoBuffer.size() <= 0) {
					UIHelper.ToastMessage(SystemBugInfoAty.this, R.string.setting_sys_aboutus_logoentered_errorLog_chooseprompt);
					return;
				}
				
				Intent email = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
				//邮件发送类型：带附件的邮件
				email.setType("application/octet-stream");
				 //邮件接收者（数组，可以是多位接收者）

				String title = getString(R.string.main_menu_system_aboutus_logo_hide_sysinfo_error_log);
				//设置邮件地址
				email.putExtra(android.content.Intent.EXTRA_EMAIL, "");
				//设置邮件标题
				email.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
				//设置发送的内容
				email.putExtra(android.content.Intent.EXTRA_TEXT, "");
				
				email.putParcelableArrayListExtra(Intent.EXTRA_STREAM, getBugInfoArrayList());
				 //调用系统的邮件系统
				startActivity(Intent.createChooser(email, getString(R.string.setting_sys_aboutus_logoentered_filefind_email_choosetosend)));
			}
		});
		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
		rl_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (AppContext.selectedBugInfoBuffer.size() <= 0) {
					UIHelper.ToastMessage(SystemBugInfoAty.this, R.string.setting_sys_aboutus_logoentered_errorLog_chooseprompt);
					return;
				}
				
				UIHelper.ToastMessage(SystemBugInfoAty.this, R.string.setting_sys_aboutus_logoentered_errorLog_cannotdelete);
				return;
			}
		});
		moreButton = (ImageView) findViewById(R.id.system_buginfo_head_morebutton);
		moreButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (rl_operation.getVisibility() == View.GONE) {
					rl_operation.setVisibility(View.VISIBLE);
					moreButton.setBackgroundResource(R.drawable.ic_cancel);
					mAdapter.refresh(mData, "SELECT");
				} else {
					rl_operation.setVisibility(View.GONE);
					moreButton.setBackgroundResource(R.drawable.ic_selectall);
					mAdapter.refresh(mData, "CANCEL");
				}
				
			}
		});
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(SystemBugInfoAty.this);
			}
		});

		
		lv_buginfo = (ListView) findViewById(R.id.listview_system_buginfo);
		mAdapter = new SystemBugInfoAdapter(SystemBugInfoAty.this, mData,
				R.layout.listitem_system_buginfo,"CANCEL");
		lv_buginfo.setAdapter(mAdapter);
		lv_buginfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String filePath = fileList.get(mData.get(position).split("\\|")[0].toString());
				String details = ReadTxtFile(filePath);
				
				final SimpleTextDialog _dialog = new SimpleTextDialog(
						SystemBugInfoAty.this, getString(R.string.setting_sys_aboutus_logoentered_errorLog_detailinfo), details);
				_dialog.setTextSize(20);
				_dialog.setOKButton(R.string.simple_text_dialog_btnok,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO 自动生成的方法存根
								_dialog.dismiss();
							}
						});
				_dialog.show();
			}
		});
	}
	
	//读取文本文件中的内容
    private static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);

            try {
                InputStream instream = new FileInputStream(file); 
                if (instream != null) 
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }                
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e) 
            {e.printStackTrace();} 
            catch (IOException e) 
            {e.printStackTrace();}
            return content;
    }

    private ArrayList<Uri> getBugInfoArrayList() {
    	ArrayList<Uri> imageUris = new ArrayList<Uri>();
    	
    	for (int i = 0; i < AppContext.selectedBugInfoBuffer.size(); i++) {
    		File file = new File(AppConst.XSTLogFilePath() + AppContext.selectedBugInfoBuffer.get(i));
    		
    		imageUris.add(Uri.fromFile(file));
    	}
    	
		return imageUris;
    }
}