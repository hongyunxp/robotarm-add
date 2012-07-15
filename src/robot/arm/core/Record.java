package robot.arm.core;

import android.app.Activity;
import android.content.Intent;

/**
 * activity状态记录
 * 
 * @author li.li
 * 
 */
public class Record {
	private int id;
	private Intent intent;
	private boolean resumable;// 是否可恢复
	private Class<? extends Activity> actClazz;

	public Record(int id, Intent intent, boolean resumable, Class<? extends Activity> actClazz) {
		this.id = id;
		this.intent = intent;
		this.resumable = resumable;
		this.actClazz = actClazz;
	}
	
	public Record(int id, Intent intent, Class<? extends Activity> actClazz) {
		this.id = id;
		this.intent = intent;
		this.actClazz = actClazz;
	}
	
	public int getId() {
		return this.id;
	}

	public Intent getIntent() {
		return this.intent;
	}

	public boolean isResumable() {
		return resumable;
	}

	public Class<? extends Activity> getActClazz() {
		return actClazz;
	}

	@Override
	public boolean equals(Object o) {

		return getId() == ((Record) o).getId();
	}
}
