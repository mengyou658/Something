package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.ui.Tool_Camera_PreviewAty;

public class Tool_Camera_ImageGirdViewAdapter extends CommonAdapter<String>
{
	private String Select = "";
	private String Cancel = "";
	
	private String mDirPath;
	private String mtype;
	private String[] datas;
	
	public Tool_Camera_ImageGirdViewAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath, String type)
	{
		super(context, mDatas, itemLayoutId);
		mDirPath = dirPath;
		mtype = type;
		
		/* 转换成STRING数组，将照片传到浏览界面 */
		datas = new String[mDatas.size()];
		mDatas.toArray(datas);
		Select=context.getString(R.string.camera_choose);
		Cancel=context.getString(R.string.camera_cancel);
	}
	
	public void refresh(List<String> list, String opertype) {
		this.mDatas = list;
		datas = new String[list.size()];
		list.toArray(datas);
		mtype = opertype;
		AppContext.selectedPhotoBuffer.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public void convert(final ViewHolder helper, final String item)
	{
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		helper.setImageResource(R.id.id_item_select,
						R.drawable.picture_unselected);
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		
		if (mtype.equals(Select))
			mSelect.setVisibility(View.GONE);
		else
			mSelect.setVisibility(View.VISIBLE);
		
		mImageView.setColorFilter(null);
		mImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mtype.equals(Cancel)) {
					if (AppContext.selectedPhotoBuffer.contains(mDirPath + "/" + item))
					{
						AppContext.selectedPhotoBuffer.remove(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.picture_unselected);
						mImageView.setColorFilter(null);
					} else
					{
						AppContext.selectedPhotoBuffer.add(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.pictures_selected);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
					}
				} else {
					for (int i = 0; i < datas.length; i++) {
						datas[i] = mDirPath + "/" + datas[i];
					}
					Intent intent = new Intent(mContext, Tool_Camera_PreviewAty.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("mDatas", datas);
					intent.putExtra("visible", "true");
					intent.putExtra("position", mDatas.indexOf(item));
					mContext.startActivity(intent);
				}
			}
		});
		
		if (AppContext.selectedPhotoBuffer.contains(mDirPath + "/" + item)) {
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		} 
	}
}
