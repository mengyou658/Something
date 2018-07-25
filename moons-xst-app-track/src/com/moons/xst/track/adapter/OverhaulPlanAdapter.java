package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.OverhaulPlan;

public class OverhaulPlanAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<OverhaulPlan> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public TextView overhaulPlanItemCoding, overhaulPlanItemName,
				overhaulPlanItemContent;
		public ImageView overhaulPlanItemIcon;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public OverhaulPlanAdapter(Context context, List<OverhaulPlan> data,
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = data;
		this.itemViewResource = resource;
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

	public void refresh(List<OverhaulPlan> list) {
		listItems = list;
		this.notifyDataSetChanged();
	}

	public List<OverhaulPlan> getlist() {
		return listItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		// 自定义视图
		ListItemView listItemView;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();

			listItemView.overhaulPlanItemCoding = (TextView) convertView
					.findViewById(R.id.overhaul_item_coding);
			listItemView.overhaulPlanItemName = (TextView) convertView
					.findViewById(R.id.overhaul_item_name);
			listItemView.overhaulPlanItemContent = (TextView) convertView
					.findViewById(R.id.overhaul_item_content);
			listItemView.overhaulPlanItemIcon = (ImageView) convertView
					.findViewById(R.id.icon_overhaul_plan);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		OverhaulPlan overhaulPlan = listItems.get(position);
		listItemView.overhaulPlanItemCoding.setText(overhaulPlan.getJXPlan_CD());
		listItemView.overhaulPlanItemName
				.setText(overhaulPlan.getPlanName_TX());
		listItemView.overhaulPlanItemContent.setText(overhaulPlan
				.getPlanContent_TX());
		if(overhaulPlan.getisPlanFinished()){
			listItemView.overhaulPlanItemIcon
					.setImageResource(R.drawable.widget_bar_operatebill_complete_xh);
		}else {
			listItemView.overhaulPlanItemIcon
			.setImageResource(R.drawable.widget_bar_overhaulplan_uncomplete_xh);
		}
		return convertView;
	}
}
