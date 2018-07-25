/**
 * 
 */
package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.buss.DJResultHelper;
import com.moons.xst.buss.XSTMethodByLineTypeHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.WWSR_ListViewAdapter;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.MObject_State;
import com.moons.xst.track.bean.Z_MObjectState;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.MathHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.MoonsBaseDialog;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;

/**
 * 外围启停设置
 * 
 * @author lkz
 * 
 */
public class WWSRDialog extends MoonsBaseDialog {

	private final static String Tag = "WWSRDialog";
	private Context mcontext;
	private SimpleMultiListViewDialogListener mlistener;
	private TextView lktitleTextView;
	private ListView lkListView;
	private Button OkButton, cancelButton, lastresultButton;
	private WWSR_ListViewAdapter mAdapter;

	private List<List<String>> mData;
	private String mSelfTitleContent = "";
	private boolean judgeExpYN = false;
	private CheckBox checkedAll;
	
	private LoadingDialog saving;
	private Handler mHandler;

	public WWSRDialog(Context context) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.wwsr_multi_listview);
		mcontext = context;
	}

	public WWSRDialog(Context context,
			SimpleMultiListViewDialogListener listener,
			List<List<String>> data, Boolean mjudgeExpYN,
			String selfTitleContent) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.wwsr_multi_listview);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		mcontext = context;
		mlistener = listener;
		mData = data;
		judgeExpYN = mjudgeExpYN;
		mSelfTitleContent = selfTitleContent;
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
		lktitleTextView = (TextView) findViewById(R.id.wwsr_multi_listview_title);
		lkListView = (ListView) findViewById(R.id.wwsr_multi_list);
		OkButton = (Button) findViewById(R.id.btn_wwsr_multi_dialog_ok);
		OkButton.setText(R.string.idpos_srset_dialog_okbtn);
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
					if (hasChecked && AppContext.SRStateBuffer != null
							&& AppContext.SRStateBuffer.size() > 0) {
						final String[] items = new String[AppContext.SRStateBuffer
								.size()];
						int key = 0;
						for (Z_MObjectState b : AppContext.SRStateBuffer) {
							items[key] = b.getName_TX();
							key++;
						}
						new AlertDialog.Builder(mcontext).setTitle(R.string.dj_srstate_list)
								.setIcon(android.R.drawable.ic_dialog_info)
								.setCancelable(true)
								.setItems(items, new OnClickListener() {
									@Override
									public void onClick(final DialogInterface dialog,
											int which) {
										if (mData != null && mData.size() > 0) {											
											mHandler = new Handler() {
												public void handleMessage(Message msg) {
													if (saving != null)
														saving.dismiss();
													if (msg.what == 1) {
														mAdapter.notifyDataSetChanged();
														checkedAll.setChecked(false);
													} else {
														UIHelper.ToastMessage(mcontext, 
																((Exception)(msg.obj)).getMessage());
													}
													dialog.dismiss();
												}
											};
											setWWSRThread(which);									
										} else {
											mAdapter.notifyDataSetChanged();
											dialog.dismiss();
										}										
									}
								}).show();
					}
				}
				if (!hasChecked) {
					UIHelper.ToastMessage(mcontext,
							R.string.plan_resitem_notice1);
				}
			}
		});
		lastresultButton = (Button) findViewById(R.id.btn_wwsr_multi_dialog_lastresult);
		lastresultButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				boolean hasChecked = false;
				boolean hasLastResult = true;
				if (mData != null && mData.size() > 0) {
					for (List<String> _itemList : mData) {
						if (_itemList.get(2).equals("1")) {
							hasChecked = true;
							break;
						}
					}
					for (List<String> _itemList : mData) {
						if (_itemList.get(2).equals("1")) {
							int mobStateIndex = MathHelper.SRTransToInt(_itemList.get(3));
							if (mobStateIndex <= -1) {
								hasLastResult = false;
								break;
							}
						}
					}
					if (hasChecked && hasLastResult 
							&& AppContext.SRStateBuffer != null
							&& AppContext.SRStateBuffer.size() > 0) {
						String selfTitleContent = mcontext.getString(R.string.dj_sr_list);
						List<List<String>> data = new ArrayList<List<String>>();
						for (List<String> _itemList : mData) {
							if (_itemList.get(2).equals("1")) {
								List<String> _temList = new ArrayList<String>();
								_temList.add(_itemList.get(0));
								_temList.add(_itemList.get(1));
								_temList.add(_itemList.get(2));
								_temList.add(_itemList.get(3));
								_temList.add(_itemList.get(4));
								data.add(_temList);
							}
						}
						
						SRLastResultDialog _dialog = new SRLastResultDialog(mcontext,
								new SimpleMultiListViewDialogListener() {
	
									@Override
									public void refreshParentUI() {
										// TODO 自动生成的方法存根
	
									}
	
									@Override
									public void goBackToParentUI() {
										// TODO 自动生成的方法存根
	
									}
	
									@Override
									public void btnOK(final DialogInterface dialog,
											List<List<String>> mData) {
										if (mData != null && mData.size() > 0) {											
											mHandler = new Handler() {
												public void handleMessage(Message msg) {
													if (saving != null)
														saving.dismiss();
													if (msg.what == 1) {
														mAdapter.notifyDataSetChanged();
														checkedAll.setChecked(false);
													} else {
														UIHelper.ToastMessage(mcontext, 
																((Exception)(msg.obj)).getMessage());
													}
													dialog.dismiss();
												}
											};
											setWWSRLastResultThread(mData);									
										} else {
											mAdapter.notifyDataSetChanged();
											dialog.dismiss();
										}		
										dialog.dismiss();
									}
								}, data, selfTitleContent);
						
						_dialog.setCancelable(true);
						_dialog.setTitle(R.string.idpos_srlastresult_dialog_title);
						_dialog.show();
					}
				}
				if (!hasChecked) {
					UIHelper.ToastMessage(mcontext,
							R.string.plan_resitem_notice1);
				} else {
					if (!hasLastResult) {
						UIHelper.ToastMessage(mcontext,
								R.string.dj_sr_nolastresult_notice);
					}
				}
				
			}
		});
		cancelButton = (Button) findViewById(R.id.btn_wwsr_multi_dialog_cancel);
		cancelButton.setText(R.string.idpos_srset_dialog_cancel);
		cancelButton.setVisibility(View.GONE);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				WWSRDialog.this.dismiss();
				mlistener.goBackToParentUI();
			}
		});
		/*lkListView.setOnItemClickListener(new OnItemClickListener() {
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
		});*/
		checkedAll = (CheckBox) findViewById(R.id.wwsr_multi_listview_checkall);
		checkedAll
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO 自动生成的方法存根
						if (isChecked) {
							for (List<String> _list : mData) {
								_list.set(2, "1");
							}
						} else {
							for (List<String> _list : mData) {
								_list.set(2, "0");
							}
						}
						mAdapter.notifyDataSetChanged();
						//bindingData(mSelfTitleContent);
					}
				});
	}
	
	private void setWWSRThread(final int which) {
		saving = new LoadingDialog(mcontext);
		saving.setLoadText(mcontext.getString(R.string.save_ing));
		saving.setCancelable(false);
		saving.show();
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 批量保存主控数据
					setWWSR(which);
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	
	private void setWWSR(int which) {
		for (List<String> _item : mData) {
			if (_item.get(2).equals("1")) {
				DJ_ControlPoint _temControlPoint = AppContext.SRBuffer.get(Integer
						.valueOf(_item
								.get(0)));
				
				String lastCmdtStr = _temControlPoint
						.getLastSRResult_TM();
				if (!StringUtils
						.isEmpty(lastCmdtStr)) {
					// TODO
					// 修改启停点信息时，处理结果数据

				}
				String lastSRResult_TM = DateTimeHelper
						.getDateTimeNow();
				// TODO 设置启停点时的操作
				_temControlPoint
						.setLastSRResult_TM(lastSRResult_TM);
				int tmpInt = (int) Math
						.pow(2, which);
				String srString = MathHelper
						.SRTransToString(tmpInt);
				_temControlPoint
						.setLastSRResult_TX(srString);
				
				DJPlanHelper
						.GetIntance()
						.updateSR(mcontext,
								_temControlPoint);
				_item.set(3, srString);
				_item.set(4, lastSRResult_TM);
				genMojectStateResult(_temControlPoint);
			}
		}
	}
	
	private void setWWSRLastResultThread(final List<List<String>> data) {
		saving = new LoadingDialog(mcontext);
		saving.setLoadText(mcontext.getString(R.string.save_ing));
		saving.setCancelable(false);
		saving.show();
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 批量保存主控数据
					setWWSRLastResult(data);
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	
	private void setWWSRLastResult(List<List<String>> data) {
		for (List<String> _item : mData) {
			if (_item.get(2).equals("1")) {
				for (List<String> _temp : data) {
					if (_item.get(0).equals(_temp.get(0))) {
						DJ_ControlPoint _temControlPoint = AppContext.SRBuffer.get(Integer
								.valueOf(_item
										.get(0)));
						String lastCmdtStr = _temControlPoint
								.getLastSRResult_TM();
						if (!StringUtils
								.isEmpty(lastCmdtStr)) {
							// TODO
							// 修改启停点信息时，处理结果数据

						}
						String lastSRResult_TM = DateTimeHelper
								.getDateTimeNow();
						// TODO 设置启停点时的操作
						_temControlPoint
								.setLastSRResult_TM(lastSRResult_TM);
						_temControlPoint
								.setLastSRResult_TX(_temp.get(3).toString());
						
						DJPlanHelper
						.GetIntance()
						.updateSR(mcontext,
								_temControlPoint);
						_item.set(3, _temp.get(3).toString());
						_item.set(4, lastSRResult_TM);
						genMojectStateResult(_temControlPoint);
					}
				}
			}
		}
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

	/**
	 * 绑定到列表
	 */
	private void bindingData(String selfTitleContent) {
		mAdapter = new WWSR_ListViewAdapter(mcontext, mData, judgeExpYN,
				R.layout.listitem_multi_dialog);
		lkListView.setAdapter(mAdapter);
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
		lktitleTextView.setText(selfTitleContent);
	}

	/**
	 * 保存一条启停状态
	 * 
	 * @param cp
	 */
	private void genMojectStateResult(DJ_ControlPoint cp) {
		MObject_State _mObject_State = new MObject_State();
		_mObject_State.setAppUser_CD(AppContext.getUserCD());
		_mObject_State.setCompleteTime_DT(cp.getLastSRResult_TM());
		_mObject_State.setDataState_YN("");
		_mObject_State.setMObjectState_CD(cp.getLastSRResult_TX());
		_mObject_State.setMObjectStateName_TX(AppContext.SRStateBuffer.get(
				MathHelper.SRTransToInt(cp.getLastSRResult_TX())).getName_TX());
		_mObject_State.setStartAndEndPoint_ID(cp.getControlPoint_ID());
		if (!StringUtils.isEmpty(cp.getTransInfo_TX())) {
			
			String transInfo= XSTMethodByLineTypeHelper.getInstance()
					.setCPTransInfo(cp.getTransInfo_TX());
			_mObject_State.setTransInfo_TX(transInfo);
			
			/*if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
				_mObject_State.setTransInfo_TX(cp.getTransInfo_TX());
			} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
				if (!StringUtils.isEmpty(AppConst.PlanTimeIDStr)) {
					_mObject_State.setTransInfo_TX(cp.getTransInfo_TX()
							+ "|" + AppConst.PlanTimeIDStr);
				}
				else
					_mObject_State.setTransInfo_TX(cp.getTransInfo_TX());
			}*/
		}
		else {
			_mObject_State.setTransInfo_TX("");
		}
		DJResultHelper.GetIntance().InsertMObjectStateResult(mcontext,
				_mObject_State, cp.getName_TX());
	}

}
