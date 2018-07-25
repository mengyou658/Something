/**
 * 
 */
package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.buss.DJResultHelper;
import com.moons.xst.buss.XSTMethodByLineTypeHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.SR_ListViewAdapter;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.MObject_State;
import com.moons.xst.track.bean.Z_MObjectState;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.MathHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.MoonsBaseDialog;
import com.moons.xst.track.xstinterface.PriorityListener;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;

/**
 * 启停点Dialog
 * 
 * @author lkz
 * 
 */
public class DianJianSRDialog extends MoonsBaseDialog {

	private final static String Tag = "DianJianSRDialog";
	private Context mcontext;
	private PriorityListener mlistener;
	/**
	 * 当前钮扣下启停点信息
	 */
	private List<DJ_ControlPoint> idPosSRBuffer = new ArrayList<DJ_ControlPoint>();
	private DJ_ControlPoint currPlanSR = new DJ_ControlPoint();
	private TextView srtitleTextView, srCurrItemTextView;
	private ListView srListView;
	private SR_ListViewAdapter srAdapter;
	private Button okbButton, currSetButton, lastresultButton;
	private LoadingDialog saving;

	private String dj_idposId;
	private DJPlan dj_Plan;
	private boolean mAutoYN = true;
	private static LoadingDialog loading;
	private Handler mHandler, mSRLastResHandler;
	private int srno;
	
	private static boolean flag = false;

	static DianJianSRDialog _intance = null;

	public DianJianSRDialog(Context context) {
		super(context);
		this.setCancelable(false);
		this.setTitle(R.string.dianjian_sr_title);
		setContentView(R.layout.dianjian_sr);
		mcontext = context;
	}

	public DianJianSRDialog(Context context, PriorityListener listener,
			String idpos_id, DJPlan mdjPlan, boolean autoYN,int no) {
		super(context);
		this.setCancelable(false);
		this.setTitle(R.string.dianjian_sr_title);
		setContentView(R.layout.dianjian_sr);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		mcontext = context;
		mlistener = listener;
		dj_idposId = idpos_id;
		dj_Plan = mdjPlan;
		mAutoYN = autoYN;
		srno = no;
		initView();
		loadSRToListView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((dj_Plan != null) && (!dj_Plan.SrIsSetting())) {
				UIHelper.ToastMessage(mcontext, R.string.plan_sr_notice2);
				return false;
			} else {
				this.dismiss();
				mlistener.comeBackToDianjianMain(mAutoYN);
				DianJianSRDialog.this.dismiss();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {
		srtitleTextView = (TextView) findViewById(R.id.dj_sr_title);
		srListView = (ListView) findViewById(R.id.dj_sr_list);
		srListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				/*final DJ_ControlPoint _selectedControlPoint = (DJ_ControlPoint) (((TextView) arg1
						.findViewById(R.id.sr_listitem_title)).getTag());*/
				if (!flag) {
					flag = true;
					final DJ_ControlPoint _selectedControlPoint = (DJ_ControlPoint)srAdapter.getItem(arg2);
					
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
								.setTitle(mcontext.getString(R.string.plan_sr, _selectedControlPoint.getName_TX()))
								.setIcon(android.R.drawable.ic_dialog_info)
								.setCancelable(false)
								.setNegativeButton(mcontext.getString(R.string.cancel), new android.content.DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										flag = false;
									}
								})
								.setItems(items, new OnClickListener() {
	
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										String lastCmdtStr = _selectedControlPoint
												.getLastSRResult_TM();
										if (!StringUtils.isEmpty(lastCmdtStr)) {
											// TODO 修改启停点信息时，处理结果数据
	
										}
										// TODO 设置启停点时的操作
										_selectedControlPoint
												.setLastSRResult_TM(DateTimeHelper
														.getDateTimeNow());
										int tmpInt = (int) Math.pow(2, which);
										String srString = MathHelper
												.SRTransToString(tmpInt);
										_selectedControlPoint
												.setLastSRResult_TX(srString);	
										
										DJPlanHelper.GetIntance().updateSR(
												mcontext, _selectedControlPoint);
										genMojectStateResult(_selectedControlPoint);
										
										// 这里只刷新列表，无需重新绑定
										srAdapter.notifyDataSetChanged();
										//bindingSRData();
										dialog.dismiss();
//										mlistener.refreshPriorityUI(mAutoYN);
										
										flag = false;
									}
								}).show();
					} else {
						UIHelper.ToastMessage(mcontext, R.string.dj_sr_statenotset);
						flag = false;
					}
				}
			}
		});
		srListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				return false;
			}
		});
		okbButton = (Button) findViewById(R.id.btn_sr_ok);
		okbButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if ((dj_Plan != null) && (!dj_Plan.SrIsSetting())) {
					UIHelper.ToastMessage(mcontext, R.string.plan_sr_notice2);
				} else {
					//mlistener.refreshPriorityUI(mAutoYN);
					mlistener.comeBackToDianjianMain(mAutoYN);
					DianJianSRDialog.this.dismiss();					
				}
			}
		});
		lastresultButton = (Button) findViewById(R.id.btn_sr_lastresult);
		lastresultButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				List<List<String>> data = new ArrayList<List<String>>();
				if (idPosSRBuffer != null && idPosSRBuffer.size() > 0) {
					for (DJ_ControlPoint point : idPosSRBuffer) {
						int mobStateIndex = MathHelper.SRTransToInt(point.getLastSRResult_TX());
						if (mobStateIndex > -1) {
							List<String> list = new ArrayList<String>();
							list.add(String.valueOf(AppContext.SRBuffer.indexOf(point)));
							list.add(point.getName_TX());
							list.add("0");
							list.add(point.getLastSRResult_TX());
							list.add(point.getLastSRResult_TM());
							data.add(list);
						}
					}
					if (data.size() <= 0) {
						UIHelper.ToastMessage(mcontext, R.string.plan_sr_notice3);
						return;
					}
					
					String selfTitleContent = mcontext.getString(R.string.dj_sr_list);
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
										mSRLastResHandler = new Handler() {
											public void handleMessage(Message msg) {
												if (saving != null)
													saving.dismiss();
												if (msg.what == 1) {
													srAdapter.notifyDataSetChanged();
												} else {
													UIHelper.ToastMessage(mcontext, 
															((Exception)(msg.obj)).getMessage());
												}
												dialog.dismiss();
											}
										};
										setSRLastResultThread(mData);									
									} else {
										srAdapter.notifyDataSetChanged();
										dialog.dismiss();
									}		
									dialog.dismiss();
									//mlistener.refreshPriorityUI(mAutoYN);
								}
							}, data, selfTitleContent);
					
					_dialog.setCancelable(true);
					_dialog.setTitle(R.string.idpos_srlastresult_dialog_title);
					_dialog.show();
				}
			}
		});
		srCurrItemTextView = (TextView) findViewById(R.id.dj_sr_curr_item);
		srCurrItemTextView.setText("");
		//currSetButton = (Button) findViewById(R.id.btn_sr_curr_set);
//		currSetButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO 自动生成的方法存根
//				final DJ_ControlPoint _selectedControlPoint = currPlanSR;
//				if (AppContext.SRStateBuffer != null
//						&& AppContext.SRStateBuffer.size() > 0) {
//					final String[] items = new String[AppContext.SRStateBuffer
//							.size()];
//					int key = 0;
//					for (Z_MObjectState b : AppContext.SRStateBuffer) {
//						items[key] = b.getName_TX();
//						key++;
//					}
//					new AlertDialog.Builder(mcontext)
//							.setTitle(
//									"启停点-" + _selectedControlPoint.getName_TX())
//							.setIcon(android.R.drawable.ic_dialog_info)
//							.setCancelable(true)
//							.setItems(items, new OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									String lastCmdtStr = _selectedControlPoint
//											.getLastSRResult_TM();
//									if (!StringUtils.isEmpty(lastCmdtStr)) {
//										// TODO 修改启停点信息时，处理结果数据
//
//									}
//									// TODO 设置启停点时的操作
//									_selectedControlPoint
//											.setLastSRResult_TM(DateTimeHelper
//													.getDateTimeNow());
//									int tmpInt = (int) Math.pow(2, which);
//									String srString = MathHelper
//											.SRTransToString(tmpInt);
//									_selectedControlPoint
//											.setLastSRResult_TX(srString);
//									DJPlanHelper.GetIntance().updateSR(
//											mcontext, _selectedControlPoint);
//									genMojectStateResult(_selectedControlPoint);
//									bindingSRData();
//									dialog.dismiss();
//									mlistener.refreshPriorityUI(mAutoYN);
//								}
//							}).show();
//				}
//			}
//		});

	}

	private void loadSRToListView() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					bindingSRData();
				}
			}
		};
		this.loadSRDataThread(false);
	}

	/**
	 * 加载ID位置状态数据的线程
	 */
	private void loadSRDataThread(final boolean isRefresh) {
		if (loading != null && loading.isShowing())
			loading.dismiss();
		loading = new LoadingDialog(mcontext);
		loading.setCancelable(false);
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 重新加载ID位置数据
					loadSRData();

					msg.what = 1;
					msg.obj = AppContext.DJIDPosBuffer;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	private void loadSRData() {
		idPosSRBuffer = DJPlanHelper.GetIntance().getSRbyidPosID(mcontext,
				dj_idposId);
		if (AppContext.SRBuffer != null && AppContext.SRBuffer.size() > 0) {
			Iterator<DJ_ControlPoint> iter = idPosSRBuffer.iterator();
			while (iter.hasNext()) {
				DJ_ControlPoint _temControlPoint = iter.next();
				if (_temControlPoint.getControlPoint_ID().equals(
						dj_Plan.getDJPlan().getSRPoint_ID())) {
					_temControlPoint.setisCurrSR(true);
					currPlanSR = _temControlPoint;
				} else {
					_temControlPoint.setisCurrSR(false);
				}
			}
		}
	}

	/**
	 * 绑定到列表
	 */
	private void bindingSRData() {
		srAdapter = new SR_ListViewAdapter(mcontext, idPosSRBuffer,
				R.layout.listitem_sr, dj_Plan.getDJPlan());
		srListView.setAdapter(srAdapter);
		srListView.setSelection(srno);
		String countInfoString = mcontext.getString(R.string.dj_sr_count, String.valueOf(idPosSRBuffer.size()));
		srtitleTextView.setText(countInfoString);
		if (currPlanSR != null) {
			srCurrItemTextView.setText(mcontext.getString(R.string.dj_sr_current, currPlanSR.getName_TX()));
			srCurrItemTextView.setTag(currPlanSR);
		}
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
	
	private void setSRLastResultThread(final List<List<String>> data) {
		saving = new LoadingDialog(mcontext);
		saving.setLoadText(mcontext.getString(R.string.save_ing));
		saving.setCancelable(false);
		saving.show();
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 批量保存主控数据
					setSRLastResult(data);
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mSRLastResHandler.sendMessage(msg);
			}
		}.start();
	}
	
	private void setSRLastResult(List<List<String>> data) {
		if (idPosSRBuffer != null && idPosSRBuffer.size() > 0) {
			for (DJ_ControlPoint _item : idPosSRBuffer) {
				int mobStateIndex = MathHelper.SRTransToInt(_item.getLastSRResult_TX());
				if (mobStateIndex > -1) {
					for (List<String> _temp : data) {
						if (String.valueOf(AppContext.SRBuffer.indexOf(_item)).equals(_temp.get(0))) {
							DJ_ControlPoint _temControlPoint = AppContext.SRBuffer.get(Integer
									.valueOf(AppContext.SRBuffer.indexOf(_item)));
							
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
							_item.setLastSRResult_TX(_temp.get(3).toString());
							_item.setLastSRResult_TM(lastSRResult_TM);
							genMojectStateResult(_temControlPoint);
						}
					}
				}
			}
		}
	}
}
