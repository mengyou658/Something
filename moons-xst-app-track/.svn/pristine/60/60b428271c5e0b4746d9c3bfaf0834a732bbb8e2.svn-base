package com.moons.xst.track.xstinterface;

import java.util.List;

import android.content.Context;

import com.moons.xst.buss.OperateHelper;
import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.common.CyptoUtils;

public class OperateBillLogin implements OtherLoginInterface<BillUsers> {

	@Override
	public List<BillUsers> checkLogin(Context context, String account,
			String password) {

		List<BillUsers> userList = OperateHelper.GetIntance()
				.checkLogin(context, account, CyptoUtils.MD5(password));

		return userList;
	}
}