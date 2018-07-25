package com.moons.xst.track.ui.overhaul_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.OverhaulProject;
import com.moons.xst.track.common.StringUtils;

public class FragmentQC extends Fragment {

	private EditText ed_name;
	private EditText ed_memory;
	private OverhaulProject overhaulProject;
	private int num;
	private TextView projectExamineMemoTitle;

	public FragmentQC(OverhaulProject overhaulProject, int num) {
		this.overhaulProject = overhaulProject;
		this.num = num;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.fragment_qc, container, false);
		ed_name = (EditText) rootView.findViewById(R.id.et_examine_autograph);
		ed_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String editable = ed_name.getText().toString();  
	            String str = StringUtils.stringFilter(editable.toString());
	            if(!editable.equals(str)){
	            	ed_name.setText(str);
	              //设置新的光标所在位置  
	            	ed_name.setSelection(str.length());
	            }
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		ed_memory = (EditText) rootView.findViewById(R.id.project_examine_memo);
		projectExamineMemoTitle = (TextView) rootView
				.findViewById(R.id.project_examine_memo_title);
		ed_memory.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String editable = ed_memory.getText().toString();  
	            String str = StringUtils.stringFilterWithSomeSymbol(editable.toString());
	            if(!editable.equals(str)){
	        	    ed_memory.setText(str);
	              //设置新的光标所在位置  
	        	    ed_memory.setSelection(str.length());
	            }
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				projectExamineMemoTitle.setText(s.toString().length() + "/250");
			}
		});
		if (num == 1) {
			ed_name.setText(overhaulProject.getQC1_TX());
			ed_memory.setText(overhaulProject.getQC1_MemoTX());
			if ("1".equals(overhaulProject.getQC1())) {
				ed_name.setEnabled(false);
				ed_memory.setEnabled(false);
			}
			return rootView;
		} else if (num == 2) {
			ed_name.setText(overhaulProject.getQC2_TX());
			ed_memory.setText(overhaulProject.getQC2_MemoTX());
			if ("1".equals(overhaulProject.getQC2())) {
				ed_name.setEnabled(false);
				ed_memory.setEnabled(false);
			}
			return rootView;
		} else if (num == 3) {
			ed_name.setText(overhaulProject.getQC3_TX());
			ed_memory.setText(overhaulProject.getQC3_MemoTX());
			if ("1".equals(overhaulProject.getQC3())) {
				ed_name.setEnabled(false);
				ed_memory.setEnabled(false);
			}
			return rootView;
		}
		return null;
	}

	public void getSignName(CallBack callBack) {
		String name = ed_name.getText().toString();
		callBack.getResult(name);
	}

	public void getremarkTX(CallBack callBack) {
		String remarkTX = ed_memory.getText().toString();
		callBack.getResult(remarkTX);
	}

	public interface CallBack {
		public void getResult(String result);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

}
