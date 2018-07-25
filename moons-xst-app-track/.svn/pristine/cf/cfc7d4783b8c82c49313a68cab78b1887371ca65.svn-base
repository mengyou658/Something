package com.moons.xst.track.ui;

import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.SRLastResult_ListViewAdapter;
import com.moons.xst.track.adapter.WWSR_ListViewAdapter;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.Z_MObjectState;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.MathHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.widget.MoonsBaseDialog;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;

public class SRLastResultDialog extends MoonsBaseDialog {
	private Context mcontext;
	private TextView srlastresulttitle;
	private ListView srlastresultlv;
	private Button OkButton, cancelButton;
	
	private SRLastResult_ListViewAdapter mAdapter;
	private SimpleMultiListViewDialogListener mlistener;
	private List<List<String>> mData;
	
	public SRLastResultDialog(Context context) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.sr_lastresult);
		mcontext = context;
	}

	public SRLastResultDialog(Context context,
			SimpleMultiListViewDialogListener listener,
			List<List<String>> data,
			String selfTitleContent) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.sr_lastresult);

		mcontext = context;
		mlistener = listener;
		mData = data;
		initView();
		bindingData(selfTitleContent);
	}
	
	private void initView() {
		srlastresulttitle = (TextView) findViewById(R.id.sr_lastresult_title);
		srlastresultlv = (ListView) findViewById(R.id.sr_lastresult_list);
		OkButton = (Button) findViewById(R.id.btn_sr_lastresult_dialog_ok);
		OkButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (mData != null && mData.size() > 0) {
					mlistener.btnOK(SRLastResultDialog.this, mData);
				}
			}
		});
		cancelButton = (Button) findViewById(R.id.btn_sr_lastresult_dialog_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				SRLastResultDialog.this.dismiss();
			}
		});
	}
	
	/**
	 * 绑定到列表
	 */
	private void bindingData(String selfTitleContent) {
		mAdapter = new SRLastResult_ListViewAdapter(mcontext, mData,
				R.layout.listitem_multi_dialog);
		srlastresultlv.setAdapter(mAdapter);
		srlastresultlv
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {
						// TODO 自动生成的方法存根
						if (AppContext.SRStateBuffer != null
								&& AppContext.SRStateBuffer.size() > 0) {
							final String[] items = new String[AppContext.SRStateBuffer
									.size()];
							int key = 0;
							for (Z_MObjectState b : AppContext.SRStateBuffer) {
								items[key] = b.getName_TX();
								key++;
							}
							new AlertDialog.Builder(mcontext)
									.setTitle(mcontext.getString(R.string.plan_sr, ((List<String>)mAdapter.getItem(arg2)).get(1).toString()))
									.setIcon(android.R.drawable.ic_dialog_info)
									.setCancelable(true)
									.setItems(items, new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											// TODO 设置启停点时的操作
											String lastSRResult_TM = DateTimeHelper
													.getDateTimeNow();
											int tmpInt = (int) Math.pow(2, which);
											String srString = MathHelper
													.SRTransToString(tmpInt);
											mData.get(arg2).set(3, srString);
											mData.get(arg2).set(4, lastSRResult_TM);
											// 这里只刷新列表，无需重新绑定
											mAdapter.notifyDataSetChanged();
											dialog.dismiss();
										}
									}).show();
						}
					}
				});
		srlastresulttitle.setText(selfTitleContent);
	}
}