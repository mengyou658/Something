/**
 * 
 */
package com.moons.xst.simplecache;

import com.moons.xst.track.AppContext;

/**
 * 小神探二级缓存机制封装
 * 
 * @一级缓存：临时保存在内存中
 * @二级缓存:临时保存到物理磁盘中（采用主流的轻量级的AsimpleCache框架）
 * 
 * @author LKZ
 * 
 */
public class MoonsCacheHelper {

	/**
	 * 将对象保存到内存缓存中
	 * 
	 * @param key
	 * @param value
	 */
	public static void setMemCache(String key, Object value) {
		AppContext.memCacheRegion.put(key, value);
	}

	/**
	 * 从内存缓存中获取对象
	 * 
	 * @param key
	 * @return
	 */
	public static Object getMemCache(String key) {
		return AppContext.memCacheRegion.get(key);
	}

	/**
	 * 清除内存缓存
	 */
	public static void clearMemCache() {
		AppContext.memCacheRegion.clear();
	}

	/**
	 * 清理指定内存缓存
	 * 
	 * @param key
	 */
	public static void clearMemCacheByKey(String key) {
		AppContext.memCacheRegion.remove(key);
	}
}
