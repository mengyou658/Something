package com.moons.xst.track.xstinterface;

import java.util.List;

import android.content.Context;

import com.moons.xst.buss.OperateHelper;
import com.moons.xst.buss.WorkBillHelper;
import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.common.CyptoUtils;

public class WorkBillLogin implements OtherLoginInterface<BillUsers> {

	@Override
	public List<BillUsers> checkLogin(Context context, String account,
			String password) {

		List<BillUsers> userList = WorkBillHelper.GetIntance()
				.checkLogin(context, account, CyptoUtils.MD5(password));

		return userList;
	}
}
