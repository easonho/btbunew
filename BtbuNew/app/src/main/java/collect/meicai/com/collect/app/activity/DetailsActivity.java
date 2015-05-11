package collect.meicai.com.collect.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.onekeyshare.OnekeyShare;
import com.umeng.analytics.MobclickAgent;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import collect.meicai.com.collect.R;
import collect.meicai.com.collect.app.bean.NewsEntity;
import collect.meicai.com.collect.app.net.NewApi;
import collect.meicai.com.collect.app.service.NEWService;


/**
 * 何毅：新闻的详情信息列表
 */
@SuppressLint("JavascriptInterface")
public class DetailsActivity extends BaseActivity {
	private TextView title;
	private ProgressBar progressBar;
	private FrameLayout customview_layout;
	private String news_url;
	private String news_title;
	private NewsEntity news;
	private TextView action_comment_count;
	private String dateilNewClassName;
	private WebView webView;
	private WebSettings settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
        ShareSDK.initSDK(this);
		setNeedBackGesture(true);//设置需要手势监听
		getData();
		initView();
		initWebView();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    /** 获取传递过来的数据 */
	private void getData() {
		news = (NewsEntity) getIntent().getSerializableExtra("news");
		this.dateilNewClassName = getIntent().getStringExtra("dateilNewClassName");
		this.news_url = news.getSource_url();
		this.news_title = news.getTitle();
	}

	private void initWebView() {

		webView = (WebView)findViewById(R.id.wb_details);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		if (!TextUtils.isEmpty(news_url)) {
			settings = webView.getSettings();
			/**设置可以运行JS脚本*/
			settings.setJavaScriptEnabled(true);
			settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			/**打开页面时， 自适应屏幕**/
			settings.setLoadWithOverviewMode(true);
			settings.setSupportZoom(false);// 用于设置webview放大
			settings.setBuiltInZoomControls(true);
			webView.setBackgroundResource(R.color.transparent);
			/** 添加js交互接口类，并起别名 imagelistner*/
			webView.addJavascriptInterface(new JavascriptInterface(getApplicationContext()),"imagelistner");
			webView.setWebChromeClient(new MyWebChromeClient());
			webView.setWebViewClient(new MyWebViewClient());


			/**访问URl 新闻标题 新闻来源 日期*/
			new MyAsnycTask().execute(news_url, news_title,dateilNewClassName);
		}
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title);
		progressBar = (ProgressBar) findViewById(R.id.ss_htmlprogessbar);
		customview_layout = (FrameLayout) findViewById(R.id.customview_layout);

		this.findViewById(R.id.action_textsize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WebSettings.TextSize.LARGER.equals(settings.getTextSize())){
                    settings.setTextSize(WebSettings.TextSize.NORMAL);
                }else{
                    settings.setTextSize(WebSettings.TextSize.LARGER);
                }
            }
        });

        findViewById(R.id.action_repost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

		progressBar.setVisibility(View.VISIBLE);
		title.setTextSize(13);
		title.setVisibility(View.VISIBLE);
		title.setText(news_url);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	/**
	 * 获得详细新闻详细新闻信息
	 */
	private class MyAsnycTask extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... urls) {
			String data= NEWService.getNewsDetails(DetailsActivity.this, urls[0], urls[1], urls[2]);
			return data;
		}

		@Override
		protected void onPostExecute(String data) {
			webView.loadDataWithBaseURL (null, data, "text/html", "utf-8",null);
		}
	}

	/** 注入js函数监听*/
	private void addImageClickListner() {
		//document.getElementsByTagName("img");"
		/** 这段js函数的功能就是，遍历所有的jpg几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去*/
		webView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\");"
				+ "var imgurl=''; " + "for(var i=0;i<objs.length;i++)  " + "{"
				+ "imgurl+=objs[i].src+',';"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(imgurl); "
				+ "    }  " + "}" + "})()");
	}

	/** js通信接口*/
	public class JavascriptInterface {

		private Context context;
		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String img) {

			String[] imgs = img.split(",");
			ArrayList<String> imgsUrl = new ArrayList<String>();
			for (String s : imgs) {
				imgsUrl.add(s);
			}
			Intent intent = new Intent();
			intent.putStringArrayListExtra("infos", imgsUrl);
			intent.setClass(context, ImageShowActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}


	/*** 监听*/
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return  true;
			//return  super.shouldOverrideUrlLoading(view,url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			/** html加载完成之后，添加监听图片的点击js函数*/
			addImageClickListner();
			progressBar.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
			progressBar.setVisibility(View.GONE);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if(newProgress != 100){
				progressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

    private void share() {
        OnekeyShare oks = new OnekeyShare();
        oks.setNotification(R.drawable.ic_launcher,
        this.getString(R.string.app_name));

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(news_url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(this.getString(R.string.share));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(this.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(news_url);

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(this.getString(R.string.app_name) + this.getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(news_url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(news_title +"\n新闻详细地址：" +news_url);
        oks.setSilent(true);
        oks.show(this);
    }

}
