/**
 * 
 */
package com.moons.xst.track.pad_ui.fragement;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moons.xst.track.R;

/**
 * @author LKZ
 * 
 */
public class Pad_JXGL_Fragment extends Fragment {

	View myLayout;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myLayout = inflater.inflate(R.layout.fragment_home_jxgl, container,
				false);
		return myLayout;
	}
}
