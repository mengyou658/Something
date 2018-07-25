package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;

public class SystemBugInfoAdapter extends CommonAdapter<String> {
	
	private final String Select = "SELECT";
	private final String Cancel = "CANCEL";
	private String mtype;
	
	public SystemBugInfoAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String type) {
		super(context, mDatas, itemLayoutId);
		mtype = type;
		AppContext.selectedBugInfoBuffer.clear();
	}
	
	public void refresh(List<String> list, String opertype) {
		this.mDatas = list;
		mtype = opertype;
		AppContext.selectedBugInfoBuffer.clear();
		this.notifyDataSetChanged();
	}
	
	@Override
	public void convert(final ViewHolder helper, final String item) {
		
		final TextView mTextView = helper.getView(R.id.listitem_systembuginfo_itemdesc);
		final TextView mSubTextView = helper.getView(R.id.listitem_systembuginfo_itemdesc_subdesc);
		final CheckBox mCheckBox = helper.getView(R.id.systembuginfo_chk_setting);
		
		mTextView.setText(item.split("\\|", 3)[0].toString());
		mSubTextView.setText(item.split("\\|", 3)[1].toString() 
				+ "  " + item.split("\\|", 3)[2].toString());
		if (mtype.equals(Cancel)) {
			mCheckBox.setChecked(false);
			mCheckBox.setVisibility(View.GONE);
		}
		else {
			mCheckBox.setVisibility(View.VISIBLE);
		}
		
		mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 自动生成的方法存根
				if (isChecked) {
					AppContext.selectedBugInfoBuffer.add(item.split("\\|", 3)[0].toString());
				} else {
					AppContext.selectedBugInfoBuffer.remove(item.split("\\|", 3)[0].toString());
				}
			}
		});
	}
}