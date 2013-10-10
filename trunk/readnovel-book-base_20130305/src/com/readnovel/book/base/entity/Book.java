package com.readnovel.book.base.entity;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
	private static final long serialVersionUID =-2805284943658356093L;

	private int id;
	private String name;
	private String intro;
	private String author;
	private List<Chapter> chapters;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIntro() {
		return intro;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}


	public Chapter getByChapterId(int id) {
		for (Chapter chapter : chapters) {
			if (chapter.getId() == id)
				return chapter;
		}

		return null;
	}

}
