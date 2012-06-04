package com.mokoclient.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mokoclient.core.bean.MonthPostBean;
import com.mokoclient.core.bean.PostBean;
import com.mokoclient.core.bean.PostDetailBean;

public class Util {
	
	private static final Util u = new Util();
	private Util(){}
	public static Util getInstance(){
		return u;
	}
	
	/**职业大类 电影电视ID 1*/
	public static final int VOCATION_MOVIES_ID = 1;
	
	/**职业大类 演员主持ID 143*/
	public static final int VOCATION_ACTOR_ID = 143;
	
	/**职业大类 音乐ID 13*/
	public static final int VOCATION_MUSIC_ID = 13;
	
	/**职业大类 摄影造型ID 28*/
	public static final int VOCATION_PHOTOGRAPHY_ID = 28;
	
	/**职业大类 模特儿ID 23*/
	public static final int VOCATION_MODEL_ID = 23;
	
	/**职业大类 艺术ID 71*/
	public static final int VOCATION_ARTS_ID = 71;
	
	/**职业大类 设计ID 41*/
	public static final int VOCATION_DESIGN_ID = 41;
	
	/**职业大类 广告传媒ID 53*/
	public static final int VOCATION_ADS_ID = 53;
	
	/**职业大类 更多行业ID 94*/
	public static final int VOCATION_MORE_ID = 94;
	
	/**展示列表页ID -1*/
	public static final int POSTLIST_ID = -1;
	
	/**MTG5列表ID -2*/
	public static final int MTG5_ID = -2;
	
	/**
	 * moko 域名
	 */
	public static final String MOKO_DOMAIN = "http://www.moko.cc";
	public static final String MOKO_LOGIN_ACTION = MOKO_DOMAIN +"/login.action";
	public static final String USERNAMEKEY = "usermingzi";
	public static final String PASSWORDKEY = "userkey";
	public static final String USERNAME = "h231847@rppkn.com";
	public static final String PASSWORD = "h231847@rppkn.com";
	
	/**
	 * 错误页面标识
	 */
	public static final String ERROR_MARK = "images/nobody.jpg";

	private static Map<Integer, List<PostBean>> postListMap = new HashMap<Integer, List<PostBean>>(9);//缓存展示列表
	private static Map<String, List<String>> postDetailMap = new HashMap<String, List<String>>();//缓存展示详细
	private static Map<String, Integer> postDetailCountMap = new HashMap<String, Integer>();//缓存展示详细页数
	private static Map<Integer, Integer> vocationPageCounter;//缓存展示列表当前页
	static{
		vocationPageCounter = new HashMap<Integer, Integer>(9);
		vocationPageCounter.put(VOCATION_ACTOR_ID, 1);
		vocationPageCounter.put(VOCATION_ADS_ID, 1);
		vocationPageCounter.put(VOCATION_ARTS_ID, 1);
		vocationPageCounter.put(VOCATION_DESIGN_ID, 1);
		vocationPageCounter.put(VOCATION_MODEL_ID, 1);
		vocationPageCounter.put(VOCATION_MORE_ID, 1);
		vocationPageCounter.put(VOCATION_MOVIES_ID, 1);
		vocationPageCounter.put(VOCATION_MUSIC_ID, 1);
		vocationPageCounter.put(VOCATION_PHOTOGRAPHY_ID, 1);
		vocationPageCounter.put(POSTLIST_ID, 1);
		vocationPageCounter.put(MTG5_ID, 1);
	}
	
	/**
	 * 获取指定行业指定页url
	 * @param vocationId 行业ID
	 * @param curPage 当前页
	 * @return
	 */
	private String getVocationUrl(int vocationId, int curPage){
		switch (vocationId) {
		case POSTLIST_ID:
			return String.format(MOKO_DOMAIN + "/moko/post/%s.html", curPage);
		case MTG5_ID:
			return String.format(MOKO_DOMAIN + "/mtg/all/%s.html", curPage);
		default:
			return String.format(MOKO_DOMAIN + "/channels/post/%s/%s.html", vocationId, curPage);
		}
	}
	
	private String getListSelect(int vocationId){
		switch (vocationId) {
		case POSTLIST_ID:
			return "ul.post.big-post";
		case MTG5_ID:
			return "ul.contestant";
		default:
			return "ul.post.small-post";
		}
	}
	
	/**
	 * 获取当前月的精选展示url
	 * @return
	 */
	private String getMonthPostUrl(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		return getMonthPostUrl(year, month);
	}
	
	/**
	 * 获取指定月的精选展示url
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	private String getMonthPostUrl(int year, int month){
		return String.format(MOKO_DOMAIN + "/%s/%s/1/postMonthList.html", year, month);
	}

	/**
	 * 构造一个新的展示详细页列表
	 * @param postDetailUrl
	 * @param pageSize
	 * @return
	 * @throws Throwable
	 */
	private List<String> buildPostDetailList(String postDetailUrl, int pageSize) throws Throwable{
		List<String> postPicList = new ArrayList<String>();
		String html = HttpClientUtil.getInstance().get(postDetailUrl);
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("p.picBox img");
		for(Element postPic : elements)
			postPicList.add(postPic.attr("src2"));
		postDetailMap.put(postDetailUrl, postPicList);
		postDetailCountMap.put(postDetailUrl, postPicList.size() % pageSize > 0 ? postPicList.size() / pageSize + 1 : postPicList.size() / pageSize);
		return postPicList;
	}

	/**
	 * 构造一个新的展示列表
	 * @param vocationId
	 * @return
	 * @throws Throwable
	 */
	private void buildPostList(int vocationId, List<PostBean> postList) throws Throwable{
		int postCurPage = vocationPageCounter.get(vocationId);
		String html = getVocationHtml(vocationId, postCurPage);
		vocationPageCounter.put(vocationId, ++ postCurPage) ;
		Document doc = Jsoup.parse(html);
		String select = getListSelect(vocationId);
		Elements elements = doc.select(select);
		for(Element post : elements){
			PostBean postBean = getPostBean(vocationId, post);
			postList.add(postBean);
		}
		postListMap.put(vocationId, postList);
	}

	private PostBean getPostBean(int vocationId, Element post){
		Element cover;
		String title;
		String coverUrl;
		String detailUrl;
		switch (vocationId) {
		case MTG5_ID:
			cover = post.select("li.name a").get(0);
			title = cover.text();
			detailUrl = MOKO_DOMAIN + cover.attr("href");
			coverUrl = post.select("img[src2]").get(0).attr("src2");
			return new PostBean(title, coverUrl, detailUrl);
		default:
			cover = post.select(".cover").get(0);
			title = cover.attr("cover-text");
			coverUrl = cover.select("img").get(0).attr("src2").replace("_cover_", "_mokoshow_");//保证都是小图
			detailUrl = MOKO_DOMAIN + cover.select("a").get(0).attr("href");
			return new PostBean(title, coverUrl, detailUrl);
		}
	}
	
	/**
	 * 获取行业html字符串
	 * @param vocationId
	 * @param curPage
	 * @return
	 * @throws Throwable
	 */
	private String getVocationHtml(int vocationId, int curPage) throws Throwable{
		String url = getVocationUrl(vocationId, curPage);
		return HttpClientUtil.getInstance().get(url);
	}

	/**
	 * 获取展示列表
	 * @param vocationId
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Throwable
	 */
	public List<PostBean> getPostList(int vocationId, int curPage, int pageSize) throws Throwable{
		List<PostBean> postList = postListMap.get(vocationId);
		if(postList != null && postList.size() == 0)
			return null;
		if(postList == null)
			postList = new ArrayList<PostBean>();
		else if(postList.size() >= curPage * pageSize)
			return postList.subList((curPage - 1) * pageSize, curPage * pageSize);
		buildPostList(vocationId, postList);
		if(postList.size() >= curPage * pageSize)
			return postList.subList((curPage - 1) * pageSize, curPage * pageSize);
		if(postList.size() % pageSize > 0 && curPage > postList.size() / pageSize + 1)
			return null;
		return postList.subList(postList.size() - (postList.size() % pageSize), postList.size());
	}

	/**
	 * 获取展示详细页列表
	 * @param postDetailUrl
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Throwable
	 */
	public PostDetailBean getPostDetail(String postDetailUrl, int curPage, int pageSize) throws Throwable{
		List<String> postPicList = postDetailMap.get(postDetailUrl);
		if(postPicList == null)
			postPicList = buildPostDetailList(postDetailUrl, pageSize);
		if(postPicList.size() == 0)
			return null;
		if(postPicList.size() >= curPage * pageSize)
			return new PostDetailBean(Util.postDetailCountMap.get(postDetailUrl), postPicList.subList((curPage - 1) * pageSize, curPage * pageSize));
		if(postPicList.size() % pageSize > 0 && curPage > postPicList.size() / pageSize + 1)
			return null;
		return new PostDetailBean(Util.postDetailCountMap.get(postDetailUrl), postPicList.subList(postPicList.size() - (postPicList.size() % pageSize), postPicList.size()));
	}
	
	/**
	 * 登录
	 * @throws Throwable
	 */
	public void login() throws Throwable{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(USERNAMEKEY, USERNAME));
		params.add(new BasicNameValuePair(PASSWORDKEY, PASSWORD));
		HttpClientUtil.getInstance().post(MOKO_LOGIN_ACTION, params);
	}
	
	public List<MonthPostBean> getMonthPostList(){
		getMonthPostUrl();
		return null;
	}
	
}
