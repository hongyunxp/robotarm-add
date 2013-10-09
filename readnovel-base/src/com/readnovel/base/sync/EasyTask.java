package com.readnovel.base.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;

/**
 * 异步任务抽象类
 * 
 * 性能是new Thread().start()的5到15倍
 * 
 * @author li.li
 *
 * Sep 6, 2012
 */
public abstract class EasyTask<Caller, Params, Progress, Result> implements Task<Caller, Params, Progress, Result> {
	protected static final ExecutorService WORKERS = Executors.newFixedThreadPool(5);// 线程池
	protected static final Handler HANDLER = new Handler();//UI处理

	protected Caller caller;// 当前异步任务的调用者

	public EasyTask(Caller caller) {
		this.caller = caller;
	}

	@Override
	public void execute(final Params... params) {

		WORKERS.execute(new Runnable() {

			@Override
			public void run() {
				HANDLER.post(new Runnable() {//1. 初始化

					@Override
					public void run() {
						onPreExecute();
					}
				});

				final Result result = doInBackground(params);//2. 执行异步操作

				HANDLER.post(new Runnable() {

					@Override
					public void run() {
						onPostExecute(result);//3. 异步操作后执行UI更新
					}
				});

			}
		});

	}

	@Override
	public void publishProgress(final Progress... values) {
		HANDLER.post(new Runnable() {

			@Override
			public void run() {
				onProgressUpdate(values);
			}
		});
	}

	@Override
	public void onPreExecute() {
	}

	@Override
	public void onProgressUpdate(Progress... values) {
	}

	@Override
	public abstract Result doInBackground(Params... params);

	@Override
	public abstract void onPostExecute(Result result);

}
