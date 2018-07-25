package com.moons.xst.track.common.factory.myworkfactory;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.ui.Main_Page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class DJPCWork implements MyWork {
	private Context mContext;
	
	public void enterMyWork(Context context, final DJLine djline) {
		mContext = context;
		
		AppContext.voiceConvertService.Speaking(R.string.dj_confirm_speaking
				+ djline.getLineName());

		AppContext.DJSpecCaseFlag = 0;
		UIHelper.showDianjianTouchIDPos(mContext, djline.getLineID(),
				djline.getLineName(), false);
		
		/*if (DateTimeHelper.StringToDate(DateTimeHelper.getDateNow(), "yyyy-MM-dd")
				.after(djline.getBuildTime())) {
			LayoutInflater factory = LayoutInflater.from(mContext);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(mContext)
					.builder()
					.setTitle(mContext.getString(R.string.system_notice))
					.setView(view)
					.setMsg(mContext.getString(R.string.djpc_confirm_outofcycle_msg))
					.setPositiveButton(mContext.getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									return;
								}
							}).show();
		} else {
			AppContext.DJSpecCaseFlag = 0;
			UIHelper.showDianjianTouchIDPos(mContext, djline.getLineID(),
					djline.getLineName(), false);
		}*/
	}
}