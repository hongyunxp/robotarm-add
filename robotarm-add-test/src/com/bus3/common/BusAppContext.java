package com.bus3.common;

import android.app.Application;

public class BusAppContext {
	private static volatile BusAppContext bac;
	private Application app;

	private BusAppContext(Application app) {
		this.app = app;
	}

	public Application getApp() {
		return app;
	}

	public static BusAppContext getInstance() {
		return bac;
	}

	public static BusAppContext getInstance(Application app) {
		
		if (bac == null) {
			synchronized (BusAppContext.class) {
				if (bac == null) {
					bac = new BusAppContext(app);
				}
			}
		}

		return bac;
	}
}
