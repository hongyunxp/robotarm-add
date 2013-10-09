package com.eastedge.readnovel.task;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.NoticeCheck;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.NoticeActivity;
import com.xs.cn.http.HttpImpl;

public class NoticeCheckTask extends EasyTask<Activity, Void, Void, NoticeCheck> {
	private FrameLayout noticeLayout;
	private TextView noticeTextView;

	public NoticeCheckTask(Activity caller) {
		super(caller);

		noticeLayout = (FrameLayout) caller.findViewById(R.id.bookshell_notice_layout);
		noticeTextView = (TextView) caller.findViewById(R.id.bookshell_notice_tv);
	}

	@Override
	public NoticeCheck doInBackground(Void... params) {
		return HttpImpl.noticeCheck();
	}

	@Override
	public void onPostExecute(final NoticeCheck result) {

		if (result != null) {
			if (NoticeCheck.IS_OPEN.equals(result.getSign())) {
				noticeLayout.setVisibility(View.VISIBLE);
				noticeTextView.setText(result.getTitle());

				noticeTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LogUtils.info("执行NoticeCheckTask|onClick");
						//去公告
						Intent intent = new Intent(caller, NoticeActivity.class);
						intent.putExtra("url", result.getUrl());
						caller.startActivity(intent);
					}
				});

			} else
				noticeLayout.setVisibility(View.INVISIBLE);

		}

	}
}
