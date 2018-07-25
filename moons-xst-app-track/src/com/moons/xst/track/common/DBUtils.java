package com.moons.xst.track.common;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
  
public class DBUtils{   
     private DBHelper dbHelper;   
	 public static DBUtils instance = null;   
	 private SQLiteDatabase sqliteDatabase;   
	 private Context context;  
     private String m_DBName;
      
	 private DBUtils(Context context, String dbName, CursorFactory factory, int version) {
		 this.context = context;
		 dbHelper = new DBHelper(context,dbName,factory,version); 
		 m_DBName = dbName;
		 sqliteDatabase = dbHelper.getReadableDatabase();                                                           
	 }   
	 
	 //定义成静态内部类更方法使用和维护  
	 private static class DBHelper extends SQLiteOpenHelper {  
		  // 可以在这写数据库各字段名称的静态变量名，但是最好是写在数据库管理类里，这里创建sql的时候，为了一目了然，最好还是以小写来写  
		  public DBHelper(Context context, String dbName, CursorFactory factory, int version) {  
			  super(context, dbName, factory, version);  
			  // 这里只继续父类构造函数，建表在onCreate方法里  
			  // this.context = context;  
		  }
		  
	        @Override
	        public void onCreate(SQLiteDatabase db) {
	            //Log.i(TAG, "Creating DataBase: " + CREATE_STUDENT_TABLE);
	            //db.execSQL(CREATE_STUDENT_TABLE);
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	           // Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
	        }		  
		  
	  }  
	  

    
      
    public static final DBUtils getInstance(Context context, String dbName, CursorFactory factory, int version) {   
        if (instance == null)    
            instance = new DBUtils(context,dbName,factory,version);   
        return instance;   
    }   
        
      
    
    public void close() {   
        if(sqliteDatabase.isOpen()) sqliteDatabase.close();   
        if(dbHelper != null) dbHelper.close();   
        if(instance != null) instance = null;   
    }   
    
      
    public Long insertDataBySql(String sql, String[] bindArgs) throws Exception{   
        long result = 0;   
        if(sqliteDatabase.isOpen()){   
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);   
            if(bindArgs != null){   
                int size = bindArgs.length;   
                for(int i = 0; i < size; i++){   
                    //将参数和占位符绑定，对应   
                    statement.bindString(i+1, bindArgs[i]);   
                }   
                result = statement.executeInsert();   
                statement.close();   
            }   
        }else{   
            Log.i("info", "数据库已关闭");   
        }   
        return result;   
    }   
        
      
    public Long insertData(String table, ContentValues values){   
        long result = 0;   
        if(sqliteDatabase.isOpen()){   
            result = sqliteDatabase.insert(table, null, values);   
        }   
        return result;   
    }   
        
      
    public void updateDataBySql(String sql, String[] bindArgs) throws Exception{   
        if(sqliteDatabase.isOpen()){   
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);   
            if(bindArgs != null){   
                int size = bindArgs.length;   
                for(int i = 0; i < size; i++){   
                    statement.bindString(i+1, bindArgs[i]);   
                }   
                statement.execute();   
                statement.close();   
            }   
        }else{   
            Log.i("info", "数据库已关闭");   
        }   
    }   
        
      
    public int updataData(String table, ContentValues values, String whereClause, String[] whereArgs){   
        int result = 0;   
        if(sqliteDatabase.isOpen()){   
            result = sqliteDatabase.update(table, values, whereClause, whereArgs);   
        }   
        return result;   
    }   
    
      
    public void deleteDataBySql(String sql, String[] bindArgs) throws Exception{   
        if(sqliteDatabase.isOpen()){   
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);   
            if(bindArgs != null){   
                int size = bindArgs.length;   
                for(int i = 0; i < size; i++){   
                    statement.bindString(i+1, bindArgs[i]);   
                }   
                Method[] mm = statement.getClass().getDeclaredMethods();   
                for (Method method : mm) {   
                    Log.i("info", method.getName());           
                      
                }   
                statement.execute();       
                statement.close();   
            }   
        }else{   
            Log.i("info", "数据库已关闭");   
        }   
    }   
    
      
    public int deleteData(String table, String whereClause, String[] whereArgs){   
        int result = 0;   
        if(sqliteDatabase.isOpen()){   
            result = sqliteDatabase.delete(table, whereClause, whereArgs);   
        }   
        return result;   
    }   
        
      
    public Cursor queryData2Cursor(String sql, String[] selectionArgs) throws Exception{   
        if(sqliteDatabase.isOpen()){   
            Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);   
            if (cursor != null) {   
                cursor.moveToFirst();   
            }   
            return cursor;   
        }   
        return null;   
    }   
        
      
    public List<Object> ListqueryData2Object(String sql, String[] selectionArgs, Object object) throws Exception{   
    	List<Object> mList  = new ArrayList<Object>();   
        if(sqliteDatabase.isOpen()){   
            Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);   
            Field[] f;   
            if(cursor != null && cursor.getCount() > 0) {   
                while(cursor.moveToNext()){   
                    f = object.getClass().getDeclaredFields();   
                    for(int i = 0; i < f.length; i++) {   
                        //为JavaBean 设值   
                        invokeSet(object, f[i].getName(), cursor.getString(cursor.getColumnIndex(f[i].getName())));   
                    }   
                    mList.add(object);   
                }   
            }   
            cursor.close();   
        }else{   
            Log.i("info", "数据库已关闭");   
        }   
        return mList;   
    }   
        
      
    public List<Map<String, Object>> queryData2Map(String sql, String[] selectionArgs, Object object)throws Exception{   
    	List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();   
        if(sqliteDatabase.isOpen()){   
            Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);   
            Field[] f;   
            Map map;   
            if(cursor != null && cursor.getCount() > 0) {   
                while(cursor.moveToNext()){   
                    map = new HashMap();   
                    f = object.getClass().getDeclaredFields();   
                    for(int i = 0; i < f.length; i++) {   
                        map.put(f[i].getName(), cursor.getString(cursor.getColumnIndex(f[i].getName())));   
                    }   
                    mList.add(map);   
                }   
            }   
            cursor.close();   
        }else{   
            Log.i("info", "数据库已关闭");   
        }   
        return mList;   
    }    
        
             
    @SuppressWarnings("unchecked")          
    public static Method getSetMethod(Class objectClass, String fieldName) {          
        try {          
            Class[] parameterTypes = new Class[1];          
            Field field = objectClass.getDeclaredField(fieldName);          
            parameterTypes[0] = field.getType();          
            StringBuffer sb = new StringBuffer();          
            sb.append("set");          
            sb.append(fieldName.substring(0, 1).toUpperCase());          
            sb.append(fieldName.substring(1));          
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);          
            return method;          
        } catch (Exception e) {          
            e.printStackTrace();          
        }          
        return null;          
    }          
          
             
    public static void invokeSet(Object object, String fieldName, Object value) {          
        Method method = getSetMethod(object.getClass(), fieldName);          
        try {          
            method.invoke(object, new Object[] { value });          
        } catch (Exception e) {          
            e.printStackTrace();          
        }          
    } 
}