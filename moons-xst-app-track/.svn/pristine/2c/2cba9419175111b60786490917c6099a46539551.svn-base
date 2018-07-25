package com.moons.xst.track.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.Walkie_talkie_SettingInfo;
import com.moons.xst.track.common.UIHelper;

public class Walkie_Talkie_Setting_ListViewAdapter extends BaseAdapter {
	private Context context;// 运行上下文
	private List<Walkie_talkie_SettingInfo> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public TextView title;
		public TextView frequency;
		public CheckBox check;
		public RelativeLayout delete;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public Walkie_Talkie_Setting_ListViewAdapter(Context context, List<Walkie_talkie_SettingInfo> data,
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void refresh(List<Walkie_talkie_SettingInfo> list){
		listItems = list;
		this.notifyDataSetChanged();
	}
	
	public List<Walkie_talkie_SettingInfo> getlist(){
		return listItems;
	}

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView listItemView = null;
		final int selectid = position;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.title = (TextView) convertView
					.findViewById(R.id.walkie_talkie_setting_listitem_title);
			listItemView.frequency = (TextView) convertView
					.findViewById(R.id.walkie_talkie_setting_frequency);
			listItemView.check = (CheckBox) convertView
					.findViewById(R.id.walkie_talkie_setting_chk_setting);
		    listItemView.delete = (RelativeLayout) convertView
		    		.findViewById(R.id.layout_walkie_talkie_setting_deleteicon);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// 设置文字和图片
		Walkie_talkie_SettingInfo info = listItems.get(position);
		listItemView.title.setText(info.getXDName());
		listItemView.check.setChecked(info.getChecked());
		listItemView.frequency.setText(info.getFrequency());
		
		listItemView.check.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				for (Walkie_talkie_SettingInfo info : listItems){
					info.setChecked(false);
				}
				listItems.get(selectid).setChecked(true);
				notifyDataSetChanged();
			}
		});
		
		listItemView.delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//删除此条数据
				LayoutInflater factory = LayoutInflater
						.from(context);
					final View view = factory.inflate(R.layout.textview_layout, null);
					new com.moons.xst.track.widget.AlertDialog(context).builder()
					.setTitle("删除")
					.setView(view)
					.setMsg("确定要删除吗？")
					.setPositiveButton(context.getString(R.string.sure), new OnClickListener() {
						@Override
						public void onClick(View v) {
							 if (listItems.get(selectid).getChecked()) {
			        			  UIHelper.ToastMessage(context,"该信道已被选中,无法删除！");
			        			  return;
			        		  }
			        		  listItems.remove(selectid);
			        		  notifyDataSetChanged();
						}
					}).setNegativeButton(context.getString(R.string.cancel), new OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}).setCanceledOnTouchOutside(false).show();
			}
		});
		
		return convertView;
	}
}
