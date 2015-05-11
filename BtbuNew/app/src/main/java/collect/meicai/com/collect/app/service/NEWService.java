package collect.meicai.com.collect.app.service;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.VolleyLog;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import collect.meicai.com.collect.R;
import collect.meicai.com.collect.app.bean.NewsEntity;
import collect.meicai.com.collect.app.net.NetWorkUtil;
import collect.meicai.com.collect.app.net.NewApi;

/**
 * Coder: 何毅
 * Desc : 媒体工商新闻服务类
 * Date : 2015-04-19
 * Time : 22:37
 * Version:1.0
 */
public class NEWService {

	/***
	 * 读取NewEntity数据
	 * @param context  上下文
	 * @param address  请求地址
	 * @param page     请求的页数
	 * @param className  解析的className
	 * @return  新闻列表
	 */
	public  static  ArrayList<NewsEntity> getNewsByXutils(final Activity context,final String address, final int page,final String className){

		String ServiceUrl;
		final ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();

		if(page <= 1){
			ServiceUrl = address + "index.htm";
		}else{
			ServiceUrl = address + "index" + (page - 1) +".htm";
		}

		VolleyLog.e("访问的服务器地址" + ServiceUrl);
		RequestParams params = new RequestParams();
		NetWorkUtil.getJSONByXutil(context,ServiceUrl, HttpRequest.HttpMethod.GET,params,true,"正在加载数据...",new NetWorkUtil.NetWorkListener() {
			@Override
			public void onSucceed(String response) {

				Document doc =  Jsoup.parse(response);
				Element tagName  = doc.getElementsByAttributeValue("class", className).get(0).child(0);
				Elements news    = tagName.children();

				int lenght = 10;
				lenght = news.size() > lenght ? lenght:news.size();

				for(int i = 0 ; i < news.size(); i++){
					NewsEntity bean = new NewsEntity();
					String  publishTime = news.get(i).getElementsByTag("span").text();
					String  title = news.get(i).getElementsByTag("a").text();
					String  source_url = news.get(i).getElementsByTag("a").attr("href");

					bean.setId(source_url);            /**设置信息地址*/
					bean.setPublishTime(publishTime);  /** 发布的时间*/
					bean.setTitle(title+"");           /**新闻标题*/
					if(source_url.startsWith(context.getString(R.string.host))){
						bean.setSource_url(source_url); /**新闻详细地址*/
					}else{
						bean.setSource_url(address + source_url);/**新闻详细地址*/
					}

					bean.setId((page-1)*25+i+"");
					bean.setNewsId(i + "");
					bean.setCollectStatus(false);
					bean.setCommentNum("100");
					bean.setInterestedStatus(true);
					bean.setLikeStatus(true);
					bean.setReadStatus(false);
					bean.setNewsCategory("推荐");
					bean.setNewsCategoryId("2");
					bean.setComment("评论部分，说的非常好。");
					bean.setMark(i);
					bean.setPicOne("http://www.btbu.edu.cn/images/content/2015-04/20150420174712412586.jpg");
					bean.setPicTwo("http://www.btbu.edu.cn/images/content/2015-04/20150420174712412586.jpg");
					bean.setPicThr("http://www.btbu.edu.cn/images/content/2015-04/20150420174712412586.jpg");
					newsList.add(bean);

				}
			}

			@Override
			public void onFailed(String errorCode, String errorMsg) {

			}
		});

		return newsList;

	}



	/**
	 * 详细新闻信息类
	 * @param url  访问的地址
	 * @param news_title 新闻标题
	 * @return
	 */
	public static String getNewsDetails(Activity context,String url, String news_title,
										String className) {

		Document document = null;
		String data = "<body>";
			/**动态的显示是否显示titleBar*/
			if(url.startsWith(NewApi.ZHXW) || url.startsWith(NewApi.ZXZDT)){
				data = data +"<center><h2 style='font-size:18px;'>" + news_title + "</h2></center>";
				data = data + "<hr size='1' />";
			}
		try {
			document = Jsoup.connect(url).timeout(9000).get();
			Element element = null;
			if (url.startsWith(NewApi.ZHXW)) {
				/**详细综合新闻*/
				element = document.getElementsByTag("tbody").get(6);
			}else if(url.startsWith(NewApi.ZXZDT)){
				/**详细的要闻信息*/
				element = document.getElementsByTag("tbody").get(6);
			}else{
				/**得到的是媒体工商新闻*/
				element = document.getElementsByAttributeValue("class",className).get(0);
			}
			if (element != null) {
				data = data + replace1(element.html().toString(),"../../", context.getString(R.string.host));
			}
			data = data + "</body>";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}


	/**源字串，要替换源字串,替换为的目的字串*/
	private  static  String   replace1(String  s, String org, String   ob)
	{
		String  newString="";
		int   first=0;
		while(s.indexOf(org)!=-1)
		{
			first=s.indexOf(org);

			if (first!=s.length())
			{
				newString=newString+s.substring(0,first)+ob;
				s=s.substring(first+org.length()   ,s.length()   );
			}
		} newString=newString+s;
		return   newString;
	}
}
