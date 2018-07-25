package com.moons.xst.track.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;

public class SimpleSpinnerAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<List<String>> listItems = new ArrayList<List<String>>();// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView {
		public ImageView flagimage;
		public TextView title;
		public TextView spinnerTX;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public SimpleSpinnerAdapter(Context context, List<List<String>> data,
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
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.flagimage = (ImageView) convertView
					.findViewById(R.id.listitem_spinner_icon);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.listitem_spinner_itemdesc1);
			listItemView.spinnerTX = (TextView) convertView
					.findViewById(R.id.listitem_spinner_itemdesc2);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		final List<String> _data = listItems.get(position);
		if (!StringUtils.isEmpty(_data.get(1)))
			listItemView.title.setText(_data.get(1));
		else {
			listItemView.title.setText("");
		}
		listItemView.title.setTag(_data);
		if (!StringUtils.isEmpty(_data.get(2)))
			listItemView.spinnerTX.setText(_data.get(2));
		else {
			listItemView.spinnerTX.setText("");
		}
		listItemView.flagimage.setImageResource(R.drawable.icon_sr);
		return convertView;
	}
}
