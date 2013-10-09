package com.eastedge.readnovel.common;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.task.AlipayWalletPayTask;
import com.eastedge.readnovel.task.CheckBaoYueTask;
import com.eastedge.readnovel.task.SupportAuthorPageTask;
import com.eastedge.readnovel.task.SupportAuthorTask;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.alipay.Alipay;
import com.readnovel.base.sync.Task;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.LoginActivity;
import com.xs.cn.activitys.MainActivity;
import com.xs.cn.activitys.MenuTheme;
import com.xs.cn.activitys.Novel_sbxxy;
import com.xs.cn.activitys.ReadingActivity;
import com.xs.cn.activitys.UpdateInfo;
import com.xs.cn.activitys.UpdateMiMa;

/**
 * 处理JavaScript回调
 * 
 * @author li.li
 *
 * Jan 15, 2013
 */
public class JavaScript {
	public static final String NAME = "xs";

	private Activity act;
	private WebView mWebView;
	private Task<?, ?, ?, ?> task;

	private JavaScript(Activity act, WebView mWebView) {
		this.act = act;
		this.mWebView = mWebView;
	}

	private JavaScript(Activity act, WebView mWebView, Task<?, ?, ?, ?> task) {
		this.act = act;
		this.mWebView = mWebView;
		this.task = task;
	}

	public static JavaScript newInstance(Activity act, WebView mWebView) {
		return new JavaScript(act, mWebView);
	}

	public static JavaScript newInstance(Activity act, WebView mWebView, Task<?, ?, ?, ?> task) {
		return new JavaScript(act, mWebView, task);
	}

	/**                                                                                                                                                                                                                                                                                                                                                                           
	 * 去用户中心，js回调
	 */
	public void goPersonCenter() {

		Intent intent = new Intent(act, MainActivity.class);
		intent.putExtra("id", R.id.main_usercenter);
		act.startActivity(intent);
		act.finish();
	}

	/**
	 * ***************************************
	 * 手机包月相关
	 * ***************************************
	 */

	/**
	 * 预定购
	 * @param phoneNo 手机号
	 */
	public void preOrder(final String phoneNo) {
		mWebView.post(new Runnable() {

			@Override
			public void run() {
				User user = BookApp.getUser();

				if (user != null) {
					user.setTel(phoneNo);

					mWebView.clearHistory();//清除历史记录

					//预定购不使用缓存
					// 设置缓存模式-不使用缓存
					mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

					String url = String.format(Constants.PAY_MONTH_ORDER, user.getUid(), user.getToken(), phoneNo);
					mWebView.loadUrl(url);
				}
			}
		});
	}

	/**
	 * 重定购
	 */
	public void reOrder() {
		mWebView.goBack();
	}

	/**
	 * 查看手机包月状态
	 */
	public void showOrderStatus() {
		//得到包月状态异步任务
		new CheckBaoYueTask(act).execute();

	}

	/**
	 * 订购
	 * @param smsContent 订购指令
	 * @param smsSendTo 订购指令发送号码
	 */
	public void order(String smsContent, String smsSendTo) {
		//		sendMsg(smsSendTo, smsContent);
		boolean success = sendMsg(act, smsSendTo, smsContent);

		if (success)
			ViewUtils.showDialog(act, "温馨提示", "短信发送完成", null);
		else
			ViewUtils.showDialog(act, "温馨提示", "短信发送失败", null);

	}

	/**
	 * 发短信
	 * @param smsSendTo
	 * @param smsContent
	 */
	private void sendMsg(String smsSendTo, String smsContent) {
		Uri uri = Uri.parse("smsto:" + smsSendTo);
		Intent i = new Intent(Intent.ACTION_SENDTO, uri);

		String smsBodyName = act.getString(R.string.sms_body_name);
		i.putExtra(smsBodyName, smsContent);

		act.startActivity(i);
	}

	/**
	 * 发短信
	 * @param ctx
	 * @param receiver
	 * @param content
	 */
	private static boolean sendMsg(Context ctx, String receiver, String content) {
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

			return true;
		}

		return false;
	}

	/**
	 * 支付宝APP充值WEB页面
	 */
	public void alipayPay(double money) {
		Alipay alipay = new Alipay(act, new DialogInterface.OnClickListener() {//支付成功跳到用户中心

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(act, MainActivity.class);
						intent.putExtra("id", R.id.main_usercenter);
						act.startActivity(intent);
					}

				});

		// 检测安全支付服务是否安装
		if (!alipay.checkIsInstall())
			return;

		AlipayWalletPayTask alipayTask = new AlipayWalletPayTask(act, alipay, money);
		alipayTask.execute();
	}

	/**
	 * ******************************************************
	 * 红包
	 * ******************************************************
	 */

	/**
	 * 关闭对话框 
	 */
	public void goBack() {
		if (task instanceof SupportAuthorPageTask) {
			((SupportAuthorPageTask) task).getDialog().cancel();
		}
	}

	/**
	 * 确认送红包
	 * @param money
	 */
	public void bonusPay(String money) {
		if (BookApp.getUser() == null) {
			Intent intent = new Intent(act, LoginActivity.class);
			act.startActivity(intent);
			return;
		}

		if (task instanceof SupportAuthorPageTask) {
			((SupportAuthorPageTask) task).getDialog().cancel();
			new SupportAuthorTask(act, ((SupportAuthorPageTask) task).getAId(), money).execute();
		}
	}

	public void goLogin() {
		CommonUtils.goToLogin(act);
	}

	public void goReg() {
		CommonUtils.goToRegist(act);
	}

	public void goPay() {
		CommonUtils.goToConsume(act);
	}

	public void goSystem() {
		CommonUtils.goToSetting(act);
	}

	public void goRecommend() {
		CommonUtils.goToApp(act);
	}

	public void addBookrack(String aId, String name, String url, String finish) {
		CommonUtils.addBookAndDown(act, aId, name, url, Integer.parseInt(finish));
	}

	public void goMyvip() {
		CommonUtils.goMySubVip(act);
	}

	public void goUsercp() {
		CommonUtils.goMyInfo(act);
	}

	public void mobile() {
		CommonUtils.goBindMobile(act);
	}

	public void bindMobile() {
		CommonUtils.bindPhone(act);
	}

	public void unbindMobile() {
		CommonUtils.unBindPhone(act);
	}

	public void goLogout() {
		CommonUtils.logout(act);

		Intent intent = new Intent(act, MainActivity.class);
		intent.putExtra("id", R.id.main_usercenter);
		act.startActivity(intent);
	}

	public void changePassword() {
		Intent intent = new Intent(act, UpdateMiMa.class);
		act.startActivity(intent);
	}

	public void changeEmai() {
		Intent intent = new Intent(act, UpdateInfo.class);
		act.startActivity(intent);
	}

	public void goReading(String url) {
		Intent intent = new Intent(act, ReadingActivity.class);
		intent.putExtra("url", url);
		act.startActivity(intent);
	}

	public void close() {
		act.finish();
	}

	public void goDiscount() {
		Intent intent = new Intent(act, MenuTheme.class);
		intent.putExtra("aid", "99999");
		intent.putExtra("sorted", "high");// 默认男生专区
		intent.putExtra("page", 1);//默认第一页
		act.startActivity(intent);
	}

	public void goBookDetail(String aId) {
		Intent intent = new Intent(act, Novel_sbxxy.class);
		Bundle bundle = new Bundle();
		bundle.putString("Articleid", aId);
		act.startActivity(intent);
	}

}
