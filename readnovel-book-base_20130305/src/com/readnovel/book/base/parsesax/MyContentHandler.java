//package com.readnovel.book.base.parsesax;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//import android.util.Log;
//
//import com.readnovel.book.base.entity.Chapter;
//import com.readnovel.book.base.entity.ChapterPageNum;
//import com.readnovel.book.base.entity.Version;
//public class MyContentHandler extends DefaultHandler {
//
//	private List<Chapter> booklist;
//	private List<Version> versionlist;
//	private String preTAG;
//	private Chapter chapter;
//	private Version version;
//	private ChapterPageNum chapterPageNum;
//	private List<ChapterPageNum> chapterPageNumList;
//	
//	public List<Chapter> getChapterlist() {
//		return booklist;
//	}
//
//	public List<ChapterPageNum> getChapterPageNumList() {
//		return chapterPageNumList;
//	}
//
//	public List<Version> getVersionlist() {
//		return versionlist;
//	}
//	
//	@Override
//	public void startDocument() throws SAXException {
//		booklist = new ArrayList<Chapter>();
//		versionlist = new ArrayList<Version>();
//		chapterPageNumList=new ArrayList<ChapterPageNum>();
//	}
//
//	@Override
//	public void startElement(String uri, String localName, String qName,
//			Attributes attributes) throws SAXException {
//		if ("chapter".equals(localName)) {
//			chapter = new Chapter();
//			chapterPageNum=new ChapterPageNum();
//		} else if ("verinfo".equals(localName)) {
//			version = new Version();
//		}
//		preTAG = localName;
//	}
//
//	@Override
//	public void characters(char[] ch, int start, int length)
//			throws SAXException {
//		if (preTAG != null) {
//			String data = new String(ch, start, length);
//
//			
//			if ("title".equals(preTAG)) {
//				chapter.setChapterTitle(data);
//			    
//			}
//
//			if ("file_name".equals(preTAG)) {
//				chapter.setChapterFileName(data);
//				chapterPageNum.setBookName(data);
//			}
//			if ("version".equals(preTAG)) {
//				version.setVersion(data);
//			}
//			if ("subver".equals(preTAG)) {
//				version.setSubVersion(data);
//			} else if ("appname".equals(preTAG)) {
//				version.setAppname(data);
//			}else if("num".equals(preTAG)){
//				chapterPageNum.setNumber(data);
//			}
//		}
//	}
//
//	@Override
//	public void endElement(String uri, String localName, String qName)
//			throws SAXException {
//		if ("chapter".equals(localName)) {
//			booklist.add(chapter);
//			chapterPageNumList.add(chapterPageNum);
//			chapter = null;
//		} else if ("verinfo".equals(localName)) {
//			versionlist.add(version);
//			version = null;
//		}
//
//		preTAG = null;
//	}
//
//}
