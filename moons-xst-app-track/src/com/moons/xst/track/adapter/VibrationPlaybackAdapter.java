package com.moons.xst.track.adapter;

import java.util.List;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.ProvisionalM;
import com.moons.xst.track.common.DateTimeHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VibrationPlaybackAdapter extends BaseAdapter{

	Context mContext;
	List<ProvisionalM> mList;
	LayoutInflater listContainer;
	
	public VibrationPlaybackAdapter(Context context,List<ProvisionalM> list){
		mContext=context;
		mList=list;
		listContainer = LayoutInflater.from(context);
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
		ListItemView listItemView;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.vibration_playback_item,
					null);
			listItemView.name = (TextView) convertView
					.findViewById(R.id.tv_name);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.tv_time);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		ProvisionalM entity=mList.get(position);
		listItemView.name.setText(entity.getDeviceName());
		String time=DateTimeHelper.DateToString(DateTimeHelper.StringToDate(entity.getDJTime(),"yyyyMMddHHmmss"));
		listItemView.time.setText(time);
		
		return convertView;
	}

	class ListItemView {
		TextView name;
		TextView time;
	}
	
}
