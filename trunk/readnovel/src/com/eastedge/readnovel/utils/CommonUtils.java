package com.eastedge.readnovel.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.AliPayCallParams;
import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.ConstantEvents;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.AutoOrderTable;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.fragment.MainGroupFragment;
import com.eastedge.readnovel.task.OpenLoginTask;
import com.eastedge.readnovel.task.QQShareTask;
import com.eastedge.readnovel.task.SDCleanTask;
import com.eastedge.readnovel.task.SinaShareTask;
import com.eastedge.readnovel.task.SupportAuthorPageTask;
import com.eastedge.readnovel.threads.SendLogRegInfo;
import com.readnovel.base.cache.viewdata.JSONObjectSDCache;
import com.readnovel.base.common.CommonApp;
import com.readnovel.base.common.NetType;
import com.readnovel.base.openapi.LoginListener;
import com.readnovel.base.openapi.QZoneAble;
import com.readnovel.base.openapi.SinaAPI;
import com.readnovel.base.openapi.TencentAPI;
import com.readnovel.base.util.EventLogUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.NetUtils;
import com.readnovel.base.util.PhoneUtils;
import com.readnovel.base.util.ResourceUtils;
import com.readnovel.base.util.StorageUtils;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;
import com.xs.cn.R;
import com.xs.cn.activitys.BindMobileActivity;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.ConsumeOnlyZfb;
import com.xs.cn.activitys.ConsumeZfb;
import com.xs.cn.activitys.LoginActivity;
import com.xs.cn.activitys.MainActivity;
import com.xs.cn.activitys.MyInfoActivity;
import com.xs.cn.activitys.MySubVipActivity;
import com.xs.cn.activitys.ReadBookLastActivity;
import com.xs.cn.activitys.RegistActivity;
import com.xs.cn.activitys.SettingActivity;
import com.xs.cn.activitys.SettingAppActivity;
import com.xs.cn.activitys.UserInfoActivity;
import com.xs.cn.http.DownFile;

public class CommonUtils {
	private static WakeLock wl;

	/**
	 * 是否需要加载业务图片
	 * @param netType
	 * @return true不需要，false需要
	 */
	public static boolean isNoNeedPic(NetType netType) {
		return NetType.TYPE_2G.equals(netType);
	}

	/**
	 * 更新书后清缓存
	 * @param curBook 当前章节
	 * @param lastBook 更新前的目录
	 */
	public static void delCacheAfterUpbook(BFBook curBook) {
		//更新成功后清除掉相应书的相应缓存(清除书的明细页面缓存,vip最后章节缓存)
		if (curBook != null) {
			String onBookUrl = String.format(Constants.ONE_BOOK_URL, new Object[] { curBook.getArticleid() });
			JSONObjectSDCache.getInstance(Constants.READNOVEL_VIEW_DATA_CACHE_ABS).delCache(onBookUrl);
		}

	}

	/**
	 * 得到当前渠道logo资源
	 * @param ctx
	 * @return 当前渠道logo资源，0不存在合作
	 */
	public static int getChannelPartnerDrawable(Context ctx) {
		//当前渠道名
		String curChannelName = getChannel(ctx);
		//所有合作渠道
		String[] cps = ctx.getResources().getStringArray(R.array.channel_partner);

		//返回当前合作渠道logo 
		for (String cp : cps) {
			if (cp.equals(curChannelName))
				return ResourceUtils.getDrawableResource(ctx, "welcome_" + curChannelName);
		}

		return 0;//不存在合作
	}

	/**
	 * 是否显示广告
	 *  vip用级大于0，包月用户不显示广告
	 * @return  显示广告true,不显示false,默认不显示
	 */
	public static boolean isShowAd() {

		NetType netType = NetUtils.checkNet(CommonApp.getInstance());
		if (NetType.TYPE_NONE.equals(netType))//无网络不显示广告
			return false;

		User user = BookApp.getUser();
		if (user != null && (user.getVipLevel() > 0 || user.isBaoYue()))//vip用级大于0，包月用户不显示广告
			return false;

		return true;
	}

	/**
	 * 加入书架
	 * @param ctx
	 * @param articleId
	 * @param title
	 * @param iconUrl
	 */
	public static boolean addBookToBF(Activity act, String articleId, String title, String iconUrl, int finishflag) {
		DBAdapter dbAdapter = null;

		try {
			dbAdapter = new DBAdapter(act);
			dbAdapter.open();
			if (!dbAdapter.exitBookBF1(articleId)) {
				BFBook book = new BFBook();
				book.setArticleid(articleId);
				//图片和标题
				book.setImgFile(Util.saveImgFile(act, iconUrl));
				book.setTitle(title);
				book.setFinishFlag(finishflag);
				dbAdapter.insertBook(book);

				// 添加到关系表
				if (!dbAdapter.exitBookGx(articleId, "-1")) {
					dbAdapter.insertGx(articleId, "-1", 0);
				}

				ViewUtils.toastOnUI(act, "加入书架成功", Toast.LENGTH_SHORT);
				return true;

			} else {
				ViewUtils.toastOnUI(act, "已加入书架", Toast.LENGTH_SHORT);
				return false;
			}

		} finally {
			if (dbAdapter != null)
				dbAdapter.close();
		}

	}

	/**
	 * 加入书架并下载
	 * @param act
	 * @param articleId
	 * @param title
	 * @param iconUrl
	 * @param finishflag
	 */
	public static void addBookAndDown(Activity act, String articleId, String title, String iconUrl, int finishflag) {
		boolean isSuccess = addBookToBF(act, articleId, title, iconUrl, finishflag);

		if (isSuccess) {
			DownFile downFile = new DownFile(act, articleId, title);
			downFile.start();
			HCData.downingBook.put(articleId, downFile);
		}

	}

	/**
	 * 去设置中心
	 * @param act
	 */
	public static void goToSetting(Activity act) {
		Intent intent = new Intent(act, SettingActivity.class);
		act.startActivity(intent);
	}

	/**
	 * 去我的VIP订阅
	 * @param act
	 */
	public static void goMySubVip(Activity act) {
		Intent intent = new Intent(act, MySubVipActivity.class);
		act.startActivity(intent);
	}

	/**
	 * 去我的信息
	 * @param act
	 */
	public static void goMyInfo(Activity act) {
		Intent intent = new Intent(act, MyInfoActivity.class);
		act.startActivity(intent);
	}

	/**
	 * 去绑定手机号
	 * @param act
	 */
	public static void goBindMobile(Activity act) {
		Intent intent = new Intent(act, BindMobileActivity.class);
		act.startActivity(intent);
	}

	/**
	 * 去登陆
	 * @param act
	 */
	public static void goToLogin(Activity act) {
		Intent intent = new Intent(act, LoginActivity.class);
		act.startActivity(intent);
	}

	/**
	 * 去注册
	 * @param act
	 */
	public static void goToRegist(Activity act) {
		Intent intent = new Intent(act, RegistActivity.class);
		act.startActivity(intent);
	}

	/**
	 * 去消费页面
	 * @param act
	 */
	public static void goToConsume(Context ctx) {
		LoginType logInType = LocalStore.getLastLoginType(ctx);

		if (logInType != null && LoginType.alipay.equals(logInType))
			ctx.startActivity(new Intent(ctx, ConsumeOnlyZfb.class));
		else {
			Intent intent = new Intent(ctx, ConsumeZfb.class);
			intent.putExtra("tag", "zfb");
			ctx.startActivity(intent);
		}
	}

	/**
	 * 退出登陆
	 * @param act
	 */
	public static void logout(Context ctx) {
		BookApp.cleanUser();
		LocalStore.setStime(ctx, "");
	}

	/**
	 * 去我的资料
	 */
	public static void goToUserInfo(Activity act) {
		Intent intent = new Intent(act, UserInfoActivity.class);
		act.startActivity(intent);
	}

	/**
	 * 推荐应用
	 * @param view
	 */
	public static void goToApp(Activity act) {
		Intent intent = new Intent(act, SettingAppActivity.class);
		act.startActivity(intent);
	}

	/**
	 * 设置自动定阅
	 * @param act
	 * @param aId
	 */
	public static void addAutoOrder(Activity act, String aId) {
		AutoOrderTable autoOrderTable = new AutoOrderTable(act);
		autoOrderTable.open();
		autoOrderTable.insertAutoOrder(aId);
		autoOrderTable.close();
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

			Toast.makeText(ctx, "发送完成", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 绑定手机号
	 * @param view
	 */
	public static void bindPhone(final Context ctx) {

		ViewUtils.confirm(ctx, "温馨提示", ctx.getString(R.string.bind_phone_confirm_msg), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String uid = BookApp.getUser().getUid();
				String token = Util.md5(uid + Constants.SMS_KEY);
				token = token.substring(token.length() - 8, token.length());
				String content = "小说阅读#bd#" + uid + "#" + token;
				sendMsg(ctx, Constants.SMS_NO, content);
			}

		}, null);
	}

	/**
	 * 解除绑定
	 * @param ctx
	 */
	public static void unBindPhone(final Context ctx) {

		ViewUtils.confirm(ctx, "温馨提示", ctx.getString(R.string.unbind_phone_confirm_msg), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String content = "小说阅读#jb";
				sendMsg(ctx, Constants.SMS_NO, content);
			}

		}, null);
	}

	/**
	 * 注册
	 */
	public static void register(final Context ctx) {

		ViewUtils.confirm(ctx, "温馨提示", ctx.getString(R.string.reg_confirm), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String content = "小说阅读#zc#apk";
				sendMsg(ctx, Constants.SMS_NO, content);
			}

		}, null);

	}

	/**
	 * 找回密码
	 */
	public static void forgotPassword(final Context ctx) {

		ViewUtils.confirm(ctx, "温馨提示", ctx.getString(R.string.forget_password_confirm_msg), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String content = "小说阅读#mm";
				sendMsg(ctx, Constants.SMS_NO, content);
			}

		}, null);
	}

	/**
	 * 生成神舟付订单编号
	 * @return
	 */
	public static String createCardPayNo() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = format.format(new Date());

		StringBuilder sb = new StringBuilder(time);

		sb.append(new Random().nextInt(9));
		sb.append(new Random().nextInt(9));
		sb.append(new Random().nextInt(9));
		sb.append(new Random().nextInt(9));

		return sb.toString();
	}

	/**
	 * 记录登陆状态
	 */
	public static void saveLoginStatus(Context ctx, String uid, LoginType loginType) {
		//最后用户的登陆平台类型
		LocalStore.setLastLoginType(ctx, loginType);

		saveLoginStatus(ctx, uid);

		EventLogUtils.sendEventLog(ctx, ConstantEvents.LOGIN_TYPE, loginType.name());
	}

	/**
	 * 记录登陆状态
	 */
	public static void saveLoginStatus(Context ctx, String uid) {
		//最后登陆用户ID
		LocalStore.setLastUid(ctx, uid);

		Date date = new Date();
		date.setMonth(date.getMonth() + 1);
		String endTime = new SimpleDateFormat("yyyyMMdd").format(date);
		LocalStore.setStime(ctx, endTime);
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

	/**
	 * 得到定制版渠道名
	 * @param act
	 * @return
	 */
	public static String getCustomChannel(Context ctx) {

		try {
			String customChannel = CommonUtils.getMetaData(ctx, "UMENG_CHANNEL");

			return customChannel;

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static <T> T getMetaData(Context ctx, String key) {
		try {
			ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			@SuppressWarnings("unchecked")
			T metaData = (T) info.metaData.get(key);

			return metaData;

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 得到定制版auth
	 * @return
	 */
	public static String getCustomAuth(long curTime) {
		String curTimeMd5 = Util.md5(String.valueOf(curTime));
		String auth = Util.md5(Constants.CUSTOM_PRIVATE_KEY + curTimeMd5);

		return auth.substring(0, 10);

	}

	/**
	 * 强制预读章节重命名
	 * @param textid
	 * @param uid
	 * @param token
	 */
	public static void renameTempVip(String textid, String uid, String token) {

		String textToken = Util.md5(textid + Constants.PRIVATE_KEY).substring(0, 10);
		String fromUrl = String.format(Constants.PREPARE_VIP_TEXT, new Object[] { textid, textToken });
		String fromFileName = StorageUtils.convertUrlToFileName(fromUrl);//vip章节临时缓存名

		String channel = getCustomChannel(BookApp.getInstance());
		String model = getChannel(BookApp.getInstance());
		String imei = PhoneUtils.getPhoneImei(BookApp.getInstance());

		String toUrl = String.format(Constants.VIP_TEXT_URL, new Object[] { textid, uid, token, channel, model.toUpperCase(), imei });
		String toFileName = StorageUtils.convertUrlToFileName(toUrl);//vip章节缓存名

		JSONObjectSDCache.getInstance(Constants.READNOVEL_VIEW_DATA_CACHE_ABS).rename(fromFileName, toFileName);//重命名
	}

	/**
	 * 最后一页提示框
	 * @param str
	 */
	public static void dialogWaitUp(final Activity act, String msg) {
		new AlertDialog.Builder(act).setTitle("温馨提示").setMessage(msg).setPositiveButton("返回书城", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//跳转到书城
				Intent intent = new Intent(act, MainGroupFragment.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				act.startActivity(intent);
				act.finish();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}

	/**
	 * 去分享提示框
	 * @param str
	 */
	public static void dialogFinish(final Activity act, String msg, final String articleId, final String chapterName) {
		new AlertDialog.Builder(act).setTitle("温馨提示").setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//跳转到分享页面
				Shubenmulu mulu = Util.read(articleId);

				if (mulu == null)
					return;//目录为空不处理

				Intent intent = new Intent(act, ReadBookLastActivity.class);
				intent.putExtra("bookName", mulu.getTitle());
				intent.putExtra("chapterName", chapterName);
				intent.putExtra("bookId", articleId);
				act.startActivity(intent);
				act.finish();
			}
		}).show();
	}

	/**
	 * 阅读最后一页处理
	 */
	public static void doReadLastPage(Activity act, int finishFlag, String articleId, String chapterName) {
		if (finishFlag == 0) {
			new SupportAuthorPageTask(act, articleId, 2).execute();
			//			CommonUtils.dialogWaitUp(act, "作品已经到最后章节了，请等待作者更新！");
		} else if (finishFlag == 1) {
			new SupportAuthorPageTask(act, articleId, 1).execute();
			//			CommonUtils.dialogFinish(act, "本书已经完结，请去书城阅读更多精品热门小说。", articleId, chapterName);
		}
	}

	/**
	 * 点击QQ登陆
	 * @param view
	 */
	public static void loginForQQ(final QZoneAble act) {
		if (PhoneUtils.getVersionCode() < Build.VERSION_CODES.ECLAIR_MR1) {//小于2.1
			ViewUtils.showDialogOnUi(act, "温馨提示", "此功能仅支持安卓2.1及以上版本", null);
			return;
		}

		final TencentAPI tencentAPI = TencentAPI.getInstance(act, Constants.QQ_APP_ID);
		act.setTencentAPI(tencentAPI);
		tencentAPI.login(new LoginListener() {

			@Override
			public void onComplete(String accessToken) {
				if (StringUtils.isBlank(accessToken))
					return;

				String token = Util.md5(accessToken + Constants.PRIVATE_KEY).substring(0, 10);
				String loginUrl = String.format(Constants.QQ_LOGIN_URL, accessToken, token);

				//执行登陆
				new OpenLoginTask(act, loginUrl, accessToken, LoginType.qq).execute();

			}

		});

	}

	/**
	 * QQ分享
	 * @param act
	 * @param content
	 */
	public static void shareForQQ(QZoneAble act, String content, String bookId) {
		if (PhoneUtils.getVersionCode() < Build.VERSION_CODES.ECLAIR_MR1) {//小于2.1
			ViewUtils.showDialogOnUi(act, "温馨提示", "此功能仅支持安卓2.1及以上版本", null);
			return;
		}
		new QQShareTask(act, content, bookId).execute();
	}

	/**
	 * QQ分享
	 * @param act
	 * @param content
	 */
	public static void shareForQQLogin(QZoneAble act, String content, String imageUrl) {
		if (PhoneUtils.getVersionCode() < Build.VERSION_CODES.ECLAIR_MR1) {//小于2.1
			ViewUtils.showDialogOnUi(act, "温馨提示", "此功能仅支持安卓2.1及以上版本", null);
			return;
		}
		QQShareTask task = new QQShareTask(act, content);
		task.setImageUrl(imageUrl);

		task.execute();
	}

	/**
	 * 点击新浪登陆
	 * @param view
	 */
	public static void loginForSina(final QZoneAble act) {

		final SinaAPI sinaAPI = SinaAPI.getInstance(act, Constants.CONSUMER_KEY, Constants.REDIRECT_URL);

		sinaAPI.login(new LoginListener() {

			@Override
			public void onComplete(String accessToken) {

				if (StringUtils.isBlank(accessToken))
					return;

				String token = Util.md5(accessToken + Constants.PRIVATE_KEY).substring(0, 10);
				String loginUrl = String.format(Constants.SINA_LOGIN_URL, accessToken, token);

				//执行登陆
				new OpenLoginTask(act, loginUrl, accessToken, LoginType.sina).execute();

			}

		});

	}

	/**
	 * 新浪分享
	 * @param act
	 * @param content
	 */
	public static void shareForSina(final QZoneAble act, String content, String bookId) {

		new SinaShareTask(act, content, bookId).execute();

	}

	/**
	 * 新浪分享
	 * @param act
	 * @param content
	 */
	public static void shareForSinaLogin(final QZoneAble act, String content, String imageUrl) {
		SinaShareTask task = new SinaShareTask(act, content);
		task.setImageUrl(imageUrl);
		task.execute();

	}

	/**
	 * 新浪加好友
	 * @param act
	 */
	public static void addFriendForSina(final QZoneAble act, long uid, String name) {
		//登陆分享
		CommonUtils.shareForSinaLogin(act, act.getString(R.string.login_share_content), Constants.OPEN_LOGIN_SHARE_IMG);
		//登陆加好友
		SinaAPI sinaAPI = SinaAPI.getInstance(act, Constants.CONSUMER_KEY, Constants.REDIRECT_URL);
		sinaAPI.addFriend(uid, name, new RequestListener() {

			@Override
			public void onComplete(String response) {
				LogUtils.info("添加好友成功！");
			}

			@Override
			public void onIOException(IOException e) {
				LogUtils.info("添加好友失败！");
			}

			@Override
			public void onError(WeiboException e) {
				LogUtils.info("添加好友成失败！");
			}

		});
	}

	/**
	 * 生成登陆token
	 */
	public static String logInToken(String uid) {
		return Util.md5(uid + Constants.PRIVATE_KEY).substring(0, 10);
	}

	/**
	 * 开放登陆回调
	 * @param act
	 * @param result 登陆返回数据
	 */
	public static void openLoginCallBack(Activity act, User user, LoginType loginType) {

		try {
			//登陆成功
			if (user != null && User.LOGIN_SUCCESS.equals(user.getCode())) {

				//1. 登录成功后记录登陆状态
				CommonUtils.saveLoginStatus(act, user.getUid(), loginType);

				//2. 登录成功后缓存用户(在Session中)
				BookApp.setUser(user);

				//3. 判断是否是第1次启动，第一次进入书城，后面进入书架
				if (LocalStore.getfirstload(act).equals("1")) {
					Intent intent = new Intent(act, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					act.startActivity(intent);
				} else {
					Intent intent = new Intent(act, MainGroupFragment.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					act.startActivity(intent);
				}

				//4. 是否为老用户第一次登录
				if (LocalStore.getFirstLogin(act) == 0) {
					LocalStore.setFirstLogin(act, 1);
					if (user != null) {
						SendLogRegInfo sendInfo = new SendLogRegInfo(act, user.getUid(), user.getToken(), 2);
						sendInfo.start();
					}
				}

				//5. 登陆成功提示
				if (LoginType.qq.equals(loginType))
					Toast.makeText(act, "QQ登陆成功！", Toast.LENGTH_LONG).show();
				else if (LoginType.sina.equals(loginType))
					Toast.makeText(act, "新浪微博登陆成功！", Toast.LENGTH_LONG).show();
				else if (LoginType.alipay.equals(loginType))
					Toast.makeText(act, "您已使用支付宝账号登录，全方面支付保障，安全无忧", Toast.LENGTH_LONG).show();

			} else
				Toast.makeText(act, "登陆失败！", Toast.LENGTH_LONG).show();

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}
	}

	/**
	 * 控制分享按钮
	 * @param act
	 * @param shareQQ
	 * @param shareSina
	 */
	public static void controlShareButton(Activity act, View shareQQ, View shareSina) {
		/**
		 * 1. 当前为QQ登陆时，并且session未过期只显示QQ分享按钮
		 * 2. 当前为Sina登陆时，并且session未过期只显示sina分享按钮
		 * 3. 当都普通登陆时，qq分享和sina分享按钮都显示 
		 */
		if (TencentAPI.isSessionValid(act) && LocalStore.getLastLoginType(act).equals(LoginType.qq)) {//
			shareQQ.setVisibility(View.VISIBLE);
			shareSina.setVisibility(View.GONE);
		} else if (SinaAPI.isSessionValid(act) && LocalStore.getLastLoginType(act).equals(LoginType.sina)) {//
			shareSina.setVisibility(View.VISIBLE);
			shareQQ.setVisibility(View.GONE);
		}

		if (BookApp.getUser() == null) {//如果退出状态分享都显示
			shareSina.setVisibility(View.VISIBLE);
			shareQQ.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 手机存储卡检测
	 * @param act
	 * @return true可继续，false不可继续
	 */
	public static boolean sdCardCheck(final Activity act) {

		boolean cardIsAble = StorageUtils.externalMemoryAvailable();

		if (!cardIsAble) {//SD卡不可用
			ViewUtils.showDialogOnUi(act, "温馨提示", "您的手机无SD卡或SD卡不可用，请插入SD卡或确认其可用。", null);

			return false;//不可以继续
		}

		long sdcardSize = StorageUtils.getAvailableExternalMemorySize();

		if (Constants.FIRST_LEAST_SIZE * StorageUtils.MB >= sdcardSize) {
			ViewUtils.confirmOnUi(act, "您的SD卡空间不足" + Constants.FIRST_LEAST_SIZE + "M，为了不影响您正常使用请及时清理。", "清理数据", "取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//清理数据
					new SDCleanTask(act).execute();
				}
			}, null);

			return false;//提示清理，不可以继续
		}

		if (Constants.SECOND_LEAST_SIZE * StorageUtils.MB >= sdcardSize) {
			ViewUtils.confirmOnUi(act, "您的SD卡空间不足" + Constants.SECOND_LEAST_SIZE + "M，为了不影响您正常使用请及时清理。", "清理数据", "取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//清理数据
					new SDCleanTask(act).execute();
				}
			}, null);

			return true;//提示清理，可以继续
		}

		if (Constants.THIRD_LEAST_SIZE * StorageUtils.MB >= sdcardSize) {
			ViewUtils.showDialogOnUi(act, "温馨提示", "您的SD卡空间不足" + Constants.THIRD_LEAST_SIZE + "M，为了不影响您正常使用请手动及时清理。", null);
			return true;//不提示清理，可以继续
		}

		return true;
	}

	/**
	 * 分享对话框 
	 * @param ctx
	 * @param layoutResId
	 * @return
	 */
	public static Dialog customDialog(QZoneAble act, String content, String aId) {
		Dialog dialog = new Dialog(act, R.style.Theme_FullHeightDialog);
		dialog.setContentView(R.layout.read_book_share_tools_dialog);

		addListener(act, dialog, content, aId);

		return dialog;
	}

	private static void addListener(final QZoneAble act, final Dialog dialog, String content, final String aId) {

		final TextView title = (TextView) dialog.findViewById(R.id.last_read_page_share_title);
		final TextView textView = (TextView) dialog.findViewById(R.id.last_read_page_share_content_count);
		final EditText editText = (EditText) dialog.findViewById(R.id.last_read_page_share_content);
		final Button shareQQ = (Button) dialog.findViewById(R.id.read_book_share_qq_button);
		final Button shareSina = (Button) dialog.findViewById(R.id.read_book_share_sina_button);

		/**
		 * 1. 当前为QQ登陆时，并且session未过期只显示QQ分享按钮
		 * 2. 当前为Sina登陆时，并且session未过期只显示sina分享按钮
		 * 3. 当都普通登陆时，qq分享和sina分享按钮都显示 
		 */
		CommonUtils.controlShareButton(act, shareQQ, shareSina);

		editText.setText(content);

		editText.addTextChangedListener(new TextWatcher() {

			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String shareContentLimit = String.format(act.getString(R.string.last_read_share_content_limit),
						Constants.MAX_SHARE_TEXT_SIZE - s.length());
				textView.setText(shareContentLimit);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				temp = s;

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > Constants.MAX_SHARE_TEXT_SIZE) {
					Toast.makeText(act, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
					s.delete(Constants.MAX_SHARE_TEXT_SIZE, editText.getText().length());
					editText.setText(s);
					editText.setSelection(s.length());
				}
			}
		});

		//关闭
		title.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dialog.dismiss();
				return false;
			}
		});

		shareQQ.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.shareForQQ(act, editText.getText().toString(), aId);
				dialog.dismiss();

			}

		});

		shareSina.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.shareForSina(act, editText.getText().toString(), aId);
				dialog.dismiss();
			}
		});
	}

	/**
	 * 解析支付宝唤起参数
	 * @param act
	 */
	public static AliPayCallParams parseAlipayCallParams(Activity act) {
		//支付宝钱包调用
		Intent intent = act.getIntent();

		String alipayUserId = intent.getStringExtra("alipay_user_id");
		String authCode = intent.getStringExtra("auth_code");
		String appId = intent.getStringExtra("app_id");
		String version = intent.getStringExtra("version");
		String alipayClientVersion = intent.getStringExtra("alipay_client_version");

		//无解决内容返回空
		if (StringUtils.isBlank(alipayUserId) || StringUtils.isBlank(authCode) || StringUtils.isBlank(appId) || StringUtils.isBlank(version)
				|| StringUtils.isBlank(alipayClientVersion))
			return null;

		StringBuilder builder = new StringBuilder("支付宝用户ID：");
		builder.append(alipayUserId).append("\n").append("auth_code:").append(authCode).append("\n").append("app_id:").append(appId).append("\n")
				.append("version:").append(version).append("\n").append("alipay_client_version:").append(alipayClientVersion).append("\n");

		LogUtils.debug(builder.toString());

		AliPayCallParams alipayCallParams = new AliPayCallParams(alipayUserId, authCode, appId, version, alipayClientVersion);

		return alipayCallParams;
	}

	/** 
	 * 保持屏幕唤醒状态（即背景灯不熄灭） 
	 * @param on 是否唤醒 
	 */
	public static void keepScreenOn(Context context, boolean on) {
		if (on) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
			wl.acquire();
		} else if (wl != null) {
			wl.release();
			wl = null;
		}
	}
}
