package com.eastedge.readnovel.task;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.ConsumeQb2;
import com.xs.cn.activitys.MainActivity;

public class ConsumeQQResultTask extends EasyTask<ConsumeQb2, String, Void, Void> {
	private ProgressDialog pd;

	public ConsumeQQResultTask(ConsumeQb2 caller) {
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
	public Void doInBackground(String... params) {

		for (String url : params) {

			QQConsumeCheckResult checkResult = HttpHelper.get(url, null, QQConsumeCheckResult.class);

			if (checkResult != null) {
				if ("300".equals(checkResult.getCode()))
					showDialog("充值成功", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							Intent i = new Intent(caller, MainActivity.class);
							i.putExtra("id", R.id.main_usercenter);
							caller.startActivity(i);
						}

					});
				else if ("301".equals(checkResult.getCode()))
					showDialog("查询请求参数出错");
				else if ("302".equals(checkResult.getCode()))
					showDialog("正在支付中");
				else if ("303".equals(checkResult.getCode()))
					showDialog("查询订单不存在");
				else if ("304".equals(checkResult.getCode()))
					showDialog("充值失败");
			} else
				showDialog("其它错误");
		}

		return null;
	}

	private static final class QQConsumeCheckResult {
		private String code;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}

	//提示消息
	private void showDialog(final String msg, final DialogInterface.OnClickListener listener) {
		HANDLER.post(new Runnable() {

			@Override
			public void run() {
				ViewUtils.showDialog(caller, msg, R.drawable.infoicon, listener);
			}

		});
	}

	//提示消息
	private void showDialog(final String msg) {
		HANDLER.post(new Runnable() {

			@Override
			public void run() {
				ViewUtils.showDialog(caller, msg, R.drawable.infoicon, null);
			}

		});
	}

}
