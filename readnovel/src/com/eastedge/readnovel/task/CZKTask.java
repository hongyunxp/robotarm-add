package com.eastedge.readnovel.task;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.eastedge.readnovel.beans.CardConsumeBean;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.LocalStore;
import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.ConsumeCzk;
import com.xs.cn.activitys.MainActivity;

/**
 * 神舟付充值支付异步任务
 * 
 * @author li.li
 *
 */
public class CZKTask extends EasyTask<ConsumeCzk, Void, Void, Void> {
	private ProgressDialog pd;

	public CZKTask(ConsumeCzk caller) {
		super(caller);
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller);
	}

	@Override
	public void onPostExecute(Void result) {
		//关闭加载对话框
		pd.cancel();
	}

	@Override
	public Void doInBackground(Void... params) {
		Map<String, String> p = new HashMap<String, String>();

		CardConsumeBean cardConsumeBean = LocalStore.getCzk(caller);
		p.put("uid", String.valueOf(cardConsumeBean.getuId()));
		p.put("orderId", cardConsumeBean.getOrderId());
		p.put("cardType", String.valueOf(cardConsumeBean.getCardType()));
		p.put("cardMoney", String.valueOf(cardConsumeBean.getCartMoney()));
		//		p.put("payMoney", String.valueOf(0.1));//测试使用充值金额
		p.put("payMoney", String.valueOf(cardConsumeBean.getPayMoney()));
		p.put("cardId", String.valueOf(cardConsumeBean.getCardId()));
		p.put("cardPwd", String.valueOf(cardConsumeBean.getCardPwd()));

		if (Double.parseDouble(p.get("payMoney")) < Constants.CONSUME_PAY_MIN) {
			//			showDialog("最低充值限额" + Constants.CONSUME_PAY_MIN + "元");
			ViewUtils.showDialog(caller, "最低充值限额" + Constants.CONSUME_PAY_MIN + "元", R.drawable.infoicon, null);
			return null;
		}

		LogUtils.info("电话卡充值|" + p);

		//请求
		CZKConsumeResult czkConsumeResult = HttpHelper.post(Constants.CZK_URL, p, CZKConsumeResult.class);

		if (czkConsumeResult != null && CZKConsumeResult.SIGN_SUCCESS.equals(czkConsumeResult.getSign())
				&& CZKConsumeResult.CODE_SUCCESS.equals(czkConsumeResult.getCode())) {//成功

			//成功提示，支付成功跳到用户中心
			showDialog("订单提交成功", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(caller, MainActivity.class);
					i.putExtra("id", R.id.main_usercenter);
					caller.startActivity(i);
				}

			});
		}

		else if (czkConsumeResult != null) {//失败

			//检查提交参数
			if (czkConsumeResult != null && CZKConsumeResult.SIGN_FAIL.equals(czkConsumeResult.getSign()))
				showDialog("基本参数错误");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_701.equals(czkConsumeResult.getCode()))
				showDialog("充值用户ID不正确");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_702.equals(czkConsumeResult.getCode()))
				showDialog("充值用户不是有效的小说网注册用户");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_703.equals(czkConsumeResult.getCode()))
				showDialog("订单号不存在或不正确错");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_704.equals(czkConsumeResult.getCode()))
				showDialog("充值卡类型不正确");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_705.equals(czkConsumeResult.getCode()))
				showDialog("充值卡面额不正确");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_706.equals(czkConsumeResult.getCode()))
				showDialog("充值卡金额填写不正确");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_707.equals(czkConsumeResult.getCode()))
				showDialog("充值卡信息填写不正确");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_708.equals(czkConsumeResult.getCode()))
				showDialog("增加订单失败");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_101.equals(czkConsumeResult.getCode()))
				showDialog("md5 验证失败");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_102.equals(czkConsumeResult.getCode()))
				showDialog("订单号重复");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_103.equals(czkConsumeResult.getCode()))
				showDialog("恶意用户");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_104.equals(czkConsumeResult.getCode()))
				showDialog("卡号，密码简单验证失败");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_105.equals(czkConsumeResult.getCode()))
				showDialog("密码正在处理中");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_106.equals(czkConsumeResult.getCode()))
				showDialog("系统繁忙，暂停提交");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_107.equals(czkConsumeResult.getCode()))
				showDialog("多次支付，卡内余额不足");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_109.equals(czkConsumeResult.getCode()))
				showDialog("DES 解密失败");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_201.equals(czkConsumeResult.getCode()))
				showDialog("证书验证失败");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_501.equals(czkConsumeResult.getCode()))
				showDialog("插入数据库失败");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_502.equals(czkConsumeResult.getCode()))
				showDialog("插入数据库失败");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_200.equals(czkConsumeResult.getCode()))
				showDialog("请求成功（非支付成功）");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_902.equals(czkConsumeResult.getCode()))
				showDialog("商户参数不全");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_903.equals(czkConsumeResult.getCode()))
				showDialog("商户ID 不存在");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_904.equals(czkConsumeResult.getCode()))
				showDialog("商户没有激活");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_905.equals(czkConsumeResult.getCode()))
				showDialog("商户没有使用该接口的权限");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_906.equals(czkConsumeResult.getCode()))
				showDialog("商户没有设置密钥");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_907.equals(czkConsumeResult.getCode()))
				showDialog("商户没有设置DES 密钥");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_908.equals(czkConsumeResult.getCode()))
				showDialog("该笔订单已经处理完成（订单状态已经确定的状态：成功或者失败）");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_909.equals(czkConsumeResult.getCode()))
				showDialog("该笔订单不符合重复支付的条件");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_910.equals(czkConsumeResult.getCode()))
				showDialog("服务器返回地址，不符合规范");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_911.equals(czkConsumeResult.getCode()))
				showDialog("订单号不符合规范");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_912.equals(czkConsumeResult.getCode()))
				showDialog("非法订单");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_913.equals(czkConsumeResult.getCode()))
				showDialog("该地方卡暂不支持");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_914.equals(czkConsumeResult.getCode()))
				showDialog("支付金额非法");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_915.equals(czkConsumeResult.getCode()))
				showDialog("卡面额非法");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_916.equals(czkConsumeResult.getCode()))
				showDialog("商户不支持该充值卡的支付");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_917.equals(czkConsumeResult.getCode()))
				showDialog("参数格式不正确");
			else if (czkConsumeResult != null && CZKConsumeResult.CODE_FAIL_0.equals(czkConsumeResult.getCode()))
				showDialog("网络连接不正确");
			else
				showDialog("订单提交失败" + czkConsumeResult.getCode());
		} else {
			LogUtils.info("czkConsumeResult为空");
		}

		return null;

	}

	//提示消息
	private void showDialog(final String msg, final OnClickListener pl) {
		HANDLER.post(new Runnable() {

			@Override
			public void run() {
				ViewUtils.showDialog(caller, msg, R.drawable.infoicon, pl);
			}

		});
	}

	private void showDialog(String msg) {
		showDialog(msg, null);
	}

	private static final class CZKConsumeResult {
		public static final String SIGN_SUCCESS = "1";//基本参数正确
		public static final String SIGN_FAIL = "0";//基本参数错误

		public static final String CODE_SUCCESS = "200";//请求成功(非支付成功)
		//基本错误
		public static final String CODE_FAIL_701 = "701";
		public static final String CODE_FAIL_702 = "702";
		public static final String CODE_FAIL_703 = "703";
		public static final String CODE_FAIL_704 = "704";
		public static final String CODE_FAIL_705 = "705";
		public static final String CODE_FAIL_706 = "706";
		public static final String CODE_FAIL_707 = "707";
		public static final String CODE_FAIL_708 = "708";
		//高级错误
		public static final String CODE_FAIL_101 = "101";
		public static final String CODE_FAIL_102 = "102";
		public static final String CODE_FAIL_103 = "103";
		public static final String CODE_FAIL_104 = "104";
		public static final String CODE_FAIL_105 = "105";
		public static final String CODE_FAIL_106 = "106";
		public static final String CODE_FAIL_107 = "107";
		public static final String CODE_FAIL_109 = "109";

		public static final String CODE_FAIL_201 = "201";
		public static final String CODE_FAIL_501 = "501";
		public static final String CODE_FAIL_502 = "502";
		public static final String CODE_FAIL_200 = "200";
		public static final String CODE_FAIL_902 = "902";
		public static final String CODE_FAIL_903 = "903";
		public static final String CODE_FAIL_904 = "904";
		public static final String CODE_FAIL_905 = "905";
		public static final String CODE_FAIL_906 = "906";
		public static final String CODE_FAIL_907 = "907";
		public static final String CODE_FAIL_908 = "908";
		public static final String CODE_FAIL_909 = "909";
		public static final String CODE_FAIL_910 = "910";
		public static final String CODE_FAIL_911 = "911";
		public static final String CODE_FAIL_912 = "912";
		public static final String CODE_FAIL_913 = "913";
		public static final String CODE_FAIL_914 = "914";
		public static final String CODE_FAIL_915 = "915";
		public static final String CODE_FAIL_916 = "916";
		public static final String CODE_FAIL_917 = "917";
		public static final String CODE_FAIL_0 = "0";

		private String code;
		private String sign;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

	}

}
