package com.moons.xst.track.ui;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IImeasureService;
import android.os.Message;
import android.os.ServiceManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.WhewView;

public class WalkieTalkieActivity extends BaseActivity{
	
		public static final int REQCODE_SET = 1;
		private static int mDebugFlag = 0;
		public static final int PopMenu_SETTING = 0;// 设置
		//当前信道名称
		private String mCurName = "";
		//当前频率段
		private float mCurXD = (float) 460.5;
		//当前音量
		private int  mCurYL = 100;
		AudioManager mAudioManager = null;
		private int mMaxAudio = 100;
		private int mCurAudio = 100;
		
		private QuickActionWidget mGrid;// 快捷栏控件
		private WhewView wv;
		private TextView state, title;
		private Button speak;
		private ImageButton returnbtn, morebtn;
		MyReceiver receiver;
	 
	    @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.walkie_talkie);
			//先默认从配置文件读取参数信息，如果页面传递了参数则覆盖配置文件参数
			LoadConfig();
			setXD(mCurXD);
			
			title = (TextView)findViewById(R.id.walkie_talkie_title);
			title.setText(mCurName + "(" + mCurXD + ")");
			
			initQuickActionGrid();
			
			returnbtn = (ImageButton)findViewById(R.id.home_head_Rebutton);
			returnbtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					AppManager.getAppManager().finishActivity(WalkieTalkieActivity.class);
				}
			});
			
			morebtn = (ImageButton)findViewById(R.id.walkie_talkie_head_morebutton);
			morebtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mGrid.show(v);
				}
			});
			
			wv = (WhewView) findViewById(R.id.wv);
			state = (TextView)findViewById(R.id.walkie_talkie_tips_line1);
			try {
				mDebugFlag = getIntent().getExtras().getInt("debugFlag");
			} catch (Exception e) {
				
			}
			try {
				String typeMsg = getIntent().getExtras().getString("RevMsg");
				if (typeMsg.equals("::PPT_OPEN::"))
		         {
					state.setText("状态    打开");
					
		         }
			} catch (Exception e) {
				
			}
			
			speak = (Button)findViewById(R.id.btn_speaking);
			speak.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO 自动生成的方法存根
					switch (event.getAction()){
					case MotionEvent.ACTION_DOWN:
						UIHelper.ToastMessage(WalkieTalkieActivity.this, "暂不支持");
						break;
					}
					return false;
				}
			});
			
			mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			
			//将模块音量设置为最大
			setMKYL(9);
			refershCurYL(0);
			
			regReceiver();	
		}
	    
	    @Override
		protected void onDestroy() {
			close();
			super.onDestroy();
		}
	    
	    /**
		 * 初始化快捷栏
		 */
		private void initQuickActionGrid() {
			mGrid = new QuickActionGrid(this);

			// 设置
			mGrid.addQuickAction(new MyQuickAction(this,
					R.drawable.main_pop_setting_icon, R.string.walkie_talkie_setting_head_title));

			mGrid.setOnQuickActionClickListener(mActionListener);
		}

		/**
		 * 快捷栏item点击事件
		 */
		private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
			public void onQuickActionClicked(QuickActionWidget widget, int position) {
				switch (position) {
				case PopMenu_SETTING:// 测温
					showWalkieTalkieSet(WalkieTalkieActivity.this);
					break;
				}
			}
		};
	    
	    private void refershCurYL(int addNum)
		{
//			mMaxAudio = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
//			mCurAudio =  mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
//			mCurAudio = mCurAudio + addNum ;
//			if (mCurAudio > mMaxAudio)
//				mCurAudio = mMaxAudio;
//			if (mCurAudio < 0)
//				mCurAudio = 0;
//	        mCurYL = (mCurAudio  * 127)/mMaxAudio;
//	        if (mCurYL < 1)
//	        	mCurYL = 1;
//	        if (mCurYL > 127)
//	        	mCurYL = 127;
	       mCurYL = 121;
	        		
	       setYL(mCurYL);
		}
	
		/**
		 * 注册接收器
		 */
	    private void regReceiver()
	    {
	    	//注册接收器
	    	receiver=new MyReceiver();
	    	IntentFilter filter=new IntentFilter();
	    	filter.addAction("android.intent.action.WalkieActivity");
	    	WalkieTalkieActivity.this.registerReceiver(receiver,filter);
	
	    }
    
	    /**
	     * 解除注册接收器
	     */
	    private void unRegReceiver()
	    {
	    	WalkieTalkieActivity.this.unregisterReceiver(receiver);
	    }
    
	    /**
		 * 从配置文件读取参数配置信息
		 */
		private void LoadConfig()
		{
			mCurYL = 50;		
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				String path = AppConst.WalkieTalkieConfigPath();
				File f = new File(path);
				Document doc = db.parse(f);
				doc.normalize();
				// 找到根Element
				Element root = doc.getDocumentElement();
				NodeList nodes = root.getElementsByTagName("Setting");
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) (nodes.item(i));
					// 获取river中name属性值
					if (StringUtils.toBool(((Element) element).getAttribute("Checked"))) {
						mCurXD = Float.parseFloat(((Element) element).getAttribute("Frequency"));
						mCurName = ((Element) element).getAttribute("Name").toString();
						} 
				   }
				}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
	
		/**
		 * 说话模式
		 */
		private void start()
		{
			if (getSpeakStatus() != 1)
				speakCtrl(1);
		}
		/**
		 * 听话模式
		 */
		private void stop()
		{
			if (getSpeakStatus() != 0)
				speakCtrl(0);
		}
	
		Handler mRefreshBtnEnableHandler = new Handler() {   
		    @Override   
			public void handleMessage(Message msg) {   
		    	//刷新数据
	//	    	btnCancel.setEnabled(true);
	//			btnSet.setEnabled(true);
				
			    super.handleMessage(msg);   
				}   
			};  
		/**
	     * 打开对讲机模块
	     */
		private int open()
		{		
				new Thread(){
					 @Override
				     public void run() {						     
							if(mDebugFlag == 0)
							{
								if (getStatus() != 1)
								{
									try {
										 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
							                   ServiceManager.getService("imeasure"));  					             	
										 int rst = imeasureService.wptt_open();									
									} catch (Exception e) {									
									}
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
						   mRefreshBtnEnableHandler.sendMessage(new Message());//向Handler发送消息，   							
					 }
				 }.start();			
			 return 1;
		}
		 /**
		 /**
		 * 关闭对讲机模块
		 */
		   private int close() {
			unRegReceiver();
			int rst = -1;
			if(mDebugFlag == 0)
			{
				if (getStatus() != 0)
				{
					try {
						 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
			                   ServiceManager.getService("imeasure"));  
			            
						 rst = imeasureService.wptt_close();
						
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
		 /**
	     * 获取对讲机状态，返回值1 表示处在打开状态 0 表示处在关闭状态
	     */
		   private int getStatus(){
			int rst = -1;
			if(mDebugFlag == 0)
			{
				try {
					 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
		                   ServiceManager.getService("imeasure"));  
		            
					 rst = imeasureService.wptt_getStatus();
					
				} catch (Exception e) {
					rst = -1;
				}
			}
			else
				rst = 1;
			return rst;
		   }
		    
		   /**
		    * 设置模式，1表示说话模式 0表示听模式
		    * @param ctrl
		    * @return 返回值 1表示说话模式 0表示听模式
		    */
			private int speakCtrl(int ctrl)
			{
				int rst = -1;
				if(mDebugFlag == 0)
				{
					try {
						 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
			                  ServiceManager.getService("imeasure"));  
			           
						 rst = imeasureService.wppt_speakCtrl(ctrl);
						
					} catch (Exception e) {
						rst = -1;
					}
				}
				else
					rst = ctrl;
				return rst;
			}  
			
			 /**
			 /**
		     * 获取模式，返回值 1表示说话模式 0表示听模式
		     */
			 private int getSpeakStatus(){
				int rst = -1;
				if(mDebugFlag == 0)
				{
					try {
						 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
			                   ServiceManager.getService("imeasure"));  
			            
						 rst = imeasureService.wptt_getSpeakStatus();
						
					} catch (Exception e) {
						rst = -1;
					}
				}
				else
					rst = 1;
				return rst;
			}
			 
			/**
			 * 设置喇叭音量
			 * @param vol 取值范围 1～127
			 */
			private int setYL(int vol)
			{
				int rst = -1;
				if(mDebugFlag == 0)
				{
					try {
						 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
			                  ServiceManager.getService("imeasure"));  
			           
						 rst = imeasureService.wppt_setSpeakerVolume(vol);
						
					} catch (Exception e) {
						rst = -1;
					}
				}
				else
					rst = 0;
				return rst;
			}
	
			/**
			 * 设置模块音量
			 * @param vol 取值范围 1～9
			 */
			private int setMKYL(int vol){
				int rst = -1;
				if(mDebugFlag == 0)
				{
					try {
						 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
			                  ServiceManager.getService("imeasure"));  
			           
						 rst = imeasureService.wppt_setVolume(vol);
						
					} catch (Exception e) {
						rst = -1;
					}
				}
				else
					rst = 0;
				return rst;
			}
			/**
			 * 设置对讲机频率段
			 * @param chn 取值范围 400.0000MHz～480.0000MHz，取值必须为6.25K或5K的整数倍
			 */
			private int setXD(float chn){
				int rst = -1;
				if(mDebugFlag == 0)
				{
					try {
						 IImeasureService imeasureService =  IImeasureService.Stub.asInterface(  
			                  ServiceManager.getService("imeasure"));  
			           
						 rst = imeasureService.wppt_setChannel(chn);
						
					} catch (Exception e) {
						rst = -1;
					}
				}
				else
					rst = 0;
				return rst;
			}
			
			@Override
		   	public boolean onKeyDown(int keyCode, KeyEvent event) {
		    	
		    	switch (keyCode) { 
		    	       case KeyEvent.KEYCODE_BACK:
		    	    	   AppManager.getAppManager().finishActivity(WalkieTalkieActivity.class);
		    	    	   break;
		   	           case KeyEvent.KEYCODE_VOLUME_DOWN:  
		   	        	    super.onKeyDown(keyCode, event);
		   	        	    refershCurYL(-1);
		   		            break;
		   	   
		   		        case KeyEvent.KEYCODE_VOLUME_UP:  
		   		        	super.onKeyDown(keyCode, event);
		   		        	refershCurYL(1);
		   		            break;
		   		        case KeyEvent.KEYCODE_VOLUME_MUTE:  
		   		        	super.onKeyDown(keyCode, event);
		   		        	refershCurYL(0);
		   		            break;
		   		       }
		    
		   		return super.onKeyDown(keyCode, event);
		   	}
			
			public class MyReceiver extends BroadcastReceiver {

		    	//自定义一个广播接收器
		    	@Override
		    	public void onReceive(Context context, Intent intent) {
		    	Bundle bundle=intent.getExtras();

		    	String typeMsg = bundle.getString("RevMsg");
		    	
		    	//处理接收到的内容
		    	 if (typeMsg.equals("::PPT_CLOSE::"))
		         {
		    		state.setText("状态    关闭");
					finish();
		         }
		         else if (typeMsg.equals("::PPT_SPEEK::"))
		         {
		        	 wv.start();
		        	 //txtInfo.setText("说状态...释放F2取消对讲");
		         }
		         else if (typeMsg.equals("::PPT_LISTEN::"))
		         {
		        	 wv.stop();
		        	 //txtInfo.setText("听状态...按下F2进行对讲，长按F1退出");
		         }
		    	}

		    	public MyReceiver(){
		    	//构造函数，做一些初始化工作，本例中无任何作用
		    		}
		    	}

		    /**
			 * 转至设置页面
			 * 
			 * @param
			
			 */
			private  void showWalkieTalkieSet(Context context) {
				Intent intent = new Intent(context, WalkieTalkieSetting.class);
				startActivityForResult(intent,REQCODE_SET);
			}
			@Override
		    protected  void onActivityResult(int requestCode, int resultCode, Intent intent){
			 super.onActivityResult(requestCode, resultCode, intent);  
			 if(requestCode == REQCODE_SET){
				if (resultCode == Activity.RESULT_OK){
					mCurXD = Float.parseFloat(intent.getExtras().getString("frequency"));
					setXD(mCurXD);
					mCurName = intent.getExtras().getString("xdname");
					title.setText(mCurName + "(" + mCurXD + ")");
				}
		     }		     
		  }
}