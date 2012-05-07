package com.mokoclient.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mokoclient.core.bean.PostBean;
import com.mokoclient.core.bean.PostDetailBean;
import com.mokoclient.util.HttpClientUtil;
import com.mokoclient.util.Util;

public enum MokoClient {
	
	MOVIES{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_MOVIES_ID, curPage, pageSize);
		}
	},
	ACTOR{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_ACTOR_ID, curPage, pageSize);
		}
	},
	MUSIC{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_MUSIC_ID, curPage, pageSize);
		}
	},
	PHOTOGRAPHY{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_PHOTOGRAPHY_ID, curPage, pageSize);
		}
	},
	MODEL{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_MODEL_ID, curPage, pageSize);
		}
	},
	ARTS{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_ARTS_ID, curPage, pageSize);
		}
	},
	DESIGN{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_DESIGN_ID, curPage, pageSize);
		}
	},
	ADS{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_ADS_ID, curPage, pageSize);
		}
	},
	MORE{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return super.getPostList(Util.VOCATION_MORE_ID, curPage, pageSize);
		}
	};
	
	private static Map<Integer, List<PostBean>> postListMap = new HashMap<Integer, List<PostBean>>(9);//这个map会一直增大,会出问题吗?~~~
	private static Map<String, List<String>> postDetailMap = new HashMap<String, List<String>>();//这个map会一直增大,会出问题吗?~~~
	private static Map<String, Integer> postDetailCountMap = new HashMap<String, Integer>();//这个map会一直增大,会出问题吗?~~~
	
	public abstract List<PostBean> getPostList(int curPage, int pageSize) throws Throwable;
	
	public PostDetailBean getPostDetail(String postDetailUrl, int curPage, int pageSize) throws Throwable{
		List<String> postPicList = postDetailMap.get(postDetailUrl);
		if(postPicList == null){
			postPicList = new ArrayList<String>();
			String html = HttpClientUtil.getInstance().get(postDetailUrl);
			Document doc = Jsoup.parse(html);
			Elements elements = doc.select("p.picBox img");
			for(Element postPic : elements)
				postPicList.add(postPic.attr("src2"));
			postDetailMap.put(postDetailUrl, postPicList);
			postDetailCountMap.put(postDetailUrl, postPicList.size() % pageSize > 0 ? postPicList.size() / pageSize + 1 : postPicList.size() / pageSize);
		}
		if(postPicList.size() == 0)
			return null;
		if(postPicList.size() >= curPage * pageSize)
			return new PostDetailBean(postDetailCountMap.get(postDetailUrl), postPicList.subList((curPage - 1) * pageSize, curPage * pageSize));
		if(postPicList.size() % pageSize > 0 && curPage > postPicList.size() / pageSize + 1)
			return null;
		return new PostDetailBean(postDetailCountMap.get(postDetailUrl), postPicList.subList(postPicList.size() - (postPicList.size() % pageSize), postPicList.size()));
	}

	private List<PostBean> getPostList(int vocationId, int curPage, int pageSize) throws Throwable{
		List<PostBean> postList = postListMap.get(vocationId);
		if(postList != null && postList.size() == 0)
			return postList;
		if(postList == null)
			postList = new ArrayList<PostBean>();
		else if(postList.size() >= curPage * pageSize)
			return postList.subList((curPage - 1) * pageSize, curPage * pageSize);

		int postCurPage = vocationPageCounter.get(vocationId);
		String html = getVocationHtml(vocationId, postCurPage);
		vocationPageCounter.put(vocationId, ++ postCurPage) ;
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("ul.post.small-post");
		for(Element post : elements){
			Element cover = post.select(".cover").get(0);
			String title = cover.attr("cover-text");
			String coverUrl = cover.select("img").get(0).attr("src2");
			String detailUrl = Util.MOKO_DOMAIN + cover.select("a").get(0).attr("href");
			PostBean postBean = new PostBean(title, coverUrl, detailUrl);
			postList.add(postBean);
		}
		postListMap.put(vocationId, postList);

		if(postList.size() >= curPage * pageSize)
			return postList.subList((curPage - 1) * pageSize, curPage * pageSize);
		if(postList.size() % pageSize > 0 && curPage > postList.size() / pageSize + 1)
			return null;
		return postList.subList(postList.size() - (postList.size() % pageSize), postList.size());
	}

	private String getVocationHtml(int vocationId, int curPage) throws Throwable{
		String url = Util.getVocationUrl(vocationId, curPage);
		return HttpClientUtil.getInstance().get(url);
	}
	
	public static class Login{
		public Login() throws Throwable{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Util.USERNAMEKEY, Util.USERNAME));
			params.add(new BasicNameValuePair(Util.PASSWORDKEY, Util.PASSWORD));
			HttpClientUtil.getInstance().post(Util.MOKO_LOGIN_ACTION, params);
		}
	}
	
	private static Map<Integer, Integer> vocationPageCounter;
	static{
		vocationPageCounter = new HashMap<Integer, Integer>(9);
		vocationPageCounter.put(Util.VOCATION_ACTOR_ID, 1);
		vocationPageCounter.put(Util.VOCATION_ADS_ID, 1);
		vocationPageCounter.put(Util.VOCATION_ARTS_ID, 1);
		vocationPageCounter.put(Util.VOCATION_DESIGN_ID, 1);
		vocationPageCounter.put(Util.VOCATION_MODEL_ID, 1);
		vocationPageCounter.put(Util.VOCATION_MORE_ID, 1);
		vocationPageCounter.put(Util.VOCATION_MOVIES_ID, 1);
		vocationPageCounter.put(Util.VOCATION_MUSIC_ID, 1);
		vocationPageCounter.put(Util.VOCATION_PHOTOGRAPHY_ID, 1);
	}
}
