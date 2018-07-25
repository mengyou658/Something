package com.moons.xst.track.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IImeasureService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.R.color;
import com.moons.xst.track.common.BTClsUtils;
import com.moons.xst.track.common.HexUtils;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;

public class TemperatureActivity extends BaseActivity {

		//[start] 自定义传入传出参数
		private static int mDebugFlag = 0;
		private static int mFsl = 95;
		private String mSaveType = "Current";
		/*0-表示内置，1-表示外置*/
		private static int mCLModuleType = 0;
		String _btAddress = "";
	    String _btPassword = "0000";
		//[end]
	    
	    //[start] 自定义内部使用参数
	    private static int mInitYmax = 10;
		private static float mCurValue = 0;
		private static float mMaxValue =  Float.MIN_VALUE;
		private static float mMinValue =  Float.MAX_VALUE;
		private static int mXChartCount = 100;
		
		public static final int REQCODE_TEMPERATURESET = 1;//测温设置
		
		private static Handler handler = new Handler();
	    //[end]
	    
		//[start] 外接测量自定义参数
	    boolean bRun = true;
	    boolean bThread = false;
	    /*SPP服务UUID号*/
	    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
		private static byte[] packStart = new byte[]{0x33,(byte) 0xcc};
	    private static byte[] packEnd = new byte[]{0x66,(byte) 0x99};
	    private static int packageMinLen = 16;
	    
	    /*显示用数据缓存*/
	    private String mReceiveDataStr = "";   
	    /*输入流，用来接收蓝牙数据*/
	    private InputStream is;    
	    
	    /*蓝牙设备*/
	    BluetoothDevice _device = null;     
	    /*蓝牙通信socket*/
	    static BluetoothSocket _socket = null;
	    /*蓝牙适配器*/
		private BluetoothAdapter _bluetooth = null;
		//[end]
		
		//[start] 控件
		private RelativeLayout setupBtn, operationBtn, saveBtn;
		private ImageButton returnButton;
		
		private TextView temperature_memu_operation, temperature_memu_operation_desc;
		private static TextView txtValue = null;	
		private static TextView txtMaxValue = null;	
		private static TextView txtMinValue = null;	
		
		private static LineChart mLineChart = null;  	
		private static LineData mLineData = null;
	
		private LoadingDialog loading;
	    //[end]
		
	    //[start] 自定义方法
	    private void initHeadView(){
	    	returnButton = (ImageButton)findViewById(R.id.temperature_head_Rebutton);
			returnButton.setOnClickListener(new View.OnClickListener() {		
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					if (saveBtn.isEnabled()) {
						if (temperature_memu_operation.getText().toString().contains(getString(R.string.temperature_stop)))
							stop();
						
						closePower();
						AppManager.getAppManager().finishActivity(TemperatureActivity.class);
					}
				}
			});
	    }
	    
	    private void initTipView(){
	    	saveBtn = (RelativeLayout) findViewById(R.id.ll_temperature_memu_ok);
			saveBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					if (temperature_memu_operation.getText().toString().contains(getString(R.string.temperature_stop)))
					{
						stop();
					}
					closePower();
					Intent intent=new Intent();
					intent.putExtra("rsts", Float.toString(getSaveData()));
				    
				    setResult(RESULT_OK,intent);
					finish();
				}
			});
			
			temperature_memu_operation = (TextView) findViewById(R.id.temperature_memu_operation);
			temperature_memu_operation_desc = (TextView) findViewById(R.id.temperature_memu_operation_desc);
			operationBtn = (RelativeLayout) findViewById(R.id.ll_temperature_memu_operation);
			operationBtn.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
	                String oldTextString = (String) temperature_memu_operation.getText();
					
					if (oldTextString.contains(getString(R.string.temperature_stop)))
						stop();
					else {
						if (mDebugFlag == 0 
								&& mCLModuleType == 1)
							startforouter();
						else
							start();
					}
				}			
			});
			
			setupBtn = (RelativeLayout) findViewById(R.id.ll_temperature_memu_setup);
			setupBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					if (temperature_memu_operation.getText().toString().contains(getString(R.string.temperature_stop)))
					{
						stop();
					}
					
					showCLSet(TemperatureActivity.this,mFsl,mSaveType,_btAddress,_btPassword);
				}
			});
	    }
	    
	    /**
		 * 从配置文件读取参数配置信息
		 */
		private void readConfig(){
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				String path = AppConst.TemperatureConfigPath();
				File f = new File(path);
				Document doc = db.parse(f);
				doc.normalize();
				/*找到根Element*/
				Element root = doc.getDocumentElement();
				NodeList nodes = root.getElementsByTagName("Setting");
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) (nodes.item(i));
					/*获取river中name属性值*/
					if (element.getAttribute("Name").equals("FSL")) {
						mFsl = StringUtils.toInt(element.getAttribute("Using"));
						} 
					else if (element.getAttribute("Name").equals("SaveType")) {
						mSaveType = element.getAttribute("Using");
					}
				  }
				}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
		/**
		 * 获取要保存的值
		 * @return
		 */
		private float getSaveData(){
			//float rvalue = mCurValue;
			float rvalue = Float.valueOf(txtValue.getText().toString());
			if (mSaveType.equals("MAX")){
				rvalue = Float.valueOf(txtMaxValue.getText().toString());
			}
			else if (mSaveType.equals("MIN")){
				rvalue = Float.valueOf(txtMinValue.getText().toString());
			}
			return rvalue;
		}
		
		/**
	     * 打开电源
	     */
		private int openPower(){
			
			loading = new LoadingDialog(this);
			loading.setLoadText(getString(R.string.temperature_start_process));
			loading.setCancelable(false);
			loading.show();
			new Thread(){
				 @Override
			     public void run() {	
					 Message msg = Message.obtain();
					 if(mDebugFlag == 0){
						 try {
							 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
					                   ServiceManager.getService("imeasure"));  				             	
							 int rst = imeasureService.openTemperature();		
							 msg.what = 1;
							 } catch (Exception e) {
								e.printStackTrace();
								msg.what = -1;
								msg.obj = e;
								}
						 } else{
							try {
								sleep(5000);
							} catch (InterruptedException e) {}
						}						
						/*向Handler发送消息*/
					    mRefreshBtnEnableHandler.sendMessage(msg);   					
				 }
			 }.start();
			 return 0;
		}
		
		/**
	     * 关闭电源
	     */
		private int closePower(){
			int rst = -1;
			if(mDebugFlag == 0){
				/*外置*/
				if (mCLModuleType == 1) 
					rst = 0; 
				else{
					try {
						 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
			                   ServiceManager.getService("imeasure"));  
			            
						 rst = imeasureService.closeTemperature();
						
					} catch (Exception e) {
						rst = -1;
					}
				}
			}
			else
				rst = 0;
			return rst;
		}
		
		/**
		 * 启动测量
		 */
		private void start(){
			setFSL(mFsl);
			initData();
			
			/*打开电源后先读取两次数据扔掉*/
			getCLValue();
			getCLValue();
			temperature_memu_operation.setText(R.string.temperature_stop);
			temperature_memu_operation_desc.setText(R.string.temperature_stop_desc);
			
			readCLData();
		}
		
		/**
		 * 外接测量启动
		 */
		private void startforouter(){
			temperature_memu_operation.setText(R.string.temperature_stop);
			temperature_memu_operation_desc.setText(R.string.temperature_stop_desc);
			OpenBTDevice();
			
			setFSL(mFsl);
			initData();
			
			connectBTDevice();
			readCLData();
		}
		
		/**
		 * 停止测量
		 */
		private void stop(){
			/*停止Timer*/
			handler.removeCallbacks(runnable);           		
			
			/*外置*/
			if (mDebugFlag != 1 && mCLModuleType == 1) 
			{
				//if (_socket != null)
					//SendCloseBtComand();
				diconnectBTDevice();
			}
			
			temperature_memu_operation.setText(R.string.temperature_start);
			temperature_memu_operation_desc.setText(R.string.temperature_start_desc);
		}
		
		/**
		 * 设置发射率
		 * @param fsl
		 */
		private int setFSL(int fsl){
			int rst = -1;
			if(mDebugFlag == 0){
				/*外置*/
				if (mCLModuleType == 1) 
					rst = 0;
	            else {
					try {
						 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
			                  ServiceManager.getService("imeasure"));  
			           
						 rst = imeasureService.setTemperatureEm(fsl);
						
					} catch (Exception e) {
						rst = -1;
					}
				}
			}
			else
				rst = 0;
			
			return rst;
		}	
		
		/**
		 * 获取温度值
		 * @return
		 */
		private static String getCLValue(){
			if(mDebugFlag == 1){
				 DecimalFormat df = new DecimalFormat("#0.00");
				 String cl = df.format(50 * (Math.random()));
				 return cl;
			}
			else {
				/*外置*/
				if (mCLModuleType == 1){
					return "";
				} else {
					IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
				               ServiceManager.getService("imeasure"));  
				        
						 float val;
						try {
							val = imeasureService.getTemperature();
							if (val >=10000)
								return "值错误"; 
						} catch (RemoteException e) {
							// TODO 自动生成的 catch 块
							return "值错误";
						}
						 String dataString =  String.valueOf(val);
						 return dataString;
				}
			}	
		}
		
		/**
		 * 初始化数据
		 */
		private void initData(){
			mCurValue = 0;
			mMaxValue = Float.MIN_VALUE;
			mMinValue = Float.MAX_VALUE;
			
			List<Entry> yValues = mLineData.getDataSetByIndex(0).getYVals();
			for(int i = 0; i < yValues.size(); i ++){
				   yValues.get(i).setVal(0);		   
			}
		}
		
		/**
		 *定时读取温度
		 */
		private void readCLData() {
			/*开始Timer*/	
			handler.postDelayed(runnable,100);
		}
		
		private static void RefreshData(){
			if (mMaxValue < mCurValue){
				mMaxValue = mCurValue;
				ResetYaxMaxValueAndMinValue();
			}
			if (mMinValue > mCurValue){
				mMinValue = mCurValue;
				ResetYaxMaxValueAndMinValue();
			}
			String vl = Float.toString(mCurValue);
	    	txtValue.setText(vl);
	    	txtMaxValue.setText(Float.toString(mMaxValue));
	    	txtMinValue.setText(Float.toString(mMinValue));
			AddData(vl);
			RefreshChart();
		}
		
		/**
		 * 转至设置页面
		 * 
		 * @param fsl		
		 */
		private  void showCLSet(Context context, int fsl, String saveType, String btaddress, String btpwd) {
			Intent intent = new Intent(context, TemperatureSetting.class);
			intent.putExtra("fsl", fsl);
			intent.putExtra("saveType", saveType);
			intent.putExtra("btAddress", btaddress);
			intent.putExtra("btPassword", btpwd);
			startActivityForResult(intent,REQCODE_TEMPERATURESET);
		}
		
		private static Runnable runnable = new Runnable( ) {
			public void run ( ) {
				try {
					/*内置 或 调试*/
					if (mCLModuleType == 0 || mDebugFlag == 1){
						mCurValue = Float.parseFloat(getCLValue());
						
						RefreshData();
					}
					else{
						/*外置只需要发起读取温度指令即可*/	
						SendCWComand(); 	
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			    
				/*postDelayed(this,1000)方法安排一个Runnable对象到主线程队列中*/
			    handler.postDelayed(this,1000);
			}
		};
		
		Handler mRefreshBtnEnableHandler = new Handler() {   
		    @Override   
			public void handleMessage(Message msg) {   
		    	if (loading != null)
		    		loading.dismiss();
		    	if (msg.what == 1) {
			    	/*刷新数据*/
			    	operationBtn.setEnabled(true);
					saveBtn.setEnabled(true);
					setupBtn.setEnabled(true);
					start();
		    	} else {
		    		UIHelper.ToastMessage(getApplication(), 
							((Exception)(msg.obj)).getMessage());
		    	}
			    //super.handleMessage(msg);   
			  }   
			};  
			
		//[start] 图表相关函数
		/**
	    * 往图表数据里加数据
	    * @param data
	    */
	    private static void AddData(String data)
	    {
		   if (mLineData == null)
			   return;
		  
		   List<Entry> yValues = mLineData.getDataSetByIndex(0).getYVals();
		   float value = (float)Double.parseDouble(data);  
		   int size = yValues.size();
		   if (size < mXChartCount){
			   yValues.add(new Entry(value, mXChartCount - size - 1));  
		   }
		   else{
			   for(int i = size - 1; i > 0; i --){
				   yValues.get(i).setVal(yValues.get(i - 1).getVal());			   
			   }
			   
			   yValues.get(0).setVal(value);
		   }
	    }
	    
	    /**
		 * 重新刷新坐标值
		 */
		private static void ResetYaxMaxValueAndMinValue(){
			float axMax = mLineChart.getAxisLeft().getAxisMaxValue();
			float axMin = mLineChart.getAxisLeft().getAxisMinValue();
			if (mMaxValue > axMax){
				int bs = (int) (mMaxValue % mInitYmax ==0?mMaxValue/mInitYmax:mMaxValue/mInitYmax + 1);
				mLineChart.getAxisLeft().setAxisMaxValue(bs * mInitYmax );
			}
			if (mMinValue < axMin){
				int bs = (int) (mMinValue % mInitYmax ==0?mMinValue/mInitYmax:mMinValue/mInitYmax - 1);
				mLineChart.getAxisLeft().setAxisMinValue(bs * mInitYmax );
			}
		}	
		
		/**
		 * 刷新图
		 */
		private static void RefreshChart(){
			/* add data */     
			/* 设置数据 */
			mLineChart.setData(mLineData);      
			mLineChart.invalidate();
		}
		
	    /**
	     * 设置图表显示样式
	     */
	    private void initChart( int color) { 
		   
		   LineChart lineChart = mLineChart;
	       lineChart.setDrawBorders(false);  //是否在折线图上添加边框     
	       lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
			
			@Override
			public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {
				mLineChart.getXAxis().removeAllLimitLines();
				//游标获取到X轴的值
				int xIndex = arg2.getXIndex();
				addLine(xIndex);
				//游标获取到Y轴的值	
				float val = arg0.getVal();
				temperatureYValue.setText(String.valueOf(val));
			}
			
			@Override
			public void onNothingSelected() {
				
			}
		});
	   
	       //隐藏左边坐标轴横网格线
	       // lineChart.getAxisLeft().setDrawGridLines(false);
	       //隐藏右边坐标轴横网格线
	       lineChart.getAxisRight().setDrawGridLines(false);
	       //隐藏X轴竖网格线
	       lineChart.getXAxis().setDrawGridLines(false); 
	       
	  
	       // 设置在Y轴上是否是从0开始显示   
		   lineChart.getAxisLeft().setStartAtZero(false);  
	       //是否在Y轴显示数据，就是曲线上的数据   
	       // lineChart.setDrawYValues(true);  
	
	       //设置Y轴坐标最小为多少
	       lineChart.getAxisLeft().setAxisMaxValue(mInitYmax);
	       lineChart.getAxisLeft().setAxisMinValue(0 - mInitYmax);
	       //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布 
	       //lineChart.getAxisLeft().setLabelCount(5); 
	       //lineChart.getLegend().setEnabled(true);
	       //重新设置Y轴坐标最大为多少，自动调整 
	       //lineChart.resetAxisMaxValue(); 
	       //lineChart.resetAxisMinValue(); 
	       
	       //参数如果为true Y轴坐标只显示最大值和最小值
	       //lineChart.getAxisLeft().setShowOnlyMinMax(true); 
	       
	       //lineChart setUnit("°C");   
	       lineChart.getAxisRight().setEnabled(false);
	       lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
	        // no description text     
	       lineChart.setDescription("");// 数据描述     
	       // 如果没有数据的时候，会显示这个，类似listview的emtpyview     
	       lineChart.setNoDataTextDescription("You need to provide data for the chart.");    
	      
	        // enable / disable grid background     
	       lineChart.setDrawGridBackground(false); // 是否显示表格颜色     
	       lineChart.setGridBackgroundColor(color & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度     
	        
	       // enable touch gestures     
	       lineChart.setTouchEnabled(true); // 设置是否可以触摸     
	   
	       // enable scaling and dragging     
	       lineChart.setDragEnabled(false);// 是否可以拖拽     
	       lineChart.setScaleEnabled(true);// 是否可以缩放     
	   
	       // if disabled, scaling can be done on x- and y-axis separately     
	       lineChart.setPinchZoom(false);//      
	  
		   lineChart.setBackgroundColor(color);// 设置背景     	        
	   
	       // get the legend (only possible after setting data)     
	       Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的     
	    
	        // modify the legend ...     
	        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);     
	        mLegend.setForm(LegendForm.CIRCLE);// 样式     
	        mLegend.setFormSize(6f);// 字体     
	        mLegend.setTextColor(color);// 颜色     
	//      mLegend.setTypeface(mTf);// 字体     
	    
	      //是否可以缩放 仅y轴
	        lineChart.setScaleYEnabled(false); 
	      //是否显示X坐标轴上的刻度，默认是true
	        lineChart.getXAxis().setDrawLabels(false);     
	    }
	    
	    /**
	    * 初始化数据
	    * @param color
	    * @return
	    */
	    private LineData InitLineData(int color) {    
		   ArrayList<String> xValues = new ArrayList<String>();    
		          for (int i = 0; i < mXChartCount; i++) {    
		              // x轴显示的数据，这里默认使用数字下标显示     
		              xValues.add("" + i);    
		          } 	               
	   
	       // y轴的数据     
	       ArrayList<Entry> yValues = new ArrayList<Entry>();  
	       
	      
	       for (int i = 0; i < mXChartCount; i++) {    
	          // x轴显示的数据，这里默认使用数字下标显示     
	    	   yValues.add(new Entry(0, i));    
	      }    	          
	       // create a dataset and give it a type     
	      // y轴的数据集合     
//	       LineDataSet lineDataSet = new LineDataSet(yValues, "温度趋势" /*显示在比例图上*/);    
	       LineDataSet lineDataSet = new LineDataSet(yValues, null/*显示在比例图上*/);    
	       // mLineDataSet.setFillAlpha(110);     
	       // mLineDataSet.setFillColor(Color.RED);     
	      
	       //用y轴的集合来设置参数     
	       lineDataSet.setLineWidth(1.75f); // 线宽     
	       lineDataSet.setCircleSize(0f);// 显示的圆形大小     
	       lineDataSet.setColor(color);// 显示颜色     
	       lineDataSet.setCircleColor(color);// 圆形的颜色     
	      
	       lineDataSet.setHighLightColor(Color.TRANSPARENT); // 高亮的线的颜色     
	       lineDataSet.setValueTextColor(Color.TRANSPARENT);//设置折线上点的文字颜色
	       ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();    
	       lineDataSets.add(lineDataSet); // add the datasets     
	   
	       // create a data object with the datasets     
	       LineData lineData = new LineData(xValues, lineDataSets);    
	   
	       return lineData;    
		}
		//[end]
		
	    //[start] 外接相关函数
		/**
		 * 关闭蓝牙连接
		 */
		private void diconnectBTDevice(){
			//关闭连接socket
	    	bRun = false;
	    	bThread=false;
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				
			}
	    	if (is != null)
	    	{
	    		try {
	    			is.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
	    		
	    		is = null;
	    	}
	    	if (_socket != null)
	    	{
	    		try {
	    			_socket.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
	    		
	    		_socket = null;
	    	}
		}
	  		
		/**
	     * 打开蓝牙
	     */
	    private void OpenBTDevice()
	    {
	    	//_bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
	    	//pairBT(_btAddress,_btPassword);
	    	SearchBT();
			/* 如果打开本地蓝牙设备不成功，提示信息，结束程序 */
	        if (_bluetooth == null){
	        	UIHelper.ToastMessage(this, R.string.temperature_start_bluetooth_hint);
	            AppManager.getAppManager().finishActivity(TemperatureActivity.this);
	            return;
	        }
	        
	        /* 设置设备可以被搜索  */ 
	        if(_bluetooth.isEnabled()==false){
	     		_bluetooth.enable();
	        }      
	    }
	  	    
	    /**
		 * 通过蓝牙连接外置测量设备	
		 */
		private void  connectBTDevice(){			
	       if(_bluetooth.isEnabled() == false){  
	    	   /* 如果蓝牙服务不可用则提示 */
	    	   UIHelper.ToastMessage(this, R.string.temperature_underway_start_bluetooth);
	   		   return;
	   	   }
	       if(_btAddress.equals("")){
	    	   UIHelper.ToastMessage(this, R.string.temperature_no_bluetooth);
	     	   return;
	     	}
	       
	       /* MAC地址 */
	       String address = _btAddress;
	       // 得到蓝牙设备句柄      
	       _device = _bluetooth.getRemoteDevice(address);
	       if (!AppConst.isEffectDevice(TemperatureActivity.this, _device.getName())) {
				UIHelper.ToastMessage(this, R.string.temperature_bluetooth_connect_hint2);
				return;
			}
	
	       // 用服务号得到socket
	       try{
	       	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
	       }catch(IOException e){
	       }
	       //连接socket	   	   
	       try{
	       	 _socket.connect();
	       	 
	       	 UIHelper.ToastMessage(this, getString(R.string.temperature_bluetooth_connect_hint1,_device.getName()));
	       	
	       }catch(IOException e){
	       	try{
	       		UIHelper.ToastMessage(this, R.string.temperature_bluetooth_connect_hint2);
	       		_socket.close();
	       		_socket = null;
	       		return;
	       	}catch(IOException ee){
	       		UIHelper.ToastMessage(this, R.string.temperature_bluetooth_connect_hint3);
	       		_socket = null;
	       		is = null;
	       		return;
	       	}	       
	       }
	       
	       /* 打开接收线程 */
	       try{
	    	/* 得到蓝牙数据输入流 */
	   		is = _socket.getInputStream();   
	   		}catch(IOException e){
	   			UIHelper.ToastMessage(this, R.string.temperature_reception_defeated);
	   			return;
	   		}
	   		if(bThread==false){
	   			/* 接收数据线程 */
		  	    new Thread(){
		  	    	
		  	    	public void run(){
		  	    		int num = 0;
		  	    		byte[] buffer = new byte[1024];
		  	    		
		  	    		int i = 0;
		  	    	
		  	    		bRun = true;
		  	    		//接收线程
		  	    		while(bRun == true){
		  	    			try{
		  	    				
		  	    				String smsg = "";
		  	    				while(bRun == true){
		  	    					
		  	    					num = is.read(buffer);         //读入数据
		  	    					byte[] receveData = new byte[num];
		  	    					for(i=0;i<num;i++){
		  	    						
		  	    						receveData[i]= buffer[i]; 
		  	    					}
		  	    					String s = HexUtils.encodeHexStr(receveData);
		  	    					smsg+=s;   //写入接收缓存
		  	    					if(is.available()==0)break;  //短时间没有数据才跳出进行显示
		  	    				}
		  	    				
		  	    				
		  	    			//收到结尾为6699就处理数据
		  	                    if (smsg.length() > 4 && smsg.substring(smsg.length() - 4, smsg.length()).equals("6699"))
		  	                    {
		  		    				if (CheckPackage(smsg))
		  		    				{
		  		    					mReceiveDataStr = smsg;
		  			    				//发送显示消息，进行显示刷新
		  			    				refreshwjclhandler.sendMessage(refreshwjclhandler.obtainMessage()); 
		  		    				}
		  		    				
		  		    				smsg = "";
		  	                    }
		  	    	    		}catch(IOException e){
		  	    	    		}
		  	    		}
		  	    	}
		  	    }.start();
	   			bThread=true;
	   		}else{
	   			bRun = true;
	   		}
	   		
	   		temperature_memu_operation.setText(R.string.temperature_stop);
			temperature_memu_operation_desc.setText(R.string.temperature_stop_desc);
		  }
	  		
	  		
	  	    
	  	    private boolean CheckPackage(String revStr)
	  	    {
	  	    	//接收包长度最小16字节（无数据情况下）
	  	    	if (revStr.length() < packageMinLen * 2)
	  				return false;
	  			String dataLenHex = revStr.substring(22, 24) + revStr.substring(20, 22);
	  			int packageLen = packageMinLen + Integer.parseInt(dataLenHex, 16);
	  			if (revStr.length() < packageLen * 2)
	  				return false;
	  			if (!revStr.substring(0, 4).equals("33cc") || !revStr.substring(packageLen * 2 - 4, packageLen * 2).equals("6699"))
	  				return false;
	  			return true;
	  	    }
	  	    
	  	    //外接设备消息处理队列
	  	    Handler refreshwjclhandler= new Handler(){
	  	    	public void handleMessage(Message msg){
	  	    		super.handleMessage(msg);
	  	    		String valueHexStr = "";
	  	    		
	  	    		String gNM = "";
	  	    		
	  	    		//33cc07080900000002020200fd00528c6699
	  	    		//根据mReceiveDataStr解析出温度值进行刷新
	  	    		
	  	    			String recString = mReceiveDataStr;
	  	    			try {
	  	    			 gNM = recString.substring(16, 20);
	  	    			 if (gNM.equals("0202")) //重发上一个读取数据包
	  	    			 {
	  	    				 valueHexStr = mReceiveDataStr.substring(26, 28) + mReceiveDataStr.substring(24, 26);
	  	    				 //byte[] revBytes = HexUtils.hexStr2Bytes(mReceiveDataStr);
	  	    		 
	  	    				 int tempValue = Integer.parseInt(valueHexStr, 16);
	  	    				 mCurValue = ((float)tempValue) / 10;
	  	    				 mReceiveDataStr = "";
	  	    		
	  	    				 RefreshData();
	  	    				 
	  	    			 }
	  	    			
	  	    			} catch (Exception e) {
	  						String ss = recString;
	  								}
	  	    	}
	  	    };
			private TextView temperatureYValue;
			private TextView temperatureXValue;
	  	    
	  	    private static boolean SendCWComand()
	  	    {
	  	    	byte[] comandData = getCWComandData();
	  	    	return SendComand(comandData);
	  	    	
	  	    }
	  	    private static boolean SendCloseBtComand()
	  	    {
	  	    	byte[] comandData = getCloseBtComandData();
	  	    	return SendComand(comandData);
	  	    	
	  	    }
	  	    private static boolean SendStatusBtComand()
	  	    {
	  	    	byte[] comandData = getStatusBtComandData();
	  	    	return SendComand(comandData);
	  	    	
	  	    }
	  	    private static boolean SendComand(byte[] data)
	  	    {	    	
	  	    	byte[] crcRst  = HexUtils.CRC16_Check(data,data.length);
	  	    	try {
	  				OutputStream os = _socket.getOutputStream();
	  				
	  				byte[] all = new byte[packStart.length + data.length + crcRst.length + packEnd.length];
	  				System.arraycopy(packStart, 0, all, 0, packStart.length);
	  				System.arraycopy(data, 0, all, packStart.length, data.length);
	  				System.arraycopy(crcRst, 0, all, packStart.length + data.length, crcRst.length);
	  				System.arraycopy(packEnd, 0, all, packStart.length + data.length + crcRst.length, packEnd.length);
	  				
	  				os.write(all);
	  				/*os.write(packStart);
	  				os.write(data);
	  				os.write(crcRst);
	  				os.write(packEnd);*/
	  			} catch (IOException e) {
	  				// TODO 自动生成的 catch 块
	  				e.printStackTrace();
	  			}   //蓝牙连接输出流
	  	    	
	  	    	return true;	    	
	  	    }
	  	    
	  	    private static byte[] getCWComandData(){
	  	    	byte[] rst = new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x02,0x00,0x02,0x00,(byte) (mFsl % 256),0x00};
	  	    	return rst;
	  	    }
	  	    private static byte[] getCloseBtComandData()
	  	    {
	  	    	byte[] rst = new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x04,0x00,0x00,0x00};
	  	    	return rst;
	  	    }
	  	    private static byte[] getStatusBtComandData()
	  	    {
	  	    	byte[] rst = new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x08,0x00,0x00,0x00};
	  	    	return rst;
	  	    }
	  	    
	  	    public void SearchBT()
	  		 {
	  			 _bluetooth = BluetoothAdapter
	  						.getDefaultAdapter();
	
	  				if (!_bluetooth.isEnabled())
	  				{
	  					_bluetooth.enable();
	  				}
	  				
	  				 // 关闭再进行的服务查找
	  		        if (_bluetooth.isDiscovering()) {
	  					_bluetooth.cancelDiscovery();
	  		        }
	  		        //并重新开始
	  				_bluetooth.startDiscovery();
	  				_bluetooth.cancelDiscovery();
	  				
	  		 }
	  	    
	  	    public boolean pairBT(String strAddr, String strPsw)
	  		{
	  			boolean result = false;
	  			_bluetooth = BluetoothAdapter
	  					.getDefaultAdapter();
	
	  			if (!_bluetooth.isEnabled())
	  			{
	  				_bluetooth.enable();
	  			}
	  			// 关闭再进行的服务查找
	  	        if (_bluetooth.isDiscovering()) {
	  				_bluetooth.cancelDiscovery();
	  	        }
	  	        //并重新开始
	  			_bluetooth.startDiscovery();
	  			//_bluetooth.cancelDiscovery();
	
	  			if (!BluetoothAdapter.checkBluetoothAddress(strAddr))
	  			{ // 检查蓝牙地址是否有效
	
	  				//Log.d("mylog", "devAdd un effient!");
	  			}
	
	  			BluetoothDevice device = _bluetooth.getRemoteDevice(strAddr);
	
	  			if (device.getBondState() != BluetoothDevice.BOND_BONDED)
	  			{
	  				try
	  				{
	  					//Log.d("mylog", "NOT BOND_BONDED");
	  					BTClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
	  					BTClsUtils.createBond(device.getClass(), device);
	  					_device = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
	  					result = true;
	  				}
	  				catch (Exception e)
	  				{
	  					// TODO Auto-generated catch block
	
	  					//Log.d("mylog", "setPiN failed!");
	  					e.printStackTrace();
	  				} //
	
	  			}
	  			else
	  			{
	  				//Log.d("mylog", "HAS BOND_BONDED");
	  				try
	  				{
	  					BTClsUtils.createBond(device.getClass(), device);
	  					BTClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
	  					BTClsUtils.createBond(device.getClass(), device);
	  					_device = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
	  					result = true;
	  				}
	  				catch (Exception e)
	  				{
	  					// TODO Auto-generated catch block
	  					//Log.d("mylog", "setPiN failed!");
	  					e.printStackTrace();
	  				}
	  			}
	  			return result;
	  		}
	  		//[end]
	    
	    //[end]
	    
	  	//[start] 重写方法
	    @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.temperature);
			
			/*先默认从配置文件读取参数信息，如果页面传递了参数则覆盖配置文件参数*/
			readConfig();
			
			//[start] 传入参数赋值
			try {
				mDebugFlag = getIntent().getExtras().getInt("debugFlag");
			} catch (Exception e) {}
			
			try {
				mCLModuleType = getIntent().getExtras().getInt("clModuleType");
			} catch (Exception e) {}
			
			try {
				_btAddress = getIntent().getStringExtra("btAddress");
			} catch (Exception e) {}
			
			try {
				_btPassword = getIntent().getStringExtra("btPassword");
			} catch (Exception e) {}
			
			try {
				if (getIntent().getStringExtra("parms").trim().length() > 0)
					mFsl = Integer.parseInt(getIntent().getStringExtra("parms"));
			} catch (Exception e) {}
			//[end]
	
			//[start] 初始化图表
			txtValue = (TextView)findViewById(R.id.valueTxt);	
			txtMaxValue = (TextView)findViewById(R.id.maxValueTxt);	
			txtMinValue = (TextView)findViewById(R.id.minValueTxt);	
			txtValue.setTextColor(Color.BLUE);
			txtMaxValue.setTextColor(Color.RED);
			txtMinValue.setTextColor(Color.RED);
			
			mLineChart = (LineChart) findViewById(R.id.chart);  
			mLineData = InitLineData(Color.RED);
			initChart(Color.WHITE);
			RefreshChart();
			//[end]
			
			/*初始化Head*/
			initHeadView();	
			/*初始化Tip*/
			initTipView();
			initView();
			
			
			
			/* 内置才打开电源*/
			if(mCLModuleType == 0){
				operationBtn.setEnabled(false);
				saveBtn.setEnabled(false);
				setupBtn.setEnabled(false);
				
				openPower();
			}
		}
	
	    private void initView() {
				temperatureYValue = (TextView) findViewById(R.id.tv_temperature_yvalue);
				temperatureXValue = (TextView) findViewById(R.id.tv_temperature_yvalue);
			}

		@Override
	   	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	switch (keyCode){
	    	case KeyEvent.KEYCODE_BACK :
		    	if (saveBtn.isEnabled()){
		   			if (temperature_memu_operation.getText().toString().contains(getString(R.string.temperature_stop)))
						stop();					  			
		   			closePower();
		   			AppManager.getAppManager().finishActivity(TemperatureActivity.class);
		   			return true;
				}
				else {
					return false;
				}
	    	}
	   		return super.onKeyDown(keyCode, event);
	   	}
		
	    @Override
	    protected  void onActivityResult(int requestCode, int resultCode, Intent intent){
		 super.onActivityResult(requestCode, resultCode, intent);  
		 if(requestCode == REQCODE_TEMPERATURESET){
			 if (resultCode == Activity.RESULT_OK) {
				 mFsl = intent.getExtras().getInt("fsl");
				 mSaveType = intent.getExtras().getString("saveType");
				 if (AppContext.getMeasureType().equals(AppConst.MeasureType_Outer)){
					 _btAddress = intent.getExtras().getString("btAddress");
					 _btPassword = intent.getExtras().getString("btPwd");
				 }
			 }
		   }	     
	     }
	    /**
		 * 添加图表上面的竖线
		 * */
		private void addLine(int xHighLight) {
			LimitLine ll = new LimitLine(xHighLight, "");
			ll.setLineWidth(1);
			ll.setLineColor(Color.rgb(0, 0, 255));
			mLineChart.getXAxis().addLimitLine(ll);
		}
		//[end]
		
		/**
		 * OK键点击确认
		 * 
		 * @param event
		 * @return
		 */
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_UP) {
				if (temperature_memu_operation.getText().toString().contains(getString(R.string.temperature_stop)))
				{
					stop();
				}
				closePower();
				Intent intent=new Intent();
				intent.putExtra("rsts", Float.toString(getSaveData()));
			    
			    setResult(RESULT_OK,intent);
				AppManager.getAppManager().finishActivity(TemperatureActivity.this);
			}
			return super.dispatchKeyEvent(event);
		}
}


