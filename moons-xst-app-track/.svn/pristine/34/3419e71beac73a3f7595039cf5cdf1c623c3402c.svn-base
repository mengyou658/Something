package com.moons.xst.track.adapter;

import java.util.List;

import com.moons.xst.track.R;
import com.moons.xst.track.adapter.OperateAdapter.ListItemView;
import com.moons.xst.track.bean.Work_Bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkBillAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<Work_Bill> mList;
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	public WorkBillAdapter(Context context, List<Work_Bill> list, int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.mList = list;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ListItemView listItemView;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();

			listItemView.operateid = (TextView) convertView
					.findViewById(R.id.tv_operate_id);
			listItemView.content = (TextView) convertView
					.findViewById(R.id.tv_operate_content);
			listItemView.name = (TextView) convertView
					.findViewById(R.id.tv_operate_name);
			listItemView.iv_operate_state = (ImageView) convertView
					.findViewById(R.id.iv_operate_state);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		Work_Bill entity = mList.get(position);
		listItemView.operateid.setText(entity.getWork_Bill_Code());
		listItemView.content.setText(entity.getWork_Bill_TaskContent_TX());
		//listItemView.name.setText(entity.getWork_Bill_Operator());
		if (entity.getJD_ID() == 2) {
			listItemView.iv_operate_state
				.setImageResource(R.drawable.widget_bar_operatebill_complete_xh);
		} else if (entity.getJD_ID() == 1) {
			listItemView.iv_operate_state
				.setImageResource(R.drawable.widget_bar_workbill_unsign_xh);
		} else {
			listItemView.iv_operate_state
				.setImageResource(R.drawable.widget_bar_workbill_uncomplete_xh);
		}

		return convertView;
	}

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public TextView operateid;
		public TextView content;
		public TextView name;
		public ImageView iv_operate_state;
	}
}
