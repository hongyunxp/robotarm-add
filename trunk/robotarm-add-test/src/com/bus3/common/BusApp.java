package com.bus3.common;

import robot.arm.common.RobotArmApp;

import com.bus3.common.utils.BaseUtils;

public class BusApp extends RobotArmApp {

	@Override
	public void onCreate() {
		super.onCreate();

		BusAppContext.getInstance(this);
		
		BaseUtils.getContactSync2(this);//得到通讯录信息
	}

}
