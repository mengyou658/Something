package com.moons.xst.track.common.factory.myworkfactory;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.moons.xst.buss.CycleHelper;
import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.buss.XSTMethodByLineTypeHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.Z_SpecCase;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.ui.AboutXST;
import com.moons.xst.track.widget.SimpleMultiListViewDialog;
import com.moons.xst.track.widget.SimpleRadioListViewDialog;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;
import com.moons.xst.track.xstinterface.SimpleRadioListViewDialogListener;

import de.greenrobot.event.EventBus;

public class XDJWork implements MyWork {
	
	private Context mContext;

	public void enterMyWork(Context context, final DJLine djline) {
		mContext = context;
		
		AppContext.voiceConvertService.Speaking(R.string.dj_confirm_speaking
				+ djline.getLineName());
		
		// 加载工作日节假日
		if (CycleHelper.GetIntance().loadWorkDate()) {
			if (AppContext.workDateBuffer != null 
					&& AppContext.workDateBuffer.size() <= 0) {
				UIHelper.ToastMessage(context, R.string.workdate_nodata);
				return;
			}			
		} else {
			UIHelper.ToastMessage(context, R.string.workdate_error);
			return;
		}
		// 加载特巡条件
		DJPlanHelper.GetIntance().loadDJSpecCase(mContext,
				djline, AppContext.DJLineBuffer);
		
		AppContext.DJSpecCaseFlag = 0;
		if (djline.getSpecCaseYN()) {
			String selfTitleContent = djline.getLineName();
			final List<List<String>> data = new ArrayList<List<String>>();
			List<String> _temList = new ArrayList<String>();
			_temList.add("0");
			_temList.add(mContext.getString(R.string.line_xjmode_normal));
			_temList.add("1");
			data.add(_temList);
			_temList = new ArrayList<String>();
			_temList.add("1");
			_temList.add(mContext.getString(R.string.line_xjmode_spec));
			_temList.add("0");
			data.add(_temList);
			SimpleRadioListViewDialog _Dialog = new SimpleRadioListViewDialog(
					mContext, new SimpleRadioListViewDialogListener() {

						@Override
						public void refreshParentUI() {
							// TODO 自动生成的方法存根

						}

						@Override
						public void goBackToParentUI() {
							// TODO 自动生成的方法存根

						}

						@Override
						public void btnOK(DialogInterface dialog) {
							// TODO 自动生成的方法存根
							if (data != null && data.size() > 0) {
								List<String> checkedItem = new ArrayList<String>();
								for (List<String> _item : data) {
									if (_item.get(2).equals("1")) {
										checkedItem = _item;
										break;
									}
								}
								Integer index = Integer.parseInt(checkedItem
										.get(0));
								if (index == 1) {
									AppContext.DJSpecCaseFlag = 1;
									selectSpecCase(djline);
								} else {
									AppContext.DJSpecCaseFlag = 0;
									UIHelper.showDianjianTouchIDPos(
											mContext,
											djline.getLineID(),
											djline.getLineName(),
											false);
								}
							}
							dialog.dismiss();
						}

						@Override
						public void btnCancel(DialogInterface dialog) {
							// TODO Auto-generated method stub
							((AppContext)(mContext.getApplicationContext())).closeDB();
							XSTMethodByLineTypeHelper.getInstance().disposeHelper();
							dialog.dismiss();
						}
					}, data, selfTitleContent);
			_Dialog.setTitle(R.string.choice);
			_Dialog.show();
		} else {
			UIHelper.showDianjianTouchIDPos(mContext, djline.getLineID(),
					djline.getLineName(), false);
		}
	}
	
	private void selectSpecCase(final DJLine adjline) {
		ArrayList<Z_SpecCase> specCaselist = AppContext.DJSpecCaseBuffer
				.get(String.valueOf(adjline.getLineID()));
		final List<List<String>> data = new ArrayList<List<String>>();
		data.clear();
		for (Z_SpecCase _item : specCaselist) {
			List<String> _temList = new ArrayList<String>();
			_temList.add(_item.getSpecCase_ID());
			_temList.add(_item.getName_TX());
			_temList.add("0");
			data.add(_temList);
		}
		SimpleMultiListViewDialog dialog = new SimpleMultiListViewDialog(
				mContext, new SimpleMultiListViewDialogListener() {

					@Override
					public void refreshParentUI() {
						// TODO 自动生成的方法存根

					}

					@Override
					public void goBackToParentUI() {
						// TODO 自动生成的方法存根

					}

					@Override
					public void btnOK(DialogInterface dialog,
							List<List<String>> mData) {
						byte[] code = new byte[30];
						AppContext.selectedDJSpecCaseStr = GetSpecCode(code,
								data);
						// TODO 自动生成的方法存根
						UIHelper.showDianjianTouchIDPos(mContext,
								adjline.getLineID(),
								adjline.getLineName(), false);
						dialog.dismiss();
					}
				}, data, false, mContext.getResources().getString(R.string.dj_confirm_choice_speccase));
		dialog.setTitle(R.string.dj_confirm_speccase_list);
		dialog.show();
	}
	
	private String GetSpecCode(byte[] b, List<List<String>> mData) {
		String ret = "";
		for (int idx = 0; idx < b.length; idx++) {
			if (idx < mData.size()) {
				if (mData.get(idx).get(2).equals("1")) {
					ret += "1";
				} else if (mData.get(idx).get(2).equals("0")) {
					ret += "0";
				}
			} else {
				ret += "0";
			}
		}
		return ret;
	}
}