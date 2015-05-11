package com.meicai.util.net;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by woo on 1/13/15.
 */
public class http {
    public static String Post(String url,String cookie,String value)  {
        HttpClient hc = new DefaultHttpClient();

		HttpPost hp = new HttpPost(url);
        hp.setHeader("Content-Type","application/x-www-form-urlencoded");
        if (cookie != null) {
            hp.setHeader("Cookie",cookie);
			hp.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
        }

        try {
            hp.setEntity(new StringEntity(value));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return  getFromResp(hc,hp);
    }

    public static String Post(String url,String cookie,String  value,boolean b)  {
        HttpClient hc = new DefaultHttpClient();

		HttpPost hp = new HttpPost(url);
        hp.setHeader("Content-Type","application/x-www-form-urlencoded");
        if (cookie != null) {
            hp.setHeader("Cookie",cookie);
        }
		if(b){
			hp.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
		}


        try {
            hp.setEntity(new StringEntity(value));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return  getFromResp(hc,hp);
    }
    /*public static String Post(String url,String cookie1,String cookie2 ,String  value,boolean b)  {
        HttpClient hc = new DefaultHttpClient();

		HttpPost hp = new HttpPost(url);
        hp.setHeader("Content-Type","application/x-www-form-urlencoded");
        if (cookie1 != null) {
            hp.setHeader("Cookie",cookie1);
        }
		if(cookie2 != null){
			hp.setHeader("Cookie",cookie2);
		}

		if(b){
			hp.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
		}


        try {
            hp.setEntity(new StringEntity(value));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return  getFromResp(hc,hp);
    }
*/
    public static String Get(String url,String cookie)  {
        HttpClient hc = new DefaultHttpClient();
		HttpParams params = hc.getParams();
		HttpConnectionParams.setConnectionTimeout(params,10000);
		HttpConnectionParams.setSoTimeout(params, 10000);
        HttpGet hp = new HttpGet(url);
        if (cookie != null) {
            hp.setHeader("Cookie",cookie);
        }
        return getFromResp(hc,hp);
    }

    private static String getFromResp(HttpClient hc, HttpUriRequest hp) {
        try {
            HttpResponse hr = hc.execute(hp);
            BufferedReader rd = new BufferedReader(new InputStreamReader(hr.getEntity().getContent()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
