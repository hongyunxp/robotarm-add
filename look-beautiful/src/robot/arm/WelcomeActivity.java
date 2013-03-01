package robot.arm;

import robot.arm.common.Util;
import robot.arm.utils.NetType;
import robot.arm.utils.NetUtils;
import robot.arm.utils.ViewUtils;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;
import cn.waps.AppConnect;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends Activity {
	private static final int DURATION = 5000;

	private TextView textView;
	private NetType net;
	private AnimationDrawable anim; //加载动画
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);

		init();
	}

	private void init() {
		//广告
		AppConnect.getInstance(this);

		//插屏广告
		//⑴	始化（预先加载）广告数据
		AppConnect.getInstance(this).initPopAd(this);
		//⑵	显示插屏广告,限制该方法3分钟内只能调用一次。
		AppConnect.getInstance(this).showPopAd(this);

		textView = (TextView) findViewById(R.id.welcome_text);

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
				//				if (net.available) {
				//					textView.setText("版本检测...");
				//					boolean needUpdate = AppUpdateProvider.getInstance().start(WelcomeActivity.this);
				//
				//					if (needUpdate)
				//						return;// 需要更新时不继续执行
				//				}

				// 登陆
				NetType netType = NetUtils.checkNet();
				if (netType != NetType.TYPE_NONE) {
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
				textView.setText(net.getDesc());

				// 不是WIFI提示
				if (net != NetType.TYPE_WIFI)
					ViewUtils.showDialog(WelcomeActivity.this, "温馨提示", getString(R.string.common_logo_alert), null);

			}
		});

		return animation;
	}

}
