package com.moons.xst.track.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;
import com.moons.xst.track.bean.OperateLogBean;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;

public class OperateLogDetail extends BaseActivity {
	
	ListView mlistview;		
	CommonAdapter mAdapter;
	
	LoadingDialog loading;
	String filePath = "";
	List<OperateLogBean> mDatas = new ArrayList<OperateLogBean>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operatelog_detail);
		
		if (savedInstanceState != null) {
			filePath = savedInstanceState.getString("currentFilePath");
		} else {
			filePath = getIntent().getStringExtra("filePath");
		}
		
		ImageButton returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(OperateLogDetail.this);
			}
		});	
		
		loadDetail();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onRestoreInstanceState(savedInstanceState);
		savedInstanceState.putString("currentFilePath", filePath);
	}
	
	private void loadDetail() {
		loadDetailThread();
	}
	
	private void loadDetailThread() {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.setCancelable(false);
			loading.setCanceledOnTouchOutside(false);
			loading.show();
		}
		
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					getDetailData();
					msg.what = 1;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e.getMessage();
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 1) {
				bindingData();// 获取的数据绑定到列表中
			} else if (msg.what == -1 && msg.obj != null) {
				String msgString = String.valueOf(msg.obj);
				UIHelper.ToastMessage(OperateLogDetail.this, msgString);
			}
		}
	};
	
	private void getDetailData() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			File f = new File(filePath);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Log");
			for (int i = 0; i < nodes.getLength(); i++) {
				OperateLogBean bean = new OperateLogBean();				
				NodeList lists = nodes.item(i).getChildNodes();
				for (int j = 0; j < lists.getLength(); j++) {
					if (lists.item(j).getNodeName().equals("UserName")) {
						bean.setUserName_TX(lists.item(j).getTextContent().toString());
					} else if (lists.item(j).getNodeName().equals("Operate")) {
						bean.setOperate_TX(lists.item(j).getTextContent().toString());
					} else if (lists.item(j).getNodeName().equals("Time")) {
						bean.setTime_TX(lists.item(j).getTextContent().toString());
					} else if (lists.item(j).getNodeName().equals("State")) {
						bean.setState_TX(lists.item(j).getTextContent().toString());
					} else if (lists.item(j).getNodeName().equals("Message")) {
						bean.setMessage_TX(lists.item(j).getTextContent().toString());
					}
				}
				mDatas.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void bindingData() {
		mlistview = (ListView) findViewById(R.id.lv_operatelog_detail);
		mlistview.setAdapter(mAdapter = new CommonAdapter<OperateLogBean>(
        		OperateLogDetail.this, mDatas, R.layout.listitem_operatelog_detail){

			@Override
			public void convert(com.moons.xst.track.adapter.ViewHolder helper,
					OperateLogBean bean) {
				// TODO 自动生成的方法存根
				helper.setText(R.id.listitem_operatelog_detail_itemdesc,
						"[" + bean.getUserName_TX() + "] " + bean.getOperate_TX());
				helper.setText(R.id.listitem_operatelog_time_itemdesc, bean.getTime_TX());
				helper.setText(R.id.tv_error_desc, bean.getMessage_TX());
				
				if (bean.getState_TX().equals(AppConst.LOGSTATUS_NORMAL)) {
					helper.setImageResource(R.id.operatelog_listitem_icon, R.drawable.commu_success_50dp);
					helper.setText(R.id.listitem_operatelog_state_itemdesc, getString(R.string.system_success));
					helper.setColor(R.id.listitem_operatelog_state_itemdesc, getResources().getColor(R.color.green));
				} else {
					helper.setImageResource(R.id.operatelog_listitem_icon, R.drawable.commu_fail_50dp);
					helper.setText(R.id.listitem_operatelog_state_itemdesc, getString(R.string.system_failed));
					helper.setColor(R.id.listitem_operatelog_state_itemdesc, getResources().getColor(R.color.red));			
				}
			}  
        });
	}
}