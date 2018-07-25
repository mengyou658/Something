package com.moons.xst.track.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.StringUtils;

public class Comm_DJLine_ListViewAdapter extends BaseAdapter {
	private Context context;// 运行上下文
	private List<DJLine> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private HashMap<Integer, DJLine> hashMap = new HashMap<Integer, DJLine>();
	private boolean isUSB;

	static class ListItemView { // 自定义控件集合，与djline_listitem布局一致
		public ImageView flagimage;
		public TextView title;
		public TextView buildtime;
		public ProgressBar downProgressBar;
		public TextView downloadstate;
		public CheckBox comm_checkbox;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public Comm_DJLine_ListViewAdapter(Context context, List<DJLine> data,
			int resource,boolean commType) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
		this.isUSB=commType;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 自定义视图
		final ListItemView listItemView;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.flagimage = (ImageView) convertView
					.findViewById(R.id.comm_djline_listitem_icon);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.comm_djline_listitem_title);
			listItemView.buildtime = (TextView) convertView
					.findViewById(R.id.comm_djline_listitem_BuildTime);
			listItemView.downProgressBar = (ProgressBar) convertView
					.findViewById(R.id.comm_djline_listitem_down_progressBar);
			listItemView.downloadstate = (TextView) convertView
					.findViewById(R.id.comm_djline_listitem_downloadstate);
			listItemView.comm_checkbox=(CheckBox) convertView.findViewById(R.id.comm_djline_listitem_checkbox);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// 设置文字和图片
		DJLine djline = listItems.get(position);
		
		if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Normal.toString())) {
			listItemView.title.setTextAppearance(context, R.style.widget_listview_title2);
			listItemView.buildtime.setTextAppearance(context, R.style.widget_listview_subdesc);
			//listItemView.downloadstate.setTextAppearance(context, R.style.widget_listview_subdesc2);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Big.toString()))
		{
			listItemView.title.setTextAppearance(context, R.style.widget_listview_title2_big);
			listItemView.buildtime.setTextAppearance(context, R.style.widget_listview_subdesc_big);
			//listItemView.downloadstate.setTextAppearance(context, R.style.widget_listview_subdesc2_big);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Huge.toString()))
		{
			listItemView.title.setTextAppearance(context, R.style.widget_listview_title2_super);
			listItemView.buildtime.setTextAppearance(context, R.style.widget_listview_subdesc_super);
			//listItemView.downloadstate.setTextAppearance(context, R.style.widget_listview_subdesc2_super);
		}
		listItemView.title.setTag(djline);// 设置隐藏参数(实体类)
		listItemView.title.setText(djline.getLineName());
		if (djline.getBuidYN())
			listItemView.buildtime.setText(StringUtils.formatDateTime(djline
					.getBuildTime()));
		else
			listItemView.buildtime
					.setText(R.string.line_list_buildTime_noBuild);
		if (djline.getneedUpdate()) {
			listItemView.title.setTextColor(Color.RED);
			listItemView.buildtime.setTextColor(Color.RED);
		} else {
			listItemView.title.setTextColor(Color.parseColor("#272636"));
			listItemView.buildtime.setTextColor(Color.parseColor("#a9a9a9"));
		}
		int linetypePic = R.drawable.widget_bar_xj_line_xh;
		switch (djline.getlineType()) {
		case 0:
			linetypePic = R.drawable.widget_bar_xj_line_xh;
			break;
		case 1:
			linetypePic = R.drawable.widget_bar_djpc_line_xh;
			break;
		case 2:
			linetypePic = R.drawable.widget_bar_xg_line;
			break;
		case 3:
			linetypePic = R.drawable.widget_bar_jm_line;
			break;
		case 4:
			linetypePic = R.drawable.widget_bar_xs_line_xh;
			break;
		case 5:
			linetypePic = R.drawable.widget_bar_sis_line;
			break;
		case 6:
			linetypePic = R.drawable.widget_bar_xx_line_xh;
			break;
		case 7:
			linetypePic = R.drawable.widget_bar_xx_line_xh;
			break;
		case 8:
			linetypePic = R.drawable.widget_bar_case_line_xh;
			break;
		default:
			linetypePic = R.drawable.widget_bar_xj_line_xh;
			break;
		}
		//点检排程显示多选框
		if(AppContext.getPlanType().equals(AppConst.PlanType_DJPC)&&!isUSB){
			listItemView.comm_checkbox.setVisibility(View.VISIBLE);
			if(djline.getIsSelected()){
				listItemView.comm_checkbox.setChecked(true);
			}else{
				listItemView.comm_checkbox.setChecked(false);
			}
		}
		listItemView.flagimage.setImageResource(linetypePic);
		listItemView.flagimage.setVisibility(ImageView.VISIBLE);
		listItemView.downProgressBar.setVisibility(ProgressBar.INVISIBLE);
		listItemView.downloadstate.setVisibility(TextView.GONE);
		return convertView;
	}
	
	//刷新item的Checkbox
	public void refreshItemCheckbox(int posi, ListView listView){
		int visibleFirstPosi = listView.getFirstVisiblePosition();
		int visibleLastPosi = listView.getLastVisiblePosition();
		if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
			View view = listView.getChildAt(posi - visibleFirstPosi);
			ListItemView holder = (ListItemView) view.getTag();
			if(listItems.get(posi).getIsSelected()){
				listItems.get(posi).setIsSelected(false);
				holder.comm_checkbox.setChecked(false);
			}else{
				listItems.get(posi).setIsSelected(true);
				holder.comm_checkbox.setChecked(true);
			}
		}
	}
	//获取选中的路线id
	public List<String> getSelectedLineID(){
		List<String> list=new ArrayList<String>();
		for(int i=0;i<listItems.size();i++){
			if(listItems.get(i).getIsSelected()){
				list.add(Integer.toString(listItems.get(i).getLineID()));
			}
		}
		return list;
	}

	public HashMap<Integer, DJLine> GetNeedUpgrateLineList(ListView listView) {
		for (int i = 0; i < listItems.size(); i++) {
			DJLine _djLine = listItems.get(i);
			final int firstListItemPosition = listView
					.getFirstVisiblePosition();
			final int lastListItemPosition = firstListItemPosition
					+ listView.getChildCount() - 1;
			if (i < firstListItemPosition || i > lastListItemPosition) {
				listView.getAdapter().getView(i, null, listView);
			}
			if (_djLine.getneedUpdate()) {
				if (!hashMap.containsKey(i))
					hashMap.put(i, _djLine);
			}
		}
		return hashMap;
	}

	/**
	 * 更新某条ItemView
	 * 
	 * @param view
	 * @param doNum
	 */
	public int updateView(int pos, View view, int doNum) {
		// 获取控件对象
		ListItemView _listItemView = new ListItemView();
		_listItemView.title = (TextView) view
				.findViewById(R.id.comm_djline_listitem_title);
		_listItemView.buildtime = (TextView) view
				.findViewById(R.id.comm_djline_listitem_BuildTime);
		_listItemView.downProgressBar = (ProgressBar) view
				.findViewById(R.id.comm_djline_listitem_down_progressBar);
		_listItemView.downloadstate = (TextView) view
				.findViewById(R.id.comm_djline_listitem_downloadstate);
		_listItemView.downProgressBar.setVisibility(ProgressBar.VISIBLE);
		_listItemView.downProgressBar.setProgress(doNum);
		_listItemView.downloadstate.setVisibility(TextView.GONE);
		if (doNum == 100) {
			_listItemView.downProgressBar.setVisibility(ProgressBar.INVISIBLE);
			_listItemView.title.setTextColor(Color.parseColor("#272636"));
			_listItemView.buildtime.setTextColor(Color.parseColor("#a9a9a9"));

			hashMap.remove(pos);
			if (listItems == null || listItems.size() == 0)
				listItems = AppContext.CommDJLineBuffer;
			if (listItems!=null && listItems.size()>0 && pos <= listItems.size() - 1) {
				listItems.get(pos).setneedUpdate(false);//添加不为空判断
			}
		}
		return hashMap.size();
	}

	/**
	 * 下载状态
	 * 
	 * @param view
	 * @param stateString
	 */
	public void updateDownLoadState(View view, int stateString,
			boolean errMesYn, boolean finishyn) {
		ListItemView _listItemView = new ListItemView();
		_listItemView.downloadstate = (TextView) view
				.findViewById(R.id.comm_djline_listitem_downloadstate);
		_listItemView.downloadstate.setVisibility(ProgressBar.VISIBLE);
		_listItemView.downloadstate.setText(stateString);
		if (errMesYn)
			_listItemView.downloadstate.setTextColor(Color.RED);
		if (finishyn)
			_listItemView.downloadstate.setVisibility(ProgressBar.GONE);
	}

	public View getViewByPosition(int position, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition
				+ listView.getChildCount() - 1;

		if (position < firstListItemPosition || position > lastListItemPosition) {
			return listView.getAdapter().getView(position, null, listView);
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

}
