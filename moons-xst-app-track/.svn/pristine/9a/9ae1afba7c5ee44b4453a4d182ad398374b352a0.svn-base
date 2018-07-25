/**
 * 
 */
package com.moons.xst.track.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;

/**
 * @author LKZ
 * 
 */
public class LK_ListViewAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<DJPlan> listItems = new ArrayList<DJPlan>();// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private List<DJPlan> mselectedData;

	static class ListItemView {
		public ImageView flagimage;
		public TextView title;
		public TextView lastResult;
		public TextView lastTime;
		public TextView srName;
		public CheckBox checkBox;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public LK_ListViewAdapter(Context context, List<DJPlan> data,
			List<DJPlan> selectedData, int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
		// 首次弹出主控，将标识设为已弹出过；将主控下计划全部选中
		if (data != null && data.size() > 0) {
			for (DJPlan djPlan : data) {
				djPlan.setLKDoYn(true);
				if (!StringUtils.isEmpty(djPlan.getDJPlan().getMustCheck_YN())
						&& djPlan.getDJPlan().getMustCheck_YN().equals("1")) {
				} else {
					selectedData.add(djPlan);
				}
			}
		}
		mselectedData = selectedData;
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
					.findViewById(R.id.icon_lk);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.lk_listitem_title);
			listItemView.lastResult = (TextView) convertView
					.findViewById(R.id.lk_listitem_lastresult);
			listItemView.lastTime = (TextView) convertView
					.findViewById(R.id.lk_listitem_lasttime);
			listItemView.srName = (TextView) convertView
					.findViewById(R.id.lk_listitem_sr);
			listItemView.checkBox = (CheckBox) convertView
					.findViewById(R.id.lk_listitem_checkbox);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		final DJPlan _plan = listItems.get(position);
		if (!StringUtils.isEmpty(_plan.getDJPlan().getPlanDesc_TX()))
			listItemView.title.setText(_plan.getDJPlan().getPlanDesc_TX());
		else {
			listItemView.title.setText("");
		}
		listItemView.title.setTag(_plan);
		if (!StringUtils.isEmpty(_plan.getDJPlan().getLastResult_TX()))
			listItemView.lastResult.setText(_plan.getDJPlan()
					.getLastResult_TX() + " ");
		else {
			listItemView.lastResult.setText("");
		}
		if (!StringUtils.isEmpty(_plan.getDJPlan().getLastComplete_DT()))
			listItemView.lastTime.setText(_plan.getDJPlan()
					.getLastComplete_DT() + " ");
		else {
			listItemView.lastTime.setText("");
		}
		if (_plan.JudgePlanIsSrControl()
				&& (!StringUtils.isEmpty(_plan.GetSrPoint().getName_TX())))
			listItemView.srName.setText(_plan.GetSrPoint().getName_TX() + " ");
		else {
			listItemView.lastResult.setText("");
		}
		listItemView.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 被选中并且缓存中没有该项则添加到内存
						if ((!mselectedData.contains(_plan)) && isChecked) {
							mselectedData.add(_plan);
						}
						// 被取消选中且缓存中有该项则从内存中移除
						if (mselectedData.contains(_plan) && (!isChecked)) {
							mselectedData.remove(_plan);
						}
					}
				});

		if (!mselectedData.contains(_plan)) {
			listItemView.checkBox.setChecked(false);
		} else {
			listItemView.checkBox.setChecked(true);
		}
		listItemView.flagimage.setImageResource(R.drawable.icon_sr);
		return convertView;
	}
}
