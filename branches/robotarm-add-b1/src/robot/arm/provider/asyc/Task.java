package robot.arm.provider.asyc;
/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 */
public abstract class Task {
	// 发送请求
	public abstract void doCall();

	// 处理请求结果
	public abstract void doResult();

	// 执行任务
	public abstract void execute();

}
