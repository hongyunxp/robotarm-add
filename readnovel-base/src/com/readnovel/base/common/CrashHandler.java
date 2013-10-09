package com.readnovel.base.common;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.readnovel.base.util.LogUtils;

/** 
 *  
 *  
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。  
 *                           如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框 
 *                           实现该接口并注册为程序中的默认未捕获异常处理  
 *                           这样当未捕获异常发生时，就可以做些异常处理操作 
 *                           例如：收集异常信息，发送错误报告 等。 
 *  
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告. 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/** CrashHandler实例 */
	private static final CrashHandler INSTANCE = new CrashHandler();
	/** 程序的Context对象 */
	private Context mContext;
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/** 
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器 
	 *  
	 * @param ctx 
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/** 
	 * 当UncaughtException发生时会转入该函数来处理 
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理  
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleep一会后结束程序  
			// 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序  
			try {
				Thread.sleep(3000);
				android.os.Process.killProcess(android.os.Process.myPid());
			} catch (InterruptedException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}
	}

	/** 
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑 
	 *  
	 * @param ex 
	 * @return true:如果处理了该异常信息;否则返回false 
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null)
			return true;

		// 使用Toast来显示异常信息  
		new Thread() {
			@Override
			public void run() {
				// Toast 显示需要出现在一个线程的消息队列中  
				Looper.prepare();
				Toast.makeText(mContext, "程序出错啦", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		// 发送错误报告到服务器  
		LogUtils.error(ex.getMessage(), ex);

		return true;
	}

}
