package robot.arm;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_content);
	}

	@Override
	protected void onResume() {
		super.onResume();
		((TabInvHandler) getParent()).setTitle(R.layout.text_title);
	}
}
