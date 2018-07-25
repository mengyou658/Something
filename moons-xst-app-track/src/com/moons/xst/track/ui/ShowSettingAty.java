package com.moons.xst.track.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.AMDragRateAdapter;
import com.moons.xst.track.bean.custom;
import com.moons.xst.track.widget.dragsortlist.DragSortListView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ShowSettingAty extends BaseActivity {
	ImageButton home_head_Rebutton;
	List<custom> list;
	DragSortListView dragsort_list;
	AMDragRateAdapter adapter;
	boolean isTwoBil = false;
	boolean isOverhaul = false;

	AppContext appcontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_operate_setting);
		appcontext = (AppContext) getApplication();
		isTwoBil = getIntent().getBooleanExtra("TwoBil", false);
		isOverhaul = getIntent().getBooleanExtra("Overhaul", false);
		initData();
		init();
	}

	private void init() {
		dragsort_list = (DragSortListView) findViewById(R.id.dragsort_list);
		adapter = new AMDragRateAdapter(this, list, appcontext);
		// 得到滑动listview并且设置监听器。
		dragsort_list.setDropListener(onDrop);
		dragsort_list.setDragEnabled(true); // 设置是否可拖动。
		dragsort_list.setAdapter(adapter);

		home_head_Rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		home_head_Rebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(ShowSettingAty.this);
			}
		});
	}

	int sum = 0;

	private void initData() {
		list = new ArrayList<custom>();

		if (AppContext.getShortcutFunction() == null) {
			for (int i = 0; i < 7; i++) {
				custom custom = new custom();
				if (i == 0) {
					custom.setTag("isPlanDownLoad");
				}
				if (i == 1 && isTwoBil) {
					custom.setTag("isTwoBill");
				}
				if (i == 2) {
					custom.setTag("isCamera");
				}
				if (i == 3 && AppContext.getTemperatureUseYN()) {
					custom.setTag("isCewen");
				}
				if (i == 4 && AppContext.getVibrationUseYN()) {
					custom.setTag("isCezhen");
				}
				if (i == 5 && AppContext.getSpeedUseYN()) {
					custom.setTag("isCZS");
				}
				if (i == 6 && AppContext.getOverhaulYN()) {
					custom.setTag("isOverhaul");
				}
				if (custom.getTag() != null) {
					custom.setQuickState(false);
					custom.setQuickorder(sum);
					sum++;
					list.add(custom);
				}
			}
			appcontext.setConfigShortcutFunction(getString(list));

		} else {
			list = getList(AppContext.getShortcutFunction());
			// 判断list中是否有二票
			int TwoBilSum = isExistItem(list, "isTwoBill");
			if (isTwoBil) {// 支持两票功能
				if (TwoBilSum < 0) {
					custom custom = new custom();
					custom.setQuickState(false);
					custom.setQuickorder(list.size());
					custom.setTag("isTwoBill");
					list.add(custom);
				}
			} else {// 不支持两票功能
				if (TwoBilSum >= 0) {
					list.remove(TwoBilSum);
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setQuickorder(i);
					}
				}
			}
			int CewenSum = isExistItem(list, "isCewen");
			if (AppContext.getTemperatureUseYN()) {// 支持测温
				if (CewenSum < 0) {
					custom custom = new custom();
					custom.setQuickState(false);
					custom.setQuickorder(list.size());
					custom.setTag("isCewen");
					list.add(custom);
				}
			} else {// 不支持测温
				if (CewenSum >= 0) {
					list.remove(CewenSum);
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setQuickorder(i);
					}
				}
			}
			int CezhenSum = isExistItem(list, "isCezhen");
			if (AppContext.getVibrationUseYN()) {// 支持测振
				if (CezhenSum < 0) {
					custom custom = new custom();
					custom.setQuickState(false);
					custom.setQuickorder(list.size());
					custom.setTag("isCezhen");
					list.add(custom);
				}
			} else {// 不支持测振
				if (CezhenSum >= 0) {
					list.remove(CezhenSum);
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setQuickorder(i);
					}
				}
			}
			int CZSSum = isExistItem(list, "isCZS");
			if (AppContext.getSpeedUseYN()) {// 支持测转速
				if (CZSSum < 0) {
					custom custom = new custom();
					custom.setQuickState(false);
					custom.setQuickorder(list.size());
					custom.setTag("isCZS");
					list.add(custom);
				}
			} else {// 不支持测转速
				if (CZSSum >= 0) {
					list.remove(CZSSum);
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setQuickorder(i);
					}
				}
			}
			int Overhaul = isExistItem(list, "isOverhaul");
			if (AppContext.getOverhaulYN()) {// 支持检修管理
				if (Overhaul < 0) {
					custom custom = new custom();
					custom.setQuickState(false);
					custom.setQuickorder(list.size());
					custom.setTag("isOverhaul");
					list.add(custom);
				}
			} else {// 不支持检修管理
				if (Overhaul >= 0) {
					list.remove(Overhaul);
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setQuickorder(i);
					}
				}
			}

			if (isExistItem(list, "isPlanDownLoad") < 0) {
				custom custom = new custom();
				custom.setQuickState(false);
				custom.setQuickorder(list.size());
				custom.setTag("isPlanDownLoad");
				list.add(custom);
			}

			if (isExistItem(list, "isCamera") < 0) {
				custom custom = new custom();
				custom.setQuickState(false);
				custom.setQuickorder(list.size());
				custom.setTag("isCamera");
				list.add(custom);
			}
			appcontext.setConfigShortcutFunction(getString(list));
		}
	}

	private int isExistItem(List<custom> list, String tag) {
		int sum = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTag().equals(tag)) {
				return i;
			}
		}
		return sum;
	}

	// List转String
	public static String getString(List<custom> custom) {
		try {
			Gson gson = new GsonBuilder().serializeNulls()
					.disableHtmlEscaping().create();
			String json = gson.toJson(custom);
			return json;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}

	}

	// string转list
	public static List<custom> getList(String value) {
		List<custom> c = new ArrayList<custom>();
		try {
			Gson gson = new GsonBuilder().serializeNulls()
					.disableHtmlEscaping().create();
			Type listType = new TypeToken<LinkedList<custom>>() {
			}.getType();
			c = gson.fromJson(value, listType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (c == null) {
			return new ArrayList<custom>();
		}
		return c;

	}

	// 监听器在手机拖动停下的时候触发
	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {// from to 分别表示 被拖动控件原位置 和目标位置
			if (from != to) {
				custom item = (custom) adapter.getItem(from);
				adapter.remove(from);// 在适配器中”原位置“的数据。
				adapter.insert(item, to, from);// 在目标位置中插入被拖动的控件。
			}
		}
	};

}
