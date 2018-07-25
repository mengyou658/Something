package com.moons.xst.track.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
public class MeasureSpeedActivity extends BaseActivity {

	
	private static int mDebugFlag = 0;
	/*0-表示内置，1-表示外置*/
	private static int mCLModuleType = 0;
	
	//private static int mResultCode = 1;
	private static int mInitYmax = 10;

	private static int mCurValue = 0;
	private static int mMaxValue =  Integer.MIN_VALUE;
	private static int mMinValue =  Integer.MAX_VALUE;

	private RelativeLayout operationBtn, saveBtn;
	private TextView speed_memu_operation, speed_memu_operation_desc;
	private ImageButton returnButtom;
	
	static TextView txtValue = null;	
	static TextView txtMaxValue = null;	
	static TextView txtMinValue = null;	
	private static LineChart mLineChart = null;  
	
	private LoadingDialog loading;
	
	private static LineData mLineData = null;
	private static int mXChartCount = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measurespeed);
		//先默认从配置文件读取参数信息，如果页面传递了参数则覆盖配置文件参数
		LoadConfig();
		
		try {
			mDebugFlag = getIntent().getExtras().getInt("debugFlag");
		} catch (Exception e) {
			
		}
		try {
			mCLModuleType = getIntent().getExtras().getInt("clModuleType");
		} catch (Exception e) {}
//		try {
//			mResultCode = getIntent().getExtras().getInt("resultCode");
//		} catch (Exception e) {
//			
//		}
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
		
		returnButtom = (ImageButton) findViewById(R.id.speed_head_Rebutton);
		returnButtom.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (saveBtn.isEnabled()){
		   			if (speed_memu_operation.getText().toString().contains(getString(R.string.speed_stop)))
						stop();					   			
		   			closePower(); 
		   			AppManager.getAppManager().finishActivity(MeasureSpeedActivity.class);
	   			}
			}
		});
		
		saveBtn = (RelativeLayout) findViewById(R.id.ll_speed_memu_ok);
		saveBtn.setOnClickListener(new View.OnClickListener() 
		{
	
			@Override
			public void onClick(View v) {
				
				if (speed_memu_operation.getText().toString().contains(getString(R.string.speed_stop)))
				{
					stop();
					
				}
				closePower();
				Intent intent=new Intent();
				intent.putExtra("rsts", Float.toString(mCurValue));
			   
			    setResult(RESULT_OK,intent);

				finish();
			}
			
		});
		
		speed_memu_operation = (TextView) findViewById(R.id.speed_memu_operation);
		speed_memu_operation_desc = (TextView) findViewById(R.id.speed_memu_operation_desc);
		operationBtn = (RelativeLayout) findViewById(R.id.ll_speed_memu_operation);
		operationBtn.setOnClickListener(new View.OnClickListener() 
		{
	
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				
				String oldTextString = (String) speed_memu_operation.getText();
				
				if (oldTextString.contains(getString(R.string.speed_stop)))
				{
					stop();
				}
				else {
					start();
				}				
			}
			
		});
		
		if(mCLModuleType == 0) {
			saveBtn.setEnabled(false);
			operationBtn.setEnabled(false);		
			openPower();
		} else {
			UIHelper.ToastMessage(getApplication(), R.string.speed_measure_hint);
			AppManager.getAppManager().finishActivity(MeasureSpeedActivity.class);
		}
	}
	
	/**
	 * 从配置文件读取参数配置信息
	 */
	private void LoadConfig()
	{
		
		
	}
	/**
	 * 获取要保存的值
	 * @return
	 */
	private float getSaveData()
	{
		
		return mCurValue;
	}
	/**
	 * 启动测量
	 */
	private void start()
	{	
		initData();
		//打开电源后先读取两次数据扔掉
		getCLValue();
		getCLValue();
		speed_memu_operation.setText(R.string.speed_stop);
		speed_memu_operation_desc.setText(R.string.speed_stop_desc);
		readCLData();
	}
	/**
	 * 停止测量
	 */
	private void stop()
	{
		handler.removeCallbacks(runnable);           //停止Timer		
		
		speed_memu_operation.setText(R.string.speed_start);
		speed_memu_operation_desc.setText(R.string.speed_start_desc);
		
	}
	Handler mRefreshBtnEnableHandler = new Handler() {   
	    @Override   
		public void handleMessage(Message msg) {   
	    	if (loading != null)
	    		loading.dismiss();
	    	if (msg.what == 1) {
		    	//刷新数据
		    	saveBtn.setEnabled(true);
				operationBtn.setEnabled(true);			
				start();
	    	} else {
	    		UIHelper.ToastMessage(getApplication(), 
						((Exception)(msg.obj)).getMessage());
	    	}
		    //super.handleMessage(msg);   
			}   
		};  
    /**
     * 打开电源
     */
	private int openPower()
	{
		loading = new LoadingDialog(this);
		loading.setLoadText(getString(R.string.speed_start_process));
		loading.setCancelable(false);
		loading.show();
		new Thread(){
			 @Override
		     public void run() {
				    Message msg = Message.obtain();
					if(mDebugFlag == 0)
					{
						try {
							 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
				                   ServiceManager.getService("imeasure"));  
				             	
							 int rst = imeasureService.openRotationlSpeed();
							 msg.what = 1;							
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = -1;
							msg.obj = e;
						}
					
					}
					else
					{
						try {
							sleep(5000);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							
						}
					}
				   mRefreshBtnEnableHandler.sendMessage(msg);//向Handler发送消息，   
						
					
			 }
		 }.start();
		 return 0;
	}
	
	/**
     * 关闭电源
     */
	private int closePower()
	{
		int rst = -1;
		if(mDebugFlag == 0)
		{
			try {
				 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
	                   ServiceManager.getService("imeasure"));  
	            
				 rst = imeasureService.closeRotationlSpeed();
				
			} catch (Exception e) {
				rst = -1;
			}
		}
		else
			rst = 0;
		return rst;
	}
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		mCurValue = 0;
		mMaxValue = Integer.MIN_VALUE;
		mMinValue = Integer.MAX_VALUE;
		
		List<Entry> yValues = mLineData.getDataSetByIndex(0).getYVals();
		for(int i = 0; i < yValues.size(); i ++)
		   {
			   yValues.get(i).setVal(0);
			   
		   }
	}
	/**
	 * 获取转速值
	 * @return
	 */
	private static String getCLValue()
	{
		if(mDebugFlag == 1)
		{
			 DecimalFormat df = new DecimalFormat("#0");
			 String cl = df.format(50 * (Math.random()));
			 return cl;
		}
		else {
			IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
		               ServiceManager.getService("imeasure"));  
		        
				int val;
				try {
					val = imeasureService.getRotationlSpeed();
//					if (val >=10000)
//						return "值错误"; 
				} catch (RemoteException e) {
					// TODO 自动生成的 catch 块
					return "值错误";
				}
				 String dataString =  String.valueOf(val);
				 return dataString;
		}
		
	
	}
	/**
	 *定时读取温度
	 */
	private void readCLData() {
		
		handler.postDelayed(runnable,100);         // 开始Timer


		
	}
	
	private static Handler handler = new Handler( );

	private static Runnable runnable = new Runnable( ) {

	public void run ( ) {

		try {
			mCurValue = Integer.parseInt(getCLValue());
			if (mMaxValue < mCurValue)
			{
				mMaxValue = mCurValue;
				ResetYaxMaxValueAndMinValue();
			}
			if (mMinValue > mCurValue)
			{
				mMinValue = mCurValue;
				ResetYaxMaxValueAndMinValue();
			}
			String vl = Integer.toString(mCurValue);
        	txtValue.setText(vl);
        	txtMaxValue.setText(Integer.toString(mMaxValue));
        	txtMinValue.setText(Integer.toString(mMinValue));
			AddData(vl);
			RefreshChart();
		} catch (Exception e) {
			// TODO: handle exception
		}
			
	   handler.postDelayed(this,500);     //postDelayed(this,1000)方法安排一个Runnable对象到主线程队列中


	}


	};
	/**
	    * 往图表数据里加数据
	    * @param data
	    */
	   private static void AddData(String data)
	   {
		   if (mLineData == null)
			   return;
		  
		   List<Entry> yValues = mLineData.getDataSetByIndex(0).getYVals();
		   int value = (int)Integer.parseInt(data);  
		   int size = yValues.size();
		   if (size < mXChartCount)
		   {
			   yValues.add(new Entry(value, mXChartCount - size - 1));  
		   }
		   else
		   {
			   for(int i = size - 1; i > 0; i --)
			   {
				   yValues.get(i).setVal(yValues.get(i - 1).getVal());
				   
			   }
			   
			   yValues.get(0).setVal(value);
		   }

	   }
	/**
	 * 重新刷新坐标值
	 */
	private static void ResetYaxMaxValueAndMinValue()
	{
		float axMax = mLineChart.getAxisLeft().getAxisMaxValue();
		float axMin = mLineChart.getAxisLeft().getAxisMinValue();
		if (mMaxValue > axMax)
		{
			int bs = (int) (mMaxValue % mInitYmax ==0?mMaxValue/mInitYmax:mMaxValue/mInitYmax + 1);
			mLineChart.getAxisLeft().setAxisMaxValue(bs * mInitYmax );
		}
		if (mMinValue < axMin)
		{
			int bs = (int) (mMinValue % mInitYmax ==0?mMinValue/mInitYmax:mMinValue/mInitYmax - 1);
			mLineChart.getAxisLeft().setAxisMinValue(bs * mInitYmax );
		}
	}
	/**
	 * 刷新图
	 */
	private static void RefreshChart()
	{
		// add data     
		mLineChart.setData(mLineData); // 设置数据     
		mLineChart.invalidate();
	}
	  /**
	   * 设置图表显示样式
	   */
	   private void initChart( int color) { 
		   
		   LineChart lineChart = mLineChart;
	       lineChart.setDrawBorders(false);  //是否在折线图上添加边框     
	   
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
	           LineDataSet lineDataSet = new LineDataSet(yValues, getString(R.string.speed_tendency) /*显示在比例图上*/);    
	           // mLineDataSet.setFillAlpha(110);     
	           // mLineDataSet.setFillColor(Color.RED);     
	          
	           //用y轴的集合来设置参数     
	           lineDataSet.setLineWidth(1.75f); // 线宽     
	           lineDataSet.setCircleSize(0f);// 显示的圆形大小     
	           lineDataSet.setColor(color);// 显示颜色     
	           lineDataSet.setCircleColor(color);// 圆形的颜色     
	          
	           lineDataSet.setHighLightColor(color); // 高亮的线的颜色     
	       
	           ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();    
	           lineDataSets.add(lineDataSet); // add the datasets     
	       
	           // create a data object with the datasets     
	           LineData lineData = new LineData(xValues, lineDataSets);    
	       
	           return lineData;    
	      }    
        
	    @Override
	   	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	if (saveBtn.isEnabled()){
	   			if (speed_memu_operation.getText().toString().contains(getString(R.string.speed_stop)))
					stop();					   			
	   			closePower(); 
	   			AppManager.getAppManager().finishActivity(MeasureSpeedActivity.class);
   			}
   			else {
   				return false;
			}
	   		return super.onKeyDown(keyCode, event);
	   	}
	    
	    /**
		 * OK键点击确认
		 * 
		 * @param event
		 * @return
		 */
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_UP) {
				if (speed_memu_operation.getText().toString().contains(getString(R.string.speed_stop)))
				{
					stop();
					
				}
				closePower();
				Intent intent=new Intent();
				intent.putExtra("rsts", Float.toString(mCurValue));
			   
			    setResult(RESULT_OK,intent);
				AppManager.getAppManager().finishActivity(MeasureSpeedActivity.this);
			}
			return super.dispatchKeyEvent(event);
		}
}


