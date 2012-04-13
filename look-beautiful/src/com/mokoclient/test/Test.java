package com.mokoclient.test;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostBean;

public class Test {
	
	public static void main(String[] args) throws Throwable {
		new MokoClient.Login();
		for(PostBean post : MokoClient.MOVIES.getPostList(1)){
			System.out.println("Title: " + post.getTitle());
			System.out.println("Cover: " + post.getCoverUrl());
			System.out.println("Detail: " + post.getDetailUrl());
			for(String pic : MokoClient.MOVIES.getPostDetail(post.getDetailUrl()))
				System.out.println("    pic: " + pic);
		}
	}

}
