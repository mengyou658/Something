package com.moons.xst.track.ui;

import greendroid.widget.CommolySearchView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.searchadapter.factorymethod.CommSearchAdapter;

import com.moons.xst.track.bean.BaseSearch;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.common.UIHelper;

import de.greenrobot.event.EventBus;

public class UnifySearchAty<T> extends BaseActivity {

//	public static UnifySearchAty instance = null;

	/**
	 * 搜索框
	 */
	CommolySearchView<BaseSearch> mCsvShow;
	/**
	 * 数据展示
	 */
	ListView mLvShow;
	/**
	 * 适配器
	 */
	private BaseAdapter mAdapter;
	/**
	 * 封装的搜索bean
	 */
	private CommonSearchBean<T> mCommonSearchBean;
	/**
	 * 搜索类型(用于区分不同的搜索)
	 */
	private String searchType;

//	public static <T extends UnifySearchAty<T>> UnifySearchAty<T> instance() {
//		if (instance != null) {
//			return instance;
//		}
//		return null;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_search);
//		instance = this;
		initData();
		if (mCommonSearchBean.getList() == null) {
			// 弹出软键盘
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					InputMethodManager imm = (InputMethodManager) UnifySearchAty.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}, 500);
			return;
		}
			
		initView();
		initAdapter();
		initSearch();
		// 弹出软键盘
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager imm = (InputMethodManager) UnifySearchAty.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
	}

	/**
	 * 初始化适配器,一般的扩展只需修改该方法即可
	 */
	private void initAdapter() {
		mAdapter = CommSearchAdapter.getAdapterByType(UnifySearchAty.this,
				mCommonSearchBean);
		if (mAdapter == null) {
			UIHelper.ToastMessage(UnifySearchAty.this, "");
			AppManager.getAppManager().finishActivity(UnifySearchAty.this);
		}
		
		// 点击事件
		mLvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				EventBus.getDefault().post(
						mCommonSearchBean.getList().get(position));
				AppManager.getAppManager().finishActivity(UnifySearchAty.this);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void initData() {
		mCommonSearchBean = (CommonSearchBean<T>) getIntent()
				.getSerializableExtra("seachBean");
		searchType = mCommonSearchBean.getSearchType();
	}

	/**
	 * 初始化view
	 */
	@SuppressWarnings("unchecked")
	private void initView() {
		mCsvShow = (CommolySearchView<BaseSearch>) findViewById(R.id.csv_show);
		mCsvShow.setHint(mCommonSearchBean.getHint());
		mLvShow = (ListView) findViewById(R.id.lv_show);
	}

	/**
	 * 初始化搜索
	 */
	@SuppressWarnings("unchecked")
	private void initSearch() {
		mLvShow.setAdapter(mAdapter);
		// 设置数据源
		mCsvShow.setDatas((List<BaseSearch>)mCommonSearchBean.getList());
		// 设置适配器
		mCsvShow.setAdapter(mAdapter);
		// 设置搜索
		mCsvShow.setSearchDataListener(new CommolySearchView.SearchDatas<BaseSearch>() {
			@Override
			public List<BaseSearch> filterDatas(List<BaseSearch> datas,
					List<BaseSearch> filterdatas, String inputstr) {
				for (int i = 0; i < datas.size(); i++) {
					// 筛选条件,如果有必要,在此做修改
					if (datas.get(i).getSearchCondition().contains(inputstr)) {
						filterdatas.add(datas.get(i));
					}
				}
				return filterdatas;
			}
		});
	}
}
