package com.eastedge.readnovel.task;

import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.ImageView;

import com.eastedge.readnovel.beans.SupportAuthorBean;
import com.eastedge.readnovel.beans.orm.SupportAuthorRecord;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.orm.OrmDBHelper;
import com.eastedge.readnovel.utils.CommonUtils;
import com.j256.ormlite.dao.Dao;
import com.readnovel.base.db.orm.DBHelper;
import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;

/**
 * 红包赠送
 * @author li.li
 *
 * Aug 9, 2013
 */
public class SupportAuthorTask extends EasyTask<Activity, Void, Void, SupportAuthorBean> {
	private ProgressDialog pd;
	private String articleid;
	private String bonus;
	private OrmDBHelper dbHelper;

	public SupportAuthorTask(Activity caller, String articleid, String bonus) {
		super(caller);

		this.articleid = articleid;
		this.bonus = bonus;

		dbHelper = DBHelper.getHelper(OrmDBHelper.class);

	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller);
	}

	@Override
	public void onPostExecute(SupportAuthorBean result) {
		//关闭加载对话框
		pd.cancel();

		if (result != null) {
			if (SupportAuthorBean.SUCCESS.equals(result.getCode())) {

				try {//赠送成功后添加记录

					Dao<SupportAuthorRecord, Integer> supportAuthorDAO = dbHelper.getDao(SupportAuthorRecord.class);

					SupportAuthorRecord sar = new SupportAuthorRecord();
					sar.setBookId(articleid);
					sar.setUserId(BookApp.getUser().getUid());
					sar.setAddTime(new Date(System.currentTimeMillis()));

					supportAuthorDAO.createIfNotExists(sar);

				} catch (SQLException e) {
					LogUtils.error(e.getMessage(), e);
				}

				ViewUtils.showDialog(caller, "温馨提示", getRandomSucMsg(), null);
				ImageView supportAuthor = (ImageView) caller.findViewById(R.id.support_author);//红包
				if (supportAuthor != null)
					supportAuthor.setVisibility(View.GONE);
			} else if (SupportAuthorBean.FAILS.equals(result.getCode())) {

				if ("remain is not enough".equals(result.getMsg()))
					ViewUtils.showDialog(caller, "温馨提示", "赠送失败，用户余额不足", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							CommonUtils.goToConsume(caller);
						}
					});
				else if ("articleid or bonus error".equals(result.getMsg()))
					ViewUtils.showDialog(caller, "温馨提示", "赠送失败，作品id或红包金额错误", null);
				else if ("articleid or bonus error".equals(result.getMsg()))
					ViewUtils.showDialog(caller, "温馨提示", "赠送失败，作品id或红包金额错误", null);
				else if ("not such user info".equals(result.getMsg()))
					ViewUtils.showDialog(caller, "温馨提示", "赠送失败，用户id无效", null);
				else if ("get user remain failed".equals(result.getMsg()))
					ViewUtils.showDialog(caller, "温馨提示", "赠送失败，获取用户余额失败", null);
				else if ("send failed".equals(result.getMsg()))
					ViewUtils.showDialog(caller, "温馨提示", "赠送失败，赠送失败", null);
				else if ("send success add user bonus failed".equals(result.getMsg()))
					ViewUtils.showDialog(caller, "温馨提示", "赠送失败，赠送成功 记录赠送记录失败", null);
				else
					ViewUtils.showDialog(caller, "温馨提示", "赠送失败，其它错误", null);

			}
		} else
			ViewUtils.showDialog(caller, "温馨提示", "赠送失败，非法结果", null);
	}

	@Override
	public SupportAuthorBean doInBackground(Void... params) {

		String token = Util.md5(BookApp.getUser().getUid() + Constants.PRIVATE_KEY).substring(0, 10);
		String userId = BookApp.getUser().getUid();
		String channel = CommonUtils.getChannel(BookApp.getInstance());

		String url = String.format(Constants.SUPPORT_AUTHOR_URL, token, userId, channel, articleid, bonus);

		SupportAuthorBean result = HttpHelper.get(url, null, SupportAuthorBean.class);

		return result;
	}

	//随机获得成功语
	private String getRandomSucMsg() {
		String[] msgs = caller.getResources().getStringArray(R.array.red_packet_success_msg);

		return msgs[new Random().nextInt(msgs.length - 1)];
	}

}
