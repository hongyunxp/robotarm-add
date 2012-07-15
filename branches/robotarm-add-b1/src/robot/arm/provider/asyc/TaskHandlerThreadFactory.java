package robot.arm.provider.asyc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 */
public class TaskHandlerThreadFactory implements ThreadFactory {
	private static List<Thread> threads = new ArrayList<Thread>();// 工作中的线程
	private Thread curThread;
	
	@Override
	public Thread newThread(Runnable r) {
		curThread = new Thread(r);
		curThread.setUncaughtExceptionHandler(new DefaultExceptionHandler());

		threads.add(curThread);

		return curThread;
	}

	public static void cancel() {//中断
		for (Thread t : threads) {
			t.interrupt();
		}
	}
}
