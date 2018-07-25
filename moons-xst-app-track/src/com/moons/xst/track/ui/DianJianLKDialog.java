/**
 * 
 */
package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.LK_ListViewAdapter;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.MoonsBaseDialog;
import com.moons.xst.track.xstinterface.LKListener;

/**
 * @author LKZ
 * 
 */
public class DianJianLKDialog extends MoonsBaseDialog {

	private Context mcontext;
	private LKListener mlistener;
	private OnClickListener mOnClickListener;
	private TextView lktitleTextView;
	private ListView lkListView;
	private Button LKOkButton;
	private Button LKcancelButton;
	private LK_ListViewAdapter lkAdapter;

	private List<DJPlan> dj_PlanS;
	private List<DJPlan> selectedDJPlans;
	private static LoadingDialog loading;
	private Handler mHandler;

	public DianJianLKDialog(Context context) {
		super(context);
		this.setCancelable(false);
		this.setTitle(R.string.dianjian_lk_title);
		setContentView(R.layout.dianjian_lk);
		mcontext = context;
	}

	public DianJianLKDialog(Context context, LKListener listener,
			List<DJPlan> djPlans) {
		super(context);
		this.setCancelable(false);
		this.setTitle(R.string.dianjian_lk_title);
		setContentView(R.layout.dianjian_lk);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		mcontext = context;
		mlistener = listener;
		dj_PlanS = djPlans;
		initView();
		bindingLKData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {
		lktitleTextView = (TextView) findViewById(R.id.dj_lk_title);
		lkListView = (ListView) findViewById(R.id.dj_lk_list);
		LKOkButton = (Button) findViewById(R.id.btn_lk_ok);
		LKcancelButton = (Button) findViewById(R.id.btn_lk_cancel);
		LKOkButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (selectedDJPlans != null && selectedDJPlans.size() > 0)
					mlistener.btnOK((DialogInterface) DianJianLKDialog.this,
							selectedDJPlans);
				else {
					UIHelper.ToastMessage(mcontext,
							R.string.plan_resitem_notice1);
				}
			}
		});
		LKcancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
		lkListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				CheckBox viewCheckBox = (CheckBox) arg1
						.findViewById(R.id.lk_listitem_checkbox);
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
	}

	/**
	 * 绑定到列表
	 */
	private void bindingLKData() {
		selectedDJPlans = new ArrayList<DJPlan>();
		lkAdapter = new LK_ListViewAdapter(mcontext, dj_PlanS, selectedDJPlans,
				R.layout.listitem_lk);
		lkListView.setAdapter(lkAdapter);
		String countInfoString = mcontext.getString(R.string.plan_lkforplan_count, String.valueOf(dj_PlanS.size()));
		lktitleTextView.setText(countInfoString);
	}

	private void goBack() {
		LayoutInflater factory = LayoutInflater.from(mcontext);
		final View view = factory.inflate(R.layout.textview_layout,
				null);
		new com.moons.xst.track.widget.AlertDialog(mcontext)
				.builder()
				.setTitle(mcontext.getString(R.string.system_notice))
				.setView(view)
				.setMsg(mcontext.getString(R.string.plan_lk_cancel_notice1))
				.setPositiveButton(mcontext.getString(R.string.sure),
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								mlistener.goBackToParentUI();
								DianJianLKDialog.this.dismiss();
							}
						})
				.setNegativeButton(mcontext.getString(R.string.cancel),
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
		/*final SimpleTextDialog _dialog = new SimpleTextDialog(mcontext, mcontext.getString(R.string.system_notice),
				mcontext.getString(R.string.plan_lk_cancel_notice1));
		_dialog.setTextSize(DianJianLKDialog.this.getContext().getResources().getDimension(R.dimen.text_size_16));
		_dialog.setOKButton(R.string.simple_text_dialog_btnok,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						_dialog.dismiss();
						mlistener.goBackToParentUI();
						DianJianLKDialog.this.dismiss();
					}
				});
		_dialog.setCancelButton(R.string.simple_text_dialog_btncancel,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						_dialog.dismiss();
					}
				});
		_dialog.show();*/
	}
}
