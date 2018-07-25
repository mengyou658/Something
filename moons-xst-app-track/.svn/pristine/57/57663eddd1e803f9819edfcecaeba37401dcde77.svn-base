package com.moons.xst.track.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.moons.xst.buss.TempMeasureDBHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.track.bean.TempMeasureBaseInfo;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.widget.PhotoView;
import com.moons.xst.track.widget.ViewPagerFixed;
import com.tencent.mm.sdk.platformtools.Log;

public class Tool_Camera_PreviewAty extends BaseActivity {

	private Intent intent;
	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	// 删除按钮
	private Button btnDelete;
	private ImageButton rebutton;
	private TextView counttv;
	private MyPageAdapter adapter;

	// 当前的位置
	private int location = 0;
	private String[] mDatas;
	// private String mDirPath = "";
	int id = 0;
	/* 设置删除按钮是否可见 */
	private boolean setDelBtnVisible = true;

	private TempMeasureBaseInfo mTempMeasureBaseInfo;
	
	BitmapFactory.Options options;
	BitmapFactory.Options options2;

	private final static int mTag=5<<24;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tool_camera_preview);
		options = new  BitmapFactory.Options();
        options.inJustDecodeBounds =  false;
        options.inSampleSize =  10;   // width，hight设为原来的十分一
        options2 = new  BitmapFactory.Options();
        options2.inJustDecodeBounds =  false;
		intent = getIntent();
		mDatas = getIntent().getStringArrayExtra("mDatas");
		setDelBtnVisible = StringUtils.toBool(getIntent().getStringExtra(
				"visible"));
		// mDirPath = getIntent().getStringExtra("dirPath");
		id = getIntent().getIntExtra("position", 0);

		rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		rebutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				goBack();
			}
		});
		counttv = (TextView) findViewById(R.id.ll_camera_preview_title);

		btnDelete = (Button) findViewById(R.id.gallery_del);
		btnDelete.setOnClickListener(new DelListener());
		if (setDelBtnVisible)
			btnDelete.setVisibility(View.VISIBLE);
		else
			btnDelete.setVisibility(View.INVISIBLE);
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < mDatas.length; i++) {
			//Bitmap imageBitmap = BitmapFactory.decodeFile(mDatas[i]);
			initListViews(mDatas[i]);
		}

		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int) getResources().getDimensionPixelOffset(
				R.dimen.text_size_10));
		pager.setCurrentItem(id);

		counttv.setText((location + 1) + "/" + listViews.size());
		if(location==0){
			refreshViewPager(0);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 滑动退回上一界面
	 */
	/*
	 * @Override public boolean dispatchTouchEvent(MotionEvent event) {
	 * createVelocityTracker(event); switch (event.getAction()) { case
	 * MotionEvent.ACTION_DOWN: xDown = event.getRawX(); yDown =
	 * event.getRawY(); break; case MotionEvent.ACTION_MOVE: xMove =
	 * event.getRawX(); yMove= event.getRawY(); //滑动的距离 int distanceX = (int)
	 * (xMove - xDown); int distanceY= (int) (yMove - yDown); //获取顺时速度 int
	 * ySpeed = getScrollVelocity(); //关闭Activity需满足以下条件：
	 * //1.点击屏幕时，x轴坐标必须小于15,也就是从屏幕最左边开始 //2.x轴滑动的距离>XDISTANCE_MIN
	 * //3.y轴滑动的距离在YDISTANCE_MIN范围内
	 * //4.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity if(location
	 * == 0 && xDown < 15 && distanceX > XDISTANCE_MIN
	 * &&(distanceY<YDISTANCE_MIN&&distanceY>-YDISTANCE_MIN)&& ySpeed <
	 * YSPEED_MIN) {
	 * AppManager.getAppManager().finishActivity(Tool_Camera_PreviewAty.this); }
	 * break; case MotionEvent.ACTION_UP: recycleVelocityTracker(); break;
	 * default: break; } return super.dispatchTouchEvent(event); }
	 */

	private void goBack() {
		// 退出时保存当前所剩图片的路径
		List<String> paths = new ArrayList<String>();
		for (int i = 0; i < listViews.size(); i++) {
			paths.add(String.valueOf(listViews.get(i).getTag()));
		}
		AppContext.DJImagePath = paths;

		AppManager.getAppManager().finishActivity(Tool_Camera_PreviewAty.this);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
			counttv.setText((location + 1) + "/" + listViews.size());
			
			refreshViewPager(arg0);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	//刷新ViewPager
	private void refreshViewPager(final int arg0){
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean isUpload=false;
						String obj="";
						isUpload=uploadImage(arg0,isUpload);
						obj+=listViews.get(arg0).getTag();
						if(arg0>0){
							isUpload=uploadImage(arg0-1,isUpload);
							obj+=listViews.get(arg0-1).getTag();
						}
						if(arg0<listViews.size()-1){
							isUpload=uploadImage(arg0+1,isUpload);
							obj+=listViews.get(arg0+1).getTag();
						}
						if(isUpload){
							Message msg=new Message();
							msg.what=1;
							msg.obj=obj;
							handler.sendMessage(msg);
						}
			}
		}).start();
	}

	Handler handler= new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				adapter.setTag(msg.obj.toString());
				adapter.notifyDataSetChanged();
			}
			if(msg.what==2){
				int arg0=msg.arg1;
				Bitmap imageBitmap=(Bitmap) msg.obj;
				((PhotoView)listViews.get(arg0)).setImageBitmap(imageBitmap);
				listViews.get(arg0).setTag(mTag, false);
			}
		}};
		
		//加载图片
		public boolean uploadImage(int arg0,boolean isUpload){
			if( (Boolean) listViews.get(arg0).getTag(mTag)){
				Bitmap imageBitmap;
				if (!AppContext.getRotateCameraYN()){//没旋转过得图片加载时压缩十分之一
					imageBitmap=BitmapFactory.decodeFile(listViews.get(arg0).getTag().toString(), options);
				}else{//旋转过的图片已经压缩过十分之一了所有这里不压缩
					imageBitmap=BitmapFactory.decodeFile(listViews.get(arg0).getTag().toString(),options2);
				}
				
				Message msg=new Message();
				msg.what=2;
				msg.obj=imageBitmap;
				msg.arg1=arg0;
				handler.sendMessage(msg);
				return true;
			}
			return isUpload;
		}
		
	private void initListViews(String imagePath) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		//img.setImageBitmap(bm);
		img.setImageResource(R.drawable.pictures_no);
		img.setTag(imagePath);
		img.setTag(mTag, true);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	// 删除按钮添加的监听器
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
			LayoutInflater factory = LayoutInflater
					.from(Tool_Camera_PreviewAty.this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(
					Tool_Camera_PreviewAty.this)
					.builder()
					.setTitle(getString(R.string.camera_delete_picture))
					.setView(view)
					.setMsg(getString(R.string.camera_confirm_delete))
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {

									// TODO 自动生成的方法存根
									String path = String.valueOf(listViews.get(
											location).getTag());
									File file = new File(path);
									mTempMeasureBaseInfo = new TempMeasureBaseInfo();
									mTempMeasureBaseInfo.setGUID(FileUtils
											.getFileNameNoFormat(path));
									if (listViews.size() == 1) {
										file.delete();
										TempMeasureDBHelper
												.GetIntance()
												.DeleteTempMeasureBaseInfo(
														Tool_Camera_PreviewAty.this,
														mTempMeasureBaseInfo);
										Intent intent = new Intent(
												"data.broadcast.action");
										sendBroadcast(intent);
										AppContext.DJImagePath.clear();
										AppManager
												.getAppManager()
												.finishActivity(
														Tool_Camera_PreviewAty.this);
									} else {
										pager.removeAllViews();
										file.delete();
										TempMeasureDBHelper
												.GetIntance()
												.DeleteTempMeasureBaseInfo(
														Tool_Camera_PreviewAty.this,
														mTempMeasureBaseInfo);
										listViews.remove(location);
										adapter.setListViews(listViews);
										//adapter.setTag(listViews.get(location).getTag().toString());
										adapter.notifyDataSetChanged();
										refreshViewPager(location);
										counttv.setText((location + 1) + "/"
												+ listViews.size());
									}

								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
								}
							}).show();
		}
	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;

		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		//设置只刷新部分item
		public int getItemPosition(Object object) {
			View view = (View)object; 
	          if(mTag.contains(view.getTag().toString())){  
	              return POSITION_NONE;  
	          }else{  
	              return POSITION_UNCHANGED;    
	          } 
		}
		
		String mTag="";
		public void setTag(String mTag){
			this.mTag=mTag;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}