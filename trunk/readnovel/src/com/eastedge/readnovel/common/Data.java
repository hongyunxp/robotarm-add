package com.eastedge.readnovel.common;

import java.util.ArrayList;
import java.util.Random;

import com.eastedge.readnovel.beans.Book;

public class Data {

	public static ArrayList<Book> getNewBookList(){
		ArrayList<Book> al=new ArrayList<Book>();
		
		for (int i = 1; i <=10; i++) {
			Book  book=new Book();
			book.setBookId("bk00"+i);
			book.setBookName("测试书本"+i);
			book.setBookEditer("张三"+i);
			book.setBookType("类型"+i);
			Random random=new Random();
			book.setBookPoint(random.nextInt(10000000));
			al.add(book);
		}
		
		return al;
	}
	
	
	public static ArrayList<String> getPaihangList(){
		ArrayList<String> al=new ArrayList<String>();
		
		for (int i = 1; i <=20; i++) {
			String str="测试"+i+"TOP  100";
			al.add(str);
		}
		
		return al;
	}
	
}
