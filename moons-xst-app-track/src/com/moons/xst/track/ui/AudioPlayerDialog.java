package com.moons.xst.track.ui;

import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.widget.MoonsBaseDialog;

public class AudioPlayerDialog extends MoonsBaseDialog{

	private String  path;
	private int pathNum;
	private int timelong;
	private String timeStr;
	
	private  List<String> pathList;
	private MediaPlayer player;
	private ImageButton play;
	private ImageButton next;
	private ImageButton back;
	private ImageButton close;
	private TextView timeTextView;
	private Context con;
	
	private int costtime = 0;
	private Handler mHandler;
	private boolean isPause = false;
	private boolean isRun = true;
	
	public AudioPlayerDialog(Context context,String aupath,List<String> list) {
		super(context);
		// TODO Auto-generated constructor stub
		this.con = context;
		this.setCancelable(false);
		this.setTitle("录音播放");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recordplayer);
		this.path = aupath;
		this.pathList = list;
		player = new MediaPlayer();
		pathNum = _indexOf(pathList, path);
		initView();
		Start();
		refreshView();
	}
	
	@SuppressLint("HandlerLeak")
	private void refreshView() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1 && msg.obj != null) {
					timeTextView.setText(msg.obj.toString());
				}
			}
		};
		this.uiReflashClock();
	}
	/**
	 * UI信息刷新计时器
	 */
	private void uiReflashClock() {
		costtime = 0;
				new Thread(new Runnable() { // UI thread
					@Override
					public void run() {
					while (isRun) {

						Message msg = Message.obtain();
						if (!isPause) {
							costtime ++;
							int mins = costtime / 60;
							String minString = mins < 10 ? "0" + mins : String
									.valueOf(mins);
							int seconds = costtime % 60;
							String secondsString = seconds < 10 ? "0" + seconds
									: String.valueOf(seconds);
							String s =(String.format(minString + ":"
									+ secondsString)+"/" + timeStr);
							msg.what = 1;
							msg.obj = s;
							mHandler.sendMessage(msg);
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						}
					}
				}).start();
	}
	
	private void Start() {
		try {
			player.reset();
			String dataSource = pathList.get(pathNum);
			player.setDataSource(dataSource);
			player.prepare();
			player.start();
			timelong = player.getDuration()/1000 +1;//获取播放时间
			int mins = timelong / 60000;
			String minString = mins < 10 ? "0" + mins : String
					.valueOf(mins);
			int seconds = timelong % 60;
			String secondsString = seconds < 10 ? "0" + seconds
					: String.valueOf(seconds);
			timeStr = String.format(minString + ":"+ secondsString);
			timeTextView.setText("00:00/" + timeStr);
			costtime = 0;
			//当前播放完自动下面
			player.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					next();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void pause() {
		if (player.isPlaying()) {
			player.pause();
			isPause = true;
		}
		else {
			player.start();
			isPause = false;
		}
	}
	
	private void stop(){
		if (player.isPlaying()) {
			player.stop();
		}
	}
	
	private void next() {
		pathNum = pathNum == pathList.size() - 1? 0:pathNum+1;
		Start();
	}
	
	private void back() {
		pathNum = pathNum == 0?pathList.size() - 1:pathNum-1;
		Start();
	}
	
    private void close() {  
		if (player.isPlaying()) {
			player.stop();
		}
		player.release();
		isRun = false;
		this.dismiss();
    }  
	
	private void initView() {
		play = (ImageButton) findViewById(R.id.play_button);
		back = (ImageButton) findViewById(R.id.before_button);
		next = (ImageButton) findViewById(R.id.next_button);
		close = (ImageButton) findViewById(R.id.close_button);
		timeTextView = (TextView) findViewById(R.id.time);
		next.setOnClickListener(new ButtonClickListener());  
		back.setOnClickListener(new ButtonClickListener());  
		play.setOnClickListener(new ButtonClickListener());  
		close.setOnClickListener(new ButtonClickListener());  
	}
	
    private final class ButtonClickListener implements View.OnClickListener{  
        @Override  
        public void onClick(View v) {  
            ImageButton button = (ImageButton) v;
            try {  
                switch (v.getId()) {//通过传过来的Buttonid可以判断Button的类型   
                case R.id.play_button:
    				if (player.isPlaying()) {
    					((ImageButton) play).setBackgroundDrawable(con.getResources().getDrawable(
    							R.drawable.play));
    				}
    				else {
    					((ImageButton) play).setBackgroundDrawable(con.getResources().getDrawable(
    							R.drawable.pause));
    				}
    				pause();
                    break;  
                case R.id.before_button:  
                	back();
                    break;  
                case R.id.next_button:  
                	next();
                    break;  
                case R.id.close_button:  
                	close();
                    break;  
                }  
            } catch (Exception e) {//抛出异常   
                e.printStackTrace();
            }  
        }         
    } 
	
	private int _indexOf(List<String> arrayList, String paString) {
		int index = -1;
		if(arrayList == null || arrayList.isEmpty()) {
			return index;
		}
		Iterator<String> iter = arrayList.iterator();
		while(iter.hasNext()) {
			index++;
			if(iter.next().compareTo(paString) == 0) {
				return index;
			}
		}
		index = -1;
		return index;
	}
}


