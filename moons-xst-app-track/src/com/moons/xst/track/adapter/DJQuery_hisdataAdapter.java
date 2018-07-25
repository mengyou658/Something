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
import com.moons.xst.track.bean.DJ_TaskIDPosHis;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;

public class DJQuery_hisdataAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<XJ_TaskIDPosHis> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView title;
		public TextView tex;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public DJQuery_hisdataAdapter(Context context, List<XJ_TaskIDPosHis> data,
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
					.findViewById(R.id.icon_hisdata);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.hisdata_listitem_title);
			listItemView.tex = (TextView) convertView
					.findViewById(R.id.hisdata_listitem_distanceTxt);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		XJ_TaskIDPosHis Result = listItems.get(position);

		listItemView.title.setText(Result.getAppUserName_TX());
		listItemView.title.setTag(Result);// 设置隐藏参数(实体类)
		listItemView.tex.setText(Result.getIDPosStart_TM());
		int linetypePic = R.drawable.idpos_finish;

		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(ImageView.VISIBLE);
		return convertView;
	}
}
