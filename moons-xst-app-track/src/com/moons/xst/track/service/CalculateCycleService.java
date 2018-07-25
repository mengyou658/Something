/**
 *  @author LKZ
 */
package com.moons.xst.track.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.buss.cycle.CycleComMethod;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.common.DateTimeHelper;

/**
 * 周期运算服务
 * 
 * @author LKZ
 * 
 */
public class CalculateCycleService extends Service {

	private static final String TAG = "CalculateCycleService";
	private IBinder binder = new CalculateCycleService.LocalBinder();
	boolean mAllowRebind;
	//private Timer calculateCycleTimer = new Timer();
	//TimerTask task;
	List<Cycle> calCyclelist = new ArrayList<Cycle>();
	// 运算频率
	int calFrequency = 5;
	boolean autoCalYN = false;

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		super.onUnbind(intent);
		// All clients have unbound with unbindService()
		return mAllowRebind;
	}

	@Override
	public void onRebind(Intent intent) {
		// A client is binding to the service with bindService(),
		// after onUnbind() has already been called
	}

	@Override
	public void onCreate() {
		super.onCreate();
		calculateCycle();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 定义内容类继承Binder
	 * 
	 * @author LKZ
	 * 
	 */
	public class LocalBinder extends Binder {
		// 返回本地服务
		public CalculateCycleService getService() {
			return CalculateCycleService.this;
		}
	}
	Handler handler = new Handler();
	
	Runnable calculateCycleTimer = new Runnable() {
		@Override
		public void run() {
			new Thread(new Runnable() {
				public void run() {
					if (autoCalYN) {
						calculate();
					}
					handler.postDelayed(calculateCycleTimer,calFrequency * 60000);
				}
			}).start();
		}
	};
	private void calculateCycle() {
		handler.postDelayed(calculateCycleTimer,1000);
	}

	/**
	 * 计算周期
	 * 
	 * @param afterAutoCalcYN
	 *            本次计算完成后，是否启动定期计算
	 * @author LKZ
	 */
	public void calculateCycleNow(boolean afterAutoCalcYN) {
		autoCalYN = afterAutoCalcYN;
		calculate();
	}

	/**
	 * 计算周期
	 * 
	 * @author LKZ
	 * @Desc 运算加了同步锁，保证运算准确
	 */
	private synchronized void calculate() {
		if (AppContext.DJCycleBuffer == null
				|| AppContext.DJCycleBuffer.size() <= 0)
			return;
		try {
			for (Cycle _cycle : AppContext.DJCycleBuffer) {
				CycleComMethod.CalCycleTaskTime(_cycle, DateTimeHelper
						.StringToDate(DateTimeHelper.getDateTimeNow()));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 计算周期
	 * 
	 * @author LKZ
	 * @Desc 运算加了同步锁，保证运算准确
	 */
	public synchronized void calculate(Cycle _cycle, Date date,Boolean IsTempCal) {
		if (AppContext.DJCycleBuffer == null
				|| AppContext.DJCycleBuffer.size() <= 0)
			return;
		try {
			CycleComMethod.CalCycleTaskTime(_cycle,date,
					IsTempCal);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
