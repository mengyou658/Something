package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.adapter.TrackinitAdapter.ListItemView;
import com.moons.xst.track.bean.MobjectBugCodeInfo;

public class InputBugAdapter extends BaseAdapter{
	private Context 					context;//运行上下文
	private List<MobjectBugCodeInfo> 	listItems;//数据集合
	private LayoutInflater 			    listContainer;//视图容器
	private int 						itemViewResource;//自定义项视图源 
	
	public InputBugAdapter(Context context, List<MobjectBugCodeInfo> data,int resource){
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}
	
	@Override
	public int getCount() {
		return listItems.size();
	}
	@Override
	public Object getItem(int arg0) {
		return null;
	}
	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//自定义视图
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//获取控件对象
			listItemView.flagimage= (ImageView)convertView.findViewById(R.id.disclosureImg);
			listItemView.title = (TextView)convertView.findViewById(R.id.contentText);
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}	
		
		//设置文字和图片
		MobjectBugCodeInfo mobjectbugcode = listItems.get(position);
		
		listItemView.title.setText(mobjectbugcode.getMOBJECTBUGCODE_TX());
		listItemView.title.setTag(mobjectbugcode);//设置隐藏参数(实体类)
		int linetypePic =R.drawable.widget_bar_xj_line;
		linetypePic =R.drawable.icon_point;
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(ImageView.VISIBLE);
		return convertView;
	}
}
