package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.OverhaulDaoSession;
import com.moons.xst.track.R.string;
import com.moons.xst.track.bean.OverhaulProject;
import com.moons.xst.track.common.DateTimeHelper;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table JX_WORK_ORDER_FOR_HNYN.
 */
public class OverhaulProjectDao extends AbstractDao<OverhaulProject, Void> {

	public static final String TABLENAME = "JX_WorkOrderForHNYN";

	/**
	 * Properties of entity JX_WorkOrderForHNYN.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property OVERHAULPROJECT_ID = new Property(0,
				int.class, "WorkOrderForHNYN_ID", false, "WorkOrderForHNYN_ID");
		public final static Property WorkOrder_CD = new Property(1,
				String.class, "WorkOrder_CD", false, "WorkOrder_CD");
		public final static Property ZYX_ID = new Property(2, Integer.class,
				"ZYX_ID", false, "ZYX_ID");
		public final static Property Mobject_ID = new Property(3,
				Integer.class, "Mobject_ID", false, "Mobject_ID");
		public final static Property Spec_ID = new Property(4, Integer.class,
				"Spec_ID", false, "Spec_ID");
		public final static Property DJOwner_ID = new Property(5,
				Integer.class, "DJOwner_ID", false, "DJOwner_ID");
		public final static Property CZDept_ID = new Property(6, Integer.class,
				"CZDept_ID", false, "CZDept_ID");
		public final static Property JXDept_ID = new Property(7, Integer.class,
				"JXDept_ID", false, "JXDept_ID");
		public final static Property JXType_ID = new Property(8, Integer.class,
				"JXType_ID", false, "JXType_ID");
		public final static Property WorkOrderName_TX = new Property(9,
				String.class, "WorkOrderName_TX", false, "WorkOrderName_TX");
		public final static Property WorkOrderContent_TX = new Property(10,
				String.class, "WorkOrderContent_TX", false,
				"WorkOrderContent_TX");
		public final static Property PlanStart_DT = new Property(11,
				String.class, "PlanStart_DT", false, "PlanStart_DT");
		public final static Property PlanEnd_DT = new Property(12,
				String.class, "PlanEnd_DT", false, "PlanEnd_DT");
		public final static Property JXPlan_ID = new Property(13,
				Integer.class, "JXPlan_ID", false, "JXPlan_ID");
		public final static Property HNYNType_TX = new Property(14,
				String.class, "HNYNType_TX", false, "HNYNType_TX");
		public final static Property TACHE_ID = new Property(15,
				Integer.class, "TACHE_ID", false, "TACHE_ID");
		public final static Property QC0 = new Property(16, String.class,
				"QC0", false, "QC0");
		public final static Property QC0_TX = new Property(17, String.class,
				"QC0_TX", false, "QC0_TX");
		public final static Property QC0_MemoTX = new Property(18,
				String.class, "QC0_MemoTX", false, "QC0_MemoTX");
		public final static Property QC1 = new Property(19, String.class,
				"QC1", false, "QC1");
		public final static Property QC1_TX = new Property(20, String.class,
				"QC1_TX", false, "QC1_TX");
		public final static Property QC1_MemoTX = new Property(21,
				String.class, "QC1_MemoTX", false, "QC1_MemoTX");
		public final static Property QC2 = new Property(22, String.class,
				"QC2", false, "QC2");
		public final static Property QC2_TX = new Property(23, String.class,
				"QC2_TX", false, "QC2_TX");
		public final static Property QC2_MemoTX = new Property(24,
				String.class, "QC2_MemoTX", false, "QC2_MemoTX");
		public final static Property QC3 = new Property(25, String.class,
				"QC3", false, "QC3");
		public final static Property QC3_TX = new Property(26, String.class,
				"QC3_TX", false, "QC3_TX");
		public final static Property QC3_MemoTX = new Property(27,
				String.class, "QC3_MemoTX", false, "QC3_MemoTX");
		public final static Property Finish_YN = new Property(28, String.class,
				"Finish_YN", false, "Finish_YN");
		public final static Property CurrentQC_TX = new Property(29,
				String.class, "CurrentQC_TX", false, "CurrentQC_TX");
	};

	public OverhaulProjectDao(DaoConfig config) {
		super(config);
	}

	public OverhaulProjectDao(DaoConfig config, OverhaulDaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'JX_WorkOrderForHNYN' (" + //
				"'OVERHAULPROJECT_ID' INTEGER NOT NULL ," + // 0:
															// OVERHAULPROJECT_ID
				"'WORK_ORDER_CD' TEXT," + // 1: WorkOrder_CD
				"'ZYX_ID' INTEGER," + // 2: ZYX_ID
				"'MOBJECT_ID' INTEGER," + // 3: Mobject_ID
				"'SPEC_ID' INTEGER," + // 4: Spec_ID
				"'DJOWNER_ID' INTEGER," + // 5: DJOwner_ID
				"'CZDEPT_ID' INTEGER," + // 6: CZDept_ID
				"'JXDEPT_ID' INTEGER," + // 7: JXDept_ID
				"'JXTYPE_ID' INTEGER," + // 8: JXType_ID
				"'WORK_ORDER_NAME_TX' TEXT," + // 9: WorkOrderName_TX
				"'WORK_ORDER_CONTENT_TX' TEXT," + // 10: WorkOrderContent_TX
				"'PLAN_START_DT' TEXT," + // 11: PlanStart_DT
				"'PLAN_END_DT' TEXT," + // 12: PlanEnd_DT
				"'JXPLAN_ID' INTEGER," + // 13: JXPlan_ID
				"'HNYNTYPE_TX' TEXT," + // 14: HNYNType_TX
				"'TACHE_ID' Integer," +
				"'QC0' TEXT," + // 15: QC0
				"'QC0_TX' TEXT," + // 16: QC0_TX
				"'QC0_MEMO_TX' TEXT," + // 17: QC0_MemoTX
				"'QC1' TEXT," + // 18: QC1
				"'QC1_TX' TEXT," + // 19: QC1_TX
				"'QC1_MEMO_TX' TEXT," + // 20: QC1_MemoTX
				"'QC2' TEXT," + // 21: QC2
				"'QC2_TX' TEXT," + // 22: QC2_TX
				"'QC2_MEMO_TX' TEXT," + // 23: QC2_MemoTX
				"'QC3' TEXT," + // 24: QC3
				"'QC3_TX' TEXT," + // 25: QC3_TX
				"'QC3_MEMO_TX ' TEXT," + // 26: QC3_MemoTX
				"'FINISH_YN ' TEXT," + // 27: FINISH_YN
				"'CurrentQC_TX' TEXT);"

		); // 27: Finish_YN
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'JX_WorkOrderForHNYN'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, OverhaulProject entity) {
		stmt.clearBindings();
		stmt.bindLong(1, entity.getWorkOrderForHNYN_ID());

		String WorkOrder_CD = entity.getWorkOrder_CD();
		if (WorkOrder_CD != null) {
			stmt.bindString(2, WorkOrder_CD);
		}

		Integer ZYX_ID = entity.getZYX_ID();
		if (ZYX_ID != null) {
			stmt.bindLong(3, ZYX_ID);
		}

		Integer Mobject_ID = entity.getMobject_ID();
		if (Mobject_ID != null) {
			stmt.bindLong(4, Mobject_ID);
		}

		Integer Spec_ID = entity.getSpec_ID();
		if (Spec_ID != null) {
			stmt.bindLong(5, Spec_ID);
		}

		Integer DJOwner_ID = entity.getDJOwner_ID();
		if (DJOwner_ID != null) {
			stmt.bindLong(6, DJOwner_ID);
		}

		Integer CZDept_ID = entity.getCZDept_ID();
		if (CZDept_ID != null) {
			stmt.bindLong(7, CZDept_ID);
		}

		Integer JXDept_ID = entity.getJXDept_ID();
		if (JXDept_ID != null) {
			stmt.bindLong(8, JXDept_ID);
		}

		Integer JXType_ID = entity.getJXType_ID();
		if (JXType_ID != null) {
			stmt.bindLong(9, JXType_ID);
		}

		String WorkOrderName_TX = entity.getWorkOrderName_TX();
		if (WorkOrderName_TX != null) {
			stmt.bindString(10, WorkOrderName_TX);
		}

		String WorkOrderContent_TX = entity.getWorkOrderContent_TX();
		if (WorkOrderContent_TX != null) {
			stmt.bindString(11, WorkOrderContent_TX);
		}

		String PlanStart_DT = entity.getPlanStart_DT();
		if (PlanStart_DT != null) {
			stmt.bindString(12, PlanStart_DT);
		}

		String PlanEnd_DT = entity.getPlanEnd_DT();
		if (PlanEnd_DT != null) {
			stmt.bindString(13, PlanEnd_DT);
		}

		Integer JXPlan_ID = entity.getJXPlan_ID();
		if (JXPlan_ID != null) {
			stmt.bindLong(14, JXPlan_ID);
		}

		String HNYNType_TX = entity.getHNYNType_TX();
		if (HNYNType_TX != null) {
			stmt.bindString(15, HNYNType_TX);
		}
		
		Integer TACHE_ID = entity.getTACHE_ID();
		if (TACHE_ID != null) {
			stmt.bindLong(16, TACHE_ID);
		}

		String QC0 = entity.getQC0();
		if (QC0 != null) {
			stmt.bindString(17, QC0);
		}

		String QC0_TX = entity.getQC0_TX();
		if (QC0_TX != null) {
			stmt.bindString(18, QC0_TX);
		}

		String QC0_MemoTX = entity.getQC0_MemoTX();
		if (QC0_MemoTX != null) {
			stmt.bindString(19, QC0_MemoTX);
		}
		String QC1 = entity.getQC1();
		if (QC1 != null) {
			stmt.bindString(20, QC1);
		}

		String QC1_TX = entity.getQC1_TX();
		if (QC1_TX != null) {
			stmt.bindString(21, QC1_TX);
		}

		String QC1_MemoTX = entity.getQC1_MemoTX();
		if (QC1_MemoTX != null) {
			stmt.bindString(22, QC1_MemoTX);
		}
		String QC2 = entity.getQC2();
		if (QC2 != null) {
			stmt.bindString(23, QC2);
		}

		String QC2_TX = entity.getQC2_TX();
		if (QC2_TX != null) {
			stmt.bindString(24, QC2_TX);
		}

		String QC2_MemoTX = entity.getQC2_MemoTX();
		if (QC2_MemoTX != null) {
			stmt.bindString(25, QC2_MemoTX);
		}

		String QC3 = entity.getQC3();
		if (QC3 != null) {
			stmt.bindString(26, QC3);
		}

		String QC3_TX = entity.getQC3_TX();
		if (QC3_TX != null) {
			stmt.bindString(27, QC3_TX);
		}

		String QC3_MemoTX = entity.getQC3_MemoTX();
		if (QC3_MemoTX != null) {
			stmt.bindString(28, QC3_MemoTX);
		}

		String Finish_YN = entity.getFinish_YN();
		if (Finish_YN != null) {
			stmt.bindString(29, Finish_YN);
		}
		String CurrentQC_TX = entity.getCurrentQC();
		if (CurrentQC_TX != null) {
			stmt.bindString(30, CurrentQC_TX);
		}
	}

	/** @inheritdoc */
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public OverhaulProject readEntity(Cursor cursor, int offset) {
		OverhaulProject entity = new OverhaulProject(
				//
				cursor.getInt(offset + 0), // OVERHAULPROJECT_ID
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // WorkOrder_CD
				cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // ZYX_ID
				cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // Mobject_ID
				cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // Spec_ID
				cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // DJOwner_ID
				cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // CZDept_ID
				cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // JXDept_ID
				cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // JXType_ID
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // WorkOrderName_TX
				cursor.isNull(offset + 10) ? null : cursor
						.getString(offset + 10), // WorkOrderContent_TX
				cursor.isNull(offset + 11) ? null : cursor
						.getString(offset + 11), // PlanStart_DT
				cursor.isNull(offset + 12) ? null : cursor
						.getString(offset + 12), // PlanEnd_DT
				cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // JXPlan_ID
				cursor.isNull(offset + 14) ? null : cursor
						.getString(offset + 14), // HNYNType_TX
				cursor.isNull(offset + 15) ? null : cursor
						.getInt(offset + 15), // TACHE_ID
				cursor.isNull(offset + 16) ? null : cursor
						.getString(offset + 16), // QC0
				cursor.isNull(offset + 17) ? null : cursor
						.getString(offset + 17), // QC0_TX
				cursor.isNull(offset + 18) ? null : cursor
						.getString(offset + 18), // QC0_MemoTX
				cursor.isNull(offset + 19) ? null : cursor
						.getString(offset + 19), // QC1

				cursor.isNull(offset + 20) ? null : cursor
						.getString(offset + 20), // QC1_TX
				cursor.isNull(offset + 21) ? null : cursor
						.getString(offset + 21), // QC1_MemoTX
				cursor.isNull(offset + 22) ? null : cursor
						.getString(offset + 22), // QC2
				cursor.isNull(offset + 23) ? null : cursor
						.getString(offset + 23), // QC2_TX
				cursor.isNull(offset + 24) ? null : cursor
						.getString(offset + 24), // QC2_MemoTX
				cursor.isNull(offset + 25) ? null : cursor
						.getString(offset + 25), // QC3
				cursor.isNull(offset + 26) ? null : cursor
						.getString(offset + 26), // QC3_TX
				cursor.isNull(offset + 27) ? null : cursor
						.getString(offset + 27), // QC3_MemoTX
				cursor.isNull(offset + 28) ? null : cursor
						.getString(offset + 28), // Finish_YN
				cursor.isNull(offset + 29) ? null : cursor
						.getString(offset + 29) // Finish_YN

		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, OverhaulProject entity, int offset) {
		entity.setWorkOrderForHNYN_ID(cursor.getInt(offset + 0));
		entity.setWorkOrder_CD(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setZYX_ID(cursor.isNull(offset + 2) ? null : cursor
				.getInt(offset + 2));
		entity.setMobject_ID(cursor.isNull(offset + 3) ? null : cursor
				.getInt(offset + 3));
		entity.setSpec_ID(cursor.isNull(offset + 4) ? null : cursor
				.getInt(offset + 4));
		entity.setDJOwner_ID(cursor.isNull(offset + 5) ? null : cursor
				.getInt(offset + 5));
		entity.setCZDept_ID(cursor.isNull(offset + 6) ? null : cursor
				.getInt(offset + 6));
		entity.setJXDept_ID(cursor.isNull(offset + 7) ? null : cursor
				.getInt(offset + 7));
		entity.setJXType_ID(cursor.isNull(offset + 8) ? null : cursor
				.getInt(offset + 8));
		entity.setWorkOrderName_TX(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setWorkOrderContent_TX(cursor.isNull(offset + 10) ? null
				: cursor.getString(offset + 10));
		entity.setPlanStart_DT(cursor.isNull(offset + 11) ? null : cursor
				.getString(offset + 11));
		entity.setPlanEnd_DT(cursor.isNull(offset + 12) ? null : cursor
				.getString(offset + 12));
		entity.setJXPlan_ID(cursor.isNull(offset + 13) ? null : cursor
				.getInt(offset + 13));
		entity.setHNYNType_TX(cursor.isNull(offset + 14) ? null : cursor
				.getString(offset + 14));
		entity.setTACHE_ID(cursor.isNull(offset + 15) ? null : cursor
				.getInt(offset + 15));
		entity.setQC0(cursor.isNull(offset + 16) ? null : cursor
				.getString(offset + 16));
		entity.setQC0_TX(cursor.isNull(offset + 17) ? null : cursor
				.getString(offset + 17));
		entity.setQC0_MemoTX(cursor.isNull(offset + 18) ? null : cursor
				.getString(offset + 18));
		entity.setQC1(cursor.isNull(offset + 19) ? null : cursor
				.getString(offset + 19));
		entity.setQC1_TX(cursor.isNull(offset + 20) ? null : cursor
				.getString(offset + 20));
		entity.setQC1_MemoTX(cursor.isNull(offset + 21) ? null : cursor
				.getString(offset + 21));
		entity.setQC2(cursor.isNull(offset + 22) ? null : cursor
				.getString(offset + 22));
		entity.setQC2_TX(cursor.isNull(offset + 23) ? null : cursor
				.getString(offset + 23));
		entity.setQC2_MemoTX(cursor.isNull(offset + 24) ? null : cursor
				.getString(offset + 24));
		entity.setQC3(cursor.isNull(offset + 25) ? null : cursor
				.getString(offset + 25));
		entity.setQC3_TX(cursor.isNull(offset + 26) ? null : cursor
				.getString(offset + 26));
		entity.setQC3_MemoTX(cursor.isNull(offset + 27) ? null : cursor
				.getString(offset + 27));
		entity.setFinish_YN(cursor.isNull(offset + 28) ? null : cursor
				.getString(offset + 28));
		entity.setCurrentQC(cursor.isNull(offset + 29) ? null : cursor
				.getString(offset + 29));
	}

	/** @inheritdoc */
	@Override
	protected Void updateKeyAfterInsert(OverhaulProject entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	/** @inheritdoc */
	@Override
	public Void getKey(OverhaulProject entity) {
		return null;
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	public List<OverhaulProject> loadAllBYDeptID(int deptID) {
		String sql = "select * from JX_WorkOrderForHNYN where JXDept_ID ='"
				+ deptID + "' order by WorkOrderForHNYN_ID asc";
		Cursor cursor = db.rawQuery(sql, null);
		return loadAllAndCloseCursor(cursor);
	}

	public void upDateProjectImplement(OverhaulProject overhaulProject) {
		String sqlString = "update JX_WorkOrderForHNYN set QC0 ='"
				+ overhaulProject.getQC0()
				+ "', CurrentQC_TX ='"
				+ overhaulProject.getCurrentQC()
				+ "', QC0_TX ='"
				+ overhaulProject.getQC0_TX() 
				+ "', QC0_MemoTX ='"
				+ overhaulProject.getQC0_MemoTX() 
				+ "' where WORKORDERFORHNYN_ID='"
				+ overhaulProject.getWorkOrderForHNYN_ID() 
				+ "'";
		db.execSQL(sqlString);
		
	}

	/**
	 * 更新表相关信息
	 * 
	 * @param context
	 * @param qc
	 *            项目编号
	 * @param Finish_YN
	 *            项目是否完成
	 * @param name
	 *            签名人信息+完成时间
	 * @param remark
	 *            备注信息
	 */
	public void upDateExamine1(OverhaulProject overhaulProject) {
		String sqlString = "update JX_WorkOrderForHNYN set QC1 ='"
				+ overhaulProject.getQC1() + "',CurrentQC_TX='"
				+ overhaulProject.getCurrentQC() + "', Finish_YN ='"
				+ overhaulProject.getFinish_YN() + "', QC1_TX ='"
				+ overhaulProject.getQC1_TX() + "', QC1_MemoTX ='"
				+ overhaulProject.getQC1_MemoTX() 
				+ "' where WORKORDERFORHNYN_ID='"
				+ overhaulProject.getWorkOrderForHNYN_ID()
				+ "'";
		db.execSQL(sqlString);
	}

	public void upDateExamine2(OverhaulProject overhaulProject) {
		String sqlString = "update JX_WorkOrderForHNYN set QC2 ='"
				+ overhaulProject.getQC2() + "',CurrentQC_TX='"
				+ overhaulProject.getCurrentQC() + "', Finish_YN ='"
				+ overhaulProject.getFinish_YN() + "', QC2_TX ='"
				+ overhaulProject.getQC2_TX() + "', QC2_MemoTX ='"
				+ overhaulProject.getQC2_MemoTX()
		    	+ "' where WORKORDERFORHNYN_ID='"
				+ overhaulProject.getWorkOrderForHNYN_ID()
				+ "'";
		db.execSQL(sqlString);
	}

	public void upDateExamine3(OverhaulProject overhaulProject) {
		String sqlString = "update JX_WorkOrderForHNYN set QC3 ='"
				+ overhaulProject.getQC3() + "',CurrentQC_TX='"
				+ overhaulProject.getCurrentQC() + "', Finish_YN ='"
				+ overhaulProject.getFinish_YN() + "', QC3_TX ='"
				+ overhaulProject.getQC3_TX() + "', QC3_MemoTX ='"
				+ overhaulProject.getQC3_MemoTX() 
				+ "' where WORKORDERFORHNYN_ID='"
				+ overhaulProject.getWorkOrderForHNYN_ID()
				+ "'";
		db.execSQL(sqlString);
	}
}
