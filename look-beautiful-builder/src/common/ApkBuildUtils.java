package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ApkBuildUtils {
	private static final SimpleDateFormat format = new SimpleDateFormat("MMdd");

	/** 
	 * 执行build.xml文件 
	 * @param build  build.xml文件 
	 * @param level  日志输出级别(Project.MSG_INFO) 
	 * */
	public static boolean doBuild(String build, int level) {
		Project p = new Project();

		try {
			File buildFile = new File(build);
			//添加日志输出          
			DefaultLogger consoleLogger = new DefaultLogger();
			consoleLogger.setErrorPrintStream(System.err);
			consoleLogger.setOutputPrintStream(System.out);
			//输出信息级别  
			consoleLogger.setMessageOutputLevel(level);
			p.addBuildListener(consoleLogger);

			p.fireBuildStarted();
			p.init();
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			helper.parse(p, buildFile);
			p.executeTarget(p.getDefaultTarget());
			p.fireBuildFinished(null);

			return true;
		} catch (Throwable e) {
			p.fireBuildFinished(e);
		}

		return false;
	}

	public static String getVersionCode(String fileName) {
		InputStream in = null;

		try {
			File file = new File(fileName);
			in = new FileInputStream(file);
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(in);
			Attribute attr = (Attribute) doc.selectSingleNode("//manifest/@android:versionCode");
			String versionCode = attr.getValue();

			return versionCode;

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return null;
	}

	public static String getVersionName(String fileName) {
		InputStream in = null;

		try {
			File file = new File(fileName);
			in = new FileInputStream(file);
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(in);
			Attribute attr = (Attribute) doc.selectSingleNode("//manifest/@android:versionName");
			String versionName = attr.getValue();

			return versionName;

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return null;
	}

	public static String getToday() {
		return format.format(new Date());
	}

	//更改AndroidManifest.xml渠道
	public static boolean changeChannel(String fileName, String channelName) {
		InputStream in = null;
		XMLWriter xmlWriter = null;

		try {
			File file = new File(fileName);
			in = new FileInputStream(file);
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(in);
			Attribute attr = (Attribute) doc.selectSingleNode("//manifest//application//meta-data/@android:value");
			attr.setValue(channelName);

			//写文�?
			//			OutputFormat of = OutputFormat.createPrettyPrint();

			OutputStream out = new FileOutputStream(fileName);
			Writer writer = new OutputStreamWriter(out, "UTF-8");
			xmlWriter = new XMLWriter(writer);

			xmlWriter.write(doc);
			xmlWriter.close();

			return true;
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

			if (xmlWriter != null)
				try {
					xmlWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}

		return false;

	}

	/**
	 * 文件拷贝
	 * @param from
	 * @param to
	 */
	public static void copy(String from, String to) {
		try {
			//移动apk包到指定目录
			File fromFile = new File(from);
			File toFile = new File(to);

			if (toFile.exists())
				toFile.delete();

			File toParentFile = toFile.getParentFile();
			if (!toParentFile.exists())
				toParentFile.mkdirs();

			InputStream input = new FileInputStream(fromFile);
			OutputStream output = new FileOutputStream(toFile);
			IOUtils.copy(input, output);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 目录拷贝
	 * @param src
	 * @param dest
	 */
	public static void fileCopy(String from, String to) {
		File file = new File(from);

		if (!file.exists()) {
			System.out.println(from + "  Not Exists. ");
			return;
		}

		File fileb = new File(to);

		if (file.isFile()) {

			copy(from, to);

		} else if (file.isDirectory()) {

			if (!fileb.exists()) {
				fileb.mkdir();
			}

			String[] fileList = file.list();

			for (String f : fileList) {
				fileCopy(from + File.separator + f, to + File.separator + f);
			}
		}

	}

	/**
	 * 删除目录
	 * @param f
	 */
	public static void delDir(File f) {

		if (f.isFile())
			f.delete();
		else
			for (File file : f.listFiles()) {
				delDir(file);
			}
	}

	/**
	 * 更改程序�?
	 */
	public static boolean changeAppName(String fileName, String appName) {

		return change(fileName, "//resources//string[1]", appName);
	}

	public static boolean change(String fileName, String xPath, String value) {
		InputStream in = null;
		XMLWriter xmlWriter = null;

		try {
			File file = new File(fileName);
			in = new FileInputStream(file);
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(in);

			Node node = doc.selectSingleNode(xPath);

			if (node instanceof Attribute) {
				Attribute attr = (Attribute) node;
				attr.setValue(value);
			} else {
				node.setText(value);
			}

			//写文�?
			xmlWriter = new XMLWriter(new FileWriter(new File(fileName)));
			xmlWriter.write(doc);
			xmlWriter.close();

			return true;
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

			if (xmlWriter != null)
				try {
					xmlWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}

		return false;
	}

	public static void main(String[] args) {
		//		String name = "E:\\work\\AndroidWorkSpace2\\android-builder\\test.txt";
		//		File file = new File(name);
		//		System.out.println(file.getParent());
		//		String n = "E:\\work\\AndroidWorkSpace2\\book-26340\\AndroidManifest.xml";
		//		String r = getVersionName(n);
		//		System.out.println(r);
		System.out.println(getToday());
	}
}
