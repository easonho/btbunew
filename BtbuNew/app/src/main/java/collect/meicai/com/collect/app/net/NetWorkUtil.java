package collect.meicai.com.collect.app.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.android.volley.VolleyLog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import collect.meicai.com.collect.app.utils.SystemInfoUtils;
import collect.meicai.com.collect.app.utils.ToastUtil;

/**
 * Coder: 何毅
 * Desc : 用于封装整个app网络数据请求
 * Date : 2015-04-01
 * Time : 19:47
 * Version:1.0
 */
public class NetWorkUtil {

	protected static ProgressDialog progressDialog;

	/**
	 *
	 * @param context  上下文
	 * @param serviceUrl 访问地址
	 * @param httpMethod 访问的方法
	 * @param params    请求的参数
	 * @param DialogShow  是否显示对话框
	 * @param dialogMessage  显示对话框的信息
	 * @param workListener   接口回调
	 */
	public  static  void  getJSONByXutil( final Activity context,
										  String serviceUrl,
										  HttpRequest.HttpMethod httpMethod,
										  RequestParams params,
										  final Boolean DialogShow,
										  String dialogMessage,
										  final NetWorkListener workListener){


		VolleyLog.e("访问的地址" + serviceUrl);

		if (DialogShow) {
			progressDialog = ProgressDialog.show(context, null,
					dialogMessage);
			progressDialog.setCancelable(true);
		}

		if (SystemInfoUtils.getNetWorkType(context).equals("NET_NO")) {
			ToastUtil.show(context, "请检查网络");
			if (progressDialog != null);
			progressDialog.dismiss();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configTimeout(5*1000);
        /**设置新的地址缓存3秒 默认和1分钟*/
        httpUtils.configCurrentHttpCacheExpiry(3*1000);
		/**1，请求方式 2，请求地址 3,请求参数 4，回调（请求结果的回调）*/
		httpUtils.send(httpMethod,serviceUrl,params,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> stringResponseInfo) {

				if (stringResponseInfo.result!= null && !"".equals(stringResponseInfo.result)){

					workListener.onSucceed(stringResponseInfo.result);

				}else{

					workListener.onFailed(stringResponseInfo.statusCode + "",stringResponseInfo.result);
				}

					if (progressDialog!= null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				VolleyLog.e("错误信息=" + e.getExceptionCode() + e.getMessage() + s);
				workListener.onFailed("500","服务器响应失败");
				if (DialogShow) {
					if (progressDialog!= null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}

			}
		});

		if (DialogShow) {
			progressDialog
					.setOnKeyListener(new DialogInterface.OnKeyListener() {
						public boolean onKey(DialogInterface dialog,
											 int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								progressDialog.dismiss();
							}
							return true;
						}
					});// 如果点击返回键结束访问
		}

	}


	/**
	 * Coder: 何毅
	 * Desc : 接口回调
	 * Date : 2015-04-01
	 * Time : 19:50
	 * Version:1.0
	 */
	public interface NetWorkListener {
		/**
		 * 得到response 结果集
		 *
		 * Coder: 何毅
		 * Date : 2015-04-01 19:51
		 */
		public void onSucceed(String response);
		/**
		 *  失败的结果码
		 * Coder: 何毅
		 * Date : 2015-04-01  19:51
		 */
		public void onFailed(String errorCode, String errorMsg);
	}
}



