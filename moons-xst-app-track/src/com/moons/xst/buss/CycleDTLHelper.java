package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.Z_DJ_CycleDTL;
import com.moons.xst.track.dao.Z_DJ_CycleDTLDao;

public class CycleDTLHelper {

	private static final String TAG = "CycleDTLHelper";

	private DJDAOSession djdaoSession;

	private Z_DJ_CycleDTLDao djcycledtldao;

	InitDJsqlite init = new InitDJsqlite();
	List<Z_DJ_CycleDTL> djcycledtllist = new ArrayList<Z_DJ_CycleDTL>();

	public List<Z_DJ_CycleDTL> loadCycleDTLData(Context context) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			djcycledtldao = djdaoSession.getZ_DJ_CycleDTLDao();
			djcycledtllist = djcycledtldao.loadAll();
			return djcycledtllist;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}

	}

}
