package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UnitHelper;

/**
 * 点检路线Adapter类
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class DJIDPos_ListViewAdapter extends BaseAdapter {
	private Context context;// 运行上下文
	private List<DJ_IDPos> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView title;
		//public TextView lastuser;
		public TextView plancount;
		public TextView lasttime;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public DJIDPos_ListViewAdapter(Context context, List<DJ_IDPos> data,
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}

	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.flagimage = (ImageView) convertView
					.findViewById(R.id.djidpos_listitem_icon);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.djidpos_listitem_title);
			listItemView.lasttime = (TextView) convertView
					.findViewById(R.id.djidpos_listitem_lastTime);
			listItemView.plancount = (TextView) convertView
					.findViewById(R.id.djidpos_listitem_planCount);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		DJ_IDPos djidpos = listItems.get(position);
		if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Normal.toString())) {
			listItemView.title.setTextAppearance(context, R.style.widget_listview_title2);
			listItemView.lasttime.setTextAppearance(context, R.style.widget_listview_subdesc);
			listItemView.plancount.setTextAppearance(context, R.style.widget_listview_subdesc);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Big.toString()))
		{
			listItemView.title.setTextAppearance(context, R.style.widget_listview_title2_big);
			listItemView.lasttime.setTextAppearance(context, R.style.widget_listview_subdesc_big);
			listItemView.plancount.setTextAppearance(context, R.style.widget_listview_subdesc_big);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Huge.toString()))
		{
			listItemView.title.setTextAppearance(context, R.style.widget_listview_title2_super);
			listItemView.lasttime.setTextAppearance(context, R.style.widget_listview_subdesc_super);
			listItemView.plancount.setTextAppearance(context, R.style.widget_listview_subdesc_super);
		}

		listItemView.title.setText(djidpos.getPlace_TX());
		listItemView.title.setTag(djidpos);// 设置隐藏参数(实体类)

		//listItemView.lastuser.setText(" ");
		if (!StringUtils.isEmpty(djidpos.getLastArrivedTime_DT()))
			listItemView.lasttime.setText(djidpos.getLastArrivedTime_DT());
		else {
			listItemView.lasttime.setText("                   ");
		}
		listItemView.plancount.setText(djidpos.getPlanCount());
		switch (djidpos.getIDPosState()) {
		case AppConst.IDPOS_STATUS_FINISHED:
			listItemView.flagimage.setImageResource(R.drawable.idpos_finish);
			break;
		case AppConst.IDPOS_STATUS_NEEDED:
			// listItemView.flagimage.setImageResource(R.drawable.idpos_need);
			listItemView.flagimage.setImageResource(R.drawable.idpos_notfinish);
			break;
		case AppConst.IDPOS_STATUS_NOTFINISHED:
			listItemView.flagimage.setImageResource(R.drawable.idpos_notfinish);
			break;
		case AppConst.IDPOS_STATUS_NOTNEEDED:
			listItemView.flagimage.setImageResource(R.drawable.idpos_notneed);
			break;
		default:
			listItemView.flagimage.setImageResource(R.drawable.idpos_finish);
			break;
		}

		return convertView;
	}
}