package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJLine_ListViewAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.InputTools;

public class SearchMyWorkAty extends BaseActivity {
	
	private EditText etcontent;
	private ImageView ivdelete;
	private RelativeLayout rlcancel;
	private ListView lvdjline;
	
	private DJLine_ListViewAdapter lvDJLineAdapter;
	List<DJLine> mData = new ArrayList<DJLine>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_mywork);
		
		//ButterKnife.bind(this);
		etcontent = (EditText) findViewById(R.id.search_content);
		ivdelete = (ImageView) findViewById(R.id.delete);
		ivdelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				etcontent.setText("");
			}
		});
		rlcancel = (RelativeLayout) findViewById(R.id.rl_search_cancel);
		rlcancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(SearchMyWorkAty.this);
			}
		});
		lvdjline = (ListView) findViewById(R.id.listview_dianjian_djline);
		/* 初始化数据 */
		initData();		
	}
	
	private void initData() {
		if (AppContext.DJLineBuffer == null)
			return;
		
		for (DJLine djline : AppContext.DJLineBuffer) {
			mData.add(djline);
		}
		
		lvDJLineAdapter = new DJLine_ListViewAdapter(this,
				mData, R.layout.listitem_djline);
		if (lvdjline == null) {
			return;
		}
		lvdjline.setAdapter(lvDJLineAdapter);
		
		lvdjline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				TextView aTitle = (TextView) view
						.findViewById(R.id.djline_listitem_title);
				if (aTitle == null) {
					return;
				}
				final DJLine adjline = (DJLine) aTitle.getTag();

				if (adjline == null)
					return;

				InputTools.HideKeyboard(etcontent);
				Intent intent = new Intent();
				intent.putExtra("djline", adjline);
				setResult(RESULT_OK,intent);
				
				AppManager.getAppManager().finishActivity(SearchMyWorkAty.this);
			}
		});
		
		etcontent.setOnEditorActionListener(new OnEditorActionListener() {  
            @Override  
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            	if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            		InputTools.HideKeyboard(etcontent);
            	}
                return false;  
            }  
        });  
		
		etcontent.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub		
			}
			
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivdelete.setVisibility(View.GONE);
				} else {
					ivdelete.setVisibility(View.VISIBLE);
				}
				mHandler.post(eChanged);
			}
		});
	}
	
	Handler mHandler = new Handler();
	
	Runnable eChanged = new Runnable() {
		
	    @Override
	    public void run() {
	    	
	          // TODO Auto-generated method stub
	          String data = etcontent.getText().toString();			
	          mData.clear();			
	          getmDataSub(mData, data);				
	          lvDJLineAdapter.notifyDataSetChanged();
				
	    }
	};
	
	/**
	 * 根据输入值筛选数据源
	 * @param mDataSubs
	 * @param data
	 */
	private void getmDataSub(List<DJLine> mDataSubs, String data)
	{
	     for(int i = 0; i < AppContext.DJLineBuffer.size(); ++i){
	           if(AppContext.DJLineBuffer.get(i).getLineName().contains(data)){
	                mDataSubs.add(AppContext.DJLineBuffer.get(i));
	            }
	     }
	}
}