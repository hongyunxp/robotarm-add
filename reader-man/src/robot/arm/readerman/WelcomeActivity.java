package robot.arm.readerman;

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
	private static final int DURATION = 5000;

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

			}
		});

		return animation;
	}

}
