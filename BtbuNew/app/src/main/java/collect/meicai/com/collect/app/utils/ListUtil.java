package collect.meicai.com.collect.app.utils;


import java.util.List;

/**
 * Coder: 何毅
 * Desc : 工具类 两个ListView去重
 * Date : 2015-04-03
 * Time : 14:52
 * Version:1.0
 */
public class ListUtil {


	/**
	 * list数据去重问题
	 * @param list1
	 * @param list2
	 * @param <T>
	 * @return
	 */
	public static  <T> List<T> addList(List<T> list1,List<T> list2) {
		for(T item:list2){
			if(!list1.contains(item)){
				list1.add(item);
			}
		}
		return list1;
	}

}
