package com.moons.xst.buss;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.Z_DJ_Cycle;
import com.moons.xst.track.bean.Z_DJ_CycleDTL;
import com.moons.xst.track.bean.Z_WorkDate;
import com.moons.xst.track.dao.Z_DJ_CycleDao;

public class CycleHelper {
	private static final String TAG = "CycleHelper";

	private DJDAOSession djdaoSession;

	private Z_DJ_CycleDao djcycledao;

	static CycleHelper _intance;

	public static CycleHelper GetIntance() {
		if (_intance == null) {
			_intance = new CycleHelper();
		}
		return _intance;
	}

	InitDJsqlite init = new InitDJsqlite();
	CycleDTLHelper cycleDTLhelper = new CycleDTLHelper();

	public boolean loadCycleData(Context context) {
		try {
			AppContext.DJCycleBuffer.clear();
			List<Z_DJ_Cycle> djcyclelist = new ArrayList<Z_DJ_Cycle>();
			List<Z_DJ_CycleDTL> cycleDTLlist = new ArrayList<Z_DJ_CycleDTL>();
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			djcycledao = djdaoSession.getZ_DJ_CycleDao();
			djcyclelist = djcycledao.loadAll();
			cycleDTLlist = cycleDTLhelper.loadCycleDTLData(context);
			for (int i = 0; i < djcyclelist.size(); i++) {
				Cycle cycle = new Cycle();
				cycle.setDJCycle(djcyclelist.get(i));
				for (int j = 0; j < cycleDTLlist.size(); j++) {
					if (djcyclelist.get(i).getCycle_ID()
							.equals(cycleDTLlist.get(j).getCycle_ID())) {
						cycle.GetCycleDTL().add(cycleDTLlist.get(j));
					}
				}
				AppContext.DJCycleBuffer.add(cycle);
			}
			// 重新加载了周期后，马上进行一次周期运算
			// AppContext.calculateCycleService.calculateCycleNow(true);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return true;
		}
		return false;
	}

	public boolean loadWorkDate() {
		try {
			AppContext.workDateBuffer.clear();
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(AppConst.XSTBasePath()
					+ AppConst.WorkDateXmlFile));
			doc.normalize();
			NodeList tableList = doc.getElementsByTagName("Table");
			for (int i = 0; i < tableList.getLength(); i++) {
				Z_WorkDate date = new Z_WorkDate();
				Node table = tableList.item(i);
				int yearno = 0;
				int monthno = 0;
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeName().toUpperCase().equals("YEAR_NR")) {
						yearno = Integer.valueOf(node.getTextContent())
								.intValue();
						date.setYear_NR(Integer.valueOf(node.getTextContent())
								.intValue());
					} else if (node.getNodeName().toUpperCase()
							.equals("MONTH_NR")) {
						monthno = Integer.valueOf(node.getTextContent())
								.intValue();
						date.setMonth_NR(Integer.valueOf(node.getTextContent())
								.intValue());
					} else if (node.getNodeName().toUpperCase()
							.equals("DAY_TX")) {
						date.setDay_TX(node.getTextContent());
					}
				}
				if ((yearno == year && monthno == month)
						|| (yearno == year && monthno == month + 1)) {
					AppContext.workDateBuffer.add(date);
				}
			}
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

}
