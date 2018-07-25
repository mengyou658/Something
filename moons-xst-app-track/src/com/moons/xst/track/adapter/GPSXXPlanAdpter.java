package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.GPSXXPlan;

/**
 * 点检路线列表Adapter类
 * 
 * @author lkz
 * @version 1.0
 * @created 2015-10-15
 */
public class GPSXXPlanAdpter extends BaseAdapter {
	private Context context;// 运行上下文
	private List<GPSXXPlan> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	
	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public TextView planTitle;
		public TextView planBaseDate;
		public TextView planBaseType;
		public TextView planDesc;
		public TextView planPipeInfo;
		public TextView planResult;
		public TextView planBugType;
		public TextView planMemo;
		public TextView planEvent;
		public Button saveBtn;
		private RelativeLayout layout;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public GPSXXPlanAdpter(Context context, List<GPSXXPlan> data, int resource) {
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
			listItemView.planTitle = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_desc);
			listItemView.planBaseType = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_basedetail_cptype);
			listItemView.planBaseDate = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_basedetail_cpdate);
			listItemView.planDesc = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_detail_plandesc);
			listItemView.planPipeInfo = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_detail_gxxx);
			listItemView.planResult = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_detail_result);
			listItemView.planBugType = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_detail_bugtype);
			listItemView.planMemo = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_detail_meno);
			listItemView.planEvent = (TextView) convertView
					.findViewById(R.id.cp_plan_listitem_detail_event);
			listItemView.saveBtn = (Button) convertView
					.findViewById(R.id.btn_gpsxx_plan_ok);
			listItemView.layout = (RelativeLayout)convertView.findViewById(R.id.layout_cp_plan_listitem_detail);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		GPSXXPlan gpsPlan = listItems.get(position);

		listItemView.planTitle.setText(gpsPlan.getName_TX());
		listItemView.planTitle.setTag(gpsPlan);// 设置隐藏参数(实体类)
		listItemView.planBaseType.setText(gpsPlan.getPipeLine_TX());
		listItemView.planBaseDate.setText("");
		listItemView.planDesc.setText(gpsPlan.getXXContent_TX());
		listItemView.planPipeInfo.setTag(gpsPlan.getPipeLine_ID());
		listItemView.planPipeInfo.setText(gpsPlan.getPipeLine_TX());
		listItemView.planResult.setText(R.string.gpsplan_whether_normal);
		listItemView.planBugType.setText(R.string.gpsplan_abnormal_type);
		listItemView.planMemo.setText("");
		listItemView.planEvent.setText(R.string.gpsplan_incident_report);
		listItemView.layout.setVisibility(View.GONE);
		listItemView.saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根

			}
		});
		return convertView;
	}
}