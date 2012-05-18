package com.mokoclient.core;

import java.util.List;

import com.mokoclient.core.bean.MonthPostBean;
import com.mokoclient.core.bean.PostBean;
import com.mokoclient.core.bean.PostDetailBean;
import com.mokoclient.util.Util;

public enum MokoClient {
	
	MOVIES{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_MOVIES_ID, curPage, pageSize);
		}
	},
	ACTOR{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_ACTOR_ID, curPage, pageSize);
		}
	},
	MUSIC{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_MUSIC_ID, curPage, pageSize);
		}
	},
	PHOTOGRAPHY{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_PHOTOGRAPHY_ID, curPage, pageSize);
		}
	},
	MODEL{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_MODEL_ID, curPage, pageSize);
		}
	},
	ARTS{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_ARTS_ID, curPage, pageSize);
		}
	},
	DESIGN{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_DESIGN_ID, curPage, pageSize);
		}
	},
	ADS{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_ADS_ID, curPage, pageSize);
		}
	},
	MORE{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.VOCATION_MORE_ID, curPage, pageSize);
		}
	},
	POST{
		@Override
		public List<PostBean> getPostList(int curPage, int pageSize) throws Throwable {
			return Util.getInstance().getPostList(Util.POSTLIST_ID, curPage, pageSize);
		}
	};
	
	/**
	 * 获取展示列表
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Throwable
	 */
	public abstract List<PostBean> getPostList(int curPage, int pageSize) throws Throwable;
	
	/**
	 * 获取展示详细页列表
	 * @param postDetailUrl
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Throwable
	 */
	public PostDetailBean getPostDetail(String postDetailUrl, int curPage, int pageSize) throws Throwable{
		return Util.getInstance().getPostDetail(postDetailUrl, curPage, pageSize);
	}

	/**
	 * 登录
	 * @author liuy
	 */
	public static class Login{
		public Login() throws Throwable{
			Util.getInstance().login();
		}
	}
	
	/**
	 * 月度最佳
	 * @author liuyang
	 */
	public static class MonthPost{
		private static final MonthPost mp = new MonthPost();
		private MonthPost(){}
		public static MonthPost getInstance(){
			return mp;
		}
		
		/**
		 * 获取月度最佳列表
		 * @return
		 */
		public List<MonthPostBean> getMonthPostList(){
			return Util.getInstance().getMonthPostList();
		}
		
		/**
		 * 获取月度最佳列表
		 * @return
		 */
		public List<PostBean> getPostList(String url){
			return null;
		}
	}
}
