package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.moons.xst.sqlite.WorkBillSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.bean.Work_Bill;
import com.moons.xst.track.bean.Work_Detail_Bill;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.dao.UsersDao;
import com.moons.xst.track.dao.Work_BillDao;
import com.moons.xst.track.dao.Work_Detail_BillDao;

/**
 * 工作票下载库Helper
 **/
public class WorkBillHelper {
	private static final String TAG = "WorkBillHelper";
	private WorkBillSession workBillSession;

	private UsersDao usersDao;
	private Work_BillDao work_BillDao;
	private Work_Detail_BillDao work_Detail_BillDao;

	static WorkBillHelper _intance;

	public static WorkBillHelper GetIntance() {
		if (_intance == null) {
			_intance = new WorkBillHelper();
		}
		return _intance;
	}

	/**
	 * 查询工作票
	 * 
	 * @return
	 */
	public List<Work_Bill> getWorkBill(Context context, List<BillUsers> user) {
		List<Work_Bill> list = new ArrayList<Work_Bill>();
		try {
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_BillDao = workBillSession.getWork_BillDao();
			List<Work_Bill> temporary = work_BillDao.loadAll();
			for (int i = 0; i < temporary.size(); i++) {
				for (int j = 0; j < user.size(); j++) {
					if (temporary.get(i).getTache_ID().toString()
							.equals(user.get(j).getTache_ID())) {
						list.add(temporary.get(i));
						break;
					}
				}
			}
		} catch (Exception e) {
			e.toString();
		}
		return list;
	}

	// 查询单条工作票信息
	public Work_Bill getSingleWork(Context context, int workId) {
		Work_Bill entity = new Work_Bill();
		try {
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_BillDao = workBillSession.getWork_BillDao();
			List<Work_Bill> temporary = work_BillDao.selectWork(workId);
			entity = temporary.get(0);
		} catch (Exception e) {
			e.toString();
			return entity;
		}
		return entity;
	}

	/**
	 * 工作票登录验证
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
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			usersDao = workBillSession.getUsersDao();
			list = usersDao.checkLogin(account, pwd);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
		return list;
	}

	/**
	 * 根据作业id获取措施项
	 * 
	 * @param context
	 * @param workId
	 * @return
	 */
	public List<Work_Detail_Bill> getMeasureClause(Context context, int workId) {
		List<Work_Detail_Bill> list = new ArrayList<Work_Detail_Bill>();
		try {
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_Detail_BillDao = workBillSession.getWork_Detail_BillDao();
			list = work_Detail_BillDao.selectMeasureClause(workId);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
		return list;
	}

	/**
	 * 获取隔离措施详情
	 * 
	 * @param context
	 * @param workId
	 * @param order
	 * @return
	 */
	public Work_Detail_Bill getMeasureDetail(Context context, int workId,
			int order) {
		Work_Detail_Bill entity = new Work_Detail_Bill();
		try {
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_Detail_BillDao = workBillSession.getWork_Detail_BillDao();
			entity = work_Detail_BillDao.selectMeasureDetail(workId, order)
					.get(0);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
		return entity;
	}

	/**
	 * 保存隔离措施
	 * 
	 * @param context
	 * @param workId
	 * @param order
	 * @param time
	 * @param state
	 * @return
	 */
	public boolean saveMeasureDetail(Context context, String ItemId,
			String time, String state) {
		try {
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_Detail_BillDao = workBillSession.getWork_Detail_BillDao();
			work_Detail_BillDao.saveMeasureDetail(ItemId, time, state);
			return true;
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			return false;
		}
	}

	/**
	 * 修改工作票状态
	 * 
	 * @param context
	 * @param workId
	 * @return
	 */
	public boolean updateWorkState(Context context, int workId) {
		try {
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_BillDao = workBillSession.getWork_BillDao();
			work_BillDao.updateState(workId);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 保存负责人信息
	 * 
	 * @param context
	 * @param workId
	 * @return
	 */
	public String savePrincipal(Context context, int workId, String name,
			String OperatorName) {
		try {
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_BillDao = workBillSession.getWork_BillDao();
			work_BillDao.savePrincipal(workId, name, OperatorName);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			return e.toString();
		}
		return "true";
	}

	/**
	 * 不通过还原工作票
	 * 
	 * @param context
	 * @param workId
	 * @return
	 */
	public boolean updateWorkRestore(Context context, int workId) {
		try {
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_BillDao = workBillSession.getWork_BillDao();
			work_Detail_BillDao = workBillSession.getWork_Detail_BillDao();
			work_BillDao.restoreWork(workId);
			work_Detail_BillDao.restoreWorkDetail(workId);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 判断工作票是否有未签名的项，能否上传
	 * 
	 * @param context
	 * @return
	 */
	public boolean WorkBillCanUpload(Context context) {
		try {
			String DataBaseName = AppConst.XSTDBPath() + "WorkBill.sdf";
			// 如果下载库不存在则跳过不用判断
			if (!FileUtils.checkFileExists(DataBaseName)) {
				return true;
			}
			workBillSession = ((AppContext) context.getApplicationContext())
					.getWorkBillSession();
			work_BillDao = workBillSession.getWork_BillDao();
			List<Work_Bill> temporary = new ArrayList<Work_Bill>();
			temporary = work_BillDao.loadAll();
			for (Work_Bill entity : temporary) {
				if (entity.getJD_ID() == 1) {
					return false;
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			return false;
		}
		return true;
	}
}
