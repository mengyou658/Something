package com.moons.xst.track.xstinterface;

import java.util.List;

import android.content.Context;

public interface OtherLoginInterface<T> {
	
	public List<T> checkLogin(Context context, String account, String pwd);
}