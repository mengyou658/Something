package com.moons.xst.track.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenu;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuCreator;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuItem;
import com.moons.xst.track.widget.SwipeMenu.SwipeMenuListView;

/**
 * 点检路线列表Adapter类
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class DJLine_ListViewAdapter extends BaseAdapter {
	private Context context;// 运行上下文
	private List<DJLine> listItems=new ArrayList<DJLine>();// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private SwipeMenuListView listview;

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView title;
		// public TextView idcount;
		// public TextView plancount;
		public TextView buildtime;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public DJLine_ListViewAdapter(Context context, List<DJLine> data,
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}

	public DJLine_ListViewAdapter(Context context, List<DJLine> data,
			int resource, SwipeMenuListView listview) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listview = listview;
		this.listItems = data;
	}

	public List<DJLine> getData() {
		return listItems;
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
			listItemView.title = (TextView) convertView
					.findViewById(R.id.djline_listitem_title);
			listItemView.buildtime = (TextView) convertView
					.findViewById(R.id.djline_listitem_BuildTime);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		DJLine djline = listItems.get(position);

		if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Normal.toString())) {
			listItemView.title.setTextAppearance(context,
					R.style.widget_listview_title2);
			listItemView.buildtime.setTextAppearance(context,
					R.style.widget_listview_subdesc);
		} else if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Big.toString())) {
			listItemView.title.setTextAppearance(context,
					R.style.widget_listview_title2_big);
			listItemView.buildtime.setTextAppearance(context,
					R.style.widget_listview_subdesc_big);
		} else if (AppContext.getFontSize().equalsIgnoreCase(
				AppConst.FontSizeType.Huge.toString())) {
			listItemView.title.setTextAppearance(context,
					R.style.widget_listview_title2_super);
			listItemView.buildtime.setTextAppearance(context,
					R.style.widget_listview_subdesc_super);
		}

		listItemView.title.setText(djline.getLineName());

		listItemView.title.setTag(djline);// 设置隐藏参数(实体类)
		// 纽扣数量暂时不显示
		// listItemView.idcount.setText("");
		if (djline.getBuidYN())
			listItemView.buildtime.setText(StringUtils.formatDateTime(djline
					.getBuildTime()));
		else
			listItemView.buildtime
					.setText(R.string.line_list_buildTime_noBuild);

		// 计划数量暂时不显示
		// listItemView.plancount.setText("");
		int linetypePic = R.drawable.widget_bar_xj_line_xh;
		switch (djline.getlineType()) {
		case 0:
			linetypePic = R.drawable.widget_bar_xj_line_xh;
			break;
		case 1:
			linetypePic = R.drawable.widget_bar_djpc_line_xh;
			break;
		case 2:
			linetypePic = R.drawable.widget_bar_xg_line;
			break;
		case 3:
			linetypePic = R.drawable.widget_bar_jm_line;
			break;
		case 4:
			linetypePic = R.drawable.widget_bar_xs_line_xh;
			break;
		case 5:
			linetypePic = R.drawable.widget_bar_sis_line;
			break;
		case 6:
			linetypePic = R.drawable.widget_bar_xx_line_xh;
			break;
		case 7:
			linetypePic = R.drawable.widget_bar_xx_line_xh;
			break;
		case 8:
			linetypePic = R.drawable.widget_bar_case_line_xh;
			break;
		default:
			linetypePic = R.drawable.widget_bar_xj_line_xh;
			break;
		}
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(ImageView.VISIBLE);

		if (listview != null) {

			// 创建一个MenuCreator
			SwipeMenuCreator creator = new SwipeMenuCreator() {

				@Override
				public void create(SwipeMenu menu) {

					/*
					 * SwipeMenuItem cancelItem = new SwipeMenuItem(
					 * context.getApplicationContext());
					 * cancelItem.setBackground(R.color.cancelmenubg_gray);
					 * cancelItem.setHeight(dp2px(64));
					 * cancelItem.setWidth(dp2px(65));
					 * cancelItem.setTitle(context.getString(R.string.cancel));
					 * cancelItem.setTitleSize(18);
					 * cancelItem.setTitleColor(Color.WHITE);
					 * menu.addMenuItem(cancelItem);
					 */

					SwipeMenuItem openItem1 = new SwipeMenuItem(
							context.getApplicationContext());
					openItem1.setBackground(R.color.red);
					openItem1.setHeight(dp2px(64));
					openItem1.setWidth(dp2px(75));
					openItem1.setTitle(context
							.getString(R.string.main_menu_tools_camera_delete));
					openItem1.setTitleSize(15);
					openItem1.setTitleColor(Color.WHITE);
					openItem1.setIcon(R.drawable.ic_delete);
					menu.addMenuItem(openItem1);

				}
			};
			listview.setMenuCreator(creator,false);
		}
		return convertView;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
}