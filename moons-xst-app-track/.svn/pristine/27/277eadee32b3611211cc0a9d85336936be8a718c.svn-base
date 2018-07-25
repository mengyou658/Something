package com.moons.xst.track.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.TrackHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.CheckPointInfo;

public class MapPointAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<CheckPointInfo> listItems = new ArrayList<CheckPointInfo>();// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private TrackHelper trackHelper = new TrackHelper();

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView title;
		public TextView time;
		public TextView spendtime;
		public TextView distance;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public MapPointAdapter(Context context, List<CheckPointInfo> data,
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getLatitude().toString().compareTo("0") != 0)
				this.listItems.add(data.get(i));
		}

		// this.listItems = data;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listItems.get(arg0);
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
					.findViewById(R.id.point_listitem_title);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.point_listitem_time);
			listItemView.spendtime = (TextView) convertView
					.findViewById(R.id.point_listitem_spendCount);
			listItemView.distance = (TextView) convertView
					.findViewById(R.id.point_listitem_distanceTxt);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		CheckPointInfo cpoint = listItems.get(position);

		listItemView.title.setText(cpoint.getDesc_TX());
		listItemView.title.setTag(cpoint);// 设置隐藏参数(实体类)
		listItemView.spendtime.setText(context.getString(R.string.mapmain_message_spendtime, String.valueOf(cpoint
				.getNextPointHour_NR())));
		trackHelper.loadGPSXXPlanData(context, String.valueOf(cpoint.getGPSPosition_ID()));
		if (AppContext.gpsXXPlanBuffer.containsKey(String.valueOf(cpoint.getGPSPosition_ID()))) {
			Log.e("", String.valueOf(cpoint.getGPSPosition_ID()));
			int size = AppContext.gpsXXPlanBuffer.get(String.valueOf(cpoint.getGPSPosition_ID())).size();
			listItemView.time.setText(context.getString(R.string.mapmain_message_tasksum,size));
		}
		else {
			listItemView.time.setText(context.getString(R.string.mapmain_message_tasksum,0));
		}
		String distance = "";
		if (!StringUtils.isEmpty(cpoint.getNearDistance())) {
			int nearDistance = Integer.valueOf(cpoint.getNearDistance());
			if (nearDistance >= 1000)
				distance = context.getString(R.string.mapmain_message_kilometrewithin, new DecimalFormat("###.00")
				.format((nearDistance / 1000.00)));
			else {
				distance = context.getString(R.string.mapmain_message_meterwithin, nearDistance);
			}
		} else {
			distance = context.getString(R.string.mapmain_message_calculateunderway);
		}
//		if (!StringUtils.isEmpty(cpoint.getNearDistance())) {
//			int nearDistance = Integer.valueOf(cpoint.getNearDistance());
//			if (nearDistance >= 1000)
//				distance ="<"+ new DecimalFormat("###.00")
//						.format((nearDistance / 1000.00)) + "km";
//			else if(nearDistance >= 900){
//				distance = "<1km";
//			}else{
//				distance = "<900m";
//			}
//		} else {
//			distance = "计算中";
//		}
		listItemView.distance.setText(distance);
		int linetypePic = R.drawable.icon_marka;
		if (cpoint.getCheckPoint_Type().toUpperCase().equals("P")) {
			linetypePic = R.drawable.icon_marka;
			if (cpoint.getShakeNum() > 0)
				linetypePic = R.drawable.icon_marka_green;
		} else {
			linetypePic = R.drawable.icon_nfc_red;
			if (cpoint.getShakeNum() > 0)
				linetypePic = R.drawable.icon_nfc_green;
		}
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(ImageView.VISIBLE);
		return convertView;
	}
}
