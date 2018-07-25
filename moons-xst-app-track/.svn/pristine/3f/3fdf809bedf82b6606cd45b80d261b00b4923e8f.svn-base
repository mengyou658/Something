package com.moons.xst.track.adapter;

import java.util.LinkedList;
import java.util.List;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.ui.Tool_Video_PreviewAty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Tool_Camera_VideoGirdViewAdapter extends CommonAdapter<String>
{
	private  String Select = "";
	private  String Cancel = "";
	public static List<String> mSelectedImage = new LinkedList<String>();

	private String mDirPath;
	
	private String mtype;

	public Tool_Camera_VideoGirdViewAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath, String type)
	{
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
		mtype = type;
		Select=context.getString(R.string.camera_choose);
		Cancel=context.getString(R.string.camera_cancel);
	}
	
	public void refresh(List<String> list, String opertype) {
		this.mDatas = list;
		mtype = opertype;
		AppContext.selectedVideoBuffer.clear();
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
		final ImageView mPlay = helper.getView(R.id.id_item_play);
		
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
					if (AppContext.selectedVideoBuffer.contains(mDirPath + "/" + item))
					{
						AppContext.selectedVideoBuffer.remove(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.picture_unselected);
						mImageView.setColorFilter(null);
					} else
					{
						AppContext.selectedVideoBuffer.add(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.pictures_selected);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
					}
				}
			}
		});
		
		mPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mtype.equals(Select)) {
					String temp = mDirPath + "/" + item;
					int start = temp.lastIndexOf("/") + 1;
					int end = temp.length();
					String videoName = temp.substring(start, end);
					videoName = videoName.replaceAll("jpg", "3gp");
					Intent intent = new Intent(mContext, Tool_Video_PreviewAty.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("videoPath", AppConst.XSTTempVideosPath() + videoName);
					intent.putExtra("thumbnailPath", mDirPath + "/" + item);
					intent.putExtra("visible", "true");
					mContext.startActivity(intent);
				}
			}
		});
		if (AppContext.selectedVideoBuffer.contains(mDirPath + "/" + item)) {
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		} 
	}
}
