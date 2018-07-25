package com.moons.xst.track.adapter;

import java.util.List;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.CommUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DJPCUserAdapter extends BaseAdapter {

	Context mContext;
	List<CommUser> mList;
	private int itemViewResource;// 自定义项视图源
	private LayoutInflater listContainer;// 视图容器

	public DJPCUserAdapter(Context context, List<CommUser> list, int resource) {
		this.mContext = context;
		this.mList = list;
		this.itemViewResource = resource;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ListItemView listItemView ;
			if (convertView == null) {
				convertView = listContainer.inflate(this.itemViewResource, null);
				listItemView = new ListItemView();
				listItemView.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
				listItemView.tv_number=(TextView) convertView.findViewById(R.id.tv_tv_number);
				
				convertView.setTag(listItemView);
			}else{
				listItemView = (ListItemView) convertView.getTag();
			}
			CommUser entity=mList.get(position);
			listItemView.tv_name.setText(entity.getNAME_TX());
			listItemView.tv_number.setText(entity.getAPPUSER_CD());
		return convertView;
	}

	static class ListItemView {
		TextView tv_name;
		TextView tv_number;
	}

}
