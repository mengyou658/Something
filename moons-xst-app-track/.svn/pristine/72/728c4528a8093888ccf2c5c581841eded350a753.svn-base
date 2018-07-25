package com.moons.xst.track.adapter;

import java.util.List;

import com.moons.xst.track.bean.Operate_Detail_Bill;

import com.moons.xst.track.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OperateClauseAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<Operate_Detail_Bill> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public TextView operateid;
		public TextView content;
		public TextView time;
		public ImageView operitestate;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public OperateClauseAdapter(Context context,
			List<Operate_Detail_Bill> data, int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = data;
		this.itemViewResource = resource;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	public void refresh(List<Operate_Detail_Bill> list) {
		listItems = list;
		this.notifyDataSetChanged();
	}

	public List<Operate_Detail_Bill> getlist() {
		return listItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();

			listItemView.operateid = (TextView) convertView
					.findViewById(R.id.tv_operate_id);
			listItemView.content = (TextView) convertView
					.findViewById(R.id.tv_operate_content);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.tv_operate_time);
			listItemView.operitestate = (ImageView) convertView
					.findViewById(R.id.iv_operate_state);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		Operate_Detail_Bill operate = listItems.get(position);
		String state = operate.getOperate_Detail_Item_State() + "";
		if (state.equals("0")) {
			listItemView.operitestate.setImageResource(R.drawable.datamark);
		} else if (state.equals("1")) {
			listItemView.operitestate.setImageResource(R.drawable.circle_green);
		} else if (state.length() <= 0) {
			listItemView.operitestate.setImageResource(R.drawable.circle_gray);
		}
		listItemView.operateid.setText(operate.getOperate_Detail_Item_Order());
		listItemView.content.setText(operate.getOperate_Detail_Item_Content());
		listItemView.time.setText(operate.getOperate_Detail_Item_Exe_Time());
		return convertView;
	}

}
