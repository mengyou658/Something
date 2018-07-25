/**
 * 
 */
package com.moons.xst.track.widget;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.adapter.SimpleMultiListViewAdapter;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;

/**
 * 复选弹出框
 * 
 * @author LKZ
 * 
 */
public class SimpleMultiListViewDialog extends MoonsBaseDialog {

	private Context mcontext;
	private SimpleMultiListViewDialogListener mlistener;
	private TextView lktitleTextView;
	private ListView lkListView;
	private Button OkButton, cancelButton;
	private SimpleMultiListViewAdapter mAdapter;
	private CheckBox checkedAll;

	private List<List<String>> mData;

	private boolean judgeExpYN = false;

	public SimpleMultiListViewDialog(Context context) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.simple_multi_listview);
		mcontext = context;
	}

	public SimpleMultiListViewDialog(Context context,
			SimpleMultiListViewDialogListener listener,
			List<List<String>> data, Boolean mjudgeExpYN,
			String selfTitleContent) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.simple_multi_listview);
		mcontext = context;
		mlistener = listener;
		mData = data;
		judgeExpYN = mjudgeExpYN;
		initView();
		bindingData(selfTitleContent);
	}

	/**
	 * 是否判断正常异常不能同时选中
	 * 
	 * @param yn
	 */
	public void setjudgeExpYN(boolean yn) {
		judgeExpYN = yn;
	}

	/**
	 * 设置取消按钮的可见性
	 * 
	 * @param mVisibility
	 */
	public void setCancelButton(Boolean mVisibility) {
		if (mVisibility) {
			cancelButton.setVisibility(View.VISIBLE);
		} else {
			cancelButton.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.dismiss();
			mlistener.goBackToParentUI();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {
		lktitleTextView = (TextView) findViewById(R.id.simple_multi_listview_title);
		lkListView = (ListView) findViewById(R.id.simple_multi_list);
		OkButton = (Button) findViewById(R.id.btn_simple_multi_dialog_ok);
		OkButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				boolean hasChecked = false;
				if (mData != null && mData.size() > 0) {
					for (List<String> _itemList : mData) {
						if (_itemList.get(2).equals("1")) {
							hasChecked = true;
							break;
						}
					}
				}
				if (hasChecked) {
					mlistener.btnOK(
							(DialogInterface) SimpleMultiListViewDialog.this,
							mData);
				} else {
					UIHelper.ToastMessage(mcontext,
							R.string.plan_resitem_notice1);
				}
			}
		});
		cancelButton = (Button) findViewById(R.id.btn_simple_multi_dialog_cancel);
		cancelButton.setVisibility(View.GONE);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				SimpleMultiListViewDialog.this.dismiss();
			}
		});
		lkListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO 自动生成的方法存根
						CheckBox viewCheckBox = (CheckBox) arg1
								.findViewById(R.id.listitem_multi_itemcheckbox);
						if (viewCheckBox.isChecked())
							viewCheckBox.setChecked(false);
						else {
							viewCheckBox.setChecked(true);
						}
					}
				});
		lkListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				return false;
			}
		});
		checkedAll = (CheckBox) findViewById(R.id.simple_multi_listview_checkall);
		checkedAll
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO 自动生成的方法存根

					}
				});
		checkedAll.setVisibility(View.GONE);
	}

	/**
	 * 绑定到列表
	 */
	private void bindingData(String selfTitleContent) {
		mAdapter = new SimpleMultiListViewAdapter(mcontext, mData, judgeExpYN,
				R.layout.listitem_multi_dialog);
		lkListView.setAdapter(mAdapter);
		lktitleTextView.setText(selfTitleContent);
	}
}
