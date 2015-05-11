package collect.meicai.com.collect.app.bean;

/**
 * Coder: 何毅
 * Desc : JOPO/TDO类 封装请求网络数据参数
 * Date : 2015-04-01
 * Time : 20:57
 * Version:1.0
 */
public class Business {


	/**
	 * token码
	 */
	private  String token;
	/**
	 * 请求的页数
	 */
	private String page;
	/**
	 * 请求页面大小
	 */
	private String pagesize;
	/**
	 * 业务类型
	 */
	private String businessType;
	/**
	 * 搜索的开始时间
	 */
	private String start_date;
	/**
	 * 搜索的结束时间
	 */
	private String end_time;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
}
