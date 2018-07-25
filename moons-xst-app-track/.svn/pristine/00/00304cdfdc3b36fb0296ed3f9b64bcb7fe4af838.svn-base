package com.moons.xst.track.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Bundle;
import android.os.IBinder;

import com.moons.xst.track.ui.WalkieTalkieActivity;


public class WalkieTalkieService extends Service {
	
		private static int mDebugFlag = 0;
		boolean isStop=false;

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		public void onCreate(){
			super.onCreate();
			localReceiveStart();
		}

		@Override
		public void onStart(Intent intent,int startId){	
			super.onStart(intent, startId);

			try {
				mDebugFlag = intent.getExtras().getInt("debugFlag");
			} catch (Exception e) {		
			}
			//localReceiveStart();			
		}

		@Override
		
		public void onDestroy() {		
			isStop=true;//即使service销毁线程也不会停止，所以这里通过设置isStop来停止线程
			try {
				if (skReceiver != null){
					skReceiver.close();
					skReceiver = null;
				}
			} catch (IOException e) {			
			}
			try {
				if (skServer != null)
				{
					skServer.close();
					skServer = null;
				}
				
			} catch (IOException e) {
				
			}			
			super.onDestroy();		
		}

		LocalServerSocket skServer = null;
		LocalSocket skReceiver = null;
		private void localReceiveStart()
		{
			// 接收线程    
			Thread localReceive = new Thread(){
				
		    @Override
		    public void run() {	   	
		       try {
		           skServer = new LocalServerSocket("ppt.localsocket");
		           
		           boolean running = true;
		           InputStream in = null;
		           DataInputStream din = null;
		
		           while (running) {
		           	   skReceiver = skServer.accept();
		           	
		               if (skReceiver != null) {
		            	   in = skReceiver.getInputStream();  // get input stream and create data input stream
		                   din = new DataInputStream(in);
		
		                   String typeMsg = din.readLine();        //read
		                   if (isStop)
		                  	{
		                  		break;
		                  	}
		                   else {
		                	   if (typeMsg.equals("::PPT_OPEN::"))
		                       {	                           	
		                		   Intent intent = new Intent(WalkieTalkieService.this, WalkieTalkieActivity.class);
		                   		   intent.putExtra("debugFlag", mDebugFlag);
		                   		   intent.putExtra("RevMsg", typeMsg);
		                   		   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		                   		   WalkieTalkieService.this.startActivity(intent);	                           
		                       }
		                       else {
		                    	    Intent intent1 = new Intent();		
		                      		intent1.putExtra("RevMsg", typeMsg);		
		                      		intent1.setAction("android.intent.action.WalkieActivity");//action与接收器相同		
		                      		sendBroadcast(intent1);
		        			}	                      
						}		              
		             }	                 	            
		    	 }
		       }catch (IOException e) {
		       }	       
		   } 
		};
		localReceive.start();
    }
}


