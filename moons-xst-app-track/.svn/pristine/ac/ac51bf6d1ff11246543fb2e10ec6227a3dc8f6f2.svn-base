/**
 * 
 */
package com.moons.xst.track.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.common.MathHelper;
import com.moons.xst.track.common.StringUtils;

/**
 * @author LKZ
 * 
 */
public class SR_ListViewAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<DJ_ControlPoint> listItems = new ArrayList<DJ_ControlPoint>();// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private Z_DJ_Plan mDj_Plan;

	static class ListItemView {
		public ImageView flagimage;
		public TextView title;
		public TextView mobjectstate;
		public TextView plancount;
		public TextView lasttime;
		public TextView currPlanName;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public SR_ListViewAdapter(Context context, List<DJ_ControlPoint> data,
			int resource, Z_DJ_Plan mPlan) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
		this.mDj_Plan = mPlan;
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
					.findViewById(R.id.icon_sr);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.sr_listitem_title);
			listItemView.mobjectstate = (TextView) convertView
					.findViewById(R.id.sr_listitem_mobjectstate);
			listItemView.plancount = (TextView) convertView
					.findViewById(R.id.sr_listitem_plancount);
			listItemView.lasttime = (TextView) convertView
					.findViewById(R.id.sr_listitem_lasttime);
			listItemView.currPlanName = (TextView) convertView
					.findViewById(R.id.sr_listitem_currplan);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		DJ_ControlPoint cp = listItems.get(position);
		if (!StringUtils.isEmpty(cp.getName_TX()))
			listItemView.title.setText(cp.getName_TX());
		else {
			listItemView.title.setText("");
		}
		listItemView.title.setTag(cp);
		int mobStateIndex = MathHelper.SRTransToInt(cp.getLastSRResult_TX());
		if (mobStateIndex > -1)
			listItemView.mobjectstate.setText(" "
					+ AppContext.SRStateBuffer.get(mobStateIndex).getName_TX());
		else {
			listItemView.mobjectstate.setText(" " + context.getString(R.string.dj_sr_notset));
		}
		// if (!StringUtils.isEmpty(cp.getLastSRResult_TX()))
		// listItemView.mobjectstate.setText(" " + cp.getLastSRResult_TX());
		// else {
		// listItemView.mobjectstate.setText(" " + "未设置");
		// }
		listItemView.plancount.setText(" "
				+ String.valueOf(" " + cp.getSRPlanCount()));
		if (!StringUtils.isEmpty(cp.getLastSRResult_TM()))
			listItemView.lasttime.setText(" " + cp.getLastSRResult_TM());
		else {
			listItemView.lasttime.setText(" " + context.getString(R.string.dj_sr_unknown));
		}
		int linetypePic;
		if (!cp.getisCurrSR()) {
			listItemView.title.setTextColor(Color.BLACK);
			linetypePic = R.drawable.icon_sr;
		} else {
			listItemView.title.setTextColor(Color.RED);
			linetypePic = R.drawable.icon_sr_curr;
		}
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setTag(linetypePic);
		return convertView;
	}
}
