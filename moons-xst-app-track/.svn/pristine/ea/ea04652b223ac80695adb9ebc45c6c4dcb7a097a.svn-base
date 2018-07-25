package com.moons.xst.track.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.R;
import com.moons.xst.track.common.DateTimeHelper;

public class DJQueryPlan_LvAdpter extends BaseAdapter {
	private Context context;// 运行上下文
	private List<DJPlan> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private int lineType;
	
	private Date date = DateTimeHelper.GetDateTimeNow();

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView title;
		public TextView type;
		public TextView time;
		public ImageView resultimage;
		public TextView result;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public DJQueryPlan_LvAdpter(Context context, List<DJPlan> data, int resource, int linetype) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
		this.lineType = linetype;
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
					.findViewById(R.id.djplan_listitem_flag);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.djplan_listitem_title);
			listItemView.type = (TextView) convertView
					.findViewById(R.id.djplan_listitem_Type);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.djplan_listitem_Time);
			listItemView.resultimage = (ImageView) convertView
					.findViewById(R.id.djplan_listitem_resulticon);
			listItemView.result = (TextView) convertView
					.findViewById(R.id.djplan_listitem_result);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		DJPlan plan = listItems.get(position);

		listItemView.title.setText(plan.getDJPlan().getPlanDesc_TX());
		listItemView.title.setTag(plan);// 设置隐藏参数(实体类)
		
		String Result_TX = "";
		if (plan.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_GC)) {// 观察类
			Result_TX = context.getString(R.string.plan_type_gc);
		} else if (plan.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {// 记录类
			Result_TX = context.getString(R.string.plan_type_jl);
		} else if (plan.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)) {// 测温类
			Result_TX = context.getString(R.string.plan_type_cw);
		} else if (plan.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)) {// 测振类
			Result_TX = context.getString(R.string.plan_type_cz);
		} else {                                                 // 测速类
			Result_TX = context.getString(R.string.plan_type_cs);
		}
		listItemView.type.setText(Result_TX);
		
		int linetypePic = R.drawable.icon_cp;
		linetypePic = R.drawable.icon_cp;
		
		if (lineType == AppConst.LineType.XDJ.getLineType()) {
			if (plan.GetCycle() != null) {
				if (plan.JudgePlanIsCompleted(date)) {
					listItemView.result.setText(plan.getDJPlan().getLastResult_TX());
					listItemView.time.setText(plan.getDJPlan().getLastComplete_DT());
					linetypePic = R.drawable.icon_marka_green;
				} else {
					listItemView.result.setText("");
					listItemView.time.setText("");
					
				}
			} else {
				listItemView.result.setText("");
				listItemView.time.setText("");
				
			}
		} else if (lineType == AppConst.LineType.CaseXJ.getLineType()) {
			if (plan.JudgePlanIsCompletedForCASEXJ(context)) {
				listItemView.result.setText(plan.getDJPlan().getLastResult_TX());
				listItemView.time.setText(plan.getDJPlan().getLastComplete_DT());
				linetypePic = R.drawable.icon_marka_green;
			} else {
				listItemView.result.setText("");
				listItemView.time.setText("");
				
			}
		} else {
			listItemView.result.setText(plan.getDJPlan().getLastResult_TX());
			listItemView.time.setText(plan.getDJPlan().getLastComplete_DT());
		}
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(ImageView.VISIBLE);
		listItemView.resultimage
				.setImageResource(R.drawable.widget_comment_count_icon);
		return convertView;
	}
}
