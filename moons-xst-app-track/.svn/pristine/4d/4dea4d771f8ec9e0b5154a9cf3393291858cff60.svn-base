package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;

public class offmap_searchAdapter extends BaseExpandableListAdapter{
	private Context context;// 运行上下文
	private List<MKOLSearchRecord> falistItems;// 父数据集合
	//private List<MKOLSearchRecord> chlistItems;// 子数据集合
	private LayoutInflater listContainer;// 视图容器
	private int faitemViewResource;// 父自定义项视图源
	private int chitemViewResource;// 子自定义项视图源

	static class chListItemView { // 自定义控件集合，与offmap_listitem布局一致
		public ImageView chflagimage;
		public TextView chtitle;
		public TextView chsize;
		public TextView chstate;
	}
	static class faListItemView { // 自定义控件集合，与offmap_listitem布局一致
		public ImageView faflagimage;
		public TextView fatitle;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public offmap_searchAdapter(Context context, List<MKOLSearchRecord> data,int faresource,int chresource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.faitemViewResource = faresource;
		this.chitemViewResource = chresource;
		this.falistItems = data;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return falistItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if (falistItems.get(groupPosition).childCities !=null) {
			return falistItems.get(groupPosition).childCities.size();
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return falistItems.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		if (falistItems.get(groupPosition).childCities !=null) {
			return falistItems.get(groupPosition).childCities.get(childPosition);
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MKOLSearchRecord Record = falistItems.get(groupPosition);
		if (Record.childCities != null) {
			// 自定义视图
			faListItemView listItemView = null;
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.faitemViewResource, null);

			listItemView = new faListItemView();
			// 获取控件对象
			listItemView.fatitle = (TextView) convertView
					.findViewById(R.id.faoffmap_listitem_title);
			listItemView.faflagimage = (ImageView) convertView
					.findViewById(R.id.faoffmap_listitem_image);
			if (isExpanded)
				listItemView.faflagimage.setImageResource(R.drawable.icon_right);
				else
				listItemView.faflagimage.setImageResource(R.drawable.icon_down);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
			listItemView.fatitle.setText(Record.cityName);
		}else {
			// 自定义视图
			chListItemView listItemView = null;
				// 获取list_item布局文件的视图
				convertView = listContainer.inflate(this.chitemViewResource, null);

				listItemView = new chListItemView();
				// 获取控件对象
				listItemView.chtitle = (TextView) convertView
						.findViewById(R.id.offmap_listitem_title);
				listItemView.chsize = (TextView) convertView
						.findViewById(R.id.offmap_listitem_size);
				listItemView.chstate=(TextView) convertView
						.findViewById(R.id.offmap_listitem_state);
				// 设置控件集到convertView
				convertView.setTag(listItemView);
			String ret = "";
			if (Record.size < (1024 * 1024)) {
				ret = String.format("%dK", Record.size / 1024);
			} else {
				ret = String.format("%.1fM", Record.size / (1024 * 1024.0));
			}
			String state="";
			for (MKOLUpdateElement element:AppContext.localMapList) {
				if (element.cityID == Record.cityID) {
					if (element.ratio == 100) {
						listItemView.chstate.setTextColor(Color.BLACK);
						state=context.getString(R.string.offmap_already_download);
					}
					else {
						listItemView.chstate.setTextColor(Color.RED);
						state=context.getString(R.string.offmap_underway_download)+String.valueOf(element.ratio)+"%";		
					}	
				}
			}
			listItemView.chtitle.setText(Record.cityName);
			listItemView.chsize.setText(ret);
			listItemView.chstate.setText(state);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 自定义视图
		chListItemView listItemView = null;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.chitemViewResource, null);

			listItemView = new chListItemView();
			// 获取控件对象
			listItemView.chtitle = (TextView) convertView
					.findViewById(R.id.offmap_listitem_title);
			listItemView.chsize = (TextView) convertView
					.findViewById(R.id.offmap_listitem_size);
			listItemView.chstate=(TextView) convertView
					.findViewById(R.id.offmap_listitem_state);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (chListItemView) convertView.getTag();
		}
		MKOLSearchRecord Record = falistItems.get(groupPosition).childCities.get(childPosition);
		String ret = "";
		if (Record.size < (1024 * 1024)) {
			ret = String.format("%dK", Record.size / 1024);
		} else {
			ret = String.format("%.1fM", Record.size / (1024 * 1024.0));
		}
		String state="";
		for (MKOLUpdateElement element:AppContext.localMapList) {
			if (element.cityID == Record.cityID) {
				if (element.ratio == 100) {
					state=context.getString(R.string.offmap_already_download);
					listItemView.chstate.setTextColor(Color.BLACK);
				}
				else {
					state=context.getString(R.string.offmap_underway_download)+String.valueOf(element.ratio)+"%";
					listItemView.chstate.setTextColor(Color.RED);
				}	
			}
		}
		listItemView.chtitle.setText(Record.cityName);
		listItemView.chsize.setText(ret);
		listItemView.chstate.setText(state);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
//		Toast.makeText(context,
//				"第" + groupPosition + "大项，第" + childPosition + "小项被点击了",
//				Toast.LENGTH_SHORT).show();
		return true;
	}
	
}
