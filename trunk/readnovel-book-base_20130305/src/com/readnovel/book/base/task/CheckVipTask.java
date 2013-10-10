package com.readnovel.book.base.task;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.ContextWrapper;
import android.widget.Toast;

import com.readnovel.book.base.bean.VipPayInterval;
import com.readnovel.book.base.common.Constants;
import com.readnovel.book.base.db.table.PayRecordTable;
import com.readnovel.book.base.db.table.VipPayIntervalRecord;
import com.readnovel.book.base.entity.PayCheckResult;
import com.readnovel.book.base.sync.EasyTask;
import com.readnovel.book.base.utils.CommonUtils;
import com.readnovel.book.base.utils.DateUtils;
import com.readnovel.book.base.utils.LogUtils;
import com.readnovel.book.base.utils.NetUtils;
import com.readnovel.book.base.utils.NetUtils.NetType;

/**
 * 延迟验证异步任务 ，主要用于验证
 * 
 * @author li.li
 * 
 *         Sep 23, 2012
 */
public class CheckVipTask extends EasyTask<ContextWrapper, Integer, PayCheckResult, Void> {
	private static final List<Integer> CHECK_LOCK = new LinkedList<Integer>();
	private final VipPayIntervalRecord vpir;
	private final PayRecordTable prt;
	private int yanzheng;
	private TimerTask mTimerTask;
	private Timer timer;
	private int runtimecount = 0;

	public CheckVipTask(ContextWrapper caller, int myanzheng) {
		super(caller);
		yanzheng = myanzheng;
		vpir = new VipPayIntervalRecord(caller);
		prt = new PayRecordTable(caller);

	}

	@Override
	public Void doInBackground(Integer... chapterIds) {
		LogUtils.info("启动CheckVipTask异步任务");

		if (yanzheng == Constants.yanzhengjiange_onehour) { // 如果是一个小时验证一次
			for (final int chapterId : chapterIds) {
				// 一个小时后验证状态
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						NetType netType = NetUtils.checkNet(caller);
						if (netType != NetType.TYPE_NONE) {
							// 未验证状态，确定超过一个小时更新状态
							VipPayInterval vpi = vpir.getByChapterId(chapterId);
							String lastUpTime = vpi.getUpTime();
							if (vpi != null && vpi.getState() == VipPayInterval.UN_CHECK && DateUtils.isOutHour(lastUpTime, Constants.PAY_CHECK_DELAY_TIME)) {
								PayCheckResult pcr = CommonUtils.payCheck(caller);

								publishProgress(pcr);// 更新ui

								LogUtils.info("验证结果为：" + pcr.getCode() + "|" + pcr.getFee());

								if (String.valueOf(Constants.CODE_PAY_CHECK_SUCCESS).equals(pcr.getCode())) {// 成功
									vpir.upStateById(vpi.getId(), VipPayInterval.PAY_SUCCESS);
									prt.upFee(vpi.getId(), pcr.getFee());

								} else if (String.valueOf(Constants.CODE_PAY_CHECK_FAIL).equals(pcr.getCode()))// 失败
									vpir.upStateById(vpi.getId(), VipPayInterval.PAY_FAIL);

							}
						}

						timer.cancel();
					}
				}, Constants.PAY_CHECK_DELAY_TIME);
			}
		} else if (yanzheng == Constants.yanzhengjiange_tenminute) {
			for (final int chapterId : chapterIds) {

				if (isLock(chapterId))
					return null;//判断是否加锁

				lock(chapterId);//加锁

				timer = new Timer();
				mTimerTask = new TimerTask() {

					@Override
					public void run() {
						NetType netType = NetUtils.checkNet(caller);

						if (netType != NetType.TYPE_NONE) {
							VipPayInterval vpi = vpir.getByChapterId(chapterId);

							// 未验证状态，确定10分钟之内订购成功，就进行数据更新
							if (vpi != null && vpi.getState() == VipPayInterval.UN_CHECK) {
								PayCheckResult pcr = CommonUtils.payCheck(caller);

								LogUtils.info("验证每隔2分钟间隔结果为：" + pcr.getCode() + "|" + pcr.getFee());

								if (String.valueOf(Constants.CODE_PAY_CHECK_SUCCESS).equals(pcr.getCode())) {// 成功
									vpir.upStateById(vpi.getId(), VipPayInterval.PAY_SUCCESS);
									prt.upFee(vpi.getId(), pcr.getFee());
									// 执行完结束定时任务
									unlock(chapterId);//解锁
									timer.cancel();
								}

								if (runtimecount >= (Constants.PAY_CHECK_DELAY_TIME / Constants.PAY_CHECK_DELAY_TEN_MINUTE_TIME - 1)) {
									// 执行完结束定时任务
									unlock(chapterId);//解锁
									timer.cancel();
								} else
									runtimecount++;
							}
						}

					}
				};
				timer.schedule(mTimerTask, DateUtils.SECOND * 1, Constants.PAY_CHECK_DELAY_TEN_MINUTE_TIME);
			}
		} else if (yanzheng == Constants.yanzhengjiange_rightnow) {
			for (final int chapterId : chapterIds) {

				NetType netType = NetUtils.checkNet(caller);

				if (netType != NetType.TYPE_NONE) {
					// 未验证状态，确定超过一个小时更新状态
					VipPayInterval vpi = vpir.getByChapterId(chapterId);
					String lastUpTime = vpi.getUpTime();
					if (vpi != null && vpi.getState() == VipPayInterval.UN_CHECK && DateUtils.isOutHour(lastUpTime, Constants.PAY_CHECK_DELAY_TIME)) {
						PayCheckResult pcr = CommonUtils.payCheck(caller);

						publishProgress(pcr);// 更新ui

						LogUtils.info("验证结果为：" + pcr.getCode() + "|" + pcr.getFee());

						if (String.valueOf(Constants.CODE_PAY_CHECK_SUCCESS).equals(pcr.getCode())) {// 成功
							vpir.upStateById(vpi.getId(), VipPayInterval.PAY_SUCCESS);
							prt.upFee(vpi.getId(), pcr.getFee());

						} else if (String.valueOf(Constants.CODE_PAY_CHECK_FAIL).equals(pcr.getCode()))// 失败
							vpir.upStateById(vpi.getId(), VipPayInterval.PAY_FAIL);

					}
				}

			}
		}

		return null;
	}

	@Override
	public void onProgressUpdate(PayCheckResult... values) {
		super.onProgressUpdate(values);

		for (PayCheckResult result : values) {
			if (String.valueOf(Constants.CODE_PAY_CHECK_ERROR).equals(result.getCode()))
				Toast.makeText(caller, "验证网络连接超时", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onPostExecute(Void result) {

	}

	private void lock(int chapterId) {
		VipPayInterval vpi = vpir.getByChapterId(chapterId);
		if (!CHECK_LOCK.contains(vpi.getId()))
			CHECK_LOCK.add(vpi.getId());
	}

	private void unlock(int chapterId) {
		VipPayInterval vpi = vpir.getByChapterId(chapterId);
		if (CHECK_LOCK.contains(vpi.getId()))
			CHECK_LOCK.remove(Integer.valueOf(vpi.getId()));
		
	}

	private boolean isLock(int chapterId) {
		VipPayInterval vpi = vpir.getByChapterId(chapterId);
		return CHECK_LOCK.contains(vpi.getId());
	}

}
