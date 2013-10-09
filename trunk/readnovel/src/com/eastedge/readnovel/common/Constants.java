package com.eastedge.readnovel.common;

import java.util.Arrays;
import java.util.List;

import android.os.Environment;

import com.readnovel.base.common.CommonApp;
import com.readnovel.base.util.DateUtils;
import com.xs.cn.R;

public class Constants {
	private static final String SMQHE_LIMAGE = "http://opendata.smqhe.com/thumbnails/thumb_1110019.jpg";
	// ////////////////////////////////// sHh45Yjm78Ktwk62
	public static final String PRIVATE_KEY = "sHh45Ywm78Ktpn62";
	public static final String ALIPAY_CHANNEL_NAME = "alipay";

	// 服务地址
	public static final String URL = "http://opendata.readnovel.com";
	public static final String imgURL = URL + "/web/images/client/";
	public static final String READNOVEL_LOG = Environment
			.getExternalStorageDirectory() + "/readnovel_error.log";
	// 本地SD卡存储目录
	public static final String READNOVEL_DATA_ROOT = Environment
			.getExternalStorageDirectory() + "/readnovel";
	public static final String READNOVEL_BOOK = Environment
			.getExternalStorageDirectory() + "/readnovel/downBook";// 下载的书目录
	public static final String READNOVEL_CACHE = CommonApp.getInstance()
			.getCacheDir().getAbsolutePath()
			+ "/readnovel/readCache";// 在线阅读书缓存目录（手机存储缓存-在线阅读）
	public static final String READNOVEL_IMGCACHE = Environment
			.getExternalStorageDirectory() + "/readnovel/imgCache";// 图片缓存绝对位置
	public static final String READNOVEL_IMGCACHE_ABS = "/readnovel/imgCache/";// 图片缓存相对位置
	public static final String READNOVEL_DownML = Environment
			.getExternalStorageDirectory() + "/readnovel/dlmulu";// 书目录(包括在线阅读书和书架的书)
	public static final String READNOVEL_VIEW_DATA_CACHE_ABS = "/readnovel/viewdataCache/";// 视图数据缓存相对位置

	public static final String SMS_NO = "12114";
	public static final String SMS_KEY = "mnpRycrMXHM49fch";

	public static String CUSTOM_PRIVATE_KEY = "RHh78Ywm66Ktpn32";

	// 普通网络请求图片网络超时
	public static final int COMMON_CONNECT_TIMEOUT = 30 * 1000;// 连接超时
	public static final int COMMON_SO_TIMEOUT = 60 * 1000;// 读取超时

	// 阅读器背景颜色数组
	public static final List<Integer> READ_BG_LIST = Arrays.asList(
			R.drawable.readbg13, R.drawable.readbg23, R.drawable.readbg33,
			R.drawable.readbg43, R.drawable.readbg4); // 背景id 的集合

	// 图片缓存名
	public static final String IMG_CACHE_KEY_CURPAGE = "image_cache_key_curpage";
	public static final String IMG_CACHE_KEY_NEXTPAGE = "image_cache_key_nextpage";
	public static final String IMG_CACHE_KEY_PAGEWIDGET_BG = "image_cache_key_pagewidget_bg_%s";
	public static final String IMG_CACHE_KEY_PAGEFACTORY_BG = "image_cache_key_pagefactory_bg_%s";
	// public static final String IMG_CACHE_KEY_TEMP_BG =
	// "image_cache_key_pagefactory_temp_bg";

	// 充值最小金额,包括支付宝app和充值卡
	public static final double CONSUME_PAY_MIN = 20;

	// SD卡空间不足提醒
	public static final int FIRST_LEAST_SIZE = 1;
	public static final int SECOND_LEAST_SIZE = 3;
	public static final int THIRD_LEAST_SIZE = 5;

	/**
	 * *************************************************************************
	 * ************ 网络请求URL
	 * *****************************************************
	 * ********************************
	 */
	// 特别推荐(推荐一级页面) /web/store.php?a=sp_rec_new&ct=android&v=1&data=json
	// public static final String TUIJIAN_URL = URL +
	// "/web/store.php?a=sp_rec&ct=android&v=1&data=json";
	public static final String TUIJIAN_URL = URL
			+ "/web/store.php?a=sp_rec_new&ct=android&v=1&data=json";
	// 折扣书列表
	public static final String DISCOUNT_URL = URL
			+ "/web/store.php?a=discount_book&sort=%s&page=%s";
	// 专题列表显示(推荐二级页面)
	public static final String THEME_URL = URL
			+ "/web/store.php?a=album&aid=%s&ct=android&v=1&data=json&page=%s";
	// 排行榜(排行一级页面)
	public static final String PAIHANG_URL = URL
			+ "/web/store.php?a=top_list&ct=android&v=1&data=json";
	// 各类排行榜接口(排行二级页面)
	public static final String PAIHANG_ITEM_URL = URL
			+ "/web/store.php?a=top&top_id=%s&ct=android&v=1&data=json";
	// 新书列表
	public static final String NEW_URL = URL
			+ "/web/store.php?a=new&sortid=%s&ct=android&v=1&data=json&page=%s";
	// 分类列表(分类一级页面)
	public static final String SORT_LIST_URL = URL
			+ "/web/store.php?a=sort_list&ch_type=xx&ct=android&v=1&data=json";
	// 分类单项列表(分类二级/三级页面)
	public static final String SORT_URL = URL
			+ "/web/store.php?a=sort&sortid=%s&ct=android&v=1&data=json&page=%s";
	// 搜索热词
	public static final String TAG_WORD_URL = URL
			+ "/web/store.php?a=tag_words&ct=android&v=1&data=json";
	// 搜索关键字
	public static final String SEARCH_URL = URL
			+ "/web/store.php?a=search&keyword=%s&ct=android&v=1&data=json&page=%s";
	// 书本信息页(书本明细)
	public static final String ONE_BOOK_URL = URL
			+ "/web/book.php?a=one_book&articleid=%s&data=json";
	// 软件更新提醒
	public static final String UPDATE_INFO_URL = URL
			+ "/web/ver.php?a=android&appid=a01&data=json";
	// 注册
	public static final String REG_URL = URL
			+ "/web/user.php?a=reg&username=%s&passwdhash=%s&ct=android&v=1&data=json&auth=%s";
	// 登陆
	public static final String LOGIN_URL = URL
			+ "/web/user.php?a=login&username=%s&passwdhash=%s&ct=android&v=1&data=json";
	// 修改密码
	public static final String CH_PASSWD_URL = URL
			+ "/web/user.php?a=ch_passwd&uid=%s&oldpasswdhash=%s&newpass=%s&ct=android&v=1&data=json&auth=%s&t=%s&token=%s";
	// 更新提醒
	public static final String BOOK_UP_INFO_URL = URL
			+ "/web/book.php?a=up_info&booklist=%s&data=json&ct=android";
	// 得到手机绑定号码(20121121)
	public static final String GET_BIND_MOBILE_URL2 = URL
			+ "/web/user.php?a=is_bind_user&uid=%s&k=%s";
	// 与主站收藏同步
	public static final String SYNCHRO_MY_FAVOR_URL = URL
			+ "/web/book.php?a=synchro_myfavor&uid=%s&token=%s&ct=android&data=json&add_ids=%s&del_ids=%s";
	// 关于我们QQ电话接口
	public static final String HELP_INFO_URL = URL
			+ "/web/client.php?a=help_info";
	// 设置版本更新
	public static final String GET_NEWEST_VERSION = URL
			+ "/web/ver.php?a=get_newest_version&appid=a03&data=json";
	// public static final String GET_NEWEST_VERSION =
	// "http://10.228.8.200:8080/json";//升级测试地址
	// //目录
	// public static final String ARTICLE_ID_URL = URL +
	// "/web/book.php?a=index_info&articleid=%s&page=%s&size=50&data=json";
	// 所有目录
	public static final String INDEX_INFO_ALL_URL = URL
			+ "/web/book.php?a=index_info_all&articleid=%s&data=json&ct=android";
	// 同步资料
	public static final String INFO_URL = URL
			+ "/web/user.php?a=info&uid=%s&token=%s&ct=android&data=json";

	// 同步资料(支付宝)
	public static final String INFO_ALIPAY_URL = URL
			+ "/web/user.php?a=alipay_get_user_info&token=%s&access_token=%s&userid=%s";

	// 普通章节在线阅读
	public static final String TEXT_URL = URL
			+ "/web/book.php?a=text&textid=%s&ct=android&data=json";

	/**
	 * VIP在线阅读，订阅，订阅全部确认，订阅全部为免费阅读VIP章节控制接口
	 * 
	 * 其中model在三星定制版中为手机的型号，在普通版本为手机的渠道
	 */
	// VIP在线阅读
	public static final String VIP_TEXT_URL = URL
			+ "/web/book.php?a=vip_text&textid=%s&ct=android&data=json&uid=%s&token=%s&srcid=%s&model=%s&imei=%s";
	// 单章订阅
	public static final String SUB_TEXT_URL = URL
			+ "/web/book.php?a=sub_text&textid=%s&uid=%s&ct=android&data=json&token=%s&auth=%s&t=%s&srcid=%s&srcauth=%s&model=%s&imei=%s";
	// 单章订阅全部确认（订阅全部章节返回信息明细）
	public static final String TEXT_ALL_URL = URL
			+ "/web/book.php?a=text_all&articleid=%s&ct=android&data=json&uid=%s&token=%s&srcid=%s&model=%s&imei=%s";
	// 订阅全部
	public static final String SUB_ALL_URL = URL
			+ "/web/book.php?a=sub_all&articleid=%s&uid=%s&ct=android&data=json&token=%s&auth=%s&t=%s&srcid=%s&srcauth=%s&model=%s&imei=%s";
	// 订阅全部折扣书
	public static final String ALL_DISCOUNT_URL = URL
			+ "/web/book.php?a=go_sub_discount_book&articleid=%s&uid=%s&ct=android&data=json&token=%s&t=%s&auth=%s";
	// 确认订阅全部折扣书
	public static final String SUB_ALL_DISCOUNT_URL = URL
			+ "/web/book.php?a=sub_discount_book&articleid=%s&uid=%s&ct=android&data=json&token=%s&t=%s&auth=%s";

	// 多章订阅
	public static final String SUB_BATCH_TEXT_URL = URL
			+ "/web/book.php?a=sub_texts&textid=%s&uid=%s&ct=android&data=json&token=%s&t=%s&auth=%s&number=%s";

	// 手机包月状态
	public static final String MONTH_URL = URL
			+ "/web/user.php?a=month&ct=android&data=json"
			+ "&ct=android&data=json&uid=%s&token=%s";
	// 修改资料
	public static final String UPDATE_USER_INFO = URL
			+ "/web/user.php?a=update_user_info&uid=%s&token=%s&tel=%s&email=%s&ct=android&data=json";
	// 书评论
	public static final String SHUPING_URL = URL
			+ "/web/book.php?a=shuping&articleid=%s&ct=android&data=json&page=%s&size=5";
	// 书评回复
	public static final String SHUPING_DETAIL_URL = URL
			+ "/web/book.php?a=shuping_detail&articleid=%s&tid=%s&ct=android&data=json&page=%s";
	// 书架同步数据接口
	public static final String MY_FAVOR_URL = URL
			+ "/web/book.php?a=myfavor&uid=%s&token=%s&ct=android&data=json";
	// 我的vip同步接口
	public static final String MY_VIP_URL = URL
			+ "/web/book.php?a=myvip&uid=%s&token=%s&data=json&page=1&ct=android";
	// 已订阅章节信息
	public static final String GET_SUBED_CHAPTERS_INFO_URL = URL
			+ "/web/book.php?a=get_subed_chapters_info&articleid=%s&uid=%s&data=json&ct=android&token=%s";
	// 新用户注册、老用户登录发送机器信息
	public static final String FEEDBACK_URL = URL
			+ "/web/user.php?a=feedback&uid=%s&token=%s&type=%s";
	// 安装成功发送机器信息
	public static final String INSTALLED_INFO_URL = URL
			+ "/web/user.php?a=installed&uid=0&token=%s";
	// 预读章节
	public static final String PREPARE_VIP_TEXT = URL
			+ "/web/book.php?a=prepare_vip_text&textid=%s&ct=android&data=json&token=%s";
	// 下载书
	public static final String BOOK_DOWNLOAD = URL
			+ "/web/book.php?a=download&articleid=%s&data=json&ct=android";
	// //更新书
	// public static final String BOOK_UP_DOWNLOAD = URL +
	// "/web/book.php?a=up_download&articleid=%s&textid=%s&data=json&ct=android";
	// 公告 id:渠道id;
	public static final String NOTICE_CHECK_URL = URL
			+ "/web/store.php?a=client_notice&id=%s&model=%s&imei=%s&versioncode=%s&uid=%s";

	// 支付宝app支付充值
	public static final String ALIPAY_APP_URL = URL
			+ "/web/alipay.php?a=alipay_pay&uid=%s&total_fee=%s&gid=1";
	// 支付宝钱包app支付充值
	public static final String ALIPAY_APP_WALLET_URL = URL
			+ "/web/alipay.php?a=alipay_qianbao_pay&uid=%s&total_fee=%s&gid=1&extern_token=%s";
	// 支付宝wap支付充值
	public static final String ALIPAY_WAP_URL = URL
			+ "/web/alipay.php?a=alipay_pay_wap&uid=%s&fee=%s";
	// 消息中心
	public static final String MSG_CENTER_URL = URL
			+ "/web/store.php?a=quick_center&uid=%s&token=%s";
	// 用户中心
	public static final String USER_CENTER_URL = URL
			+ "/web/user.php?a=index&uid=%s&token=%s&access_token=%s";
	// 我的VIP订阅
	public static final String MY_SUB_VIP_URL = URL
			+ "/web/user.php?a=mysub_vip&uid=%s&token=%s";
	// 我的信息
	public static final String MY_INFO_URL = URL
			+ "/web/user.php?a=user&uid=%s&token=%s";
	// 绑定手机
	public static final String BIND_MOBILE_URL = URL
			+ "/web/user.php?a=user_bind_mobile&uid=%s&token=%s";
	// 支付宝钱包APP充值（网页）
	public static final String ALIPAY_APP_WEP_URL = URL
			+ "/web/user.php?a=go_app_alipay_pay&ct=android";
	// 支付宝钱包长token换短token
	public static final String ALIPAY_APP_WALLET_REFRESH_TOKEN_URL = URL
			+ "/web/user.php?a=refesh_token&ct=android&token=%s&refesh_token=%s";

	// 网银wap支付之信用卡支付
	public static final String WY_XXK_WAP_URL = URL
			+ "/web/alipay.php?a=alipay_pay_wap&channelType=credit_card&&uid=%s&fee=%s";
	// 网银wap支付之信用卡支付
	public static final String WY_CCK_WAP_URL = URL
			+ "/web/alipay.php?a=alipay_pay_wap&channelType=debit_card&&uid=%s&fee=%s";

	// 神舟付支付充值
	public static final String CZK_URL = URL
			+ "/web/shenzhoufu.php?a=shenzhoufu_pay";
	// Q币支付充值
	public static final String CONSUME_QQ_URL = URL
			+ "/web/yeepay.php?a=qb_pay";

	// QQ登陆 code值1成功2其它
	public static final String QQ_LOGIN_URL = URL
			+ "/web/user.php?a=qq_login&access_token=%s&token=%s&ct=android&v=1&data=json";
	// sina登陆 code值1成功2其它
	public static final String SINA_LOGIN_URL = URL
			+ "/web/user.php?a=weibo_login&access_token=%s&token=%s&ct=android&v=1&data=json";
	// 支付宝登陆 code值1成功2其它
	public static final String ALIPAY_LOGIN_URL = URL
			+ "/web/user.php?a=alipay_login&ct=android&alipay_userid=%s&userid=%s&token=%s&auth_code=%s";
	// APP推荐
	public static final String SETTING_APP_URL = URL
			+ "/web/show.php?a=app_partner_rec";

	/**
	 * ************************************************ 包月新接口
	 * ************************************************
	 */
	// 包月申请
	public static final String PAY_MONTH_PRE_ORDER = URL
			+ "/web/user.php?a=go_app_bind_mobile&uid=%s&token=%s";
	// 包月预订购
	public static final String PAY_MONTH_ORDER = URL
			+ "/web/user.php?a=sub_app_bind_mobile&uid=%s&token=%s&mobile=%s";

	// QQ APP_ID
	public static final String QQ_APP_ID = "100278192";// QQ登陆、分享使用;
														// 和小说阅读网网站使用一个appid
	// sina APP_ID
	public static final String CONSUMER_KEY = "3670619661";// 替换为开发者的appkey，例如"1646212860";
	public static final String REDIRECT_URL = "http://event.readnovel.com/android_app/";
	// 客户端分享图片
	public static final String OPEN_LOGIN_SHARE_IMG = "http://event.readnovel.com/app-images/weibo.jpg";
	public static final String OPEN_SHARE_URL_LOCATION = "http://event.readnovel.com/wap/download.html";
	public static final String OPEN_SHARE_URL_NAME = "http://event.readnovel.com/wap/download.html";
	public static final String OPEN_SINA_ADD_FRIEND_NAME = "小说阅读网官方微博";
	public static final long OPEN_SINA_ADD_FRIEND_ID = 3247350900l;// 新浪登陆关注的人,客户端3247350900；主站1719101244

	// 登陆类型
	public static String DEFAULT_TYPE = "default_type";
	public static String QQ_LOGIN_TYPE = "qq_type";
	public static String SINA_LOGIN_TYPE = "sina_type";
	public static final int MAX_SHARE_TEXT_SIZE = 100;
	public static final long SHARE_AFTER_LOGIN_TIME = DateUtils.DAY * 180;// 开放平台登陆分享时间间隔

	// 送红包页面
	public static final String SUPPORT_AUTHOR_PAGE_URL = URL
			+ "/web/user.php?a=go_app_send_bonues&title_type=%s";

	// 送红包
	public static final String SUPPORT_AUTHOR_URL = URL
			+ "/web/user.php?a=sub_send_bonus&ct=android&token=%s&userid=%s&from_channel=%s&articleid=%s&bonus=%s";

	/**
	 * 登陆类型
	 */
	public static enum LoginType {
		def(0), // 默认登陆
		qq(1), // QQ登陆
		sina(2), // 新浪登陆
		alipay(3), // 支付宝登陆
		;

		private int value;

		private LoginType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static LoginType getByValue(int value) {
			for (LoginType loginType : LoginType.values()) {
				if (loginType.getValue() == value)
					return loginType;
			}

			return LoginType.def;
		}
	}

}
