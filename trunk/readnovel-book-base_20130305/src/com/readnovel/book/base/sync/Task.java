package com.readnovel.book.base.sync;

/**
 * 异步任务接口
 * 
 * 
 * 
 * @author li.li
 *
 * Sep 4, 2012
 */
public interface Task<Caller, Params, Progress, Result> {
	/**
	 * 初始化
	 */
	void onPreExecute();	
	
	/**
	 * 执行异步操作
	 * @param params
	 * @return
	 */
	Result doInBackground(Params... params);
	
	/**
	 * 执行异步操作中触发UI更新
	 * @param upParams
	 */
	void publishProgress(Progress... values);
	
	/**
	 * 异步操作中执行UI更新
	 * @param upParams
	 */
	void onProgressUpdate(Progress... values);
	
	/**
	 * 异步操作执行UI更新
	 * @param result
	 */
	void onPostExecute(Result result);
	
	/**
	 * 执行异步任务入口
	 * @param params
	 */
	void execute(Params... params);
}
