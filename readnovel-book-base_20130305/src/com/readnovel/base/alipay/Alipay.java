package com.readnovel.base.alipay;

import com.readnovel.book.base.utils.ViewUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;

/**
 * 支付宝支付
 */
public class Alipay {
	public static final String PAY_SUCCESS = "9000";//交易成功
	public static final String PAY_SYS_ERROR = "4000";//系统异常
	public static final String PAY_DATA_FORMAT_ERROR = "4001";//数据格式不正确
	public static final String PAY_DENY = "4003";//该用户绑定的支付宝账户被冻结或不允许支付
	public static final String PAY_UNBIND = "4004";//该用户已解除绑定
	public static final String PAY_BIND_FAIL = "4005";//绑定失败或没有绑定
	public static final String PAY_FAIL = "4006";//订单支付失败
	public static final String PAY_REBIND = "4010";//重新绑定账户
	public static final String PAY_UPDATE = "6000";//支付服务正在进行升级操作
	public static final String PAY_CACEL = "6001";//用户中途取消支付操作
	public static final String PAY_NET_ERROR = "6002";//网络连接异常

	private ProgressDialog mProgress;
	private Activity act;
	private OnClickListener positiveListener;

	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				switch (msg.what) {
				case AlixId.RQF_PAY: {
					closeProgress();

					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");

						if (imemoStart == -1 || imemoEnd == -1 || imemoEnd > strRet.length())
							return;

						tradeStatus = strRet.substring(imemoStart, imemoEnd);

						//先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {// 验签失败
							BaseHelper.showDialog(act, "提示", "失败",
									android.R.drawable.ic_dialog_alert);
						} else {//判断交易码
							if (tradeStatus.equals(PAY_SUCCESS))//判断交易状态码，只有9000表示交易成功
								ViewUtils.showDialog(act, "订单支付成功", null, positiveListener);
							else
								ViewUtils.showDialog(act, "订单支付失败", null, null);
						}
					} catch (Exception e) {
					}
				}
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public Alipay(Activity act, OnClickListener positiveListener) {
		this.act = act;
		this.positiveListener = positiveListener;
	}

	public void pay(String orderInfo, String strsign, String signType) {
		String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&" + getSignType(signType);

		pay(info);
	}

	public void pay(String info) {
		// 调用pay方法进行支付
		MobileSecurePayer msp = new MobileSecurePayer();
		boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, act);

		if (bRet) {
			// 显示“正在支付”进度条
			closeProgress();
			mProgress = BaseHelper.showProgress(act, null, "正在支付", false, true);
		}
	}

	/**
	 * 获取签名方式
	 * 
	 * @return
	 */
	private String getSignType(String type) {
		String getSignType = "sign_type=" + "\"" + type + "\"";
		return getSignType;
	}

	// 关闭进度框
	private void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
		}
	}

	//检测安全支付服务是否被安装
	public boolean checkIsInstall() {
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(act);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();

		return isMobile_spExist;
	}

}
