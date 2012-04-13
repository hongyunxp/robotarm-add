package com.mokoclient.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mokoclient.core.bean.PostBean;
import com.mokoclient.util.HttpClientUtil;
import com.mokoclient.util.Util;

public enum MokoClient {
	
	MOVIES{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_MOVIES_ID, curPage);
		}
	},
	ACTOR{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_ACTOR_ID, curPage);
		}
	},
	MUSIC{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_MUSIC_ID, curPage);
		}
	},
	PHOTOGRAPHY{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_PHOTOGRAPHY_ID, curPage);
		}
	},
	MODEL{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_MODEL_ID, curPage);
		}
	},
	ARTS{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_ARTS_ID, curPage);
		}
	},
	DESIGN{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_DESIGN_ID, curPage);
		}
	},
	ADS{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_ADS_ID, curPage);
		}
	},
	MORE{
		@Override
		public List<PostBean> getPostList(int curPage) throws Throwable {
			return super.getPostList(Util.VOCATION_MORE_ID, curPage);
		}
	};
	
	public abstract List<PostBean> getPostList(int curPage) throws Throwable;
	
	public List<String> getPostDetail(String postDetailUrl) throws Throwable{
		List<String> postPicList = new ArrayList<String>();
		String html = HttpClientUtil.getInstance().get(postDetailUrl);
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("p.picBox img");
		for(Element postPic : elements)
			postPicList.add(postPic.attr("src2"));
		return postPicList;
	}
	
	private List<PostBean> getPostList(int VocationId, int curPage) throws Throwable{
		String html = getVocationHtml(VocationId, curPage);
		List<PostBean> postList = new ArrayList<PostBean>();
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
		return postList;
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
}
