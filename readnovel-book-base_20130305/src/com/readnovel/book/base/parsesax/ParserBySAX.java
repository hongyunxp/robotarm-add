//package com.readnovel.book.base.parsesax;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import java.util.List;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//import org.xml.sax.SAXException;
//
//import android.util.Log;
//
//import com.readnovel.book.base.entity.Chapter;
//import com.readnovel.book.base.entity.ChapterPageNum;
//import com.readnovel.book.base.entity.Version;
//
//public class ParserBySAX {
////	public static List<Chapter> parseXML(InputStream input) {
////		try {
////			SAXParserFactory factory = SAXParserFactory.newInstance();
////			SAXParser parser = factory.newSAXParser();
////			MyContentHandler handler = new MyContentHandler();
////			parser.parse(input, handler);
////			return handler.getChapterlist();
////
////		} catch (ParserConfigurationException e) {
////			e.printStackTrace();
////		} catch (SAXException e) {
////			e.printStackTrace();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////
////		return null;
////	}
//
////	public static List<Version> parseXMLVersion(InputStream input) {
////		try {
////			SAXParserFactory factory = SAXParserFactory.newInstance();
////			SAXParser parser = factory.newSAXParser();
////			MyContentHandler handler = new MyContentHandler();
////
////			parser.parse(input, handler);
////			input.close();
////			return handler.getVersionlist();
////
////		} catch (ParserConfigurationException e) {
////			e.printStackTrace();
////		} catch (SAXException e) {
////			e.printStackTrace();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////
////		return null;
////	}
//
////	public static List<ChapterPageNum> parseXMLChapterNum(InputStream input) {
////		try {
////			SAXParserFactory factory = SAXParserFactory.newInstance();
////			SAXParser parser = factory.newSAXParser();
////			MyContentHandler handler = new MyContentHandler();
////
////			parser.parse(input, handler);
////			input.close();
////			return handler.getChapterPageNumList();
////
////		} catch (ParserConfigurationException e) {
////			e.printStackTrace();
////		} catch (SAXException e) {
////			e.printStackTrace();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////
////		return null;
////	}
//}
