package com.readnovel.book.base.common;

import java.util.Arrays;
import java.util.List;

import android.os.Environment;

import com.readnovel.book.base.R;
import com.readnovel.book.base.utils.DateUtils;

public class Constants {
	public static final String MORE_DANBEN_BOOK_URL = "http://opendata.readnovel.com/web/book.php?a=rec_bookinfo_one_hundred&datatype=json&page=%s";//更多合集
	public static final String MORE_HEJI_BOOK_URL = "http://opendata.readnovel.com/web/book.php?a=more_heji&page=%s";//更多单本
	public static final String DOWN_APP_URL = "http://a.readnovel.com/article.php?a=download_app";
	public static String PAY_NUMBER = "1066666688";//订购号码
	public static String PAY_CEN_PREFIX = "10033";//订购内容前缀
	public static String PAY_SPLIT = "@";
	public static String READNOVEL_IMGCACHE = Environment.getExternalStorageDirectory() + "/readnovel/imgCache";
	//图片加载网络超时
	public static final int IMG_CONNECT_TIMEOUT = 30 * 1000;
	public static final int IMG_SO_TIMEOUT = 30 * 1000;

	//	public static long PAY_INPROGRESS_TIME = DateUtils.SECOND * 10;//订购中状态持续时间30秒
	//	public static long PAY_CHECK_DELAY_TIME = DateUtils.SECOND * 60;//验证时间1小时
	//	public static int PAY_SMG_TIME = 6;//每天短信次数限制

	public static long PAY_INPROGRESS_TIME = DateUtils.SECOND * 30;//订购中状态持续时间30秒
	public static long PAY_CHECK_DELAY_TIME = DateUtils.HOUR * 1;//验证时间1小时
	//	public static long PAY_CHECK_DELAY_TIME =DateUtils.SECOND * 90;
	public static int PAY_SMG_TIME = 3;//每天短信次数限制
	public static final int SECTION_SIZE = 40;//vip区间大小
	public static long PAY_CHECK_DELAY_TEN_MINUTE_TIME = DateUtils.MINUTE * 2;//2分钟验证一次
	public static final List<Integer> READ_BG_LIST = Arrays.asList(R.drawable.readbg0, R.drawable.readbg1, R.drawable.readbg2, R.drawable.readbg3,
			R.drawable.readbg4, R.drawable.readbg4); // 背景id 的集合

	//验证状态
	public static final int CODE_PAY_CHECK_SUCCESS = 1;//成功
	public static final int CODE_PAY_CHECK_FAIL = 2;//失败
	public static final int CODE_PAY_CHECK_ERROR = 0;//错误
	public static final int yanzhengjiange_onehour = 1; // 1小时后进行验证
	public static final int yanzhengjiange_tenminute = 2; // 每隔10分钟去验证一次 
	public static final int yanzhengjiange_rightnow = 3; // 每隔10分钟去验证一次 

	//书类型
	public static final String BOOK_TYPE_GIRL = "1";//女频
	public static final String BOOK_TYPE_BOY = "2";//男频
	public static final String BOOK_TYPE_SCHOOL = "3";//校园

	public static String MSG_SECURE_KEY = "sHh4SYwm7BKtpn6Z";
	public static final String URL = "http://opendata.readnovel.com";
	public static final String CHECK_URL = URL + "/web/book.php?a=single_book_paid_sure&imei=%s&token=%s";//章节区间付费验证
	//	public static final String CHECK_TEST_URL = "http://10.228.8.200:8080/book_check";//测试使用 - 章节区间付费验证
	public static final String DEFAULT_BOOK_AD_URL = URL + "/web/app/singleBook/readnovel/%s.apk";

	//友盟统计事件
	public static final String UMENG_EVENT_INSTALLED = "installed";//安装量
	public static final String UMENG_EVENT_CLICK_MORE = "click_more";//点击更多
	public static final String UMENG_EVENT_CLICK_DOWN_BOOK = "click_down_book";//点击下载书
	public static final String UMENG_EVENT_CLICK_DOWN_READNOVEL = "click_down_readnovel";//点击下载客户端
	public static final String INSTALL_FOR_CHANNEL = "install_for_channel_%s";//根据渠道统计安装量事件前缀
	public static final String UMENG_EVENT_BOOK_LAUNCH = "book_launch"; // 启动次数
}
