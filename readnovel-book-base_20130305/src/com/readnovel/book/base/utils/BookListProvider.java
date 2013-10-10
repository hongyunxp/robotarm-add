package com.readnovel.book.base.utils;

import android.content.Context;

import com.readnovel.book.base.entity.Book;

public class BookListProvider {
	private static volatile BookListProvider instance;
	private static volatile Book book;

	private BookListProvider(Context ctx) {
		book = CommonUtils.getBook(ctx);
	}

	public static BookListProvider getInstance(Context ctx) {
		if (instance == null) { //double check
			synchronized (BookListProvider.class) {
				if (instance == null) {
					instance = new BookListProvider(ctx);
				}
			}
		}
		return instance;
	}

	public Book getBook() {
		return book;
	}
}
