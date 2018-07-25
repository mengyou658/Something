package com.moons.xst.track.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moons.xst.track.R;

public class MapPointView extends LinearLayout {

	 ImageButton ib;  
	 TextView  et; 
	 private teListener listener = null; //事件回调接口
	    
	public MapPointView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	   //设置回调接口
    public void setRudderListener(teListener Listener) {
        listener = Listener;
    }
	
    public MapPointView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        LayoutInflater.from(context).inflate(R.drawable.btn_map_pointview, this, true);  
        init();  
  
    } 
    
    private void init() {  
        ib = (ImageButton) findViewById(R.id.ib);  
        et = (TextView) findViewById(R.id.et);  
        //et.addTextChangedListener(tw);// 为输入框绑定一个监听文字变化的监听器  
        // 添加按钮点击事件  
        ib.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
               // hideBtn();// 隐藏按钮  
                //et.setText("");// 设置输入框内容为空  
            }  
        });  
        ib.setVisibility(View.VISIBLE);
  
    }
    
    public void hideBtn() {  
        // 设置按钮不可见  
       // if (ib.isShown()) ib.setVisibility(View.GONE);  
  
    }  
    
    public void showBtn() {  
        // 设置按钮可见  
       // if (!ib.isShown()) ib.setVisibility(View.VISIBLE);  
  
    }
    
    public void setText(String S)
    {
    	et.setText(S);
    }
  
  
	//回调接口
	public interface teListener {
    void onClickListener();
	}
//	interface EdtInterface {  
  
	//	public void hideBtn();  
  
	//	public void showBtn();  
  
}
