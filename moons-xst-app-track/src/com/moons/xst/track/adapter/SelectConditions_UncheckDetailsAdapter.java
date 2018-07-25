package com.moons.xst.track.adapter;

import java.util.List;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.Operate_Bill;
import com.moons.xst.track.bean.Operate_Detail_Bill;
import com.moons.xst.track.common.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectConditions_UncheckDetailsAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<String[]> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView icon;
		public TextView content;
		public ImageView checked;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public SelectConditions_UncheckDetailsAdapter(Context context, List<String[]> data,
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = data;
		this.itemViewResource = resource;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	public List<String[]> getlist() {
		return listItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		// 自定义视图
		ListItemView listItemView ;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();

			listItemView.icon = (ImageView) convertView
					.findViewById(R.id.selectfilter_idpos_icon);
			listItemView.content = (TextView) convertView
					.findViewById(R.id.selectfilter_idpos_desc);
			listItemView.checked = (ImageView) convertView
					.findViewById(R.id.selectfilter_idpos_selected);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		String desc = listItems.get(position)[0].toString();
		if (desc.equalsIgnoreCase(context.getString(R.string.query_select_conditions_all))) {
			listItemView.icon.setVisibility(View.INVISIBLE);
			listItemView.content.setTextColor(context.getResources().getColor(R.color.xstblue));
		} else {
			listItemView.icon.setVisibility(View.VISIBLE);
			listItemView.content.setTextColor(context.getResources().getColor(R.color.black));
		}

		listItemView.content.setText(desc);
		boolean b = StringUtils.toBool(listItems.get(position)[1].toString());
		if (b) {
			listItemView.checked.setVisibility(View.VISIBLE);
		} else {
			listItemView.checked.setVisibility(View.GONE);
		}
		
		return convertView;
	}
}
