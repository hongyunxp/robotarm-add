package robot.arm;

import robot.arm.common.Util;
import robot.arm.provider.AppUpdateProvider;
import robot.arm.utils.NetType;
import robot.arm.utils.NetUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends Activity {
	private static final int DURATION = 3000;

	private TextView textView;
	private NetType net;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);

		textView = (TextView) findViewById(R.id.welcome_text);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		net = NetUtils.checkNet();
		textView.setText(net.desc);
		
		//版本升级
		if (net.available) {
			boolean hasUpdate = AppUpdateProvider.getInstance().start(this);

			if (hasUpdate)
				return;//有新版本不继续执行
		}

		//登陆
		if (net.available)
			Util.login();
		
		//网络提示
		if (net == NetType.GPRS_WEB || net == NetType.GPRS_WAP)
			NetUtils.dialog(WelcomeActivity.this, getString(R.string.common_logo_alert));

		
		//动画
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(DURATION); // 动画显示时间
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation aim) {

				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation aim) {
			}

			@Override
			public void onAnimationStart(Animation aim) {
			}
		});
		textView.startAnimation(animation);

	}
}