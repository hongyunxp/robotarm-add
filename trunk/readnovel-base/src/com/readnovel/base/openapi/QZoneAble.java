package com.readnovel.base.openapi;

import android.app.Activity;
import android.content.Intent;

import com.readnovel.base.openapi.TencentAPI;

/**
 * QQ空间互通
 * 
 * 使用时需Activity继承
 * 
 * @author li.li
 *
 * Apr 3, 2013
 */
public class QZoneAble extends Activity {
	private TencentAPI tencentAPI;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (tencentAPI != null)
			tencentAPI.onActivityResult(requestCode, resultCode, data);
	}

	public TencentAPI getTencentAPI() {
		return tencentAPI;
	}

	public void setTencentAPI(TencentAPI tencentAPI) {
		this.tencentAPI = tencentAPI;
	}

}
