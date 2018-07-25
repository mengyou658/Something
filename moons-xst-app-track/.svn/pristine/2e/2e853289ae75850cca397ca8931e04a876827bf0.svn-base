package com.moons.xst.track.xstinterface;

import java.util.List;

import android.content.Context;

public class OtherLoginManager<T> {
	private OtherLoginInterface<T> iOtherLogin;
	
	public OtherLoginManager(OtherLoginInterface<T> mInterface) {
		this.iOtherLogin = mInterface;
	}
	
	public List<T> checkLogin(Context context, String account, String pwd) {
		return iOtherLogin.checkLogin(context, account, pwd);
	}
}