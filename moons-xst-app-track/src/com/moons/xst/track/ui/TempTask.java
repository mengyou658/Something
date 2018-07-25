package com.moons.xst.track.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.moons.xst.buss.ComDBHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.GPSXXTempTask;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.WebserviceFactory;

public class TempTask extends BaseActivity {
	
	private TextView taskname;
	private TextView taskpeople;
	private TextView tasktime;
	private TextView tasktype;
	private TextView taskdes;
	
	private ImageButton rebutton;
	private Button okbutton;
	private RadioGroup ragroup;
	private RadioButton nomalbutton;
	private RadioButton unnomalbutton;
	private ImageButton mapbButton;
	// 地图导航按钮（地图左下角上）
	ImageButton nativeButton;
	private GPSXXTempTask temptask;
	
	private RelativeLayout relayout;
	boolean isFirstLoc = true;// 是否首次定位
	private Marker mMarkerA;
	BitmapDescriptor cpPointRed = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	LatLng llA;
	private boolean IsNull = false;
	/**
	 * 地图显示控件
	 */
	private MapView mMapView;
	/**
	 * 百度地图Map
	 */
	private BaiduMap mBaiduMap;
	private float zoomMaxValue, zoomMinValue;
	/**
	 * 地图跟随图标按钮（地图左下角）
	 */
	ImageButton requestLocButton;
	/**
	 * 地图模式
	 */
	private LocationMode mCurrentMode;
	/**
	 * 当前覆盖物信息
	 */
	BitmapDescriptor mCurrentMarker;
	/**
	 * 百度地图位置对象
	 */
	LocationClient mLocClient;
	/**
	 * 百度地图位置监听
	 */
//	public MyLocationListenner myListener = new MyLocationListenner();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temptask);
		init(); 
		if (AppContext.nowTempTask.size() != 0) {
		temptask = AppContext.nowTempTask.remove();
		setData();
		CoordinateConverter converter = new CoordinateConverter();
		converter.coord(new LatLng(
				(Double.valueOf(temptask.getTaskWD_tx())), (Double.valueOf(temptask.getTaskJD_tx()))));
		llA = converter.convert();
		//LatLng llA = new LatLng(39.963175, 116.400244);
		MarkerOptions ooA = new MarkerOptions().position(llA).icon(cpPointRed)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		rebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(TempTask.class);
			}
		});
		okbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (nomalbutton.isChecked()) {
					new Thread() {
						public void run() {
							String comtime = DateTimeHelper.getDateTimeNow();
							temptask.setComplete_dt(comtime);
							temptask.setTaskStatus_tx("正常");
							boolean res = WebserviceFactory.updateTempTaskInfo(TempTask.this, temptask.getTempGPSTask_ID(), comtime);
							if (res) {
								ComDBHelper.GetIntance().updateTempTask(TempTask.this, temptask);
							}		
						}
					}.start();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();
					if (!AppContext.nowTempTask.isEmpty()) {
						Intent i = new Intent(getBaseContext(), TempTask.class);
						startActivity(i);
					}

				}
				else {
					new Thread() {
						public void run() {
							String comtime = DateTimeHelper.getDateTimeNow();
							temptask.setComplete_dt(comtime);
							temptask.setTaskStatus_tx("异常");
							boolean res = WebserviceFactory.updateTempTaskInfo(TempTask.this, temptask.getTempGPSTask_ID(), comtime);
							if (res) {
								ComDBHelper.GetIntance().updateTempTask(TempTask.this, temptask);
							}		
						}
					}.start();
					UIHelper.showTaskInputbug(TempTask.this, temptask.getTempGPSTask_ID(),"TEMP");
					finish();
				}
			}
			
		});
		mapbButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (relayout.getVisibility() == View.INVISIBLE) {
					relayout.setVisibility(View.VISIBLE);
				}
				else {
					relayout.setVisibility(View.INVISIBLE);
				}
				
			}
		});
		nativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CoordinateConverter converter = new CoordinateConverter();
				converter.from(CoordType.GPS);
				if (AppContext.getCurrLocation() ==null) {
					UIHelper.ToastMessage(TempTask.this, "未获取到位置信息");
					return;
				}
				BDLocation currLocation = AppContext.getCurrLocation();
				int locType = currLocation.getLocType();
				if(locType==BDLocation.TypeGpsLocation||locType==BDLocation.TypeNetWorkLocation){
					// 当前位置坐标
					converter.coord(new LatLng(currLocation.getLatitude()
							,currLocation.getLongitude()));
					LatLng pt1 = converter.convert();
					// 最近考核点坐标
					NaviParaOption para = new NaviParaOption()
					.startPoint(pt1).endPoint(llA)
					.startName("当前位置").endName("目的地");
					
					try {
						BaiduMapNavigation.openBaiduMapNavi(para, TempTask.this);
					} catch (BaiduMapAppNotSupportNaviException e) {
						e.printStackTrace();
						showDialog();
					}
				}
			}
		});
		IsNull = false;
		}
		else{
			UIHelper.ToastMessage(TempTask.this, "临时任务已完成！");
			IsNull = true;
		}
		registerBoradcastReceiver();
	}
	
	/**
	 * 初始化控件
	 */
	private void init() {
		taskname = (TextView) findViewById(R.id.temptask_name_value);
		taskpeople = (TextView) findViewById(R.id.temptask_creat_name_value);
		tasktime = (TextView) findViewById(R.id.temptask_time_value);
		tasktype = (TextView) findViewById(R.id.temptask_type_value);
		taskdes = (TextView) findViewById(R.id.temptask_des_value);
		rebutton = (ImageButton) findViewById(R.id.temptask_head_Rebutton);
		ragroup = (RadioGroup) findViewById(R.id.temptask_rg);
		nomalbutton = (RadioButton) findViewById(R.id.radioture);
		unnomalbutton = (RadioButton) findViewById(R.id.radiofalse); 
		okbutton = (Button) findViewById(R.id.temptask_ok);
		mapbButton = (ImageButton) findViewById(R.id.home_head_mapbutton);
		nativeButton = (ImageButton) findViewById(R.id.btn_temp_native);
		relayout = (RelativeLayout)findViewById(R.id.rl_temptask_map);
		requestLocButton = (ImageButton) findViewById(R.id.btn_pomode);
		mMapView = (MapView) findViewById(R.id.bmapView1);
		// 地图初始化
		mBaiduMap = mMapView.getMap();
		
		
	}
	private BroadcastReceiver myBroadcastReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("com.xst.track.service.updataCurrentLoction")){
				initLocation();
			}
		}
	};
private void registerBoradcastReceiver(){
	IntentFilter intentFilter =new IntentFilter();
	intentFilter.addAction("com.xst.track.service.updataCurrentLoction");
	registerReceiver(myBroadcastReceiver, intentFilter);
}
	/**
	 * 填充数据
	 */
	private void setData(){
		taskname.setText(temptask.getTaskName_tx());
		taskpeople.setText(temptask.getCreateUserName_tx());
		tasktime.setText(temptask.getCreate_dt());
		//tasktype.setText(temptask.getEventType_id());
		taskdes.setText(temptask.getTaskdesc_tx());
	
		
		// 是否显示Zoom
		mMapView.showZoomControls(true);
		// 是否显示比例尺
		mMapView.showScaleControl(true);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
//		mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(myListener);
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(1000);
//		mLocClient.setLocOption(option);
//		mLocClient.start();
		initLocation();
		OnClickListener requestLocListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					((ImageButton) v).setImageDrawable(getResources()
							.getDrawable(R.drawable.po_fol));
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					((ImageButton) v).setImageDrawable(getResources()
							.getDrawable(R.drawable.po_nor));
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(requestLocListener);
		// 默认跟随
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.FOLLOWING, true, mCurrentMarker));
	}
	
	private void initLocation() {
		BDLocation currLocation = AppContext.getCurrLocation();
		 // map view 销毁后不在处理新接收的位置
        if (currLocation == null || mMapView == null) {
            return;
        }
        int locType = currLocation.getLocType();
        if(locType==BDLocation.TypeGpsLocation||locType==BDLocation.TypeNetWorkLocation){
        	MyLocationData locData = new MyLocationData.Builder()
        	.accuracy(currLocation.getRadius())
        	// 此处设置开发者获取到的方向信息，顺时针0-360
        	.direction(100).latitude(currLocation.getLatitude())
        	.longitude(currLocation.getLongitude()).build();
        	mBaiduMap.setMyLocationData(locData);
        	if (isFirstLoc) {
        		isFirstLoc = false;
        		LatLng ll = new LatLng(currLocation.getLatitude(),
        				currLocation.getLongitude());
        		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        		mBaiduMap.animateMapStatus(u);
        	}
        }
	}

	/**
	 * 定位SDK监听函数
	 */
//	public class MyLocationListenner implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // map view 销毁后不在处理新接收的位置
//            if (location == null || mMapView == null) {
//                return;
//            }
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            mBaiduMap.setMyLocationData(locData);
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(),
//                        location.getLongitude());
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//                mBaiduMap.animateMapStatus(u);
//            }
//        }
//
//		public void onReceivePoi(BDLocation poiLocation) {
//		}
//	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (!IsNull) {
			// 退出时销毁定位
//			mLocClient.stop();
			cpPointRed.recycle();
			BaiduMapNavigation.finish(this);
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
		}
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	 /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(TempTask.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
}
