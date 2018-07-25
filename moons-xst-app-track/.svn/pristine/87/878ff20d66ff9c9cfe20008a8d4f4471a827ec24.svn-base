package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.QuerydataAdapter.ListItemView;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.XSTFileInfo;
import com.moons.xst.track.common.UIHelper;

public class SystemFileInfoAdapter extends BaseAdapter {
	
	private Context context;// 运行上下文
	private List<XSTFileInfo> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	
	static class ListItemView { // 自定义控件集合
		public ImageView flagimage;
		public TextView fileName;
		public TextView subInfo;
		public ImageView details;
	}
	
	public SystemFileInfoAdapter(Context context, List<XSTFileInfo> mDatas, 
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = mDatas;
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
					.findViewById(R.id.systembuginfo_listitem_icon);
			listItemView.fileName = (TextView) convertView
					.findViewById(R.id.listitem_systemfileinfo_itemdesc);
			listItemView.subInfo = (TextView) convertView
					.findViewById(R.id.listitem_systemfileinfo_itemdesc_subdesc);
			listItemView.details = (ImageView) convertView
					.findViewById(R.id.iv_system_fileinfo);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		XSTFileInfo info = listItems.get(position);

		if (info.getDirectoryYN()) {
			listItemView.flagimage.setBackgroundResource(R.drawable.ic_directory);
			listItemView.fileName.setText(info.getFileName());
			listItemView.subInfo.setText("文件夹：" + info.getSubDirectoryCount() +
					", 文件：" + info.getSubFileCount());
			listItemView.details.setVisibility(View.VISIBLE);
		} else {
			listItemView.fileName.setText(info.getFileName());
			listItemView.subInfo.setText(info.getFileTime() + "  " + info.getFileLen());
			if (info.getFileType().equalsIgnoreCase("txt"))
				listItemView.flagimage.setBackgroundResource(R.drawable.ic_fileinfo_txt);
			else if (info.getFileType().equalsIgnoreCase("xml"))
				listItemView.flagimage.setBackgroundResource(R.drawable.ic_fileinfo_xml);
			else if (info.getFileType().equalsIgnoreCase("jpg") 
					|| info.getFileType().equalsIgnoreCase("png"))
				listItemView.flagimage.setBackgroundResource(R.drawable.ic_fileinfo_jpg);
			else if (info.getFileType().equalsIgnoreCase("3gp"))
				listItemView.flagimage.setBackgroundResource(R.drawable.ic_fileinfo_3gp);
			else 
				listItemView.flagimage.setBackgroundResource(R.drawable.ic_fileinfo_unknown);
			listItemView.details.setVisibility(View.GONE);
		}
		
		return convertView;
	}
}