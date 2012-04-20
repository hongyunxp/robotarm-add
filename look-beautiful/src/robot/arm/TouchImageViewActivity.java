/**
 * 
 */
package robot.arm;

import robot.arm.provider.view.touch.TouchImageView;
import robot.arm.utils.LoadImageUtils;
import android.app.Activity;
import android.os.Bundle;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class TouchImageViewActivity extends Activity {
	private TouchImageView touchImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touch_image_content);

		touchImageView = (TouchImageView) findViewById(R.id.touch_image_view);
		Bundle bundle = getIntent().getExtras();    
		String imageUrl = bundle.getString(getString(R.string.detailUrl));//读出数据  
		LoadImageUtils.loadImageSync(this, imageUrl, touchImageView);
		touchImageView.reset();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// ((TabInvHandler) getParent()).titleVisible(false);
		// ((TabInvHandler) getParent()).tabVisible(false);
		// ((TabInvHandler) getParent()).needCloseSoftInput(true);
	}
}
