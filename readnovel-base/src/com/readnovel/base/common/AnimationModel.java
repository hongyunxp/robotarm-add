package com.readnovel.base.common;

import android.annotation.TargetApi;
import android.app.Activity;

/**
 * 动画包装
 */
public class AnimationModel {
	private Activity act;

	public AnimationModel(Activity act) {
		this.act = act;
	}

	/**
	 * call overridePendingTransition() on the supplied Activity.
	 * @param a 
	 * @param b
	 */

	@TargetApi(5)
	public void overridePendingTransition(int enterAnim, int exitAnim) {
		act.overridePendingTransition(enterAnim, exitAnim);
	}
}
