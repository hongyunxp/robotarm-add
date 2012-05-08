package robot.arm.provider.asyc;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 * @param <T>
 * 
 * 异步任务
 */
public abstract class AsycTask<T extends Activity> extends Task {
	private static final String TAG = AsycTask.class.getName();

	// 线程池
	private static final int N = 5;// 5个工人
	protected static final ExecutorService worker = Executors.newFixedThreadPool(N, new TaskHandlerThreadFactory());// 线程池
	// private static List<Future<?>> futureList = new ArrayList<Future<?>>();// 工作中的线程
	protected T act;// 当前的活动

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			doResult();// step 2
		}
	};

	public AsycTask(final T activity) {
		this.act = activity;
	}

	/**
	 * 执行的入口
	 */
	@Override
	public void execute() {

		/**
		 * 子线程执行
		 */
		worker.execute(new Runnable() {

			@Override
			public void run() {
				doCall();// setp 1

				handler.sendMessage(new Message());
			}
		});

	}

	/**
	 * 发送请求
	 */
	@Override
	public abstract void doCall();

	// 处理请求结果
	@Override
	public abstract void doResult();

	/**
	 * ************************************************************
	 * 
	 * 以下为静态方法
	 * 
	 * ************************************************************
	 */

	public static void call(Activity act, Class<? extends Task> taskClazz) {
		try {
			newTask(act, taskClazz).execute();

		} catch (Throwable e) {// 主线程的异常
			Log.e(TAG, e.getMessage(), e);
		}
	}

	/**
	 * 得到新的Task
	 * 
	 * @param act
	 *            使用当前Task的Activity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Task newTask(Activity act, Class<? extends Task> tClazz) throws Throwable {
		Class<Task> taskClass = (Class<Task>) Class.forName(tClazz.getName());
		/**
		 * 支持一个Task被多个Activity使用<br/>
		 * 不建议一个Task被多个Activity使用<br/>
		 * 所以Class[] params = {act.getClass()}方式不够准确，会出异常<br/>
		 * 当被多个Activity使用时，Task定义时指定其父类，BaseActivity
		 */
		Class<?>[] params = {getTaskActClazz(tClazz)};
		Constructor<Task> constructor = taskClass.getConstructor(params);

		return constructor.newInstance(act);

	}

	/**
	 * 得到具体Task所属的具体Activity子类
	 * 
	 * @param clazz
	 *            当前执行的Task
	 * @return
	 */
	private static Class<?> getTaskActClazz(Class<? extends Task> clazz) throws Throwable {
		Type genType = clazz.getGenericSuperclass();
		Type[] ps = ((ParameterizedType) genType).getActualTypeArguments();

		return (Class<?>) ps[0];
	}

	// 结束线程池中的线程执行（中断）
	public static void cancel() {
		// 只发中断信号
		TaskHandlerThreadFactory.cancel();
	}

	// 销毁线程池
	public static void destory() {
		if (worker != null && worker instanceof ExecutorService) {
			ExecutorService es = ExecutorService.class.cast(worker);
			if (!es.isShutdown()) {
				es.shutdownNow();
			}
		}
	}
}
