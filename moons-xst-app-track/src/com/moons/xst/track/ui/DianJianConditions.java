package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.DJConditionHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;
import com.moons.xst.track.bean.Z_Condition;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.UIHelper;

public class DianJianConditions extends BaseActivity {
	
	// 当前的路线ID
	private int current_djlineID = 0;
	private String current_djlineName = "";
	private String mParentCondition = "0";
	private String mConditionLevel = "0";
	CommonAdapter mAdapter;
	static List<Z_Condition> list = new ArrayList<Z_Condition>();
	static List<Z_Condition> array = new ArrayList<Z_Condition>();
	Map<Integer, Z_Condition> tempMap = new HashMap<Integer, Z_Condition>();
	Z_Condition info;
	
	TextView head_title,condition;
	ListView mlistview;
	ImageButton returnButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dianjian_conditions);
		if (savedInstanceState != null) {
			current_djlineID = savedInstanceState.getInt("current_djlineID");
			current_djlineName = savedInstanceState.getString("current_djlineName");
			mParentCondition = savedInstanceState.getString("mParentCondition");
		} else {
			current_djlineID = Integer.parseInt(getIntent().getExtras().getString("line_id"));
			current_djlineName = getIntent().getStringExtra("line_name");
			mParentCondition = getIntent().getStringExtra("parentcondition");
		}
		
		head_title = (TextView) findViewById(R.id.conditions_title);
		head_title.setText(current_djlineName);
		condition = (TextView) findViewById(R.id.dianjian_conditions_condition);
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				back();
			}
		});
		
		
		RefreshListView("NEXT");
			
		mlistview = (ListView) findViewById(R.id.conditions_listview);
		mlistview.setAdapter(mAdapter = new CommonAdapter<Z_Condition>(
        		DianJianConditions.this, array, R.layout.listitem_conditions){

			@Override
			public void convert(com.moons.xst.track.adapter.ViewHolder helper,
					Z_Condition item) {
				// TODO 自动生成的方法存根
				helper.setText(R.id.conditions_desc, item.getConValue_TX());
				if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Normal.toString())) {
					helper.setStyle(DianJianConditions.this,
							R.id.conditions_desc, 
							R.style.widget_listview_title2);
				}
				else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Big.toString())) {
					helper.setStyle(DianJianConditions.this,
							R.id.conditions_desc, 
							R.style.widget_listview_title2_big);
				}
				else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Huge.toString())) {
					helper.setStyle(DianJianConditions.this,
							R.id.conditions_desc, 
							R.style.widget_listview_title2_super);
				}
			}  
        });
		
		mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Z_Condition condition = (Z_Condition)(mAdapter.getItem(position));
				mParentCondition = condition.getCondition_id();
				if (condition.getConType_TX().equals("CASE"))
					AppConst.ConditionStr = condition.getConLevel_TX();
				if (condition.getConType_TX().equals("TIME")) {
					AppConst.PlanTimeStr = condition.getConValue_TX();
	                String timelevelstr = condition.getConLevel_TX();
	                if (!StringUtils.isEmpty(timelevelstr) && timelevelstr.split("\\|").length > 0)
	                	AppConst.PlanTimeIDStr = timelevelstr.split("\\|")[timelevelstr.split("\\|").length - 1];
				}
				RefreshListView("NEXT");
				mAdapter.notifyDataSetChanged();
			}			
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onRestoreInstanceState(savedInstanceState);
		savedInstanceState.putInt("current_djlineID", current_djlineID);
		savedInstanceState.putString("current_djlineName", current_djlineName);
		savedInstanceState.putString("mParentCondition", mParentCondition);
	}
	
	private void RefreshListView(String type) {
         try
         {
        	 DJConditionHelper.GetIntance().loadDJCondition(getApplication());
             if (AppContext.conditionBuffer.size() > 0)
             {
                 if (type == "NEXT")
                     InitNextLevelCondition(mParentCondition);
                 else if (type == "PRE")
                     InitPreLevelCondition(String.valueOf(Integer.parseInt(mConditionLevel) - 1));
                 
                 condition.setText(array.get(0).getConName_TX());
             }
         }
         catch (Exception ex)
         {
        	 ex.printStackTrace();
         }
     }
	 
	 /**
	  * 显示下一层
	  * @param parentId
	  */
     private void InitNextLevelCondition(String parentId) {
         try
         {
        	 list.clear();
        	 for (Z_Condition ZC : AppContext.conditionBuffer) {
        		 if (ZC.getParentCon_id().equals(parentId))
        			 list.add(ZC);
        	 }
             if (list.size() > 0)
             {
            	 array.clear();
                 mConditionLevel = list.get(0).getLevelNum_TX();
                                 
                 if (list.get(0).getConType_TX().equalsIgnoreCase("TIME"))
                 {
                     for (Z_Condition c : list)
                     {
                         String begintime, endtime;
                         if (!StringUtils.isEmpty(c.getConValue_TX())
                             && c.getConValue_TX().split("~").length == 2)
                         {
                             String datetimenow = DateTimeHelper.getDateTimeNow();
                             begintime = c.getConValue_TX().split("~")[0].replace("/", "-");
                             endtime = c.getConValue_TX().split("~")[1].replace("/", "-");
                             String temp = DateTimeHelper.DateToString(DateTimeHelper.StringToDate(begintime)
                            		 , "MM-dd HH:mm") 
                            		 + "~"
                            		 + DateTimeHelper.DateToString(DateTimeHelper.StringToDate(endtime)
                            		 , "MM-dd HH:mm");
                             c.setConValue_TX(temp);
                             if (DateTimeHelper.StringToDate(datetimenow).after(DateTimeHelper.StringToDate(begintime)))
                             {
                            	 long timespan = DateTimeHelper.StringToDate(datetimenow).getTime() - 
                            			 DateTimeHelper.StringToDate(begintime).getTime();
                            	 int totalMinutes = (int)(timespan / 60000);
                            	 tempMap.put(totalMinutes, c);
                             }
                         }
                     }
                     int min = 0;
                     Iterator it = tempMap.entrySet().iterator();
                     while (it.hasNext()) {
                    	 Entry<Integer, Z_Condition> entry = (Entry<Integer, Z_Condition>) it.next();
                    	 if (min == 0)
                    		 min = Integer.parseInt(String.valueOf(entry.getKey()));
                    	 else
                    		 min = Integer.parseInt(String.valueOf(entry.getKey())) <= min ? 
                    				 Integer.parseInt(String.valueOf(entry.getKey())) : min;
                     }
                     if (min != 0)
                         array.add(tempMap.get(min));
                 }
                 else
                 {
                     for (Z_Condition z : list)
                     {
                         array.add(z);
                     }
                 }
                 tempMap.clear();
             }
             else if (list.size() == 0)
             {
                 /* 进入碰钮扣界面 */
            	 AppContext.setCondition(array.get(0).getConValue_TX());
            	 UIHelper.showDianjianTouchIDPos(DianJianConditions.this,
            			 current_djlineID, current_djlineName + "(" + array.get(0).getConValue_TX() + ")", false);
             }
         }
         catch (Exception ex)
         {
        	 ex.printStackTrace();
         }
     }
     
     /**
      * 返回上一层
      * @param levelId
      * @return
      */
     private void InitPreLevelCondition(String levelId) {
         try
         {
        	 list.clear();
        	 for (Z_Condition ZC : AppContext.conditionBuffer) {
        		 if (ZC.getLevelNum_TX().equals(levelId))
        			 list.add(ZC);
        	 }
             if (list.size() > 0)
             {
            	 array.clear();
            	 for (Z_Condition z : list)
                 {
                     array.add(z);
                 }
            	 mConditionLevel = levelId;
             }
             else
            	 AppManager.getAppManager().finishActivity(DianJianConditions.class);
         }
         catch (Exception ex)
         {
        	 ex.printStackTrace();
        	 AppManager.getAppManager().finishActivity(DianJianConditions.class);
         }
     }
     
     private void back(){
    	array.clear();
		RefreshListView("PRE");
		mAdapter.notifyDataSetChanged();
     }
}