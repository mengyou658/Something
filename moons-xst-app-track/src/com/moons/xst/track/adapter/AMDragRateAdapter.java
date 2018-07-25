package com.moons.xst.track.adapter;

import java.util.List;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.custom;
import com.moons.xst.track.ui.ShowSettingAty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AMDragRateAdapter extends BaseAdapter {
	Context mContext;
	List<custom> mList;

	AppContext appcontext;

	public AMDragRateAdapter(Context context, List<custom> list,
			AppContext appcontext) {
		mContext = context;
		mList = list;
		this.appcontext = appcontext;
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

	public void remove(int arg0) {// 删除指定位置的item
		mList.remove(arg0);
		this.notifyDataSetChanged();// 不要忘记更改适配器对象的数据源
	}

	public void insert(custom item, int to, int from) {// 在指定位置插入item
		// item.setQuickorder(to);
		mList.add(to, item);
		for (int i = 0; i < mList.size(); i++) {
			mList.get(i).setQuickorder(i);
		}
		// 保存改变的参数
		appcontext.setConfigShortcutFunction(ShowSettingAty.getString(mList));
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.custom_item_layout, null);
			viewHolder.custom_name = (TextView) convertView
					.findViewById(R.id.custom_name);
			viewHolder.custom_state = (CheckBox) convertView
					.findViewById(R.id.custom_state);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getQuickorder() == position) {
				// 适应中英文切换
				if (mList.get(i).getTag().equals("isPlanDownLoad")) {
					viewHolder.custom_name
							.setText(mContext
									.getString(R.string.main_menu_home_data_synchronization));
				} else if (mList.get(i).getTag().equals("isTwoBill")) {
					viewHolder.custom_name.setText(mContext
							.getString(R.string.main_menu_home_two_ticket));
				} else if (mList.get(i).getTag().equals("isCamera")) {
					viewHolder.custom_name.setText(mContext
							.getString(R.string.main_menu_home_camera));
				} else if (mList.get(i).getTag().equals("isCewen")) {
					viewHolder.custom_name.setText(mContext
							.getString(R.string.main_menu_home_cewen));
				} else if (mList.get(i).getTag().equals("isCezhen")) {
					viewHolder.custom_name.setText(mContext
							.getString(R.string.main_menu_home_cezhen));
				} else if (mList.get(i).getTag().equals("isCZS")) {
					viewHolder.custom_name.setText(mContext
							.getString(R.string.main_menu_home_cezs));
				} else if (mList.get(i).getTag().equals("isOverhaul")) {
					viewHolder.custom_name.setText(mContext
							.getString(R.string.main_menu_home_overhaul));
				}

				viewHolder.custom_state
						.setChecked(mList.get(i).getQuickState());
			}
		}

		viewHolder.custom_state.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (viewHolder.custom_state.isChecked()) {
					mList.get(position).setQuickState(true);
				} else {
					mList.get(position).setQuickState(false);
				}
				// 保存改变的参数
				appcontext.setConfigShortcutFunction(ShowSettingAty
						.getString(mList));
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView custom_name;
		CheckBox custom_state;
	}
}
