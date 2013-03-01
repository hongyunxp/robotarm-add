package com.bus3.test.request;

import java.text.DecimalFormat;

import org.apache.http.client.methods.HttpGet;

import robot.arm.provider.asyc.EasyTask;

import com.bus3.test.MoreSecondActivity;

public class MoreSecondRequest extends EasyTask<MoreSecondActivity, Void, Void, Void> {
	public MoreSecondRequest(MoreSecondActivity caller) {
		super(caller);
	}

	private static final int STEP = 1000;
	private static DecimalFormat df = new DecimalFormat("00.00%");
	public static HttpGet get;

	@Override
	public Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPostExecute(Void result) {
		// TODO Auto-generated method stub

	}

}
