package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.SettingConfigInfo;

public class SettingConfigAdapter extends BaseAdapter {
	private List<SettingConfigInfo> list = null;
	private Context mContext;
	
	public SettingConfigAdapter(Context mContext, List<SettingConfigInfo> list) {
		this.mContext = mContext;
		this.list = list;

	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.layout_settingconfig_item, null);
			viewHolder.tvName = (TextView) view.findViewById(R.id.listitem_systemsettingauth_itemdesc);
			viewHolder.tvUsing = (TextView) view.findViewById(R.id.listitem_systemsettingauth_value);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					return;
				}
			});
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final SettingConfigInfo mContent = list.get(position);
		if (position == 0) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getLetter());
		} else {
			String lastCatalog = list.get(position - 1).getLetter();
			if (mContent.getLetter().equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getLetter());
			}
		}
	
		viewHolder.tvName.setText(this.list.get(position).getInfo()[1]);
		viewHolder.tvUsing.setText(this.list.get(position).getInfo()[2]);
		
		return view;

	}

	final static class ViewHolder {
		TextView tvName;
		TextView tvUsing;
		TextView tvLetter;
	}
}
