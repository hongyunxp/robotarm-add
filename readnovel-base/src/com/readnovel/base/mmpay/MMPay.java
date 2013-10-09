package com.readnovel.base.mmpay;

import java.util.HashMap;

import mm.purchasesdk.Purchase;
import mm.purchasesdk.PurchaseCode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.PhoneUtils;
import com.readnovel.base.util.ViewUtils;

/**
 * 移动MM支付
 * @author li.li
 *
 * Jun 4, 2013
 */
public class MMPay {
	//计费信息 
	private final String AppId;
	private final String AppKey;
	private final String mPaycode;
	private final int mProductNum;
	//付费生命周期状态
	public static final int INIT_FINISH = 10000;
	public static final int BILL_FINISH = 10001;
	public static final int QUERY_FINISH = 10002;
	public static final int UNSUB_FINISH = 10003;

	private IAPListener mListener;
	private Purchase purchase;
	private Activity act;

	private ProgressDialog mProgressDialog;
	private MMPayListener payListener;

	public MMPay(Activity act, String AppId, String AppKey, String mPaycode, int mProductNum, MMPayListener payListener) {
		this.act = act;
		this.AppId = AppId;
		this.AppKey = AppKey;
		this.mPaycode = mPaycode;
		this.mProductNum = mProductNum;

		this.payListener = payListener;

		mProgressDialog = new ProgressDialog(act);
	}

	/**
	 * 初始化
	 */
	public void init() {
		/**
		 * IAP组件初始化.包括下面3步。
		 */

		/**
		 * step1.实例化PurchaseListener。实例化传入的参数与您实现PurchaseListener接口的对象有关。
		 * 例如，此Demo代码中使用IAPListener继承PurchaseListener，其构造函数需要Context实例。
		 */
		mListener = new IAPListener(this, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int what = msg.what;
				switch (what) {
				case INIT_FINISH:
					dismissProgressDialog();

					order(mPaycode, mProductNum);

					break;
				default:
					break;
				}
			}

		}) {
			@Override
			public void onBillingFinish(int code, HashMap arg1) {
				//商品购买成功或者已经购买
				if (code == PurchaseCode.ORDER_OK || (code == PurchaseCode.AUTH_OK))
					payListener.onSuccess();
				else
					payListener.onFails();

				super.onBillingFinish(code, arg1);
			}

		};
		/**
		 * step2.获取Purchase实例。
		 */
		purchase = Purchase.getInstance();

		try {
			/**
			 * step3.向Purhase传入应用信息。APPID，APPKEY。 需要传入参数APPID，APPKEY。 APPID，见开发者文档
			 * APPKEY，见开发者文档
			 */
			purchase.setAppInfo(AppId, AppKey);
			/**
			 * step4. IAP组件初始化开始， 参数PurchaseListener，初始化函数需传入step1时实例化的
			 * PurchaseListener。
			 */
			purchase.init(act, mListener);

			showProgressDialog();
		} catch (Exception e) {

			if (e.getMessage().contains("Another request is processing"))
				ViewUtils.showDialog(act, "温馨提示", "初始化中请稍候...", null);
			else
				LogUtils.error(e.getMessage(), e);
		}

	}

	public void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(act);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setMessage("初始化请稍候.....");
		}
		if (!mProgressDialog.isShowing()) {
			//			Toast.makeText(act, "初始化完成", Toast.LENGTH_LONG).show();
			mProgressDialog.show();
		}
	}

	public void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	/**
	 * 购买
	 * @param context
	 * @param listener
	 * @param mPaycode
	 * @param mProductNum
	 */
	private void order(String mPaycode, int mProductNum) {
		try {
			if (act == null)
				return;

			if (!PhoneUtils.getCurActivity(act).equals(act.getClass().getName()))
				return;

			purchase.order(act, mPaycode, mProductNum, mListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
