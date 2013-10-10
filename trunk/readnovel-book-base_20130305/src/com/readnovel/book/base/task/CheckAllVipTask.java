package com.readnovel.book.base.task;

import java.util.List;

import android.content.ContextWrapper;

import com.readnovel.book.base.bean.PayRecord;
import com.readnovel.book.base.bean.VipPayInterval;
import com.readnovel.book.base.common.Constants;
import com.readnovel.book.base.db.table.PayRecordTable;
import com.readnovel.book.base.db.table.VipPayIntervalRecord;
import com.readnovel.book.base.sync.EasyTask;
import com.readnovel.book.base.utils.DateUtils;
import com.readnovel.book.base.utils.LogUtils;
import com.readnovel.book.base.utils.NetUtils;
import com.readnovel.book.base.utils.NetUtils.NetType;

public class CheckAllVipTask extends EasyTask<ContextWrapper, Void, Void, Void> {

	public CheckAllVipTask(ContextWrapper caller) {
		super(caller);
	}

	@Override
	public Void doInBackground(Void... params) {
		LogUtils.info("启动CheckAllVipTask异步任务");

		NetType netType = NetUtils.checkNet(caller);

		if (!NetType.TYPE_NONE.equals(netType)) {
			PayRecordTable prt = new PayRecordTable(caller);
			VipPayIntervalRecord vpir = new VipPayIntervalRecord(caller);

			List<PayRecord> prtList = prt.getAll();
			for (PayRecord pr : prtList) {
				VipPayInterval curVpi = vpir.getByChapterId(pr.getChapterId());
				String lastUpTime = curVpi.getUpTime();

				//确定当前章节超过一个小时
				if (DateUtils.isOutHour(lastUpTime, Constants.PAY_CHECK_DELAY_TIME)) {
					//调用验证服务
					//					Intent payCheckIntent = new Intent(caller, PayCheckService.class);
					//					payCheckIntent.putExtra("chapterId", pr.getChapterId());
					//					caller.startService(payCheckIntent);

					CheckVipTask checkVipTask = new CheckVipTask(caller, Constants.yanzhengjiange_rightnow);
					checkVipTask.execute(pr.getChapterId());
				}
			}
		}

		return null;
	}

	@Override
	public void onPostExecute(Void result) {
	}

}
