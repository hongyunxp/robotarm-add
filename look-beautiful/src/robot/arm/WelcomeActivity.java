package robot.arm;

import robot.arm.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 定义splash 动画
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(3000); // 动画显示时间
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

		findViewById(R.id.welcome).startAnimation(animation);
	}
}
