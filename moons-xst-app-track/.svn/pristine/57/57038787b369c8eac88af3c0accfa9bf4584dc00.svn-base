package com.moons.xst.track.ui.main_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.moons.xst.track.R;
import com.moons.xst.track.common.UIHelper;

import de.greenrobot.event.EventBus;

public class ToolsFragment extends Fragment implements View.OnClickListener {

	View toolsFragment;
	
	RelativeLayout rl_layout_tools_readid, rl_layout_tools_capture, rl_layout_tools_Carema, 
				rl_layout_tools_Recorder, rl_layout_tools_GPS, rl_layout_tools_Voice;
	
	RelativeLayout rl_layout_tools_cewen, rl_layout_tools_cezhen, rl_layout_tools_cezs;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (toolsFragment == null) {
			toolsFragment = inflater.inflate(R.layout.fragment_tools, container, false);	
		}
		
		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
        ViewGroup parent = (ViewGroup) toolsFragment.getParent();
        if (parent != null) {
            parent.removeView(toolsFragment);
        }
		return toolsFragment;		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	@Override 
	public void onDestroyView() {
        super.onDestroyView();
    }
	
	private void init() {
		rl_layout_tools_readid = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_readid);
		rl_layout_tools_capture = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_capture);
		rl_layout_tools_Carema = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_Carema);
		rl_layout_tools_Recorder = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_Recorder);
		rl_layout_tools_GPS = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_GPS);
		rl_layout_tools_Voice = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_Voice);
		
		rl_layout_tools_cewen = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_cewen);
		rl_layout_tools_cezhen = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_cezhen);
		rl_layout_tools_cezs = (RelativeLayout) toolsFragment.findViewById(R.id.rl_layout_tools_cezs);
		
		rl_layout_tools_readid.setOnClickListener(this);
		rl_layout_tools_capture.setOnClickListener(this);
		rl_layout_tools_Carema.setOnClickListener(this);
		rl_layout_tools_Recorder.setOnClickListener(this);
		rl_layout_tools_GPS.setOnClickListener(this);
		rl_layout_tools_Voice.setOnClickListener(this);
		
		rl_layout_tools_cewen.setOnClickListener(this);
		rl_layout_tools_cezhen.setOnClickListener(this);
		rl_layout_tools_cezs.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 标牌识别工具
		case R.id.rl_layout_tools_readid:
			UIHelper.showNFCTool(getActivity());
			break;
		// 扫一扫
		case R.id.rl_layout_tools_capture:
			UIHelper.showCapture(getActivity());
			break;
		// 相机
		case R.id.rl_layout_tools_Carema:
			UIHelper.showCamera(getActivity());
			break;
		// 录音机
		case R.id.rl_layout_tools_Recorder:
			EventBus.getDefault().post("Recorder");
			break;
		// GPS定位
		case R.id.rl_layout_tools_GPS:
			UIHelper.showGPSTool(getActivity());
			break;
		// 语音工具
		case R.id.rl_layout_tools_Voice:
			UIHelper.showVoice(getActivity());
			break;
		// 温度测量
		case R.id.rl_layout_tools_cewen:
			EventBus.getDefault().post("CW");
			break;
		// 振动测量
		case R.id.rl_layout_tools_cezhen:
			EventBus.getDefault().post("CZ");
			break;
		// 转速测量
		case R.id.rl_layout_tools_cezs:
			EventBus.getDefault().post("CS");
			break;
		}
	}
}
