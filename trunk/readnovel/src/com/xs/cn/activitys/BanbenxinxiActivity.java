package com.xs.cn.activitys;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.BanbenxinxiAdapter;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.threads.BanbenxinThread;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;
import com.xs.cn.http.MyAutoUpdate;

/**
 * 版本更新页面
 */
public class BanbenxinxiActivity extends Activity implements OnClickListener {
	private Button left1;
	private TextView banbenxinxi_banben; //版本名字
	private TextView banbenxinxi_gxrq; //更新时间	
	private ListView banbenxinxilistView; //更新信息列表
	private BanbenxinThread bbxxth; //版本更新线程
	private BanbenxinxiAdapter adapter;
	private Button button_gx; //更新按钮
	private String apkurl;
	private int code; //版本code		
	private String content = ""; //弹出更新提示的时候    dialog里面显示的内容
	private boolean isforce;
	private int[] wrongversion;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				code = bbxxth.bbxx.getVersionCode();
				apkurl = bbxxth.bbxx.getAppurl();
				//write by wangwei
				isforce = bbxxth.bbxx.getIsforce();
				wrongversion = bbxxth.bbxx.getWrongversion();

				banbenxinxi_banben.setText(bbxxth.bbxx.getVersion());
				banbenxinxi_gxrq.setText(bbxxth.bbxx.getUpdatetime());

				String arr[] = bbxxth.bbxx.getFeatures();
				if (arr != null && arr.length > 0) {
					adapter = new BanbenxinxiAdapter(BanbenxinxiActivity.this, arr);
					banbenxinxilistView.setAdapter(adapter);
					//弹出更新提示的时候    dialog里面显示的内容
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] != null && !"".equals(arr[i])) {
							buffer.append(i + 1 + "、" + arr[i] + "。");
						}
					}
					content = buffer.toString();
				}
				button_gx.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						//检测更新
						MyAutoUpdate update = new MyAutoUpdate(BanbenxinxiActivity.this, code, apkurl, content, isforce, wrongversion);
						update.check();
					}
				});
				break;
			case 2:

				Toast.makeText(BanbenxinxiActivity.this, BanbenxinxiActivity.this.getString(R.string.network_err), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.banbenxinxi);
		CloseActivity.add(this);

		setTopBar();
		banbenxinxi_banben = (TextView) findViewById(R.id.banbenxinxi_banben);
		banbenxinxi_gxrq = (TextView) findViewById(R.id.banbenxinxi_gxrq);
		TextView banbenxinxi_gxrq = (TextView) findViewById(R.id.bbt);
		//获取当前APK的版本名
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			banbenxinxi_gxrq.setText(info.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		banbenxinxilistView = (ListView) findViewById(R.id.banbenxinxilistView);
		button_gx = (Button) findViewById(R.id.button_gx);

		//启动获取版本信息 线程
		bbxxth = new BanbenxinThread(BanbenxinxiActivity.this, handler);
		bbxxth.start();
	}

	private void setTopBar() {
		left1 = (Button) findViewById(R.id.title_btn_left2);
		left1.setText("返回");
		left1.setGravity(Gravity.CENTER);
		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("设置");
		left1.setVisibility(View.VISIBLE);
		left1.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == left1.getId()) {
			finish();
		}
	}

	protected void onResume() {
		super.onResume();
		CloseActivity.curActivity = this;
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}
