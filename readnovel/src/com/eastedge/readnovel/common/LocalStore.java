package com.eastedge.readnovel.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.eastedge.readnovel.beans.CardConsumeBean;
import com.eastedge.readnovel.beans.ConsumeQQBean;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.view.BookPageFactory;
import com.readnovel.base.util.JsonUtils;
import com.readnovel.base.util.LogUtils;

public class LocalStore {
	private static String USER_PREF = "user_pref";
	// 用户名
	private static String user_name = "user_name";

	public static void setUserName(Context ctx, String flag) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		Editor editor = sp.edit();
		editor.putString(user_name, flag);
		editor.commit();
	}

	public static String getUserName(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		String flag = sp.getString(user_name, "");
		return flag;
	}

	//第一次预装
	private static String IS_PRE_INSTALL = "isPreInstall";

	public static void setPreInstall(Context ctx, boolean isFirst) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit();
		editor.putBoolean(IS_PRE_INSTALL, isFirst);
		editor.commit();
	}

	public static boolean getPreInstall(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
		return sp.getBoolean(IS_PRE_INSTALL, false);
	}

	//激活
	private static String activate = "activate";

	public static void setActivate(Context ctx, boolean isActivite) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putBoolean(activate, isActivite);
		editor.commit();
	}

	public static boolean getActivate(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getBoolean(activate, false);
	}

	// 当前登录用户名
	private static String lastUid = "last_uid";

	public static void setLastUid(Context ctx, String uid) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		Editor editor = sp.edit();
		editor.putString(lastUid, uid);
		editor.commit();
	}

	public static String getLastUid(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		String uid = sp.getString(lastUid, "-1");
		return uid;
	}

	// 当前登录类型
	private static String lastLoginType = "last_login_type";

	public static void setLastLoginType(Context ctx, LoginType loginType) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		Editor editor = sp.edit();
		editor.putInt(lastLoginType, loginType.getValue());
		editor.commit();
	}

	public static LoginType getLastLoginType(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		int type = sp.getInt(lastLoginType, LoginType.def.getValue());

		return LoginType.getByValue(type);
	}

	// 密码
	private static String user_pwd = "user_pwd";

	public static void setUserPwd(Context ctx, String flag) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		Editor editor = sp.edit();
		editor.putString(user_pwd, flag);
		editor.commit();
	}

	public static String getUserPwd(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		String flag = sp.getString(user_pwd, "");
		return flag;
	}

	// 是否记住密码
	private static String is_ji = "is_ji";

	public static void setIsJi(Context ctx, boolean flag) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		Editor editor = sp.edit();
		editor.putBoolean(is_ji, flag);
		editor.commit();
	}

	public static boolean getIsJi(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		boolean flag = sp.getBoolean(is_ji, true);
		return flag;

	}

	// 记住密码时间
	private static String stime = "stime";

	public static void setStime(Context ctx, String flag) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		Editor editor = sp.edit();
		editor.putString(stime, flag);
		editor.commit();
	}

	public static String getStime(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		String flag = sp.getString(stime, "");
		return flag;
	}

	// 版本更新设置
	private static String update = "update";

	public static void setUpdate(Context ctx, int flag) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(update, flag);
		editor.commit();
	}

	public static int getUpdate(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		int flag = sp.getInt(update, 0);
		return flag;
	}

	// 第一次进入程序
	private static String firstload = "firstload";

	public static void setfirstload(Context ctx, String flag) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		Editor editor = sp.edit();
		editor.putString(firstload, flag);
		editor.commit();
	}

	public static String getfirstload(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		String flag = sp.getString(firstload, "");
		return flag;
	}

	//第一次进书架
	private static String firstbf = "firstbf";

	public static void setFirstbf(Context ctx, int flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putInt(firstbf, flag);
		editor.commit();
	}

	public static int getFirstbf(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		int flag = sp.getInt(firstbf, 0);
		return flag;
	}

	//第一次进阅读
	private static String firstRead = "firstRead";

	public static void setFirstRead(Context ctx, int flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putInt(firstRead, flag);
		editor.commit();
	}

	public static int getFirstRead(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getInt(firstRead, 0);
	}

	//字体大小
	private static String fontsize = "fontsize";

	public static void setFontsize(Context ctx, int flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putInt(fontsize, flag);
		editor.commit();
	}

	public static int getFontsize(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getInt(fontsize, BookPageFactory.DEF_FOND_SIZE_INDEX);
	}

	//默认背景
	private static String mrbg = "mrbg";

	public static void setMrbg(Context ctx, int flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putInt(mrbg, flag);
		editor.commit();
	}

	public static int getMrbg(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getInt(mrbg, 0);
	}

	//默认亮度
	private static String mrld = "mrld";

	public static void setMrld(Context ctx, int flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putInt(mrld, flag);
		editor.commit();
	}

	public static int getMrld(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getInt(mrld, Util.getScreenBrightness(ctx) - 20);
	}

	//默认黑夜
	private static String mrnt = "mrnt";

	public static void setMrnt(Context ctx, int flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putInt(mrnt, flag);
		editor.commit();
	}

	public static int getMrnt(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getInt(mrnt, 0);
	}

	//老用户第一次登录
	private static String firstlogin = "firstlogin";

	public static void setFirstLogin(Context ctx, int flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putInt(firstlogin, flag);
		editor.commit();
	}

	public static int getFirstLogin(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getInt(firstlogin, 0);
	}

	//第一次安装软件
	private static String firstinstall = "firstinstall";

	public static void setFirstInstall(Context ctx, int flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putInt(firstinstall, flag);
		editor.commit();
	}

	public static int getFirstInstall(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getInt(firstinstall, 0);
	}

	//是否启用推送
	private static String isSetPush = "setpush";

	public static void setPush(Context ctx, boolean flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putBoolean(isSetPush, flag);
		editor.commit();
	}

	public static boolean getIsSetPush(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getBoolean(isSetPush, true);
	}

	// 上次检查更新时间
	private static String Uptime = "uptime";

	public static void setUptime(Context ctx, String flag) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		Editor editor = sp.edit();
		editor.putString(Uptime, flag);
		editor.commit();
	}

	public static String getUptime(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		String flag = sp.getString(Uptime, "");
		return flag;
	}

	//是否重新启动程序
	private static String isFullStart = "isfirststart";

	public static void setIsFullStart(Context ctx, boolean flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putBoolean(isFullStart, flag);
		editor.commit();
	}

	public static boolean getIsFullStart(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getBoolean(isFullStart, false);
	}

	//正在运行
	private static String isRun = "isRun";

	public static void setIsRun(Context ctx, boolean flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putBoolean(isRun, flag);
		editor.commit();
	}

	public static boolean getIsRun(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getBoolean(isRun, false);
	}

	//充值卡充值信息
	private static String CONSUME_CZK = "is_czk";

	public static void setCzk(Context ctx, CardConsumeBean ccb) {
		String json = JsonUtils.toJson(ccb);

		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putString(CONSUME_CZK, json);
		editor.commit();
	}

	public static CardConsumeBean getCzk(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		String json = sp.getString(CONSUME_CZK, null);

		return JsonUtils.fromJson(json, CardConsumeBean.class);
	}

	//QQ充值卡充值信息
	private static String CONSUME_QQ = "is_consume_qq";

	public static void setConsumeQQ(Context ctx, ConsumeQQBean ccb) {
		String json = JsonUtils.toJson(ccb);

		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putString(CONSUME_QQ, json);
		editor.commit();
	}

	public static ConsumeQQBean getConsumeQQ(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		String json = sp.getString(CONSUME_QQ, null);

		return JsonUtils.fromJson(json, ConsumeQQBean.class);
	}

	//第一次订阅
	private static String FIRST_CONSUME = "first_consume";

	public static void setFirstConsume(Context ctx, boolean isFirst) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putBoolean(FIRST_CONSUME, isFirst);
		editor.commit();
	}

	public static boolean getFirstConsume(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		boolean result = sp.getBoolean(FIRST_CONSUME, true);

		return result;
	}

	//当前登陆用户
	private static final String CUR_LOGIN_USER = "cur_login_user";

	public static User getCurLoginUser(Context ctx) {

		try {
			SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
			String userJson = sp.getString(CUR_LOGIN_USER, null);
			User user = JsonUtils.fromJson(userJson, User.class);

			return user;
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static void setCurLoginUser(Context ctx, User user) {

		try {
			SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
			editor.putString(CUR_LOGIN_USER, JsonUtils.toJson(user));
			editor.commit();
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}
	}

	public static void cleanCurLoginUser(Context ctx) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putString(CUR_LOGIN_USER, null);
		editor.commit();
	}

	//是否启用屏幕常亮
	private static String keepScreenOn = "keep_screen_on";

	public static void setKeepScreenOn(Context ctx, boolean flag) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putBoolean(keepScreenOn, flag);
		editor.commit();
	}

	public static boolean getKeepScreenOn(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getBoolean(keepScreenOn, true);
	}

	//导航菜单打开时间
	private static String SLIDING_MENU_TIME = "sliding_menu_time";

	public static void setSlidingMenuTime(Context ctx, long time) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(USER_PREF, 0).edit();
		editor.putLong(SLIDING_MENU_TIME, time);
		editor.commit();
	}

	public static long getSlidingMenuTime(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(USER_PREF, 0);
		return sp.getLong(SLIDING_MENU_TIME, 0);
	}

}
