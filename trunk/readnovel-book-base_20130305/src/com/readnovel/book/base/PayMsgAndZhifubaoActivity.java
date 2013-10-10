package com.readnovel.book.base;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.pay.BXPay;
import com.readnovel.base.alipay.AlixId;
import com.readnovel.base.alipay.BaseHelper;
import com.readnovel.base.alipay.MobileSecurePayHelper;
import com.readnovel.base.alipay.ResultChecker;
import com.readnovel.base.alipay.Rsa;
import com.readnovel.book.base.bean.Parter;
import com.readnovel.book.base.bean.VipPayInterval;
import com.readnovel.book.base.db.table.PayRecordTable;
import com.readnovel.book.base.db.table.VipPayIntervalRecord;
import com.readnovel.book.base.entity.Chapter;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.task.ZhifubaoInitTask;
import com.readnovel.book.base.utils.BookInfoUtils;
import com.readnovel.book.base.utils.BookListProvider;
import com.readnovel.book.base.utils.CommonUtils;
import com.readnovel.book.base.utils.NetUtils;
import com.readnovel.book.base.utils.NetUtils.NetType;

public class PayMsgAndZhifubaoActivity extends Activity {
	String outtrade_no;
	String token;
	private Button zhifubao ,weipaiButton ,yibaoButton;
	public Parter parter;
	String title;
	private ZhifubaoInitTask task;
	private StyleSaveUtil sst;
	private int chapterid;
	private TextView showchapter;
	private static final String NOTIFY_URL = "http://www.smqhe.com/pay/notify_url.php";// 支付通知地址
	public String aid;
	// 这里接收支付结果，支付宝手机端同步通知
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;
				switch (msg.what) {
				case 3:
					break;
				case AlixId.RQF_PAY: {
					task.closeProgress();
					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						if (imemoStart != -1 && imemoEnd != -1 && imemoEnd > strRet.length()) {
							BaseHelper.showDialog(PayMsgAndZhifubaoActivity.this, "提示", "网络数据错误，请重试" + tradeStatus, R.drawable.infoicon);
							return;
						}
						tradeStatus = strRet.substring(imemoStart, imemoEnd);
						// 先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {// 验签失败
							BaseHelper.showDialog(PayMsgAndZhifubaoActivity.this, "提示", "您的订单信息已被非法篡改.", android.R.drawable.ic_dialog_alert);
						} else {// 判断交易码
							if (tradeStatus.equals("9000")) {// 判断交易状态码，只有9000表示交易成功
								// 跳转到阅读界面。    
								//sst.setpay(true);
								// 修改付费状态
								PayRecordTable prt = new PayRecordTable(PayMsgAndZhifubaoActivity.this);
								VipPayIntervalRecord vpir = new VipPayIntervalRecord(PayMsgAndZhifubaoActivity.this);
								VipPayInterval vpi = vpir.getByChapterId(chapterid);
								vpir.upStateById(vpi.getId(), VipPayInterval.PAY_SUCCESS);
								prt.upFee(vpi.getId(), 2.0f);
								Chapter chapter = BookInfoUtils.getByChapterId(PayMsgAndZhifubaoActivity.this, chapterid);
								CommonUtils.goToFlipRead(PayMsgAndZhifubaoActivity.this, chapter.getFileName(), chapter.getTitle(), chapter.getId());//进入章节内容
								finish();
							} else {
								BaseHelper.showDialog(PayMsgAndZhifubaoActivity.this, "提示", "支付失败。交易状态码:" + tradeStatus, R.drawable.infoicon);
							}
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_msg);
		zhifubao = (Button) this.findViewById(R.id.zhifubao);
		zhifubao.setText(Html.fromHtml(getString(R.string.zhifubao_item)));
		weipaiButton = (Button)this.findViewById(R.id.weipai_pay);
		showchapter = (TextView) this.findViewById(R.id.showchapter);
		sst = new StyleSaveUtil(PayMsgAndZhifubaoActivity.this);
		chapterid = this.getIntent().getExtras().getInt("chapterID");
		weipaiButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(PayMsgAndZhifubaoActivity.this, "正在启动支付，请稍后", Toast.LENGTH_LONG).show();
				String  payCode ="0001";//费用项代码
			     BXPay bxPay = new BXPay(PayMsgAndZhifubaoActivity.this);
			        Map<String,String> devPrivate = new HashMap<String,String>();
			        devPrivate.put("bookid", "125889 ");
//			        devPrivate.put("chapterid", ""+chapterid);
			        bxPay.setDevPrivate(devPrivate);
			     bxPay.pay(payCode, new BXPay.PayCallback() {
					
					@Override
					public void pay(Map<String, String> arg0) {
						String result = arg0.get("result");
						Log.i("msg",result );
						if ("success".equals(result)) {
							Toast.makeText(PayMsgAndZhifubaoActivity.this, "支付成功，马上进入阅读，请稍等", Toast.LENGTH_SHORT).show();
							
							// 修改付费状态
							PayRecordTable prt = new PayRecordTable(PayMsgAndZhifubaoActivity.this);
							VipPayIntervalRecord vpir = new VipPayIntervalRecord(PayMsgAndZhifubaoActivity.this);
							VipPayInterval vpi = vpir.getByChapterId(chapterid);
							vpir.upStateById(vpi.getId(), VipPayInterval.PAY_SUCCESS);
							prt.upFee(vpi.getId(), 2.0f);
							Chapter chapter = BookInfoUtils.getByChapterId(PayMsgAndZhifubaoActivity.this, chapterid);
							CommonUtils.goToFlipRead(PayMsgAndZhifubaoActivity.this, chapter.getFileName(), chapter.getTitle(), chapter.getId());//进入章节内容
							finish();
						}else {
							Toast.makeText(PayMsgAndZhifubaoActivity.this, "付费未成功，请开启网络", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
		zhifubao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 判断有没有网络，没有网络提示用户需要打开网络
				// 判断网络状态 支付需要网络，请开启网络
				NetType netType = NetUtils.checkNet(PayMsgAndZhifubaoActivity.this);
				if (!NetType.TYPE_NONE.equals(netType)) {
					outtrade_no = CommonUtils.getOutTradeNo(PayMsgAndZhifubaoActivity.this);
					token = CommonUtils.getPayToken(PayMsgAndZhifubaoActivity.this, outtrade_no);
					// 准备调用支付宝的接口
					MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(PayMsgAndZhifubaoActivity.this);
					boolean isMobile_spExist = mspHelper.detectMobile_sp();
					if (!isMobile_spExist)
						return;
					task = new ZhifubaoInitTask(PayMsgAndZhifubaoActivity.this);
					task.execute();
				} else {
					Toast.makeText(PayMsgAndZhifubaoActivity.this, "网络未开启，请开启网络", Toast.LENGTH_SHORT).show();
				}
			}
		});
//		yibaoButton = (Button)this.findViewById(R.id.yibao_pay);
//		// 易宝支付哦，准备好了没？
//		yibaoButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {	
//             Toast.makeText(PayMsgAndZhifubaoActivity.this, "易宝支付开始了哦", Toast.LENGTH_SHORT).show();				
//             NetType netType = NetUtils.checkNet(PayMsgAndZhifubaoActivity.this);
//				if (!NetType.TYPE_NONE.equals(netType)) {
//					outtrade_no = CommonUtils.getOutTradeNo(PayMsgAndZhifubaoActivity.this);
//					token = CommonUtils.getPayToken(PayMsgAndZhifubaoActivity.this, outtrade_no);
//					// 准备调用支付宝的接口
//					MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(PayMsgAndZhifubaoActivity.this);
//					boolean isMobile_spExist = mspHelper.detectMobile_sp();
//					if (!isMobile_spExist)
//						return;
//					task = new ZhifubaoInitTask(PayMsgAndZhifubaoActivity.this);
//					task.execute();
//				} else {
//					Toast.makeText(PayMsgAndZhifubaoActivity.this, "网络未开启，请开启网络", Toast.LENGTH_SHORT).show();
//				}		
//				Toast.makeText(PayMsgAndZhifubaoActivity.this, "nimeide ", Toast.LENGTH_SHORT).show();
//				
//				
//			}
//		});
	};

	/**
	 * 获取商品订单
	 * @param position
	 *            商品在列表中的位置
	 * @return
	 */
	public String getOrderInfo() {
		String strOrderInfo = "partner=" + "\"" + parter.getPARTNER() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + parter.getSELLER() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + outtrade_no + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + "001"+ BookListProvider.getInstance(this).getBook().getId() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + title + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\"" + 1.6 + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\"" + NOTIFY_URL + "\"";
		return strOrderInfo;
	}

	/**
	 * 检测配置信息 partnerid商户id，seller收款帐号不能为空
	 */
	public boolean checkInfo() {
		String partner = parter.getPARTNER();
		String seller = parter.getSELLER();
		if (partner == null || partner.length() <= 0 || seller == null || seller.length() <= 0)
			return false;
		return true;
	}

	/**
	 * 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	public String sign(String signType, String content) {
		return Rsa.sign(content, parter.getRSA_PRIVATE());
	}

	/**
	 * 获取签名方式
	 * 
	 * @return
	 */
	public String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		String[] chapterScope = CommonUtils.chapterScope(this, chapterid);
		//设置可看的章节区间
		showchapter.setText("第" + chapterScope[0] + "章 - 第" + chapterScope[1] + "章");
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
