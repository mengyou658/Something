package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.GPSMobjectBugResultForFiles;

public class BugInfoPhotoAdapter extends CommonAdapter<GPSMobjectBugResultForFiles> {

	public BugInfoPhotoAdapter(Context context, List<GPSMobjectBugResultForFiles> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO 自动生成的构造函数存根
	}
	
	@Override
	public void convert(ViewHolder helper, GPSMobjectBugResultForFiles item) {
		// TODO 自动生成的方法存根
		if (item.getFile_Type().equals("JPG")) {//设置拍照图片
			helper.setImageResource(R.id.planimage_listitem_icon, R.drawable.pictures_no);
			helper.setImageByUrl(R.id.planimage_listitem_icon, item.getFilePath());
		}
		
		final ImageView image = helper.getView(R.id.planimage_listitem_icon);
		final TextView textView = helper.getView(R.id.planimage_listitem_title);
		textView.setText(item.getGUID());
		image.setTag(item.getFilePath());
		image.setScaleType(ImageView.ScaleType.FIT_XY);			
	}	
}
