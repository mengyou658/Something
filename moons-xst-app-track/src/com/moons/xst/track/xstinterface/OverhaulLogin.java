package com.moons.xst.track.xstinterface;

import java.util.List;

import android.content.Context;

import com.moons.xst.buss.OverhaulHelper;
import com.moons.xst.track.bean.OverhaulUser;
import com.moons.xst.track.common.CyptoUtils;

public class OverhaulLogin implements OtherLoginInterface<OverhaulUser> {

	@Override
	public List<OverhaulUser> checkLogin(Context context, String account,
			String password) {

		List<OverhaulUser> userList = OverhaulHelper.getInstance()
				.getLoginPerson(context, account, CyptoUtils.MD5(password));

		return userList;
	}
}