package collect.meicai.com.collect.app.dao;

import android.content.ContentValues;

import java.util.List;
import java.util.Map;

import collect.meicai.com.collect.app.bean.ChannelItem;

/**
 * @version V1.0
 * @功能描述: ${file_name}
 * @author: 何毅
 * @date: ${date} ${time}
 */
public interface ChannelDaoInface {


	/**
	 * 添加到缓存
	 * @param item
	 * @return
	 */
	public boolean addCache(ChannelItem item);

	/**
	 * 清除缓存
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean deleteCache(String whereClause, String[] whereArgs);

	/**
	 * 更新缓存
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean updateCache(ContentValues values, String whereClause,
							   String[] whereArgs);

	/**
	 * 获得缓存的View对象
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public Map<String, String> viewCache(String selection,
										 String[] selectionArgs);

	/**
	 * 缓存的组数
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public List<Map<String, String>> listCache(String selection,
											   String[] selectionArgs);

	public void clearFeedTable();




}
