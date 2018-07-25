package com.moons.xst.track.adapter;

import java.util.List;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJPCInterfaceResultInfo;
import com.moons.xst.track.common.UIHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DJPCInterfaceInfoAdapter extends BaseAdapter {

	Context mContext;
	List<DJPCInterfaceResultInfo> mList;
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	public DJPCInterfaceInfoAdapter(Context context,
			List<DJPCInterfaceResultInfo> list, int resource) {
		this.mContext = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.mList = list;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	public List<DJPCInterfaceResultInfo> getList() {
		return mList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ListItemView listItemView;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();

			listItemView.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			listItemView.tv_number = (TextView) convertView
					.findViewById(R.id.tv_number);
			listItemView.iv_selected = (ImageView) convertView
					.findViewById(R.id.iv_selected);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		final DJPCInterfaceResultInfo entity = mList.get(position);
		listItemView.tv_name.setText(entity.getDJPCItemTX());
		listItemView.tv_number.setText(entity.getDJPCItemCD());

		if (entity.getIsSelected()) {
			listItemView.iv_selected
					.setImageResource(R.drawable.login_checkbox_selected);
		} else {
			listItemView.iv_selected
					.setImageResource(R.drawable.login_checkbox_unselected);
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (mList.get(position).getIsSelected()) {
					mList.get(position).setIsSelected(false);
					listItemView.iv_selected
							.setImageResource(R.drawable.login_checkbox_unselected);
				} else {
					mList.get(position).setIsSelected(true);
					listItemView.iv_selected
							.setImageResource(R.drawable.login_checkbox_selected);
				}
			}
		});
		return convertView;
	}

	static class ListItemView { // 自定义控件集合
		public TextView tv_name;
		public TextView tv_number;
		public ImageView iv_selected;
	}
}
