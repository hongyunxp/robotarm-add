package com.readnovel.book.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Application;
import android.content.Context;

import com.mobclick.android.MobclickAgent;
import com.readnovel.book.base.utils.NetUtils.NetType;

public class EventLogUtils {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final long TIME_LIMIT = 5000;
	public static Application app;

	/**
	 * 发送自定义事件
	 */
	public static void sendEventLog(Context ctx, String event, String label) {

		MobclickAgent.onEvent(ctx, event, label);
	}

	/**
	 * 发送自定义事件
	 * @param ctx
	 * @param rd
	 * @param duration
	 */
	public static void sendEventLog(Context ctx, String event, Map<String, String> map, long duration) {

		if (duration <= TIME_LIMIT)
			return;

		Map<String, String> commonMap = createCommonMap(ctx);
		commonMap.putAll(map);

		MobclickAgent.onEventDuration(ctx, event, commonMap, duration);
	}

	private static Map<String, String> createCommonMap(Context ctx) {
		NetType netType = NetUtils.checkNet(ctx);
		Map<String, String> map = new HashMap<String, String>();
		map.put("netType", netType.getDesc());
		map.put("time", dateFormat.format(new Date()) + "|" + UUID.randomUUID().toString());

		return map;
	}

}
