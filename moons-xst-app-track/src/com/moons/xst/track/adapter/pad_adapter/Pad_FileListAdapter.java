/**
 * 
 */
package com.moons.xst.track.adapter.pad_adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.FileInfo;

/**
 * @author LKZ
 * 
 */
public class Pad_FileListAdapter extends BaseAdapter {
	private Context context;// 运行上下文
	private List<FileInfo> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView fileName;
		public TextView creater;
		public TextView plancount;
		public TextView buildtime;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public Pad_FileListAdapter(Context context, List<FileInfo> data,
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
					.findViewById(R.id.djline_listitem_icon);
			listItemView.fileName = (TextView) convertView
					.findViewById(R.id.djline_listitem_title);
			listItemView.buildtime = (TextView) convertView
					.findViewById(R.id.djline_listitem_BuildTime);
			listItemView.creater = (TextView) convertView
					.findViewById(R.id.djline_listitem_IDCount);
			listItemView.plancount = (TextView) convertView
					.findViewById(R.id.djline_listitem_PlanCount);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		FileInfo fileInfo = listItems.get(position);

		listItemView.fileName.setText(fileInfo.getFileName());
		listItemView.fileName.setTag(fileInfo);// 设置隐藏参数(实体类)
		// 纽扣数量暂时不显示
		listItemView.creater.setText("");
		listItemView.buildtime.setText(fileInfo.getFileTime());
		;
		// 计划数量暂时不显示
		listItemView.plancount.setText("");
		int linetypePic = R.drawable.pad_file_icon;
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(View.VISIBLE);
		return convertView;
	}

}
