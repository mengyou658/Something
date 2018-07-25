package com.moons.xst.track.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
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

public class DJPlanVedioAdapter extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private List<String> listItems;// 数据集合(照片文件列表)
	private List<String> listTitles;// 标题集合
	private List<DJ_PhotoByResult> photoByResults;
	
	private List<GPSMobjectBugResultForFiles> fileinfos;

	static class ListItemView { // 自定义控件集合，与布局一致
		public TextView imageTitle;
		public ImageView imageData;
	}

	public DJPlanVedioAdapter(Context context, List<String> data,
			List<String> titles, int resource) {
		this.mContext = context;
		this.listItems = data;
		this.listTitles = titles;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
	}

	public DJPlanVedioAdapter(Context context, List<DJ_PhotoByResult> data,
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
	
	public DJPlanVedioAdapter(Context context, List<GPSMobjectBugResultForFiles> data,int no,
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
	@Override
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

		// 读取文件

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;// 图片宽高都为原来的二分之一，即图片为原来的四分之一

		Bitmap bit = null;
		bit = getVideoThumbnail(listItems.get(position));
		listItemView.imageData.setImageBitmap(bit);
		if (photoByResults != null) {
			listItemView.imageData.setTag(photoByResults.get(position));// 文件对象
		}
		else if (fileinfos !=null) {
			listItemView.imageData.setTag(fileinfos.get(position));// 文件对象
		}
		else
			listItemView.imageData.setTag(listItems.get(position));// 设置隐藏参数(文件名)

		// bit.recycle();
		// bit = null;

		// listItemView.imageData.setImageResource(R.drawable.picture1);

		listItemView.imageData.setScaleType(ImageView.ScaleType.FIT_XY);

		listItemView.imageTitle.setText(listTitles.get(position));

		convertView.setLayoutParams(new Gallery.LayoutParams(253, 180));
		convertView.setBackgroundColor(Color.alpha(1));
		return convertView;

	}
	/**  
	 *  获取帧缩略图，根据容器的高宽进行缩放
	 *  @param filePath
	 *  @return   
	 */
	private Bitmap getVideoThumbnail(String filePath) {  
		Bitmap bitmap = null;  
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
		try {  
			retriever.setDataSource(filePath);  
			bitmap = retriever.getFrameAtTime(-1);  
		}   
		catch(IllegalArgumentException e) {  
			e.printStackTrace();  
		}   
		catch (RuntimeException e) {  
			e.printStackTrace();  
		}   
		finally {  
			try {  
				retriever.release();  
			}   
			catch (RuntimeException e) {  
				e.printStackTrace();  
			}  
		} 
		if(bitmap==null)
			return null;
		// Scale down the bitmap if it's too large.
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int pWidth=320;// 容器宽度
		int pHeight=480;//容器高度
		//获取宽高跟容器宽高相比较小的倍数，以此为标准进行缩放
		float scale = Math.min((float)width/pWidth, (float)height/pHeight);
		int w = Math.round(scale * pWidth);
		int h = Math.round(scale * pHeight);
		bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
		return bitmap;  
	}  

}
