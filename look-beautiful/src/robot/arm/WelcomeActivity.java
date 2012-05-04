package robot.arm;

import com.waps.AppConnect;

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

		init();
	}

	private void init() {
		textView = (TextView) findViewById(R.id.welcome_text);

		//广告
		AppConnect.getInstance(this);
		
		// 定义动画
		AlphaAnimation animation = createAnimation();

		// 启动动画
		textView.startAnimation(animation);
	}

	private AlphaAnimation createAnimation() {
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(DURATION); // 动画显示时间

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation aim) {
				// 版本升级
				if (net.available) {
					textView.setText("版本检测...");
					boolean needUpdate = AppUpdateProvider.getInstance().start(WelcomeActivity.this);

					if (needUpdate)
						return;// 需要更新时不继续执行
				}

				// 登陆
				if (net.available) {
					textView.setText("初始化...");

					new Thread() {
						public void run() {
							Util.login();
						}
					}.start();
				}

				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation aim) {
			}

			@Override
			public void onAnimationStart(Animation aim) {
				// 检测网络
				net = NetUtils.checkNet();

				// 网络提示
				textView.setText(net.desc);

				// 不是WIFI提示
				if (net == NetType.GPRS_WEB || net == NetType.GPRS_WAP)
					NetUtils.dialog(WelcomeActivity.this, getString(R.string.common_logo_alert));
				
			}
		});

		return animation;
	}

}
