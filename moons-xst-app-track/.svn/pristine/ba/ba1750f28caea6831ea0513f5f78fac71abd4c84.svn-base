package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJ_PhotoByResult;

public class NewDJAdapter extends CommonAdapter<DJ_PhotoByResult> {

	public NewDJAdapter(Context context, List<DJ_PhotoByResult> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO 自动生成的构造函数存根
	}
	
	@Override
	public void convert(ViewHolder helper, DJ_PhotoByResult item) {
		// TODO 自动生成的方法存根
		if (item.getLCType().equals("ZP")) {//设置拍照图片
			helper.setImageResource(R.id.planimage_listitem_icon, R.drawable.pictures_no);
			helper.setImageByUrl(R.id.planimage_listitem_icon, item.getFilePath());
		} else if (item.getLCType().equals("LY")) {//设置录音图片
			helper.setImageResource(R.id.planimage_listitem_icon,
					R.drawable.widget_bar_audio);
		}
		
		final ImageView image = helper.getView(R.id.planimage_listitem_icon);
		image.setTag(item.getFilePath());
		image.setScaleType(ImageView.ScaleType.FIT_XY);
		
	}
	
}
