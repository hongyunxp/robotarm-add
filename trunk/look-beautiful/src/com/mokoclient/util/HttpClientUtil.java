package com.mokoclient.util;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	
	private static final DefaultHttpClient client = new DefaultHttpClient();
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux i686; rv:5.0) Gecko/20100101 Firefox/5.0";
	private static final HttpClientUtil httpClientUtil = new HttpClientUtil();
	
	private HttpClientUtil(){}
	
	public static HttpClientUtil getInstance(){
		return httpClientUtil;
	}

	/**
	 * get
	 */
	public String get(String url) throws Throwable {
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, HTTP.UTF_8);
		get.abort();
		return result;
	}

	/**
	 * post
	 */
	public String post(String url, List<NameValuePair> params) throws Throwable{
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, HTTP.UTF_8);
		post.abort();
		return result;
	}
	
}
