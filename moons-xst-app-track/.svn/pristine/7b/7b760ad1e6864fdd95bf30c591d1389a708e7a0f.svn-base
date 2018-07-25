package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.WorkBillSession;
import com.moons.xst.sqlite.TwoBillSession;
import com.moons.xst.track.bean.BillUsers;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class UsersDao extends AbstractDao<BillUsers, Void> {
	public static final String TABLENAME = "Users";

	public static class Properties {
		public final static Property UserID = new Property(0,String.class, "UserID", false, "UserID");
		public final static Property UserCD = new Property(1,String.class, "UserCD", false, "UserCD");
		public final static Property Account_TX = new Property(2,String.class, "Account_TX", false, "Account_TX");
		public final static Property UserType = new Property(3,String.class, "UserType", false,"UserType");
		public final static Property UserName_TX = new Property(4,String.class, "UserName_TX", false, "UserName_TX");
		public final static Property PassWord_TX = new Property(5, String.class, "PassWord_TX", false,"PassWord_TX");
		public final static Property Tache_ID = new Property(6, String.class, "Tache_ID", false,"Tache_ID");
	};

	public UsersDao(DaoConfig config) {
		super(config);
	}

	public UsersDao(DaoConfig config, TwoBillSession daoSession) {
		super(config, daoSession);
	}
	
	public UsersDao(DaoConfig config, WorkBillSession daoSession){
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Users' (" + //
                "'UserID' INTEGER PRIMARY KEY NOT NULL ," + // 0: UserID
                "'UserType' nvarchar(200) NOT NULL ," + // 1: UserType
                "'UserName_TX' nvarchar(200) NOT NULL ," + // 2: UserName_TX
                "'PassWord_TX' nvarchar(200) NOT NULL ," ); // 3: PassWord_TX
    }
    
	 /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Users'";
        db.execSQL(sql);
    }
	
	@Override
	protected void bindValues(SQLiteStatement stmt, BillUsers entity) {
		// TODO 自动生成的方法存根
		stmt.clearBindings();
        stmt.bindString(1, entity.getUser_ID());
        stmt.bindString(2, entity.getUser_CD());
        stmt.bindString(3, entity.getAccount_TX());
        stmt.bindString(4, entity.getUserType());
        stmt.bindString(5, entity.getUserName_TX());
        stmt.bindString(6, entity.getPassWord_TX());
        stmt.bindString(7, entity.getTache_ID());
	}

	@Override
	protected Void getKey(BillUsers entity) {
		// TODO 自动生成的方法存根
            return null;
	}

	@Override
	protected boolean isEntityUpdateable() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	protected BillUsers readEntity(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		BillUsers entity = new BillUsers( //
	            cursor.getString(offset + 0), // 
	            cursor.getString(offset + 1), // 
	            cursor.getString(offset + 2), // 
	            cursor.getString(offset + 3), //
	            cursor.getString(offset + 4), // 
	            cursor.getString(offset + 5), // 
	            cursor.getString(offset + 6) //
	        );
	        return entity;
	}

	@Override
	protected void readEntity(Cursor cursor, BillUsers entity, int offset) {
		// TODO 自动生成的方法存根
		entity.setUser_ID(cursor.getString(offset + 0));
		entity.setUser_CD(cursor.getString(offset + 1));
		entity.setAccount_TX(cursor.getString(offset + 2));
        entity.setUserType(cursor.getString(offset + 3));
        entity.setUserName_TX(cursor.getString(offset + 4));
        entity.setPassWord_TX(cursor.getString(offset + 5));
        entity.setTache_ID(cursor.getString(offset + 6));
	}

	@Override
	protected Void readKey(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected Void updateKeyAfterInsert(BillUsers entity, long arg1) {
		// TODO 自动生成的方法存根
		return null;
	}

	public List<BillUsers> checkLogin(String account, String pwd) {
		String sql = "select * from Users WHERE Account_TX='" + account
				+ "' and PassWord_TX='" + pwd + "'";
		Cursor cursor = db.rawQuery(sql, null);
		
		 List<BillUsers> res =  loadAllAndCloseCursor(cursor);
		return res;
	}
}
