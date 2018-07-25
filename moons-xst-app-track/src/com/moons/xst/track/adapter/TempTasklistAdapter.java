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
import com.moons.xst.track.bean.GPSXXTempTask;

public class TempTasklistAdapter extends BaseAdapter{
	private Context context;// 运行上下文
	private List<GPSXXTempTask> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView title;
		public TextView crename;
		public TextView time;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public TempTasklistAdapter(Context context, List<GPSXXTempTask> data, int resource) {
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
					.findViewById(R.id.icon_point);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.tt_list_listitem_title);
			listItemView.crename = (TextView) convertView
					.findViewById(R.id.tt_list_listitem_crename);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.tt_list_listitem_time);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		GPSXXTempTask tempTask = listItems.get(position);

		listItemView.title.setText(tempTask.getTaskName_tx());
		listItemView.title.setTag(tempTask);// 设置隐藏参数(实体类)
		listItemView.crename.setText(tempTask.getCreateUserName_tx());
		listItemView.time.setText(tempTask.getCreate_dt());
		int linetypePic = R.drawable.task_finish;
		linetypePic = R.drawable.task_finish;
		if (tempTask.getComplete_dt() ==null) {
			linetypePic = R.drawable.task_unfinish;
		}
		else {
			listItemView.time.setText(tempTask.getCreate_dt()+" "+tempTask.getTaskStatus_tx());
		}
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(ImageView.VISIBLE);
		return convertView;
	}
}
