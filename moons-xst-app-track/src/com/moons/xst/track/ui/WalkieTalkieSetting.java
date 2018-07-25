package com.moons.xst.track.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.Walkie_Talkie_Setting_ListViewAdapter;
import com.moons.xst.track.bean.Walkie_talkie_SettingInfo;
import com.moons.xst.track.common.StringUtils;

public class WalkieTalkieSetting extends BaseActivity{
	
	private Walkie_Talkie_Setting_ListViewAdapter listAdapter;
	private ImageButton returnButton;
	private ListView xdlistview;
	private TextView head_title;
	private RelativeLayout recovery;
	private Button addbtn;
	
	Walkie_talkie_SettingInfo settingInfo;
	private List<Walkie_talkie_SettingInfo> list = new ArrayList<Walkie_talkie_SettingInfo>();
	private static final int REQCODE_ADDXDINFO = 1;
	private static final int REQCODE_MODIFYXDINFO = 2;
	private static int tempposition = 0;
	
	static String mXDName = "";
	static String mFrequency = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walkie_talkie_setting);
			
		initHeadViewAndMoreBar();
		getInfoFromConfig();
		bindWalkieTalkieConfigData();
		xdlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Walkie_talkie_SettingInfo info = (Walkie_talkie_SettingInfo)listAdapter.getItem(position);
				if (info == null)
					return;
				
				tempposition = position;
				Intent intent = new Intent(WalkieTalkieSetting.this, WalkieTalkieXDInfo.class);
				intent.putExtra("opertype", "modify");
				intent.putExtra("xdname", info.getXDName().toString());
				intent.putExtra("frequency", info.getFrequency().toString());
				startActivityForResult(intent,REQCODE_MODIFYXDINFO);
			}			
		});
		addbtn = (Button)findViewById(R.id.btn_additems);
		addbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(WalkieTalkieSetting.this, WalkieTalkieXDInfo.class);
				intent.putExtra("opertype", "add");
				startActivityForResult(intent,REQCODE_ADDXDINFO);
			}
		});
		
		returnButton = (ImageButton)findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				save();
				Intent intent = new Intent();
				intent.putExtra("xdname", mXDName);
				intent.putExtra("frequency", mFrequency);
				setResult(RESULT_OK,intent);
				
				finish();
			}
		});
	}
	
	/**
	 * 初始化头部视图
	 */
	private void initHeadViewAndMoreBar() {
		head_title = (TextView) findViewById(R.id.ll_walkie_talkie_setting_title);
		head_title.setText(R.string.walkie_talkie_setting_head_title);
		recovery = (RelativeLayout)findViewById(R.id.ll_walkie_talkie_setting_recovery);
		recovery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				LayoutInflater factory = LayoutInflater
						.from(WalkieTalkieSetting.this);
					final View view = factory.inflate(R.layout.textview_layout, null);
					new com.moons.xst.track.widget.AlertDialog(WalkieTalkieSetting.this).builder()
					.setTitle("恢复默认设置")
					.setView(view)
					.setMsg("确定要恢复默认设置吗？")
					.setPositiveButton(getString(R.string.sure), new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String tempPathString = AppConst.XSTBasePath() + "WalkieTalkieConfig.xml";
							File file = new File(tempPathString);
							if (file.exists())
								file.delete();
							list.clear();
							getInfoFromConfig();
							listAdapter.refresh(list);
						}
					}).setNegativeButton(getString(R.string.cancel), new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}).show();
			
			}
		});
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				save();
				Intent intent = new Intent();
				intent.putExtra("xdname", mXDName);
				intent.putExtra("frequency", mFrequency);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			save();
			Intent intent = new Intent();
			intent.putExtra("xdname", mXDName);
			intent.putExtra("frequency", mFrequency);
			setResult(RESULT_OK,intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 根据配置文件加载信道信息
	 */
	private void getInfoFromConfig(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.WalkieTalkieConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				settingInfo = new Walkie_talkie_SettingInfo();
				settingInfo.setXDName(element.getAttribute("Name").toString());
				settingInfo.setFrequency(element.getAttribute("Frequency").toString());
				settingInfo.setChecked(StringUtils.toBool(element.getAttribute("Checked").toString()));
				list.add(settingInfo);
			   }
			}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void bindWalkieTalkieConfigData(){
		listAdapter = new Walkie_Talkie_Setting_ListViewAdapter(this,
				list,R.layout.listitem_walkietalkie_setting);
		xdlistview = (ListView)findViewById(R.id.walkie_talkie_setting_listview_status);
		xdlistview.setAdapter(listAdapter);
	}
	
	private void save(){
		String configStr = ParseObjectListToXml(list);
		
		 FileWriter fw = null;  
         BufferedWriter bw = null;   
         try {   
             fw = new FileWriter(AppConst.XSTBasePath() + "WalkieTalkieConfig.xml", false);
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
             } catch (IOException e1) {   
             }  
         }
	}
	
	private static String ParseObjectListToXml(List<Walkie_talkie_SettingInfo> enitities)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<Configuration>");
        for (Walkie_talkie_SettingInfo obj : enitities)
        {
            sb.append("<Setting Name=\"" + obj.getXDName() + "\" ");
            sb.append("Frequency=\"" + obj.getFrequency() + "\" ");
            if (obj.getChecked())
            {
            	sb.append("Checked=\"true\" />");
            	mXDName = obj.getXDName();
            	mFrequency = obj.getFrequency();
            }
            else
            	sb.append("Checked=\"false\" />");
        }
        sb.append("</Configuration>");
        return sb.toString();
    }
	
	@Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent intent){
	 super.onActivityResult(requestCode, resultCode, intent);  
	 if(requestCode == REQCODE_ADDXDINFO){
		 if (resultCode == Activity.RESULT_OK) {
			 Walkie_talkie_SettingInfo info = new Walkie_talkie_SettingInfo();
			 info.setXDName(intent.getExtras().getString("xdname"));
			 info.setFrequency(intent.getExtras().getString("frequency"));
			 info.setChecked(false);
			 list.add(info);
			 listAdapter.refresh(list);		 
		 }
	   }
	 if(requestCode == REQCODE_MODIFYXDINFO){
		 if (resultCode == Activity.RESULT_OK) {
			 list.get(tempposition).setXDName(intent.getExtras().getString("xdname"));
			 list.get(tempposition).setFrequency(intent.getExtras().getString("frequency"));
			 listAdapter.refresh(list);		 
		 }
	 }
     }
}