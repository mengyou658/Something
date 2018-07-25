package com.moons.xst.track.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.GPSMobjectBugResultForFiles;

public class DJPlanAudioAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private List<String> listItems;// 数据集合(音频文件列表)
	private List<String> listTitles;// 标题集合
	private List<DJ_PhotoByResult> photoByResults;
	
	private List<GPSMobjectBugResultForFiles> fileinfos;

	static class ListItemView { // 自定义控件集合，与布局一致
		public TextView imageTitle;
		public ImageView imageData;
	}

	public DJPlanAudioAdapter(Context context, List<String> data,
			List<String> titles, int resource) {
		this.mContext = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
		this.listTitles = titles;
	}

	public DJPlanAudioAdapter(Context context, List<DJ_PhotoByResult> data,
			int resource) {
		List<String> _data = new ArrayList<String>();
		List<String> _titles = new ArrayList<String>();
		if (data != null) {
			for (DJ_PhotoByResult dj_PhotoByResult : data) {
				_data.add(dj_PhotoByResult.getFilePath());
				_titles.add(dj_PhotoByResult.getFileTitle());
			}
		}
		this.photoByResults = data;
		this.mContext = context;
		this.listItems = _data;
		this.listTitles = _titles;
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
	}
	public DJPlanAudioAdapter(Context context, List<GPSMobjectBugResultForFiles> data,int no,
			int resource) {
		List<String> _data = new ArrayList<String>();
		List<String> _titles = new ArrayList<String>();
		if (data != null) {
			for (GPSMobjectBugResultForFiles fileResult : data) {
				_data.add(fileResult.getFilePath());
				_titles.add(fileResult.getFileTitle());
			}
		}
		this.fileinfos = data;
		this.mContext = context;
		this.listItems = _data;
		this.listTitles = _titles;
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
	}
	@Override
	public int getCount() /* 一定要重写的方法getCount,传回图片数目总数 */
	{
		// return myImageIds.length;
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

	// 返回具体位置的ImageView对象
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null) {
			convertView = listContainer.inflate(this.itemViewResource, null);
			listItemView = new ListItemView();
			listItemView.imageTitle = (TextView) convertView
					.findViewById(R.id.planimage_listitem_title);
			listItemView.imageData = (ImageView) convertView
					.findViewById(R.id.planimage_listitem_icon);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 读取文件标题debug

		Bitmap bit = null;
		try {
			bit = BitmapFactory.decodeStream(mContext.getAssets().open(
					listItems.get(position)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		listItemView.imageData.setImageBitmap(bit);
		if (photoByResults != null) {
			listItemView.imageData.setTag(photoByResults.get(position));// 文件对象
		} 
		else if (fileinfos !=null) {
			listItemView.imageData.setTag(fileinfos.get(position));// 文件对象
		}else
			listItemView.imageData.setTag(listItems.get(position));// 设置隐藏参数(文件名)

		listItemView.imageData.setImageResource(R.drawable.widget_bar_audio);

		listItemView.imageData.setScaleType(ImageView.ScaleType.FIT_XY);

		listItemView.imageTitle.setText(listTitles.get(position));

		convertView.setLayoutParams(new Gallery.LayoutParams(253, 180));
		convertView.setBackgroundColor(Color.alpha(1));
		return convertView;

	}

}
