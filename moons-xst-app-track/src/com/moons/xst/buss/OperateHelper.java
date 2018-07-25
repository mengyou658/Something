package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.moons.xst.sqlite.TwoBillSession;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.bean.Operate_Bill;
import com.moons.xst.track.bean.Operate_Detail_Bill;
import com.moons.xst.track.dao.Operate_BillDao;
import com.moons.xst.track.dao.Operate_DetailBillDao;
import com.moons.xst.track.dao.UsersDao;

public class OperateHelper {
	private static final String TAG = "OperateHelper";
	private TwoBillSession twoBillSession;

	private UsersDao usersDao;
	private Operate_BillDao Operate_BillDao;
	private Operate_DetailBillDao Operate_DetailBillDao;

	InitDJsqlite init = new InitDJsqlite();
	static OperateHelper _intance;

	public static OperateHelper GetIntance() {
		if (_intance == null) {
			_intance = new OperateHelper();
		}
		return _intance;
	}

	/**
	 * 查询所有操作信息
	 * 
	 */
	public List<Operate_Bill> operateQuery(Context context, List<BillUsers> user) {
		try {
			List<Operate_Bill> initial = new ArrayList<Operate_Bill>();
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_BillDao = twoBillSession.getOperate_BillDao();
			List<Operate_Bill> temporary = Operate_BillDao.loadAll();
			for (int i = 0; i < temporary.size(); i++) {
				for (int j = 0; j < user.size(); j++) {
					if (temporary.get(i).getTache_ID()
							.equals(user.get(j).getTache_ID())) {
						initial.add(temporary.get(i));
						break;
					}
				}
			}
			return initial;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Operate_Detail_Bill> DetailQuery(Context context) {
		try {
			List<Operate_Detail_Bill> list = new ArrayList<Operate_Detail_Bill>();
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_DetailBillDao = twoBillSession.getOperate_DetailBillDao();
			list = Operate_DetailBillDao.loadAll();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 模糊搜索操作信息
	 * 
	 */
	public List<Operate_Bill> operateSearch(Context context, String search) {
		try {
			List<Operate_Bill> initial = new ArrayList<Operate_Bill>();
			List<Operate_Bill> OperateList = new ArrayList<Operate_Bill>();
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_BillDao = twoBillSession.getOperate_BillDao();
			initial = Operate_BillDao.loadAll();
			for (Operate_Bill temporary : initial) {
				if (temporary.getOperate_Task_Content().contains(search)) {
					OperateList.add(temporary);
				}
			}
			return OperateList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 查询单个操作信息
	 * 
	 */
	public Operate_Bill aOperateQuery(Context context, String Code) {
		try {
			List<Operate_Bill> initial = new ArrayList<Operate_Bill>();
			Operate_Bill Operate = new Operate_Bill();
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_BillDao = twoBillSession.getOperate_BillDao();
			initial = Operate_BillDao.loadAll();
			for (Operate_Bill temporary : initial) {
				if (temporary.getOperate_Bill_Code().equals(Code)) {
					Operate = temporary;
					break;
				}
			}
			return Operate;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 查询危险点
	 * 
	 */
	public String dangerpointQuery(Context context, String Code) {
		try {
			List<Operate_Detail_Bill> initial = new ArrayList<Operate_Detail_Bill>();
			// List<Operate_Detail_Bill> OperateDetail = new
			// ArrayList<Operate_Detail_Bill>();
			String dangerpoint = "";
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_DetailBillDao = twoBillSession.getOperate_DetailBillDao();
			initial = Operate_DetailBillDao.loadAll();
			for (Operate_Detail_Bill temporary : initial) {
				if (temporary.getOperate_Bill_Code().equals(Code)
						&& temporary.getOperate_Type_ID().equals("0")) {
					dangerpoint = dangerpoint
							+ temporary.getOperate_Detail_Item_Content() + "\n";
				}
			}
			return dangerpoint;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 查询操作項目
	 * 
	 */
	public List<Operate_Detail_Bill> clauseQuery(Context context, String Code) {
		List<Operate_Detail_Bill> OperateDetail = new ArrayList<Operate_Detail_Bill>();
		try {
			List<Operate_Detail_Bill> initial = new ArrayList<Operate_Detail_Bill>();
			// String dangerpoint = "";
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_DetailBillDao = twoBillSession.getOperate_DetailBillDao();
			initial = Operate_DetailBillDao.loadAll();
			for (Operate_Detail_Bill temporary : initial) {
				if (temporary.getOperate_Bill_Code().equals(Code)
						&& temporary.getOperate_Type_ID().equals("1")) {
					OperateDetail.add(temporary);
				}
			}
			// 按照操作顺序排序
			Collections.sort(OperateDetail,
					new Comparator<Operate_Detail_Bill>() {
						@Override
						public int compare(Operate_Detail_Bill lhs,
								Operate_Detail_Bill rhs) {
							// TODO 自动生成的方法存根
							int a = Integer.valueOf(
									lhs.getOperate_Detail_Item_Order())
									.intValue();
							int b = Integer.valueOf(
									rhs.getOperate_Detail_Item_Order())
									.intValue();
							if (b > a) {
								return -1;
							}
							if (b < a) {
								return 1;
							}
							return 0;
						}
					});
			return OperateDetail;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 修改操作状态
	 * 
	 */
	public void updateOperateState(Context context, String Operate_id,
			String State, String time) {
		try {
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_DetailBillDao = twoBillSession.getOperate_DetailBillDao();
			Operate_DetailBillDao.updateOperateState(Operate_id, State, time);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 查询操作项详情
	 * 
	 */
	public Operate_Detail_Bill clauseDetailQuery(Context context, String Code,
			String Order) {
		Operate_Detail_Bill OperateDetail = new Operate_Detail_Bill();
		try {
			List<Operate_Detail_Bill> initial = new ArrayList<Operate_Detail_Bill>();
			// String dangerpoint = "";
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_DetailBillDao = twoBillSession.getOperate_DetailBillDao();
			initial = Operate_DetailBillDao.loadAll();
			for (Operate_Detail_Bill temporary : initial) {
				if (temporary.getOperate_Bill_Code().equals(Code)
						&& temporary.getOperate_Detail_Item_Order().equals(
								Order)
						&& temporary.getOperate_Type_ID().equals("1")) {
					OperateDetail = temporary;
				}
			}

			return OperateDetail;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public Boolean operateJudge(Context context, String Code, String Order) {
		List<String> list = new ArrayList<String>();
		try {
			List<Operate_Detail_Bill> initial = new ArrayList<Operate_Detail_Bill>();
			// String dangerpoint = "";
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			Operate_DetailBillDao = twoBillSession.getOperate_DetailBillDao();
			initial = Operate_DetailBillDao.loadAll();
			for (Operate_Detail_Bill temporary : initial) {
				if (temporary.getOperate_Bill_Code().equals(Code)
						&& temporary.getOperate_Type_ID().equals("1")) {
					list.add(temporary.getOperate_Detail_Item_Order());
				}
			}
			int count = Integer.valueOf(Order).intValue();
			if (list.size() > count) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	/**
	 * 操作票登录验证
	 * 
	 * @param context
	 * @param account
	 * @param pwd
	 * @return
	 */
	public List<BillUsers> checkLogin(Context context, String account,
			String pwd) {
		List<BillUsers> list = new ArrayList<BillUsers>();
		try {
			twoBillSession = ((AppContext) context.getApplicationContext())
					.getTwoBillSession();
			usersDao = twoBillSession.getUsersDao();
			list = usersDao.checkLogin(account, pwd);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
		return list;
	}
}
