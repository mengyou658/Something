package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.buss.TrackHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.common.UIHelper;

public class QuerydataAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<CheckPointInfo> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private TrackHelper trackHelper = new TrackHelper();
	
	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView title;
		public TextView plancount;
		public TextView time;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public QuerydataAdapter(Context context, List<CheckPointInfo> data,
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
		final int selectid = position;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.flagimage = (ImageView) convertView
					.findViewById(R.id.icon_point);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.query_data_listitem_title);
			listItemView.plancount = (TextView) convertView
					.findViewById(R.id.query_data_listitem_plancount);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.query_data_listitem_lng);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		CheckPointInfo cpoint = listItems.get(position);

		listItemView.title.setText(cpoint.getDesc_TX());
		listItemView.title.setTag(cpoint);// 设置隐藏参数(实体类)
		trackHelper.loadGPSXXPlanData(context, String.valueOf(cpoint.getGPSPosition_ID()));
		if (AppContext.gpsXXPlanBuffer.containsKey(String.valueOf(cpoint.getGPSPosition_ID()))) {
			Log.e("", String.valueOf(cpoint.getGPSPosition_ID()));
			int size = AppContext.gpsXXPlanBuffer.get(String.valueOf(cpoint.getGPSPosition_ID())).size();
			listItemView.plancount.setText(context.getString(R.string.plan_count) + size);
		}
		else {
			listItemView.plancount.setText(context.getString(R.string.plan_count) + "0");
		}
		listItemView.time.setText(cpoint.getKHDate_DT());
		int linetypePic = R.drawable.icon_cp;
		linetypePic = R.drawable.icon_cp;
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(ImageView.VISIBLE);
		/*listItemView.hisresult.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				UIHelper.showQueryDataCPHisResult(context, 
						String.valueOf(listItems.get(selectid).getGPSPosition_ID()));
			}
		});*/
		return convertView;
	}
}
