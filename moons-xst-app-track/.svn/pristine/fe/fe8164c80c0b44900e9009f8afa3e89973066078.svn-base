package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJIDPosResult;
import com.moons.xst.track.common.StringUtils;

/**
 * 点检到位记录Adapter类
 * @author gaojun
 * @version 1.0
 * @created 2014-9-28
 */
public class DJIDPosResult_ListViewAdapter extends BaseAdapter {
	private Context 					context;//运行上下文
	private List<DJIDPosResult> 			listItems;//数据集合
	private LayoutInflater 			listContainer;//视图容器
	private int 							itemViewResource;//自定义项视图源 
	static class ListItemView{				//自定义控件集合，与djline_listitem布局一致
	        public TextView title;  
		    public TextView timerange;
		    public TextView timecount;
		    public TextView appusername;
		    public TextView shiftname;
		    public TextView groupname;
	 }  

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public DJIDPosResult_ListViewAdapter(Context context, List<DJIDPosResult> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}
	
	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}
	
	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		//自定义视图
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//获取控件对象
			listItemView.title = (TextView)convertView.findViewById(R.id.djidposresult_listitem_title);
			listItemView.timerange = (TextView)convertView.findViewById(R.id.djidposresult_listitem_timerange);
			listItemView.timecount = (TextView)convertView.findViewById(R.id.djidposresult_listitem_timecount);
			listItemView.appusername = (TextView)convertView.findViewById(R.id.djidposresult_listitem_appuser);
			listItemView.shiftname = (TextView)convertView.findViewById(R.id.djidposresult_listitem_shiftname);
			listItemView.groupname = (TextView)convertView.findViewById(R.id.djidposresult_listitem_groupname);
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}	
		
		//设置文字和图片
		DJIDPosResult djidposresult = listItems.get(position);
		
		listItemView.title.setText(djidposresult.getIDPosName());
		listItemView.title.setTag(djidposresult);//设置隐藏参数(实体类)

		listItemView.timerange.setText(StringUtils.formatTime(djidposresult.getStartTime()) + "-" + StringUtils.formatTime(djidposresult.getEndTime()));
		listItemView.timecount.setText(StringUtils.formatTimeCount(djidposresult.getTimeCount()));
		listItemView.appusername.setText(djidposresult.getAppUserName());
		listItemView.shiftname.setText(djidposresult.getShiftName());
		listItemView.groupname.setText(djidposresult.getGroupName());
	
		return convertView;
	}
}