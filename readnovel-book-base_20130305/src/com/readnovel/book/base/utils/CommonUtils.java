package com.readnovel.book.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.dom.DOMAttribute;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.readnovel.base.alipay.BaseHelper;
import com.readnovel.book.base.PageFlipActivity;
import com.readnovel.book.base.PayMsgAndZhifubaoActivity;
import com.readnovel.book.base.R;
import com.readnovel.book.base.bean.PayRecord;
import com.readnovel.book.base.bean.VipPayInterval;
import com.readnovel.book.base.common.Constants;
import com.readnovel.book.base.db.table.PayRecordTable;
import com.readnovel.book.base.db.table.VipPayIntervalRecord;
import com.readnovel.book.base.entity.Book;
import com.readnovel.book.base.entity.Chapter;
import com.readnovel.book.base.entity.PayCheckResult;
import com.readnovel.book.base.http.HttpProvider;
import com.readnovel.book.base.http.HttpResult;
import com.readnovel.book.base.sp.StyleSaveUtil;

public class CommonUtils {
	private static final String DEF_FENGMIAN = "640.0*960.0";//默认封面图片分辨率
	private static final String TAG = CommonUtils.class.getName();
	public static StyleSaveUtil util;// 保存若干信息的对象

	///////////////////////////////支付宝方法/////////////////////////////////////////////////

	public static String getOutTradeNo(Context ctx) {
		return BaseHelper.uuid();
	}

	public static String getPayToken(Context ctx, String outtradeno) {
		String key = md5(outtradeno + Constants.MSG_SECURE_KEY);
		return key.substring(0, 10);
	}

	////////////////////////////////////////////////////////////////////////////

	// 存储文件的目录
	public static String setMkdir(Context context) {
		String filePath = CommonUtils.getAssetsPath(context);
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		return filePath;
	}

	// 写文件
	public static void writeFile(final Context context) {
		int chaptertotalnum = BookInfoUtils.getChapterNum(context);
		String forechapter = BookInfoUtils.getChapterPrefix(context);
		String res;
		InputStream input = null;
		PrintWriter out = null;
		for (int i = 1; i <= chaptertotalnum; i++) {
			try {
				StringBuffer sb = new StringBuffer();
				String name = forechapter + i + ".txt";
				input = context.getAssets().open(name);
				BufferedReader in = new BufferedReader(new InputStreamReader(input));
				while ((res = in.readLine()) != null) {
					sb.append(res + "\n\n");
				}

				String filename = CommonUtils.setMkdir(context);
				File f = new File(filename, name);

				if (!f.exists()) {
					out = new PrintWriter(f);
					out.write(sb.toString());
					sb = null;
				}

			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			} finally {
				try {
					if (input != null)
						input.close();
					if (out != null)
						out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static String getAssetsPath(Context context) {
		String filePath = context.getFilesDir().getPath() + File.separator + "singlebook03";

		return filePath;
	}

	// 结束所有Activity
	public static void finishActs(List<Activity> acts) {
		if (acts == null || acts.isEmpty())
			return;

		for (Activity act : acts) {
			if (!act.isFinishing())
				act.finish();
		}
		killProcess();
	}

	// 结束掉当前程序进程
	public static void killProcess() {
		// 保证完全退出，或System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID，
	}

	// 解析XML显示目录信息
	public static Book getBook(Context context) {
		// 解析XML显示目录信息
		AssetManager asset = context.getAssets();
		InputStream input = null;
		Book book = null;
		try {
			input = asset.open("chapterInfo");
			// 转移到sdcard 
			if (input != null) {
				//加速初始化
				book = ObjectUtils.read(input);
			} else {
				input = asset.open("chapterInfo.xml");
				book = parseToBook(input);
			}
			return book;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
		}

		return null;
	}

	// 关闭Cursor
	public static void closeCursor(Cursor c) {
		try {
			if (c != null && c.isClosed() != true) {
				c.close();
				c = null;
			}
		} catch (Exception e) {
			Log.e("xs", "SingleBook onDestroy:" + e.toString());
		}
	}

	// 获得当前的时间
	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String time = year + "-" + month + "-" + day;
		return time;
	}

	public static Book parseToBook(InputStream in) {

		try {
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(in);

			Book book = new Book();
			//设置book属性
			Attribute bookIdAttr = (Attribute) doc.selectSingleNode("//book/@id");
			book.setId(Integer.parseInt(bookIdAttr.getValue()));
			Element title = (Element) doc.selectSingleNode("//book//title");
			book.setName(title.getText());
			Element intro = (Element) doc.selectSingleNode("//book//description");
			book.setIntro(intro.getText());
			Element author = (Element) doc.selectSingleNode("//book//author");
			book.setAuthor(author.getText());

			List<Attribute> idAttrs = (List<Attribute>) doc.selectNodes("//book//chapter/@id");
			List<Integer> ids = new ArrayList<Integer>(idAttrs.size());//章节ids
			for (Attribute idAttr : idAttrs) {
				ids.add(Integer.parseInt(idAttr.getValue()));
			}

			List<Element> ctitleAttrs = (List<Element>) doc.selectNodes("//book//chapter//title");
			List<String> titles = new ArrayList<String>(ctitleAttrs.size());//章节title
			for (Element ctitleEle : ctitleAttrs) {
				titles.add(ctitleEle.getTextTrim());
			}

			List<Element> file_nameAttrs = (List<Element>) doc.selectNodes("//book//chapter//file_name");
			List<String> fileNames = new ArrayList<String>(file_nameAttrs.size());//章节位置
			for (Element file_nameEle : file_nameAttrs) {
				fileNames.add(file_nameEle.getTextTrim());
			}

			List<Element> numAttrs = (List<Element>) doc.selectNodes("//book//chapter//num");
			List<String> nums = new ArrayList<String>(numAttrs.size());//章节字数
			for (Element numEle : numAttrs) {
				nums.add(numEle.getTextTrim());
			}

			List<Chapter> chapters = new ArrayList<Chapter>(ctitleAttrs.size());
			List<Element> chapterEleList = (List<Element>) doc.selectNodes("//book//chapter");
			List<Integer> isVips = new ArrayList<Integer>(idAttrs.size());//vip
			for (Element chapter : chapterEleList) {//取到是否为vip章节
				Attribute isVipAttr = chapter.attribute(1);//isVip属性
				if (isVipAttr == null)
					isVips.add(0);
				else {
					String isVip = isVipAttr.getText();
					if (isVip != null && "1".equals(isVip))
						isVips.add(1);
					else
						isVips.add(0);
				}
			}

			for (int i = 0; i < ctitleAttrs.size(); i++) {
				Chapter chapter = new Chapter();
				chapter.setId(ids.get(i));
				chapter.setTitle(titles.get(i));
				chapter.setFileName(fileNames.get(i));
				chapter.setNum(nums.get(i));
				chapter.setVip(isVips.get(i) == 1 ? true : false);

				chapters.add(chapter);
			}
			book.setChapters(chapters);//设置book章节
			return book;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
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

	//得到每单的字数
	public static List<Integer> getNums(String dirName) {
		File file = new File(dirName);//取文件
		String[] fileNames = file.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.contains("book");
			}
		});

		List<String> files = Arrays.asList(fileNames);
		Collections.sort(files, new Comparator<String>() {//由小到大排序

					@Override
					public int compare(String o1, String o2) {
						int i = Integer.parseInt(o1.substring(o1.indexOf("_") + 1, o1.length() - 4));
						int j = Integer.parseInt(o2.substring(o2.indexOf("_") + 1, o2.length() - 4));
						return i < j ? -1 : (i > j ? 1 : 0);
					}
				});

		final List<Integer> nums = new ArrayList<Integer>(files.size());
		int num = 0;

		for (String fileName : files) {
			StringBuilder sb = new StringBuilder();
			try {

				InputStream fis = new FileInputStream(new File(dirName + fileName));
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));
				String res = null;
				while ((res = in.readLine()) != null) {
					sb.append(res + "\n\n");
				}
				num += sb.length();
				nums.add(num);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return nums;
	}

	//更新xml
	public static void update(String title, String intro, String antro, String appName, List<Integer> nums, String fileName)
			throws DocumentException, IOException {
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new File(fileName));

		Element book = (Element) doc.selectSingleNode("//book");

		//加标题
		QName titleName = new QName("title");
		Attribute titleAttr = new DOMAttribute(titleName, title);
		if (book.attribute(titleName) == null) {
			book.add(titleAttr);
		}

		//加介绍
		QName introName = new QName("intro");
		Attribute introAttr = new DOMAttribute(new QName("intro"), intro);
		if (book.attribute(introName) == null) {
			book.add(introAttr);
		}

		//加作者
		QName authorName = new QName("author");
		Attribute authorAttr = new DOMAttribute(new QName("author"), antro);
		if (book.attribute(authorName) == null) {
			book.add(authorAttr);
		}

		//加程序名
		QName app = new QName("app_name");
		Attribute appNameAttr = new DOMAttribute(new QName("app_name"), appName);
		if (book.attribute(app) == null) {
			book.add(appNameAttr);
		}

		List<Element> chapters = (List<Element>) doc.selectNodes("//book//chapter");

		if (chapters.size() != nums.size()) {
			System.out.println("非法！~");
			return;//非法
		}

		for (int i = 0; i < chapters.size(); i++) {
			QName numName = new QName("num");

			if (chapters.get(i).element(numName) == null) {
				Element numEle = new DOMElement(numName);
				numEle.setText(String.valueOf(nums.get(i)));
				chapters.get(i).add(numEle);//加字数
			}

		}

		//设置格式化输出
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");//设置编码  
		format.setExpandEmptyElements(true);
		format.setNewLineAfterDeclaration(true);
		format.setNewLineAfterNTags(2);

		//写文件 
		XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File(fileName)), format);
		xmlWriter.write(doc);
		xmlWriter.close();
	}

	/**
	 * 获得对字符串进行MD5加密后的结果字符串
	 */
	private static String getMD5(String value) {
		if (value == null || "".equals(value))
			return null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes("UTF-8"));
			return toHexString(md.digest());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获得指定byte[]对象中的所有byte值的16进制形式的结果字符串
	 */
	private static String toHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Character.forDigit((bytes[i] & 0XF0) >> 4, 16));
			sb.append(Character.forDigit(bytes[i] & 0X0F, 16));
		}
		return sb.toString();
	}

	/**
	 * 判断是否为默认封面
	 * @param ctx
	 * @return
	 */
//	public static boolean isDefFengMian(Context ctx) {
//
//		float[] resolution = getResolution(ctx, R.drawable.cover);
//
//		if (resolution == null)
//			return false;
//
//		String res = resolution[0] + "*" + resolution[1];
//
//		return DEF_FENGMIAN.equals(res);
//	}

	/**
	 * 得到某一资源图片的分辨率
	 * @param ctx
	 * @param resource
	 * @return
	 */
	public static float[] getResolution(Context ctx, int resource) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		//通过这个bitmap获取图片真实的宽和高     
		BitmapFactory.decodeResource(ctx.getResources(), resource, options);

		float[] resolution = { options.outWidth, options.outHeight };

		return resolution;
	}

	/**
	 * 发短信
	 * @param ctx
	 * @param receiver
	 * @param content
	 */
	public static void sendMsg(Context ctx, String receiver, String content) {
		//先判断有没有手机卡
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		int telstate = tm.getSimState();
		if (telstate == 1 || telstate == 0) {
			Toast.makeText(ctx, "您没有手机卡", Toast.LENGTH_SHORT).show();
		} else {
			LogUtils.info("短信内容|" + content);
			SmsManager smsManager = SmsManager.getDefault();
			PendingIntent sentIntent = PendingIntent.getBroadcast(ctx, 0, new Intent(), 0);
			//如果字数超过70,需拆分成多条短信发送
			if (content.length() > 70) {
				List<String> msgs = smsManager.divideMessage(content);
				for (String msg : msgs) {
					smsManager.sendTextMessage(receiver, null, msg, sentIntent, null);
				}
			} else
				smsManager.sendTextMessage(receiver, null, content, sentIntent, null);

			Toast.makeText(ctx, "短信发送完成", Toast.LENGTH_LONG).show();
		}
	}

	public static void confirm(final Context context, String title, String msg, OnClickListener pl, OnClickListener nl) {
		//		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		//		tDialog.setTitle(title);
		//		tDialog.setMessage(msg);
		//		tDialog.setPositiveButton("确定", pl);
		//		tDialog.setNegativeButton("取消", nl);
		//		tDialog.show();
		Dialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(msg).

		setPositiveButton("确定", pl).setNegativeButton("取消", nl).create();
		alertDialog.show();
	}

	public static String[] chapterScope(Context ctx, int chapterId) {
		VipPayIntervalRecord vpir = new VipPayIntervalRecord(ctx);
		VipPayInterval vpi = vpir.getByChapterId(chapterId);
		String chapterIds = vpi.getBookIds();
		String[] chapters = chapterIds.split(",");
		String first = chapters[0];
		String end = chapters[chapters.length - 1];

		return new String[] { first, end };
	}

	public static String getDeviceId(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

		return tm.getDeviceId();
	}

	/**
	 * md5加密
	 * @param s
	 * @return
	 */
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			LogUtils.error(e.getMessage(), e);
		}

		return "";
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getPayToken(Context ctx) {
		String key = md5(getImei(ctx) + Constants.MSG_SECURE_KEY);

		return key.substring(0, 10);
	}

	public static String getImei(Context ctx) {
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

		return telephonyManager.getDeviceId();
	}

	/**
	 * 订购验证
	 * {"code":"1"} 为成功
	 * @param ctx
	 * @return
	 */
	public static PayCheckResult payCheck(Context ctx) {
		HttpProvider http = HttpProvider.newInstance();
		PayCheckResult pcr = new PayCheckResult();
		pcr.setCode(String.valueOf(Constants.CODE_PAY_CHECK_ERROR));

		try {
			String imei = CommonUtils.getImei(ctx);
			String token = CommonUtils.getPayToken(ctx);
			String url = String.format(Constants.CHECK_URL, new Object[] { imei, token });
			LogUtils.info("验证地址：" + url);
			HttpResult result = http.get(url, null, null, null);

			String content = result.httpEntityContent();
			JSONObject retJsonObject = new JSONObject(content);

			if (!retJsonObject.isNull("code"))
				pcr.setCode(retJsonObject.getString("code"));

			if (!retJsonObject.isNull("fee"))
				pcr.setFee((float) retJsonObject.getDouble("fee"));

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			http.shutdown();
		}

		return pcr;//其它错误,如500,404,无正常的服务器端
	}

	public static String getChannel(Activity act) {

		try {
			ApplicationInfo info = act.getPackageManager().getApplicationInfo(act.getPackageName(), PackageManager.GET_META_DATA);
			String channel = info.metaData.getString("UMENG_CHANNEL");

			return channel;

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static boolean goToChannelRight(Activity act, int chapterId) {

		if (BookInfoUtils.isFree(act))
			return false;

		Chapter chapter = BookInfoUtils.getByChapterId(act, chapterId);

		return false;

	}

	public static boolean goToPay(Activity act, Chapter chapter, boolean skip) {
		if (chapter.isVip()) {
			// 再判断一下是否是已经付费了。
			util = new StyleSaveUtil(act);
			// 根据区间进行付费的判断
			if (chapter.isVip()) {
				VipPayIntervalRecord vpir = new VipPayIntervalRecord(act);
				VipPayInterval vpi = vpir.getByChapterId(chapter.getId());

				if (VipPayInterval.UN_ORDER == vpi.getState()) {//未订购
					// 直接跳到付费界面
					Intent intent = new Intent(act, PayMsgAndZhifubaoActivity.class);
					intent.putExtra("chapterID", chapter.getId());
					act.startActivity(intent);
					return true;
				}
			}
			//			if (util.getpay()) {
			//				return false;
			//			} else {
			//				// 跳转到付费界面
			//				Intent pay = new Intent(act, PayMsgAndZhifubaoActivity.class);
			//				pay.putExtra("chapterID", chapter.getId());
			//				act.startActivity(pay);
			//				return true;
			//			}
		}
		return false;
	}

	/**
	 * 当前章节是否显示广告
	 * @return
	 */
	public static boolean isShowAd(Context ctx, Chapter chapter) {
		if (CommonUtils.isChannelRight(ctx)) {//当为特权渠道VIP显示广告,免费不显示广告
			if (chapter.isVip())
				return true;
			else
				return false;
		}
		if (chapter.isVip()) {
			PayRecordTable prt = new PayRecordTable(ctx);
			VipPayIntervalRecord vpir = new VipPayIntervalRecord(ctx);
			List<PayRecord> prs = prt.getAllDesc();
			VipPayInterval vpi = vpir.getByChapterId(chapter.getId());
			if (VipPayInterval.PAY_SUCCESS == vpi.getState()) //当前订购成功状态不显示广告
				return false;
			if (VipPayInterval.PAY_FAIL == vpi.getState()) //当前订购失败状态显示广告
				return true;
			else if (prs != null && prs.size() > 1) {//上次订购失败显示广告
				for (PayRecord pr : prs) {
					VipPayInterval pre = vpir.getByChapterId(pr.getChapterId());

					if (VipPayInterval.PAY_SUCCESS == pre.getState())
						break;

					if (VipPayInterval.PAY_FAIL == pre.getState())
						return true;
				}
			}

		}

		return false;//默认不显示广告
	}

	/**
	 * 判断并决定是否跳转到VIP付费逻辑
	 * @param act
	 * @param chapterId
	 * @return
	 */
	public static boolean goToPay(Activity act, int chapterId) {
		Chapter chapter = BookInfoUtils.getByChapterId(act, chapterId);
		return goToPay(act, chapter, true);
	}

	/**
	 * 阅读页面
	 * @param ctx
	 * @param chapterFileName
	 * @param chapterName
	 * @param chapterId
	 */
	public static void goToFlipRead(Context ctx, String chapterFileName, String chapterName, int chapterId) {
		Intent intent = new Intent(ctx, PageFlipActivity.class);
		Bundle bundle = new Bundle();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		bundle.putString("filename", chapterFileName);
		bundle.putString("chaptername", chapterName);
		bundle.putInt("chapterId", chapterId);
		intent.putExtras(bundle);
		ctx.startActivity(intent);
	}

	public static void downApp(Activity act) {
		Uri uri = Uri.parse(Constants.DOWN_APP_URL);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		act.startActivity(intent);
	}

	public static ColorStateList color(Context ctx, int color) {
		return (ColorStateList) ctx.getResources().getColorStateList(color);
	}

	/**
	 * 判断当前是否是特权渠道
	 * @param ctx
	 * @return
	 */
	public static boolean isChannelRight(Context ctx) {
		//当前渠道名
		String curChannelName = getChannel(ctx);
		//所有渠道特权
		String[] channelsRight = ctx.getResources().getStringArray(R.array.channel_right);

		for (String channelRight : channelsRight) {
			if (curChannelName.equals(channelRight))
				return true;
		}

		return false;
	}

	public static String getChannel(Context ctx) {

		try {
			ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			String channel = info.metaData.getString("UMENG_CHANNEL");

			return channel;

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

}
