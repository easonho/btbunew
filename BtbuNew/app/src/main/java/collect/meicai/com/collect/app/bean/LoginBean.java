package collect.meicai.com.collect.app.bean;

/**
 * Coder: 何毅
 * Desc : 登陆模块所需要使用到参数
 * Date : 2015-04-02
 * Time : 14:10
 * Version:1.0
 */
public class LoginBean {

	private String phone;
	private String password;

	public LoginBean(String password, String phone) {
		this.password = password;
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
