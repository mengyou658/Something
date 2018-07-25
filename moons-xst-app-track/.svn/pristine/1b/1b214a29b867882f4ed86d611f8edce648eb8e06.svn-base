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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.adapter.SimpleRadioListViewAdapter;
import com.moons.xst.track.ui.WWSRDialog;
import com.moons.xst.track.xstinterface.SimpleRadioListViewDialogListener;

/**
 * 单选弹出框
 * 
 * @author LKZ
 * 
 */
public class SimpleRadioListViewDialog extends MoonsBaseDialog {

	private Context mcontext;
	private SimpleRadioListViewDialogListener mlistener;
	private TextView lktitleTextView;
	private ListView lkListView;
	private Button OkButton;
	private Button cancelButton;
	private SimpleRadioListViewAdapter mAdapter;

	private List<List<String>> mData;

	public SimpleRadioListViewDialog(Context context) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.simple_radio_listview);
		mcontext = context;
	}

	public SimpleRadioListViewDialog(Context context,
			SimpleRadioListViewDialogListener listener,
			List<List<String>> data, String selfTitleContent) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.simple_radio_listview);
		mcontext = context;
		mlistener = listener;
		mData = data;
		initView();
		bindingData(selfTitleContent);
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
		lktitleTextView = (TextView) findViewById(R.id.simple_radio_listview_title);
		OkButton = (Button) findViewById(R.id.btn_simple_radio_dialog_ok);
		cancelButton = (Button) findViewById(R.id.btn_simple_radio_dialog_cancel);
		OkButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				mlistener
						.btnOK((DialogInterface) SimpleRadioListViewDialog.this);
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mlistener.btnCancel((DialogInterface) SimpleRadioListViewDialog.this);
			}
		});
		lkListView = (ListView) findViewById(R.id.simple_radio_list);
		lkListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根

			}
		});
		lkListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				return false;
			}
		});
	}

	/**
	 * 绑定到列表
	 */
	private void bindingData(String selfTitleContent) {
		mAdapter = new SimpleRadioListViewAdapter(mcontext, mData,
				R.layout.listitem_radio_dialog);
		lkListView.setAdapter(mAdapter);
		lktitleTextView.setText(selfTitleContent);
	}
}
