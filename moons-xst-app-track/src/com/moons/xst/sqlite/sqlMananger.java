/**
 * @sqlMananger.java
 * @author LKZ
 * @2015-1-19
 */
package com.moons.xst.sqlite;

import java.io.InputStream;

import android.content.Context;

import com.moons.xst.track.R;
import com.moons.xst.track.common.FileUtils;


/**
 * com.moons.xst.sqlite
 * 数据库表创建语句管理服务类
 */
public class sqlMananger {

	private static String resultDBsql ="";
	/**
	 * 获取创建结果数据的sql语句
	 * @return
	 */
	public static String getResultDBSql(Context context)
	{
		InputStream inputStream =context.getResources().openRawResource(R.raw.creupbas);
		resultDBsql = FileUtils.readInStream(inputStream);
		return resultDBsql;
	}

	
}
