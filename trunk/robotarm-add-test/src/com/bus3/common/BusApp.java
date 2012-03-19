package com.bus3.common;

import robot.arm.common.RobotArmApp;

public class BusApp extends RobotArmApp {

	@Override
	public void onCreate() {
		super.onCreate();

		BusAppContext.getInstance(this);

	}

}
