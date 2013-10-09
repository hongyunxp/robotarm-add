package com.eastedge.readnovel.task;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.eastedge.readnovel.beans.ConsumeQQBean;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.LocalStore;
import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.ConsumeQb2;
import com.xs.cn.activitys.MainActivity;

public class ConsumeQQTask extends EasyTask<ConsumeQb2, Void, Void, Void> {
	private ProgressDialog pd;

	private DialogInterface.OnClickListener toUserCenterListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent i = new Intent(caller, MainActivity.class);
			i.putExtra("id", R.id.main_usercenter);
			caller.startActivity(i);
		}

	};

	public ConsumeQQTask(ConsumeQb2 caller) {
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

		ConsumeQQBean consumeQQBean = LocalStore.getConsumeQQ(caller);
		p.put("uid", String.valueOf(consumeQQBean.getuId()));
		p.put("orderId", consumeQQBean.getOrderId());
		p.put("money", String.valueOf(consumeQQBean.getPayMoney()));
		p.put("cardId", String.valueOf(consumeQQBean.getCardId()));
		p.put("cardPwd", String.valueOf(consumeQQBean.getCardPwd()));

		//请求
		final QQConsumeResult qqConsumeResult = HttpHelper.post(Constants.CONSUME_QQ_URL, p, QQConsumeResult.class);

		//提交结果
		if (qqConsumeResult != null && QQConsumeResult.SIGN_SUCCESS.equals(qqConsumeResult.getSign())
				&& QQConsumeResult.CODE_SUCCESS.equals(qqConsumeResult.getCode())) {//成功

			//成功提示，支付成功跳到用户中心
			confirm("订单提交成功，点击确定查看冲值结果", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String url = qqConsumeResult.getMsg();
					new ConsumeQQResultTask(caller).execute(url);
				}

			}, toUserCenterListener);

		} else if (qqConsumeResult != null) {
			showDialog(qqConsumeResult.getMsg());
		}

		return null;
	}

	//对话框
	private void confirm(final String msg, final OnClickListener pl, final OnClickListener nl) {
		HANDLER.post(new Runnable() {

			@Override
			public void run() {
				ViewUtils.confirm(caller, "温馨提示", msg, pl, nl);
			}

		});
	}

	//提示消息
	private void showDialog(final String msg) {
		HANDLER.post(new Runnable() {

			@Override
			public void run() {
				ViewUtils.showDialog(caller, msg, R.drawable.infoicon, toUserCenterListener);
			}

		});
	}

	private static final class QQConsumeResult {
		public static final String SIGN_SUCCESS = "1";//基本参数正确
		public static final String SIGN_FAIL = "0";//基本参数错误

		public static final String CODE_SUCCESS = "500";//请求成功(非支付成功)

		private String code;
		private String sign;
		private String msg;

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

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}

}
