package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.OperateBillBaseInfo;
import com.moons.xst.track.common.StringUtils;

public class Comm_OperateBillInfo_ListViewAdapter extends BaseAdapter {
	
	private Context context;// 运行上下文
	private List<OperateBillBaseInfo> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	
	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public TextView operatebill_code;
		public TextView operatebill_content;
		public CheckBox check;
	}
	
	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public Comm_OperateBillInfo_ListViewAdapter(Context context, List<OperateBillBaseInfo> data,
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
	
	public List<OperateBillBaseInfo> getlist(){
		return listItems;
	}
	
	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.operatebill_code = (TextView) convertView
					.findViewById(R.id.download_operatebill_listitem_operatebill_code);
			listItemView.operatebill_content = (TextView) convertView
					.findViewById(R.id.download_operatebill_listitem_operatebill_content);
			listItemView.check = (CheckBox) convertView
					.findViewById(R.id.download_operatebill_chk_setting);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// 设置文字和图片
		OperateBillBaseInfo info = listItems.get(position);
		listItemView.operatebill_code.setText(info.getOperateBill_Code());
		listItemView.operatebill_content.setText(info.getOperateBill_Content());
		listItemView.check.setChecked(StringUtils.toBool(info.getChecked()));
		
		listItemView.check.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (((CheckBox)v).isChecked()) {
					listItems.get(position).setChecked("true");
				} else {
					listItems.get(position).setChecked("false");
				}
			}
		});
		
		return convertView;
	}
}